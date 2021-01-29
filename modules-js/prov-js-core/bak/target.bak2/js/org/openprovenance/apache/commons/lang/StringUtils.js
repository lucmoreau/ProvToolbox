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
                     * <p><code>StringUtils</code> instances should NOT be constructed in
                     * standard programming. Instead, the class should be used as
                     * <code>StringUtils.trim(" foo ");</code>.</p>
                     *
                     * <p>This constructor is public to permit tools that require a JavaBean
                     * instance to operate.</p>
                     * @class
                     * @author Apache Software Foundation
                     */
                    class StringUtils {
                        constructor() {
                        }
                        /**
                         * <p>Checks if a String is empty ("") or null.</p>
                         *
                         * <pre>
                         * StringUtils.isEmpty(null)      = true
                         * StringUtils.isEmpty("")        = true
                         * StringUtils.isEmpty(" ")       = false
                         * StringUtils.isEmpty("bob")     = false
                         * StringUtils.isEmpty("  bob  ") = false
                         * </pre>
                         *
                         * <p>NOTE: This method changed in Lang version 2.0.
                         * It no longer trims the String.
                         * That functionality is available in isBlank().</p>
                         *
                         * @param {string} str  the String to check, may be null
                         * @return {boolean} <code>true</code> if the String is empty or null
                         */
                        static isEmpty(str) {
                            return str == null || str.length === 0;
                        }
                        /**
                         * <p>Checks if a String is not empty ("") and not null.</p>
                         *
                         * <pre>
                         * StringUtils.isNotEmpty(null)      = false
                         * StringUtils.isNotEmpty("")        = false
                         * StringUtils.isNotEmpty(" ")       = true
                         * StringUtils.isNotEmpty("bob")     = true
                         * StringUtils.isNotEmpty("  bob  ") = true
                         * </pre>
                         *
                         * @param {string} str  the String to check, may be null
                         * @return {boolean} <code>true</code> if the String is not empty and not null
                         */
                        static isNotEmpty(str) {
                            return !StringUtils.isEmpty(str);
                        }
                        /**
                         * <p>Checks if a String is whitespace, empty ("") or null.</p>
                         *
                         * <pre>
                         * StringUtils.isBlank(null)      = true
                         * StringUtils.isBlank("")        = true
                         * StringUtils.isBlank(" ")       = true
                         * StringUtils.isBlank("bob")     = false
                         * StringUtils.isBlank("  bob  ") = false
                         * </pre>
                         *
                         * @param {string} str  the String to check, may be null
                         * @return {boolean} <code>true</code> if the String is null, empty or whitespace
                         * @since 2.0
                         */
                        static isBlank(str) {
                            let strLen;
                            if (str == null || (strLen = str.length) === 0) {
                                return true;
                            }
                            for (let i = 0; i < strLen; i++) {
                                {
                                    if ((javaemul.internal.CharacterHelper.isWhitespace(str.charAt(i)) === false)) {
                                        return false;
                                    }
                                }
                                ;
                            }
                            return true;
                        }
                        /**
                         * <p>Checks if a String is not empty (""), not null and not whitespace only.</p>
                         *
                         * <pre>
                         * StringUtils.isNotBlank(null)      = false
                         * StringUtils.isNotBlank("")        = false
                         * StringUtils.isNotBlank(" ")       = false
                         * StringUtils.isNotBlank("bob")     = true
                         * StringUtils.isNotBlank("  bob  ") = true
                         * </pre>
                         *
                         * @param {string} str  the String to check, may be null
                         * @return {boolean} <code>true</code> if the String is
                         * not empty and not null and not whitespace
                         * @since 2.0
                         */
                        static isNotBlank(str) {
                            return !StringUtils.isBlank(str);
                        }
                        /**
                         * <p>Removes control characters (char &lt;= 32) from both
                         * ends of this String, handling <code>null</code> by returning
                         * an empty String ("").</p>
                         *
                         * <pre>
                         * StringUtils.clean(null)          = ""
                         * StringUtils.clean("")            = ""
                         * StringUtils.clean("abc")         = "abc"
                         * StringUtils.clean("    abc    ") = "abc"
                         * StringUtils.clean("     ")       = ""
                         * </pre>
                         *
                         * @see String#trim()
                         * @param {string} str  the String to clean, may be null
                         * @return {string} the trimmed text, never <code>null</code>
                         * @deprecated Use the clearer named {@link #trimToEmpty(String)}.
                         * Method will be removed in Commons Lang 3.0.
                         */
                        static clean(str) {
                            return str == null ? StringUtils.EMPTY : str.trim();
                        }
                        /**
                         * <p>Removes control characters (char &lt;= 32) from both
                         * ends of this String, handling <code>null</code> by returning
                         * <code>null</code>.</p>
                         *
                         * <p>The String is trimmed using {@link String#trim()}.
                         * Trim removes start and end characters &lt;= 32.
                         * To strip whitespace use {@link #strip(String)}.</p>
                         *
                         * <p>To trim your choice of characters, use the
                         * {@link #strip(String, String)} methods.</p>
                         *
                         * <pre>
                         * StringUtils.trim(null)          = null
                         * StringUtils.trim("")            = ""
                         * StringUtils.trim("     ")       = ""
                         * StringUtils.trim("abc")         = "abc"
                         * StringUtils.trim("    abc    ") = "abc"
                         * </pre>
                         *
                         * @param {string} str  the String to be trimmed, may be null
                         * @return {string} the trimmed string, <code>null</code> if null String input
                         */
                        static trim(str) {
                            return str == null ? null : str.trim();
                        }
                        /**
                         * <p>Removes control characters (char &lt;= 32) from both
                         * ends of this String returning <code>null</code> if the String is
                         * empty ("") after the trim or if it is <code>null</code>.
                         *
                         * <p>The String is trimmed using {@link String#trim()}.
                         * Trim removes start and end characters &lt;= 32.
                         * To strip whitespace use {@link #stripToNull(String)}.</p>
                         *
                         * <pre>
                         * StringUtils.trimToNull(null)          = null
                         * StringUtils.trimToNull("")            = null
                         * StringUtils.trimToNull("     ")       = null
                         * StringUtils.trimToNull("abc")         = "abc"
                         * StringUtils.trimToNull("    abc    ") = "abc"
                         * </pre>
                         *
                         * @param {string} str  the String to be trimmed, may be null
                         * @return {string} the trimmed String,
                         * <code>null</code> if only chars &lt;= 32, empty or null String input
                         * @since 2.0
                         */
                        static trimToNull(str) {
                            const ts = StringUtils.trim(str);
                            return StringUtils.isEmpty(ts) ? null : ts;
                        }
                        /**
                         * <p>Removes control characters (char &lt;= 32) from both
                         * ends of this String returning an empty String ("") if the String
                         * is empty ("") after the trim or if it is <code>null</code>.
                         *
                         * <p>The String is trimmed using {@link String#trim()}.
                         * Trim removes start and end characters &lt;= 32.
                         * To strip whitespace use {@link #stripToEmpty(String)}.</p>
                         *
                         * <pre>
                         * StringUtils.trimToEmpty(null)          = ""
                         * StringUtils.trimToEmpty("")            = ""
                         * StringUtils.trimToEmpty("     ")       = ""
                         * StringUtils.trimToEmpty("abc")         = "abc"
                         * StringUtils.trimToEmpty("    abc    ") = "abc"
                         * </pre>
                         *
                         * @param {string} str  the String to be trimmed, may be null
                         * @return {string} the trimmed String, or an empty String if <code>null</code> input
                         * @since 2.0
                         */
                        static trimToEmpty(str) {
                            return str == null ? StringUtils.EMPTY : str.trim();
                        }
                        static strip$java_lang_String(str) {
                            return StringUtils.strip$java_lang_String$java_lang_String(str, null);
                        }
                        /**
                         * <p>Strips whitespace from the start and end of a String  returning
                         * <code>null</code> if the String is empty ("") after the strip.</p>
                         *
                         * <p>This is similar to {@link #trimToNull(String)} but removes whitespace.
                         * Whitespace is defined by {@link Character#isWhitespace(char)}.</p>
                         *
                         * <pre>
                         * StringUtils.stripToNull(null)     = null
                         * StringUtils.stripToNull("")       = null
                         * StringUtils.stripToNull("   ")    = null
                         * StringUtils.stripToNull("abc")    = "abc"
                         * StringUtils.stripToNull("  abc")  = "abc"
                         * StringUtils.stripToNull("abc  ")  = "abc"
                         * StringUtils.stripToNull(" abc ")  = "abc"
                         * StringUtils.stripToNull(" ab c ") = "ab c"
                         * </pre>
                         *
                         * @param {string} str  the String to be stripped, may be null
                         * @return {string} the stripped String,
                         * <code>null</code> if whitespace, empty or null String input
                         * @since 2.0
                         */
                        static stripToNull(str) {
                            if (str == null) {
                                return null;
                            }
                            str = StringUtils.strip$java_lang_String$java_lang_String(str, null);
                            return str.length === 0 ? null : str;
                        }
                        /**
                         * <p>Strips whitespace from the start and end of a String  returning
                         * an empty String if <code>null</code> input.</p>
                         *
                         * <p>This is similar to {@link #trimToEmpty(String)} but removes whitespace.
                         * Whitespace is defined by {@link Character#isWhitespace(char)}.</p>
                         *
                         * <pre>
                         * StringUtils.stripToEmpty(null)     = ""
                         * StringUtils.stripToEmpty("")       = ""
                         * StringUtils.stripToEmpty("   ")    = ""
                         * StringUtils.stripToEmpty("abc")    = "abc"
                         * StringUtils.stripToEmpty("  abc")  = "abc"
                         * StringUtils.stripToEmpty("abc  ")  = "abc"
                         * StringUtils.stripToEmpty(" abc ")  = "abc"
                         * StringUtils.stripToEmpty(" ab c ") = "ab c"
                         * </pre>
                         *
                         * @param {string} str  the String to be stripped, may be null
                         * @return {string} the trimmed String, or an empty String if <code>null</code> input
                         * @since 2.0
                         */
                        static stripToEmpty(str) {
                            return str == null ? StringUtils.EMPTY : StringUtils.strip$java_lang_String$java_lang_String(str, null);
                        }
                        static strip$java_lang_String$java_lang_String(str, stripChars) {
                            if (StringUtils.isEmpty(str)) {
                                return str;
                            }
                            str = StringUtils.stripStart(str, stripChars);
                            return StringUtils.stripEnd(str, stripChars);
                        }
                        /**
                         * <p>Strips any of a set of characters from the start and end of a String.
                         * This is similar to {@link String#trim()} but allows the characters
                         * to be stripped to be controlled.</p>
                         *
                         * <p>A <code>null</code> input String returns <code>null</code>.
                         * An empty string ("") input returns the empty string.</p>
                         *
                         * <p>If the stripChars String is <code>null</code>, whitespace is
                         * stripped as defined by {@link Character#isWhitespace(char)}.
                         * Alternatively use {@link #strip(String)}.</p>
                         *
                         * <pre>
                         * StringUtils.strip(null, *)          = null
                         * StringUtils.strip("", *)            = ""
                         * StringUtils.strip("abc", null)      = "abc"
                         * StringUtils.strip("  abc", null)    = "abc"
                         * StringUtils.strip("abc  ", null)    = "abc"
                         * StringUtils.strip(" abc ", null)    = "abc"
                         * StringUtils.strip("  abcyx", "xyz") = "  abc"
                         * </pre>
                         *
                         * @param {string} str  the String to remove characters from, may be null
                         * @param {string} stripChars  the characters to remove, null treated as whitespace
                         * @return {string} the stripped String, <code>null</code> if null String input
                         */
                        static strip(str, stripChars) {
                            if (((typeof str === 'string') || str === null) && ((typeof stripChars === 'string') || stripChars === null)) {
                                return org.openprovenance.apache.commons.lang.StringUtils.strip$java_lang_String$java_lang_String(str, stripChars);
                            }
                            else if (((typeof str === 'string') || str === null) && stripChars === undefined) {
                                return org.openprovenance.apache.commons.lang.StringUtils.strip$java_lang_String(str);
                            }
                            else
                                throw new Error('invalid overload');
                        }
                        /**
                         * <p>Strips any of a set of characters from the start of a String.</p>
                         *
                         * <p>A <code>null</code> input String returns <code>null</code>.
                         * An empty string ("") input returns the empty string.</p>
                         *
                         * <p>If the stripChars String is <code>null</code>, whitespace is
                         * stripped as defined by {@link Character#isWhitespace(char)}.</p>
                         *
                         * <pre>
                         * StringUtils.stripStart(null, *)          = null
                         * StringUtils.stripStart("", *)            = ""
                         * StringUtils.stripStart("abc", "")        = "abc"
                         * StringUtils.stripStart("abc", null)      = "abc"
                         * StringUtils.stripStart("  abc", null)    = "abc"
                         * StringUtils.stripStart("abc  ", null)    = "abc  "
                         * StringUtils.stripStart(" abc ", null)    = "abc "
                         * StringUtils.stripStart("yxabc  ", "xyz") = "abc  "
                         * </pre>
                         *
                         * @param {string} str  the String to remove characters from, may be null
                         * @param {string} stripChars  the characters to remove, null treated as whitespace
                         * @return {string} the stripped String, <code>null</code> if null String input
                         */
                        static stripStart(str, stripChars) {
                            let strLen;
                            if (str == null || (strLen = str.length) === 0) {
                                return str;
                            }
                            let start = 0;
                            if (stripChars == null) {
                                while (((start !== strLen) && javaemul.internal.CharacterHelper.isWhitespace(str.charAt(start)))) {
                                    {
                                        start++;
                                    }
                                }
                                ;
                            }
                            else if (stripChars.length === 0) {
                                return str;
                            }
                            else {
                                while (((start !== strLen) && (stripChars.indexOf(str.charAt(start)) !== StringUtils.INDEX_NOT_FOUND))) {
                                    {
                                        start++;
                                    }
                                }
                                ;
                            }
                            return str.substring(start);
                        }
                        /**
                         * <p>Strips any of a set of characters from the end of a String.</p>
                         *
                         * <p>A <code>null</code> input String returns <code>null</code>.
                         * An empty string ("") input returns the empty string.</p>
                         *
                         * <p>If the stripChars String is <code>null</code>, whitespace is
                         * stripped as defined by {@link Character#isWhitespace(char)}.</p>
                         *
                         * <pre>
                         * StringUtils.stripEnd(null, *)          = null
                         * StringUtils.stripEnd("", *)            = ""
                         * StringUtils.stripEnd("abc", "")        = "abc"
                         * StringUtils.stripEnd("abc", null)      = "abc"
                         * StringUtils.stripEnd("  abc", null)    = "  abc"
                         * StringUtils.stripEnd("abc  ", null)    = "abc"
                         * StringUtils.stripEnd(" abc ", null)    = " abc"
                         * StringUtils.stripEnd("  abcyx", "xyz") = "  abc"
                         * StringUtils.stripEnd("120.00", ".0")   = "12"
                         * </pre>
                         *
                         * @param {string} str  the String to remove characters from, may be null
                         * @param {string} stripChars  the set of characters to remove, null treated as whitespace
                         * @return {string} the stripped String, <code>null</code> if null String input
                         */
                        static stripEnd(str, stripChars) {
                            let end;
                            if (str == null || (end = str.length) === 0) {
                                return str;
                            }
                            if (stripChars == null) {
                                while (((end !== 0) && javaemul.internal.CharacterHelper.isWhitespace(str.charAt(end - 1)))) {
                                    {
                                        end--;
                                    }
                                }
                                ;
                            }
                            else if (stripChars.length === 0) {
                                return str;
                            }
                            else {
                                while (((end !== 0) && (stripChars.indexOf(str.charAt(end - 1)) !== StringUtils.INDEX_NOT_FOUND))) {
                                    {
                                        end--;
                                    }
                                }
                                ;
                            }
                            return str.substring(0, end);
                        }
                        static stripAll$java_lang_String_A(strs) {
                            return StringUtils.stripAll$java_lang_String_A$java_lang_String(strs, null);
                        }
                        static stripAll$java_lang_String_A$java_lang_String(strs, stripChars) {
                            let strsLen;
                            if (strs == null || (strsLen = strs.length) === 0) {
                                return strs;
                            }
                            const newArr = (s => { let a = []; while (s-- > 0)
                                a.push(null); return a; })(strsLen);
                            for (let i = 0; i < strsLen; i++) {
                                {
                                    newArr[i] = StringUtils.strip$java_lang_String$java_lang_String(strs[i], stripChars);
                                }
                                ;
                            }
                            return newArr;
                        }
                        /**
                         * <p>Strips any of a set of characters from the start and end of every
                         * String in an array.</p>
                         * Whitespace is defined by {link Character#isWhitespace(char)}.
                         *
                         * <p>A new array is returned each time, except for length zero.
                         * A <code>null</code> array will return <code>null</code>.
                         * An empty array will return itself.
                         * A <code>null</code> array entry will be ignored.
                         * A <code>null</code> stripChars will strip whitespace as defined by
                         * {@link Character#isWhitespace(char)}.</p>
                         *
                         * <pre>
                         * StringUtils.stripAll(null, *)                = null
                         * StringUtils.stripAll([], *)                  = []
                         * StringUtils.stripAll(["abc", "  abc"], null) = ["abc", "abc"]
                         * StringUtils.stripAll(["abc  ", null], null)  = ["abc", null]
                         * StringUtils.stripAll(["abc  ", null], "yz")  = ["abc  ", null]
                         * StringUtils.stripAll(["yabcz", null], "yz")  = ["abc", null]
                         * </pre>
                         *
                         * @param {java.lang.String[]} strs  the array to remove characters from, may be null
                         * @param {string} stripChars  the characters to remove, null treated as whitespace
                         * @return {java.lang.String[]} the stripped Strings, <code>null</code> if null array input
                         */
                        static stripAll(strs, stripChars) {
                            if (((strs != null && strs instanceof Array && (strs.length == 0 || strs[0] == null || (typeof strs[0] === 'string'))) || strs === null) && ((typeof stripChars === 'string') || stripChars === null)) {
                                return org.openprovenance.apache.commons.lang.StringUtils.stripAll$java_lang_String_A$java_lang_String(strs, stripChars);
                            }
                            else if (((strs != null && strs instanceof Array && (strs.length == 0 || strs[0] == null || (typeof strs[0] === 'string'))) || strs === null) && stripChars === undefined) {
                                return org.openprovenance.apache.commons.lang.StringUtils.stripAll$java_lang_String_A(strs);
                            }
                            else
                                throw new Error('invalid overload');
                        }
                        /**
                         * <p>Compares two Strings, returning <code>true</code> if they are equal.</p>
                         *
                         * <p><code>null</code>s are handled without exceptions. Two <code>null</code>
                         * references are considered to be equal. The comparison is case sensitive.</p>
                         *
                         * <pre>
                         * StringUtils.equals(null, null)   = true
                         * StringUtils.equals(null, "abc")  = false
                         * StringUtils.equals("abc", null)  = false
                         * StringUtils.equals("abc", "abc") = true
                         * StringUtils.equals("abc", "ABC") = false
                         * </pre>
                         *
                         * @see String#equals(Object)
                         * @param {string} str1  the first String, may be null
                         * @param {string} str2  the second String, may be null
                         * @return {boolean} <code>true</code> if the Strings are equal, case sensitive, or
                         * both <code>null</code>
                         */
                        static equals(str1, str2) {
                            return str1 == null ? str2 == null : str1 === str2;
                        }
                        /**
                         * <p>Compares two Strings, returning <code>true</code> if they are equal ignoring
                         * the case.</p>
                         *
                         * <p><code>null</code>s are handled without exceptions. Two <code>null</code>
                         * references are considered equal. Comparison is case insensitive.</p>
                         *
                         * <pre>
                         * StringUtils.equalsIgnoreCase(null, null)   = true
                         * StringUtils.equalsIgnoreCase(null, "abc")  = false
                         * StringUtils.equalsIgnoreCase("abc", null)  = false
                         * StringUtils.equalsIgnoreCase("abc", "abc") = true
                         * StringUtils.equalsIgnoreCase("abc", "ABC") = true
                         * </pre>
                         *
                         * @see String#equalsIgnoreCase(String)
                         * @param {string} str1  the first String, may be null
                         * @param {string} str2  the second String, may be null
                         * @return {boolean} <code>true</code> if the Strings are equal, case insensitive, or
                         * both <code>null</code>
                         */
                        static equalsIgnoreCase(str1, str2) {
                            return str1 == null ? str2 == null : /* equalsIgnoreCase */ ((o1, o2) => o1.toUpperCase() === (o2 === null ? o2 : o2.toUpperCase()))(str1, str2);
                        }
                        static indexOf$java_lang_String$char(str, searchChar) {
                            if (StringUtils.isEmpty(str)) {
                                return StringUtils.INDEX_NOT_FOUND;
                            }
                            return str.indexOf(searchChar);
                        }
                        static indexOf$java_lang_String$char$int(str, searchChar, startPos) {
                            if (StringUtils.isEmpty(str)) {
                                return StringUtils.INDEX_NOT_FOUND;
                            }
                            return str.indexOf(searchChar, startPos);
                        }
                        static indexOf$java_lang_String$java_lang_String(str, searchStr) {
                            if (str == null || searchStr == null) {
                                return StringUtils.INDEX_NOT_FOUND;
                            }
                            return str.indexOf(searchStr);
                        }
                        static ordinalIndexOf$java_lang_String$java_lang_String$int(str, searchStr, ordinal) {
                            return StringUtils.ordinalIndexOf$java_lang_String$java_lang_String$int$boolean(str, searchStr, ordinal, false);
                        }
                        static ordinalIndexOf$java_lang_String$java_lang_String$int$boolean(str, searchStr, ordinal, lastIndex) {
                            if (str == null || searchStr == null || ordinal <= 0) {
                                return StringUtils.INDEX_NOT_FOUND;
                            }
                            if (searchStr.length === 0) {
                                return lastIndex ? str.length : 0;
                            }
                            let found = 0;
                            let index = lastIndex ? str.length : StringUtils.INDEX_NOT_FOUND;
                            do {
                                {
                                    if (lastIndex) {
                                        index = str.lastIndexOf(searchStr, index - 1);
                                    }
                                    else {
                                        index = str.indexOf(searchStr, index + 1);
                                    }
                                    if (index < 0) {
                                        return index;
                                    }
                                    found++;
                                }
                            } while ((found < ordinal));
                            return index;
                        }
                        /**
                         * <p>Finds the n-th index within a String, handling <code>null</code>.
                         * This method uses {@link String#indexOf(String)}.</p>
                         *
                         * <p>A <code>null</code> String will return <code>-1</code>.</p>
                         *
                         * @param {string} str  the String to check, may be null
                         * @param {string} searchStr  the String to find, may be null
                         * @param {number} ordinal  the n-th <code>searchStr</code> to find
                         * @param {boolean} lastIndex true if lastOrdinalIndexOf() otherwise false if ordinalIndexOf()
                         * @return {number} the n-th index of the search String,
                         * <code>-1</code> (<code>INDEX_NOT_FOUND</code>) if no match or <code>null</code> string input
                         * @private
                         */
                        static ordinalIndexOf(str, searchStr, ordinal, lastIndex) {
                            if (((typeof str === 'string') || str === null) && ((typeof searchStr === 'string') || searchStr === null) && ((typeof ordinal === 'number') || ordinal === null) && ((typeof lastIndex === 'boolean') || lastIndex === null)) {
                                return org.openprovenance.apache.commons.lang.StringUtils.ordinalIndexOf$java_lang_String$java_lang_String$int$boolean(str, searchStr, ordinal, lastIndex);
                            }
                            else if (((typeof str === 'string') || str === null) && ((typeof searchStr === 'string') || searchStr === null) && ((typeof ordinal === 'number') || ordinal === null) && lastIndex === undefined) {
                                return org.openprovenance.apache.commons.lang.StringUtils.ordinalIndexOf$java_lang_String$java_lang_String$int(str, searchStr, ordinal);
                            }
                            else
                                throw new Error('invalid overload');
                        }
                        static indexOf$java_lang_String$java_lang_String$int(str, searchStr, startPos) {
                            if (str == null || searchStr == null) {
                                return StringUtils.INDEX_NOT_FOUND;
                            }
                            if (searchStr.length === 0 && startPos >= str.length) {
                                return str.length;
                            }
                            return str.indexOf(searchStr, startPos);
                        }
                        /**
                         * <p>Finds the first index within a String, handling <code>null</code>.
                         * This method uses {@link String#indexOf(String, int)}.</p>
                         *
                         * <p>A <code>null</code> String will return <code>-1</code>.
                         * A negative start position is treated as zero.
                         * An empty ("") search String always matches.
                         * A start position greater than the string length only matches
                         * an empty search String.</p>
                         *
                         * <pre>
                         * StringUtils.indexOf(null, *, *)          = -1
                         * StringUtils.indexOf(*, null, *)          = -1
                         * StringUtils.indexOf("", "", 0)           = 0
                         * StringUtils.indexOf("", *, 0)            = -1 (except when * = "")
                         * StringUtils.indexOf("aabaabaa", "a", 0)  = 0
                         * StringUtils.indexOf("aabaabaa", "b", 0)  = 2
                         * StringUtils.indexOf("aabaabaa", "ab", 0) = 1
                         * StringUtils.indexOf("aabaabaa", "b", 3)  = 5
                         * StringUtils.indexOf("aabaabaa", "b", 9)  = -1
                         * StringUtils.indexOf("aabaabaa", "b", -1) = 2
                         * StringUtils.indexOf("aabaabaa", "", 2)   = 2
                         * StringUtils.indexOf("abc", "", 9)        = 3
                         * </pre>
                         *
                         * @param {string} str  the String to check, may be null
                         * @param {string} searchStr  the String to find, may be null
                         * @param {number} startPos  the start position, negative treated as zero
                         * @return {number} the first index of the search String,
                         * -1 if no match or <code>null</code> string input
                         * @since 2.0
                         */
                        static indexOf(str, searchStr, startPos) {
                            if (((typeof str === 'string') || str === null) && ((typeof searchStr === 'string') || searchStr === null) && ((typeof startPos === 'number') || startPos === null)) {
                                return org.openprovenance.apache.commons.lang.StringUtils.indexOf$java_lang_String$java_lang_String$int(str, searchStr, startPos);
                            }
                            else if (((typeof str === 'string') || str === null) && ((typeof searchStr === 'string') || searchStr === null) && ((typeof startPos === 'number') || startPos === null)) {
                                return org.openprovenance.apache.commons.lang.StringUtils.indexOf$java_lang_String$char$int(str, searchStr, startPos);
                            }
                            else if (((typeof str === 'string') || str === null) && ((typeof searchStr === 'string') || searchStr === null) && startPos === undefined) {
                                return org.openprovenance.apache.commons.lang.StringUtils.indexOf$java_lang_String$java_lang_String(str, searchStr);
                            }
                            else if (((typeof str === 'string') || str === null) && ((typeof searchStr === 'string') || searchStr === null) && startPos === undefined) {
                                return org.openprovenance.apache.commons.lang.StringUtils.indexOf$java_lang_String$char(str, searchStr);
                            }
                            else
                                throw new Error('invalid overload');
                        }
                        static indexOfIgnoreCase$java_lang_String$java_lang_String(str, searchStr) {
                            return StringUtils.indexOfIgnoreCase$java_lang_String$java_lang_String$int(str, searchStr, 0);
                        }
                        static indexOfIgnoreCase$java_lang_String$java_lang_String$int(str, searchStr, startPos) {
                            if (str == null || searchStr == null) {
                                return StringUtils.INDEX_NOT_FOUND;
                            }
                            if (startPos < 0) {
                                startPos = 0;
                            }
                            const endLimit = (str.length - searchStr.length) + 1;
                            if (startPos > endLimit) {
                                return StringUtils.INDEX_NOT_FOUND;
                            }
                            if (searchStr.length === 0) {
                                return startPos;
                            }
                            for (let i = startPos; i < endLimit; i++) {
                                {
                                    if (str.regionMatches(true, i, searchStr, 0, searchStr.length)) {
                                        return i;
                                    }
                                }
                                ;
                            }
                            return StringUtils.INDEX_NOT_FOUND;
                        }
                        /**
                         * <p>Case in-sensitive find of the first index within a String
                         * from the specified position.</p>
                         *
                         * <p>A <code>null</code> String will return <code>-1</code>.
                         * A negative start position is treated as zero.
                         * An empty ("") search String always matches.
                         * A start position greater than the string length only matches
                         * an empty search String.</p>
                         *
                         * <pre>
                         * StringUtils.indexOfIgnoreCase(null, *, *)          = -1
                         * StringUtils.indexOfIgnoreCase(*, null, *)          = -1
                         * StringUtils.indexOfIgnoreCase("", "", 0)           = 0
                         * StringUtils.indexOfIgnoreCase("aabaabaa", "A", 0)  = 0
                         * StringUtils.indexOfIgnoreCase("aabaabaa", "B", 0)  = 2
                         * StringUtils.indexOfIgnoreCase("aabaabaa", "AB", 0) = 1
                         * StringUtils.indexOfIgnoreCase("aabaabaa", "B", 3)  = 5
                         * StringUtils.indexOfIgnoreCase("aabaabaa", "B", 9)  = -1
                         * StringUtils.indexOfIgnoreCase("aabaabaa", "B", -1) = 2
                         * StringUtils.indexOfIgnoreCase("aabaabaa", "", 2)   = 2
                         * StringUtils.indexOfIgnoreCase("abc", "", 9)        = 3
                         * </pre>
                         *
                         * @param {string} str  the String to check, may be null
                         * @param {string} searchStr  the String to find, may be null
                         * @param {number} startPos  the start position, negative treated as zero
                         * @return {number} the first index of the search String,
                         * -1 if no match or <code>null</code> string input
                         * @since 2.5
                         */
                        static indexOfIgnoreCase(str, searchStr, startPos) {
                            if (((typeof str === 'string') || str === null) && ((typeof searchStr === 'string') || searchStr === null) && ((typeof startPos === 'number') || startPos === null)) {
                                return org.openprovenance.apache.commons.lang.StringUtils.indexOfIgnoreCase$java_lang_String$java_lang_String$int(str, searchStr, startPos);
                            }
                            else if (((typeof str === 'string') || str === null) && ((typeof searchStr === 'string') || searchStr === null) && startPos === undefined) {
                                return org.openprovenance.apache.commons.lang.StringUtils.indexOfIgnoreCase$java_lang_String$java_lang_String(str, searchStr);
                            }
                            else
                                throw new Error('invalid overload');
                        }
                        static lastIndexOf$java_lang_String$char(str, searchChar) {
                            if (StringUtils.isEmpty(str)) {
                                return StringUtils.INDEX_NOT_FOUND;
                            }
                            return str.lastIndexOf(searchChar);
                        }
                        static lastIndexOf$java_lang_String$char$int(str, searchChar, startPos) {
                            if (StringUtils.isEmpty(str)) {
                                return StringUtils.INDEX_NOT_FOUND;
                            }
                            return str.lastIndexOf(searchChar, startPos);
                        }
                        static lastIndexOf$java_lang_String$java_lang_String(str, searchStr) {
                            if (str == null || searchStr == null) {
                                return StringUtils.INDEX_NOT_FOUND;
                            }
                            return str.lastIndexOf(searchStr);
                        }
                        /**
                         * <p>Finds the n-th last index within a String, handling <code>null</code>.
                         * This method uses {@link String#lastIndexOf(String)}.</p>
                         *
                         * <p>A <code>null</code> String will return <code>-1</code>.</p>
                         *
                         * <pre>
                         * StringUtils.lastOrdinalIndexOf(null, *, *)          = -1
                         * StringUtils.lastOrdinalIndexOf(*, null, *)          = -1
                         * StringUtils.lastOrdinalIndexOf("", "", *)           = 0
                         * StringUtils.lastOrdinalIndexOf("aabaabaa", "a", 1)  = 7
                         * StringUtils.lastOrdinalIndexOf("aabaabaa", "a", 2)  = 6
                         * StringUtils.lastOrdinalIndexOf("aabaabaa", "b", 1)  = 5
                         * StringUtils.lastOrdinalIndexOf("aabaabaa", "b", 2)  = 2
                         * StringUtils.lastOrdinalIndexOf("aabaabaa", "ab", 1) = 4
                         * StringUtils.lastOrdinalIndexOf("aabaabaa", "ab", 2) = 1
                         * StringUtils.lastOrdinalIndexOf("aabaabaa", "", 1)   = 8
                         * StringUtils.lastOrdinalIndexOf("aabaabaa", "", 2)   = 8
                         * </pre>
                         *
                         * <p>Note that 'tail(String str, int n)' may be implemented as: </p>
                         *
                         * <pre>
                         * str.substring(lastOrdinalIndexOf(str, "\n", n) + 1)
                         * </pre>
                         *
                         * @param {string} str  the String to check, may be null
                         * @param {string} searchStr  the String to find, may be null
                         * @param {number} ordinal  the n-th last <code>searchStr</code> to find
                         * @return {number} the n-th last index of the search String,
                         * <code>-1</code> (<code>INDEX_NOT_FOUND</code>) if no match or <code>null</code> string input
                         * @since 2.5
                         */
                        static lastOrdinalIndexOf(str, searchStr, ordinal) {
                            return StringUtils.ordinalIndexOf$java_lang_String$java_lang_String$int$boolean(str, searchStr, ordinal, true);
                        }
                        static lastIndexOf$java_lang_String$java_lang_String$int(str, searchStr, startPos) {
                            if (str == null || searchStr == null) {
                                return StringUtils.INDEX_NOT_FOUND;
                            }
                            return str.lastIndexOf(searchStr, startPos);
                        }
                        /**
                         * <p>Finds the first index within a String, handling <code>null</code>.
                         * This method uses {@link String#lastIndexOf(String, int)}.</p>
                         *
                         * <p>A <code>null</code> String will return <code>-1</code>.
                         * A negative start position returns <code>-1</code>.
                         * An empty ("") search String always matches unless the start position is negative.
                         * A start position greater than the string length searches the whole string.</p>
                         *
                         * <pre>
                         * StringUtils.lastIndexOf(null, *, *)          = -1
                         * StringUtils.lastIndexOf(*, null, *)          = -1
                         * StringUtils.lastIndexOf("aabaabaa", "a", 8)  = 7
                         * StringUtils.lastIndexOf("aabaabaa", "b", 8)  = 5
                         * StringUtils.lastIndexOf("aabaabaa", "ab", 8) = 4
                         * StringUtils.lastIndexOf("aabaabaa", "b", 9)  = 5
                         * StringUtils.lastIndexOf("aabaabaa", "b", -1) = -1
                         * StringUtils.lastIndexOf("aabaabaa", "a", 0)  = 0
                         * StringUtils.lastIndexOf("aabaabaa", "b", 0)  = -1
                         * </pre>
                         *
                         * @param {string} str  the String to check, may be null
                         * @param {string} searchStr  the String to find, may be null
                         * @param {number} startPos  the start position, negative treated as zero
                         * @return {number} the first index of the search String,
                         * -1 if no match or <code>null</code> string input
                         * @since 2.0
                         */
                        static lastIndexOf(str, searchStr, startPos) {
                            if (((typeof str === 'string') || str === null) && ((typeof searchStr === 'string') || searchStr === null) && ((typeof startPos === 'number') || startPos === null)) {
                                return org.openprovenance.apache.commons.lang.StringUtils.lastIndexOf$java_lang_String$java_lang_String$int(str, searchStr, startPos);
                            }
                            else if (((typeof str === 'string') || str === null) && ((typeof searchStr === 'string') || searchStr === null) && ((typeof startPos === 'number') || startPos === null)) {
                                return org.openprovenance.apache.commons.lang.StringUtils.lastIndexOf$java_lang_String$char$int(str, searchStr, startPos);
                            }
                            else if (((typeof str === 'string') || str === null) && ((typeof searchStr === 'string') || searchStr === null) && startPos === undefined) {
                                return org.openprovenance.apache.commons.lang.StringUtils.lastIndexOf$java_lang_String$java_lang_String(str, searchStr);
                            }
                            else if (((typeof str === 'string') || str === null) && ((typeof searchStr === 'string') || searchStr === null) && startPos === undefined) {
                                return org.openprovenance.apache.commons.lang.StringUtils.lastIndexOf$java_lang_String$char(str, searchStr);
                            }
                            else
                                throw new Error('invalid overload');
                        }
                        static lastIndexOfIgnoreCase$java_lang_String$java_lang_String(str, searchStr) {
                            if (str == null || searchStr == null) {
                                return StringUtils.INDEX_NOT_FOUND;
                            }
                            return StringUtils.lastIndexOfIgnoreCase$java_lang_String$java_lang_String$int(str, searchStr, str.length);
                        }
                        static lastIndexOfIgnoreCase$java_lang_String$java_lang_String$int(str, searchStr, startPos) {
                            if (str == null || searchStr == null) {
                                return StringUtils.INDEX_NOT_FOUND;
                            }
                            if (startPos > (str.length - searchStr.length)) {
                                startPos = str.length - searchStr.length;
                            }
                            if (startPos < 0) {
                                return StringUtils.INDEX_NOT_FOUND;
                            }
                            if (searchStr.length === 0) {
                                return startPos;
                            }
                            for (let i = startPos; i >= 0; i--) {
                                {
                                    if (str.regionMatches(true, i, searchStr, 0, searchStr.length)) {
                                        return i;
                                    }
                                }
                                ;
                            }
                            return StringUtils.INDEX_NOT_FOUND;
                        }
                        /**
                         * <p>Case in-sensitive find of the last index within a String
                         * from the specified position.</p>
                         *
                         * <p>A <code>null</code> String will return <code>-1</code>.
                         * A negative start position returns <code>-1</code>.
                         * An empty ("") search String always matches unless the start position is negative.
                         * A start position greater than the string length searches the whole string.</p>
                         *
                         * <pre>
                         * StringUtils.lastIndexOfIgnoreCase(null, *, *)          = -1
                         * StringUtils.lastIndexOfIgnoreCase(*, null, *)          = -1
                         * StringUtils.lastIndexOfIgnoreCase("aabaabaa", "A", 8)  = 7
                         * StringUtils.lastIndexOfIgnoreCase("aabaabaa", "B", 8)  = 5
                         * StringUtils.lastIndexOfIgnoreCase("aabaabaa", "AB", 8) = 4
                         * StringUtils.lastIndexOfIgnoreCase("aabaabaa", "B", 9)  = 5
                         * StringUtils.lastIndexOfIgnoreCase("aabaabaa", "B", -1) = -1
                         * StringUtils.lastIndexOfIgnoreCase("aabaabaa", "A", 0)  = 0
                         * StringUtils.lastIndexOfIgnoreCase("aabaabaa", "B", 0)  = -1
                         * </pre>
                         *
                         * @param {string} str  the String to check, may be null
                         * @param {string} searchStr  the String to find, may be null
                         * @param {number} startPos  the start position
                         * @return {number} the first index of the search String,
                         * -1 if no match or <code>null</code> string input
                         * @since 2.5
                         */
                        static lastIndexOfIgnoreCase(str, searchStr, startPos) {
                            if (((typeof str === 'string') || str === null) && ((typeof searchStr === 'string') || searchStr === null) && ((typeof startPos === 'number') || startPos === null)) {
                                return org.openprovenance.apache.commons.lang.StringUtils.lastIndexOfIgnoreCase$java_lang_String$java_lang_String$int(str, searchStr, startPos);
                            }
                            else if (((typeof str === 'string') || str === null) && ((typeof searchStr === 'string') || searchStr === null) && startPos === undefined) {
                                return org.openprovenance.apache.commons.lang.StringUtils.lastIndexOfIgnoreCase$java_lang_String$java_lang_String(str, searchStr);
                            }
                            else
                                throw new Error('invalid overload');
                        }
                        static contains$java_lang_String$char(str, searchChar) {
                            if (StringUtils.isEmpty(str)) {
                                return false;
                            }
                            return str.indexOf(searchChar) >= 0;
                        }
                        static contains$java_lang_String$java_lang_String(str, searchStr) {
                            if (str == null || searchStr == null) {
                                return false;
                            }
                            return str.indexOf(searchStr) >= 0;
                        }
                        /**
                         * <p>Checks if String contains a search String, handling <code>null</code>.
                         * This method uses {@link String#indexOf(String)}.</p>
                         *
                         * <p>A <code>null</code> String will return <code>false</code>.</p>
                         *
                         * <pre>
                         * StringUtils.contains(null, *)     = false
                         * StringUtils.contains(*, null)     = false
                         * StringUtils.contains("", "")      = true
                         * StringUtils.contains("abc", "")   = true
                         * StringUtils.contains("abc", "a")  = true
                         * StringUtils.contains("abc", "z")  = false
                         * </pre>
                         *
                         * @param {string} str  the String to check, may be null
                         * @param {string} searchStr  the String to find, may be null
                         * @return {boolean} true if the String contains the search String,
                         * false if not or <code>null</code> string input
                         * @since 2.0
                         */
                        static contains(str, searchStr) {
                            if (((typeof str === 'string') || str === null) && ((typeof searchStr === 'string') || searchStr === null)) {
                                return org.openprovenance.apache.commons.lang.StringUtils.contains$java_lang_String$java_lang_String(str, searchStr);
                            }
                            else if (((typeof str === 'string') || str === null) && ((typeof searchStr === 'string') || searchStr === null)) {
                                return org.openprovenance.apache.commons.lang.StringUtils.contains$java_lang_String$char(str, searchStr);
                            }
                            else
                                throw new Error('invalid overload');
                        }
                        /**
                         * <p>Checks if String contains a search String irrespective of case,
                         * handling <code>null</code>. Case-insensitivity is defined as by
                         * {@link String#equalsIgnoreCase(String)}.
                         *
                         * <p>A <code>null</code> String will return <code>false</code>.</p>
                         *
                         * <pre>
                         * StringUtils.contains(null, *) = false
                         * StringUtils.contains(*, null) = false
                         * StringUtils.contains("", "") = true
                         * StringUtils.contains("abc", "") = true
                         * StringUtils.contains("abc", "a") = true
                         * StringUtils.contains("abc", "z") = false
                         * StringUtils.contains("abc", "A") = true
                         * StringUtils.contains("abc", "Z") = false
                         * </pre>
                         *
                         * @param {string} str  the String to check, may be null
                         * @param {string} searchStr  the String to find, may be null
                         * @return {boolean} true if the String contains the search String irrespective of
                         * case or false if not or <code>null</code> string input
                         */
                        static containsIgnoreCase(str, searchStr) {
                            if (str == null || searchStr == null) {
                                return false;
                            }
                            const len = searchStr.length;
                            const max = str.length - len;
                            for (let i = 0; i <= max; i++) {
                                {
                                    if (str.regionMatches(true, i, searchStr, 0, len)) {
                                        return true;
                                    }
                                }
                                ;
                            }
                            return false;
                        }
                        static indexOfAny$java_lang_String$char_A(str, searchChars) {
                            if (StringUtils.isEmpty(str) || org.openprovenance.apache.commons.lang.ArrayUtils.isEmpty$char_A(searchChars)) {
                                return StringUtils.INDEX_NOT_FOUND;
                            }
                            const csLen = str.length;
                            const csLast = csLen - 1;
                            const searchLen = searchChars.length;
                            const searchLast = searchLen - 1;
                            for (let i = 0; i < csLen; i++) {
                                {
                                    const ch = str.charAt(i);
                                    for (let j = 0; j < searchLen; j++) {
                                        {
                                            if ((c => c.charCodeAt == null ? c : c.charCodeAt(0))(searchChars[j]) == (c => c.charCodeAt == null ? c : c.charCodeAt(0))(ch)) {
                                                if (i < csLast && j < searchLast && org.openprovenance.apache.commons.lang.CharUtils.isHighSurrogate(ch)) {
                                                    if ((c => c.charCodeAt == null ? c : c.charCodeAt(0))(searchChars[j + 1]) == (c => c.charCodeAt == null ? c : c.charCodeAt(0))(str.charAt(i + 1))) {
                                                        return i;
                                                    }
                                                }
                                                else {
                                                    return i;
                                                }
                                            }
                                        }
                                        ;
                                    }
                                }
                                ;
                            }
                            return StringUtils.INDEX_NOT_FOUND;
                        }
                        /**
                         * <p>Search a String to find the first index of any
                         * character in the given set of characters.</p>
                         *
                         * <p>A <code>null</code> String will return <code>-1</code>.
                         * A <code>null</code> or zero length search array will return <code>-1</code>.</p>
                         *
                         * <pre>
                         * StringUtils.indexOfAny(null, *)                = -1
                         * StringUtils.indexOfAny("", *)                  = -1
                         * StringUtils.indexOfAny(*, null)                = -1
                         * StringUtils.indexOfAny(*, [])                  = -1
                         * StringUtils.indexOfAny("zzabyycdxx",['z','a']) = 0
                         * StringUtils.indexOfAny("zzabyycdxx",['b','y']) = 3
                         * StringUtils.indexOfAny("aba", ['z'])           = -1
                         * </pre>
                         *
                         * @param {string} str  the String to check, may be null
                         * @param {char[]} searchChars  the chars to search for, may be null
                         * @return {number} the index of any of the chars, -1 if no match or null input
                         * @since 2.0
                         */
                        static indexOfAny(str, searchChars) {
                            if (((typeof str === 'string') || str === null) && ((searchChars != null && searchChars instanceof Array && (searchChars.length == 0 || searchChars[0] == null || (typeof searchChars[0] === 'string'))) || searchChars === null)) {
                                return org.openprovenance.apache.commons.lang.StringUtils.indexOfAny$java_lang_String$char_A(str, searchChars);
                            }
                            else if (((typeof str === 'string') || str === null) && ((typeof searchChars === 'string') || searchChars === null)) {
                                return org.openprovenance.apache.commons.lang.StringUtils.indexOfAny$java_lang_String$java_lang_String(str, searchChars);
                            }
                            else if (((typeof str === 'string') || str === null) && ((searchChars != null && searchChars instanceof Array && (searchChars.length == 0 || searchChars[0] == null || (typeof searchChars[0] === 'string'))) || searchChars === null)) {
                                return org.openprovenance.apache.commons.lang.StringUtils.indexOfAny$java_lang_String$java_lang_String_A(str, searchChars);
                            }
                            else
                                throw new Error('invalid overload');
                        }
                        static indexOfAny$java_lang_String$java_lang_String(str, searchChars) {
                            if (StringUtils.isEmpty(str) || StringUtils.isEmpty(searchChars)) {
                                return StringUtils.INDEX_NOT_FOUND;
                            }
                            return StringUtils.indexOfAny$java_lang_String$char_A(str, /* toCharArray */ (searchChars).split(''));
                        }
                        static containsAny$java_lang_String$char_A(str, searchChars) {
                            if (StringUtils.isEmpty(str) || org.openprovenance.apache.commons.lang.ArrayUtils.isEmpty$char_A(searchChars)) {
                                return false;
                            }
                            const csLength = str.length;
                            const searchLength = searchChars.length;
                            const csLast = csLength - 1;
                            const searchLast = searchLength - 1;
                            for (let i = 0; i < csLength; i++) {
                                {
                                    const ch = str.charAt(i);
                                    for (let j = 0; j < searchLength; j++) {
                                        {
                                            if ((c => c.charCodeAt == null ? c : c.charCodeAt(0))(searchChars[j]) == (c => c.charCodeAt == null ? c : c.charCodeAt(0))(ch)) {
                                                if (org.openprovenance.apache.commons.lang.CharUtils.isHighSurrogate(ch)) {
                                                    if (j === searchLast) {
                                                        return true;
                                                    }
                                                    if (i < csLast && (c => c.charCodeAt == null ? c : c.charCodeAt(0))(searchChars[j + 1]) == (c => c.charCodeAt == null ? c : c.charCodeAt(0))(str.charAt(i + 1))) {
                                                        return true;
                                                    }
                                                }
                                                else {
                                                    return true;
                                                }
                                            }
                                        }
                                        ;
                                    }
                                }
                                ;
                            }
                            return false;
                        }
                        /**
                         * <p>Checks if the String contains any character in the given
                         * set of characters.</p>
                         *
                         * <p>A <code>null</code> String will return <code>false</code>.
                         * A <code>null</code> or zero length search array will return <code>false</code>.</p>
                         *
                         * <pre>
                         * StringUtils.containsAny(null, *)                = false
                         * StringUtils.containsAny("", *)                  = false
                         * StringUtils.containsAny(*, null)                = false
                         * StringUtils.containsAny(*, [])                  = false
                         * StringUtils.containsAny("zzabyycdxx",['z','a']) = true
                         * StringUtils.containsAny("zzabyycdxx",['b','y']) = true
                         * StringUtils.containsAny("aba", ['z'])           = false
                         * </pre>
                         *
                         * @param {string} str  the String to check, may be null
                         * @param {char[]} searchChars  the chars to search for, may be null
                         * @return {boolean} the <code>true</code> if any of the chars are found,
                         * <code>false</code> if no match or null input
                         * @since 2.4
                         */
                        static containsAny(str, searchChars) {
                            if (((typeof str === 'string') || str === null) && ((searchChars != null && searchChars instanceof Array && (searchChars.length == 0 || searchChars[0] == null || (typeof searchChars[0] === 'string'))) || searchChars === null)) {
                                return org.openprovenance.apache.commons.lang.StringUtils.containsAny$java_lang_String$char_A(str, searchChars);
                            }
                            else if (((typeof str === 'string') || str === null) && ((typeof searchChars === 'string') || searchChars === null)) {
                                return org.openprovenance.apache.commons.lang.StringUtils.containsAny$java_lang_String$java_lang_String(str, searchChars);
                            }
                            else
                                throw new Error('invalid overload');
                        }
                        static containsAny$java_lang_String$java_lang_String(str, searchChars) {
                            if (searchChars == null) {
                                return false;
                            }
                            return StringUtils.containsAny$java_lang_String$char_A(str, /* toCharArray */ (searchChars).split(''));
                        }
                        static indexOfAnyBut$java_lang_String$char_A(str, searchChars) {
                            if (StringUtils.isEmpty(str) || org.openprovenance.apache.commons.lang.ArrayUtils.isEmpty$char_A(searchChars)) {
                                return StringUtils.INDEX_NOT_FOUND;
                            }
                            const csLen = str.length;
                            const csLast = csLen - 1;
                            const searchLen = searchChars.length;
                            const searchLast = searchLen - 1;
                            outer: for (let i = 0; i < csLen; i++) {
                                {
                                    const ch = str.charAt(i);
                                    for (let j = 0; j < searchLen; j++) {
                                        {
                                            if ((c => c.charCodeAt == null ? c : c.charCodeAt(0))(searchChars[j]) == (c => c.charCodeAt == null ? c : c.charCodeAt(0))(ch)) {
                                                if (i < csLast && j < searchLast && org.openprovenance.apache.commons.lang.CharUtils.isHighSurrogate(ch)) {
                                                    if ((c => c.charCodeAt == null ? c : c.charCodeAt(0))(searchChars[j + 1]) == (c => c.charCodeAt == null ? c : c.charCodeAt(0))(str.charAt(i + 1))) {
                                                        continue outer;
                                                    }
                                                }
                                                else {
                                                    continue outer;
                                                }
                                            }
                                        }
                                        ;
                                    }
                                    return i;
                                }
                                ;
                            }
                            return StringUtils.INDEX_NOT_FOUND;
                        }
                        /**
                         * <p>Search a String to find the first index of any
                         * character not in the given set of characters.</p>
                         *
                         * <p>A <code>null</code> String will return <code>-1</code>.
                         * A <code>null</code> or zero length search array will return <code>-1</code>.</p>
                         *
                         * <pre>
                         * StringUtils.indexOfAnyBut(null, *)                              = -1
                         * StringUtils.indexOfAnyBut("", *)                                = -1
                         * StringUtils.indexOfAnyBut(*, null)                              = -1
                         * StringUtils.indexOfAnyBut(*, [])                                = -1
                         * StringUtils.indexOfAnyBut("zzabyycdxx", new char[] {'z', 'a'} ) = 3
                         * StringUtils.indexOfAnyBut("aba", new char[] {'z'} )             = 0
                         * StringUtils.indexOfAnyBut("aba", new char[] {'a', 'b'} )        = -1
                         * </pre>
                         *
                         * @param {string} str  the String to check, may be null
                         * @param {char[]} searchChars  the chars to search for, may be null
                         * @return {number} the index of any of the chars, -1 if no match or null input
                         * @since 2.0
                         */
                        static indexOfAnyBut(str, searchChars) {
                            if (((typeof str === 'string') || str === null) && ((searchChars != null && searchChars instanceof Array && (searchChars.length == 0 || searchChars[0] == null || (typeof searchChars[0] === 'string'))) || searchChars === null)) {
                                return org.openprovenance.apache.commons.lang.StringUtils.indexOfAnyBut$java_lang_String$char_A(str, searchChars);
                            }
                            else if (((typeof str === 'string') || str === null) && ((typeof searchChars === 'string') || searchChars === null)) {
                                return org.openprovenance.apache.commons.lang.StringUtils.indexOfAnyBut$java_lang_String$java_lang_String(str, searchChars);
                            }
                            else
                                throw new Error('invalid overload');
                        }
                        static indexOfAnyBut$java_lang_String$java_lang_String(str, searchChars) {
                            if (StringUtils.isEmpty(str) || StringUtils.isEmpty(searchChars)) {
                                return StringUtils.INDEX_NOT_FOUND;
                            }
                            const strLen = str.length;
                            for (let i = 0; i < strLen; i++) {
                                {
                                    const ch = str.charAt(i);
                                    const chFound = searchChars.indexOf(ch) >= 0;
                                    if (i + 1 < strLen && org.openprovenance.apache.commons.lang.CharUtils.isHighSurrogate(ch)) {
                                        const ch2 = str.charAt(i + 1);
                                        if (chFound && searchChars.indexOf(ch2) < 0) {
                                            return i;
                                        }
                                    }
                                    else {
                                        if (!chFound) {
                                            return i;
                                        }
                                    }
                                }
                                ;
                            }
                            return StringUtils.INDEX_NOT_FOUND;
                        }
                        static containsOnly$java_lang_String$char_A(str, valid) {
                            if ((valid == null) || (str == null)) {
                                return false;
                            }
                            if (str.length === 0) {
                                return true;
                            }
                            if (valid.length === 0) {
                                return false;
                            }
                            return StringUtils.indexOfAnyBut$java_lang_String$char_A(str, valid) === StringUtils.INDEX_NOT_FOUND;
                        }
                        /**
                         * <p>Checks if the String contains only certain characters.</p>
                         *
                         * <p>A <code>null</code> String will return <code>false</code>.
                         * A <code>null</code> valid character array will return <code>false</code>.
                         * An empty String (length()=0) always returns <code>true</code>.</p>
                         *
                         * <pre>
                         * StringUtils.containsOnly(null, *)       = false
                         * StringUtils.containsOnly(*, null)       = false
                         * StringUtils.containsOnly("", *)         = true
                         * StringUtils.containsOnly("ab", '')      = false
                         * StringUtils.containsOnly("abab", 'abc') = true
                         * StringUtils.containsOnly("ab1", 'abc')  = false
                         * StringUtils.containsOnly("abz", 'abc')  = false
                         * </pre>
                         *
                         * @param {string} str  the String to check, may be null
                         * @param {char[]} valid  an array of valid chars, may be null
                         * @return {boolean} true if it only contains valid chars and is non-null
                         */
                        static containsOnly(str, valid) {
                            if (((typeof str === 'string') || str === null) && ((valid != null && valid instanceof Array && (valid.length == 0 || valid[0] == null || (typeof valid[0] === 'string'))) || valid === null)) {
                                return org.openprovenance.apache.commons.lang.StringUtils.containsOnly$java_lang_String$char_A(str, valid);
                            }
                            else if (((typeof str === 'string') || str === null) && ((typeof valid === 'string') || valid === null)) {
                                return org.openprovenance.apache.commons.lang.StringUtils.containsOnly$java_lang_String$java_lang_String(str, valid);
                            }
                            else
                                throw new Error('invalid overload');
                        }
                        static containsOnly$java_lang_String$java_lang_String(str, validChars) {
                            if (str == null || validChars == null) {
                                return false;
                            }
                            return StringUtils.containsOnly$java_lang_String$char_A(str, /* toCharArray */ (validChars).split(''));
                        }
                        static containsNone$java_lang_String$char_A(str, searchChars) {
                            if (str == null || searchChars == null) {
                                return true;
                            }
                            const csLen = str.length;
                            const csLast = csLen - 1;
                            const searchLen = searchChars.length;
                            const searchLast = searchLen - 1;
                            for (let i = 0; i < csLen; i++) {
                                {
                                    const ch = str.charAt(i);
                                    for (let j = 0; j < searchLen; j++) {
                                        {
                                            if ((c => c.charCodeAt == null ? c : c.charCodeAt(0))(searchChars[j]) == (c => c.charCodeAt == null ? c : c.charCodeAt(0))(ch)) {
                                                if (org.openprovenance.apache.commons.lang.CharUtils.isHighSurrogate(ch)) {
                                                    if (j === searchLast) {
                                                        return false;
                                                    }
                                                    if (i < csLast && (c => c.charCodeAt == null ? c : c.charCodeAt(0))(searchChars[j + 1]) == (c => c.charCodeAt == null ? c : c.charCodeAt(0))(str.charAt(i + 1))) {
                                                        return false;
                                                    }
                                                }
                                                else {
                                                    return false;
                                                }
                                            }
                                        }
                                        ;
                                    }
                                }
                                ;
                            }
                            return true;
                        }
                        /**
                         * <p>Checks that the String does not contain certain characters.</p>
                         *
                         * <p>A <code>null</code> String will return <code>true</code>.
                         * A <code>null</code> invalid character array will return <code>true</code>.
                         * An empty String (length()=0) always returns true.</p>
                         *
                         * <pre>
                         * StringUtils.containsNone(null, *)       = true
                         * StringUtils.containsNone(*, null)       = true
                         * StringUtils.containsNone("", *)         = true
                         * StringUtils.containsNone("ab", '')      = true
                         * StringUtils.containsNone("abab", 'xyz') = true
                         * StringUtils.containsNone("ab1", 'xyz')  = true
                         * StringUtils.containsNone("abz", 'xyz')  = false
                         * </pre>
                         *
                         * @param {string} str  the String to check, may be null
                         * @param {char[]} searchChars  an array of invalid chars, may be null
                         * @return {boolean} true if it contains none of the invalid chars, or is null
                         * @since 2.0
                         */
                        static containsNone(str, searchChars) {
                            if (((typeof str === 'string') || str === null) && ((searchChars != null && searchChars instanceof Array && (searchChars.length == 0 || searchChars[0] == null || (typeof searchChars[0] === 'string'))) || searchChars === null)) {
                                return org.openprovenance.apache.commons.lang.StringUtils.containsNone$java_lang_String$char_A(str, searchChars);
                            }
                            else if (((typeof str === 'string') || str === null) && ((typeof searchChars === 'string') || searchChars === null)) {
                                return org.openprovenance.apache.commons.lang.StringUtils.containsNone$java_lang_String$java_lang_String(str, searchChars);
                            }
                            else
                                throw new Error('invalid overload');
                        }
                        static containsNone$java_lang_String$java_lang_String(str, invalidChars) {
                            if (str == null || invalidChars == null) {
                                return true;
                            }
                            return StringUtils.containsNone$java_lang_String$char_A(str, /* toCharArray */ (invalidChars).split(''));
                        }
                        static indexOfAny$java_lang_String$java_lang_String_A(str, searchStrs) {
                            if ((str == null) || (searchStrs == null)) {
                                return StringUtils.INDEX_NOT_FOUND;
                            }
                            const sz = searchStrs.length;
                            let ret = 2147483647;
                            let tmp = 0;
                            for (let i = 0; i < sz; i++) {
                                {
                                    const search = searchStrs[i];
                                    if (search == null) {
                                        continue;
                                    }
                                    tmp = str.indexOf(search);
                                    if (tmp === StringUtils.INDEX_NOT_FOUND) {
                                        continue;
                                    }
                                    if (tmp < ret) {
                                        ret = tmp;
                                    }
                                }
                                ;
                            }
                            return (ret === 2147483647) ? StringUtils.INDEX_NOT_FOUND : ret;
                        }
                        /**
                         * <p>Find the latest index of any of a set of potential substrings.</p>
                         *
                         * <p>A <code>null</code> String will return <code>-1</code>.
                         * A <code>null</code> search array will return <code>-1</code>.
                         * A <code>null</code> or zero length search array entry will be ignored,
                         * but a search array containing "" will return the length of <code>str</code>
                         * if <code>str</code> is not null. This method uses {@link String#indexOf(String)}</p>
                         *
                         * <pre>
                         * StringUtils.lastIndexOfAny(null, *)                   = -1
                         * StringUtils.lastIndexOfAny(*, null)                   = -1
                         * StringUtils.lastIndexOfAny(*, [])                     = -1
                         * StringUtils.lastIndexOfAny(*, [null])                 = -1
                         * StringUtils.lastIndexOfAny("zzabyycdxx", ["ab","cd"]) = 6
                         * StringUtils.lastIndexOfAny("zzabyycdxx", ["cd","ab"]) = 6
                         * StringUtils.lastIndexOfAny("zzabyycdxx", ["mn","op"]) = -1
                         * StringUtils.lastIndexOfAny("zzabyycdxx", ["mn","op"]) = -1
                         * StringUtils.lastIndexOfAny("zzabyycdxx", ["mn",""])   = 10
                         * </pre>
                         *
                         * @param {string} str  the String to check, may be null
                         * @param {java.lang.String[]} searchStrs  the Strings to search for, may be null
                         * @return {number} the last index of any of the Strings, -1 if no match
                         */
                        static lastIndexOfAny(str, searchStrs) {
                            if ((str == null) || (searchStrs == null)) {
                                return StringUtils.INDEX_NOT_FOUND;
                            }
                            const sz = searchStrs.length;
                            let ret = StringUtils.INDEX_NOT_FOUND;
                            let tmp = 0;
                            for (let i = 0; i < sz; i++) {
                                {
                                    const search = searchStrs[i];
                                    if (search == null) {
                                        continue;
                                    }
                                    tmp = str.lastIndexOf(search);
                                    if (tmp > ret) {
                                        ret = tmp;
                                    }
                                }
                                ;
                            }
                            return ret;
                        }
                        static substring$java_lang_String$int(str, start) {
                            if (str == null) {
                                return null;
                            }
                            if (start < 0) {
                                start = str.length + start;
                            }
                            if (start < 0) {
                                start = 0;
                            }
                            if (start > str.length) {
                                return StringUtils.EMPTY;
                            }
                            return str.substring(start);
                        }
                        static substring$java_lang_String$int$int(str, start, end) {
                            if (str == null) {
                                return null;
                            }
                            if (end < 0) {
                                end = str.length + end;
                            }
                            if (start < 0) {
                                start = str.length + start;
                            }
                            if (end > str.length) {
                                end = str.length;
                            }
                            if (start > end) {
                                return StringUtils.EMPTY;
                            }
                            if (start < 0) {
                                start = 0;
                            }
                            if (end < 0) {
                                end = 0;
                            }
                            return str.substring(start, end);
                        }
                        /**
                         * <p>Gets a substring from the specified String avoiding exceptions.</p>
                         *
                         * <p>A negative start position can be used to start/end <code>n</code>
                         * characters from the end of the String.</p>
                         *
                         * <p>The returned substring starts with the character in the <code>start</code>
                         * position and ends before the <code>end</code> position. All position counting is
                         * zero-based -- i.e1., to start at the beginning of the string use
                         * <code>start = 0</code>. Negative start and end positions can be used to
                         * specify offsets relative to the end of the String.</p>
                         *
                         * <p>If <code>start</code> is not strictly to the left of <code>end</code>, ""
                         * is returned.</p>
                         *
                         * <pre>
                         * StringUtils.substring(null, *, *)    = null
                         * StringUtils.substring("", * ,  *)    = "";
                         * StringUtils.substring("abc", 0, 2)   = "ab"
                         * StringUtils.substring("abc", 2, 0)   = ""
                         * StringUtils.substring("abc", 2, 4)   = "c"
                         * StringUtils.substring("abc", 4, 6)   = ""
                         * StringUtils.substring("abc", 2, 2)   = ""
                         * StringUtils.substring("abc", -2, -1) = "b"
                         * StringUtils.substring("abc", -4, 2)  = "ab"
                         * </pre>
                         *
                         * @param {string} str  the String to get the substring from, may be null
                         * @param {number} start  the position to start from, negative means
                         * count back from the end of the String by this many characters
                         * @param {number} end  the position to end at (exclusive), negative means
                         * count back from the end of the String by this many characters
                         * @return {string} substring from start position to end positon,
                         * <code>null</code> if null String input
                         */
                        static substring(str, start, end) {
                            if (((typeof str === 'string') || str === null) && ((typeof start === 'number') || start === null) && ((typeof end === 'number') || end === null)) {
                                return org.openprovenance.apache.commons.lang.StringUtils.substring$java_lang_String$int$int(str, start, end);
                            }
                            else if (((typeof str === 'string') || str === null) && ((typeof start === 'number') || start === null) && end === undefined) {
                                return org.openprovenance.apache.commons.lang.StringUtils.substring$java_lang_String$int(str, start);
                            }
                            else
                                throw new Error('invalid overload');
                        }
                        /**
                         * <p>Gets the leftmost <code>len</code> characters of a String.</p>
                         *
                         * <p>If <code>len</code> characters are not available, or the
                         * String is <code>null</code>, the String will be returned without
                         * an exception. An empty String is returned if len is negative.</p>
                         *
                         * <pre>
                         * StringUtils.left(null, *)    = null
                         * StringUtils.left(*, -ve)     = ""
                         * StringUtils.left("", *)      = ""
                         * StringUtils.left("abc", 0)   = ""
                         * StringUtils.left("abc", 2)   = "ab"
                         * StringUtils.left("abc", 4)   = "abc"
                         * </pre>
                         *
                         * @param {string} str  the String to get the leftmost characters from, may be null
                         * @param {number} len  the length of the required String
                         * @return {string} the leftmost characters, <code>null</code> if null String input
                         */
                        static left(str, len) {
                            if (str == null) {
                                return null;
                            }
                            if (len < 0) {
                                return StringUtils.EMPTY;
                            }
                            if (str.length <= len) {
                                return str;
                            }
                            return str.substring(0, len);
                        }
                        /**
                         * <p>Gets the rightmost <code>len</code> characters of a String.</p>
                         *
                         * <p>If <code>len</code> characters are not available, or the String
                         * is <code>null</code>, the String will be returned without an
                         * an exception. An empty String is returned if len is negative.</p>
                         *
                         * <pre>
                         * StringUtils.right(null, *)    = null
                         * StringUtils.right(*, -ve)     = ""
                         * StringUtils.right("", *)      = ""
                         * StringUtils.right("abc", 0)   = ""
                         * StringUtils.right("abc", 2)   = "bc"
                         * StringUtils.right("abc", 4)   = "abc"
                         * </pre>
                         *
                         * @param {string} str  the String to get the rightmost characters from, may be null
                         * @param {number} len  the length of the required String
                         * @return {string} the rightmost characters, <code>null</code> if null String input
                         */
                        static right(str, len) {
                            if (str == null) {
                                return null;
                            }
                            if (len < 0) {
                                return StringUtils.EMPTY;
                            }
                            if (str.length <= len) {
                                return str;
                            }
                            return str.substring(str.length - len);
                        }
                        /**
                         * <p>Gets <code>len</code> characters from the middle of a String.</p>
                         *
                         * <p>If <code>len</code> characters are not available, the remainder
                         * of the String will be returned without an exception. If the
                         * String is <code>null</code>, <code>null</code> will be returned.
                         * An empty String is returned if len is negative or exceeds the
                         * length of <code>str</code>.</p>
                         *
                         * <pre>
                         * StringUtils.mid(null, *, *)    = null
                         * StringUtils.mid(*, *, -ve)     = ""
                         * StringUtils.mid("", 0, *)      = ""
                         * StringUtils.mid("abc", 0, 2)   = "ab"
                         * StringUtils.mid("abc", 0, 4)   = "abc"
                         * StringUtils.mid("abc", 2, 4)   = "c"
                         * StringUtils.mid("abc", 4, 2)   = ""
                         * StringUtils.mid("abc", -2, 2)  = "ab"
                         * </pre>
                         *
                         * @param {string} str  the String to get the characters from, may be null
                         * @param {number} pos  the position to start from, negative treated as zero
                         * @param {number} len  the length of the required String
                         * @return {string} the middle characters, <code>null</code> if null String input
                         */
                        static mid(str, pos, len) {
                            if (str == null) {
                                return null;
                            }
                            if (len < 0 || pos > str.length) {
                                return StringUtils.EMPTY;
                            }
                            if (pos < 0) {
                                pos = 0;
                            }
                            if (str.length <= (pos + len)) {
                                return str.substring(pos);
                            }
                            return str.substring(pos, pos + len);
                        }
                        /**
                         * <p>Gets the substring before the first occurrence of a separator.
                         * The separator is not returned.</p>
                         *
                         * <p>A <code>null</code> string input will return <code>null</code>.
                         * An empty ("") string input will return the empty string.
                         * A <code>null</code> separator will return the input string.</p>
                         *
                         * <p>If nothing is found, the string input is returned.</p>
                         *
                         * <pre>
                         * StringUtils.substringBefore(null, *)      = null
                         * StringUtils.substringBefore("", *)        = ""
                         * StringUtils.substringBefore("abc", "a")   = ""
                         * StringUtils.substringBefore("abcba", "b") = "a"
                         * StringUtils.substringBefore("abc", "c")   = "ab"
                         * StringUtils.substringBefore("abc", "d")   = "abc"
                         * StringUtils.substringBefore("abc", "")    = ""
                         * StringUtils.substringBefore("abc", null)  = "abc"
                         * </pre>
                         *
                         * @param {string} str  the String to get a substring from, may be null
                         * @param {string} separator  the String to search for, may be null
                         * @return {string} the substring before the first occurrence of the separator,
                         * <code>null</code> if null String input
                         * @since 2.0
                         */
                        static substringBefore(str, separator) {
                            if (StringUtils.isEmpty(str) || separator == null) {
                                return str;
                            }
                            if (separator.length === 0) {
                                return StringUtils.EMPTY;
                            }
                            const pos = str.indexOf(separator);
                            if (pos === StringUtils.INDEX_NOT_FOUND) {
                                return str;
                            }
                            return str.substring(0, pos);
                        }
                        /**
                         * <p>Gets the substring after the first occurrence of a separator.
                         * The separator is not returned.</p>
                         *
                         * <p>A <code>null</code> string input will return <code>null</code>.
                         * An empty ("") string input will return the empty string.
                         * A <code>null</code> separator will return the empty string if the
                         * input string is not <code>null</code>.</p>
                         *
                         * <p>If nothing is found, the empty string is returned.</p>
                         *
                         * <pre>
                         * StringUtils.substringAfter(null, *)      = null
                         * StringUtils.substringAfter("", *)        = ""
                         * StringUtils.substringAfter(*, null)      = ""
                         * StringUtils.substringAfter("abc", "a")   = "bc"
                         * StringUtils.substringAfter("abcba", "b") = "cba"
                         * StringUtils.substringAfter("abc", "c")   = ""
                         * StringUtils.substringAfter("abc", "d")   = ""
                         * StringUtils.substringAfter("abc", "")    = "abc"
                         * </pre>
                         *
                         * @param {string} str  the String to get a substring from, may be null
                         * @param {string} separator  the String to search for, may be null
                         * @return {string} the substring after the first occurrence of the separator,
                         * <code>null</code> if null String input
                         * @since 2.0
                         */
                        static substringAfter(str, separator) {
                            if (StringUtils.isEmpty(str)) {
                                return str;
                            }
                            if (separator == null) {
                                return StringUtils.EMPTY;
                            }
                            const pos = str.indexOf(separator);
                            if (pos === StringUtils.INDEX_NOT_FOUND) {
                                return StringUtils.EMPTY;
                            }
                            return str.substring(pos + separator.length);
                        }
                        /**
                         * <p>Gets the substring before the last occurrence of a separator.
                         * The separator is not returned.</p>
                         *
                         * <p>A <code>null</code> string input will return <code>null</code>.
                         * An empty ("") string input will return the empty string.
                         * An empty or <code>null</code> separator will return the input string.</p>
                         *
                         * <p>If nothing is found, the string input is returned.</p>
                         *
                         * <pre>
                         * StringUtils.substringBeforeLast(null, *)      = null
                         * StringUtils.substringBeforeLast("", *)        = ""
                         * StringUtils.substringBeforeLast("abcba", "b") = "abc"
                         * StringUtils.substringBeforeLast("abc", "c")   = "ab"
                         * StringUtils.substringBeforeLast("a", "a")     = ""
                         * StringUtils.substringBeforeLast("a", "z")     = "a"
                         * StringUtils.substringBeforeLast("a", null)    = "a"
                         * StringUtils.substringBeforeLast("a", "")      = "a"
                         * </pre>
                         *
                         * @param {string} str  the String to get a substring from, may be null
                         * @param {string} separator  the String to search for, may be null
                         * @return {string} the substring before the last occurrence of the separator,
                         * <code>null</code> if null String input
                         * @since 2.0
                         */
                        static substringBeforeLast(str, separator) {
                            if (StringUtils.isEmpty(str) || StringUtils.isEmpty(separator)) {
                                return str;
                            }
                            const pos = str.lastIndexOf(separator);
                            if (pos === StringUtils.INDEX_NOT_FOUND) {
                                return str;
                            }
                            return str.substring(0, pos);
                        }
                        /**
                         * <p>Gets the substring after the last occurrence of a separator.
                         * The separator is not returned.</p>
                         *
                         * <p>A <code>null</code> string input will return <code>null</code>.
                         * An empty ("") string input will return the empty string.
                         * An empty or <code>null</code> separator will return the empty string if
                         * the input string is not <code>null</code>.</p>
                         *
                         * <p>If nothing is found, the empty string is returned.</p>
                         *
                         * <pre>
                         * StringUtils.substringAfterLast(null, *)      = null
                         * StringUtils.substringAfterLast("", *)        = ""
                         * StringUtils.substringAfterLast(*, "")        = ""
                         * StringUtils.substringAfterLast(*, null)      = ""
                         * StringUtils.substringAfterLast("abc", "a")   = "bc"
                         * StringUtils.substringAfterLast("abcba", "b") = "a"
                         * StringUtils.substringAfterLast("abc", "c")   = ""
                         * StringUtils.substringAfterLast("a", "a")     = ""
                         * StringUtils.substringAfterLast("a", "z")     = ""
                         * </pre>
                         *
                         * @param {string} str  the String to get a substring from, may be null
                         * @param {string} separator  the String to search for, may be null
                         * @return {string} the substring after the last occurrence of the separator,
                         * <code>null</code> if null String input
                         * @since 2.0
                         */
                        static substringAfterLast(str, separator) {
                            if (StringUtils.isEmpty(str)) {
                                return str;
                            }
                            if (StringUtils.isEmpty(separator)) {
                                return StringUtils.EMPTY;
                            }
                            const pos = str.lastIndexOf(separator);
                            if (pos === StringUtils.INDEX_NOT_FOUND || pos === (str.length - separator.length)) {
                                return StringUtils.EMPTY;
                            }
                            return str.substring(pos + separator.length);
                        }
                        static substringBetween$java_lang_String$java_lang_String(str, tag) {
                            return StringUtils.substringBetween$java_lang_String$java_lang_String$java_lang_String(str, tag, tag);
                        }
                        static substringBetween$java_lang_String$java_lang_String$java_lang_String(str, open, close) {
                            if (str == null || open == null || close == null) {
                                return null;
                            }
                            const start = str.indexOf(open);
                            if (start !== StringUtils.INDEX_NOT_FOUND) {
                                const end = str.indexOf(close, start + open.length);
                                if (end !== StringUtils.INDEX_NOT_FOUND) {
                                    return str.substring(start + open.length, end);
                                }
                            }
                            return null;
                        }
                        /**
                         * <p>Gets the String that is nested in between two Strings.
                         * Only the first match is returned.</p>
                         *
                         * <p>A <code>null</code> input String returns <code>null</code>.
                         * A <code>null</code> open/close returns <code>null</code> (no match).
                         * An empty ("") open and close returns an empty string.</p>
                         *
                         * <pre>
                         * StringUtils.substringBetween("wx[b]yz", "[", "]") = "b"
                         * StringUtils.substringBetween(null, *, *)          = null
                         * StringUtils.substringBetween(*, null, *)          = null
                         * StringUtils.substringBetween(*, *, null)          = null
                         * StringUtils.substringBetween("", "", "")          = ""
                         * StringUtils.substringBetween("", "", "]")         = null
                         * StringUtils.substringBetween("", "[", "]")        = null
                         * StringUtils.substringBetween("yabcz", "", "")     = ""
                         * StringUtils.substringBetween("yabcz", "y", "z")   = "abc"
                         * StringUtils.substringBetween("yabczyabcz", "y", "z")   = "abc"
                         * </pre>
                         *
                         * @param {string} str  the String containing the substring, may be null
                         * @param {string} open  the String before the substring, may be null
                         * @param {string} close  the String after the substring, may be null
                         * @return {string} the substring, <code>null</code> if no match
                         * @since 2.0
                         */
                        static substringBetween(str, open, close) {
                            if (((typeof str === 'string') || str === null) && ((typeof open === 'string') || open === null) && ((typeof close === 'string') || close === null)) {
                                return org.openprovenance.apache.commons.lang.StringUtils.substringBetween$java_lang_String$java_lang_String$java_lang_String(str, open, close);
                            }
                            else if (((typeof str === 'string') || str === null) && ((typeof open === 'string') || open === null) && close === undefined) {
                                return org.openprovenance.apache.commons.lang.StringUtils.substringBetween$java_lang_String$java_lang_String(str, open);
                            }
                            else
                                throw new Error('invalid overload');
                        }
                        /**
                         * <p>Searches a String for substrings delimited by a start and end tag,
                         * returning all matching substrings in an array.</p>
                         *
                         * <p>A <code>null</code> input String returns <code>null</code>.
                         * A <code>null</code> open/close returns <code>null</code> (no match).
                         * An empty ("") open/close returns <code>null</code> (no match).</p>
                         *
                         * <pre>
                         * StringUtils.substringsBetween("[a][b][c]", "[", "]") = ["a","b","c"]
                         * StringUtils.substringsBetween(null, *, *)            = null
                         * StringUtils.substringsBetween(*, null, *)            = null
                         * StringUtils.substringsBetween(*, *, null)            = null
                         * StringUtils.substringsBetween("", "[", "]")          = []
                         * </pre>
                         *
                         * @param {string} str  the String containing the substrings, null returns null, empty returns empty
                         * @param {string} open  the String identifying the start of the substring, empty returns null
                         * @param {string} close  the String identifying the end of the substring, empty returns null
                         * @return {java.lang.String[]} a String Array of substrings, or <code>null</code> if no match
                         * @since 2.3
                         */
                        static substringsBetween(str, open, close) {
                            if (str == null || StringUtils.isEmpty(open) || StringUtils.isEmpty(close)) {
                                return null;
                            }
                            const strLen = str.length;
                            if (strLen === 0) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.EMPTY_STRING_ARRAY_$LI$();
                            }
                            const closeLen = close.length;
                            const openLen = open.length;
                            const list = ([]);
                            let pos = 0;
                            while ((pos < (strLen - closeLen))) {
                                {
                                    let start = str.indexOf(open, pos);
                                    if (start < 0) {
                                        break;
                                    }
                                    start += openLen;
                                    const end = str.indexOf(close, start);
                                    if (end < 0) {
                                        break;
                                    }
                                    /* add */ (list.push(str.substring(start, end)) > 0);
                                    pos = end + closeLen;
                                }
                            }
                            ;
                            if ( /* isEmpty */(list.length == 0)) {
                                return null;
                            }
                            return list.slice(0);
                        }
                        static getNestedString$java_lang_String$java_lang_String(str, tag) {
                            return StringUtils.substringBetween$java_lang_String$java_lang_String$java_lang_String(str, tag, tag);
                        }
                        static getNestedString$java_lang_String$java_lang_String$java_lang_String(str, open, close) {
                            return StringUtils.substringBetween$java_lang_String$java_lang_String$java_lang_String(str, open, close);
                        }
                        /**
                         * <p>Gets the String that is nested in between two Strings.
                         * Only the first match is returned.</p>
                         *
                         * <p>A <code>null</code> input String returns <code>null</code>.
                         * A <code>null</code> open/close returns <code>null</code> (no match).
                         * An empty ("") open/close returns an empty string.</p>
                         *
                         * <pre>
                         * StringUtils.getNestedString(null, *, *)          = null
                         * StringUtils.getNestedString("", "", "")          = ""
                         * StringUtils.getNestedString("", "", "tag")       = null
                         * StringUtils.getNestedString("", "tag", "tag")    = null
                         * StringUtils.getNestedString("yabcz", null, null) = null
                         * StringUtils.getNestedString("yabcz", "", "")     = ""
                         * StringUtils.getNestedString("yabcz", "y", "z")   = "abc"
                         * StringUtils.getNestedString("yabczyabcz", "y", "z")   = "abc"
                         * </pre>
                         *
                         * @param {string} str  the String containing nested-string, may be null
                         * @param {string} open  the String before nested-string, may be null
                         * @param {string} close  the String after nested-string, may be null
                         * @return {string} the nested String, <code>null</code> if no match
                         * @deprecated Use the better named {@link #substringBetween(String, String, String)}.
                         * Method will be removed in Commons Lang 3.0.
                         */
                        static getNestedString(str, open, close) {
                            if (((typeof str === 'string') || str === null) && ((typeof open === 'string') || open === null) && ((typeof close === 'string') || close === null)) {
                                return org.openprovenance.apache.commons.lang.StringUtils.getNestedString$java_lang_String$java_lang_String$java_lang_String(str, open, close);
                            }
                            else if (((typeof str === 'string') || str === null) && ((typeof open === 'string') || open === null) && close === undefined) {
                                return org.openprovenance.apache.commons.lang.StringUtils.getNestedString$java_lang_String$java_lang_String(str, open);
                            }
                            else
                                throw new Error('invalid overload');
                        }
                        static split$java_lang_String(str) {
                            return StringUtils.split$java_lang_String$java_lang_String$int(str, null, -1);
                        }
                        static split$java_lang_String$char(str, separatorChar) {
                            return StringUtils.splitWorker$java_lang_String$char$boolean(str, separatorChar, false);
                        }
                        static split$java_lang_String$java_lang_String(str, separatorChars) {
                            return StringUtils.splitWorker$java_lang_String$java_lang_String$int$boolean(str, separatorChars, -1, false);
                        }
                        static split$java_lang_String$java_lang_String$int(str, separatorChars, max) {
                            return StringUtils.splitWorker$java_lang_String$java_lang_String$int$boolean(str, separatorChars, max, false);
                        }
                        /**
                         * <p>Splits the provided text into an array with a maximum length,
                         * separators specified.</p>
                         *
                         * <p>The separator is not included in the returned String array.
                         * Adjacent separators are treated as one separator.</p>
                         *
                         * <p>A <code>null</code> input String returns <code>null</code>.
                         * A <code>null</code> separatorChars splits on whitespace.</p>
                         *
                         * <p>If more than <code>max</code> delimited substrings are found, the last
                         * returned string includes all characters after the first <code>max - 1</code>
                         * returned strings (including separator characters).</p>
                         *
                         * <pre>
                         * StringUtils.split(null, *, *)            = null
                         * StringUtils.split("", *, *)              = []
                         * StringUtils.split("ab de fg", null, 0)   = ["ab", "cd", "ef"]
                         * StringUtils.split("ab   de fg", null, 0) = ["ab", "cd", "ef"]
                         * StringUtils.split("ab:cd:ef", ":", 0)    = ["ab", "cd", "ef"]
                         * StringUtils.split("ab:cd:ef", ":", 2)    = ["ab", "cd:ef"]
                         * </pre>
                         *
                         * @param {string} str  the String to parse, may be null
                         * @param {string} separatorChars  the characters used as the delimiters,
                         * <code>null</code> splits on whitespace
                         * @param {number} max  the maximum number of elements to include in the
                         * array. A zero or negative value implies no limit
                         * @return {java.lang.String[]} an array of parsed Strings, <code>null</code> if null String input
                         */
                        static split(str, separatorChars, max) {
                            if (((typeof str === 'string') || str === null) && ((typeof separatorChars === 'string') || separatorChars === null) && ((typeof max === 'number') || max === null)) {
                                return org.openprovenance.apache.commons.lang.StringUtils.split$java_lang_String$java_lang_String$int(str, separatorChars, max);
                            }
                            else if (((typeof str === 'string') || str === null) && ((typeof separatorChars === 'string') || separatorChars === null) && max === undefined) {
                                return org.openprovenance.apache.commons.lang.StringUtils.split$java_lang_String$java_lang_String(str, separatorChars);
                            }
                            else if (((typeof str === 'string') || str === null) && ((typeof separatorChars === 'string') || separatorChars === null) && max === undefined) {
                                return org.openprovenance.apache.commons.lang.StringUtils.split$java_lang_String$char(str, separatorChars);
                            }
                            else if (((typeof str === 'string') || str === null) && separatorChars === undefined && max === undefined) {
                                return org.openprovenance.apache.commons.lang.StringUtils.split$java_lang_String(str);
                            }
                            else
                                throw new Error('invalid overload');
                        }
                        static splitByWholeSeparator$java_lang_String$java_lang_String(str, separator) {
                            return StringUtils.splitByWholeSeparatorWorker(str, separator, -1, false);
                        }
                        static splitByWholeSeparator$java_lang_String$java_lang_String$int(str, separator, max) {
                            return StringUtils.splitByWholeSeparatorWorker(str, separator, max, false);
                        }
                        /**
                         * <p>Splits the provided text into an array, separator string specified.
                         * Returns a maximum of <code>max</code> substrings.</p>
                         *
                         * <p>The separator(s) will not be included in the returned String array.
                         * Adjacent separators are treated as one separator.</p>
                         *
                         * <p>A <code>null</code> input String returns <code>null</code>.
                         * A <code>null</code> separator splits on whitespace.</p>
                         *
                         * <pre>
                         * StringUtils.splitByWholeSeparator(null, *, *)               = null
                         * StringUtils.splitByWholeSeparator("", *, *)                 = []
                         * StringUtils.splitByWholeSeparator("ab de fg", null, 0)      = ["ab", "de", "fg"]
                         * StringUtils.splitByWholeSeparator("ab   de fg", null, 0)    = ["ab", "de", "fg"]
                         * StringUtils.splitByWholeSeparator("ab:cd:ef", ":", 2)       = ["ab", "cd:ef"]
                         * StringUtils.splitByWholeSeparator("ab-!-cd-!-ef", "-!-", 5) = ["ab", "cd", "ef"]
                         * StringUtils.splitByWholeSeparator("ab-!-cd-!-ef", "-!-", 2) = ["ab", "cd-!-ef"]
                         * </pre>
                         *
                         * @param {string} str  the String to parse, may be null
                         * @param {string} separator  String containing the String to be used as a delimiter,
                         * <code>null</code> splits on whitespace
                         * @param {number} max  the maximum number of elements to include in the returned
                         * array. A zero or negative value implies no limit.
                         * @return {java.lang.String[]} an array of parsed Strings, <code>null</code> if null String was input
                         */
                        static splitByWholeSeparator(str, separator, max) {
                            if (((typeof str === 'string') || str === null) && ((typeof separator === 'string') || separator === null) && ((typeof max === 'number') || max === null)) {
                                return org.openprovenance.apache.commons.lang.StringUtils.splitByWholeSeparator$java_lang_String$java_lang_String$int(str, separator, max);
                            }
                            else if (((typeof str === 'string') || str === null) && ((typeof separator === 'string') || separator === null) && max === undefined) {
                                return org.openprovenance.apache.commons.lang.StringUtils.splitByWholeSeparator$java_lang_String$java_lang_String(str, separator);
                            }
                            else
                                throw new Error('invalid overload');
                        }
                        static splitByWholeSeparatorPreserveAllTokens$java_lang_String$java_lang_String(str, separator) {
                            return StringUtils.splitByWholeSeparatorWorker(str, separator, -1, true);
                        }
                        static splitByWholeSeparatorPreserveAllTokens$java_lang_String$java_lang_String$int(str, separator, max) {
                            return StringUtils.splitByWholeSeparatorWorker(str, separator, max, true);
                        }
                        /**
                         * <p>Splits the provided text into an array, separator string specified.
                         * Returns a maximum of <code>max</code> substrings.</p>
                         *
                         * <p>The separator is not included in the returned String array.
                         * Adjacent separators are treated as separators for empty tokens.
                         * For more control over the split use the StrTokenizer class.</p>
                         *
                         * <p>A <code>null</code> input String returns <code>null</code>.
                         * A <code>null</code> separator splits on whitespace.</p>
                         *
                         * <pre>
                         * StringUtils.splitByWholeSeparatorPreserveAllTokens(null, *, *)               = null
                         * StringUtils.splitByWholeSeparatorPreserveAllTokens("", *, *)                 = []
                         * StringUtils.splitByWholeSeparatorPreserveAllTokens("ab de fg", null, 0)      = ["ab", "de", "fg"]
                         * StringUtils.splitByWholeSeparatorPreserveAllTokens("ab   de fg", null, 0)    = ["ab", "", "", "de", "fg"]
                         * StringUtils.splitByWholeSeparatorPreserveAllTokens("ab:cd:ef", ":", 2)       = ["ab", "cd:ef"]
                         * StringUtils.splitByWholeSeparatorPreserveAllTokens("ab-!-cd-!-ef", "-!-", 5) = ["ab", "cd", "ef"]
                         * StringUtils.splitByWholeSeparatorPreserveAllTokens("ab-!-cd-!-ef", "-!-", 2) = ["ab", "cd-!-ef"]
                         * </pre>
                         *
                         * @param {string} str  the String to parse, may be null
                         * @param {string} separator  String containing the String to be used as a delimiter,
                         * <code>null</code> splits on whitespace
                         * @param {number} max  the maximum number of elements to include in the returned
                         * array. A zero or negative value implies no limit.
                         * @return {java.lang.String[]} an array of parsed Strings, <code>null</code> if null String was input
                         * @since 2.4
                         */
                        static splitByWholeSeparatorPreserveAllTokens(str, separator, max) {
                            if (((typeof str === 'string') || str === null) && ((typeof separator === 'string') || separator === null) && ((typeof max === 'number') || max === null)) {
                                return org.openprovenance.apache.commons.lang.StringUtils.splitByWholeSeparatorPreserveAllTokens$java_lang_String$java_lang_String$int(str, separator, max);
                            }
                            else if (((typeof str === 'string') || str === null) && ((typeof separator === 'string') || separator === null) && max === undefined) {
                                return org.openprovenance.apache.commons.lang.StringUtils.splitByWholeSeparatorPreserveAllTokens$java_lang_String$java_lang_String(str, separator);
                            }
                            else
                                throw new Error('invalid overload');
                        }
                        /**
                         * Performs the logic for the <code>splitByWholeSeparatorPreserveAllTokens</code> methods.
                         *
                         * @param {string} str  the String to parse, may be <code>null</code>
                         * @param {string} separator  String containing the String to be used as a delimiter,
                         * <code>null</code> splits on whitespace
                         * @param {number} max  the maximum number of elements to include in the returned
                         * array. A zero or negative value implies no limit.
                         * @param {boolean} preserveAllTokens if <code>true</code>, adjacent separators are
                         * treated as empty token separators; if <code>false</code>, adjacent
                         * separators are treated as one separator.
                         * @return {java.lang.String[]} an array of parsed Strings, <code>null</code> if null String input
                         * @since 2.4
                         * @private
                         */
                        /*private*/ static splitByWholeSeparatorWorker(str, separator, max, preserveAllTokens) {
                            if (str == null) {
                                return null;
                            }
                            const len = str.length;
                            if (len === 0) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.EMPTY_STRING_ARRAY_$LI$();
                            }
                            if ((separator == null) || (StringUtils.EMPTY === separator)) {
                                return StringUtils.splitWorker$java_lang_String$java_lang_String$int$boolean(str, null, max, preserveAllTokens);
                            }
                            const separatorLength = separator.length;
                            const substrings = ([]);
                            let numberOfSubstrings = 0;
                            let beg = 0;
                            let end = 0;
                            while ((end < len)) {
                                {
                                    end = str.indexOf(separator, beg);
                                    if (end > -1) {
                                        if (end > beg) {
                                            numberOfSubstrings += 1;
                                            if (numberOfSubstrings === max) {
                                                end = len;
                                                /* add */ (substrings.push(str.substring(beg)) > 0);
                                            }
                                            else {
                                                /* add */ (substrings.push(str.substring(beg, end)) > 0);
                                                beg = end + separatorLength;
                                            }
                                        }
                                        else {
                                            if (preserveAllTokens) {
                                                numberOfSubstrings += 1;
                                                if (numberOfSubstrings === max) {
                                                    end = len;
                                                    /* add */ (substrings.push(str.substring(beg)) > 0);
                                                }
                                                else {
                                                    /* add */ (substrings.push(StringUtils.EMPTY) > 0);
                                                }
                                            }
                                            beg = end + separatorLength;
                                        }
                                    }
                                    else {
                                        /* add */ (substrings.push(str.substring(beg)) > 0);
                                        end = len;
                                    }
                                }
                            }
                            ;
                            return substrings.slice(0);
                        }
                        static splitPreserveAllTokens$java_lang_String(str) {
                            return StringUtils.splitWorker$java_lang_String$java_lang_String$int$boolean(str, null, -1, true);
                        }
                        static splitPreserveAllTokens$java_lang_String$char(str, separatorChar) {
                            return StringUtils.splitWorker$java_lang_String$char$boolean(str, separatorChar, true);
                        }
                        /*private*/ static splitWorker$java_lang_String$char$boolean(str, separatorChar, preserveAllTokens) {
                            if (str == null) {
                                return null;
                            }
                            const len = str.length;
                            if (len === 0) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.EMPTY_STRING_ARRAY_$LI$();
                            }
                            const list = ([]);
                            let i = 0;
                            let start = 0;
                            let match = false;
                            let lastMatch = false;
                            while ((i < len)) {
                                {
                                    if ((c => c.charCodeAt == null ? c : c.charCodeAt(0))(str.charAt(i)) == (c => c.charCodeAt == null ? c : c.charCodeAt(0))(separatorChar)) {
                                        if (match || preserveAllTokens) {
                                            /* add */ (list.push(str.substring(start, i)) > 0);
                                            match = false;
                                            lastMatch = true;
                                        }
                                        start = ++i;
                                        continue;
                                    }
                                    lastMatch = false;
                                    match = true;
                                    i++;
                                }
                            }
                            ;
                            if (match || (preserveAllTokens && lastMatch)) {
                                /* add */ (list.push(str.substring(start, i)) > 0);
                            }
                            return list.slice(0);
                        }
                        static splitPreserveAllTokens$java_lang_String$java_lang_String(str, separatorChars) {
                            return StringUtils.splitWorker$java_lang_String$java_lang_String$int$boolean(str, separatorChars, -1, true);
                        }
                        static splitPreserveAllTokens$java_lang_String$java_lang_String$int(str, separatorChars, max) {
                            return StringUtils.splitWorker$java_lang_String$java_lang_String$int$boolean(str, separatorChars, max, true);
                        }
                        /**
                         * <p>Splits the provided text into an array with a maximum length,
                         * separators specified, preserving all tokens, including empty tokens
                         * created by adjacent separators.</p>
                         *
                         * <p>The separator is not included in the returned String array.
                         * Adjacent separators are treated as separators for empty tokens.
                         * Adjacent separators are treated as one separator.</p>
                         *
                         * <p>A <code>null</code> input String returns <code>null</code>.
                         * A <code>null</code> separatorChars splits on whitespace.</p>
                         *
                         * <p>If more than <code>max</code> delimited substrings are found, the last
                         * returned string includes all characters after the first <code>max - 1</code>
                         * returned strings (including separator characters).</p>
                         *
                         * <pre>
                         * StringUtils.splitPreserveAllTokens(null, *, *)            = null
                         * StringUtils.splitPreserveAllTokens("", *, *)              = []
                         * StringUtils.splitPreserveAllTokens("ab de fg", null, 0)   = ["ab", "cd", "ef"]
                         * StringUtils.splitPreserveAllTokens("ab   de fg", null, 0) = ["ab", "cd", "ef"]
                         * StringUtils.splitPreserveAllTokens("ab:cd:ef", ":", 0)    = ["ab", "cd", "ef"]
                         * StringUtils.splitPreserveAllTokens("ab:cd:ef", ":", 2)    = ["ab", "cd:ef"]
                         * StringUtils.splitPreserveAllTokens("ab   de fg", null, 2) = ["ab", "  de fg"]
                         * StringUtils.splitPreserveAllTokens("ab   de fg", null, 3) = ["ab", "", " de fg"]
                         * StringUtils.splitPreserveAllTokens("ab   de fg", null, 4) = ["ab", "", "", "de fg"]
                         * </pre>
                         *
                         * @param {string} str  the String to parse, may be <code>null</code>
                         * @param {string} separatorChars  the characters used as the delimiters,
                         * <code>null</code> splits on whitespace
                         * @param {number} max  the maximum number of elements to include in the
                         * array. A zero or negative value implies no limit
                         * @return {java.lang.String[]} an array of parsed Strings, <code>null</code> if null String input
                         * @since 2.1
                         */
                        static splitPreserveAllTokens(str, separatorChars, max) {
                            if (((typeof str === 'string') || str === null) && ((typeof separatorChars === 'string') || separatorChars === null) && ((typeof max === 'number') || max === null)) {
                                return org.openprovenance.apache.commons.lang.StringUtils.splitPreserveAllTokens$java_lang_String$java_lang_String$int(str, separatorChars, max);
                            }
                            else if (((typeof str === 'string') || str === null) && ((typeof separatorChars === 'string') || separatorChars === null) && max === undefined) {
                                return org.openprovenance.apache.commons.lang.StringUtils.splitPreserveAllTokens$java_lang_String$java_lang_String(str, separatorChars);
                            }
                            else if (((typeof str === 'string') || str === null) && ((typeof separatorChars === 'string') || separatorChars === null) && max === undefined) {
                                return org.openprovenance.apache.commons.lang.StringUtils.splitPreserveAllTokens$java_lang_String$char(str, separatorChars);
                            }
                            else if (((typeof str === 'string') || str === null) && separatorChars === undefined && max === undefined) {
                                return org.openprovenance.apache.commons.lang.StringUtils.splitPreserveAllTokens$java_lang_String(str);
                            }
                            else
                                throw new Error('invalid overload');
                        }
                        static splitWorker$java_lang_String$java_lang_String$int$boolean(str, separatorChars, max, preserveAllTokens) {
                            if (str == null) {
                                return null;
                            }
                            const len = str.length;
                            if (len === 0) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.EMPTY_STRING_ARRAY_$LI$();
                            }
                            const list = ([]);
                            let sizePlus1 = 1;
                            let i = 0;
                            let start = 0;
                            let match = false;
                            let lastMatch = false;
                            if (separatorChars == null) {
                                while ((i < len)) {
                                    {
                                        if (javaemul.internal.CharacterHelper.isWhitespace(str.charAt(i))) {
                                            if (match || preserveAllTokens) {
                                                lastMatch = true;
                                                if (sizePlus1++ === max) {
                                                    i = len;
                                                    lastMatch = false;
                                                }
                                                /* add */ (list.push(str.substring(start, i)) > 0);
                                                match = false;
                                            }
                                            start = ++i;
                                            continue;
                                        }
                                        lastMatch = false;
                                        match = true;
                                        i++;
                                    }
                                }
                                ;
                            }
                            else if (separatorChars.length === 1) {
                                const sep = separatorChars.charAt(0);
                                while ((i < len)) {
                                    {
                                        if ((c => c.charCodeAt == null ? c : c.charCodeAt(0))(str.charAt(i)) == (c => c.charCodeAt == null ? c : c.charCodeAt(0))(sep)) {
                                            if (match || preserveAllTokens) {
                                                lastMatch = true;
                                                if (sizePlus1++ === max) {
                                                    i = len;
                                                    lastMatch = false;
                                                }
                                                /* add */ (list.push(str.substring(start, i)) > 0);
                                                match = false;
                                            }
                                            start = ++i;
                                            continue;
                                        }
                                        lastMatch = false;
                                        match = true;
                                        i++;
                                    }
                                }
                                ;
                            }
                            else {
                                while ((i < len)) {
                                    {
                                        if (separatorChars.indexOf(str.charAt(i)) >= 0) {
                                            if (match || preserveAllTokens) {
                                                lastMatch = true;
                                                if (sizePlus1++ === max) {
                                                    i = len;
                                                    lastMatch = false;
                                                }
                                                /* add */ (list.push(str.substring(start, i)) > 0);
                                                match = false;
                                            }
                                            start = ++i;
                                            continue;
                                        }
                                        lastMatch = false;
                                        match = true;
                                        i++;
                                    }
                                }
                                ;
                            }
                            if (match || (preserveAllTokens && lastMatch)) {
                                /* add */ (list.push(str.substring(start, i)) > 0);
                            }
                            return list.slice(0);
                        }
                        /**
                         * Performs the logic for the <code>split</code> and
                         * <code>splitPreserveAllTokens</code> methods that return a maximum array
                         * length.
                         *
                         * @param {string} str  the String to parse, may be <code>null</code>
                         * @param {string} separatorChars the separate character
                         * @param {number} max  the maximum number of elements to include in the
                         * array. A zero or negative value implies no limit.
                         * @param {boolean} preserveAllTokens if <code>true</code>, adjacent separators are
                         * treated as empty token separators; if <code>false</code>, adjacent
                         * separators are treated as one separator.
                         * @return {java.lang.String[]} an array of parsed Strings, <code>null</code> if null String input
                         * @private
                         */
                        static splitWorker(str, separatorChars, max, preserveAllTokens) {
                            if (((typeof str === 'string') || str === null) && ((typeof separatorChars === 'string') || separatorChars === null) && ((typeof max === 'number') || max === null) && ((typeof preserveAllTokens === 'boolean') || preserveAllTokens === null)) {
                                return org.openprovenance.apache.commons.lang.StringUtils.splitWorker$java_lang_String$java_lang_String$int$boolean(str, separatorChars, max, preserveAllTokens);
                            }
                            else if (((typeof str === 'string') || str === null) && ((typeof separatorChars === 'string') || separatorChars === null) && ((typeof max === 'boolean') || max === null) && preserveAllTokens === undefined) {
                                return org.openprovenance.apache.commons.lang.StringUtils.splitWorker$java_lang_String$char$boolean(str, separatorChars, max);
                            }
                            else
                                throw new Error('invalid overload');
                        }
                        static splitByCharacterType$java_lang_String(str) {
                            return StringUtils.splitByCharacterType$java_lang_String$boolean(str, false);
                        }
                        /**
                         * <p>Splits a String by Character type as returned by
                         * <code>java.lang.Character.getType(char)</code>. Groups of contiguous
                         * characters of the same type are returned as complete tokens, with the
                         * following exception: the character of type
                         * <code>Character.UPPERCASE_LETTER</code>, if any, immediately
                         * preceding a token of type <code>Character.LOWERCASE_LETTER</code>
                         * will belong to the following token rather than to the preceding, if any,
                         * <code>Character.UPPERCASE_LETTER</code> token.
                         * <pre>
                         * StringUtils.splitByCharacterTypeCamelCase(null)         = null
                         * StringUtils.splitByCharacterTypeCamelCase("")           = []
                         * StringUtils.splitByCharacterTypeCamelCase("ab de fg")   = ["ab", " ", "de", " ", "fg"]
                         * StringUtils.splitByCharacterTypeCamelCase("ab   de fg") = ["ab", "   ", "de", " ", "fg"]
                         * StringUtils.splitByCharacterTypeCamelCase("ab:cd:ef")   = ["ab", ":", "cd", ":", "ef"]
                         * StringUtils.splitByCharacterTypeCamelCase("number5")    = ["number", "5"]
                         * StringUtils.splitByCharacterTypeCamelCase("fooBar")     = ["foo", "Bar"]
                         * StringUtils.splitByCharacterTypeCamelCase("foo200Bar")  = ["foo", "200", "Bar"]
                         * StringUtils.splitByCharacterTypeCamelCase("ASFRules")   = ["ASF", "Rules"]
                         * </pre>
                         * @param {string} str the String to split, may be <code>null</code>
                         * @return {java.lang.String[]} an array of parsed Strings, <code>null</code> if null String input
                         * @since 2.4
                         */
                        static splitByCharacterTypeCamelCase(str) {
                            return StringUtils.splitByCharacterType$java_lang_String$boolean(str, true);
                        }
                        static splitByCharacterType$java_lang_String$boolean(str, camelCase) {
                            if (str == null) {
                                return null;
                            }
                            if (str.length === 0) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.EMPTY_STRING_ARRAY_$LI$();
                            }
                            const c = (str).split('');
                            const list = ([]);
                            let tokenStart = 0;
                            let currentType = javaemul.internal.CharacterHelper.getType(c[tokenStart]);
                            for (let pos = tokenStart + 1; pos < c.length; pos++) {
                                {
                                    const type = javaemul.internal.CharacterHelper.getType(c[pos]);
                                    if (type === currentType) {
                                        continue;
                                    }
                                    if (camelCase && type === javaemul.internal.CharacterHelper.LOWERCASE_LETTER && currentType === javaemul.internal.CharacterHelper.UPPERCASE_LETTER) {
                                        const newTokenStart = pos - 1;
                                        if (newTokenStart !== tokenStart) {
                                            /* add */ (list.push(c.join('').substr(tokenStart, newTokenStart - tokenStart)) > 0);
                                            tokenStart = newTokenStart;
                                        }
                                    }
                                    else {
                                        /* add */ (list.push(c.join('').substr(tokenStart, pos - tokenStart)) > 0);
                                        tokenStart = pos;
                                    }
                                    currentType = type;
                                }
                                ;
                            }
                            /* add */ (list.push(c.join('').substr(tokenStart, c.length - tokenStart)) > 0);
                            return list.slice(0);
                        }
                        /**
                         * <p>Splits a String by Character type as returned by
                         * <code>java.lang.Character.getType(char)</code>. Groups of contiguous
                         * characters of the same type are returned as complete tokens, with the
                         * following exception: if <code>camelCase</code> is <code>true</code>,
                         * the character of type <code>Character.UPPERCASE_LETTER</code>, if any,
                         * immediately preceding a token of type <code>Character.LOWERCASE_LETTER</code>
                         * will belong to the following token rather than to the preceding, if any,
                         * <code>Character.UPPERCASE_LETTER</code> token.
                         * @param {string} str the String to split, may be <code>null</code>
                         * @param {boolean} camelCase whether to use so-called "camel-case" for letter types
                         * @return {java.lang.String[]} an array of parsed Strings, <code>null</code> if null String input
                         * @since 2.4
                         * @private
                         */
                        static splitByCharacterType(str, camelCase) {
                            if (((typeof str === 'string') || str === null) && ((typeof camelCase === 'boolean') || camelCase === null)) {
                                return org.openprovenance.apache.commons.lang.StringUtils.splitByCharacterType$java_lang_String$boolean(str, camelCase);
                            }
                            else if (((typeof str === 'string') || str === null) && camelCase === undefined) {
                                return org.openprovenance.apache.commons.lang.StringUtils.splitByCharacterType$java_lang_String(str);
                            }
                            else
                                throw new Error('invalid overload');
                        }
                        /**
                         * <p>Joins the provided elements into a single String. </p>
                         *
                         * <p>No separator is added to the joined String.
                         * Null objects or empty string elements are represented by
                         * empty strings.</p>
                         *
                         * <pre>
                         * StringUtils.concatenate(null)            = null
                         * StringUtils.concatenate([])              = ""
                         * StringUtils.concatenate([null])          = ""
                         * StringUtils.concatenate(["a", "b", "c"]) = "abc"
                         * StringUtils.concatenate([null, "", "a"]) = "a"
                         * </pre>
                         *
                         * @param {java.lang.Object[]} array  the array of values to concatenate, may be null
                         * @return {string} the concatenated String, <code>null</code> if null array input
                         * @deprecated Use the better named {@link #join(Object[])} instead.
                         * Method will be removed in Commons Lang 3.0.
                         */
                        static concatenate(array) {
                            return StringUtils.join$java_lang_Object_A$java_lang_String(array, null);
                        }
                        static join$java_lang_Object_A(array) {
                            return StringUtils.join$java_lang_Object_A$java_lang_String(array, null);
                        }
                        static join$java_lang_Object_A$char(array, separator) {
                            if (array == null) {
                                return null;
                            }
                            return StringUtils.join$java_lang_Object_A$char$int$int(array, separator, 0, array.length);
                        }
                        static join$java_lang_Object_A$char$int$int(array, separator, startIndex, endIndex) {
                            if (array == null) {
                                return null;
                            }
                            let bufSize = (endIndex - startIndex);
                            if (bufSize <= 0) {
                                return StringUtils.EMPTY;
                            }
                            bufSize *= ((array[startIndex] == null ? 16 : array[startIndex].toString().length) + 1);
                            const buf = new org.openprovenance.apache.commons.lang.text.StrBuilder(bufSize);
                            for (let i = startIndex; i < endIndex; i++) {
                                {
                                    if (i > startIndex) {
                                        buf.append$char(separator);
                                    }
                                    if (array[i] != null) {
                                        buf.append$java_lang_Object(array[i]);
                                    }
                                }
                                ;
                            }
                            return buf.toString();
                        }
                        static join$java_lang_Object_A$java_lang_String(array, separator) {
                            if (array == null) {
                                return null;
                            }
                            return StringUtils.join$java_lang_Object_A$java_lang_String$int$int(array, separator, 0, array.length);
                        }
                        static join$java_lang_Object_A$java_lang_String$int$int(array, separator, startIndex, endIndex) {
                            if (array == null) {
                                return null;
                            }
                            if (separator == null) {
                                separator = StringUtils.EMPTY;
                            }
                            let bufSize = (endIndex - startIndex);
                            if (bufSize <= 0) {
                                return StringUtils.EMPTY;
                            }
                            bufSize *= ((array[startIndex] == null ? 16 : array[startIndex].toString().length) + separator.length);
                            const buf = new org.openprovenance.apache.commons.lang.text.StrBuilder(bufSize);
                            for (let i = startIndex; i < endIndex; i++) {
                                {
                                    if (i > startIndex) {
                                        buf.append$java_lang_String(separator);
                                    }
                                    if (array[i] != null) {
                                        buf.append$java_lang_Object(array[i]);
                                    }
                                }
                                ;
                            }
                            return buf.toString();
                        }
                        /**
                         * <p>Joins the elements of the provided array into a single String
                         * containing the provided list of elements.</p>
                         *
                         * <p>No delimiter is added before or after the list.
                         * A <code>null</code> separator is the same as an empty String ("").
                         * Null objects or empty strings within the array are represented by
                         * empty strings.</p>
                         *
                         * <pre>
                         * StringUtils.join(null, *)                = null
                         * StringUtils.join([], *)                  = ""
                         * StringUtils.join([null], *)              = ""
                         * StringUtils.join(["a", "b", "c"], "--")  = "a--b--c"
                         * StringUtils.join(["a", "b", "c"], null)  = "abc"
                         * StringUtils.join(["a", "b", "c"], "")    = "abc"
                         * StringUtils.join([null, "", "a"], ',')   = ",,a"
                         * </pre>
                         *
                         * @param {java.lang.Object[]} array  the array of values to join together, may be null
                         * @param {string} separator  the separator character to use, null treated as ""
                         * @param {number} startIndex the first index to start joining from.  It is
                         * an error to pass in an end index past the end of the array
                         * @param {number} endIndex the index to stop joining from (exclusive). It is
                         * an error to pass in an end index past the end of the array
                         * @return {string} the joined String, <code>null</code> if null array input
                         */
                        static join(array, separator, startIndex, endIndex) {
                            if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (array[0] != null))) || array === null) && ((typeof separator === 'string') || separator === null) && ((typeof startIndex === 'number') || startIndex === null) && ((typeof endIndex === 'number') || endIndex === null)) {
                                return org.openprovenance.apache.commons.lang.StringUtils.join$java_lang_Object_A$java_lang_String$int$int(array, separator, startIndex, endIndex);
                            }
                            else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (array[0] != null))) || array === null) && ((typeof separator === 'string') || separator === null) && ((typeof startIndex === 'number') || startIndex === null) && ((typeof endIndex === 'number') || endIndex === null)) {
                                return org.openprovenance.apache.commons.lang.StringUtils.join$java_lang_Object_A$char$int$int(array, separator, startIndex, endIndex);
                            }
                            else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (array[0] != null))) || array === null) && ((typeof separator === 'string') || separator === null) && startIndex === undefined && endIndex === undefined) {
                                return org.openprovenance.apache.commons.lang.StringUtils.join$java_lang_Object_A$java_lang_String(array, separator);
                            }
                            else if (((array != null && (array instanceof Object)) || array === null) && ((typeof separator === 'string') || separator === null) && startIndex === undefined && endIndex === undefined) {
                                return org.openprovenance.apache.commons.lang.StringUtils.join$java_util_Iterator$java_lang_String(array, separator);
                            }
                            else if (((array != null && (array instanceof Array)) || array === null) && ((typeof separator === 'string') || separator === null) && startIndex === undefined && endIndex === undefined) {
                                return org.openprovenance.apache.commons.lang.StringUtils.join$java_util_Collection$java_lang_String(array, separator);
                            }
                            else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (array[0] != null))) || array === null) && ((typeof separator === 'string') || separator === null) && startIndex === undefined && endIndex === undefined) {
                                return org.openprovenance.apache.commons.lang.StringUtils.join$java_lang_Object_A$char(array, separator);
                            }
                            else if (((array != null && (array instanceof Object)) || array === null) && ((typeof separator === 'string') || separator === null) && startIndex === undefined && endIndex === undefined) {
                                return org.openprovenance.apache.commons.lang.StringUtils.join$java_util_Iterator$char(array, separator);
                            }
                            else if (((array != null && (array instanceof Array)) || array === null) && ((typeof separator === 'string') || separator === null) && startIndex === undefined && endIndex === undefined) {
                                return org.openprovenance.apache.commons.lang.StringUtils.join$java_util_Collection$char(array, separator);
                            }
                            else if (((array != null && array instanceof Array && (array.length == 0 || array[0] == null || (array[0] != null))) || array === null) && separator === undefined && startIndex === undefined && endIndex === undefined) {
                                return org.openprovenance.apache.commons.lang.StringUtils.join$java_lang_Object_A(array);
                            }
                            else
                                throw new Error('invalid overload');
                        }
                        static join$java_util_Iterator$char(iterator, separator) {
                            if (iterator == null) {
                                return null;
                            }
                            if (!iterator.hasNext()) {
                                return StringUtils.EMPTY;
                            }
                            const first = iterator.next();
                            if (!iterator.hasNext()) {
                                return org.openprovenance.apache.commons.lang.ObjectUtils.toString$java_lang_Object(first);
                            }
                            const buf = new org.openprovenance.apache.commons.lang.text.StrBuilder(256);
                            if (first != null) {
                                buf.append$java_lang_Object(first);
                            }
                            while ((iterator.hasNext())) {
                                {
                                    buf.append$char(separator);
                                    const obj = iterator.next();
                                    if (obj != null) {
                                        buf.append$java_lang_Object(obj);
                                    }
                                }
                            }
                            ;
                            return buf.toString();
                        }
                        static join$java_util_Iterator$java_lang_String(iterator, separator) {
                            if (iterator == null) {
                                return null;
                            }
                            if (!iterator.hasNext()) {
                                return StringUtils.EMPTY;
                            }
                            const first = iterator.next();
                            if (!iterator.hasNext()) {
                                return org.openprovenance.apache.commons.lang.ObjectUtils.toString$java_lang_Object(first);
                            }
                            const buf = new org.openprovenance.apache.commons.lang.text.StrBuilder(256);
                            if (first != null) {
                                buf.append$java_lang_Object(first);
                            }
                            while ((iterator.hasNext())) {
                                {
                                    if (separator != null) {
                                        buf.append$java_lang_String(separator);
                                    }
                                    const obj = iterator.next();
                                    if (obj != null) {
                                        buf.append$java_lang_Object(obj);
                                    }
                                }
                            }
                            ;
                            return buf.toString();
                        }
                        static join$java_util_Collection$char(collection, separator) {
                            if (collection == null) {
                                return null;
                            }
                            return StringUtils.join$java_util_Iterator$char(/* iterator */ ((a) => { var i = 0; return { next: function () { return i < a.length ? a[i++] : null; }, hasNext: function () { return i < a.length; } }; })(collection), separator);
                        }
                        static join$java_util_Collection$java_lang_String(collection, separator) {
                            if (collection == null) {
                                return null;
                            }
                            return StringUtils.join$java_util_Iterator$java_lang_String(/* iterator */ ((a) => { var i = 0; return { next: function () { return i < a.length ? a[i++] : null; }, hasNext: function () { return i < a.length; } }; })(collection), separator);
                        }
                        /**
                         * <p>Deletes all 'space' characters from a String as defined by
                         * {@link Character#isSpace(char)}.</p>
                         *
                         * <p>This is the only StringUtils method that uses the
                         * <code>isSpace</code> definition. You are advised to use
                         * {@link #deleteWhitespace(String)} instead as whitespace is much
                         * better localized.</p>
                         *
                         * <pre>
                         * StringUtils.deleteSpaces(null)           = null
                         * StringUtils.deleteSpaces("")             = ""
                         * StringUtils.deleteSpaces("abc")          = "abc"
                         * StringUtils.deleteSpaces(" \t  abc \n ") = "abc"
                         * StringUtils.deleteSpaces("ab  c")        = "abc"
                         * StringUtils.deleteSpaces("a\nb\tc     ") = "abc"
                         * </pre>
                         *
                         * <p>Spaces are defined as <code>{' ', '\t', '\r', '\n', '\b'}</code>
                         * in line with the deprecated <code>isSpace</code> method.</p>
                         *
                         * @param {string} str  the String to delete spaces from, may be null
                         * @return {string} the String without 'spaces', <code>null</code> if null String input
                         * @deprecated Use the better localized {@link #deleteWhitespace(String)}.
                         * Method will be removed in Commons Lang 3.0.
                         */
                        static deleteSpaces(str) {
                            if (str == null) {
                                return null;
                            }
                            return org.openprovenance.apache.commons.lang.CharSetUtils.delete$java_lang_String$java_lang_String(str, " \t\r\n\b");
                        }
                        /**
                         * <p>Deletes all whitespaces from a String as defined by
                         * {@link Character#isWhitespace(char)}.</p>
                         *
                         * <pre>
                         * StringUtils.deleteWhitespace(null)         = null
                         * StringUtils.deleteWhitespace("")           = ""
                         * StringUtils.deleteWhitespace("abc")        = "abc"
                         * StringUtils.deleteWhitespace("   ab  c  ") = "abc"
                         * </pre>
                         *
                         * @param {string} str  the String to delete whitespace from, may be null
                         * @return {string} the String without whitespaces, <code>null</code> if null String input
                         */
                        static deleteWhitespace(str) {
                            if (StringUtils.isEmpty(str)) {
                                return str;
                            }
                            const sz = str.length;
                            const chs = (s => { let a = []; while (s-- > 0)
                                a.push(null); return a; })(sz);
                            let count = 0;
                            for (let i = 0; i < sz; i++) {
                                {
                                    if (!javaemul.internal.CharacterHelper.isWhitespace(str.charAt(i))) {
                                        chs[count++] = str.charAt(i);
                                    }
                                }
                                ;
                            }
                            if (count === sz) {
                                return str;
                            }
                            return chs.join('').substr(0, count);
                        }
                        /**
                         * <p>Removes a substring only if it is at the begining of a source string,
                         * otherwise returns the source string.</p>
                         *
                         * <p>A <code>null</code> source string will return <code>null</code>.
                         * An empty ("") source string will return the empty string.
                         * A <code>null</code> search string will return the source string.</p>
                         *
                         * <pre>
                         * StringUtils.removeStart(null, *)      = null
                         * StringUtils.removeStart("", *)        = ""
                         * StringUtils.removeStart(*, null)      = *
                         * StringUtils.removeStart("www.domain.com", "www.")   = "domain.com"
                         * StringUtils.removeStart("domain.com", "www.")       = "domain.com"
                         * StringUtils.removeStart("www.domain.com", "domain") = "www.domain.com"
                         * StringUtils.removeStart("abc", "")    = "abc"
                         * </pre>
                         *
                         * @param {string} str  the source String to search, may be null
                         * @param {string} remove  the String to search for and remove, may be null
                         * @return {string} the substring with the string removed if found,
                         * <code>null</code> if null String input
                         * @since 2.1
                         */
                        static removeStart(str, remove) {
                            if (StringUtils.isEmpty(str) || StringUtils.isEmpty(remove)) {
                                return str;
                            }
                            if ( /* startsWith */((str, searchString, position = 0) => str.substr(position, searchString.length) === searchString)(str, remove)) {
                                return str.substring(remove.length);
                            }
                            return str;
                        }
                        /**
                         * <p>Case insensitive removal of a substring if it is at the begining of a source string,
                         * otherwise returns the source string.</p>
                         *
                         * <p>A <code>null</code> source string will return <code>null</code>.
                         * An empty ("") source string will return the empty string.
                         * A <code>null</code> search string will return the source string.</p>
                         *
                         * <pre>
                         * StringUtils.removeStartIgnoreCase(null, *)      = null
                         * StringUtils.removeStartIgnoreCase("", *)        = ""
                         * StringUtils.removeStartIgnoreCase(*, null)      = *
                         * StringUtils.removeStartIgnoreCase("www.domain.com", "www.")   = "domain.com"
                         * StringUtils.removeStartIgnoreCase("www.domain.com", "WWW.")   = "domain.com"
                         * StringUtils.removeStartIgnoreCase("domain.com", "www.")       = "domain.com"
                         * StringUtils.removeStartIgnoreCase("www.domain.com", "domain") = "www.domain.com"
                         * StringUtils.removeStartIgnoreCase("abc", "")    = "abc"
                         * </pre>
                         *
                         * @param {string} str  the source String to search, may be null
                         * @param {string} remove  the String to search for (case insensitive) and remove, may be null
                         * @return {string} the substring with the string removed if found,
                         * <code>null</code> if null String input
                         * @since 2.4
                         */
                        static removeStartIgnoreCase(str, remove) {
                            if (StringUtils.isEmpty(str) || StringUtils.isEmpty(remove)) {
                                return str;
                            }
                            if (StringUtils.startsWithIgnoreCase(str, remove)) {
                                return str.substring(remove.length);
                            }
                            return str;
                        }
                        /**
                         * <p>Removes a substring only if it is at the end of a source string,
                         * otherwise returns the source string.</p>
                         *
                         * <p>A <code>null</code> source string will return <code>null</code>.
                         * An empty ("") source string will return the empty string.
                         * A <code>null</code> search string will return the source string.</p>
                         *
                         * <pre>
                         * StringUtils.removeEnd(null, *)      = null
                         * StringUtils.removeEnd("", *)        = ""
                         * StringUtils.removeEnd(*, null)      = *
                         * StringUtils.removeEnd("www.domain.com", ".com.")  = "www.domain.com"
                         * StringUtils.removeEnd("www.domain.com", ".com")   = "www.domain"
                         * StringUtils.removeEnd("www.domain.com", "domain") = "www.domain.com"
                         * StringUtils.removeEnd("abc", "")    = "abc"
                         * </pre>
                         *
                         * @param {string} str  the source String to search, may be null
                         * @param {string} remove  the String to search for and remove, may be null
                         * @return {string} the substring with the string removed if found,
                         * <code>null</code> if null String input
                         * @since 2.1
                         */
                        static removeEnd(str, remove) {
                            if (StringUtils.isEmpty(str) || StringUtils.isEmpty(remove)) {
                                return str;
                            }
                            if ( /* endsWith */((str, searchString) => { let pos = str.length - searchString.length; let lastIndex = str.indexOf(searchString, pos); return lastIndex !== -1 && lastIndex === pos; })(str, remove)) {
                                return str.substring(0, str.length - remove.length);
                            }
                            return str;
                        }
                        /**
                         * <p>Case insensitive removal of a substring if it is at the end of a source string,
                         * otherwise returns the source string.</p>
                         *
                         * <p>A <code>null</code> source string will return <code>null</code>.
                         * An empty ("") source string will return the empty string.
                         * A <code>null</code> search string will return the source string.</p>
                         *
                         * <pre>
                         * StringUtils.removeEndIgnoreCase(null, *)      = null
                         * StringUtils.removeEndIgnoreCase("", *)        = ""
                         * StringUtils.removeEndIgnoreCase(*, null)      = *
                         * StringUtils.removeEndIgnoreCase("www.domain.com", ".com.")  = "www.domain.com"
                         * StringUtils.removeEndIgnoreCase("www.domain.com", ".com")   = "www.domain"
                         * StringUtils.removeEndIgnoreCase("www.domain.com", "domain") = "www.domain.com"
                         * StringUtils.removeEndIgnoreCase("abc", "")    = "abc"
                         * StringUtils.removeEndIgnoreCase("www.domain.com", ".COM") = "www.domain")
                         * StringUtils.removeEndIgnoreCase("www.domain.COM", ".com") = "www.domain")
                         * </pre>
                         *
                         * @param {string} str  the source String to search, may be null
                         * @param {string} remove  the String to search for (case insensitive) and remove, may be null
                         * @return {string} the substring with the string removed if found,
                         * <code>null</code> if null String input
                         * @since 2.4
                         */
                        static removeEndIgnoreCase(str, remove) {
                            if (StringUtils.isEmpty(str) || StringUtils.isEmpty(remove)) {
                                return str;
                            }
                            if (StringUtils.endsWithIgnoreCase(str, remove)) {
                                return str.substring(0, str.length - remove.length);
                            }
                            return str;
                        }
                        static remove$java_lang_String$java_lang_String(str, remove) {
                            if (StringUtils.isEmpty(str) || StringUtils.isEmpty(remove)) {
                                return str;
                            }
                            return StringUtils.replace$java_lang_String$java_lang_String$java_lang_String$int(str, remove, StringUtils.EMPTY, -1);
                        }
                        /**
                         * <p>Removes all occurrences of a substring from within the source string.</p>
                         *
                         * <p>A <code>null</code> source string will return <code>null</code>.
                         * An empty ("") source string will return the empty string.
                         * A <code>null</code> remove string will return the source string.
                         * An empty ("") remove string will return the source string.</p>
                         *
                         * <pre>
                         * StringUtils.remove(null, *)        = null
                         * StringUtils.remove("", *)          = ""
                         * StringUtils.remove(*, null)        = *
                         * StringUtils.remove(*, "")          = *
                         * StringUtils.remove("queued", "ue") = "qd"
                         * StringUtils.remove("queued", "zz") = "queued"
                         * </pre>
                         *
                         * @param {string} str  the source String to search, may be null
                         * @param {string} remove  the String to search for and remove, may be null
                         * @return {string} the substring with the string removed if found,
                         * <code>null</code> if null String input
                         * @since 2.1
                         */
                        static remove(str, remove) {
                            if (((typeof str === 'string') || str === null) && ((typeof remove === 'string') || remove === null)) {
                                return org.openprovenance.apache.commons.lang.StringUtils.remove$java_lang_String$java_lang_String(str, remove);
                            }
                            else if (((typeof str === 'string') || str === null) && ((typeof remove === 'string') || remove === null)) {
                                return org.openprovenance.apache.commons.lang.StringUtils.remove$java_lang_String$char(str, remove);
                            }
                            else
                                throw new Error('invalid overload');
                        }
                        static remove$java_lang_String$char(str, remove) {
                            if (StringUtils.isEmpty(str) || str.indexOf(remove) === StringUtils.INDEX_NOT_FOUND) {
                                return str;
                            }
                            const chars = (str).split('');
                            let pos = 0;
                            for (let i = 0; i < chars.length; i++) {
                                {
                                    if ((c => c.charCodeAt == null ? c : c.charCodeAt(0))(chars[i]) != (c => c.charCodeAt == null ? c : c.charCodeAt(0))(remove)) {
                                        chars[pos++] = chars[i];
                                    }
                                }
                                ;
                            }
                            return chars.join('').substr(0, pos);
                        }
                        /**
                         * <p>Replaces a String with another String inside a larger String, once.</p>
                         *
                         * <p>A <code>null</code> reference passed to this method is a no-op.</p>
                         *
                         * <pre>
                         * StringUtils.replaceOnce(null, *, *)        = null
                         * StringUtils.replaceOnce("", *, *)          = ""
                         * StringUtils.replaceOnce("any", null, *)    = "any"
                         * StringUtils.replaceOnce("any", *, null)    = "any"
                         * StringUtils.replaceOnce("any", "", *)      = "any"
                         * StringUtils.replaceOnce("aba", "a", null)  = "aba"
                         * StringUtils.replaceOnce("aba", "a", "")    = "ba"
                         * StringUtils.replaceOnce("aba", "a", "z")   = "zba"
                         * </pre>
                         *
                         * @see #replace(String text, String searchString, String replacement, int max)
                         * @param {string} text  text to search and replace in, may be null
                         * @param {string} searchString  the String to search for, may be null
                         * @param {string} replacement  the String to replace with, may be null
                         * @return {string} the text with any replacements processed,
                         * <code>null</code> if null String input
                         */
                        static replaceOnce(text, searchString, replacement) {
                            return StringUtils.replace$java_lang_String$java_lang_String$java_lang_String$int(text, searchString, replacement, 1);
                        }
                        static replace$java_lang_String$java_lang_String$java_lang_String(text, searchString, replacement) {
                            return StringUtils.replace$java_lang_String$java_lang_String$java_lang_String$int(text, searchString, replacement, -1);
                        }
                        static replace$java_lang_String$java_lang_String$java_lang_String$int(text, searchString, replacement, max) {
                            if (StringUtils.isEmpty(text) || StringUtils.isEmpty(searchString) || replacement == null || max === 0) {
                                return text;
                            }
                            let start = 0;
                            let end = text.indexOf(searchString, start);
                            if (end === StringUtils.INDEX_NOT_FOUND) {
                                return text;
                            }
                            const replLength = searchString.length;
                            let increase = replacement.length - replLength;
                            increase = (increase < 0 ? 0 : increase);
                            increase *= (max < 0 ? 16 : (max > 64 ? 64 : max));
                            const buf = new org.openprovenance.apache.commons.lang.text.StrBuilder(text.length + increase);
                            while ((end !== StringUtils.INDEX_NOT_FOUND)) {
                                {
                                    buf.append$java_lang_String(text.substring(start, end)).append$java_lang_String(replacement);
                                    start = end + replLength;
                                    if (--max === 0) {
                                        break;
                                    }
                                    end = text.indexOf(searchString, start);
                                }
                            }
                            ;
                            buf.append$java_lang_String(text.substring(start));
                            return buf.toString();
                        }
                        /**
                         * <p>Replaces a String with another String inside a larger String,
                         * for the first <code>max</code> values of the search String.</p>
                         *
                         * <p>A <code>null</code> reference passed to this method is a no-op.</p>
                         *
                         * <pre>
                         * StringUtils.replace(null, *, *, *)         = null
                         * StringUtils.replace("", *, *, *)           = ""
                         * StringUtils.replace("any", null, *, *)     = "any"
                         * StringUtils.replace("any", *, null, *)     = "any"
                         * StringUtils.replace("any", "", *, *)       = "any"
                         * StringUtils.replace("any", *, *, 0)        = "any"
                         * StringUtils.replace("abaa", "a", null, -1) = "abaa"
                         * StringUtils.replace("abaa", "a", "", -1)   = "b"
                         * StringUtils.replace("abaa", "a", "z", 0)   = "abaa"
                         * StringUtils.replace("abaa", "a", "z", 1)   = "zbaa"
                         * StringUtils.replace("abaa", "a", "z", 2)   = "zbza"
                         * StringUtils.replace("abaa", "a", "z", -1)  = "zbzz"
                         * </pre>
                         *
                         * @param {string} text  text to search and replace in, may be null
                         * @param {string} searchString  the String to search for, may be null
                         * @param {string} replacement  the String to replace it with, may be null
                         * @param {number} max  maximum number of values to replace, or <code>-1</code> if no maximum
                         * @return {string} the text with any replacements processed,
                         * <code>null</code> if null String input
                         */
                        static replace(text, searchString, replacement, max) {
                            if (((typeof text === 'string') || text === null) && ((typeof searchString === 'string') || searchString === null) && ((typeof replacement === 'string') || replacement === null) && ((typeof max === 'number') || max === null)) {
                                return org.openprovenance.apache.commons.lang.StringUtils.replace$java_lang_String$java_lang_String$java_lang_String$int(text, searchString, replacement, max);
                            }
                            else if (((typeof text === 'string') || text === null) && ((typeof searchString === 'string') || searchString === null) && ((typeof replacement === 'string') || replacement === null) && max === undefined) {
                                return org.openprovenance.apache.commons.lang.StringUtils.replace$java_lang_String$java_lang_String$java_lang_String(text, searchString, replacement);
                            }
                            else
                                throw new Error('invalid overload');
                        }
                        static replaceEach$java_lang_String$java_lang_String_A$java_lang_String_A(text, searchList, replacementList) {
                            return StringUtils.replaceEach$java_lang_String$java_lang_String_A$java_lang_String_A$boolean$int(text, searchList, replacementList, false, 0);
                        }
                        /**
                         * <p>
                         * Replaces all occurrences of Strings within another String.
                         * </p>
                         *
                         * <p>
                         * A <code>null</code> reference passed to this method is a no-op, or if
                         * any "search string" or "string to replace" is null, that replace will be
                         * ignored. This will not repeat. For repeating replaces, call the
                         * overloaded method.
                         * </p>
                         *
                         * <pre>
                         * StringUtils.replaceEach(null, *, *, *) = null
                         * StringUtils.replaceEach("", *, *, *) = ""
                         * StringUtils.replaceEach("aba", null, null, *) = "aba"
                         * StringUtils.replaceEach("aba", new String[0], null, *) = "aba"
                         * StringUtils.replaceEach("aba", null, new String[0], *) = "aba"
                         * StringUtils.replaceEach("aba", new String[]{"a"}, null, *) = "aba"
                         * StringUtils.replaceEach("aba", new String[]{"a"}, new String[]{""}, *) = "b"
                         * StringUtils.replaceEach("aba", new String[]{null}, new String[]{"a"}, *) = "aba"
                         * StringUtils.replaceEach("abcde", new String[]{"ab", "d"}, new String[]{"w", "t"}, *) = "wcte"
                         * (example of how it repeats)
                         * StringUtils.replaceEach("abcde", new String[]{"ab", "d"}, new String[]{"d", "t"}, false) = "dcte"
                         * StringUtils.replaceEach("abcde", new String[]{"ab", "d"}, new String[]{"d", "t"}, true) = "tcte"
                         * StringUtils.replaceEach("abcde", new String[]{"ab", "d"}, new String[]{"d", "ab"}, true) = IllegalArgumentException
                         * StringUtils.replaceEach("abcde", new String[]{"ab", "d"}, new String[]{"d", "ab"}, false) = "dcabe"
                         * </pre>
                         *
                         * @param {string} text
                         * text to search and replace in, no-op if null
                         * @param {java.lang.String[]} searchList
                         * the Strings to search for, no-op if null
                         * @param {java.lang.String[]} replacementList
                         * the Strings to replace them with, no-op if null
                         * @return {string} the text with any replacements processed, <code>null</code> if
                         * null String input
                         * @throws IllegalArgumentException
                         * if the search is repeating and there is an endless loop due
                         * to outputs of one being inputs to another
                         * @throws IndexOutOfBoundsException
                         * if the lengths of the arrays are not the same (null is ok,
                         * and/or size 0)
                         * @since 2.4
                         */
                        static replaceEachRepeatedly(text, searchList, replacementList) {
                            const timeToLive = searchList == null ? 0 : searchList.length;
                            return StringUtils.replaceEach$java_lang_String$java_lang_String_A$java_lang_String_A$boolean$int(text, searchList, replacementList, true, timeToLive);
                        }
                        static replaceEach$java_lang_String$java_lang_String_A$java_lang_String_A$boolean$int(text, searchList, replacementList, repeat, timeToLive) {
                            if (text == null || text.length === 0 || searchList == null || searchList.length === 0 || replacementList == null || replacementList.length === 0) {
                                return text;
                            }
                            if (timeToLive < 0) {
                                throw Object.defineProperty(new Error("TimeToLive of " + timeToLive + " is less than 0: " + text), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.IllegalStateException', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.Exception'] });
                            }
                            const searchLength = searchList.length;
                            const replacementLength = replacementList.length;
                            if (searchLength !== replacementLength) {
                                throw Object.defineProperty(new Error("Search and Replace array lengths don\'t match: " + searchLength + " vs " + replacementLength), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.IllegalArgumentException', 'java.lang.Exception'] });
                            }
                            const noMoreMatchesForReplIndex = (s => { let a = []; while (s-- > 0)
                                a.push(false); return a; })(searchLength);
                            let textIndex = -1;
                            let replaceIndex = -1;
                            let tempIndex = -1;
                            for (let i = 0; i < searchLength; i++) {
                                {
                                    if (noMoreMatchesForReplIndex[i] || searchList[i] == null || searchList[i].length === 0 || replacementList[i] == null) {
                                        continue;
                                    }
                                    tempIndex = text.indexOf(searchList[i]);
                                    if (tempIndex === -1) {
                                        noMoreMatchesForReplIndex[i] = true;
                                    }
                                    else {
                                        if (textIndex === -1 || tempIndex < textIndex) {
                                            textIndex = tempIndex;
                                            replaceIndex = i;
                                        }
                                    }
                                }
                                ;
                            }
                            if (textIndex === -1) {
                                return text;
                            }
                            let start = 0;
                            let increase = 0;
                            for (let i = 0; i < searchList.length; i++) {
                                {
                                    if (searchList[i] == null || replacementList[i] == null) {
                                        continue;
                                    }
                                    const greater = replacementList[i].length - searchList[i].length;
                                    if (greater > 0) {
                                        increase += 3 * greater;
                                    }
                                }
                                ;
                            }
                            increase = Math.min(increase, (text.length / 5 | 0));
                            const buf = new org.openprovenance.apache.commons.lang.text.StrBuilder(text.length + increase);
                            while ((textIndex !== -1)) {
                                {
                                    for (let i = start; i < textIndex; i++) {
                                        {
                                            buf.append$char(text.charAt(i));
                                        }
                                        ;
                                    }
                                    buf.append$java_lang_String(replacementList[replaceIndex]);
                                    start = textIndex + searchList[replaceIndex].length;
                                    textIndex = -1;
                                    replaceIndex = -1;
                                    tempIndex = -1;
                                    for (let i = 0; i < searchLength; i++) {
                                        {
                                            if (noMoreMatchesForReplIndex[i] || searchList[i] == null || searchList[i].length === 0 || replacementList[i] == null) {
                                                continue;
                                            }
                                            tempIndex = text.indexOf(searchList[i], start);
                                            if (tempIndex === -1) {
                                                noMoreMatchesForReplIndex[i] = true;
                                            }
                                            else {
                                                if (textIndex === -1 || tempIndex < textIndex) {
                                                    textIndex = tempIndex;
                                                    replaceIndex = i;
                                                }
                                            }
                                        }
                                        ;
                                    }
                                }
                            }
                            ;
                            const textLength = text.length;
                            for (let i = start; i < textLength; i++) {
                                {
                                    buf.append$char(text.charAt(i));
                                }
                                ;
                            }
                            const result = buf.toString();
                            if (!repeat) {
                                return result;
                            }
                            return StringUtils.replaceEach$java_lang_String$java_lang_String_A$java_lang_String_A$boolean$int(result, searchList, replacementList, repeat, timeToLive - 1);
                        }
                        /**
                         * <p>
                         * Replaces all occurrences of Strings within another String.
                         * </p>
                         *
                         * <p>
                         * A <code>null</code> reference passed to this method is a no-op, or if
                         * any "search string" or "string to replace" is null, that replace will be
                         * ignored.
                         * </p>
                         *
                         * <pre>
                         * StringUtils.replaceEach(null, *, *, *) = null
                         * StringUtils.replaceEach("", *, *, *) = ""
                         * StringUtils.replaceEach("aba", null, null, *) = "aba"
                         * StringUtils.replaceEach("aba", new String[0], null, *) = "aba"
                         * StringUtils.replaceEach("aba", null, new String[0], *) = "aba"
                         * StringUtils.replaceEach("aba", new String[]{"a"}, null, *) = "aba"
                         * StringUtils.replaceEach("aba", new String[]{"a"}, new String[]{""}, *) = "b"
                         * StringUtils.replaceEach("aba", new String[]{null}, new String[]{"a"}, *) = "aba"
                         * StringUtils.replaceEach("abcde", new String[]{"ab", "d"}, new String[]{"w", "t"}, *) = "wcte"
                         * (example of how it repeats)
                         * StringUtils.replaceEach("abcde", new String[]{"ab", "d"}, new String[]{"d", "t"}, false) = "dcte"
                         * StringUtils.replaceEach("abcde", new String[]{"ab", "d"}, new String[]{"d", "t"}, true) = "tcte"
                         * StringUtils.replaceEach("abcde", new String[]{"ab", "d"}, new String[]{"d", "ab"}, *) = IllegalArgumentException
                         * </pre>
                         *
                         * @param {string} text
                         * text to search and replace in, no-op if null
                         * @param {java.lang.String[]} searchList
                         * the Strings to search for, no-op if null
                         * @param {java.lang.String[]} replacementList
                         * the Strings to replace them with, no-op if null
                         * @param {boolean} repeat if true, then replace repeatedly
                         * until there are no more possible replacements or timeToLive < 0
                         * @param {number} timeToLive
                         * if less than 0 then there is a circular reference and endless
                         * loop
                         * @return {string} the text with any replacements processed, <code>null</code> if
                         * null String input
                         * @throws IllegalArgumentException
                         * if the search is repeating and there is an endless loop due
                         * to outputs of one being inputs to another
                         * @throws IndexOutOfBoundsException
                         * if the lengths of the arrays are not the same (null is ok,
                         * and/or size 0)
                         * @since 2.4
                         * @private
                         */
                        static replaceEach(text, searchList, replacementList, repeat, timeToLive) {
                            if (((typeof text === 'string') || text === null) && ((searchList != null && searchList instanceof Array && (searchList.length == 0 || searchList[0] == null || (typeof searchList[0] === 'string'))) || searchList === null) && ((replacementList != null && replacementList instanceof Array && (replacementList.length == 0 || replacementList[0] == null || (typeof replacementList[0] === 'string'))) || replacementList === null) && ((typeof repeat === 'boolean') || repeat === null) && ((typeof timeToLive === 'number') || timeToLive === null)) {
                                return org.openprovenance.apache.commons.lang.StringUtils.replaceEach$java_lang_String$java_lang_String_A$java_lang_String_A$boolean$int(text, searchList, replacementList, repeat, timeToLive);
                            }
                            else if (((typeof text === 'string') || text === null) && ((searchList != null && searchList instanceof Array && (searchList.length == 0 || searchList[0] == null || (typeof searchList[0] === 'string'))) || searchList === null) && ((replacementList != null && replacementList instanceof Array && (replacementList.length == 0 || replacementList[0] == null || (typeof replacementList[0] === 'string'))) || replacementList === null) && repeat === undefined && timeToLive === undefined) {
                                return org.openprovenance.apache.commons.lang.StringUtils.replaceEach$java_lang_String$java_lang_String_A$java_lang_String_A(text, searchList, replacementList);
                            }
                            else
                                throw new Error('invalid overload');
                        }
                        static replaceChars$java_lang_String$char$char(str, searchChar, replaceChar) {
                            if (str == null) {
                                return null;
                            }
                            return /* replace */ str.split(searchChar).join(replaceChar);
                        }
                        static replaceChars$java_lang_String$java_lang_String$java_lang_String(str, searchChars, replaceChars) {
                            if (StringUtils.isEmpty(str) || StringUtils.isEmpty(searchChars)) {
                                return str;
                            }
                            if (replaceChars == null) {
                                replaceChars = StringUtils.EMPTY;
                            }
                            let modified = false;
                            const replaceCharsLength = replaceChars.length;
                            const strLength = str.length;
                            const buf = new org.openprovenance.apache.commons.lang.text.StrBuilder(strLength);
                            for (let i = 0; i < strLength; i++) {
                                {
                                    const ch = str.charAt(i);
                                    const index = searchChars.indexOf(ch);
                                    if (index >= 0) {
                                        modified = true;
                                        if (index < replaceCharsLength) {
                                            buf.append$char(replaceChars.charAt(index));
                                        }
                                    }
                                    else {
                                        buf.append$char(ch);
                                    }
                                }
                                ;
                            }
                            if (modified) {
                                return buf.toString();
                            }
                            return str;
                        }
                        /**
                         * <p>Replaces multiple characters in a String in one go.
                         * This method can also be used to delete characters.</p>
                         *
                         * <p>For example:
                         * {@code replaceChars(&quot;hello&quot;, &quot;ho&quot;, &quot;jy&quot;) = jelly}.</p>
                         *
                         * <p>A {@code null} string input returns {@code null}.
                         * An empty ("") string input returns an empty string.
                         * A null or empty set of search characters returns the input string.</p>
                         *
                         * <p>The length of the search characters should normally equal the length
                         * of the replace characters.
                         * If the search characters is longer, then the extra search characters
                         * are deleted.
                         * If the search characters is shorter, then the extra replace characters
                         * are ignored.</p>
                         *
                         * <pre>
                         * StringUtils.replaceChars(null, *, *)           = null
                         * StringUtils.replaceChars("", *, *)             = ""
                         * StringUtils.replaceChars("abc", null, *)       = "abc"
                         * StringUtils.replaceChars("abc", "", *)         = "abc"
                         * StringUtils.replaceChars("abc", "b", null)     = "ac"
                         * StringUtils.replaceChars("abc", "b", "")       = "ac"
                         * StringUtils.replaceChars("abcba", "bc", "yz")  = "ayzya"
                         * StringUtils.replaceChars("abcba", "bc", "y")   = "ayya"
                         * StringUtils.replaceChars("abcba", "bc", "yzx") = "ayzya"
                         * </pre>
                         *
                         * @param {string} str  String to replace characters in, may be null
                         * @param {string} searchChars  a set of characters to search for, may be null
                         * @param {string} replaceChars  a set of characters to replace, may be null
                         * @return {string} modified String, {@code null} if null string input
                         * @since 2.0
                         */
                        static replaceChars(str, searchChars, replaceChars) {
                            if (((typeof str === 'string') || str === null) && ((typeof searchChars === 'string') || searchChars === null) && ((typeof replaceChars === 'string') || replaceChars === null)) {
                                return org.openprovenance.apache.commons.lang.StringUtils.replaceChars$java_lang_String$java_lang_String$java_lang_String(str, searchChars, replaceChars);
                            }
                            else if (((typeof str === 'string') || str === null) && ((typeof searchChars === 'string') || searchChars === null) && ((typeof replaceChars === 'string') || replaceChars === null)) {
                                return org.openprovenance.apache.commons.lang.StringUtils.replaceChars$java_lang_String$char$char(str, searchChars, replaceChars);
                            }
                            else
                                throw new Error('invalid overload');
                        }
                        /**
                         * <p>Overlays part of a String with another String.</p>
                         *
                         * <pre>
                         * StringUtils.overlayString(null, *, *, *)           = NullPointerException
                         * StringUtils.overlayString(*, null, *, *)           = NullPointerException
                         * StringUtils.overlayString("", "abc", 0, 0)         = "abc"
                         * StringUtils.overlayString("abcdef", null, 2, 4)    = "abef"
                         * StringUtils.overlayString("abcdef", "", 2, 4)      = "abef"
                         * StringUtils.overlayString("abcdef", "zzzz", 2, 4)  = "abzzzzef"
                         * StringUtils.overlayString("abcdef", "zzzz", 4, 2)  = "abcdzzzzcdef"
                         * StringUtils.overlayString("abcdef", "zzzz", -1, 4) = IndexOutOfBoundsException
                         * StringUtils.overlayString("abcdef", "zzzz", 2, 8)  = IndexOutOfBoundsException
                         * </pre>
                         *
                         * @param {string} text  the String to do overlaying in, may be null
                         * @param {string} overlay  the String to overlay, may be null
                         * @param {number} start  the position to start overlaying at, must be valid
                         * @param {number} end  the position to stop overlaying before, must be valid
                         * @return {string} overlayed String, {@code null} if null String input
                         * @throws NullPointerException if text or overlay is null
                         * @throws IndexOutOfBoundsException if either position is invalid
                         * @deprecated Use better named {@link #overlay(String, String, int, int)} instead.
                         * Method will be removed in Commons Lang 3.0.
                         */
                        static overlayString(text, overlay, start, end) {
                            return new org.openprovenance.apache.commons.lang.text.StrBuilder(start + overlay.length + text.length - end + 1).append$java_lang_String(text.substring(0, start)).append$java_lang_String(overlay).append$java_lang_String(text.substring(end)).toString();
                        }
                        /**
                         * <p>Overlays part of a String with another String.</p>
                         *
                         * <p>A {@code null} string input returns {@code null}.
                         * A negative index is treated as zero.
                         * An index greater than the string length is treated as the string length.
                         * The start index is always the smaller of the two indices.</p>
                         *
                         * <pre>
                         * StringUtils.overlay(null, *, *, *)            = null
                         * StringUtils.overlay("", "abc", 0, 0)          = "abc"
                         * StringUtils.overlay("abcdef", null, 2, 4)     = "abef"
                         * StringUtils.overlay("abcdef", "", 2, 4)       = "abef"
                         * StringUtils.overlay("abcdef", "", 4, 2)       = "abef"
                         * StringUtils.overlay("abcdef", "zzzz", 2, 4)   = "abzzzzef"
                         * StringUtils.overlay("abcdef", "zzzz", 4, 2)   = "abzzzzef"
                         * StringUtils.overlay("abcdef", "zzzz", -1, 4)  = "zzzzef"
                         * StringUtils.overlay("abcdef", "zzzz", 2, 8)   = "abzzzz"
                         * StringUtils.overlay("abcdef", "zzzz", -2, -3) = "zzzzabcdef"
                         * StringUtils.overlay("abcdef", "zzzz", 8, 10)  = "abcdefzzzz"
                         * </pre>
                         *
                         * @param {string} str  the String to do overlaying in, may be null
                         * @param {string} overlay  the String to overlay, may be null
                         * @param {number} start  the position to start overlaying at
                         * @param {number} end  the position to stop overlaying before
                         * @return {string} overlayed String, {@code null} if null String input
                         * @since 2.0
                         */
                        static overlay(str, overlay, start, end) {
                            if (str == null) {
                                return null;
                            }
                            if (overlay == null) {
                                overlay = StringUtils.EMPTY;
                            }
                            const len = str.length;
                            if (start < 0) {
                                start = 0;
                            }
                            if (start > len) {
                                start = len;
                            }
                            if (end < 0) {
                                end = 0;
                            }
                            if (end > len) {
                                end = len;
                            }
                            if (start > end) {
                                const temp = start;
                                start = end;
                                end = temp;
                            }
                            return new org.openprovenance.apache.commons.lang.text.StrBuilder(len + start - end + overlay.length + 1).append$java_lang_String(str.substring(0, start)).append$java_lang_String(overlay).append$java_lang_String(str.substring(end)).toString();
                        }
                        static chomp$java_lang_String(str) {
                            if (StringUtils.isEmpty(str)) {
                                return str;
                            }
                            if (str.length === 1) {
                                const ch = str.charAt(0);
                                if ((c => c.charCodeAt == null ? c : c.charCodeAt(0))(ch) == (c => c.charCodeAt == null ? c : c.charCodeAt(0))(org.openprovenance.apache.commons.lang.CharUtils.CR) || (c => c.charCodeAt == null ? c : c.charCodeAt(0))(ch) == (c => c.charCodeAt == null ? c : c.charCodeAt(0))(org.openprovenance.apache.commons.lang.CharUtils.LF)) {
                                    return StringUtils.EMPTY;
                                }
                                return str;
                            }
                            let lastIdx = str.length - 1;
                            const last = str.charAt(lastIdx);
                            if ((c => c.charCodeAt == null ? c : c.charCodeAt(0))(last) == (c => c.charCodeAt == null ? c : c.charCodeAt(0))(org.openprovenance.apache.commons.lang.CharUtils.LF)) {
                                if ((c => c.charCodeAt == null ? c : c.charCodeAt(0))(str.charAt(lastIdx - 1)) == (c => c.charCodeAt == null ? c : c.charCodeAt(0))(org.openprovenance.apache.commons.lang.CharUtils.CR)) {
                                    lastIdx--;
                                }
                            }
                            else if ((c => c.charCodeAt == null ? c : c.charCodeAt(0))(last) != (c => c.charCodeAt == null ? c : c.charCodeAt(0))(org.openprovenance.apache.commons.lang.CharUtils.CR)) {
                                lastIdx++;
                            }
                            return str.substring(0, lastIdx);
                        }
                        static chomp$java_lang_String$java_lang_String(str, separator) {
                            if (StringUtils.isEmpty(str) || separator == null) {
                                return str;
                            }
                            if ( /* endsWith */((str, searchString) => { let pos = str.length - searchString.length; let lastIndex = str.indexOf(searchString, pos); return lastIndex !== -1 && lastIndex === pos; })(str, separator)) {
                                return str.substring(0, str.length - separator.length);
                            }
                            return str;
                        }
                        /**
                         * <p>Removes {@code separator} from the end of
                         * {@code str} if it's there, otherwise leave it alone.</p>
                         *
                         * <p>NOTE: This method changed in version 2.0.
                         * It now more closely matches Perl chomp.
                         * For the previous behavior, use {@link #substringBeforeLast(String, String)}.
                         * This method uses {@link String#endsWith(String)}.</p>
                         *
                         * <pre>
                         * StringUtils.chomp(null, *)         = null
                         * StringUtils.chomp("", *)           = ""
                         * StringUtils.chomp("foobar", "bar") = "foo"
                         * StringUtils.chomp("foobar", "baz") = "foobar"
                         * StringUtils.chomp("foo", "foo")    = ""
                         * StringUtils.chomp("foo ", "foo")   = "foo "
                         * StringUtils.chomp(" foo", "foo")   = " "
                         * StringUtils.chomp("foo", "foooo")  = "foo"
                         * StringUtils.chomp("foo", "")       = "foo"
                         * StringUtils.chomp("foo", null)     = "foo"
                         * </pre>
                         *
                         * @param {string} str  the String to chomp from, may be null
                         * @param {string} separator  separator String, may be null
                         * @return {string} String without trailing separator, {@code null} if null String input
                         */
                        static chomp(str, separator) {
                            if (((typeof str === 'string') || str === null) && ((typeof separator === 'string') || separator === null)) {
                                return org.openprovenance.apache.commons.lang.StringUtils.chomp$java_lang_String$java_lang_String(str, separator);
                            }
                            else if (((typeof str === 'string') || str === null) && separator === undefined) {
                                return org.openprovenance.apache.commons.lang.StringUtils.chomp$java_lang_String(str);
                            }
                            else
                                throw new Error('invalid overload');
                        }
                        static chompLast$java_lang_String(str) {
                            return StringUtils.chompLast$java_lang_String$java_lang_String(str, "\n");
                        }
                        static chompLast$java_lang_String$java_lang_String(str, sep) {
                            if (str.length === 0) {
                                return str;
                            }
                            const sub = str.substring(str.length - sep.length);
                            if (sep === sub) {
                                return str.substring(0, str.length - sep.length);
                            }
                            return str;
                        }
                        /**
                         * <p>Remove a value if and only if the String ends with that value.</p>
                         *
                         * @param {string} str  the String to chomp from, must not be null
                         * @param {string} sep  the String to chomp, must not be null
                         * @return {string} String without chomped ending
                         * @throws NullPointerException if str or sep is {@code null}
                         * @deprecated Use {@link #chomp(String,String)} instead.
                         * Method will be removed in Commons Lang 3.0.
                         */
                        static chompLast(str, sep) {
                            if (((typeof str === 'string') || str === null) && ((typeof sep === 'string') || sep === null)) {
                                return org.openprovenance.apache.commons.lang.StringUtils.chompLast$java_lang_String$java_lang_String(str, sep);
                            }
                            else if (((typeof str === 'string') || str === null) && sep === undefined) {
                                return org.openprovenance.apache.commons.lang.StringUtils.chompLast$java_lang_String(str);
                            }
                            else
                                throw new Error('invalid overload');
                        }
                        /**
                         * <p>Remove everything and return the last value of a supplied String, and
                         * everything after it from a String.</p>
                         *
                         * @param {string} str  the String to chomp from, must not be null
                         * @param {string} sep  the String to chomp, must not be null
                         * @return {string} String chomped
                         * @throws NullPointerException if str or sep is {@code null}
                         * @deprecated Use {@link #substringAfterLast(String, String)} instead
                         * (although this doesn't include the separator)
                         * Method will be removed in Commons Lang 3.0.
                         */
                        static getChomp(str, sep) {
                            const idx = str.lastIndexOf(sep);
                            if (idx === str.length - sep.length) {
                                return sep;
                            }
                            else if (idx !== -1) {
                                return str.substring(idx);
                            }
                            else {
                                return StringUtils.EMPTY;
                            }
                        }
                        /**
                         * <p>Remove the first value of a supplied String, and everything before it
                         * from a String.</p>
                         *
                         * @param {string} str  the String to chomp from, must not be null
                         * @param {string} sep  the String to chomp, must not be null
                         * @return {string} String without chomped beginning
                         * @throws NullPointerException if str or sep is {@code null}
                         * @deprecated Use {@link #substringAfter(String,String)} instead.
                         * Method will be removed in Commons Lang 3.0.
                         */
                        static prechomp(str, sep) {
                            const idx = str.indexOf(sep);
                            if (idx === -1) {
                                return str;
                            }
                            return str.substring(idx + sep.length);
                        }
                        /**
                         * <p>Remove and return everything before the first value of a
                         * supplied String from another String.</p>
                         *
                         * @param {string} str  the String to chomp from, must not be null
                         * @param {string} sep  the String to chomp, must not be null
                         * @return {string} String prechomped
                         * @throws NullPointerException if str or sep is {@code null}
                         * @deprecated Use {@link #substringBefore(String,String)} instead
                         * (although this doesn't include the separator).
                         * Method will be removed in Commons Lang 3.0.
                         */
                        static getPrechomp(str, sep) {
                            const idx = str.indexOf(sep);
                            if (idx === -1) {
                                return StringUtils.EMPTY;
                            }
                            return str.substring(0, idx + sep.length);
                        }
                        /**
                         * <p>Remove the last character from a String.</p>
                         *
                         * <p>If the String ends in {@code \r\n}, then remove both
                         * of them.</p>
                         *
                         * <pre>
                         * StringUtils.chop(null)          = null
                         * StringUtils.chop("")            = ""
                         * StringUtils.chop("abc \r")      = "abc "
                         * StringUtils.chop("abc\n")       = "abc"
                         * StringUtils.chop("abc\r\n")     = "abc"
                         * StringUtils.chop("abc")         = "ab"
                         * StringUtils.chop("abc\nabc")    = "abc\nab"
                         * StringUtils.chop("a")           = ""
                         * StringUtils.chop("\r")          = ""
                         * StringUtils.chop("\n")          = ""
                         * StringUtils.chop("\r\n")        = ""
                         * </pre>
                         *
                         * @param {string} str  the String to chop last character from, may be null
                         * @return {string} String without last character, {@code null} if null String input
                         */
                        static chop(str) {
                            if (str == null) {
                                return null;
                            }
                            const strLen = str.length;
                            if (strLen < 2) {
                                return StringUtils.EMPTY;
                            }
                            const lastIdx = strLen - 1;
                            const ret = str.substring(0, lastIdx);
                            const last = str.charAt(lastIdx);
                            if ((c => c.charCodeAt == null ? c : c.charCodeAt(0))(last) == (c => c.charCodeAt == null ? c : c.charCodeAt(0))(org.openprovenance.apache.commons.lang.CharUtils.LF)) {
                                if ((c => c.charCodeAt == null ? c : c.charCodeAt(0))(ret.charAt(lastIdx - 1)) == (c => c.charCodeAt == null ? c : c.charCodeAt(0))(org.openprovenance.apache.commons.lang.CharUtils.CR)) {
                                    return ret.substring(0, lastIdx - 1);
                                }
                            }
                            return ret;
                        }
                        /**
                         * <p>Removes {@code \n} from end of a String if it's there.
                         * If a {@code \r} precedes it, then remove that too.</p>
                         *
                         * @param {string} str  the String to chop a newline from, must not be null
                         * @return {string} String without newline
                         * @throws NullPointerException if str is {@code null}
                         * @deprecated Use {@link #chomp(String)} instead.
                         * Method will be removed in Commons Lang 3.0.
                         */
                        static chopNewline(str) {
                            let lastIdx = str.length - 1;
                            if (lastIdx <= 0) {
                                return StringUtils.EMPTY;
                            }
                            const last = str.charAt(lastIdx);
                            if ((c => c.charCodeAt == null ? c : c.charCodeAt(0))(last) == (c => c.charCodeAt == null ? c : c.charCodeAt(0))(org.openprovenance.apache.commons.lang.CharUtils.LF)) {
                                if ((c => c.charCodeAt == null ? c : c.charCodeAt(0))(str.charAt(lastIdx - 1)) == (c => c.charCodeAt == null ? c : c.charCodeAt(0))(org.openprovenance.apache.commons.lang.CharUtils.CR)) {
                                    lastIdx--;
                                }
                            }
                            else {
                                lastIdx++;
                            }
                            return str.substring(0, lastIdx);
                        }
                        /**
                         * <p>Escapes any values it finds into their String form.</p>
                         *
                         * <p>So a tab becomes the characters {@code '\\'} and
                         * {@code 't'}.</p>
                         *
                         * <p>As of Lang 2.0, this calls {@link StringEscapeUtils#escapeJava(String)}
                         * behind the scenes.
                         * </p>
                         * @see StringEscapeUtils#escapeJava(String)
                         * @param {string} str String to escape values in
                         * @return {string} String with escaped values
                         * @throws NullPointerException if str is {@code null}
                         * @deprecated Use {@link StringEscapeUtils#escapeJava(String)}
                         * This method will be removed in Commons Lang 3.0
                         */
                        static escape(str) {
                            return org.openprovenance.apache.commons.lang.StringEscapeUtils.escapeJava$java_lang_String(str);
                        }
                        static repeat$java_lang_String$int(str, repeat) {
                            if (str == null) {
                                return null;
                            }
                            if (repeat <= 0) {
                                return StringUtils.EMPTY;
                            }
                            const inputLength = str.length;
                            if (repeat === 1 || inputLength === 0) {
                                return str;
                            }
                            if (inputLength === 1 && repeat <= StringUtils.PAD_LIMIT) {
                                return StringUtils.padding(repeat, str.charAt(0));
                            }
                            const outputLength = inputLength * repeat;
                            switch ((inputLength)) {
                                case 1:
                                    const ch = str.charAt(0);
                                    const output1 = (s => { let a = []; while (s-- > 0)
                                        a.push(null); return a; })(outputLength);
                                    for (let i = repeat - 1; i >= 0; i--) {
                                        {
                                            output1[i] = ch;
                                        }
                                        ;
                                    }
                                    return output1.join('');
                                case 2:
                                    const ch0 = str.charAt(0);
                                    const ch1 = str.charAt(1);
                                    const output2 = (s => { let a = []; while (s-- > 0)
                                        a.push(null); return a; })(outputLength);
                                    for (let i = repeat * 2 - 2; i >= 0; i--, i--) {
                                        {
                                            output2[i] = ch0;
                                            output2[i + 1] = ch1;
                                        }
                                        ;
                                    }
                                    return output2.join('');
                                default:
                                    const buf = new org.openprovenance.apache.commons.lang.text.StrBuilder(outputLength);
                                    for (let i = 0; i < repeat; i++) {
                                        {
                                            buf.append$java_lang_String(str);
                                        }
                                        ;
                                    }
                                    return buf.toString();
                            }
                        }
                        static repeat$java_lang_String$java_lang_String$int(str, separator, repeat) {
                            if (str == null || separator == null) {
                                return StringUtils.repeat$java_lang_String$int(str, repeat);
                            }
                            else {
                                const result = StringUtils.repeat$java_lang_String$int(str + separator, repeat);
                                return StringUtils.removeEnd(result, separator);
                            }
                        }
                        /**
                         * <p>Repeat a String {@code repeat} times to form a
                         * new String, with a String separator injected each time. </p>
                         *
                         * <pre>
                         * StringUtils.repeat(null, null, 2) = null
                         * StringUtils.repeat(null, "x", 2)  = null
                         * StringUtils.repeat("", null, 0)   = ""
                         * StringUtils.repeat("", "", 2)     = ""
                         * StringUtils.repeat("", "x", 3)    = "xxx"
                         * StringUtils.repeat("?", ", ", 3)  = "?, ?, ?"
                         * </pre>
                         *
                         * @param {string} str        the String to repeat, may be null
                         * @param {string} separator  the String to inject, may be null
                         * @param {number} repeat     number of times to repeat str, negative treated as zero
                         * @return {string} a new String consisting of the original String repeated,
                         * {@code null} if null String input
                         * @since 2.5
                         */
                        static repeat(str, separator, repeat) {
                            if (((typeof str === 'string') || str === null) && ((typeof separator === 'string') || separator === null) && ((typeof repeat === 'number') || repeat === null)) {
                                return org.openprovenance.apache.commons.lang.StringUtils.repeat$java_lang_String$java_lang_String$int(str, separator, repeat);
                            }
                            else if (((typeof str === 'string') || str === null) && ((typeof separator === 'number') || separator === null) && repeat === undefined) {
                                return org.openprovenance.apache.commons.lang.StringUtils.repeat$java_lang_String$int(str, separator);
                            }
                            else
                                throw new Error('invalid overload');
                        }
                        /**
                         * <p>Returns padding using the specified delimiter repeated
                         * to a given length.</p>
                         *
                         * <pre>
                         * StringUtils.padding(0, 'e1')  = ""
                         * StringUtils.padding(3, 'e1')  = "eee"
                         * StringUtils.padding(-2, 'e1') = IndexOutOfBoundsException
                         * </pre>
                         *
                         * <p>Note: this method doesn't not support padding with
                         * <a href="http://www.unicode.org/glossary/#supplementary_character">Unicode Supplementary Characters</a>
                         * as they require a pair of {@code char}s to be represented.
                         * If you are needing to support full I18N of your applications
                         * consider using {@link #repeat(String, int)} instead.
                         * </p>
                         *
                         * @param {number} repeat  number of times to repeat delim
                         * @param {string} padChar  character to repeat
                         * @return {string} String with repeated character
                         * @throws IndexOutOfBoundsException if {@code repeat &lt; 0}
                         * @see #repeat(String, int)
                         * @private
                         */
                        /*private*/ static padding(repeat, padChar) {
                            if (repeat < 0) {
                                throw Object.defineProperty(new Error("Cannot pad a negative amount: " + repeat), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.IndexOutOfBoundsException', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.Exception'] });
                            }
                            const buf = (s => { let a = []; while (s-- > 0)
                                a.push(null); return a; })(repeat);
                            for (let i = 0; i < buf.length; i++) {
                                {
                                    buf[i] = padChar;
                                }
                                ;
                            }
                            return buf.join('');
                        }
                        static rightPad$java_lang_String$int(str, size) {
                            return StringUtils.rightPad$java_lang_String$int$char(str, size, ' ');
                        }
                        static rightPad$java_lang_String$int$char(str, size, padChar) {
                            if (str == null) {
                                return null;
                            }
                            const pads = size - str.length;
                            if (pads <= 0) {
                                return str;
                            }
                            if (pads > StringUtils.PAD_LIMIT) {
                                return StringUtils.rightPad$java_lang_String$int$java_lang_String(str, size, /* valueOf */ String(padChar).toString());
                            }
                            return str.concat(StringUtils.padding(pads, padChar));
                        }
                        static rightPad$java_lang_String$int$java_lang_String(str, size, padStr) {
                            if (str == null) {
                                return null;
                            }
                            if (StringUtils.isEmpty(padStr)) {
                                padStr = " ";
                            }
                            const padLen = padStr.length;
                            const strLen = str.length;
                            const pads = size - strLen;
                            if (pads <= 0) {
                                return str;
                            }
                            if (padLen === 1 && pads <= StringUtils.PAD_LIMIT) {
                                return StringUtils.rightPad$java_lang_String$int$char(str, size, padStr.charAt(0));
                            }
                            if (pads === padLen) {
                                return str.concat(padStr);
                            }
                            else if (pads < padLen) {
                                return str.concat(padStr.substring(0, pads));
                            }
                            else {
                                const padding = (s => { let a = []; while (s-- > 0)
                                    a.push(null); return a; })(pads);
                                const padChars = (padStr).split('');
                                for (let i = 0; i < pads; i++) {
                                    {
                                        padding[i] = padChars[i % padLen];
                                    }
                                    ;
                                }
                                return str.concat(padding.join(''));
                            }
                        }
                        /**
                         * <p>Right pad a String with a specified String.</p>
                         *
                         * <p>The String is padded to the size of {@code size}.</p>
                         *
                         * <pre>
                         * StringUtils.rightPad(null, *, *)      = null
                         * StringUtils.rightPad("", 3, "z")      = "zzz"
                         * StringUtils.rightPad("bat", 3, "yz")  = "bat"
                         * StringUtils.rightPad("bat", 5, "yz")  = "batyz"
                         * StringUtils.rightPad("bat", 8, "yz")  = "batyzyzy"
                         * StringUtils.rightPad("bat", 1, "yz")  = "bat"
                         * StringUtils.rightPad("bat", -1, "yz") = "bat"
                         * StringUtils.rightPad("bat", 5, null)  = "bat  "
                         * StringUtils.rightPad("bat", 5, "")    = "bat  "
                         * </pre>
                         *
                         * @param {string} str  the String to pad out, may be null
                         * @param {number} size  the size to pad to
                         * @param {string} padStr  the String to pad with, null or empty treated as single space
                         * @return {string} right padded String or original String if no padding is necessary,
                         * {@code null} if null String input
                         */
                        static rightPad(str, size, padStr) {
                            if (((typeof str === 'string') || str === null) && ((typeof size === 'number') || size === null) && ((typeof padStr === 'string') || padStr === null)) {
                                return org.openprovenance.apache.commons.lang.StringUtils.rightPad$java_lang_String$int$java_lang_String(str, size, padStr);
                            }
                            else if (((typeof str === 'string') || str === null) && ((typeof size === 'number') || size === null) && ((typeof padStr === 'string') || padStr === null)) {
                                return org.openprovenance.apache.commons.lang.StringUtils.rightPad$java_lang_String$int$char(str, size, padStr);
                            }
                            else if (((typeof str === 'string') || str === null) && ((typeof size === 'number') || size === null) && padStr === undefined) {
                                return org.openprovenance.apache.commons.lang.StringUtils.rightPad$java_lang_String$int(str, size);
                            }
                            else
                                throw new Error('invalid overload');
                        }
                        static leftPad$java_lang_String$int(str, size) {
                            return StringUtils.leftPad$java_lang_String$int$char(str, size, ' ');
                        }
                        static leftPad$java_lang_String$int$char(str, size, padChar) {
                            if (str == null) {
                                return null;
                            }
                            const pads = size - str.length;
                            if (pads <= 0) {
                                return str;
                            }
                            if (pads > StringUtils.PAD_LIMIT) {
                                return StringUtils.leftPad$java_lang_String$int$java_lang_String(str, size, /* valueOf */ String(padChar).toString());
                            }
                            return StringUtils.padding(pads, padChar).concat(str);
                        }
                        static leftPad$java_lang_String$int$java_lang_String(str, size, padStr) {
                            if (str == null) {
                                return null;
                            }
                            if (StringUtils.isEmpty(padStr)) {
                                padStr = " ";
                            }
                            const padLen = padStr.length;
                            const strLen = str.length;
                            const pads = size - strLen;
                            if (pads <= 0) {
                                return str;
                            }
                            if (padLen === 1 && pads <= StringUtils.PAD_LIMIT) {
                                return StringUtils.leftPad$java_lang_String$int$char(str, size, padStr.charAt(0));
                            }
                            if (pads === padLen) {
                                return padStr.concat(str);
                            }
                            else if (pads < padLen) {
                                return padStr.substring(0, pads).concat(str);
                            }
                            else {
                                const padding = (s => { let a = []; while (s-- > 0)
                                    a.push(null); return a; })(pads);
                                const padChars = (padStr).split('');
                                for (let i = 0; i < pads; i++) {
                                    {
                                        padding[i] = padChars[i % padLen];
                                    }
                                    ;
                                }
                                return padding.join('').concat(str);
                            }
                        }
                        /**
                         * <p>Left pad a String with a specified String.</p>
                         *
                         * <p>Pad to a size of {@code size}.</p>
                         *
                         * <pre>
                         * StringUtils.leftPad(null, *, *)      = null
                         * StringUtils.leftPad("", 3, "z")      = "zzz"
                         * StringUtils.leftPad("bat", 3, "yz")  = "bat"
                         * StringUtils.leftPad("bat", 5, "yz")  = "yzbat"
                         * StringUtils.leftPad("bat", 8, "yz")  = "yzyzybat"
                         * StringUtils.leftPad("bat", 1, "yz")  = "bat"
                         * StringUtils.leftPad("bat", -1, "yz") = "bat"
                         * StringUtils.leftPad("bat", 5, null)  = "  bat"
                         * StringUtils.leftPad("bat", 5, "")    = "  bat"
                         * </pre>
                         *
                         * @param {string} str  the String to pad out, may be null
                         * @param {number} size  the size to pad to
                         * @param {string} padStr  the String to pad with, null or empty treated as single space
                         * @return {string} left padded String or original String if no padding is necessary,
                         * {@code null} if null String input
                         */
                        static leftPad(str, size, padStr) {
                            if (((typeof str === 'string') || str === null) && ((typeof size === 'number') || size === null) && ((typeof padStr === 'string') || padStr === null)) {
                                return org.openprovenance.apache.commons.lang.StringUtils.leftPad$java_lang_String$int$java_lang_String(str, size, padStr);
                            }
                            else if (((typeof str === 'string') || str === null) && ((typeof size === 'number') || size === null) && ((typeof padStr === 'string') || padStr === null)) {
                                return org.openprovenance.apache.commons.lang.StringUtils.leftPad$java_lang_String$int$char(str, size, padStr);
                            }
                            else if (((typeof str === 'string') || str === null) && ((typeof size === 'number') || size === null) && padStr === undefined) {
                                return org.openprovenance.apache.commons.lang.StringUtils.leftPad$java_lang_String$int(str, size);
                            }
                            else
                                throw new Error('invalid overload');
                        }
                        /**
                         * Gets a String's length or {@code 0} if the String is {@code null}.
                         *
                         * @param {string} str
                         * a String or {@code null}
                         * @return {number} String length or {@code 0} if the String is {@code null}.
                         * @since 2.4
                         */
                        static length(str) {
                            return str == null ? 0 : str.length;
                        }
                        static center$java_lang_String$int(str, size) {
                            return StringUtils.center$java_lang_String$int$char(str, size, ' ');
                        }
                        static center$java_lang_String$int$char(str, size, padChar) {
                            if (str == null || size <= 0) {
                                return str;
                            }
                            const strLen = str.length;
                            const pads = size - strLen;
                            if (pads <= 0) {
                                return str;
                            }
                            str = StringUtils.leftPad$java_lang_String$int$char(str, strLen + (pads / 2 | 0), padChar);
                            str = StringUtils.rightPad$java_lang_String$int$char(str, size, padChar);
                            return str;
                        }
                        static center$java_lang_String$int$java_lang_String(str, size, padStr) {
                            if (str == null || size <= 0) {
                                return str;
                            }
                            if (StringUtils.isEmpty(padStr)) {
                                padStr = " ";
                            }
                            const strLen = str.length;
                            const pads = size - strLen;
                            if (pads <= 0) {
                                return str;
                            }
                            str = StringUtils.leftPad$java_lang_String$int$java_lang_String(str, strLen + (pads / 2 | 0), padStr);
                            str = StringUtils.rightPad$java_lang_String$int$java_lang_String(str, size, padStr);
                            return str;
                        }
                        /**
                         * <p>Centers a String in a larger String of size {@code size}.
                         * Uses a supplied String as the value to pad the String with.</p>
                         *
                         * <p>If the size is less than the String length, the String is returned.
                         * A {@code null} String returns {@code null}.
                         * A negative size is treated as zero.</p>
                         *
                         * <pre>
                         * StringUtils.center(null, *, *)     = null
                         * StringUtils.center("", 4, " ")     = "    "
                         * StringUtils.center("ab", -1, " ")  = "ab"
                         * StringUtils.center("ab", 4, " ")   = " ab"
                         * StringUtils.center("abcd", 2, " ") = "abcd"
                         * StringUtils.center("a", 4, " ")    = " a  "
                         * StringUtils.center("a", 4, "yz")   = "yayz"
                         * StringUtils.center("abc", 7, null) = "  abc  "
                         * StringUtils.center("abc", 7, "")   = "  abc  "
                         * </pre>
                         *
                         * @param {string} str  the String to center, may be null
                         * @param {number} size  the int size of new String, negative treated as zero
                         * @param {string} padStr  the String to pad the new String with, must not be null or empty
                         * @return {string} centered String, {@code null} if null String input
                         * @throws IllegalArgumentException if padStr is {@code null} or empty
                         */
                        static center(str, size, padStr) {
                            if (((typeof str === 'string') || str === null) && ((typeof size === 'number') || size === null) && ((typeof padStr === 'string') || padStr === null)) {
                                return org.openprovenance.apache.commons.lang.StringUtils.center$java_lang_String$int$java_lang_String(str, size, padStr);
                            }
                            else if (((typeof str === 'string') || str === null) && ((typeof size === 'number') || size === null) && ((typeof padStr === 'string') || padStr === null)) {
                                return org.openprovenance.apache.commons.lang.StringUtils.center$java_lang_String$int$char(str, size, padStr);
                            }
                            else if (((typeof str === 'string') || str === null) && ((typeof size === 'number') || size === null) && padStr === undefined) {
                                return org.openprovenance.apache.commons.lang.StringUtils.center$java_lang_String$int(str, size);
                            }
                            else
                                throw new Error('invalid overload');
                        }
                        static upperCase$java_lang_String(str) {
                            if (str == null) {
                                return null;
                            }
                            return str.toUpperCase();
                        }
                        static upperCase$java_lang_String$java_util_Locale(str, locale) {
                            if (str == null) {
                                return null;
                            }
                            return /* toUpperCase */ str.toUpperCase();
                        }
                        /**
                         * <p>Converts a String to upper case as per {@link String#toUpperCase(Locale)}.</p>
                         *
                         * <p>A {@code null} input String returns {@code null}.</p>
                         *
                         * <pre>
                         * StringUtils.upperCase(null, Locale.ENGLISH)  = null
                         * StringUtils.upperCase("", Locale.ENGLISH)    = ""
                         * StringUtils.upperCase("aBc", Locale.ENGLISH) = "ABC"
                         * </pre>
                         *
                         * @param {string} str  the String to upper case, may be null
                         * @param {string} locale  the locale that defines the case transformation rules, must not be null
                         * @return {string} the upper cased String, {@code null} if null String input
                         * @since 2.5
                         */
                        static upperCase(str, locale) {
                            if (((typeof str === 'string') || str === null) && ((typeof locale === 'string') || locale === null)) {
                                return org.openprovenance.apache.commons.lang.StringUtils.upperCase$java_lang_String$java_util_Locale(str, locale);
                            }
                            else if (((typeof str === 'string') || str === null) && locale === undefined) {
                                return org.openprovenance.apache.commons.lang.StringUtils.upperCase$java_lang_String(str);
                            }
                            else
                                throw new Error('invalid overload');
                        }
                        static lowerCase$java_lang_String(str) {
                            if (str == null) {
                                return null;
                            }
                            return str.toLowerCase();
                        }
                        static lowerCase$java_lang_String$java_util_Locale(str, locale) {
                            if (str == null) {
                                return null;
                            }
                            return /* toLowerCase */ str.toLowerCase();
                        }
                        /**
                         * <p>Converts a String to lower case as per {@link String#toLowerCase(Locale)}.</p>
                         *
                         * <p>A {@code null} input String returns {@code null}.</p>
                         *
                         * <pre>
                         * StringUtils.lowerCase(null, Locale.ENGLISH)  = null
                         * StringUtils.lowerCase("", Locale.ENGLISH)    = ""
                         * StringUtils.lowerCase("aBc", Locale.ENGLISH) = "abc"
                         * </pre>
                         *
                         * @param {string} str  the String to lower case, may be null
                         * @param {string} locale  the locale that defines the case transformation rules, must not be null
                         * @return {string} the lower cased String, {@code null} if null String input
                         * @since 2.5
                         */
                        static lowerCase(str, locale) {
                            if (((typeof str === 'string') || str === null) && ((typeof locale === 'string') || locale === null)) {
                                return org.openprovenance.apache.commons.lang.StringUtils.lowerCase$java_lang_String$java_util_Locale(str, locale);
                            }
                            else if (((typeof str === 'string') || str === null) && locale === undefined) {
                                return org.openprovenance.apache.commons.lang.StringUtils.lowerCase$java_lang_String(str);
                            }
                            else
                                throw new Error('invalid overload');
                        }
                        /**
                         * <p>Capitalizes a String changing the first letter to title case as
                         * per {@link Character#toTitleCase(char)}. No other letters are changed.</p>
                         *
                         * <p>For a word based algorithm, see {@link WordUtils#capitalize(String)}.
                         * A {@code null} input String returns {@code null}.</p>
                         *
                         * <pre>
                         * StringUtils.capitalize(null)  = null
                         * StringUtils.capitalize("")    = ""
                         * StringUtils.capitalize("cat") = "Cat"
                         * StringUtils.capitalize("cAt") = "CAt"
                         * </pre>
                         *
                         * @param {string} str  the String to capitalize, may be null
                         * @return {string} the capitalized String, {@code null} if null String input
                         * @see WordUtils#capitalize(String)
                         * @see #uncapitalize(String)
                         * @since 2.0
                         */
                        static capitalize(str) {
                            let strLen;
                            if (str == null || (strLen = str.length) === 0) {
                                return str;
                            }
                            return new org.openprovenance.apache.commons.lang.text.StrBuilder(strLen).append$char(javaemul.internal.CharacterHelper.toTitleCase(str.charAt(0))).append$java_lang_String(str.substring(1)).toString();
                        }
                        /**
                         * <p>Capitalizes a String changing the first letter to title case as
                         * per {@link Character#toTitleCase(char)}. No other letters are changed.</p>
                         *
                         * @param {string} str  the String to capitalize, may be null
                         * @return {string} the capitalized String, {@code null} if null String input
                         * @deprecated Use the standardly named {@link #capitalize(String)}.
                         * Method will be removed in Commons Lang 3.0.
                         */
                        static capitalise(str) {
                            return StringUtils.capitalize(str);
                        }
                        /**
                         * <p>Uncapitalizes a String changing the first letter to title case as
                         * per {@link Character#toLowerCase(char)}. No other letters are changed.</p>
                         *
                         * <p>For a word based algorithm, see {@link WordUtils#uncapitalize(String)}.
                         * A {@code null} input String returns {@code null}.</p>
                         *
                         * <pre>
                         * StringUtils.uncapitalize(null)  = null
                         * StringUtils.uncapitalize("")    = ""
                         * StringUtils.uncapitalize("Cat") = "cat"
                         * StringUtils.uncapitalize("CAT") = "cAT"
                         * </pre>
                         *
                         * @param {string} str  the String to uncapitalize, may be null
                         * @return {string} the uncapitalized String, {@code null} if null String input
                         * @see WordUtils#uncapitalize(String)
                         * @see #capitalize(String)
                         * @since 2.0
                         */
                        static uncapitalize(str) {
                            let strLen;
                            if (str == null || (strLen = str.length) === 0) {
                                return str;
                            }
                            return new org.openprovenance.apache.commons.lang.text.StrBuilder(strLen).append$char(/* toLowerCase */ str.charAt(0).toLowerCase()).append$java_lang_String(str.substring(1)).toString();
                        }
                        /**
                         * <p>Uncapitalizes a String changing the first letter to title case as
                         * per {@link Character#toLowerCase(char)}. No other letters are changed.</p>
                         *
                         * @param {string} str  the String to uncapitalize, may be null
                         * @return {string} the uncapitalized String, {@code null} if null String input
                         * @deprecated Use the standardly named {@link #uncapitalize(String)}.
                         * Method will be removed in Commons Lang 3.0.
                         */
                        static uncapitalise(str) {
                            return StringUtils.uncapitalize(str);
                        }
                        /**
                         * <p>Swaps the case of a String changing upper and title case to
                         * lower case, and lower case to upper case.</p>
                         *
                         * <ul>
                         * <li>Upper case character converts to Lower case</li>
                         * <li>Title case character converts to Lower case</li>
                         * <li>Lower case character converts to Upper case</li>
                         * </ul>
                         *
                         * <p>For a word based algorithm, see {@link WordUtils#swapCase(String)}.
                         * A {@code null} input String returns {@code null}.</p>
                         *
                         * <pre>
                         * StringUtils.swapCase(null)                 = null
                         * StringUtils.swapCase("")                   = ""
                         * StringUtils.swapCase("The dog has a BONE") = "tHE DOG HAS A bone"
                         * </pre>
                         *
                         * <p>NOTE: This method changed in Lang version 2.0.
                         * It no longer performs a word based algorithm.
                         * If you only use ASCII, you will notice no change.
                         * That functionality is available in WordUtils.</p>
                         *
                         * @param {string} str  the String to swap case, may be null
                         * @return {string} the changed String, {@code null} if null String input
                         */
                        static swapCase(str) {
                            let strLen;
                            if (str == null || (strLen = str.length) === 0) {
                                return str;
                            }
                            const buffer = new org.openprovenance.apache.commons.lang.text.StrBuilder(strLen);
                            let ch = String.fromCharCode(0);
                            for (let i = 0; i < strLen; i++) {
                                {
                                    ch = str.charAt(i);
                                    if ( /* isUpperCase */(s => s.toUpperCase() === s)(ch)) {
                                        ch = /* toLowerCase */ ch.toLowerCase();
                                    }
                                    else if (javaemul.internal.CharacterHelper.isTitleCase(ch)) {
                                        ch = /* toLowerCase */ ch.toLowerCase();
                                    }
                                    else if ( /* isLowerCase */(s => s.toLowerCase() === s)(ch)) {
                                        ch = /* toUpperCase */ ch.toUpperCase();
                                    }
                                    buffer.append$char(ch);
                                }
                                ;
                            }
                            return buffer.toString();
                        }
                        /**
                         * <p>Capitalizes all the whitespace separated words in a String.
                         * Only the first letter of each word is changed.</p>
                         *
                         * <p>Whitespace is defined by {@link Character#isWhitespace(char)}.
                         * A {@code null} input String returns {@code null}.</p>
                         *
                         * @param {string} str  the String to capitalize, may be null
                         * @return {string} capitalized String, {@code null} if null String input
                         * @deprecated Use the relocated {@link WordUtils#capitalize(String)}.
                         * Method will be removed in Commons Lang 3.0.
                         */
                        static capitaliseAllWords(str) {
                            return org.openprovenance.apache.commons.lang.WordUtils.capitalize$java_lang_String(str);
                        }
                        /**
                         * <p>Counts how many times the substring appears in the larger String.</p>
                         *
                         * <p>A {@code null} or empty ("") String input returns {@code 0}.</p>
                         *
                         * <pre>
                         * StringUtils.countMatches(null, *)       = 0
                         * StringUtils.countMatches("", *)         = 0
                         * StringUtils.countMatches("abba", null)  = 0
                         * StringUtils.countMatches("abba", "")    = 0
                         * StringUtils.countMatches("abba", "a")   = 2
                         * StringUtils.countMatches("abba", "ab")  = 1
                         * StringUtils.countMatches("abba", "xxx") = 0
                         * </pre>
                         *
                         * @param {string} str  the String to check, may be null
                         * @param {string} sub  the substring to count, may be null
                         * @return {number} the number of occurrences, 0 if either String is {@code null}
                         */
                        static countMatches(str, sub) {
                            if (StringUtils.isEmpty(str) || StringUtils.isEmpty(sub)) {
                                return 0;
                            }
                            let count = 0;
                            let idx = 0;
                            while (((idx = str.indexOf(sub, idx)) !== StringUtils.INDEX_NOT_FOUND)) {
                                {
                                    count++;
                                    idx += sub.length;
                                }
                            }
                            ;
                            return count;
                        }
                        /**
                         * <p>Checks if the String contains only unicode letters.</p>
                         *
                         * <p>{@code null} will return {@code false}.
                         * An empty String (length()=0) will return {@code true}.</p>
                         *
                         * <pre>
                         * StringUtils.isAlpha(null)   = false
                         * StringUtils.isAlpha("")     = true
                         * StringUtils.isAlpha("  ")   = false
                         * StringUtils.isAlpha("abc")  = true
                         * StringUtils.isAlpha("ab2c") = false
                         * StringUtils.isAlpha("ab-c") = false
                         * </pre>
                         *
                         * @param {string} str  the String to check, may be null
                         * @return {boolean} {@code true} if only contains letters, and is non-null
                         */
                        static isAlpha(str) {
                            if (str == null) {
                                return false;
                            }
                            const sz = str.length;
                            for (let i = 0; i < sz; i++) {
                                {
                                    if ( /* isLetter *//[a-zA-Z]/.test(str.charAt(i)[0]) === false) {
                                        return false;
                                    }
                                }
                                ;
                            }
                            return true;
                        }
                        /**
                         * <p>Checks if the String contains only unicode letters and
                         * space (' ').</p>
                         *
                         * <p>{@code null} will return {@code false}
                         * An empty String (length()=0) will return {@code true}.</p>
                         *
                         * <pre>
                         * StringUtils.isAlphaSpace(null)   = false
                         * StringUtils.isAlphaSpace("")     = true
                         * StringUtils.isAlphaSpace("  ")   = true
                         * StringUtils.isAlphaSpace("abc")  = true
                         * StringUtils.isAlphaSpace("ab c") = true
                         * StringUtils.isAlphaSpace("ab2c") = false
                         * StringUtils.isAlphaSpace("ab-c") = false
                         * </pre>
                         *
                         * @param {string} str  the String to check, may be null
                         * @return {boolean} {@code true} if only contains letters and space,
                         * and is non-null
                         */
                        static isAlphaSpace(str) {
                            if (str == null) {
                                return false;
                            }
                            const sz = str.length;
                            for (let i = 0; i < sz; i++) {
                                {
                                    if (( /* isLetter *//[a-zA-Z]/.test(str.charAt(i)[0]) === false) && ((c => c.charCodeAt == null ? c : c.charCodeAt(0))(str.charAt(i)) != ' '.charCodeAt(0))) {
                                        return false;
                                    }
                                }
                                ;
                            }
                            return true;
                        }
                        /**
                         * <p>Checks if the String contains only unicode letters or digits.</p>
                         *
                         * <p>{@code null} will return {@code false}.
                         * An empty String (length()=0) will return {@code true}.</p>
                         *
                         * <pre>
                         * StringUtils.isAlphanumeric(null)   = false
                         * StringUtils.isAlphanumeric("")     = true
                         * StringUtils.isAlphanumeric("  ")   = false
                         * StringUtils.isAlphanumeric("abc")  = true
                         * StringUtils.isAlphanumeric("ab c") = false
                         * StringUtils.isAlphanumeric("ab2c") = true
                         * StringUtils.isAlphanumeric("ab-c") = false
                         * </pre>
                         *
                         * @param {string} str  the String to check, may be null
                         * @return {boolean} {@code true} if only contains letters or digits,
                         * and is non-null
                         */
                        static isAlphanumeric(str) {
                            if (str == null) {
                                return false;
                            }
                            const sz = str.length;
                            for (let i = 0; i < sz; i++) {
                                {
                                    if ( /* isLetterOrDigit *//[a-zA-Z\d]/.test(str.charAt(i)[0]) === false) {
                                        return false;
                                    }
                                }
                                ;
                            }
                            return true;
                        }
                        /**
                         * <p>Checks if the String contains only unicode letters, digits
                         * or space ({@code ' '}).</p>
                         *
                         * <p>{@code null} will return {@code false}.
                         * An empty String (length()=0) will return {@code true}.</p>
                         *
                         * <pre>
                         * StringUtils.isAlphanumeric(null)   = false
                         * StringUtils.isAlphanumeric("")     = true
                         * StringUtils.isAlphanumeric("  ")   = true
                         * StringUtils.isAlphanumeric("abc")  = true
                         * StringUtils.isAlphanumeric("ab c") = true
                         * StringUtils.isAlphanumeric("ab2c") = true
                         * StringUtils.isAlphanumeric("ab-c") = false
                         * </pre>
                         *
                         * @param {string} str  the String to check, may be null
                         * @return {boolean} {@code true} if only contains letters, digits or space,
                         * and is non-null
                         */
                        static isAlphanumericSpace(str) {
                            if (str == null) {
                                return false;
                            }
                            const sz = str.length;
                            for (let i = 0; i < sz; i++) {
                                {
                                    if (( /* isLetterOrDigit *//[a-zA-Z\d]/.test(str.charAt(i)[0]) === false) && ((c => c.charCodeAt == null ? c : c.charCodeAt(0))(str.charAt(i)) != ' '.charCodeAt(0))) {
                                        return false;
                                    }
                                }
                                ;
                            }
                            return true;
                        }
                        /**
                         * <p>Checks if the string contains only ASCII printable characters.</p>
                         *
                         * <p>{@code null} will return {@code false}.
                         * An empty String (length()=0) will return {@code true}.</p>
                         *
                         * <pre>
                         * StringUtils.isAsciiPrintable(null)     = false
                         * StringUtils.isAsciiPrintable("")       = true
                         * StringUtils.isAsciiPrintable(" ")      = true
                         * StringUtils.isAsciiPrintable("Ceki")   = true
                         * StringUtils.isAsciiPrintable("ab2c")   = true
                         * StringUtils.isAsciiPrintable("!ab-c~") = true
                         * StringUtils.isAsciiPrintable(" ") = true
                         * StringUtils.isAsciiPrintable("!") = true
                         * StringUtils.isAsciiPrintable("~") = true
                         * StringUtils.isAsciiPrintable("") = false
                         * StringUtils.isAsciiPrintable("Ceki Gülcü") = false
                         * </pre>
                         *
                         * @param {string} str the string to check, may be null
                         * @return {boolean} {@code true} if every character is in the range
                         * 32 thru 126
                         * @since 2.1
                         */
                        static isAsciiPrintable(str) {
                            if (str == null) {
                                return false;
                            }
                            const sz = str.length;
                            for (let i = 0; i < sz; i++) {
                                {
                                    if (org.openprovenance.apache.commons.lang.CharUtils.isAsciiPrintable(str.charAt(i)) === false) {
                                        return false;
                                    }
                                }
                                ;
                            }
                            return true;
                        }
                        /**
                         * <p>Checks if the String contains only unicode digits.
                         * A decimal point is not a unicode digit and returns false.</p>
                         *
                         * <p>{@code null} will return {@code false}.
                         * An empty String (length()=0) will return {@code true}.</p>
                         *
                         * <pre>
                         * StringUtils.isNumeric(null)   = false
                         * StringUtils.isNumeric("")     = true
                         * StringUtils.isNumeric("  ")   = false
                         * StringUtils.isNumeric("123")  = true
                         * StringUtils.isNumeric("12 3") = false
                         * StringUtils.isNumeric("ab2c") = false
                         * StringUtils.isNumeric("12-3") = false
                         * StringUtils.isNumeric("12.3") = false
                         * </pre>
                         *
                         * @param {string} str  the String to check, may be null
                         * @return {boolean} {@code true} if only contains digits, and is non-null
                         */
                        static isNumeric(str) {
                            if (str == null) {
                                return false;
                            }
                            const sz = str.length;
                            for (let i = 0; i < sz; i++) {
                                {
                                    if ( /* isDigit *//\d/.test(str.charAt(i)[0]) === false) {
                                        return false;
                                    }
                                }
                                ;
                            }
                            return true;
                        }
                        /**
                         * <p>Checks if the String contains only unicode digits or space
                         * ({@code ' '}).
                         * A decimal point is not a unicode digit and returns false.</p>
                         *
                         * <p>{@code null} will return {@code false}.
                         * An empty String (length()=0) will return {@code true}.</p>
                         *
                         * <pre>
                         * StringUtils.isNumeric(null)   = false
                         * StringUtils.isNumeric("")     = true
                         * StringUtils.isNumeric("  ")   = true
                         * StringUtils.isNumeric("123")  = true
                         * StringUtils.isNumeric("12 3") = true
                         * StringUtils.isNumeric("ab2c") = false
                         * StringUtils.isNumeric("12-3") = false
                         * StringUtils.isNumeric("12.3") = false
                         * </pre>
                         *
                         * @param {string} str  the String to check, may be null
                         * @return {boolean} {@code true} if only contains digits or space,
                         * and is non-null
                         */
                        static isNumericSpace(str) {
                            if (str == null) {
                                return false;
                            }
                            const sz = str.length;
                            for (let i = 0; i < sz; i++) {
                                {
                                    if (( /* isDigit *//\d/.test(str.charAt(i)[0]) === false) && ((c => c.charCodeAt == null ? c : c.charCodeAt(0))(str.charAt(i)) != ' '.charCodeAt(0))) {
                                        return false;
                                    }
                                }
                                ;
                            }
                            return true;
                        }
                        /**
                         * <p>Checks if the String contains only whitespace.</p>
                         *
                         * <p>{@code null} will return {@code false}.
                         * An empty String (length()=0) will return {@code true}.</p>
                         *
                         * <pre>
                         * StringUtils.isWhitespace(null)   = false
                         * StringUtils.isWhitespace("")     = true
                         * StringUtils.isWhitespace("  ")   = true
                         * StringUtils.isWhitespace("abc")  = false
                         * StringUtils.isWhitespace("ab2c") = false
                         * StringUtils.isWhitespace("ab-c") = false
                         * </pre>
                         *
                         * @param {string} str  the String to check, may be null
                         * @return {boolean} {@code true} if only contains whitespace, and is non-null
                         * @since 2.0
                         */
                        static isWhitespace(str) {
                            if (str == null) {
                                return false;
                            }
                            const sz = str.length;
                            for (let i = 0; i < sz; i++) {
                                {
                                    if ((javaemul.internal.CharacterHelper.isWhitespace(str.charAt(i)) === false)) {
                                        return false;
                                    }
                                }
                                ;
                            }
                            return true;
                        }
                        /**
                         * <p>Checks if the String contains only lowercase characters.</p>
                         *
                         * <p>{@code null} will return {@code false}.
                         * An empty String (length()=0) will return {@code false}.</p>
                         *
                         * <pre>
                         * StringUtils.isAllLowerCase(null)   = false
                         * StringUtils.isAllLowerCase("")     = false
                         * StringUtils.isAllLowerCase("  ")   = false
                         * StringUtils.isAllLowerCase("abc")  = true
                         * StringUtils.isAllLowerCase("abC") = false
                         * </pre>
                         *
                         * @param {string} str  the String to check, may be null
                         * @return {boolean} {@code true} if only contains lowercase characters, and is non-null
                         * @since 2.5
                         */
                        static isAllLowerCase(str) {
                            if (str == null || StringUtils.isEmpty(str)) {
                                return false;
                            }
                            const sz = str.length;
                            for (let i = 0; i < sz; i++) {
                                {
                                    if ( /* isLowerCase */(s => s.toLowerCase() === s)(str.charAt(i)) === false) {
                                        return false;
                                    }
                                }
                                ;
                            }
                            return true;
                        }
                        /**
                         * <p>Checks if the String contains only uppercase characters.</p>
                         *
                         * <p>{@code null} will return {@code false}.
                         * An empty String (length()=0) will return {@code false}.</p>
                         *
                         * <pre>
                         * StringUtils.isAllUpperCase(null)   = false
                         * StringUtils.isAllUpperCase("")     = false
                         * StringUtils.isAllUpperCase("  ")   = false
                         * StringUtils.isAllUpperCase("ABC")  = true
                         * StringUtils.isAllUpperCase("aBC") = false
                         * </pre>
                         *
                         * @param {string} str  the String to check, may be null
                         * @return {boolean} {@code true} if only contains uppercase characters, and is non-null
                         * @since 2.5
                         */
                        static isAllUpperCase(str) {
                            if (str == null || StringUtils.isEmpty(str)) {
                                return false;
                            }
                            const sz = str.length;
                            for (let i = 0; i < sz; i++) {
                                {
                                    if ( /* isUpperCase */(s => s.toUpperCase() === s)(str.charAt(i)) === false) {
                                        return false;
                                    }
                                }
                                ;
                            }
                            return true;
                        }
                        static defaultString$java_lang_String(str) {
                            return str == null ? StringUtils.EMPTY : str;
                        }
                        static defaultString$java_lang_String$java_lang_String(str, defaultStr) {
                            return str == null ? defaultStr : str;
                        }
                        /**
                         * <p>Returns either the passed in String, or if the String is
                         * {@code null}, the value of {@code defaultStr}.</p>
                         *
                         * <pre>
                         * StringUtils.defaultString(null, "NULL")  = "NULL"
                         * StringUtils.defaultString("", "NULL")    = ""
                         * StringUtils.defaultString("bat", "NULL") = "bat"
                         * </pre>
                         *
                         * @see ObjectUtils#toString(Object,String)
                         * @see String#valueOf(Object)
                         * @param {string} str  the String to check, may be null
                         * @param {string} defaultStr  the default String to return
                         * if the input is {@code null}, may be null
                         * @return {string} the passed in String, or the default if it was {@code null}
                         */
                        static defaultString(str, defaultStr) {
                            if (((typeof str === 'string') || str === null) && ((typeof defaultStr === 'string') || defaultStr === null)) {
                                return org.openprovenance.apache.commons.lang.StringUtils.defaultString$java_lang_String$java_lang_String(str, defaultStr);
                            }
                            else if (((typeof str === 'string') || str === null) && defaultStr === undefined) {
                                return org.openprovenance.apache.commons.lang.StringUtils.defaultString$java_lang_String(str);
                            }
                            else
                                throw new Error('invalid overload');
                        }
                        /**
                         * <p>Returns either the passed in String, or if the String is
                         * whitespace, empty ("") or {@code null}, the value of {@code defaultStr}.</p>
                         *
                         * <pre>
                         * StringUtils.defaultIfBlank(null, "NULL")  = "NULL"
                         * StringUtils.defaultIfBlank("", "NULL")    = "NULL"
                         * StringUtils.defaultIfBlank(" ", "NULL")   = "NULL"
                         * StringUtils.defaultIfBlank("bat", "NULL") = "bat"
                         * StringUtils.defaultIfBlank("", null)      = null
                         * </pre>
                         * @param {string} str the String to check, may be null
                         * @param {string} defaultStr  the default String to return
                         * if the input is whitespace, empty ("") or {@code null}, may be null
                         * @return {string} the passed in String, or the default
                         * @see StringUtils#defaultString(String, String)
                         * @since 2.6
                         */
                        static defaultIfBlank(str, defaultStr) {
                            return StringUtils.isBlank(str) ? defaultStr : str;
                        }
                        /**
                         * <p>Returns either the passed in String, or if the String is
                         * empty or {@code null}, the value of {@code defaultStr}.</p>
                         *
                         * <pre>
                         * StringUtils.defaultIfEmpty(null, "NULL")  = "NULL"
                         * StringUtils.defaultIfEmpty("", "NULL")    = "NULL"
                         * StringUtils.defaultIfEmpty("bat", "NULL") = "bat"
                         * StringUtils.defaultIfEmpty("", null)      = null
                         * </pre>
                         *
                         * @param {string} str  the String to check, may be null
                         * @param {string} defaultStr  the default String to return
                         * if the input is empty ("") or {@code null}, may be null
                         * @return {string} the passed in String, or the default
                         * @see StringUtils#defaultString(String, String)
                         */
                        static defaultIfEmpty(str, defaultStr) {
                            return StringUtils.isEmpty(str) ? defaultStr : str;
                        }
                        /**
                         * <p>Reverses a String as per {@link StrBuilder#reverse()}.</p>
                         *
                         * <p>A {@code null} String returns {@code null}.</p>
                         *
                         * <pre>
                         * StringUtils.reverse(null)  = null
                         * StringUtils.reverse("")    = ""
                         * StringUtils.reverse("bat") = "tab"
                         * </pre>
                         *
                         * @param {string} str  the String to reverse, may be null
                         * @return {string} the reversed String, {@code null} if null String input
                         */
                        static reverse(str) {
                            if (str == null) {
                                return null;
                            }
                            return new org.openprovenance.apache.commons.lang.text.StrBuilder(str).reverse().toString();
                        }
                        /**
                         * <p>Reverses a String that is delimited by a specific character.</p>
                         *
                         * <p>The Strings between the delimiters are not reversed.
                         * Thus java.lang.String becomes String.lang.java (if the delimiter
                         * is {@code '.'}).</p>
                         *
                         * <pre>
                         * StringUtils.reverseDelimited(null, *)      = null
                         * StringUtils.reverseDelimited("", *)        = ""
                         * StringUtils.reverseDelimited("a.b.c", 'x') = "a.b.c"
                         * StringUtils.reverseDelimited("a.b.c", ".") = "c.b.a"
                         * </pre>
                         *
                         * @param {string} str  the String to reverse, may be null
                         * @param {string} separatorChar  the separator character to use
                         * @return {string} the reversed String, {@code null} if null String input
                         * @since 2.0
                         */
                        static reverseDelimited(str, separatorChar) {
                            if (str == null) {
                                return null;
                            }
                            const strs = StringUtils.split$java_lang_String$char(str, separatorChar);
                            org.openprovenance.apache.commons.lang.ArrayUtils.reverse$java_lang_Object_A(strs);
                            return StringUtils.join$java_lang_Object_A$char(strs, separatorChar);
                        }
                        /**
                         * <p>Reverses a String that is delimited by a specific character.</p>
                         *
                         * <p>The Strings between the delimiters are not reversed.
                         * Thus java.lang.String becomes String.lang.java (if the delimiter
                         * is {@code "."}).</p>
                         *
                         * <pre>
                         * StringUtils.reverseDelimitedString(null, *)       = null
                         * StringUtils.reverseDelimitedString("",*)          = ""
                         * StringUtils.reverseDelimitedString("a.b.c", null) = "a.b.c"
                         * StringUtils.reverseDelimitedString("a.b.c", ".")  = "c.b.a"
                         * </pre>
                         *
                         * @param {string} str  the String to reverse, may be null
                         * @param {string} separatorChars  the separator characters to use, null treated as whitespace
                         * @return {string} the reversed String, {@code null} if null String input
                         * @deprecated Use {@link #reverseDelimited(String, char)} instead.
                         * This method is broken as the join doesn't know which char to use.
                         * Method will be removed in Commons Lang 3.0.
                         */
                        static reverseDelimitedString(str, separatorChars) {
                            if (str == null) {
                                return null;
                            }
                            const strs = StringUtils.split$java_lang_String$java_lang_String(str, separatorChars);
                            org.openprovenance.apache.commons.lang.ArrayUtils.reverse$java_lang_Object_A(strs);
                            if (separatorChars == null) {
                                return StringUtils.join$java_lang_Object_A$char(strs, ' ');
                            }
                            return StringUtils.join$java_lang_Object_A$java_lang_String(strs, separatorChars);
                        }
                        static abbreviate$java_lang_String$int(str, maxWidth) {
                            return StringUtils.abbreviate$java_lang_String$int$int(str, 0, maxWidth);
                        }
                        static abbreviate$java_lang_String$int$int(str, offset, maxWidth) {
                            if (str == null) {
                                return null;
                            }
                            if (maxWidth < 4) {
                                throw Object.defineProperty(new Error("Minimum abbreviation width is 4"), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.IllegalArgumentException', 'java.lang.Exception'] });
                            }
                            if (str.length <= maxWidth) {
                                return str;
                            }
                            if (offset > str.length) {
                                offset = str.length;
                            }
                            if ((str.length - offset) < (maxWidth - 3)) {
                                offset = str.length - (maxWidth - 3);
                            }
                            if (offset <= 4) {
                                return str.substring(0, maxWidth - 3) + "...";
                            }
                            if (maxWidth < 7) {
                                throw Object.defineProperty(new Error("Minimum abbreviation width with offset is 7"), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.IllegalArgumentException', 'java.lang.Exception'] });
                            }
                            if ((offset + (maxWidth - 3)) < str.length) {
                                return "..." + StringUtils.abbreviate$java_lang_String$int(str.substring(offset), maxWidth - 3);
                            }
                            return "..." + str.substring(str.length - (maxWidth - 3));
                        }
                        /**
                         * <p>Abbreviates a String using ellipses. This will turn
                         * "Now is the time for all good men" into "...is the time for..."</p>
                         *
                         * <p>Works like {@code abbreviate(String, int)}, but allows you to specify
                         * a "left edge" offset.  Note that this left edge is not necessarily going to
                         * be the leftmost character in the result, or the first character following the
                         * ellipses, but it will appear somewhere in the result.
                         *
                         * <p>In no case will it return a String of length greater than
                         * {@code maxWidth}.</p>
                         *
                         * <pre>
                         * StringUtils.abbreviate(null, *, *)                = null
                         * StringUtils.abbreviate("", 0, 4)                  = ""
                         * StringUtils.abbreviate("abcdefghijklmno", -1, 10) = "abcdefg..."
                         * StringUtils.abbreviate("abcdefghijklmno", 0, 10)  = "abcdefg..."
                         * StringUtils.abbreviate("abcdefghijklmno", 1, 10)  = "abcdefg..."
                         * StringUtils.abbreviate("abcdefghijklmno", 4, 10)  = "abcdefg..."
                         * StringUtils.abbreviate("abcdefghijklmno", 5, 10)  = "...fghi..."
                         * StringUtils.abbreviate("abcdefghijklmno", 6, 10)  = "...ghij..."
                         * StringUtils.abbreviate("abcdefghijklmno", 8, 10)  = "...ijklmno"
                         * StringUtils.abbreviate("abcdefghijklmno", 10, 10) = "...ijklmno"
                         * StringUtils.abbreviate("abcdefghijklmno", 12, 10) = "...ijklmno"
                         * StringUtils.abbreviate("abcdefghij", 0, 3)        = IllegalArgumentException
                         * StringUtils.abbreviate("abcdefghij", 5, 6)        = IllegalArgumentException
                         * </pre>
                         *
                         * @param {string} str  the String to check, may be null
                         * @param {number} offset  left edge of source String
                         * @param {number} maxWidth  maximum length of result String, must be at least 4
                         * @return {string} abbreviated String, {@code null} if null String input
                         * @throws IllegalArgumentException if the width is too small
                         * @since 2.0
                         */
                        static abbreviate(str, offset, maxWidth) {
                            if (((typeof str === 'string') || str === null) && ((typeof offset === 'number') || offset === null) && ((typeof maxWidth === 'number') || maxWidth === null)) {
                                return org.openprovenance.apache.commons.lang.StringUtils.abbreviate$java_lang_String$int$int(str, offset, maxWidth);
                            }
                            else if (((typeof str === 'string') || str === null) && ((typeof offset === 'number') || offset === null) && maxWidth === undefined) {
                                return org.openprovenance.apache.commons.lang.StringUtils.abbreviate$java_lang_String$int(str, offset);
                            }
                            else
                                throw new Error('invalid overload');
                        }
                        /**
                         * <p>Abbreviates a String to the length passed, replacing the middle characters with the supplied
                         * replacement String.</p>
                         *
                         * <p>This abbreviation only occurs if the following criteria is met:
                         * <ul>
                         * <li>Neither the String for abbreviation nor the replacement String are null or empty </li>
                         * <li>The length to truncate to is less than the length of the supplied String</li>
                         * <li>The length to truncate to is greater than 0</li>
                         * <li>The abbreviated String will have enough room for the length supplied replacement String
                         * and the first and last characters of the supplied String for abbreviation</li>
                         * </ul>
                         * Otherwise, the returned String will be the same as the supplied String for abbreviation.
                         *
                         * <pre>
                         * StringUtils.abbreviateMiddle(null, null, 0)      = null
                         * StringUtils.abbreviateMiddle("abc", null, 0)      = "abc"
                         * StringUtils.abbreviateMiddle("abc", ".", 0)      = "abc"
                         * StringUtils.abbreviateMiddle("abc", ".", 3)      = "abc"
                         * StringUtils.abbreviateMiddle("abcdef", ".", 4)     = "ab.f"
                         * </pre>
                         *
                         * @param {string} str  the String to abbreviate, may be null
                         * @param {string} middle the String to replace the middle characters with, may be null
                         * @param {number} length the length to abbreviate {@code str} to.
                         * @return {string} the abbreviated String if the above criteria is met, or the original String supplied for abbreviation.
                         * @since 2.5
                         */
                        static abbreviateMiddle(str, middle, length) {
                            if (StringUtils.isEmpty(str) || StringUtils.isEmpty(middle)) {
                                return str;
                            }
                            if (length >= str.length || length < (middle.length + 2)) {
                                return str;
                            }
                            const targetSting = length - middle.length;
                            const startOffset = (targetSting / 2 | 0) + targetSting % 2;
                            const endOffset = str.length - (targetSting / 2 | 0);
                            const builder = new org.openprovenance.apache.commons.lang.text.StrBuilder(length);
                            builder.append$java_lang_String(str.substring(0, startOffset));
                            builder.append$java_lang_String(middle);
                            builder.append$java_lang_String(str.substring(endOffset));
                            return builder.toString();
                        }
                        /**
                         * <p>Compares two Strings, and returns the portion where they differ.
                         * (More precisely, return the remainder of the second String,
                         * starting from where it's different from the first.)</p>
                         *
                         * <p>For example,
                         * {@code difference("i am a machine", "i am a robot") -> "robot"}.</p>
                         *
                         * <pre>
                         * StringUtils.difference(null, null) = null
                         * StringUtils.difference("", "") = ""
                         * StringUtils.difference("", "abc") = "abc"
                         * StringUtils.difference("abc", "") = ""
                         * StringUtils.difference("abc", "abc") = ""
                         * StringUtils.difference("ab", "abxyz") = "xyz"
                         * StringUtils.difference("abcde", "abxyz") = "xyz"
                         * StringUtils.difference("abcde", "xyz") = "xyz"
                         * </pre>
                         *
                         * @param {string} str1  the first String, may be null
                         * @param {string} str2  the second String, may be null
                         * @return {string} the portion of str2 where it differs from str1; returns the
                         * empty String if they are equal
                         * @since 2.0
                         */
                        static difference(str1, str2) {
                            if (str1 == null) {
                                return str2;
                            }
                            if (str2 == null) {
                                return str1;
                            }
                            const at = StringUtils.indexOfDifference$java_lang_String$java_lang_String(str1, str2);
                            if (at === StringUtils.INDEX_NOT_FOUND) {
                                return StringUtils.EMPTY;
                            }
                            return str2.substring(at);
                        }
                        static indexOfDifference$java_lang_String$java_lang_String(str1, str2) {
                            if (str1 === str2) {
                                return StringUtils.INDEX_NOT_FOUND;
                            }
                            if (str1 == null || str2 == null) {
                                return 0;
                            }
                            let i;
                            for (i = 0; i < str1.length && i < str2.length; ++i) {
                                {
                                    if ((c => c.charCodeAt == null ? c : c.charCodeAt(0))(str1.charAt(i)) != (c => c.charCodeAt == null ? c : c.charCodeAt(0))(str2.charAt(i))) {
                                        break;
                                    }
                                }
                                ;
                            }
                            if (i < str2.length || i < str1.length) {
                                return i;
                            }
                            return StringUtils.INDEX_NOT_FOUND;
                        }
                        /**
                         * <p>Compares two Strings, and returns the index at which the
                         * Strings begin to differ.</p>
                         *
                         * <p>For example,
                         * {@code indexOfDifference("i am a machine", "i am a robot") -> 7}</p>
                         *
                         * <pre>
                         * StringUtils.indexOfDifference(null, null) = -1
                         * StringUtils.indexOfDifference("", "") = -1
                         * StringUtils.indexOfDifference("", "abc") = 0
                         * StringUtils.indexOfDifference("abc", "") = 0
                         * StringUtils.indexOfDifference("abc", "abc") = -1
                         * StringUtils.indexOfDifference("ab", "abxyz") = 2
                         * StringUtils.indexOfDifference("abcde", "abxyz") = 2
                         * StringUtils.indexOfDifference("abcde", "xyz") = 0
                         * </pre>
                         *
                         * @param {string} str1  the first String, may be null
                         * @param {string} str2  the second String, may be null
                         * @return {number} the index where str2 and str1 begin to differ; -1 if they are equal
                         * @since 2.0
                         */
                        static indexOfDifference(str1, str2) {
                            if (((typeof str1 === 'string') || str1 === null) && ((typeof str2 === 'string') || str2 === null)) {
                                return org.openprovenance.apache.commons.lang.StringUtils.indexOfDifference$java_lang_String$java_lang_String(str1, str2);
                            }
                            else if (((str1 != null && str1 instanceof Array && (str1.length == 0 || str1[0] == null || (typeof str1[0] === 'string'))) || str1 === null) && str2 === undefined) {
                                return org.openprovenance.apache.commons.lang.StringUtils.indexOfDifference$java_lang_String_A(str1);
                            }
                            else
                                throw new Error('invalid overload');
                        }
                        static indexOfDifference$java_lang_String_A(strs) {
                            if (strs == null || strs.length <= 1) {
                                return StringUtils.INDEX_NOT_FOUND;
                            }
                            let anyStringNull = false;
                            let allStringsNull = true;
                            const arrayLen = strs.length;
                            let shortestStrLen = 2147483647;
                            let longestStrLen = 0;
                            for (let i = 0; i < arrayLen; i++) {
                                {
                                    if (strs[i] == null) {
                                        anyStringNull = true;
                                        shortestStrLen = 0;
                                    }
                                    else {
                                        allStringsNull = false;
                                        shortestStrLen = Math.min(strs[i].length, shortestStrLen);
                                        longestStrLen = Math.max(strs[i].length, longestStrLen);
                                    }
                                }
                                ;
                            }
                            if (allStringsNull || (longestStrLen === 0 && !anyStringNull)) {
                                return StringUtils.INDEX_NOT_FOUND;
                            }
                            if (shortestStrLen === 0) {
                                return 0;
                            }
                            let firstDiff = -1;
                            for (let stringPos = 0; stringPos < shortestStrLen; stringPos++) {
                                {
                                    const comparisonChar = strs[0].charAt(stringPos);
                                    for (let arrayPos = 1; arrayPos < arrayLen; arrayPos++) {
                                        {
                                            if ((c => c.charCodeAt == null ? c : c.charCodeAt(0))(strs[arrayPos].charAt(stringPos)) != (c => c.charCodeAt == null ? c : c.charCodeAt(0))(comparisonChar)) {
                                                firstDiff = stringPos;
                                                break;
                                            }
                                        }
                                        ;
                                    }
                                    if (firstDiff !== -1) {
                                        break;
                                    }
                                }
                                ;
                            }
                            if (firstDiff === -1 && shortestStrLen !== longestStrLen) {
                                return shortestStrLen;
                            }
                            return firstDiff;
                        }
                        /**
                         * <p>Compares all Strings in an array and returns the initial sequence of
                         * characters that is common to all of them.</p>
                         *
                         * <p>For example,
                         * {@code getCommonPrefix(new String[] {"i am a machine", "i am a robot"}) -> "i am a "}</p>
                         *
                         * <pre>
                         * StringUtils.getCommonPrefix(null) = ""
                         * StringUtils.getCommonPrefix(new String[] {}) = ""
                         * StringUtils.getCommonPrefix(new String[] {"abc"}) = "abc"
                         * StringUtils.getCommonPrefix(new String[] {null, null}) = ""
                         * StringUtils.getCommonPrefix(new String[] {"", ""}) = ""
                         * StringUtils.getCommonPrefix(new String[] {"", null}) = ""
                         * StringUtils.getCommonPrefix(new String[] {"abc", null, null}) = ""
                         * StringUtils.getCommonPrefix(new String[] {null, null, "abc"}) = ""
                         * StringUtils.getCommonPrefix(new String[] {"", "abc"}) = ""
                         * StringUtils.getCommonPrefix(new String[] {"abc", ""}) = ""
                         * StringUtils.getCommonPrefix(new String[] {"abc", "abc"}) = "abc"
                         * StringUtils.getCommonPrefix(new String[] {"abc", "a"}) = "a"
                         * StringUtils.getCommonPrefix(new String[] {"ab", "abxyz"}) = "ab"
                         * StringUtils.getCommonPrefix(new String[] {"abcde", "abxyz"}) = "ab"
                         * StringUtils.getCommonPrefix(new String[] {"abcde", "xyz"}) = ""
                         * StringUtils.getCommonPrefix(new String[] {"xyz", "abcde"}) = ""
                         * StringUtils.getCommonPrefix(new String[] {"i am a machine", "i am a robot"}) = "i am a "
                         * </pre>
                         *
                         * @param {java.lang.String[]} strs  array of String objects, entries may be null
                         * @return {string} the initial sequence of characters that are common to all Strings
                         * in the array; empty String if the array is null, the elements are all null
                         * or if there is no common prefix.
                         * @since 2.4
                         */
                        static getCommonPrefix(strs) {
                            if (strs == null || strs.length === 0) {
                                return StringUtils.EMPTY;
                            }
                            const smallestIndexOfDiff = StringUtils.indexOfDifference$java_lang_String_A(strs);
                            if (smallestIndexOfDiff === StringUtils.INDEX_NOT_FOUND) {
                                if (strs[0] == null) {
                                    return StringUtils.EMPTY;
                                }
                                return strs[0];
                            }
                            else if (smallestIndexOfDiff === 0) {
                                return StringUtils.EMPTY;
                            }
                            else {
                                return strs[0].substring(0, smallestIndexOfDiff);
                            }
                        }
                        /**
                         * <p>Find the Levenshtein distance between two Strings.</p>
                         *
                         * <p>This is the number of changes needed to change one String into
                         * another, where each change is a single character modification (deletion,
                         * insertion or substitution).</p>
                         *
                         * <p>The previous implementation of the Levenshtein distance algorithm
                         * was from <a href="http://www.merriampark.com/ld.htm">http://www.merriampark.com/ld.htm</a></p>
                         *
                         * <p>Chas Emerick has written an implementation in Java, which avoids an OutOfMemoryError
                         * which can occur when my Java implementation is used with very large strings.<br>
                         * This implementation of the Levenshtein distance algorithm
                         * is from <a href="http://www.merriampark.com/ldjava.htm">http://www.merriampark.com/ldjava.htm</a></p>
                         *
                         * <pre>
                         * StringUtils.getLevenshteinDistance(null, *)             = IllegalArgumentException
                         * StringUtils.getLevenshteinDistance(*, null)             = IllegalArgumentException
                         * StringUtils.getLevenshteinDistance("","")               = 0
                         * StringUtils.getLevenshteinDistance("","a")              = 1
                         * StringUtils.getLevenshteinDistance("aaapppp", "")       = 7
                         * StringUtils.getLevenshteinDistance("frog", "fog")       = 1
                         * StringUtils.getLevenshteinDistance("fly", "ant")        = 3
                         * StringUtils.getLevenshteinDistance("elephant", "hippo") = 7
                         * StringUtils.getLevenshteinDistance("hippo", "elephant") = 7
                         * StringUtils.getLevenshteinDistance("hippo", "zzzzzzzz") = 8
                         * StringUtils.getLevenshteinDistance("hello", "hallo")    = 1
                         * </pre>
                         *
                         * @param {string} s  the first String, must not be null
                         * @param {string} t  the second String, must not be null
                         * @return {number} result distance
                         * @throws IllegalArgumentException if either String input {@code null}
                         */
                        static getLevenshteinDistance(s, t) {
                            if (s == null || t == null) {
                                throw Object.defineProperty(new Error("Strings must not be null"), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.IllegalArgumentException', 'java.lang.Exception'] });
                            }
                            let n = s.length;
                            let m = t.length;
                            if (n === 0) {
                                return m;
                            }
                            else if (m === 0) {
                                return n;
                            }
                            if (n > m) {
                                const tmp = s;
                                s = t;
                                t = tmp;
                                n = m;
                                m = t.length;
                            }
                            let p = (s => { let a = []; while (s-- > 0)
                                a.push(0); return a; })(n + 1);
                            let d = (s => { let a = []; while (s-- > 0)
                                a.push(0); return a; })(n + 1);
                            let _d;
                            let i;
                            let j;
                            let t_j;
                            let cost;
                            for (i = 0; i <= n; i++) {
                                {
                                    p[i] = i;
                                }
                                ;
                            }
                            for (j = 1; j <= m; j++) {
                                {
                                    t_j = t.charAt(j - 1);
                                    d[0] = j;
                                    for (i = 1; i <= n; i++) {
                                        {
                                            cost = (c => c.charCodeAt == null ? c : c.charCodeAt(0))(s.charAt(i - 1)) == (c => c.charCodeAt == null ? c : c.charCodeAt(0))(t_j) ? 0 : 1;
                                            d[i] = Math.min(Math.min(d[i - 1] + 1, p[i] + 1), p[i - 1] + cost);
                                        }
                                        ;
                                    }
                                    _d = p;
                                    p = d;
                                    d = _d;
                                }
                                ;
                            }
                            return p[n];
                        }
                        static startsWith$java_lang_String$java_lang_String(str, prefix) {
                            return StringUtils.startsWith$java_lang_String$java_lang_String$boolean(str, prefix, false);
                        }
                        /**
                         * <p>Case insensitive check if a String starts with a specified prefix.</p>
                         *
                         * <p>{@code null}s are handled without exceptions. Two {@code null}
                         * references are considered to be equal. The comparison is case insensitive.</p>
                         *
                         * <pre>
                         * StringUtils.startsWithIgnoreCase(null, null)      = true
                         * StringUtils.startsWithIgnoreCase(null, "abc")     = false
                         * StringUtils.startsWithIgnoreCase("abcdef", null)  = false
                         * StringUtils.startsWithIgnoreCase("abcdef", "abc") = true
                         * StringUtils.startsWithIgnoreCase("ABCDEF", "abc") = true
                         * </pre>
                         *
                         * @see String#startsWith(String)
                         * @param {string} str  the String to check, may be null
                         * @param {string} prefix the prefix to find, may be null
                         * @return {boolean} {@code true} if the String starts with the prefix, case insensitive, or
                         * both {@code null}
                         * @since 2.4
                         */
                        static startsWithIgnoreCase(str, prefix) {
                            return StringUtils.startsWith$java_lang_String$java_lang_String$boolean(str, prefix, true);
                        }
                        static startsWith$java_lang_String$java_lang_String$boolean(str, prefix, ignoreCase) {
                            if (str == null || prefix == null) {
                                return (str == null && prefix == null);
                            }
                            if (prefix.length > str.length) {
                                return false;
                            }
                            return str.regionMatches(ignoreCase, 0, prefix, 0, prefix.length);
                        }
                        /**
                         * <p>Check if a String starts with a specified prefix (optionally case insensitive).</p>
                         *
                         * @see String#startsWith(String)
                         * @param {string} str  the String to check, may be null
                         * @param {string} prefix the prefix to find, may be null
                         * @param {boolean} ignoreCase inidicates whether the compare should ignore case
                         * (case insensitive) or not.
                         * @return {boolean} {@code true} if the String starts with the prefix or
                         * both {@code null}
                         * @private
                         */
                        static startsWith(str, prefix, ignoreCase) {
                            if (((typeof str === 'string') || str === null) && ((typeof prefix === 'string') || prefix === null) && ((typeof ignoreCase === 'boolean') || ignoreCase === null)) {
                                return org.openprovenance.apache.commons.lang.StringUtils.startsWith$java_lang_String$java_lang_String$boolean(str, prefix, ignoreCase);
                            }
                            else if (((typeof str === 'string') || str === null) && ((typeof prefix === 'string') || prefix === null) && ignoreCase === undefined) {
                                return org.openprovenance.apache.commons.lang.StringUtils.startsWith$java_lang_String$java_lang_String(str, prefix);
                            }
                            else
                                throw new Error('invalid overload');
                        }
                        /**
                         * <p>Check if a String starts with any of an array of specified strings.</p>
                         *
                         * <pre>
                         * StringUtils.startsWithAny(null, null)      = false
                         * StringUtils.startsWithAny(null, new String[] {"abc"})  = false
                         * StringUtils.startsWithAny("abcxyz", null)     = false
                         * StringUtils.startsWithAny("abcxyz", new String[] {""}) = false
                         * StringUtils.startsWithAny("abcxyz", new String[] {"abc"}) = true
                         * StringUtils.startsWithAny("abcxyz", new String[] {null, "xyz", "abc"}) = true
                         * </pre>
                         *
                         * @see #startsWith(String, String)
                         * @param {string} string  the String to check, may be null
                         * @param {java.lang.String[]} searchStrings the Strings to find, may be null or empty
                         * @return {boolean} {@code true} if the String starts with any of the the prefixes, case insensitive, or
                         * both {@code null}
                         * @since 2.5
                         */
                        static startsWithAny(string, searchStrings) {
                            if (StringUtils.isEmpty(string) || org.openprovenance.apache.commons.lang.ArrayUtils.isEmpty$java_lang_Object_A(searchStrings)) {
                                return false;
                            }
                            for (let i = 0; i < searchStrings.length; i++) {
                                {
                                    const searchString = searchStrings[i];
                                    if (StringUtils.startsWith$java_lang_String$java_lang_String(string, searchString)) {
                                        return true;
                                    }
                                }
                                ;
                            }
                            return false;
                        }
                        static endsWith$java_lang_String$java_lang_String(str, suffix) {
                            return StringUtils.endsWith$java_lang_String$java_lang_String$boolean(str, suffix, false);
                        }
                        /**
                         * <p>Case insensitive check if a String ends with a specified suffix.</p>
                         *
                         * <p>{@code null}s are handled without exceptions. Two {@code null}
                         * references are considered to be equal. The comparison is case insensitive.</p>
                         *
                         * <pre>
                         * StringUtils.endsWithIgnoreCase(null, null)      = true
                         * StringUtils.endsWithIgnoreCase(null, "def")     = false
                         * StringUtils.endsWithIgnoreCase("abcdef", null)  = false
                         * StringUtils.endsWithIgnoreCase("abcdef", "def") = true
                         * StringUtils.endsWithIgnoreCase("ABCDEF", "def") = true
                         * StringUtils.endsWithIgnoreCase("ABCDEF", "cde") = false
                         * </pre>
                         *
                         * @see String#endsWith(String)
                         * @param {string} str  the String to check, may be null
                         * @param {string} suffix the suffix to find, may be null
                         * @return {boolean} {@code true} if the String ends with the suffix, case insensitive, or
                         * both {@code null}
                         * @since 2.4
                         */
                        static endsWithIgnoreCase(str, suffix) {
                            return StringUtils.endsWith$java_lang_String$java_lang_String$boolean(str, suffix, true);
                        }
                        static endsWith$java_lang_String$java_lang_String$boolean(str, suffix, ignoreCase) {
                            if (str == null || suffix == null) {
                                return (str == null && suffix == null);
                            }
                            if (suffix.length > str.length) {
                                return false;
                            }
                            const strOffset = str.length - suffix.length;
                            return str.regionMatches(ignoreCase, strOffset, suffix, 0, suffix.length);
                        }
                        /**
                         * <p>Check if a String ends with a specified suffix (optionally case insensitive).</p>
                         *
                         * @see String#endsWith(String)
                         * @param {string} str  the String to check, may be null
                         * @param {string} suffix the suffix to find, may be null
                         * @param {boolean} ignoreCase inidicates whether the compare should ignore case
                         * (case insensitive) or not.
                         * @return {boolean} {@code true} if the String starts with the prefix or
                         * both {@code null}
                         * @private
                         */
                        static endsWith(str, suffix, ignoreCase) {
                            if (((typeof str === 'string') || str === null) && ((typeof suffix === 'string') || suffix === null) && ((typeof ignoreCase === 'boolean') || ignoreCase === null)) {
                                return org.openprovenance.apache.commons.lang.StringUtils.endsWith$java_lang_String$java_lang_String$boolean(str, suffix, ignoreCase);
                            }
                            else if (((typeof str === 'string') || str === null) && ((typeof suffix === 'string') || suffix === null) && ignoreCase === undefined) {
                                return org.openprovenance.apache.commons.lang.StringUtils.endsWith$java_lang_String$java_lang_String(str, suffix);
                            }
                            else
                                throw new Error('invalid overload');
                        }
                        /**
                         * <p>
                         * Similar to <a
                         * href="http://www.w3.org/TR/xpath/#function-normalize-space">http://www.w3.org/TR/xpath/#function-normalize
                         * -space</a>
                         * </p>
                         * <p>
                         * The function returns the argument string with whitespace normalized by using
                         * {@code {@link #trim(String)}} to remove leading and trailing whitespace
                         * and then replacing sequences of whitespace characters by a single space.
                         * </p>
                         * In XML Whitespace characters are the same as those allowed by the <a
                         * href="http://www.w3.org/TR/REC-xml/#NT-S">S</a> production, which is S ::= (#x20 | #x9 | #xD | #xA)+
                         * <p>
                         * See Java's {@link Character#isWhitespace(char)} for which characters are considered whitespace.
                         * <p>
                         * The difference is that Java's whitespace includes vertical tab and form feed, which this functional will also
                         * normalize. Additonally {@code {@link #trim(String)}} removes control characters (char &lt;= 32) from both
                         * ends of this String.
                         * </p>
                         *
                         * @see Character#isWhitespace(char)
                         * @see #trim(String)
                         * @see <a href="http://www.w3.org/TR/xpath/#function-normalize-space">
                         * http://www.w3.org/TR/xpath/#function-normalize-space</a>
                         * @param {string} str the source String to normalize whitespaces from, may be null
                         * @return {string} the modified string with whitespace normalized, {@code null} if null String input
                         *
                         * @since 2.6
                         */
                        static normalizeSpace(str) {
                            str = StringUtils.strip$java_lang_String(str);
                            if (str == null || str.length <= 2) {
                                return str;
                            }
                            const b = new org.openprovenance.apache.commons.lang.text.StrBuilder(str.length);
                            for (let i = 0; i < str.length; i++) {
                                {
                                    const c = str.charAt(i);
                                    if (javaemul.internal.CharacterHelper.isWhitespace(c)) {
                                        if (i > 0 && !javaemul.internal.CharacterHelper.isWhitespace(str.charAt(i - 1))) {
                                            b.append$char(' ');
                                        }
                                    }
                                    else {
                                        b.append$char(c);
                                    }
                                }
                                ;
                            }
                            return b.toString();
                        }
                        /**
                         * <p>Check if a String ends with any of an array of specified strings.</p>
                         *
                         * <pre>
                         * StringUtils.endsWithAny(null, null)      = false
                         * StringUtils.endsWithAny(null, new String[] {"abc"})  = false
                         * StringUtils.endsWithAny("abcxyz", null)     = false
                         * StringUtils.endsWithAny("abcxyz", new String[] {""}) = true
                         * StringUtils.endsWithAny("abcxyz", new String[] {"xyz"}) = true
                         * StringUtils.endsWithAny("abcxyz", new String[] {null, "xyz", "abc"}) = true
                         * </pre>
                         *
                         * @param {string} string  the String to check, may be null
                         * @param {java.lang.String[]} searchStrings the Strings to find, may be null or empty
                         * @return {boolean} {@code true} if the String ends with any of the the prefixes, case insensitive, or
                         * both {@code null}
                         * @since 2.6
                         */
                        static endsWithAny(string, searchStrings) {
                            if (StringUtils.isEmpty(string) || org.openprovenance.apache.commons.lang.ArrayUtils.isEmpty$java_lang_Object_A(searchStrings)) {
                                return false;
                            }
                            for (let i = 0; i < searchStrings.length; i++) {
                                {
                                    const searchString = searchStrings[i];
                                    if (StringUtils.endsWith$java_lang_String$java_lang_String(string, searchString)) {
                                        return true;
                                    }
                                }
                                ;
                            }
                            return false;
                        }
                    }
                    /**
                     * The empty String <code>""</code>.
                     * @since 2.0
                     */
                    StringUtils.EMPTY = "";
                    /**
                     * Represents a failed index search.
                     * @since 2.1
                     */
                    StringUtils.INDEX_NOT_FOUND = -1;
                    /**
                     * <p>The maximum size to which the padding constant(s) can expand.</p>
                     */
                    StringUtils.PAD_LIMIT = 8192;
                    lang.StringUtils = StringUtils;
                    StringUtils["__class"] = "org.openprovenance.apache.commons.lang.StringUtils";
                })(lang = commons.lang || (commons.lang = {}));
            })(commons = apache.commons || (apache.commons = {}));
        })(apache = openprovenance.apache || (openprovenance.apache = {}));
    })(openprovenance = org.openprovenance || (org.openprovenance = {}));
})(org || (org = {}));
