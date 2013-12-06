package org.openprovenance.prov.sql;
import javax.xml.namespace.QName;
import org.openprovenance.prov.model.QualifiedName;

/* Helper class, to refactor somehow. */

abstract public class Helper2 extends org.openprovenance.prov.xml.Helper {

    public static String QNameToString(QualifiedName element) {
	if (element==null) return null;
	return element.getPrefix()
	    + " "
	    + element.getNamespaceURI()
	    + " "
	    + element.getLocalPart();
    }
    public static QName stringToQName(String s) {
	String []parts=s.split(" ");
	return new QName( parts[1], // namespaceURI
			       parts[2], // localPart
			       parts[0]  // prfix
			       );
    }
    
}
