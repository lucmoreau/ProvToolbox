/* Generated from Java with JSweet 3.0.0 - http://www.jsweet.org */
var org;
(function (org) {
    var openprovenance;
    (function (openprovenance) {
        var prov;
        (function (prov) {
            var json;
            (function (json) {
                class ProvDeserialiser {
                    constructor(pFactory) {
                        if (this.pFactory === undefined) {
                            this.pFactory = null;
                        }
                        this.pFactory = pFactory;
                    }
                    /**
                     * Deerializes a document from a stream
                     *
                     * @param {{ str: string, cursor: number }} in an {@link InputStream}
                     * @return {*} org.openprovenance.prov.model.Document
                     */
                    deserialiseDocument(__in) {
                        return new org.openprovenance.prov.json.Converter(this.pFactory).readDocument$java_io_InputStream(__in);
                    }
                }
                json.ProvDeserialiser = ProvDeserialiser;
                ProvDeserialiser["__class"] = "org.openprovenance.prov.json.ProvDeserialiser";
                ProvDeserialiser["__interfaces"] = ["org.openprovenance.prov.model.ProvDeserialiser"];
            })(json = prov.json || (prov.json = {}));
        })(prov = openprovenance.prov || (openprovenance.prov = {}));
    })(openprovenance = org.openprovenance || (org.openprovenance = {}));
})(org || (org = {}));
