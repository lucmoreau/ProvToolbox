/* Generated from Java with JSweet 3.0.0 - http://www.jsweet.org */
namespace org.openprovenance.prov.model.exception {
    /**
     * A generic unchecked exception.
     * @author lavm
     * @param {string} reason
     * @param {Error} e
     * @class
     * @extends Error
     */
    export class UncheckedException extends Error {
        public constructor(reason?: any, e?: any) {
            if (((typeof reason === 'string') || reason === null) && ((e != null && (e["__classes"] && e["__classes"].indexOf("java.lang.Exception") >= 0) || e != null && e instanceof <any>Error) || e === null)) {
                let __args = arguments;
                super(reason); this.message=reason;
            } else if (((reason != null && (reason["__classes"] && reason["__classes"].indexOf("java.lang.Exception") >= 0) || reason != null && reason instanceof <any>Error) || reason === null) && e === undefined) {
                let __args = arguments;
                let e: any = __args[0];
                super("Unchecked Exception"); this.message="Unchecked Exception";
            } else throw new Error('invalid overload');
        }

        /**
         * 
         */
        static serialVersionUID: number = 1569547113186462619;
    }
    UncheckedException["__class"] = "org.openprovenance.prov.model.exception.UncheckedException";
    UncheckedException["__interfaces"] = ["java.io.Serializable"];


}

