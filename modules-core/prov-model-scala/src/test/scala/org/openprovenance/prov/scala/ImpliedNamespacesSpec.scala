package org.openprovenance.prov.scala

import org.openprovenance.prov.model.NamespacePrefixMapper
import org.openprovenance.prov.scala.immutable._
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

/**
 * The notation of a document writes a qualified specialization, alternate, mention or membership as a provext
 * statement, and the openprov attributes under the openprov prefix; a document whose namespace declares neither,
 * one built in memory, is written with them declared, or could not be read back.
 */
class ImpliedNamespacesSpec extends AnyFlatSpec with Matchers {

  val openprov: String = NamespacePrefixMapper.OPENPROV_NS
  val provext: String = NamespacePrefixMapper.PROV_EXT_NS
  val declaration = s"prefix provext <$provext>"

  val provn: String =
    s"""document
       |prefix ex <http://example.org/>
       |prefix openprov <$openprov>
       |prefix provext <$provext>
       |entity(ex:e1)
       |entity(ex:e2)
       |provext:specializationOf(ex:spe; ex:e1, ex:e2, [openprov:previousEntity = 'ex:e0'])
       |endDocument
       |""".stripMargin

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

  def undeclared(): org.openprovenance.prov.model.Namespace = {
    val ns = new org.openprovenance.prov.model.Namespace
    ns.addKnownNamespaces()
    ns.register("ex", "http://example.org/")
    ns
  }

  "a document declaring neither" should "be written with provext and openprov declared, and read back" in {
    val doc = new Document(parse(provn).statementOrBundle, undeclared())
    val written = doc.toString
    written should include(declaration)
    written should include(s"prefix openprov <$openprov>")
    written should include("provext:specializationOf(ex:spe;")
    written should include("openprov:previousEntity = 'ex:e0'")
    parse(written).statements() should be(doc.statements())
  }

  "a document declaring provext" should "keep its own declaration" in {
    val ns = undeclared()
    ns.register("provext", NamespacePrefixMapper.LEGACY_PROV_EXT_NS)
    ns.register("openprov", openprov)
    val written = new Document(parse(provn).statementOrBundle, ns).toString
    written should not include declaration
    written should include(s"prefix provext <${NamespacePrefixMapper.LEGACY_PROV_EXT_NS}>")
  }

  "a document without a provext statement" should "not declare provext" in {
    val plain = parse(provn.replace("provext:specializationOf(ex:spe; ex:e1, ex:e2, [openprov:previousEntity = 'ex:e0'])", "specializationOf(ex:e1, ex:e2)"))
    new Document(plain.statementOrBundle, undeclared()).toString should not include "provext"
  }

  "a provext statement in a bundle" should "be declared once, at document level" in {
    val inner = parse(provn)
    val bundleNs = new org.openprovenance.prov.model.Namespace
    bundleNs.register("ex", "http://example.org/")
    val bundle = new Bundle(QualifiedName(ProvFactory.pf.newQualifiedName("http://example.org/", "b", "ex")), inner.statements(), bundleNs)
    val written = new Document(Seq(bundle), undeclared()).toString
    written should include(declaration)
    written.split("prefix provext ", -1).length - 1 should be(1)
    parse(written).bundles().head.statement.toSet should be(inner.statements().toSet)
  }
}
