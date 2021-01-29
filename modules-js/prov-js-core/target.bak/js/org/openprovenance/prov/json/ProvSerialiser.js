/* Generated from Java with JSweet 3.0.0 - http://www.jsweet.org */
var org;
(function (org) {
    var openprovenance;
    (function (openprovenance) {
        var prov;
        (function (prov) {
            var json;
            (function (json) {
                class ProvSerialiser {
                    constructor(pFactory) {
                        if (this.pFactory === undefined) {
                            this.pFactory = null;
                        }
                        this.pFactory = pFactory;
                    }
                    serialiseDocument$java_io_OutputStream$org_openprovenance_prov_model_Document$boolean(out, document, formatted) {
                        new org.openprovenance.prov.json.Converter(this.pFactory).writeDocument$org_openprovenance_prov_model_Document$java_io_OutputStream(document, out);
                    }
                    serialiseDocument$org_openprovenance_prov_model_Document(document) {
                        return new org.openprovenance.prov.json.Converter(this.pFactory).getString(document);
                    }
                    static myMedia_$LI$() { if (ProvSerialiser.myMedia == null) {
                        ProvSerialiser.myMedia = java.util.Set.of(org.openprovenance.prov.interop.InteropMediaType.MEDIA_APPLICATION_JSON);
                    } return ProvSerialiser.myMedia; }
                    /**
                     *
                     * @return {string[]}
                     */
                    mediaTypes() {
                        return ProvSerialiser.myMedia_$LI$();
                    }
                    serialiseDocument$java_io_OutputStream$org_openprovenance_prov_model_Document$java_lang_String$boolean(out, document, mediaType, formatted) {
                        this.serialiseDocument$java_io_OutputStream$org_openprovenance_prov_model_Document$boolean(out, document, formatted);
                    }
                    /**
                     *
                     * @param {java.io.OutputStream} out
                     * @param {*} document
                     * @param {string} mediaType
                     * @param {boolean} formatted
                     */
                    serialiseDocument(out, document, mediaType, formatted) {
                        if (((out != null && out instanceof java.io.OutputStream) || out === null) && ((document != null && (document.constructor != null && document.constructor["__interfaces"] != null && document.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Document") >= 0)) || document === null) && ((typeof mediaType === 'string') || mediaType === null) && ((typeof formatted === 'boolean') || formatted === null)) {
                            return this.serialiseDocument$java_io_OutputStream$org_openprovenance_prov_model_Document$java_lang_String$boolean(out, document, mediaType, formatted);
                        }
                        else if (((out != null && out instanceof java.io.OutputStream) || out === null) && ((document != null && (document.constructor != null && document.constructor["__interfaces"] != null && document.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Document") >= 0)) || document === null) && ((typeof mediaType === 'boolean') || mediaType === null) && formatted === undefined) {
                            return this.serialiseDocument$java_io_OutputStream$org_openprovenance_prov_model_Document$boolean(out, document, mediaType);
                        }
                        else if (((out != null && (out.constructor != null && out.constructor["__interfaces"] != null && out.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Document") >= 0)) || out === null) && document === undefined && mediaType === undefined && formatted === undefined) {
                            return this.serialiseDocument$org_openprovenance_prov_model_Document(out);
                        }
                        else
                            throw new Error('invalid overload');
                    }
                }
                json.ProvSerialiser = ProvSerialiser;
                ProvSerialiser["__class"] = "org.openprovenance.prov.json.ProvSerialiser";
                ProvSerialiser["__interfaces"] = ["org.openprovenance.prov.model.ProvSerialiser"];
            })(json = prov.json || (prov.json = {}));
        })(prov = openprovenance.prov || (openprovenance.prov = {}));
    })(openprovenance = org.openprovenance || (org.openprovenance = {}));
})(org || (org = {}));
