/* Generated from Java with JSweet 3.0.0 - http://www.jsweet.org */
var com;
(function (com) {
    var google;
    (function (google) {
        var gson;
        (function (gson) {
            class Gson {
                constructor(serializers, deserializers) {
                    if (this.serializers === undefined) {
                        this.serializers = null;
                    }
                    if (this.deserializers === undefined) {
                        this.deserializers = null;
                    }
                    this.serializers = serializers;
                    this.deserializers = deserializers;
                }
                toJson$java_lang_Object(src) {
                    return this.toJson$com_google_gson_JsonElement(this.serialize$java_lang_Object(src));
                }
                toJson$com_google_gson_JsonElement(src) {
                    return src.toString();
                }
                fromJson$java_lang_String$java_lang_Class(json, classOfT) {
                    return (this.fromJson$com_google_gson_JsonElement$java_lang_Class(new com.google.gson.JsonElement(JSON.parse(json)), classOfT));
                }
                fromJson(json, classOfT) {
                    if (((typeof json === 'string') || json === null) && ((classOfT != null && (classOfT["__class"] != null || ((t) => { try {
                        new t;
                        return true;
                    }
                    catch (_a) {
                        return false;
                    } })(classOfT))) || classOfT === null)) {
                        return this.fromJson$java_lang_String$java_lang_Class(json, classOfT);
                    }
                    else if (((json != null && json instanceof com.google.gson.JsonElement) || json === null) && ((classOfT != null && (classOfT["__class"] != null || ((t) => { try {
                        new t;
                        return true;
                    }
                    catch (_a) {
                        return false;
                    } })(classOfT))) || classOfT === null)) {
                        return this.fromJson$com_google_gson_JsonElement$java_lang_Class(json, classOfT);
                    }
                    else if (((json != null) || json === null) && ((classOfT != null && (classOfT["__class"] != null || ((t) => { try {
                        new t;
                        return true;
                    }
                    catch (_a) {
                        return false;
                    } })(classOfT))) || classOfT === null)) {
                        return this.fromJson$java_lang_Object$java_lang_Class(json, classOfT);
                    }
                    else
                        throw new Error('invalid overload');
                }
                fromJson$com_google_gson_JsonElement$java_lang_Class(json, classOfT) {
                    return (this.deserialize(json, classOfT));
                }
                serialize$java_lang_Object(src) {
                    let clazz = src.constructor;
                    if ( /* get */((m, k) => { if (m.entries == null)
                        m.entries = []; for (let i = 0; i < m.entries.length; i++)
                        if (m.entries[i].key == null && k == null || m.entries[i].key.equals != null && m.entries[i].key.equals(k) || m.entries[i].key === k) {
                            return m.entries[i].value;
                        } return null; })(this.serializers, clazz) == null) {
                        if (src != null && (src instanceof Object)) {
                            clazz = "java.util.Map";
                        }
                        if (src != null && (src instanceof Array)) {
                            clazz = "java.lang.Iterable";
                        }
                    }
                    return this.serialize$java_lang_Object$java_lang_Class(src, clazz);
                }
                serialize$java_lang_Object$java_lang_Class(src, typeOfSrc) {
                    const jsonSerializer = ((m, k) => { if (m.entries == null)
                        m.entries = []; for (let i = 0; i < m.entries.length; i++)
                        if (m.entries[i].key == null && k == null || m.entries[i].key.equals != null && m.entries[i].key.equals(k) || m.entries[i].key === k) {
                            return m.entries[i].value;
                        } return null; })(this.serializers, typeOfSrc);
                    if (jsonSerializer != null) {
                        return jsonSerializer.serialize((src), (typeOfSrc), this);
                    }
                    if (src != null && src instanceof com.google.gson.JsonElement) {
                        return src;
                    }
                    console.log("HERE 1 " + typeOfSrc)

                    if ("java.util.Map" === typeOfSrc || Object === typeOfSrc) {
                        const l = src;
                        const obj = new com.google.gson.JsonObject();
                        console.log("foobar1");
                        console.log(src);
                        console.log("foobar2" + l.entries);
                        if (l.entries) {
                            /* entrySet */ ((m) => { if (m.entries == null)
                            m.entries = []; return m.entries; })(l).forEach(((obj) => {
                            return (kv) => {
                                console.log("foobar3")

                                obj.add(kv.getKey().toString(), this.serialize$java_lang_Object(kv.getValue()));
                            };
                        })(obj));
                            } else {
                            Object.keys(src).forEach(k => {
                                if (k !== "entries") {
                                    obj.add(k, this.serialize$java_lang_Object(src[k]))
                                }
                            })
                        }

                        console.log("foobar4");
                            console.log(obj);
                        return obj;
                    }
                    console.log("HERE 2 " + typeOfSrc)
                    console.log("HERE 2 " + src)
                    if ("java.lang.Iterable" === typeOfSrc) {
                        const l = src;
                        const array = new com.google.gson.JsonArray();
                        for (let index242 = 0; index242 < l.length; index242++) {
                            let o = l[index242];
                            {
                                console.log(" HERE 2b " + o)
                                array.add$com_google_gson_JsonElement(this.serialize$java_lang_Object(o));
                            }
                        }
                        console.log(" foobar 5");
                        console.log(array);

                        return array;
                    }
                    return new com.google.gson.JsonElement(src);
                }
                /**
                 *
                 * @param {*} src
                 * @param {*} typeOfSrc
                 * @return {com.google.gson.JsonElement}
                 */
                serialize(src, typeOfSrc) {
                    if (((src != null) || src === null) && ((typeOfSrc != null && (typeOfSrc["__class"] != null || ((t) => { try {
                        new t;
                        return true;
                    }
                    catch (_a) {
                        return false;
                    } })(typeOfSrc))) || typeOfSrc === null)) {
                        return this.serialize$java_lang_Object$java_lang_Class(src, typeOfSrc);
                    }
                    else if (((src != null) || src === null) && typeOfSrc === undefined) {
                        return this.serialize$java_lang_Object(src);
                    }
                    else
                        throw new Error('invalid overload');
                }
                /*private*/ deserialize_default(je) {
                    if (je.isJsonArray())
                        return (this.deserialize(je, "java.util.List"));
                    if (je.isJsonObject())
                        return (this.deserialize(je, "java.util.Map"));
                    if (je.isJsonNull())
                        return (this.deserialize(je, Object));
                    if (je.isJsonPrimitive()) {
                        const jp = je.getAsJsonPrimitive();
                        if (jp.isBoolean())
                            return (this.deserialize(je, Boolean));
                        if (jp.isNumber())
                            return (this.deserialize(je, Number));
                        if (jp.isString())
                            return (this.deserialize(je, String));
                    }
                    throw Object.defineProperty(new Error("Unrecognizable json element"), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.IllegalStateException', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.Exception'] });
                }
                /**
                 *
                 * @param {com.google.gson.JsonElement} je
                 * @param {*} classOfT
                 * @return {*}
                 */
                deserialize(je, classOfT) {
                    const jsonDeserializer = ((m, k) => { if (m.entries == null)
                        m.entries = []; for (let i = 0; i < m.entries.length; i++)
                        if (m.entries[i].key == null && k == null || m.entries[i].key.equals != null && m.entries[i].key.equals(k) || m.entries[i].key === k) {
                            return m.entries[i].value;
                        } return null; })(this.deserializers, classOfT);
                    if (jsonDeserializer != null)
                        return jsonDeserializer.deserialize(je, (classOfT), this);
                    if (Object === classOfT)
                        return je.o;
                    if (String === classOfT)
                        return je.o.toString();
                    if (com.google.gson.JsonElement === classOfT) {
                        return je;
                    }
                    if (com.google.gson.JsonArray === classOfT) {
                        return je.getAsJsonArray();
                    }
                    if (com.google.gson.JsonNull === classOfT) {
                        if (je.isJsonNull())
                            return com.google.gson.JsonNull.INSTANCE_$LI$();
                        throw Object.defineProperty(new Error("Not a JSON null: " + je.toString()), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.IllegalStateException', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.Exception'] });
                    }
                    if (com.google.gson.JsonObject === classOfT) {
                        return je.getAsJsonObject();
                    }
                    if (com.google.gson.JsonPrimitive === classOfT) {
                        return je.getAsJsonPrimitive();
                    }
                    if (Number === classOfT || Number === classOfT || Number === classOfT || Number === classOfT || Number === classOfT)
                        return je.getAsJsonPrimitive().getAsDouble();
                    if (Boolean === classOfT)
                        return je.getAsJsonPrimitive().getAsBoolean();
                    if ("java.util.List" === classOfT || "java.util.ArrayList" === classOfT) {
                        const ja = je.getAsJsonArray();
                        const res = ([]);
                        for (let i = 0; i < ja.size(); ++i) { /* add */
                            (res.push(this.deserialize_default(ja.get(i))) > 0);
                        }
                        return res;
                    }
                    if ("java.util.Map" === classOfT || Object === classOfT) {
                        const ja = je.getAsJsonObject();
                        const res = ({});
                        {
                            let array244 = ja.keySet();
                            for (let index243 = 0; index243 < array244.length; index243++) {
                                let key = array244[index243];
                                /* put */ (res[key] = this.deserialize_default(ja.get(key)));
                            }
                        }
                        return res;
                    }
                    return je.o;
                }
                fromJson$java_lang_Object$java_lang_Class(buf, class1) {
                    return null;
                }
                toJson$org_openprovenance_prov_model_Document$java_lang_Object(doc, writer) {
                }
                toJson(doc, writer) {
                    if (((doc != null && (doc.constructor != null && doc.constructor["__interfaces"] != null && doc.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Document") >= 0)) || doc === null) && ((writer != null) || writer === null)) {
                        return this.toJson$org_openprovenance_prov_model_Document$java_lang_Object(doc, writer);
                    }
                    else if (((doc != null && doc instanceof com.google.gson.JsonElement) || doc === null) && writer === undefined) {
                        return this.toJson$com_google_gson_JsonElement(doc);
                    }
                    else if (((doc != null) || doc === null) && writer === undefined) {
                        return this.toJson$java_lang_Object(doc);
                    }
                    else
                        throw new Error('invalid overload');
                }
            }
            gson.Gson = Gson;
            Gson["__class"] = "com.google.gson.Gson";
            Gson["__interfaces"] = ["com.google.gson.JsonSerializationContext", "com.google.gson.JsonDeserializationContext"];
        })(gson = google.gson || (google.gson = {}));
    })(google = com.google || (com.google = {}));
})(com || (com = {}));
