/* Generated from Java with JSweet 3.0.0 - http://www.jsweet.org */
var org;
(function (org) {
    var openprovenance;
    (function (openprovenance) {
        var prov;
        (function (prov) {
            var json;
            (function (json) {
                class ProvDocumentSerializer {
                    constructor(pFactory) {
                        if (this.pFactory === undefined) {
                            this.pFactory = null;
                        }
                        this.pFactory = pFactory;
                    }
                    serialize$org_openprovenance_prov_model_Document$java_lang_reflect_Type$com_google_gson_JsonSerializationContext(doc, typeOfSrc, context) {
                        const jsonConstructor = new org.openprovenance.prov.json.JSONConstructor(this.pFactory.getName());
                        const bt = new org.openprovenance.prov.model.BeanTraversal(jsonConstructor, this.pFactory);
                        bt.doAction$org_openprovenance_prov_model_Document(doc);
                        const jsonStructure = jsonConstructor.getJSONStructure$();
                        return context['serialize$java_lang_Object'](jsonStructure);
                    }
                    /**
                     *
                     * @param {*} doc
                     * @param {*} typeOfSrc
                     * @param {*} context
                     * @return {com.google.gson.JsonElement}
                     */
                    serialize(doc, typeOfSrc, context) {
                        if (((doc != null && (doc.constructor != null && doc.constructor["__interfaces"] != null && doc.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Document") >= 0)) || doc === null)
                            //   && ((typeOfSrc != null && (typeOfSrc.constructor != null && typeOfSrc.constructor["__interfaces"] != null && typeOfSrc.constructor["__interfaces"].indexOf("java.lang.reflect.Type") >= 0)) || typeOfSrc === null)
                            && ((context != null && (context.constructor != null && context.constructor["__interfaces"] != null && context.constructor["__interfaces"].indexOf("com.google.gson.JsonSerializationContext") >= 0)) || context === null)) {


                            return this.serialize$org_openprovenance_prov_model_Document$java_lang_reflect_Type$com_google_gson_JsonSerializationContext(doc, typeOfSrc, context);
                        }
                        else {
                            console.log("doc.constructor")
                            console.log(doc.constructor["__interfaces"] );
                            console.log(doc.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Document") >= 0);
                            console.log("typeofsrc.constructor")
                            console.log(typeOfSrc);
                            console.log(typeOfSrc.constructor);
                            console.log(typeOfSrc.constructor["__interfaces"] );
                            console.log("typeofsrc.constructor")
                            console.log("context")
                            throw new Error('invalid overload');
                        }
                    }
                }
                json.ProvDocumentSerializer = ProvDocumentSerializer;
                ProvDocumentSerializer["__class"] = "org.openprovenance.prov.json.ProvDocumentSerializer";
                ProvDocumentSerializer["__interfaces"] = ["com.google.gson.JsonSerializer"];
            })(json = prov.json || (prov.json = {}));
        })(prov = openprovenance.prov || (openprovenance.prov = {}));
    })(openprovenance = org.openprovenance || (org.openprovenance = {}));
})(org || (org = {}));
