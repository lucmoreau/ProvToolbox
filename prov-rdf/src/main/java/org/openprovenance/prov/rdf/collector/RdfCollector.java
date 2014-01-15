package org.openprovenance.prov.rdf.collector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import javax.xml.datatype.XMLGregorianCalendar;

import org.openprovenance.prov.rdf.Ontology;
import org.openprovenance.prov.model.ActedOnBehalfOf;
import org.openprovenance.prov.model.DictionaryMembership;
import org.openprovenance.prov.model.HadMember;
import org.openprovenance.prov.model.AlternateOf;
import org.openprovenance.prov.model.Attribute;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.MentionOf;
import org.openprovenance.prov.model.NamedBundle;
import org.openprovenance.prov.model.NamespacePrefixMapper;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.model.Entry;
import org.openprovenance.prov.model.QualifiedName;
import org.openprovenance.prov.model.Key;
import org.openprovenance.prov.model.Name;
import org.openprovenance.prov.model.Namespace;
import org.openprovenance.prov.model.SpecializationOf;
import org.openprovenance.prov.model.Used;
import org.openprovenance.prov.model.ValueConverter;
import org.openprovenance.prov.model.WasAssociatedWith;
import org.openprovenance.prov.model.WasAttributedTo;
import org.openprovenance.prov.model.WasDerivedFrom;
import org.openprovenance.prov.model.WasEndedBy;
import org.openprovenance.prov.model.WasGeneratedBy;
import org.openprovenance.prov.model.WasInfluencedBy;
import org.openprovenance.prov.model.WasInformedBy;
import org.openprovenance.prov.model.WasInvalidatedBy;
import org.openprovenance.prov.model.WasStartedBy;
import org.openrdf.model.BNode;
import org.openrdf.model.Literal;
import org.openrdf.model.Resource;
import org.openrdf.model.Statement;
import org.openrdf.model.URI;
import org.openrdf.model.Value;
import org.openrdf.model.impl.BNodeImpl;
import org.openrdf.model.vocabulary.RDF;
import org.openrdf.rio.helpers.RDFHandlerBase;

public class RdfCollector extends RDFHandlerBase {

    final protected ProvFactory pFactory;
    final protected Name name;
    protected HashMap<QualifiedName, HashMap<QualifiedName, List<Statement>>> collators;
    private Hashtable<QualifiedName, BundleHolder> bundles;
    protected Document document;
   // private Hashtable<String, String> revnss;
    private ValueConverter valueConverter;
    final protected Ontology onto;
    protected static String BNODE_NS = "http://openprovenance.org/provtoolbox/bnode/";

    final protected Types types;
    
    public RdfCollector(ProvFactory pFactory, Ontology onto) {
   	this.pFactory = pFactory;
   	this.name=pFactory.getName();
   	this.onto=onto;
   	this.collators = new HashMap<QualifiedName, HashMap<QualifiedName, List<Statement>>>();
   	//this.revnss = new Hashtable<String, String>();
   	this.document = pFactory.newDocument();
   	this.valueConverter = new ValueConverter(pFactory);
   	this.bundles = new Hashtable<QualifiedName, BundleHolder>();
   	this.namespace=new Namespace();
   	document.setNamespace(this.namespace);
   	handleNamespace(NamespacePrefixMapper.XSD_PREFIX,
   	                NamespacePrefixMapper.XSD_HASH_NS);
   	handleNamespace("bnode", BNODE_NS);
   	this.types=new Types(onto);
   	
   	QUAL_PREDS= Arrays.asList(
   	             	    onto.QNAME_PROVO_qualifiedAssociation,
   	     	    onto.QNAME_PROVO_qualifiedAttribution,
   	     	    onto.QNAME_PROVO_qualifiedCommunication,
   	     	    onto.QNAME_PROVO_qualifiedDelegation,
   	     	    onto.QNAME_PROVO_qualifiedDerivation,
   	     	    onto.QNAME_PROVO_qualifiedEnd,
   	     	    onto.QNAME_PROVO_qualifiedGeneration,
   	     	    onto.QNAME_PROVO_qualifiedInfluence,
   	     	    onto.QNAME_PROVO_qualifiedInvalidation,
   	     	    onto.QNAME_PROVO_qualifiedPrimarySource,
   	     	    onto.QNAME_PROVO_qualifiedQuotation,
   	     	    onto.QNAME_PROVO_qualifiedRevision,
   	     	    onto.QNAME_PROVO_qualifiedStart,
   	     	    onto.QNAME_PROVO_qualifiedUsage,
   	     	    onto.QNAME_PROVO_qualifiedInsertion,
   	     	    onto.QNAME_PROVO_qualifiedRemoval

   	         );
   	
   	DM_TYPES=Arrays.asList(new QualifiedName[] {
   		    onto.QNAME_PROVO_Bundle, onto.QNAME_PROVO_Collection,
   		    onto.QNAME_PROVO_EmptyCollection,
   		    onto.QNAME_PROVO_Organization, onto.QNAME_PROVO_Person,
   		    onto.QNAME_PROVO_Plan, onto.QNAME_PROVO_PrimarySource,
   		    onto.QNAME_PROVO_Quotation, onto.QNAME_PROVO_Revision,
   		    onto.QNAME_PROVO_SoftwareAgent,
   		    onto.QNAME_PROVDC_Contributor, onto.QNAME_PROVO_Dictionary,
   		    onto.QNAME_PROVO_EmptyDictionary

   	    });

    }

