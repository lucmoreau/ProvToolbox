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
	registerType(ProvType.ENTITY, onto.QNAME_PROVO_Entity);
	registerType(ProvType.AGENT, onto.QNAME_PROVO_Agent);
	registerType(ProvType.ACTIVITY, onto.QNAME_PROVO_Activity);
	registerType(ProvType.INFLUENCE, onto.QNAME_PROVO_Influence);

	registerType(ProvType.BUNDLE, onto.QNAME_PROVO_Bundle, ProvType.ENTITY);

	registerType(ProvType.ORGANIZATION, onto.QNAME_PROVO_Organization, ProvType.AGENT);

	registerType(ProvType.PERSON, onto.QNAME_PROVO_Person, ProvType.AGENT);

	registerType(ProvType.SOFTWAREAGENT, onto.QNAME_PROVO_SoftwareAgent, ProvType.AGENT);

	registerType(ProvType.LOCATION, onto.QNAME_PROVO_Location);

	registerType(ProvType.ROLE, onto.QNAME_PROVO_Role);

	registerType(ProvType.PLAN, onto.QNAME_PROVO_Plan, ProvType.ENTITY);

	registerType(ProvType.CONTRIBUTOR, onto.QNAME_PROVDC_Contributor, ProvType.ROLE);

	registerType(ProvType.COLLECTION, onto.QNAME_PROVO_Collection, ProvType.ENTITY);

	registerType(ProvType.DICTIONARY, onto.QNAME_PROVO_Dictionary, ProvType.COLLECTION);

	registerType(ProvType.EMPTYCOLLECTION, onto.QNAME_PROVO_EmptyCollection, ProvType.COLLECTION);

	registerType(ProvType.EMPTYDICTIONARY, onto.QNAME_PROVO_EmptyDictionary, ProvType.DICTIONARY);

	registerType(ProvType.INSTANTANEOUSEVENT, onto.QNAME_PROVO_InstantaneousEvent);

	registerType(ProvType.ENTITYINFLUENCE, onto.QNAME_PROVO_EntityInfluence, ProvType.INFLUENCE);

	registerType(ProvType.INSERTION, onto.QNAME_PROVO_Insertion, ProvType.INFLUENCE);

	registerType(ProvType.REMOVAL, onto.QNAME_PROVO_Removal, ProvType.INFLUENCE);

	registerType(ProvType.ACTIVITYINFLUENCE, onto.QNAME_PROVO_ActivityInfluence,
		     ProvType.INFLUENCE);

	registerType(ProvType.AGENTINFLUENCE, onto.QNAME_PROVO_AgentInfluence, ProvType.INFLUENCE);
       
	registerType(ProvType.ASSOCIATION, onto.QNAME_PROVO_Association, ProvType.AGENTINFLUENCE);
       
	registerType(ProvType.ATTRIBUTION, onto.QNAME_PROVO_Attribution, ProvType.AGENTINFLUENCE);
       
	registerType(ProvType.COMMUNICATION, onto.QNAME_PROVO_Communication, ProvType.AGENTINFLUENCE);
       
	registerType(ProvType.DELEGATION, onto.QNAME_PROVO_Delegation, ProvType.AGENTINFLUENCE);
       
	registerType(ProvType.DERIVATION, onto.QNAME_PROVO_Derivation, ProvType.ENTITYINFLUENCE);

	registerType(ProvType.QUOTATION, onto.QNAME_PROVO_Quotation, ProvType.ENTITYINFLUENCE);
	registerType(ProvType.REVISION, onto.QNAME_PROVO_Revision, ProvType.ENTITYINFLUENCE);
    
	registerType(ProvType.PRIMARYSOURCE, onto.QNAME_PROVO_PrimarySource, ProvType.ENTITYINFLUENCE);

	registerType(ProvType.END, 
	             onto.QNAME_PROVO_End, 
	             new ProvType[] {ProvType.INSTANTANEOUSEVENT, ProvType.ENTITYINFLUENCE });

	registerType(ProvType.START, 
	             onto.QNAME_PROVO_Start, 
	             new ProvType[] {ProvType.INSTANTANEOUSEVENT, ProvType.ENTITYINFLUENCE });
	   
	registerType(ProvType.GENERATION, 
	             onto.QNAME_PROVO_Generation, 
	             new ProvType[] {ProvType.INSTANTANEOUSEVENT, ProvType.ACTIVITYINFLUENCE });

	registerType(ProvType.INVALIDATION, 
	             onto.QNAME_PROVO_Invalidation, 
	             new ProvType[] {ProvType.INSTANTANEOUSEVENT, ProvType.ACTIVITYINFLUENCE });

	registerType(ProvType.USAGE, 
	             onto.QNAME_PROVO_Usage, 
	             new ProvType[] {ProvType.INSTANTANEOUSEVENT, ProvType.ENTITYINFLUENCE });
    }


    private final Map<ProvType, QualifiedName> type2qn = new HashMap<ProvType,QualifiedName>();
    private static final Map<ProvType, ProvType[]> extendsType = new HashMap<ProvType,ProvType[]>();

    private static final Map<QualifiedName, ProvType> qn2type = new HashMap<QualifiedName, ProvType>();



    void registerType(ProvType type, QualifiedName qname)
    {
	type2qn.put(type, qname);
	qn2type.put(qname,type);
	extendsType.put(type, new ProvType[]{});
    }

    void registerType(ProvType type, QualifiedName qname, ProvType extendsType)
    {
	type2qn.put(type, qname);
	qn2type.put(qname,type);
	Types.extendsType.put(type,new ProvType[] { extendsType });
    }

    void registerType(ProvType type, QualifiedName qname, ProvType[] extendsTypes)
    {
	type2qn.put(type, qname);
	qn2type.put(qname,type);
	extendsType.put(type,extendsTypes);
    }
   

    public ProvType lookup(QualifiedName qname)
    {
	return qn2type.get(qname);
    }

    public QualifiedName find(ProvType pt) {
	return type2qn.get(pt);
    }
    
    public ProvType [] getExtends(ProvType type) {
	return extendsType.get(type);
    }

	
}


