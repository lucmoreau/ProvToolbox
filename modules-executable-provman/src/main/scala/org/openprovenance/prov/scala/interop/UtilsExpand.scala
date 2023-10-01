package org.openprovenance.prov.scala.interop
import org.openprovenance.prov.scala.immutable.ProvFactory.pf
import org.openprovenance.prov.scala.immutable._
import org.openprovenance.prov.scala.interop.CommandLine.output
import org.openprovenance.prov.scala.nf.CommandLine.parseDocument
import org.openprovenance.prov.scala.template.{BindingKind, Expander}
import org.openprovenance.prov.template.{Bindings, BindingsJson}

import scala.collection.parallel.CollectionConverters._


class UtilsExpand {


  def prepareExpansion(in: Input, config: Config): (Document, Bindings) = {

    val doc = parseDocument(in)

    val version = config.bindingsVersion


    val bindings =
      if (version == 3) {

        val bb2 = config.bindings match {
          case FileInput(f) => BindingsJson.importBean(f)
          case StandardInput() => BindingsJson.importBean(System.in)
        }

        val bindings = BindingsJson.fromBean(bb2, pf)

        //val expansion=exp.expander(doc, "ignore", bindings)

        bindings

      } else {

        val bindingDoc = parseDocument(config.bindings)

        val bindings =
          (if (config.bindingsVersion == 1) BindingKind.V1 else {
            BindingKind.V2
          }) match {
            case BindingKind.V1 => Bindings.fromDocument(bindingDoc, pf)
            case BindingKind.V2 => Bindings.fromDocument_v2(bindingDoc, ProvFactory.pf)
          }

        //val expansion=exp.expander(doc, "ignore", bindingDoc, if (config.bindingsVersion==1) BindingKind.V1 else {BindingKind.V2})

        bindings

      }

    (doc, bindings)

  }

  def expand(in: Input, config: Config): Document = {
    val (doc, bindings) = prepareExpansion(in, config)
    val exp = new Expander(config.allexpanded, config.genorder)
    val expansion = exp.expander(doc, "ignore", bindings)

    expansion
  }


  def expandExport(in: Input, config: Config): Unit = {
    val theformats = config.theOutputFormats()
    val expansion = expand(in, config)
    config.outfiles.zip(theformats).par foreach { case (o: Output, format: Format.Format) => Format2.outputers(format).output(expansion, o, Map[String, String]()) }

  }

  def expandTime(in: Input, config: Config, out: Output): Unit = {
    val (doc, bindings) = prepareExpansion(in, config)
    var i = 0
    for (i <- 0 to config.jitWait) {
      val exp = new Expander(config.allexpanded, config.genorder)
      val expansion = exp.expander(doc, "ignore", bindings)
    }
    var count = 0
    val result =
      for (count <- 0 to config.averageRepeat) yield {
        val before = System.nanoTime
        for (i <- 1 to config.averageNum) {
          val exp = new Expander(config.allexpanded, config.genorder)
          val expansion = exp.expander(doc, "ignore", bindings)
        }
        val after = System.nanoTime
        val duration: Float = (after - before) / config.averageNum
        duration
      }

    val sb = new StringBuilder
    //System.out.println("Time is " + result.map(_/1000000.0))
    val avg = result.sum / result.length / 1000000.0
    sb.append(avg)
    sb.append(", ")
    result.map(_ / 1000000.0).addString(sb, ",")
    //sb.append("\n")
    output(sb.toString(), out)
  }

  def bindings(config: Config): Unit = {
    if (config.bindingsVersion == 2) {
      bindings_v2(config)
    } else {
      bindings_v3(config)
    }
  }

  def bindings_v2(config: Config): Unit = {
    System.out.println("converting bindings to v2")

    val bindings = config.infile
    val bindingsDoc = parseDocument(bindings)
    val bindings_v1 = Bindings.fromDocument(bindingsDoc, pf)

    System.out.println("bindings v1 " ++ bindings_v1.toString())


    val doc_v2 = bindings_v1.toDocument_v2.asInstanceOf[org.openprovenance.prov.scala.immutable.Document]

    val theformats = config.theOutputFormats()

    config.outfiles.zip(theformats).par foreach { case (o: Output, format: Format.Format) => Format2.outputers(format).output(doc_v2, o, Map[String, String]()) }


  }

  def bindings_v3(config: Config): Unit = {


    // System.out.println("converting bindings to v3");
    val bindings = config.infile
    val bindingsDoc = parseDocument(bindings)
    val bindings_v1 = Bindings.fromDocument(bindingsDoc, pf)

    bindings_v1.addVariableBindingsAsAttributeBindings()

    // System.out.println("bindings v1 " ++ bindings_v1.toString())

    val doc_v2 = bindings_v1.toDocument_v2.asInstanceOf[org.openprovenance.prov.scala.immutable.Document]

    val bb = BindingsJson.toBean(bindings_v1)

    val name = config.outfiles.head match {
      case FileOutput(f) => f
      case _ => ???
    }

    BindingsJson.exportBean(name.toString, bb, config.pretty)

  }


}
