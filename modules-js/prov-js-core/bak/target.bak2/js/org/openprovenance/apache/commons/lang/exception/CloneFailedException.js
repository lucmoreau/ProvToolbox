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
                    var exception;
                    (function (exception) {
                        /**
                         * Constructs a CloneFailedException.
                         *
                         * @param {string} message description of the exception
                         * @param {Error} cause cause of the exception
                         * @since upcoming
                         * @class
                         * @extends org.openprovenance.apache.commons.lang.exception.NestableRuntimeException
                         * @author Apache Software Foundation
                         */
                        class CloneFailedException extends org.openprovenance.apache.commons.lang.exception.NestableRuntimeException {
                            constructor(message, cause) {
                                if (((typeof message === 'string') || message === null) && ((cause != null && (cause["__classes"] && cause["__classes"].indexOf("java.lang.Throwable") >= 0) || cause != null && cause instanceof Error) || cause === null)) {
                                    let __args = arguments;
                                    super(message, cause);
                                }
                                else if (((typeof message === 'string') || message === null) && cause === undefined) {
                                    let __args = arguments;
                                    super(message);
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
                        CloneFailedException.__org_openprovenance_apache_commons_lang_exception_CloneFailedException_serialVersionUID = 20091223;
                        exception.CloneFailedException = CloneFailedException;
                        CloneFailedException["__class"] = "org.openprovenance.apache.commons.lang.exception.CloneFailedException";
                        CloneFailedException["__interfaces"] = ["org.openprovenance.apache.commons.lang.exception.Nestable", "java.io.Serializable"];
                    })(exception = lang.exception || (lang.exception = {}));
                })(lang = commons.lang || (commons.lang = {}));
            })(commons = apache.commons || (apache.commons = {}));
        })(apache = openprovenance.apache || (openprovenance.apache = {}));
    })(openprovenance = org.openprovenance || (org.openprovenance = {}));
})(org || (org = {}));
