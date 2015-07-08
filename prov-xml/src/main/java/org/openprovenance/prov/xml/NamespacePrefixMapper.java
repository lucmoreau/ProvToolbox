package org.openprovenance.prov.xml;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.openprovenance.prov.model.Namespace;


/** Prefix definition for PROV serialisations. */

public class NamespacePrefixMapper extends com.sun.xml.bind.marshaller.NamespacePrefixMapper 
                                   implements org.openprovenance.prov.model.NamespacePrefixMapper {

    private Map<String,String> nss=null;

        // Must use 'internal' for Java 6
/* This file is a modification of the NamespacePrefixMapper from docx4j,
   which was licensed under Apache License, version 2.
*/
    String defaultNamespace;

    public NamespacePrefixMapper(Namespace nss) {
        if (nss!=null) {
            this.defaultNamespace=nss.getDefaultNamespace();
        }
        String xsd_prefix=nss.getNamespaces().get(XSD_NS);
        this.nss=new HashMap<String, String>(nss.getPrefixes()); // don't modify the received map
        this.nss.put(xsd_prefix, XSD_NS_FOR_XML);
        
        //System.out.println("PREFIXES IS " + nss);
        //System.out.println("DEFAULT " + defaultNamespace);
    }


       
    /* (non-Javadoc)
     * @see org.openprovenance.prov.model.NamespacePrefixMapper#getPreferredPrefix(java.lang.String, java.lang.String, boolean)
     */
    @Override
    public String getPreferredPrefix(String namespaceUri, String suggestion, boolean requirePrefix) {
        if ((defaultNamespace!=null) && (namespaceUri.equals(defaultNamespace))) {
            return "";
        }

        if (namespaceUri.equals(PROV_NS)) {
            return "prov";
        }
        if (namespaceUri.equals(PRINTER_NS)) {
            return "prn";
        }
   //     if (namespaceUri.equals(XSD_NS)) {
   //         return "xsd";
   //     }
        if (namespaceUri.equals(XSD_NS_FOR_XML)) { // TODO: TO CHECK that's the one we need
            return "xsd";
        }
        if (namespaceUri.equals(XML_NS)) {
            return "xml";
        }
        if (namespaceUri.equals(XSI_NS)) {
            return "xsi";
        }
        for (String k: nss.keySet()) {
            if (nss.get(k).equals(namespaceUri)) {
                return k;
            }
        }
        return suggestion;
    }
   
    /* (non-Javadoc)
     * @see org.openprovenance.prov.model.NAIN#getPreDeclaredNamespaceUris()
     */
    @Override
    public String[] getPreDeclaredNamespaceUris() {
        LinkedList<String> ll=new LinkedList<String>();
        if (nss!=null) {
            ll.addAll(nss.values());
        }
        ll.add(XSI_NS);
        ll.add(XSD_NS_FOR_XML);
        ll.add(XML_NS);
        ll.add(PROV_NS);
        //System.out.println("namespaceprefixmapper " + ll);
        String[] tmp=new String[1];
        return ll.toArray(tmp);
    }
                                      
}
