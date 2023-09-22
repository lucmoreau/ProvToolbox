package org.openprovenance.prov.scala.immutable
import javax.xml.datatype.XMLGregorianCalendar
import org.openprovenance.prov.model

trait ImmutableConstructorInterface {
  def newEntity(id: model.QualifiedName,
                attributes: Set[Attribute]): Entity

  def newAgent(id: QualifiedName,
               attributes: Set[Attribute]): Agent

  def newActivity(id: QualifiedName,
                  startTime: XMLGregorianCalendar,
                  endTime: XMLGregorianCalendar,
                  attributes: Set[Attribute]): Activity

  def newActivity(id: QualifiedName,
                  startTime: Option[XMLGregorianCalendar],
                  endTime: Option[XMLGregorianCalendar],
                  attributes: Set[Attribute]): Activity

  def newWasDerivedFrom(id: QualifiedName,
                        generatedEntity: QualifiedName,
                        usedEntity: QualifiedName,
                        activity: QualifiedName,
                        generation: QualifiedName,
                        usage: QualifiedName,
                        attributes: Set[Attribute]): WasDerivedFrom

  def newWasGeneratedBy(id: QualifiedName,
                        entity: QualifiedName,
                        activity: QualifiedName,
                        time: Option[XMLGregorianCalendar],
                        attributes: Set[Attribute]): WasGeneratedBy

  def newUsed(id: QualifiedName,
              activity: QualifiedName,
              entity: QualifiedName,
              time: Option[XMLGregorianCalendar],
              attributes: Set[Attribute]): Used

  def newWasInvalidatedBy(id: QualifiedName,
                          entity: QualifiedName,
                          activity: QualifiedName,
                          time: Option[XMLGregorianCalendar],
                          attributes: Set[Attribute]): WasInvalidatedBy

  def newWasStartedBy(id: QualifiedName,
                      activity: QualifiedName,
                      trigger: QualifiedName,
                      starter: QualifiedName,
                      time: Option[XMLGregorianCalendar],
                      attributes: Set[Attribute]): WasStartedBy

  def newWasEndedBy(id: QualifiedName,
                    activity: QualifiedName,
                    trigger: QualifiedName,
                    ender: QualifiedName,
                    time: Option[XMLGregorianCalendar],
                    attributes: Set[Attribute]): WasEndedBy

  def newWasAssociatedWith(id: QualifiedName,
                           activity: QualifiedName,
                           agent: QualifiedName,
                           plan: QualifiedName,
                           attributes: Set[Attribute]): WasAssociatedWith

  def newActedOnBehalfOf(id: QualifiedName,
                         delegate: QualifiedName,
                         responsible: QualifiedName,
                         activity: QualifiedName,
                         attributes: Set[Attribute]): ActedOnBehalfOf

  def newWasAttributedTo(id: QualifiedName,
                         entity: QualifiedName,
                         agent: QualifiedName,
                         attributes: Set[Attribute]): WasAttributedTo

  def newSpecializationOf(id: QualifiedName,
                          entity2: QualifiedName,
                          entity1: QualifiedName,
                          attributes: Set[Attribute]): SpecializationOf

  def newMentionOf(specializedEntity: QualifiedName,
                   generalEntity: QualifiedName,
                   bundle: QualifiedName): MentionOf

  def newMentionOf(specializedEntity: QualifiedName,
                   generalEntity: QualifiedName,
                   bundle: QualifiedName,
                   attributes: Set[Attribute]): MentionOf

  def newMentionOf(id: QualifiedName,
                   specializedEntity: QualifiedName,
                   generalEntity: QualifiedName,
                   bundle: QualifiedName,
                   attributes: Set[Attribute]): MentionOf

  def newAlternateOf(id: QualifiedName,
                     entity2: QualifiedName,
                     entity1: QualifiedName,
                     attributes: Set[Attribute]): AlternateOf

  def newWasInformedBy(id: QualifiedName,
                       informed: QualifiedName,
                       informant: QualifiedName,
                       attributes: Set[Attribute]): WasInformedBy

  def newWasInfluencedBy(id: QualifiedName,
                         influencee: QualifiedName,
                         influencer: QualifiedName,
                         attributes: Set[Attribute]): WasInfluencedBy

  def newHadMember(collection: QualifiedName,
                   entity: Set[QualifiedName]): HadMember

  def newHadMember(id: QualifiedName,
                   collection: QualifiedName,
                   entity: Set[QualifiedName],
                   attributes: Set[Attribute]): HadMember
}
