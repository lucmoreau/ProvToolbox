/* Generated from Java with JSweet 3.0.0 - http://www.jsweet.org */
namespace org.openprovenance.prov.model.exception {
    /**
     * @param {string} message a messae
     * @param {Error} cause the cause
     * @class
     * @extends Error
     */
    export class ParserException extends Error {
        public constructor(message?: any, cause?: any) {
            if (((typeof message === 'string') || message === null) && ((cause != null && (cause["__classes"] && cause["__classes"].indexOf("java.lang.Throwable") >= 0) || cause != null && cause instanceof <any>Error) || cause === null)) {
                let __args = arguments;
                super(message); this.message=message;
            } else if (((typeof message === 'string') || message === null) && cause === undefined) {
                let __args = arguments;
                super(message); this.message=message;
            } else if (((message != null && (message["__classes"] && message["__classes"].indexOf("java.lang.Throwable") >= 0) || message != null && message instanceof <any>Error) || message === null) && cause === undefined) {
                let __args = arguments;
                let cause: any = __args[0];
                super(cause); this.message=cause;
            } else if (message === undefined && cause === undefined) {
                let __args = arguments;
                super();
            } else throw new Error('invalid overload');
        }
    }
    ParserException["__class"] = "org.openprovenance.prov.model.exception.ParserException";
    ParserException["__interfaces"] = ["java.io.Serializable"];


}

