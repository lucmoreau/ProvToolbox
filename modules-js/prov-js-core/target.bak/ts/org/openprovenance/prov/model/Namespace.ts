/* Generated from Java with JSweet 3.0.0 - http://www.jsweet.org */
namespace org.openprovenance.prov.model {
    /**
     * A class to manipulate Namespaces when creating, serializing and converting prov documents.
     * @author Luc Moreau
     * 
     * @param {*} pref
     * @class
     */
    export class Namespace {
        static threadNamespace: java.lang.ThreadLocal<Namespace>; public static threadNamespace_$LI$(): java.lang.ThreadLocal<Namespace> { if (Namespace.threadNamespace == null) { Namespace.threadNamespace = new Namespace.Namespace$0(this); }  return Namespace.threadNamespace; }

        public static getThreadNamespace(): Namespace {
            return /* get */((tlObj: any) => {    if (tlObj.___value) { return tlObj.___value }     else { return tlObj.___value = tlObj.initialValue() }   })(Namespace.threadNamespace_$LI$());
        }

        public static withThreadNamespace(ns: Namespace): Namespace {
            return /* get */((tlObj: any) => {    if (tlObj.___value) { return tlObj.___value }     else { return tlObj.___value = tlObj.initialValue() }   })(Namespace.threadNamespace_$LI$()).set(ns);
        }