    final private List<QualifiedName> QUAL_PREDS;
    
    final List<QualifiedName> DM_TYPES ;

   
    private HashMap<QualifiedName, List<Statement>> getCollator(Resource context) {
	return this.collators.get(convertResourceToQualifiedName(context));
    }

    private BundleHolder getBundleHolder(QualifiedName context) {
	if (context == null)
	    context = pFactory.newQualifiedName("","",""); //TODO: what is this?
	if (!bundles.containsKey(context)) {
	    bundles.put(context, new BundleHolder());
	}
	return bundles.get(context);
    }

    protected void store(QualifiedName context,
			 org.openprovenance.prov.model.Relation0 relation0) {
	getBundleHolder(context).addStatement((org.openprovenance.prov.model.Statement) relation0);
    }

    /* Utility functions */

    protected boolean isBNodeReferenced(BNode object) {
	Collection<HashMap<QualifiedName, List<Statement>>> contexts = collators.values();
	for (HashMap<QualifiedName, List<Statement>> context : contexts) {
	    for (QualifiedName subject : context.keySet()) {
		List<Statement> statements = context.get(subject);
		for (Statement statement : statements) {
		    Value value = statement.getObject();
		    QualifiedName pred = convertURIToQualifiedName(statement.getPredicate());
		    if (QUAL_PREDS.contains(pred)) {
			continue;
		    }

		    if (value instanceof BNode) {
			if (value.equals(object)) {
			    return true;
			}
		    } /* else if (value instanceof javax.xml.namespace.QName) {
			javax.xml.namespace.QName qname = (javax.xml.namespace.QName) value;
			if (qname.getNamespaceURI().equals(BNODE_NS)
				&& qname.getLocalPart().equals(object.getID())) {
			    return true;
			}
		    } */  else if (value instanceof QualifiedName) {
			QualifiedName qname = (QualifiedName) value;
			if (qname.getNamespaceURI().equals(BNODE_NS)
				&& qname.getLocalPart().equals(object.getID())) {
			    return true;
			}
		    } else if (value instanceof Resource) {
			QualifiedName qname = convertResourceToQualifiedName((Resource) value);
			if (qname.getNamespaceURI().equals(BNODE_NS)
				&& qname.getLocalPart().equals(object.getID())) {
			    return true;
			}
		    } else {	
			
		    }
		}
	    }
	}

	return false;
    }

    private boolean isProvURI(QualifiedName qname) {
	if (!qname.getNamespaceURI().equals(NamespacePrefixMapper.PROV_NS)) {
	    return false;
	}
	return true;
    }

    protected List<Statement> getStatementsForPredicate(QualifiedName context,
							QualifiedName qname, QualifiedName uri) {
	ArrayList<Statement> statements = new ArrayList<Statement>();
	for (Statement statement : collators.get(context).get(qname)) {
	    if (convertURIToQualifiedName(statement.getPredicate()).equals(uri)) {
		statements.add(statement);
	    }
	}
	return statements;
    }

    protected Statement getSingleStatementForPredicate(QualifiedName context,
						       QualifiedName qname, QualifiedName uri) {
	List<Statement> statements = getStatementsForPredicate(context, qname,
							       uri);
	assert (statements.size() <= 1);

	Statement statement = null;
	if (statements.size() == 1) {
	    statement = statements.get(0);
	}
	return statement;
    }

