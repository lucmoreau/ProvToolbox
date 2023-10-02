/**
A SQL Query Compiler
====================

Commercial and open source database systems consist of millions of lines of highly
optimized C code. Yet, their performance on individual queries falls 10x or 100x
short of what a hand-written, specialized, implementation of the same query can
achieve.

In this tutorial, we will build a small SQL processing engine that consists of
just about 500 lines of high-level Scala code. Whereas other systems interpret query
plans, operator by operator, we will use LMS to generate and compile low-level C
code for entire queries.

We keep the query functionality intentionally simple. A more complete engine
that handles the full TPCH benchmark suite and consists of about 3000 lines of
code has been developed in the [LegoBase](http://data.epfl.ch/legobase) project, which recently received
a best paper award at VLDB'14.

See also:

- Building Efficient Query Engines in a High-Level Language ([PDF](http://infoscience.epfl.ch/record/198693/files/801-klonatos.pdf))
  Yannis Klonatos, Christoph Koch, Tiark Rompf, Hassan Chafi. VLDB'14
- Functional pearl: A SQL to C Compiler in 500 Lines of Code ([PDF](https://www.cs.purdue.edu/homes/rompf/papers/rompf-icfp15.pdf))
  Tiark Rompf, Nada Amin. ICFP'15

Outline:
<div id="tableofcontents"></div>


Setting the Stage
-----------------

Let us run a few quick benchmarks to get an idea of the relative performance of different
data processing systems. We take a data sample from [the Google Books NGram Viewer](https://books.google.com/ngrams) project.
The 2GB file that contains statistics for words starting with the letter 'A' is a good candidate to run
some simple queries. We might be interested in all occurrences of the word 'Auswanderung':

    select * from 1gram_a where n_gram = 'Auswanderung'

Here are some timings:

- Loading the CSV file into a MySQL database takes > 5 minutes, running the query about 50 seconds.

- PostgreSQL takes 3 minutes to load, the first query run takes 46 seconds, but subsequent runs get faster over time (down to 7 seconds).

- An [AWK script](https://github.com/scala-lms/tutorials/blob/master/src/out/query_t1gram2.hand.awk) that processes the CSV file directly takes 45 seconds.

- A [query interpreter](https://github.com/scala-lms/tutorials/blob/master/src/test/scala/lms/tutorial/query_unstaged.scala) written in Scala takes 39 sec.

- A hand-written specialized [Scala program](https://github.com/scala-lms/tutorials/blob/master/src/out/query_t1gram2.hand0.scala/) takes 13 sec.

- A similar hand-written [C program](https://github.com/scala-lms/tutorials/blob/master/src/out/query_t1gram2.hand.c/) performs marginally faster,
  but with [more optimizations](https://github.com/scala-lms/tutorials/blob/master/src/out/query_t1gram2.hand2.c/) we can get as good as 3.2 seconds.

The query processor we will develop in this tutorial matches the performance of the handwritten Scala and C queries (13s and 3s, respectively).

More details on running the benchmarks are available [here](https://github.com/scala-lms/tutorials/blob/master/src/data/README.md). We now turn to our actual implementation.

*/

package org.openprovenance.prov.scala.query

import org.openprovenance.prov.scala.query.QueryAST.{Schema, Table, toSchema}
import org.openprovenance.prov.scala.utilities.Utils


object QueryAST {
  type Table
  type Schema = Vector[String]
  def toSchema(schema: String*): Schema = schema.toVector

}


// relational algebra ops
sealed abstract class Operator

case class Scan(statementType: String, schema: Schema, document: Option[String]) extends Operator

case class Print(parent: Operator) extends Operator

case class Project(outSchema: Schema, inSchema: Schema, parent: Operator) extends Operator

case class Filter(pred: Predicate, parent: Operator) extends Operator

case class Join(parent1: Operator, field1: String, property1: String, parent2: Operator, field2: String, property2: String) extends Operator

case class Group(keys: Schema, agg: Schema, parent: Operator, kind: String, ref: Option[Ref]) extends Operator

case class HashJoin(parent1: Operator, parent2: Operator) extends Operator

case class LeftJoin(parent1: Operator, field1: String, property1: String, parent2: Operator, field2: String, property2: String) extends Operator
// case class Order(field: Ref, parent: Operator, kind: String) extends Operator


// filter predicates
sealed abstract class Predicate

case class Eq(pred: String, a: Ref, b: Ref) extends Predicate

case class EqL(pred: String, a: Ref, b: String) extends Predicate

case class OrPred(pred1: Predicate, pred2: Predicate) extends Predicate

sealed abstract class Ref

case class Field(name: String, field: String) extends Ref

case class Property(name: String, property: String) extends Ref

case class Value(x: Object) extends Ref


/**
  * Relational Algebra AST
*----------------------
 **
  * The core of any query processing engine is an AST representation of
*relational algebra operators.
*/
trait QueryAST {
  // some smart constructors
  //def Scan(tableName: String): Scan = Scan(tableName, None, None)
  //def Scan(tableName: String, schema: Option[Schema], delim: Option[Char]): Scan
}


/**
SQL Parser
----------

We add a parser that takes a SQL(-like) string and converts it to tree of operators.
*/

trait SQLParser extends QueryAST {

  def parseSql(input: String): Operator = Grammar.parseAll(input)

