package org.openprovenance.prov.rdf;

import java.util.Hashtable;

import javax.xml.namespace.QName;

import org.openprovenance.prov.xml.Attribute;
import org.openprovenance.prov.xml.NamespacePrefixMapper;

public class Ontology {
	   public Ontology () {
		   initInfluenceTables();
	   }
	
	   public Hashtable<QName,QName> qualifiedInfluenceTable=new Hashtable<QName, QName>();
	    public Hashtable<QName,QName> influencerTable=new Hashtable<QName, QName>();
	    public Hashtable<QName,QName> unqualifiedTable=new Hashtable<QName, QName>();
	    public Hashtable<QName,QName> otherTable=new Hashtable<QName, QName>();
	    public Hashtable<QName,QName> convertTable=new Hashtable<QName, QName>();
	    

	    public static QName newProvQName(String local) {
		return new QName(NamespacePrefixMapper.PROV_NS, local, NamespacePrefixMapper.PROV_PREFIX);
	    }

	   
		
	    public static QName newRdfQName(String local) {
			return new QName(NamespacePrefixMapper.RDF_NS, local, NamespacePrefixMapper.RDF_PREFIX);
	    }
	    
	    public static QName QNAME_PROVO_atLocation=newProvQName("atLocation");
	    public static QName QNAME_PROVO_atTime=newProvQName("atTime");
	    public static QName QNAME_PROVO_startedAtTime=newProvQName("startedAtTime");
	    public static QName QNAME_PROVO_endedAtTime=newProvQName("endedAtTime");
	    public static QName QNAME_PROVO_influencer=newProvQName("influencer");
	    public static QName QNAME_PROVO_activity=newProvQName("activity");
	    public static QName QNAME_PROVO_entity=newProvQName("entity");
	    public static QName QNAME_PROVO_agent=newProvQName("agent");
	    public static QName QNAME_PROVO_hadActivity=newProvQName("hadActivity");
	    public static QName QNAME_PROVO_hadEntity=newProvQName("hadEntity");
	    public static QName QNAME_PROVO_hadPlan=newProvQName("hadPlan");
	    public static QName QNAME_PROVO_hadGeneration=newProvQName("hadGeneration");
	    public static QName QNAME_PROVO_hadUsage=newProvQName("hadUsage");

	    public static QName QNAME_PROVO_Activity=newProvQName("Activity");
	    public static QName QNAME_PROVO_Entity=newProvQName("Entity");
	    public static QName QNAME_PROVO_Agent=newProvQName("Agent");
	    
	    public static QName QNAME_PROVO_Influence=newProvQName("Influence");
	    public static QName QNAME_PROVO_qualifiedInfluence=newProvQName("qualifiedInfluence");
	    public static QName QNAME_PROVO_wasInfluencedBy=newProvQName("wasInfluencedBy");

	    public static QName QNAME_PROVO_Generation=newProvQName("Generation");
	    public static QName QNAME_PROVO_qualifiedGeneration=newProvQName("qualifiedGeneration");
	    public static QName QNAME_PROVO_wasGeneratedBy=newProvQName("wasGeneratedBy");

	    public static QName QNAME_PROVO_Usage=newProvQName("Usage");
	    public static QName QNAME_PROVO_qualifiedUsage=newProvQName("qualifiedUsage");
	    public static QName QNAME_PROVO_used=newProvQName("used");

	    public static QName QNAME_PROVO_Invalidation=newProvQName("Invalidation");
	    public static QName QNAME_PROVO_qualifiedInvalidation=newProvQName("qualifiedInvalidation");
	    public static QName QNAME_PROVO_wasInvalidatedBy=newProvQName("wasInvalidatedBy");
	    
	    public static QName QNAME_PROVO_Start=newProvQName("Start");
	    public static QName QNAME_PROVO_qualifiedStart=newProvQName("qualifiedStart");
	    public static QName QNAME_PROVO_wasStartedBy=newProvQName("wasStartedBy");

	    public static QName QNAME_PROVO_End=newProvQName("End");
	    public static QName QNAME_PROVO_qualifiedEnd=newProvQName("qualifiedEnd");
	    public static QName QNAME_PROVO_wasEndedBy=newProvQName("wasEndedBy");