    protected Types.ProvType[] getExplicitTypes(QualifiedName context, QualifiedName qname) {
	List<Statement> statements = collators.get(context).get(qname);
	List<Types.ProvType> explicitOptions = new ArrayList<Types.ProvType>();
	for (Statement statement : statements) {
	    QualifiedName predQ = convertURIToQualifiedName(statement.getPredicate());
	    if (predQ.equals(onto.QNAME_RDF_TYPE)) {
		Value value = statement.getObject();
		if (!(value instanceof URI)) {
		    continue;
		}
		if (!isProvURI(convertURIToQualifiedName((URI) value))) {
		    continue;
		}
		Types.ProvType provType = types.lookup(convertURIToQualifiedName((URI) value));
		if (provType != null)
		    explicitOptions.add(provType);
	    }
	}

	// Prunes back to the 'top' class (e.g. if we have Person and Agent, we
	// only need Person)
	if (explicitOptions.size() > 1) {
	    List<Types.ProvType> cloned = new ArrayList<Types.ProvType>(explicitOptions);
	    for (Types.ProvType option : explicitOptions) {
		for (Types.ProvType extended : types.getExtends(option)) {
		    cloned.remove(extended);
		}
	    }
	    explicitOptions = cloned;
	}

	return explicitOptions.toArray(new Types.ProvType[] {});
    }

    protected QualifiedName convertResourceToQualifiedName(Resource resource) {
	if (resource instanceof URI) {
	    return convertURIToQualifiedName((URI) resource);
	} else if (resource instanceof BNode) {
	    return bnodeToQualifiedName((BNode) resource);
	} else {
	    return null;
	}
    }

    protected Object valueToObject(Value value) {
	if (value instanceof Resource) {
	    return convertResourceToQualifiedName((Resource) value);
	} else if (value instanceof Literal) {
	    return decodeLiteral((Literal) value);
	} else if (value instanceof URI) {
	    URI uri = (URI) (value);
	    return uri.toString();
	} else if (value instanceof BNode) {
	    return bnodeToQualifiedName(value); //FIXME
	} else {
	    return null;
	}
    }

    public QualifiedName bnodeToQualifiedName(Value value) {
	return pFactory.newQualifiedName("http://bnodeNS/", ((BNode) (value)).getID(), null);
    }


    // code replicated from valueToObject
    protected Key valueToKey(Value value) {
	if (value instanceof Resource) {
	    return pFactory.newKey(convertResourceToQualifiedName((Resource) value),
	                           name.XSD_QNAME);
	} else if (value instanceof Literal) {
	    Literal lit=(Literal) value;
	    QualifiedName type;
	    QualifiedName xsdtype;
	    if (lit.getDatatype()!=null) {
		type= convertURIToQualifiedName(lit.getDatatype());
		xsdtype=onto.convertFromRdf(type);
	    } else {
		xsdtype=name.PROV_LANG_STRING;
	    }
		    
	    Object o=decodeLiteral(lit);
	    if (o instanceof QualifiedName) {
		return pFactory.newKey(o, name.XSD_QNAME);
	    }
	    // Was old code, relying on converter
	    //return pFactory.newKey(o, this.valueConverter.getXsdType(o));
	    return pFactory.newKey(o, xsdtype);

	} else if (value instanceof URI) {
	    URI uri = (URI) (value);
	    return pFactory.newKey(uri.toString(), name.XSD_QNAME);
	} else if (value instanceof BNode) {
	    return pFactory.newKey(bnodeToQualifiedName(value), //FIXME
	                           name.XSD_QNAME);
	} else {
	    return null;
	}
    }

