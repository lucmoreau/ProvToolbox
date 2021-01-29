/* Generated from Java with JSweet 3.0.0 - http://www.jsweet.org */
namespace org.openprovenance.prov.json {
    export class ProvDocumentSerializer implements com.google.gson.JsonSerializer<org.openprovenance.prov.model.Document> {
        pFactory: org.openprovenance.prov.model.ProvFactory;

        public constructor(pFactory: org.openprovenance.prov.model.ProvFactory) {
            if (this.pFactory === undefined) { this.pFactory = null; }
            this.pFactory = pFactory;
        }

        public serialize$org_openprovenance_prov_model_Document$java_lang_reflect_Type$com_google_gson_JsonSerializationContext(doc: org.openprovenance.prov.model.Document, typeOfSrc: java.lang.reflect.Type, context: com.google.gson.JsonSerializationContext): com.google.gson.JsonElement {
            const jsonConstructor: org.openprovenance.prov.json.JSONConstructor = new org.openprovenance.prov.json.JSONConstructor(this.pFactory.getName());
            const bt: org.openprovenance.prov.model.BeanTraversal = new org.openprovenance.prov.model.BeanTraversal(jsonConstructor, this.pFactory);
            bt.doAction$org_openprovenance_prov_model_Document(doc);
            const jsonStructure: any = jsonConstructor.getJSONStructure$();
            return context['serialize$java_lang_Object'](jsonStructure);
        }

        /**
         * 
         * @param {*} doc
         * @param {*} typeOfSrc
         * @param {*} context
         * @return {com.google.gson.JsonElement}
         */
        public serialize(doc?: any, typeOfSrc?: any, context?: any): any {
            if (((doc != null && (doc.constructor != null && doc.constructor["__interfaces"] != null && doc.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Document") >= 0)) || doc === null) && ((typeOfSrc != null && (typeOfSrc.constructor != null && typeOfSrc.constructor["__interfaces"] != null && typeOfSrc.constructor["__interfaces"].indexOf("java.lang.reflect.Type") >= 0)) || typeOfSrc === null) && ((context != null && (context.constructor != null && context.constructor["__interfaces"] != null && context.constructor["__interfaces"].indexOf("com.google.gson.JsonSerializationContext") >= 0)) || context === null)) {
                return <any>this.serialize$org_openprovenance_prov_model_Document$java_lang_reflect_Type$com_google_gson_JsonSerializationContext(doc, typeOfSrc, context);
            } else throw new Error('invalid overload');
        }
    }
    ProvDocumentSerializer["__class"] = "org.openprovenance.prov.json.ProvDocumentSerializer";
    ProvDocumentSerializer["__interfaces"] = ["com.google.gson.JsonSerializer"];


}

