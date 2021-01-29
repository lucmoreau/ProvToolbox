/* Generated from Java with JSweet 3.0.0 - http://www.jsweet.org */
namespace org.openprovenance.prov.model {
    /**
     * A low-level interface for  serialization of documents.
     * @class
     */
    export interface ProvDeserialiser {
        /**
         * Deerializes a document from a stream
         * @param {{ str: string, cursor: number }} in an {@link InputStream}
         * @return {*} org.openprovenance.prov.model.Document
         */
        deserialiseDocument(__in: { str: string, cursor: number }): org.openprovenance.prov.model.Document;
    }
}

