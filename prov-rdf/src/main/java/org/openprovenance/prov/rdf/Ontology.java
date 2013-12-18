package org.openprovenance.prov.rdf;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;


import org.openprovenance.prov.model.Name;
import org.openprovenance.prov.model.QualifiedName;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.model.NamespacePrefixMapper;


public class Ontology {
 

    final private ProvFactory pFactory;
    final private Name name;

    public Ontology(ProvFactory pFactory) {
	this.pFactory=pFactory;
	this.name=pFactory.getName();
	
	QNAME_PROVO_atLocation = newProvQName("atLocation");
	QNAME_PROVO_atTime = newProvQName("atTime");
	QNAME_PROVO_startedAtTime = newProvQName("startedAtTime");
	QNAME_PROVO_endedAtTime = newProvQName("endedAtTime");
	QNAME_PROVO_influencer = newProvQName("influencer");
	QNAME_PROVO_activity = newProvQName("activity");
	QNAME_PROVO_entity = newProvQName("entity");
	QNAME_PROVO_agent = newProvQName("agent");
	QNAME_PROVO_hadActivity = newProvQName("hadActivity");
	QNAME_PROVO_hadEntity = newProvQName("hadEntity");
	QNAME_PROVO_hadPlan = newProvQName("hadPlan");
	QNAME_PROVO_hadGeneration = newProvQName("hadGeneration");
	QNAME_PROVO_hadUsage = newProvQName("hadUsage");
	QNAME_PROVO_hadRole = newProvQName("hadRole");
	QNAME_PROVO_value = newProvQName("value");
	QNAME_PROVO_generated = newProvQName("generated");
    
	QNAME_PROVO_generatedAtTime = newProvQName("generatedAtTime");
	QNAME_PROVO_influenced = newProvQName("influenced");
	QNAME_PROVO_invalidated = newProvQName("invalidated");
	QNAME_PROVO_invalidatedAtTime = newProvQName("invalidatedAtTime");

	QNAME_PROVO_Activity = newProvQName("Activity");
	QNAME_PROVO_Entity = newProvQName("Entity");
	QNAME_PROVO_Agent = newProvQName("Agent");

	QNAME_PROVO_Influence = newProvQName("Influence");
	QNAME_PROVO_qualifiedInfluence = newProvQName("qualifiedInfluence");
	QNAME_PROVO_wasInfluencedBy = newProvQName("wasInfluencedBy");

	QNAME_PROVO_Generation = newProvQName("Generation");
	QNAME_PROVO_qualifiedGeneration = newProvQName("qualifiedGeneration");
	QNAME_PROVO_wasGeneratedBy = newProvQName("wasGeneratedBy");

	QNAME_PROVO_Usage = newProvQName("Usage");
	QNAME_PROVO_qualifiedUsage = newProvQName("qualifiedUsage");
	QNAME_PROVO_used = newProvQName("used");

	QNAME_PROVO_Invalidation = newProvQName("Invalidation");
	QNAME_PROVO_qualifiedInvalidation = newProvQName("qualifiedInvalidation");
	QNAME_PROVO_wasInvalidatedBy = newProvQName("wasInvalidatedBy");

	QNAME_PROVO_Start = newProvQName("Start");
	QNAME_PROVO_qualifiedStart = newProvQName("qualifiedStart");
	QNAME_PROVO_wasStartedBy = newProvQName("wasStartedBy");

	QNAME_PROVO_End = newProvQName("End");
	QNAME_PROVO_qualifiedEnd = newProvQName("qualifiedEnd");
	QNAME_PROVO_wasEndedBy = newProvQName("wasEndedBy");

	QNAME_PROVO_Association = newProvQName("Association");
	QNAME_PROVO_qualifiedAssociation = newProvQName("qualifiedAssociation");
	QNAME_PROVO_wasAssociatedWith = newProvQName("wasAssociatedWith");

	QNAME_PROVO_Attribution = newProvQName("Attribution");
	QNAME_PROVO_qualifiedAttribution = newProvQName("qualifiedAttribution");
	QNAME_PROVO_wasAttributedTo = newProvQName("wasAttributedTo");

	QNAME_PROVO_Delegation = newProvQName("Delegation");
	QNAME_PROVO_qualifiedDelegation = newProvQName("qualifiedDelegation");
	QNAME_PROVO_actedOnBehalfOf = newProvQName("actedOnBehalfOf");

	QNAME_PROVO_Derivation = newProvQName("Derivation");
	QNAME_PROVO_qualifiedDerivation = newProvQName("qualifiedDerivation");
	QNAME_PROVO_wasDerivedFrom = newProvQName("wasDerivedFrom");

	QNAME_PROVO_Revision = newProvQName("Revision");
	QNAME_PROVO_qualifiedRevision = newProvQName("qualifiedRevision");
	QNAME_PROVO_wasRevisionOf = newProvQName("wasRevisionOf");

	QNAME_PROVO_Quotation = newProvQName("Quotation");
	QNAME_PROVO_qualifiedQuotation = newProvQName("qualifiedQuotation");
	QNAME_PROVO_wasQuotedFrom = newProvQName("wasQuotedFrom");
    
	QNAME_PROVO_PrimarySource = newProvQName("PrimarySource");
	QNAME_PROVO_qualifiedPrimarySource = newProvQName("qualifiedPrimarySource");
	QNAME_PROVO_hadPrimarySource = newProvQName("hadPrimarySource");

	QNAME_PROVO_Communication = newProvQName("Communication");
	QNAME_PROVO_qualifiedCommunication = newProvQName("qualifiedCommunication");
	QNAME_PROVO_wasInformedBy = newProvQName("wasInformedBy");

	QNAME_PROVO_specializationOf = newProvQName("specializationOf");
	QNAME_PROVO_alternateOf = newProvQName("alternateOf");
	QNAME_PROVO_mentionOf = newProvQName("mentionOf");
	QNAME_PROVO_asInBundle = newProvQName("asInBundle");
	QNAME_PROVO_hadMember = newProvQName("hadMember");


	QNAME_PROVO_Bundle = newProvQName("Bundle");
	QNAME_PROVO_Organization = newProvQName("Organization");
	QNAME_PROVO_Person = newProvQName("Person");
	QNAME_PROVO_SoftwareAgent = newProvQName("SoftwareAgent");
	QNAME_PROVO_Location = newProvQName("Location");
	QNAME_PROVO_Plan = newProvQName("Plan");
	QNAME_PROVO_Role = newProvQName("Role");
	QNAME_PROVO_Collection = newProvQName("Collection");
	QNAME_PROVO_EmptyCollection = newProvQName("EmptyCollection");

	QNAME_PROVO_InstantaneousEvent = newProvQName("InstantaneousEvent");
	QNAME_PROVO_EntityInfluence = newProvQName("EntityInfluence");
	QNAME_PROVO_ActivityInfluence = newProvQName("ActivityInfluence");
	QNAME_PROVO_AgentInfluence = newProvQName("AgentInfluence");
    
	QNAME_PROVDC_Contributor = newProvQName("Contributor");

    
	QNAME_RDF_TYPE = newRdfQName("type");
	QNAME_RDFS_LABEL = newRdfsQName("label");
    
    
    
    
	QNAME_PROVO_Dictionary = newProvQName("Dictionary");
	QNAME_PROVO_EmptyDictionary = newProvQName("EmptyDictionary");
	QNAME_PROVO_derivedByInsertion = newProvQName("derivedByInsertion");
	QNAME_PROVO_Insertion = newProvQName("Insertion");
	QNAME_PROVO_qualifiedInsertion = newProvQName("qualifiedInsertion");
	QNAME_PROVO_dictionary = newProvQName("dictionary");
	QNAME_PROVO_derivedByRemoval = newProvQName("derivedByRemoval");
	QNAME_PROVO_Removal = newProvQName("Removal");
	QNAME_PROVO_qualifiedRemoval = newProvQName("qualifiedRemoval");
	QNAME_PROVO_hadDictionaryMember = newProvQName("hadDictionaryMember");
	QNAME_PROVO_insertedKeyEntityPair = newProvQName("insertedKeyEntityPair");
	QNAME_PROVO_removedKey = newProvQName("removedKey");
	QNAME_PROVO_KeyValuePair = newProvQName("KeyValuePair");
	QNAME_PROVO_pairKey = newProvQName("pairKey");
	QNAME_PROVO_pairEntity = newProvQName("pairEntity");

	QNAME_BK_topicIn = newBookQName("topicIn");

	initInfluenceTables();
	initDomainTables();
        initRangeTables();
        initAttributeAsResourceTables();
    }
    public Hashtable<QualifiedName, QualifiedName> qualifiedInfluenceTable = new Hashtable<QualifiedName, QualifiedName>();
    public Hashtable<QualifiedName, QualifiedName> influencerTable = new Hashtable<QualifiedName, QualifiedName>();
    public Hashtable<QualifiedName, QualifiedName> unqualifiedTable = new Hashtable<QualifiedName, QualifiedName>();
    public Hashtable<QualifiedName, QualifiedName> otherTable = new Hashtable<QualifiedName, QualifiedName>();
    public Hashtable<QualifiedName, QualifiedName> convertTable = new Hashtable<QualifiedName, QualifiedName>();

