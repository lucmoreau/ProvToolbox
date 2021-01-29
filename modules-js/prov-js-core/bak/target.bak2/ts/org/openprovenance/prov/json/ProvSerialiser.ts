/* Generated from Java with JSweet 3.0.0 - http://www.jsweet.org */
namespace org.openprovenance.prov.json {
    export class ProvSerialiser implements org.openprovenance.prov.model.ProvSerialiser {
        /*private*/ pFactory: org.openprovenance.prov.model.ProvFactory;

        public constructor(pFactory: org.openprovenance.prov.model.ProvFactory) {
            if (this.pFactory === undefined) { this.pFactory = null; }
            this.pFactory = pFactory;
        }

        public serialiseDocument$java_io_OutputStream$org_openprovenance_prov_model_Document$boolean(out: java.io.OutputStream, document: org.openprovenance.prov.model.Document, formatted: boolean) {
            new org.openprovenance.prov.json.Converter(this.pFactory).writeDocument$org_openprovenance_prov_model_Document$java_io_OutputStream(document, out);
        }

        public serialiseDocument$org_openprovenance_prov_model_Document(document: org.openprovenance.prov.model.Document): string {
            return new org.openprovenance.prov.json.Converter(this.pFactory).getString(document);
        }

        static myMedia: Array<string>; public static myMedia_$LI$(): Array<string> { if (ProvSerialiser.myMedia == null) { ProvSerialiser.myMedia = java.util.Set.of<any>(org.openprovenance.prov.interop.InteropMediaType.MEDIA_APPLICATION_JSON); }  return ProvSerialiser.myMedia; }

        /**
         * 
         * @return {string[]}
         */
        public mediaTypes(): Array<string> {
            return ProvSerialiser.myMedia_$LI$();
        }

        public serialiseDocument$java_io_OutputStream$org_openprovenance_prov_model_Document$java_lang_String$boolean(out: java.io.OutputStream, document: org.openprovenance.prov.model.Document, mediaType: string, formatted: boolean) {
            this.serialiseDocument$java_io_OutputStream$org_openprovenance_prov_model_Document$boolean(out, document, formatted);
        }

        /**
         * 
         * @param {java.io.OutputStream} out
         * @param {*} document
         * @param {string} mediaType
         * @param {boolean} formatted
         */
        public serialiseDocument(out?: any, document?: any, mediaType?: any, formatted?: any) {
            if (((out != null && out instanceof <any>java.io.OutputStream) || out === null) && ((document != null && (document.constructor != null && document.constructor["__interfaces"] != null && document.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Document") >= 0)) || document === null) && ((typeof mediaType === 'string') || mediaType === null) && ((typeof formatted === 'boolean') || formatted === null)) {
                return <any>this.serialiseDocument$java_io_OutputStream$org_openprovenance_prov_model_Document$java_lang_String$boolean(out, document, mediaType, formatted);
            } else if (((out != null && out instanceof <any>java.io.OutputStream) || out === null) && ((document != null && (document.constructor != null && document.constructor["__interfaces"] != null && document.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Document") >= 0)) || document === null) && ((typeof mediaType === 'boolean') || mediaType === null) && formatted === undefined) {
                return <any>this.serialiseDocument$java_io_OutputStream$org_openprovenance_prov_model_Document$boolean(out, document, mediaType);
            } else if (((out != null && (out.constructor != null && out.constructor["__interfaces"] != null && out.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Document") >= 0)) || out === null) && document === undefined && mediaType === undefined && formatted === undefined) {
                return <any>this.serialiseDocument$org_openprovenance_prov_model_Document(out);
            } else throw new Error('invalid overload');
        }
    }
    ProvSerialiser["__class"] = "org.openprovenance.prov.json.ProvSerialiser";
    ProvSerialiser["__interfaces"] = ["org.openprovenance.prov.model.ProvSerialiser"];


}

