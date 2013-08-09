package org.openprovenance.prov.rdf;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;

import javax.xml.namespace.QName;

import org.openprovenance.prov.xml.Attribute;
import org.openprovenance.prov.xml.NamespacePrefixMapper;


public class Ontology {

    public Ontology() {
	initInfluenceTables();
	initDomainTables();
        initRangeTables();
        initAttributeAsResourceTables();
    }
    public Hashtable<QName, QName> qualifiedInfluenceTable = new Hashtable<QName, QName>();
    public Hashtable<QName, QName> influencerTable = new Hashtable<QName, QName>();
    public Hashtable<QName, QName> unqualifiedTable = new Hashtable<QName, QName>();
    public Hashtable<QName, QName> otherTable = new Hashtable<QName, QName>();
    public Hashtable<QName, QName> convertTable = new Hashtable<QName, QName>();

    public Hashtable<QName, QName> domains = new Hashtable<QName, QName>();
    public Hashtable<QName, QName> ranges = new Hashtable<QName, QName>();
    
    public Set<QName> asObjectProperty=new HashSet<QName>();
    
    public static QName newProvQName(String local) {
	return new QName(NamespacePrefixMapper.PROV_NS, local,
			 NamespacePrefixMapper.PROV_PREFIX);
    }

    public static QName newBookQName(String local) {
	return new QName(NamespacePrefixMapper.BOOK_NS, local,
			 NamespacePrefixMapper.BOOK_PREFIX);
    }

    public static QName newRdfQName(String local) {
	return new QName(NamespacePrefixMapper.RDF_NS, local,
			 NamespacePrefixMapper.RDF_PREFIX);
    }
    
    public static QName newRdfsQName(String local) {
	return new QName(NamespacePrefixMapper.RDFS_NS, local,
			 NamespacePrefixMapper.RDFS_PREFIX);
    }

    public static QName QNAME_PROVO_atLocation = newProvQName("atLocation");
    public static QName QNAME_PROVO_atTime = newProvQName("atTime");
    public static QName QNAME_PROVO_startedAtTime = newProvQName("startedAtTime");
    public static QName QNAME_PROVO_endedAtTime = newProvQName("endedAtTime");
    public static QName QNAME_PROVO_influencer = newProvQName("influencer");
    public static QName QNAME_PROVO_activity = newProvQName("activity");
    public static QName QNAME_PROVO_entity = newProvQName("entity");
    public static QName QNAME_PROVO_agent = newProvQName("agent");
    public static QName QNAME_PROVO_hadActivity = newProvQName("hadActivity");
    public static QName QNAME_PROVO_hadEntity = newProvQName("hadEntity");
    public static QName QNAME_PROVO_hadPlan = newProvQName("hadPlan");
    public static QName QNAME_PROVO_hadGeneration = newProvQName("hadGeneration");
    public static QName QNAME_PROVO_hadUsage = newProvQName("hadUsage");
    public static QName QNAME_PROVO_hadRole = newProvQName("hadRole");
    public static QName QNAME_PROVO_value = newProvQName("value");
    public static QName QNAME_PROVO_generated = newProvQName("generated");
    
    public static QName QNAME_PROVO_generatedAtTime = newProvQName("generatedAtTime");
    public static QName QNAME_PROVO_influenced = newProvQName("influenced");
    public static QName QNAME_PROVO_invalidated = newProvQName("invalidated");
    public static QName QNAME_PROVO_invalidatedAtTime = newProvQName("invalidatedAtTime");

    public static QName QNAME_PROVO_Activity = newProvQName("Activity");
    public static QName QNAME_PROVO_Entity = newProvQName("Entity");
    public static QName QNAME_PROVO_Agent = newProvQName("Agent");

    public static QName QNAME_PROVO_Influence = newProvQName("Influence");
    public static QName QNAME_PROVO_qualifiedInfluence = newProvQName("qualifiedInfluence");
    public static QName QNAME_PROVO_wasInfluencedBy = newProvQName("wasInfluencedBy");

    public static QName QNAME_PROVO_Generation = newProvQName("Generation");
    public static QName QNAME_PROVO_qualifiedGeneration = newProvQName("qualifiedGeneration");
    public static QName QNAME_PROVO_wasGeneratedBy = newProvQName("wasGeneratedBy");

