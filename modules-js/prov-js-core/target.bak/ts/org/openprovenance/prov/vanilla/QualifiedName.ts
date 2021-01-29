/* Generated from Java with JSweet 3.0.0 - http://www.jsweet.org */
namespace org.openprovenance.prov.vanilla {
    export class QualifiedName implements org.openprovenance.prov.model.QualifiedName {
        static qnU: org.openprovenance.prov.model.QualifiedNameUtils; public static qnU_$LI$(): org.openprovenance.prov.model.QualifiedNameUtils { if (QualifiedName.qnU == null) { QualifiedName.qnU = new org.openprovenance.prov.model.QualifiedNameUtils(); }  return QualifiedName.qnU; }

        /*private*/ namespace: string;

        /*private*/ local: string;

        /*private*/ prefix: string;

        public constructor(namespaceURI?: any, localPart?: any, prefix?: any) {
            if (((typeof namespaceURI === 'string') || namespaceURI === null) && ((typeof localPart === 'string') || localPart === null) && ((typeof prefix === 'string') || prefix === null)) {
                let __args = arguments;
                if (this.namespace === undefined) { this.namespace = null; } 
                if (this.local === undefined) { this.local = null; } 
                if (this.prefix === undefined) { this.prefix = null; } 
                if (this.ref === undefined) { this.ref = null; } 
                this.namespace = namespaceURI;
                this.local = localPart;
                this.prefix = prefix;
            } else if (namespaceURI === undefined && localPart === undefined && prefix === undefined) {
                let __args = arguments;
                if (this.namespace === undefined) { this.namespace = null; } 
                if (this.local === undefined) { this.local = null; } 
                if (this.prefix === undefined) { this.prefix = null; } 
                if (this.ref === undefined) { this.ref = null; } 
            } else throw new Error('invalid overload');
        }

        ref: org.openprovenance.prov.model.QualifiedName;

        /**
         * 
         * @return {javax.xml.namespace.QName}
         */
        public toQName(): javax.xml.namespace.QName {
            const escapedLocal: string = QualifiedName.qnU_$LI$().escapeToXsdLocalName(this.getUnescapedLocalPart());
            if (QualifiedName.qnU_$LI$().is_NC_Name(escapedLocal)){
                if (this.prefix == null){
                    return new javax.xml.namespace.QName(this.namespace, escapedLocal);
                } else {
                    return new javax.xml.namespace.QName(this.namespace, escapedLocal, this.prefix);
                }
            } else {
                throw new org.openprovenance.prov.model.exception.QualifiedNameException("PROV-XML QName: local not valid " + escapedLocal);
            }
        }

        /*private*/ getUnescapedLocalPart(): string {
            return QualifiedName.qnU_$LI$().unescapeProvLocalName(this.local);
        }

        /**
         * 
         * @return {string}
         */
        public getUri(): string {
            return this.getNamespaceURI() + this.getUnescapedLocalPart();
        }

        /**
         * 
         * @param {string} uri
         */
        public setUri(uri: string) {
        }

        /**
         * 
         * @return {string}
         */
        public getLocalPart(): string {
            return this.local;
        }

        /**
         * 
         * @param {string} local
         */
        public setLocalPart(local: string) {
        }

        /**
         * 
         * @return {string}
         */
        public getNamespaceURI(): string {
            return this.namespace;
        }

        /**
         * 
         * @param {string} namespaceURI
         */
        public setNamespaceURI(namespaceURI: string) {
        }

        /**
         * 
         * @return {string}
         */
        public getPrefix(): string {
            return this.prefix;
        }

        /**
         * 
         * @param {string} prefix
         */
        public setPrefix(prefix: string) {
        }

        /**
         * 
         * @param {*} objectToTest
         * @return {boolean}
         */
        public equals(objectToTest: any): boolean {
            if (objectToTest === this){
                return true;
            }
            if (objectToTest != null && objectToTest instanceof <any>org.openprovenance.prov.vanilla.QualifiedName){
                const qualifiedName: QualifiedName = <QualifiedName>objectToTest;
                return (this.local === qualifiedName.local) && (this.namespace === qualifiedName.namespace);
            }
            return false;
        }

        /**
         * 
         * @return {number}
         */
        public hashCode(): number {
            return /* hashCode */(<any>((o: any) => { if (o.hashCode) { return o.hashCode(); } else { return o.toString().split('').reduce((prevHash, currVal) => (((prevHash << 5) - prevHash) + currVal.charCodeAt(0))|0, 0); }})(this.namespace)) ^ /* hashCode */(<any>((o: any) => { if (o.hashCode) { return o.hashCode(); } else { return o.toString().split('').reduce((prevHash, currVal) => (((prevHash << 5) - prevHash) + currVal.charCodeAt(0))|0, 0); }})(this.local));
        }

        public toString(): string {
            return "\'" + this.prefix + ":{{" + this.namespace + "}}" + this.local + "\'";
        }
    }
    QualifiedName["__class"] = "org.openprovenance.prov.vanilla.QualifiedName";
    QualifiedName["__interfaces"] = ["org.openprovenance.prov.model.QualifiedName"];


}

