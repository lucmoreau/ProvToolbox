package org.openprovenance.prov.rdf.collector;

import java.util.HashMap;
import java.util.Map;


import org.openprovenance.prov.model.QualifiedName;
import org.openprovenance.prov.rdf.Ontology;

public class Types {
    
    final private Ontology onto;

    public Types(Ontology onto) {
	this.onto=onto;
	initialize();
    }
  
    public enum ProvType {
	ENTITY, 
	AGENT, 
	ACTIVITY, 
	INFLUENCE,

	BUNDLE,

	ORGANIZATION,

	PERSON,

	SOFTWAREAGENT,

	LOCATION,

	ROLE,

	PLAN,

	CONTRIBUTOR,

	COLLECTION,

	DICTIONARY,

	EMPTYCOLLECTION,

	EMPTYDICTIONARY,

	INSTANTANEOUSEVENT,

	ENTITYINFLUENCE,

	INSERTION,

	REMOVAL,

	ACTIVITYINFLUENCE,

	AGENTINFLUENCE,

	ASSOCIATION,

	ATTRIBUTION,

	COMMUNICATION,

	DELEGATION,

	DERIVATION,

	QUOTATION, 
	REVISION, 
    
	PRIMARYSOURCE,

	END,

	START,

	GENERATION,

	INVALIDATION,

	USAGE;
    }

    public void initialize() {
	registerType(ProvType.ENTITY, onto.QualifiedName_PROVO_Entity);
	registerType(ProvType.AGENT, onto.QualifiedName_PROVO_Agent);
	registerType(ProvType.ACTIVITY, onto.QualifiedName_PROVO_Activity);
	registerType(ProvType.INFLUENCE, onto.QualifiedName_PROVO_Influence);

	registerType(ProvType.BUNDLE, onto.QualifiedName_PROVO_Bundle, ProvType.ENTITY);

	registerType(ProvType.ORGANIZATION, onto.QualifiedName_PROVO_Organization, ProvType.AGENT);

	registerType(ProvType.PERSON, onto.QualifiedName_PROVO_Person, ProvType.AGENT);

	registerType(ProvType.SOFTWAREAGENT, onto.QualifiedName_PROVO_SoftwareAgent, ProvType.AGENT);

	registerType(ProvType.LOCATION, onto.QualifiedName_PROVO_Location);

	registerType(ProvType.ROLE, onto.QualifiedName_PROVO_Role);

	registerType(ProvType.PLAN, onto.QualifiedName_PROVO_Plan, ProvType.ENTITY);

	registerType(ProvType.CONTRIBUTOR, onto.QualifiedName_PROVDC_Contributor, ProvType.ROLE);

	registerType(ProvType.COLLECTION, onto.QualifiedName_PROVO_Collection, ProvType.ENTITY);

	registerType(ProvType.DICTIONARY, onto.QualifiedName_PROVO_Dictionary, ProvType.COLLECTION);

	registerType(ProvType.EMPTYCOLLECTION, onto.QualifiedName_PROVO_EmptyCollection, ProvType.COLLECTION);

	registerType(ProvType.EMPTYDICTIONARY, onto.QualifiedName_PROVO_EmptyDictionary, ProvType.DICTIONARY);

	registerType(ProvType.INSTANTANEOUSEVENT, onto.QualifiedName_PROVO_InstantaneousEvent);

	registerType(ProvType.ENTITYINFLUENCE, onto.QualifiedName_PROVO_EntityInfluence, ProvType.INFLUENCE);

	registerType(ProvType.INSERTION, onto.QualifiedName_PROVO_Insertion, ProvType.INFLUENCE);

	registerType(ProvType.REMOVAL, onto.QualifiedName_PROVO_Removal, ProvType.INFLUENCE);

	registerType(ProvType.ACTIVITYINFLUENCE, onto.QualifiedName_PROVO_ActivityInfluence,
		     ProvType.INFLUENCE);

	registerType(ProvType.AGENTINFLUENCE, onto.QualifiedName_PROVO_AgentInfluence, ProvType.INFLUENCE);
       
	registerType(ProvType.ASSOCIATION, onto.QualifiedName_PROVO_Association, ProvType.AGENTINFLUENCE);
       
	registerType(ProvType.ATTRIBUTION, onto.QualifiedName_PROVO_Attribution, ProvType.AGENTINFLUENCE);
       
	registerType(ProvType.COMMUNICATION, onto.QualifiedName_PROVO_Communication, ProvType.AGENTINFLUENCE);
       
	registerType(ProvType.DELEGATION, onto.QualifiedName_PROVO_Delegation, ProvType.AGENTINFLUENCE);
       
	registerType(ProvType.DERIVATION, onto.QualifiedName_PROVO_Derivation, ProvType.ENTITYINFLUENCE);

	registerType(ProvType.QUOTATION, onto.QualifiedName_PROVO_Quotation, ProvType.ENTITYINFLUENCE);
	registerType(ProvType.REVISION, onto.QualifiedName_PROVO_Revision, ProvType.ENTITYINFLUENCE);
    
	registerType(ProvType.PRIMARYSOURCE, onto.QualifiedName_PROVO_PrimarySource, ProvType.ENTITYINFLUENCE);

	registerType(ProvType.END, 
	             onto.QualifiedName_PROVO_End, 
	             new ProvType[] {ProvType.INSTANTANEOUSEVENT, ProvType.ENTITYINFLUENCE });

	registerType(ProvType.START, 
	             onto.QualifiedName_PROVO_Start, 
	             new ProvType[] {ProvType.INSTANTANEOUSEVENT, ProvType.ENTITYINFLUENCE });
	   
	registerType(ProvType.GENERATION, 
	             onto.QualifiedName_PROVO_Generation, 
	             new ProvType[] {ProvType.INSTANTANEOUSEVENT, ProvType.ACTIVITYINFLUENCE });

	registerType(ProvType.INVALIDATION, 
	             onto.QualifiedName_PROVO_Invalidation, 
	             new ProvType[] {ProvType.INSTANTANEOUSEVENT, ProvType.ACTIVITYINFLUENCE });

	registerType(ProvType.USAGE, 
	             onto.QualifiedName_PROVO_Usage, 
	             new ProvType[] {ProvType.INSTANTANEOUSEVENT, ProvType.ENTITYINFLUENCE });
    }


