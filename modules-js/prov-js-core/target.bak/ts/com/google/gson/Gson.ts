/* Generated from Java with JSweet 3.0.0 - http://www.jsweet.org */
namespace com.google.gson {
    export class Gson implements com.google.gson.JsonSerializationContext, com.google.gson.JsonDeserializationContext {
        /*private*/ serializers: any;

        /*private*/ deserializers: any;

        constructor(serializers: any, deserializers: any) {
            if (this.serializers === undefined) { this.serializers = null; }
            if (this.deserializers === undefined) { this.deserializers = null; }
            this.serializers = serializers;
            this.deserializers = deserializers;
        }

        public toJson$java_lang_Object(src: any): string {
            return this.toJson$com_google_gson_JsonElement(this.serialize$java_lang_Object(src));
        }

        public toJson$com_google_gson_JsonElement(src: com.google.gson.JsonElement): string {
            return src.toString();
        }

        public fromJson$java_lang_String$java_lang_Class<T>(json: string, classOfT: any): T {
            return <any>(this.fromJson$com_google_gson_JsonElement$java_lang_Class(new com.google.gson.JsonElement(JSON.parse(json)), classOfT));
        }

        public fromJson<T>(json?: any, classOfT?: any): any {
            if (((typeof json === 'string') || json === null) && ((classOfT != null && (classOfT["__class"] != null || ((t) => { try { new t; return true; } catch { return false; } })(classOfT))) || classOfT === null)) {
                return <any>this.fromJson$java_lang_String$java_lang_Class(json, classOfT);
            } else if (((json != null && json instanceof <any>com.google.gson.JsonElement) || json === null) && ((classOfT != null && (classOfT["__class"] != null || ((t) => { try { new t; return true; } catch { return false; } })(classOfT))) || classOfT === null)) {
                return <any>this.fromJson$com_google_gson_JsonElement$java_lang_Class(json, classOfT);
            } else if (((json != null) || json === null) && ((classOfT != null && (classOfT["__class"] != null || ((t) => { try { new t; return true; } catch { return false; } })(classOfT))) || classOfT === null)) {
                return <any>this.fromJson$java_lang_Object$java_lang_Class(json, classOfT);
            } else throw new Error('invalid overload');
        }

        public fromJson$com_google_gson_JsonElement$java_lang_Class<T>(json: com.google.gson.JsonElement, classOfT: any): T {
            return <any>(this.deserialize<any>(json, classOfT));
        }

        public serialize$java_lang_Object(src: any): com.google.gson.JsonElement {
            let clazz: any = (<any>src.constructor);
            if (/* get */((m,k) => { if(m.entries==null) m.entries=[]; for(let i=0;i<m.entries.length;i++) if(m.entries[i].key==null && k==null || m.entries[i].key.equals!=null && m.entries[i].key.equals(k) || m.entries[i].key===k) { return m.entries[i].value; } return null; })(<any>this.serializers, clazz) == null){
                if (src != null && (src instanceof Object)){
                    clazz = "java.util.Map";
                }
                if (src != null && (src instanceof Array)){
                    clazz = "java.lang.Iterable";
                }
            }
            return this.serialize$java_lang_Object$java_lang_Class(src, clazz);
        }

