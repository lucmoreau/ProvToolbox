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
                     * A generic unchecked exception.
                     * @author lavm
                     * @param {string} reason
                     * @param {Error} e
                     * @class
                     * @extends Error
                     */
                    class UncheckedException extends Error {
                        constructor(reason, e) {
                            if (((typeof reason === 'string') || reason === null) && ((e != null && (e["__classes"] && e["__classes"].indexOf("java.lang.Exception") >= 0) || e != null && e instanceof Error) || e === null)) {
                                let __args = arguments;
                                super(reason);
                                this.message = reason;
                            }
                            else if (((reason != null && (reason["__classes"] && reason["__classes"].indexOf("java.lang.Exception") >= 0) || reason != null && reason instanceof Error) || reason === null) && e === undefined) {
                                let __args = arguments;
                                let e = __args[0];
                                super("Unchecked Exception");
                                this.message = "Unchecked Exception";
                            }
                            else
                                throw new Error('invalid overload');
                        }
                    }
                    /**
                     *
                     */
                    UncheckedException.serialVersionUID = 1569547113186462619;
                    exception.UncheckedException = UncheckedException;
                    UncheckedException["__class"] = "org.openprovenance.prov.model.exception.UncheckedException";
                    UncheckedException["__interfaces"] = ["java.io.Serializable"];
                })(exception = model.exception || (model.exception = {}));
            })(model = prov.model || (prov.model = {}));
        })(prov = openprovenance.prov || (openprovenance.prov = {}));
    })(openprovenance = org.openprovenance || (org.openprovenance = {}));
})(org || (org = {}));
