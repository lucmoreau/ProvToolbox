/* Generated from Java with JSweet 3.0.0 - http://www.jsweet.org */
namespace org.openprovenance.apache.commons.lang.builder {
    /**
     * Constructor for IDKey
     * @param {*} _value The value
     * @class
     * @author Apache Software Foundation
     */
    export class IDKey {
        /*private*/ value: any;

        /*private*/ id: number;

        public constructor(_value: any) {
            if (this.value === undefined) { this.value = null; }
            if (this.id === undefined) { this.id = 0; }
            this.id = java.lang.System.identityHashCode(_value);
            this.value = _value;
        }

        /**
         * returns hashcode - i.e1. the system identity hashcode.
         * @return {number} the hashcode
         */
        public hashCode(): number {
            return this.id;
        }

        /**
         * checks if instances are equal
         * @param {*} other The other object to compare to
         * @return {boolean} if the instances are for the same object
         */
        public equals(other: any): boolean {
            if (!(other != null && other instanceof <any>org.openprovenance.apache.commons.lang.builder.IDKey)){
                return false;
            }
            const idKey: IDKey = <IDKey>other;
            if (this.id !== idKey.id){
                return false;
            }
            return this.value === idKey.value;
        }
    }
    IDKey["__class"] = "org.openprovenance.apache.commons.lang.builder.IDKey";

}

