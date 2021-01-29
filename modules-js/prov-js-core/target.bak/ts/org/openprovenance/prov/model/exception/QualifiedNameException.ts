/* Generated from Java with JSweet 3.0.0 - http://www.jsweet.org */
namespace org.openprovenance.prov.model.exception {
    /**
     * @param {string} message a messae
     * @param {Error} cause the cause
     * @class
     * @extends Error
     * @author lavm
     */
    export class QualifiedNameException extends Error {
        /**
         * 
         */
        static serialVersionUID: number = -2683382479059721612;

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
    QualifiedNameException["__class"] = "org.openprovenance.prov.model.exception.QualifiedNameException";
    QualifiedNameException["__interfaces"] = ["java.io.Serializable"];


}