  object Grammar extends scala.util.parsing.combinator.JavaTokenParsers with scala.util.parsing.combinator.PackratParsers {

    def stm: Parser[Operator] =
      selectClause ~ fromClause ~ whereClause ~ groupClause ^^ {
        case p ~ s ~ f ~ g => g(p(f(s))) }

    def selectClause: Parser[Operator=>Operator] =
      "select" ~> ("*" ^^ { _ => (op:Operator) => op } | fieldList ^^ {
        case (fs,fs1) => Project(fs,fs1,_:Operator) })

    def fromClause: Parser[Operator] =
      "from" ~> joinClause

    def whereClause: Parser[Operator=>Operator] =
      opt("where" ~> repsep(predicate,"and") ^^ { pp => (operator: Operator) => pp.foldLeft(operator)((a,b)=>Filter(b, a)) }) ^^ { _.getOrElse(op => op)}

    def groupClause: Parser[Operator=>Operator] =
      opt("group" ~> "by" ~> fieldIdList ~ ("sum" ~> fieldIdList) ^^ {
        case p1 ~ p2 => Group(p1,p2, _:Operator, "", None) }) ^^ { _.getOrElse(op => op)}

    def joinClause: Parser[Operator] =
      ("DUMMY" ~> tableClause ~ "from" ~ repsep(tableJoinClause,"from") ^^ { case tableOp~_~(ll:List[Operator => Operator] )  => ll.foldLeft(tableOp)((a:Operator,b) => b(a)) }) |
      (repsep(tableClause, "join") ^^ { _.reduceLeft((a,b) => HashJoin(a,b)) })

    def tableJoinClause: Parser[Operator => Operator] =
      tableClause ~ "join" ~ joinClause2 ^^ {
        case table~_~pair => (op:Operator) => Join(op,pair._1.name,pair._1.field,table,  pair._2.name,pair._2.field)}

    def tableClause: Parser[Operator] =
      fieldIdent ~ "a" ~ qualifiedName   ^^ {
        case f~a~q  => Scan(q, toSchema(f), None):Operator } |
        ("(" ~> stm <~ ")")

    def fieldIdent: Parser[String] = """[\w\#]+""".r
    def tableIdent: Parser[String] = """[\w_\-/\.]+""".r | "?"
    def qualifiedName: Parser[String] = """[\w_\-/\.]+:[\w_\-/\.]+""".r | "?"

    def fieldList:  Parser[(Schema,Schema)] =
      repsep(fieldIdent ~ opt("as" ~> fieldIdent), ",") ^^ { fs2s =>
        val (fs,fs1) = fs2s.map { case a~b => (b.getOrElse(a),a) }.unzip
        (toSchema(fs:_*),toSchema(fs1:_*)) }
    def fieldIdList:  Parser[Schema] =
      repsep(fieldIdent,",") ^^ (fs => toSchema(fs:_*))

    def predicate: Parser[Predicate] =
      ref ~ ">=" ~ ref ^^ { case a ~ _ ~ b => Eq("includesQualifiedName",a,b) } |
        (ref ~ "exists" ^^ { case a ~ _ => Eq("exists",a, null) })

    def joinClause2: Parser[(Field,Field)] =
      ref ~ "=" ~ ref ^^ { case a ~ _ ~ b => (a.asInstanceOf[Field],b.asInstanceOf[Field]) }

    def ref: Parser[Ref] =
      fieldIdent ~ "[" ~ qualifiedName ~ "]" ^^ { case f~_~q~_ =>  Property(f,q) }  |
      fieldIdent ~ "." ~ fieldIdent  ^^ { case f~_~q=>  Field(f,q) }  |
      """'[^']*'""".r ^^ (s => Value(s.drop(1).dropRight(1))) /* |
      """[0-9]+""".r ^^ (s => Value(s.toInt))*/

    def parseAll(input: String): Operator = parseAll(stm,input) match {
      case Success(res,_)  => res
      case res => throw new Exception(res.toString)
    }
  }
}



/**
Iterative Development of a Query Processor
------------------------------------------

We develop our SQL engine in multiple steps. Each steps leads to a
working processor, and each successive step either adds a feature or
optimization.

### Step 1: A (Plain) Query Interpreter

We start with a plain query processor: an interpreter.

- [QueryInterpreter.scala](query_unstaged.html)


### Step 2: A Staged Query Interpreter (= Compiler)

Staging our query interpreter yields a query compiler.
In the first iteration we generate Scala code but we disregard
operators that require internal data structures:

- [query_staged0](query_staged0.html)


### Step 3: Specializing Data Structures

The next iteration adds optimized data structure implementations
that follow a column-store layout. This includes specialized hash
tables for groupBy and join operators:

- [query_staged](query_staged.html)


### Step 4: Switching to C and Optimizing IO

For additional low-level optimizations we switch to generating C
code:

- [query_optc](query_optc.html)

On the C level, we optimize the IO layer by mapping files directly
into memory and we further specialize internal data structures
to minimize data conversions and to enable representing string objects
directly as pointers into the memory-mapped input file.


Plumbing
--------

To actually run queries and test the different implementations side
by side, a little bit of plumbing is necessary. We define a common
interface for all query processors (plain or staged, Scala or C).

*/
trait QueryProcessor extends QueryAST {
  def version: String
  val defaultFieldDelimiter = ','

  def filePath(table: String): String = table

  def execQuery(q: Operator): Unit
}