    Namespace namespace;
    protected Object decodeLiteral(Literal literal) {
	
	//System.out.println("+++-----> Literal " + literal.getDatatype());
	    //System.out.println("+++--------> Literal " + obj2);
	String dataType = NamespacePrefixMapper.XSD_HASH_NS + "string";
	if (literal.getLanguage() != null) {
	    return pFactory.newInternationalizedString(literal.stringValue(),
						       literal.getLanguage());
	}

	if (literal.getDatatype() != null) {
	    if (literal instanceof URI) {
		QualifiedName qname = namespace.stringToQualifiedName(literal.getDatatype()
							       .stringValue(),pFactory);
		dataType = qname.getNamespaceURI() + qname.getLocalPart();
	    } else {
		dataType = literal.getDatatype().stringValue();
	    }
	}

	if (dataType.equals(NamespacePrefixMapper.XSD_HASH_NS + "QName")) {
	    return namespace.stringToQualifiedName(literal.stringValue(), pFactory);
	} else if (dataType.equals(NamespacePrefixMapper.XSD_HASH_NS + "string")) {
	    return literal.stringValue();
	} else if (dataType.equals(NamespacePrefixMapper.XSD_HASH_NS
		+ "dateTime")
		|| dataType.equals(NamespacePrefixMapper.XSD_HASH_NS + "time")
		|| dataType.equals(NamespacePrefixMapper.XSD_HASH_NS + "date")
		|| dataType.equals(NamespacePrefixMapper.XSD_HASH_NS
			+ "gYearMonth")
		|| dataType.equals(NamespacePrefixMapper.XSD_HASH_NS
			+ "gMonthDay")
		|| dataType.equals(NamespacePrefixMapper.XSD_HASH_NS + "gYear")
		|| dataType.equals(NamespacePrefixMapper.XSD_HASH_NS + "gMonth")
		|| dataType.equals(NamespacePrefixMapper.XSD_HASH_NS + "gDay")) {
	    return literal.calendarValue();
	} else if (dataType.equals(NamespacePrefixMapper.XSD_HASH_NS + "int")) {
	    return literal.intValue();
	} else if (dataType.equals(NamespacePrefixMapper.XSD_HASH_NS
		+ "integer")) {
	    return literal.integerValue();
	} else if (dataType.equals(NamespacePrefixMapper.XSD_HASH_NS
		+ "boolean")) {
	    return literal.booleanValue();
	} else if (dataType.equals(NamespacePrefixMapper.XSD_HASH_NS + "double")) {
	    return literal.doubleValue();
	} else if (dataType.equals(NamespacePrefixMapper.XSD_HASH_NS + "float")) {
	    return literal.floatValue();
	} else if (dataType.equals(NamespacePrefixMapper.XSD_HASH_NS + "long")) {
	    return literal.longValue();
	} else if (dataType.equals(NamespacePrefixMapper.XSD_HASH_NS + "short")) {
	    return literal.shortValue();
	} else if (dataType.equals(NamespacePrefixMapper.XSD_HASH_NS + "byte")) {
	    return literal.byteValue();
	} else if (dataType.equals(NamespacePrefixMapper.XSD_HASH_NS
		+ "decimal")) {
	    return literal.decimalValue();
	} else if (dataType.equals(NamespacePrefixMapper.XSD_HASH_NS + "anyURI")) {
	    return literal.stringValue();
	} else {
	    return literal.stringValue();
	}
    }
    


    private void handleTypes(Types.ProvType[] types, QualifiedName context, QualifiedName subject) {
	for (Types.ProvType type : types) {
	    switch (type) {
	    case ACTIVITY:
		createActivity(context, subject);
		break;
	    case AGENT:
	    case PERSON:
	    case ORGANIZATION:
	    case SOFTWAREAGENT:
		createAgent(context, subject);
		break;
	    case COLLECTION:
	    case DICTIONARY:
	    case ENTITY:
	    case PLAN:
	    case BUNDLE:
		createEntity(context, subject);
		break;
	    default:
	    }
	}

	handlePredicates(context, subject);
    }

    protected void buildGraph() {
	for (QualifiedName contextQ : collators.keySet()) {
	    HashMap<QualifiedName, List<Statement>> collator = collators.get(contextQ);
	    for (QualifiedName qname : collator.keySet()) {
		Types.ProvType[] explicitTypes = getExplicitTypes(contextQ, qname);
		handleTypes(explicitTypes, contextQ, qname);
	    }
	}
    }

    protected void buildBundles() {
	// Add 'default' bundle
	if (bundles.containsKey(emptyQualifiedName())) { //FIXME
	    BundleHolder defaultBundle = bundles.get(emptyQualifiedName()); //FIXME
	    for (org.openprovenance.prov.model.Activity activity : defaultBundle.getActivities()) {
		document.getStatementOrBundle().add(activity);
	    }

	    for (org.openprovenance.prov.model.Agent agent : defaultBundle.getAgents()) {
		document.getStatementOrBundle().add(agent);
	    }

	    for (org.openprovenance.prov.model.Entity entity : defaultBundle.getEntities()) {
		document.getStatementOrBundle().add(entity);
	    }

	    for (org.openprovenance.prov.model.Statement statement : defaultBundle.getStatements()) {
		document.getStatementOrBundle().add(statement);
	    }
	}

	for (QualifiedName key : bundles.keySet()) {
	    if (key.getLocalPart().equals("")) {
		continue;
	    }
	    BundleHolder bundleHolder = bundles.get(key);
	    Collection<org.openprovenance.prov.model.Statement> statements = new ArrayList<org.openprovenance.prov.model.Statement>();
	    statements.addAll(bundleHolder.getActivities());
	    statements.addAll(bundleHolder.getEntities());
	    statements.addAll(bundleHolder.getAgents());
	    statements.addAll(bundleHolder.getStatements());
	    NamedBundle bundle = pFactory.newNamedBundle(key, null, statements);
	    document.getStatementOrBundle().add(bundle);
	}

    }

