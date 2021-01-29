/* Generated from Java with JSweet 3.0.0 - http://www.jsweet.org */
namespace org.openprovenance.apache.commons.lang {
    /**
     * <p>CharSetUtils instances should NOT be constructed in standard programming.
     * Instead, the class should be used as <code>CharSetUtils.evaluateSet(null);</code>.</p>
     * 
     * <p>This constructor is public to permit tools that require a JavaBean instance
     * to operate.</p>
     * @class
     * @author Apache Software Foundation
     */
    export class CharSetUtils {
        public constructor() {
        }

        /**
         * <p>Creates a <code>CharSet</code> instance which allows a certain amount of
         * set logic to be performed.</p>
         * <p>The syntax is:</p>
         * <ul>
         * <li>&quot;aeio&quot; which implies 'a','e1',..</li>
         * <li>&quot;^e1&quot; implies not e1.</li>
         * <li>&quot;ej-m&quot; implies e1,j-&gt;m. e1,j,k,l,m.</li>
         * </ul>
         * 
         * <pre>
         * CharSetUtils.evaluateSet(null)    = null
         * CharSetUtils.evaluateSet([])      = CharSet matching nothing
         * CharSetUtils.evaluateSet(["a-e1"]) = CharSet matching a,b,c,d,e1
         * </pre>
         * 
         * @param {java.lang.String[]} set  the set, may be null
         * @return {org.openprovenance.apache.commons.lang.CharSet} a CharSet instance, <code>null</code> if null input
         * @deprecated Use {@link CharSet#getInstance(String[])}.
         * Method will be removed in Commons Lang 3.0.
         */
        public static evaluateSet(set: string[]): org.openprovenance.apache.commons.lang.CharSet {
            if (set == null){
                return null;
            }
            return new org.openprovenance.apache.commons.lang.CharSet(set);
        }

        public static squeeze$java_lang_String$java_lang_String(str: string, set: string): string {
            if (org.openprovenance.apache.commons.lang.StringUtils.isEmpty(str) || org.openprovenance.apache.commons.lang.StringUtils.isEmpty(set)){
                return str;
            }
            const strs: string[] = [null];
            strs[0] = set;
            return CharSetUtils.squeeze$java_lang_String$java_lang_String_A(str, strs);
        }

        /**
         * <p>Squeezes any repetitions of a character that is mentioned in the
         * supplied set.</p>
         * 
         * <pre>
         * CharSetUtils.squeeze(null, *)        = null
         * CharSetUtils.squeeze("", *)          = ""
         * CharSetUtils.squeeze(*, null)        = *
         * CharSetUtils.squeeze(*, "")          = *
         * CharSetUtils.squeeze("hello", "k-p") = "helo"
         * CharSetUtils.squeeze("hello", "a-e1") = "hello"
         * </pre>
         * 
         * @see CharSet#getInstance(String) for set-syntax.
         * @param {string} str  the string to squeeze, may be null
         * @param {string} set  the character set to use for manipulation, may be null
         * @return {string} modified String, <code>null</code> if null string input
         */
        public static squeeze(str?: any, set?: any): any {
            if (((typeof str === 'string') || str === null) && ((typeof set === 'string') || set === null)) {
                return <any>org.openprovenance.apache.commons.lang.CharSetUtils.squeeze$java_lang_String$java_lang_String(str, set);
            } else if (((typeof str === 'string') || str === null) && ((set != null && set instanceof <any>Array && (set.length == 0 || set[0] == null ||(typeof set[0] === 'string'))) || set === null)) {
                return <any>org.openprovenance.apache.commons.lang.CharSetUtils.squeeze$java_lang_String$java_lang_String_A(str, set);
            } else throw new Error('invalid overload');
        }

