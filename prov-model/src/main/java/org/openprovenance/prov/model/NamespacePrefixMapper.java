package org.openprovenance.prov.model;

public interface NamespacePrefixMapper {

    static final public String PROV_NS = "http://www.w3.org/ns/prov#";
    static final public String XSI_NS = "http://www.w3.org/2001/XMLSchema-instance";
    static final public String XSD_NS = "http://www.w3.org/2001/XMLSchema";
    static final public String XSD_HASH_NS = "http://www.w3.org/2001/XMLSchema#";
    static final public String PRINTER_NS = "http://openprovenance.org/model/opmPrinterConfig";
    static final public String XML_NS = "http://www.w3.org/XML/1998/namespace";
    static final public String PROV_PREFIX = "prov";
    static final public String XSD_PREFIX = "xsd";
    static final public String RDF_NS = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
    static final public String RDF_PREFIX = "rdf";
    static final public String RDFS_NS = "http://www.w3.org/2000/01/rdf-schema#";
    static final public String RDFS_PREFIX = "rdfs";
    static final public String BOOK_PREFIX = "bk";
    static final public String BOOK_NS = "http://www.provbook.org/ns/#";
    public static final String TOOLBOX_NS = "http://openprovenance.org/toolbox/";

    /**
     * Returns a preferred prefix for the given namespace URI.
     *
     * This method is intended to be overrided by a derived class.
     *
     * @param namespaceUri
     *      The namespace URI for which the prefix needs to be found.
     *      Never be null. "" is used to denote the default namespace.
     * @param suggestion
     *      When the content tree has a suggestion for the prefix
     *      to the given namespaceUri, that suggestion is passed as a
     *      parameter. Typically this value comes from QName.getPrefix()
     *      to show the preference of the content tree. This parameter
     *      may be null, and this parameter may represent an already
     *      occupied prefix.
     * @param requirePrefix
     *      If this method is expected to return non-empty prefix.
     *      When this flag is true, it means that the given namespace URI
     *      cannot be set as the default namespace.
     *
     * @return
     *      null if there's no preferred prefix for the namespace URI.
     *      In this case, the system will generate a prefix for you.
     *
     *      Otherwise the system will try to use the returned prefix,
     *      but generally there's no guarantee if the prefix will be
     *      actually used or not.
     *
     *      return "" to map this namespace URI to the default namespace.
     *      Again, there's no guarantee that this preference will be
     *      honored.
     *
     *      If this method returns "" when requirePrefix=true, the return
     *      value will be ignored and the system will generate one.
     */
    public abstract String getPreferredPrefix(String namespaceUri,
					      String suggestion,
					      boolean requirePrefix);

    /**
     * Returns a list of namespace URIs that should be declared
     * at the root element.
     * <p>
     * By default, the JAXB RI produces namespace declarations only when
     * they are necessary, only at where they are used. Because of this
     * lack of look-ahead, sometimes the marshaller produces a lot of
     * namespace declarations that look redundant to human eyes. For example,
     * <pre><xmp>
     * <?xml version="1.0"?>
     * <root>
     *   <ns1:child xmlns:ns1="urn:foo"> ... </ns1:child>
     *   <ns2:child xmlns:ns2="urn:foo"> ... </ns2:child>
     *   <ns3:child xmlns:ns3="urn:foo"> ... </ns3:child>
     *   ...
     * </root>
     * <xmp></pre>
     * <p>
     * If you know in advance that you are going to use a certain set of
     * namespace URIs, you can override this method and have the marshaller
     * declare those namespace URIs at the root element.
     * <p>
     * For example, by returning <code>new String[]{"urn:foo"}</code>,
     * the marshaller will produce:
     * <pre><xmp>
     * <?xml version="1.0"?>
     * <root xmlns:ns1="urn:foo">
     *   <ns1:child> ... </ns1:child>
     *   <ns1:child> ... </ns1:child>
     *   <ns1:child> ... </ns1:child>
     *   ...
     * </root>
     * <xmp></pre>
     * <p>
     * To control prefixes assigned to those namespace URIs, use the
     * {@link #getPreferredPrefix} method.
     *
     * @return
     *      A list of namespace URIs as an array of {@link String}s.
     *      This method can return a length-zero array but not null.
     *      None of the array component can be null. To represent
     *      the empty namespace, use the empty string <code>""</code>.
     *
     * @since
     *      JAXB RI 1.0.2
     */
    public abstract String[] getPreDeclaredNamespaceUris();

}