    public static QName QNAME_PROVO_Usage = newProvQName("Usage");
    public static QName QNAME_PROVO_qualifiedUsage = newProvQName("qualifiedUsage");
    public static QName QNAME_PROVO_used = newProvQName("used");

    public static QName QNAME_PROVO_Invalidation = newProvQName("Invalidation");
    public static QName QNAME_PROVO_qualifiedInvalidation = newProvQName("qualifiedInvalidation");
    public static QName QNAME_PROVO_wasInvalidatedBy = newProvQName("wasInvalidatedBy");

    public static QName QNAME_PROVO_Start = newProvQName("Start");
    public static QName QNAME_PROVO_qualifiedStart = newProvQName("qualifiedStart");
    public static QName QNAME_PROVO_wasStartedBy = newProvQName("wasStartedBy");

    public static QName QNAME_PROVO_End = newProvQName("End");
    public static QName QNAME_PROVO_qualifiedEnd = newProvQName("qualifiedEnd");
    public static QName QNAME_PROVO_wasEndedBy = newProvQName("wasEndedBy");

    public static QName QNAME_PROVO_Association = newProvQName("Association");
    public static QName QNAME_PROVO_qualifiedAssociation = newProvQName("qualifiedAssociation");
    public static QName QNAME_PROVO_wasAssociatedWith = newProvQName("wasAssociatedWith");

    public static QName QNAME_PROVO_Attribution = newProvQName("Attribution");
    public static QName QNAME_PROVO_qualifiedAttribution = newProvQName("qualifiedAttribution");
    public static QName QNAME_PROVO_wasAttributedTo = newProvQName("wasAttributedTo");

    public static QName QNAME_PROVO_Delegation = newProvQName("Delegation");
    public static QName QNAME_PROVO_qualifiedDelegation = newProvQName("qualifiedDelegation");
    public static QName QNAME_PROVO_actedOnBehalfOf = newProvQName("actedOnBehalfOf");

    public static QName QNAME_PROVO_Derivation = newProvQName("Derivation");
    public static QName QNAME_PROVO_qualifiedDerivation = newProvQName("qualifiedDerivation");
    public static QName QNAME_PROVO_wasDerivedFrom = newProvQName("wasDerivedFrom");

    public static QName QNAME_PROVO_Revision = newProvQName("Revision");
    public static QName QNAME_PROVO_qualifiedRevision = newProvQName("qualifiedRevision");
    public static QName QNAME_PROVO_wasRevisionOf = newProvQName("wasRevisionOf");

    public static QName QNAME_PROVO_Quotation = newProvQName("Quotation");
    public static QName QNAME_PROVO_qualifiedQuotation = newProvQName("qualifiedQuotation");
    public static QName QNAME_PROVO_wasQuotedFrom = newProvQName("wasQuotedFrom");
    
    public static QName QNAME_PROVO_PrimarySource = newProvQName("PrimarySource");
    public static QName QNAME_PROVO_qualifiedPrimarySource = newProvQName("qualifiedPrimarySource");
    public static QName QNAME_PROVO_hadPrimarySource = newProvQName("hadPrimarySource");

    public static QName QNAME_PROVO_Communication = newProvQName("Communication");
    public static QName QNAME_PROVO_qualifiedCommunication = newProvQName("qualifiedCommunication");
    public static QName QNAME_PROVO_wasInformedBy = newProvQName("wasInformedBy");

    public static QName QNAME_PROVO_specializationOf = newProvQName("specializationOf");
    public static QName QNAME_PROVO_alternateOf = newProvQName("alternateOf");
    public static QName QNAME_PROVO_mentionOf = newProvQName("mentionOf");
    public static QName QNAME_PROVO_asInBundle = newProvQName("asInBundle");
    public static QName QNAME_PROVO_hadMember = newProvQName("hadMember");


