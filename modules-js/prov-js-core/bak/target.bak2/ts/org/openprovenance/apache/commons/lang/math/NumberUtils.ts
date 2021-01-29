/* Generated from Java with JSweet 3.0.0 - http://www.jsweet.org */
namespace org.openprovenance.apache.commons.lang.math {
    /**
     * <p><code>NumberUtils</code> instances should NOT be constructed in standard programming.
     * Instead, the class should be used as <code>NumberUtils.toInt("6");</code>.</p>
     * 
     * <p>This constructor is public to permit tools that require a JavaBean instance
     * to operate.</p>
     * @class
     * @author Apache Software Foundation
     */
    export class NumberUtils {
        /**
         * Reusable Long constant for zero.
         */
        public static LONG_ZERO: number; public static LONG_ZERO_$LI$(): number { if (NumberUtils.LONG_ZERO == null) { NumberUtils.LONG_ZERO = new Number(0).valueOf(); }  return NumberUtils.LONG_ZERO; }

        /**
         * Reusable Long constant for one.
         */
        public static LONG_ONE: number; public static LONG_ONE_$LI$(): number { if (NumberUtils.LONG_ONE == null) { NumberUtils.LONG_ONE = new Number(1).valueOf(); }  return NumberUtils.LONG_ONE; }

        /**
         * Reusable Long constant for minus one.
         */
        public static LONG_MINUS_ONE: number; public static LONG_MINUS_ONE_$LI$(): number { if (NumberUtils.LONG_MINUS_ONE == null) { NumberUtils.LONG_MINUS_ONE = new Number(-1).valueOf(); }  return NumberUtils.LONG_MINUS_ONE; }

        /**
         * Reusable Integer constant for zero.
         */
        public static INTEGER_ZERO: number; public static INTEGER_ZERO_$LI$(): number { if (NumberUtils.INTEGER_ZERO == null) { NumberUtils.INTEGER_ZERO = new Number(0).valueOf(); }  return NumberUtils.INTEGER_ZERO; }

        /**
         * Reusable Integer constant for one.
         */
        public static INTEGER_ONE: number; public static INTEGER_ONE_$LI$(): number { if (NumberUtils.INTEGER_ONE == null) { NumberUtils.INTEGER_ONE = new Number(1).valueOf(); }  return NumberUtils.INTEGER_ONE; }

        /**
         * Reusable Integer constant for minus one.
         */
        public static INTEGER_MINUS_ONE: number; public static INTEGER_MINUS_ONE_$LI$(): number { if (NumberUtils.INTEGER_MINUS_ONE == null) { NumberUtils.INTEGER_MINUS_ONE = new Number(-1).valueOf(); }  return NumberUtils.INTEGER_MINUS_ONE; }

        /**
         * Reusable Short constant for zero.
         */
        public static SHORT_ZERO: number; public static SHORT_ZERO_$LI$(): number { if (NumberUtils.SHORT_ZERO == null) { NumberUtils.SHORT_ZERO = <number>new Number((<number>0|0)); }  return NumberUtils.SHORT_ZERO; }

        /**
         * Reusable Short constant for one.
         */
        public static SHORT_ONE: number; public static SHORT_ONE_$LI$(): number { if (NumberUtils.SHORT_ONE == null) { NumberUtils.SHORT_ONE = <number>new Number((<number>1|0)); }  return NumberUtils.SHORT_ONE; }

        /**
         * Reusable Short constant for minus one.
         */
        public static SHORT_MINUS_ONE: number; public static SHORT_MINUS_ONE_$LI$(): number { if (NumberUtils.SHORT_MINUS_ONE == null) { NumberUtils.SHORT_MINUS_ONE = <number>new Number((<number>-1|0)); }  return NumberUtils.SHORT_MINUS_ONE; }

        /**
         * Reusable Byte constant for zero.
         */
        public static BYTE_ZERO: number; public static BYTE_ZERO_$LI$(): number { if (NumberUtils.BYTE_ZERO == null) { NumberUtils.BYTE_ZERO = <number>new Number((<number>0|0)); }  return NumberUtils.BYTE_ZERO; }

        /**
         * Reusable Byte constant for one.
         */
        public static BYTE_ONE: number; public static BYTE_ONE_$LI$(): number { if (NumberUtils.BYTE_ONE == null) { NumberUtils.BYTE_ONE = <number>new Number((<number>1|0)); }  return NumberUtils.BYTE_ONE; }

        /**
         * Reusable Byte constant for minus one.
         */
        public static BYTE_MINUS_ONE: number; public static BYTE_MINUS_ONE_$LI$(): number { if (NumberUtils.BYTE_MINUS_ONE == null) { NumberUtils.BYTE_MINUS_ONE = <number>new Number((<number>-1|0)); }  return NumberUtils.BYTE_MINUS_ONE; }

        /**
         * Reusable Double constant for zero.
         */
        public static DOUBLE_ZERO: number; public static DOUBLE_ZERO_$LI$(): number { if (NumberUtils.DOUBLE_ZERO == null) { NumberUtils.DOUBLE_ZERO = new Number(0.0).valueOf(); }  return NumberUtils.DOUBLE_ZERO; }