    public Hashtable<QualifiedName, QualifiedName> domains = new Hashtable<QualifiedName, QualifiedName>();
    public Hashtable<QualifiedName, QualifiedName> ranges = new Hashtable<QualifiedName, QualifiedName>();
    
    public Set<QualifiedName> asObjectProperty=new HashSet<QualifiedName>();
    
    public QualifiedName newProvQName(String local) {
	return pFactory.newQualifiedName(NamespacePrefixMapper.PROV_NS,
					 local,
					 NamespacePrefixMapper.PROV_PREFIX);
    }

    public QualifiedName newBookQName(String local) {
	return pFactory.newQualifiedName(NamespacePrefixMapper.BOOK_NS,
					 local,
					 NamespacePrefixMapper.BOOK_PREFIX);
    }

    public QualifiedName newRdfQName(String local) {
	return pFactory.newQualifiedName(NamespacePrefixMapper.RDF_NS,
					 local,
					 NamespacePrefixMapper.RDF_PREFIX);
    }
    
    public QualifiedName newRdfsQName(String local) {
	return pFactory.newQualifiedName(NamespacePrefixMapper.RDFS_NS,
					 local,
					 NamespacePrefixMapper.RDFS_PREFIX);
    }

    final public QualifiedName QNAME_PROVO_atLocation;
    final public QualifiedName QNAME_PROVO_atTime;
    final public QualifiedName QNAME_PROVO_startedAtTime;
    final public QualifiedName QNAME_PROVO_endedAtTime;
    final public QualifiedName QNAME_PROVO_influencer;
    final public QualifiedName QNAME_PROVO_activity;
    final public QualifiedName QNAME_PROVO_entity;
    final public QualifiedName QNAME_PROVO_agent;
    final public QualifiedName QNAME_PROVO_hadActivity;
    final public QualifiedName QNAME_PROVO_hadEntity;
    final public QualifiedName QNAME_PROVO_hadPlan;
    final public QualifiedName QNAME_PROVO_hadGeneration;
    final public QualifiedName QNAME_PROVO_hadUsage;
    final public QualifiedName QNAME_PROVO_hadRole;
    final public QualifiedName QNAME_PROVO_value;
    final public QualifiedName QNAME_PROVO_generated;
    final public QualifiedName QNAME_PROVO_generatedAtTime;
    final public QualifiedName QNAME_PROVO_influenced;
    final public QualifiedName QNAME_PROVO_invalidated;
    final public QualifiedName QNAME_PROVO_invalidatedAtTime;

