package org.openprovenance.prov.template;

import java.util.Iterator;
import java.util.List;

import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.QualifiedName;
import org.openprovenance.prov.model.Statement;
import org.openprovenance.prov.xml.ProvUtilities;

public class Expand {
    Document expand(Document template,
                    Bindings bindings) {
	return null;
	
    }
    
    ProvUtilities u=new ProvUtilities();

    public void expand(Statement statement, 
                       Bindings bindings1,
		       Groupings grp1, 
		       Using us1) {
	Iterator<List<Integer>> iter=us1.iterator();
	while (iter.hasNext()) {
	    List<Integer> index=iter.next();
	    System.out.println("Found " + statement);

	    for (int i = 0; i < u.getFirstTimeIndex(statement); i++) {
		QualifiedName qual=(QualifiedName) u.getter(statement, i);
		if (qual!=null) {
		    System.out.println("Found " + qual);
		}
	    }
	    System.out.println("$$ " + index);
	}
	
    }

}
