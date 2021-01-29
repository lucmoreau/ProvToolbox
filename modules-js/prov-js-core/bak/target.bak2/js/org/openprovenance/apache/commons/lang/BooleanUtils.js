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
                    /**
                     * <p><code>BooleanUtils</code> instances should NOT be constructed in standard programming.
                     * Instead, the class should be used as <code>BooleanUtils.toBooleanObject(true);</code>.</p>
                     *
                     * <p>This constructor is public to permit tools that require a JavaBean instance
                     * to operate.</p>
                     * @class
                     * @author Apache Software Foundation
                     */
                    class BooleanUtils {
                        constructor() {
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
                        static negate(bool) {
                            if (bool == null) {
                                return null;
                            }
                            return ( /* booleanValue */bool ? false : true);
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
                        static isTrue(bool) {
                            if (bool == null) {
                                return false;
                            }
                            return /* booleanValue */ bool ? true : false;
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
                        static isNotTrue(bool) {
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
                        static isFalse(bool) {
                            if (bool == null) {
                                return false;
                            }
                            return /* booleanValue */ bool ? false : true;
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
                        static isNotFalse(bool) {
                            return !BooleanUtils.isFalse(bool);
                        }
                        static toBooleanObject$boolean(bool) {
                            return bool ? true : false;
                        }
                        static toBoolean$java_lang_Boolean(bool) {
                            if (bool == null) {
                                return false;
                            }
                            return /* booleanValue */ bool ? true : false;
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
                        static toBooleanDefaultIfNull(bool, valueIfNull) {
                            if (bool == null) {
                                return valueIfNull;
                            }
                            return /* booleanValue */ bool ? true : false;
                        }
                        static toBoolean$int(value) {
                            return value === 0 ? false : true;
                        }
                        static toBooleanObject$int(value) {
                            return value === 0 ? false : true;
                        }
                        static toBooleanObject$java_lang_Integer(value) {
                            if (value == null) {
                                return null;
                            }
                            return /* intValue */ (value | 0) === 0 ? false : true;
                        }
                        static toBoolean$int$int$int(value, trueValue, falseValue) {
                            if (value === trueValue) {
                                return true;
                            }
                            else if (value === falseValue) {
                                return false;
                            }
                            throw Object.defineProperty(new Error("The Integer did not match either specified value"), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.IllegalArgumentException', 'java.lang.Exception'] });
                        }
                        static toBoolean$java_lang_Integer$java_lang_Integer$java_lang_Integer(value, trueValue, falseValue) {
                            if (value == null) {
                                if (trueValue == null) {
                                    return true;
                                }
                                else if (falseValue == null) {
                                    return false;
                                }
                            }
                            else if (value === trueValue) {
                                return true;
                            }
                            else if (value === falseValue) {
                                return false;
                            }
                            throw Object.defineProperty(new Error("The Integer did not match either specified value"), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.IllegalArgumentException', 'java.lang.Exception'] });
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
                        static toBoolean(value, trueValue, falseValue) {
                            if (((typeof value === 'number') || value === null) && ((typeof trueValue === 'number') || trueValue === null) && ((typeof falseValue === 'number') || falseValue === null)) {
                                return org.openprovenance.apache.commons.lang.BooleanUtils.toBoolean$java_lang_Integer$java_lang_Integer$java_lang_Integer(value, trueValue, falseValue);
                            }
                            else if (((typeof value === 'string') || value === null) && ((typeof trueValue === 'string') || trueValue === null) && ((typeof falseValue === 'string') || falseValue === null)) {
                                return org.openprovenance.apache.commons.lang.BooleanUtils.toBoolean$java_lang_String$java_lang_String$java_lang_String(value, trueValue, falseValue);
                            }
                            else if (((typeof value === 'number') || value === null) && ((typeof trueValue === 'number') || trueValue === null) && ((typeof falseValue === 'number') || falseValue === null)) {
                                return org.openprovenance.apache.commons.lang.BooleanUtils.toBoolean$int$int$int(value, trueValue, falseValue);
                            }
                            else if (((typeof value === 'boolean') || value === null) && trueValue === undefined && falseValue === undefined) {
                                return org.openprovenance.apache.commons.lang.BooleanUtils.toBoolean$java_lang_Boolean(value);
                            }
                            else if (((typeof value === 'string') || value === null) && trueValue === undefined && falseValue === undefined) {
                                return org.openprovenance.apache.commons.lang.BooleanUtils.toBoolean$java_lang_String(value);
                            }
                            else if (((typeof value === 'number') || value === null) && trueValue === undefined && falseValue === undefined) {
                                return org.openprovenance.apache.commons.lang.BooleanUtils.toBoolean$int(value);
                            }
                            else
                                throw new Error('invalid overload');
                        }
                        static toBooleanObject$int$int$int$int(value, trueValue, falseValue, nullValue) {
                            if (value === trueValue) {
                                return true;
                            }
                            else if (value === falseValue) {
                                return false;
                            }
                            else if (value === nullValue) {
                                return null;
                            }
                            throw Object.defineProperty(new Error("The Integer did not match any specified value"), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.IllegalArgumentException', 'java.lang.Exception'] });
                        }
                        static toBooleanObject$java_lang_Integer$java_lang_Integer$java_lang_Integer$java_lang_Integer(value, trueValue, falseValue, nullValue) {
                            if (value == null) {
                                if (trueValue == null) {
                                    return true;
                                }
                                else if (falseValue == null) {
                                    return false;
                                }
                                else if (nullValue == null) {
                                    return null;
                                }
                            }
                            else if (value === trueValue) {
                                return true;
                            }
                            else if (value === falseValue) {
                                return false;
                            }
                            else if (value === nullValue) {
                                return null;
                            }
                            throw Object.defineProperty(new Error("The Integer did not match any specified value"), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.IllegalArgumentException', 'java.lang.Exception'] });
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
                        static toBooleanObject(value, trueValue, falseValue, nullValue) {
                            if (((typeof value === 'number') || value === null) && ((typeof trueValue === 'number') || trueValue === null) && ((typeof falseValue === 'number') || falseValue === null) && ((typeof nullValue === 'number') || nullValue === null)) {
                                return org.openprovenance.apache.commons.lang.BooleanUtils.toBooleanObject$java_lang_Integer$java_lang_Integer$java_lang_Integer$java_lang_Integer(value, trueValue, falseValue, nullValue);
                            }
                            else if (((typeof value === 'string') || value === null) && ((typeof trueValue === 'string') || trueValue === null) && ((typeof falseValue === 'string') || falseValue === null) && ((typeof nullValue === 'string') || nullValue === null)) {
                                return org.openprovenance.apache.commons.lang.BooleanUtils.toBooleanObject$java_lang_String$java_lang_String$java_lang_String$java_lang_String(value, trueValue, falseValue, nullValue);
                            }
                            else if (((typeof value === 'number') || value === null) && ((typeof trueValue === 'number') || trueValue === null) && ((typeof falseValue === 'number') || falseValue === null) && ((typeof nullValue === 'number') || nullValue === null)) {
                                return org.openprovenance.apache.commons.lang.BooleanUtils.toBooleanObject$int$int$int$int(value, trueValue, falseValue, nullValue);
                            }
                            else if (((typeof value === 'number') || value === null) && trueValue === undefined && falseValue === undefined && nullValue === undefined) {
                                return org.openprovenance.apache.commons.lang.BooleanUtils.toBooleanObject$java_lang_Integer(value);
                            }
                            else if (((typeof value === 'string') || value === null) && trueValue === undefined && falseValue === undefined && nullValue === undefined) {
                                return org.openprovenance.apache.commons.lang.BooleanUtils.toBooleanObject$java_lang_String(value);
                            }
                            else if (((typeof value === 'boolean') || value === null) && trueValue === undefined && falseValue === undefined && nullValue === undefined) {
                                return org.openprovenance.apache.commons.lang.BooleanUtils.toBooleanObject$boolean(value);
                            }
                            else if (((typeof value === 'number') || value === null) && trueValue === undefined && falseValue === undefined && nullValue === undefined) {
                                return org.openprovenance.apache.commons.lang.BooleanUtils.toBooleanObject$int(value);
                            }
                            else
                                throw new Error('invalid overload');
                        }
                        static toInteger$boolean(bool) {
                            return bool ? 1 : 0;
                        }
                        static toIntegerObject$boolean(bool) {
                            return bool ? org.openprovenance.apache.commons.lang.math.NumberUtils.INTEGER_ONE_$LI$() : org.openprovenance.apache.commons.lang.math.NumberUtils.INTEGER_ZERO_$LI$();
                        }
                        static toIntegerObject$java_lang_Boolean(bool) {
                            if (bool == null) {
                                return null;
                            }
                            return /* booleanValue */ bool ? org.openprovenance.apache.commons.lang.math.NumberUtils.INTEGER_ONE_$LI$() : org.openprovenance.apache.commons.lang.math.NumberUtils.INTEGER_ZERO_$LI$();
                        }
                        static toInteger$boolean$int$int(bool, trueValue, falseValue) {
                            return bool ? trueValue : falseValue;
                        }
                        static toInteger$java_lang_Boolean$int$int$int(bool, trueValue, falseValue, nullValue) {
                            if (bool == null) {
                                return nullValue;
                            }
                            return /* booleanValue */ bool ? trueValue : falseValue;
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
                        static toInteger(bool, trueValue, falseValue, nullValue) {
                            if (((typeof bool === 'boolean') || bool === null) && ((typeof trueValue === 'number') || trueValue === null) && ((typeof falseValue === 'number') || falseValue === null) && ((typeof nullValue === 'number') || nullValue === null)) {
                                return org.openprovenance.apache.commons.lang.BooleanUtils.toInteger$java_lang_Boolean$int$int$int(bool, trueValue, falseValue, nullValue);
                            }
                            else if (((typeof bool === 'boolean') || bool === null) && ((typeof trueValue === 'number') || trueValue === null) && ((typeof falseValue === 'number') || falseValue === null) && nullValue === undefined) {
                                return org.openprovenance.apache.commons.lang.BooleanUtils.toInteger$boolean$int$int(bool, trueValue, falseValue);
                            }
                            else if (((typeof bool === 'boolean') || bool === null) && trueValue === undefined && falseValue === undefined && nullValue === undefined) {
                                return org.openprovenance.apache.commons.lang.BooleanUtils.toInteger$boolean(bool);
                            }
                            else
                                throw new Error('invalid overload');
                        }
                        static toIntegerObject$boolean$java_lang_Integer$java_lang_Integer(bool, trueValue, falseValue) {
                            return bool ? trueValue : falseValue;
                        }
                        static toIntegerObject$java_lang_Boolean$java_lang_Integer$java_lang_Integer$java_lang_Integer(bool, trueValue, falseValue, nullValue) {
                            if (bool == null) {
                                return nullValue;
                            }
                            return /* booleanValue */ bool ? trueValue : falseValue;
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
                        static toIntegerObject(bool, trueValue, falseValue, nullValue) {
                            if (((typeof bool === 'boolean') || bool === null) && ((typeof trueValue === 'number') || trueValue === null) && ((typeof falseValue === 'number') || falseValue === null) && ((typeof nullValue === 'number') || nullValue === null)) {
                                return org.openprovenance.apache.commons.lang.BooleanUtils.toIntegerObject$java_lang_Boolean$java_lang_Integer$java_lang_Integer$java_lang_Integer(bool, trueValue, falseValue, nullValue);
                            }
                            else if (((typeof bool === 'boolean') || bool === null) && ((typeof trueValue === 'number') || trueValue === null) && ((typeof falseValue === 'number') || falseValue === null) && nullValue === undefined) {
                                return org.openprovenance.apache.commons.lang.BooleanUtils.toIntegerObject$boolean$java_lang_Integer$java_lang_Integer(bool, trueValue, falseValue);
                            }
                            else if (((typeof bool === 'boolean') || bool === null) && trueValue === undefined && falseValue === undefined && nullValue === undefined) {
                                return org.openprovenance.apache.commons.lang.BooleanUtils.toIntegerObject$java_lang_Boolean(bool);
                            }
                            else if (((typeof bool === 'boolean') || bool === null) && trueValue === undefined && falseValue === undefined && nullValue === undefined) {
                                return org.openprovenance.apache.commons.lang.BooleanUtils.toIntegerObject$boolean(bool);
                            }
                            else
                                throw new Error('invalid overload');
                        }
                        static toBooleanObject$java_lang_String(str) {
                            if (str === "true") {
                                return true;
                            }
                            if (str == null) {
                                return null;
                            }
                            switch ((str.length)) {
                                case 1:
                                    {
                                        const ch0 = str.charAt(0);
                                        if (((c => c.charCodeAt == null ? c : c.charCodeAt(0))(ch0) == 'y'.charCodeAt(0) || (c => c.charCodeAt == null ? c : c.charCodeAt(0))(ch0) == 'Y'.charCodeAt(0)) || ((c => c.charCodeAt == null ? c : c.charCodeAt(0))(ch0) == 't'.charCodeAt(0) || (c => c.charCodeAt == null ? c : c.charCodeAt(0))(ch0) == 'T'.charCodeAt(0))) {
                                            return true;
                                        }
                                        if (((c => c.charCodeAt == null ? c : c.charCodeAt(0))(ch0) == 'n'.charCodeAt(0) || (c => c.charCodeAt == null ? c : c.charCodeAt(0))(ch0) == 'N'.charCodeAt(0)) || ((c => c.charCodeAt == null ? c : c.charCodeAt(0))(ch0) == 'f'.charCodeAt(0) || (c => c.charCodeAt == null ? c : c.charCodeAt(0))(ch0) == 'F'.charCodeAt(0))) {
                                            return false;
                                        }
                                        break;
                                    }
                                    ;
                                case 2:
                                    {
                                        const ch0 = str.charAt(0);
                                        const ch1 = str.charAt(1);
                                        if (((c => c.charCodeAt == null ? c : c.charCodeAt(0))(ch0) == 'o'.charCodeAt(0) || (c => c.charCodeAt == null ? c : c.charCodeAt(0))(ch0) == 'O'.charCodeAt(0)) && ((c => c.charCodeAt == null ? c : c.charCodeAt(0))(ch1) == 'n'.charCodeAt(0) || (c => c.charCodeAt == null ? c : c.charCodeAt(0))(ch1) == 'N'.charCodeAt(0))) {
                                            return true;
                                        }
                                        if (((c => c.charCodeAt == null ? c : c.charCodeAt(0))(ch0) == 'n'.charCodeAt(0) || (c => c.charCodeAt == null ? c : c.charCodeAt(0))(ch0) == 'N'.charCodeAt(0)) && ((c => c.charCodeAt == null ? c : c.charCodeAt(0))(ch1) == 'o'.charCodeAt(0) || (c => c.charCodeAt == null ? c : c.charCodeAt(0))(ch1) == 'O'.charCodeAt(0))) {
                                            return false;
                                        }
                                        break;
                                    }
                                    ;
                                case 3:
                                    {
                                        const ch0 = str.charAt(0);
                                        const ch1 = str.charAt(1);
                                        const ch2 = str.charAt(2);
                                        if (((c => c.charCodeAt == null ? c : c.charCodeAt(0))(ch0) == 'y'.charCodeAt(0) || (c => c.charCodeAt == null ? c : c.charCodeAt(0))(ch0) == 'Y'.charCodeAt(0)) && ((c => c.charCodeAt == null ? c : c.charCodeAt(0))(ch1) == 'e'.charCodeAt(0) || (c => c.charCodeAt == null ? c : c.charCodeAt(0))(ch1) == 'E'.charCodeAt(0)) && ((c => c.charCodeAt == null ? c : c.charCodeAt(0))(ch2) == 's'.charCodeAt(0) || (c => c.charCodeAt == null ? c : c.charCodeAt(0))(ch2) == 'S'.charCodeAt(0))) {
                                            return true;
                                        }
                                        if (((c => c.charCodeAt == null ? c : c.charCodeAt(0))(ch0) == 'o'.charCodeAt(0) || (c => c.charCodeAt == null ? c : c.charCodeAt(0))(ch0) == 'O'.charCodeAt(0)) && ((c => c.charCodeAt == null ? c : c.charCodeAt(0))(ch1) == 'f'.charCodeAt(0) || (c => c.charCodeAt == null ? c : c.charCodeAt(0))(ch1) == 'F'.charCodeAt(0)) && ((c => c.charCodeAt == null ? c : c.charCodeAt(0))(ch2) == 'f'.charCodeAt(0) || (c => c.charCodeAt == null ? c : c.charCodeAt(0))(ch2) == 'F'.charCodeAt(0))) {
                                            return false;
                                        }
                                        break;
                                    }
                                    ;
                                case 4:
                                    {
                                        const ch0 = str.charAt(0);
                                        const ch1 = str.charAt(1);
                                        const ch2 = str.charAt(2);
                                        const ch3 = str.charAt(3);
                                        if (((c => c.charCodeAt == null ? c : c.charCodeAt(0))(ch0) == 't'.charCodeAt(0) || (c => c.charCodeAt == null ? c : c.charCodeAt(0))(ch0) == 'T'.charCodeAt(0)) && ((c => c.charCodeAt == null ? c : c.charCodeAt(0))(ch1) == 'r'.charCodeAt(0) || (c => c.charCodeAt == null ? c : c.charCodeAt(0))(ch1) == 'R'.charCodeAt(0)) && ((c => c.charCodeAt == null ? c : c.charCodeAt(0))(ch2) == 'u'.charCodeAt(0) || (c => c.charCodeAt == null ? c : c.charCodeAt(0))(ch2) == 'U'.charCodeAt(0)) && ((c => c.charCodeAt == null ? c : c.charCodeAt(0))(ch3) == 'e'.charCodeAt(0) || (c => c.charCodeAt == null ? c : c.charCodeAt(0))(ch3) == 'E'.charCodeAt(0))) {
                                            return true;
                                        }
                                        break;
                                    }
                                    ;
                                case 5:
                                    {
                                        const ch0 = str.charAt(0);
                                        const ch1 = str.charAt(1);
                                        const ch2 = str.charAt(2);
                                        const ch3 = str.charAt(3);
                                        const ch4 = str.charAt(4);
                                        if (((c => c.charCodeAt == null ? c : c.charCodeAt(0))(ch0) == 'f'.charCodeAt(0) || (c => c.charCodeAt == null ? c : c.charCodeAt(0))(ch0) == 'F'.charCodeAt(0)) && ((c => c.charCodeAt == null ? c : c.charCodeAt(0))(ch1) == 'a'.charCodeAt(0) || (c => c.charCodeAt == null ? c : c.charCodeAt(0))(ch1) == 'A'.charCodeAt(0)) && ((c => c.charCodeAt == null ? c : c.charCodeAt(0))(ch2) == 'l'.charCodeAt(0) || (c => c.charCodeAt == null ? c : c.charCodeAt(0))(ch2) == 'L'.charCodeAt(0)) && ((c => c.charCodeAt == null ? c : c.charCodeAt(0))(ch3) == 's'.charCodeAt(0) || (c => c.charCodeAt == null ? c : c.charCodeAt(0))(ch3) == 'S'.charCodeAt(0)) && ((c => c.charCodeAt == null ? c : c.charCodeAt(0))(ch4) == 'e'.charCodeAt(0) || (c => c.charCodeAt == null ? c : c.charCodeAt(0))(ch4) == 'E'.charCodeAt(0))) {
                                            return false;
                                        }
                                        break;
                                    }
                                    ;
                            }
                            return null;
                        }
                        static toBooleanObject$java_lang_String$java_lang_String$java_lang_String$java_lang_String(str, trueString, falseString, nullString) {
                            if (str == null) {
                                if (trueString == null) {
                                    return true;
                                }
                                else if (falseString == null) {
                                    return false;
                                }
                                else if (nullString == null) {
                                    return null;
                                }
                            }
                            else if (str === trueString) {
                                return true;
                            }
                            else if (str === falseString) {
                                return false;
                            }
                            else if (str === nullString) {
                                return null;
                            }
                            throw Object.defineProperty(new Error("The String did not match any specified value"), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.IllegalArgumentException', 'java.lang.Exception'] });
                        }
                        static toBoolean$java_lang_String(str) {
                            return BooleanUtils.toBoolean$java_lang_Boolean(BooleanUtils.toBooleanObject$java_lang_String(str));
                        }
                        static toBoolean$java_lang_String$java_lang_String$java_lang_String(str, trueString, falseString) {
                            if (str == null) {
                                if (trueString == null) {
                                    return true;
                                }
                                else if (falseString == null) {
                                    return false;
                                }
                            }
                            else if (str === trueString) {
                                return true;
                            }
                            else if (str === falseString) {
                                return false;
                            }
                            throw Object.defineProperty(new Error("The String did not match either specified value"), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.IllegalArgumentException', 'java.lang.Exception'] });
                        }
                        static toStringTrueFalse$java_lang_Boolean(bool) {
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
                        static toStringTrueFalse(bool) {
                            if (((typeof bool === 'boolean') || bool === null)) {
                                return org.openprovenance.apache.commons.lang.BooleanUtils.toStringTrueFalse$java_lang_Boolean(bool);
                            }
                            else if (((typeof bool === 'boolean') || bool === null)) {
                                return org.openprovenance.apache.commons.lang.BooleanUtils.toStringTrueFalse$boolean(bool);
                            }
                            else
                                throw new Error('invalid overload');
                        }
                        static toStringOnOff$java_lang_Boolean(bool) {
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
                        static toStringOnOff(bool) {
                            if (((typeof bool === 'boolean') || bool === null)) {
                                return org.openprovenance.apache.commons.lang.BooleanUtils.toStringOnOff$java_lang_Boolean(bool);
                            }
                            else if (((typeof bool === 'boolean') || bool === null)) {
                                return org.openprovenance.apache.commons.lang.BooleanUtils.toStringOnOff$boolean(bool);
                            }
                            else
                                throw new Error('invalid overload');
                        }
                        static toStringYesNo$java_lang_Boolean(bool) {
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
                        static toStringYesNo(bool) {
                            if (((typeof bool === 'boolean') || bool === null)) {
                                return org.openprovenance.apache.commons.lang.BooleanUtils.toStringYesNo$java_lang_Boolean(bool);
                            }
                            else if (((typeof bool === 'boolean') || bool === null)) {
                                return org.openprovenance.apache.commons.lang.BooleanUtils.toStringYesNo$boolean(bool);
                            }
                            else
                                throw new Error('invalid overload');
                        }
                        static toString$java_lang_Boolean$java_lang_String$java_lang_String$java_lang_String(bool, trueString, falseString, nullString) {
                            if (bool == null) {
                                return nullString;
                            }
                            return /* booleanValue */ bool ? trueString : falseString;
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
                        static toString(bool, trueString, falseString, nullString) {
                            if (((typeof bool === 'boolean') || bool === null) && ((typeof trueString === 'string') || trueString === null) && ((typeof falseString === 'string') || falseString === null) && ((typeof nullString === 'string') || nullString === null)) {
                                return org.openprovenance.apache.commons.lang.BooleanUtils.toString$java_lang_Boolean$java_lang_String$java_lang_String$java_lang_String(bool, trueString, falseString, nullString);
                            }
                            else if (((typeof bool === 'boolean') || bool === null) && ((typeof trueString === 'string') || trueString === null) && ((typeof falseString === 'string') || falseString === null) && nullString === undefined) {
                                return org.openprovenance.apache.commons.lang.BooleanUtils.toString$boolean$java_lang_String$java_lang_String(bool, trueString, falseString);
                            }
                            else
                                throw new Error('invalid overload');
                        }
                        static toStringTrueFalse$boolean(bool) {
                            return BooleanUtils.toString$boolean$java_lang_String$java_lang_String(bool, "true", "false");
                        }
                        static toStringOnOff$boolean(bool) {
                            return BooleanUtils.toString$boolean$java_lang_String$java_lang_String(bool, "on", "off");
                        }
                        static toStringYesNo$boolean(bool) {
                            return BooleanUtils.toString$boolean$java_lang_String$java_lang_String(bool, "yes", "no");
                        }
                        static toString$boolean$java_lang_String$java_lang_String(bool, trueString, falseString) {
                            return bool ? trueString : falseString;
                        }
                        static xor$boolean_A(array) {
                            if (array == null) {
                                throw Object.defineProperty(new Error("The Array must not be null"), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.IllegalArgumentException', 'java.lang.Exception'] });
                            }
                            else if (array.length === 0) {
                                throw Object.defineProperty(new Error("Array is empty"), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.IllegalArgumentException', 'java.lang.Exception'] });
                            }
                            let trueCount = 0;
                            for (let i = 0; i < array.length; i++) {
                                {
                                    if (array[i]) {
                                        if (trueCount < 1) {
                                            trueCount++;
                                        }
                                        else {
                                            return false;
                                        }
                                    }
                                }
                                ;
                            }
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
                        static xor(array) {
                            if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'boolean'))) || array === null)) {
                                return org.openprovenance.apache.commons.lang.BooleanUtils.xor$boolean_A(array);
                            }
                            else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (typeof array[0] === 'boolean'))) || array === null)) {
                                return org.openprovenance.apache.commons.lang.BooleanUtils.xor$java_lang_Boolean_A(array);
                            }
                            else
                                throw new Error('invalid overload');
                        }
                        static xor$java_lang_Boolean_A(array) {
                            if (array == null) {
                                throw Object.defineProperty(new Error("The Array must not be null"), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.IllegalArgumentException', 'java.lang.Exception'] });
                            }
                            else if (array.length === 0) {
                                throw Object.defineProperty(new Error("Array is empty"), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.IllegalArgumentException', 'java.lang.Exception'] });
                            }
                            let primitive = null;
                            try {
                                primitive = org.openprovenance.apache.commons.lang.ArrayUtils.toPrimitive$java_lang_Boolean_A(array);
                            }
                            catch (ex) {
                                throw Object.defineProperty(new Error("The array must not contain any null elements"), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.IllegalArgumentException', 'java.lang.Exception'] });
                            }
                            return BooleanUtils.xor$boolean_A(primitive) ? true : false;
                        }
                    }
                    lang.BooleanUtils = BooleanUtils;
                    BooleanUtils["__class"] = "org.openprovenance.apache.commons.lang.BooleanUtils";
                })(lang = commons.lang || (commons.lang = {}));
            })(commons = apache.commons || (apache.commons = {}));
        })(apache = openprovenance.apache || (openprovenance.apache = {}));
    })(openprovenance = org.openprovenance || (org.openprovenance = {}));
})(org || (org = {}));
