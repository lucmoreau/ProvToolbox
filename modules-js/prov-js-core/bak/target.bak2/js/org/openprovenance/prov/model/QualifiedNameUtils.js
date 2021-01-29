/* Generated from Java with JSweet 3.0.0 - http://www.jsweet.org */
var org;
(function (org) {
    var openprovenance;
    (function (openprovenance) {
        var prov;
        (function (prov) {
            var model;
            (function (model) {
                class QualifiedNameUtils {
                    static ESCAPE_PROV_LOCAL_NAME_$LI$() { if (QualifiedNameUtils.ESCAPE_PROV_LOCAL_NAME == null) {
                        QualifiedNameUtils.ESCAPE_PROV_LOCAL_NAME = new org.apache.commons.lang3.text.translate.AggregateTranslator(new org.apache.commons.lang3.text.translate.LookupTranslator([["=", "\\="], ["\'", "\\\'"], ["(", "\\("], [")", "\\)"], [",", "\\,"], [":", "\\:"], [";", "\\;"], ["[", "\\["], ["]", "\\]"], ["<", "%3C"], [">", "%3E"], ["\\", "%5C"]]), org.apache.commons.lang3.text.translate.UnicodeEscaper.outsideOf(32, 65535));
                    } return QualifiedNameUtils.ESCAPE_PROV_LOCAL_NAME; }
                    static UNESCAPE_PROV_LOCAL_NAME_$LI$() { if (QualifiedNameUtils.UNESCAPE_PROV_LOCAL_NAME == null) {
                        QualifiedNameUtils.UNESCAPE_PROV_LOCAL_NAME = new org.apache.commons.lang3.text.translate.AggregateTranslator(new org.apache.commons.lang3.text.translate.LookupTranslator([["\\=", "="], ["\\\'", "\'"], ["\\(", "("], ["\\)", ")"], ["\\,", ","], ["\\-", "-"], ["\\:", ":"], ["\\;", ";"], ["\\[", "["], ["\\]", "]"], ["\\.", "."], ["%3C", "<"], ["%3E", ">"], ["%3c", "<"], ["%3e", ">"], ["%5C", "\\"], ["%5c", "\\"]]), new org.apache.commons.lang3.text.translate.UnicodeUnescaper());
                    } return QualifiedNameUtils.UNESCAPE_PROV_LOCAL_NAME; }
                    static ESCAPE_UNICODE_$LI$() { if (QualifiedNameUtils.ESCAPE_UNICODE == null) {
                        QualifiedNameUtils.ESCAPE_UNICODE = new org.apache.commons.lang3.text.translate.AggregateTranslator(org.apache.commons.lang3.text.translate.UnicodeEscaper.outsideOf(32, 255));
                    } return QualifiedNameUtils.ESCAPE_UNICODE; }
                    escapeProvLocalName(localName) {
                        if ("-" === localName)
                            return "\\-";
                        const tmp = QualifiedNameUtils.ESCAPE_PROV_LOCAL_NAME_$LI$().translate(localName);
                        const len = tmp.length;
                        if (len > 0 && (c => c.charCodeAt == null ? c : c.charCodeAt(0))(tmp.charAt(len - 1)) == '.'.charCodeAt(0)) {
                            return (tmp.substring(0, len - 1)) + "\\.";
                        }
                        else if (len > 0 && (c => c.charCodeAt == null ? c : c.charCodeAt(0))(tmp.charAt(0)) == '.'.charCodeAt(0)) {
                            return ("\\." + tmp.substring(1));
                        }
                        else {
                            return tmp;
                        }
                    }
                    unescapeProvLocalName(localName) {
                        return QualifiedNameUtils.UNESCAPE_PROV_LOCAL_NAME_$LI$().translate(localName);
                    }
                    static ESCAPE_TO_XML_QNAME_LOCAL_NAME_$LI$() { if (QualifiedNameUtils.ESCAPE_TO_XML_QNAME_LOCAL_NAME == null) {
                        QualifiedNameUtils.ESCAPE_TO_XML_QNAME_LOCAL_NAME = new org.apache.commons.lang3.text.translate.AggregateTranslator(new org.apache.commons.lang3.text.translate.LookupTranslator([["=", QualifiedNameUtils.INNER_ESCAPE + "3D"], ["\'", QualifiedNameUtils.INNER_ESCAPE + "27"], ["(", QualifiedNameUtils.INNER_ESCAPE + "28"], [")", QualifiedNameUtils.INNER_ESCAPE + "29"], [",", QualifiedNameUtils.INNER_ESCAPE + "2C"], [QualifiedNameUtils.INNER_ESCAPE, QualifiedNameUtils.INNER_ESCAPE + QualifiedNameUtils.INNER_ESCAPE], [":", QualifiedNameUtils.INNER_ESCAPE + "3A"], [";", QualifiedNameUtils.INNER_ESCAPE + "3B"], ["[", QualifiedNameUtils.INNER_ESCAPE + "5B"], ["]", QualifiedNameUtils.INNER_ESCAPE + "5D"], ["/", QualifiedNameUtils.INNER_ESCAPE + "2F"], ["\\", QualifiedNameUtils.INNER_ESCAPE + "5C"], ["?", QualifiedNameUtils.INNER_ESCAPE + "3F"], ["@", QualifiedNameUtils.INNER_ESCAPE + "40"], ["~", QualifiedNameUtils.INNER_ESCAPE + "7E"], ["&", QualifiedNameUtils.INNER_ESCAPE + "26"], ["+", QualifiedNameUtils.INNER_ESCAPE + "2B"], ["*", QualifiedNameUtils.INNER_ESCAPE + "2A"], ["#", QualifiedNameUtils.INNER_ESCAPE + "23"], ["$", QualifiedNameUtils.INNER_ESCAPE + "24"], ["!", QualifiedNameUtils.INNER_ESCAPE + "21"], ["<", QualifiedNameUtils.INNER_ESCAPE + "3C"], [">", QualifiedNameUtils.INNER_ESCAPE + "3E"], ["%", QualifiedNameUtils.INNER_ESCAPE + "25"]]), org.apache.commons.lang3.text.translate.JavaUnicodeEscaper.outsideOf(32, 65535));
                    } return QualifiedNameUtils.ESCAPE_TO_XML_QNAME_LOCAL_NAME; }
                    static UNESCAPE_FROM_XML_QNAME_LOCAL_NAME_$LI$() { if (QualifiedNameUtils.UNESCAPE_FROM_XML_QNAME_LOCAL_NAME == null) {
                        QualifiedNameUtils.UNESCAPE_FROM_XML_QNAME_LOCAL_NAME = new org.apache.commons.lang3.text.translate.AggregateTranslator(new org.apache.commons.lang3.text.translate.LookupTranslator([[QualifiedNameUtils.INNER_ESCAPE + "3D", "="], [QualifiedNameUtils.INNER_ESCAPE + "27", "\'"], [QualifiedNameUtils.INNER_ESCAPE + "28", "("], [QualifiedNameUtils.INNER_ESCAPE + "29", ")"], [QualifiedNameUtils.INNER_ESCAPE + "2C", ","], [QualifiedNameUtils.INNER_ESCAPE + QualifiedNameUtils.INNER_ESCAPE, QualifiedNameUtils.INNER_ESCAPE], [QualifiedNameUtils.INNER_ESCAPE + "00", ""], [QualifiedNameUtils.INNER_ESCAPE + "3A", ":"], [QualifiedNameUtils.INNER_ESCAPE + "3B", ";"], [QualifiedNameUtils.INNER_ESCAPE + "5B", "["], [QualifiedNameUtils.INNER_ESCAPE + "5D", "]"], [QualifiedNameUtils.INNER_ESCAPE + "2F", "/"], [QualifiedNameUtils.INNER_ESCAPE + "5C", "\\"], [QualifiedNameUtils.INNER_ESCAPE + "3F", "?"], [QualifiedNameUtils.INNER_ESCAPE + "40", "@"], [QualifiedNameUtils.INNER_ESCAPE + "7E", "~"], [QualifiedNameUtils.INNER_ESCAPE + "26", "&"], [QualifiedNameUtils.INNER_ESCAPE + "2B", "+"], [QualifiedNameUtils.INNER_ESCAPE + "2A", "*"], [QualifiedNameUtils.INNER_ESCAPE + "23", "#"], [QualifiedNameUtils.INNER_ESCAPE + "24", "$"], [QualifiedNameUtils.INNER_ESCAPE + "21", "!"], [QualifiedNameUtils.INNER_ESCAPE + "3C", "<"], [QualifiedNameUtils.INNER_ESCAPE + "3E", ">"], [QualifiedNameUtils.INNER_ESCAPE + "25", "%"]]), new org.apache.commons.lang3.text.translate.UnicodeUnescaper());
                    } return QualifiedNameUtils.UNESCAPE_FROM_XML_QNAME_LOCAL_NAME; }
                    static UNESCAPE_UNICODE_$LI$() { if (QualifiedNameUtils.UNESCAPE_UNICODE == null) {
                        QualifiedNameUtils.UNESCAPE_UNICODE = new org.apache.commons.lang3.text.translate.AggregateTranslator(new org.apache.commons.lang3.text.translate.UnicodeUnescaper());
                    } return QualifiedNameUtils.UNESCAPE_UNICODE; }
                    static isNCNameStartCharToEscape(c) {
                        return /* isLowerCase */ (s => s.toLowerCase() === s)(c) || /* isUpperCase */ (s => s.toUpperCase() === s)(c);
                    }
                    escapeToXsdLocalName(localName) {
                        if ("" === localName)
                            return QualifiedNameUtils.START_ESCAPE;
                        const s = QualifiedNameUtils.ESCAPE_TO_XML_QNAME_LOCAL_NAME_$LI$().translate(localName);
                        if (!QualifiedNameUtils.isNCNameStartCharToEscape(s.charAt(0)))
                            return QualifiedNameUtils.START_ESCAPE + s;
                        return s;
                    }
                    unescapeFromXsdLocalName(localName) {
                        if (QualifiedNameUtils.START_ESCAPE === localName) {
                            return "";
                        }
                        if ((c => c.charCodeAt == null ? c : c.charCodeAt(0))(localName.charAt(0)) == (c => c.charCodeAt == null ? c : c.charCodeAt(0))(QualifiedNameUtils.START_ESCAPE.charAt(0))) {
                            localName = localName.substring(1);
                        }
                        const s = QualifiedNameUtils.UNESCAPE_FROM_XML_QNAME_LOCAL_NAME_$LI$().translate(localName);
                        return s;
                    }
                    escapeUnicode(name) {
                        return QualifiedNameUtils.ESCAPE_UNICODE_$LI$().translate(name);
                    }
                    unescapeUnicode(name) {
                        return QualifiedNameUtils.UNESCAPE_UNICODE_$LI$().translate(name);
                    }
                    static PN_CHARS_$LI$() { if (QualifiedNameUtils.PN_CHARS == null) {
                        QualifiedNameUtils.PN_CHARS = "(" + QualifiedNameUtils.PN_CHARS_U + "|[0-9\\-\\u00B7\\u0300-\\u036F\\u203F-\\u2040])";
                    } return QualifiedNameUtils.PN_CHARS; }
                    static PERCENT_$LI$() { if (QualifiedNameUtils.PERCENT == null) {
                        QualifiedNameUtils.PERCENT = "(%(" + QualifiedNameUtils.HEX + ")(" + QualifiedNameUtils.HEX + "))";
                    } return QualifiedNameUtils.PERCENT; }
                    static PN_CHARS_OTHERS_$LI$() { if (QualifiedNameUtils.PN_CHARS_OTHERS == null) {
                        QualifiedNameUtils.PN_CHARS_OTHERS = "(([/@~&\\+\\*\\?#$!])|" + QualifiedNameUtils.PN_CHARS_ESC + "|" + QualifiedNameUtils.PERCENT_$LI$() + ")";
                    } return QualifiedNameUtils.PN_CHARS_OTHERS; }
                    static PN_LOCAL_$LI$() { if (QualifiedNameUtils.PN_LOCAL == null) {
                        QualifiedNameUtils.PN_LOCAL = "(((" + QualifiedNameUtils.PN_CHARS_U + ")|([0-9])|(" + QualifiedNameUtils.PN_CHARS_OTHERS_$LI$() + ")))((((" + QualifiedNameUtils.PN_CHARS_$LI$() + ")|(\\.)|(" + QualifiedNameUtils.PN_CHARS_OTHERS_$LI$() + ")))*(((" + QualifiedNameUtils.PN_CHARS_$LI$() + ")|(" + QualifiedNameUtils.PN_CHARS_OTHERS_$LI$() + "))))?";
                    } return QualifiedNameUtils.PN_LOCAL; }
                    static pat_$LI$() { if (QualifiedNameUtils.pat == null) {
                        QualifiedNameUtils.pat = java.util.regex.Pattern.compile(QualifiedNameUtils.PN_LOCAL_$LI$());
                    } return QualifiedNameUtils.pat; }
                    patternExactMatch(input) {
                        if ("" === input)
                            return true;
                        const match = QualifiedNameUtils.pat_$LI$().matcher(input);
                        if (match.find()) {
                            return match.start() === 0 && match.end() === input.length;
                        }
                        else {
                            return false;
                        }
                    }
                    static XML_NameChar_$LI$() { if (QualifiedNameUtils.XML_NameChar == null) {
                        QualifiedNameUtils.XML_NameChar = "(" + QualifiedNameUtils.XML_NameStartChar + "|[\\-\\.0-9\\u00B7\\u0300-\\u036F\\u203F-\\u2040])";
                    } return QualifiedNameUtils.XML_NameChar; }
                    static XML_Name_$LI$() { if (QualifiedNameUtils.XML_Name == null) {
                        QualifiedNameUtils.XML_Name = QualifiedNameUtils.XML_NameStartChar + "(" + QualifiedNameUtils.XML_NameChar_$LI$() + ")*";
                    } return QualifiedNameUtils.XML_Name; }
                    static NC_NameChar_$LI$() { if (QualifiedNameUtils.NC_NameChar == null) {
                        QualifiedNameUtils.NC_NameChar = QualifiedNameUtils.XML_NameChar_$LI$();
                    } return QualifiedNameUtils.NC_NameChar; }
                    static NC_NameStartChar_$LI$() { if (QualifiedNameUtils.NC_NameStartChar == null) {
                        QualifiedNameUtils.NC_NameStartChar = QualifiedNameUtils.XML_NameStartChar;
                    } return QualifiedNameUtils.NC_NameStartChar; }
                    static NC_Name_$LI$() { if (QualifiedNameUtils.NC_Name == null) {
                        QualifiedNameUtils.NC_Name = QualifiedNameUtils.NC_NameStartChar_$LI$() + "(" + QualifiedNameUtils.NC_NameChar_$LI$() + ")*";
                    } return QualifiedNameUtils.NC_Name; }
                    static NC_pat_$LI$() { if (QualifiedNameUtils.NC_pat == null) {
                        QualifiedNameUtils.NC_pat = java.util.regex.Pattern.compile(QualifiedNameUtils.NC_Name_$LI$());
                    } return QualifiedNameUtils.NC_pat; }
                    is_NC_Name(input) {
                        if ("" === input)
                            return false;
                        const match = QualifiedNameUtils.NC_pat_$LI$().matcher(input);
                        if (match.matches()) {
                            return match.start() === 0 && match.end() === input.length;
                        }
                        else {
                            return false;
                        }
                    }
                }
                QualifiedNameUtils.INNER_ESCAPE = "_";
                QualifiedNameUtils.START_ESCAPE = "_";
                QualifiedNameUtils.PN_CHARS_U = "[A-Za-z_\\u00C0-\\u00D6\\u00D8-\\u00F6\\u00F8-\\u02FF\\u0370-\\u037D\\u037F-\\u1FFF\\u200C-\\u200D\\u2070-\\u218F\\u2C00-\\u2FEF\\u3001-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFFD]";
                QualifiedNameUtils.PN_CHARS_ESC = "((\\\\)([\\=\\\'\\(\\)\\,\\-\\:\\;\\[\\]\\.]))";
                QualifiedNameUtils.HEX = "[0-9A-Fa-f]";
                QualifiedNameUtils.XML_NameStartChar = "[A-Za-z_\\u00C0-\\u00D6\\u00D8-\\u00F6\\u00F8-\\u02FF\\u0370-\\u037D\\u037F-\\u1FFF\\u200C-\\u200D\\u2070-\\u218F\\u2C00-\\u2FEF\\u3001-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFFD]";
                model.QualifiedNameUtils = QualifiedNameUtils;
                QualifiedNameUtils["__class"] = "org.openprovenance.prov.model.QualifiedNameUtils";
            })(model = prov.model || (prov.model = {}));
        })(prov = openprovenance.prov || (openprovenance.prov = {}));
    })(openprovenance = org.openprovenance || (org.openprovenance = {}));
})(org || (org = {}));
