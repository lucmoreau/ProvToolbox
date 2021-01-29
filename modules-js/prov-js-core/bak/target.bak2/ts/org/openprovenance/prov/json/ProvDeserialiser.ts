/* Generated from Java with JSweet 3.0.0 - http://www.jsweet.org */
namespace org.openprovenance.prov.json {
    export class ProvDeserialiser implements org.openprovenance.prov.model.ProvDeserialiser {
        /*private*/ pFactory: org.openprovenance.prov.model.ProvFactory;

        public constructor(pFactory: org.openprovenance.prov.model.ProvFactory) {
            if (this.pFactory === undefined) { this.pFactory = null; }
            this.pFactory = pFactory;
        }

        /**
         * Deerializes a document from a stream
         * 
         * @param {{ str: string, cursor: number }} in an {@link InputStream}
         * @return {*} org.openprovenance.prov.model.Document
         */
        public deserialiseDocument(__in: { str: string, cursor: number }): org.openprovenance.prov.model.Document {
            return new org.openprovenance.prov.json.Converter(this.pFactory).readDocument$java_io_InputStream(__in);
        }
    }
    ProvDeserialiser["__class"] = "org.openprovenance.prov.json.ProvDeserialiser";
    ProvDeserialiser["__interfaces"] = ["org.openprovenance.prov.model.ProvDeserialiser"];


}

