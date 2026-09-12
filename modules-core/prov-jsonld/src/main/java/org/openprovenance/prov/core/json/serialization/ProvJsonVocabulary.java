package org.openprovenance.prov.core.json.serialization;

import java.util.List;
import java.util.Set;

/** The property names of <a href="https://www.w3.org/submissions/prov-json/">PROV-JSON</a>. */
public interface ProvJsonVocabulary {

    String PREFIX = "prefix";
    /** The reserved prefix under which the default namespace is declared. */
    String DEFAULT = "default";

    String ENTITY = "entity";
    String ACTIVITY = "activity";
    String AGENT = "agent";
    String USED = "used";
    String WAS_GENERATED_BY = "wasGeneratedBy";
    String WAS_INVALIDATED_BY = "wasInvalidatedBy";
    String WAS_STARTED_BY = "wasStartedBy";
    String WAS_ENDED_BY = "wasEndedBy";
    String WAS_INFORMED_BY = "wasInformedBy";
    String WAS_DERIVED_FROM = "wasDerivedFrom";
    String WAS_ATTRIBUTED_TO = "wasAttributedTo";
    String WAS_ASSOCIATED_WITH = "wasAssociatedWith";
    String ACTED_ON_BEHALF_OF = "actedOnBehalfOf";
    String WAS_INFLUENCED_BY = "wasInfluencedBy";
    String SPECIALIZATION_OF = "specializationOf";
    String ALTERNATE_OF = "alternateOf";
    String HAD_MEMBER = "hadMember";
    String MENTION_OF = "mentionOf";
    String BUNDLE = "bundle";

    /** The sections a document may have. */
    List<String> SECTIONS = List.of(ENTITY, ACTIVITY, AGENT, USED, WAS_GENERATED_BY, WAS_INVALIDATED_BY,
            WAS_STARTED_BY, WAS_ENDED_BY, WAS_INFORMED_BY, WAS_DERIVED_FROM, WAS_ATTRIBUTED_TO, WAS_ASSOCIATED_WITH,
            ACTED_ON_BEHALF_OF, WAS_INFLUENCED_BY, SPECIALIZATION_OF, ALTERNATE_OF, HAD_MEMBER, MENTION_OF, BUNDLE);

    String PROV_ENTITY = "prov:entity";
    String PROV_ACTIVITY = "prov:activity";
    String PROV_AGENT = "prov:agent";
    String PROV_TIME = "prov:time";
    String PROV_START_TIME = "prov:startTime";
    String PROV_END_TIME = "prov:endTime";
    String PROV_TRIGGER = "prov:trigger";
    String PROV_STARTER = "prov:starter";
    String PROV_ENDER = "prov:ender";
    String PROV_INFORMED = "prov:informed";
    String PROV_INFORMANT = "prov:informant";
    String PROV_GENERATED_ENTITY = "prov:generatedEntity";
    String PROV_USED_ENTITY = "prov:usedEntity";
    String PROV_GENERATION = "prov:generation";
    String PROV_USAGE = "prov:usage";
    String PROV_PLAN = "prov:plan";
    String PROV_DELEGATE = "prov:delegate";
    String PROV_RESPONSIBLE = "prov:responsible";
    String PROV_INFLUENCEE = "prov:influencee";
    String PROV_INFLUENCER = "prov:influencer";
    String PROV_SPECIFIC_ENTITY = "prov:specificEntity";
    String PROV_GENERAL_ENTITY = "prov:generalEntity";
    String PROV_ALTERNATE1 = "prov:alternate1";
    String PROV_ALTERNATE2 = "prov:alternate2";
    String PROV_COLLECTION = "prov:collection";
    String PROV_BUNDLE = "prov:bundle";

    /** The properties that identify what a relation relates, as opposed to its attributes. */
    Set<String> CORE_PROPERTIES = Set.of(PROV_ENTITY, PROV_ACTIVITY, PROV_AGENT, PROV_TIME, PROV_START_TIME, PROV_END_TIME,
            PROV_TRIGGER, PROV_STARTER, PROV_ENDER, PROV_INFORMED, PROV_INFORMANT, PROV_GENERATED_ENTITY, PROV_USED_ENTITY,
            PROV_GENERATION, PROV_USAGE, PROV_PLAN, PROV_DELEGATE, PROV_RESPONSIBLE, PROV_INFLUENCEE, PROV_INFLUENCER,
            PROV_SPECIFIC_ENTITY, PROV_GENERAL_ENTITY, PROV_ALTERNATE1, PROV_ALTERNATE2, PROV_COLLECTION, PROV_BUNDLE);

    String PROV_TYPE = "prov:type";
    String PROV_LABEL = "prov:label";
    String PROV_LOCATION = "prov:location";
    String PROV_ROLE = "prov:role";
    String PROV_VALUE = "prov:value";

    /** The value of a typed or language-tagged literal. */
    String DOLLAR = "$";
    String TYPE = "type";
    String LANG = "lang";

    /** The type PROV-JSON gives a qualified name value, and the one PROV-DM gives it; both are read. */
    String XSD_QNAME = "xsd:QName";
    String PROV_QUALIFIED_NAME = "prov:QUALIFIED_NAME";

    /** Blank identifiers, {@code _:local}, carry no meaning: a relation so identified has no identifier. */
    String BLANK_PREFIX = "_";
    String BLANK_NS = "https://openprovenance.org/blank#";
}
