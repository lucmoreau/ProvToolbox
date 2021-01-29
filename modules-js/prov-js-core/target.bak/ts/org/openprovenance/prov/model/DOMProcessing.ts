/* Generated from Java with JSweet 3.0.0 - http://www.jsweet.org */
namespace org.openprovenance.prov.model {
    /**
     * Utility class for processing DOM {@link Element}, in particular conversion from a PROV attribute to an {@link Element} and vice-versa.
     * 
     * @author lavm
     * @param {org.openprovenance.prov.model.ProvFactory} pFactory
     * @class
     */
    export class DOMProcessing {
        static __static_initialized: boolean = false;
        static __static_initialize() { if (!DOMProcessing.__static_initialized) { DOMProcessing.__static_initialized = true; DOMProcessing.__static_initializer_0(); } }

        static XML_LITERAL: string = "XMLLiteral";

        static RDF_PREFIX: string; public static RDF_PREFIX_$LI$(): string { DOMProcessing.__static_initialize(); if (DOMProcessing.RDF_PREFIX == null) { DOMProcessing.RDF_PREFIX = org.openprovenance.prov.model.NamespacePrefixMapper.RDF_PREFIX; }  return DOMProcessing.RDF_PREFIX; }

        static RDF_NAMESPACE: string; public static RDF_NAMESPACE_$LI$(): string { DOMProcessing.__static_initialize(); if (DOMProcessing.RDF_NAMESPACE == null) { DOMProcessing.RDF_NAMESPACE = org.openprovenance.prov.model.NamespacePrefixMapper.RDF_NS; }  return DOMProcessing.RDF_NAMESPACE; }

        static RDF_LITERAL: string; public static RDF_LITERAL_$LI$(): string { DOMProcessing.__static_initialize(); if (DOMProcessing.RDF_LITERAL == null) { DOMProcessing.RDF_LITERAL = DOMProcessing.RDF_PREFIX_$LI$() + ":" + DOMProcessing.XML_LITERAL; }  return DOMProcessing.RDF_LITERAL; }

        public static XSD_NS_FOR_XML: string = "http://www.w3.org/2001/XMLSchema";

        static FOR_XML_XSD_QNAME: string; public static FOR_XML_XSD_QNAME_$LI$(): string { DOMProcessing.__static_initialize(); if (DOMProcessing.FOR_XML_XSD_QNAME == null) { DOMProcessing.FOR_XML_XSD_QNAME = org.openprovenance.prov.model.NamespacePrefixMapper.XSD_NS + "QName"; }  return DOMProcessing.FOR_XML_XSD_QNAME; }

        /*private*/ qnU: org.openprovenance.prov.model.QualifiedNameUtils;

        /*private*/ pFactory: org.openprovenance.prov.model.ProvFactory;

        public constructor(pFactory: org.openprovenance.prov.model.ProvFactory) {
            this.qnU = new org.openprovenance.prov.model.QualifiedNameUtils();
            if (this.pFactory === undefined) { this.pFactory = null; }
            this.pFactory = pFactory;
        }

        public static builder: javax.xml.parsers.DocumentBuilder; public static builder_$LI$(): javax.xml.parsers.DocumentBuilder { DOMProcessing.__static_initialize();  return DOMProcessing.builder; }