    public QualifiedName emptyQualifiedName() {
	return pFactory.newQualifiedName("","",null);//FIXME
    }


    
    protected QualifiedName convertURIToQualifiedName(URI uri) {
	QualifiedName qname;
	String uriNamespace = uri.getNamespace();
	String prefix=namespace.getNamespaces().get(uriNamespace);
	String uriLocalName = uri.getLocalName();
	if (prefix!=null) {
	    qname = pFactory.newQualifiedName(uriNamespace, uriLocalName, prefix);
	} else {
	    String defaultNS=namespace.getDefaultNamespace();
	    if ((defaultNS!=null) && (defaultNS.equals(uriNamespace))) {
		qname = pFactory.newQualifiedName(uriNamespace, uriLocalName,null);
	    } else {
		namespace.newPrefix(uriNamespace);
		String pref=namespace.getNamespaces().get(uriNamespace);
		qname = pFactory.newQualifiedName(uriNamespace, uriLocalName, pref);
	    }
	}	
	return qname;
    }

    /*
     * Handles all attributes (location, role, type, etc). Returns a list of
     * Attribute objects.
     */
    public List<Attribute> collectAttributes(QualifiedName context, QualifiedName qname,
					     Types.ProvType type) {
	List<Attribute> attributes = new ArrayList<Attribute>();
	List<Statement> statements = collators.get(context).get(qname);
	for (Statement statement : statements) {
	    QualifiedName predQ = convertURIToQualifiedName(statement.getPredicate());
	    Value value = statement.getObject();

	    if (statement.getPredicate().equals(RDF.TYPE)) {
		Object obj = valueToObject(statement.getObject());
		if (obj != null) {

		    Value vobj = statement.getObject();
		    Boolean sameAsType = false;
		    if (vobj instanceof URI) {
			// TODO: Nasty.
			URI uri = (URI) (vobj);

			String uriVal = uri.getNamespace() + uri.getLocalName();
			sameAsType = uriVal.equals(types.find(type));
		    }

		    if (!sameAsType) {

			if (statement.getObject() instanceof Resource) {
			    QualifiedName typeQ = convertResourceToQualifiedName((Resource) statement.getObject());

			    if (isProvURI(typeQ)
				    && DM_TYPES.indexOf(typeQ) == -1) {
				// System.out.println("Skipping type: " + typeQ);
			    } else {
				attributes.add(pFactory.newAttribute(name.PROV_TYPE,
								     typeQ,
								     name.XSD_QNAME));
			    }

			} else if (statement.getObject() instanceof Literal) {	   
			    Literal lit=(Literal)statement.getObject();
			    Attribute attr = newAttribute(lit,name.PROV_TYPE);
			    attributes.add(attr);
			}
		    }
		} else {
		    System.out.println(value);
		    System.out.println("Value wasn't a suitable type");
		}
	    }

	    if (predQ.equals(onto.QNAME_PROVO_hadRole)) {
		Value obj=statement.getObject();

		Attribute attr = newAttributeForValue(obj,name.PROV_ROLE);
		attributes.add(attr);
	    }

	    if (predQ.equals(onto.QNAME_PROVO_atLocation)) {
		Value obj=statement.getObject();
		Attribute attr = newAttributeForValue(obj,name.PROV_LOCATION);
		attributes.add(attr);

	    }

	    if (predQ.equals(onto.QNAME_RDFS_LABEL)) {
		Literal lit = (Literal) (statement.getObject());
		Attribute attr=newAttribute(lit, name.PROV_LABEL);
		attributes.add(attr);		
	    }
	    if (predQ.equals(onto.QNAME_PROVO_value)) {
		Attribute attr=newAttributeForValue(value, name.PROV_VALUE);
		attributes.add(attr);
	    }
	    if (!isProvURI(predQ)) {
		if (!predQ.equals(onto.QNAME_RDF_TYPE)
			&& !predQ.equals(onto.QNAME_RDFS_LABEL)) {
		    Attribute attr = newAttributeForValue(value, predQ);
		    attributes.add(attr);
		}
	    }
	}
	return attributes;
    }

