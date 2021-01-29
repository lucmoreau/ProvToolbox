/* Generated from Java with JSweet 3.0.0 - http://www.jsweet.org */
namespace org.openprovenance.apache.commons.lang {
    /**
     * <p>{@code StringEscapeUtils} instances should NOT be constructed in
     * standard programming.</p>
     * 
     * <p>Instead, the class should be used as:
     * <pre>StringEscapeUtils.escapeJava("foo");</pre>
     * 
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     * @class
     * @author Apache Software Foundation
     */
    export class StringEscapeUtils {
        static CSV_DELIMITER: string = ',';

        static CSV_QUOTE: string = '\"';

        static CSV_QUOTE_STR: string; public static CSV_QUOTE_STR_$LI$(): string { if (StringEscapeUtils.CSV_QUOTE_STR == null) { StringEscapeUtils.CSV_QUOTE_STR = /* valueOf */String(StringEscapeUtils.CSV_QUOTE).toString(); }  return StringEscapeUtils.CSV_QUOTE_STR; }

        static CSV_SEARCH_CHARS: string[]; public static CSV_SEARCH_CHARS_$LI$(): string[] { if (StringEscapeUtils.CSV_SEARCH_CHARS == null) { StringEscapeUtils.CSV_SEARCH_CHARS = [StringEscapeUtils.CSV_DELIMITER, StringEscapeUtils.CSV_QUOTE, org.openprovenance.apache.commons.lang.CharUtils.CR, org.openprovenance.apache.commons.lang.CharUtils.LF]; }  return StringEscapeUtils.CSV_SEARCH_CHARS; }

        public constructor() {
        }

        public static escapeJava$java_lang_String(str: string): string {
            return StringEscapeUtils.escapeJavaStyleString$java_lang_String$boolean$boolean(str, false, false);
        }

        public static escapeJava$java_io_Writer$java_lang_String(out: java.io.Writer, str: string) {
            StringEscapeUtils.escapeJavaStyleString$java_io_Writer$java_lang_String$boolean$boolean(out, str, false, false);
        }

        /**
         * <p>Escapes the characters in a {@code String} using Java String rules to
         * a {@code Writer}.</p>
         * 
         * <p>A {@code null} string input has no effect.</p>
         * 
         * @see #escapeJava(String)
         * @param {java.io.Writer} out  Writer to write escaped string into
         * @param {string} str  String to escape values in, may be null
         * @throws IllegalArgumentException if the Writer is {@code null}
         * @throws IOException if error occurs on underlying Writer
         */
        public static escapeJava(out?: any, str?: any) {
            if (((out != null && out instanceof <any>java.io.Writer) || out === null) && ((typeof str === 'string') || str === null)) {
                return <any>org.openprovenance.apache.commons.lang.StringEscapeUtils.escapeJava$java_io_Writer$java_lang_String(out, str);
            } else if (((typeof out === 'string') || out === null) && str === undefined) {
                return <any>org.openprovenance.apache.commons.lang.StringEscapeUtils.escapeJava$java_lang_String(out);
            } else throw new Error('invalid overload');
        }

        public static escapeJavaScript$java_lang_String(str: string): string {
            return StringEscapeUtils.escapeJavaStyleString$java_lang_String$boolean$boolean(str, true, true);
        }

        public static escapeJavaScript$java_io_Writer$java_lang_String(out: java.io.Writer, str: string) {
            StringEscapeUtils.escapeJavaStyleString$java_io_Writer$java_lang_String$boolean$boolean(out, str, true, true);
        }

        /**
         * <p>Escapes the characters in a {@code String} using JavaScript String rules
         * to a {@code Writer}.</p>
         * 
         * <p>A {@code null} string input has no effect.</p>
         * 
         * @see #escapeJavaScript(String)
         * @param {java.io.Writer} out  Writer to write escaped string into
         * @param {string} str  String to escape values in, may be null
         * @throws IllegalArgumentException if the Writer is {@code null}
         * @throws IOException if error occurs on underlying Writer
         */
        public static escapeJavaScript(out?: any, str?: any) {
            if (((out != null && out instanceof <any>java.io.Writer) || out === null) && ((typeof str === 'string') || str === null)) {
                return <any>org.openprovenance.apache.commons.lang.StringEscapeUtils.escapeJavaScript$java_io_Writer$java_lang_String(out, str);
            } else if (((typeof out === 'string') || out === null) && str === undefined) {
                return <any>org.openprovenance.apache.commons.lang.StringEscapeUtils.escapeJavaScript$java_lang_String(out);
            } else throw new Error('invalid overload');
        }

