/* Generated from Java with JSweet 3.0.0 - http://www.jsweet.org */
var org;
(function (org) {
    var openprovenance;
    (function (openprovenance) {
        var prov;
        (function (prov) {
            var json;
            (function (json) {
                class Converter {
                    constructor(pFactory) {
                        if (this.pFactory === undefined) {
                            this.pFactory = null;
                        }
                        if (this.gson === undefined) {
                            this.gson = null;
                        }
                        if (this.class1 === undefined) {
                            this.class1 = null;
                        }
                        this.pFactory = pFactory;
                        this.class1 = pFactory.newDocument$().constructor;
                        this.gson = new com.google.gson.GsonBuilder().registerTypeAdapter(this.class1, new org.openprovenance.prov.json.ProvDocumentDeserializer(pFactory)).registerTypeAdapter(this.class1, new org.openprovenance.prov.json.ProvDocumentSerializer(pFactory)).setPrettyPrinting().create();
                    }
                    toJsonElement(doc) {
                        return this.gson.serialize$java_lang_Object(doc);
                    }
                    readDocument$java_lang_String(file) {
                        const buf = new java.io.FileReader(file);
                        const doc = this.gson.fromJson$java_lang_Object$java_lang_Class(buf, this.class1);
                        /* close */ ;
                        return doc;
                    }
                    readDocument(file) {
                        if (((typeof file === 'string') || file === null)) {
                            return this.readDocument$java_lang_String(file);
                        }
                        else if (((file != null && (file instanceof Object)) || file === null)) {
                            return this.readDocument$java_io_InputStream(file);
                        }
                        else
                            throw new Error('invalid overload');
                    }
                    readDocument$java_io_InputStream(is) {
                        const buf = is;
                        const doc = this.gson.fromJson$java_lang_Object$java_lang_Class(buf, this.class1);
                        /* close */ ;
                        return doc;
                    }
                    writeDocument$org_openprovenance_prov_model_Document$java_lang_String(doc, file) {
                        const writer = new java.io.BufferedWriter(new java.io.FileWriter(file));
                        this.gson.toJson$org_openprovenance_prov_model_Document$java_lang_Object(doc, writer);
                        writer.close();
                    }
                    writeDocument(doc, file) {
                        if (((doc != null && (doc.constructor != null && doc.constructor["__interfaces"] != null && doc.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Document") >= 0)) || doc === null) && ((typeof file === 'string') || file === null)) {
                            return this.writeDocument$org_openprovenance_prov_model_Document$java_lang_String(doc, file);
                        }
                        else if (((doc != null && (doc.constructor != null && doc.constructor["__interfaces"] != null && doc.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Document") >= 0)) || doc === null) && ((file != null && file instanceof java.io.Writer) || file === null)) {
                            return this.writeDocument$org_openprovenance_prov_model_Document$java_io_Writer(doc, file);
                        }
                        else if (((doc != null && (doc.constructor != null && doc.constructor["__interfaces"] != null && doc.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Document") >= 0)) || doc === null) && ((file != null && file instanceof java.io.OutputStream) || file === null)) {
                            return this.writeDocument$org_openprovenance_prov_model_Document$java_io_OutputStream(doc, file);
                        }
                        else
                            throw new Error('invalid overload');
                    }
                    writeDocument$org_openprovenance_prov_model_Document$java_io_Writer(doc, out) {
                        const writer = new java.io.BufferedWriter(out);
                        this.gson.toJson$org_openprovenance_prov_model_Document$java_lang_Object(doc, writer);
                        writer.close();
                    }
                    writeDocument$org_openprovenance_prov_model_Document$java_io_OutputStream(doc, out) {
                        const writer = new java.io.BufferedWriter(new java.io.OutputStreamWriter(out));
                        this.gson.toJson$org_openprovenance_prov_model_Document$java_lang_Object(doc, writer);
                        try {
                            writer.close();
                        }
                        catch (e) {
                            throw new org.openprovenance.prov.model.exception.UncheckedException(e);
                        }
                    }
                    getString(doc) {
                        return this.gson.toJson$java_lang_Object(doc);
                    }
                    fromString(jsonStr) {
                        return (this.gson.fromJson$java_lang_String$java_lang_Class(jsonStr, this.class1));
                    }
                }
                json.Converter = Converter;
                Converter["__class"] = "org.openprovenance.prov.json.Converter";
            })(json = prov.json || (prov.json = {}));
        })(prov = openprovenance.prov || (openprovenance.prov = {}));
    })(openprovenance = org.openprovenance || (org.openprovenance = {}));
})(org || (org = {}));
