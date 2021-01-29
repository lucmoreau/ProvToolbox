/* Generated from Java with JSweet 3.0.0 - http://www.jsweet.org */
var com;
(function (com) {
    var google;
    (function (google) {
        var gson;
        (function (gson) {
            class JsonElement {
                constructor(o) {
                    if (this.o === undefined) {
                        this.o = null;
                    }
                    this.o = o;
                }
                isJsonArray() {
                    return Array.isArray(this.o);
                }
                isJsonObject() {
                   // console.log(typeof this.o)
                    return !this.isJsonNull() && ("object" === typeof this.o);
                }
                isJsonPrimitive() {
                    switch ((typeof this.o)) {
                        case "boolean":
                        case "number":
                        case "string":
                            return true;
                    }
                    return false;
                }
                isJsonNull() {
                    return this.o === null;
                }
                getAsJsonObject() {
                    //console.log(this)
                    if (this.isJsonObject()) {
                        return (this != null && this instanceof com.google.gson.JsonObject) ? this : new com.google.gson.JsonObject(this.o);
                    }
                    else {
                        throw Object.defineProperty(new Error("Not a JSON Object: " + this), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.IllegalStateException', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.Exception'] });
                    }
                }
                getAsJsonArray() {
                    if (this.isJsonArray()) {
                        return (this != null && this instanceof com.google.gson.JsonArray) ? this : new com.google.gson.JsonArray(this.o);
                    }
                    else {
                        throw Object.defineProperty(new Error("Not a JSON Array: " + this), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.IllegalStateException', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.Exception'] });
                    }
                }
                getAsJsonPrimitive() {
                    if (this.isJsonPrimitive()) {
                        if (this != null && this instanceof com.google.gson.JsonPrimitive)
                            return this;
                        switch ((typeof this.o)) {
                            case "boolean":
                                return new com.google.gson.JsonPrimitive(this.o);
                            case "number":
                                return new com.google.gson.JsonPrimitive(this.o);
                            case "string":
                                return new com.google.gson.JsonPrimitive(this.o);
                        }
                        throw Object.defineProperty(new Error("Not a JSON Primitive: " + this), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.IllegalStateException', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.Exception'] });
                    }
                    else {
                        throw Object.defineProperty(new Error("Not a JSON Primitive: " + this), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.IllegalStateException', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.Exception'] });
                    }
                }
                getAsJsonNull() {
                    if (this.isJsonNull()) {
                        return this;
                    }
                    else {
                        throw Object.defineProperty(new Error("Not a JSON Null: " + this), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.IllegalStateException', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.Exception'] });
                    }
                }
                toString() {
                    return JSON.stringify(this.o);
                }
            }
            gson.JsonElement = JsonElement;
            JsonElement["__class"] = "com.google.gson.JsonElement";
        })(gson = google.gson || (google.gson = {}));
    })(google = com.google || (com.google = {}));
})(com || (com = {}));
