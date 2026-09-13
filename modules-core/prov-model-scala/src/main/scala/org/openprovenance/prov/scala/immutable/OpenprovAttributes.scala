package org.openprovenance.prov.scala.immutable

import org.openprovenance.prov.model.OpenprovTerms
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
      val term = if (OpenprovTerms.isOpenprov(name)) OpenprovTerms.term(kind, name.localPart) else null
      if (term == null) (name, values)
      else {
        val shown = QualifiedName(ProvFactory.pf.newQualifiedName(name.namespaceURI, term, name.prefix))
        (shown, values.map(o => Other(shown, o.`type`, o.value)))
      }
    }
}