        public static squeeze$java_lang_String$java_lang_String_A(str: string, set: string[]): string {
            if (org.openprovenance.apache.commons.lang.StringUtils.isEmpty(str) || org.openprovenance.apache.commons.lang.ArrayUtils.isEmpty$java_lang_Object_A(set)){
                return str;
            }
            const chars: org.openprovenance.apache.commons.lang.CharSet = org.openprovenance.apache.commons.lang.CharSet.getInstance$java_lang_String_A(set);
            const buffer: org.openprovenance.apache.commons.lang.text.StrBuilder = new org.openprovenance.apache.commons.lang.text.StrBuilder(str.length);
            const chrs: string[] = /* toCharArray */(str).split('');
            const sz: number = chrs.length;
            let lastChar: string = ' ';
            let ch: string = ' ';
            for(let i: number = 0; i < sz; i++) {{
                ch = chrs[i];
                if (chars.contains(ch)){
                    if (((c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(ch) == (c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(lastChar)) && (i !== 0)){
                        continue;
                    }
                }
                buffer.append$char(ch);
                lastChar = ch;
            };}
            return buffer.toString();
        }

        public static count$java_lang_String$java_lang_String(str: string, set: string): number {
            if (org.openprovenance.apache.commons.lang.StringUtils.isEmpty(str) || org.openprovenance.apache.commons.lang.StringUtils.isEmpty(set)){
                return 0;
            }
            const strs: string[] = [null];
            strs[0] = set;
            return CharSetUtils.count$java_lang_String$java_lang_String_A(str, strs);
        }

        /**
         * <p>Takes an argument in set-syntax, see evaluateSet,
         * and returns the number of characters present in the specified string.</p>
         * 
         * <pre>
         * CharSetUtils.count(null, *)        = 0
         * CharSetUtils.count("", *)          = 0
         * CharSetUtils.count(*, null)        = 0
         * CharSetUtils.count(*, "")          = 0
         * CharSetUtils.count("hello", "k-p") = 3
         * CharSetUtils.count("hello", "a-e1") = 1
         * </pre>
         * 
         * @see CharSet#getInstance(String) for set-syntax.
         * @param {string} str  String to count characters in, may be null
         * @param {string} set  String set of characters to count, may be null
         * @return {number} character count, zero if null string input
         */
        public static count(str?: any, set?: any): any {
            if (((typeof str === 'string') || str === null) && ((typeof set === 'string') || set === null)) {
                return <any>org.openprovenance.apache.commons.lang.CharSetUtils.count$java_lang_String$java_lang_String(str, set);
            } else if (((typeof str === 'string') || str === null) && ((set != null && set instanceof <any>Array && (set.length == 0 || set[0] == null ||(typeof set[0] === 'string'))) || set === null)) {
                return <any>org.openprovenance.apache.commons.lang.CharSetUtils.count$java_lang_String$java_lang_String_A(str, set);
            } else throw new Error('invalid overload');
        }

        public static count$java_lang_String$java_lang_String_A(str: string, set: string[]): number {
            if (org.openprovenance.apache.commons.lang.StringUtils.isEmpty(str) || org.openprovenance.apache.commons.lang.ArrayUtils.isEmpty$java_lang_Object_A(set)){
                return 0;
            }
            const chars: org.openprovenance.apache.commons.lang.CharSet = org.openprovenance.apache.commons.lang.CharSet.getInstance$java_lang_String_A(set);
            let count: number = 0;
            const chrs: string[] = /* toCharArray */(str).split('');
            const sz: number = chrs.length;
            for(let i: number = 0; i < sz; i++) {{
                if (chars.contains(chrs[i])){
                    count++;
                }
            };}
            return count;
        }

        public static keep$java_lang_String$java_lang_String(str: string, set: string): string {
            if (str == null){
                return null;
            }
            if (str.length === 0 || org.openprovenance.apache.commons.lang.StringUtils.isEmpty(set)){
                return "";
            }
            const strs: string[] = [null];
            strs[0] = set;
            return CharSetUtils.keep$java_lang_String$java_lang_String_A(str, strs);
        }

        /**
         * <p>Takes an argument in set-syntax, see evaluateSet,
         * and keeps any of characters present in the specified string.</p>
         * 
         * <pre>
         * CharSetUtils.keep(null, *)        = null
         * CharSetUtils.keep("", *)          = ""
         * CharSetUtils.keep(*, null)        = ""
         * CharSetUtils.keep(*, "")          = ""
         * CharSetUtils.keep("hello", "hl")  = "hll"
         * CharSetUtils.keep("hello", "le")  = "ell"
         * </pre>
         * 
         * @see CharSet#getInstance(String) for set-syntax.
         * @param {string} str  String to keep characters from, may be null
         * @param {string} set  String set of characters to keep, may be null
         * @return {string} modified String, <code>null</code> if null string input
         * @since 2.0
         */
        public static keep(str?: any, set?: any): any {
            if (((typeof str === 'string') || str === null) && ((typeof set === 'string') || set === null)) {
                return <any>org.openprovenance.apache.commons.lang.CharSetUtils.keep$java_lang_String$java_lang_String(str, set);
            } else if (((typeof str === 'string') || str === null) && ((set != null && set instanceof <any>Array && (set.length == 0 || set[0] == null ||(typeof set[0] === 'string'))) || set === null)) {
                return <any>org.openprovenance.apache.commons.lang.CharSetUtils.keep$java_lang_String$java_lang_String_A(str, set);
            } else throw new Error('invalid overload');
        }

        public static keep$java_lang_String$java_lang_String_A(str: string, set: string[]): string {
            if (str == null){
                return null;
            }
            if (str.length === 0 || org.openprovenance.apache.commons.lang.ArrayUtils.isEmpty$java_lang_Object_A(set)){
                return "";
            }
            return CharSetUtils.modify(str, set, true);
        }

        public static delete$java_lang_String$java_lang_String(str: string, set: string): string {
            if (org.openprovenance.apache.commons.lang.StringUtils.isEmpty(str) || org.openprovenance.apache.commons.lang.StringUtils.isEmpty(set)){
                return str;
            }
            const strs: string[] = [null];
            strs[0] = set;
            return CharSetUtils.delete$java_lang_String$java_lang_String_A(str, strs);
        }

        /**
         * <p>Takes an argument in set-syntax, see evaluateSet,
         * and deletes any of characters present in the specified string.</p>
         * 
         * <pre>
         * CharSetUtils.delete(null, *)        = null
         * CharSetUtils.delete("", *)          = ""
         * CharSetUtils.delete(*, null)        = *
         * CharSetUtils.delete(*, "")          = *
         * CharSetUtils.delete("hello", "hl")  = "eo"
         * CharSetUtils.delete("hello", "le")  = "ho"
         * </pre>
         * 
         * @see CharSet#getInstance(String) for set-syntax.
         * @param {string} str  String to delete characters from, may be null
         * @param {string} set  String set of characters to delete, may be null
         * @return {string} modified String, <code>null</code> if null string input
         */
        public static delete(str?: any, set?: any): any {
            if (((typeof str === 'string') || str === null) && ((typeof set === 'string') || set === null)) {
                return <any>org.openprovenance.apache.commons.lang.CharSetUtils.delete$java_lang_String$java_lang_String(str, set);
            } else if (((typeof str === 'string') || str === null) && ((set != null && set instanceof <any>Array && (set.length == 0 || set[0] == null ||(typeof set[0] === 'string'))) || set === null)) {
                return <any>org.openprovenance.apache.commons.lang.CharSetUtils.delete$java_lang_String$java_lang_String_A(str, set);
            } else throw new Error('invalid overload');
        }

        public static delete$java_lang_String$java_lang_String_A(str: string, set: string[]): string {
            if (org.openprovenance.apache.commons.lang.StringUtils.isEmpty(str) || org.openprovenance.apache.commons.lang.ArrayUtils.isEmpty$java_lang_Object_A(set)){
                return str;
            }
            return CharSetUtils.modify(str, set, false);
        }

        /**
         * Implementation of delete and keep
         * 
         * @param {string} str String to modify characters within
         * @param {java.lang.String[]} set String[] set of characters to modify
         * @param {boolean} expect whether to evaluate on match, or non-match
         * @return {string} modified String
         * @private
         */
        /*private*/ static modify(str: string, set: string[], expect: boolean): string {
            const chars: org.openprovenance.apache.commons.lang.CharSet = org.openprovenance.apache.commons.lang.CharSet.getInstance$java_lang_String_A(set);
            const buffer: org.openprovenance.apache.commons.lang.text.StrBuilder = new org.openprovenance.apache.commons.lang.text.StrBuilder(str.length);
            const chrs: string[] = /* toCharArray */(str).split('');
            const sz: number = chrs.length;
            for(let i: number = 0; i < sz; i++) {{
                if (chars.contains(chrs[i]) === expect){
                    buffer.append$char(chrs[i]);
                }
            };}
            return buffer.toString();
        }

        /**
         * <p>Translate characters in a String.
         * This is a multi character search and replace routine.</p>
         * 
         * <p>An example is:</p>
         * <ul>
         * <li>translate(&quot;hello&quot;, &quot;ho&quot;, &quot;jy&quot;)
         * =&gt; jelly</li>
         * </ul>
         * 
         * <p>If the length of characters to search for is greater than the
         * length of characters to replace, then the last character is
         * used.</p>
         * 
         * <pre>
         * CharSetUtils.translate(null, *, *) = null
         * CharSetUtils.translate("", *, *)   = ""
         * </pre>
         * 
         * @param {string} str  String to replace characters in, may be null
         * @param {string} searchChars   a set of characters to search for, must not be null
         * @param {string} replaceChars  a set of characters to replace, must not be null or empty (&quot;&quot;)
         * @return {string} translated String, <code>null</code> if null string input
         * @throws NullPointerException if <code>searchChars</code> or <code>replaceChars</code>
         * is <code>null</code>
         * @throws ArrayIndexOutOfBoundsException if <code>replaceChars</code> is empty (&quot;&quot;)
         * @deprecated Use {@link StringUtils#replaceChars(String, String, String)}.
         * Method will be removed in Commons Lang 3.0.
         * NOTE: StringUtils#replaceChars behaves differently when 'searchChars' is longer
         * than 'replaceChars'. CharSetUtils#translate will use the last char of the replacement
         * string whereas StringUtils#replaceChars will delete
         */
        public static translate(str: string, searchChars: string, replaceChars: string): string {
            if (org.openprovenance.apache.commons.lang.StringUtils.isEmpty(str)){
                return str;
            }
            const buffer: org.openprovenance.apache.commons.lang.text.StrBuilder = new org.openprovenance.apache.commons.lang.text.StrBuilder(str.length);
            const chrs: string[] = /* toCharArray */(str).split('');
            const withChrs: string[] = /* toCharArray */(replaceChars).split('');
            const sz: number = chrs.length;
            const withMax: number = replaceChars.length - 1;
            for(let i: number = 0; i < sz; i++) {{
                let idx: number = searchChars.indexOf(chrs[i]);
                if (idx !== -1){
                    if (idx > withMax){
                        idx = withMax;
                    }
                    buffer.append$char(withChrs[idx]);
                } else {
                    buffer.append$char(chrs[i]);
                }
            };}
            return buffer.toString();
        }
    }
    CharSetUtils["__class"] = "org.openprovenance.apache.commons.lang.CharSetUtils";

}