        /**
         * Reusable Double constant for one.
         */
        public static DOUBLE_ONE: number; public static DOUBLE_ONE_$LI$(): number { if (NumberUtils.DOUBLE_ONE == null) { NumberUtils.DOUBLE_ONE = new Number(1.0).valueOf(); }  return NumberUtils.DOUBLE_ONE; }

        /**
         * Reusable Double constant for minus one.
         */
        public static DOUBLE_MINUS_ONE: number; public static DOUBLE_MINUS_ONE_$LI$(): number { if (NumberUtils.DOUBLE_MINUS_ONE == null) { NumberUtils.DOUBLE_MINUS_ONE = new Number(-1.0).valueOf(); }  return NumberUtils.DOUBLE_MINUS_ONE; }

        /**
         * Reusable Float constant for zero.
         */
        public static FLOAT_ZERO: number; public static FLOAT_ZERO_$LI$(): number { if (NumberUtils.FLOAT_ZERO == null) { NumberUtils.FLOAT_ZERO = new Number(0.0).valueOf(); }  return NumberUtils.FLOAT_ZERO; }

        /**
         * Reusable Float constant for one.
         */
        public static FLOAT_ONE: number; public static FLOAT_ONE_$LI$(): number { if (NumberUtils.FLOAT_ONE == null) { NumberUtils.FLOAT_ONE = new Number(1.0).valueOf(); }  return NumberUtils.FLOAT_ONE; }

        /**
         * Reusable Float constant for minus one.
         */
        public static FLOAT_MINUS_ONE: number; public static FLOAT_MINUS_ONE_$LI$(): number { if (NumberUtils.FLOAT_MINUS_ONE == null) { NumberUtils.FLOAT_MINUS_ONE = new Number(-1.0).valueOf(); }  return NumberUtils.FLOAT_MINUS_ONE; }

        public constructor() {
        }

        public static stringToInt$java_lang_String(str: string): number {
            return NumberUtils.toInt$java_lang_String(str);
        }

        public static toInt$java_lang_String(str: string): number {
            return NumberUtils.toInt$java_lang_String$int(str, 0);
        }

