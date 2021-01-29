/* Generated from Java with JSweet 3.0.0 - http://www.jsweet.org */
namespace org.openprovenance.apache.commons.lang {
    /**
     * <p><code>BooleanUtils</code> instances should NOT be constructed in standard programming.
     * Instead, the class should be used as <code>BooleanUtils.toBooleanObject(true);</code>.</p>
     * 
     * <p>This constructor is public to permit tools that require a JavaBean instance
     * to operate.</p>
     * @class
     * @author Apache Software Foundation
     */
    export class BooleanUtils {
        public constructor() {
        }

        /**
         * <p>Negates the specified boolean.</p>
         * 
         * <p>If <code>null</code> is passed in, <code>null</code> will be returned.</p>
         * 
         * <pre>
         * BooleanUtils.negate(Boolean.TRUE)  = Boolean.FALSE;
         * BooleanUtils.negate(Boolean.FALSE) = Boolean.TRUE;
         * BooleanUtils.negate(null)          = null;
         * </pre>
         * 
         * @param {boolean} bool  the Boolean to negate, may be null
         * @return {boolean} the negated Boolean, or <code>null</code> if <code>null</code> input
         */
        public static negate(bool: boolean): boolean {
            if (bool == null){
                return null;
            }
            return (/* booleanValue */bool ? false : true);
        }

        /**
         * <p>Checks if a <code>Boolean</code> value is <code>true</code>,
         * handling <code>null</code> by returning <code>false</code>.</p>
         * 
         * <pre>
         * BooleanUtils.isTrue(Boolean.TRUE)  = true
         * BooleanUtils.isTrue(Boolean.FALSE) = false
         * BooleanUtils.isTrue(null)          = false
         * </pre>
         * 
         * @param {boolean} bool  the boolean to check, null returns <code>false</code>
         * @return {boolean} <code>true</code> only if the input is non-null and true
         * @since 2.1
         */
        public static isTrue(bool: boolean): boolean {
            if (bool == null){
                return false;
            }
            return /* booleanValue */bool ? true : false;
        }

        /**
         * <p>Checks if a <code>Boolean</code> value is <i>not</i> <code>true</code>,
         * handling <code>null</code> by returning <code>true</code>.</p>
         * 
         * <pre>
         * BooleanUtils.isNotTrue(Boolean.TRUE)  = false
         * BooleanUtils.isNotTrue(Boolean.FALSE) = true
         * BooleanUtils.isNotTrue(null)          = true
         * </pre>
         * 
         * @param {boolean} bool  the boolean to check, null returns <code>true</code>
         * @return {boolean} <code>true</code> if the input is null or false
         * @since 2.3
         */
        public static isNotTrue(bool: boolean): boolean {
            return !BooleanUtils.isTrue(bool);
        }

        /**
         * <p>Checks if a <code>Boolean</code> value is <code>false</code>,
         * handling <code>null</code> by returning <code>false</code>.</p>
         * 
         * <pre>
         * BooleanUtils.isFalse(Boolean.TRUE)  = false
         * BooleanUtils.isFalse(Boolean.FALSE) = true
         * BooleanUtils.isFalse(null)          = false
         * </pre>
         * 
         * @param {boolean} bool  the boolean to check, null returns <code>false</code>
         * @return {boolean} <code>true</code> only if the input is non-null and false
         * @since 2.1
         */
        public static isFalse(bool: boolean): boolean {
            if (bool == null){
                return false;
            }
            return /* booleanValue */bool ? false : true;
        }

        /**
         * <p>Checks if a <code>Boolean</code> value is <i>not</i> <code>false</code>,
         * handling <code>null</code> by returning <code>true</code>.</p>
         * 
         * <pre>
         * BooleanUtils.isNotFalse(Boolean.TRUE)  = true
         * BooleanUtils.isNotFalse(Boolean.FALSE) = false
         * BooleanUtils.isNotFalse(null)          = true
         * </pre>
         * 
         * @param {boolean} bool  the boolean to check, null returns <code>true</code>
         * @return {boolean} <code>true</code> if the input is null or true
         * @since 2.3
         */
        public static isNotFalse(bool: boolean): boolean {
            return !BooleanUtils.isFalse(bool);
        }

        public static toBooleanObject$boolean(bool: boolean): boolean {
            return bool ? true : false;
        }

        public static toBoolean$java_lang_Boolean(bool: boolean): boolean {
            if (bool == null){
                return false;
            }
            return /* booleanValue */bool ? true : false;
        }

