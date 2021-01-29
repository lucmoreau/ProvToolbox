/* Generated from Java with JSweet 3.0.0 - http://www.jsweet.org */
namespace org.openprovenance.apache.commons.lang.text {
    /**
     * A matcher class that can be queried to determine if a character array
     * portion matches.
     * <p>
     * This class comes complete with various factory methods.
     * If these do not suffice, you can subclass and implement your own matcher.
     * 
     * @author Apache Software Foundation
     * @since 2.2
     * @version $Id: StrMatcher.java 905636 2010-02-02 14:03:32Z niallp $
     * @class
     */
    export abstract class StrMatcher {
        /**
         * Matches the comma character.
         */
        static COMMA_MATCHER: StrMatcher; public static COMMA_MATCHER_$LI$(): StrMatcher { if (StrMatcher.COMMA_MATCHER == null) { StrMatcher.COMMA_MATCHER = new StrMatcher.CharMatcher(','); }  return StrMatcher.COMMA_MATCHER; }

        /**
         * Matches the tab character.
         */
        static TAB_MATCHER: StrMatcher; public static TAB_MATCHER_$LI$(): StrMatcher { if (StrMatcher.TAB_MATCHER == null) { StrMatcher.TAB_MATCHER = new StrMatcher.CharMatcher('\t'); }  return StrMatcher.TAB_MATCHER; }

        /**
         * Matches the space character.
         */
        static SPACE_MATCHER: StrMatcher; public static SPACE_MATCHER_$LI$(): StrMatcher { if (StrMatcher.SPACE_MATCHER == null) { StrMatcher.SPACE_MATCHER = new StrMatcher.CharMatcher(' '); }  return StrMatcher.SPACE_MATCHER; }

        /**
         * Matches the same characters as StringTokenizer,
         * namely space, tab, newline, formfeed.
         */
        static SPLIT_MATCHER: StrMatcher; public static SPLIT_MATCHER_$LI$(): StrMatcher { if (StrMatcher.SPLIT_MATCHER == null) { StrMatcher.SPLIT_MATCHER = new StrMatcher.CharSetMatcher(/* toCharArray */(" \t\n\r\f").split('')); }  return StrMatcher.SPLIT_MATCHER; }

        /**
         * Matches the String trim() whitespace characters.
         */
        static TRIM_MATCHER: StrMatcher; public static TRIM_MATCHER_$LI$(): StrMatcher { if (StrMatcher.TRIM_MATCHER == null) { StrMatcher.TRIM_MATCHER = new StrMatcher.TrimMatcher(); }  return StrMatcher.TRIM_MATCHER; }

        /**
         * Matches the double quote character.
         */
        static SINGLE_QUOTE_MATCHER: StrMatcher; public static SINGLE_QUOTE_MATCHER_$LI$(): StrMatcher { if (StrMatcher.SINGLE_QUOTE_MATCHER == null) { StrMatcher.SINGLE_QUOTE_MATCHER = new StrMatcher.CharMatcher('\''); }  return StrMatcher.SINGLE_QUOTE_MATCHER; }

        /**
         * Matches the double quote character.
         */
        static DOUBLE_QUOTE_MATCHER: StrMatcher; public static DOUBLE_QUOTE_MATCHER_$LI$(): StrMatcher { if (StrMatcher.DOUBLE_QUOTE_MATCHER == null) { StrMatcher.DOUBLE_QUOTE_MATCHER = new StrMatcher.CharMatcher('\"'); }  return StrMatcher.DOUBLE_QUOTE_MATCHER; }

        /**
         * Matches the single or double quote character.
         */
        static QUOTE_MATCHER: StrMatcher; public static QUOTE_MATCHER_$LI$(): StrMatcher { if (StrMatcher.QUOTE_MATCHER == null) { StrMatcher.QUOTE_MATCHER = new StrMatcher.CharSetMatcher(/* toCharArray */("\'\"").split('')); }  return StrMatcher.QUOTE_MATCHER; }

        /**
         * Matches no characters.
         */
        static NONE_MATCHER: StrMatcher; public static NONE_MATCHER_$LI$(): StrMatcher { if (StrMatcher.NONE_MATCHER == null) { StrMatcher.NONE_MATCHER = new StrMatcher.NoMatcher(); }  return StrMatcher.NONE_MATCHER; }

        /**
         * Returns a matcher which matches the comma character.
         * 
         * @return {org.openprovenance.apache.commons.lang.text.StrMatcher} a matcher for a comma
         */
        public static commaMatcher(): StrMatcher {
            return StrMatcher.COMMA_MATCHER_$LI$();
        }