        public static stringToInt$java_lang_String$int(str: string, defaultValue: number): number {
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
        public static stringToInt(str?: any, defaultValue?: any): any {
            if (((typeof str === 'string') || str === null) && ((typeof defaultValue === 'number') || defaultValue === null)) {
                return <any>org.openprovenance.apache.commons.lang.math.NumberUtils.stringToInt$java_lang_String$int(str, defaultValue);
            } else if (((typeof str === 'string') || str === null) && defaultValue === undefined) {
                return <any>org.openprovenance.apache.commons.lang.math.NumberUtils.stringToInt$java_lang_String(str);
            } else throw new Error('invalid overload');
        }

        public static toInt$java_lang_String$int(str: string, defaultValue: number): number {
            if (str == null){
                return defaultValue;
            }
            try {
                return /* parseInt */parseInt(str);
            } catch(nfe) {
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
        public static toInt(str?: any, defaultValue?: any): any {
            if (((typeof str === 'string') || str === null) && ((typeof defaultValue === 'number') || defaultValue === null)) {
                return <any>org.openprovenance.apache.commons.lang.math.NumberUtils.toInt$java_lang_String$int(str, defaultValue);
            } else if (((typeof str === 'string') || str === null) && defaultValue === undefined) {
                return <any>org.openprovenance.apache.commons.lang.math.NumberUtils.toInt$java_lang_String(str);
            } else throw new Error('invalid overload');
        }

        public static toLong$java_lang_String(str: string): number {
            return NumberUtils.toLong$java_lang_String$long(str, 0);
        }

        public static toLong$java_lang_String$long(str: string, defaultValue: number): number {
            if (str == null){
                return defaultValue;
            }
            try {
                return /* parseLong */parseInt(str);
            } catch(nfe) {
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
        public static toLong(str?: any, defaultValue?: any): any {
            if (((typeof str === 'string') || str === null) && ((typeof defaultValue === 'number') || defaultValue === null)) {
                return <any>org.openprovenance.apache.commons.lang.math.NumberUtils.toLong$java_lang_String$long(str, defaultValue);
            } else if (((typeof str === 'string') || str === null) && defaultValue === undefined) {
                return <any>org.openprovenance.apache.commons.lang.math.NumberUtils.toLong$java_lang_String(str);
            } else throw new Error('invalid overload');
        }

        public static toFloat$java_lang_String(str: string): number {
            return NumberUtils.toFloat$java_lang_String$float(str, 0.0);
        }

        public static toFloat$java_lang_String$float(str: string, defaultValue: number): number {
            if (str == null){
                return defaultValue;
            }
            try {
                return /* parseFloat */parseFloat(str);
            } catch(nfe) {
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
        public static toFloat(str?: any, defaultValue?: any): any {
            if (((typeof str === 'string') || str === null) && ((typeof defaultValue === 'number') || defaultValue === null)) {
                return <any>org.openprovenance.apache.commons.lang.math.NumberUtils.toFloat$java_lang_String$float(str, defaultValue);
            } else if (((typeof str === 'string') || str === null) && defaultValue === undefined) {
                return <any>org.openprovenance.apache.commons.lang.math.NumberUtils.toFloat$java_lang_String(str);
            } else throw new Error('invalid overload');
        }

        public static toDouble$java_lang_String(str: string): number {
            return NumberUtils.toDouble$java_lang_String$double(str, 0.0);
        }

        public static toDouble$java_lang_String$double(str: string, defaultValue: number): number {
            if (str == null){
                return defaultValue;
            }
            try {
                return /* parseDouble */parseFloat(str);
            } catch(nfe) {
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
        public static toDouble(str?: any, defaultValue?: any): any {
            if (((typeof str === 'string') || str === null) && ((typeof defaultValue === 'number') || defaultValue === null)) {
                return <any>org.openprovenance.apache.commons.lang.math.NumberUtils.toDouble$java_lang_String$double(str, defaultValue);
            } else if (((typeof str === 'string') || str === null) && defaultValue === undefined) {
                return <any>org.openprovenance.apache.commons.lang.math.NumberUtils.toDouble$java_lang_String(str);
            } else throw new Error('invalid overload');
        }

        public static toByte$java_lang_String(str: string): number {
            return NumberUtils.toByte$java_lang_String$byte(str, (<number>0|0));
        }

        public static toByte$java_lang_String$byte(str: string, defaultValue: number): number {
            if (str == null){
                return defaultValue;
            }
            try {
                return /* parseByte */parseInt(str);
            } catch(nfe) {
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
        public static toByte(str?: any, defaultValue?: any): any {
            if (((typeof str === 'string') || str === null) && ((typeof defaultValue === 'number') || defaultValue === null)) {
                return <any>org.openprovenance.apache.commons.lang.math.NumberUtils.toByte$java_lang_String$byte(str, defaultValue);
            } else if (((typeof str === 'string') || str === null) && defaultValue === undefined) {
                return <any>org.openprovenance.apache.commons.lang.math.NumberUtils.toByte$java_lang_String(str);
            } else throw new Error('invalid overload');
        }

        public static toShort$java_lang_String(str: string): number {
            return NumberUtils.toShort$java_lang_String$short(str, (<number>0|0));
        }

        public static toShort$java_lang_String$short(str: string, defaultValue: number): number {
            if (str == null){
                return defaultValue;
            }
            try {
                return /* parseShort */parseInt(str);
            } catch(nfe) {
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
        public static toShort(str?: any, defaultValue?: any): any {
            if (((typeof str === 'string') || str === null) && ((typeof defaultValue === 'number') || defaultValue === null)) {
                return <any>org.openprovenance.apache.commons.lang.math.NumberUtils.toShort$java_lang_String$short(str, defaultValue);
            } else if (((typeof str === 'string') || str === null) && defaultValue === undefined) {
                return <any>org.openprovenance.apache.commons.lang.math.NumberUtils.toShort$java_lang_String(str);
            } else throw new Error('invalid overload');
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
        public static createNumber(str: string): number {
            if (str == null){
                return null;
            }
            if (org.openprovenance.apache.commons.lang.StringUtils.isBlank(str)){
                throw Object.defineProperty(new Error("A blank string is not a valid number"), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.NumberFormatException','java.lang.Object','java.lang.RuntimeException','java.lang.IllegalArgumentException','java.lang.Exception'] });
            }
            if (/* startsWith */((str, searchString, position = 0) => str.substr(position, searchString.length) === searchString)(str, "--")){
                return null;
            }
            if (/* startsWith */((str, searchString, position = 0) => str.substr(position, searchString.length) === searchString)(str, "0x") || /* startsWith */((str, searchString, position = 0) => str.substr(position, searchString.length) === searchString)(str, "-0x")){
                return NumberUtils.createInteger(str);
            }
            const lastChar: string = str.charAt(str.length - 1);
            let mant: string;
            let dec: string;
            let exp: string;
            const decPos: number = str.indexOf('.');
            const expPos: number = str.indexOf('e') + str.indexOf('E') + 1;
            if (decPos > -1){
                if (expPos > -1){
                    if (expPos < decPos || expPos > str.length){
                        throw Object.defineProperty(new Error(str + " is not a valid number."), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.NumberFormatException','java.lang.Object','java.lang.RuntimeException','java.lang.IllegalArgumentException','java.lang.Exception'] });
                    }
                    dec = str.substring(decPos + 1, expPos);
                } else {
                    dec = str.substring(decPos + 1);
                }
                mant = str.substring(0, decPos);
            } else {
                if (expPos > -1){
                    if (expPos > str.length){
                        throw Object.defineProperty(new Error(str + " is not a valid number."), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.NumberFormatException','java.lang.Object','java.lang.RuntimeException','java.lang.IllegalArgumentException','java.lang.Exception'] });
                    }
                    mant = str.substring(0, expPos);
                } else {
                    mant = str;
                }
                dec = null;
            }
            if (!/* isDigit *//\d/.test(lastChar[0]) && (c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(lastChar) != '.'.charCodeAt(0)){
                if (expPos > -1 && expPos < str.length - 1){
                    exp = str.substring(expPos + 1, str.length - 1);
                } else {
                    exp = null;
                }
                const numeric: string = str.substring(0, str.length - 1);
                const allZeros: boolean = NumberUtils.isAllZeros(mant) && NumberUtils.isAllZeros(exp);
                switch((lastChar).charCodeAt(0)) {
                case 108 /* 'l' */:
                case 76 /* 'L' */:
                    if (dec == null && exp == null && ((c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(numeric.charAt(0)) == '-'.charCodeAt(0) && NumberUtils.isDigits(numeric.substring(1)) || NumberUtils.isDigits(numeric))){
                        try {
                            return NumberUtils.createLong(numeric);
                        } catch(nfe) {
                        }
                        return NumberUtils.createBigInteger(numeric);
                    }
                    throw Object.defineProperty(new Error(str + " is not a valid number."), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.NumberFormatException','java.lang.Object','java.lang.RuntimeException','java.lang.IllegalArgumentException','java.lang.Exception'] });
                case 102 /* 'f' */:
                case 70 /* 'F' */:
                    try {
                        const f: number = NumberUtils.createFloat(numeric);
                        if (!(/* isInfinite */((value) => Number.NEGATIVE_INFINITY === value || Number.POSITIVE_INFINITY === value)(f) || (/* floatValue */f === 0.0 && !allZeros))){
                            return f;
                        }
                    } catch(nfe) {
                    }
                case 100 /* 'd' */:
                case 68 /* 'D' */:
                    try {
                        const d: number = NumberUtils.createDouble(numeric);
                        if (!(/* isInfinite */((value) => Number.NEGATIVE_INFINITY === value || Number.POSITIVE_INFINITY === value)(d) || (/* floatValue */d === 0.0 && !allZeros))){
                            return d;
                        }
                    } catch(nfe) {
                    }
                    try {
                        return NumberUtils.createBigDecimal(numeric);
                    } catch(e) {
                    }
                default:
                    throw Object.defineProperty(new Error(str + " is not a valid number."), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.NumberFormatException','java.lang.Object','java.lang.RuntimeException','java.lang.IllegalArgumentException','java.lang.Exception'] });
                }
            } else {
                if (expPos > -1 && expPos < str.length - 1){
                    exp = str.substring(expPos + 1, str.length);
                } else {
                    exp = null;
                }
                if (dec == null && exp == null){
                    try {
                        return NumberUtils.createInteger(str);
                    } catch(nfe) {
                    }
                    try {
                        return NumberUtils.createLong(str);
                    } catch(nfe) {
                    }
                    return NumberUtils.createBigInteger(str);
                } else {
                    const allZeros: boolean = NumberUtils.isAllZeros(mant) && NumberUtils.isAllZeros(exp);
                    try {
                        const f: number = NumberUtils.createFloat(str);
                        if (!(/* isInfinite */((value) => Number.NEGATIVE_INFINITY === value || Number.POSITIVE_INFINITY === value)(f) || (/* floatValue */f === 0.0 && !allZeros))){
                            return f;
                        }
                    } catch(nfe) {
                    }
                    try {
                        const d: number = NumberUtils.createDouble(str);
                        if (!(/* isInfinite */((value) => Number.NEGATIVE_INFINITY === value || Number.POSITIVE_INFINITY === value)(d) || (/* doubleValue */d === 0.0 && !allZeros))){
                            return d;
                        }
                    } catch(nfe) {
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
        /*private*/ static isAllZeros(str: string): boolean {
            if (str == null){
                return true;
            }
            for(let i: number = str.length - 1; i >= 0; i--) {{
                if ((c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(str.charAt(i)) != '0'.charCodeAt(0)){
                    return false;
                }
            };}
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
        public static createFloat(str: string): number {
            if (str == null){
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
        public static createDouble(str: string): number {
            if (str == null){
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
        public static createInteger(str: string): number {
            if (str == null){
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
        public static createLong(str: string): number {
            if (str == null){
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
        public static createBigInteger(str: string): java.math.BigInteger {
            if (str == null){
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
        public static createBigDecimal(str: string): java.math.BigDecimal {
            if (str == null){
                return null;
            }
            if (org.openprovenance.apache.commons.lang.StringUtils.isBlank(str)){
                throw Object.defineProperty(new Error("A blank string is not a valid number"), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.NumberFormatException','java.lang.Object','java.lang.RuntimeException','java.lang.IllegalArgumentException','java.lang.Exception'] });
            }
            return new java.math.BigDecimal(str);
        }

        public static min$long_A(array: number[]): number {
            if (array == null){
                throw Object.defineProperty(new Error("The Array must not be null"), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.Object','java.lang.RuntimeException','java.lang.IllegalArgumentException','java.lang.Exception'] });
            } else if (array.length === 0){
                throw Object.defineProperty(new Error("Array cannot be empty."), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.Object','java.lang.RuntimeException','java.lang.IllegalArgumentException','java.lang.Exception'] });
            }
            let min: number = array[0];
            for(let i: number = 1; i < array.length; i++) {{
                if (array[i] < min){
                    min = array[i];
                }
            };}
            return min;
        }

        public static min$int_A(array: number[]): number {
            if (array == null){
                throw Object.defineProperty(new Error("The Array must not be null"), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.Object','java.lang.RuntimeException','java.lang.IllegalArgumentException','java.lang.Exception'] });
            } else if (array.length === 0){
                throw Object.defineProperty(new Error("Array cannot be empty."), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.Object','java.lang.RuntimeException','java.lang.IllegalArgumentException','java.lang.Exception'] });
            }
            let min: number = array[0];
            for(let j: number = 1; j < array.length; j++) {{
                if (array[j] < min){
                    min = array[j];
                }
            };}
            return min;
        }

        public static min$short_A(array: number[]): number {
            if (array == null){
                throw Object.defineProperty(new Error("The Array must not be null"), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.Object','java.lang.RuntimeException','java.lang.IllegalArgumentException','java.lang.Exception'] });
            } else if (array.length === 0){
                throw Object.defineProperty(new Error("Array cannot be empty."), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.Object','java.lang.RuntimeException','java.lang.IllegalArgumentException','java.lang.Exception'] });
            }
            let min: number = array[0];
            for(let i: number = 1; i < array.length; i++) {{
                if (array[i] < min){
                    min = array[i];
                }
            };}
            return min;
        }

        public static min$byte_A(array: number[]): number {
            if (array == null){
                throw Object.defineProperty(new Error("The Array must not be null"), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.Object','java.lang.RuntimeException','java.lang.IllegalArgumentException','java.lang.Exception'] });
            } else if (array.length === 0){
                throw Object.defineProperty(new Error("Array cannot be empty."), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.Object','java.lang.RuntimeException','java.lang.IllegalArgumentException','java.lang.Exception'] });
            }
            let min: number = array[0];
            for(let i: number = 1; i < array.length; i++) {{
                if (array[i] < min){
                    min = array[i];
                }
            };}
            return min;
        }

        public static min$double_A(array: number[]): number {
            if (array == null){
                throw Object.defineProperty(new Error("The Array must not be null"), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.Object','java.lang.RuntimeException','java.lang.IllegalArgumentException','java.lang.Exception'] });
            } else if (array.length === 0){
                throw Object.defineProperty(new Error("Array cannot be empty."), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.Object','java.lang.RuntimeException','java.lang.IllegalArgumentException','java.lang.Exception'] });
            }
            let min: number = array[0];
            for(let i: number = 1; i < array.length; i++) {{
                if (/* isNaN */isNaN(array[i])){
                    return NaN;
                }
                if (array[i] < min){
                    min = array[i];
                }
            };}
            return min;
        }

        public static min$float_A(array: number[]): number {
            if (array == null){
                throw Object.defineProperty(new Error("The Array must not be null"), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.Object','java.lang.RuntimeException','java.lang.IllegalArgumentException','java.lang.Exception'] });
            } else if (array.length === 0){
                throw Object.defineProperty(new Error("Array cannot be empty."), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.Object','java.lang.RuntimeException','java.lang.IllegalArgumentException','java.lang.Exception'] });
            }
            let min: number = array[0];
            for(let i: number = 1; i < array.length; i++) {{
                if (/* isNaN */isNaN(array[i])){
                    return NaN;
                }
                if (array[i] < min){
                    min = array[i];
                }
            };}
            return min;
        }

        public static max$long_A(array: number[]): number {
            if (array == null){
                throw Object.defineProperty(new Error("The Array must not be null"), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.Object','java.lang.RuntimeException','java.lang.IllegalArgumentException','java.lang.Exception'] });
            } else if (array.length === 0){
                throw Object.defineProperty(new Error("Array cannot be empty."), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.Object','java.lang.RuntimeException','java.lang.IllegalArgumentException','java.lang.Exception'] });
            }
            let max: number = array[0];
            for(let j: number = 1; j < array.length; j++) {{
                if (array[j] > max){
                    max = array[j];
                }
            };}
            return max;
        }

        public static max$int_A(array: number[]): number {
            if (array == null){
                throw Object.defineProperty(new Error("The Array must not be null"), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.Object','java.lang.RuntimeException','java.lang.IllegalArgumentException','java.lang.Exception'] });
            } else if (array.length === 0){
                throw Object.defineProperty(new Error("Array cannot be empty."), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.Object','java.lang.RuntimeException','java.lang.IllegalArgumentException','java.lang.Exception'] });
            }
            let max: number = array[0];
            for(let j: number = 1; j < array.length; j++) {{
                if (array[j] > max){
                    max = array[j];
                }
            };}
            return max;
        }

        public static max$short_A(array: number[]): number {
            if (array == null){
                throw Object.defineProperty(new Error("The Array must not be null"), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.Object','java.lang.RuntimeException','java.lang.IllegalArgumentException','java.lang.Exception'] });
            } else if (array.length === 0){
                throw Object.defineProperty(new Error("Array cannot be empty."), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.Object','java.lang.RuntimeException','java.lang.IllegalArgumentException','java.lang.Exception'] });
            }
            let max: number = array[0];
            for(let i: number = 1; i < array.length; i++) {{
                if (array[i] > max){
                    max = array[i];
                }
            };}
            return max;
        }

        public static max$byte_A(array: number[]): number {
            if (array == null){
                throw Object.defineProperty(new Error("The Array must not be null"), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.Object','java.lang.RuntimeException','java.lang.IllegalArgumentException','java.lang.Exception'] });
            } else if (array.length === 0){
                throw Object.defineProperty(new Error("Array cannot be empty."), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.Object','java.lang.RuntimeException','java.lang.IllegalArgumentException','java.lang.Exception'] });
            }
            let max: number = array[0];
            for(let i: number = 1; i < array.length; i++) {{
                if (array[i] > max){
                    max = array[i];
                }
            };}
            return max;
        }

        public static max$double_A(array: number[]): number {
            if (array == null){
                throw Object.defineProperty(new Error("The Array must not be null"), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.Object','java.lang.RuntimeException','java.lang.IllegalArgumentException','java.lang.Exception'] });
            } else if (array.length === 0){
                throw Object.defineProperty(new Error("Array cannot be empty."), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.Object','java.lang.RuntimeException','java.lang.IllegalArgumentException','java.lang.Exception'] });
            }
            let max: number = array[0];
            for(let j: number = 1; j < array.length; j++) {{
                if (/* isNaN */isNaN(array[j])){
                    return NaN;
                }
                if (array[j] > max){
                    max = array[j];
                }
            };}
            return max;
        }

        public static max$float_A(array: number[]): number {
            if (array == null){
                throw Object.defineProperty(new Error("The Array must not be null"), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.Object','java.lang.RuntimeException','java.lang.IllegalArgumentException','java.lang.Exception'] });
            } else if (array.length === 0){
                throw Object.defineProperty(new Error("Array cannot be empty."), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.Object','java.lang.RuntimeException','java.lang.IllegalArgumentException','java.lang.Exception'] });
            }
            let max: number = array[0];
            for(let j: number = 1; j < array.length; j++) {{
                if (/* isNaN */isNaN(array[j])){
                    return NaN;
                }
                if (array[j] > max){
                    max = array[j];
                }
            };}
            return max;
        }

        public static min$long$long$long(a: number, b: number, c: number): number {
            if (b < a){
                a = b;
            }
            if (c < a){
                a = c;
            }
            return a;
        }

        public static min$int$int$int(a: number, b: number, c: number): number {
            if (b < a){
                a = b;
            }
            if (c < a){
                a = c;
            }
            return a;
        }

        public static min$short$short$short(a: number, b: number, c: number): number {
            if (b < a){
                a = b;
            }
            if (c < a){
                a = c;
            }
            return a;
        }

        public static min$byte$byte$byte(a: number, b: number, c: number): number {
            if (b < a){
                a = b;
            }
            if (c < a){
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
        public static min(a?: any, b?: any, c?: any): any {
            if (((typeof a === 'number') || a === null) && ((typeof b === 'number') || b === null) && ((typeof c === 'number') || c === null)) {
                return <any>org.openprovenance.apache.commons.lang.math.NumberUtils.min$byte$byte$byte(a, b, c);
            } else if (((typeof a === 'number') || a === null) && ((typeof b === 'number') || b === null) && ((typeof c === 'number') || c === null)) {
                return <any>org.openprovenance.apache.commons.lang.math.NumberUtils.min$short$short$short(a, b, c);
            } else if (((typeof a === 'number') || a === null) && ((typeof b === 'number') || b === null) && ((typeof c === 'number') || c === null)) {
                return <any>org.openprovenance.apache.commons.lang.math.NumberUtils.min$int$int$int(a, b, c);
            } else if (((typeof a === 'number') || a === null) && ((typeof b === 'number') || b === null) && ((typeof c === 'number') || c === null)) {
                return <any>org.openprovenance.apache.commons.lang.math.NumberUtils.min$long$long$long(a, b, c);
            } else if (((typeof a === 'number') || a === null) && ((typeof b === 'number') || b === null) && ((typeof c === 'number') || c === null)) {
                return <any>org.openprovenance.apache.commons.lang.math.NumberUtils.min$float$float$float(a, b, c);
            } else if (((typeof a === 'number') || a === null) && ((typeof b === 'number') || b === null) && ((typeof c === 'number') || c === null)) {
                return <any>org.openprovenance.apache.commons.lang.math.NumberUtils.min$double$double$double(a, b, c);
            } else if (((a != null && a instanceof <any>Array && (a.length == 0 || a[0] == null ||(typeof a[0] === 'number'))) || a === null) && b === undefined && c === undefined) {
                return <any>org.openprovenance.apache.commons.lang.math.NumberUtils.min$long_A(a);
            } else if (((a != null && a instanceof <any>Array && (a.length == 0 || a[0] == null ||(typeof a[0] === 'number'))) || a === null) && b === undefined && c === undefined) {
                return <any>org.openprovenance.apache.commons.lang.math.NumberUtils.min$int_A(a);
            } else if (((a != null && a instanceof <any>Array && (a.length == 0 || a[0] == null ||(typeof a[0] === 'number'))) || a === null) && b === undefined && c === undefined) {
                return <any>org.openprovenance.apache.commons.lang.math.NumberUtils.min$short_A(a);
            } else if (((a != null && a instanceof <any>Array && (a.length == 0 || a[0] == null ||(typeof a[0] === 'number'))) || a === null) && b === undefined && c === undefined) {
                return <any>org.openprovenance.apache.commons.lang.math.NumberUtils.min$byte_A(a);
            } else if (((a != null && a instanceof <any>Array && (a.length == 0 || a[0] == null ||(typeof a[0] === 'number'))) || a === null) && b === undefined && c === undefined) {
                return <any>org.openprovenance.apache.commons.lang.math.NumberUtils.min$double_A(a);
            } else if (((a != null && a instanceof <any>Array && (a.length == 0 || a[0] == null ||(typeof a[0] === 'number'))) || a === null) && b === undefined && c === undefined) {
                return <any>org.openprovenance.apache.commons.lang.math.NumberUtils.min$float_A(a);
            } else throw new Error('invalid overload');
        }

        public static min$double$double$double(a: number, b: number, c: number): number {
            return Math.min(Math.min(a, b), c);
        }

        public static min$float$float$float(a: number, b: number, c: number): number {
            return Math.min(Math.min(a, b), c);
        }

        public static max$long$long$long(a: number, b: number, c: number): number {
            if (b > a){
                a = b;
            }
            if (c > a){
                a = c;
            }
            return a;
        }

        public static max$int$int$int(a: number, b: number, c: number): number {
            if (b > a){
                a = b;
            }
            if (c > a){
                a = c;
            }
            return a;
        }

        public static max$short$short$short(a: number, b: number, c: number): number {
            if (b > a){
                a = b;
            }
            if (c > a){
                a = c;
            }
            return a;
        }

        public static max$byte$byte$byte(a: number, b: number, c: number): number {
            if (b > a){
                a = b;
            }
            if (c > a){
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
        public static max(a?: any, b?: any, c?: any): any {
            if (((typeof a === 'number') || a === null) && ((typeof b === 'number') || b === null) && ((typeof c === 'number') || c === null)) {
                return <any>org.openprovenance.apache.commons.lang.math.NumberUtils.max$byte$byte$byte(a, b, c);
            } else if (((typeof a === 'number') || a === null) && ((typeof b === 'number') || b === null) && ((typeof c === 'number') || c === null)) {
                return <any>org.openprovenance.apache.commons.lang.math.NumberUtils.max$short$short$short(a, b, c);
            } else if (((typeof a === 'number') || a === null) && ((typeof b === 'number') || b === null) && ((typeof c === 'number') || c === null)) {
                return <any>org.openprovenance.apache.commons.lang.math.NumberUtils.max$int$int$int(a, b, c);
            } else if (((typeof a === 'number') || a === null) && ((typeof b === 'number') || b === null) && ((typeof c === 'number') || c === null)) {
                return <any>org.openprovenance.apache.commons.lang.math.NumberUtils.max$long$long$long(a, b, c);
            } else if (((typeof a === 'number') || a === null) && ((typeof b === 'number') || b === null) && ((typeof c === 'number') || c === null)) {
                return <any>org.openprovenance.apache.commons.lang.math.NumberUtils.max$float$float$float(a, b, c);
            } else if (((typeof a === 'number') || a === null) && ((typeof b === 'number') || b === null) && ((typeof c === 'number') || c === null)) {
                return <any>org.openprovenance.apache.commons.lang.math.NumberUtils.max$double$double$double(a, b, c);
            } else if (((a != null && a instanceof <any>Array && (a.length == 0 || a[0] == null ||(typeof a[0] === 'number'))) || a === null) && b === undefined && c === undefined) {
                return <any>org.openprovenance.apache.commons.lang.math.NumberUtils.max$long_A(a);
            } else if (((a != null && a instanceof <any>Array && (a.length == 0 || a[0] == null ||(typeof a[0] === 'number'))) || a === null) && b === undefined && c === undefined) {
                return <any>org.openprovenance.apache.commons.lang.math.NumberUtils.max$int_A(a);
            } else if (((a != null && a instanceof <any>Array && (a.length == 0 || a[0] == null ||(typeof a[0] === 'number'))) || a === null) && b === undefined && c === undefined) {
                return <any>org.openprovenance.apache.commons.lang.math.NumberUtils.max$short_A(a);
            } else if (((a != null && a instanceof <any>Array && (a.length == 0 || a[0] == null ||(typeof a[0] === 'number'))) || a === null) && b === undefined && c === undefined) {
                return <any>org.openprovenance.apache.commons.lang.math.NumberUtils.max$byte_A(a);
            } else if (((a != null && a instanceof <any>Array && (a.length == 0 || a[0] == null ||(typeof a[0] === 'number'))) || a === null) && b === undefined && c === undefined) {
                return <any>org.openprovenance.apache.commons.lang.math.NumberUtils.max$double_A(a);
            } else if (((a != null && a instanceof <any>Array && (a.length == 0 || a[0] == null ||(typeof a[0] === 'number'))) || a === null) && b === undefined && c === undefined) {
                return <any>org.openprovenance.apache.commons.lang.math.NumberUtils.max$float_A(a);
            } else throw new Error('invalid overload');
        }

        public static max$double$double$double(a: number, b: number, c: number): number {
            return Math.max(Math.max(a, b), c);
        }

        public static max$float$float$float(a: number, b: number, c: number): number {
            return Math.max(Math.max(a, b), c);
        }

        public static compare$double$double(lhs: number, rhs: number): number {
            if (lhs < rhs){
                return -1;
            }
            if (lhs > rhs){
                return +1;
            }
            const lhsBits: number = /* doubleToLongBits */((f) => { let buf = new ArrayBuffer(4); (new Float32Array(buf))[0]=f; return (new Uint32Array(buf))[0]; })((<any>Math).fround(lhs));
            const rhsBits: number = /* doubleToLongBits */((f) => { let buf = new ArrayBuffer(4); (new Float32Array(buf))[0]=f; return (new Uint32Array(buf))[0]; })((<any>Math).fround(rhs));
            if (lhsBits === rhsBits){
                return 0;
            }
            if (lhsBits < rhsBits){
                return -1;
            } else {
                return +1;
            }
        }

        public static compare$float$float(lhs: number, rhs: number): number {
            if (lhs < rhs){
                return -1;
            }
            if (lhs > rhs){
                return +1;
            }
            const lhsBits: number = /* floatToIntBits */((f) => { let buf = new ArrayBuffer(4); (new Float32Array(buf))[0]=f; return (new Uint32Array(buf))[0]; })(lhs);
            const rhsBits: number = /* floatToIntBits */((f) => { let buf = new ArrayBuffer(4); (new Float32Array(buf))[0]=f; return (new Uint32Array(buf))[0]; })(rhs);
            if (lhsBits === rhsBits){
                return 0;
            }
            if (lhsBits < rhsBits){
                return -1;
            } else {
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
        public static compare(lhs?: any, rhs?: any): any {
            if (((typeof lhs === 'number') || lhs === null) && ((typeof rhs === 'number') || rhs === null)) {
                return <any>org.openprovenance.apache.commons.lang.math.NumberUtils.compare$float$float(lhs, rhs);
            } else if (((typeof lhs === 'number') || lhs === null) && ((typeof rhs === 'number') || rhs === null)) {
                return <any>org.openprovenance.apache.commons.lang.math.NumberUtils.compare$double$double(lhs, rhs);
            } else throw new Error('invalid overload');
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
        public static isDigits(str: string): boolean {
            if (org.openprovenance.apache.commons.lang.StringUtils.isEmpty(str)){
                return false;
            }
            for(let i: number = 0; i < str.length; i++) {{
                if (!/* isDigit *//\d/.test(str.charAt(i)[0])){
                    return false;
                }
            };}
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
        public static isNumber(str: string): boolean {
            if (org.openprovenance.apache.commons.lang.StringUtils.isEmpty(str)){
                return false;
            }
            const chars: string[] = /* toCharArray */(str).split('');
            let sz: number = chars.length;
            let hasExp: boolean = false;
            let hasDecPoint: boolean = false;
            let allowSigns: boolean = false;
            let foundDigit: boolean = false;
            const start: number = ((c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(chars[0]) == '-'.charCodeAt(0)) ? 1 : 0;
            if (sz > start + 1){
                if ((c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(chars[start]) == '0'.charCodeAt(0) && (c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(chars[start + 1]) == 'x'.charCodeAt(0)){
                    let i: number = start + 2;
                    if (i === sz){
                        return false;
                    }
                    for(; i < chars.length; i++) {{
                        if (((c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(chars[i]) < '0'.charCodeAt(0) || (c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(chars[i]) > '9'.charCodeAt(0)) && ((c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(chars[i]) < 'a'.charCodeAt(0) || (c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(chars[i]) > 'f'.charCodeAt(0)) && ((c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(chars[i]) < 'A'.charCodeAt(0) || (c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(chars[i]) > 'F'.charCodeAt(0))){
                            return false;
                        }
                    };}
                    return true;
                }
            }
            sz--;
            let i: number = start;
            while((i < sz || (i < sz + 1 && allowSigns && !foundDigit))) {{
                if ((c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(chars[i]) >= '0'.charCodeAt(0) && (c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(chars[i]) <= '9'.charCodeAt(0)){
                    foundDigit = true;
                    allowSigns = false;
                } else if ((c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(chars[i]) == '.'.charCodeAt(0)){
                    if (hasDecPoint || hasExp){
                        return false;
                    }
                    hasDecPoint = true;
                } else if ((c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(chars[i]) == 'e'.charCodeAt(0) || (c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(chars[i]) == 'E'.charCodeAt(0)){
                    if (hasExp){
                        return false;
                    }
                    if (!foundDigit){
                        return false;
                    }
                    hasExp = true;
                    allowSigns = true;
                } else if ((c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(chars[i]) == '+'.charCodeAt(0) || (c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(chars[i]) == '-'.charCodeAt(0)){
                    if (!allowSigns){
                        return false;
                    }
                    allowSigns = false;
                    foundDigit = false;
                } else {
                    return false;
                }
                i++;
            }};
            if (i < chars.length){
                if ((c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(chars[i]) >= '0'.charCodeAt(0) && (c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(chars[i]) <= '9'.charCodeAt(0)){
                    return true;
                }
                if ((c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(chars[i]) == 'e'.charCodeAt(0) || (c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(chars[i]) == 'E'.charCodeAt(0)){
                    return false;
                }
                if ((c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(chars[i]) == '.'.charCodeAt(0)){
                    if (hasDecPoint || hasExp){
                        return false;
                    }
                    return foundDigit;
                }
                if (!allowSigns && ((c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(chars[i]) == 'd'.charCodeAt(0) || (c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(chars[i]) == 'D'.charCodeAt(0) || (c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(chars[i]) == 'f'.charCodeAt(0) || (c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(chars[i]) == 'F'.charCodeAt(0))){
                    return foundDigit;
                }
                if ((c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(chars[i]) == 'l'.charCodeAt(0) || (c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(chars[i]) == 'L'.charCodeAt(0)){
                    return foundDigit && !hasExp;
                }
                return false;
            }
            return !allowSigns && foundDigit;
        }
    }
    NumberUtils["__class"] = "org.openprovenance.apache.commons.lang.math.NumberUtils";

}

