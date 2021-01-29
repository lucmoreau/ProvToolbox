/* Generated from Java with JSweet 3.0.0 - http://www.jsweet.org */
var org;
(function (org) {
    var openprovenance;
    (function (openprovenance) {
        var prov;
        (function (prov) {
            var json;
            (function (json_1) {
                class ProvDeserialiser {
                    constructor(pFactory) {
                        if (this.pFactory === undefined) {
                            this.pFactory = null;
                        }
                        this.pFactory = pFactory;
                    }
                    deserialiseDocument$java_io_InputStream(__in) {
                        return new org.openprovenance.prov.json.Converter(this.pFactory).readDocument$java_io_InputStream(__in);
                    }
                    /**
                     * Deerializes a document from a stream
                     *
                     * @param {{ str: string, cursor: number }} in an {@link InputStream}
                     * @return {*} org.openprovenance.prov.model.Document
                     */
                    deserialiseDocument(__in) {
                        if (((__in != null && (__in instanceof Object)) || __in === null)) {
                            return this.deserialiseDocument$java_io_InputStream(__in);
                        }
                        else if (((typeof __in === 'string') || __in === null)) {
                            return this.deserialiseDocument$java_lang_String(__in);
                        }
                        else
                            throw new Error('invalid overload');
                    }
                    deserialiseDocument$java_lang_String(json) {
                        return new org.openprovenance.prov.json.Converter(this.pFactory).fromString(json);
                    }
                }
                json_1.ProvDeserialiser = ProvDeserialiser;
                ProvDeserialiser["__class"] = "org.openprovenance.prov.json.ProvDeserialiser";
                ProvDeserialiser["__interfaces"] = ["org.openprovenance.prov.model.ProvDeserialiser"];
            })(json = prov.json || (prov.json = {}));
        })(prov = openprovenance.prov || (openprovenance.prov = {}));
    })(openprovenance = org.openprovenance || (org.openprovenance = {}));
})(org || (org = {}));
