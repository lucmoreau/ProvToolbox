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
                         * Constructs a new <code>NestableRuntimeException</code> with specified
                         * detail message and nested <code>Throwable</code>.
                         *
                         * @param {string} msg    the error message
                         * @param {Error} cause  the exception or error that caused this exception to be
                         * thrown
                         * @class
                         * @extends Error
                         * @author Daniel L. Rall
                         */
                        class NestableRuntimeException extends Error {
                            constructor(msg, cause) {
                                if (((typeof msg === 'string') || msg === null) && ((cause != null && (cause["__classes"] && cause["__classes"].indexOf("java.lang.Throwable") >= 0) || cause != null && cause instanceof Error) || cause === null)) {
                                    let __args = arguments;
                                    super(msg);
                                    this.message = msg;
                                    this.delegate = new org.openprovenance.apache.commons.lang.exception.NestableDelegate(this);
                                    this.__org_openprovenance_apache_commons_lang_exception_NestableRuntimeException_cause = null;
                                    this.__org_openprovenance_apache_commons_lang_exception_NestableRuntimeException_cause = cause;
                                }
                                else if (((typeof msg === 'string') || msg === null) && cause === undefined) {
                                    let __args = arguments;
                                    super(msg);
                                    this.message = msg;
                                    this.delegate = new org.openprovenance.apache.commons.lang.exception.NestableDelegate(this);
                                    this.__org_openprovenance_apache_commons_lang_exception_NestableRuntimeException_cause = null;
                                }
                                else if (((msg != null && (msg["__classes"] && msg["__classes"].indexOf("java.lang.Throwable") >= 0) || msg != null && msg instanceof Error) || msg === null) && cause === undefined) {
                                    let __args = arguments;
                                    let cause = __args[0];
                                    super();
                                    this.delegate = new org.openprovenance.apache.commons.lang.exception.NestableDelegate(this);
                                    this.__org_openprovenance_apache_commons_lang_exception_NestableRuntimeException_cause = null;
                                    this.__org_openprovenance_apache_commons_lang_exception_NestableRuntimeException_cause = cause;
                                }
                                else if (msg === undefined && cause === undefined) {
                                    let __args = arguments;
                                    super();
                                    this.delegate = new org.openprovenance.apache.commons.lang.exception.NestableDelegate(this);
                                    this.__org_openprovenance_apache_commons_lang_exception_NestableRuntimeException_cause = null;
                                }
                                else
                                    throw new Error('invalid overload');
                            }
                            /**
                             * {@inheritDoc}
                             * @return {Error}
                             */
                            getCause() {
                                return this.__org_openprovenance_apache_commons_lang_exception_NestableRuntimeException_cause;
                            }
                            getMessage$() {
                                if (this.message != null) {
                                    return this.message;
                                }
                                else if (this.__org_openprovenance_apache_commons_lang_exception_NestableRuntimeException_cause != null) {
                                    return this.__org_openprovenance_apache_commons_lang_exception_NestableRuntimeException_cause.toString();
                                }
                                else {
                                    return null;
                                }
                            }
                            getMessage$int(index) {
                                if (index === 0) {
                                    return this.message;
                                }
                                return this.delegate.getMessage$int(index);
                            }
                            /**
                             * {@inheritDoc}
                             * @param {number} index
                             * @return {string}
                             */
                            getMessage(index) {
                                if (((typeof index === 'number') || index === null)) {
                                    return this.getMessage$int(index);
                                }
                                else if (index === undefined) {
                                    return this.getMessage$();
                                }
                                else
                                    throw new Error('invalid overload');
                            }
                            /**
                             * {@inheritDoc}
                             * @return {java.lang.String[]}
                             */
                            getMessages() {
                                return this.delegate.getMessages();
                            }
                            /**
                             * {@inheritDoc}
                             * @param {number} index
                             * @return {Error}
                             */
                            getThrowable(index) {
                                return this.delegate.getThrowable(index);
                            }
                            /**
                             * {@inheritDoc}
                             * @return {number}
                             */
                            getThrowableCount() {
                                return this.delegate.getThrowableCount();
                            }
                            /**
                             * {@inheritDoc}
                             * @return {java.lang.Throwable[]}
                             */
                            getThrowables() {
                                return this.delegate.getThrowables();
                            }
                            indexOfThrowable$java_lang_Class(type) {
                                return this.delegate.indexOfThrowable(type, 0);
                            }
                            indexOfThrowable$java_lang_Class$int(type, fromIndex) {
                                return this.delegate.indexOfThrowable(type, fromIndex);
                            }
                            /**
                             * {@inheritDoc}
                             * @param {*} type
                             * @param {number} fromIndex
                             * @return {number}
                             */
                            indexOfThrowable(type, fromIndex) {
                                if (((type != null && (type["__class"] != null || ((t) => { try {
                                    new t;
                                    return true;
                                }
                                catch (_a) {
                                    return false;
                                } })(type))) || type === null) && ((typeof fromIndex === 'number') || fromIndex === null)) {
                                    return this.indexOfThrowable$java_lang_Class$int(type, fromIndex);
                                }
                                else if (((type != null && (type["__class"] != null || ((t) => { try {
                                    new t;
                                    return true;
                                }
                                catch (_a) {
                                    return false;
                                } })(type))) || type === null) && fromIndex === undefined) {
                                    return this.indexOfThrowable$java_lang_Class(type);
                                }
                                else
                                    throw new Error('invalid overload');
                            }
                            printStackTrace$() {
                                this.delegate.printStackTrace$();
                            }
                            printStackTrace$java_io_PrintStream(out) {
                                this.delegate.printStackTrace$java_io_PrintStream(out);
                            }
                            /**
                             * {@inheritDoc}
                             * @param {java.io.PrintStream} out
                             */
                            printStackTrace(out) {
                                if (((out != null && out instanceof java.io.PrintStream) || out === null)) {
                                    return this.printStackTrace$java_io_PrintStream(out);
                                }
                                else if (((out != null && out instanceof java.io.PrintWriter) || out === null)) {
                                    return this.printStackTrace$java_io_PrintWriter(out);
                                }
                                else if (out === undefined) {
                                    return this.printStackTrace$();
                                }
                                else
                                    throw new Error('invalid overload');
                            }
                            printStackTrace$java_io_PrintWriter(out) {
                                this.delegate.printStackTrace$java_io_PrintWriter(out);
                            }
                            /**
                             * {@inheritDoc}
                             * @param {java.io.PrintWriter} out
                             */
                            printPartialStackTrace(out) {
                                console.error(super.message, super.);
                            }
                        }
                        /**
                         * Required for serialization support.
                         *
                         * @see java.io.Serializable
                         */
                        NestableRuntimeException.serialVersionUID = 1;
                        exception.NestableRuntimeException = NestableRuntimeException;
                        NestableRuntimeException["__class"] = "org.openprovenance.apache.commons.lang.exception.NestableRuntimeException";
                        NestableRuntimeException["__interfaces"] = ["org.openprovenance.apache.commons.lang.exception.Nestable", "java.io.Serializable"];
                    })(exception = lang.exception || (lang.exception = {}));
                })(lang = commons.lang || (commons.lang = {}));
            })(commons = apache.commons || (apache.commons = {}));
        })(apache = openprovenance.apache || (openprovenance.apache = {}));
    })(openprovenance = org.openprovenance || (org.openprovenance = {}));
})(org || (org = {}));
