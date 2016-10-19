package org.openprovenance.prov.template;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.Collections;
import java.util.Collection;

import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.HasOther;
import org.openprovenance.prov.model.Identifiable;
import org.openprovenance.prov.model.Bundle;
import org.openprovenance.prov.model.Other;
import org.openprovenance.prov.model.ProvUtilities;
import org.openprovenance.prov.model.QualifiedName;
import org.openprovenance.prov.model.Statement;

import static org.openprovenance.prov.template.ExpandUtil.LINKED_URI;;

public class Groupings {
    final private List<List<QualifiedName>> variables;
    
    
        static ProvUtilities u= new ProvUtilities();

    
    public Groupings() {
	variables=new LinkedList<List<QualifiedName>>();
    }
    
    public List<QualifiedName> get(int group) {
	return variables.get(group);
    }
    
    public int size() {
	return variables.size();
    }
   
    public void addVariable(QualifiedName name) {
	List<QualifiedName> ll=new LinkedList<QualifiedName>();
	ll.add(name);
	variables.add(ll);
    }

    
    public void addVariable(int group, QualifiedName name) {
	List<QualifiedName> v=variables.get(group);
	v.add(name);
    }
    
    
 
    
    @Override
    public String toString () {
	return "" + variables;
    }
    
    static public Groupings fromDocument(Document doc) {
	Hashtable<QualifiedName,Set<QualifiedName>> linked=new Hashtable<QualifiedName, Set<QualifiedName>>();
	Hashtable<QualifiedName,Integer> linkedGroups=new Hashtable<QualifiedName, Integer>();
		
	Bundle bun=u.getBundle(doc).get(0);
	Groupings grps=new Groupings();
	Set<QualifiedName> allVars=new HashSet<QualifiedName>();
	for (Statement statement: bun.getStatement()) {
	    Set<QualifiedName> vars=ExpandUtil.freeVariables(statement);
	    allVars.addAll(vars);
	    if (statement instanceof HasOther) {
		HasOther stmt2=(HasOther)statement;
		for (Other other: stmt2.getOther()) {
		    if (LINKED_URI.equals(other.getElementName().getUri())) {
			QualifiedName id=((Identifiable)statement).getId();
			QualifiedName otherId=(QualifiedName) other.getValue();
			addEntry(linked, otherId, id);
			addEntry(linked, id, otherId);
		    }
		}
	    }
	}
	// Compute transitive closure
	for(QualifiedName visit: Collections.list(linked.keys())) {
		Stack<QualifiedName> toVisit = new Stack<QualifiedName>();
		toVisit.push(visit);
		Collection<QualifiedName> reachable = new HashSet<QualifiedName>();
		while(toVisit.size()>0) {
			QualifiedName local_qn = toVisit.pop();
			for(QualifiedName neighbour: linked.get(local_qn)) {
				if (!reachable.contains(neighbour)) {
					reachable.add(neighbour);
					toVisit.push(neighbour);
				}
			}
		}
		linked.get(visit).addAll(reachable);
	}

	QualifiedName [] sorted=allVars.toArray(new QualifiedName[0]);
	Arrays.sort(sorted, new Comparator<QualifiedName>() {
	    @Override
	    public int compare(QualifiedName arg0, QualifiedName arg1) {
		return arg0.getUri().compareTo(arg1.getUri());
	    }
	    
	});;
	
	int currentGroup=0;	
	for (QualifiedName qn: sorted) {
	    Set<QualifiedName> links=linked.get(qn);
	    if (links==null || links.isEmpty()) {
		grps.addVariable(qn);
	    } else {
		Integer aGroup=linkedGroups.get(qn);
		if (aGroup!=null) {
		    grps.addVariable(aGroup,qn);
		} else {
		    grps.addVariable(qn);
		    for (QualifiedName otherQn: links) {
			linkedGroups.put(otherQn,currentGroup);
		    }
		}	
	    }
	    currentGroup++;
	}
	
	return grps;
	
    }

    static  void addEntry(Hashtable<QualifiedName, Set<QualifiedName>> linked,
			  QualifiedName id, QualifiedName otherId) {
	if (linked.get(otherId)==null) {
	    linked.put(otherId,new HashSet<QualifiedName>());
	}
	linked.get(otherId).add(id);
    }
	
}