    public static QName QNAME_PROVO_Bundle = newProvQName("Bundle");
    public static QName QNAME_PROVO_Organization = newProvQName("Organization");
    public static QName QNAME_PROVO_Person = newProvQName("Person");
    public static QName QNAME_PROVO_SoftwareAgent = newProvQName("SoftwareAgent");
    public static QName QNAME_PROVO_Location = newProvQName("Location");
    public static QName QNAME_PROVO_Plan = newProvQName("Plan");
    public static QName QNAME_PROVO_Role = newProvQName("Role");
    public static QName QNAME_PROVO_Collection = newProvQName("Collection");
    public static QName QNAME_PROVO_EmptyCollection = newProvQName("EmptyCollection");

    public static QName QNAME_PROVO_InstantaneousEvent = newProvQName("InstantaneousEvent");
    public static QName QNAME_PROVO_EntityInfluence = newProvQName("EntityInfluence");
    public static QName QNAME_PROVO_ActivityInfluence = newProvQName("ActivityInfluence");
    public static QName QNAME_PROVO_AgentInfluence = newProvQName("AgentInfluence");
    
    public static QName QNAME_PROVDC_Contributor = newProvQName("Contributor");

    
    public static QName QNAME_RDF_TYPE = newRdfQName("type");
    public static QName QNAME_RDFS_LABEL = newRdfsQName("label");
    
    
    // dictionary stuff
    
    public static QName QNAME_PROVO_Dictionary = newProvQName("Dictionary");
    public static QName QNAME_PROVO_EmptyDictionary = newProvQName("EmptyDictionary");
    public static QName QNAME_PROVO_derivedByInsertion = newProvQName("derivedByInsertion");
    public static QName QNAME_PROVO_Insertion = newProvQName("Insertion");
    public static QName QNAME_PROVO_qualifiedInsertion = newProvQName("qualifiedInsertion");
    public static QName QNAME_PROVO_dictionary = newProvQName("dictionary");
    public static QName QNAME_PROVO_derivedByRemoval = newProvQName("derivedByRemoval");
    public static QName QNAME_PROVO_Removal = newProvQName("Removal");
    public static QName QNAME_PROVO_qualifiedRemoval = newProvQName("qualifiedRemoval");
    public static QName QNAME_PROVO_hadDictionaryMember = newProvQName("hadDictionaryMember");
	public static QName QNAME_PROVO_insertedKeyEntityPair = newProvQName("insertedKeyEntityPair");
	public static QName QNAME_PROVO_removedKey = newProvQName("removedKey");
	public static QName QNAME_PROVO_KeyValuePair = newProvQName("KeyValuePair");
	public static QName QNAME_PROVO_pairKey = newProvQName("pairKey");
	public static QName QNAME_PROVO_pairEntity = newProvQName("pairEntity");

    // prov book

