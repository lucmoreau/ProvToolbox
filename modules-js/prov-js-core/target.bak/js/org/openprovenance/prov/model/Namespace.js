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
                 * A class to manipulate Namespaces when creating, serializing and converting prov documents.
                 * @author Luc Moreau
                 *
                 * @param {*} pref
                 * @class
                 */
                class Namespace {
                    constructor(pref) {
                        if (((pref != null && (pref instanceof Object)) || pref === null)) {
                            let __args = arguments;
                            this.prefixes = ({});
                            this.namespaces = ({});
                            this.defaultNamespace = null;
                            this.parent = null;
                            this.prefixCount = 0;
                            {
                                let array165 = /* entrySet */ (o => { let s = []; for (let e in o)
                                    s.push({ k: e, v: o[e], getKey: function () { return this.k; }, getValue: function () { return this.v; } }); return s; })(pref);
                                for (let index164 = 0; index164 < array165.length; index164++) {
                                    let entry = array165[index164];
                                    {
                                        /* put */ (this.prefixes[entry.getKey()] = entry.getValue());
                                        /* put */ (this.namespaces[entry.getValue()] = entry.getKey());
                                    }
                                }
                            }
                        }
                        else if (((pref != null && pref instanceof org.openprovenance.prov.model.Namespace) || pref === null)) {
                            let __args = arguments;
                            let other = __args[0];
                            this.prefixes = ({});
                            this.namespaces = ({});
                            this.defaultNamespace = null;
                            this.parent = null;
                            this.prefixCount = 0;
                            this.defaultNamespace = other.defaultNamespace;
                            this.prefixes = ({});
                            this.prefixes.putAll(other.prefixes);
                            this.namespaces = ({});
                            this.namespaces.putAll(other.namespaces);
                        }
                        else if (pref === undefined) {
                            let __args = arguments;
                            this.prefixes = ({});
                            this.namespaces = ({});
                            this.defaultNamespace = null;
                            this.parent = null;
                            this.prefixCount = 0;
                        }
                        else
                            throw new Error('invalid overload');
                    }
                    static threadNamespace_$LI$() { if (Namespace.threadNamespace == null) {
                        Namespace.threadNamespace = new Namespace.Namespace$0(this);
                    } return Namespace.threadNamespace; }
                    static getThreadNamespace() {
                        return /* get */ ((tlObj) => { if (tlObj.___value) {
                            return tlObj.___value;
                        }
                        else {
                            return tlObj.___value = tlObj.initialValue();
                        } })(Namespace.threadNamespace_$LI$());
                    }
                    static withThreadNamespace(ns) {
                        return /* get */ ((tlObj) => { if (tlObj.___value) {
                            return tlObj.___value;
                        }
                        else {
                            return tlObj.___value = tlObj.initialValue();
                        } })(Namespace.threadNamespace_$LI$()).set(ns);
                    }
                    /*private*/ set(ns) {
                        if (ns == null)
                            return null;
                        const tmp1 = ({});
                        Object.assign(tmp1,ns.prefixes);
                        const tmp2 = ({});
                        Object.assign(tmp2,ns.namespaces);
                        this.prefixes = tmp1;
                        this.namespaces = tmp2;
                        this.defaultNamespace = ns.defaultNamespace;
                        this.parent = ns.parent;
                        return this;
                    }
                    /**
                     * Extends this Namespace with all the prefix/namespace registration of the Namespace received as argument.
                     * @param {org.openprovenance.prov.model.Namespace} ns the {@link Namespace} whose prefix/namespace registration are added to this {@link Namespace}.
                     */
                    extendWith(ns) {
                        if (ns == null)
                            return;
                        if (ns.getDefaultNamespace() != null) {
                            this.registerDefault(ns.getDefaultNamespace());
                        }
                        {
                            let array163 = /* keySet */ Object.keys(ns.prefixes);
                            for (let index162 = 0; index162 < array163.length; index162++) {
                                let prefix = array163[index162];
                                {
                                    this.register(prefix, /* get */ ((m, k) => m[k] === undefined ? null : m[k])(ns.prefixes, prefix));
                                }
                            }
                        }
                    }
                    setParent(parent) {
                        this.parent = parent;
                    }
                    getParent() {
                        return this.parent;
                    }
                    addKnownNamespaces() {
                        /* put */ (this.getPrefixes()["prov"] = org.openprovenance.prov.model.NamespacePrefixMapper.PROV_NS);
                        /* put */ (this.getNamespaces()[org.openprovenance.prov.model.NamespacePrefixMapper.PROV_NS] = "prov");
                        /* put */ (this.getPrefixes()["xsd"] = org.openprovenance.prov.model.NamespacePrefixMapper.XSD_NS);
                        /* put */ (this.getNamespaces()[org.openprovenance.prov.model.NamespacePrefixMapper.XSD_NS] = "xsd");
                    }
                    getDefaultNamespace() {
                        return this.defaultNamespace;
                    }
                    setDefaultNamespace(defaultNamespace) {
                        this.defaultNamespace = defaultNamespace;
                    }
                    getPrefixes() {
                        return this.prefixes;
                    }
                    getNamespaces() {
                        return this.namespaces;
                    }
                    check(prefix, namespace) {
                        const knownAs = ((m, k) => m[k] === undefined ? null : m[k])(this.prefixes, prefix);
                        return namespace === knownAs;
                    }
                    registerDefault(namespace) {
                        this.register(null, namespace);
                    }
                    register(prefix, namespace) {
                        if ((prefix == null) || (prefix === javax.xml.XMLConstants.DEFAULT_NS_PREFIX)) {
                            if (this.defaultNamespace == null) {
                                this.defaultNamespace = namespace;
                            }
                            else {
                                this.newPrefix(namespace);
                            }
                        }
                        else {
                            const old = ((m, k) => m[k] === undefined ? null : m[k])(this.prefixes, prefix);
                            if (old == null) {
                                /* put */ (this.prefixes[prefix] = namespace);
                                if ( /* get */((m, k) => m[k] === undefined ? null : m[k])(this.namespaces, namespace) == null) {
                                    /* put */ (this.namespaces[namespace] = prefix);
                                }
                            }
                            else {
                                this.newPrefix(namespace);
                            }
                        }
                    }
                    newPrefix(namespace) {
                        let success = false;
                       // console.log("newPrefix " + namespace)
                        while ((!success)) {
                            {
                                const old = ((m, k) => m[k] === undefined ? null : m[k])(this.namespaces, namespace);
                                if (old != null)
                                    return;
                                const count = this.prefixCount++;
                                const newPrefix = Namespace.xmlns + count;
                                const oldPrefix = ((m, k) => m[k] === undefined ? null : m[k])(this.prefixes, newPrefix);
                                if (oldPrefix == null) {
                                    /* put */ (this.prefixes[newPrefix] = namespace);
                                    success = true;
                                    if ( /* get */((m, k) => m[k] === undefined ? null : m[k])(this.namespaces, namespace) == null) {
                                        /* put */ (this.namespaces[namespace] = newPrefix);
                                    }
                                }
                            }
                        }
                        ;
                    }
                    unregister(prefix, namespace) {
                        const val = ((m, k) => m[k] === undefined ? null : m[k])(this.prefixes, prefix);
                        if (val != null) {
                            if (val === namespace) {
                                /* remove */ (map => { let deleted = this.prefixes[prefix]; delete this.prefixes[prefix]; return deleted; })(this.prefixes);
                                /* remove */ (map => { let deleted = this.namespaces[namespace]; delete this.namespaces[namespace]; return deleted; })(this.namespaces);
                            }
                        }
                    }
                    unregisterDeafult(namespace) {
                        const val = this.getDefaultNamespace();
                        if (val != null) {
                            if (val === namespace) {
                                this.setDefaultNamespace(null);
                            }
                        }
                    }
                    static u_$LI$() { if (Namespace.u == null) {
                        Namespace.u = new org.openprovenance.prov.model.ProvUtilities();
                    } return Namespace.u; }
                    static gatherNamespaces$org_openprovenance_prov_model_Document(doc) {
                        const gatherer = new org.openprovenance.prov.model.NamespaceGatherer();
                        Namespace.u_$LI$().forAllStatementOrBundle(doc.getStatementOrBundle(), gatherer);
                        const ns = gatherer.getNamespace();
                        return ns;
                    }
                    static gatherNamespaces$org_openprovenance_prov_model_Bundle(bundle) {
                        const gatherer = new org.openprovenance.prov.model.NamespaceGatherer();
                        Namespace.u_$LI$().forAllStatement(bundle.getStatement(), gatherer);
                        gatherer.register$org_openprovenance_prov_model_QualifiedName(bundle.getId());
                        const ns = gatherer.getNamespace();
                        return ns;
                    }
                    static gatherNamespaces$org_openprovenance_prov_model_Bundle$org_openprovenance_prov_model_ProvFactory(bundle, pFactory) {
                        const gatherer = new org.openprovenance.prov.model.NamespaceGatherer();
                        Namespace.u_$LI$().forAllStatement(bundle.getStatement(), gatherer);
                        gatherer.register$org_openprovenance_prov_model_QualifiedName(bundle.getId());
                        const ns = gatherer.getNamespace();
                        const ns2 = pFactory.newNamespace$org_openprovenance_prov_model_Namespace(ns);
                        return ns2;
                    }
                    static gatherNamespaces(bundle, pFactory) {
                        if (((bundle != null && (bundle.constructor != null && bundle.constructor["__interfaces"] != null && bundle.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Bundle") >= 0)) || bundle === null) && ((pFactory != null && pFactory instanceof org.openprovenance.prov.model.ProvFactory) || pFactory === null)) {
                            return org.openprovenance.prov.model.Namespace.gatherNamespaces$org_openprovenance_prov_model_Bundle$org_openprovenance_prov_model_ProvFactory(bundle, pFactory);
                        }
                        else if (((bundle != null && (bundle.constructor != null && bundle.constructor["__interfaces"] != null && bundle.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Document") >= 0)) || bundle === null) && pFactory === undefined) {
                            return org.openprovenance.prov.model.Namespace.gatherNamespaces$org_openprovenance_prov_model_Document(bundle);
                        }
                        else if (((bundle != null && (bundle.constructor != null && bundle.constructor["__interfaces"] != null && bundle.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Bundle") >= 0)) || bundle === null) && pFactory === undefined) {
                            return org.openprovenance.prov.model.Namespace.gatherNamespaces$org_openprovenance_prov_model_Bundle(bundle);
                        }
                        else
                            throw new Error('invalid overload');
                    }
                    stringToQualifiedName$java_lang_String$org_openprovenance_prov_model_ProvFactory(id, pFactory) {
                        return this.stringToQualifiedName$java_lang_String$org_openprovenance_prov_model_ProvFactory$boolean(id, pFactory, true);
                    }
                    static qnU_$LI$() { if (Namespace.qnU == null) {
                        Namespace.qnU = new org.openprovenance.prov.model.QualifiedNameUtils();
                    } return Namespace.qnU; }
                    stringToQualifiedName$java_lang_String$org_openprovenance_prov_model_ProvFactory$boolean(id, pFactory, isEscaped) {
                        if (id == null)
                            return null;
                      //  console.log(id)
                     //   console.log(this.prefixes)
                        const index = id.indexOf(':');
                        if (index === -1) {
                            let tmp = this.getDefaultNamespace();
                            if (tmp == null) {
                                if (this.parent != null) {
                                    tmp = this.parent.getDefaultNamespace();
                                    if (tmp == null)
                                        throw Object.defineProperty(new Error("Namespace.stringToQualifiedName(): Null namespace for parent " + id), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.NullPointerException', 'java.lang.Exception'] });
                                }
                                else {
                                    throw Object.defineProperty(new Error("Namespace.stringToQualifiedName(): Null namespace for " + id), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.NullPointerException', 'java.lang.Exception'] });
                                }
                            }
                            return pFactory.newQualifiedName$java_lang_String$java_lang_String$java_lang_String(tmp, id, null);
                        }
                        const prefix = id.substring(0, index);
                        const tmp = ((m, k) => m[k] === undefined ? null : m[k])(this.prefixes, prefix);
                        if (tmp == null) {
                            if (this.parent != null) {
                                return this.parent.stringToQualifiedName$java_lang_String$org_openprovenance_prov_model_ProvFactory$boolean(id, pFactory, isEscaped);
                            }
                            else {
                                throw new org.openprovenance.prov.model.exception.QualifiedNameException("Namespace.stringToQualifiedName(): Null namespace for " + id + " namespace " + this);
                            }
                        }
                        const local = id.substring(index + 1);
                        if (isEscaped) {
                         //   console.log("escaping id " + tmp)
                            return pFactory.newQualifiedName$java_lang_String$java_lang_String$java_lang_String(tmp, local, prefix);
                        }
                        else {
                          //  console.log("not escaping id " + tmp)
                          //  console.log(tmp)
                            return pFactory.newQualifiedName$java_lang_String$java_lang_String$java_lang_String(tmp, Namespace.qnU_$LI$().escapeProvLocalName(local), prefix);
                        }
                    }
                    stringToQualifiedName(id, pFactory, isEscaped) {
                        if (((typeof id === 'string') || id === null) && ((pFactory != null && pFactory instanceof org.openprovenance.prov.model.ProvFactory) || pFactory === null) && ((typeof isEscaped === 'boolean') || isEscaped === null)) {
                            return this.stringToQualifiedName$java_lang_String$org_openprovenance_prov_model_ProvFactory$boolean(id, pFactory, isEscaped);
                        }
                        else if (((typeof id === 'string') || id === null) && ((pFactory != null && pFactory instanceof org.openprovenance.prov.model.ProvFactory) || pFactory === null) && isEscaped === undefined) {
                            return this.stringToQualifiedName$java_lang_String$org_openprovenance_prov_model_ProvFactory(id, pFactory);
                        }
                        else
                            throw new Error('invalid overload');
                    }
                    /**
                     * Creates a qualified name, with given prefix and local name. The prefix needs to have been pre-registered.
                     * Prefix is allowed to be null: in that case, the intended namespace is the default namespace
                     * (see {@link Namespace#getDefaultNamespace()}) which must have been pre-registered.
                     * @param {string} prefix the prefix for the {@link QualifiedName}
                     * @param {string} local the local name for the {@link QualifiedName}
                     * @param {org.openprovenance.prov.model.ProvFactory} pFactory the factory method used to construct the {@link QualifiedName}
                     * @return {*} a {@link QualifiedName}
                     * @throws QualifiedNameException if prefix is not pre-registered.
                     * @throws NullPointerException if prefix is null and defaultnamespace has not been registered.
                     */
                    qualifiedName(prefix, local, pFactory) {
                        if (prefix == null) {
                            let tmp = this.getDefaultNamespace();
                            if (tmp == null && this.parent != null)
                                tmp = this.parent.getDefaultNamespace();
                            if (tmp == null)
                                throw Object.defineProperty(new Error("Namespace.stringToQualifiedName(: Null namespace for " + local), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.NullPointerException', 'java.lang.Exception'] });
                            return pFactory.newQualifiedName$java_lang_String$java_lang_String$java_lang_String(tmp, local, null);
                        }
                        if ("prov" === prefix) {
                            return pFactory.newQualifiedName$java_lang_String$java_lang_String$java_lang_String(org.openprovenance.prov.model.NamespacePrefixMapper.PROV_NS, local, prefix);
                        }
                        else if ("xsd" === prefix) {
                            return pFactory.newQualifiedName$java_lang_String$java_lang_String$java_lang_String(org.openprovenance.prov.model.NamespacePrefixMapper.XSD_NS, local, prefix);
                        }
                        else {
                            const tmp = ((m, k) => m[k] === undefined ? null : m[k])(this.prefixes, prefix);
                            if (tmp == null) {
                                if (this.parent != null) {
                                    return this.parent.qualifiedName(prefix, local, pFactory);
                                }
                                else {
                                    throw new org.openprovenance.prov.model.exception.QualifiedNameException("Namespace.stringToQualifiedName(): Null namespace for " + prefix + ":" + local + " namespace " + this);
                                }
                            }
                            return pFactory.newQualifiedName$java_lang_String$java_lang_String$java_lang_String(tmp, local, prefix);
                        }
                    }
                    static qualifiedNameToStringWithNamespace(name) {
                        const ns = Namespace.getThreadNamespace();
                        return ns.qualifiedNameToString$org_openprovenance_prov_model_QualifiedName(name);
                    }
                    qualifiedNameToString$javax_xml_namespace_QName(name) {
                        return this.qualifiedNameToString$javax_xml_namespace_QName$org_openprovenance_prov_model_Namespace(name, null);
                    }
                    qualifiedNameToString$org_openprovenance_prov_model_QualifiedName(name) {
                        return this.qualifiedNameToString$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_Namespace(name, null);
                    }
                    qualifiedNameToString$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_Namespace(name, child) {
                        if ((this.getDefaultNamespace() != null) && (this.getDefaultNamespace() === name.getNamespaceURI())) {
                            return name.getLocalPart();
                        }
                        else {
                            const pref = ((m, k) => m[k] === undefined ? null : m[k])(this.getNamespaces(), name.getNamespaceURI());
                            if (pref != null) {
                                return pref + ":" + name.getLocalPart();
                            }
                            else {
                                if (this.parent != null) {
                                    return this.parent.qualifiedNameToString$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_Namespace(name, this);
                                }
                                else
                                    throw new org.openprovenance.prov.model.exception.QualifiedNameException("unknown qualified name " + name + " with namespace " + this.toString() + ((child == null) ? "" : (" and " + child)));
                            }
                        }
                    }
                    /**
                     * @param {*} name the QualifiedName to convert to string
                     * @param {org.openprovenance.prov.model.Namespace} child argument used just for the purpose of debugging when throwing an exception
                     * @return {string} a string representation of the QualifiedName
                     */
                    qualifiedNameToString(name, child) {
                        if (((name != null && (name.constructor != null && name.constructor["__interfaces"] != null && name.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || name === null) && ((child != null && child instanceof org.openprovenance.prov.model.Namespace) || child === null)) {
                            return this.qualifiedNameToString$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_Namespace(name, child);
                        }
                        else if (((name != null && name instanceof javax.xml.namespace.QName) || name === null) && ((child != null && child instanceof org.openprovenance.prov.model.Namespace) || child === null)) {
                            return this.qualifiedNameToString$javax_xml_namespace_QName$org_openprovenance_prov_model_Namespace(name, child);
                        }
                        else if (((name != null && name instanceof javax.xml.namespace.QName) || name === null) && child === undefined) {
                            return this.qualifiedNameToString$javax_xml_namespace_QName(name);
                        }
                        else if (((name != null && (name.constructor != null && name.constructor["__interfaces"] != null && name.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || name === null) && child === undefined) {
                            return this.qualifiedNameToString$org_openprovenance_prov_model_QualifiedName(name);
                        }
                        else
                            throw new Error('invalid overload');
                    }
                    qualifiedNameToString$javax_xml_namespace_QName$org_openprovenance_prov_model_Namespace(name, child) {
                        if ((this.getDefaultNamespace() != null) && (this.getDefaultNamespace() === name.getNamespaceURI())) {
                            return name.getLocalPart();
                        }
                        else {
                            const pref = ((m, k) => m[k] === undefined ? null : m[k])(this.getNamespaces(), name.getNamespaceURI());
                            if (pref != null) {
                                return pref + ":" + name.getLocalPart();
                            }
                            else {
                                if (this.parent != null) {
                                    return this.parent.qualifiedNameToString$javax_xml_namespace_QName$org_openprovenance_prov_model_Namespace(name, this);
                                }
                                else
                                    throw new org.openprovenance.prov.model.exception.QualifiedNameException("unknown qualified name " + name + " with namespace " + this.toString() + ((child == null) ? "" : (" and " + child)));
                            }
                        }
                    }
                    toString() {
                        return "[Namespace (" + this.defaultNamespace + ") " + this.prefixes + ", parent: " + this.parent + "]";
                    }
                }
                Namespace.xmlns = "pre_";
                model.Namespace = Namespace;
                Namespace["__class"] = "org.openprovenance.prov.model.Namespace";
                (function (Namespace) {
                    class Namespace$0 {
                        constructor(__parent) {
                            this.__parent = __parent;
                        }
                        initialValue() {
                            return new org.openprovenance.prov.model.Namespace();
                        }
                    }
                    Namespace.Namespace$0 = Namespace$0;
                })(Namespace = model.Namespace || (model.Namespace = {}));
            })(model = prov.model || (prov.model = {}));
        })(prov = openprovenance.prov || (openprovenance.prov = {}));
    })(openprovenance = org.openprovenance || (org.openprovenance = {}));
})(org || (org = {}));
