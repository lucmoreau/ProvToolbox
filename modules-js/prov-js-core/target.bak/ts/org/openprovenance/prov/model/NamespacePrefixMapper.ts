/* Generated from Java with JSweet 3.0.0 - http://www.jsweet.org */
namespace org.openprovenance.prov.model {
    /**
     * Namespace and prefix declarations for common namespaces manipulated by ProvToolbox. API to
     * Sun's NamespacePrefixMapper
     * @author lavm
     * @see <a href="http://grepcode.com/file/repo1.maven.org/maven2/com.sun.xml.bind/jaxb-impl/2.1.11/com/sun/xml/bind/marshaller/NamespacePrefixMapper.java">JAXB NamespacePrefixMapper</a>
     * @class
     */
    export interface NamespacePrefixMapper {
        /**
         * Returns a preferred prefix for the given namespace URI.
         * 
         * This method is intended to be overrided by a derived class.
         * 
         * @param {string} namespaceUri
         * The namespace URI for which the prefix needs to be found.
         * Never be null. "" is used to denote the default namespace.
         * @param {string} suggestion
         * When the content tree has a suggestion for the prefix
         * to the given namespaceUri, that suggestion is passed as a
         * parameter. Typically this value comes from QName.getPrefix()
         * to show the preference of the content tree. This parameter
         * may be null, and this parameter may represent an already
         * occupied prefix.
         * @param {boolean} requirePrefix
         * If this method is expected to return non-empty prefix.
         * When this flag is true, it means that the given namespace URI
         * cannot be set as the default namespace.
         * 
         * @return
         * null if there's no preferred prefix for the namespace URI.
         * In this case, the system will generate a prefix for you.
         * 
         * Otherwise the system will try to use the returned prefix,
         * but generally there's no guarantee if the prefix will be
         * actually used or not.
         * 
         * return "" to map this namespace URI to the default namespace.
         * Again, there's no guarantee that this preference will be
         * honored.
         * 
         * If this method returns "" when requirePrefix=true, the return
         * value will be ignored and the system will generate one.
         * @return {string}
         */
        getPreferredPrefix(namespaceUri: string, suggestion: string, requirePrefix: boolean): string;

        /**
         * Returns a list of namespace URIs that should be declared
         * at the root element.
         * <p>
         * By default, the JAXB RI produces namespace declarations only when
         * they are necessary, only at where they are used. Because of this
         * lack of look-ahead, sometimes the marshaller produces a lot of
         * namespace declarations that look redundant to human eyes. For example,
         * <pre>&lt;xmp&gt;
         * &lt;?xml version="1.0"?&gt;
         * &lt;root&gt;
         * &lt;ns1:child xmlns:ns1="urn:foo"&gt; ... &lt;/ns1:child&gt;
         * &lt;ns2:child xmlns:ns2="urn:foo"&gt; ... &lt;/ns2:child&gt;
         * &lt;ns3:child xmlns:ns3="urn:foo"&gt; ... &lt;/ns3:child&gt;
         * ...
         * &lt;/root&gt;
         * &lt;/xmp&gt;</pre>
         * <p>
         * If you know in advance that you are going to use a certain set of
         * namespace URIs, you can override this method and have the marshaller
         * declare those namespace URIs at the root element.
         * <p>
         * For example, by returning <code>new String[]{"urn:foo"}</code>,
         * the marshaller will produce:
         * <pre>&lt;xmp&gt;
         * &lt;?xml version="1.0"?&gt;
         * &lt;root xmlns:ns1="urn:foo"&gt;
         * &lt;ns1:child&gt; ... &lt;/ns1:child&gt;
         * &lt;ns1:child&gt; ... &lt;/ns1:child&gt;
         * &lt;ns1:child&gt; ... &lt;/ns1:child&gt;
         * ...
         * &lt;/root&gt;
         * &lt;/xmp&gt;</pre>
         * <p>
         * To control prefixes assigned to those namespace URIs, use the
         * {@link #getPreferredPrefix} method.
         * 
         * @return
         * A list of namespace URIs as an array of {@link String}s.
         * This method can return a length-zero array but not null.
         * None of the array component can be null. To represent
         * the empty namespace, use the empty string <code>""</code>.
         * 
         * @since
         * JAXB RI 1.0.2
         * @return {java.lang.String[]}
         */
        getPreDeclaredNamespaceUris(): string[];
    }

    export namespace NamespacePrefixMapper {

        export const PROV_EXT_NS: string = "http://openprovenance.org/prov/extension#";

        export const PROV_NS: string = "http://www.w3.org/ns/prov#";

        export const XSI_NS: string = "http://www.w3.org/2001/XMLSchema-instance";

        export const XSD_NS: string = "http://www.w3.org/2001/XMLSchema#";

        export const PRINTER_NS: string = "http://openprovenance.org/model/opmPrinterConfig";

        export const XML_NS: string = "http://www.w3.org/XML/1998/namespace";

        export const PROV_PREFIX: string = "prov";

        export const XSD_PREFIX: string = "xsd";

        export const RDF_NS: string = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";

        export const RDF_PREFIX: string = "rdf";

        export const RDFS_NS: string = "http://www.w3.org/2000/01/rdf-schema#";

        export const RDFS_PREFIX: string = "rdfs";

        export const BOOK_PREFIX: string = "bk";

        export const BOOK_NS: string = "http://www.provbook.org/ns/#";

        export const SHARED_PROV_TOOLBOX_PREFIX: string = "http://openprovenance.org/provtoolbox/";

        export let TOOLBOX_NS: string; export function TOOLBOX_NS_$LI$(): string { if (NamespacePrefixMapper.TOOLBOX_NS == null) { NamespacePrefixMapper.TOOLBOX_NS = NamespacePrefixMapper.SHARED_PROV_TOOLBOX_PREFIX + "ns#"; }  return NamespacePrefixMapper.TOOLBOX_NS; };

        export const TOOLBOX_PREFIX: string = "box";

        export let DOT_NS: string; export function DOT_NS_$LI$(): string { if (NamespacePrefixMapper.DOT_NS == null) { NamespacePrefixMapper.DOT_NS = NamespacePrefixMapper.SHARED_PROV_TOOLBOX_PREFIX + "dot/ns#"; }  return NamespacePrefixMapper.DOT_NS; };

        export const DOT_PREFIX: string = "dot";

        export let SUMMARY_NS: string; export function SUMMARY_NS_$LI$(): string { if (NamespacePrefixMapper.SUMMARY_NS == null) { NamespacePrefixMapper.SUMMARY_NS = NamespacePrefixMapper.SHARED_PROV_TOOLBOX_PREFIX + "summary/ns#"; }  return NamespacePrefixMapper.SUMMARY_NS; };

        export const SUMMARY_PREFIX: string = "sum";
    }

}

