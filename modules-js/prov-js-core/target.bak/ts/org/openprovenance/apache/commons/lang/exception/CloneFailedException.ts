/* Generated from Java with JSweet 3.0.0 - http://www.jsweet.org */
namespace org.openprovenance.apache.commons.lang.exception {
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
    export class CloneFailedException extends org.openprovenance.apache.commons.lang.exception.NestableRuntimeException {
        static __org_openprovenance_apache_commons_lang_exception_CloneFailedException_serialVersionUID: number = 20091223;

        public constructor(message?: any, cause?: any) {
            if (((typeof message === 'string') || message === null) && ((cause != null && (cause["__classes"] && cause["__classes"].indexOf("java.lang.Throwable") >= 0) || cause != null && cause instanceof <any>Error) || cause === null)) {
                let __args = arguments;
                super(message, cause);
            } else if (((typeof message === 'string') || message === null) && cause === undefined) {
                let __args = arguments;
                super(message);
            } else if (((message != null && (message["__classes"] && message["__classes"].indexOf("java.lang.Throwable") >= 0) || message != null && message instanceof <any>Error) || message === null) && cause === undefined) {
                let __args = arguments;
                let cause: any = __args[0];
                super(cause);
            } else throw new Error('invalid overload');
        }
    }
    CloneFailedException["__class"] = "org.openprovenance.apache.commons.lang.exception.CloneFailedException";
    CloneFailedException["__interfaces"] = ["org.openprovenance.apache.commons.lang.exception.Nestable","java.io.Serializable"];


}

