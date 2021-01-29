/* Generated from Java with JSweet 3.0.0 - http://www.jsweet.org */
var com;
(function (com) {
    var google;
    (function (google) {
        var gson;
        (function (gson) {
            class JsonArray extends com.google.gson.JsonElement {
                constructor(array) {
                    if (((array != null && array instanceof Array) || array === null)) {
                        let __args = arguments;
                        super(array);
                    }
                    else if (((typeof array === 'number') || array === null)) {
                        let __args = arguments;
                        let capacity = __args[0];
                        {
                            let __args = arguments;
                            let array = new Array(capacity);
                            super(array);
                        }
                    }
                    else if (array === undefined) {
                        let __args = arguments;
                        {
                            let __args = arguments;
                            let array = new Array();
                            super(array);
                        }
                    }
                    else
                        throw new Error('invalid overload');
                }
                /*private*/ asArray() {
                    return this.o;
                }
                add$java_lang_Boolean(boolVal) {
                    this.asArray().push((boolVal));
                }
                add(boolVal) {
                    if (((typeof boolVal === 'boolean') || boolVal === null)) {
                        return this.add$java_lang_Boolean(boolVal);
                    }
                    else if (((typeof boolVal === 'number') || boolVal === null)) {
                        return this.add$java_lang_Number(boolVal);
                    }
                    else if (((typeof boolVal === 'string') || boolVal === null)) {
                        return this.add$java_lang_String(boolVal);
                    }
                    else if (((boolVal != null && boolVal instanceof com.google.gson.JsonElement) || boolVal === null)) {
                        return this.add$com_google_gson_JsonElement(boolVal);
                    }
                    else
                        throw new Error('invalid overload');
                }
                add$java_lang_Number(numberVal) {
                    this.asArray().push((numberVal));
                }
                add$java_lang_String(stringVal) {
                    this.asArray().push((stringVal));
                }
                add$com_google_gson_JsonElement(element) {
                    if (element == null) {
                        element = com.google.gson.JsonNull.INSTANCE_$LI$();
                    }
                    this.asArray().push(element.o);
                }
                set(index, element) {
                    const othO = this.asArray()[index];
                    this.asArray()[index] = element.o;
                    return new com.google.gson.JsonElement(othO);
                }
                remove$com_google_gson_JsonElement(element) {
                    for (let i = 0; i < this.asArray().length; ++i) {
                        {
                            if (this.asArray()[i] === element.o) {
                                this.remove$int(i);
                                return true;
                            }
                        }
                        ;
                    }
                    return false;
                }
                remove(element) {
                    if (((element != null && element instanceof com.google.gson.JsonElement) || element === null)) {
                        return this.remove$com_google_gson_JsonElement(element);
                    }
                    else if (((typeof element === 'number') || element === null)) {
                        return this.remove$int(element);
                    }
                    else
                        throw new Error('invalid overload');
                }
                remove$int(index) {
                    const splice = this.asArray().splice(index, 1);
                    return new com.google.gson.JsonElement(splice[0]);
                }
                contains(element) {
                    for (let i = 0; i < this.asArray().length; ++i) {
                        {
                            if (this.asArray()[i] === element.o) {
                                return true;
                            }
                        }
                        ;
                    }
                    return false;
                }
                size() {
                    return this.asArray().length;
                }
                iterator() {
                    return new JsonArray.JsonArray$0(this);
                }
                get(i) {
                    return new com.google.gson.JsonElement(this.asArray()[i]);
                }
            }
            gson.JsonArray = JsonArray;
            JsonArray["__class"] = "com.google.gson.JsonArray";
            JsonArray["__interfaces"] = ["java.lang.Iterable"];
            (function (JsonArray) {
                class JsonArray$0 {
                    constructor(__parent) {
                        this.__parent = __parent;
                        this.a = this.__parent.asArray();
                        if (this.pos === undefined) {
                            this.pos = 0;
                        }
                    }
                    /**
                     *
                     * @return {boolean}
                     */
                    hasNext() {
                        return this.pos < this.a.length;
                    }
                    /**
                     *
                     * @return {com.google.gson.JsonElement}
                     */
                    next() {
                        return new com.google.gson.JsonElement(this.a[this.pos++]);
                    }
                    /**
                     *
                     */
                    remove() {
                        this.a.splice(--this.pos, 1);
                    }
                }
                JsonArray.JsonArray$0 = JsonArray$0;
                JsonArray$0["__interfaces"] = ["java.util.Iterator"];
            })(JsonArray = gson.JsonArray || (gson.JsonArray = {}));
        })(gson = google.gson || (google.gson = {}));
    })(google = com.google || (com.google = {}));
})(com || (com = {}));