    public static QName QNAME_BK_topicIn = newBookQName("topicIn");


	
    void initInfluenceTables() {
	qualifiedInfluenceTable.put(QNAME_PROVO_Influence,
				    QNAME_PROVO_qualifiedInfluence);
	qualifiedInfluenceTable.put(QNAME_PROVO_Generation,
				    QNAME_PROVO_qualifiedGeneration);
	qualifiedInfluenceTable.put(QNAME_PROVO_Usage,
				    QNAME_PROVO_qualifiedUsage);
	qualifiedInfluenceTable.put(QNAME_PROVO_Invalidation,
				    QNAME_PROVO_qualifiedInvalidation);
	qualifiedInfluenceTable.put(QNAME_PROVO_Start,
				    QNAME_PROVO_qualifiedStart);
	qualifiedInfluenceTable.put(QNAME_PROVO_End, QNAME_PROVO_qualifiedEnd);
	qualifiedInfluenceTable.put(QNAME_PROVO_Association,
				    QNAME_PROVO_qualifiedAssociation);
	qualifiedInfluenceTable.put(QNAME_PROVO_Attribution,
				    QNAME_PROVO_qualifiedAttribution);
	qualifiedInfluenceTable.put(QNAME_PROVO_Delegation,
				    QNAME_PROVO_qualifiedDelegation);
	qualifiedInfluenceTable.put(QNAME_PROVO_Derivation,
				    QNAME_PROVO_qualifiedDerivation);
	qualifiedInfluenceTable.put(QNAME_PROVO_Quotation,
				    QNAME_PROVO_qualifiedQuotation);
	qualifiedInfluenceTable.put(QNAME_PROVO_Revision,
				    QNAME_PROVO_qualifiedRevision);
	qualifiedInfluenceTable.put(QNAME_PROVO_PrimarySource,
				    QNAME_PROVO_qualifiedPrimarySource);
	qualifiedInfluenceTable.put(QNAME_PROVO_Communication,
		    QNAME_PROVO_qualifiedCommunication);
	qualifiedInfluenceTable.put(QNAME_PROVO_Insertion,
		    QNAME_PROVO_qualifiedInsertion);
	qualifiedInfluenceTable.put(QNAME_PROVO_Removal,
		    QNAME_PROVO_qualifiedRemoval);

	influencerTable.put(QNAME_PROVO_Influence, QNAME_PROVO_influencer);
	activityInfluence(QNAME_PROVO_Generation);
	entityInfluence(QNAME_PROVO_Usage);
	activityInfluence(QNAME_PROVO_Invalidation);
	entityInfluence(QNAME_PROVO_Start);
	entityInfluence(QNAME_PROVO_End);
	agentInfluence(QNAME_PROVO_Association);
	agentInfluence(QNAME_PROVO_Attribution);
	agentInfluence(QNAME_PROVO_Delegation);
	entityInfluence(QNAME_PROVO_Derivation);
	entityInfluence(QNAME_PROVO_Quotation);
	entityInfluence(QNAME_PROVO_Revision);
	entityInfluence(QNAME_PROVO_PrimarySource);
	activityInfluence(QNAME_PROVO_Communication);
	
	dictionaryInfluence(QNAME_PROVO_Insertion);
	dictionaryInfluence(QNAME_PROVO_Removal);

	unqualifiedTable.put(QNAME_PROVO_Influence, QNAME_PROVO_wasInfluencedBy);
	unqualifiedTable.put(QNAME_PROVO_Generation, QNAME_PROVO_wasGeneratedBy);
	unqualifiedTable.put(QNAME_PROVO_Usage, QNAME_PROVO_used);
	unqualifiedTable.put(QNAME_PROVO_Invalidation,
			     QNAME_PROVO_wasInvalidatedBy);
	unqualifiedTable.put(QNAME_PROVO_Start, QNAME_PROVO_wasStartedBy);
	unqualifiedTable.put(QNAME_PROVO_End, QNAME_PROVO_wasEndedBy);
	unqualifiedTable.put(QNAME_PROVO_Association,
			     QNAME_PROVO_wasAssociatedWith);
	unqualifiedTable.put(QNAME_PROVO_Attribution,
			     QNAME_PROVO_wasAttributedTo);
	unqualifiedTable.put(QNAME_PROVO_Delegation,
			     QNAME_PROVO_actedOnBehalfOf);
	unqualifiedTable.put(QNAME_PROVO_Derivation, QNAME_PROVO_wasDerivedFrom);
	unqualifiedTable.put(QNAME_PROVO_Revision, QNAME_PROVO_wasRevisionOf);
	unqualifiedTable.put(QNAME_PROVO_Quotation, QNAME_PROVO_wasQuotedFrom);
	unqualifiedTable.put(QNAME_PROVO_PrimarySource,
			     QNAME_PROVO_hadPrimarySource);
	unqualifiedTable.put(QNAME_PROVO_Communication,
		     QNAME_PROVO_wasInformedBy);
	unqualifiedTable.put(QNAME_PROVO_Insertion,
		     QNAME_PROVO_derivedByInsertion);
	unqualifiedTable.put(QNAME_PROVO_Removal,
		     QNAME_PROVO_derivedByRemoval);

	otherTable.put(QNAME_PROVO_Start, QNAME_PROVO_hadActivity);
	otherTable.put(QNAME_PROVO_End, QNAME_PROVO_hadActivity);
	otherTable.put(QNAME_PROVO_Derivation, QNAME_PROVO_hadActivity);
	otherTable.put(QNAME_PROVO_Revision, QNAME_PROVO_hadActivity);
	otherTable.put(QNAME_PROVO_Quotation, QNAME_PROVO_hadActivity);
	otherTable.put(QNAME_PROVO_PrimarySource, QNAME_PROVO_hadActivity);
	otherTable.put(QNAME_PROVO_Association, QNAME_PROVO_hadPlan);
	otherTable.put(QNAME_PROVO_Delegation, QNAME_PROVO_hadActivity);
	otherTable.put(QNAME_PROVO_Insertion, QNAME_PROVO_insertedKeyEntityPair);
	otherTable.put(QNAME_PROVO_Removal, QNAME_PROVO_insertedKeyEntityPair);

	convertTable.put(Attribute.PROV_LABEL_QNAME, QNAME_RDFS_LABEL);
	convertTable.put(Attribute.PROV_TYPE_QNAME, QNAME_RDF_TYPE);
	convertTable.put(Attribute.PROV_LOCATION_QNAME, QNAME_PROVO_atLocation);
	convertTable.put(Attribute.PROV_VALUE_QNAME, QNAME_PROVO_value);
	convertTable.put(Attribute.PROV_ROLE_QNAME, QNAME_PROVO_hadRole);
    }
    
