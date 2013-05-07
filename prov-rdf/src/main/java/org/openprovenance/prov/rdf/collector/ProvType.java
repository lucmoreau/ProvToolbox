package org.openprovenance.prov.rdf.collector;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import javax.xml.namespace.QName;

import org.openprovenance.prov.rdf.Ontology;

public enum ProvType
{

	ENTITY(Ontology.QNAME_PROVO_Entity), AGENT(Ontology.QNAME_PROVO_Agent), ACTIVITY(
			Ontology.QNAME_PROVO_Activity), INFLUENCE(
			Ontology.QNAME_PROVO_Influence),

	BUNDLE(Ontology.QNAME_PROVO_Bundle, ProvType.ENTITY),

	ORGANIZATION(Ontology.QNAME_PROVO_Organization, ProvType.AGENT),

	PERSON(Ontology.QNAME_PROVO_Person, ProvType.AGENT),

	SOFTWAREAGENT(Ontology.QNAME_PROVO_SoftwareAgent, ProvType.AGENT),

	LOCATION(Ontology.QNAME_PROVO_Location),

	ROLE(Ontology.QNAME_PROVO_Role),

	PLAN(Ontology.QNAME_PROVO_Plan, ProvType.ENTITY),
	
	CONTRIBUTOR(Ontology.QNAME_PROVDC_Contributor, ProvType.ROLE),

	COLLECTION(Ontology.QNAME_PROVO_Collection, ProvType.ENTITY),

	DICTIONARY(Ontology.QNAME_PROVO_Dictionary, ProvType.COLLECTION),

	EMPTYCOLLECTION(Ontology.QNAME_PROVO_EmptyCollection,
			ProvType.COLLECTION),

	EMPTYDICTIONARY(Ontology.QNAME_PROVO_EmptyDictionary,
			ProvType.DICTIONARY),

	INSTANTANEOUSEVENT(Ontology.QNAME_PROVO_InstantaneousEvent),

	ENTITYINFLUENCE(Ontology.QNAME_PROVO_EntityInfluence,
			ProvType.INFLUENCE),

			
	INSERTION(Ontology.QNAME_PROVO_Insertion,
					ProvType.INFLUENCE),

	REMOVAL(Ontology.QNAME_PROVO_Removal,
							ProvType.INFLUENCE),
					
	ACTIVITYINFLUENCE(Ontology.QNAME_PROVO_ActivityInfluence,
			ProvType.INFLUENCE),

	AGENTINFLUENCE(Ontology.QNAME_PROVO_AgentInfluence, ProvType.INFLUENCE),

	ASSOCIATION(Ontology.QNAME_PROVO_Association, ProvType.AGENTINFLUENCE),

	ATTRIBUTION(Ontology.QNAME_PROVO_Attribution, ProvType.AGENTINFLUENCE),

	COMMUNICATION(Ontology.QNAME_PROVO_Communication,
			ProvType.AGENTINFLUENCE),

	DELEGATION(Ontology.QNAME_PROVO_Delegation, ProvType.AGENTINFLUENCE),

	DERIVATION(Ontology.QNAME_PROVO_Derivation, ProvType.ENTITYINFLUENCE),

	QUOTATION(Ontology.QNAME_PROVO_Quotation, ProvType.ENTITYINFLUENCE), REVISION(
			Ontology.QNAME_PROVO_Revision, ProvType.ENTITYINFLUENCE), PRIMARYSOURCE(
			Ontology.QNAME_PROVO_PrimarySource, ProvType.ENTITYINFLUENCE),

	END(Ontology.QNAME_PROVO_End, new ProvType[] {
			ProvType.INSTANTANEOUSEVENT, ProvType.ENTITYINFLUENCE }),

	START(Ontology.QNAME_PROVO_Start, new ProvType[] {
			ProvType.INSTANTANEOUSEVENT, ProvType.ENTITYINFLUENCE }),

	GENERATION(Ontology.QNAME_PROVO_Generation, new ProvType[] {
			ProvType.INSTANTANEOUSEVENT, ProvType.ACTIVITYINFLUENCE }),

	INVALIDATION(Ontology.QNAME_PROVO_Invalidation, new ProvType[] {
			ProvType.INSTANTANEOUSEVENT, ProvType.ACTIVITYINFLUENCE }),

			USAGE(Ontology.QNAME_PROVO_Usage, new ProvType[] {
					ProvType.INSTANTANEOUSEVENT, ProvType.ENTITYINFLUENCE });

	private static final Map<QName, ProvType> lookup = new HashMap<QName, ProvType>();
	static
	{
		for (ProvType provStatement : EnumSet.allOf(ProvType.class))
			lookup.put(provStatement.getQName(), provStatement);
	}

	private ProvType[] extendsTypes;
	private QName qname;

	private ProvType(QName qname)
	{
		this.qname = qname;
		this.extendsTypes = new ProvType[] {};
	}

	private ProvType(QName qname, ProvType extendsType)
	{
		this(qname);
		this.extendsTypes = new ProvType[] { extendsType };
	}

	private ProvType(QName qname, ProvType[] extendsTypes)
	{
		this(qname);
		this.extendsTypes = extendsTypes;
	}

	public QName getQName()
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

	public static ProvType lookup(QName qname)
	{
		return lookup.get(qname);
	}

	public String toString()
	{
		return qname.toString();
	}

	public boolean equals(ProvType b)
	{
		return b.getQName().equals(this.getQName());
	}

}