    private final Map<ProvType, QualifiedName> type2qn = new HashMap<ProvType,QualifiedName>();
    private static final Map<ProvType, ProvType[]> extendsType = new HashMap<ProvType,ProvType[]>();

    private static final Map<QualifiedName, ProvType> qn2type = new HashMap<QualifiedName, ProvType>();



    void registerType(ProvType type, QualifiedName qualifiedName)
    {
	type2qn.put(type, qualifiedName);
	qn2type.put(qualifiedName,type);
	extendsType.put(type, new ProvType[]{});
    }

    void registerType(ProvType type, QualifiedName qualifiedName, ProvType extendsType)
    {
	type2qn.put(type, qualifiedName);
	qn2type.put(qualifiedName,type);
	Types.extendsType.put(type,new ProvType[] { extendsType });
    }

    void registerType(ProvType type, QualifiedName qualifiedName, ProvType[] extendsTypes)
    {
	type2qn.put(type, qualifiedName);
	qn2type.put(qualifiedName,type);
	extendsType.put(type,extendsTypes);
    }
   

    public ProvType lookup(QualifiedName qualifiedName)
    {
	return qn2type.get(qualifiedName);
    }

    public QualifiedName find(ProvType pt) {
	return type2qn.get(pt);
    }
    
    public ProvType [] getExtends(ProvType type) {
	return extendsType.get(type);
    }

	
}


