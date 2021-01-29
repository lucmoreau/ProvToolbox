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
                     * <p><code>CharUtils</code> instances should NOT be constructed in standard programming.
                     * Instead, the class should be used as <code>CharUtils.toString('c');</code>.</p>
                     *
                     * <p>This constructor is public to permit tools that require a JavaBean instance
                     * to operate.</p>
                     * @class
                     * @author Apache Software Foundation
                     */
                    class CharUtils {
                        constructor() {
                        }
                        static __static_initialize() { if (!CharUtils.__static_initialized) {
                            CharUtils.__static_initialized = true;
                            CharUtils.__static_initializer_0();
                        } }
                        static CHAR_STRING_ARRAY_$LI$() { CharUtils.__static_initialize(); if (CharUtils.CHAR_STRING_ARRAY == null) {
                            CharUtils.CHAR_STRING_ARRAY = (s => { let a = []; while (s-- > 0)
                                a.push(null); return a; })(128);
                        } return CharUtils.CHAR_STRING_ARRAY; }
                        static CHAR_ARRAY_$LI$() { CharUtils.__static_initialize(); if (CharUtils.CHAR_ARRAY == null) {
                            CharUtils.CHAR_ARRAY = (s => { let a = []; while (s-- > 0)
                                a.push(null); return a; })(128);
                        } return CharUtils.CHAR_ARRAY; }
                        static __static_initializer_0() {
                            for (let i = 127; i >= 0; i--) {
                                {
                                    CharUtils.CHAR_STRING_ARRAY_$LI$()[i] = CharUtils.CHAR_STRING.substring(i, i + 1);
                                    CharUtils.CHAR_ARRAY_$LI$()[i] = new String(String.fromCharCode(i));
                                }
                                ;
                            }
                        }
                        static toCharacterObject$char(ch) {
                            if ((c => c.charCodeAt == null ? c : c.charCodeAt(0))(ch) < CharUtils.CHAR_ARRAY_$LI$().length) {
                                return CharUtils.CHAR_ARRAY_$LI$()[(ch).charCodeAt(0)];
                            }
                            return new String(ch);
                        }
                        static toCharacterObject$java_lang_String(str) {
                            if (org.openprovenance.apache.commons.lang.StringUtils.isEmpty(str)) {
                                return null;
                            }
                            return CharUtils.toCharacterObject$char(str.charAt(0));
                        }
                        /**
                         * <p>Converts the String to a Character using the first character, returning
                         * null for empty Strings.</p>
                         *
                         * <p>For ASCII 7 bit characters, this uses a cache that will return the
                         * same Character object each time.</p>
                         *
                         * <pre>
                         * CharUtils.toCharacterObject(null) = null
                         * CharUtils.toCharacterObject("")   = null
                         * CharUtils.toCharacterObject("A")  = 'A'
                         * CharUtils.toCharacterObject("BA") = 'B'
                         * </pre>
                         *
                         * @param {string} str  the character to convert
                         * @return {string} the Character value of the first letter of the String
                         */
                        static toCharacterObject(str) {
                            if (((typeof str === 'string') || str === null)) {
                                return org.openprovenance.apache.commons.lang.CharUtils.toCharacterObject$java_lang_String(str);
                            }
                            else if (((typeof str === 'string') || str === null)) {
                                return org.openprovenance.apache.commons.lang.CharUtils.toCharacterObject$char(str);
                            }
                            else
                                throw new Error('invalid overload');
                        }
                        static toChar$java_lang_Character(ch) {
                            if ((c => c.charCodeAt == null ? c : c.charCodeAt(0))(ch) == null) {
                                throw Object.defineProperty(new Error("The Character must not be null"), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.IllegalArgumentException', 'java.lang.Exception'] });
                            }
                            return /* charValue */ ch;
                        }
                        static toChar$java_lang_Character$char(ch, defaultValue) {
                            if ((c => c.charCodeAt == null ? c : c.charCodeAt(0))(ch) == null) {
                                return defaultValue;
                            }
                            return /* charValue */ ch;
                        }
                        /**
                         * <p>Converts the Character to a char handling <code>null</code>.</p>
                         *
                         * <pre>
                         * CharUtils.toChar(null, 'X') = 'X'
                         * CharUtils.toChar(' ', 'X')  = ' '
                         * CharUtils.toChar('A', 'X')  = 'A'
                         * </pre>
                         *
                         * @param {string} ch  the character to convert
                         * @param {string} defaultValue  the value to use if the  Character is null
                         * @return {string} the char value of the Character or the default if null
                         */
                        static toChar(ch, defaultValue) {
                            if (((typeof ch === 'string') || ch === null) && ((typeof defaultValue === 'string') || defaultValue === null)) {
                                return org.openprovenance.apache.commons.lang.CharUtils.toChar$java_lang_Character$char(ch, defaultValue);
                            }
                            else if (((typeof ch === 'string') || ch === null) && ((typeof defaultValue === 'string') || defaultValue === null)) {
                                return org.openprovenance.apache.commons.lang.CharUtils.toChar$java_lang_String$char(ch, defaultValue);
                            }
                            else if (((typeof ch === 'string') || ch === null) && defaultValue === undefined) {
                                return org.openprovenance.apache.commons.lang.CharUtils.toChar$java_lang_Character(ch);
                            }
                            else if (((typeof ch === 'string') || ch === null) && defaultValue === undefined) {
                                return org.openprovenance.apache.commons.lang.CharUtils.toChar$java_lang_String(ch);
                            }
                            else
                                throw new Error('invalid overload');
                        }
                        static toChar$java_lang_String(str) {
                            if (org.openprovenance.apache.commons.lang.StringUtils.isEmpty(str)) {
                                throw Object.defineProperty(new Error("The String must not be empty"), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.IllegalArgumentException', 'java.lang.Exception'] });
                            }
                            return str.charAt(0);
                        }
                        static toChar$java_lang_String$char(str, defaultValue) {
                            if (org.openprovenance.apache.commons.lang.StringUtils.isEmpty(str)) {
                                return defaultValue;
                            }
                            return str.charAt(0);
                        }
                        static toIntValue$char(ch) {
                            if (CharUtils.isAsciiNumeric(ch) === false) {
                                throw Object.defineProperty(new Error("The character " + ch + " is not in the range \'0\' - \'9\'"), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.IllegalArgumentException', 'java.lang.Exception'] });
                            }
                            return (c => c.charCodeAt == null ? c : c.charCodeAt(0))(ch) - 48;
                        }
                        static toIntValue$char$int(ch, defaultValue) {
                            if (CharUtils.isAsciiNumeric(ch) === false) {
                                return defaultValue;
                            }
                            return (c => c.charCodeAt == null ? c : c.charCodeAt(0))(ch) - 48;
                        }
                        static toIntValue$java_lang_Character(ch) {
                            if ((c => c.charCodeAt == null ? c : c.charCodeAt(0))(ch) == null) {
                                throw Object.defineProperty(new Error("The character must not be null"), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.IllegalArgumentException', 'java.lang.Exception'] });
                            }
                            return CharUtils.toIntValue$char(/* charValue */ ch);
                        }
                        static toIntValue$java_lang_Character$int(ch, defaultValue) {
                            if ((c => c.charCodeAt == null ? c : c.charCodeAt(0))(ch) == null) {
                                return defaultValue;
                            }
                            return CharUtils.toIntValue$char$int(/* charValue */ ch, defaultValue);
                        }
                        /**
                         * <p>Converts the character to the Integer it represents, throwing an
                         * exception if the character is not numeric.</p>
                         *
                         * <p>This method coverts the char '1' to the int 1 and so on.</p>
                         *
                         * <pre>
                         * CharUtils.toIntValue(null, -1) = -1
                         * CharUtils.toIntValue('3', -1)  = 3
                         * CharUtils.toIntValue('A', -1)  = -1
                         * </pre>
                         *
                         * @param {string} ch  the character to convert
                         * @param {number} defaultValue  the default value to use if the character is not numeric
                         * @return {number} the int value of the character
                         */
                        static toIntValue(ch, defaultValue) {
                            if (((typeof ch === 'string') || ch === null) && ((typeof defaultValue === 'number') || defaultValue === null)) {
                                return org.openprovenance.apache.commons.lang.CharUtils.toIntValue$java_lang_Character$int(ch, defaultValue);
                            }
                            else if (((typeof ch === 'string') || ch === null) && ((typeof defaultValue === 'number') || defaultValue === null)) {
                                return org.openprovenance.apache.commons.lang.CharUtils.toIntValue$char$int(ch, defaultValue);
                            }
                            else if (((typeof ch === 'string') || ch === null) && defaultValue === undefined) {
                                return org.openprovenance.apache.commons.lang.CharUtils.toIntValue$java_lang_Character(ch);
                            }
                            else if (((typeof ch === 'string') || ch === null) && defaultValue === undefined) {
                                return org.openprovenance.apache.commons.lang.CharUtils.toIntValue$char(ch);
                            }
                            else
                                throw new Error('invalid overload');
                        }
                        static toString$char(ch) {
                            if ((c => c.charCodeAt == null ? c : c.charCodeAt(0))(ch) < 128) {
                                return CharUtils.CHAR_STRING_ARRAY_$LI$()[(ch).charCodeAt(0)];
                            }
                            return [ch].join('');
                        }
                        static toString$java_lang_Character(ch) {
                            if ((c => c.charCodeAt == null ? c : c.charCodeAt(0))(ch) == null) {
                                return null;
                            }
                            return CharUtils.toString$char(/* charValue */ ch);
                        }
                        /**
                         * <p>Converts the character to a String that contains the one character.</p>
                         *
                         * <p>For ASCII 7 bit characters, this uses a cache that will return the
                         * same String object each time.</p>
                         *
                         * <p>If <code>null</code> is passed in, <code>null</code> will be returned.</p>
                         *
                         * <pre>
                         * CharUtils.toString(null) = null
                         * CharUtils.toString(' ')  = " "
                         * CharUtils.toString('A')  = "A"
                         * </pre>
                         *
                         * @param {string} ch  the character to convert
                         * @return {string} a String containing the one specified character
                         */
                        static toString(ch) {
                            if (((typeof ch === 'string') || ch === null)) {
                                return org.openprovenance.apache.commons.lang.CharUtils.toString$java_lang_Character(ch);
                            }
                            else if (((typeof ch === 'string') || ch === null)) {
                                return org.openprovenance.apache.commons.lang.CharUtils.toString$char(ch);
                            }
                            else
                                throw new Error('invalid overload');
                        }
                        static unicodeEscaped$char(ch) {
                            if ((c => c.charCodeAt == null ? c : c.charCodeAt(0))(ch) < 16) {
                                return "\\u000" + javaemul.internal.IntegerHelper.toHexString(ch);
                            }
                            else if ((c => c.charCodeAt == null ? c : c.charCodeAt(0))(ch) < 256) {
                                return "\\u00" + javaemul.internal.IntegerHelper.toHexString(ch);
                            }
                            else if ((c => c.charCodeAt == null ? c : c.charCodeAt(0))(ch) < 4096) {
                                return "\\u0" + javaemul.internal.IntegerHelper.toHexString(ch);
                            }
                            return "\\u" + javaemul.internal.IntegerHelper.toHexString(ch);
                        }
                        static unicodeEscaped$java_lang_Character(ch) {
                            if ((c => c.charCodeAt == null ? c : c.charCodeAt(0))(ch) == null) {
                                return null;
                            }
                            return CharUtils.unicodeEscaped$char(/* charValue */ ch);
                        }
                        /**
                         * <p>Converts the string to the unicode format ' '.</p>
                         *
                         * <p>This format is the Java source code format.</p>
                         *
                         * <p>If <code>null</code> is passed in, <code>null</code> will be returned.</p>
                         *
                         * <pre>
                         * CharUtils.unicodeEscaped(null) = null
                         * CharUtils.unicodeEscaped(' ')  = " "
                         * CharUtils.unicodeEscaped('A')  = "A"
                         * </pre>
                         *
                         * @param {string} ch  the character to convert, may be null
                         * @return {string} the escaped unicode string, null if null input
                         */
                        static unicodeEscaped(ch) {
                            if (((typeof ch === 'string') || ch === null)) {
                                return org.openprovenance.apache.commons.lang.CharUtils.unicodeEscaped$java_lang_Character(ch);
                            }
                            else if (((typeof ch === 'string') || ch === null)) {
                                return org.openprovenance.apache.commons.lang.CharUtils.unicodeEscaped$char(ch);
                            }
                            else
                                throw new Error('invalid overload');
                        }
                        /**
                         * <p>Checks whether the character is ASCII 7 bit.</p>
                         *
                         * <pre>
                         * CharUtils.isAscii('a')  = true
                         * CharUtils.isAscii('A')  = true
                         * CharUtils.isAscii('3')  = true
                         * CharUtils.isAscii('-')  = true
                         * CharUtils.isAscii('\n') = true
                         * CharUtils.isAscii('&copy;') = false
                         * </pre>
                         *
                         * @param {string} ch  the character to check
                         * @return {boolean} true if less than 128
                         */
                        static isAscii(ch) {
                            return (c => c.charCodeAt == null ? c : c.charCodeAt(0))(ch) < 128;
                        }
                        /**
                         * <p>Checks whether the character is ASCII 7 bit printable.</p>
                         *
                         * <pre>
                         * CharUtils.isAsciiPrintable('a')  = true
                         * CharUtils.isAsciiPrintable('A')  = true
                         * CharUtils.isAsciiPrintable('3')  = true
                         * CharUtils.isAsciiPrintable('-')  = true
                         * CharUtils.isAsciiPrintable('\n') = false
                         * CharUtils.isAsciiPrintable('&copy;') = false
                         * </pre>
                         *
                         * @param {string} ch  the character to check
                         * @return {boolean} true if between 32 and 126 inclusive
                         */
                        static isAsciiPrintable(ch) {
                            return (c => c.charCodeAt == null ? c : c.charCodeAt(0))(ch) >= 32 && (c => c.charCodeAt == null ? c : c.charCodeAt(0))(ch) < 127;
                        }
                        /**
                         * <p>Checks whether the character is ASCII 7 bit control.</p>
                         *
                         * <pre>
                         * CharUtils.isAsciiControl('a')  = false
                         * CharUtils.isAsciiControl('A')  = false
                         * CharUtils.isAsciiControl('3')  = false
                         * CharUtils.isAsciiControl('-')  = false
                         * CharUtils.isAsciiControl('\n') = true
                         * CharUtils.isAsciiControl('&copy;') = false
                         * </pre>
                         *
                         * @param {string} ch  the character to check
                         * @return {boolean} true if less than 32 or equals 127
                         */
                        static isAsciiControl(ch) {
                            return (c => c.charCodeAt == null ? c : c.charCodeAt(0))(ch) < 32 || (c => c.charCodeAt == null ? c : c.charCodeAt(0))(ch) == 127;
                        }
                        /**
                         * <p>Checks whether the character is ASCII 7 bit alphabetic.</p>
                         *
                         * <pre>
                         * CharUtils.isAsciiAlpha('a')  = true
                         * CharUtils.isAsciiAlpha('A')  = true
                         * CharUtils.isAsciiAlpha('3')  = false
                         * CharUtils.isAsciiAlpha('-')  = false
                         * CharUtils.isAsciiAlpha('\n') = false
                         * CharUtils.isAsciiAlpha('&copy;') = false
                         * </pre>
                         *
                         * @param {string} ch  the character to check
                         * @return {boolean} true if between 65 and 90 or 97 and 122 inclusive
                         */
                        static isAsciiAlpha(ch) {
                            return ((c => c.charCodeAt == null ? c : c.charCodeAt(0))(ch) >= 'A'.charCodeAt(0) && (c => c.charCodeAt == null ? c : c.charCodeAt(0))(ch) <= 'Z'.charCodeAt(0)) || ((c => c.charCodeAt == null ? c : c.charCodeAt(0))(ch) >= 'a'.charCodeAt(0) && (c => c.charCodeAt == null ? c : c.charCodeAt(0))(ch) <= 'z'.charCodeAt(0));
                        }
                        /**
                         * <p>Checks whether the character is ASCII 7 bit alphabetic upper case.</p>
                         *
                         * <pre>
                         * CharUtils.isAsciiAlphaUpper('a')  = false
                         * CharUtils.isAsciiAlphaUpper('A')  = true
                         * CharUtils.isAsciiAlphaUpper('3')  = false
                         * CharUtils.isAsciiAlphaUpper('-')  = false
                         * CharUtils.isAsciiAlphaUpper('\n') = false
                         * CharUtils.isAsciiAlphaUpper('&copy;') = false
                         * </pre>
                         *
                         * @param {string} ch  the character to check
                         * @return {boolean} true if between 65 and 90 inclusive
                         */
                        static isAsciiAlphaUpper(ch) {
                            return (c => c.charCodeAt == null ? c : c.charCodeAt(0))(ch) >= 'A'.charCodeAt(0) && (c => c.charCodeAt == null ? c : c.charCodeAt(0))(ch) <= 'Z'.charCodeAt(0);
                        }
                        /**
                         * <p>Checks whether the character is ASCII 7 bit alphabetic lower case.</p>
                         *
                         * <pre>
                         * CharUtils.isAsciiAlphaLower('a')  = true
                         * CharUtils.isAsciiAlphaLower('A')  = false
                         * CharUtils.isAsciiAlphaLower('3')  = false
                         * CharUtils.isAsciiAlphaLower('-')  = false
                         * CharUtils.isAsciiAlphaLower('\n') = false
                         * CharUtils.isAsciiAlphaLower('&copy;') = false
                         * </pre>
                         *
                         * @param {string} ch  the character to check
                         * @return {boolean} true if between 97 and 122 inclusive
                         */
                        static isAsciiAlphaLower(ch) {
                            return (c => c.charCodeAt == null ? c : c.charCodeAt(0))(ch) >= 'a'.charCodeAt(0) && (c => c.charCodeAt == null ? c : c.charCodeAt(0))(ch) <= 'z'.charCodeAt(0);
                        }
                        /**
                         * <p>Checks whether the character is ASCII 7 bit numeric.</p>
                         *
                         * <pre>
                         * CharUtils.isAsciiNumeric('a')  = false
                         * CharUtils.isAsciiNumeric('A')  = false
                         * CharUtils.isAsciiNumeric('3')  = true
                         * CharUtils.isAsciiNumeric('-')  = false
                         * CharUtils.isAsciiNumeric('\n') = false
                         * CharUtils.isAsciiNumeric('&copy;') = false
                         * </pre>
                         *
                         * @param {string} ch  the character to check
                         * @return {boolean} true if between 48 and 57 inclusive
                         */
                        static isAsciiNumeric(ch) {
                            return (c => c.charCodeAt == null ? c : c.charCodeAt(0))(ch) >= '0'.charCodeAt(0) && (c => c.charCodeAt == null ? c : c.charCodeAt(0))(ch) <= '9'.charCodeAt(0);
                        }
                        /**
                         * <p>Checks whether the character is ASCII 7 bit numeric.</p>
                         *
                         * <pre>
                         * CharUtils.isAsciiAlphanumeric('a')  = true
                         * CharUtils.isAsciiAlphanumeric('A')  = true
                         * CharUtils.isAsciiAlphanumeric('3')  = true
                         * CharUtils.isAsciiAlphanumeric('-')  = false
                         * CharUtils.isAsciiAlphanumeric('\n') = false
                         * CharUtils.isAsciiAlphanumeric('&copy;') = false
                         * </pre>
                         *
                         * @param {string} ch  the character to check
                         * @return {boolean} true if between 48 and 57 or 65 and 90 or 97 and 122 inclusive
                         */
                        static isAsciiAlphanumeric(ch) {
                            return ((c => c.charCodeAt == null ? c : c.charCodeAt(0))(ch) >= 'A'.charCodeAt(0) && (c => c.charCodeAt == null ? c : c.charCodeAt(0))(ch) <= 'Z'.charCodeAt(0)) || ((c => c.charCodeAt == null ? c : c.charCodeAt(0))(ch) >= 'a'.charCodeAt(0) && (c => c.charCodeAt == null ? c : c.charCodeAt(0))(ch) <= 'z'.charCodeAt(0)) || ((c => c.charCodeAt == null ? c : c.charCodeAt(0))(ch) >= '0'.charCodeAt(0) && (c => c.charCodeAt == null ? c : c.charCodeAt(0))(ch) <= '9'.charCodeAt(0));
                        }
                        /**
                         * Indicates whether {@code ch} is a high- (or leading-) surrogate code unit
                         * that is used for representing supplementary characters in UTF-16
                         * encoding.
                         *
                         * @param {string} ch
                         * the character to test.
                         * @return {boolean} {@code true} if {@code ch} is a high-surrogate code unit;
                         * {@code false} otherwise.
                         */
                        static isHighSurrogate(ch) {
                            return ('\ud800'.charCodeAt(0) <= (c => c.charCodeAt == null ? c : c.charCodeAt(0))(ch) && '\udbff'.charCodeAt(0) >= (c => c.charCodeAt == null ? c : c.charCodeAt(0))(ch));
                        }
                    }
                    CharUtils.__static_initialized = false;
                    CharUtils.CHAR_STRING = "\u0000\u0001\u0002\u0003\u0004\u0005\u0006\u0007\b\t\n\u000b\f\r\u000e\u000f\u0010\u0011\u0012\u0013\u0014\u0015\u0016\u0017\u0018\u0019\u001a\u001b\u001c\u001d\u001e\u001f !\"#$%&\'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u007f";
                    /**
                     * <code>
                     * </code> linefeed LF ('\n').
                     *
                     * @see <a href="http://java.sun.com/docs/books/jls/third_edition/html/lexical.html#101089">JLF: Escape Sequences
                     * for Character and String Literals</a>
                     * @since 2.2
                     */
                    CharUtils.LF = '\n';
                    /**
                     * <code>
                     * </code> carriage return CR ('\r').
                     *
                     * @see <a href="http://java.sun.com/docs/books/jls/third_edition/html/lexical.html#101089">JLF: Escape Sequences
                     * for Character and String Literals</a>
                     * @since 2.2
                     */
                    CharUtils.CR = '\r';
                    lang.CharUtils = CharUtils;
                    CharUtils["__class"] = "org.openprovenance.apache.commons.lang.CharUtils";
                })(lang = commons.lang || (commons.lang = {}));
            })(commons = apache.commons || (apache.commons = {}));
        })(apache = openprovenance.apache || (openprovenance.apache = {}));
    })(openprovenance = org.openprovenance || (org.openprovenance = {}));
})(org || (org = {}));
org.openprovenance.apache.commons.lang.CharUtils.__static_initialize();