    final public QualifiedName QNAME_PROVO_Activity;
    final public QualifiedName QNAME_PROVO_Entity;
    final public QualifiedName QNAME_PROVO_Agent;

    final public QualifiedName QNAME_PROVO_Influence;
    final public QualifiedName QNAME_PROVO_qualifiedInfluence;
    final public QualifiedName QNAME_PROVO_wasInfluencedBy;

    final public QualifiedName QNAME_PROVO_Generation;
    final public QualifiedName QNAME_PROVO_qualifiedGeneration;
    final public QualifiedName QNAME_PROVO_wasGeneratedBy;

    final public QualifiedName QNAME_PROVO_Usage;
    final public QualifiedName QNAME_PROVO_qualifiedUsage;
    final public QualifiedName QNAME_PROVO_used;

    final public QualifiedName QNAME_PROVO_Invalidation;
    final public QualifiedName QNAME_PROVO_qualifiedInvalidation;
    final public QualifiedName QNAME_PROVO_wasInvalidatedBy;

    final public QualifiedName QNAME_PROVO_Start;
    final public QualifiedName QNAME_PROVO_qualifiedStart;
    final public QualifiedName QNAME_PROVO_wasStartedBy;

    final public QualifiedName QNAME_PROVO_End;
    final public QualifiedName QNAME_PROVO_qualifiedEnd;
    final public QualifiedName QNAME_PROVO_wasEndedBy;

    final public QualifiedName QNAME_PROVO_Association;
    final public QualifiedName QNAME_PROVO_qualifiedAssociation;
    final public QualifiedName QNAME_PROVO_wasAssociatedWith;

    final public QualifiedName QNAME_PROVO_Attribution;
    final public QualifiedName QNAME_PROVO_qualifiedAttribution;
    final public QualifiedName QNAME_PROVO_wasAttributedTo;

    final public QualifiedName QNAME_PROVO_Delegation;
    final public QualifiedName QNAME_PROVO_qualifiedDelegation;
    final public QualifiedName QNAME_PROVO_actedOnBehalfOf;

    final public QualifiedName QNAME_PROVO_Derivation;
    final public QualifiedName QNAME_PROVO_qualifiedDerivation;
    final public QualifiedName QNAME_PROVO_wasDerivedFrom;

    final public QualifiedName QNAME_PROVO_Revision;
    final public QualifiedName QNAME_PROVO_qualifiedRevision;
    final public QualifiedName QNAME_PROVO_wasRevisionOf;

