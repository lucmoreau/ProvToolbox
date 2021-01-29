/* Generated from Java with JSweet 3.0.0 - http://www.jsweet.org */
namespace org.openprovenance.prov.model {
    /**
     * A low-level interface for JAXB-compatible serialization of documents.
     * @class
     */
    export interface ProvSerialiser {
        serialiseDocument(out?: any, document?: any, mediaType?: any, formatted?: any);

        mediaTypes(): Array<string>;
    }
}

