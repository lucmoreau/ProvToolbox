/* Generated from Java with JSweet 3.0.0 - http://www.jsweet.org */
var org;
(function (org) {
    var openprovenance;
    (function (openprovenance) {
        var apache;
        (function (apache) {
            var commons;
            (function (commons) {
                var lang;
                (function (lang) {
                    class StringEscapeUtils {
                        static escapeJavaScript(format) {
                            return format;
                        }
                        static escapeCsv(format) {
                            return "\"" + format + "\"";
                        }
                    }
                    lang.StringEscapeUtils = StringEscapeUtils;
                    StringEscapeUtils["__class"] = "org.openprovenance.apache.commons.lang.StringEscapeUtils";
                })(lang = commons.lang || (commons.lang = {}));
            })(commons = apache.commons || (apache.commons = {}));
        })(apache = openprovenance.apache || (openprovenance.apache = {}));
    })(openprovenance = org.openprovenance || (org.openprovenance = {}));
})(org || (org = {}));
