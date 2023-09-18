package org.openprovenance.prov.scala.utilities

import org.openprovenance.prov.model.StatementOrBundle
import org.openprovenance.prov.scala.immutable.Kind.Kind
import org.openprovenance.prov.scala.immutable._
import Utils.fourNullable



class WasDerivedFromPlus(val id: QualifiedName,
                         val generatedEntity: QualifiedName,
                         val usedEntity: QualifiedName,
                         val activity: QualifiedName,
                         val generation: QualifiedName,
                         val usage: QualifiedName,
                         val label: Set[LangString],
                         val typex: Set[Type],
                         val other: Map[QualifiedName,Set[Other]]) extends Statement with ExtensionStatement with Relation with org.openprovenance.prov.model.Relation {


  def addAttributes (attr: Set[Attribute]) = {
    this
  }

  override def toNotation(sb: StringBuilder): Unit =  {
    sb++="wasDerivedFrom+("
    optionalId(id,sb)
    idOrMarker(generatedEntity, sb)
    sb+=','
    idOrMarker(usedEntity, sb)
    Attribute.toNotation(sb, label, typex, Set(), Set(), Set(), other)
    sb+=')'
  }


  def getCause() = usedEntity
  def getEffect() = generatedEntity



  override def getAttributes(): Set[Attribute] = Set()

  override val enumType: Kind = Kind.winfl  // TODO

  override def getKind: StatementOrBundle.Kind = StatementOrBundle.Kind.PROV_DERIVATION // TODO

  override def addIds(set: Set[QualifiedName]): Set[QualifiedName] = {
    fourNullable(set,id,generatedEntity,usedEntity,activity)
  }
}

class WasDerivedFromStar(val id: QualifiedName,
                         val generatedEntity: QualifiedName,
                         val usedEntity: QualifiedName,
                         val activity: QualifiedName,
                         val generation: QualifiedName,
                         val usage: QualifiedName,
                         val label: Set[LangString],
                         val typex: Set[Type],
                         val other: Map[QualifiedName,Set[Other]]) extends Statement with ExtensionStatement with Relation with org.openprovenance.prov.model.Relation {


  def addAttributes (attr: Set[Attribute]) = {
    this
  }

  override def toNotation(sb: StringBuilder): Unit =  {
    sb++="wasDerivedFrom*("
    optionalId(id,sb)
    idOrMarker(generatedEntity, sb)
    sb+=','
    idOrMarker(usedEntity, sb)
    Attribute.toNotation(sb, label, typex, Set(), Set(), Set(), other)
    sb+=')'
  }


  def getCause() = usedEntity
  def getEffect() = generatedEntity



  override def getAttributes(): Set[Attribute] = Set()

  override val enumType: Kind = Kind.winfl  // TODO

  override def getKind: StatementOrBundle.Kind = StatementOrBundle.Kind.PROV_DERIVATION // TODO

  override def addIds(set: Set[QualifiedName]): Set[QualifiedName] = {
    fourNullable(set,id,generatedEntity,usedEntity,activity)
  }
}