	    public static QName QNAME_PROVO_Association=newProvQName("Association");
	    public static QName QNAME_PROVO_qualifiedAssociation=newProvQName("qualifiedAssociation");
	    public static QName QNAME_PROVO_wasAssociatedWith=newProvQName("wasAssociatedWith");

	    public static QName QNAME_PROVO_Attribution=newProvQName("Attribution");
	    public static QName QNAME_PROVO_qualifiedAttribution=newProvQName("qualifiedAttribution");
	    public static QName QNAME_PROVO_wasAttributedTo=newProvQName("wasAttributedTo");

	    public static QName QNAME_PROVO_Delegation=newProvQName("Delegation");
	    public static QName QNAME_PROVO_qualifiedDelegation=newProvQName("qualifiedDelegation");
	    public static QName QNAME_PROVO_actedOnBehalfOf=newProvQName("actedOnBehalfOf");

	    public static QName QNAME_PROVO_Derivation=newProvQName("Derivation");
	    public static QName QNAME_PROVO_qualifiedDerivation=newProvQName("qualifiedDerivation");
	    public static QName QNAME_PROVO_wasDerivedFrom=newProvQName("wasDerivedFrom");

	    public static QName QNAME_PROVO_Revision=newProvQName("Revision");
	    public static QName QNAME_PROVO_qualifiedRevision=newProvQName("qualifiedRevision");
	    public static QName QNAME_PROVO_wasRevisionOf=newProvQName("wasRevisionOf");

	    
	    public static QName QNAME_PROVO_Quotation=newProvQName("Quotation");
	    public static QName QNAME_PROVO_qualifiedQuotation=newProvQName("qualifiedQuotation");
	    public static QName QNAME_PROVO_wasQuotedFrom=newProvQName("wasQuotedFrom");//TODO: check it's right

	    
	    public static QName QNAME_PROVO_PrimarySource=newProvQName("PrimarySource");
	    public static QName QNAME_PROVO_qualifiedPrimarySource=newProvQName("qualifiedPrimarySource");
	    public static QName QNAME_PROVO_hadPrimarySource=newProvQName("hadPrimarySource");

	    
	    public static QName QNAME_PROVO_Communication=newProvQName("Communication");
	    public static QName QNAME_PROVO_qualifiedCommunication=newProvQName("qualifiedCommunication");
	    public static QName QNAME_PROVO_wasInformedBy=newProvQName("wasInformedBy");
	    
	    public static QName QNAME_PROVO_specializationOf=newProvQName("specializationOf");
	    public static QName QNAME_PROVO_alternateOf=newProvQName("alternateOf");
	    public static QName QNAME_PROVO_mentionOf=newProvQName("mentionOf");
	    public static QName QNAME_PROVO_asInBundle=newProvQName("asInBundle");
	    public static QName QNAME_PROVO_hadMember=newProvQName("hadMember");


	    public static QName QNAME_RDF_TYPE=newRdfQName("type");
	    public static QName QNAME_RDF_LABEL=newRdfQName("label");
	    
