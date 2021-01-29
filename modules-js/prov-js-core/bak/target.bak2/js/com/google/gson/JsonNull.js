/* Generated from Java with JSweet 3.0.0 - http://www.jsweet.org */
var com;
(function (com) {
    var google;
    (function (google) {
        var gson;
        (function (gson) {
            class JsonNull extends com.google.gson.JsonElement {
                constructor() {
                    super(null);
                }
                static INSTANCE_$LI$() { if (JsonNull.INSTANCE == null) {
                    JsonNull.INSTANCE = new JsonNull();
                } return JsonNull.INSTANCE; }
            }
            gson.JsonNull = JsonNull;
            JsonNull["__class"] = "com.google.gson.JsonNull";
        })(gson = google.gson || (google.gson = {}));
    })(google = com.google || (com.google = {}));
})(com || (com = {}));
