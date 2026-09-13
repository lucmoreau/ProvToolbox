package org.openprovenance.prov.scala

import org.openprovenance.prov.model.NamespacePrefixMapper
import org.openprovenance.prov.scala.immutable._
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

/**
 * The immutable model reads the openprov attributes of an attribution, a membership or a specialization under their
 * written names into the properties (openprov:association is openprov:hadAssociation) and writes them back without
 * had, as the Java model does.
 */
class OpenprovAttributesSpec extends AnyFlatSpec with Matchers {

  val openprov: String = NamespacePrefixMapper.OPENPROV_NS

  val provn: String =
    s"""document
       |prefix ex <http://example.org/>
       |prefix openprov <$openprov>
       |prefix provext <${NamespacePrefixMapper.PROV_EXT_NS}>
       |entity(ex:e1, [openprov:association = 'ex:not-a-triangle'])
       |entity(ex:e2)
       |entity(ex:c)
       |agent(ex:ag1)
       |activity(ex:a1)
       |wasAttributedTo(ex:att; ex:e1, ex:ag1, [openprov:activity = 'ex:a1', openprov:association = 'ex:asc1', openprov:generation = 'ex:gen1', ex:note = "kept"])
       |provext:hadMember(ex:mem; ex:c, ex:e1, [openprov:activity = 'ex:adding', openprov:collectionGeneration = 'ex:gen0', openprov:itemGeneration = 'ex:gen1'])
       |provext:specializationOf(ex:spe; ex:e1, ex:e2, [openprov:entity = 'ex:e0', openprov:derivation = 'ex:der1', openprov:specialization = 'ex:spe0'])
       |endDocument
       |""".stripMargin

  /** As Parser.readDocument, from text rather than a file. */
  def parse(text: String): Document = {
    val actions = new MyActions()
    val actions2 = new MyActions2()
    val funs = new org.openprovenance.prov.scala.streaming.DocBuilderFunctions()
    val docBuilder = new org.openprovenance.prov.scala.streaming.DocBuilder(funs)
    val ns = new org.openprovenance.prov.model.Namespace
    ns.addKnownNamespaces()
    actions2.docns = ns
    actions2.bun_ns = None
    actions2.next = docBuilder
    val p = new MyParser(text, actions2, actions)
    p.document.run() match {
      case scala.util.Success(_) => docBuilder.document()
      case scala.util.Failure(e: org.parboiled2.ParseError) => fail("not valid PROV-N: " + p.formatError(e))
      case scala.util.Failure(e) => throw e
    }
  }

  def openprovOf(other: Map[QualifiedName, Set[Other]]): Map[String, String] =
    other.collect { case (name, values) if name.namespaceURI == openprov =>
      name.localPart -> values.head.value.asInstanceOf[QualifiedName].localPart
    }

  "the parser" should "read the openprov attributes as the properties" in {
    val doc = parse(provn)
    val att = doc.statements().collectFirst { case a: WasAttributedTo => a }.get
    openprovOf(att.other) should be(Map("hadActivity" -> "a1", "hadAssociation" -> "asc1", "hadGeneration" -> "gen1"))
    att.other.keys.count(_.localPart == "note") should be(1)
    val mem = doc.statements().collectFirst { case m: HadMember => m }.get
    openprovOf(mem.other) should be(Map("hadActivity" -> "adding", "hadCollectionGeneration" -> "gen0", "hadItemGeneration" -> "gen1"))
    val spe = doc.statements().collectFirst { case s: SpecializationOf => s }.get
    openprovOf(spe.other) should be(Map("hadEntity" -> "e0", "hadDerivation" -> "der1", "hadSpecialization" -> "spe0"))
  }

  it should "leave the same name on an entity as it is" in {
    val doc = parse(provn)
    val e1 = doc.statements().collectFirst { case e: Entity if e.id.localPart == "e1" => e }.get
    openprovOf(e1.other) should be(Map("association" -> "not-a-triangle"))
  }

  "the notation" should "show them without had, and read back equal" in {
    val doc = parse(provn)
    val ns = new org.openprovenance.prov.model.Namespace
    ns.addKnownNamespaces()
    ns.register("ex", "http://example.org/")
    ns.register("openprov", openprov)
    ns.register("provext", NamespacePrefixMapper.PROV_EXT_NS)
    org.openprovenance.prov.model.Namespace.withThreadNamespace(ns)
    val written = doc.statements().map(_.toString).mkString("document\n" + s"prefix ex <http://example.org/>\nprefix openprov <$openprov>\nprefix provext <${NamespacePrefixMapper.PROV_EXT_NS}>\n", "\n", "\nendDocument\n")
    written should include("openprov:association = 'ex:asc1'")
    written should include("openprov:collectionGeneration = 'ex:gen0'")
    written should include("openprov:entity = 'ex:e0'")
    written should not include "openprov:had"
    parse(written).statements() should be(doc.statements())
  }
}
