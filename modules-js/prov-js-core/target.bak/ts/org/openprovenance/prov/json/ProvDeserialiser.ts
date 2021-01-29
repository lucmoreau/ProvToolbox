/* Generated from Java with JSweet 3.0.0 - http://www.jsweet.org */
namespace org.openprovenance.prov.json {
    export class ProvDeserialiser implements org.openprovenance.prov.model.ProvDeserialiser {
        /*private*/ pFactory: org.openprovenance.prov.model.ProvFactory;

        public constructor(pFactory: org.openprovenance.prov.model.ProvFactory) {
            if (this.pFactory === undefined) { this.pFactory = null; }
            this.pFactory = pFactory;
        }

        public deserialiseDocument$java_io_InputStream(__in: { str: string, cursor: number }): org.openprovenance.prov.model.Document {
            return new org.openprovenance.prov.json.Converter(this.pFactory).readDocument$java_io_InputStream(__in);
        }

        /**
         * Deerializes a document from a stream
         * 
         * @param {{ str: string, cursor: number }} in an {@link InputStream}
         * @return {*} org.openprovenance.prov.model.Document
         */
        public deserialiseDocument(__in?: any): any {
            if (((__in != null && (__in instanceof Object)) || __in === null)) {
                return <any>this.deserialiseDocument$java_io_InputStream(__in);
            } else if (((typeof __in === 'string') || __in === null)) {
                return <any>this.deserialiseDocument$java_lang_String(__in);
            } else throw new Error('invalid overload');
        }

        public deserialiseDocument$java_lang_String(json: string): org.openprovenance.prov.model.Document {
            return new org.openprovenance.prov.json.Converter(this.pFactory).fromString(json);
        }
    }
    ProvDeserialiser["__class"] = "org.openprovenance.prov.json.ProvDeserialiser";
    ProvDeserialiser["__interfaces"] = ["org.openprovenance.prov.model.ProvDeserialiser"];


}