        /*private*/ set(ns: Namespace): Namespace {
            if (ns == null)return null;
            const tmp1: any = <any>({});
            tmp1.putAll(ns.prefixes);
            const tmp2: any = <any>({});
            tmp2.putAll(ns.namespaces);
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
        public extendWith(ns: Namespace) {
            if (ns == null)return;
            if (ns.getDefaultNamespace() != null){
                this.registerDefault(ns.getDefaultNamespace());
            }
            {
                let array163 = /* keySet */Object.keys(ns.prefixes);
                for(let index162=0; index162 < array163.length; index162++) {
                    let prefix = array163[index162];
                    {
                        this.register(prefix, /* get */((m,k) => m[k]===undefined?null:m[k])(ns.prefixes, prefix));
                    }
                }
            }
        }

        prefixes: any;

        namespaces: any;

        /*private*/ defaultNamespace: string;

        /*private*/ parent: Namespace;

        public setParent(parent: Namespace) {
            this.parent = parent;
        }

        public getParent(): Namespace {
            return this.parent;
        }

        public addKnownNamespaces() {
            /* put */(this.getPrefixes()["prov"] = org.openprovenance.prov.model.NamespacePrefixMapper.PROV_NS);
            /* put */(this.getNamespaces()[org.openprovenance.prov.model.NamespacePrefixMapper.PROV_NS] = "prov");
            /* put */(this.getPrefixes()["xsd"] = org.openprovenance.prov.model.NamespacePrefixMapper.XSD_NS);
            /* put */(this.getNamespaces()[org.openprovenance.prov.model.NamespacePrefixMapper.XSD_NS] = "xsd");
        }

        public constructor(pref?: any) {
            if (((pref != null && (pref instanceof Object)) || pref === null)) {
                let __args = arguments;
                this.prefixes = <any>({});
                this.namespaces = <any>({});
                this.defaultNamespace = null;
                this.parent = null;
                this.prefixCount = 0;
                {
                    let array165 = /* entrySet */(o => { let s = []; for (let e in o) s.push({ k: e, v: o[e], getKey: function() { return this.k }, getValue: function() { return this.v } }); return s; })(pref);
                    for(let index164=0; index164 < array165.length; index164++) {
                        let entry = array165[index164];
                        {
                            /* put */(this.prefixes[entry.getKey()] = entry.getValue());
                            /* put */(this.namespaces[entry.getValue()] = entry.getKey());
                        }
                    }
                }
            } else if (((pref != null && pref instanceof <any>org.openprovenance.prov.model.Namespace) || pref === null)) {
                let __args = arguments;
                let other: any = __args[0];
                this.prefixes = <any>({});
                this.namespaces = <any>({});
                this.defaultNamespace = null;
                this.parent = null;
                this.prefixCount = 0;
                this.defaultNamespace = other.defaultNamespace;
                this.prefixes = <any>({});
                this.prefixes.putAll(other.prefixes);
                this.namespaces = <any>({});
                this.namespaces.putAll(other.namespaces);
            } else if (pref === undefined) {
                let __args = arguments;
                this.prefixes = <any>({});
                this.namespaces = <any>({});
                this.defaultNamespace = null;
                this.parent = null;
                this.prefixCount = 0;
            } else throw new Error('invalid overload');
        }

        public getDefaultNamespace(): string {
            return this.defaultNamespace;
        }

        public setDefaultNamespace(defaultNamespace: string) {
            this.defaultNamespace = defaultNamespace;
        }

        public getPrefixes(): any {
            return this.prefixes;
        }

        public getNamespaces(): any {
            return this.namespaces;
        }

        public check(prefix: string, namespace: string): boolean {
            const knownAs: string = /* get */((m,k) => m[k]===undefined?null:m[k])(this.prefixes, prefix);
            return namespace === knownAs;
        }

        public registerDefault(namespace: string) {
            this.register(null, namespace);
        }

        public register(prefix: string, namespace: string) {
            if ((prefix == null) || (prefix === javax.xml.XMLConstants.DEFAULT_NS_PREFIX)){
                if (this.defaultNamespace == null){
                    this.defaultNamespace = namespace;
                } else {
                    this.newPrefix(namespace);
                }
            } else {
                const old: string = /* get */((m,k) => m[k]===undefined?null:m[k])(this.prefixes, prefix);
                if (old == null){
                    /* put */(this.prefixes[prefix] = namespace);
                    if (/* get */((m,k) => m[k]===undefined?null:m[k])(this.namespaces, namespace) == null){
                        /* put */(this.namespaces[namespace] = prefix);
                    }
                } else {
                    this.newPrefix(namespace);
                }
            }
        }

        public static xmlns: string = "pre_";

        prefixCount: number;

        public newPrefix(namespace: string) {
            let success: boolean = false;
            while((!success)) {{
                const old: string = /* get */((m,k) => m[k]===undefined?null:m[k])(this.namespaces, namespace);
                if (old != null)return;
                const count: number = this.prefixCount++;
                const newPrefix: string = Namespace.xmlns + count;
                const oldPrefix: string = /* get */((m,k) => m[k]===undefined?null:m[k])(this.prefixes, newPrefix);
                if (oldPrefix == null){
                    /* put */(this.prefixes[newPrefix] = namespace);
                    success = true;
                    if (/* get */((m,k) => m[k]===undefined?null:m[k])(this.namespaces, namespace) == null){
                        /* put */(this.namespaces[namespace] = newPrefix);
                    }
                }
            }};
        }

        public unregister(prefix: string, namespace: string) {
            const val: string = /* get */((m,k) => m[k]===undefined?null:m[k])(this.prefixes, prefix);
            if (val != null){
                if (val === namespace){
                    /* remove */(map => { let deleted = this.prefixes[prefix];delete this.prefixes[prefix];return deleted;})(this.prefixes);
                    /* remove */(map => { let deleted = this.namespaces[namespace];delete this.namespaces[namespace];return deleted;})(this.namespaces);
                }
            }
        }

        public unregisterDeafult(namespace: string) {
            const val: string = this.getDefaultNamespace();
            if (val != null){
                if (val === namespace){
                    this.setDefaultNamespace(null);
                }
            }
        }

        static u: org.openprovenance.prov.model.ProvUtilities; public static u_$LI$(): org.openprovenance.prov.model.ProvUtilities { if (Namespace.u == null) { Namespace.u = new org.openprovenance.prov.model.ProvUtilities(); }  return Namespace.u; }

        public static gatherNamespaces$org_openprovenance_prov_model_Document(doc: org.openprovenance.prov.model.Document): Namespace {
            const gatherer: org.openprovenance.prov.model.NamespaceGatherer = new org.openprovenance.prov.model.NamespaceGatherer();
            Namespace.u_$LI$().forAllStatementOrBundle(doc.getStatementOrBundle(), gatherer);
            const ns: Namespace = gatherer.getNamespace();
            return ns;
        }

        public static gatherNamespaces$org_openprovenance_prov_model_Bundle(bundle: org.openprovenance.prov.model.Bundle): Namespace {
            const gatherer: org.openprovenance.prov.model.NamespaceGatherer = new org.openprovenance.prov.model.NamespaceGatherer();
            Namespace.u_$LI$().forAllStatement(bundle.getStatement(), gatherer);
            gatherer.register$org_openprovenance_prov_model_QualifiedName(bundle.getId());
            const ns: Namespace = gatherer.getNamespace();
            return ns;
        }

        public static gatherNamespaces$org_openprovenance_prov_model_Bundle$org_openprovenance_prov_model_ProvFactory(bundle: org.openprovenance.prov.model.Bundle, pFactory: org.openprovenance.prov.model.ProvFactory): Namespace {
            const gatherer: org.openprovenance.prov.model.NamespaceGatherer = new org.openprovenance.prov.model.NamespaceGatherer();
            Namespace.u_$LI$().forAllStatement(bundle.getStatement(), gatherer);
            gatherer.register$org_openprovenance_prov_model_QualifiedName(bundle.getId());
            const ns: Namespace = gatherer.getNamespace();
            const ns2: Namespace = pFactory.newNamespace$org_openprovenance_prov_model_Namespace(ns);
            return ns2;
        }

        public static gatherNamespaces(bundle?: any, pFactory?: any): any {
            if (((bundle != null && (bundle.constructor != null && bundle.constructor["__interfaces"] != null && bundle.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Bundle") >= 0)) || bundle === null) && ((pFactory != null && pFactory instanceof <any>org.openprovenance.prov.model.ProvFactory) || pFactory === null)) {
                return <any>org.openprovenance.prov.model.Namespace.gatherNamespaces$org_openprovenance_prov_model_Bundle$org_openprovenance_prov_model_ProvFactory(bundle, pFactory);
            } else if (((bundle != null && (bundle.constructor != null && bundle.constructor["__interfaces"] != null && bundle.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Document") >= 0)) || bundle === null) && pFactory === undefined) {
                return <any>org.openprovenance.prov.model.Namespace.gatherNamespaces$org_openprovenance_prov_model_Document(bundle);
            } else if (((bundle != null && (bundle.constructor != null && bundle.constructor["__interfaces"] != null && bundle.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Bundle") >= 0)) || bundle === null) && pFactory === undefined) {
                return <any>org.openprovenance.prov.model.Namespace.gatherNamespaces$org_openprovenance_prov_model_Bundle(bundle);
            } else throw new Error('invalid overload');
        }

        public stringToQualifiedName$java_lang_String$org_openprovenance_prov_model_ProvFactory(id: string, pFactory: org.openprovenance.prov.model.ProvFactory): org.openprovenance.prov.model.QualifiedName {
            return this.stringToQualifiedName$java_lang_String$org_openprovenance_prov_model_ProvFactory$boolean(id, pFactory, true);
        }

        static qnU: org.openprovenance.prov.model.QualifiedNameUtils; public static qnU_$LI$(): org.openprovenance.prov.model.QualifiedNameUtils { if (Namespace.qnU == null) { Namespace.qnU = new org.openprovenance.prov.model.QualifiedNameUtils(); }  return Namespace.qnU; }

        public stringToQualifiedName$java_lang_String$org_openprovenance_prov_model_ProvFactory$boolean(id: string, pFactory: org.openprovenance.prov.model.ProvFactory, isEscaped: boolean): org.openprovenance.prov.model.QualifiedName {
            if (id == null)return null;
            const index: number = id.indexOf(':');
            if (index === -1){
                let tmp: string = this.getDefaultNamespace();
                if (tmp == null){
                    if (this.parent != null){
                        tmp = this.parent.getDefaultNamespace();
                        if (tmp == null)throw Object.defineProperty(new Error("Namespace.stringToQualifiedName(): Null namespace for parent " + id), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.Object','java.lang.RuntimeException','java.lang.NullPointerException','java.lang.Exception'] });
                    } else {
                        throw Object.defineProperty(new Error("Namespace.stringToQualifiedName(): Null namespace for " + id), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.Object','java.lang.RuntimeException','java.lang.NullPointerException','java.lang.Exception'] });
                    }
                }
                return pFactory.newQualifiedName$java_lang_String$java_lang_String$java_lang_String(tmp, id, null);
            }
            const prefix: string = id.substring(0, index);
            const tmp: string = /* get */((m,k) => m[k]===undefined?null:m[k])(this.prefixes, prefix);
            if (tmp == null){
                if (this.parent != null){
                    return this.parent.stringToQualifiedName$java_lang_String$org_openprovenance_prov_model_ProvFactory$boolean(id, pFactory, isEscaped);
                } else {
                    throw new org.openprovenance.prov.model.exception.QualifiedNameException("Namespace.stringToQualifiedName(): Null namespace for " + id + " namespace " + this);
                }
            }
            const local: string = id.substring(index + 1);
            if (isEscaped){
                return pFactory.newQualifiedName$java_lang_String$java_lang_String$java_lang_String(tmp, local, prefix);
            } else {
                return pFactory.newQualifiedName$java_lang_String$java_lang_String$java_lang_String(tmp, Namespace.qnU_$LI$().escapeProvLocalName(local), prefix);
            }
        }

        public stringToQualifiedName(id?: any, pFactory?: any, isEscaped?: any): any {
            if (((typeof id === 'string') || id === null) && ((pFactory != null && pFactory instanceof <any>org.openprovenance.prov.model.ProvFactory) || pFactory === null) && ((typeof isEscaped === 'boolean') || isEscaped === null)) {
                return <any>this.stringToQualifiedName$java_lang_String$org_openprovenance_prov_model_ProvFactory$boolean(id, pFactory, isEscaped);
            } else if (((typeof id === 'string') || id === null) && ((pFactory != null && pFactory instanceof <any>org.openprovenance.prov.model.ProvFactory) || pFactory === null) && isEscaped === undefined) {
                return <any>this.stringToQualifiedName$java_lang_String$org_openprovenance_prov_model_ProvFactory(id, pFactory);
            } else throw new Error('invalid overload');
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
        public qualifiedName(prefix: string, local: string, pFactory: org.openprovenance.prov.model.ProvFactory): org.openprovenance.prov.model.QualifiedName {
            if (prefix == null){
                let tmp: string = this.getDefaultNamespace();
                if (tmp == null && this.parent != null)tmp = this.parent.getDefaultNamespace();
                if (tmp == null)throw Object.defineProperty(new Error("Namespace.stringToQualifiedName(: Null namespace for " + local), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.Object','java.lang.RuntimeException','java.lang.NullPointerException','java.lang.Exception'] });
                return pFactory.newQualifiedName$java_lang_String$java_lang_String$java_lang_String(tmp, local, null);
            }
            if ("prov" === prefix){
                return pFactory.newQualifiedName$java_lang_String$java_lang_String$java_lang_String(org.openprovenance.prov.model.NamespacePrefixMapper.PROV_NS, local, prefix);
            } else if ("xsd" === prefix){
                return pFactory.newQualifiedName$java_lang_String$java_lang_String$java_lang_String(org.openprovenance.prov.model.NamespacePrefixMapper.XSD_NS, local, prefix);
            } else {
                const tmp: string = /* get */((m,k) => m[k]===undefined?null:m[k])(this.prefixes, prefix);
                if (tmp == null){
                    if (this.parent != null){
                        return this.parent.qualifiedName(prefix, local, pFactory);
                    } else {
                        throw new org.openprovenance.prov.model.exception.QualifiedNameException("Namespace.stringToQualifiedName(): Null namespace for " + prefix + ":" + local + " namespace " + this);
                    }
                }
                return pFactory.newQualifiedName$java_lang_String$java_lang_String$java_lang_String(tmp, local, prefix);
            }
        }

        public static qualifiedNameToStringWithNamespace(name: org.openprovenance.prov.model.QualifiedName): string {
            const ns: Namespace = Namespace.getThreadNamespace();
            return ns.qualifiedNameToString$org_openprovenance_prov_model_QualifiedName(name);
        }

        public qualifiedNameToString$javax_xml_namespace_QName(name: javax.xml.namespace.QName): string {
            return this.qualifiedNameToString$javax_xml_namespace_QName$org_openprovenance_prov_model_Namespace(name, null);
        }

        public qualifiedNameToString$org_openprovenance_prov_model_QualifiedName(name: org.openprovenance.prov.model.QualifiedName): string {
            return this.qualifiedNameToString$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_Namespace(name, null);
        }

        public qualifiedNameToString$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_Namespace(name: org.openprovenance.prov.model.QualifiedName, child: Namespace): string {
            if ((this.getDefaultNamespace() != null) && (this.getDefaultNamespace() === name.getNamespaceURI())){
                return name.getLocalPart();
            } else {
                const pref: string = /* get */((m,k) => m[k]===undefined?null:m[k])(this.getNamespaces(), name.getNamespaceURI());
                if (pref != null){
                    return pref + ":" + name.getLocalPart();
                } else {
                    if (this.parent != null){
                        return this.parent.qualifiedNameToString$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_Namespace(name, this);
                    } else throw new org.openprovenance.prov.model.exception.QualifiedNameException("unknown qualified name " + name + " with namespace " + this.toString() + ((child == null) ? "" : (" and " + child)));
                }
            }
        }

        /**
         * @param {*} name the QualifiedName to convert to string
         * @param {org.openprovenance.prov.model.Namespace} child argument used just for the purpose of debugging when throwing an exception
         * @return {string} a string representation of the QualifiedName
         */
        public qualifiedNameToString(name?: any, child?: any): any {
            if (((name != null && (name.constructor != null && name.constructor["__interfaces"] != null && name.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || name === null) && ((child != null && child instanceof <any>org.openprovenance.prov.model.Namespace) || child === null)) {
                return <any>this.qualifiedNameToString$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_Namespace(name, child);
            } else if (((name != null && name instanceof <any>javax.xml.namespace.QName) || name === null) && ((child != null && child instanceof <any>org.openprovenance.prov.model.Namespace) || child === null)) {
                return <any>this.qualifiedNameToString$javax_xml_namespace_QName$org_openprovenance_prov_model_Namespace(name, child);
            } else if (((name != null && name instanceof <any>javax.xml.namespace.QName) || name === null) && child === undefined) {
                return <any>this.qualifiedNameToString$javax_xml_namespace_QName(name);
            } else if (((name != null && (name.constructor != null && name.constructor["__interfaces"] != null && name.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || name === null) && child === undefined) {
                return <any>this.qualifiedNameToString$org_openprovenance_prov_model_QualifiedName(name);
            } else throw new Error('invalid overload');
        }

        public qualifiedNameToString$javax_xml_namespace_QName$org_openprovenance_prov_model_Namespace(name: javax.xml.namespace.QName, child: Namespace): string {
            if ((this.getDefaultNamespace() != null) && (this.getDefaultNamespace() === name.getNamespaceURI())){
                return name.getLocalPart();
            } else {
                const pref: string = /* get */((m,k) => m[k]===undefined?null:m[k])(this.getNamespaces(), name.getNamespaceURI());
                if (pref != null){
                    return pref + ":" + name.getLocalPart();
                } else {
                    if (this.parent != null){
                        return this.parent.qualifiedNameToString$javax_xml_namespace_QName$org_openprovenance_prov_model_Namespace(name, this);
                    } else throw new org.openprovenance.prov.model.exception.QualifiedNameException("unknown qualified name " + name + " with namespace " + this.toString() + ((child == null) ? "" : (" and " + child)));
                }
            }
        }

        public toString(): string {
            return "[Namespace (" + this.defaultNamespace + ") " + this.prefixes + ", parent: " + this.parent + "]";
        }
    }
    Namespace["__class"] = "org.openprovenance.prov.model.Namespace";


    export namespace Namespace {

        export class Namespace$0 {
            public __parent: any;
            initialValue(): org.openprovenance.prov.model.Namespace {
                return new org.openprovenance.prov.model.Namespace();
            }

            constructor(__parent: any) {
                this.__parent = __parent;
            }
        }

    }

}

