package org.openprovenance.prov.xml;
import java.util.Hashtable;
import java.util.LinkedList;

import org.openprovenance.prov.model.Namespace;


/** Prefix definition for PROV serialisations. */

public class NamespacePrefixMapper extends com.sun.xml.bind.marshaller.NamespacePrefixMapper {
    
        static final public String PROV_NS="http://www.w3.org/ns/prov#";
        static final public String XSI_NS="http://www.w3.org/2001/XMLSchema-instance";
        static final public String XSD_NS="http://www.w3.org/2001/XMLSchema";
        static final public String XSD_HASH_NS="http://www.w3.org/2001/XMLSchema#";
        static final public String PRINTER_NS="http://openprovenance.org/model/opmPrinterConfig";
        static final public String XML_NS="http://www.w3.org/XML/1998/namespace";
        static final public String PROV_PREFIX = "prov";
        static final public String XSD_PREFIX = "xsd";
        
        static final public String RDF_NS = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
        static final public String RDF_PREFIX = "rdf";
        static final public String RDFS_NS = "http://www.w3.org/2000/01/rdf-schema#";
        static final public String RDFS_PREFIX = "rdfs";
        
        static final public String BOOK_PREFIX = "bk";
        static final public String BOOK_NS = "http://www.provbook.org/ns/#";
    
        
        public static final String TOOLBOX_NS = "http://openprovenance.org/toolbox/";


    private Hashtable<String,String> nss=null;

        // Must use 'internal' for Java 6
/* This file is a modification of the NamespacePrefixMapper from docx4j,
   which was licensed under Apache License, version 2.
*/
    String defaultNamespace;

    public NamespacePrefixMapper(Namespace nss) {
        if (nss!=null) {
            this.defaultNamespace=nss.getDefaultNamespace();
        }
        this.nss=nss.getPrefixes();
        //System.out.println("PREFIXES IS " + nss);
        //System.out.println("DEFAULT " + defaultNamespace);
    }


       
    /* (non-Javadoc)
     * @see org.openprovenance.prov.model.NAIN#getPreferredPrefix(java.lang.String, java.lang.String, boolean)
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
        if (namespaceUri.equals(XSD_NS)) {
            return "xsd";
        }
        if (namespaceUri.equals(XSD_HASH_NS)) {
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
        ll.add(XSD_NS);
        ll.add(XML_NS);
        ll.add(PROV_NS);
        //System.out.println("namespaceprefixmapper " + ll);
        String[] tmp=new String[1];
        return ll.toArray(tmp);
    }
                                      
}
