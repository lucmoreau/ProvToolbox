/* Generated from Java with JSweet 3.0.0 - http://www.jsweet.org */
var org;
(function (org) {
    var openprovenance;
    (function (openprovenance) {
        var prov;
        (function (prov) {
            var model;
            (function (model) {
                var exception;
                (function (exception) {
                    /**
                     * @param {string} message a message
                     * @param {Error} cause the exception
                     * @class
                     * @extends Error
                     * @author lavm
                     */
                    class InvalidCaseException extends Error {
                        constructor(message, cause) {
                            if (((typeof message === 'string') || message === null) && ((cause != null && (cause["__classes"] && cause["__classes"].indexOf("java.lang.Throwable") >= 0) || cause != null && cause instanceof Error) || cause === null)) {
                                let __args = arguments;
                                super(message);
                                this.message = message;
                            }
                            else if (((typeof message === 'string') || message === null) && cause === undefined) {
                                let __args = arguments;
                                super(message);
                                this.message = message;
                            }
                            else if (((message != null && (message["__classes"] && message["__classes"].indexOf("java.lang.Throwable") >= 0) || message != null && message instanceof Error) || message === null) && cause === undefined) {
                                let __args = arguments;
                                let cause = __args[0];
                                super(cause);
                                this.message = cause;
                            }
                            else if (message === undefined && cause === undefined) {
                                let __args = arguments;
                                super();
                            }
                            else
                                throw new Error('invalid overload');
                        }
                    }
                    /**
                     *
                     */
                    InvalidCaseException.serialVersionUID = -2443740480287709699;
                    exception.InvalidCaseException = InvalidCaseException;
                    InvalidCaseException["__class"] = "org.openprovenance.prov.model.exception.InvalidCaseException";
                    InvalidCaseException["__interfaces"] = ["java.io.Serializable"];
                })(exception = model.exception || (model.exception = {}));
            })(model = prov.model || (prov.model = {}));
        })(prov = openprovenance.prov || (openprovenance.prov = {}));
    })(openprovenance = org.openprovenance || (org.openprovenance = {}));
})(org || (org = {}));