        /**
         * Returns a matcher which matches the tab character.
         * 
         * @return {org.openprovenance.apache.commons.lang.text.StrMatcher} a matcher for a tab
         */
        public static tabMatcher(): StrMatcher {
            return StrMatcher.TAB_MATCHER_$LI$();
        }

        /**
         * Returns a matcher which matches the space character.
         * 
         * @return {org.openprovenance.apache.commons.lang.text.StrMatcher} a matcher for a space
         */
        public static spaceMatcher(): StrMatcher {
            return StrMatcher.SPACE_MATCHER_$LI$();
        }

        /**
         * Matches the same characters as StringTokenizer,
         * namely space, tab, newline and formfeed.
         * 
         * @return {org.openprovenance.apache.commons.lang.text.StrMatcher} the split matcher
         */
        public static splitMatcher(): StrMatcher {
            return StrMatcher.SPLIT_MATCHER_$LI$();
        }

        /**
         * Matches the String trim() whitespace characters.
         * 
         * @return {org.openprovenance.apache.commons.lang.text.StrMatcher} the trim matcher
         */
        public static trimMatcher(): StrMatcher {
            return StrMatcher.TRIM_MATCHER_$LI$();
        }

        /**
         * Returns a matcher which matches the single quote character.
         * 
         * @return {org.openprovenance.apache.commons.lang.text.StrMatcher} a matcher for a single quote
         */
        public static singleQuoteMatcher(): StrMatcher {
            return StrMatcher.SINGLE_QUOTE_MATCHER_$LI$();
        }

        /**
         * Returns a matcher which matches the double quote character.
         * 
         * @return {org.openprovenance.apache.commons.lang.text.StrMatcher} a matcher for a double quote
         */
        public static doubleQuoteMatcher(): StrMatcher {
            return StrMatcher.DOUBLE_QUOTE_MATCHER_$LI$();
        }

        /**
         * Returns a matcher which matches the single or double quote character.
         * 
         * @return {org.openprovenance.apache.commons.lang.text.StrMatcher} a matcher for a single or double quote
         */
        public static quoteMatcher(): StrMatcher {
            return StrMatcher.QUOTE_MATCHER_$LI$();
        }

        /**
         * Matches no characters.
         * 
         * @return {org.openprovenance.apache.commons.lang.text.StrMatcher} a matcher that matches nothing
         */
        public static noneMatcher(): StrMatcher {
            return StrMatcher.NONE_MATCHER_$LI$();
        }

        /**
         * Constructor that creates a matcher from a character.
         * 
         * @param {string} ch  the character to match, must not be null
         * @return {org.openprovenance.apache.commons.lang.text.StrMatcher} a new Matcher for the given char
         */
        public static charMatcher(ch: string): StrMatcher {
            return new StrMatcher.CharMatcher(ch);
        }

        public static charSetMatcher$char_A(chars: string[]): StrMatcher {
            if (chars == null || chars.length === 0){
                return StrMatcher.NONE_MATCHER_$LI$();
            }
            if (chars.length === 1){
                return new StrMatcher.CharMatcher(chars[0]);
            }
            return new StrMatcher.CharSetMatcher(chars);
        }

        /**
         * Constructor that creates a matcher from a set of characters.
         * 
         * @param {char[]} chars  the characters to match, null or empty matches nothing
         * @return {org.openprovenance.apache.commons.lang.text.StrMatcher} a new matcher for the given char[]
         */
        public static charSetMatcher(chars?: any): any {
            if (((chars != null && chars instanceof <any>Array && (chars.length == 0 || chars[0] == null ||(typeof chars[0] === 'string'))) || chars === null)) {
                return <any>org.openprovenance.apache.commons.lang.text.StrMatcher.charSetMatcher$char_A(chars);
            } else if (((typeof chars === 'string') || chars === null)) {
                return <any>org.openprovenance.apache.commons.lang.text.StrMatcher.charSetMatcher$java_lang_String(chars);
            } else throw new Error('invalid overload');
        }

        public static charSetMatcher$java_lang_String(chars: string): StrMatcher {
            if (chars == null || chars.length === 0){
                return StrMatcher.NONE_MATCHER_$LI$();
            }
            if (chars.length === 1){
                return new StrMatcher.CharMatcher(chars.charAt(0));
            }
            return new StrMatcher.CharSetMatcher(/* toCharArray */(chars).split(''));
        }