    void initRangeTables() {
    	this.ranges.put(QNAME_PROVO_actedOnBehalfOf, QNAME_PROVO_Agent);
    	this.ranges.put(QNAME_PROVO_used, QNAME_PROVO_Entity);
    	this.ranges.put(QNAME_PROVO_wasAssociatedWith, QNAME_PROVO_Agent);
    	this.ranges.put(QNAME_PROVO_wasAttributedTo, QNAME_PROVO_Agent);
    	this.ranges.put(QNAME_PROVO_wasDerivedFrom, QNAME_PROVO_Entity);
    	this.ranges.put(QNAME_PROVO_wasGeneratedBy, QNAME_PROVO_Activity);
    	this.ranges.put(QNAME_PROVO_wasInformedBy, QNAME_PROVO_Activity);
    	this.ranges.put(QNAME_PROVO_alternateOf, QNAME_PROVO_Entity);
    	this.ranges.put(QNAME_PROVO_atLocation, QNAME_PROVO_Location);
    	this.ranges.put(QNAME_PROVO_generated, QNAME_PROVO_Entity);
    	this.ranges.put(QNAME_PROVO_hadMember, QNAME_PROVO_Entity);
    	this.ranges.put(QNAME_PROVO_hadPrimarySource, QNAME_PROVO_Entity);
    	this.ranges.put(QNAME_PROVO_invalidated, QNAME_PROVO_Entity);
    	this.ranges.put(QNAME_PROVO_specializationOf, QNAME_PROVO_Entity);
    	this.ranges.put(QNAME_PROVO_wasEndedBy, QNAME_PROVO_Entity);
    	this.ranges.put(QNAME_PROVO_wasInvalidatedBy, QNAME_PROVO_Activity);
    	this.ranges.put(QNAME_PROVO_wasQuotedFrom, QNAME_PROVO_Entity);
    	this.ranges.put(QNAME_PROVO_wasRevisionOf, QNAME_PROVO_Entity);
    	this.ranges.put(QNAME_PROVO_wasStartedBy, QNAME_PROVO_Entity);
    	this.ranges.put(QNAME_PROVO_activity, QNAME_PROVO_Activity);
    	this.ranges.put(QNAME_PROVO_agent, QNAME_PROVO_Agent);
    	this.ranges.put(QNAME_PROVO_entity, QNAME_PROVO_Entity);
    	this.ranges.put(QNAME_PROVO_hadActivity, QNAME_PROVO_Activity);
    	this.ranges.put(QNAME_PROVO_hadGeneration, QNAME_PROVO_Generation);
    	this.ranges.put(QNAME_PROVO_hadPlan, QNAME_PROVO_Plan);
    	this.ranges.put(QNAME_PROVO_hadUsage, QNAME_PROVO_Usage);
    	this.ranges.put(QNAME_PROVO_qualifiedAssociation, QNAME_PROVO_Association);
    	this.ranges.put(QNAME_PROVO_qualifiedAttribution, QNAME_PROVO_Attribution);
    	this.ranges.put(QNAME_PROVO_qualifiedCommunication, QNAME_PROVO_Communication);
    	this.ranges.put(QNAME_PROVO_qualifiedDelegation, QNAME_PROVO_Delegation);
    	this.ranges.put(QNAME_PROVO_qualifiedDerivation, QNAME_PROVO_Derivation);
    	this.ranges.put(QNAME_PROVO_qualifiedEnd, QNAME_PROVO_End);
    	this.ranges.put(QNAME_PROVO_qualifiedGeneration, QNAME_PROVO_Generation);
    	this.ranges.put(QNAME_PROVO_qualifiedInfluence, QNAME_PROVO_Influence);
    	this.ranges.put(QNAME_PROVO_qualifiedInvalidation, QNAME_PROVO_Invalidation);
    	this.ranges.put(QNAME_PROVO_qualifiedPrimarySource, QNAME_PROVO_PrimarySource);
    	this.ranges.put(QNAME_PROVO_qualifiedQuotation, QNAME_PROVO_Quotation);
    	this.ranges.put(QNAME_PROVO_qualifiedRevision, QNAME_PROVO_Revision);
    	this.ranges.put(QNAME_PROVO_qualifiedStart, QNAME_PROVO_Start);
    	this.ranges.put(QNAME_PROVO_qualifiedUsage, QNAME_PROVO_Usage);
    	this.ranges.put(QNAME_PROVO_qualifiedInsertion, QNAME_PROVO_Insertion);
    	this.ranges.put(QNAME_PROVO_derivedByInsertion, QNAME_PROVO_Dictionary);
    	this.ranges.put(QNAME_PROVO_qualifiedRemoval, QNAME_PROVO_Removal);
    	this.ranges.put(QNAME_PROVO_derivedByRemoval, QNAME_PROVO_Dictionary);
    	this.ranges.put(QNAME_PROVO_insertedKeyEntityPair, QNAME_PROVO_KeyValuePair);
        this.ranges.put(QNAME_BK_topicIn, QNAME_PROVO_Bundle);

    	
    }
    
