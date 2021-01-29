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
                     * <p><code>WordUtils</code> instances should NOT be constructed in
                     * standard programming. Instead, the class should be used as
                     * <code>WordUtils.wrap("foo bar", 20);</code>.</p>
                     *
                     * <p>This constructor is public to permit tools that require a JavaBean
                     * instance to operate.</p>
                     * @class
                     * @author Apache Jakarta Velocity
                     */
                    class WordUtils {
                        constructor() {
                        }
                        static wrap$java_lang_String$int(str, wrapLength) {
                            return WordUtils.wrap$java_lang_String$int$java_lang_String$boolean(str, wrapLength, null, false);
                        }
                        static wrap$java_lang_String$int$java_lang_String$boolean(str, wrapLength, newLineStr, wrapLongWords) {
                            if (str == null) {
                                return null;
                            }
                            if (newLineStr == null) {
                                newLineStr = org.openprovenance.apache.commons.lang.SystemUtils.LINE_SEPARATOR_$LI$();
                            }
                            if (wrapLength < 1) {
                                wrapLength = 1;
                            }
                            const inputLineLength = str.length;
                            let offset = 0;
                            const wrappedLine = { str: "", toString: function () { return this.str; } };
                            while (((inputLineLength - offset) > wrapLength)) {
                                {
                                    if ((c => c.charCodeAt == null ? c : c.charCodeAt(0))(str.charAt(offset)) == ' '.charCodeAt(0)) {
                                        offset++;
                                        continue;
                                    }
                                    let spaceToWrapAt = str.lastIndexOf(' ', wrapLength + offset);
                                    if (spaceToWrapAt >= offset) {
                                        /* append */ (sb => { sb.str += str.substring(offset, spaceToWrapAt); return sb; })(wrappedLine);
                                        /* append */ (sb => { sb.str += newLineStr; return sb; })(wrappedLine);
                                        offset = spaceToWrapAt + 1;
                                    }
                                    else {
                                        if (wrapLongWords) {
                                            /* append */ (sb => { sb.str += str.substring(offset, wrapLength + offset); return sb; })(wrappedLine);
                                            /* append */ (sb => { sb.str += newLineStr; return sb; })(wrappedLine);
                                            offset += wrapLength;
                                        }
                                        else {
                                            spaceToWrapAt = str.indexOf(' ', wrapLength + offset);
                                            if (spaceToWrapAt >= 0) {
                                                /* append */ (sb => { sb.str += str.substring(offset, spaceToWrapAt); return sb; })(wrappedLine);
                                                /* append */ (sb => { sb.str += newLineStr; return sb; })(wrappedLine);
                                                offset = spaceToWrapAt + 1;
                                            }
                                            else {
                                                /* append */ (sb => { sb.str += str.substring(offset); return sb; })(wrappedLine);
                                                offset = inputLineLength;
                                            }
                                        }
                                    }
                                }
                            }
                            ;
                            /* append */ (sb => { sb.str += str.substring(offset); return sb; })(wrappedLine);
                            return /* toString */ wrappedLine.str;
                        }
                        /**
                         * <p>Wraps a single line of text, identifying words by <code>' '</code>.</p>
                         *
                         * <p>Leading spaces on a new line are stripped.
                         * Trailing spaces are not stripped.</p>
                         *
                         * <pre>
                         * WordUtils.wrap(null, *, *, *) = null
                         * WordUtils.wrap("", *, *, *) = ""
                         * </pre>
                         *
                         * @param {string} str  the String to be word wrapped, may be null
                         * @param {number} wrapLength  the column to wrap the words at, less than 1 is treated as 1
                         * @param {string} newLineStr  the string to insert for a new line,
                         * <code>null</code> uses the system property line separator
                         * @param {boolean} wrapLongWords  true if long words (such as URLs) should be wrapped
                         * @return {string} a line with newlines inserted, <code>null</code> if null input
                         */
                        static wrap(str, wrapLength, newLineStr, wrapLongWords) {
                            if (((typeof str === 'string') || str === null) && ((typeof wrapLength === 'number') || wrapLength === null) && ((typeof newLineStr === 'string') || newLineStr === null) && ((typeof wrapLongWords === 'boolean') || wrapLongWords === null)) {
                                return org.openprovenance.apache.commons.lang.WordUtils.wrap$java_lang_String$int$java_lang_String$boolean(str, wrapLength, newLineStr, wrapLongWords);
                            }
                            else if (((typeof str === 'string') || str === null) && ((typeof wrapLength === 'number') || wrapLength === null) && newLineStr === undefined && wrapLongWords === undefined) {
                                return org.openprovenance.apache.commons.lang.WordUtils.wrap$java_lang_String$int(str, wrapLength);
                            }
                            else
                                throw new Error('invalid overload');
                        }
                        static capitalize$java_lang_String(str) {
                            return WordUtils.capitalize$java_lang_String$char_A(str, null);
                        }
                        static capitalize$java_lang_String$char_A(str, delimiters) {
                            const delimLen = (delimiters == null ? -1 : delimiters.length);
                            if (str == null || str.length === 0 || delimLen === 0) {
                                return str;
                            }
                            const strLen = str.length;
                            const buffer = { str: "", toString: function () { return this.str; } };
                            let capitalizeNext = true;
                            for (let i = 0; i < strLen; i++) {
                                {
                                    const ch = str.charAt(i);
                                    if (WordUtils.isDelimiter(ch, delimiters)) {
                                        /* append */ (sb => { sb.str += ch; return sb; })(buffer);
                                        capitalizeNext = true;
                                    }
                                    else if (capitalizeNext) {
                                        /* append */ (sb => { sb.str += javaemul.internal.CharacterHelper.toTitleCase(ch); return sb; })(buffer);
                                        capitalizeNext = false;
                                    }
                                    else {
                                        /* append */ (sb => { sb.str += ch; return sb; })(buffer);
                                    }
                                }
                                ;
                            }
                            return /* toString */ buffer.str;
                        }
                        /**
                         * <p>Capitalizes all the delimiter separated words in a String.
                         * Only the first letter of each word is changed. To convert the
                         * rest of each word to lowercase at the same time,
                         * use {@link #capitalizeFully(String, char[])}.</p>
                         *
                         * <p>The delimiters represent a set of characters understood to separate words.
                         * The first string character and the first non-delimiter character after a
                         * delimiter will be capitalized. </p>
                         *
                         * <p>A <code>null</code> input String returns <code>null</code>.
                         * Capitalization uses the unicode title case, normally equivalent to
                         * upper case.</p>
                         *
                         * <pre>
                         * WordUtils.capitalize(null, *)            = null
                         * WordUtils.capitalize("", *)              = ""
                         * WordUtils.capitalize(*, new char[0])     = *
                         * WordUtils.capitalize("i am fine", null)  = "I Am Fine"
                         * WordUtils.capitalize("i aM.fine", {'.'}) = "I aM.Fine"
                         * </pre>
                         *
                         * @param {string} str  the String to capitalize, may be null
                         * @param {char[]} delimiters  set of characters to determine capitalization, null means whitespace
                         * @return {string} capitalized String, <code>null</code> if null String input
                         * @see #uncapitalize(String)
                         * @see #capitalizeFully(String)
                         * @since 2.1
                         */
                        static capitalize(str, delimiters) {
                            if (((typeof str === 'string') || str === null) && ((delimiters != null && delimiters instanceof Array && (delimiters.length == 0 || delimiters[0] == null || (typeof delimiters[0] === 'string'))) || delimiters === null)) {
                                return org.openprovenance.apache.commons.lang.WordUtils.capitalize$java_lang_String$char_A(str, delimiters);
                            }
                            else if (((typeof str === 'string') || str === null) && delimiters === undefined) {
                                return org.openprovenance.apache.commons.lang.WordUtils.capitalize$java_lang_String(str);
                            }
                            else
                                throw new Error('invalid overload');
                        }
                        static capitalizeFully$java_lang_String(str) {
                            return WordUtils.capitalizeFully$java_lang_String$char_A(str, null);
                        }
                        static capitalizeFully$java_lang_String$char_A(str, delimiters) {
                            const delimLen = (delimiters == null ? -1 : delimiters.length);
                            if (str == null || str.length === 0 || delimLen === 0) {
                                return str;
                            }
                            str = str.toLowerCase();
                            return WordUtils.capitalize$java_lang_String$char_A(str, delimiters);
                        }
                        /**
                         * <p>Converts all the delimiter separated words in a String into capitalized words,
                         * that is each word is made up of a titlecase character and then a series of
                         * lowercase characters. </p>
                         *
                         * <p>The delimiters represent a set of characters understood to separate words.
                         * The first string character and the first non-delimiter character after a
                         * delimiter will be capitalized. </p>
                         *
                         * <p>A <code>null</code> input String returns <code>null</code>.
                         * Capitalization uses the unicode title case, normally equivalent to
                         * upper case.</p>
                         *
                         * <pre>
                         * WordUtils.capitalizeFully(null, *)            = null
                         * WordUtils.capitalizeFully("", *)              = ""
                         * WordUtils.capitalizeFully(*, null)            = *
                         * WordUtils.capitalizeFully(*, new char[0])     = *
                         * WordUtils.capitalizeFully("i aM.fine", {'.'}) = "I am.Fine"
                         * </pre>
                         *
                         * @param {string} str  the String to capitalize, may be null
                         * @param {char[]} delimiters  set of characters to determine capitalization, null means whitespace
                         * @return {string} capitalized String, <code>null</code> if null String input
                         * @since 2.1
                         */
                        static capitalizeFully(str, delimiters) {
                            if (((typeof str === 'string') || str === null) && ((delimiters != null && delimiters instanceof Array && (delimiters.length == 0 || delimiters[0] == null || (typeof delimiters[0] === 'string'))) || delimiters === null)) {
                                return org.openprovenance.apache.commons.lang.WordUtils.capitalizeFully$java_lang_String$char_A(str, delimiters);
                            }
                            else if (((typeof str === 'string') || str === null) && delimiters === undefined) {
                                return org.openprovenance.apache.commons.lang.WordUtils.capitalizeFully$java_lang_String(str);
                            }
                            else
                                throw new Error('invalid overload');
                        }
                        static uncapitalize$java_lang_String(str) {
                            return WordUtils.uncapitalize$java_lang_String$char_A(str, null);
                        }
                        static uncapitalize$java_lang_String$char_A(str, delimiters) {
                            const delimLen = (delimiters == null ? -1 : delimiters.length);
                            if (str == null || str.length === 0 || delimLen === 0) {
                                return str;
                            }
                            const strLen = str.length;
                            const buffer = { str: "", toString: function () { return this.str; } };
                            let uncapitalizeNext = true;
                            for (let i = 0; i < strLen; i++) {
                                {
                                    const ch = str.charAt(i);
                                    if (WordUtils.isDelimiter(ch, delimiters)) {
                                        /* append */ (sb => { sb.str += ch; return sb; })(buffer);
                                        uncapitalizeNext = true;
                                    }
                                    else if (uncapitalizeNext) {
                                        /* append */ (sb => { sb.str += ch.toLowerCase(); return sb; })(buffer);
                                        uncapitalizeNext = false;
                                    }
                                    else {
                                        /* append */ (sb => { sb.str += ch; return sb; })(buffer);
                                    }
                                }
                                ;
                            }
                            return /* toString */ buffer.str;
                        }
                        /**
                         * <p>Uncapitalizes all the whitespace separated words in a String.
                         * Only the first letter of each word is changed.</p>
                         *
                         * <p>The delimiters represent a set of characters understood to separate words.
                         * The first string character and the first non-delimiter character after a
                         * delimiter will be uncapitalized. </p>
                         *
                         * <p>Whitespace is defined by {@link Character#isWhitespace(char)}.
                         * A <code>null</code> input String returns <code>null</code>.</p>
                         *
                         * <pre>
                         * WordUtils.uncapitalize(null, *)            = null
                         * WordUtils.uncapitalize("", *)              = ""
                         * WordUtils.uncapitalize(*, null)            = *
                         * WordUtils.uncapitalize(*, new char[0])     = *
                         * WordUtils.uncapitalize("I AM.FINE", {'.'}) = "i AM.fINE"
                         * </pre>
                         *
                         * @param {string} str  the String to uncapitalize, may be null
                         * @param {char[]} delimiters  set of characters to determine uncapitalization, null means whitespace
                         * @return {string} uncapitalized String, <code>null</code> if null String input
                         * @see #capitalize(String)
                         * @since 2.1
                         */
                        static uncapitalize(str, delimiters) {
                            if (((typeof str === 'string') || str === null) && ((delimiters != null && delimiters instanceof Array && (delimiters.length == 0 || delimiters[0] == null || (typeof delimiters[0] === 'string'))) || delimiters === null)) {
                                return org.openprovenance.apache.commons.lang.WordUtils.uncapitalize$java_lang_String$char_A(str, delimiters);
                            }
                            else if (((typeof str === 'string') || str === null) && delimiters === undefined) {
                                return org.openprovenance.apache.commons.lang.WordUtils.uncapitalize$java_lang_String(str);
                            }
                            else
                                throw new Error('invalid overload');
                        }
                        /**
                         * <p>Swaps the case of a String using a word based algorithm.</p>
                         *
                         * <ul>
                         * <li>Upper case character converts to Lower case</li>
                         * <li>Title case character converts to Lower case</li>
                         * <li>Lower case character after Whitespace or at start converts to Title case</li>
                         * <li>Other Lower case character converts to Upper case</li>
                         * </ul>
                         *
                         * <p>Whitespace is defined by {@link Character#isWhitespace(char)}.
                         * A <code>null</code> input String returns <code>null</code>.</p>
                         *
                         * <pre>
                         * StringUtils.swapCase(null)                 = null
                         * StringUtils.swapCase("")                   = ""
                         * StringUtils.swapCase("The dog has a BONE") = "tHE DOG HAS A bone"
                         * </pre>
                         *
                         * @param {string} str  the String to swap case, may be null
                         * @return {string} the changed String, <code>null</code> if null String input
                         */
                        static swapCase(str) {
                            let strLen;
                            if (str == null || (strLen = str.length) === 0) {
                                return str;
                            }
                            const buffer = { str: "", toString: function () { return this.str; } };
                            let whitespace = true;
                            let ch = String.fromCharCode(0);
                            let tmp = String.fromCharCode(0);
                            for (let i = 0; i < strLen; i++) {
                                {
                                    ch = str.charAt(i);
                                    if ( /* isUpperCase */(s => s.toUpperCase() === s)(ch)) {
                                        tmp = /* toLowerCase */ ch.toLowerCase();
                                    }
                                    else if (javaemul.internal.CharacterHelper.isTitleCase(ch)) {
                                        tmp = /* toLowerCase */ ch.toLowerCase();
                                    }
                                    else if ( /* isLowerCase */(s => s.toLowerCase() === s)(ch)) {
                                        if (whitespace) {
                                            tmp = javaemul.internal.CharacterHelper.toTitleCase(ch);
                                        }
                                        else {
                                            tmp = /* toUpperCase */ ch.toUpperCase();
                                        }
                                    }
                                    else {
                                        tmp = ch;
                                    }
                                    /* append */ (sb => { sb.str += tmp; return sb; })(buffer);
                                    whitespace = javaemul.internal.CharacterHelper.isWhitespace(ch);
                                }
                                ;
                            }
                            return /* toString */ buffer.str;
                        }
                        static initials$java_lang_String(str) {
                            return WordUtils.initials$java_lang_String$char_A(str, null);
                        }
                        static initials$java_lang_String$char_A(str, delimiters) {
                            if (str == null || str.length === 0) {
                                return str;
                            }
                            if (delimiters != null && delimiters.length === 0) {
                                return "";
                            }
                            const strLen = str.length;
                            const buf = (s => { let a = []; while (s-- > 0)
                                a.push(null); return a; })((strLen / 2 | 0) + 1);
                            let count = 0;
                            let lastWasGap = true;
                            for (let i = 0; i < strLen; i++) {
                                {
                                    const ch = str.charAt(i);
                                    if (WordUtils.isDelimiter(ch, delimiters)) {
                                        lastWasGap = true;
                                    }
                                    else if (lastWasGap) {
                                        buf[count++] = ch;
                                        lastWasGap = false;
                                    }
                                    else {
                                    }
                                }
                                ;
                            }
                            return buf.join('').substr(0, count);
                        }
                        /**
                         * <p>Extracts the initial letters from each word in the String.</p>
                         *
                         * <p>The first letter of the string and all first letters after the
                         * defined delimiters are returned as a new string.
                         * Their case is not changed.</p>
                         *
                         * <p>If the delimiters array is null, then Whitespace is used.
                         * Whitespace is defined by {@link Character#isWhitespace(char)}.
                         * A <code>null</code> input String returns <code>null</code>.
                         * An empty delimiter array returns an empty String.</p>
                         *
                         * <pre>
                         * WordUtils.initials(null, *)                = null
                         * WordUtils.initials("", *)                  = ""
                         * WordUtils.initials("Ben John Lee", null)   = "BJL"
                         * WordUtils.initials("Ben J.Lee", null)      = "BJ"
                         * WordUtils.initials("Ben J.Lee", [' ','.']) = "BJL"
                         * WordUtils.initials(*, new char[0])         = ""
                         * </pre>
                         *
                         * @param {string} str  the String to get initials from, may be null
                         * @param {char[]} delimiters  set of characters to determine words, null means whitespace
                         * @return {string} String of initial letters, <code>null</code> if null String input
                         * @see #initials(String)
                         * @since 2.2
                         */
                        static initials(str, delimiters) {
                            if (((typeof str === 'string') || str === null) && ((delimiters != null && delimiters instanceof Array && (delimiters.length == 0 || delimiters[0] == null || (typeof delimiters[0] === 'string'))) || delimiters === null)) {
                                return org.openprovenance.apache.commons.lang.WordUtils.initials$java_lang_String$char_A(str, delimiters);
                            }
                            else if (((typeof str === 'string') || str === null) && delimiters === undefined) {
                                return org.openprovenance.apache.commons.lang.WordUtils.initials$java_lang_String(str);
                            }
                            else
                                throw new Error('invalid overload');
                        }
                        /**
                         * Is the character a delimiter.
                         *
                         * @param {string} ch  the character to check
                         * @param {char[]} delimiters  the delimiters
                         * @return {boolean} true if it is a delimiter
                         * @private
                         */
                        /*private*/ static isDelimiter(ch, delimiters) {
                            if (delimiters == null) {
                                return javaemul.internal.CharacterHelper.isWhitespace(ch);
                            }
                            for (let i = 0, isize = delimiters.length; i < isize; i++) {
                                {
                                    if ((c => c.charCodeAt == null ? c : c.charCodeAt(0))(ch) == (c => c.charCodeAt == null ? c : c.charCodeAt(0))(delimiters[i])) {
                                        return true;
                                    }
                                }
                                ;
                            }
                            return false;
                        }
                        /**
                         * Abbreviates a string nicely.
                         *
                         * This method searches for the first space after the lower limit and abbreviates
                         * the String there. It will also append any String passed as a parameter
                         * to the end of the String. The upper limit can be specified to forcibly
                         * abbreviate a String.
                         *
                         * @param {string} str         the string to be abbreviated. If null is passed, null is returned.
                         * If the empty String is passed, the empty string is returned.
                         * @param {number} lower       the lower limit.
                         * @param {number} upper       the upper limit; specify -1 if no limit is desired.
                         * If the upper limit is lower than the lower limit, it will be
                         * adjusted to be the same as the lower limit.
                         * @param {string} appendToEnd String to be appended to the end of the abbreviated string.
                         * This is appended ONLY if the string was indeed abbreviated.
                         * The append does not count towards the lower or upper limits.
                         * @return {string} the abbreviated String.
                         * @since 2.4
                         */
                        static abbreviate(str, lower, upper, appendToEnd) {
                            if (str == null) {
                                return null;
                            }
                            if (str.length === 0) {
                                return org.openprovenance.apache.commons.lang.StringUtils.EMPTY;
                            }
                            if (lower > str.length) {
                                lower = str.length;
                            }
                            if (upper === -1 || upper > str.length) {
                                upper = str.length;
                            }
                            if (upper < lower) {
                                upper = lower;
                            }
                            const result = { str: "", toString: function () { return this.str; } };
                            const index = org.openprovenance.apache.commons.lang.StringUtils.indexOf$java_lang_String$java_lang_String$int(str, " ", lower);
                            if (index === -1) {
                                /* append */ (sb => { sb.str += str.substring(0, upper); return sb; })(result);
                                if (upper !== str.length) {
                                    /* append */ (sb => { sb.str += org.openprovenance.apache.commons.lang.StringUtils.defaultString$java_lang_String(appendToEnd); return sb; })(result);
                                }
                            }
                            else if (index > upper) {
                                /* append */ (sb => { sb.str += str.substring(0, upper); return sb; })(result);
                                /* append */ (sb => { sb.str += org.openprovenance.apache.commons.lang.StringUtils.defaultString$java_lang_String(appendToEnd); return sb; })(result);
                            }
                            else {
                                /* append */ (sb => { sb.str += str.substring(0, index); return sb; })(result);
                                /* append */ (sb => { sb.str += org.openprovenance.apache.commons.lang.StringUtils.defaultString$java_lang_String(appendToEnd); return sb; })(result);
                            }
                            return /* toString */ result.str;
                        }
                    }
                    lang.WordUtils = WordUtils;
                    WordUtils["__class"] = "org.openprovenance.apache.commons.lang.WordUtils";
                })(lang = commons.lang || (commons.lang = {}));
            })(commons = apache.commons || (apache.commons = {}));
        })(apache = openprovenance.apache || (openprovenance.apache = {}));
    })(openprovenance = org.openprovenance || (org.openprovenance = {}));
})(org || (org = {}));
