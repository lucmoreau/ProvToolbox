package org.openprovenance.prov.rdf;

import org.openprovenance.prov.notation.BeanTreeConstructor;
import org.openprovenance.prov.xml.BeanTraversal;
import org.openprovenance.prov.xml.ProvFactory;
import org.openrdf.elmo.ElmoManager;

public class Utility {

	
  public Object convertTreeToJavaRdf(org.openprovenance.prov.xml.Document c, ProvFactory pFactory, ElmoManager manager) throws java.io.IOException, Throwable {
	  RdfConstructor rdfc=new RdfConstructor(pFactory,manager);
	  
	  BeanTraversal bt=new BeanTraversal(new BeanTreeConstructor(rdfc));
      Object o=bt.convert(c);
      return o;
  }
  
}
  
  
