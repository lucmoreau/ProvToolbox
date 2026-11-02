package org.openprovenance.prov.scala.immutable

import org.openprovenance.prov.model.{NamespacePrefixMapper, OpenprovTerms}
import org.openprovenance.prov.model.StatementOrBundle.Kind

/**
 * The openprov attributes of an attribution, a membership or a specialization, in the immutable model: read under
 * their written names (openprov:association) into the properties the model holds (openprov:hadAssociation), and
 * written back under the written names. See [[org.openprovenance.prov.model.OpenprovTerms]].
 */
object OpenprovAttributes {

  /** Reading: the attributes as the model holds them. */
  def canonical(kind: Kind, attributes: Seq[Attribute]): Seq[Attribute] =
    attributes.map {
      case o: Other if OpenprovTerms.isOpenprov(o.elementName) =>
        val local = OpenprovTerms.propertyLocalName(kind, o.elementName.localPart)
        if (local == null) o else Other(QualifiedName(ProvFactory.pf.newQualifiedName(o.elementName.namespaceURI, local, o.elementName.prefix)), o.`type`, o.value)
      case a => a
    }

  /** Writing: the other attributes as a document shows them, an openprov property under its term. */
  def surface(kind: Kind, other: Map[QualifiedName, Set[Other]]): Map[QualifiedName, Set[Other]] =
    if (other == null || other.isEmpty || !OpenprovTerms.kinds.contains(kind)) other
    else other.map { case (name, values) =>
      val term = if (OpenprovTerms.isOpenprov(name)) OpenprovTerms.shownAs(kind, name.localPart) else null
      if (term == null) (name, values)
      else {
        val shown = QualifiedName(ProvFactory.pf.newQualifiedName(name.namespaceURI, term, name.prefix))
        (shown, values.map(o => Other(shown, o.`type`, o.value)))
      }
    }
}

/**
 * The namespace a document is written with: provext declared when a qualified specialization, alternate, mention or
 * membership is written as a provext statement, openprov when its attributes are written, unless the namespace
 * declares them already. A document built in memory need not declare either; undeclared, what is written could not
 * be read back.
 */
object ImpliedNamespaces {

  val PROVEXT_PREFIX = "provext"

  def apply(namespace: org.openprovenance.prov.model.Namespace, statements: Iterable[Statement]): org.openprovenance.prov.model.Namespace = {
    val provext = statements.exists(writtenAsProvextStatement)
    val openprov = statements.exists(hasOpenprovAttribute)
    val declaresProvext = namespace.getPrefixes.containsKey(PROVEXT_PREFIX)
    val declaresOpenprov = namespace.getPrefixes.containsKey(NamespacePrefixMapper.OPENPROV_PREFIX)
    if ((!provext || declaresProvext) && (!openprov || declaresOpenprov)) namespace
    else {
      val declared = new org.openprovenance.prov.model.Namespace(namespace)
      if (provext && !declaresProvext) declared.register(PROVEXT_PREFIX, NamespacePrefixMapper.PROV_EXT_NS)
      if (openprov && !declaresOpenprov) declared.register(NamespacePrefixMapper.OPENPROV_PREFIX, NamespacePrefixMapper.OPENPROV_NS)
      declared
    }
  }

  /** As the statements' toNotation decides: an identifier, or any attribute, makes the statement a provext one. */
  def writtenAsProvextStatement(s: Statement): Boolean = s match {
    case x: SpecializationOf => x.id != null || x.label.nonEmpty || x.typex.nonEmpty || x.other.nonEmpty
    case x: AlternateOf      => x.id != null || x.label.nonEmpty || x.typex.nonEmpty || x.other.nonEmpty
    case x: MentionOf        => x.id != null || x.label.nonEmpty || x.typex.nonEmpty || x.other.nonEmpty
    case x: HadMember        => x.id != null || x.label.nonEmpty || x.typex.nonEmpty || x.other.nonEmpty
    case _ => false
  }

  def hasOpenprovAttribute(s: Statement): Boolean = s match {
    case h: HasOther => h.other.keys.exists(OpenprovTerms.isOpenprov)
    case _ => false
  }
}
