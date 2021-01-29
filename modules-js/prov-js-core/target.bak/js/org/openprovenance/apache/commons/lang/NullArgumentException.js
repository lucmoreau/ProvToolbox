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
                    /**
                     * <p>Instantiates with the given argument name.</p>
                     *
                     * @param {string} argName  the name of the argument that was <code>null</code>.
                     * @class
                     * @extends Error
                     * @author Apache Software Foundation
                     */
                    class NullArgumentException {
                        constructor(argName) {
                            Object.setPrototypeOf(this, NullArgumentException.prototype);
                        }
                    }
                    /**
                     * Required for serialization support.
                     *
                     * @see java.io.Serializable
                     */
                    NullArgumentException.__org_openprovenance_apache_commons_lang_NullArgumentException_serialVersionUID = 1174360235354917591;
                    lang.NullArgumentException = NullArgumentException;
                    NullArgumentException["__class"] = "org.openprovenance.apache.commons.lang.NullArgumentException";
                    NullArgumentException["__interfaces"] = ["java.io.Serializable"];
                })(lang = commons.lang || (commons.lang = {}));
            })(commons = apache.commons || (apache.commons = {}));
        })(apache = openprovenance.apache || (openprovenance.apache = {}));
    })(openprovenance = org.openprovenance || (org.openprovenance = {}));
})(org || (org = {}));
