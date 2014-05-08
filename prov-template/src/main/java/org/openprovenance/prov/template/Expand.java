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
import org.openprovenance.prov.model.NamedBundle;
import org.openprovenance.prov.model.Namespace;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.model.QualifiedName;
import org.openprovenance.prov.model.Statement;
import org.openprovenance.prov.model.StatementOrBundle;
import org.openprovenance.prov.model.TypedValue;
import org.openprovenance.prov.template.Using.UsingIterator;
import org.openprovenance.prov.xml.ProvUtilities;

public class Expand {
    static final String VAR_NS = "http://openprovenance.org/var#";
    static final String VARGEN_NS = "http://openprovenance.org/vargen#";
    static final String TMPL_NS = "http://openprovenance.org/tmpl#";
    static final String TMPL_PREFIX = "tmpl";
    static final String VAR_PREFIX = "var";
    static final String VARGEN_PREFIX = "vargen";
    
    static final String LINKED = "linked";
    static final String LINKED_URI = TMPL_NS + "linked";

    Document expand(Document template, Bindings bindings) {
	return null;

    }

    public Document expander(Document docIn, String out, Document docBindings) {

	NamedBundle bun = (NamedBundle) docIn.getStatementOrBundle().get(0);

	Bindings bindings1 = Bindings.fromDocument(docBindings);

	Groupings grp1 = Groupings.fromDocument(docIn);
	System.out.println("expander: Found groupings " + grp1);

	NamedBundle bun1 = (NamedBundle) expand(bun, bindings1, grp1).get(0);
	Document doc1 = pf.newDocument();
	doc1.getStatementOrBundle().add(bun1);

	bun1.setNamespace(Namespace.gatherNamespaces(bun1));

	doc1.setNamespace(bun1.getNamespace());

	return doc1;
    }

    static ProvUtilities u = new ProvUtilities();
    static ProvFactory pf = new org.openprovenance.prov.xml.ProvFactory();

    public List<StatementOrBundle> expand(Statement statement,
					  Bindings bindings1, Groupings grp1) {
	Using us1 = usedGroups(statement, grp1, bindings1);
	return expand(statement, bindings1, grp1, us1);
    }

    public List<StatementOrBundle> expand(NamedBundle bun, Bindings bindings1,
					  Groupings grp1) {
	Hashtable<QualifiedName, QualifiedName> env0 = new Hashtable<QualifiedName, QualifiedName>();
	Hashtable<QualifiedName, List<TypedValue>> env1 = new Hashtable<QualifiedName, List<TypedValue>>();
	ExpandAction action = new ExpandAction(pf, u, this, env0, env1, null,
					       bindings1, grp1, -1);
	u.doAction(bun, action);
	return action.getList();
    }

    public List<StatementOrBundle> expand(Statement statement,
					  Bindings bindings1, Groupings grp1,
					  Using us1) {
	List<StatementOrBundle> results = new LinkedList<StatementOrBundle>();
	Iterator<List<Integer>> iter = us1.iterator();
	while (iter.hasNext()) {
	    List<Integer> index = iter.next();
	    int attrIndex = ((Using.UsingIterator) iter).getCount();
	    Hashtable<QualifiedName, QualifiedName> env = us1.get(bindings1,
								  grp1, index);
	    Hashtable<QualifiedName, List<TypedValue>> env2;

	    if (IGNORE_ATTRIBUTES) {
		env2 = us1.getAttr(freeAttributeVariables(statement),
				   bindings1, (UsingIterator) iter);
	    } else {
		env2 = us1.getAttr(bindings1, grp1, index);
	    }

	    ExpandAction action = new ExpandAction(pf, u, this, env, env2,
						   index, bindings1, grp1,
						   attrIndex);
	    u.doAction(statement, action);
	    results.addAll(action.getList());

	}
	return results;

    }

    static public boolean IGNORE_ATTRIBUTES = true;

    static public Set<QualifiedName> freeVariables(Statement statement) {
	HashSet<QualifiedName> result = new HashSet<QualifiedName>();
	for (int i = 0; i < u.getFirstTimeIndex(statement); i++) {
	    QualifiedName name = (QualifiedName) u.getter(statement, i);
	    if (name != null && isVariable(name))
		result.add(name);
	}
	if (!IGNORE_ATTRIBUTES) {
	    result.addAll(freeAttributeVariables(statement));
	}
	return result;
    }

    static public HashSet<QualifiedName> freeAttributeVariables(Statement statement) {
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
	    List<QualifiedName> vals = bindings.getVariables().get(vs.get(0));
	    if (vals != null) {
		u.addGroup(g, vals.size());

	    } else {
		List<List<TypedValue>> attrs = bindings.getAttributes()
						       .get(vs.get(0));
		if (attrs != null) {
		    u.addGroup(g, attrs.size());
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