        /**
         * <p>Converts a Boolean to a boolean handling <code>null</code>.</p>
         * 
         * <pre>
         * BooleanUtils.toBooleanDefaultIfNull(Boolean.TRUE, false) = true
         * BooleanUtils.toBooleanDefaultIfNull(Boolean.FALSE, true) = false
         * BooleanUtils.toBooleanDefaultIfNull(null, true)          = true
         * </pre>
         * 
         * @param {boolean} bool  the boolean to convert
         * @param {boolean} valueIfNull  the boolean value to return if <code>null</code>
         * @return {boolean} <code>true</code> or <code>false</code>
         */
        public static toBooleanDefaultIfNull(bool: boolean, valueIfNull: boolean): boolean {
            if (bool == null){
                return valueIfNull;
            }
            return /* booleanValue */bool ? true : false;
        }

        public static toBoolean$int(value: number): boolean {
            return value === 0 ? false : true;
        }

        public static toBooleanObject$int(value: number): boolean {
            return value === 0 ? false : true;
        }

        public static toBooleanObject$java_lang_Integer(value: number): boolean {
            if (value == null){
                return null;
            }
            return /* intValue */(value|0) === 0 ? false : true;
        }

        public static toBoolean$int$int$int(value: number, trueValue: number, falseValue: number): boolean {
            if (value === trueValue){
                return true;
            } else if (value === falseValue){
                return false;
            }
            throw Object.defineProperty(new Error("The Integer did not match either specified value"), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.Object','java.lang.RuntimeException','java.lang.IllegalArgumentException','java.lang.Exception'] });
        }

        public static toBoolean$java_lang_Integer$java_lang_Integer$java_lang_Integer(value: number, trueValue: number, falseValue: number): boolean {
            if (value == null){
                if (trueValue == null){
                    return true;
                } else if (falseValue == null){
                    return false;
                }
            } else if (value === trueValue){
                return true;
            } else if (value === falseValue){
                return false;
            }
            throw Object.defineProperty(new Error("The Integer did not match either specified value"), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.Object','java.lang.RuntimeException','java.lang.IllegalArgumentException','java.lang.Exception'] });
        }

        /**
         * <p>Converts an Integer to a boolean specifying the conversion values.</p>
         * 
         * <pre>
         * BooleanUtils.toBoolean(new Integer(0), new Integer(1), new Integer(0)) = false
         * BooleanUtils.toBoolean(new Integer(1), new Integer(1), new Integer(0)) = true
         * BooleanUtils.toBoolean(new Integer(2), new Integer(1), new Integer(2)) = false
         * BooleanUtils.toBoolean(new Integer(2), new Integer(2), new Integer(0)) = true
         * BooleanUtils.toBoolean(null, null, new Integer(0))                     = true
         * </pre>
         * 
         * @param {number} value  the Integer to convert
         * @param {number} trueValue  the value to match for <code>true</code>,
         * may be <code>null</code>
         * @param {number} falseValue  the value to match for <code>false</code>,
         * may be <code>null</code>
         * @return {boolean} <code>true</code> or <code>false</code>
         * @throws IllegalArgumentException if no match
         */
        public static toBoolean(value?: any, trueValue?: any, falseValue?: any): any {
            if (((typeof value === 'number') || value === null) && ((typeof trueValue === 'number') || trueValue === null) && ((typeof falseValue === 'number') || falseValue === null)) {
                return <any>org.openprovenance.apache.commons.lang.BooleanUtils.toBoolean$java_lang_Integer$java_lang_Integer$java_lang_Integer(value, trueValue, falseValue);
            } else if (((typeof value === 'string') || value === null) && ((typeof trueValue === 'string') || trueValue === null) && ((typeof falseValue === 'string') || falseValue === null)) {
                return <any>org.openprovenance.apache.commons.lang.BooleanUtils.toBoolean$java_lang_String$java_lang_String$java_lang_String(value, trueValue, falseValue);
            } else if (((typeof value === 'number') || value === null) && ((typeof trueValue === 'number') || trueValue === null) && ((typeof falseValue === 'number') || falseValue === null)) {
                return <any>org.openprovenance.apache.commons.lang.BooleanUtils.toBoolean$int$int$int(value, trueValue, falseValue);
            } else if (((typeof value === 'boolean') || value === null) && trueValue === undefined && falseValue === undefined) {
                return <any>org.openprovenance.apache.commons.lang.BooleanUtils.toBoolean$java_lang_Boolean(value);
            } else if (((typeof value === 'string') || value === null) && trueValue === undefined && falseValue === undefined) {
                return <any>org.openprovenance.apache.commons.lang.BooleanUtils.toBoolean$java_lang_String(value);
            } else if (((typeof value === 'number') || value === null) && trueValue === undefined && falseValue === undefined) {
                return <any>org.openprovenance.apache.commons.lang.BooleanUtils.toBoolean$int(value);
            } else throw new Error('invalid overload');
        }

        public static toBooleanObject$int$int$int$int(value: number, trueValue: number, falseValue: number, nullValue: number): boolean {
            if (value === trueValue){
                return true;
            } else if (value === falseValue){
                return false;
            } else if (value === nullValue){
                return null;
            }
            throw Object.defineProperty(new Error("The Integer did not match any specified value"), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.Object','java.lang.RuntimeException','java.lang.IllegalArgumentException','java.lang.Exception'] });
        }

        public static toBooleanObject$java_lang_Integer$java_lang_Integer$java_lang_Integer$java_lang_Integer(value: number, trueValue: number, falseValue: number, nullValue: number): boolean {
            if (value == null){
                if (trueValue == null){
                    return true;
                } else if (falseValue == null){
                    return false;
                } else if (nullValue == null){
                    return null;
                }
            } else if (value === trueValue){
                return true;
            } else if (value === falseValue){
                return false;
            } else if (value === nullValue){
                return null;
            }
            throw Object.defineProperty(new Error("The Integer did not match any specified value"), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.Object','java.lang.RuntimeException','java.lang.IllegalArgumentException','java.lang.Exception'] });
        }

        /**
         * <p>Converts an Integer to a Boolean specifying the conversion values.</p>
         * 
         * <pre>
         * BooleanUtils.toBooleanObject(new Integer(0), new Integer(0), new Integer(2), new Integer(3)) = Boolean.TRUE
         * BooleanUtils.toBooleanObject(new Integer(2), new Integer(1), new Integer(2), new Integer(3)) = Boolean.FALSE
         * BooleanUtils.toBooleanObject(new Integer(3), new Integer(1), new Integer(2), new Integer(3)) = null
         * </pre>
         * 
         * @param {number} value  the Integer to convert
         * @param {number} trueValue  the value to match for <code>true</code>,
         * may be <code>null</code>
         * @param {number} falseValue  the value to match for <code>false</code>,
         * may be <code>null</code>
         * @param {number} nullValue  the value to to match for <code>null</code>,
         * may be <code>null</code>
         * @return {boolean} Boolean.TRUE, Boolean.FALSE, or <code>null</code>
         * @throws IllegalArgumentException if no match
         */
        public static toBooleanObject(value?: any, trueValue?: any, falseValue?: any, nullValue?: any): any {
            if (((typeof value === 'number') || value === null) && ((typeof trueValue === 'number') || trueValue === null) && ((typeof falseValue === 'number') || falseValue === null) && ((typeof nullValue === 'number') || nullValue === null)) {
                return <any>org.openprovenance.apache.commons.lang.BooleanUtils.toBooleanObject$java_lang_Integer$java_lang_Integer$java_lang_Integer$java_lang_Integer(value, trueValue, falseValue, nullValue);
            } else if (((typeof value === 'string') || value === null) && ((typeof trueValue === 'string') || trueValue === null) && ((typeof falseValue === 'string') || falseValue === null) && ((typeof nullValue === 'string') || nullValue === null)) {
                return <any>org.openprovenance.apache.commons.lang.BooleanUtils.toBooleanObject$java_lang_String$java_lang_String$java_lang_String$java_lang_String(value, trueValue, falseValue, nullValue);
            } else if (((typeof value === 'number') || value === null) && ((typeof trueValue === 'number') || trueValue === null) && ((typeof falseValue === 'number') || falseValue === null) && ((typeof nullValue === 'number') || nullValue === null)) {
                return <any>org.openprovenance.apache.commons.lang.BooleanUtils.toBooleanObject$int$int$int$int(value, trueValue, falseValue, nullValue);
            } else if (((typeof value === 'number') || value === null) && trueValue === undefined && falseValue === undefined && nullValue === undefined) {
                return <any>org.openprovenance.apache.commons.lang.BooleanUtils.toBooleanObject$java_lang_Integer(value);
            } else if (((typeof value === 'string') || value === null) && trueValue === undefined && falseValue === undefined && nullValue === undefined) {
                return <any>org.openprovenance.apache.commons.lang.BooleanUtils.toBooleanObject$java_lang_String(value);
            } else if (((typeof value === 'boolean') || value === null) && trueValue === undefined && falseValue === undefined && nullValue === undefined) {
                return <any>org.openprovenance.apache.commons.lang.BooleanUtils.toBooleanObject$boolean(value);
            } else if (((typeof value === 'number') || value === null) && trueValue === undefined && falseValue === undefined && nullValue === undefined) {
                return <any>org.openprovenance.apache.commons.lang.BooleanUtils.toBooleanObject$int(value);
            } else throw new Error('invalid overload');
        }

        public static toInteger$boolean(bool: boolean): number {
            return bool ? 1 : 0;
        }

        public static toIntegerObject$boolean(bool: boolean): number {
            return bool ? org.openprovenance.apache.commons.lang.math.NumberUtils.INTEGER_ONE_$LI$() : org.openprovenance.apache.commons.lang.math.NumberUtils.INTEGER_ZERO_$LI$();
        }

        public static toIntegerObject$java_lang_Boolean(bool: boolean): number {
            if (bool == null){
                return null;
            }
            return /* booleanValue */bool ? org.openprovenance.apache.commons.lang.math.NumberUtils.INTEGER_ONE_$LI$() : org.openprovenance.apache.commons.lang.math.NumberUtils.INTEGER_ZERO_$LI$();
        }

        public static toInteger$boolean$int$int(bool: boolean, trueValue: number, falseValue: number): number {
            return bool ? trueValue : falseValue;
        }

        public static toInteger$java_lang_Boolean$int$int$int(bool: boolean, trueValue: number, falseValue: number, nullValue: number): number {
            if (bool == null){
                return nullValue;
            }
            return /* booleanValue */bool ? trueValue : falseValue;
        }

        /**
         * <p>Converts a Boolean to an int specifying the conversion values.</p>
         * 
         * <pre>
         * BooleanUtils.toInteger(Boolean.TRUE, 1, 0, 2)  = 1
         * BooleanUtils.toInteger(Boolean.FALSE, 1, 0, 2) = 0
         * BooleanUtils.toInteger(null, 1, 0, 2)          = 2
         * </pre>
         * 
         * @param {boolean} bool  the Boolean to convert
         * @param {number} trueValue  the value to return if <code>true</code>
         * @param {number} falseValue  the value to return if <code>false</code>
         * @param {number} nullValue  the value to return if <code>null</code>
         * @return {number} the appropriate value
         */
        public static toInteger(bool?: any, trueValue?: any, falseValue?: any, nullValue?: any): any {
            if (((typeof bool === 'boolean') || bool === null) && ((typeof trueValue === 'number') || trueValue === null) && ((typeof falseValue === 'number') || falseValue === null) && ((typeof nullValue === 'number') || nullValue === null)) {
                return <any>org.openprovenance.apache.commons.lang.BooleanUtils.toInteger$java_lang_Boolean$int$int$int(bool, trueValue, falseValue, nullValue);
            } else if (((typeof bool === 'boolean') || bool === null) && ((typeof trueValue === 'number') || trueValue === null) && ((typeof falseValue === 'number') || falseValue === null) && nullValue === undefined) {
                return <any>org.openprovenance.apache.commons.lang.BooleanUtils.toInteger$boolean$int$int(bool, trueValue, falseValue);
            } else if (((typeof bool === 'boolean') || bool === null) && trueValue === undefined && falseValue === undefined && nullValue === undefined) {
                return <any>org.openprovenance.apache.commons.lang.BooleanUtils.toInteger$boolean(bool);
            } else throw new Error('invalid overload');
        }

        public static toIntegerObject$boolean$java_lang_Integer$java_lang_Integer(bool: boolean, trueValue: number, falseValue: number): number {
            return bool ? trueValue : falseValue;
        }

        public static toIntegerObject$java_lang_Boolean$java_lang_Integer$java_lang_Integer$java_lang_Integer(bool: boolean, trueValue: number, falseValue: number, nullValue: number): number {
            if (bool == null){
                return nullValue;
            }
            return /* booleanValue */bool ? trueValue : falseValue;
        }

        /**
         * <p>Converts a Boolean to an Integer specifying the conversion values.</p>
         * 
         * <pre>
         * BooleanUtils.toIntegerObject(Boolean.TRUE, new Integer(1), new Integer(0), new Integer(2))  = new Integer(1)
         * BooleanUtils.toIntegerObject(Boolean.FALSE, new Integer(1), new Integer(0), new Integer(2)) = new Integer(0)
         * BooleanUtils.toIntegerObject(null, new Integer(1), new Integer(0), new Integer(2))          = new Integer(2)
         * </pre>
         * 
         * @param {boolean} bool  the Boolean to convert
         * @param {number} trueValue  the value to return if <code>true</code>,
         * may be <code>null</code>
         * @param {number} falseValue  the value to return if <code>false</code>,
         * may be <code>null</code>
         * @param {number} nullValue  the value to return if <code>null</code>,
         * may be <code>null</code>
         * @return {number} the appropriate value
         */
        public static toIntegerObject(bool?: any, trueValue?: any, falseValue?: any, nullValue?: any): any {
            if (((typeof bool === 'boolean') || bool === null) && ((typeof trueValue === 'number') || trueValue === null) && ((typeof falseValue === 'number') || falseValue === null) && ((typeof nullValue === 'number') || nullValue === null)) {
                return <any>org.openprovenance.apache.commons.lang.BooleanUtils.toIntegerObject$java_lang_Boolean$java_lang_Integer$java_lang_Integer$java_lang_Integer(bool, trueValue, falseValue, nullValue);
            } else if (((typeof bool === 'boolean') || bool === null) && ((typeof trueValue === 'number') || trueValue === null) && ((typeof falseValue === 'number') || falseValue === null) && nullValue === undefined) {
                return <any>org.openprovenance.apache.commons.lang.BooleanUtils.toIntegerObject$boolean$java_lang_Integer$java_lang_Integer(bool, trueValue, falseValue);
            } else if (((typeof bool === 'boolean') || bool === null) && trueValue === undefined && falseValue === undefined && nullValue === undefined) {
                return <any>org.openprovenance.apache.commons.lang.BooleanUtils.toIntegerObject$java_lang_Boolean(bool);
            } else if (((typeof bool === 'boolean') || bool === null) && trueValue === undefined && falseValue === undefined && nullValue === undefined) {
                return <any>org.openprovenance.apache.commons.lang.BooleanUtils.toIntegerObject$boolean(bool);
            } else throw new Error('invalid overload');
        }

        public static toBooleanObject$java_lang_String(str: string): boolean {
            if (str === "true"){
                return true;
            }
            if (str == null){
                return null;
            }
            switch((str.length)) {
            case 1:
                {
                    const ch0: string = str.charAt(0);
                    if (((c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(ch0) == 'y'.charCodeAt(0) || (c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(ch0) == 'Y'.charCodeAt(0)) || ((c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(ch0) == 't'.charCodeAt(0) || (c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(ch0) == 'T'.charCodeAt(0))){
                        return true;
                    }
                    if (((c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(ch0) == 'n'.charCodeAt(0) || (c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(ch0) == 'N'.charCodeAt(0)) || ((c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(ch0) == 'f'.charCodeAt(0) || (c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(ch0) == 'F'.charCodeAt(0))){
                        return false;
                    }
                    break;
                };
            case 2:
                {
                    const ch0: string = str.charAt(0);
                    const ch1: string = str.charAt(1);
                    if (((c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(ch0) == 'o'.charCodeAt(0) || (c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(ch0) == 'O'.charCodeAt(0)) && ((c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(ch1) == 'n'.charCodeAt(0) || (c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(ch1) == 'N'.charCodeAt(0))){
                        return true;
                    }
                    if (((c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(ch0) == 'n'.charCodeAt(0) || (c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(ch0) == 'N'.charCodeAt(0)) && ((c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(ch1) == 'o'.charCodeAt(0) || (c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(ch1) == 'O'.charCodeAt(0))){
                        return false;
                    }
                    break;
                };
            case 3:
                {
                    const ch0: string = str.charAt(0);
                    const ch1: string = str.charAt(1);
                    const ch2: string = str.charAt(2);
                    if (((c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(ch0) == 'y'.charCodeAt(0) || (c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(ch0) == 'Y'.charCodeAt(0)) && ((c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(ch1) == 'e'.charCodeAt(0) || (c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(ch1) == 'E'.charCodeAt(0)) && ((c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(ch2) == 's'.charCodeAt(0) || (c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(ch2) == 'S'.charCodeAt(0))){
                        return true;
                    }
                    if (((c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(ch0) == 'o'.charCodeAt(0) || (c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(ch0) == 'O'.charCodeAt(0)) && ((c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(ch1) == 'f'.charCodeAt(0) || (c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(ch1) == 'F'.charCodeAt(0)) && ((c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(ch2) == 'f'.charCodeAt(0) || (c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(ch2) == 'F'.charCodeAt(0))){
                        return false;
                    }
                    break;
                };
            case 4:
                {
                    const ch0: string = str.charAt(0);
                    const ch1: string = str.charAt(1);
                    const ch2: string = str.charAt(2);
                    const ch3: string = str.charAt(3);
                    if (((c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(ch0) == 't'.charCodeAt(0) || (c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(ch0) == 'T'.charCodeAt(0)) && ((c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(ch1) == 'r'.charCodeAt(0) || (c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(ch1) == 'R'.charCodeAt(0)) && ((c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(ch2) == 'u'.charCodeAt(0) || (c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(ch2) == 'U'.charCodeAt(0)) && ((c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(ch3) == 'e'.charCodeAt(0) || (c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(ch3) == 'E'.charCodeAt(0))){
                        return true;
                    }
                    break;
                };
            case 5:
                {
                    const ch0: string = str.charAt(0);
                    const ch1: string = str.charAt(1);
                    const ch2: string = str.charAt(2);
                    const ch3: string = str.charAt(3);
                    const ch4: string = str.charAt(4);
                    if (((c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(ch0) == 'f'.charCodeAt(0) || (c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(ch0) == 'F'.charCodeAt(0)) && ((c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(ch1) == 'a'.charCodeAt(0) || (c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(ch1) == 'A'.charCodeAt(0)) && ((c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(ch2) == 'l'.charCodeAt(0) || (c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(ch2) == 'L'.charCodeAt(0)) && ((c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(ch3) == 's'.charCodeAt(0) || (c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(ch3) == 'S'.charCodeAt(0)) && ((c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(ch4) == 'e'.charCodeAt(0) || (c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(ch4) == 'E'.charCodeAt(0))){
                        return false;
                    }
                    break;
                };
            }
            return null;
        }

        public static toBooleanObject$java_lang_String$java_lang_String$java_lang_String$java_lang_String(str: string, trueString: string, falseString: string, nullString: string): boolean {
            if (str == null){
                if (trueString == null){
                    return true;
                } else if (falseString == null){
                    return false;
                } else if (nullString == null){
                    return null;
                }
            } else if (str === trueString){
                return true;
            } else if (str === falseString){
                return false;
            } else if (str === nullString){
                return null;
            }
            throw Object.defineProperty(new Error("The String did not match any specified value"), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.Object','java.lang.RuntimeException','java.lang.IllegalArgumentException','java.lang.Exception'] });
        }

        public static toBoolean$java_lang_String(str: string): boolean {
            return BooleanUtils.toBoolean$java_lang_Boolean(BooleanUtils.toBooleanObject$java_lang_String(str));
        }

        public static toBoolean$java_lang_String$java_lang_String$java_lang_String(str: string, trueString: string, falseString: string): boolean {
            if (str == null){
                if (trueString == null){
                    return true;
                } else if (falseString == null){
                    return false;
                }
            } else if (str === trueString){
                return true;
            } else if (str === falseString){
                return false;
            }
            throw Object.defineProperty(new Error("The String did not match either specified value"), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.Object','java.lang.RuntimeException','java.lang.IllegalArgumentException','java.lang.Exception'] });
        }

        public static toStringTrueFalse$java_lang_Boolean(bool: boolean): string {
            return BooleanUtils.toString$java_lang_Boolean$java_lang_String$java_lang_String$java_lang_String(bool, "true", "false", null);
        }

        /**
         * <p>Converts a Boolean to a String returning <code>'true'</code>,
         * <code>'false'</code>, or <code>null</code>.</p>
         * 
         * <pre>
         * BooleanUtils.toStringTrueFalse(Boolean.TRUE)  = "true"
         * BooleanUtils.toStringTrueFalse(Boolean.FALSE) = "false"
         * BooleanUtils.toStringTrueFalse(null)          = null;
         * </pre>
         * 
         * @param {boolean} bool  the Boolean to check
         * @return {string} <code>'true'</code>, <code>'false'</code>,
         * or <code>null</code>
         */
        public static toStringTrueFalse(bool?: any): any {
            if (((typeof bool === 'boolean') || bool === null)) {
                return <any>org.openprovenance.apache.commons.lang.BooleanUtils.toStringTrueFalse$java_lang_Boolean(bool);
            } else if (((typeof bool === 'boolean') || bool === null)) {
                return <any>org.openprovenance.apache.commons.lang.BooleanUtils.toStringTrueFalse$boolean(bool);
            } else throw new Error('invalid overload');
        }

        public static toStringOnOff$java_lang_Boolean(bool: boolean): string {
            return BooleanUtils.toString$java_lang_Boolean$java_lang_String$java_lang_String$java_lang_String(bool, "on", "off", null);
        }

        /**
         * <p>Converts a Boolean to a String returning <code>'on'</code>,
         * <code>'off'</code>, or <code>null</code>.</p>
         * 
         * <pre>
         * BooleanUtils.toStringOnOff(Boolean.TRUE)  = "on"
         * BooleanUtils.toStringOnOff(Boolean.FALSE) = "off"
         * BooleanUtils.toStringOnOff(null)          = null;
         * </pre>
         * 
         * @param {boolean} bool  the Boolean to check
         * @return {string} <code>'on'</code>, <code>'off'</code>,
         * or <code>null</code>
         */
        public static toStringOnOff(bool?: any): any {
            if (((typeof bool === 'boolean') || bool === null)) {
                return <any>org.openprovenance.apache.commons.lang.BooleanUtils.toStringOnOff$java_lang_Boolean(bool);
            } else if (((typeof bool === 'boolean') || bool === null)) {
                return <any>org.openprovenance.apache.commons.lang.BooleanUtils.toStringOnOff$boolean(bool);
            } else throw new Error('invalid overload');
        }

        public static toStringYesNo$java_lang_Boolean(bool: boolean): string {
            return BooleanUtils.toString$java_lang_Boolean$java_lang_String$java_lang_String$java_lang_String(bool, "yes", "no", null);
        }

        /**
         * <p>Converts a Boolean to a String returning <code>'yes'</code>,
         * <code>'no'</code>, or <code>null</code>.</p>
         * 
         * <pre>
         * BooleanUtils.toStringYesNo(Boolean.TRUE)  = "yes"
         * BooleanUtils.toStringYesNo(Boolean.FALSE) = "no"
         * BooleanUtils.toStringYesNo(null)          = null;
         * </pre>
         * 
         * @param {boolean} bool  the Boolean to check
         * @return {string} <code>'yes'</code>, <code>'no'</code>,
         * or <code>null</code>
         */
        public static toStringYesNo(bool?: any): any {
            if (((typeof bool === 'boolean') || bool === null)) {
                return <any>org.openprovenance.apache.commons.lang.BooleanUtils.toStringYesNo$java_lang_Boolean(bool);
            } else if (((typeof bool === 'boolean') || bool === null)) {
                return <any>org.openprovenance.apache.commons.lang.BooleanUtils.toStringYesNo$boolean(bool);
            } else throw new Error('invalid overload');
        }

        public static toString$java_lang_Boolean$java_lang_String$java_lang_String$java_lang_String(bool: boolean, trueString: string, falseString: string, nullString: string): string {
            if (bool == null){
                return nullString;
            }
            return /* booleanValue */bool ? trueString : falseString;
        }

        /**
         * <p>Converts a Boolean to a String returning one of the input Strings.</p>
         * 
         * <pre>
         * BooleanUtils.toString(Boolean.TRUE, "true", "false", null)   = "true"
         * BooleanUtils.toString(Boolean.FALSE, "true", "false", null)  = "false"
         * BooleanUtils.toString(null, "true", "false", null)           = null;
         * </pre>
         * 
         * @param {boolean} bool  the Boolean to check
         * @param {string} trueString  the String to return if <code>true</code>,
         * may be <code>null</code>
         * @param {string} falseString  the String to return if <code>false</code>,
         * may be <code>null</code>
         * @param {string} nullString  the String to return if <code>null</code>,
         * may be <code>null</code>
         * @return {string} one of the three input Strings
         */
        public static toString(bool?: any, trueString?: any, falseString?: any, nullString?: any): any {
            if (((typeof bool === 'boolean') || bool === null) && ((typeof trueString === 'string') || trueString === null) && ((typeof falseString === 'string') || falseString === null) && ((typeof nullString === 'string') || nullString === null)) {
                return <any>org.openprovenance.apache.commons.lang.BooleanUtils.toString$java_lang_Boolean$java_lang_String$java_lang_String$java_lang_String(bool, trueString, falseString, nullString);
            } else if (((typeof bool === 'boolean') || bool === null) && ((typeof trueString === 'string') || trueString === null) && ((typeof falseString === 'string') || falseString === null) && nullString === undefined) {
                return <any>org.openprovenance.apache.commons.lang.BooleanUtils.toString$boolean$java_lang_String$java_lang_String(bool, trueString, falseString);
            } else throw new Error('invalid overload');
        }

        public static toStringTrueFalse$boolean(bool: boolean): string {
            return BooleanUtils.toString$boolean$java_lang_String$java_lang_String(bool, "true", "false");
        }

        public static toStringOnOff$boolean(bool: boolean): string {
            return BooleanUtils.toString$boolean$java_lang_String$java_lang_String(bool, "on", "off");
        }

        public static toStringYesNo$boolean(bool: boolean): string {
            return BooleanUtils.toString$boolean$java_lang_String$java_lang_String(bool, "yes", "no");
        }

        public static toString$boolean$java_lang_String$java_lang_String(bool: boolean, trueString: string, falseString: string): string {
            return bool ? trueString : falseString;
        }

        public static xor$boolean_A(array: boolean[]): boolean {
            if (array == null){
                throw Object.defineProperty(new Error("The Array must not be null"), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.Object','java.lang.RuntimeException','java.lang.IllegalArgumentException','java.lang.Exception'] });
            } else if (array.length === 0){
                throw Object.defineProperty(new Error("Array is empty"), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.Object','java.lang.RuntimeException','java.lang.IllegalArgumentException','java.lang.Exception'] });
            }
            let trueCount: number = 0;
            for(let i: number = 0; i < array.length; i++) {{
                if (array[i]){
                    if (trueCount < 1){
                        trueCount++;
                    } else {
                        return false;
                    }
                }
            };}
            return trueCount === 1;
        }

        /**
         * <p>Performs an xor on a set of booleans.</p>
         * 
         * <pre>
         * BooleanUtils.xor(new boolean[] { true, true })   = false
         * BooleanUtils.xor(new boolean[] { false, false }) = false
         * BooleanUtils.xor(new boolean[] { true, false })  = true
         * </pre>
         * 
         * @param {boolean[]} array  an array of {@code boolean}s
         * @return {boolean} {@code true} if the xor is successful.
         * @throws IllegalArgumentException if {@code array} is {@code null}
         * @throws IllegalArgumentException if {@code array} is empty.
         */
        public static xor(array?: any): any {
            if (((array != null && array instanceof <any>Array && (array.length == 0 || array[0] == null ||(typeof array[0] === 'boolean'))) || array === null)) {
                return <any>org.openprovenance.apache.commons.lang.BooleanUtils.xor$boolean_A(array);
            } else if (((array != null && array instanceof <any>Array && (array.length == 0 || array[0] == null ||(typeof array[0] === 'boolean'))) || array === null)) {
                return <any>org.openprovenance.apache.commons.lang.BooleanUtils.xor$java_lang_Boolean_A(array);
            } else throw new Error('invalid overload');
        }

        public static xor$java_lang_Boolean_A(array: boolean[]): boolean {
            if (array == null){
                throw Object.defineProperty(new Error("The Array must not be null"), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.Object','java.lang.RuntimeException','java.lang.IllegalArgumentException','java.lang.Exception'] });
            } else if (array.length === 0){
                throw Object.defineProperty(new Error("Array is empty"), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.Object','java.lang.RuntimeException','java.lang.IllegalArgumentException','java.lang.Exception'] });
            }
            let primitive: boolean[] = null;
            try {
                primitive = org.openprovenance.apache.commons.lang.ArrayUtils.toPrimitive$java_lang_Boolean_A(array);
            } catch(ex) {
                throw Object.defineProperty(new Error("The array must not contain any null elements"), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.Object','java.lang.RuntimeException','java.lang.IllegalArgumentException','java.lang.Exception'] });
            }
            return BooleanUtils.xor$boolean_A(primitive) ? true : false;
        }
    }
    BooleanUtils["__class"] = "org.openprovenance.apache.commons.lang.BooleanUtils";

}