        /*private*/ static escapeJavaStyleString$java_lang_String$boolean$boolean(str: string, escapeSingleQuotes: boolean, escapeForwardSlash: boolean): string {
            if (str == null){
                return null;
            }
            try {
                const writer: java.io.StringWriter = new java.io.StringWriter(str.length * 2);
                StringEscapeUtils.escapeJavaStyleString$java_io_Writer$java_lang_String$boolean$boolean(writer, str, escapeSingleQuotes, escapeForwardSlash);
                return writer.toString();
            } catch(ioe) {
                throw new org.openprovenance.apache.commons.lang.UnhandledException(ioe);
            }
        }

        public static escapeJavaStyleString$java_io_Writer$java_lang_String$boolean$boolean(out: java.io.Writer, str: string, escapeSingleQuote: boolean, escapeForwardSlash: boolean) {
            if (out == null){
                throw Object.defineProperty(new Error("The Writer must not be null"), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.Object','java.lang.RuntimeException','java.lang.IllegalArgumentException','java.lang.Exception'] });
            }
            if (str == null){
                return;
            }
            let sz: number;
            sz = str.length;
            for(let i: number = 0; i < sz; i++) {{
                const ch: string = str.charAt(i);
                if ((c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(ch) > 4095){
                    out.write("\\u" + StringEscapeUtils.hex(ch));
                } else if ((c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(ch) > 255){
                    out.write("\\u0" + StringEscapeUtils.hex(ch));
                } else if ((c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(ch) > 127){
                    out.write("\\u00" + StringEscapeUtils.hex(ch));
                } else if ((c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(ch) < 32){
                    switch((ch).charCodeAt(0)) {
                    case 8 /* '\b' */:
                        out.write(('\\').charCodeAt(0));
                        out.write(('b').charCodeAt(0));
                        break;
                    case 10 /* '\n' */:
                        out.write(('\\').charCodeAt(0));
                        out.write(('n').charCodeAt(0));
                        break;
                    case 9 /* '\t' */:
                        out.write(('\\').charCodeAt(0));
                        out.write(('t').charCodeAt(0));
                        break;
                    case 12 /* '\f' */:
                        out.write(('\\').charCodeAt(0));
                        out.write(('f').charCodeAt(0));
                        break;
                    case 13 /* '\r' */:
                        out.write(('\\').charCodeAt(0));
                        out.write(('r').charCodeAt(0));
                        break;
                    default:
                        if ((c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(ch) > 15){
                            out.write("\\u00" + StringEscapeUtils.hex(ch));
                        } else {
                            out.write("\\u000" + StringEscapeUtils.hex(ch));
                        }
                        break;
                    }
                } else {
                    switch((ch).charCodeAt(0)) {
                    case 39 /* '\'' */:
                        if (escapeSingleQuote){
                            out.write(('\\').charCodeAt(0));
                        }
                        out.write(('\'').charCodeAt(0));
                        break;
                    case 34 /* '\"' */:
                        out.write(('\\').charCodeAt(0));
                        out.write(('\"').charCodeAt(0));
                        break;
                    case 92 /* '\\' */:
                        out.write(('\\').charCodeAt(0));
                        out.write(('\\').charCodeAt(0));
                        break;
                    case 47 /* '/' */:
                        if (escapeForwardSlash){
                            out.write(('\\').charCodeAt(0));
                        }
                        out.write(('/').charCodeAt(0));
                        break;
                    default:
                        out.write((ch).charCodeAt(0));
                        break;
                    }
                }
            };}
        }

        /**
         * <p>Worker method for the {@link #escapeJavaScript(String)} method.</p>
         * 
         * @param {java.io.Writer} out write to receieve the escaped string
         * @param {string} str String to escape values in, may be null
         * @param {boolean} escapeSingleQuote escapes single quotes if {@code true}
         * @param {boolean} escapeForwardSlash TODO
         * @throws IOException if an IOException occurs
         * @private
         */
        public static escapeJavaStyleString(out?: any, str?: any, escapeSingleQuote?: any, escapeForwardSlash?: any) {
            if (((out != null && out instanceof <any>java.io.Writer) || out === null) && ((typeof str === 'string') || str === null) && ((typeof escapeSingleQuote === 'boolean') || escapeSingleQuote === null) && ((typeof escapeForwardSlash === 'boolean') || escapeForwardSlash === null)) {
                return <any>org.openprovenance.apache.commons.lang.StringEscapeUtils.escapeJavaStyleString$java_io_Writer$java_lang_String$boolean$boolean(out, str, escapeSingleQuote, escapeForwardSlash);
            } else if (((typeof out === 'string') || out === null) && ((typeof str === 'boolean') || str === null) && ((typeof escapeSingleQuote === 'boolean') || escapeSingleQuote === null) && escapeForwardSlash === undefined) {
                return <any>org.openprovenance.apache.commons.lang.StringEscapeUtils.escapeJavaStyleString$java_lang_String$boolean$boolean(out, str, escapeSingleQuote);
            } else throw new Error('invalid overload');
        }

        /**
         * <p>Returns an upper case hexadecimal {@code String} for the given
         * character.</p>
         * 
         * @param {string} ch The character to convert.
         * @return {string} An upper case hexadecimal {@code String}
         * @private
         */
        /*private*/ static hex(ch: string): string {
            return /* toUpperCase */javaemul.internal.IntegerHelper.toHexString(ch).toUpperCase();
        }

        public static unescapeJava$java_lang_String(str: string): string {
            if (str == null){
                return null;
            }
            try {
                const writer: java.io.StringWriter = new java.io.StringWriter(str.length);
                StringEscapeUtils.unescapeJava$java_io_Writer$java_lang_String(writer, str);
                return writer.toString();
            } catch(ioe) {
                throw new org.openprovenance.apache.commons.lang.UnhandledException(ioe);
            }
        }

        public static unescapeJava$java_io_Writer$java_lang_String(out: java.io.Writer, str: string) {
            if (out == null){
                throw Object.defineProperty(new Error("The Writer must not be null"), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.Object','java.lang.RuntimeException','java.lang.IllegalArgumentException','java.lang.Exception'] });
            }
            if (str == null){
                return;
            }
            const sz: number = str.length;
            const unicode: org.openprovenance.apache.commons.lang.text.StrBuilder = new org.openprovenance.apache.commons.lang.text.StrBuilder(4);
            let hadSlash: boolean = false;
            let inUnicode: boolean = false;
            for(let i: number = 0; i < sz; i++) {{
                const ch: string = str.charAt(i);
                if (inUnicode){
                    unicode.append$char(ch);
                    if (unicode.length() === 4){
                        try {
                            const value: number = /* parseInt */parseInt(unicode.toString(), 16);
                            out.write((String.fromCharCode(value)).charCodeAt(0));
                            unicode.setLength(0);
                            inUnicode = false;
                            hadSlash = false;
                        } catch(nfe) {
                            throw new org.openprovenance.apache.commons.lang.exception.NestableRuntimeException("Unable to parse unicode value: " + unicode, nfe);
                        }
                    }
                    continue;
                }
                if (hadSlash){
                    hadSlash = false;
                    switch((ch).charCodeAt(0)) {
                    case 92 /* '\\' */:
                        out.write(('\\').charCodeAt(0));
                        break;
                    case 39 /* '\'' */:
                        out.write(('\'').charCodeAt(0));
                        break;
                    case 34 /* '\"' */:
                        out.write(('\"').charCodeAt(0));
                        break;
                    case 114 /* 'r' */:
                        out.write(('\r').charCodeAt(0));
                        break;
                    case 102 /* 'f' */:
                        out.write(('\f').charCodeAt(0));
                        break;
                    case 116 /* 't' */:
                        out.write(('\t').charCodeAt(0));
                        break;
                    case 110 /* 'n' */:
                        out.write(('\n').charCodeAt(0));
                        break;
                    case 98 /* 'b' */:
                        out.write(('\b').charCodeAt(0));
                        break;
                    case 117 /* 'u' */:
                        {
                            inUnicode = true;
                            break;
                        };
                    default:
                        out.write((ch).charCodeAt(0));
                        break;
                    }
                    continue;
                } else if ((c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(ch) == '\\'.charCodeAt(0)){
                    hadSlash = true;
                    continue;
                }
                out.write((ch).charCodeAt(0));
            };}
            if (hadSlash){
                out.write(('\\').charCodeAt(0));
            }
        }

        /**
         * <p>Unescapes any Java literals found in the {@code String} to a
         * {@code Writer}.</p>
         * 
         * <p>For example, it will turn a sequence of {@code '\'} and
         * {@code 'n'} into a newline character, unless the {@code '\'}
         * is preceded by another {@code '\'}.</p>
         * 
         * <p>A {@code null} string input has no effect.</p>
         * 
         * @param {java.io.Writer} out  the {@code Writer} used to output unescaped characters
         * @param {string} str  the {@code String} to unescape, may be null
         * @throws IllegalArgumentException if the Writer is {@code null}
         * @throws IOException if error occurs on underlying Writer
         */
        public static unescapeJava(out?: any, str?: any) {
            if (((out != null && out instanceof <any>java.io.Writer) || out === null) && ((typeof str === 'string') || str === null)) {
                return <any>org.openprovenance.apache.commons.lang.StringEscapeUtils.unescapeJava$java_io_Writer$java_lang_String(out, str);
            } else if (((typeof out === 'string') || out === null) && str === undefined) {
                return <any>org.openprovenance.apache.commons.lang.StringEscapeUtils.unescapeJava$java_lang_String(out);
            } else throw new Error('invalid overload');
        }

        public static unescapeJavaScript$java_lang_String(str: string): string {
            return StringEscapeUtils.unescapeJava$java_lang_String(str);
        }

        public static unescapeJavaScript$java_io_Writer$java_lang_String(out: java.io.Writer, str: string) {
            StringEscapeUtils.unescapeJava$java_io_Writer$java_lang_String(out, str);
        }

        /**
         * <p>Unescapes any JavaScript literals found in the {@code String} to a
         * {@code Writer}.</p>
         * 
         * <p>For example, it will turn a sequence of {@code '\'} and {@code 'n'}
         * into a newline character, unless the {@code '\'} is preceded by another
         * {@code '\'}.</p>
         * 
         * <p>A {@code null} string input has no effect.</p>
         * 
         * @see #unescapeJava(Writer,String)
         * @param {java.io.Writer} out  the {@code Writer} used to output unescaped characters
         * @param {string} str  the {@code String} to unescape, may be null
         * @throws IllegalArgumentException if the Writer is {@code null}
         * @throws IOException if error occurs on underlying Writer
         */
        public static unescapeJavaScript(out?: any, str?: any) {
            if (((out != null && out instanceof <any>java.io.Writer) || out === null) && ((typeof str === 'string') || str === null)) {
                return <any>org.openprovenance.apache.commons.lang.StringEscapeUtils.unescapeJavaScript$java_io_Writer$java_lang_String(out, str);
            } else if (((typeof out === 'string') || out === null) && str === undefined) {
                return <any>org.openprovenance.apache.commons.lang.StringEscapeUtils.unescapeJavaScript$java_lang_String(out);
            } else throw new Error('invalid overload');
        }

        public static escapeHtml$java_lang_String(str: string): string {
            if (str == null){
                return null;
            }
            try {
                const writer: java.io.StringWriter = new java.io.StringWriter((<number>(str.length * 1.5)|0));
                StringEscapeUtils.escapeHtml$java_io_Writer$java_lang_String(writer, str);
                return writer.toString();
            } catch(ioe) {
                throw new org.openprovenance.apache.commons.lang.UnhandledException(ioe);
            }
        }

        public static escapeHtml$java_io_Writer$java_lang_String(writer: java.io.Writer, string: string) {
            if (writer == null){
                throw Object.defineProperty(new Error("The Writer must not be null."), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.Object','java.lang.RuntimeException','java.lang.IllegalArgumentException','java.lang.Exception'] });
            }
            if (string == null){
                return;
            }
            org.openprovenance.apache.commons.lang.Entities.HTML40_$LI$().escape$java_io_Writer$java_lang_String(writer, string);
        }

        /**
         * <p>Escapes the characters in a {@code String} using HTML entities and writes
         * them to a {@code Writer}.</p>
         * 
         * <p>
         * For example:
         * </p>
         * {@code "bread" &amp; "butter"}
         * <p>becomes:</p>
         * {@code &amp;quot;bread&amp;quot; &amp;amp; &amp;quot;butter&amp;quot;}.
         * 
         * <p>Supports all known HTML 4.0 entities, including funky accents.
         * Note that the commonly used apostrophe escape character (&amp;apos;)
         * is not a legal generalEntity and so is not supported). </p>
         * 
         * @param {java.io.Writer} writer  the writer receiving the escaped string, not null
         * @param {string} string  the {@code String} to escape, may be null
         * @throws IllegalArgumentException if the writer is null
         * @throws IOException when {@code Writer} passed throws the exception from
         * calls to the {@link Writer#write(int)} methods.
         * 
         * @see #escapeHtml(String)
         * @see #unescapeHtml(String)
         * @see <a href="http://hotwired.lycos.com/webmonkey/reference/special_characters/">ISO Entities</a>
         * @see <a href="http://www.w3.org/TR/REC-html32#latin1">HTML 3.2 Character Entities for ISO Latin-1</a>
         * @see <a href="http://www.w3.org/TR/REC-html40/sgml/entities.html">HTML 4.0 Character generalEntity references</a>
         * @see <a href="http://www.w3.org/TR/html401/charset.html#h-5.3">HTML 4.01 Character References</a>
         * @see <a href="http://www.w3.org/TR/html401/charset.html#code-position">HTML 4.01 Code positions</a>
         */
        public static escapeHtml(writer?: any, string?: any) {
            if (((writer != null && writer instanceof <any>java.io.Writer) || writer === null) && ((typeof string === 'string') || string === null)) {
                return <any>org.openprovenance.apache.commons.lang.StringEscapeUtils.escapeHtml$java_io_Writer$java_lang_String(writer, string);
            } else if (((typeof writer === 'string') || writer === null) && string === undefined) {
                return <any>org.openprovenance.apache.commons.lang.StringEscapeUtils.escapeHtml$java_lang_String(writer);
            } else throw new Error('invalid overload');
        }

        public static unescapeHtml$java_lang_String(str: string): string {
            if (str == null){
                return null;
            }
            try {
                const writer: java.io.StringWriter = new java.io.StringWriter((<number>(str.length * 1.5)|0));
                StringEscapeUtils.unescapeHtml$java_io_Writer$java_lang_String(writer, str);
                return writer.toString();
            } catch(ioe) {
                throw new org.openprovenance.apache.commons.lang.UnhandledException(ioe);
            }
        }

        public static unescapeHtml$java_io_Writer$java_lang_String(writer: java.io.Writer, string: string) {
            if (writer == null){
                throw Object.defineProperty(new Error("The Writer must not be null."), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.Object','java.lang.RuntimeException','java.lang.IllegalArgumentException','java.lang.Exception'] });
            }
            if (string == null){
                return;
            }
            org.openprovenance.apache.commons.lang.Entities.HTML40_$LI$().unescape$java_io_Writer$java_lang_String(writer, string);
        }

        /**
         * <p>Unescapes a string containing generalEntity escapes to a string
         * containing the actual Unicode characters corresponding to the
         * escapes. Supports HTML 4.0 entities.</p>
         * 
         * <p>For example, the string "&amp;lt;Fran&amp;ccedil;ais&amp;gt;"
         * will become "&lt;Fran&ccedil;ais&gt;"</p>
         * 
         * <p>If an generalEntity is unrecognized, it is left alone, and inserted
         * verbatim into the result string. e1.g. "&amp;gt;&amp;zzzz;x" will
         * become "&gt;&amp;zzzz;x".</p>
         * 
         * @param {java.io.Writer} writer  the writer receiving the unescaped string, not null
         * @param {string} string  the {@code String} to unescape, may be null
         * @throws IllegalArgumentException if the writer is null
         * @throws IOException if an IOException occurs
         * @see #escapeHtml(String)
         */
        public static unescapeHtml(writer?: any, string?: any) {
            if (((writer != null && writer instanceof <any>java.io.Writer) || writer === null) && ((typeof string === 'string') || string === null)) {
                return <any>org.openprovenance.apache.commons.lang.StringEscapeUtils.unescapeHtml$java_io_Writer$java_lang_String(writer, string);
            } else if (((typeof writer === 'string') || writer === null) && string === undefined) {
                return <any>org.openprovenance.apache.commons.lang.StringEscapeUtils.unescapeHtml$java_lang_String(writer);
            } else throw new Error('invalid overload');
        }

        public static escapeXml$java_io_Writer$java_lang_String(writer: java.io.Writer, str: string) {
            if (writer == null){
                throw Object.defineProperty(new Error("The Writer must not be null."), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.Object','java.lang.RuntimeException','java.lang.IllegalArgumentException','java.lang.Exception'] });
            }
            if (str == null){
                return;
            }
            org.openprovenance.apache.commons.lang.Entities.XML_$LI$().escape$java_io_Writer$java_lang_String(writer, str);
        }

        /**
         * <p>Escapes the characters in a {@code String} using XML entities.</p>
         * 
         * <p>For example: {@code "bread" &amp; "butter"}
         * {@code &amp;quot;bread&amp;quot; &amp;amp; &amp;quot;butter&amp;quot;}.
         * </p>
         * 
         * <p>Supports only the five basic XML entities (gt, lt, quot, amp, apos).
         * Does not support DTDs or external entities.</p>
         * 
         * <p>Note that unicode characters greater than 0x7f are currently escaped to
         * their numerical \\u equivalent. This may change in future releases. </p>
         * 
         * @param {java.io.Writer} writer  the writer receiving the unescaped string, not null
         * @param {string} str  the {@code String} to escape, may be null
         * @throws IllegalArgumentException if the writer is null
         * @throws IOException if there is a problem writing
         * @see #unescapeXml(String)
         */
        public static escapeXml(writer?: any, str?: any) {
            if (((writer != null && writer instanceof <any>java.io.Writer) || writer === null) && ((typeof str === 'string') || str === null)) {
                return <any>org.openprovenance.apache.commons.lang.StringEscapeUtils.escapeXml$java_io_Writer$java_lang_String(writer, str);
            } else if (((typeof writer === 'string') || writer === null) && str === undefined) {
                return <any>org.openprovenance.apache.commons.lang.StringEscapeUtils.escapeXml$java_lang_String(writer);
            } else throw new Error('invalid overload');
        }

        public static escapeXml$java_lang_String(str: string): string {
            if (str == null){
                return null;
            }
            return org.openprovenance.apache.commons.lang.Entities.XML_$LI$().escape$java_lang_String(str);
        }

        public static unescapeXml$java_io_Writer$java_lang_String(writer: java.io.Writer, str: string) {
            if (writer == null){
                throw Object.defineProperty(new Error("The Writer must not be null."), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.Object','java.lang.RuntimeException','java.lang.IllegalArgumentException','java.lang.Exception'] });
            }
            if (str == null){
                return;
            }
            org.openprovenance.apache.commons.lang.Entities.XML_$LI$().unescape$java_io_Writer$java_lang_String(writer, str);
        }

        /**
         * <p>Unescapes a string containing XML generalEntity escapes to a string
         * containing the actual Unicode characters corresponding to the
         * escapes.</p>
         * 
         * <p>Supports only the five basic XML entities (gt, lt, quot, amp, apos).
         * Does not support DTDs or external entities.</p>
         * 
         * <p>Note that numerical \\u unicode codes are unescaped to their respective
         * unicode characters. This may change in future releases. </p>
         * 
         * @param {java.io.Writer} writer  the writer receiving the unescaped string, not null
         * @param {string} str  the {@code String} to unescape, may be null
         * @throws IllegalArgumentException if the writer is null
         * @throws IOException if there is a problem writing
         * @see #escapeXml(String)
         */
        public static unescapeXml(writer?: any, str?: any) {
            if (((writer != null && writer instanceof <any>java.io.Writer) || writer === null) && ((typeof str === 'string') || str === null)) {
                return <any>org.openprovenance.apache.commons.lang.StringEscapeUtils.unescapeXml$java_io_Writer$java_lang_String(writer, str);
            } else if (((typeof writer === 'string') || writer === null) && str === undefined) {
                return <any>org.openprovenance.apache.commons.lang.StringEscapeUtils.unescapeXml$java_lang_String(writer);
            } else throw new Error('invalid overload');
        }

        public static unescapeXml$java_lang_String(str: string): string {
            if (str == null){
                return null;
            }
            return org.openprovenance.apache.commons.lang.Entities.XML_$LI$().unescape$java_lang_String(str);
        }

        /**
         * <p>Escapes the characters in a {@code String} to be suitable to pass to
         * an SQL query.</p>
         * 
         * <p>For example,
         * <pre>statement.executeQuery("SELECT * FROM MOVIES WHERE TITLE='" +
         * StringEscapeUtils.escapeSql("McHale's Navy") +
         * "'");</pre>
         * 
         * 
         * <p>At present, this method only turns single-quotes into doubled single-quotes
         * ({@code "McHale's Navy"} =&gt; {@code "McHale''s Navy"}). It does not
         * handle the cases of percent (%) or underscore (_) for use in LIKE clauses.</p>
         * 
         * see http://www.jguru.com/faq/view.jsp?EID=8881
         * @param {string} str  the string to escape, may be null
         * @return {string} a new String, escaped for SQL, {@code null} if null string input
         */
        public static escapeSql(str: string): string {
            if (str == null){
                return null;
            }
            return org.openprovenance.apache.commons.lang.StringUtils.replace$java_lang_String$java_lang_String$java_lang_String(str, "\'", "\'\'");
        }

        public static escapeCsv$java_lang_String(str: string): string {
            if (org.openprovenance.apache.commons.lang.StringUtils.containsNone$java_lang_String$char_A(str, StringEscapeUtils.CSV_SEARCH_CHARS_$LI$())){
                return str;
            }
            try {
                const writer: java.io.StringWriter = new java.io.StringWriter();
                StringEscapeUtils.escapeCsv$java_io_Writer$java_lang_String(writer, str);
                return writer.toString();
            } catch(ioe) {
                throw new org.openprovenance.apache.commons.lang.UnhandledException(ioe);
            }
        }

        public static escapeCsv$java_io_Writer$java_lang_String(out: java.io.Writer, str: string) {
            if (org.openprovenance.apache.commons.lang.StringUtils.containsNone$java_lang_String$char_A(str, StringEscapeUtils.CSV_SEARCH_CHARS_$LI$())){
                if (str != null){
                    out.write(str);
                }
                return;
            }
            out.write((StringEscapeUtils.CSV_QUOTE).charCodeAt(0));
            for(let i: number = 0; i < str.length; i++) {{
                const c: string = str.charAt(i);
                if ((c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(c) == (c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(StringEscapeUtils.CSV_QUOTE)){
                    out.write((StringEscapeUtils.CSV_QUOTE).charCodeAt(0));
                }
                out.write((c).charCodeAt(0));
            };}
            out.write((StringEscapeUtils.CSV_QUOTE).charCodeAt(0));
        }

        /**
         * <p>Writes a {@code String} value for a CSV column enclosed in double quotes,
         * if required.</p>
         * 
         * <p>If the value contains a comma, newline or double quote, then the
         * String value is written enclosed in double quotes.</p>
         * 
         * <p>Any double quote characters in the value are escaped with another double quote.</p>
         * 
         * <p>If the value does not contain a comma, newline or double quote, then the
         * String value is written unchanged (null values are ignored).</p>
         * 
         * see <a href="http://en.wikipedia.org/wiki/Comma-separated_values">Wikipedia</a> and
         * <a href="http://tools.ietf.org/html/rfc4180">RFC 4180</a>.
         * 
         * @param {string} str the input CSV column String, may be null
         * @param {java.io.Writer} out Writer to write input string to, enclosed in double quotes if it contains
         * a comma, newline or double quote
         * @throws IOException if error occurs on underlying Writer
         * @since 2.4
         */
        public static escapeCsv(out?: any, str?: any) {
            if (((out != null && out instanceof <any>java.io.Writer) || out === null) && ((typeof str === 'string') || str === null)) {
                return <any>org.openprovenance.apache.commons.lang.StringEscapeUtils.escapeCsv$java_io_Writer$java_lang_String(out, str);
            } else if (((typeof out === 'string') || out === null) && str === undefined) {
                return <any>org.openprovenance.apache.commons.lang.StringEscapeUtils.escapeCsv$java_lang_String(out);
            } else throw new Error('invalid overload');
        }

        public static unescapeCsv$java_lang_String(str: string): string {
            if (str == null){
                return null;
            }
            try {
                const writer: java.io.StringWriter = new java.io.StringWriter();
                StringEscapeUtils.unescapeCsv$java_io_Writer$java_lang_String(writer, str);
                return writer.toString();
            } catch(ioe) {
                throw new org.openprovenance.apache.commons.lang.UnhandledException(ioe);
            }
        }

        public static unescapeCsv$java_io_Writer$java_lang_String(out: java.io.Writer, str: string) {
            if (str == null){
                return;
            }
            if (str.length < 2){
                out.write(str);
                return;
            }
            if ((c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(str.charAt(0)) != (c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(StringEscapeUtils.CSV_QUOTE) || (c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(str.charAt(str.length - 1)) != (c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(StringEscapeUtils.CSV_QUOTE)){
                out.write(str);
                return;
            }
            const quoteless: string = str.substring(1, str.length - 1);
            if (org.openprovenance.apache.commons.lang.StringUtils.containsAny$java_lang_String$char_A(quoteless, StringEscapeUtils.CSV_SEARCH_CHARS_$LI$())){
                str = org.openprovenance.apache.commons.lang.StringUtils.replace$java_lang_String$java_lang_String$java_lang_String(quoteless, StringEscapeUtils.CSV_QUOTE_STR_$LI$() + StringEscapeUtils.CSV_QUOTE_STR_$LI$(), StringEscapeUtils.CSV_QUOTE_STR_$LI$());
            }
            out.write(str);
        }

        /**
         * <p>Returns a {@code String} value for an unescaped CSV column. </p>
         * 
         * <p>If the value is enclosed in double quotes, and contains a comma, newline
         * or double quote, then quotes are removed.
         * </p>
         * 
         * <p>Any double quote escaped characters (a pair of double quotes) are unescaped
         * to just one double quote. </p>
         * 
         * <p>If the value is not enclosed in double quotes, or is and does not contain a
         * comma, newline or double quote, then the String value is returned unchanged.</p>
         * 
         * see <a href="http://en.wikipedia.org/wiki/Comma-separated_values">Wikipedia</a> and
         * <a href="http://tools.ietf.org/html/rfc4180">RFC 4180</a>.
         * 
         * @param {string} str the input CSV column String, may be null
         * @param {java.io.Writer} out Writer to write the input String to, with enclosing double quotes
         * removed and embedded double quotes unescaped, {@code null} if null string input
         * @throws IOException if error occurs on underlying Writer
         * @since 2.4
         */
        public static unescapeCsv(out?: any, str?: any) {
            if (((out != null && out instanceof <any>java.io.Writer) || out === null) && ((typeof str === 'string') || str === null)) {
                return <any>org.openprovenance.apache.commons.lang.StringEscapeUtils.unescapeCsv$java_io_Writer$java_lang_String(out, str);
            } else if (((typeof out === 'string') || out === null) && str === undefined) {
                return <any>org.openprovenance.apache.commons.lang.StringEscapeUtils.unescapeCsv$java_lang_String(out);
            } else throw new Error('invalid overload');
        }
    }
    StringEscapeUtils["__class"] = "org.openprovenance.apache.commons.lang.StringEscapeUtils";

}

