package org.openprovenance.prov.template;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.NamedBundle;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.model.QualifiedName;
import org.openprovenance.prov.model.Statement;
import org.openprovenance.prov.model.StatementOrBundle;
import org.openprovenance.prov.xml.ProvUtilities;
import org.openrdf.query.algebra.In;

public class Expand {
    static final String VAR_NS = "http://openprovenance.org/var#";
    static final String APP_NS = "http://openprovenance.org/app#";

    
    Document expand(Document template,
                    Bindings bindings) {
	return null;
	
    }
    
    ProvUtilities u=new ProvUtilities();
    ProvFactory pf=new org.openprovenance.prov.xml.ProvFactory();

    public List<StatementOrBundle> expand(Statement statement, 
                       Bindings bindings1,
		       Groupings grp1) {
	Using us1=usedGroups(statement, grp1,bindings1);
	return expand(statement, bindings1,grp1,us1);
    }
    
    public List<StatementOrBundle> expand(NamedBundle bun, 
                                          Bindings bindings1,
                                          Groupings grp1) {
	Hashtable<QualifiedName, QualifiedName> env0=new Hashtable<QualifiedName, QualifiedName>();
	ExpandAction action=new ExpandAction(pf, u, this, env0, null, bindings1, grp1);
	u.doAction(bun, action);
	return action.getList();
    }
    
    public List<StatementOrBundle> expand(Statement statement, 
                                          Bindings bindings1,
                                          Groupings grp1,
                                          Using us1){
	List<StatementOrBundle> results=new LinkedList<StatementOrBundle>();
	Iterator<List<Integer>> iter=us1.iterator();
	while (iter.hasNext()) {
	    List<Integer> index=iter.next();	    
	    Hashtable<QualifiedName, QualifiedName> env=us1.get(bindings1, grp1, index);
	    

	    ExpandAction action=new ExpandAction(pf, u, this, env, index, bindings1, grp1);
	    u.doAction(statement, action);
	    results.addAll(action.getList());
	    
	}
	return results;
	
    }
    
    
    
    Set<QualifiedName> freeVariables(Statement statement) {
	HashSet<QualifiedName> result=new HashSet<QualifiedName>();
	 for (int i = 0; i < u.getFirstTimeIndex(statement); i++) {
	     QualifiedName name=(QualifiedName) u.getter(statement, i);
	     if (name!=null) result.add(name);
	 }
	 return result;	
    }

    Using usedGroups(Statement statement,
                             Groupings groupings,
                             Bindings bindings) {
	Set<QualifiedName> vars=freeVariables(statement);
	System.out.println("Vars" + vars);
	Set<Integer> groups=new HashSet<Integer>();
	for (QualifiedName var: vars) {
	    for (int grp=0; grp<groupings.size(); grp++) {
		List<QualifiedName> names=groupings.get(grp);
		if (names.contains(var)) {
		    groups.add(grp);
		}
	    }
	}
	
	Using u=new Using();
	Integer [] sorted=groups.toArray(new Integer[0]);
	Arrays.sort(sorted);
	
	System.out.println("Found sorted " + groups);
	System.out.println("Found groupings " + groupings);
	
	
	for (Integer g: sorted) {
	    List<QualifiedName> vs=groupings.get(g);
	    List<QualifiedName> vals=bindings.getVariables().get(vs.get(0));
	    int len=(vals==null) ? 0 : vals.size();
	    u.addGroup(g, len);    
	}
	// TODO: generate a Using Structure.
	// For each group, get the length of bindings for their variables.
	//return new LinkedList<Integer>(groups);
	return u;
    }
    
    public boolean isVariable(QualifiedName id) {
	return (id!=null) && (VAR_NS.equals(id.getNamespaceURI()));
    }
    
    
    

}