        static initBuilder() {
            try {
                const docBuilderFactory: javax.xml.parsers.DocumentBuilderFactory = javax.xml.parsers.DocumentBuilderFactory.newInstance();
                docBuilderFactory.setNamespaceAware(true);
                DOMProcessing.builder = docBuilderFactory.newDocumentBuilder();
            } catch(ex) {
                throw Object.defineProperty(new Error(ex.message), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.Object','java.lang.RuntimeException','java.lang.Exception'] });
            }
        }

        static  __static_initializer_0() {
            DOMProcessing.initBuilder();
        }

        public static qualifiedNameToString$org_openprovenance_prov_model_QualifiedName(name: org.openprovenance.prov.model.QualifiedName): string {
            const ns: org.openprovenance.prov.model.Namespace = org.openprovenance.prov.model.Namespace.getThreadNamespace();
            return ns.qualifiedNameToString$org_openprovenance_prov_model_QualifiedName(name);
        }

        public static qualifiedNameToString(name?: any): any {
            if (((name != null && (name.constructor != null && name.constructor["__interfaces"] != null && name.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || name === null)) {
                return <any>org.openprovenance.prov.model.DOMProcessing.qualifiedNameToString$org_openprovenance_prov_model_QualifiedName(name);
            } else if (((name != null && name instanceof <any>javax.xml.namespace.QName) || name === null)) {
                return <any>org.openprovenance.prov.model.DOMProcessing.qualifiedNameToString$javax_xml_namespace_QName(name);
            } else throw new Error('invalid overload');
        }

        public static qualifiedNameToString$javax_xml_namespace_QName(name: javax.xml.namespace.QName): string {
            const ns: org.openprovenance.prov.model.Namespace = org.openprovenance.prov.model.Namespace.getThreadNamespace();
            return ns.qualifiedNameToString$javax_xml_namespace_QName(name);
        }

        /**
         * Converts a string to a QualifiedName, extracting namespace from the DOM.  Ensures that the generated qualified name is
         * properly escaped, according to PROV-N syntax.
         * 
         * @param {string} str
         * string to convert to QualifiedName
         * @param {*} el
         * current Element in which this string was found (as attribute
         * or attribute value)
         * @return {*} a qualified name {@link QualifiedName}
         */
        public stringToQualifiedName(str: string, el: org.w3c.dom.Element): org.openprovenance.prov.model.QualifiedName {
            if (str == null)return null;
            const index: number = str.indexOf(':');
            if (index === -1){
                const qn: org.openprovenance.prov.model.QualifiedName = this.pFactory.newQualifiedName$java_lang_String$java_lang_String$java_lang_String(el.lookupNamespaceURI(null), str, null);
                return qn;
            }
            const prefix: string = str.substring(0, index);
            const local: string = str.substring(index + 1, str.length);
            const escapedLocal: string = this.qnU.escapeProvLocalName(this.qnU.unescapeFromXsdLocalName(local));
            const qn: org.openprovenance.prov.model.QualifiedName = this.pFactory.newQualifiedName$java_lang_String$java_lang_String$java_lang_String(this.convertNsFromXml(el.lookupNamespaceURI(prefix)), escapedLocal, prefix);
            return qn;
        }

        public static convertNsToXml(uri: string): string {
            if (org.openprovenance.prov.model.NamespacePrefixMapper.XSD_NS === uri){
                return DOMProcessing.XSD_NS_FOR_XML;
            }
            return uri;
        }

        public convertNsFromXml(uri: string): string {
            if (DOMProcessing.XSD_NS_FOR_XML === uri){
                return org.openprovenance.prov.model.NamespacePrefixMapper.XSD_NS;
            }
            return uri;
        }

        public static newElement$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(elementName: org.openprovenance.prov.model.QualifiedName, value: org.openprovenance.prov.model.QualifiedName): org.w3c.dom.Element {
            const doc: org.w3c.dom.Document = DOMProcessing.builder_$LI$().newDocument();
            const el: org.w3c.dom.Element = doc.createElementNS(elementName.getNamespaceURI(), DOMProcessing.qualifiedNameToString$javax_xml_namespace_QName(elementName.toQName()));
            el.setAttributeNS(org.openprovenance.prov.model.NamespacePrefixMapper.XSI_NS, "xsi:type", "xsd:QName");
            el.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:xsd", DOMProcessing.XSD_NS_FOR_XML);
            const valueAsString: string = DOMProcessing.qualifiedNameToString$javax_xml_namespace_QName(value.toQName());
            el.appendChild(doc.createTextNode(valueAsString));
            const index: number = valueAsString.indexOf(":");
            if (index !== -1){
                const prefix: string = valueAsString.substring(0, index);
                el.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:" + prefix, DOMProcessing.convertNsToXml(value.getNamespaceURI()));
            } else {
                el.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns", DOMProcessing.convertNsToXml(value.getNamespaceURI()));
            }
            doc.appendChild(el);
            return el;
        }

        public static newElement$org_openprovenance_prov_model_QualifiedName$java_lang_String$org_openprovenance_prov_model_QualifiedName(elementName: org.openprovenance.prov.model.QualifiedName, value: string, type: org.openprovenance.prov.model.QualifiedName): org.w3c.dom.Element {
            const doc: org.w3c.dom.Document = DOMProcessing.builder_$LI$().newDocument();
            let qualifiedNameString: string;
            qualifiedNameString = DOMProcessing.qualifiedNameToString$javax_xml_namespace_QName(elementName.toQName());
            const el: org.w3c.dom.Element = doc.createElementNS(elementName.getNamespaceURI(), qualifiedNameString);
            el.setAttributeNS(org.openprovenance.prov.model.NamespacePrefixMapper.XSI_NS, "xsi:type", DOMProcessing.qualifiedNameToString$org_openprovenance_prov_model_QualifiedName(type));
            el.appendChild(doc.createTextNode(value));
            doc.appendChild(el);
            return el;
        }

        public static newElement$org_openprovenance_prov_model_QualifiedName$java_lang_String$org_openprovenance_prov_model_QualifiedName$java_lang_String(qualifiedName: org.openprovenance.prov.model.QualifiedName, value: string, type: org.openprovenance.prov.model.QualifiedName, lang: string): org.w3c.dom.Element {
            const doc: org.w3c.dom.Document = DOMProcessing.builder_$LI$().newDocument();
            const el: org.w3c.dom.Element = doc.createElementNS(qualifiedName.getNamespaceURI(), DOMProcessing.qualifiedNameToString$javax_xml_namespace_QName(qualifiedName.toQName()));
            el.setAttributeNS(org.openprovenance.prov.model.NamespacePrefixMapper.XSI_NS, "xsi:type", DOMProcessing.qualifiedNameToString$org_openprovenance_prov_model_QualifiedName(type));
            if ((lang != null) && (lang !== "")){
                el.setAttributeNS(org.openprovenance.prov.model.NamespacePrefixMapper.XML_NS, "xml:lang", lang);
            }
            el.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:xml", org.openprovenance.prov.model.NamespacePrefixMapper.XML_NS);
            el.appendChild(doc.createTextNode(value));
            doc.appendChild(el);
            return el;
        }

        public static newElement(qualifiedName?: any, value?: any, type?: any, lang?: any): any {
            if (((qualifiedName != null && (qualifiedName.constructor != null && qualifiedName.constructor["__interfaces"] != null && qualifiedName.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || qualifiedName === null) && ((typeof value === 'string') || value === null) && ((type != null && (type.constructor != null && type.constructor["__interfaces"] != null && type.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || type === null) && ((typeof lang === 'string') || lang === null)) {
                return <any>org.openprovenance.prov.model.DOMProcessing.newElement$org_openprovenance_prov_model_QualifiedName$java_lang_String$org_openprovenance_prov_model_QualifiedName$java_lang_String(qualifiedName, value, type, lang);
            } else if (((qualifiedName != null && (qualifiedName.constructor != null && qualifiedName.constructor["__interfaces"] != null && qualifiedName.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || qualifiedName === null) && ((typeof value === 'string') || value === null) && ((type != null && (type.constructor != null && type.constructor["__interfaces"] != null && type.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || type === null) && lang === undefined) {
                return <any>org.openprovenance.prov.model.DOMProcessing.newElement$org_openprovenance_prov_model_QualifiedName$java_lang_String$org_openprovenance_prov_model_QualifiedName(qualifiedName, value, type);
            } else if (((qualifiedName != null && (qualifiedName.constructor != null && qualifiedName.constructor["__interfaces"] != null && qualifiedName.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || qualifiedName === null) && ((value != null && (value.constructor != null && value.constructor["__interfaces"] != null && value.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || value === null) && type === undefined && lang === undefined) {
                return <any>org.openprovenance.prov.model.DOMProcessing.newElement$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(qualifiedName, value);
            } else if (((qualifiedName != null && (qualifiedName.constructor != null && qualifiedName.constructor["__interfaces"] != null && qualifiedName.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || qualifiedName === null) && ((value != null && (value.constructor != null && value.constructor["__interfaces"] != null && value.constructor["__interfaces"].indexOf("org.w3c.dom.Element") >= 0)) || value === null) && type === undefined && lang === undefined) {
                return <any>org.openprovenance.prov.model.DOMProcessing.newElement$org_openprovenance_prov_model_QualifiedName$org_w3c_dom_Element(qualifiedName, value);
            } else throw new Error('invalid overload');
        }

        public static newElement$org_openprovenance_prov_model_QualifiedName$org_w3c_dom_Element(name: org.openprovenance.prov.model.QualifiedName, value: org.w3c.dom.Element): org.w3c.dom.Element {
            const doc: org.w3c.dom.Document = DOMProcessing.builder_$LI$().newDocument();
            const el: org.w3c.dom.Element = doc.createElementNS(name.getNamespaceURI(), DOMProcessing.qualifiedNameToString$org_openprovenance_prov_model_QualifiedName(name));
            el.setAttributeNS(org.openprovenance.prov.model.NamespacePrefixMapper.XSI_NS, "xsi:type", DOMProcessing.RDF_LITERAL_$LI$());
            el.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:" + DOMProcessing.RDF_PREFIX_$LI$(), DOMProcessing.RDF_NAMESPACE_$LI$());
            el.appendChild(doc.importNode(value, true));
            return el;
        }

        public static writeDOMToPrinter$org_w3c_dom_Node$javax_xml_transform_stream_StreamResult$boolean(document: org.w3c.dom.Node, result: javax.xml.transform.stream.StreamResult, formatted: boolean) {
            const tFactory: javax.xml.transform.TransformerFactory = javax.xml.transform.TransformerFactory.newInstance();
            const transformer: javax.xml.transform.Transformer = tFactory.newTransformer();
            const source: javax.xml.transform.dom.DOMSource = new javax.xml.transform.dom.DOMSource(document);
            transformer.setOutputProperty(javax.xml.transform.OutputKeys.METHOD, "xml");
            transformer.setOutputProperty(javax.xml.transform.OutputKeys.OMIT_XML_DECLARATION, "yes");
            if (formatted){
                transformer.setOutputProperty(javax.xml.transform.OutputKeys.INDENT, "yes");
            } else {
                transformer.setOutputProperty(javax.xml.transform.OutputKeys.INDENT, "no");
            }
            transformer.transform(source, result);
        }

        public static writeDOMToPrinter(document?: any, result?: any, formatted?: any) {
            if (((document != null && (document.constructor != null && document.constructor["__interfaces"] != null && document.constructor["__interfaces"].indexOf("org.w3c.dom.Node") >= 0)) || document === null) && ((result != null && result instanceof <any>javax.xml.transform.stream.StreamResult) || result === null) && ((typeof formatted === 'boolean') || formatted === null)) {
                return <any>org.openprovenance.prov.model.DOMProcessing.writeDOMToPrinter$org_w3c_dom_Node$javax_xml_transform_stream_StreamResult$boolean(document, result, formatted);
            } else if (((document != null && (document.constructor != null && document.constructor["__interfaces"] != null && document.constructor["__interfaces"].indexOf("org.w3c.dom.Node") >= 0)) || document === null) && ((result != null && result instanceof <any>java.io.Writer) || result === null) && ((typeof formatted === 'boolean') || formatted === null)) {
                return <any>org.openprovenance.prov.model.DOMProcessing.writeDOMToPrinter$org_w3c_dom_Node$java_io_Writer$boolean(document, result, formatted);
            } else throw new Error('invalid overload');
        }

        public static writeDOMToPrinter$org_w3c_dom_Node$java_io_Writer$boolean(document: org.w3c.dom.Node, out: java.io.Writer, formatted: boolean) {
            const result: javax.xml.transform.stream.StreamResult = new javax.xml.transform.stream.StreamResult(out);
            DOMProcessing.writeDOMToPrinter$org_w3c_dom_Node$javax_xml_transform_stream_StreamResult$boolean(document, result, formatted);
        }

        public static writeToString(toWrite: org.w3c.dom.Node): string {
            const sw: java.io.StringWriter = new java.io.StringWriter();
            DOMProcessing.writeDOMToPrinter$org_w3c_dom_Node$java_io_Writer$boolean(toWrite, new java.io.PrintWriter(sw), false);
            return sw.toString();
        }

        public static trimNode$org_w3c_dom_Node(node: org.w3c.dom.Node) {
            const nodes: Array<org.w3c.dom.Text> = <any>([]);
            DOMProcessing.trimNode$org_w3c_dom_Node$java_util_List(node, nodes);
            for(let index161=0; index161 < nodes.length; index161++) {
                let n = nodes[index161];
                {
                    if (n.getTextContent() === (""))n.getParentNode().removeChild(n);
                }
            }
        }

        public static trimNode$org_w3c_dom_Node$java_util_List(node: org.w3c.dom.Node, nodes: Array<org.w3c.dom.Text>) {
            if (node.getNodeType() === org.w3c.dom.Node.TEXT_NODE){
                node.normalize();
                const txt: org.w3c.dom.Text = <org.w3c.dom.Text><any>node;
                txt.setTextContent(txt.getTextContent().trim());
                /* add */(nodes.push(txt)>0);
            } else {
                const nl: org.w3c.dom.NodeList = node.getChildNodes();
                for(let i: number = 0; i < nl.getLength(); i++) {{
                    DOMProcessing.trimNode$org_w3c_dom_Node$java_util_List(nl.item(i), nodes);
                };}
            }
        }

        public static trimNode(node?: any, nodes?: any) {
            if (((node != null && (node.constructor != null && node.constructor["__interfaces"] != null && node.constructor["__interfaces"].indexOf("org.w3c.dom.Node") >= 0)) || node === null) && ((nodes != null && (nodes instanceof Array)) || nodes === null)) {
                return <any>org.openprovenance.prov.model.DOMProcessing.trimNode$org_w3c_dom_Node$java_util_List(node, nodes);
            } else if (((node != null && (node.constructor != null && node.constructor["__interfaces"] != null && node.constructor["__interfaces"].indexOf("org.w3c.dom.Node") >= 0)) || node === null) && nodes === undefined) {
                return <any>org.openprovenance.prov.model.DOMProcessing.trimNode$org_w3c_dom_Node(node);
            } else throw new Error('invalid overload');
        }

        public static marshalAttribute(attribute: org.openprovenance.prov.model.Attribute): org.w3c.dom.Element {
            return DOMProcessing.marshalTypedValue(attribute, attribute.getElementName());
        }

        public static marshalTypedValue(attribute: org.openprovenance.prov.model.TypedValue, elementName: org.openprovenance.prov.model.QualifiedName): org.w3c.dom.Element {
            const value: any = attribute.getValue();
            const type: org.openprovenance.prov.model.QualifiedName = attribute.getType();
            if (value != null && (value.constructor != null && value.constructor["__interfaces"] != null && value.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.LangString") >= 0)){
                const istring: org.openprovenance.prov.model.LangString = (<org.openprovenance.prov.model.LangString><any>value);
                if (istring.getLang() != null){
                    return DOMProcessing.newElement$org_openprovenance_prov_model_QualifiedName$java_lang_String$org_openprovenance_prov_model_QualifiedName$java_lang_String(elementName, istring.getValue(), attribute.getType(), istring.getLang());
                } else {
                    return DOMProcessing.newElement$org_openprovenance_prov_model_QualifiedName$java_lang_String$org_openprovenance_prov_model_QualifiedName(elementName, istring.getValue(), attribute.getType());
                }
            } else if (value != null && (value.constructor != null && value.constructor["__interfaces"] != null && value.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)){
                return DOMProcessing.newElement$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(elementName, <org.openprovenance.prov.model.QualifiedName><any>value);
            } else if ((type.getNamespaceURI() === DOMProcessing.RDF_NAMESPACE_$LI$()) && (type.getLocalPart() === DOMProcessing.XML_LITERAL)){
                return DOMProcessing.newElement$org_openprovenance_prov_model_QualifiedName$org_w3c_dom_Element(elementName, <org.w3c.dom.Element><any>attribute.getConvertedValue());
            } else {
                return DOMProcessing.newElement$org_openprovenance_prov_model_QualifiedName$java_lang_String$org_openprovenance_prov_model_QualifiedName(elementName, value.toString(), attribute.getType());
            }
        }

        public unmarshallAttribute(el: org.w3c.dom.Element, pFactory: org.openprovenance.prov.model.ProvFactory, vconv: org.openprovenance.prov.model.ValueConverter): org.openprovenance.prov.model.Attribute {
            const prefix: string = el.getPrefix();
            const namespace: string = el.getNamespaceURI();
            let local: string = el.getLocalName();
            local = this.qnU.escapeProvLocalName(this.qnU.unescapeFromXsdLocalName(local));
            const child: string = el.getTextContent();
            const typeAsString: string = el.getAttributeNS(org.openprovenance.prov.model.NamespacePrefixMapper.XSI_NS, "type");
            const lang: string = el.getAttributeNS(org.openprovenance.prov.model.NamespacePrefixMapper.XML_NS, "lang");
            let type: org.openprovenance.prov.model.QualifiedName = ((typeAsString == null) || (typeAsString === (""))) ? null : this.stringToQualifiedName(typeAsString, el);
            const name: org.openprovenance.prov.model.Name = pFactory.getName();
            if (type == null)type = name.XSD_STRING;
            if (DOMProcessing.FOR_XML_XSD_QNAME_$LI$() === type.getUri()){
                const qn: org.openprovenance.prov.model.QualifiedName = this.stringToQualifiedName(child, el);
                const x: org.openprovenance.prov.model.Attribute = pFactory.newAttribute$java_lang_String$java_lang_String$java_lang_String$java_lang_Object$org_openprovenance_prov_model_QualifiedName(namespace, local, prefix, qn, name.PROV_QUALIFIED_NAME);
                return x;
            } else if (/* equals */(<any>((o1: any, o2: any) => { if (o1 && o1.equals) { return o1.equals(o2); } else { return o1 === o2; } })(type,name.RDF_LITERAL))){
                const nodes: org.w3c.dom.NodeList = el.getChildNodes();
                let content: org.w3c.dom.Element = null;
                for(let i: number = 0; i < nodes.getLength(); i++) {{
                    const node: org.w3c.dom.Node = nodes.item(i);
                    if (node != null && (node.constructor != null && node.constructor["__interfaces"] != null && node.constructor["__interfaces"].indexOf("org.w3c.dom.Element") >= 0)){
                        content = <org.w3c.dom.Element><any>node;
                        break;
                    }
                };}
                return pFactory.newAttribute$java_lang_String$java_lang_String$java_lang_String$java_lang_Object$org_openprovenance_prov_model_QualifiedName(namespace, local, prefix, content, type);
            } else if ((lang == null) || (lang === (""))){
                if (/* equals */(<any>((o1: any, o2: any) => { if (o1 && o1.equals) { return o1.equals(o2); } else { return o1 === o2; } })(type,name.PROV_LANG_STRING))){
                    return pFactory.newAttribute$java_lang_String$java_lang_String$java_lang_String$java_lang_Object$org_openprovenance_prov_model_QualifiedName(namespace, local, prefix, pFactory.newInternationalizedString$java_lang_String(child), type);
                } else {
                    return pFactory.newAttribute$java_lang_String$java_lang_String$java_lang_String$java_lang_Object$org_openprovenance_prov_model_QualifiedName(namespace, local, prefix, vconv.convertToJava(type, child), type);
                }
            } else {
                return pFactory.newAttribute$java_lang_String$java_lang_String$java_lang_String$java_lang_Object$org_openprovenance_prov_model_QualifiedName(namespace, local, prefix, pFactory.newInternationalizedString$java_lang_String$java_lang_String(child, lang), type);
            }
        }
    }
    DOMProcessing["__class"] = "org.openprovenance.prov.model.DOMProcessing";

}


org.openprovenance.prov.model.DOMProcessing.__static_initialize();