	    void initInfluenceTables() {
	   	 qualifiedInfluenceTable.put(QNAME_PROVO_Influence, QNAME_PROVO_qualifiedInfluence);
	   	 qualifiedInfluenceTable.put(QNAME_PROVO_Generation, QNAME_PROVO_qualifiedGeneration);
	   	 qualifiedInfluenceTable.put(QNAME_PROVO_Usage, QNAME_PROVO_qualifiedUsage);
	   	 qualifiedInfluenceTable.put(QNAME_PROVO_Invalidation, QNAME_PROVO_qualifiedInvalidation);
	   	 qualifiedInfluenceTable.put(QNAME_PROVO_Start, QNAME_PROVO_qualifiedStart);
	   	 qualifiedInfluenceTable.put(QNAME_PROVO_End, QNAME_PROVO_qualifiedEnd);
	   	 qualifiedInfluenceTable.put(QNAME_PROVO_Association, QNAME_PROVO_qualifiedAssociation);
	   	 qualifiedInfluenceTable.put(QNAME_PROVO_Attribution, QNAME_PROVO_qualifiedAttribution);
	   	 qualifiedInfluenceTable.put(QNAME_PROVO_Delegation, QNAME_PROVO_qualifiedDelegation);
	   	 qualifiedInfluenceTable.put(QNAME_PROVO_Derivation, QNAME_PROVO_qualifiedDerivation);
	   	 qualifiedInfluenceTable.put(QNAME_PROVO_Quotation, QNAME_PROVO_qualifiedQuotation);
	   	 qualifiedInfluenceTable.put(QNAME_PROVO_Revision, QNAME_PROVO_qualifiedRevision);
	   	 qualifiedInfluenceTable.put(QNAME_PROVO_PrimarySource, QNAME_PROVO_qualifiedPrimarySource);
	   	 qualifiedInfluenceTable.put(QNAME_PROVO_Communication, QNAME_PROVO_qualifiedCommunication);
	   	
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
	   	 
	   	 unqualifiedTable.put(QNAME_PROVO_Influence, QNAME_PROVO_wasInfluencedBy);
	   	 unqualifiedTable.put(QNAME_PROVO_Generation, QNAME_PROVO_wasGeneratedBy);
	   	 unqualifiedTable.put(QNAME_PROVO_Usage, QNAME_PROVO_used);
	   	 unqualifiedTable.put(QNAME_PROVO_Invalidation, QNAME_PROVO_wasInvalidatedBy);
	   	 unqualifiedTable.put(QNAME_PROVO_Start, QNAME_PROVO_wasStartedBy);
	  	 unqualifiedTable.put(QNAME_PROVO_End, QNAME_PROVO_wasEndedBy);
	  	 unqualifiedTable.put(QNAME_PROVO_Association, QNAME_PROVO_wasAssociatedWith);
	  	 unqualifiedTable.put(QNAME_PROVO_Attribution, QNAME_PROVO_wasAttributedTo);
	  	 unqualifiedTable.put(QNAME_PROVO_Delegation, QNAME_PROVO_actedOnBehalfOf);
	  	 unqualifiedTable.put(QNAME_PROVO_Derivation, QNAME_PROVO_wasDerivedFrom);
	  	 unqualifiedTable.put(QNAME_PROVO_Revision, QNAME_PROVO_wasRevisionOf);
	  	 unqualifiedTable.put(QNAME_PROVO_Quotation, QNAME_PROVO_wasQuotedFrom);
	  	 unqualifiedTable.put(QNAME_PROVO_PrimarySource, QNAME_PROVO_hadPrimarySource);
	  	 unqualifiedTable.put(QNAME_PROVO_Communication, QNAME_PROVO_wasInformedBy);
	  	 
	  	 otherTable.put(QNAME_PROVO_Start, QNAME_PROVO_hadActivity);
	  	 otherTable.put(QNAME_PROVO_End, QNAME_PROVO_hadActivity);
	  	 otherTable.put(QNAME_PROVO_Derivation, QNAME_PROVO_hadActivity);
	  	 otherTable.put(QNAME_PROVO_Revision, QNAME_PROVO_hadActivity);
	  	 otherTable.put(QNAME_PROVO_Quotation, QNAME_PROVO_hadActivity);
	  	 otherTable.put(QNAME_PROVO_PrimarySource, QNAME_PROVO_hadActivity);
	  	 otherTable.put(QNAME_PROVO_Association, QNAME_PROVO_hadPlan);
	  	 otherTable.put(QNAME_PROVO_Delegation, QNAME_PROVO_hadActivity);

	    
	  	 convertTable.put(Attribute.PROV_LABEL_QNAME, QNAME_RDF_LABEL);
	  	 convertTable.put(Attribute.PROV_TYPE_QNAME, QNAME_RDF_TYPE);
	  	 convertTable.put(Attribute.PROV_LOCATION_QNAME, QNAME_PROVO_atLocation);
        }	

	    void activityInfluence(QName name) {
			influencerTable.put(name, QNAME_PROVO_activity);
		}
	    void entityInfluence(QName name) {
			influencerTable.put(name, QNAME_PROVO_entity);
		}
	    void agentInfluence(QName name) {
			influencerTable.put(name, QNAME_PROVO_agent);
		}
	    
	    public QName convertToRdf(QName qname) {
	    	QName res=convertTable.get(qname);
	    	if (res!=null) return res;
	    	return qname;
	    }
	   


}
