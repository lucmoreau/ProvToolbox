/* Generated from Java with JSweet 3.0.0 - http://www.jsweet.org */
namespace org.openprovenance.apache.commons.lang {
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
    export class WordUtils {
        public constructor() {
        }

        public static wrap$java_lang_String$int(str: string, wrapLength: number): string {
            return WordUtils.wrap$java_lang_String$int$java_lang_String$boolean(str, wrapLength, null, false);
        }

        public static wrap$java_lang_String$int$java_lang_String$boolean(str: string, wrapLength: number, newLineStr: string, wrapLongWords: boolean): string {
            if (str == null){
                return null;
            }
            if (newLineStr == null){
                newLineStr = org.openprovenance.apache.commons.lang.SystemUtils.LINE_SEPARATOR_$LI$();
            }
            if (wrapLength < 1){
                wrapLength = 1;
            }
            const inputLineLength: number = str.length;
            let offset: number = 0;
            const wrappedLine: { str: string, toString: Function } = { str: "", toString: function() { return this.str; } };
            while(((inputLineLength - offset) > wrapLength)) {{
                if ((c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(str.charAt(offset)) == ' '.charCodeAt(0)){
                    offset++;
                    continue;
                }
                let spaceToWrapAt: number = str.lastIndexOf(' ', wrapLength + offset);
                if (spaceToWrapAt >= offset){
                    /* append */(sb => { sb.str += <any>str.substring(offset, spaceToWrapAt); return sb; })(wrappedLine);
                    /* append */(sb => { sb.str += <any>newLineStr; return sb; })(wrappedLine);
                    offset = spaceToWrapAt + 1;
                } else {
                    if (wrapLongWords){
                        /* append */(sb => { sb.str += <any>str.substring(offset, wrapLength + offset); return sb; })(wrappedLine);
                        /* append */(sb => { sb.str += <any>newLineStr; return sb; })(wrappedLine);
                        offset += wrapLength;
                    } else {
                        spaceToWrapAt = str.indexOf(' ', wrapLength + offset);
                        if (spaceToWrapAt >= 0){
                            /* append */(sb => { sb.str += <any>str.substring(offset, spaceToWrapAt); return sb; })(wrappedLine);
                            /* append */(sb => { sb.str += <any>newLineStr; return sb; })(wrappedLine);
                            offset = spaceToWrapAt + 1;
                        } else {
                            /* append */(sb => { sb.str += <any>str.substring(offset); return sb; })(wrappedLine);
                            offset = inputLineLength;
                        }
                    }
                }
            }};
            /* append */(sb => { sb.str += <any>str.substring(offset); return sb; })(wrappedLine);
            return /* toString */wrappedLine.str;
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
        public static wrap(str?: any, wrapLength?: any, newLineStr?: any, wrapLongWords?: any): any {
            if (((typeof str === 'string') || str === null) && ((typeof wrapLength === 'number') || wrapLength === null) && ((typeof newLineStr === 'string') || newLineStr === null) && ((typeof wrapLongWords === 'boolean') || wrapLongWords === null)) {
                return <any>org.openprovenance.apache.commons.lang.WordUtils.wrap$java_lang_String$int$java_lang_String$boolean(str, wrapLength, newLineStr, wrapLongWords);
            } else if (((typeof str === 'string') || str === null) && ((typeof wrapLength === 'number') || wrapLength === null) && newLineStr === undefined && wrapLongWords === undefined) {
                return <any>org.openprovenance.apache.commons.lang.WordUtils.wrap$java_lang_String$int(str, wrapLength);
            } else throw new Error('invalid overload');
        }

        public static capitalize$java_lang_String(str: string): string {
            return WordUtils.capitalize$java_lang_String$char_A(str, null);
        }

        public static capitalize$java_lang_String$char_A(str: string, delimiters: string[]): string {
            const delimLen: number = (delimiters == null ? -1 : delimiters.length);
            if (str == null || str.length === 0 || delimLen === 0){
                return str;
            }
            const strLen: number = str.length;
            const buffer: { str: string, toString: Function } = { str: "", toString: function() { return this.str; } };
            let capitalizeNext: boolean = true;
            for(let i: number = 0; i < strLen; i++) {{
                const ch: string = str.charAt(i);
                if (WordUtils.isDelimiter(ch, delimiters)){
                    /* append */(sb => { sb.str += <any>ch; return sb; })(buffer);
                    capitalizeNext = true;
                } else if (capitalizeNext){
                    /* append */(sb => { sb.str += <any>javaemul.internal.CharacterHelper.toTitleCase(ch); return sb; })(buffer);
                    capitalizeNext = false;
                } else {
                    /* append */(sb => { sb.str += <any>ch; return sb; })(buffer);
                }
            };}
            return /* toString */buffer.str;
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
        public static capitalize(str?: any, delimiters?: any): any {
            if (((typeof str === 'string') || str === null) && ((delimiters != null && delimiters instanceof <any>Array && (delimiters.length == 0 || delimiters[0] == null ||(typeof delimiters[0] === 'string'))) || delimiters === null)) {
                return <any>org.openprovenance.apache.commons.lang.WordUtils.capitalize$java_lang_String$char_A(str, delimiters);
            } else if (((typeof str === 'string') || str === null) && delimiters === undefined) {
                return <any>org.openprovenance.apache.commons.lang.WordUtils.capitalize$java_lang_String(str);
            } else throw new Error('invalid overload');
        }

        public static capitalizeFully$java_lang_String(str: string): string {
            return WordUtils.capitalizeFully$java_lang_String$char_A(str, null);
        }

        public static capitalizeFully$java_lang_String$char_A(str: string, delimiters: string[]): string {
            const delimLen: number = (delimiters == null ? -1 : delimiters.length);
            if (str == null || str.length === 0 || delimLen === 0){
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
        public static capitalizeFully(str?: any, delimiters?: any): any {
            if (((typeof str === 'string') || str === null) && ((delimiters != null && delimiters instanceof <any>Array && (delimiters.length == 0 || delimiters[0] == null ||(typeof delimiters[0] === 'string'))) || delimiters === null)) {
                return <any>org.openprovenance.apache.commons.lang.WordUtils.capitalizeFully$java_lang_String$char_A(str, delimiters);
            } else if (((typeof str === 'string') || str === null) && delimiters === undefined) {
                return <any>org.openprovenance.apache.commons.lang.WordUtils.capitalizeFully$java_lang_String(str);
            } else throw new Error('invalid overload');
        }

        public static uncapitalize$java_lang_String(str: string): string {
            return WordUtils.uncapitalize$java_lang_String$char_A(str, null);
        }

        public static uncapitalize$java_lang_String$char_A(str: string, delimiters: string[]): string {
            const delimLen: number = (delimiters == null ? -1 : delimiters.length);
            if (str == null || str.length === 0 || delimLen === 0){
                return str;
            }
            const strLen: number = str.length;
            const buffer: { str: string, toString: Function } = { str: "", toString: function() { return this.str; } };
            let uncapitalizeNext: boolean = true;
            for(let i: number = 0; i < strLen; i++) {{
                const ch: string = str.charAt(i);
                if (WordUtils.isDelimiter(ch, delimiters)){
                    /* append */(sb => { sb.str += <any>ch; return sb; })(buffer);
                    uncapitalizeNext = true;
                } else if (uncapitalizeNext){
                    /* append */(sb => { sb.str += <any>/* toLowerCase */ch.toLowerCase(); return sb; })(buffer);
                    uncapitalizeNext = false;
                } else {
                    /* append */(sb => { sb.str += <any>ch; return sb; })(buffer);
                }
            };}
            return /* toString */buffer.str;
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
        public static uncapitalize(str?: any, delimiters?: any): any {
            if (((typeof str === 'string') || str === null) && ((delimiters != null && delimiters instanceof <any>Array && (delimiters.length == 0 || delimiters[0] == null ||(typeof delimiters[0] === 'string'))) || delimiters === null)) {
                return <any>org.openprovenance.apache.commons.lang.WordUtils.uncapitalize$java_lang_String$char_A(str, delimiters);
            } else if (((typeof str === 'string') || str === null) && delimiters === undefined) {
                return <any>org.openprovenance.apache.commons.lang.WordUtils.uncapitalize$java_lang_String(str);
            } else throw new Error('invalid overload');
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
        public static swapCase(str: string): string {
            let strLen: number;
            if (str == null || (strLen = str.length) === 0){
                return str;
            }
            const buffer: { str: string, toString: Function } = { str: "", toString: function() { return this.str; } };
            let whitespace: boolean = true;
            let ch: string = String.fromCharCode(0);
            let tmp: string = String.fromCharCode(0);
            for(let i: number = 0; i < strLen; i++) {{
                ch = str.charAt(i);
                if (/* isUpperCase */(s => s.toUpperCase() === s)(ch)){
                    tmp = /* toLowerCase */ch.toLowerCase();
                } else if (javaemul.internal.CharacterHelper.isTitleCase(ch)){
                    tmp = /* toLowerCase */ch.toLowerCase();
                } else if (/* isLowerCase */(s => s.toLowerCase() === s)(ch)){
                    if (whitespace){
                        tmp = javaemul.internal.CharacterHelper.toTitleCase(ch);
                    } else {
                        tmp = /* toUpperCase */ch.toUpperCase();
                    }
                } else {
                    tmp = ch;
                }
                /* append */(sb => { sb.str += <any>tmp; return sb; })(buffer);
                whitespace = javaemul.internal.CharacterHelper.isWhitespace(ch);
            };}
            return /* toString */buffer.str;
        }

        public static initials$java_lang_String(str: string): string {
            return WordUtils.initials$java_lang_String$char_A(str, null);
        }

        public static initials$java_lang_String$char_A(str: string, delimiters: string[]): string {
            if (str == null || str.length === 0){
                return str;
            }
            if (delimiters != null && delimiters.length === 0){
                return "";
            }
            const strLen: number = str.length;
            const buf: string[] = (s => { let a=[]; while(s-->0) a.push(null); return a; })((strLen / 2|0) + 1);
            let count: number = 0;
            let lastWasGap: boolean = true;
            for(let i: number = 0; i < strLen; i++) {{
                const ch: string = str.charAt(i);
                if (WordUtils.isDelimiter(ch, delimiters)){
                    lastWasGap = true;
                } else if (lastWasGap){
                    buf[count++] = ch;
                    lastWasGap = false;
                } else {
                }
            };}
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
        public static initials(str?: any, delimiters?: any): any {
            if (((typeof str === 'string') || str === null) && ((delimiters != null && delimiters instanceof <any>Array && (delimiters.length == 0 || delimiters[0] == null ||(typeof delimiters[0] === 'string'))) || delimiters === null)) {
                return <any>org.openprovenance.apache.commons.lang.WordUtils.initials$java_lang_String$char_A(str, delimiters);
            } else if (((typeof str === 'string') || str === null) && delimiters === undefined) {
                return <any>org.openprovenance.apache.commons.lang.WordUtils.initials$java_lang_String(str);
            } else throw new Error('invalid overload');
        }

        /**
         * Is the character a delimiter.
         * 
         * @param {string} ch  the character to check
         * @param {char[]} delimiters  the delimiters
         * @return {boolean} true if it is a delimiter
         * @private
         */
        /*private*/ static isDelimiter(ch: string, delimiters: string[]): boolean {
            if (delimiters == null){
                return javaemul.internal.CharacterHelper.isWhitespace(ch);
            }
            for(let i: number = 0, isize: number = delimiters.length; i < isize; i++) {{
                if ((c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(ch) == (c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(delimiters[i])){
                    return true;
                }
            };}
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
        public static abbreviate(str: string, lower: number, upper: number, appendToEnd: string): string {
            if (str == null){
                return null;
            }
            if (str.length === 0){
                return org.openprovenance.apache.commons.lang.StringUtils.EMPTY;
            }
            if (lower > str.length){
                lower = str.length;
            }
            if (upper === -1 || upper > str.length){
                upper = str.length;
            }
            if (upper < lower){
                upper = lower;
            }
            const result: { str: string, toString: Function } = { str: "", toString: function() { return this.str; } };
            const index: number = org.openprovenance.apache.commons.lang.StringUtils.indexOf$java_lang_String$java_lang_String$int(str, " ", lower);
            if (index === -1){
                /* append */(sb => { sb.str += <any>str.substring(0, upper); return sb; })(result);
                if (upper !== str.length){
                    /* append */(sb => { sb.str += <any>org.openprovenance.apache.commons.lang.StringUtils.defaultString$java_lang_String(appendToEnd); return sb; })(result);
                }
            } else if (index > upper){
                /* append */(sb => { sb.str += <any>str.substring(0, upper); return sb; })(result);
                /* append */(sb => { sb.str += <any>org.openprovenance.apache.commons.lang.StringUtils.defaultString$java_lang_String(appendToEnd); return sb; })(result);
            } else {
                /* append */(sb => { sb.str += <any>str.substring(0, index); return sb; })(result);
                /* append */(sb => { sb.str += <any>org.openprovenance.apache.commons.lang.StringUtils.defaultString$java_lang_String(appendToEnd); return sb; })(result);
            }
            return /* toString */result.str;
        }
    }
    WordUtils["__class"] = "org.openprovenance.apache.commons.lang.WordUtils";

}