    public Attribute newAttributeForValue(Value obj, QualifiedName type) {
	Attribute attr;
	if (obj instanceof Literal) {			
	    attr=newAttribute((Literal)obj,type);
	} else if (obj instanceof Resource) {
	    attr=pFactory.newAttribute(type,
	                               convertResourceToQualifiedName((Resource) obj),
	                               name.XSD_QNAME);
	} else {
	    throw new UnsupportedOperationException();
	}
	return attr;
    }

    public Attribute newAttribute(Literal lit, QualifiedName type) {
	Object theValue;
	if (lit.getLanguage() != null) {
	    theValue = pFactory.newInternationalizedString(lit.stringValue(),
							   lit.getLanguage());
	} else {
	    theValue = lit.stringValue();
	}
	Attribute attr = pFactory.newAttribute(type,
					       theValue,
					       ((lit.getDatatype() == null) ? 
					               ((lit.getLanguage()==null)
					                       ? name.XSD_STRING
					                       : name.PROV_LANG_STRING)
						       : onto.convertFromRdf(convertURIToQualifiedName(lit.getDatatype()))));
	return attr;
    }

    /*
     * Handles PROV-O predicates, creating beans where appropriate.
     */
    private void handlePredicates(QualifiedName context, QualifiedName qname) {
	List<QualifiedName> members = new ArrayList<QualifiedName>();
	List<QualifiedName> dictionaryMembers = new ArrayList<QualifiedName>();

	List<Statement> statements = collators.get(context).get(qname);
	for (Statement statement : statements) {
	    QualifiedName predQ = convertURIToQualifiedName(statement.getPredicate());
	    Value value = statement.getObject();

	    if (predQ.equals(onto.QNAME_PROVO_wasInfluencedBy)) {
		QualifiedName anyQ = convertResourceToQualifiedName((Resource) (statement.getObject()));
		WasInfluencedBy wib = pFactory.newWasInfluencedBy(null, qname,
								  anyQ, null);

		store(convertResourceToQualifiedName(statement.getContext()), wib);
	    }

	    if (predQ.equals(onto.QNAME_PROVO_influenced)) {
		QualifiedName anyQ = convertResourceToQualifiedName((Resource) (statement.getObject()));

		WasInfluencedBy wib = pFactory.newWasInfluencedBy(null, anyQ,
								  qname, null);

		store(convertResourceToQualifiedName(statement.getContext()), wib);
	    }

	    if (value instanceof Resource) {
		QualifiedName valueQ = convertResourceToQualifiedName((Resource) value);
		if (predQ.equals(onto.QNAME_PROVO_wasDerivedFrom)) {
		    WasDerivedFrom wdf = pFactory.newWasDerivedFrom((QualifiedName) null,
								    qname,
								    valueQ,
								    null, null,
								    null, null);

		    store(context, wdf);
		} else if (predQ.equals(onto.QNAME_PROVO_hadPrimarySource)) {
		    WasDerivedFrom wdf = pFactory.newWasDerivedFrom(null,
								    qname,
								    valueQ,
								    null, null,
								    null, null);

		    pFactory.addPrimarySourceType(wdf);
		    store(context, wdf);
		} else if (predQ.equals(onto.QNAME_PROVO_wasQuotedFrom)) {
		    WasDerivedFrom wdf = pFactory.newWasDerivedFrom(null,
								    qname,
								    valueQ,
								    null, null,
								    null, null);
		    pFactory.addQuotationType(wdf);
		    store(context, wdf);
		} else if (predQ.equals(onto.QNAME_PROVO_wasRevisionOf)) {
		    WasDerivedFrom wdf = pFactory.newWasDerivedFrom(null,
								    qname,
								    valueQ,
								    null, null,
								    null, null);
		    pFactory.addRevisionType(wdf);
		    store(context, wdf);
		} else if (predQ.equals(onto.QNAME_PROVO_wasGeneratedBy)) {
		    WasGeneratedBy wgb = pFactory.newWasGeneratedBy(null,
								    qname,
								    valueQ,
								    null, null);

		    store(context, wgb);
		} else if (predQ.equals(onto.QNAME_PROVO_alternateOf)) {
		    AlternateOf ao = pFactory.newAlternateOf(qname, valueQ);

		    store(context, ao);
		} else if (predQ.equals(onto.QNAME_PROVO_specializationOf)) {
		    SpecializationOf so = pFactory.newSpecializationOf(qname,
								       valueQ);

		    store(context, so);
		} else if (predQ.equals(onto.QNAME_PROVO_wasInvalidatedBy)) {
		    WasInvalidatedBy wib = pFactory.newWasInvalidatedBy(null,
									qname,
									valueQ,
									null,
									null);

		    store(context, wib);
		} else if (predQ.equals(onto.QNAME_PROVO_wasAttributedTo)) {
		    WasAttributedTo wit = pFactory.newWasAttributedTo(null,
								      qname,
								      valueQ,
								      null);

		    store(context, wit);
		} else if (predQ.equals(onto.QNAME_PROVO_mentionOf)) {
		    Statement asInBundleStatement = getSingleStatementForPredicate(context,
										   qname,
										   onto.QNAME_PROVO_asInBundle);
		    Object o = (asInBundleStatement == null) ? null
			    : asInBundleStatement.getObject();
		    QualifiedName bundleQ = (o == null) ? null
			    : convertURIToQualifiedName((URI) o);
		    QualifiedName entityQ = (value == null) ? null
			    : convertURIToQualifiedName((URI) value);
		    MentionOf mo = pFactory.newMentionOf(entityQ, qname,
							 bundleQ);

		    store(context, mo);
		} else if (predQ.equals(onto.QNAME_PROVO_wasAssociatedWith)) {
		    WasAssociatedWith waw = pFactory.newWasAssociatedWith(null,
									  qname,
									  valueQ,
									  null,
									  null);

		    store(context, waw);

		} else if (predQ.equals(onto.QNAME_PROVO_used)) {
		    Used used = pFactory.newUsed(null, qname,valueQ, null,
						 null);
		    store(context, used);

		} else if (predQ.equals(onto.QNAME_PROVO_wasStartedBy)) {
		    WasStartedBy wsb = pFactory.newWasStartedBy(null, qname,
								valueQ, null,
								null, null);
		    store(context, wsb);

		} else if (predQ.equals(onto.QNAME_PROVO_generated)) {
		    WasGeneratedBy wgb = pFactory.newWasGeneratedBy(null,
								    valueQ,
								    qname,
								    null, null);
		    store(context, wgb);

		} else if (predQ.equals(onto.QNAME_PROVO_wasEndedBy)) {
		    WasEndedBy web = pFactory.newWasEndedBy(null, qname,
							    valueQ, null, null,
							    null);
		    store(context, web);

		} else if (predQ.equals(onto.QNAME_PROVO_wasInformedBy)) {
		    WasInformedBy wib = pFactory.newWasInformedBy(null, qname,
								  valueQ, null);
		    store(context, wib);
		} else if (predQ.equals(onto.QNAME_PROVO_actedOnBehalfOf)) {
		    QualifiedName agentQ = convertResourceToQualifiedName((Resource) value);
		    ActedOnBehalfOf aobo = pFactory.newActedOnBehalfOf(null,
								       qname,
								       agentQ,
								       null,
								       null);

		    store(context, aobo);
		} else if (predQ.equals(onto.QNAME_PROVO_hadMember)) {
		    members.add(convertResourceToQualifiedName((Resource) (statement.getObject())));
		} else if (predQ.equals(onto.QNAME_PROVO_hadDictionaryMember)) {
		    dictionaryMembers.add(convertResourceToQualifiedName((Resource) (statement.getObject())));
		}
	    }
	}

	if (members.size() > 0) {
	    HadMember hm = pFactory.newHadMember(qname, members);
	    store(context, hm);
	}
	if (dictionaryMembers.size() > 0) {
	    List<Entry> pairs = createKeyEntityPairs(context,
							    dictionaryMembers);

	    DictionaryMembership hm = pFactory.newDictionaryMembership(qname,
								       pairs);
	    store(context, hm);
	}
    }

