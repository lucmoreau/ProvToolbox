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
                    var math;
                    (function (math) {
                        /**
                         * <p><code>NumberUtils</code> instances should NOT be constructed in standard programming.
                         * Instead, the class should be used as <code>NumberUtils.toInt("6");</code>.</p>
                         *
                         * <p>This constructor is public to permit tools that require a JavaBean instance
                         * to operate.</p>
                         * @class
                         * @author Apache Software Foundation
                         */
                        class NumberUtils {
                            constructor() {
                            }
                            static LONG_ZERO_$LI$() { if (NumberUtils.LONG_ZERO == null) {
                                NumberUtils.LONG_ZERO = new Number(0).valueOf();
                            } return NumberUtils.LONG_ZERO; }
                            static LONG_ONE_$LI$() { if (NumberUtils.LONG_ONE == null) {
                                NumberUtils.LONG_ONE = new Number(1).valueOf();
                            } return NumberUtils.LONG_ONE; }
                            static LONG_MINUS_ONE_$LI$() { if (NumberUtils.LONG_MINUS_ONE == null) {
                                NumberUtils.LONG_MINUS_ONE = new Number(-1).valueOf();
                            } return NumberUtils.LONG_MINUS_ONE; }
                            static INTEGER_ZERO_$LI$() { if (NumberUtils.INTEGER_ZERO == null) {
                                NumberUtils.INTEGER_ZERO = new Number(0).valueOf();
                            } return NumberUtils.INTEGER_ZERO; }
                            static INTEGER_ONE_$LI$() { if (NumberUtils.INTEGER_ONE == null) {
                                NumberUtils.INTEGER_ONE = new Number(1).valueOf();
                            } return NumberUtils.INTEGER_ONE; }
                            static INTEGER_MINUS_ONE_$LI$() { if (NumberUtils.INTEGER_MINUS_ONE == null) {
                                NumberUtils.INTEGER_MINUS_ONE = new Number(-1).valueOf();
                            } return NumberUtils.INTEGER_MINUS_ONE; }
                            static SHORT_ZERO_$LI$() { if (NumberUtils.SHORT_ZERO == null) {
                                NumberUtils.SHORT_ZERO = new Number((0 | 0));
                            } return NumberUtils.SHORT_ZERO; }
                            static SHORT_ONE_$LI$() { if (NumberUtils.SHORT_ONE == null) {
                                NumberUtils.SHORT_ONE = new Number((1 | 0));
                            } return NumberUtils.SHORT_ONE; }
                            static SHORT_MINUS_ONE_$LI$() { if (NumberUtils.SHORT_MINUS_ONE == null) {
                                NumberUtils.SHORT_MINUS_ONE = new Number((-1 | 0));
                            } return NumberUtils.SHORT_MINUS_ONE; }
                            static BYTE_ZERO_$LI$() { if (NumberUtils.BYTE_ZERO == null) {
                                NumberUtils.BYTE_ZERO = new Number((0 | 0));
                            } return NumberUtils.BYTE_ZERO; }
                            static BYTE_ONE_$LI$() { if (NumberUtils.BYTE_ONE == null) {
                                NumberUtils.BYTE_ONE = new Number((1 | 0));
                            } return NumberUtils.BYTE_ONE; }
                            static BYTE_MINUS_ONE_$LI$() { if (NumberUtils.BYTE_MINUS_ONE == null) {
                                NumberUtils.BYTE_MINUS_ONE = new Number((-1 | 0));
                            } return NumberUtils.BYTE_MINUS_ONE; }
                            static DOUBLE_ZERO_$LI$() { if (NumberUtils.DOUBLE_ZERO == null) {
                                NumberUtils.DOUBLE_ZERO = new Number(0.0).valueOf();
                            } return NumberUtils.DOUBLE_ZERO; }
                            static DOUBLE_ONE_$LI$() { if (NumberUtils.DOUBLE_ONE == null) {
                                NumberUtils.DOUBLE_ONE = new Number(1.0).valueOf();
                            } return NumberUtils.DOUBLE_ONE; }
                            static DOUBLE_MINUS_ONE_$LI$() { if (NumberUtils.DOUBLE_MINUS_ONE == null) {
                                NumberUtils.DOUBLE_MINUS_ONE = new Number(-1.0).valueOf();
                            } return NumberUtils.DOUBLE_MINUS_ONE; }
                            static FLOAT_ZERO_$LI$() { if (NumberUtils.FLOAT_ZERO == null) {
                                NumberUtils.FLOAT_ZERO = new Number(0.0).valueOf();
                            } return NumberUtils.FLOAT_ZERO; }
                            static FLOAT_ONE_$LI$() { if (NumberUtils.FLOAT_ONE == null) {
                                NumberUtils.FLOAT_ONE = new Number(1.0).valueOf();
                            } return NumberUtils.FLOAT_ONE; }
                            static FLOAT_MINUS_ONE_$LI$() { if (NumberUtils.FLOAT_MINUS_ONE == null) {
                                NumberUtils.FLOAT_MINUS_ONE = new Number(-1.0).valueOf();
                            } return NumberUtils.FLOAT_MINUS_ONE; }
                            static stringToInt$java_lang_String(str) {
                                return NumberUtils.toInt$java_lang_String(str);
                            }
                            static toInt$java_lang_String(str) {
                                return NumberUtils.toInt$java_lang_String$int(str, 0);
                            }
                            static stringToInt$java_lang_String$int(str, defaultValue) {
                                return NumberUtils.toInt$java_lang_String$int(str, defaultValue);
                            }
                            /**
                             * <p>Convert a <code>String</code> to an <code>int</code>, returning a
                             * default value if the conversion fails.</p>
                             *
                             * <p>If the string is <code>null</code>, the default value is returned.</p>
                             *
                             * <pre>
                             * NumberUtils.stringToInt(null, 1) = 1
                             * NumberUtils.stringToInt("", 1)   = 1
                             * NumberUtils.stringToInt("1", 0)  = 1
                             * </pre>
                             *
                             * @param {string} str  the string to convert, may be null
                             * @param {number} defaultValue  the default value
                             * @return {number} the int represented by the string, or the default if conversion fails
                             * @deprecated Use {@link #toInt(String, int)}
                             * This method will be removed in Commons Lang 3.0
                             */
                            static stringToInt(str, defaultValue) {
                                if (((typeof str === 'string') || str === null) && ((typeof defaultValue === 'number') || defaultValue === null)) {
                                    return org.openprovenance.apache.commons.lang.math.NumberUtils.stringToInt$java_lang_String$int(str, defaultValue);
                                }
                                else if (((typeof str === 'string') || str === null) && defaultValue === undefined) {
                                    return org.openprovenance.apache.commons.lang.math.NumberUtils.stringToInt$java_lang_String(str);
                                }
                                else
                                    throw new Error('invalid overload');
                            }
                            static toInt$java_lang_String$int(str, defaultValue) {
                                if (str == null) {
                                    return defaultValue;
                                }
                                try {
                                    return /* parseInt */ parseInt(str);
                                }
                                catch (nfe) {
                                    return defaultValue;
                                }
                            }
                            /**
                             * <p>Convert a <code>String</code> to an <code>int</code>, returning a
                             * default value if the conversion fails.</p>
                             *
                             * <p>If the string is <code>null</code>, the default value is returned.</p>
                             *
                             * <pre>
                             * NumberUtils.toInt(null, 1) = 1
                             * NumberUtils.toInt("", 1)   = 1
                             * NumberUtils.toInt("1", 0)  = 1
                             * </pre>
                             *
                             * @param {string} str  the string to convert, may be null
                             * @param {number} defaultValue  the default value
                             * @return {number} the int represented by the string, or the default if conversion fails
                             * @since 2.1
                             */
                            static toInt(str, defaultValue) {
                                if (((typeof str === 'string') || str === null) && ((typeof defaultValue === 'number') || defaultValue === null)) {
                                    return org.openprovenance.apache.commons.lang.math.NumberUtils.toInt$java_lang_String$int(str, defaultValue);
                                }
                                else if (((typeof str === 'string') || str === null) && defaultValue === undefined) {
                                    return org.openprovenance.apache.commons.lang.math.NumberUtils.toInt$java_lang_String(str);
                                }
                                else
                                    throw new Error('invalid overload');
                            }
                            static toLong$java_lang_String(str) {
                                return NumberUtils.toLong$java_lang_String$long(str, 0);
                            }
                            static toLong$java_lang_String$long(str, defaultValue) {
                                if (str == null) {
                                    return defaultValue;
                                }
                                try {
                                    return /* parseLong */ parseInt(str);
                                }
                                catch (nfe) {
                                    return defaultValue;
                                }
                            }
                            /**
                             * <p>Convert a <code>String</code> to a <code>long</code>, returning a
                             * default value if the conversion fails.</p>
                             *
                             * <p>If the string is <code>null</code>, the default value is returned.</p>
                             *
                             * <pre>
                             * NumberUtils.toLong(null, 1L) = 1L
                             * NumberUtils.toLong("", 1L)   = 1L
                             * NumberUtils.toLong("1", 0L)  = 1L
                             * </pre>
                             *
                             * @param {string} str  the string to convert, may be null
                             * @param {number} defaultValue  the default value
                             * @return {number} the long represented by the string, or the default if conversion fails
                             * @since 2.1
                             */
                            static toLong(str, defaultValue) {
                                if (((typeof str === 'string') || str === null) && ((typeof defaultValue === 'number') || defaultValue === null)) {
                                    return org.openprovenance.apache.commons.lang.math.NumberUtils.toLong$java_lang_String$long(str, defaultValue);
                                }
                                else if (((typeof str === 'string') || str === null) && defaultValue === undefined) {
                                    return org.openprovenance.apache.commons.lang.math.NumberUtils.toLong$java_lang_String(str);
                                }
                                else
                                    throw new Error('invalid overload');
                            }
                            static toFloat$java_lang_String(str) {
                                return NumberUtils.toFloat$java_lang_String$float(str, 0.0);
                            }
                            static toFloat$java_lang_String$float(str, defaultValue) {
                                if (str == null) {
                                    return defaultValue;
                                }
                                try {
                                    return /* parseFloat */ parseFloat(str);
                                }
                                catch (nfe) {
                                    return defaultValue;
                                }
                            }
                            /**
                             * <p>Convert a <code>String</code> to a <code>float</code>, returning a
                             * default value if the conversion fails.</p>
                             *
                             * <p>If the string <code>str</code> is <code>null</code>, the default
                             * value is returned.</p>
                             *
                             * <pre>
                             * NumberUtils.toFloat(null, 1.1f)   = 1.0f
                             * NumberUtils.toFloat("", 1.1f)     = 1.1f
                             * NumberUtils.toFloat("1.5", 0.0f)  = 1.5f
                             * </pre>
                             *
                             * @param {string} str the string to convert, may be <code>null</code>
                             * @param {number} defaultValue the default value
                             * @return {number} the float represented by the string, or defaultValue
                             * if conversion fails
                             * @since 2.1
                             */
                            static toFloat(str, defaultValue) {
                                if (((typeof str === 'string') || str === null) && ((typeof defaultValue === 'number') || defaultValue === null)) {
                                    return org.openprovenance.apache.commons.lang.math.NumberUtils.toFloat$java_lang_String$float(str, defaultValue);
                                }
                                else if (((typeof str === 'string') || str === null) && defaultValue === undefined) {
                                    return org.openprovenance.apache.commons.lang.math.NumberUtils.toFloat$java_lang_String(str);
                                }
                                else
                                    throw new Error('invalid overload');
                            }
                            static toDouble$java_lang_String(str) {
                                return NumberUtils.toDouble$java_lang_String$double(str, 0.0);
                            }
                            static toDouble$java_lang_String$double(str, defaultValue) {
                                if (str == null) {
                                    return defaultValue;
                                }
                                try {
                                    return /* parseDouble */ parseFloat(str);
                                }
                                catch (nfe) {
                                    return defaultValue;
                                }
                            }
                            /**
                             * <p>Convert a <code>String</code> to a <code>double</code>, returning a
                             * default value if the conversion fails.</p>
                             *
                             * <p>If the string <code>str</code> is <code>null</code>, the default
                             * value is returned.</p>
                             *
                             * <pre>
                             * NumberUtils.toDouble(null, 1.1d)   = 1.1d
                             * NumberUtils.toDouble("", 1.1d)     = 1.1d
                             * NumberUtils.toDouble("1.5", 0.0d)  = 1.5d
                             * </pre>
                             *
                             * @param {string} str the string to convert, may be <code>null</code>
                             * @param {number} defaultValue the default value
                             * @return {number} the double represented by the string, or defaultValue
                             * if conversion fails
                             * @since 2.1
                             */
                            static toDouble(str, defaultValue) {
                                if (((typeof str === 'string') || str === null) && ((typeof defaultValue === 'number') || defaultValue === null)) {
                                    return org.openprovenance.apache.commons.lang.math.NumberUtils.toDouble$java_lang_String$double(str, defaultValue);
                                }
                                else if (((typeof str === 'string') || str === null) && defaultValue === undefined) {
                                    return org.openprovenance.apache.commons.lang.math.NumberUtils.toDouble$java_lang_String(str);
                                }
                                else
                                    throw new Error('invalid overload');
                            }
                            static toByte$java_lang_String(str) {
                                return NumberUtils.toByte$java_lang_String$byte(str, (0 | 0));
                            }
                            static toByte$java_lang_String$byte(str, defaultValue) {
                                if (str == null) {
                                    return defaultValue;
                                }
                                try {
                                    return /* parseByte */ parseInt(str);
                                }
                                catch (nfe) {
                                    return defaultValue;
                                }
                            }
                            /**
                             * <p>Convert a <code>String</code> to a <code>byte</code>, returning a
                             * default value if the conversion fails.</p>
                             *
                             * <p>If the string is <code>null</code>, the default value is returned.</p>
                             *
                             * <pre>
                             * NumberUtils.toByte(null, 1) = 1
                             * NumberUtils.toByte("", 1)   = 1
                             * NumberUtils.toByte("1", 0)  = 1
                             * </pre>
                             *
                             * @param {string} str  the string to convert, may be null
                             * @param {number} defaultValue  the default value
                             * @return {number} the byte represented by the string, or the default if conversion fails
                             * @since 2.5
                             */
                            static toByte(str, defaultValue) {
                                if (((typeof str === 'string') || str === null) && ((typeof defaultValue === 'number') || defaultValue === null)) {
                                    return org.openprovenance.apache.commons.lang.math.NumberUtils.toByte$java_lang_String$byte(str, defaultValue);
                                }
                                else if (((typeof str === 'string') || str === null) && defaultValue === undefined) {
                                    return org.openprovenance.apache.commons.lang.math.NumberUtils.toByte$java_lang_String(str);
                                }
                                else
                                    throw new Error('invalid overload');
                            }
                            static toShort$java_lang_String(str) {
                                return NumberUtils.toShort$java_lang_String$short(str, (0 | 0));
                            }
                            static toShort$java_lang_String$short(str, defaultValue) {
                                if (str == null) {
                                    return defaultValue;
                                }
                                try {
                                    return /* parseShort */ parseInt(str);
                                }
                                catch (nfe) {
                                    return defaultValue;
                                }
                            }
                            /**
                             * <p>Convert a <code>String</code> to an <code>short</code>, returning a
                             * default value if the conversion fails.</p>
                             *
                             * <p>If the string is <code>null</code>, the default value is returned.</p>
                             *
                             * <pre>
                             * NumberUtils.toShort(null, 1) = 1
                             * NumberUtils.toShort("", 1)   = 1
                             * NumberUtils.toShort("1", 0)  = 1
                             * </pre>
                             *
                             * @param {string} str  the string to convert, may be null
                             * @param {number} defaultValue  the default value
                             * @return {number} the short represented by the string, or the default if conversion fails
                             * @since 2.5
                             */
                            static toShort(str, defaultValue) {
                                if (((typeof str === 'string') || str === null) && ((typeof defaultValue === 'number') || defaultValue === null)) {
                                    return org.openprovenance.apache.commons.lang.math.NumberUtils.toShort$java_lang_String$short(str, defaultValue);
                                }
                                else if (((typeof str === 'string') || str === null) && defaultValue === undefined) {
                                    return org.openprovenance.apache.commons.lang.math.NumberUtils.toShort$java_lang_String(str);
                                }
                                else
                                    throw new Error('invalid overload');
                            }
                            /**
                             * <p>Turns a string value into a java.lang.Number.</p>
                             *
                             * <p>First, the value is examined for a type qualifier on the end
                             * (<code>'f','F','d','D','l','L'</code>).  If it is found, it starts
                             * trying to create successively larger types from the type specified
                             * until one is found that can represent the value.</p>
                             *
                             * <p>If a type specifier is not found, it will check for a decimal point
                             * and then try successively larger types from <code>Integer</code> to
                             * <code>BigInteger</code> and from <code>Float</code> to
                             * <code>BigDecimal</code>.</p>
                             *
                             * <p>If the string starts with <code>0x</code> or <code>-0x</code>, it
                             * will be interpreted as a hexadecimal integer.  Values with leading
                             * <code>0</code>'s will not be interpreted as octal.</p>
                             *
                             * <p>Returns <code>null</code> if the string is <code>null</code>.</p>
                             *
                             * <p>This method does not trim the input string, i.e1., strings with leading
                             * or trailing spaces will generate NumberFormatExceptions.</p>
                             *
                             * @param {string} str  String containing a number, may be null
                             * @return {number} Number created from the string
                             * @throws NumberFormatException if the value cannot be converted
                             */
                            static createNumber(str) {
                                if (str == null) {
                                    return null;
                                }
                                if (org.openprovenance.apache.commons.lang.StringUtils.isBlank(str)) {
                                    throw Object.defineProperty(new Error("A blank string is not a valid number"), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.NumberFormatException', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.IllegalArgumentException', 'java.lang.Exception'] });
                                }
                                if ( /* startsWith */((str, searchString, position = 0) => str.substr(position, searchString.length) === searchString)(str, "--")) {
                                    return null;
                                }
                                if ( /* startsWith */((str, searchString, position = 0) => str.substr(position, searchString.length) === searchString)(str, "0x") || /* startsWith */ ((str, searchString, position = 0) => str.substr(position, searchString.length) === searchString)(str, "-0x")) {
                                    return NumberUtils.createInteger(str);
                                }
                                const lastChar = str.charAt(str.length - 1);
                                let mant;
                                let dec;
                                let exp;
                                const decPos = str.indexOf('.');
                                const expPos = str.indexOf('e') + str.indexOf('E') + 1;
                                if (decPos > -1) {
                                    if (expPos > -1) {
                                        if (expPos < decPos || expPos > str.length) {
                                            throw Object.defineProperty(new Error(str + " is not a valid number."), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.NumberFormatException', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.IllegalArgumentException', 'java.lang.Exception'] });
                                        }
                                        dec = str.substring(decPos + 1, expPos);
                                    }
                                    else {
                                        dec = str.substring(decPos + 1);
                                    }
                                    mant = str.substring(0, decPos);
                                }
                                else {
                                    if (expPos > -1) {
                                        if (expPos > str.length) {
                                            throw Object.defineProperty(new Error(str + " is not a valid number."), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.NumberFormatException', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.IllegalArgumentException', 'java.lang.Exception'] });
                                        }
                                        mant = str.substring(0, expPos);
                                    }
                                    else {
                                        mant = str;
                                    }
                                    dec = null;
                                }
                                if (!/\d/.test(lastChar[0]) && (c => c.charCodeAt == null ? c : c.charCodeAt(0))(lastChar) != '.'.charCodeAt(0)) {
                                    if (expPos > -1 && expPos < str.length - 1) {
                                        exp = str.substring(expPos + 1, str.length - 1);
                                    }
                                    else {
                                        exp = null;
                                    }
                                    const numeric = str.substring(0, str.length - 1);
                                    const allZeros = NumberUtils.isAllZeros(mant) && NumberUtils.isAllZeros(exp);
                                    switch ((lastChar).charCodeAt(0)) {
                                        case 108 /* 'l' */:
                                        case 76 /* 'L' */:
                                            if (dec == null && exp == null && ((c => c.charCodeAt == null ? c : c.charCodeAt(0))(numeric.charAt(0)) == '-'.charCodeAt(0) && NumberUtils.isDigits(numeric.substring(1)) || NumberUtils.isDigits(numeric))) {
                                                try {
                                                    return NumberUtils.createLong(numeric);
                                                }
                                                catch (nfe) {
                                                }
                                                return NumberUtils.createBigInteger(numeric);
                                            }
                                            throw Object.defineProperty(new Error(str + " is not a valid number."), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.NumberFormatException', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.IllegalArgumentException', 'java.lang.Exception'] });
                                        case 102 /* 'f' */:
                                        case 70 /* 'F' */:
                                            try {
                                                const f = NumberUtils.createFloat(numeric);
                                                if (!( /* isInfinite */((value) => Number.NEGATIVE_INFINITY === value || Number.POSITIVE_INFINITY === value)(f) || ( /* floatValue */f === 0.0 && !allZeros))) {
                                                    return f;
                                                }
                                            }
                                            catch (nfe) {
                                            }
                                        case 100 /* 'd' */:
                                        case 68 /* 'D' */:
                                            try {
                                                const d = NumberUtils.createDouble(numeric);
                                                if (!( /* isInfinite */((value) => Number.NEGATIVE_INFINITY === value || Number.POSITIVE_INFINITY === value)(d) || ( /* floatValue */d === 0.0 && !allZeros))) {
                                                    return d;
                                                }
                                            }
                                            catch (nfe) {
                                            }
                                            try {
                                                return NumberUtils.createBigDecimal(numeric);
                                            }
                                            catch (e) {
                                            }
                                        default:
                                            throw Object.defineProperty(new Error(str + " is not a valid number."), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.NumberFormatException', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.IllegalArgumentException', 'java.lang.Exception'] });
                                    }
                                }
                                else {
                                    if (expPos > -1 && expPos < str.length - 1) {
                                        exp = str.substring(expPos + 1, str.length);
                                    }
                                    else {
                                        exp = null;
                                    }
                                    if (dec == null && exp == null) {
                                        try {
                                            return NumberUtils.createInteger(str);
                                        }
                                        catch (nfe) {
                                        }
                                        try {
                                            return NumberUtils.createLong(str);
                                        }
                                        catch (nfe) {
                                        }
                                        return NumberUtils.createBigInteger(str);
                                    }
                                    else {
                                        const allZeros = NumberUtils.isAllZeros(mant) && NumberUtils.isAllZeros(exp);
                                        try {
                                            const f = NumberUtils.createFloat(str);
                                            if (!( /* isInfinite */((value) => Number.NEGATIVE_INFINITY === value || Number.POSITIVE_INFINITY === value)(f) || ( /* floatValue */f === 0.0 && !allZeros))) {
                                                return f;
                                            }
                                        }
                                        catch (nfe) {
                                        }
                                        try {
                                            const d = NumberUtils.createDouble(str);
                                            if (!( /* isInfinite */((value) => Number.NEGATIVE_INFINITY === value || Number.POSITIVE_INFINITY === value)(d) || ( /* doubleValue */d === 0.0 && !allZeros))) {
                                                return d;
                                            }
                                        }
                                        catch (nfe) {
                                        }
                                        return NumberUtils.createBigDecimal(str);
                                    }
                                }
                            }
                            /**
                             * <p>Utility method for {@link #createNumber(String)}.</p>
                             *
                             * <p>Returns <code>true</code> if s is <code>null</code>.</p>
                             *
                             * @param {string} str  the String to check
                             * @return {boolean} if it is all zeros or <code>null</code>
                             * @private
                             */
                            /*private*/ static isAllZeros(str) {
                                if (str == null) {
                                    return true;
                                }
                                for (let i = str.length - 1; i >= 0; i--) {
                                    {
                                        if ((c => c.charCodeAt == null ? c : c.charCodeAt(0))(str.charAt(i)) != '0'.charCodeAt(0)) {
                                            return false;
                                        }
                                    }
                                    ;
                                }
                                return str.length > 0;
                            }
                            /**
                             * <p>Convert a <code>String</code> to a <code>Float</code>.</p>
                             *
                             * <p>Returns <code>null</code> if the string is <code>null</code>.</p>
                             *
                             * @param {string} str  a <code>String</code> to convert, may be null
                             * @return {number} converted <code>Float</code>
                             * @throws NumberFormatException if the value cannot be converted
                             */
                            static createFloat(str) {
                                if (str == null) {
                                    return null;
                                }
                                return parseFloat(str);
                            }
                            /**
                             * <p>Convert a <code>String</code> to a <code>Double</code>.</p>
                             *
                             * <p>Returns <code>null</code> if the string is <code>null</code>.</p>
                             *
                             * @param {string} str  a <code>String</code> to convert, may be null
                             * @return {number} converted <code>Double</code>
                             * @throws NumberFormatException if the value cannot be converted
                             */
                            static createDouble(str) {
                                if (str == null) {
                                    return null;
                                }
                                return parseFloat(str);
                            }
                            /**
                             * <p>Convert a <code>String</code> to a <code>Integer</code>, handling
                             * hex and octal notations.</p>
                             *
                             * <p>Returns <code>null</code> if the string is <code>null</code>.</p>
                             *
                             * @param {string} str  a <code>String</code> to convert, may be null
                             * @return {number} converted <code>Integer</code>
                             * @throws NumberFormatException if the value cannot be converted
                             */
                            static createInteger(str) {
                                if (str == null) {
                                    return null;
                                }
                                return javaemul.internal.IntegerHelper.decode(str);
                            }
                            /**
                             * <p>Convert a <code>String</code> to a <code>Long</code>.</p>
                             *
                             * <p>Returns <code>null</code> if the string is <code>null</code>.</p>
                             *
                             * @param {string} str  a <code>String</code> to convert, may be null
                             * @return {number} converted <code>Long</code>
                             * @throws NumberFormatException if the value cannot be converted
                             */
                            static createLong(str) {
                                if (str == null) {
                                    return null;
                                }
                                return parseFloat(str);
                            }
                            /**
                             * <p>Convert a <code>String</code> to a <code>BigInteger</code>.</p>
                             *
                             * <p>Returns <code>null</code> if the string is <code>null</code>.</p>
                             *
                             * @param {string} str  a <code>String</code> to convert, may be null
                             * @return {java.math.BigInteger} converted <code>BigInteger</code>
                             * @throws NumberFormatException if the value cannot be converted
                             */
                            static createBigInteger(str) {
                                if (str == null) {
                                    return null;
                                }
                                return new java.math.BigInteger(str);
                            }
                            /**
                             * <p>Convert a <code>String</code> to a <code>BigDecimal</code>.</p>
                             *
                             * <p>Returns <code>null</code> if the string is <code>null</code>.</p>
                             *
                             * @param {string} str  a <code>String</code> to convert, may be null
                             * @return {java.math.BigDecimal} converted <code>BigDecimal</code>
                             * @throws NumberFormatException if the value cannot be converted
                             */
                            static createBigDecimal(str) {
                                if (str == null) {
                                    return null;
                                }
                                if (org.openprovenance.apache.commons.lang.StringUtils.isBlank(str)) {
                                    throw Object.defineProperty(new Error("A blank string is not a valid number"), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.NumberFormatException', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.IllegalArgumentException', 'java.lang.Exception'] });
                                }
                                return new java.math.BigDecimal(str);
                            }
                            static min$long_A(array) {
                                if (array == null) {
                                    throw Object.defineProperty(new Error("The Array must not be null"), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.IllegalArgumentException', 'java.lang.Exception'] });
                                }
                                else if (array.length === 0) {
                                    throw Object.defineProperty(new Error("Array cannot be empty."), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.IllegalArgumentException', 'java.lang.Exception'] });
                                }
                                let min = array[0];
                                for (let i = 1; i < array.length; i++) {
                                    {
                                        if (array[i] < min) {
                                            min = array[i];
                                        }
                                    }
                                    ;
                                }
                                return min;
                            }
                            static min$int_A(array) {
                                if (array == null) {
                                    throw Object.defineProperty(new Error("The Array must not be null"), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.IllegalArgumentException', 'java.lang.Exception'] });
                                }
                                else if (array.length === 0) {
                                    throw Object.defineProperty(new Error("Array cannot be empty."), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.IllegalArgumentException', 'java.lang.Exception'] });
                                }
                                let min = array[0];
                                for (let j = 1; j < array.length; j++) {
                                    {
                                        if (array[j] < min) {
                                            min = array[j];
                                        }
                                    }
                                    ;
                                }
                                return min;
                            }
                            static min$short_A(array) {
                                if (array == null) {
                                    throw Object.defineProperty(new Error("The Array must not be null"), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.IllegalArgumentException', 'java.lang.Exception'] });
                                }
                                else if (array.length === 0) {
                                    throw Object.defineProperty(new Error("Array cannot be empty."), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.IllegalArgumentException', 'java.lang.Exception'] });
                                }
                                let min = array[0];
                                for (let i = 1; i < array.length; i++) {
                                    {
                                        if (array[i] < min) {
                                            min = array[i];
                                        }
                                    }
                                    ;
                                }
                                return min;
                            }
                            static min$byte_A(array) {
                                if (array == null) {
                                    throw Object.defineProperty(new Error("The Array must not be null"), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.IllegalArgumentException', 'java.lang.Exception'] });
                                }
                                else if (array.length === 0) {
                                    throw Object.defineProperty(new Error("Array cannot be empty."), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.IllegalArgumentException', 'java.lang.Exception'] });
                                }
                                let min = array[0];
                                for (let i = 1; i < array.length; i++) {
                                    {
                                        if (array[i] < min) {
                                            min = array[i];
                                        }
                                    }
                                    ;
                                }
                                return min;
                            }
                            static min$double_A(array) {
                                if (array == null) {
                                    throw Object.defineProperty(new Error("The Array must not be null"), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.IllegalArgumentException', 'java.lang.Exception'] });
                                }
                                else if (array.length === 0) {
                                    throw Object.defineProperty(new Error("Array cannot be empty."), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.IllegalArgumentException', 'java.lang.Exception'] });
                                }
                                let min = array[0];
                                for (let i = 1; i < array.length; i++) {
                                    {
                                        if ( /* isNaN */isNaN(array[i])) {
                                            return NaN;
                                        }
                                        if (array[i] < min) {
                                            min = array[i];
                                        }
                                    }
                                    ;
                                }
                                return min;
                            }
                            static min$float_A(array) {
                                if (array == null) {
                                    throw Object.defineProperty(new Error("The Array must not be null"), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.IllegalArgumentException', 'java.lang.Exception'] });
                                }
                                else if (array.length === 0) {
                                    throw Object.defineProperty(new Error("Array cannot be empty."), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.IllegalArgumentException', 'java.lang.Exception'] });
                                }
                                let min = array[0];
                                for (let i = 1; i < array.length; i++) {
                                    {
                                        if ( /* isNaN */isNaN(array[i])) {
                                            return NaN;
                                        }
                                        if (array[i] < min) {
                                            min = array[i];
                                        }
                                    }
                                    ;
                                }
                                return min;
                            }
                            static max$long_A(array) {
                                if (array == null) {
                                    throw Object.defineProperty(new Error("The Array must not be null"), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.IllegalArgumentException', 'java.lang.Exception'] });
                                }
                                else if (array.length === 0) {
                                    throw Object.defineProperty(new Error("Array cannot be empty."), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.IllegalArgumentException', 'java.lang.Exception'] });
                                }
                                let max = array[0];
                                for (let j = 1; j < array.length; j++) {
                                    {
                                        if (array[j] > max) {
                                            max = array[j];
                                        }
                                    }
                                    ;
                                }
                                return max;
                            }
                            static max$int_A(array) {
                                if (array == null) {
                                    throw Object.defineProperty(new Error("The Array must not be null"), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.IllegalArgumentException', 'java.lang.Exception'] });
                                }
                                else if (array.length === 0) {
                                    throw Object.defineProperty(new Error("Array cannot be empty."), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.IllegalArgumentException', 'java.lang.Exception'] });
                                }
                                let max = array[0];
                                for (let j = 1; j < array.length; j++) {
                                    {
                                        if (array[j] > max) {
                                            max = array[j];
                                        }
                                    }
                                    ;
                                }
                                return max;
                            }
                            static max$short_A(array) {
                                if (array == null) {
                                    throw Object.defineProperty(new Error("The Array must not be null"), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.IllegalArgumentException', 'java.lang.Exception'] });
                                }
                                else if (array.length === 0) {
                                    throw Object.defineProperty(new Error("Array cannot be empty."), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.IllegalArgumentException', 'java.lang.Exception'] });
                                }
                                let max = array[0];
                                for (let i = 1; i < array.length; i++) {
                                    {
                                        if (array[i] > max) {
                                            max = array[i];
                                        }
                                    }
                                    ;
                                }
                                return max;
                            }
                            static max$byte_A(array) {
                                if (array == null) {
                                    throw Object.defineProperty(new Error("The Array must not be null"), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.IllegalArgumentException', 'java.lang.Exception'] });
                                }
                                else if (array.length === 0) {
                                    throw Object.defineProperty(new Error("Array cannot be empty."), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.IllegalArgumentException', 'java.lang.Exception'] });
                                }
                                let max = array[0];
                                for (let i = 1; i < array.length; i++) {
                                    {
                                        if (array[i] > max) {
                                            max = array[i];
                                        }
                                    }
                                    ;
                                }
                                return max;
                            }
                            static max$double_A(array) {
                                if (array == null) {
                                    throw Object.defineProperty(new Error("The Array must not be null"), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.IllegalArgumentException', 'java.lang.Exception'] });
                                }
                                else if (array.length === 0) {
                                    throw Object.defineProperty(new Error("Array cannot be empty."), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.IllegalArgumentException', 'java.lang.Exception'] });
                                }
                                let max = array[0];
                                for (let j = 1; j < array.length; j++) {
                                    {
                                        if ( /* isNaN */isNaN(array[j])) {
                                            return NaN;
                                        }
                                        if (array[j] > max) {
                                            max = array[j];
                                        }
                                    }
                                    ;
                                }
                                return max;
                            }
                            static max$float_A(array) {
                                if (array == null) {
                                    throw Object.defineProperty(new Error("The Array must not be null"), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.IllegalArgumentException', 'java.lang.Exception'] });
                                }
                                else if (array.length === 0) {
                                    throw Object.defineProperty(new Error("Array cannot be empty."), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.IllegalArgumentException', 'java.lang.Exception'] });
                                }
                                let max = array[0];
                                for (let j = 1; j < array.length; j++) {
                                    {
                                        if ( /* isNaN */isNaN(array[j])) {
                                            return NaN;
                                        }
                                        if (array[j] > max) {
                                            max = array[j];
                                        }
                                    }
                                    ;
                                }
                                return max;
                            }
                            static min$long$long$long(a, b, c) {
                                if (b < a) {
                                    a = b;
                                }
                                if (c < a) {
                                    a = c;
                                }
                                return a;
                            }
                            static min$int$int$int(a, b, c) {
                                if (b < a) {
                                    a = b;
                                }
                                if (c < a) {
                                    a = c;
                                }
                                return a;
                            }
                            static min$short$short$short(a, b, c) {
                                if (b < a) {
                                    a = b;
                                }
                                if (c < a) {
                                    a = c;
                                }
                                return a;
                            }
                            static min$byte$byte$byte(a, b, c) {
                                if (b < a) {
                                    a = b;
                                }
                                if (c < a) {
                                    a = c;
                                }
                                return a;
                            }
                            /**
                             * <p>Gets the minimum of three <code>byte</code> values.</p>
                             *
                             * @param {number} a  value 1
                             * @param {number} b  value 2
                             * @param {number} c  value 3
                             * @return  {number} the smallest of the values
                             */
                            static min(a, b, c) {
                                if (((typeof a === 'number') || a === null) && ((typeof b === 'number') || b === null) && ((typeof c === 'number') || c === null)) {
                                    return org.openprovenance.apache.commons.lang.math.NumberUtils.min$byte$byte$byte(a, b, c);
                                }
                                else if (((typeof a === 'number') || a === null) && ((typeof b === 'number') || b === null) && ((typeof c === 'number') || c === null)) {
                                    return org.openprovenance.apache.commons.lang.math.NumberUtils.min$short$short$short(a, b, c);
                                }
                                else if (((typeof a === 'number') || a === null) && ((typeof b === 'number') || b === null) && ((typeof c === 'number') || c === null)) {
                                    return org.openprovenance.apache.commons.lang.math.NumberUtils.min$int$int$int(a, b, c);
                                }
                                else if (((typeof a === 'number') || a === null) && ((typeof b === 'number') || b === null) && ((typeof c === 'number') || c === null)) {
                                    return org.openprovenance.apache.commons.lang.math.NumberUtils.min$long$long$long(a, b, c);
                                }
                                else if (((typeof a === 'number') || a === null) && ((typeof b === 'number') || b === null) && ((typeof c === 'number') || c === null)) {
                                    return org.openprovenance.apache.commons.lang.math.NumberUtils.min$float$float$float(a, b, c);
                                }
                                else if (((typeof a === 'number') || a === null) && ((typeof b === 'number') || b === null) && ((typeof c === 'number') || c === null)) {
                                    return org.openprovenance.apache.commons.lang.math.NumberUtils.min$double$double$double(a, b, c);
                                }
                                else if (((a != null && a instanceof Array && (a.length == 0 || a[0] == null || (typeof a[0] === 'number'))) || a === null) && b === undefined && c === undefined) {
                                    return org.openprovenance.apache.commons.lang.math.NumberUtils.min$long_A(a);
                                }
                                else if (((a != null && a instanceof Array && (a.length == 0 || a[0] == null || (typeof a[0] === 'number'))) || a === null) && b === undefined && c === undefined) {
                                    return org.openprovenance.apache.commons.lang.math.NumberUtils.min$int_A(a);
                                }
                                else if (((a != null && a instanceof Array && (a.length == 0 || a[0] == null || (typeof a[0] === 'number'))) || a === null) && b === undefined && c === undefined) {
                                    return org.openprovenance.apache.commons.lang.math.NumberUtils.min$short_A(a);
                                }
                                else if (((a != null && a instanceof Array && (a.length == 0 || a[0] == null || (typeof a[0] === 'number'))) || a === null) && b === undefined && c === undefined) {
                                    return org.openprovenance.apache.commons.lang.math.NumberUtils.min$byte_A(a);
                                }
                                else if (((a != null && a instanceof Array && (a.length == 0 || a[0] == null || (typeof a[0] === 'number'))) || a === null) && b === undefined && c === undefined) {
                                    return org.openprovenance.apache.commons.lang.math.NumberUtils.min$double_A(a);
                                }
                                else if (((a != null && a instanceof Array && (a.length == 0 || a[0] == null || (typeof a[0] === 'number'))) || a === null) && b === undefined && c === undefined) {
                                    return org.openprovenance.apache.commons.lang.math.NumberUtils.min$float_A(a);
                                }
                                else
                                    throw new Error('invalid overload');
                            }
                            static min$double$double$double(a, b, c) {
                                return Math.min(Math.min(a, b), c);
                            }
                            static min$float$float$float(a, b, c) {
                                return Math.min(Math.min(a, b), c);
                            }
                            static max$long$long$long(a, b, c) {
                                if (b > a) {
                                    a = b;
                                }
                                if (c > a) {
                                    a = c;
                                }
                                return a;
                            }
                            static max$int$int$int(a, b, c) {
                                if (b > a) {
                                    a = b;
                                }
                                if (c > a) {
                                    a = c;
                                }
                                return a;
                            }
                            static max$short$short$short(a, b, c) {
                                if (b > a) {
                                    a = b;
                                }
                                if (c > a) {
                                    a = c;
                                }
                                return a;
                            }
                            static max$byte$byte$byte(a, b, c) {
                                if (b > a) {
                                    a = b;
                                }
                                if (c > a) {
                                    a = c;
                                }
                                return a;
                            }
                            /**
                             * <p>Gets the maximum of three <code>byte</code> values.</p>
                             *
                             * @param {number} a  value 1
                             * @param {number} b  value 2
                             * @param {number} c  value 3
                             * @return  {number} the largest of the values
                             */
                            static max(a, b, c) {
                                if (((typeof a === 'number') || a === null) && ((typeof b === 'number') || b === null) && ((typeof c === 'number') || c === null)) {
                                    return org.openprovenance.apache.commons.lang.math.NumberUtils.max$byte$byte$byte(a, b, c);
                                }
                                else if (((typeof a === 'number') || a === null) && ((typeof b === 'number') || b === null) && ((typeof c === 'number') || c === null)) {
                                    return org.openprovenance.apache.commons.lang.math.NumberUtils.max$short$short$short(a, b, c);
                                }
                                else if (((typeof a === 'number') || a === null) && ((typeof b === 'number') || b === null) && ((typeof c === 'number') || c === null)) {
                                    return org.openprovenance.apache.commons.lang.math.NumberUtils.max$int$int$int(a, b, c);
                                }
                                else if (((typeof a === 'number') || a === null) && ((typeof b === 'number') || b === null) && ((typeof c === 'number') || c === null)) {
                                    return org.openprovenance.apache.commons.lang.math.NumberUtils.max$long$long$long(a, b, c);
                                }
                                else if (((typeof a === 'number') || a === null) && ((typeof b === 'number') || b === null) && ((typeof c === 'number') || c === null)) {
                                    return org.openprovenance.apache.commons.lang.math.NumberUtils.max$float$float$float(a, b, c);
                                }
                                else if (((typeof a === 'number') || a === null) && ((typeof b === 'number') || b === null) && ((typeof c === 'number') || c === null)) {
                                    return org.openprovenance.apache.commons.lang.math.NumberUtils.max$double$double$double(a, b, c);
                                }
                                else if (((a != null && a instanceof Array && (a.length == 0 || a[0] == null || (typeof a[0] === 'number'))) || a === null) && b === undefined && c === undefined) {
                                    return org.openprovenance.apache.commons.lang.math.NumberUtils.max$long_A(a);
                                }
                                else if (((a != null && a instanceof Array && (a.length == 0 || a[0] == null || (typeof a[0] === 'number'))) || a === null) && b === undefined && c === undefined) {
                                    return org.openprovenance.apache.commons.lang.math.NumberUtils.max$int_A(a);
                                }
                                else if (((a != null && a instanceof Array && (a.length == 0 || a[0] == null || (typeof a[0] === 'number'))) || a === null) && b === undefined && c === undefined) {
                                    return org.openprovenance.apache.commons.lang.math.NumberUtils.max$short_A(a);
                                }
                                else if (((a != null && a instanceof Array && (a.length == 0 || a[0] == null || (typeof a[0] === 'number'))) || a === null) && b === undefined && c === undefined) {
                                    return org.openprovenance.apache.commons.lang.math.NumberUtils.max$byte_A(a);
                                }
                                else if (((a != null && a instanceof Array && (a.length == 0 || a[0] == null || (typeof a[0] === 'number'))) || a === null) && b === undefined && c === undefined) {
                                    return org.openprovenance.apache.commons.lang.math.NumberUtils.max$double_A(a);
                                }
                                else if (((a != null && a instanceof Array && (a.length == 0 || a[0] == null || (typeof a[0] === 'number'))) || a === null) && b === undefined && c === undefined) {
                                    return org.openprovenance.apache.commons.lang.math.NumberUtils.max$float_A(a);
                                }
                                else
                                    throw new Error('invalid overload');
                            }
                            static max$double$double$double(a, b, c) {
                                return Math.max(Math.max(a, b), c);
                            }
                            static max$float$float$float(a, b, c) {
                                return Math.max(Math.max(a, b), c);
                            }
                            static compare$double$double(lhs, rhs) {
                                if (lhs < rhs) {
                                    return -1;
                                }
                                if (lhs > rhs) {
                                    return +1;
                                }
                                const lhsBits = ((f) => { let buf = new ArrayBuffer(4); (new Float32Array(buf))[0] = f; return (new Uint32Array(buf))[0]; })(Math.fround(lhs));
                                const rhsBits = ((f) => { let buf = new ArrayBuffer(4); (new Float32Array(buf))[0] = f; return (new Uint32Array(buf))[0]; })(Math.fround(rhs));
                                if (lhsBits === rhsBits) {
                                    return 0;
                                }
                                if (lhsBits < rhsBits) {
                                    return -1;
                                }
                                else {
                                    return +1;
                                }
                            }
                            static compare$float$float(lhs, rhs) {
                                if (lhs < rhs) {
                                    return -1;
                                }
                                if (lhs > rhs) {
                                    return +1;
                                }
                                const lhsBits = ((f) => { let buf = new ArrayBuffer(4); (new Float32Array(buf))[0] = f; return (new Uint32Array(buf))[0]; })(lhs);
                                const rhsBits = ((f) => { let buf = new ArrayBuffer(4); (new Float32Array(buf))[0] = f; return (new Uint32Array(buf))[0]; })(rhs);
                                if (lhsBits === rhsBits) {
                                    return 0;
                                }
                                if (lhsBits < rhsBits) {
                                    return -1;
                                }
                                else {
                                    return +1;
                                }
                            }
                            /**
                             * <p>Compares two floats for order.</p>
                             *
                             * <p>This method is more comprehensive than the standard Java greater than,
                             * less than and equals operators.</p>
                             * <ul>
                             * <li>It returns <code>-1</code> if the first value is less than the second.
                             * <li>It returns <code>+1</code> if the first value is greater than the second.
                             * <li>It returns <code>0</code> if the values are equal.
                             * </ul>
                             *
                             * <p> The ordering is as follows, largest to smallest:
                             * <ul>
                             * <li>NaN
                             * <li>Positive infinity
                             * <li>Maximum float
                             * <li>Normal positive numbers
                             * <li>+0.0
                             * <li>-0.0
                             * <li>Normal negative numbers
                             * <li>Minimum float (<code>-Float.MAX_VALUE</code>)
                             * <li>Negative infinity
                             * </ul>
                             *
                             * <p>Comparing <code>NaN</code> with <code>NaN</code> will return
                             * <code>0</code>.</p>
                             *
                             * @param {number} lhs  the first <code>float</code>
                             * @param {number} rhs  the second <code>float</code>
                             * @return {number} <code>-1</code> if lhs is less, <code>+1</code> if greater,
                             * <code>0</code> if equal to rhs
                             */
                            static compare(lhs, rhs) {
                                if (((typeof lhs === 'number') || lhs === null) && ((typeof rhs === 'number') || rhs === null)) {
                                    return org.openprovenance.apache.commons.lang.math.NumberUtils.compare$float$float(lhs, rhs);
                                }
                                else if (((typeof lhs === 'number') || lhs === null) && ((typeof rhs === 'number') || rhs === null)) {
                                    return org.openprovenance.apache.commons.lang.math.NumberUtils.compare$double$double(lhs, rhs);
                                }
                                else
                                    throw new Error('invalid overload');
                            }
                            /**
                             * <p>Checks whether the <code>String</code> contains only
                             * digit characters.</p>
                             *
                             * <p><code>Null</code> and empty String will return
                             * <code>false</code>.</p>
                             *
                             * @param {string} str  the <code>String</code> to check
                             * @return {boolean} <code>true</code> if str contains only unicode numeric
                             */
                            static isDigits(str) {
                                if (org.openprovenance.apache.commons.lang.StringUtils.isEmpty(str)) {
                                    return false;
                                }
                                for (let i = 0; i < str.length; i++) {
                                    {
                                        if (!/\d/.test(str.charAt(i)[0])) {
                                            return false;
                                        }
                                    }
                                    ;
                                }
                                return true;
                            }
                            /**
                             * <p>Checks whether the String a valid Java number.</p>
                             *
                             * <p>Valid numbers include hexadecimal marked with the <code>0x</code>
                             * qualifier, scientific notation and numbers marked with a type
                             * qualifier (e1.g. 123L).</p>
                             *
                             * <p><code>Null</code> and empty String will return
                             * <code>false</code>.</p>
                             *
                             * @param {string} str  the <code>String</code> to check
                             * @return {boolean} <code>true</code> if the string is a correctly formatted number
                             */
                            static isNumber(str) {
                                if (org.openprovenance.apache.commons.lang.StringUtils.isEmpty(str)) {
                                    return false;
                                }
                                const chars = (str).split('');
                                let sz = chars.length;
                                let hasExp = false;
                                let hasDecPoint = false;
                                let allowSigns = false;
                                let foundDigit = false;
                                const start = ((c => c.charCodeAt == null ? c : c.charCodeAt(0))(chars[0]) == '-'.charCodeAt(0)) ? 1 : 0;
                                if (sz > start + 1) {
                                    if ((c => c.charCodeAt == null ? c : c.charCodeAt(0))(chars[start]) == '0'.charCodeAt(0) && (c => c.charCodeAt == null ? c : c.charCodeAt(0))(chars[start + 1]) == 'x'.charCodeAt(0)) {
                                        let i = start + 2;
                                        if (i === sz) {
                                            return false;
                                        }
                                        for (; i < chars.length; i++) {
                                            {
                                                if (((c => c.charCodeAt == null ? c : c.charCodeAt(0))(chars[i]) < '0'.charCodeAt(0) || (c => c.charCodeAt == null ? c : c.charCodeAt(0))(chars[i]) > '9'.charCodeAt(0)) && ((c => c.charCodeAt == null ? c : c.charCodeAt(0))(chars[i]) < 'a'.charCodeAt(0) || (c => c.charCodeAt == null ? c : c.charCodeAt(0))(chars[i]) > 'f'.charCodeAt(0)) && ((c => c.charCodeAt == null ? c : c.charCodeAt(0))(chars[i]) < 'A'.charCodeAt(0) || (c => c.charCodeAt == null ? c : c.charCodeAt(0))(chars[i]) > 'F'.charCodeAt(0))) {
                                                    return false;
                                                }
                                            }
                                            ;
                                        }
                                        return true;
                                    }
                                }
                                sz--;
                                let i = start;
                                while ((i < sz || (i < sz + 1 && allowSigns && !foundDigit))) {
                                    {
                                        if ((c => c.charCodeAt == null ? c : c.charCodeAt(0))(chars[i]) >= '0'.charCodeAt(0) && (c => c.charCodeAt == null ? c : c.charCodeAt(0))(chars[i]) <= '9'.charCodeAt(0)) {
                                            foundDigit = true;
                                            allowSigns = false;
                                        }
                                        else if ((c => c.charCodeAt == null ? c : c.charCodeAt(0))(chars[i]) == '.'.charCodeAt(0)) {
                                            if (hasDecPoint || hasExp) {
                                                return false;
                                            }
                                            hasDecPoint = true;
                                        }
                                        else if ((c => c.charCodeAt == null ? c : c.charCodeAt(0))(chars[i]) == 'e'.charCodeAt(0) || (c => c.charCodeAt == null ? c : c.charCodeAt(0))(chars[i]) == 'E'.charCodeAt(0)) {
                                            if (hasExp) {
                                                return false;
                                            }
                                            if (!foundDigit) {
                                                return false;
                                            }
                                            hasExp = true;
                                            allowSigns = true;
                                        }
                                        else if ((c => c.charCodeAt == null ? c : c.charCodeAt(0))(chars[i]) == '+'.charCodeAt(0) || (c => c.charCodeAt == null ? c : c.charCodeAt(0))(chars[i]) == '-'.charCodeAt(0)) {
                                            if (!allowSigns) {
                                                return false;
                                            }
                                            allowSigns = false;
                                            foundDigit = false;
                                        }
                                        else {
                                            return false;
                                        }
                                        i++;
                                    }
                                }
                                ;
                                if (i < chars.length) {
                                    if ((c => c.charCodeAt == null ? c : c.charCodeAt(0))(chars[i]) >= '0'.charCodeAt(0) && (c => c.charCodeAt == null ? c : c.charCodeAt(0))(chars[i]) <= '9'.charCodeAt(0)) {
                                        return true;
                                    }
                                    if ((c => c.charCodeAt == null ? c : c.charCodeAt(0))(chars[i]) == 'e'.charCodeAt(0) || (c => c.charCodeAt == null ? c : c.charCodeAt(0))(chars[i]) == 'E'.charCodeAt(0)) {
                                        return false;
                                    }
                                    if ((c => c.charCodeAt == null ? c : c.charCodeAt(0))(chars[i]) == '.'.charCodeAt(0)) {
                                        if (hasDecPoint || hasExp) {
                                            return false;
                                        }
                                        return foundDigit;
                                    }
                                    if (!allowSigns && ((c => c.charCodeAt == null ? c : c.charCodeAt(0))(chars[i]) == 'd'.charCodeAt(0) || (c => c.charCodeAt == null ? c : c.charCodeAt(0))(chars[i]) == 'D'.charCodeAt(0) || (c => c.charCodeAt == null ? c : c.charCodeAt(0))(chars[i]) == 'f'.charCodeAt(0) || (c => c.charCodeAt == null ? c : c.charCodeAt(0))(chars[i]) == 'F'.charCodeAt(0))) {
                                        return foundDigit;
                                    }
                                    if ((c => c.charCodeAt == null ? c : c.charCodeAt(0))(chars[i]) == 'l'.charCodeAt(0) || (c => c.charCodeAt == null ? c : c.charCodeAt(0))(chars[i]) == 'L'.charCodeAt(0)) {
                                        return foundDigit && !hasExp;
                                    }
                                    return false;
                                }
                                return !allowSigns && foundDigit;
                            }
                        }
                        math.NumberUtils = NumberUtils;
                        NumberUtils["__class"] = "org.openprovenance.apache.commons.lang.math.NumberUtils";
                    })(math = lang.math || (lang.math = {}));
                })(lang = commons.lang || (commons.lang = {}));
            })(commons = apache.commons || (apache.commons = {}));
        })(apache = openprovenance.apache || (openprovenance.apache = {}));
    })(openprovenance = org.openprovenance || (org.openprovenance = {}));
})(org || (org = {}));
