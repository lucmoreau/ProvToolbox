/* Generated from Java with JSweet 3.0.0 - http://www.jsweet.org */
namespace org.openprovenance.prov.model.exception {
    /**
     * An Exception when an attribute is given an invalid value. For instance, when a prov:label is given a value that is not a string.
     * @author lavm
     * @param {string} message
     * @param {Error} cause
     * @param {boolean} enableSuppression
     * @param {boolean} writableStackTrace
     * @class
     * @extends Error
     */
    export class InvalidAttributeValueException extends Error {
        /**
         * 
         */
        static serialVersionUID: number = 1;

        public constructor(message?: any, cause?: any, enableSuppression?: any, writableStackTrace?: any) {
            if (((typeof message === 'string') || message === null) && ((cause != null && (cause["__classes"] && cause["__classes"].indexOf("java.lang.Throwable") >= 0) || cause != null && cause instanceof <any>Error) || cause === null) && ((typeof enableSuppression === 'boolean') || enableSuppression === null) && ((typeof writableStackTrace === 'boolean') || writableStackTrace === null)) {
                let __args = arguments;
                super(message); this.message=message;
            } else if (((typeof message === 'string') || message === null) && ((cause != null && (cause["__classes"] && cause["__classes"].indexOf("java.lang.Throwable") >= 0) || cause != null && cause instanceof <any>Error) || cause === null) && enableSuppression === undefined && writableStackTrace === undefined) {
                let __args = arguments;
                super(message); this.message=message;
            } else if (((typeof message === 'string') || message === null) && cause === undefined && enableSuppression === undefined && writableStackTrace === undefined) {
                let __args = arguments;
                let arg0: any = __args[0];
                super(arg0); this.message=arg0;
            } else if (((message != null && (message["__classes"] && message["__classes"].indexOf("java.lang.Throwable") >= 0) || message != null && message instanceof <any>Error) || message === null) && cause === undefined && enableSuppression === undefined && writableStackTrace === undefined) {
                let __args = arguments;
                let cause: any = __args[0];
                super(cause); this.message=cause;
            } else if (message === undefined && cause === undefined && enableSuppression === undefined && writableStackTrace === undefined) {
                let __args = arguments;
                super();
            } else throw new Error('invalid overload');
        }
    }
    InvalidAttributeValueException["__class"] = "org.openprovenance.prov.model.exception.InvalidAttributeValueException";
    InvalidAttributeValueException["__interfaces"] = ["java.io.Serializable"];


}

