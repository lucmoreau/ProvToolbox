/* Generated from Java with JSweet 3.0.0 - http://www.jsweet.org */
var com;
(function (com) {
    var google;
    (function (google) {
        var gson;
        (function (gson) {
            class GsonBuilder {
                constructor() {
                    this.serializers = ({});
                    this.deserializers = ({});
                }
                registerTypeAdapter(type, o) {
                    if (o != null && (o.constructor != null && o.constructor["__interfaces"] != null && o.constructor["__interfaces"].indexOf("com.google.gson.JsonSerializer") >= 0)) /* put */
                        ((m, k, v) => { if (m.entries == null)
                            m.entries = []; for (let i = 0; i < m.entries.length; i++)
                            if (m.entries[i].key == null && k == null || m.entries[i].key.equals != null && m.entries[i].key.equals(k) || m.entries[i].key === k) {
                                m.entries[i].value = v;
                                return;
                            } m.entries.push({ key: k, value: v, getKey: function () { return this.key; }, getValue: function () { return this.value; } }); })(this.serializers, type, o);
                    else if (o != null && (o.constructor != null && o.constructor["__interfaces"] != null && o.constructor["__interfaces"].indexOf("com.google.gson.JsonDeserializer") >= 0)) /* put */
                        ((m, k, v) => { if (m.entries == null)
                            m.entries = []; for (let i = 0; i < m.entries.length; i++)
                            if (m.entries[i].key == null && k == null || m.entries[i].key.equals != null && m.entries[i].key.equals(k) || m.entries[i].key === k) {
                                m.entries[i].value = v;
                                return;
                            } m.entries.push({ key: k, value: v, getKey: function () { return this.key; }, getValue: function () { return this.value; } }); })(this.deserializers, type, o);
                    else
                        throw Object.defineProperty(new Error("Unrecognized type adapter"), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.IllegalArgumentException', 'java.lang.Exception'] });
                    return this;
                }
                create() {
                    return new com.google.gson.Gson(this.serializers, this.deserializers);
                }
                setPrettyPrinting() {
                    return this;
                }
            }
            gson.GsonBuilder = GsonBuilder;
            GsonBuilder["__class"] = "com.google.gson.GsonBuilder";
        })(gson = google.gson || (google.gson = {}));
    })(google = com.google || (com.google = {}));
})(com || (com = {}));