    protected List<Value> getDataObjects(QualifiedName context, 
                                         QualifiedName subject,
					 QualifiedName pred) {
	List<Statement> statements = collators.get(context).get(subject);
	List<Value> objects = new ArrayList<Value>();
	for (Statement statement : statements) {
	    QualifiedName predQ = convertURIToQualifiedName(statement.getPredicate());
	    Value value = statement.getObject();
	    if (pred.equals(predQ) && (!(value instanceof Resource))) {
		objects.add(value);
	    }
	}
	return objects;
    }

    protected List<Entry> createKeyEntityPairs(QualifiedName context,
						      List<QualifiedName> pairs) {
	List<Entry> result = new LinkedList<Entry>();
	for (QualifiedName pair : pairs) {
	    List<Value> keys = getDataObjects(context, pair,
					      onto.QNAME_PROVO_pairKey); // key
									     // is
									     // data
									     // property!

	    List<QualifiedName> entities = getObjects(context, pair,
					      onto.QNAME_PROVO_pairEntity);
	    Key key=null;
	    QualifiedName name=null;
	    if (!keys.isEmpty())
		key = valueToKey(keys.get(0)); // we ignore the others
	    if (!entities.isEmpty())
		name = entities.get(0); // we ignore the others
	    Entry p=pFactory.newEntry(key, name);

	    result.add(p);
	}
	return result;
    }

