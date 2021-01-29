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
                    var builder;
                    (function (builder) {
                        /**
                         * Constructor for IDKey
                         * @param {*} _value The value
                         * @class
                         * @author Apache Software Foundation
                         */
                        class IDKey {
                            constructor(_value) {
                                if (this.value === undefined) {
                                    this.value = null;
                                }
                                if (this.id === undefined) {
                                    this.id = 0;
                                }
                                this.id = java.lang.System.identityHashCode(_value);
                                this.value = _value;
                            }
                            /**
                             * returns hashcode - i.e1. the system identity hashcode.
                             * @return {number} the hashcode
                             */
                            hashCode() {
                                return this.id;
                            }
                            /**
                             * checks if instances are equal
                             * @param {*} other The other object to compare to
                             * @return {boolean} if the instances are for the same object
                             */
                            equals(other) {
                                if (!(other != null && other instanceof org.openprovenance.apache.commons.lang.builder.IDKey)) {
                                    return false;
                                }
                                const idKey = other;
                                if (this.id !== idKey.id) {
                                    return false;
                                }
                                return this.value === idKey.value;
                            }
                        }
                        builder.IDKey = IDKey;
                        IDKey["__class"] = "org.openprovenance.apache.commons.lang.builder.IDKey";
                    })(builder = lang.builder || (lang.builder = {}));
                })(lang = commons.lang || (commons.lang = {}));
            })(commons = apache.commons || (apache.commons = {}));
        })(apache = openprovenance.apache || (openprovenance.apache = {}));
    })(openprovenance = org.openprovenance || (org.openprovenance = {}));
})(org || (org = {}));
