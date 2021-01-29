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
                 * Helper class to process attributes of a Statement.
                 *
                 * @author lavm
                 * @param {*[]} attributes
                 * @class
                 */
                class AttributeProcessor {
                    constructor(attributes) {
                        this.namespaceIndex = ({});
                        if (this.attributes === undefined) {
                            this.attributes = null;
                        }
                        this.attributes = attributes;
                    }
                    attributesWithNamespace(namespace) {
                        let result = ((m, k) => m[k] === undefined ? null : m[k])(this.namespaceIndex, namespace);
                        if (result == null) {
                            result = ({});
                            /* put */ (this.namespaceIndex[namespace] = result);
                        }
                        for (let index201 = 0; index201 < this.attributes.length; index201++) {
                            let attribute = this.attributes[index201];
                            {
                                const name = attribute.getElementName();
                                if (namespace === name.getNamespaceURI()) {
                                    const ll = ((m, k) => m[k] === undefined ? null : m[k])(result, name.getLocalPart());
                                    if (ll == null) {
                                        const tmp = ([]);
                                        /* add */ (tmp.push(attribute) > 0);
                                        /* put */ (result[name.getLocalPart()] = tmp);
                                    }
                                    else {
                                        /* add */ (ll.push(attribute) > 0);
                                    }
                                }
                            }
                        }
                        return result;
                    }
                }
                model.AttributeProcessor = AttributeProcessor;
                AttributeProcessor["__class"] = "org.openprovenance.prov.model.AttributeProcessor";
            })(model = prov.model || (prov.model = {}));
        })(prov = openprovenance.prov || (openprovenance.prov = {}));
    })(openprovenance = org.openprovenance || (org.openprovenance = {}));
})(org || (org = {}));