    protected List<QualifiedName> getObjects(QualifiedName context, QualifiedName subject, QualifiedName pred) {
	List<Statement> statements = collators.get(context).get(subject);
	List<QualifiedName> objects = new ArrayList<QualifiedName>();
	for (Statement statement : statements) {
	    QualifiedName predQ = convertURIToQualifiedName(statement.getPredicate());
	    Value value = statement.getObject();
	    if (pred.equals(predQ) && value instanceof Resource) {
		objects.add(convertResourceToQualifiedName((Resource) value));
	    }
	}
	return objects;
    }

    private void createEntity(QualifiedName context, QualifiedName qname) {
	List<Attribute> attributes = collectAttributes(context, 
	                                               qname,
						       Types.ProvType.ENTITY);

	org.openprovenance.prov.model.Entity entity = pFactory.newEntity(qname,
									 attributes);
	getBundleHolder(context).store(entity);
    }

    private void createAgent(QualifiedName context, QualifiedName qname) {
	List<Attribute> attributes = collectAttributes(context, qname,
						       Types.ProvType.AGENT);

	org.openprovenance.prov.model.Agent agent = pFactory.newAgent(qname,
								      attributes);
	getBundleHolder(context).store(agent);
    }

    private void createActivity(QualifiedName context, QualifiedName qname) {
	List<Attribute> attributes = collectAttributes(context, qname,
						       Types.ProvType.ACTIVITY);
	List<Statement> statements = collators.get(context).get(qname);

	XMLGregorianCalendar startTime = null;
	XMLGregorianCalendar endTime = null;

	for (Statement statement : statements) {
	    QualifiedName predQ = convertURIToQualifiedName(statement.getPredicate());
	    Value value = statement.getObject();
	    if (value instanceof Literal) {
		if (predQ.equals(onto.QNAME_PROVO_startedAtTime)) {
		    Object literal = decodeLiteral((Literal) value);
		    startTime = (XMLGregorianCalendar) literal;
		} else if (predQ.equals(onto.QNAME_PROVO_endedAtTime)) {
		    Object literal = decodeLiteral((Literal) value);
		    endTime = (XMLGregorianCalendar) literal;
		}
	    }
	}

	org.openprovenance.prov.model.Activity activity = pFactory.newActivity(qname,
									       startTime,
									       endTime,
									       attributes);
	getBundleHolder(context).store(activity);
    }

    public Document getDocument() {
	return document;
    }

    /**
     * RDFHandlerBase overrides
     */

    @Override
    public void handleNamespace(String prefix, String namespace) {
	if (prefix.equals("")) {
	    prefix = "def";
	}
	this.document.getNamespace().register(prefix, namespace);
	//pFactory.setNamespaces(this.document.getNss());
	//this.revnss.put(namespace, prefix);
    }

  

    protected QualifiedName bnodeToQualifiedName(BNode bnode) {
	return pFactory.newQualifiedName(BNODE_NS, bnode.getID(), "bnode");
    }

    @Override
    public void handleStatement(Statement statement) {
	QualifiedName subjectQ = null;

	QualifiedName contextQ = convertResourceToQualifiedName(statement.getContext());

	Resource subject = statement.getSubject();
	if (subject instanceof BNodeImpl) {
	    subjectQ = bnodeToQualifiedName((BNodeImpl) subject);
	} else if (subject instanceof URI) {
	    URI uri = (URI) subject;
	    subjectQ = convertURIToQualifiedName(uri);
	} else {
	    System.err.println("Invalid subject resource");
	}

	if (!collators.containsKey(contextQ)) {
	    collators.put(contextQ, new HashMap<QualifiedName, List<Statement>>());
	}

	HashMap<QualifiedName, List<Statement>> currcollator = getCollator(statement.getContext());

	if (!currcollator.containsKey(subjectQ)) {
	    currcollator.put(subjectQ, new ArrayList<Statement>());
	}

	currcollator.get(subjectQ).add(statement);
    }

    @Override
    public void endRDF() {
	buildGraph();
	buildBundles();
    }
}
