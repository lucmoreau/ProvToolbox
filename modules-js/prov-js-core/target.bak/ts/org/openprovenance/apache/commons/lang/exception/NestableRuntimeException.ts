/* Generated from Java with JSweet 3.0.0 - http://www.jsweet.org */
namespace org.openprovenance.apache.commons.lang.exception {
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
    export class NestableRuntimeException extends Error implements org.openprovenance.apache.commons.lang.exception.Nestable {
        /**
         * Required for serialization support.
         * 
         * @see java.io.Serializable
         */
        static serialVersionUID: number = 1;

        /**
         * The helper instance which contains much of the code which we
         * delegate to.
         */
        delegate: org.openprovenance.apache.commons.lang.exception.NestableDelegate;

        /**
         * Holds the reference to the exception or error that caused
         * this exception to be thrown.
         */
        /*private*/ __org_openprovenance_apache_commons_lang_exception_NestableRuntimeException_cause: Error;

        public constructor(msg?: any, cause?: any) {
            if (((typeof msg === 'string') || msg === null) && ((cause != null && (cause["__classes"] && cause["__classes"].indexOf("java.lang.Throwable") >= 0) || cause != null && cause instanceof <any>Error) || cause === null)) {
                let __args = arguments;
                super(msg); this.message=msg;
                this.delegate = new org.openprovenance.apache.commons.lang.exception.NestableDelegate(this);
                this.__org_openprovenance_apache_commons_lang_exception_NestableRuntimeException_cause = null;
                this.__org_openprovenance_apache_commons_lang_exception_NestableRuntimeException_cause = cause;
            } else if (((typeof msg === 'string') || msg === null) && cause === undefined) {
                let __args = arguments;
                super(msg); this.message=msg;
                this.delegate = new org.openprovenance.apache.commons.lang.exception.NestableDelegate(this);
                this.__org_openprovenance_apache_commons_lang_exception_NestableRuntimeException_cause = null;
            } else if (((msg != null && (msg["__classes"] && msg["__classes"].indexOf("java.lang.Throwable") >= 0) || msg != null && msg instanceof <any>Error) || msg === null) && cause === undefined) {
                let __args = arguments;
                let cause: any = __args[0];
                super();
                this.delegate = new org.openprovenance.apache.commons.lang.exception.NestableDelegate(this);
                this.__org_openprovenance_apache_commons_lang_exception_NestableRuntimeException_cause = null;
                this.__org_openprovenance_apache_commons_lang_exception_NestableRuntimeException_cause = cause;
            } else if (msg === undefined && cause === undefined) {
                let __args = arguments;
                super();
                this.delegate = new org.openprovenance.apache.commons.lang.exception.NestableDelegate(this);
                this.__org_openprovenance_apache_commons_lang_exception_NestableRuntimeException_cause = null;
            } else throw new Error('invalid overload');
        }

        /**
         * {@inheritDoc}
         * @return {Error}
         */
        public getCause(): Error {
            return this.__org_openprovenance_apache_commons_lang_exception_NestableRuntimeException_cause;
        }

        public getMessage$(): string {
            if (this.message != null){
                return this.message;
            } else if (this.__org_openprovenance_apache_commons_lang_exception_NestableRuntimeException_cause != null){
                return this.__org_openprovenance_apache_commons_lang_exception_NestableRuntimeException_cause.toString();
            } else {
                return null;
            }
        }

        public getMessage$int(index: number): string {
            if (index === 0){
                return this.message;
            }
            return this.delegate.getMessage$int(index);
        }

        /**
         * {@inheritDoc}
         * @param {number} index
         * @return {string}
         */
        public getMessage(index?: any): any {
            if (((typeof index === 'number') || index === null)) {
                return <any>this.getMessage$int(index);
            } else if (index === undefined) {
                return <any>this.getMessage$();
            } else throw new Error('invalid overload');
        }

        /**
         * {@inheritDoc}
         * @return {java.lang.String[]}
         */
        public getMessages(): string[] {
            return this.delegate.getMessages();
        }

        /**
         * {@inheritDoc}
         * @param {number} index
         * @return {Error}
         */
        public getThrowable(index: number): Error {
            return this.delegate.getThrowable(index);
        }

        /**
         * {@inheritDoc}
         * @return {number}
         */
        public getThrowableCount(): number {
            return this.delegate.getThrowableCount();
        }

        /**
         * {@inheritDoc}
         * @return {java.lang.Throwable[]}
         */
        public getThrowables(): Error[] {
            return this.delegate.getThrowables();
        }

        public indexOfThrowable$java_lang_Class(type: any): number {
            return this.delegate.indexOfThrowable(type, 0);
        }

        public indexOfThrowable$java_lang_Class$int(type: any, fromIndex: number): number {
            return this.delegate.indexOfThrowable(type, fromIndex);
        }

        /**
         * {@inheritDoc}
         * @param {*} type
         * @param {number} fromIndex
         * @return {number}
         */
        public indexOfThrowable(type?: any, fromIndex?: any): any {
            if (((type != null && (type["__class"] != null || ((t) => { try { new t; return true; } catch { return false; } })(type))) || type === null) && ((typeof fromIndex === 'number') || fromIndex === null)) {
                return <any>this.indexOfThrowable$java_lang_Class$int(type, fromIndex);
            } else if (((type != null && (type["__class"] != null || ((t) => { try { new t; return true; } catch { return false; } })(type))) || type === null) && fromIndex === undefined) {
                return <any>this.indexOfThrowable$java_lang_Class(type);
            } else throw new Error('invalid overload');
        }

        public printStackTrace$() {
            this.delegate.printStackTrace$();
        }

        public printStackTrace$java_io_PrintStream(out: java.io.PrintStream) {
            this.delegate.printStackTrace$java_io_PrintStream(out);
        }

        /**
         * {@inheritDoc}
         * @param {java.io.PrintStream} out
         */
        public printStackTrace(out?: any) {
            if (((out != null && out instanceof <any>java.io.PrintStream) || out === null)) {
                return <any>this.printStackTrace$java_io_PrintStream(out);
            } else if (((out != null && out instanceof <any>java.io.PrintWriter) || out === null)) {
                return <any>this.printStackTrace$java_io_PrintWriter(out);
            } else if (out === undefined) {
                return <any>this.printStackTrace$();
            } else throw new Error('invalid overload');
        }

        public printStackTrace$java_io_PrintWriter(out: java.io.PrintWriter) {
            this.delegate.printStackTrace$java_io_PrintWriter(out);
        }

        /**
         * {@inheritDoc}
         * @param {java.io.PrintWriter} out
         */
        public printPartialStackTrace(out: java.io.PrintWriter) {
            console.error(super.message, super);
        }
    }
    NestableRuntimeException["__class"] = "org.openprovenance.apache.commons.lang.exception.NestableRuntimeException";
    NestableRuntimeException["__interfaces"] = ["org.openprovenance.apache.commons.lang.exception.Nestable","java.io.Serializable"];


}

