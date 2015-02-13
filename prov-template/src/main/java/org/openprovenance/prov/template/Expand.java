package org.openprovenance.prov.template;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.openprovenance.prov.model.Attribute;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.Bundle;
import org.openprovenance.prov.model.Namespace;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.model.QualifiedName;
import org.openprovenance.prov.model.Statement;
import org.openprovenance.prov.model.StatementOrBundle;
import org.openprovenance.prov.model.TypedValue;
import org.openprovenance.prov.template.Using.UsingIterator;
import org.openprovenance.prov.xml.ProvUtilities;

public class Expand {
    public static final String VAR_NS = "http://openprovenance.org/var#";
    public static final String VARGEN_NS = "http://openprovenance.org/vargen#";
    public static final String TMPL_NS = "http://openprovenance.org/tmpl#";
    public static final String TMPL_PREFIX = "tmpl";
    public static final String VAR_PREFIX = "var";
    public static final String VARGEN_PREFIX = "vargen";
    
    public static final String LINKED = "linked";
    public static final String LINKED_URI = TMPL_NS + LINKED;
    public static final String LABEL = "label";
    public static final String TIME = "time";
    public static final String STARTTIME = "startTime";
    public static final String ENDTIME = "endTime";
    public static final String LABEL_URI = TMPL_NS + LABEL;
    public static final String TIME_URI = TMPL_NS + TIME;
    public static final String STARTTIME_URI = TMPL_NS + STARTTIME;
    public static final String ENDTIME_URI = TMPL_NS + ENDTIME;
    
    public Expand(ProvFactory pf) {
    	this.pf=pf;
    }

    Document expand(Document template, Bindings bindings) {
	return null;

    }

    public Document expander(Document docIn, String out, Document docBindings) {

	Bundle bun = (Bundle) docIn.getStatementOrBundle().get(0);

	Bindings bindings1 = Bindings.fromDocument(docBindings,pf);

	Groupings grp1 = Groupings.fromDocument(docIn);
	System.out.println("expander: Found groupings " + grp1);

	Bundle bun1 = (Bundle) expand(bun, bindings1, grp1).get(0);
	Document doc1 = pf.newDocument();
	doc1.getStatementOrBundle().add(bun1);

	bun1.setNamespace(Namespace.gatherNamespaces(bun1));

	//doc1.setNamespace(bun1.getNamespace());
	doc1.setNamespace(new Namespace());

	return doc1;
    }

    private final ProvFactory pf ;

    
    static ProvUtilities u = new ProvUtilities();

    public List<StatementOrBundle> expand(Statement statement,
					  Bindings bindings1, Groupings grp1) {
	Using us1 = usedGroups(statement, grp1, bindings1);
	return expand(statement, bindings1, grp1, us1);
    }

    public List<StatementOrBundle> expand(Bundle bun, Bindings bindings1,
					  Groupings grp1) {
	Hashtable<QualifiedName, QualifiedName> env0 = new Hashtable<QualifiedName, QualifiedName>();
	Hashtable<QualifiedName, List<TypedValue>> env1 = new Hashtable<QualifiedName, List<TypedValue>>();
	
	ExpandAction action = new ExpandAction(pf, u, this, env0, env1, null,
					       bindings1, grp1);
	u.doAction(bun, action);
	return action.getList();
    }


    public List<StatementOrBundle> expand(Statement statement,
					  Bindings bindings1, 
					  Groupings grp1,
					  Using us1) {
	List<StatementOrBundle> results = new LinkedList<StatementOrBundle>();
	Iterator<List<Integer>> iter = us1.iterator();
	/*System.out.println(" --------------------- " );
	System.out.println(" Statement " + statement);
	System.out.println(" Using " + us1);
	System.out.println(" Groupings " + grp1);*/
	while (iter.hasNext()) {
	    List<Integer> index = iter.next();
		//System.out.println(" Index " + index);

	    Hashtable<QualifiedName, QualifiedName> env = us1.get(bindings1,
								  grp1, index);
	    Hashtable<QualifiedName, List<TypedValue>> env2;

	    env2 = us1.getAttr(freeAttributeVariables(statement,pf),
				   bindings1, (UsingIterator) iter);

	    ExpandAction action = new ExpandAction(pf, u, this, env, env2,
						   index, bindings1, grp1);
	    u.doAction(statement, action);
	    results.addAll(action.getList());

	}
	return results;

    }


    static public Set<QualifiedName> freeVariables(Statement statement) {
	HashSet<QualifiedName> result = new HashSet<QualifiedName>();
	for (int i = 0; i < u.getFirstTimeIndex(statement); i++) {
	    Object o=u.getter(statement, i);
	    if (o instanceof QualifiedName) {
		QualifiedName name = (QualifiedName) o;
		if (name != null && isVariable(name))
		    result.add(name);
	    } else {
		if (o instanceof List) {
		    List<QualifiedName> ll=(List<QualifiedName>) o;
		    for (QualifiedName name: ll) {
			if (name != null && isVariable(name))
			    result.add(name);
		    }
		}
	    }
	}

	return result;
    }
    static public Set<QualifiedName> freeVariables(Bundle statement) {
	HashSet<QualifiedName> result = new HashSet<QualifiedName>();
	QualifiedName name=statement.getId();
	if (name != null && isVariable(name)) {
	    result.add(name);
	}
	return result;
    }

    static public HashSet<QualifiedName> freeAttributeVariables(Statement statement,ProvFactory pf) {
	HashSet<QualifiedName> result = new HashSet<QualifiedName>();
	Collection<Attribute> ll = pf.getAttributes(statement);
	for (Attribute attr : ll) {
	    if (pf.getName().XSD_QNAME.equals(attr.getType())) {
		Object o = attr.getValue();
		if (o instanceof QualifiedName) {
		    QualifiedName qn = (QualifiedName) o;
		    if (isVariable(qn))
			result.add(qn);
		}
	    }
	}
	return result;
    }

    Using usedGroups(Statement statement, Groupings groupings, Bindings bindings) {
	Set<QualifiedName> vars = freeVariables(statement);
	Set<Integer> groups = new HashSet<Integer>();
	for (QualifiedName var : vars) {
	    for (int grp = 0; grp < groupings.size(); grp++) {
		List<QualifiedName> names = groupings.get(grp);
		if (names.contains(var)) {
		    groups.add(grp);
		}
	    }
	}

	Using u = new Using();
	Integer[] sorted = groups.toArray(new Integer[0]);
	Arrays.sort(sorted);

	for (Integer g : sorted) {
	    List<QualifiedName> vs = groupings.get(g);
	    QualifiedName qn=vs.get(0);
	    List<QualifiedName> vals = bindings.getVariables().get(qn);
	    if (vals != null) {
		u.addGroup(g, vals.size());

	    } else {
		if (isGensymVariable(qn)) {
		    u.addGroup(g,1); // uuid to be generated for this gensym variable
		} else {
		    List<List<TypedValue>> attrs = bindings.getAttributes()
			    .get(vs.get(0));
		    if (attrs != null) {
			u.addGroup(g, attrs.size());
		    }
		}

	    }
	}

	return u;
    }

    static public boolean isVariable(QualifiedName id) {
	if (id==null) return false;
	final String namespaceURI = id.getNamespaceURI();
	return  (VAR_NS.equals(namespaceURI)
		||
		VARGEN_NS.equals(namespaceURI));
    }
    static public boolean isGensymVariable(QualifiedName id) {
	if (id==null) return false;
	final String namespaceURI = id.getNamespaceURI();
	return  VARGEN_NS.equals(namespaceURI);
    }

}
