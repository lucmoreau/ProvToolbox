package org.openprovenance.prov.rdf;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;


import org.openprovenance.prov.model.Name;
import org.openprovenance.prov.model.QualifiedName;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.xml.NamespacePrefixMapper;


public class Ontology {

    final private ProvFactory pFactory;

    public Ontology(ProvFactory pFactory) {
	System.out.println("In ontology " + pFactory);
	this.pFactory=pFactory;
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
	System.out.println("newProvQNAME " + pFactory);
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

    public QualifiedName QNAME_PROVO_atLocation = newProvQName("atLocation");
    public QualifiedName QNAME_PROVO_atTime = newProvQName("atTime");
    public QualifiedName QNAME_PROVO_startedAtTime = newProvQName("startedAtTime");
    public QualifiedName QNAME_PROVO_endedAtTime = newProvQName("endedAtTime");
    public QualifiedName QNAME_PROVO_influencer = newProvQName("influencer");
    public QualifiedName QNAME_PROVO_activity = newProvQName("activity");
    public QualifiedName QNAME_PROVO_entity = newProvQName("entity");
    public QualifiedName QNAME_PROVO_agent = newProvQName("agent");
    public QualifiedName QNAME_PROVO_hadActivity = newProvQName("hadActivity");
    public QualifiedName QNAME_PROVO_hadEntity = newProvQName("hadEntity");
    public QualifiedName QNAME_PROVO_hadPlan = newProvQName("hadPlan");
    public QualifiedName QNAME_PROVO_hadGeneration = newProvQName("hadGeneration");
    public QualifiedName QNAME_PROVO_hadUsage = newProvQName("hadUsage");
    public QualifiedName QNAME_PROVO_hadRole = newProvQName("hadRole");
    public QualifiedName QNAME_PROVO_value = newProvQName("value");
    public QualifiedName QNAME_PROVO_generated = newProvQName("generated");
    
    public QualifiedName QNAME_PROVO_generatedAtTime = newProvQName("generatedAtTime");
    public QualifiedName QNAME_PROVO_influenced = newProvQName("influenced");
    public QualifiedName QNAME_PROVO_invalidated = newProvQName("invalidated");
    public QualifiedName QNAME_PROVO_invalidatedAtTime = newProvQName("invalidatedAtTime");

    public QualifiedName QNAME_PROVO_Activity = newProvQName("Activity");
    public QualifiedName QNAME_PROVO_Entity = newProvQName("Entity");
    public QualifiedName QNAME_PROVO_Agent = newProvQName("Agent");

    public QualifiedName QNAME_PROVO_Influence = newProvQName("Influence");
    public QualifiedName QNAME_PROVO_qualifiedInfluence = newProvQName("qualifiedInfluence");
    public QualifiedName QNAME_PROVO_wasInfluencedBy = newProvQName("wasInfluencedBy");

    public QualifiedName QNAME_PROVO_Generation = newProvQName("Generation");
    public QualifiedName QNAME_PROVO_qualifiedGeneration = newProvQName("qualifiedGeneration");
    public QualifiedName QNAME_PROVO_wasGeneratedBy = newProvQName("wasGeneratedBy");

    public QualifiedName QNAME_PROVO_Usage = newProvQName("Usage");
    public QualifiedName QNAME_PROVO_qualifiedUsage = newProvQName("qualifiedUsage");
    public QualifiedName QNAME_PROVO_used = newProvQName("used");

    public QualifiedName QNAME_PROVO_Invalidation = newProvQName("Invalidation");
    public QualifiedName QNAME_PROVO_qualifiedInvalidation = newProvQName("qualifiedInvalidation");
    public QualifiedName QNAME_PROVO_wasInvalidatedBy = newProvQName("wasInvalidatedBy");

    public QualifiedName QNAME_PROVO_Start = newProvQName("Start");
    public QualifiedName QNAME_PROVO_qualifiedStart = newProvQName("qualifiedStart");
    public QualifiedName QNAME_PROVO_wasStartedBy = newProvQName("wasStartedBy");

    public QualifiedName QNAME_PROVO_End = newProvQName("End");
    public QualifiedName QNAME_PROVO_qualifiedEnd = newProvQName("qualifiedEnd");
    public QualifiedName QNAME_PROVO_wasEndedBy = newProvQName("wasEndedBy");

    public QualifiedName QNAME_PROVO_Association = newProvQName("Association");
    public QualifiedName QNAME_PROVO_qualifiedAssociation = newProvQName("qualifiedAssociation");
    public QualifiedName QNAME_PROVO_wasAssociatedWith = newProvQName("wasAssociatedWith");

    public QualifiedName QNAME_PROVO_Attribution = newProvQName("Attribution");
    public QualifiedName QNAME_PROVO_qualifiedAttribution = newProvQName("qualifiedAttribution");
    public QualifiedName QNAME_PROVO_wasAttributedTo = newProvQName("wasAttributedTo");

    public QualifiedName QNAME_PROVO_Delegation = newProvQName("Delegation");
    public QualifiedName QNAME_PROVO_qualifiedDelegation = newProvQName("qualifiedDelegation");
    public QualifiedName QNAME_PROVO_actedOnBehalfOf = newProvQName("actedOnBehalfOf");

    public QualifiedName QNAME_PROVO_Derivation = newProvQName("Derivation");
    public QualifiedName QNAME_PROVO_qualifiedDerivation = newProvQName("qualifiedDerivation");
    public QualifiedName QNAME_PROVO_wasDerivedFrom = newProvQName("wasDerivedFrom");

    public QualifiedName QNAME_PROVO_Revision = newProvQName("Revision");
    public QualifiedName QNAME_PROVO_qualifiedRevision = newProvQName("qualifiedRevision");
    public QualifiedName QNAME_PROVO_wasRevisionOf = newProvQName("wasRevisionOf");

    public QualifiedName QNAME_PROVO_Quotation = newProvQName("Quotation");
    public QualifiedName QNAME_PROVO_qualifiedQuotation = newProvQName("qualifiedQuotation");
    public QualifiedName QNAME_PROVO_wasQuotedFrom = newProvQName("wasQuotedFrom");
    
    public QualifiedName QNAME_PROVO_PrimarySource = newProvQName("PrimarySource");
    public QualifiedName QNAME_PROVO_qualifiedPrimarySource = newProvQName("qualifiedPrimarySource");
    public QualifiedName QNAME_PROVO_hadPrimarySource = newProvQName("hadPrimarySource");

    public QualifiedName QNAME_PROVO_Communication = newProvQName("Communication");
    public QualifiedName QNAME_PROVO_qualifiedCommunication = newProvQName("qualifiedCommunication");
    public QualifiedName QNAME_PROVO_wasInformedBy = newProvQName("wasInformedBy");

    public QualifiedName QNAME_PROVO_specializationOf = newProvQName("specializationOf");
    public QualifiedName QNAME_PROVO_alternateOf = newProvQName("alternateOf");
    public QualifiedName QNAME_PROVO_mentionOf = newProvQName("mentionOf");
    public QualifiedName QNAME_PROVO_asInBundle = newProvQName("asInBundle");
    public QualifiedName QNAME_PROVO_hadMember = newProvQName("hadMember");


    public QualifiedName QNAME_PROVO_Bundle = newProvQName("Bundle");
    public QualifiedName QNAME_PROVO_Organization = newProvQName("Organization");
    public QualifiedName QNAME_PROVO_Person = newProvQName("Person");
    public QualifiedName QNAME_PROVO_SoftwareAgent = newProvQName("SoftwareAgent");
    public QualifiedName QNAME_PROVO_Location = newProvQName("Location");
    public QualifiedName QNAME_PROVO_Plan = newProvQName("Plan");
    public QualifiedName QNAME_PROVO_Role = newProvQName("Role");
    public QualifiedName QNAME_PROVO_Collection = newProvQName("Collection");
    public QualifiedName QNAME_PROVO_EmptyCollection = newProvQName("EmptyCollection");

    public QualifiedName QNAME_PROVO_InstantaneousEvent = newProvQName("InstantaneousEvent");
    public QualifiedName QNAME_PROVO_EntityInfluence = newProvQName("EntityInfluence");
    public QualifiedName QNAME_PROVO_ActivityInfluence = newProvQName("ActivityInfluence");
    public QualifiedName QNAME_PROVO_AgentInfluence = newProvQName("AgentInfluence");
    
    public QualifiedName QNAME_PROVDC_Contributor = newProvQName("Contributor");

    
    public QualifiedName QNAME_RDF_TYPE = newRdfQName("type");
    public QualifiedName QNAME_RDFS_LABEL = newRdfsQName("label");
    
    
    // dictionary stuff
    
    public QualifiedName QNAME_PROVO_Dictionary = newProvQName("Dictionary");
    public QualifiedName QNAME_PROVO_EmptyDictionary = newProvQName("EmptyDictionary");
    public QualifiedName QNAME_PROVO_derivedByInsertion = newProvQName("derivedByInsertion");
    public QualifiedName QNAME_PROVO_Insertion = newProvQName("Insertion");
    public QualifiedName QNAME_PROVO_qualifiedInsertion = newProvQName("qualifiedInsertion");
    public QualifiedName QNAME_PROVO_dictionary = newProvQName("dictionary");
    public QualifiedName QNAME_PROVO_derivedByRemoval = newProvQName("derivedByRemoval");
    public QualifiedName QNAME_PROVO_Removal = newProvQName("Removal");
    public QualifiedName QNAME_PROVO_qualifiedRemoval = newProvQName("qualifiedRemoval");
    public QualifiedName QNAME_PROVO_hadDictionaryMember = newProvQName("hadDictionaryMember");
	public QualifiedName QNAME_PROVO_insertedKeyEntityPair = newProvQName("insertedKeyEntityPair");
	public QualifiedName QNAME_PROVO_removedKey = newProvQName("removedKey");
	public QualifiedName QNAME_PROVO_KeyValuePair = newProvQName("KeyValuePair");
	public QualifiedName QNAME_PROVO_pairKey = newProvQName("pairKey");
	public QualifiedName QNAME_PROVO_pairEntity = newProvQName("pairEntity");

    // prov book

    public QualifiedName QNAME_BK_topicIn = newBookQName("topicIn");


	
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

	convertTable.put(pFactory.newQualifiedName(Name.PROV_LABEL_QNAME), QNAME_RDFS_LABEL);
	convertTable.put(pFactory.newQualifiedName(Name.PROV_TYPE_QNAME), QNAME_RDF_TYPE);
	convertTable.put(pFactory.newQualifiedName(Name.PROV_LOCATION_QNAME), QNAME_PROVO_atLocation);
	convertTable.put(pFactory.newQualifiedName(Name.PROV_VALUE_QNAME), QNAME_PROVO_value);
	convertTable.put(pFactory.newQualifiedName(Name.PROV_ROLE_QNAME), QNAME_PROVO_hadRole);
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
