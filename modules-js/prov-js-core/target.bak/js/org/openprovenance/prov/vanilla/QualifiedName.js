/* Generated from Java with JSweet 3.0.0 - http://www.jsweet.org */
var org;
(function (org) {
    var openprovenance;
    (function (openprovenance) {
        var prov;
        (function (prov) {
            var vanilla;
            (function (vanilla) {
                class QualifiedName {
                    constructor(namespaceURI, localPart, prefix) {
                        if (((typeof namespaceURI === 'string') || namespaceURI === null) && ((typeof localPart === 'string') || localPart === null) && ((typeof prefix === 'string') || prefix === null)) {
                            let __args = arguments;
                            if (this.namespace === undefined) {
                                this.namespace = null;
                            }
                            if (this.local === undefined) {
                                this.local = null;
                            }
                            if (this.prefix === undefined) {
                                this.prefix = null;
                            }
                            if (this.ref === undefined) {
                                this.ref = null;
                            }
                            this.namespace = namespaceURI;
                            this.local = localPart;
                            this.prefix = prefix;
                        }
                        else if (namespaceURI === undefined && localPart === undefined && prefix === undefined) {
                            let __args = arguments;
                            if (this.namespace === undefined) {
                                this.namespace = null;
                            }
                            if (this.local === undefined) {
                                this.local = null;
                            }
                            if (this.prefix === undefined) {
                                this.prefix = null;
                            }
                            if (this.ref === undefined) {
                                this.ref = null;
                            }
                        }
                        else
                            throw new Error('invalid overload');
                    }
                    static qnU_$LI$() { if (QualifiedName.qnU == null) {
                        QualifiedName.qnU = new org.openprovenance.prov.model.QualifiedNameUtils();
                    } return QualifiedName.qnU; }
                    /**
                     *
                     * @return {javax.xml.namespace.QName}
                     */
                    toQName() {
                        const escapedLocal = QualifiedName.qnU_$LI$().escapeToXsdLocalName(this.getUnescapedLocalPart());
                        if (QualifiedName.qnU_$LI$().is_NC_Name(escapedLocal)) {
                            if (this.prefix == null) {
                                return new javax.xml.namespace.QName(this.namespace, escapedLocal);
                            }
                            else {
                                return new javax.xml.namespace.QName(this.namespace, escapedLocal, this.prefix);
                            }
                        }
                        else {
                            throw new org.openprovenance.prov.model.exception.QualifiedNameException("PROV-XML QName: local not valid " + escapedLocal);
                        }
                    }
                    /*private*/ getUnescapedLocalPart() {
                        return QualifiedName.qnU_$LI$().unescapeProvLocalName(this.local);
                    }
                    /**
                     *
                     * @return {string}
                     */
                    getUri() {
                        return this.getNamespaceURI() + this.getUnescapedLocalPart();
                    }
                    /**
                     *
                     * @param {string} uri
                     */
                    setUri(uri) {
                    }
                    /**
                     *
                     * @return {string}
                     */
                    getLocalPart() {
                        return this.local;
                    }
                    /**
                     *
                     * @param {string} local
                     */
                    setLocalPart(local) {
                    }
                    /**
                     *
                     * @return {string}
                     */
                    getNamespaceURI() {
                        return this.namespace;
                    }
                    /**
                     *
                     * @param {string} namespaceURI
                     */
                    setNamespaceURI(namespaceURI) {
                    }
                    /**
                     *
                     * @return {string}
                     */
                    getPrefix() {
                        return this.prefix;
                    }
                    /**
                     *
                     * @param {string} prefix
                     */
                    setPrefix(prefix) {
                    }
                    /**
                     *
                     * @param {*} objectToTest
                     * @return {boolean}
                     */
                    equals(objectToTest) {
                        if (objectToTest === this) {
                            return true;
                        }
                        if (objectToTest != null && objectToTest instanceof org.openprovenance.prov.vanilla.QualifiedName) {
                            const qualifiedName = objectToTest;
                            return (this.local === qualifiedName.local) && (this.namespace === qualifiedName.namespace);
                        }
                        return false;
                    }
                    /**
                     *
                     * @return {number}
                     */
                    hashCode() {
                        return /* hashCode */ ((o) => { if (o.hashCode) {
                            return o.hashCode();
                        }
                        else {
                            return o.toString().split('').reduce((prevHash, currVal) => (((prevHash << 5) - prevHash) + currVal.charCodeAt(0)) | 0, 0);
                        } })(this.namespace) ^ /* hashCode */ ((o) => { if (o.hashCode) {
                            return o.hashCode();
                        }
                        else {
                            return o.toString().split('').reduce((prevHash, currVal) => (((prevHash << 5) - prevHash) + currVal.charCodeAt(0)) | 0, 0);
                        } })(this.local);
                    }
                    toString() {
                        return "\'" + this.prefix + ":{{" + this.namespace + "}}" + this.local + "\'";
                    }
                }
                vanilla.QualifiedName = QualifiedName;
                QualifiedName["__class"] = "org.openprovenance.prov.vanilla.QualifiedName";
                QualifiedName["__interfaces"] = ["org.openprovenance.prov.model.QualifiedName"];
            })(vanilla = prov.vanilla || (prov.vanilla = {}));
        })(prov = openprovenance.prov || (openprovenance.prov = {}));
    })(openprovenance = org.openprovenance || (org.openprovenance = {}));
})(org || (org = {}));
