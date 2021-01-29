/* Generated from Java with JSweet 3.0.0 - http://www.jsweet.org */
namespace org.openprovenance.prov.model {
    export class QualifiedNameUtils {
        public static ESCAPE_PROV_LOCAL_NAME: org.apache.commons.lang3.text.translate.CharSequenceTranslator; public static ESCAPE_PROV_LOCAL_NAME_$LI$(): org.apache.commons.lang3.text.translate.CharSequenceTranslator { if (QualifiedNameUtils.ESCAPE_PROV_LOCAL_NAME == null) { QualifiedNameUtils.ESCAPE_PROV_LOCAL_NAME = new org.apache.commons.lang3.text.translate.AggregateTranslator(new org.apache.commons.lang3.text.translate.LookupTranslator([["=", "\\="], ["\'", "\\\'"], ["(", "\\("], [")", "\\)"], [",", "\\,"], [":", "\\:"], [";", "\\;"], ["[", "\\["], ["]", "\\]"], ["<", "%3C"], [">", "%3E"], ["\\", "%5C"]]), org.apache.commons.lang3.text.translate.UnicodeEscaper.outsideOf(32, 65535)); }  return QualifiedNameUtils.ESCAPE_PROV_LOCAL_NAME; }

        public static UNESCAPE_PROV_LOCAL_NAME: org.apache.commons.lang3.text.translate.CharSequenceTranslator; public static UNESCAPE_PROV_LOCAL_NAME_$LI$(): org.apache.commons.lang3.text.translate.CharSequenceTranslator { if (QualifiedNameUtils.UNESCAPE_PROV_LOCAL_NAME == null) { QualifiedNameUtils.UNESCAPE_PROV_LOCAL_NAME = new org.apache.commons.lang3.text.translate.AggregateTranslator(new org.apache.commons.lang3.text.translate.LookupTranslator([["\\=", "="], ["\\\'", "\'"], ["\\(", "("], ["\\)", ")"], ["\\,", ","], ["\\-", "-"], ["\\:", ":"], ["\\;", ";"], ["\\[", "["], ["\\]", "]"], ["\\.", "."], ["%3C", "<"], ["%3E", ">"], ["%3c", "<"], ["%3e", ">"], ["%5C", "\\"], ["%5c", "\\"]]), new org.apache.commons.lang3.text.translate.UnicodeUnescaper()); }  return QualifiedNameUtils.UNESCAPE_PROV_LOCAL_NAME; }

        public static ESCAPE_UNICODE: org.apache.commons.lang3.text.translate.CharSequenceTranslator; public static ESCAPE_UNICODE_$LI$(): org.apache.commons.lang3.text.translate.CharSequenceTranslator { if (QualifiedNameUtils.ESCAPE_UNICODE == null) { QualifiedNameUtils.ESCAPE_UNICODE = new org.apache.commons.lang3.text.translate.AggregateTranslator(org.apache.commons.lang3.text.translate.UnicodeEscaper.outsideOf(32, 255)); }  return QualifiedNameUtils.ESCAPE_UNICODE; }

