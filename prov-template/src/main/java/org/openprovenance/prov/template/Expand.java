package org.openprovenance.prov.template;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.model.QualifiedName;
import org.openprovenance.prov.model.Statement;
import org.openprovenance.prov.xml.ProvUtilities;

public class Expand {
    static final String VAR_NS = "http://openprovenance.org/var#";
    static final String APP_NS = "http://openprovenance.org/app#";

    
    Document expand(Document template,
                    Bindings bindings) {
	return null;
	
    }
    
    ProvUtilities u=new ProvUtilities();
    ProvFactory pf=new org.openprovenance.prov.xml.ProvFactory();

    public void expand(Statement statement, 
                       Bindings bindings1,
		       Groupings grp1, 
		       Using us1) {
	Iterator<List<Integer>> iter=us1.iterator();
	while (iter.hasNext()) {
	    List<Integer> index=iter.next();
	    System.out.println("$$ " + index);
	    
	    Hashtable<QualifiedName, QualifiedName> env=us1.get(bindings1, grp1, index);
	    
	    System.out.println("" + env);

	    for (int i = 0; i < u.getFirstTimeIndex(statement); i++) {
		QualifiedName qual=(QualifiedName) u.getter(statement, i);
		if (qual!=null) {
		    System.out.println("Found " + qual + " --> " + env.get(qual));
		}
	    }
	    ExpandAction action=new ExpandAction(pf, u, this, env, index);
	    u.doAction(statement, action);
	    System.out.println(action.getList());
	    
	}
	
    }

    public boolean isVariable(QualifiedName id) {
	return (id!=null) && (VAR_NS.equals(id.getNamespaceURI()));
    }

}
