package org.openprovenance.prov.rdf.collector;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;


import org.openprovenance.prov.model.QualifiedName;
import org.openprovenance.prov.rdf.Ontology;
import org.openprovenance.prov.xml.ProvFactory;

public class Types {
    
    //FIXME: make this a independent of xml.ProvFactory
   private static Ontology onto=new Ontology(ProvFactory.getFactory());
  
   public enum ProvType
   {
    ENTITY(onto.QNAME_PROVO_Entity), 
    AGENT(onto.QNAME_PROVO_Agent), 
    ACTIVITY(onto.QNAME_PROVO_Activity), 
    INFLUENCE(onto.QNAME_PROVO_Influence),

    BUNDLE(onto.QNAME_PROVO_Bundle, ProvType.ENTITY),

    ORGANIZATION(onto.QNAME_PROVO_Organization, ProvType.AGENT),

    PERSON(onto.QNAME_PROVO_Person, ProvType.AGENT),

    SOFTWAREAGENT(onto.QNAME_PROVO_SoftwareAgent, ProvType.AGENT),

    LOCATION(onto.QNAME_PROVO_Location),

    ROLE(onto.QNAME_PROVO_Role),

    PLAN(onto.QNAME_PROVO_Plan, ProvType.ENTITY),

    CONTRIBUTOR(onto.QNAME_PROVDC_Contributor, ProvType.ROLE),

    COLLECTION(onto.QNAME_PROVO_Collection, ProvType.ENTITY),

    DICTIONARY(onto.QNAME_PROVO_Dictionary, ProvType.COLLECTION),

    EMPTYCOLLECTION(onto.QNAME_PROVO_EmptyCollection, ProvType.COLLECTION),

    EMPTYDICTIONARY(onto.QNAME_PROVO_EmptyDictionary, ProvType.DICTIONARY),

    INSTANTANEOUSEVENT(onto.QNAME_PROVO_InstantaneousEvent),

    ENTITYINFLUENCE(onto.QNAME_PROVO_EntityInfluence, ProvType.INFLUENCE),

    INSERTION(onto.QNAME_PROVO_Insertion, ProvType.INFLUENCE),

    REMOVAL(onto.QNAME_PROVO_Removal, ProvType.INFLUENCE),

    ACTIVITYINFLUENCE(onto.QNAME_PROVO_ActivityInfluence,
	    ProvType.INFLUENCE),

    AGENTINFLUENCE(onto.QNAME_PROVO_AgentInfluence, ProvType.INFLUENCE),

    ASSOCIATION(onto.QNAME_PROVO_Association, ProvType.AGENTINFLUENCE),

    ATTRIBUTION(onto.QNAME_PROVO_Attribution, ProvType.AGENTINFLUENCE),

    COMMUNICATION(onto.QNAME_PROVO_Communication, ProvType.AGENTINFLUENCE),

    DELEGATION(onto.QNAME_PROVO_Delegation, ProvType.AGENTINFLUENCE),

    DERIVATION(onto.QNAME_PROVO_Derivation, ProvType.ENTITYINFLUENCE),

    QUOTATION(onto.QNAME_PROVO_Quotation, ProvType.ENTITYINFLUENCE), 
    REVISION(onto.QNAME_PROVO_Revision, ProvType.ENTITYINFLUENCE), 
    
    PRIMARYSOURCE(onto.QNAME_PROVO_PrimarySource, ProvType.ENTITYINFLUENCE),

    END(onto.QNAME_PROVO_End, new ProvType[] { ProvType.INSTANTANEOUSEVENT,
	    ProvType.ENTITYINFLUENCE }),

    START(onto.QNAME_PROVO_Start, new ProvType[] {
	    ProvType.INSTANTANEOUSEVENT, ProvType.ENTITYINFLUENCE }),

    GENERATION(onto.QNAME_PROVO_Generation, new ProvType[] {
	    ProvType.INSTANTANEOUSEVENT, ProvType.ACTIVITYINFLUENCE }),

    INVALIDATION(onto.QNAME_PROVO_Invalidation, new ProvType[] {
	    ProvType.INSTANTANEOUSEVENT, ProvType.ACTIVITYINFLUENCE }),

    USAGE(onto.QNAME_PROVO_Usage, new ProvType[] {
	    ProvType.INSTANTANEOUSEVENT, ProvType.ENTITYINFLUENCE });


   
	private static final Map<QualifiedName, ProvType> lookup = new HashMap<QualifiedName, ProvType>();
	static
	{
		for (ProvType provStatement : EnumSet.allOf(ProvType.class))  {
			lookup.put(provStatement.getQualifiedName(), provStatement);
		}
	}

	 ProvType[] extendsTypes;
	 QualifiedName qname;

	ProvType(QualifiedName qname)
	{
		this.qname = qname;
		this.extendsTypes = new ProvType[] {};
	}

	private ProvType(QualifiedName qname, ProvType extendsType)
	{
		this(qname);
		this.extendsTypes = new ProvType[] { extendsType };
	}

	private ProvType(QualifiedName qname, ProvType[] extendsTypes)
	{
		this(qname);
		this.extendsTypes = extendsTypes;
	}

	public QualifiedName getQualifiedName()
	{
		return qname;
	}

	public String getURIString()
	{
		return qname.getNamespaceURI() + qname.getLocalPart();
	}

	public ProvType[] getExtends()
	{
		return this.extendsTypes;
	}

	public static ProvType lookup(QualifiedName qname)
	{
		return lookup.get(qname);
	}

	public String toString()
	{
		return qname.toString();
	}

	public boolean equals(ProvType b)
	{
		return b.getQualifiedName().equals(this.getQualifiedName());
	}
	
 }
}

