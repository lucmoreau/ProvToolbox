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
	
	QualifiedName_PROVO_atLocation = name.newProvQualifiedName("atLocation");
	QualifiedName_PROVO_atTime = name.newProvQualifiedName("atTime");
	QualifiedName_PROVO_startedAtTime = name.newProvQualifiedName("startedAtTime");
	QualifiedName_PROVO_endedAtTime = name.newProvQualifiedName("endedAtTime");
	QualifiedName_PROVO_influencer = name.newProvQualifiedName("influencer");
	QualifiedName_PROVO_activity = name.newProvQualifiedName("activity");
	QualifiedName_PROVO_entity = name.newProvQualifiedName("entity");
	QualifiedName_PROVO_agent = name.newProvQualifiedName("agent");
	QualifiedName_PROVO_hadActivity = name.newProvQualifiedName("hadActivity");
	QualifiedName_PROVO_hadEntity = name.newProvQualifiedName("hadEntity");
	QualifiedName_PROVO_hadPlan = name.newProvQualifiedName("hadPlan");
	QualifiedName_PROVO_hadGeneration = name.newProvQualifiedName("hadGeneration");
	QualifiedName_PROVO_hadUsage = name.newProvQualifiedName("hadUsage");
	QualifiedName_PROVO_hadRole = name.newProvQualifiedName("hadRole");
	QualifiedName_PROVO_value = name.newProvQualifiedName("value");
	QualifiedName_PROVO_generated = name.newProvQualifiedName("generated");
    
	QualifiedName_PROVO_generatedAtTime = name.newProvQualifiedName("generatedAtTime");
	QualifiedName_PROVO_influenced = name.newProvQualifiedName("influenced");
	QualifiedName_PROVO_invalidated = name.newProvQualifiedName("invalidated");
	QualifiedName_PROVO_invalidatedAtTime = name.newProvQualifiedName("invalidatedAtTime");

	QualifiedName_PROVO_Activity = name.newProvQualifiedName("Activity");
	QualifiedName_PROVO_Entity = name.newProvQualifiedName("Entity");
	QualifiedName_PROVO_Agent = name.newProvQualifiedName("Agent");

	QualifiedName_PROVO_Influence = name.newProvQualifiedName("Influence");
	QualifiedName_PROVO_qualifiedInfluence = name.newProvQualifiedName("qualifiedInfluence");
	QualifiedName_PROVO_wasInfluencedBy = name.newProvQualifiedName("wasInfluencedBy");

	QualifiedName_PROVO_Generation = name.newProvQualifiedName("Generation");
	QualifiedName_PROVO_qualifiedGeneration = name.newProvQualifiedName("qualifiedGeneration");
	QualifiedName_PROVO_wasGeneratedBy = name.newProvQualifiedName("wasGeneratedBy");

	QualifiedName_PROVO_Usage = name.newProvQualifiedName("Usage");
	QualifiedName_PROVO_qualifiedUsage = name.newProvQualifiedName("qualifiedUsage");
	QualifiedName_PROVO_used = name.newProvQualifiedName("used");

	QualifiedName_PROVO_Invalidation = name.newProvQualifiedName("Invalidation");
	QualifiedName_PROVO_qualifiedInvalidation = name.newProvQualifiedName("qualifiedInvalidation");
	QualifiedName_PROVO_wasInvalidatedBy = name.newProvQualifiedName("wasInvalidatedBy");

	QualifiedName_PROVO_Start = name.newProvQualifiedName("Start");
	QualifiedName_PROVO_qualifiedStart = name.newProvQualifiedName("qualifiedStart");
	QualifiedName_PROVO_wasStartedBy = name.newProvQualifiedName("wasStartedBy");

	QualifiedName_PROVO_End = name.newProvQualifiedName("End");
	QualifiedName_PROVO_qualifiedEnd = name.newProvQualifiedName("qualifiedEnd");
	QualifiedName_PROVO_wasEndedBy = name.newProvQualifiedName("wasEndedBy");

	QualifiedName_PROVO_Association = name.newProvQualifiedName("Association");
	QualifiedName_PROVO_qualifiedAssociation = name.newProvQualifiedName("qualifiedAssociation");
	QualifiedName_PROVO_wasAssociatedWith = name.newProvQualifiedName("wasAssociatedWith");

	QualifiedName_PROVO_Attribution = name.newProvQualifiedName("Attribution");
	QualifiedName_PROVO_qualifiedAttribution = name.newProvQualifiedName("qualifiedAttribution");
	QualifiedName_PROVO_wasAttributedTo = name.newProvQualifiedName("wasAttributedTo");

	QualifiedName_PROVO_Delegation = name.newProvQualifiedName("Delegation");
	QualifiedName_PROVO_qualifiedDelegation = name.newProvQualifiedName("qualifiedDelegation");
	QualifiedName_PROVO_actedOnBehalfOf = name.newProvQualifiedName("actedOnBehalfOf");

	QualifiedName_PROVO_Derivation = name.newProvQualifiedName("Derivation");
	QualifiedName_PROVO_qualifiedDerivation = name.newProvQualifiedName("qualifiedDerivation");
	QualifiedName_PROVO_wasDerivedFrom = name.newProvQualifiedName("wasDerivedFrom");

	QualifiedName_PROVO_Revision = name.newProvQualifiedName("Revision");
	QualifiedName_PROVO_qualifiedRevision = name.newProvQualifiedName("qualifiedRevision");
	QualifiedName_PROVO_wasRevisionOf = name.newProvQualifiedName("wasRevisionOf");

	QualifiedName_PROVO_Quotation = name.newProvQualifiedName("Quotation");
	QualifiedName_PROVO_qualifiedQuotation = name.newProvQualifiedName("qualifiedQuotation");
	QualifiedName_PROVO_wasQuotedFrom = name.newProvQualifiedName("wasQuotedFrom");
    
	QualifiedName_PROVO_PrimarySource = name.newProvQualifiedName("PrimarySource");
	QualifiedName_PROVO_qualifiedPrimarySource = name.newProvQualifiedName("qualifiedPrimarySource");
	QualifiedName_PROVO_hadPrimarySource = name.newProvQualifiedName("hadPrimarySource");

	QualifiedName_PROVO_Communication = name.newProvQualifiedName("Communication");
	QualifiedName_PROVO_qualifiedCommunication = name.newProvQualifiedName("qualifiedCommunication");
	QualifiedName_PROVO_wasInformedBy = name.newProvQualifiedName("wasInformedBy");

	QualifiedName_PROVO_specializationOf = name.newProvQualifiedName("specializationOf");
	QualifiedName_PROVO_alternateOf = name.newProvQualifiedName("alternateOf");
	QualifiedName_PROVO_mentionOf = name.newProvQualifiedName("mentionOf");
	QualifiedName_PROVO_asInBundle = name.newProvQualifiedName("asInBundle");
	QualifiedName_PROVO_hadMember = name.newProvQualifiedName("hadMember");


	QualifiedName_PROVO_Bundle = name.newProvQualifiedName("Bundle");
	QualifiedName_PROVO_Organization = name.newProvQualifiedName("Organization");
	QualifiedName_PROVO_Person = name.newProvQualifiedName("Person");
	QualifiedName_PROVO_SoftwareAgent = name.newProvQualifiedName("SoftwareAgent");
	QualifiedName_PROVO_Location = name.newProvQualifiedName("Location");
	QualifiedName_PROVO_Plan = name.newProvQualifiedName("Plan");
	QualifiedName_PROVO_Role = name.newProvQualifiedName("Role");
	QualifiedName_PROVO_Collection = name.newProvQualifiedName("Collection");
	QualifiedName_PROVO_EmptyCollection = name.newProvQualifiedName("EmptyCollection");

	QualifiedName_PROVO_InstantaneousEvent = name.newProvQualifiedName("InstantaneousEvent");
	QualifiedName_PROVO_EntityInfluence = name.newProvQualifiedName("EntityInfluence");
	QualifiedName_PROVO_ActivityInfluence = name.newProvQualifiedName("ActivityInfluence");
	QualifiedName_PROVO_AgentInfluence = name.newProvQualifiedName("AgentInfluence");
    
	QualifiedName_PROVDC_Contributor = name.newProvQualifiedName("Contributor");

    
	QualifiedName_RDF_TYPE = newRdfQualifiedName("type");
	QualifiedName_RDFS_LABEL = newRdfsQualifiedName("label");
    
    
    
    
	QualifiedName_PROVO_Dictionary = name.newProvQualifiedName("Dictionary");
	QualifiedName_PROVO_EmptyDictionary = name.newProvQualifiedName("EmptyDictionary");
	QualifiedName_PROVO_derivedByInsertion = name.newProvQualifiedName("derivedByInsertion");
	QualifiedName_PROVO_Insertion = name.newProvQualifiedName("Insertion");
	QualifiedName_PROVO_qualifiedInsertion = name.newProvQualifiedName("qualifiedInsertion");
	QualifiedName_PROVO_dictionary = name.newProvQualifiedName("dictionary");
	QualifiedName_PROVO_derivedByRemoval = name.newProvQualifiedName("derivedByRemoval");
	QualifiedName_PROVO_Removal = name.newProvQualifiedName("Removal");
	QualifiedName_PROVO_qualifiedRemoval = name.newProvQualifiedName("qualifiedRemoval");
	QualifiedName_PROVO_hadDictionaryMember = name.newProvQualifiedName("hadDictionaryMember");
	QualifiedName_PROVO_insertedKeyEntityPair = name.newProvQualifiedName("insertedKeyEntityPair");
	QualifiedName_PROVO_removedKey = name.newProvQualifiedName("removedKey");
	QualifiedName_PROVO_KeyValuePair = name.newProvQualifiedName("KeyValuePair");
	QualifiedName_PROVO_pairKey = name.newProvQualifiedName("pairKey");
	QualifiedName_PROVO_pairEntity = name.newProvQualifiedName("pairEntity");

	QualifiedName_BK_topicIn = newBookQualifiedName("topicIn");

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
    
    public QualifiedName newBookQualifiedName(String local) {
	return pFactory.newQualifiedName(NamespacePrefixMapper.BOOK_NS,
					 local,
					 NamespacePrefixMapper.BOOK_PREFIX);
    }

    public QualifiedName newRdfQualifiedName(String local) {
	return pFactory.newQualifiedName(NamespacePrefixMapper.RDF_NS,
					 local,
					 NamespacePrefixMapper.RDF_PREFIX);
    }
    
    public QualifiedName newRdfsQualifiedName(String local) {
	return pFactory.newQualifiedName(NamespacePrefixMapper.RDFS_NS,
					 local,
					 NamespacePrefixMapper.RDFS_PREFIX);
    }

    final public QualifiedName QualifiedName_PROVO_atLocation;
    final public QualifiedName QualifiedName_PROVO_atTime;
    final public QualifiedName QualifiedName_PROVO_startedAtTime;
    final public QualifiedName QualifiedName_PROVO_endedAtTime;
    final public QualifiedName QualifiedName_PROVO_influencer;
    final public QualifiedName QualifiedName_PROVO_activity;
    final public QualifiedName QualifiedName_PROVO_entity;
    final public QualifiedName QualifiedName_PROVO_agent;
    final public QualifiedName QualifiedName_PROVO_hadActivity;
    final public QualifiedName QualifiedName_PROVO_hadEntity;
    final public QualifiedName QualifiedName_PROVO_hadPlan;
    final public QualifiedName QualifiedName_PROVO_hadGeneration;
    final public QualifiedName QualifiedName_PROVO_hadUsage;
    final public QualifiedName QualifiedName_PROVO_hadRole;
    final public QualifiedName QualifiedName_PROVO_value;
    final public QualifiedName QualifiedName_PROVO_generated;
    final public QualifiedName QualifiedName_PROVO_generatedAtTime;
    final public QualifiedName QualifiedName_PROVO_influenced;
    final public QualifiedName QualifiedName_PROVO_invalidated;
    final public QualifiedName QualifiedName_PROVO_invalidatedAtTime;

    final public QualifiedName QualifiedName_PROVO_Activity;
    final public QualifiedName QualifiedName_PROVO_Entity;
    final public QualifiedName QualifiedName_PROVO_Agent;

    final public QualifiedName QualifiedName_PROVO_Influence;
    final public QualifiedName QualifiedName_PROVO_qualifiedInfluence;
    final public QualifiedName QualifiedName_PROVO_wasInfluencedBy;

    final public QualifiedName QualifiedName_PROVO_Generation;
    final public QualifiedName QualifiedName_PROVO_qualifiedGeneration;
    final public QualifiedName QualifiedName_PROVO_wasGeneratedBy;

    final public QualifiedName QualifiedName_PROVO_Usage;
    final public QualifiedName QualifiedName_PROVO_qualifiedUsage;
    final public QualifiedName QualifiedName_PROVO_used;

    final public QualifiedName QualifiedName_PROVO_Invalidation;
    final public QualifiedName QualifiedName_PROVO_qualifiedInvalidation;
    final public QualifiedName QualifiedName_PROVO_wasInvalidatedBy;

    final public QualifiedName QualifiedName_PROVO_Start;
    final public QualifiedName QualifiedName_PROVO_qualifiedStart;
    final public QualifiedName QualifiedName_PROVO_wasStartedBy;

    final public QualifiedName QualifiedName_PROVO_End;
    final public QualifiedName QualifiedName_PROVO_qualifiedEnd;
    final public QualifiedName QualifiedName_PROVO_wasEndedBy;

    final public QualifiedName QualifiedName_PROVO_Association;
    final public QualifiedName QualifiedName_PROVO_qualifiedAssociation;
    final public QualifiedName QualifiedName_PROVO_wasAssociatedWith;

    final public QualifiedName QualifiedName_PROVO_Attribution;
    final public QualifiedName QualifiedName_PROVO_qualifiedAttribution;
    final public QualifiedName QualifiedName_PROVO_wasAttributedTo;

    final public QualifiedName QualifiedName_PROVO_Delegation;
    final public QualifiedName QualifiedName_PROVO_qualifiedDelegation;
    final public QualifiedName QualifiedName_PROVO_actedOnBehalfOf;

    final public QualifiedName QualifiedName_PROVO_Derivation;
    final public QualifiedName QualifiedName_PROVO_qualifiedDerivation;
    final public QualifiedName QualifiedName_PROVO_wasDerivedFrom;

    final public QualifiedName QualifiedName_PROVO_Revision;
    final public QualifiedName QualifiedName_PROVO_qualifiedRevision;
    final public QualifiedName QualifiedName_PROVO_wasRevisionOf;

    final public QualifiedName QualifiedName_PROVO_Quotation;
    final public QualifiedName QualifiedName_PROVO_qualifiedQuotation;
    final public QualifiedName QualifiedName_PROVO_wasQuotedFrom;
    
    final public QualifiedName QualifiedName_PROVO_PrimarySource;
    final public QualifiedName QualifiedName_PROVO_qualifiedPrimarySource;
    final public QualifiedName QualifiedName_PROVO_hadPrimarySource;

    final public QualifiedName QualifiedName_PROVO_Communication;
    final public QualifiedName QualifiedName_PROVO_qualifiedCommunication;
    final public QualifiedName QualifiedName_PROVO_wasInformedBy;

    final public QualifiedName QualifiedName_PROVO_specializationOf;
    final public QualifiedName QualifiedName_PROVO_alternateOf;
    final public QualifiedName QualifiedName_PROVO_mentionOf;
    final public QualifiedName QualifiedName_PROVO_asInBundle;
    final public QualifiedName QualifiedName_PROVO_hadMember;


    final public QualifiedName QualifiedName_PROVO_Bundle;
    final public QualifiedName QualifiedName_PROVO_Organization;
    final public QualifiedName QualifiedName_PROVO_Person;
    final public QualifiedName QualifiedName_PROVO_SoftwareAgent;
    final public QualifiedName QualifiedName_PROVO_Location;
    final public QualifiedName QualifiedName_PROVO_Plan;
    final public QualifiedName QualifiedName_PROVO_Role;
    final public QualifiedName QualifiedName_PROVO_Collection;
    final public QualifiedName QualifiedName_PROVO_EmptyCollection;

    final public QualifiedName QualifiedName_PROVO_InstantaneousEvent;
    final public QualifiedName QualifiedName_PROVO_EntityInfluence;
    final public QualifiedName QualifiedName_PROVO_ActivityInfluence;
    final public QualifiedName QualifiedName_PROVO_AgentInfluence;
    
    final public QualifiedName QualifiedName_PROVDC_Contributor;

    
    final public QualifiedName QualifiedName_RDF_TYPE;
    final public QualifiedName QualifiedName_RDFS_LABEL;
    
    // dictionary stuff
    
    final public QualifiedName QualifiedName_PROVO_Dictionary;
    final public QualifiedName QualifiedName_PROVO_EmptyDictionary;
    final public QualifiedName QualifiedName_PROVO_derivedByInsertion;
    final public QualifiedName QualifiedName_PROVO_Insertion;
    final public QualifiedName QualifiedName_PROVO_qualifiedInsertion;
    final public QualifiedName QualifiedName_PROVO_dictionary;
    final public QualifiedName QualifiedName_PROVO_derivedByRemoval;
    final public QualifiedName QualifiedName_PROVO_Removal;
    final public QualifiedName QualifiedName_PROVO_qualifiedRemoval;
    final public QualifiedName QualifiedName_PROVO_hadDictionaryMember;
    final public QualifiedName QualifiedName_PROVO_insertedKeyEntityPair;
    final public QualifiedName QualifiedName_PROVO_removedKey;
    final public QualifiedName QualifiedName_PROVO_KeyValuePair;
    final public QualifiedName QualifiedName_PROVO_pairKey;
    final public QualifiedName QualifiedName_PROVO_pairEntity;

    // prov book

    final public QualifiedName QualifiedName_BK_topicIn;


	
    void initInfluenceTables() {
	qualifiedInfluenceTable.put(QualifiedName_PROVO_Influence,
				    QualifiedName_PROVO_qualifiedInfluence);
	qualifiedInfluenceTable.put(QualifiedName_PROVO_Generation,
				    QualifiedName_PROVO_qualifiedGeneration);
	qualifiedInfluenceTable.put(QualifiedName_PROVO_Usage,
				    QualifiedName_PROVO_qualifiedUsage);
	qualifiedInfluenceTable.put(QualifiedName_PROVO_Invalidation,
				    QualifiedName_PROVO_qualifiedInvalidation);
	qualifiedInfluenceTable.put(QualifiedName_PROVO_Start,
				    QualifiedName_PROVO_qualifiedStart);
	qualifiedInfluenceTable.put(QualifiedName_PROVO_End, QualifiedName_PROVO_qualifiedEnd);
	qualifiedInfluenceTable.put(QualifiedName_PROVO_Association,
				    QualifiedName_PROVO_qualifiedAssociation);
	qualifiedInfluenceTable.put(QualifiedName_PROVO_Attribution,
				    QualifiedName_PROVO_qualifiedAttribution);
	qualifiedInfluenceTable.put(QualifiedName_PROVO_Delegation,
				    QualifiedName_PROVO_qualifiedDelegation);
	qualifiedInfluenceTable.put(QualifiedName_PROVO_Derivation,
				    QualifiedName_PROVO_qualifiedDerivation);
	qualifiedInfluenceTable.put(QualifiedName_PROVO_Quotation,
				    QualifiedName_PROVO_qualifiedQuotation);
	qualifiedInfluenceTable.put(QualifiedName_PROVO_Revision,
				    QualifiedName_PROVO_qualifiedRevision);
	qualifiedInfluenceTable.put(QualifiedName_PROVO_PrimarySource,
				    QualifiedName_PROVO_qualifiedPrimarySource);
	qualifiedInfluenceTable.put(QualifiedName_PROVO_Communication,
		    QualifiedName_PROVO_qualifiedCommunication);
	qualifiedInfluenceTable.put(QualifiedName_PROVO_Insertion,
		    QualifiedName_PROVO_qualifiedInsertion);
	qualifiedInfluenceTable.put(QualifiedName_PROVO_Removal,
		    QualifiedName_PROVO_qualifiedRemoval);

	influencerTable.put(QualifiedName_PROVO_Influence, QualifiedName_PROVO_influencer);
	activityInfluence(QualifiedName_PROVO_Generation);
	entityInfluence(QualifiedName_PROVO_Usage);
	activityInfluence(QualifiedName_PROVO_Invalidation);
	entityInfluence(QualifiedName_PROVO_Start);
	entityInfluence(QualifiedName_PROVO_End);
	agentInfluence(QualifiedName_PROVO_Association);
	agentInfluence(QualifiedName_PROVO_Attribution);
	agentInfluence(QualifiedName_PROVO_Delegation);
	entityInfluence(QualifiedName_PROVO_Derivation);
	entityInfluence(QualifiedName_PROVO_Quotation);
	entityInfluence(QualifiedName_PROVO_Revision);
	entityInfluence(QualifiedName_PROVO_PrimarySource);
	activityInfluence(QualifiedName_PROVO_Communication);
	
	dictionaryInfluence(QualifiedName_PROVO_Insertion);
	dictionaryInfluence(QualifiedName_PROVO_Removal);

	unqualifiedTable.put(QualifiedName_PROVO_Influence, QualifiedName_PROVO_wasInfluencedBy);
	unqualifiedTable.put(QualifiedName_PROVO_Generation, QualifiedName_PROVO_wasGeneratedBy);
	unqualifiedTable.put(QualifiedName_PROVO_Usage, QualifiedName_PROVO_used);
	unqualifiedTable.put(QualifiedName_PROVO_Invalidation,
			     QualifiedName_PROVO_wasInvalidatedBy);
	unqualifiedTable.put(QualifiedName_PROVO_Start, QualifiedName_PROVO_wasStartedBy);
	unqualifiedTable.put(QualifiedName_PROVO_End, QualifiedName_PROVO_wasEndedBy);
	unqualifiedTable.put(QualifiedName_PROVO_Association,
			     QualifiedName_PROVO_wasAssociatedWith);
	unqualifiedTable.put(QualifiedName_PROVO_Attribution,
			     QualifiedName_PROVO_wasAttributedTo);
	unqualifiedTable.put(QualifiedName_PROVO_Delegation,
			     QualifiedName_PROVO_actedOnBehalfOf);
	unqualifiedTable.put(QualifiedName_PROVO_Derivation, QualifiedName_PROVO_wasDerivedFrom);
	unqualifiedTable.put(QualifiedName_PROVO_Revision, QualifiedName_PROVO_wasRevisionOf);
	unqualifiedTable.put(QualifiedName_PROVO_Quotation, QualifiedName_PROVO_wasQuotedFrom);
	unqualifiedTable.put(QualifiedName_PROVO_PrimarySource,
			     QualifiedName_PROVO_hadPrimarySource);
	unqualifiedTable.put(QualifiedName_PROVO_Communication,
		     QualifiedName_PROVO_wasInformedBy);
	unqualifiedTable.put(QualifiedName_PROVO_Insertion,
		     QualifiedName_PROVO_derivedByInsertion);
	unqualifiedTable.put(QualifiedName_PROVO_Removal,
		     QualifiedName_PROVO_derivedByRemoval);

	otherTable.put(QualifiedName_PROVO_Start, QualifiedName_PROVO_hadActivity);
	otherTable.put(QualifiedName_PROVO_End, QualifiedName_PROVO_hadActivity);
	otherTable.put(QualifiedName_PROVO_Derivation, QualifiedName_PROVO_hadActivity);
	otherTable.put(QualifiedName_PROVO_Revision, QualifiedName_PROVO_hadActivity);
	otherTable.put(QualifiedName_PROVO_Quotation, QualifiedName_PROVO_hadActivity);
	otherTable.put(QualifiedName_PROVO_PrimarySource, QualifiedName_PROVO_hadActivity);
	otherTable.put(QualifiedName_PROVO_Association, QualifiedName_PROVO_hadPlan);
	otherTable.put(QualifiedName_PROVO_Delegation, QualifiedName_PROVO_hadActivity);
	otherTable.put(QualifiedName_PROVO_Insertion, QualifiedName_PROVO_insertedKeyEntityPair);
	otherTable.put(QualifiedName_PROVO_Removal, QualifiedName_PROVO_insertedKeyEntityPair);

	convertTable.put(name.PROV_LABEL, QualifiedName_RDFS_LABEL);
	convertTable.put(name.PROV_TYPE, QualifiedName_RDF_TYPE);
	convertTable.put(name.PROV_LOCATION, QualifiedName_PROVO_atLocation);
	convertTable.put(name.PROV_VALUE, QualifiedName_PROVO_value);
	convertTable.put(name.PROV_ROLE, QualifiedName_PROVO_hadRole);
    }
    
    void initRangeTables() {
    	this.ranges.put(QualifiedName_PROVO_actedOnBehalfOf, QualifiedName_PROVO_Agent);
    	this.ranges.put(QualifiedName_PROVO_used, QualifiedName_PROVO_Entity);
    	this.ranges.put(QualifiedName_PROVO_wasAssociatedWith, QualifiedName_PROVO_Agent);
    	this.ranges.put(QualifiedName_PROVO_wasAttributedTo, QualifiedName_PROVO_Agent);
    	this.ranges.put(QualifiedName_PROVO_wasDerivedFrom, QualifiedName_PROVO_Entity);
    	this.ranges.put(QualifiedName_PROVO_wasGeneratedBy, QualifiedName_PROVO_Activity);
    	this.ranges.put(QualifiedName_PROVO_wasInformedBy, QualifiedName_PROVO_Activity);
    	this.ranges.put(QualifiedName_PROVO_alternateOf, QualifiedName_PROVO_Entity);
    	this.ranges.put(QualifiedName_PROVO_atLocation, QualifiedName_PROVO_Location);
    	this.ranges.put(QualifiedName_PROVO_generated, QualifiedName_PROVO_Entity);
    	this.ranges.put(QualifiedName_PROVO_hadMember, QualifiedName_PROVO_Entity);
    	this.ranges.put(QualifiedName_PROVO_hadPrimarySource, QualifiedName_PROVO_Entity);
    	this.ranges.put(QualifiedName_PROVO_invalidated, QualifiedName_PROVO_Entity);
    	this.ranges.put(QualifiedName_PROVO_specializationOf, QualifiedName_PROVO_Entity);
    	this.ranges.put(QualifiedName_PROVO_wasEndedBy, QualifiedName_PROVO_Entity);
    	this.ranges.put(QualifiedName_PROVO_wasInvalidatedBy, QualifiedName_PROVO_Activity);
    	this.ranges.put(QualifiedName_PROVO_wasQuotedFrom, QualifiedName_PROVO_Entity);
    	this.ranges.put(QualifiedName_PROVO_wasRevisionOf, QualifiedName_PROVO_Entity);
    	this.ranges.put(QualifiedName_PROVO_wasStartedBy, QualifiedName_PROVO_Entity);
    	this.ranges.put(QualifiedName_PROVO_activity, QualifiedName_PROVO_Activity);
    	this.ranges.put(QualifiedName_PROVO_agent, QualifiedName_PROVO_Agent);
    	this.ranges.put(QualifiedName_PROVO_entity, QualifiedName_PROVO_Entity);
    	this.ranges.put(QualifiedName_PROVO_hadActivity, QualifiedName_PROVO_Activity);
    	this.ranges.put(QualifiedName_PROVO_hadGeneration, QualifiedName_PROVO_Generation);
    	this.ranges.put(QualifiedName_PROVO_hadPlan, QualifiedName_PROVO_Plan);
    	this.ranges.put(QualifiedName_PROVO_hadUsage, QualifiedName_PROVO_Usage);
    	this.ranges.put(QualifiedName_PROVO_qualifiedAssociation, QualifiedName_PROVO_Association);
    	this.ranges.put(QualifiedName_PROVO_qualifiedAttribution, QualifiedName_PROVO_Attribution);
    	this.ranges.put(QualifiedName_PROVO_qualifiedCommunication, QualifiedName_PROVO_Communication);
    	this.ranges.put(QualifiedName_PROVO_qualifiedDelegation, QualifiedName_PROVO_Delegation);
    	this.ranges.put(QualifiedName_PROVO_qualifiedDerivation, QualifiedName_PROVO_Derivation);
    	this.ranges.put(QualifiedName_PROVO_qualifiedEnd, QualifiedName_PROVO_End);
    	this.ranges.put(QualifiedName_PROVO_qualifiedGeneration, QualifiedName_PROVO_Generation);
    	this.ranges.put(QualifiedName_PROVO_qualifiedInfluence, QualifiedName_PROVO_Influence);
    	this.ranges.put(QualifiedName_PROVO_qualifiedInvalidation, QualifiedName_PROVO_Invalidation);
    	this.ranges.put(QualifiedName_PROVO_qualifiedPrimarySource, QualifiedName_PROVO_PrimarySource);
    	this.ranges.put(QualifiedName_PROVO_qualifiedQuotation, QualifiedName_PROVO_Quotation);
    	this.ranges.put(QualifiedName_PROVO_qualifiedRevision, QualifiedName_PROVO_Revision);
    	this.ranges.put(QualifiedName_PROVO_qualifiedStart, QualifiedName_PROVO_Start);
    	this.ranges.put(QualifiedName_PROVO_qualifiedUsage, QualifiedName_PROVO_Usage);
    	this.ranges.put(QualifiedName_PROVO_qualifiedInsertion, QualifiedName_PROVO_Insertion);
    	this.ranges.put(QualifiedName_PROVO_derivedByInsertion, QualifiedName_PROVO_Dictionary);
    	this.ranges.put(QualifiedName_PROVO_qualifiedRemoval, QualifiedName_PROVO_Removal);
    	this.ranges.put(QualifiedName_PROVO_derivedByRemoval, QualifiedName_PROVO_Dictionary);
    	this.ranges.put(QualifiedName_PROVO_insertedKeyEntityPair, QualifiedName_PROVO_KeyValuePair);
        this.ranges.put(QualifiedName_BK_topicIn, QualifiedName_PROVO_Bundle);

    	
    }
    
    void initDomainTables() {
    	/*
    	 * The domain table maps from predicate to domain. Note that this means
    	 * that it excludes atLocation, influenced, hadRole, hadActivity, qualifiedInfluence, wasInfluencedBy
    	 * It is not possible to infer a single type from those predicates.
    	 */
    	this.domains.put(QualifiedName_PROVO_actedOnBehalfOf, QualifiedName_PROVO_Agent);
    	this.domains.put(QualifiedName_PROVO_qualifiedInfluence, QualifiedName_PROVO_Agent);
    	
    	this.domains.put(QualifiedName_PROVO_agent, QualifiedName_PROVO_AgentInfluence);
    	
    	this.domains.put(QualifiedName_PROVO_startedAtTime, QualifiedName_PROVO_Activity);
    	this.domains.put(QualifiedName_PROVO_endedAtTime, QualifiedName_PROVO_Activity);
    	this.domains.put(QualifiedName_PROVO_used, QualifiedName_PROVO_Activity);
    	this.domains.put(QualifiedName_PROVO_wasAssociatedWith, QualifiedName_PROVO_Activity);
    	this.domains.put(QualifiedName_PROVO_wasInformedBy, QualifiedName_PROVO_Activity);
    	this.domains.put(QualifiedName_PROVO_wasEndedBy, QualifiedName_PROVO_Activity);
    	this.domains.put(QualifiedName_PROVO_wasStartedBy, QualifiedName_PROVO_Activity);
    	this.domains.put(QualifiedName_PROVO_generated, QualifiedName_PROVO_Activity);
    	this.domains.put(QualifiedName_PROVO_invalidated, QualifiedName_PROVO_Activity);
    	this.domains.put(QualifiedName_PROVO_qualifiedAssociation, QualifiedName_PROVO_Activity);
    	this.domains.put(QualifiedName_PROVO_qualifiedCommunication, QualifiedName_PROVO_Activity);
    	this.domains.put(QualifiedName_PROVO_qualifiedEnd, QualifiedName_PROVO_Activity);
    	this.domains.put(QualifiedName_PROVO_qualifiedStart, QualifiedName_PROVO_Activity);
    	this.domains.put(QualifiedName_PROVO_qualifiedUsage, QualifiedName_PROVO_Activity);
    	
    	this.domains.put(QualifiedName_PROVO_activity, QualifiedName_PROVO_ActivityInfluence);
    	
    	this.domains.put(QualifiedName_PROVO_wasAttributedTo, QualifiedName_PROVO_Entity);
    	this.domains.put(QualifiedName_PROVO_wasDerivedFrom, QualifiedName_PROVO_Entity);
    	this.domains.put(QualifiedName_PROVO_wasGeneratedBy, QualifiedName_PROVO_Entity);
    	this.domains.put(QualifiedName_PROVO_alternateOf, QualifiedName_PROVO_Entity);
    	this.domains.put(QualifiedName_PROVO_generatedAtTime, QualifiedName_PROVO_Entity);
    	this.domains.put(QualifiedName_PROVO_hadMember, QualifiedName_PROVO_Collection);
    	this.domains.put(QualifiedName_PROVO_hadPrimarySource, QualifiedName_PROVO_Entity);
    	this.domains.put(QualifiedName_PROVO_invalidatedAtTime, QualifiedName_PROVO_Entity);
    	this.domains.put(QualifiedName_PROVO_wasInvalidatedBy, QualifiedName_PROVO_Entity);
    	this.domains.put(QualifiedName_PROVO_specializationOf, QualifiedName_PROVO_Entity);
    	this.domains.put(QualifiedName_PROVO_wasQuotedFrom, QualifiedName_PROVO_Entity);
    	this.domains.put(QualifiedName_PROVO_wasRevisionOf, QualifiedName_PROVO_Entity);
    	this.domains.put(QualifiedName_PROVO_value, QualifiedName_PROVO_Entity);
    	this.domains.put(QualifiedName_PROVO_qualifiedAttribution, QualifiedName_PROVO_Entity);
    	this.domains.put(QualifiedName_PROVO_qualifiedDerivation, QualifiedName_PROVO_Entity);
    	this.domains.put(QualifiedName_PROVO_qualifiedGeneration, QualifiedName_PROVO_Entity);
    	this.domains.put(QualifiedName_PROVO_qualifiedInvalidation, QualifiedName_PROVO_Entity);
    	this.domains.put(QualifiedName_PROVO_qualifiedPrimarySource, QualifiedName_PROVO_Entity);
    	this.domains.put(QualifiedName_PROVO_qualifiedQuotation, QualifiedName_PROVO_Entity);
    	this.domains.put(QualifiedName_PROVO_qualifiedRevision, QualifiedName_PROVO_Entity);
    	
    	this.domains.put(QualifiedName_PROVO_entity, QualifiedName_PROVO_EntityInfluence);
    	
    	this.domains.put(QualifiedName_PROVO_atTime, QualifiedName_PROVO_InstantaneousEvent);
    	
    	this.domains.put(QualifiedName_PROVO_influencer, QualifiedName_PROVO_Influence);
    	this.domains.put(QualifiedName_PROVO_hadGeneration, QualifiedName_PROVO_Derivation);
    	this.domains.put(QualifiedName_PROVO_hadPlan, QualifiedName_PROVO_Association);
    	this.domains.put(QualifiedName_PROVO_hadUsage, QualifiedName_PROVO_Derivation);
    	
    	
    	this.domains.put(QualifiedName_PROVO_qualifiedInsertion, QualifiedName_PROVO_Dictionary);
    	this.domains.put(QualifiedName_PROVO_derivedByInsertion, QualifiedName_PROVO_Dictionary);
    	this.domains.put(QualifiedName_PROVO_qualifiedRemoval, QualifiedName_PROVO_Dictionary);
    	this.domains.put(QualifiedName_PROVO_derivedByRemoval, QualifiedName_PROVO_Dictionary);
    	this.domains.put(QualifiedName_PROVO_insertedKeyEntityPair, QualifiedName_PROVO_Insertion);
    	
    }
    
    void initAttributeAsResourceTables() {
        asObjectProperty.add(QualifiedName_BK_topicIn);
        asObjectProperty.add(QualifiedName_PROVO_atLocation);
    }

    void activityInfluence(QualifiedName name) {
	influencerTable.put(name, QualifiedName_PROVO_activity);
    }

    void entityInfluence(QualifiedName name) {
    	influencerTable.put(name, QualifiedName_PROVO_entity);
        }
    void dictionaryInfluence(QualifiedName name) {
    	influencerTable.put(name, QualifiedName_PROVO_dictionary);
        }

    void agentInfluence(QualifiedName name) {
	influencerTable.put(name, QualifiedName_PROVO_agent);
    }
    
    

    public QualifiedName convertToRdf(QualifiedName qualifiedName) {
	QualifiedName res = convertTable.get(qualifiedName);
	if (res != null)
	    return res;
	return qualifiedName;
    }
    
      
}
