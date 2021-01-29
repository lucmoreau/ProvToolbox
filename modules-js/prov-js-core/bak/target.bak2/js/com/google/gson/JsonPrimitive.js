/* Generated from Java with JSweet 3.0.0 - http://www.jsweet.org */
var com;
(function (com) {
    var google;
    (function (google) {
        var gson;
        (function (gson) {
            class JsonPrimitive extends com.google.gson.JsonElement {
                constructor(value) {
                    if (((typeof value === 'boolean') || value === null)) {
                        let __args = arguments;
                        super((value));
                    }
                    else if (((typeof value === 'number') || value === null)) {
                        let __args = arguments;
                        super((value));
                    }
                    else if (((typeof value === 'string') || value === null)) {
                        let __args = arguments;
                        super((value));
                    }
                    else
                        throw new Error('invalid overload');
                }
                setValue(primitive) {
                    const obj = primitive;
                    new com.google.gson.JsonElement(obj).getAsJsonPrimitive();
                    this.o = obj;
                }
                isBoolean() {
                    return "boolean" === typeof this.o;
                }
                getAsBoolean() {
                    return this.isBoolean() ? this.o : javaemul.internal.BooleanHelper.parseBoolean(this.getAsString());
                }
                isNumber() {
                    return "number" === typeof this.o;
                }
                getAsNumber() {
                    return this.isNumber() ? this.o : this.getAsDouble();
                }
                isString() {
                    return "string" === typeof this.o;
                }
                getAsString() {
                    return (this.o).toString();
                }
                getAsDouble() {
                    return this.isNumber() ? this.o : /* parseDouble */ parseFloat(this.getAsString());
                }
            }
            gson.JsonPrimitive = JsonPrimitive;
            JsonPrimitive["__class"] = "com.google.gson.JsonPrimitive";
        })(gson = google.gson || (google.gson = {}));
    })(google = com.google || (com.google = {}));
})(com || (com = {}));