    final public QualifiedName QNAME_PROVO_Quotation;
    final public QualifiedName QNAME_PROVO_qualifiedQuotation;
    final public QualifiedName QNAME_PROVO_wasQuotedFrom;
    
    final public QualifiedName QNAME_PROVO_PrimarySource;
    final public QualifiedName QNAME_PROVO_qualifiedPrimarySource;
    final public QualifiedName QNAME_PROVO_hadPrimarySource;

    final public QualifiedName QNAME_PROVO_Communication;
    final public QualifiedName QNAME_PROVO_qualifiedCommunication;
    final public QualifiedName QNAME_PROVO_wasInformedBy;

    final public QualifiedName QNAME_PROVO_specializationOf;
    final public QualifiedName QNAME_PROVO_alternateOf;
    final public QualifiedName QNAME_PROVO_mentionOf;
    final public QualifiedName QNAME_PROVO_asInBundle;
    final public QualifiedName QNAME_PROVO_hadMember;


    final public QualifiedName QNAME_PROVO_Bundle;
    final public QualifiedName QNAME_PROVO_Organization;
    final public QualifiedName QNAME_PROVO_Person;
    final public QualifiedName QNAME_PROVO_SoftwareAgent;
    final public QualifiedName QNAME_PROVO_Location;
    final public QualifiedName QNAME_PROVO_Plan;
    final public QualifiedName QNAME_PROVO_Role;
    final public QualifiedName QNAME_PROVO_Collection;
    final public QualifiedName QNAME_PROVO_EmptyCollection;

    final public QualifiedName QNAME_PROVO_InstantaneousEvent;
    final public QualifiedName QNAME_PROVO_EntityInfluence;
    final public QualifiedName QNAME_PROVO_ActivityInfluence;
    final public QualifiedName QNAME_PROVO_AgentInfluence;
    
    final public QualifiedName QNAME_PROVDC_Contributor;

    
    final public QualifiedName QNAME_RDF_TYPE;
    final public QualifiedName QNAME_RDFS_LABEL;
    
    // dictionary stuff
    
    final public QualifiedName QNAME_PROVO_Dictionary;
    final public QualifiedName QNAME_PROVO_EmptyDictionary;
    final public QualifiedName QNAME_PROVO_derivedByInsertion;
    final public QualifiedName QNAME_PROVO_Insertion;
    final public QualifiedName QNAME_PROVO_qualifiedInsertion;
    final public QualifiedName QNAME_PROVO_dictionary;
    final public QualifiedName QNAME_PROVO_derivedByRemoval;
    final public QualifiedName QNAME_PROVO_Removal;
    final public QualifiedName QNAME_PROVO_qualifiedRemoval;
    final public QualifiedName QNAME_PROVO_hadDictionaryMember;
    final public QualifiedName QNAME_PROVO_insertedKeyEntityPair;
    final public QualifiedName QNAME_PROVO_removedKey;
    final public QualifiedName QNAME_PROVO_KeyValuePair;
    final public QualifiedName QNAME_PROVO_pairKey;
    final public QualifiedName QNAME_PROVO_pairEntity;

    // prov book

    final public QualifiedName QNAME_BK_topicIn;


	
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

	convertTable.put(name.PROV_LABEL, QNAME_RDFS_LABEL);
	convertTable.put(name.PROV_TYPE, QNAME_RDF_TYPE);
	convertTable.put(name.PROV_LOCATION, QNAME_PROVO_atLocation);
	convertTable.put(name.PROV_VALUE, QNAME_PROVO_value);
	convertTable.put(name.PROV_ROLE, QNAME_PROVO_hadRole);
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

    void activityInfluence(QualifiedName name) {
	influencerTable.put(name, QNAME_PROVO_activity);
    }

    void entityInfluence(QualifiedName name) {
    	influencerTable.put(name, QNAME_PROVO_entity);
        }
    void dictionaryInfluence(QualifiedName name) {
    	influencerTable.put(name, QNAME_PROVO_dictionary);
        }

    void agentInfluence(QualifiedName name) {
	influencerTable.put(name, QNAME_PROVO_agent);
    }
    
    

    public QualifiedName convertToRdf(QualifiedName qname) {
	QualifiedName res = convertTable.get(qname);
	if (res != null)
	    return res;
	return qname;
    }
    
    
    public QualifiedName convertFromRdf(QualifiedName qname) {
	if (NamespacePrefixMapper.XSD_HASH_NS.equals(qname.getNamespaceURI())) {
	    return pFactory.newQualifiedName(NamespacePrefixMapper.XSD_NS,
	                                     qname.getLocalPart(),
	                                     qname.getPrefix());
	} else {
	    return qname;
	}
    }
}
