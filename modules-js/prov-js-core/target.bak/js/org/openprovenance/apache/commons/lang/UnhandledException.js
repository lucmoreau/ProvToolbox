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
                     * Constructs the exception using a message and cause.
                     *
                     * @param {string} message  the message to use
                     * @param {Error} cause  the underlying cause
                     * @class
                     * @extends org.openprovenance.apache.commons.lang.exception.NestableRuntimeException
                     * @author Matthew Hawthorne
                     */
                    class UnhandledException extends org.openprovenance.apache.commons.lang.exception.NestableRuntimeException {
                        constructor(message, cause) {
                            if (((typeof message === 'string') || message === null) && ((cause != null && (cause["__classes"] && cause["__classes"].indexOf("java.lang.Throwable") >= 0) || cause != null && cause instanceof Error) || cause === null)) {
                                let __args = arguments;
                                super(message, cause);
                            }
                            else if (((message != null && (message["__classes"] && message["__classes"].indexOf("java.lang.Throwable") >= 0) || message != null && message instanceof Error) || message === null) && cause === undefined) {
                                let __args = arguments;
                                let cause = __args[0];
                                super(cause);
                            }
                            else
                                throw new Error('invalid overload');
                        }
                    }
                    /**
                     * Required for serialization support.
                     *
                     * @see java.io.Serializable
                     */
                    UnhandledException.__org_openprovenance_apache_commons_lang_UnhandledException_serialVersionUID = 1832101364842773720;
                    lang.UnhandledException = UnhandledException;
                    UnhandledException["__class"] = "org.openprovenance.apache.commons.lang.UnhandledException";
                    UnhandledException["__interfaces"] = ["org.openprovenance.apache.commons.lang.exception.Nestable", "java.io.Serializable"];
                })(lang = commons.lang || (commons.lang = {}));
            })(commons = apache.commons || (apache.commons = {}));
        })(apache = openprovenance.apache || (openprovenance.apache = {}));
    })(openprovenance = org.openprovenance || (org.openprovenance = {}));
})(org || (org = {}));