        public serialize$java_lang_Object$java_lang_Class(src: any, typeOfSrc: any): com.google.gson.JsonElement {
            const jsonSerializer: com.google.gson.JsonSerializer<any> = /* get */((m,k) => { if(m.entries==null) m.entries=[]; for(let i=0;i<m.entries.length;i++) if(m.entries[i].key==null && k==null || m.entries[i].key.equals!=null && m.entries[i].key.equals(k) || m.entries[i].key===k) { return m.entries[i].value; } return null; })(<any>this.serializers, typeOfSrc);
            if (jsonSerializer != null){
                return jsonSerializer.serialize(<any>((<any>(src))), <any>((<any>(typeOfSrc))), this);
            }
            if (src != null && src instanceof <any>com.google.gson.JsonElement){
                return <com.google.gson.JsonElement>src;
            }
            if ("java.util.Map" === typeOfSrc || Object === typeOfSrc){
                const l: any = <any>((<any>src));
                const obj: com.google.gson.JsonObject = new com.google.gson.JsonObject();
                /* entrySet */((m) => { if(m.entries==null) m.entries=[]; return m.entries; })(<any>l).forEach(((obj) => {
                    return (kv) => {
                        obj.add(kv.getKey().toString(), this.serialize$java_lang_Object(kv.getValue()));
                    }
                })(obj));
                return obj;
            }
            if ("java.lang.Iterable" === typeOfSrc){
                const l: Array<any> = <any>((<any>src));
                const array: com.google.gson.JsonArray = new com.google.gson.JsonArray();
                for(let index242=0; index242 < l.length; index242++) {
                    let o = l[index242];
                    {
                        array.add$com_google_gson_JsonElement(this.serialize$java_lang_Object(o));
                    }
                }
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
        public serialize(src?: any, typeOfSrc?: any): any {
            if (((src != null) || src === null) && ((typeOfSrc != null && (typeOfSrc["__class"] != null || ((t) => { try { new t; return true; } catch { return false; } })(typeOfSrc))) || typeOfSrc === null)) {
                return <any>this.serialize$java_lang_Object$java_lang_Class(src, typeOfSrc);
            } else if (((src != null) || src === null) && typeOfSrc === undefined) {
                return <any>this.serialize$java_lang_Object(src);
            } else throw new Error('invalid overload');
        }

        /*private*/ deserialize_default(je: com.google.gson.JsonElement): any {
            if (je.isJsonArray())return <any>(this.deserialize<any>(je, "java.util.List"));
            if (je.isJsonObject())return <any>(this.deserialize<any>(je, "java.util.Map"));
            if (je.isJsonNull())return <any>(this.deserialize<any>(je, Object));
            if (je.isJsonPrimitive()){
                const jp: com.google.gson.JsonPrimitive = je.getAsJsonPrimitive();
                if (jp.isBoolean())return <any>(this.deserialize<any>(je, Boolean));
                if (jp.isNumber())return <any>(this.deserialize<any>(je, Number));
                if (jp.isString())return <any>(this.deserialize<any>(je, String));
            }
            throw Object.defineProperty(new Error("Unrecognizable json element"), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.IllegalStateException','java.lang.Object','java.lang.RuntimeException','java.lang.Exception'] });
        }

        /**
         * 
         * @param {com.google.gson.JsonElement} je
         * @param {*} classOfT
         * @return {*}
         */
        public deserialize<T>(je: com.google.gson.JsonElement, classOfT: any): T {
            const jsonDeserializer: com.google.gson.JsonDeserializer<any> = /* get */((m,k) => { if(m.entries==null) m.entries=[]; for(let i=0;i<m.entries.length;i++) if(m.entries[i].key==null && k==null || m.entries[i].key.equals!=null && m.entries[i].key.equals(k) || m.entries[i].key===k) { return m.entries[i].value; } return null; })(<any>this.deserializers, classOfT);
            if (jsonDeserializer != null)return <any>((<any>jsonDeserializer.deserialize(je, <any>((<any>(classOfT))), this)));
            if (Object === classOfT)return <any>((<any>je.o));
            if (String === classOfT)return <any>((<any>je.o.toString()));
            if (com.google.gson.JsonElement === classOfT){
                return <any>((<any>je));
            }
            if (com.google.gson.JsonArray === classOfT){
                return <any>((<any>je.getAsJsonArray()));
            }
            if (com.google.gson.JsonNull === classOfT){
                if (je.isJsonNull())return <any>((<any>com.google.gson.JsonNull.INSTANCE_$LI$()));
                throw Object.defineProperty(new Error("Not a JSON null: " + je.toString()), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.IllegalStateException','java.lang.Object','java.lang.RuntimeException','java.lang.Exception'] });
            }
            if (com.google.gson.JsonObject === classOfT){
                return <any>((<any>je.getAsJsonObject()));
            }
            if (com.google.gson.JsonPrimitive === classOfT){
                return <any>((<any>je.getAsJsonPrimitive()));
            }
            if (Number === classOfT || Number === classOfT || Number === classOfT || Number === classOfT || Number === classOfT)return <any>((<any>je.getAsJsonPrimitive().getAsDouble()));
            if (Boolean === classOfT)return <any>((<any>je.getAsJsonPrimitive().getAsBoolean()));
            if ("java.util.List" === classOfT || "java.util.ArrayList" === classOfT){
                const ja: com.google.gson.JsonArray = je.getAsJsonArray();
                const res: Array<any> = <any>([]);
                for(let i: number = 0; i < ja.size(); ++i) {/* add */(res.push(this.deserialize_default(ja.get(i)))>0);}
                return <any>((<any>res));
            }
            if ("java.util.Map" === classOfT || Object === classOfT){
                const ja: com.google.gson.JsonObject = je.getAsJsonObject();
                const res: any = <any>({});
                {
                    let array244 = ja.keySet();
                    for(let index243=0; index243 < array244.length; index243++) {
                        let key = array244[index243];
                        /* put */(res[key] = this.deserialize_default(ja.get(key)))
                    }
                }
                return <any>((<any>res));
            }
            return <any>((<any>je.o));
        }

        public fromJson$java_lang_Object$java_lang_Class(buf: any, class1: any): any {
            return null;
        }

        public toJson$org_openprovenance_prov_model_Document$java_lang_Object(doc: org.openprovenance.prov.model.Document, writer: any) {
        }

        public toJson(doc?: any, writer?: any) {
            if (((doc != null && (doc.constructor != null && doc.constructor["__interfaces"] != null && doc.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Document") >= 0)) || doc === null) && ((writer != null) || writer === null)) {
                return <any>this.toJson$org_openprovenance_prov_model_Document$java_lang_Object(doc, writer);
            } else if (((doc != null && doc instanceof <any>com.google.gson.JsonElement) || doc === null) && writer === undefined) {
                return <any>this.toJson$com_google_gson_JsonElement(doc);
            } else if (((doc != null) || doc === null) && writer === undefined) {
                return <any>this.toJson$java_lang_Object(doc);
            } else throw new Error('invalid overload');
        }
    }
    Gson["__class"] = "com.google.gson.Gson";
    Gson["__interfaces"] = ["com.google.gson.JsonSerializationContext","com.google.gson.JsonDeserializationContext"];


}