        /**
         * Constructor that creates a matcher from a string.
         * 
         * @param {string} str  the string to match, null or empty matches nothing
         * @return {org.openprovenance.apache.commons.lang.text.StrMatcher} a new Matcher for the given String
         */
        public static stringMatcher(str: string): StrMatcher {
            if (str == null || str.length === 0){
                return StrMatcher.NONE_MATCHER_$LI$();
            }
            return new StrMatcher.StringMatcher(str);
        }

        constructor() {
        }

        public isMatch$char_A$int$int$int(buffer: string[], pos: number, bufferStart: number, bufferEnd: number): number { throw new Error('cannot invoke abstract overloaded method... check your argument(s) type(s)'); }

        /**
         * Returns the number of matching characters, zero for no match.
         * <p>
         * This method is called to check for a match.
         * The parameter <code>pos</code> represents the current position to be
         * checked in the string <code>buffer</code> (a character array which must
         * not be changed).
         * The API guarantees that <code>pos</code> is a valid index for <code>buffer</code>.
         * <p>
         * The character array may be larger than the active area to be matched.
         * Only values in the buffer between the specifed indices may be accessed.
         * <p>
         * The matching code may check one character or many.
         * It may check characters preceeding <code>pos</code> as well as those
         * after, so long as no checks exceed the bounds specified.
         * <p>
         * It must return zero for no match, or a positive number if a match was found.
         * The number indicates the number of characters that matched.
         * 
         * @param {char[]} buffer  the text content to match against, do not change
         * @param {number} pos  the starting position for the match, valid for buffer
         * @param {number} bufferStart  the first active index in the buffer, valid for buffer
         * @param {number} bufferEnd  the end index (exclusive) of the active buffer, valid for buffer
         * @return {number} the number of matching characters, zero for no match
         */
        public isMatch(buffer?: any, pos?: any, bufferStart?: any, bufferEnd?: any): any {
            if (((buffer != null && buffer instanceof <any>Array && (buffer.length == 0 || buffer[0] == null ||(typeof buffer[0] === 'string'))) || buffer === null) && ((typeof pos === 'number') || pos === null) && ((typeof bufferStart === 'number') || bufferStart === null) && ((typeof bufferEnd === 'number') || bufferEnd === null)) {
                return <any>this.isMatch$char_A$int$int$int(buffer, pos, bufferStart, bufferEnd);
            } else if (((buffer != null && buffer instanceof <any>Array && (buffer.length == 0 || buffer[0] == null ||(typeof buffer[0] === 'string'))) || buffer === null) && ((typeof pos === 'number') || pos === null) && bufferStart === undefined && bufferEnd === undefined) {
                return <any>this.isMatch$char_A$int(buffer, pos);
            } else throw new Error('invalid overload');
        }

        public isMatch$char_A$int(buffer: string[], pos: number): number {
            return this.isMatch$char_A$int$int$int(buffer, pos, 0, buffer.length);
        }
    }
    StrMatcher["__class"] = "org.openprovenance.apache.commons.lang.text.StrMatcher";


    export namespace StrMatcher {

        /**
         * Class used to define a set of characters for matching purposes.
         * @extends org.openprovenance.apache.commons.lang.text.StrMatcher
         * @class
         */
        export class CharSetMatcher extends org.openprovenance.apache.commons.lang.text.StrMatcher {
            /**
             * The set of characters to match.
             */
            chars: string[];

            constructor(chars: string[]) {
                super();
                if (this.chars === undefined) { this.chars = null; }
                this.chars = <string[]>/* clone */chars.slice(0);
                /* sort */((l) => {l.sort(); })(this.chars);
            }

            public isMatch$char_A$int$int$int(buffer: string[], pos: number, bufferStart: number, bufferEnd: number): number {
                return java.util.Arrays.binarySearch(this.chars, buffer[pos]) >= 0 ? 1 : 0;
            }

            /**
             * Returns whether or not the given character matches.
             * 
             * @param {char[]} buffer  the text content to match against, do not change
             * @param {number} pos  the starting position for the match, valid for buffer
             * @param {number} bufferStart  the first active index in the buffer, valid for buffer
             * @param {number} bufferEnd  the end index of the active buffer, valid for buffer
             * @return {number} the number of matching characters, zero for no match
             */
            public isMatch(buffer?: any, pos?: any, bufferStart?: any, bufferEnd?: any): any {
                if (((buffer != null && buffer instanceof <any>Array && (buffer.length == 0 || buffer[0] == null ||(typeof buffer[0] === 'string'))) || buffer === null) && ((typeof pos === 'number') || pos === null) && ((typeof bufferStart === 'number') || bufferStart === null) && ((typeof bufferEnd === 'number') || bufferEnd === null)) {
                    return <any>this.isMatch$char_A$int$int$int(buffer, pos, bufferStart, bufferEnd);
                } else if (((buffer != null && buffer instanceof <any>Array && (buffer.length == 0 || buffer[0] == null ||(typeof buffer[0] === 'string'))) || buffer === null) && ((typeof pos === 'number') || pos === null) && bufferStart === undefined && bufferEnd === undefined) {
                    return <any>this.isMatch$char_A$int(buffer, pos);
                } else throw new Error('invalid overload');
            }
        }
        CharSetMatcher["__class"] = "org.openprovenance.apache.commons.lang.text.StrMatcher.CharSetMatcher";


