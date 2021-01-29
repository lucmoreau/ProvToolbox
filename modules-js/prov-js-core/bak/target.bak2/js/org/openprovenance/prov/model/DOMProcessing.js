/* Generated from Java with JSweet 3.0.0 - http://www.jsweet.org */
var org;
(function (org) {
    var openprovenance;
    (function (openprovenance) {
        var prov;
        (function (prov) {
            var model;
            (function (model) {
                /**
                 * Utility class for processing DOM {@link Element}, in particular conversion from a PROV attribute to an {@link Element} and vice-versa.
                 *
                 * @author lavm
                 * @param {org.openprovenance.prov.model.ProvFactory} pFactory
                 * @class
                 */
                class DOMProcessing {
                    constructor(pFactory) {
                        this.qnU = new org.openprovenance.prov.model.QualifiedNameUtils();
                        if (this.pFactory === undefined) {
                            this.pFactory = null;
                        }
                        this.pFactory = pFactory;
                    }
                    static __static_initialize() { if (!DOMProcessing.__static_initialized) {
                        DOMProcessing.__static_initialized = true;
                        DOMProcessing.__static_initializer_0();
                    } }
                    static RDF_PREFIX_$LI$() { DOMProcessing.__static_initialize(); if (DOMProcessing.RDF_PREFIX == null) {
                        DOMProcessing.RDF_PREFIX = org.openprovenance.prov.model.NamespacePrefixMapper.RDF_PREFIX;
                    } return DOMProcessing.RDF_PREFIX; }
                    static RDF_NAMESPACE_$LI$() { DOMProcessing.__static_initialize(); if (DOMProcessing.RDF_NAMESPACE == null) {
                        DOMProcessing.RDF_NAMESPACE = org.openprovenance.prov.model.NamespacePrefixMapper.RDF_NS;
                    } return DOMProcessing.RDF_NAMESPACE; }
                    static RDF_LITERAL_$LI$() { DOMProcessing.__static_initialize(); if (DOMProcessing.RDF_LITERAL == null) {
                        DOMProcessing.RDF_LITERAL = DOMProcessing.RDF_PREFIX_$LI$() + ":" + DOMProcessing.XML_LITERAL;
                    } return DOMProcessing.RDF_LITERAL; }
                    static FOR_XML_XSD_QNAME_$LI$() { DOMProcessing.__static_initialize(); if (DOMProcessing.FOR_XML_XSD_QNAME == null) {
                        DOMProcessing.FOR_XML_XSD_QNAME = org.openprovenance.prov.model.NamespacePrefixMapper.XSD_NS + "QName";
                    } return DOMProcessing.FOR_XML_XSD_QNAME; }
                    static builder_$LI$() { DOMProcessing.__static_initialize(); return DOMProcessing.builder; }
                    static initBuilder() {
                        try {
                            const docBuilderFactory = javax.xml.parsers.DocumentBuilderFactory.newInstance();
                            docBuilderFactory.setNamespaceAware(true);
                            DOMProcessing.builder = docBuilderFactory.newDocumentBuilder();
                        }
                        catch (ex) {
                            throw Object.defineProperty(new Error(ex.message), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.Exception'] });
                        }
                    }
                    static __static_initializer_0() {
                        DOMProcessing.initBuilder();
                    }
                    static qualifiedNameToString$org_openprovenance_prov_model_QualifiedName(name) {
                        const ns = org.openprovenance.prov.model.Namespace.getThreadNamespace();
                        return ns.qualifiedNameToString$org_openprovenance_prov_model_QualifiedName(name);
                    }
                    static qualifiedNameToString(name) {
                        if (((name != null && (name.constructor != null && name.constructor["__interfaces"] != null && name.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || name === null)) {
                            return org.openprovenance.prov.model.DOMProcessing.qualifiedNameToString$org_openprovenance_prov_model_QualifiedName(name);
                        }
                        else if (((name != null && name instanceof javax.xml.namespace.QName) || name === null)) {
                            return org.openprovenance.prov.model.DOMProcessing.qualifiedNameToString$javax_xml_namespace_QName(name);
                        }
                        else
                            throw new Error('invalid overload');
                    }
                    static qualifiedNameToString$javax_xml_namespace_QName(name) {
                        const ns = org.openprovenance.prov.model.Namespace.getThreadNamespace();
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
                    stringToQualifiedName(str, el) {
                        if (str == null)
                            return null;
                        const index = str.indexOf(':');
                        if (index === -1) {
                            const qn = this.pFactory.newQualifiedName$java_lang_String$java_lang_String$java_lang_String(el.lookupNamespaceURI(null), str, null);
                            return qn;
                        }
                        const prefix = str.substring(0, index);
                        const local = str.substring(index + 1, str.length);
                        const escapedLocal = this.qnU.escapeProvLocalName(this.qnU.unescapeFromXsdLocalName(local));
                        const qn = this.pFactory.newQualifiedName$java_lang_String$java_lang_String$java_lang_String(this.convertNsFromXml(el.lookupNamespaceURI(prefix)), escapedLocal, prefix);
                        return qn;
                    }
                    static convertNsToXml(uri) {
                        if (org.openprovenance.prov.model.NamespacePrefixMapper.XSD_NS === uri) {
                            return DOMProcessing.XSD_NS_FOR_XML;
                        }
                        return uri;
                    }
                    convertNsFromXml(uri) {
                        if (DOMProcessing.XSD_NS_FOR_XML === uri) {
                            return org.openprovenance.prov.model.NamespacePrefixMapper.XSD_NS;
                        }
                        return uri;
                    }
                    static newElement$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(elementName, value) {
                        const doc = DOMProcessing.builder_$LI$().newDocument();
                        const el = doc.createElementNS(elementName.getNamespaceURI(), DOMProcessing.qualifiedNameToString$javax_xml_namespace_QName(elementName.toQName()));
                        el.setAttributeNS(org.openprovenance.prov.model.NamespacePrefixMapper.XSI_NS, "xsi:type", "xsd:QName");
                        el.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:xsd", DOMProcessing.XSD_NS_FOR_XML);
                        const valueAsString = DOMProcessing.qualifiedNameToString$javax_xml_namespace_QName(value.toQName());
                        el.appendChild(doc.createTextNode(valueAsString));
                        const index = valueAsString.indexOf(":");
                        if (index !== -1) {
                            const prefix = valueAsString.substring(0, index);
                            el.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:" + prefix, DOMProcessing.convertNsToXml(value.getNamespaceURI()));
                        }
                        else {
                            el.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns", DOMProcessing.convertNsToXml(value.getNamespaceURI()));
                        }
                        doc.appendChild(el);
                        return el;
                    }
                    static newElement$org_openprovenance_prov_model_QualifiedName$java_lang_String$org_openprovenance_prov_model_QualifiedName(elementName, value, type) {
                        const doc = DOMProcessing.builder_$LI$().newDocument();
                        let qualifiedNameString;
                        qualifiedNameString = DOMProcessing.qualifiedNameToString$javax_xml_namespace_QName(elementName.toQName());
                        const el = doc.createElementNS(elementName.getNamespaceURI(), qualifiedNameString);
                        el.setAttributeNS(org.openprovenance.prov.model.NamespacePrefixMapper.XSI_NS, "xsi:type", DOMProcessing.qualifiedNameToString$org_openprovenance_prov_model_QualifiedName(type));
                        el.appendChild(doc.createTextNode(value));
                        doc.appendChild(el);
                        return el;
                    }
                    static newElement$org_openprovenance_prov_model_QualifiedName$java_lang_String$org_openprovenance_prov_model_QualifiedName$java_lang_String(qualifiedName, value, type, lang) {
                        const doc = DOMProcessing.builder_$LI$().newDocument();
                        const el = doc.createElementNS(qualifiedName.getNamespaceURI(), DOMProcessing.qualifiedNameToString$javax_xml_namespace_QName(qualifiedName.toQName()));
                        el.setAttributeNS(org.openprovenance.prov.model.NamespacePrefixMapper.XSI_NS, "xsi:type", DOMProcessing.qualifiedNameToString$org_openprovenance_prov_model_QualifiedName(type));
                        if ((lang != null) && (lang !== "")) {
                            el.setAttributeNS(org.openprovenance.prov.model.NamespacePrefixMapper.XML_NS, "xml:lang", lang);
                        }
                        el.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:xml", org.openprovenance.prov.model.NamespacePrefixMapper.XML_NS);
                        el.appendChild(doc.createTextNode(value));
                        doc.appendChild(el);
                        return el;
                    }
                    static newElement(qualifiedName, value, type, lang) {
                        if (((qualifiedName != null && (qualifiedName.constructor != null && qualifiedName.constructor["__interfaces"] != null && qualifiedName.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || qualifiedName === null) && ((typeof value === 'string') || value === null) && ((type != null && (type.constructor != null && type.constructor["__interfaces"] != null && type.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || type === null) && ((typeof lang === 'string') || lang === null)) {
                            return org.openprovenance.prov.model.DOMProcessing.newElement$org_openprovenance_prov_model_QualifiedName$java_lang_String$org_openprovenance_prov_model_QualifiedName$java_lang_String(qualifiedName, value, type, lang);
                        }
                        else if (((qualifiedName != null && (qualifiedName.constructor != null && qualifiedName.constructor["__interfaces"] != null && qualifiedName.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || qualifiedName === null) && ((typeof value === 'string') || value === null) && ((type != null && (type.constructor != null && type.constructor["__interfaces"] != null && type.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || type === null) && lang === undefined) {
                            return org.openprovenance.prov.model.DOMProcessing.newElement$org_openprovenance_prov_model_QualifiedName$java_lang_String$org_openprovenance_prov_model_QualifiedName(qualifiedName, value, type);
                        }
                        else if (((qualifiedName != null && (qualifiedName.constructor != null && qualifiedName.constructor["__interfaces"] != null && qualifiedName.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || qualifiedName === null) && ((value != null && (value.constructor != null && value.constructor["__interfaces"] != null && value.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || value === null) && type === undefined && lang === undefined) {
                            return org.openprovenance.prov.model.DOMProcessing.newElement$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(qualifiedName, value);
                        }
                        else if (((qualifiedName != null && (qualifiedName.constructor != null && qualifiedName.constructor["__interfaces"] != null && qualifiedName.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || qualifiedName === null) && ((value != null && (value.constructor != null && value.constructor["__interfaces"] != null && value.constructor["__interfaces"].indexOf("org.w3c.dom.Element") >= 0)) || value === null) && type === undefined && lang === undefined) {
                            return org.openprovenance.prov.model.DOMProcessing.newElement$org_openprovenance_prov_model_QualifiedName$org_w3c_dom_Element(qualifiedName, value);
                        }
                        else
                            throw new Error('invalid overload');
                    }
                    static newElement$org_openprovenance_prov_model_QualifiedName$org_w3c_dom_Element(name, value) {
                        const doc = DOMProcessing.builder_$LI$().newDocument();
                        const el = doc.createElementNS(name.getNamespaceURI(), DOMProcessing.qualifiedNameToString$org_openprovenance_prov_model_QualifiedName(name));
                        el.setAttributeNS(org.openprovenance.prov.model.NamespacePrefixMapper.XSI_NS, "xsi:type", DOMProcessing.RDF_LITERAL_$LI$());
                        el.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:" + DOMProcessing.RDF_PREFIX_$LI$(), DOMProcessing.RDF_NAMESPACE_$LI$());
                        el.appendChild(doc.importNode(value, true));
                        return el;
                    }
                    static writeDOMToPrinter$org_w3c_dom_Node$javax_xml_transform_stream_StreamResult$boolean(document, result, formatted) {
                        const tFactory = javax.xml.transform.TransformerFactory.newInstance();
                        const transformer = tFactory.newTransformer();
                        const source = new javax.xml.transform.dom.DOMSource(document);
                        transformer.setOutputProperty(javax.xml.transform.OutputKeys.METHOD, "xml");
                        transformer.setOutputProperty(javax.xml.transform.OutputKeys.OMIT_XML_DECLARATION, "yes");
                        if (formatted) {
                            transformer.setOutputProperty(javax.xml.transform.OutputKeys.INDENT, "yes");
                        }
                        else {
                            transformer.setOutputProperty(javax.xml.transform.OutputKeys.INDENT, "no");
                        }
                        transformer.transform(source, result);
                    }
                    static writeDOMToPrinter(document, result, formatted) {
                        if (((document != null && (document.constructor != null && document.constructor["__interfaces"] != null && document.constructor["__interfaces"].indexOf("org.w3c.dom.Node") >= 0)) || document === null) && ((result != null && result instanceof javax.xml.transform.stream.StreamResult) || result === null) && ((typeof formatted === 'boolean') || formatted === null)) {
                            return org.openprovenance.prov.model.DOMProcessing.writeDOMToPrinter$org_w3c_dom_Node$javax_xml_transform_stream_StreamResult$boolean(document, result, formatted);
                        }
                        else if (((document != null && (document.constructor != null && document.constructor["__interfaces"] != null && document.constructor["__interfaces"].indexOf("org.w3c.dom.Node") >= 0)) || document === null) && ((result != null && result instanceof java.io.Writer) || result === null) && ((typeof formatted === 'boolean') || formatted === null)) {
                            return org.openprovenance.prov.model.DOMProcessing.writeDOMToPrinter$org_w3c_dom_Node$java_io_Writer$boolean(document, result, formatted);
                        }
                        else
                            throw new Error('invalid overload');
                    }
                    static writeDOMToPrinter$org_w3c_dom_Node$java_io_Writer$boolean(document, out, formatted) {
                        const result = new javax.xml.transform.stream.StreamResult(out);
                        DOMProcessing.writeDOMToPrinter$org_w3c_dom_Node$javax_xml_transform_stream_StreamResult$boolean(document, result, formatted);
                    }
                    static writeToString(toWrite) {
                        const sw = new java.io.StringWriter();
                        DOMProcessing.writeDOMToPrinter$org_w3c_dom_Node$java_io_Writer$boolean(toWrite, new java.io.PrintWriter(sw), false);
                        return sw.toString();
                    }
                    static trimNode$org_w3c_dom_Node(node) {
                        const nodes = ([]);
                        DOMProcessing.trimNode$org_w3c_dom_Node$java_util_List(node, nodes);
                        for (let index161 = 0; index161 < nodes.length; index161++) {
                            let n = nodes[index161];
                            {
                                if (n.getTextContent() === (""))
                                    n.getParentNode().removeChild(n);
                            }
                        }
                    }
                    static trimNode$org_w3c_dom_Node$java_util_List(node, nodes) {
                        if (node.getNodeType() === org.w3c.dom.Node.TEXT_NODE) {
                            node.normalize();
                            const txt = node;
                            txt.setTextContent(txt.getTextContent().trim());
                            /* add */ (nodes.push(txt) > 0);
                        }
                        else {
                            const nl = node.getChildNodes();
                            for (let i = 0; i < nl.getLength(); i++) {
                                {
                                    DOMProcessing.trimNode$org_w3c_dom_Node$java_util_List(nl.item(i), nodes);
                                }
                                ;
                            }
                        }
                    }
                    static trimNode(node, nodes) {
                        if (((node != null && (node.constructor != null && node.constructor["__interfaces"] != null && node.constructor["__interfaces"].indexOf("org.w3c.dom.Node") >= 0)) || node === null) && ((nodes != null && (nodes instanceof Array)) || nodes === null)) {
                            return org.openprovenance.prov.model.DOMProcessing.trimNode$org_w3c_dom_Node$java_util_List(node, nodes);
                        }
                        else if (((node != null && (node.constructor != null && node.constructor["__interfaces"] != null && node.constructor["__interfaces"].indexOf("org.w3c.dom.Node") >= 0)) || node === null) && nodes === undefined) {
                            return org.openprovenance.prov.model.DOMProcessing.trimNode$org_w3c_dom_Node(node);
                        }
                        else
                            throw new Error('invalid overload');
                    }
                    static marshalAttribute(attribute) {
                        return DOMProcessing.marshalTypedValue(attribute, attribute.getElementName());
                    }
                    static marshalTypedValue(attribute, elementName) {
                        const value = attribute.getValue();
                        const type = attribute.getType();
                        if (value != null && (value.constructor != null && value.constructor["__interfaces"] != null && value.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.LangString") >= 0)) {
                            const istring = value;
                            if (istring.getLang() != null) {
                                return DOMProcessing.newElement$org_openprovenance_prov_model_QualifiedName$java_lang_String$org_openprovenance_prov_model_QualifiedName$java_lang_String(elementName, istring.getValue(), attribute.getType(), istring.getLang());
                            }
                            else {
                                return DOMProcessing.newElement$org_openprovenance_prov_model_QualifiedName$java_lang_String$org_openprovenance_prov_model_QualifiedName(elementName, istring.getValue(), attribute.getType());
                            }
                        }
                        else if (value != null && (value.constructor != null && value.constructor["__interfaces"] != null && value.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) {
                            return DOMProcessing.newElement$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(elementName, value);
                        }
                        else if ((type.getNamespaceURI() === DOMProcessing.RDF_NAMESPACE_$LI$()) && (type.getLocalPart() === DOMProcessing.XML_LITERAL)) {
                            return DOMProcessing.newElement$org_openprovenance_prov_model_QualifiedName$org_w3c_dom_Element(elementName, attribute.getConvertedValue());
                        }
                        else {
                            return DOMProcessing.newElement$org_openprovenance_prov_model_QualifiedName$java_lang_String$org_openprovenance_prov_model_QualifiedName(elementName, value.toString(), attribute.getType());
                        }
                    }
                    unmarshallAttribute(el, pFactory, vconv) {
                        const prefix = el.getPrefix();
                        const namespace = el.getNamespaceURI();
                        let local = el.getLocalName();
                        local = this.qnU.escapeProvLocalName(this.qnU.unescapeFromXsdLocalName(local));
                        const child = el.getTextContent();
                        const typeAsString = el.getAttributeNS(org.openprovenance.prov.model.NamespacePrefixMapper.XSI_NS, "type");
                        const lang = el.getAttributeNS(org.openprovenance.prov.model.NamespacePrefixMapper.XML_NS, "lang");
                        let type = ((typeAsString == null) || (typeAsString === (""))) ? null : this.stringToQualifiedName(typeAsString, el);
                        const name = pFactory.getName();
                        if (type == null)
                            type = name.XSD_STRING;
                        if (DOMProcessing.FOR_XML_XSD_QNAME_$LI$() === type.getUri()) {
                            const qn = this.stringToQualifiedName(child, el);
                            const x = pFactory.newAttribute$java_lang_String$java_lang_String$java_lang_String$java_lang_Object$org_openprovenance_prov_model_QualifiedName(namespace, local, prefix, qn, name.PROV_QUALIFIED_NAME);
                            return x;
                        }
                        else if ( /* equals */((o1, o2) => { if (o1 && o1.equals) {
                            return o1.equals(o2);
                        }
                        else {
                            return o1 === o2;
                        } })(type, name.RDF_LITERAL)) {
                            const nodes = el.getChildNodes();
                            let content = null;
                            for (let i = 0; i < nodes.getLength(); i++) {
                                {
                                    const node = nodes.item(i);
                                    if (node != null && (node.constructor != null && node.constructor["__interfaces"] != null && node.constructor["__interfaces"].indexOf("org.w3c.dom.Element") >= 0)) {
                                        content = node;
                                        break;
                                    }
                                }
                                ;
                            }
                            return pFactory.newAttribute$java_lang_String$java_lang_String$java_lang_String$java_lang_Object$org_openprovenance_prov_model_QualifiedName(namespace, local, prefix, content, type);
                        }
                        else if ((lang == null) || (lang === (""))) {
                            if ( /* equals */((o1, o2) => { if (o1 && o1.equals) {
                                return o1.equals(o2);
                            }
                            else {
                                return o1 === o2;
                            } })(type, name.PROV_LANG_STRING)) {
                                return pFactory.newAttribute$java_lang_String$java_lang_String$java_lang_String$java_lang_Object$org_openprovenance_prov_model_QualifiedName(namespace, local, prefix, pFactory.newInternationalizedString$java_lang_String(child), type);
                            }
                            else {
                                return pFactory.newAttribute$java_lang_String$java_lang_String$java_lang_String$java_lang_Object$org_openprovenance_prov_model_QualifiedName(namespace, local, prefix, vconv.convertToJava(type, child), type);
                            }
                        }
                        else {
                            return pFactory.newAttribute$java_lang_String$java_lang_String$java_lang_String$java_lang_Object$org_openprovenance_prov_model_QualifiedName(namespace, local, prefix, pFactory.newInternationalizedString$java_lang_String$java_lang_String(child, lang), type);
                        }
                    }
                }
                DOMProcessing.__static_initialized = false;
                DOMProcessing.XML_LITERAL = "XMLLiteral";
                DOMProcessing.XSD_NS_FOR_XML = "http://www.w3.org/2001/XMLSchema";
                model.DOMProcessing = DOMProcessing;
                DOMProcessing["__class"] = "org.openprovenance.prov.model.DOMProcessing";
            })(model = prov.model || (prov.model = {}));
        })(prov = openprovenance.prov || (openprovenance.prov = {}));
    })(openprovenance = org.openprovenance || (org.openprovenance = {}));
})(org || (org = {}));
org.openprovenance.prov.model.DOMProcessing.__static_initialize();
