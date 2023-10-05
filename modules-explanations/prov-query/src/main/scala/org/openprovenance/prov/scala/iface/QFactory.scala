package org.openprovenance.prov.scala.iface
import org.openprovenance.prov.scala.immutable._
import org.openprovenance.prov.scala.interop.FileInput
import org.openprovenance.prov.scala.nf.CommandLine.parseDocument
import org.openprovenance.prov.scala.nlgspec_transformer.{Environment, SpecLoader}
import org.openprovenance.prov.scala.query.QueryInterpreter.RField
import org.openprovenance.prov.scala.query.{Processor, QConfig, QuerySetup, Record, StatementAccessor, StatementIndexer}
import org.openprovenance.prov.scala.utilities.OrType.or
import org.openprovenance.prov.scala.utilities.{WasDerivedFromPlus, WasDerivedFromStar}

import java.io.File
import scala.collection.mutable

class QFactory {
  def makeQueryEnfine(config: QConfig): QueryEngine[Statement, RField] =
    new QueryEngine[Statement, RField] {
      override def processQuery(theQuery: String, doc: Document, environment: Environment, statementAccessorForDocument: Option[String] => StatementAccessor[Statement]): QueryResult[RField] = {
        val queryProcessor = new Processor(statementAccessorForDocument, environment)
        val records: mutable.Set[Record] = queryProcessor.newRecords()
        queryProcessor.evalAccumulate(theQuery, records)

        val result1: Set[RField] = queryProcessor.toFields(records)
        println("found " + records.size + " records (and " + result1.size + " fields)")

        def convert[T <% Statement or Seq[Statement] or Seq[TypedValue]](t: T): Seq[Statement] = {
          t.a match {
            case None => t.b match {
              case None => throw new UnsupportedOperationException
              case Some(s: Seq[TypedValue]) => Seq()
            }
            case Some(t1) => t1.a match {
              case None => t1.b.get
              case Some(s) => Seq(s)
            }
          }
        }


        val statements = result1.flatMap(f => convert[RField](f))
        val doc2 = new Document(statements, doc.namespace)


        val result: Seq[Map[String, RField]] = records.toSeq.map(queryProcessor.toMap2)
        new QueryResult[RField] {
          override def getRecords: Seq[Map[String, RField]] = result

          override def getDocument: Document = doc2
        }
      }

      override  def processQuery(queryContents: String, doc: Document): QueryResult[RField] = {
        import scala.jdk.CollectionConverters._
        val context: Map[String, String] = doc.namespace.getPrefixes.asScala.toMap
        val environment = Environment(context, null, null, new Array[String](0), List())
        processQuery(queryContents, doc, environment)
      }


      override  def processQuery(queryContents: String, doc: Document, environment: Environment): QueryResult[RField] = {
        val accessor: StatementAccessor[Statement] = makeStatementAccessor(doc.statements().toSeq)
        val accessors: Map[String, StatementAccessor[Statement]] = accessors_seq_statements.map { case (s, ss) => (s, makeStatementAccessor(ss)) } //alternate_files.map{case (s,f) => (s, rf.makeStatementAccessor(parseDocument(new FileInput(new File(f))).statements().toSeq))}

        val statementAccessorForDocument: Option[String] => StatementAccessor[Statement] = {
          case None => accessor
          case Some(s) => accessors(s)
        }


        val result: QueryResult[RField] = processQuery(queryContents, doc, environment, statementAccessorForDocument)
        result
      }

      def makeStatementAccessor(statements: Seq[Statement]): StatementAccessor[Statement] = {

        val idx: Map[Kind.Value, List[Statement]] = StatementIndexer.splitByStatementType(statements)
        val (allDerivationsPlus, allDerivationsStar): (List[WasDerivedFromPlus], List[WasDerivedFromStar]) = StatementIndexer.computeDerivationClosure(idx)

        new StatementAccessor[Statement] {
          override def findStatement(type_string: String): List[Statement] = {
            val kind = QuerySetup.nameMapper(type_string)
            if (kind == Kind.winfl) {
              type_string match {
                case "provext:WasDerivedFromPlus" => allDerivationsPlus
                case "provext:WasDerivedFromStar" => allDerivationsStar
              }
            } else {
              idx(kind)
            }
          }
        }
      }


      def getAccessors(infiles: String): Map[String, Seq[Statement]] = {
        val alternate_files: Map[String, String] = if (infiles != null) SpecLoader.mapper.readValue(infiles, classOf[Map[String, String]]) else Map[String, String]()
        val accessors: Map[String, Seq[Statement]] = alternate_files.map { case (s, f) => (s, parseDocument(FileInput(new File(f))).statements().toSeq) }
        accessors

      }

      val accessors_seq_statements: Map[String,Seq[Statement]] = getAccessors(config.infiles)

    }
}