        /**
         * Class used to define a character for matching purposes.
         * @extends org.openprovenance.apache.commons.lang.text.StrMatcher
         * @class
         */
        export class CharMatcher extends org.openprovenance.apache.commons.lang.text.StrMatcher {
            /**
             * The character to match.
             */
            ch: string;

            constructor(ch: string) {
                super();
                if (this.ch === undefined) { this.ch = null; }
                this.ch = ch;
            }

            public isMatch$char_A$int$int$int(buffer: string[], pos: number, bufferStart: number, bufferEnd: number): number {
                return (c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(this.ch) == (c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(buffer[pos]) ? 1 : 0;
            }

            /**
             * Returns whether or not the given character matches.
             * 
             * @param {char[]} buffer  the text content to match against, do not change
             * @param {number} pos  the starting position for the match, valid for buffer
             * @param {number} bufferStart  the first active index in the buffer, valid for buffer
             * @param {number} bufferEnd  the end index of the active buffer, valid for buffer
             * @return {number} the number of matching characters, zero for no match
             */
            public isMatch(buffer?: any, pos?: any, bufferStart?: any, bufferEnd?: any): any {
                if (((buffer != null && buffer instanceof <any>Array && (buffer.length == 0 || buffer[0] == null ||(typeof buffer[0] === 'string'))) || buffer === null) && ((typeof pos === 'number') || pos === null) && ((typeof bufferStart === 'number') || bufferStart === null) && ((typeof bufferEnd === 'number') || bufferEnd === null)) {
                    return <any>this.isMatch$char_A$int$int$int(buffer, pos, bufferStart, bufferEnd);
                } else if (((buffer != null && buffer instanceof <any>Array && (buffer.length == 0 || buffer[0] == null ||(typeof buffer[0] === 'string'))) || buffer === null) && ((typeof pos === 'number') || pos === null) && bufferStart === undefined && bufferEnd === undefined) {
                    return <any>this.isMatch$char_A$int(buffer, pos);
                } else throw new Error('invalid overload');
            }
        }
        CharMatcher["__class"] = "org.openprovenance.apache.commons.lang.text.StrMatcher.CharMatcher";


        /**
         * Class used to define a set of characters for matching purposes.
         * @extends org.openprovenance.apache.commons.lang.text.StrMatcher
         * @class
         */
        export class StringMatcher extends org.openprovenance.apache.commons.lang.text.StrMatcher {
            /**
             * The string to match, as a character array.
             */
            chars: string[];

            constructor(str: string) {
                super();
                if (this.chars === undefined) { this.chars = null; }
                this.chars = /* toCharArray */(str).split('');
            }

            public isMatch$char_A$int$int$int(buffer: string[], pos: number, bufferStart: number, bufferEnd: number): number {
                const len: number = this.chars.length;
                if (pos + len > bufferEnd){
                    return 0;
                }
                for(let i: number = 0; i < this.chars.length; i++, pos++) {{
                    if ((c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(this.chars[i]) != (c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(buffer[pos])){
                        return 0;
                    }
                };}
                return len;
            }

            /**
             * Returns whether or not the given text matches the stored string.
             * 
             * @param {char[]} buffer  the text content to match against, do not change
             * @param {number} pos  the starting position for the match, valid for buffer
             * @param {number} bufferStart  the first active index in the buffer, valid for buffer
             * @param {number} bufferEnd  the end index of the active buffer, valid for buffer
             * @return {number} the number of matching characters, zero for no match
             */
            public isMatch(buffer?: any, pos?: any, bufferStart?: any, bufferEnd?: any): any {
                if (((buffer != null && buffer instanceof <any>Array && (buffer.length == 0 || buffer[0] == null ||(typeof buffer[0] === 'string'))) || buffer === null) && ((typeof pos === 'number') || pos === null) && ((typeof bufferStart === 'number') || bufferStart === null) && ((typeof bufferEnd === 'number') || bufferEnd === null)) {
                    return <any>this.isMatch$char_A$int$int$int(buffer, pos, bufferStart, bufferEnd);
                } else if (((buffer != null && buffer instanceof <any>Array && (buffer.length == 0 || buffer[0] == null ||(typeof buffer[0] === 'string'))) || buffer === null) && ((typeof pos === 'number') || pos === null) && bufferStart === undefined && bufferEnd === undefined) {
                    return <any>this.isMatch$char_A$int(buffer, pos);
                } else throw new Error('invalid overload');
            }
        }
        StringMatcher["__class"] = "org.openprovenance.apache.commons.lang.text.StrMatcher.StringMatcher";


        /**
         * Class used to match no characters.
         * @extends org.openprovenance.apache.commons.lang.text.StrMatcher
         * @class
         */
        export class NoMatcher extends org.openprovenance.apache.commons.lang.text.StrMatcher {
            constructor() {
                super();
            }

            public isMatch$char_A$int$int$int(buffer: string[], pos: number, bufferStart: number, bufferEnd: number): number {
                return 0;
            }

            /**
             * Always returns <code>false</code>.
             * 
             * @param {char[]} buffer  the text content to match against, do not change
             * @param {number} pos  the starting position for the match, valid for buffer
             * @param {number} bufferStart  the first active index in the buffer, valid for buffer
             * @param {number} bufferEnd  the end index of the active buffer, valid for buffer
             * @return {number} the number of matching characters, zero for no match
             */
            public isMatch(buffer?: any, pos?: any, bufferStart?: any, bufferEnd?: any): any {
                if (((buffer != null && buffer instanceof <any>Array && (buffer.length == 0 || buffer[0] == null ||(typeof buffer[0] === 'string'))) || buffer === null) && ((typeof pos === 'number') || pos === null) && ((typeof bufferStart === 'number') || bufferStart === null) && ((typeof bufferEnd === 'number') || bufferEnd === null)) {
                    return <any>this.isMatch$char_A$int$int$int(buffer, pos, bufferStart, bufferEnd);
                } else if (((buffer != null && buffer instanceof <any>Array && (buffer.length == 0 || buffer[0] == null ||(typeof buffer[0] === 'string'))) || buffer === null) && ((typeof pos === 'number') || pos === null) && bufferStart === undefined && bufferEnd === undefined) {
                    return <any>this.isMatch$char_A$int(buffer, pos);
                } else throw new Error('invalid overload');
            }
        }
        NoMatcher["__class"] = "org.openprovenance.apache.commons.lang.text.StrMatcher.NoMatcher";


        /**
         * Class used to match whitespace as per trim().
         * @extends org.openprovenance.apache.commons.lang.text.StrMatcher
         * @class
         */
        export class TrimMatcher extends org.openprovenance.apache.commons.lang.text.StrMatcher {
            constructor() {
                super();
            }

            public isMatch$char_A$int$int$int(buffer: string[], pos: number, bufferStart: number, bufferEnd: number): number {
                return (c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(buffer[pos]) <= 32 ? 1 : 0;
            }

            /**
             * Returns whether or not the given character matches.
             * 
             * @param {char[]} buffer  the text content to match against, do not change
             * @param {number} pos  the starting position for the match, valid for buffer
             * @param {number} bufferStart  the first active index in the buffer, valid for buffer
             * @param {number} bufferEnd  the end index of the active buffer, valid for buffer
             * @return {number} the number of matching characters, zero for no match
             */
            public isMatch(buffer?: any, pos?: any, bufferStart?: any, bufferEnd?: any): any {
                if (((buffer != null && buffer instanceof <any>Array && (buffer.length == 0 || buffer[0] == null ||(typeof buffer[0] === 'string'))) || buffer === null) && ((typeof pos === 'number') || pos === null) && ((typeof bufferStart === 'number') || bufferStart === null) && ((typeof bufferEnd === 'number') || bufferEnd === null)) {
                    return <any>this.isMatch$char_A$int$int$int(buffer, pos, bufferStart, bufferEnd);
                } else if (((buffer != null && buffer instanceof <any>Array && (buffer.length == 0 || buffer[0] == null ||(typeof buffer[0] === 'string'))) || buffer === null) && ((typeof pos === 'number') || pos === null) && bufferStart === undefined && bufferEnd === undefined) {
                    return <any>this.isMatch$char_A$int(buffer, pos);
                } else throw new Error('invalid overload');
            }
        }
        TrimMatcher["__class"] = "org.openprovenance.apache.commons.lang.text.StrMatcher.TrimMatcher";

    }

}

