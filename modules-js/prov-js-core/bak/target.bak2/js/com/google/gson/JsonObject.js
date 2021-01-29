/* Generated from Java with JSweet 3.0.0 - http://www.jsweet.org */
var com;
(function (com) {
    var google;
    (function (google) {
        var gson;
        (function (gson) {
            class JsonObject extends com.google.gson.JsonElement {
                constructor(o) {
                    if (((o != null && o instanceof Object) || o === null)) {
                        let __args = arguments;
                        super(o);
                    }
                    else if (o === undefined) {
                        let __args = arguments;
                        {
                            let __args = arguments;
                            let o = new Object();
                            super(o);
                        }
                    }
                    else
                        throw new Error('invalid overload');
                }
                /*private*/ asObject() {
                    return this.o;
                }
                add(property, value) {
                    if (value == null) {
                        value = com.google.gson.JsonNull.INSTANCE_$LI$();
                    }
                    this.asObject()[property] = value.o;
                }
                remove(property) {
                    const jsonElement = this.get(property);
                    delete this.asObject()[property];
                    return jsonElement;
                }
                addProperty$java_lang_String$java_lang_String(property, value) {
                    this.asObject()[property] = (value);
                }
                addProperty(property, value) {
                    if (((typeof property === 'string') || property === null) && ((typeof value === 'string') || value === null)) {
                        return this.addProperty$java_lang_String$java_lang_String(property, value);
                    }
                    else if (((typeof property === 'string') || property === null) && ((typeof value === 'number') || value === null)) {
                        return this.addProperty$java_lang_String$java_lang_Number(property, value);
                    }
                    else if (((typeof property === 'string') || property === null) && ((typeof value === 'boolean') || value === null)) {
                        return this.addProperty$java_lang_String$java_lang_Boolean(property, value);
                    }
                    else
                        throw new Error('invalid overload');
                }
                addProperty$java_lang_String$java_lang_Number(property, value) {
                    this.asObject()[property] = (value);
                }
                addProperty$java_lang_String$java_lang_Boolean(property, value) {
                    this.asObject()[property] = (value);
                }
                keySet() {
                    return ( /* asList */(Object.keys(this.o)).slice(0).slice(0));
                }
                size() {
                    return /* size */ this.keySet().length;
                }
                has(memberName) {
                    return this.asObject()[memberName] === undefined;
                }
                get(memberName) {
                    return new com.google.gson.JsonElement((this.asObject()[memberName]));
                }
                getAsJsonPrimitive$java_lang_String(memberName) {
                    return this.get(memberName).getAsJsonPrimitive();
                }
                getAsJsonPrimitive(memberName) {
                    if (((typeof memberName === 'string') || memberName === null)) {
                        return this.getAsJsonPrimitive$java_lang_String(memberName);
                    }
                    else if (memberName === undefined) {
                        return super.getAsJsonPrimitive();
                    }
                    else
                        throw new Error('invalid overload');
                }
                getAsJsonArray$java_lang_String(memberName) {
                    return this.get(memberName).getAsJsonArray();
                }
                getAsJsonArray(memberName) {
                    if (((typeof memberName === 'string') || memberName === null)) {
                        return this.getAsJsonArray$java_lang_String(memberName);
                    }
                    else if (memberName === undefined) {
                        return super.getAsJsonArray();
                    }
                    else
                        throw new Error('invalid overload');
                }
                getAsJsonObject$java_lang_String(memberName) {
                    return this.get(memberName).getAsJsonObject();
                }
                getAsJsonObject(memberName) {
                    console.log("HERE")
                    if (((typeof memberName === 'string') || memberName === null)) {
                        return this.getAsJsonObject$java_lang_String(memberName);
                    }
                    else if (memberName === undefined) {
                        return super.getAsJsonObject();
                    }
                    else
                        throw new Error('invalid overload');
                }
            }
            gson.JsonObject = JsonObject;
            JsonObject["__class"] = "com.google.gson.JsonObject";
        })(gson = google.gson || (google.gson = {}));
    })(google = com.google || (com.google = {}));
})(com || (com = {}));