    void initDomainTables() {
    	/*
    	 * The domain table maps from predicate to domain. Note that this means
    	 * that it excludes atLocation, influenced, hadRole, hadActivity, qualifiedInfluence, wasInfluencedBy
    	 * It is not possible to infer a single type from those predicates.
    	 */
    	this.domains.put(QNAME_PROVO_actedOnBehalfOf, QNAME_PROVO_Agent);
    	this.domains.put(QNAME_PROVO_qualifiedInfluence, QNAME_PROVO_Agent);
    	
    	this.domains.put(QNAME_PROVO_agent, QNAME_PROVO_AgentInfluence);
    	
    	this.domains.put(QNAME_PROVO_startedAtTime, QNAME_PROVO_Activity);
    	this.domains.put(QNAME_PROVO_endedAtTime, QNAME_PROVO_Activity);
    	this.domains.put(QNAME_PROVO_used, QNAME_PROVO_Activity);
    	this.domains.put(QNAME_PROVO_wasAssociatedWith, QNAME_PROVO_Activity);
    	this.domains.put(QNAME_PROVO_wasInformedBy, QNAME_PROVO_Activity);
    	this.domains.put(QNAME_PROVO_wasEndedBy, QNAME_PROVO_Activity);
    	this.domains.put(QNAME_PROVO_wasStartedBy, QNAME_PROVO_Activity);
    	this.domains.put(QNAME_PROVO_generated, QNAME_PROVO_Activity);
    	this.domains.put(QNAME_PROVO_invalidated, QNAME_PROVO_Activity);
    	this.domains.put(QNAME_PROVO_qualifiedAssociation, QNAME_PROVO_Activity);
    	this.domains.put(QNAME_PROVO_qualifiedCommunication, QNAME_PROVO_Activity);
    	this.domains.put(QNAME_PROVO_qualifiedEnd, QNAME_PROVO_Activity);
    	this.domains.put(QNAME_PROVO_qualifiedStart, QNAME_PROVO_Activity);
    	this.domains.put(QNAME_PROVO_qualifiedUsage, QNAME_PROVO_Activity);
    	
    	this.domains.put(QNAME_PROVO_activity, QNAME_PROVO_ActivityInfluence);
    	
    	this.domains.put(QNAME_PROVO_wasAttributedTo, QNAME_PROVO_Entity);
    	this.domains.put(QNAME_PROVO_wasDerivedFrom, QNAME_PROVO_Entity);
    	this.domains.put(QNAME_PROVO_wasGeneratedBy, QNAME_PROVO_Entity);
    	this.domains.put(QNAME_PROVO_alternateOf, QNAME_PROVO_Entity);
    	this.domains.put(QNAME_PROVO_generatedAtTime, QNAME_PROVO_Entity);
    	this.domains.put(QNAME_PROVO_hadMember, QNAME_PROVO_Collection);
    	this.domains.put(QNAME_PROVO_hadPrimarySource, QNAME_PROVO_Entity);
    	this.domains.put(QNAME_PROVO_invalidatedAtTime, QNAME_PROVO_Entity);
    	this.domains.put(QNAME_PROVO_wasInvalidatedBy, QNAME_PROVO_Entity);
    	this.domains.put(QNAME_PROVO_specializationOf, QNAME_PROVO_Entity);
    	this.domains.put(QNAME_PROVO_wasQuotedFrom, QNAME_PROVO_Entity);
    	this.domains.put(QNAME_PROVO_wasRevisionOf, QNAME_PROVO_Entity);
    	this.domains.put(QNAME_PROVO_value, QNAME_PROVO_Entity);
    	this.domains.put(QNAME_PROVO_qualifiedAttribution, QNAME_PROVO_Entity);
    	this.domains.put(QNAME_PROVO_qualifiedDerivation, QNAME_PROVO_Entity);
    	this.domains.put(QNAME_PROVO_qualifiedGeneration, QNAME_PROVO_Entity);
    	this.domains.put(QNAME_PROVO_qualifiedInvalidation, QNAME_PROVO_Entity);
    	this.domains.put(QNAME_PROVO_qualifiedPrimarySource, QNAME_PROVO_Entity);
    	this.domains.put(QNAME_PROVO_qualifiedQuotation, QNAME_PROVO_Entity);
    	this.domains.put(QNAME_PROVO_qualifiedRevision, QNAME_PROVO_Entity);
    	
    	this.domains.put(QNAME_PROVO_entity, QNAME_PROVO_EntityInfluence);
    	
    	this.domains.put(QNAME_PROVO_atTime, QNAME_PROVO_InstantaneousEvent);
    	
    	this.domains.put(QNAME_PROVO_influencer, QNAME_PROVO_Influence);
    	this.domains.put(QNAME_PROVO_hadGeneration, QNAME_PROVO_Derivation);
    	this.domains.put(QNAME_PROVO_hadPlan, QNAME_PROVO_Association);
    	this.domains.put(QNAME_PROVO_hadUsage, QNAME_PROVO_Derivation);
    	
    	
    	this.domains.put(QNAME_PROVO_qualifiedInsertion, QNAME_PROVO_Dictionary);
    	this.domains.put(QNAME_PROVO_derivedByInsertion, QNAME_PROVO_Dictionary);
    	this.domains.put(QNAME_PROVO_qualifiedRemoval, QNAME_PROVO_Dictionary);
    	this.domains.put(QNAME_PROVO_derivedByRemoval, QNAME_PROVO_Dictionary);
    	this.domains.put(QNAME_PROVO_insertedKeyEntityPair, QNAME_PROVO_Insertion);
    	
    }
    
    void initAttributeAsResourceTables() {
        asObjectProperty.add(QNAME_BK_topicIn);
        asObjectProperty.add(QNAME_PROVO_atLocation);
    }

    void activityInfluence(QName name) {
	influencerTable.put(name, QNAME_PROVO_activity);
    }

    void entityInfluence(QName name) {
    	influencerTable.put(name, QNAME_PROVO_entity);
        }
    void dictionaryInfluence(QName name) {
    	influencerTable.put(name, QNAME_PROVO_dictionary);
        }

    void agentInfluence(QName name) {
	influencerTable.put(name, QNAME_PROVO_agent);
    }
    
    

    public QName convertToRdf(QName qname) {
	QName res = convertTable.get(qname);
	if (res != null)
	    return res;
	return qname;
    }
}