        public escapeProvLocalName(localName: string): string {
            if ("-" === localName)return "\\-";
            const tmp: string = QualifiedNameUtils.ESCAPE_PROV_LOCAL_NAME_$LI$().translate(localName);
            const len: number = tmp.length;
            if (len > 0 && (c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(tmp.charAt(len - 1)) == '.'.charCodeAt(0)){
                return (tmp.substring(0, len - 1)) + "\\.";
            } else if (len > 0 && (c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(tmp.charAt(0)) == '.'.charCodeAt(0)){
                return ("\\." + tmp.substring(1));
            } else {
                return tmp;
            }
        }

        public unescapeProvLocalName(localName: string): string {
            return QualifiedNameUtils.UNESCAPE_PROV_LOCAL_NAME_$LI$().translate(localName);
        }

        public static INNER_ESCAPE: string = "_";

        public static START_ESCAPE: string = "_";

        public static ESCAPE_TO_XML_QNAME_LOCAL_NAME: org.apache.commons.lang3.text.translate.CharSequenceTranslator; public static ESCAPE_TO_XML_QNAME_LOCAL_NAME_$LI$(): org.apache.commons.lang3.text.translate.CharSequenceTranslator { if (QualifiedNameUtils.ESCAPE_TO_XML_QNAME_LOCAL_NAME == null) { QualifiedNameUtils.ESCAPE_TO_XML_QNAME_LOCAL_NAME = new org.apache.commons.lang3.text.translate.AggregateTranslator(new org.apache.commons.lang3.text.translate.LookupTranslator([["=", QualifiedNameUtils.INNER_ESCAPE + "3D"], ["\'", QualifiedNameUtils.INNER_ESCAPE + "27"], ["(", QualifiedNameUtils.INNER_ESCAPE + "28"], [")", QualifiedNameUtils.INNER_ESCAPE + "29"], [",", QualifiedNameUtils.INNER_ESCAPE + "2C"], [QualifiedNameUtils.INNER_ESCAPE, QualifiedNameUtils.INNER_ESCAPE + QualifiedNameUtils.INNER_ESCAPE], [":", QualifiedNameUtils.INNER_ESCAPE + "3A"], [";", QualifiedNameUtils.INNER_ESCAPE + "3B"], ["[", QualifiedNameUtils.INNER_ESCAPE + "5B"], ["]", QualifiedNameUtils.INNER_ESCAPE + "5D"], ["/", QualifiedNameUtils.INNER_ESCAPE + "2F"], ["\\", QualifiedNameUtils.INNER_ESCAPE + "5C"], ["?", QualifiedNameUtils.INNER_ESCAPE + "3F"], ["@", QualifiedNameUtils.INNER_ESCAPE + "40"], ["~", QualifiedNameUtils.INNER_ESCAPE + "7E"], ["&", QualifiedNameUtils.INNER_ESCAPE + "26"], ["+", QualifiedNameUtils.INNER_ESCAPE + "2B"], ["*", QualifiedNameUtils.INNER_ESCAPE + "2A"], ["#", QualifiedNameUtils.INNER_ESCAPE + "23"], ["$", QualifiedNameUtils.INNER_ESCAPE + "24"], ["!", QualifiedNameUtils.INNER_ESCAPE + "21"], ["<", QualifiedNameUtils.INNER_ESCAPE + "3C"], [">", QualifiedNameUtils.INNER_ESCAPE + "3E"], ["%", QualifiedNameUtils.INNER_ESCAPE + "25"]]), org.apache.commons.lang3.text.translate.JavaUnicodeEscaper.outsideOf(32, 65535)); }  return QualifiedNameUtils.ESCAPE_TO_XML_QNAME_LOCAL_NAME; }

        public static UNESCAPE_FROM_XML_QNAME_LOCAL_NAME: org.apache.commons.lang3.text.translate.CharSequenceTranslator; public static UNESCAPE_FROM_XML_QNAME_LOCAL_NAME_$LI$(): org.apache.commons.lang3.text.translate.CharSequenceTranslator { if (QualifiedNameUtils.UNESCAPE_FROM_XML_QNAME_LOCAL_NAME == null) { QualifiedNameUtils.UNESCAPE_FROM_XML_QNAME_LOCAL_NAME = new org.apache.commons.lang3.text.translate.AggregateTranslator(new org.apache.commons.lang3.text.translate.LookupTranslator([[QualifiedNameUtils.INNER_ESCAPE + "3D", "="], [QualifiedNameUtils.INNER_ESCAPE + "27", "\'"], [QualifiedNameUtils.INNER_ESCAPE + "28", "("], [QualifiedNameUtils.INNER_ESCAPE + "29", ")"], [QualifiedNameUtils.INNER_ESCAPE + "2C", ","], [QualifiedNameUtils.INNER_ESCAPE + QualifiedNameUtils.INNER_ESCAPE, QualifiedNameUtils.INNER_ESCAPE], [QualifiedNameUtils.INNER_ESCAPE + "00", ""], [QualifiedNameUtils.INNER_ESCAPE + "3A", ":"], [QualifiedNameUtils.INNER_ESCAPE + "3B", ";"], [QualifiedNameUtils.INNER_ESCAPE + "5B", "["], [QualifiedNameUtils.INNER_ESCAPE + "5D", "]"], [QualifiedNameUtils.INNER_ESCAPE + "2F", "/"], [QualifiedNameUtils.INNER_ESCAPE + "5C", "\\"], [QualifiedNameUtils.INNER_ESCAPE + "3F", "?"], [QualifiedNameUtils.INNER_ESCAPE + "40", "@"], [QualifiedNameUtils.INNER_ESCAPE + "7E", "~"], [QualifiedNameUtils.INNER_ESCAPE + "26", "&"], [QualifiedNameUtils.INNER_ESCAPE + "2B", "+"], [QualifiedNameUtils.INNER_ESCAPE + "2A", "*"], [QualifiedNameUtils.INNER_ESCAPE + "23", "#"], [QualifiedNameUtils.INNER_ESCAPE + "24", "$"], [QualifiedNameUtils.INNER_ESCAPE + "21", "!"], [QualifiedNameUtils.INNER_ESCAPE + "3C", "<"], [QualifiedNameUtils.INNER_ESCAPE + "3E", ">"], [QualifiedNameUtils.INNER_ESCAPE + "25", "%"]]), new org.apache.commons.lang3.text.translate.UnicodeUnescaper()); }  return QualifiedNameUtils.UNESCAPE_FROM_XML_QNAME_LOCAL_NAME; }

        public static UNESCAPE_UNICODE: org.apache.commons.lang3.text.translate.CharSequenceTranslator; public static UNESCAPE_UNICODE_$LI$(): org.apache.commons.lang3.text.translate.CharSequenceTranslator { if (QualifiedNameUtils.UNESCAPE_UNICODE == null) { QualifiedNameUtils.UNESCAPE_UNICODE = new org.apache.commons.lang3.text.translate.AggregateTranslator(new org.apache.commons.lang3.text.translate.UnicodeUnescaper()); }  return QualifiedNameUtils.UNESCAPE_UNICODE; }

        public static isNCNameStartCharToEscape(c: string): boolean {
            return /* isLowerCase */(s => s.toLowerCase() === s)(c) || /* isUpperCase */(s => s.toUpperCase() === s)(c);
        }

        public escapeToXsdLocalName(localName: string): string {
            if ("" === localName)return QualifiedNameUtils.START_ESCAPE;
            const s: string = QualifiedNameUtils.ESCAPE_TO_XML_QNAME_LOCAL_NAME_$LI$().translate(localName);
            if (!QualifiedNameUtils.isNCNameStartCharToEscape(s.charAt(0)))return QualifiedNameUtils.START_ESCAPE + s;
            return s;
        }

        public unescapeFromXsdLocalName(localName: string): string {
            if (QualifiedNameUtils.START_ESCAPE === localName){
                return "";
            }
            if ((c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(localName.charAt(0)) == (c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(QualifiedNameUtils.START_ESCAPE.charAt(0))){
                localName = localName.substring(1);
            }
            const s: string = QualifiedNameUtils.UNESCAPE_FROM_XML_QNAME_LOCAL_NAME_$LI$().translate(localName);
            return s;
        }

        public escapeUnicode(name: string): string {
            return QualifiedNameUtils.ESCAPE_UNICODE_$LI$().translate(name);
        }

        public unescapeUnicode(name: string): string {
            return QualifiedNameUtils.UNESCAPE_UNICODE_$LI$().translate(name);
        }

        static PN_CHARS_U: string = "[A-Za-z_\\u00C0-\\u00D6\\u00D8-\\u00F6\\u00F8-\\u02FF\\u0370-\\u037D\\u037F-\\u1FFF\\u200C-\\u200D\\u2070-\\u218F\\u2C00-\\u2FEF\\u3001-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFFD]";

        static PN_CHARS: string; public static PN_CHARS_$LI$(): string { if (QualifiedNameUtils.PN_CHARS == null) { QualifiedNameUtils.PN_CHARS = "(" + QualifiedNameUtils.PN_CHARS_U + "|[0-9\\-\\u00B7\\u0300-\\u036F\\u203F-\\u2040])"; }  return QualifiedNameUtils.PN_CHARS; }

        static PN_CHARS_ESC: string = "((\\\\)([\\=\\\'\\(\\)\\,\\-\\:\\;\\[\\]\\.]))";

        static HEX: string = "[0-9A-Fa-f]";

        static PERCENT: string; public static PERCENT_$LI$(): string { if (QualifiedNameUtils.PERCENT == null) { QualifiedNameUtils.PERCENT = "(%(" + QualifiedNameUtils.HEX + ")(" + QualifiedNameUtils.HEX + "))"; }  return QualifiedNameUtils.PERCENT; }

        static PN_CHARS_OTHERS: string; public static PN_CHARS_OTHERS_$LI$(): string { if (QualifiedNameUtils.PN_CHARS_OTHERS == null) { QualifiedNameUtils.PN_CHARS_OTHERS = "(([/@~&\\+\\*\\?#$!])|" + QualifiedNameUtils.PN_CHARS_ESC + "|" + QualifiedNameUtils.PERCENT_$LI$() + ")"; }  return QualifiedNameUtils.PN_CHARS_OTHERS; }

        static PN_LOCAL: string; public static PN_LOCAL_$LI$(): string { if (QualifiedNameUtils.PN_LOCAL == null) { QualifiedNameUtils.PN_LOCAL = "(((" + QualifiedNameUtils.PN_CHARS_U + ")|([0-9])|(" + QualifiedNameUtils.PN_CHARS_OTHERS_$LI$() + ")))((((" + QualifiedNameUtils.PN_CHARS_$LI$() + ")|(\\.)|(" + QualifiedNameUtils.PN_CHARS_OTHERS_$LI$() + ")))*(((" + QualifiedNameUtils.PN_CHARS_$LI$() + ")|(" + QualifiedNameUtils.PN_CHARS_OTHERS_$LI$() + "))))?"; }  return QualifiedNameUtils.PN_LOCAL; }

        static pat: java.util.regex.Pattern; public static pat_$LI$(): java.util.regex.Pattern { if (QualifiedNameUtils.pat == null) { QualifiedNameUtils.pat = java.util.regex.Pattern.compile(QualifiedNameUtils.PN_LOCAL_$LI$()); }  return QualifiedNameUtils.pat; }

        public patternExactMatch(input: string): boolean {
            if ("" === input)return true;
            const match: java.util.regex.Matcher = QualifiedNameUtils.pat_$LI$().matcher(input);
            if (match.find()){
                return match.start() === 0 && match.end() === input.length;
            } else {
                return false;
            }
        }

        static XML_NameStartChar: string = "[A-Za-z_\\u00C0-\\u00D6\\u00D8-\\u00F6\\u00F8-\\u02FF\\u0370-\\u037D\\u037F-\\u1FFF\\u200C-\\u200D\\u2070-\\u218F\\u2C00-\\u2FEF\\u3001-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFFD]";

        static XML_NameChar: string; public static XML_NameChar_$LI$(): string { if (QualifiedNameUtils.XML_NameChar == null) { QualifiedNameUtils.XML_NameChar = "(" + QualifiedNameUtils.XML_NameStartChar + "|[\\-\\.0-9\\u00B7\\u0300-\\u036F\\u203F-\\u2040])"; }  return QualifiedNameUtils.XML_NameChar; }

        static XML_Name: string; public static XML_Name_$LI$(): string { if (QualifiedNameUtils.XML_Name == null) { QualifiedNameUtils.XML_Name = QualifiedNameUtils.XML_NameStartChar + "(" + QualifiedNameUtils.XML_NameChar_$LI$() + ")*"; }  return QualifiedNameUtils.XML_Name; }

        static NC_NameChar: string; public static NC_NameChar_$LI$(): string { if (QualifiedNameUtils.NC_NameChar == null) { QualifiedNameUtils.NC_NameChar = QualifiedNameUtils.XML_NameChar_$LI$(); }  return QualifiedNameUtils.NC_NameChar; }

        static NC_NameStartChar: string; public static NC_NameStartChar_$LI$(): string { if (QualifiedNameUtils.NC_NameStartChar == null) { QualifiedNameUtils.NC_NameStartChar = QualifiedNameUtils.XML_NameStartChar; }  return QualifiedNameUtils.NC_NameStartChar; }

        static NC_Name: string; public static NC_Name_$LI$(): string { if (QualifiedNameUtils.NC_Name == null) { QualifiedNameUtils.NC_Name = QualifiedNameUtils.NC_NameStartChar_$LI$() + "(" + QualifiedNameUtils.NC_NameChar_$LI$() + ")*"; }  return QualifiedNameUtils.NC_Name; }

        static NC_pat: java.util.regex.Pattern; public static NC_pat_$LI$(): java.util.regex.Pattern { if (QualifiedNameUtils.NC_pat == null) { QualifiedNameUtils.NC_pat = java.util.regex.Pattern.compile(QualifiedNameUtils.NC_Name_$LI$()); }  return QualifiedNameUtils.NC_pat; }

        public is_NC_Name(input: string): boolean {
            if ("" === input)return false;
            const match: java.util.regex.Matcher = QualifiedNameUtils.NC_pat_$LI$().matcher(input);
            if (match.matches()){
                return match.start() === 0 && match.end() === input.length;
            } else {
                return false;
            }
        }
    }
    QualifiedNameUtils["__class"] = "org.openprovenance.prov.model.QualifiedNameUtils";

}

