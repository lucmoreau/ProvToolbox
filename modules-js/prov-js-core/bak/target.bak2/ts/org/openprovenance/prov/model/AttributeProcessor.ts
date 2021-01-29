/* Generated from Java with JSweet 3.0.0 - http://www.jsweet.org */
namespace org.openprovenance.prov.model {
    /**
     * Helper class to process attributes of a Statement.
     * 
     * @author lavm
     * @param {*[]} attributes
     * @class
     */
    export class AttributeProcessor {
        /*private*/ namespaceIndex: any;

        /*private*/ attributes: Array<org.openprovenance.prov.model.Other>;

        public constructor(attributes: Array<org.openprovenance.prov.model.Other>) {
            this.namespaceIndex = <any>({});
            if (this.attributes === undefined) { this.attributes = null; }
            this.attributes = attributes;
        }

        public attributesWithNamespace(namespace: string): any {
            let result: any = /* get */((m,k) => m[k]===undefined?null:m[k])(this.namespaceIndex, namespace);
            if (result == null){
                result = <any>({});
                /* put */(this.namespaceIndex[namespace] = result);
            }
            for(let index201=0; index201 < this.attributes.length; index201++) {
                let attribute = this.attributes[index201];
                {
                    const name: org.openprovenance.prov.model.QualifiedName = attribute.getElementName();
                    if (namespace === name.getNamespaceURI()){
                        const ll: Array<org.openprovenance.prov.model.Other> = /* get */((m,k) => m[k]===undefined?null:m[k])(result, name.getLocalPart());
                        if (ll == null){
                            const tmp: Array<org.openprovenance.prov.model.Other> = <any>([]);
                            /* add */(tmp.push(attribute)>0);
                            /* put */(result[name.getLocalPart()] = tmp);
                        } else {
                            /* add */(ll.push(attribute)>0);
                        }
                    }
                }
            }
            return result;
        }
    }
    AttributeProcessor["__class"] = "org.openprovenance.prov.model.AttributeProcessor";

}

