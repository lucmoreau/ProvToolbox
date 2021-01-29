/* Generated from Java with JSweet 3.0.0 - http://www.jsweet.org */
namespace org.openprovenance.apache.commons.lang {
    /**
     * Constructs the exception using a message and cause.
     * 
     * @param {string} message  the message to use
     * @param {Error} cause  the underlying cause
     * @class
     * @extends org.openprovenance.apache.commons.lang.exception.NestableRuntimeException
     * @author Matthew Hawthorne
     */
    export class UnhandledException extends org.openprovenance.apache.commons.lang.exception.NestableRuntimeException {
        /**
         * Required for serialization support.
         * 
         * @see java.io.Serializable
         */
        static __org_openprovenance_apache_commons_lang_UnhandledException_serialVersionUID: number = 1832101364842773720;

        public constructor(message?: any, cause?: any) {
            if (((typeof message === 'string') || message === null) && ((cause != null && (cause["__classes"] && cause["__classes"].indexOf("java.lang.Throwable") >= 0) || cause != null && cause instanceof <any>Error) || cause === null)) {
                let __args = arguments;
                super(message, cause);
            } else if (((message != null && (message["__classes"] && message["__classes"].indexOf("java.lang.Throwable") >= 0) || message != null && message instanceof <any>Error) || message === null) && cause === undefined) {
                let __args = arguments;
                let cause: any = __args[0];
                super(cause);
            } else throw new Error('invalid overload');
        }
    }
    UnhandledException["__class"] = "org.openprovenance.apache.commons.lang.UnhandledException";
    UnhandledException["__interfaces"] = ["org.openprovenance.apache.commons.lang.exception.Nestable","java.io.Serializable"];


}

