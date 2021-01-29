/* Generated from Java with JSweet 3.0.0 - http://www.jsweet.org */
namespace org.openprovenance.prov.json {
    export class Converter {
        /*private*/ pFactory: org.openprovenance.prov.model.ProvFactory;

        /*private*/ gson: com.google.gson.Gson;

        class1: any;

        public constructor(pFactory: org.openprovenance.prov.model.ProvFactory) {
            if (this.pFactory === undefined) { this.pFactory = null; }
            if (this.gson === undefined) { this.gson = null; }
            if (this.class1 === undefined) { this.class1 = null; }
            this.pFactory = pFactory;
            this.class1 = (<any>pFactory.newDocument$().constructor);
            this.gson = new com.google.gson.GsonBuilder().registerTypeAdapter(this.class1, new org.openprovenance.prov.json.ProvDocumentDeserializer(pFactory)).registerTypeAdapter(this.class1, new org.openprovenance.prov.json.ProvDocumentSerializer(pFactory)).setPrettyPrinting().create();
        }

        public toJsonElement(doc: org.openprovenance.prov.model.Document): com.google.gson.JsonElement {
            return this.gson.serialize$java_lang_Object(doc);
        }

        public readDocument$java_lang_String(file: string): org.openprovenance.prov.model.Document {
            const buf: { str: string, cursor: number } = new java.io.FileReader(file);
            const doc: org.openprovenance.prov.model.Document = <org.openprovenance.prov.model.Document><any>this.gson.fromJson$java_lang_Object$java_lang_Class(buf, this.class1);
            /* close */;
            return doc;
        }

        public readDocument(file?: any): any {
            if (((typeof file === 'string') || file === null)) {
                return <any>this.readDocument$java_lang_String(file);
            } else if (((file != null && (file instanceof Object)) || file === null)) {
                return <any>this.readDocument$java_io_InputStream(file);
            } else throw new Error('invalid overload');
        }

        public readDocument$java_io_InputStream(is: { str: string, cursor: number }): org.openprovenance.prov.model.Document {
            const buf: { str: string, cursor: number } = is;
            const doc: org.openprovenance.prov.model.Document = <org.openprovenance.prov.model.Document><any>this.gson.fromJson$java_lang_Object$java_lang_Class(buf, this.class1);
            /* close */;
            return doc;
        }

        public writeDocument$org_openprovenance_prov_model_Document$java_lang_String(doc: org.openprovenance.prov.model.Document, file: string) {
            const writer: java.io.BufferedWriter = new java.io.BufferedWriter(new java.io.FileWriter(file));
            this.gson.toJson$org_openprovenance_prov_model_Document$java_lang_Object(doc, writer);
            writer.close();
        }

        public writeDocument(doc?: any, file?: any) {
            if (((doc != null && (doc.constructor != null && doc.constructor["__interfaces"] != null && doc.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Document") >= 0)) || doc === null) && ((typeof file === 'string') || file === null)) {
                return <any>this.writeDocument$org_openprovenance_prov_model_Document$java_lang_String(doc, file);
            } else if (((doc != null && (doc.constructor != null && doc.constructor["__interfaces"] != null && doc.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Document") >= 0)) || doc === null) && ((file != null && file instanceof <any>java.io.Writer) || file === null)) {
                return <any>this.writeDocument$org_openprovenance_prov_model_Document$java_io_Writer(doc, file);
            } else if (((doc != null && (doc.constructor != null && doc.constructor["__interfaces"] != null && doc.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Document") >= 0)) || doc === null) && ((file != null && file instanceof <any>java.io.OutputStream) || file === null)) {
                return <any>this.writeDocument$org_openprovenance_prov_model_Document$java_io_OutputStream(doc, file);
            } else throw new Error('invalid overload');
        }

        public writeDocument$org_openprovenance_prov_model_Document$java_io_Writer(doc: org.openprovenance.prov.model.Document, out: java.io.Writer) {
            const writer: java.io.BufferedWriter = new java.io.BufferedWriter(out);
            this.gson.toJson$org_openprovenance_prov_model_Document$java_lang_Object(doc, writer);
            writer.close();
        }

        public writeDocument$org_openprovenance_prov_model_Document$java_io_OutputStream(doc: org.openprovenance.prov.model.Document, out: java.io.OutputStream) {
            const writer: java.io.BufferedWriter = new java.io.BufferedWriter(new java.io.OutputStreamWriter(out));
            this.gson.toJson$org_openprovenance_prov_model_Document$java_lang_Object(doc, writer);
            try {
                writer.close();
            } catch(e) {
                throw new org.openprovenance.prov.model.exception.UncheckedException(e);
            }
        }

        public getString(doc: org.openprovenance.prov.model.Document): string {
            return this.gson.toJson$java_lang_Object(doc);
        }

        public fromString(jsonStr: string): org.openprovenance.prov.model.Document {
            return <any>(this.gson.fromJson$java_lang_String$java_lang_Class(jsonStr, this.class1));
        }
    }
    Converter["__class"] = "org.openprovenance.prov.json.Converter";

}

