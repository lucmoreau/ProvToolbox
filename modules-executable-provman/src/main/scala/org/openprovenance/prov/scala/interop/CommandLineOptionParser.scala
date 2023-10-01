package org.openprovenance.prov.scala.interop
import org.openprovenance.prov.scala.immutable.Format
import org.openprovenance.prov.scala.summary.PrettyMethod
import scopt.OptionParser
import java.io.File





class CommandLineOptionParser extends Constants {
  implicit val outputRead: scopt.Read[Output] =
    scopt.Read.reads { x => if (x == "-") StandardOutput() else {
      FileOutput(new File(x))
    }
    }

  implicit val inputRead: scopt.Read[Input] =
    scopt.Read.reads { x => if (x == "-") StandardInput() else {
      FileInput(new File(x))
    }
    }

  implicit val formatRead: scopt.Read[Format.Format] =
    scopt.Read.reads { x => Format.withName(x.toLowerCase()) }

  val parser: OptionParser[Config]= new scopt.OptionParser[Config]("scopt") {
    head("provanalytics", "0.1")

    opt[Seq[Output]]('O', "outfiles")
      .valueName ("<file1>,<file2>,...")
      .action { (x, c) => c.copy(outfiles = x) }
      .text ("files as output")
    opt[Output]('o', "outfile")
      .valueName ("<file>")
      .action { (x, c) => c.copy(outfiles = Seq(x)) }
      .text ("file as output")
    opt[Seq[Format.Format]]('F', "outformats")
      .valueName ("<fmt1>,<fmt2>,...")
      .action { (x, c) => c.copy(outformats = x) }
      .text ("formats for outputs")
    opt[Format.Format]('f', "outformat")
      .valueName ("<fmt1>,<fmt2>,...")
      .action { (x, c) => c.copy(outformats = Seq(x)) }
      .text ("format for output")
    opt[Input]('i', "infile")
      .required()
      .valueName("<file>")
      .action { (x, c) => c.copy(infile = x) }
      .text("use given file as input")
    opt[Format.Format]("inputFormat")
      .abbr ("if")
      .valueName ("format")
      .action { (x, c) => c.copy(defaultFormat = x) }
      .text ("use format for input")
    opt[Format.Format]("defaultFormat")
      .abbr ("df")
      .valueName ("format")
      .action { (x, c) => c.copy(defaultFormat = x) }
      .text ("use format as default for output(s)")
    opt[Unit]("debug")
      .hidden()
      .action { (_, c) => c.copy(debug = true) }
      .text("this option is hidden in the usage text")


    note("\n")

    help("help")
      .text ("prints this usage text")

    note("\n")

    cmd("config")
      .action { (_, c) => c.copy(command = "config") }
      .text("print configuration")
      .children()

    checkConfig { c => if ((c.command != "config") && (c.command != "kernelize") && (c.command != "blockly") && ((c.outfiles == null) || c.outfiles.isEmpty)) failure("No output") else success }

    note("\n")

    cmd(NORMALIZE)
      .action { (_, c) => c.copy(command = "normalize") }
      .text("normalizes PROV representations")
      .children(
        opt[Boolean]('p', "pretty")
          .action { (x, c) => c.copy(pretty = x) }
          .text("Pretty print output where possible   (NOT USED)")
      )

    note("\n")

    cmd("sign")
      .action { (_, c) => c.copy(command = "sign") }
      .text("produces a signed normalized PROV representation")
      .children(
        opt[String]("id")

          .valueName ("<identifier>")

          .action { (x, c) => c.copy(id = x) }

          .text ("id used to identify element to sign"),
        opt[String]('s', "store")
          .required()
          .valueName("<file>")
          .action { (x, c) => c.copy(store = x) }
          .text("keystore location"),
        opt[String]('p', "storepass")
          .required()
          .valueName("<password>")
          .action { (x, c) => c.copy(storepass = x) }
          .text("keystore password"),
        opt[String]('k', "key")
          .required()
          .valueName("<key>")
          .action { (x, c) => c.copy(key = x) }
          .text("key"),
        opt[String]("keypass")
          .required()
          .valueName("<password>")
          .action { (x, c) => c.copy(keypass = x) }
          .text("key password")
      )


    cmd("signature")
      .action { (_, c) => c.copy(command = "signature") }
      .text ("get the signature")
      .children(
        opt[String]("id")
          .valueName ("<identifier>")
          .action { (x, c) => c.copy(id = x) }
          .text ("id used to identify element to sign"),
        opt[String]('s', "store")
          .required()
          .valueName("<file>")
          .action { (x, c) => c.copy(store = x) }
          .text("keystore location"),
        opt[String]('p', "storepass")
          .required()
          .valueName("<password>")
          .action { (x, c) => c.copy(storepass = x) }
          .text("keystore password"),
        opt[String]('k', "key")
          .required()
          .valueName("<key>")
          .action { (x, c) => c.copy(key = x) }
          .text("key"),
        opt[String]("keypass")
          .required()
          .valueName("<password>")
          .action { (x, c) => c.copy(keypass = x) }
          .text("key password")
      )


    note("\n")

    /*
  cmd("normalize.old")
.action { (_, c) =>  c.copy(command = "normalize.old") }
.text("normalizes PROV representations")
.children (

  ) */

    note("\n")

    cmd("blockly")
      .action { (_, c) => c.copy(command = "blockly") }
      .text ("convert an explanation plan from json to blockly format")
      .children(
        opt[Output]('b', "blockly")
          .action { (x, c) => c.copy(blockly = x) }
          .text ("blockly file"),
        opt[Input]('w', "withfile")
          .required()
          .valueName("<file>")
          .action { (x, c) => c.copy(withfile = x) }
          .text("input file"),
        opt[String]('x', "xplan")
          .action { (x, c) => c.copy(xplan = x) }
          .text ("xplanation plan name to extract from library")

      )


    note("\n")

    cmd("narrate")
      .action { (_, c) => c.copy(command = "narrate") }
      .text ("tell a narrative out of a PROV document")
      .children(
        opt[Output]('t', "text")
          .action { (x, c) => c.copy(text = x) }
          .text ("narrative file"),
        opt[Output]('s', "simplenlg")
          .action { (x, c) => c.copy(snlg = x) }
          .text ("simplenlg file"),
        opt[Output]('m', "matrix")
          .action { (x, c) => c.copy(matrix = x) }
          .text ("matrix file"),
        opt[Unit]('l', "linear")
          .action { (_, c) => c.copy(linear = true) }
          .text ("linear story"),
        opt[Unit]('h', "hierarchical")
          .action { (_, c) => c.copy(hierarchical = true) }
          .text ("hierarchical story"),
        opt[String]('p', "profile")
          .action { (x, c) => c.copy(profile = x) }
          .text ("profile for surface generation"),
        opt[File]('d', "description")
          .action { (x, c) => c.copy(description = x) }
          .text ("activity description (json file)"),
        opt[Seq[String]]('l', "language")
          .action { (x, c) => c.copy(language = x) }
          .text ("language files"),
        opt[Seq[String]]('T', "templates")
          .action { (x, c) => c.copy(selected_templates = x) }
          .text ("selected templates for narrative (all if not used)")

      )

    note("\n")

    cmd("explain")
      .action { (_, c) => c.copy(command = "explain") }
      .text ("construct explanations from PROV document")
      .children(
        opt[Output]('t', "text")
          .action { (x, c) => c.copy(text = x) }
          .text ("explanation file"),
        opt[Output]('s', "simplenlg")
          .action { (x, c) => c.copy(snlg = x) }
          .text ("simplenlg file"),
        opt[String]('p', "profile")
          .action { (x, c) => c.copy(profile = x) }
          .text ("profile for surface generation"),
        opt[Int]('X', "foption")
          .action { (x, c) => c.copy(format_option = x) }
          .text ("xplain format option"),

        opt[File]('d', "description")
          .action { (x, c) => c.copy(description = x) }
          .text ("activity description (json file)"),
        opt[Seq[String]]('l', "language")
          .action { (x, c) => c.copy(language = x) }
          .text ("language files"),
        opt[Seq[String]]('T', "templates")
          .action { (x, c) => c.copy(selected_templates = x) }
          .text ("selected templates for explanation "),
        opt[String]('B', "batch-templates")
          .action { (x, c) => c.copy(batch_templates = Some(x)) }
          .text ("batch templates for explanations "),
        opt[String]("infiles")
          .action { (x, c) => c.copy(infiles = x) }
          .text ("alternate named input files in json"),

        checkConfig { c => if (c.batch_templates.isDefined && c.selected_templates.nonEmpty) failure("from cannot jave both -T and -B defined") else success }

      )

    note("\n")
    cmd("compare")
      .action { (_, c) => c.copy(command = "compare") }
      .text ("Compare PROV files")
      .children(
        opt[Input]('w', "withfile")
          .required()
          .valueName("<file>")
          .action { (x, c) => c.copy(withfile = x) }
          .text("use given file as other input"),
        opt[Int]('n', "nf")
          .action { (x, c) => c.copy(nf = x) }
          .text ("normal form level")

      )

    note("\n")

    cmd("summary")
      .action { (_, c) => c.copy(command = "summary") }
      .text ("summarises PROV representations")
      .children(
        opt[Int]('f', "from")
          .action { (x, c) => c.copy(from = x) }
          .text ("minimum level of k for APT(k)"),
        opt[Int]('t', "to")
          .action { (x, c) => c.copy(to = x) }
          .text ("maximum level of k for APT(k)"),
        opt[File]('0', "level0")
          .action { (x, c) => c.copy(level0 = x) }
          .text ("level0 map file (in json format)"),
        opt[File]('T', "types")
          .action { (x, c) => c.copy(types = x) }
          .text ("summary types in json format"),
        opt[File]('F', "features")
          .action { (x, c) => c.copy(features = x) }
          .text ("provenance type features in json format"),
        opt[Boolean]("triangle")
          .action { (x, c) => c.copy(triangle = x) }
          .text ("algorithm is triange aware"),
        opt[String]('P', "prettyMethod")
          .action { (x, c) => c.copy(prettyMethod = PrettyMethod.withName(x)) }
          .text ("summary pretty method (Name|Type)"),
        opt[String]('b', "baseUri")
          .action { (x, c) => c.copy(nsBase = x) }
          .text ("namespace URI for summary"),

        opt[Seq[String]]('M', "maximal")
          .valueName ("<type1>,<type2>,...")
          .action { (x, c) => c.copy(filter = x) }
          .text ("types for maximal  propagation"),
        opt[Int]('L', "max-level")
          .action { (x, c) => c.copy(maxlevel = x) }
          .text ("maximum level of k for maximal propagation"),
        opt[Unit]('n', "no-primitives")
          .action { (_, c) => c.copy(primitivep = false) }
          .text ("no primitive types"),

        opt[Boolean]('A', "aggregate")
          .action { (x, c) => c.copy(aggregatep = x) }
          .text ("Aggregate types 0 to N"),
        opt[Boolean]("aggregate0")
          .action { (x, c) => c.copy(aggregate0p = x) }
          .text ("Aggregate types 0 and N, after all propagations are complete. To be effective, it needs aggregate flag to be set to false"),
        opt[Boolean]("always-with0")
          .action { (x, c) => c.copy(always_with_type_0 = x) }
          .text ("Aggregate types 0 and N, after each propagation is complete. To be effective, it needs aggregate flag to be set to false"),


        opt[File]('Q', "summary:queries")
          .action { (x, c) => c.copy(summary_queries = x) }
          .text ("generate summary queries json format"),

        opt[Boolean]("prov-constraints-inference")
          .action { (x, c) => c.copy(prov_constraints_inference = x) }
          .text ("enables prov-constraints inference (by default: true)"),
        opt[Boolean]("withLevel0Description")
          .action { (x, c) => c.copy(withLevel0Description = x) }
          .text ("introduce sum:level0 attribute for level0 primitive types (by default: false)"),

        checkConfig { c => if (c.from > c.to) failure("from cannot be > then to") else success })


    cmd("kernelize")
      .action { (_, c) => c.copy(command = "kernelize") }
      .text ("create k PROV representations")
      .children(
        opt[Int]('f', "from")
          .action { (x, c) => c.copy(from = x) }
          .text ("minimum level of k for APT(k)"),
        opt[Int]('t', "to")
          .action { (x, c) => c.copy(to = x) }
          .text ("maximum level of k for APT(k)"),
        opt[File]('0', "level0")
          .action { (x, c) => c.copy(level0 = x) }
          .text ("level0 map file (in json format)"),
        opt[File]('T', "types")
          .action { (x, c) => c.copy(types = x) }
          .text ("summary types in json format"),
        opt[File]('F', "features")
          .action { (x, c) => c.copy(features = x) }
          .text ("provenance type features in json format"),
        opt[Boolean]("triangle")
          .action { (x, c) => c.copy(triangle = x) }
          .text ("algorithm is triange aware"),

        opt[Seq[String]]('M', "maximal")
          .valueName ("<type1>,<type2>,...")
          .action { (x, c) => c.copy(filter = x) }
          .text ("types for maximal  propagation"),
        opt[Int]('L', "max-level")
          .action { (x, c) => c.copy(maxlevel = x) }
          .text ("maximum level of k for maximal propagation"),
        opt[Unit]('n', "no-primitives")
          .action { (_, c) => c.copy(primitivep = false) }
          .text ("no primitive types"),

        opt[Boolean]('A', "aggregate")
          .action { (x, c) => c.copy(aggregatep = x) }
          .text ("Aggregate types 0 to N"),
        opt[Boolean]("aggregate0")
          .action { (x, c) => c.copy(aggregate0p = x) }
          .text ("Aggregate types 0 and N, after all propagations are complete. To be effective, it needs aggregate flag to be set to false"),
        opt[Boolean]("always-with0")
          .action { (x, c) => c.copy(always_with_type_0 = x) }
          .text ("Aggregate types 0 and N, after each propagation is complete. To be effective, it needs aggregate flag to be set to false"),
        opt[Boolean]("prov-constraints-inference")
          .action { (x, c) => c.copy(prov_constraints_inference = x) }
          .text ("enables prov-constraints inference (by default)"),

        checkConfig { c => if (c.from > c.to) failure("from cannot be > than to") else success })

    note("\n")

    cmd("summaryDraw")
      .action { (_, c) => c.copy(command = "summaryDraw") }
      .text ("drawing PROV representations based on summaries")
      .children(
        opt[Int]('f', "from")
          .action { (x, c) => c.copy(from = x) }
          .text ("minimum level of k for APT(k)"),
        opt[Int]('t', "to")
          .action { (x, c) => c.copy(to = x) }
          .text ("maximum level of k for APT(k)"),
        opt[File]('0', "level0")
          .action { (x, c) => c.copy(level0 = x) }
          .text ("level0 map file (in json format)"),
        opt[File]('T', "types")
          .action { (x, c) => c.copy(types = x) }
          .text ("summary types in json format"),
        opt[File]('F', "features")
          .action { (x, c) => c.copy(features = x) }
          .text ("provenance type features in json format"),
        opt[String]('P', "prettyMethod")
          .action { (x, c) => c.copy(prettyMethod = PrettyMethod.withName(x)) }
          .text ("summary pretty method (Name|Type)"),
        opt[String]('b', "baseUri")
          .action { (x, c) => c.copy(nsBase = x) }
          .text ("namespace URI for summary"),

        opt[Seq[String]]('M', "maximal")
          .valueName ("<type1>,<type2>,...")
          .action { (x, c) => c.copy(filter = x) }
          .text ("types for maximal  propagation"),
        opt[Int]('L', "max-level")
          .action { (x, c) => c.copy(maxlevel = x) }
          .text ("maximum level of k for maximal propagation"),
        opt[Unit]('n', "no-primitives")
          .action { (_, c) => c.copy(primitivep = false) }
          .text ("no primitive types"),

        checkConfig { c => if (c.from > c.to) failure("from cannot be > then to") else success })

    note("\n")


    cmd("summary.compare")
      .action { (_, c) => c.copy(command = "summary.compare") }
      .text ("compare two PROV summaries")
      .children(
        /*
        opt[Input]('w', "summary")
          .required()
          .valueName("<file>")
          .action { (x, c) => c.copy(summaryFile = x) }
          .text("use given file as other summary"), */
        opt[Input]('d', "summaryDescription")
          .required()
          .valueName("<file>")
          .action { (x, c) => c.copy(summaryDescriptionFile = x) }
          .text("use given file as other summary descripton"),
        opt[Input]('w', "withSummary")
          .required()
          .valueName("<file>")
          .action { (x, c) => c.copy(withSummaryFile = x) }
          .text("use given file as other summary"),
        opt[Input]('e', "withSummaryDescription")
          .required()
          .valueName("<file>")
          .action { (x, c) => c.copy(withSummaryDescriptionFile = x) }
          .text("use given file as other summary descripton"),
        opt[File]('D', "description")
          .action { (x, c) => c.copy(description = x) }
          .text ("summary description in json format (paramer: %kind, %date)")

      )

    note("\n")

    cmd(TRANSLATE)
      .action { (_, c) => c.copy(command = "translate") }
      .text ("summarises PROV representations")
      .children(
        opt[Boolean]('s', "streaming")
          .action { (x, c) => c.copy(streaming = x) }
          .text ("streaming"),
        opt[Output]("stats")
          .action { (x, c) => c.copy(stats = x) }
          .text ("stats"),
        opt[Output]("queryResult")
          .action { (x, c) => c.copy(queryResult = x) }
          .text ("query result"),
        opt[Input]("query")
          .action { (x, c) => c.copy(query = x) }
          .text ("query file"),
        opt[String]("infiles")
          .action { (x, c) => c.copy(infiles = x) }
          .text ("alternate named input files in json")
      )

    note("\n")

    cmd(EXPAND)
      .action { (_, c) => c.copy(command = "expand") }
      .text("expand template")
      .children(
        opt[Input]('b', "bindings")
          .required()
          .action { (x, c) => c.copy(bindings = x) }
          .text("bindings"),
        opt[Int]('v', "version")
          .action { (x, c) => c.copy(bindingsVersion = x) }
          .text ("bindings version"),
        opt[Boolean]('g', "genorder")
          .action { (x, c) => c.copy(genorder = x) }
          .text ("In template expansion, generate order attribute. By default does not."),
        opt[Boolean]('A', "allexpanded")
          .action { (x, c) => c.copy(allexpanded = x) }
          .text ("In template expansion, generate term if all variables are bound."),
        opt[Output]('t', "time")
          .valueName ("<file>")
          .action { (x, c) => c.copy(time = x) }
          .text ("Timing information to be outputted in output file")
      )

    note("\n")

    cmd("bindings")
      .action { (_, c) => c.copy(command = "bindings") }
      .text ("convert bindings")
      .children(
        opt[Int]('v', "version")
          .required()
          .action { (x, c) => c.copy(bindingsVersion = x) }
          .text("bindings version"),
        opt[Boolean]('p', "pretty")
          .action { (x, c) => c.copy(pretty = x) }
          .text ("Pretty print output where possible")
      )

    note("\n")

    cmd("bindings.v2")
      .action { (_, c) => c.copy(command = "bindings.v2") }
      .text("convert bindings to v2")
      .children(
      )

    note("\n")

    cmd("bindings.v3")
      .action { (_, c) => c.copy(command = "bindings.v3") }
      .text("convert bindings to v3")
      .children(
      )


    note("\n")

    cmd("validate")
      .action { (_, c) => c.copy(command = "validate") }
      .text ("validate prov document (as per prov-constraints)")
      .children(
        opt[Output]('m', "matrix")
          .action { (x, c) => c.copy(matrix = x) }
          .text ("matrix file"),
        opt[Output]('g', "graphics")
          .action { (x, c) => c.copy(image = x) }
          .text ("graphics file")

      )


    note("\n")

    cmd("batch")
      .action { (_, c) => c.copy(command = "batch") }
      .text ("batch processing")
      .children (
        opt[Boolean]('p', "parallel")
          .action { (x, c) => c.copy(parallel = x) }
          .text ("Execute in parallel")

      )


  }

}
