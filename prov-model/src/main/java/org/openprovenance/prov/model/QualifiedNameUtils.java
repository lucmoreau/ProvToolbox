package org.openprovenance.prov.model;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.text.translate.AggregateTranslator;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.apache.commons.lang3.text.translate.JavaUnicodeEscaper;
import org.apache.commons.lang3.text.translate.LookupTranslator;
import org.apache.commons.lang3.text.translate.UnicodeEscaper;
import org.apache.commons.lang3.text.translate.UnicodeUnescaper;

public class QualifiedNameUtils {

    public static final CharSequenceTranslator ESCAPE_PROV_LOCAL_NAME = 
	    new AggregateTranslator(
	                            new LookupTranslator(
	                                                 new String[][] { 
	                                                	 {"=", "\\="},
	                                                	 {"'", "\\'"},
	                                                	 {"(", "\\("},
	                                                	 {")", "\\)"},
	                                                	 {",", "\\,"},
	                                                	// {"-", "\\-"}, Should not be escaped since - is accepted by PN_CHARS
	                                                	 {":", "\\:"},
	                                                	 {";", "\\;"},
	                                                	 {"[", "\\["},
	                                                	 {"]", "\\]"},
	                                                	 {".", "\\."},
	                                                	// {"%", "\\%"}, // This is not in PROV-N but is required for <percent> production
	                                                	// {"<", "%3C"},
	                                                	// {">", "%3E"},
	                                                 }),
	                                                 //new LookupTranslator(EntityArrays.JAVA_CTRL_CHARS_ESCAPE()),
	                                                 UnicodeEscaper.outsideOf(32, 0xFFFF) // 0x7f
		    );

    public static final CharSequenceTranslator UNESCAPE_PROV_LOCAL_NAME = 
	    new AggregateTranslator(
	                            new LookupTranslator(
	                                                 new String[][] { 
	                                                	 {"\\=", "="},
	                                                	 {"\\'", "'"},
	                                                	 {"\\(", "("},
	                                                	 {"\\)", ")"},
	                                                	 {"\\,", ","},
	                                                	 {"\\-", "-"}, // - is a PN_CHARS_ESC
	                                                	 {"\\:", ":"},
	                                                	 {"\\;", ";"},
	                                                	 {"\\[", "["},
	                                                	 {"\\]", "]"},
	                                                	 {"\\.", "."},
	                                                	// {"\\%", "%"},  // This is not in PROV-N but is required for <percent> production
	                                                	// {"%3C", "<"},
	                                                	// {"%3E", ">"},
	                                                 }),
	                                                 new UnicodeUnescaper()
		    );

    public static final CharSequenceTranslator ESCAPE_UNICODE = 
	    new AggregateTranslator(
	                            UnicodeEscaper.outsideOf(32, 0xFF) 
		    );


    
    public String escapeProvLocalName(String localName) {
	if ("-".equals(localName)) return "\\-";
	return ESCAPE_PROV_LOCAL_NAME.translate(localName);
    }

    public String unescapeProvLocalName(String localName) {
	return UNESCAPE_PROV_LOCAL_NAME.translate(localName);
    }

    public static final String INNER_ESCAPE="_";
    public static final String START_ESCAPE="_";
    
    public static final CharSequenceTranslator ESCAPE_TO_XML_QNAME_LOCAL_NAME = 
	    new AggregateTranslator(
	                            new LookupTranslator(
	                                                 new String[][] { 
	                                                	 {"=", INNER_ESCAPE + "3D"},
	                                                	 {"'", INNER_ESCAPE + "27"},
	                                                	 {"(", INNER_ESCAPE + "28"},
	                                                	 {")", INNER_ESCAPE + "29"},
	                                                	 {",", INNER_ESCAPE + "2C"},
	                                                	 {INNER_ESCAPE, INNER_ESCAPE + INNER_ESCAPE},
	                                                	 {":", INNER_ESCAPE + "3A"},
	                                                	 {";", INNER_ESCAPE + "3B"},
	                                                	 {"[", INNER_ESCAPE + "5B"},
	                                                	 {"]", INNER_ESCAPE + "5D"},
	                                                	 {"/", INNER_ESCAPE + "2F"},
	                                                	 {"\\", INNER_ESCAPE + "5C"},
	                                                	 {"?", INNER_ESCAPE + "3F"},
	                                                	 {"@", INNER_ESCAPE + "40"},
	                                                	 {"~", INNER_ESCAPE + "7E"},
	                                                	 {"&", INNER_ESCAPE + "26"},
	                                                	 {"+", INNER_ESCAPE + "2B"},
	                                                	 {"*", INNER_ESCAPE + "2A"},
	                                                	 {"#", INNER_ESCAPE + "23"},
	                                                	 {"$", INNER_ESCAPE + "24"},
	                                                	 {"!", INNER_ESCAPE + "21"},
	                                                 }),
	                                                 //StringEscapeUtils.ESCAPE_XML10
	                                                 JavaUnicodeEscaper.outsideOf(32, 0xFFFF) // 0x7f
		    );

    public static final CharSequenceTranslator UNESCAPE_FROM_XML_QNAME_LOCAL_NAME = 
	    new AggregateTranslator(
	                            new LookupTranslator(
	                                                 new String[][] { 
	                                                	 {INNER_ESCAPE + "3D", "="},
	                                                	 {INNER_ESCAPE + "27", "'"},
	                                                	 {INNER_ESCAPE + "28", "("},
	                                                	 {INNER_ESCAPE + "29", ")"},
	                                                	 {INNER_ESCAPE + "2C", ","},
	                                                	 {INNER_ESCAPE + INNER_ESCAPE, INNER_ESCAPE},
	                                                	 {INNER_ESCAPE + "00", ""},
	                                                	 {INNER_ESCAPE + "3A", ":"},
	                                                	 {INNER_ESCAPE + "3B", ";"},
	                                                	 {INNER_ESCAPE + "5B", "["},
	                                                	 {INNER_ESCAPE + "5D", "]"},
	                                                	 {INNER_ESCAPE + "2F", "/"},
	                                                	 {INNER_ESCAPE + "5C", "\\"},
	                                                	 {INNER_ESCAPE + "3F", "?"},
	                                                	 {INNER_ESCAPE + "40", "@"},
	                                                	 {INNER_ESCAPE + "7E", "~"},
	                                                	 {INNER_ESCAPE + "26", "&"},
	                                                	 {INNER_ESCAPE + "2B", "+"},
	                                                	 {INNER_ESCAPE + "2A", "*"},
	                                                	 {INNER_ESCAPE + "23", "#"},
	                                                	 {INNER_ESCAPE + "24", "$"},
	                                                	 {INNER_ESCAPE + "21", "!"},
	                                                 }),
	                                                 new UnicodeUnescaper()
);
    
    public static final CharSequenceTranslator UNESCAPE_UNICODE = 
	    new AggregateTranslator(new UnicodeUnescaper());
    
    public static final boolean isNCNameStartCharToEscape (char c) { // what about unicode?
	return Character.isLowerCase(c)||Character.isUpperCase(c);
    }

    public String escapeToXsdLocalName(String localName) {
	if ("".equals(localName)) return START_ESCAPE;
	String s=ESCAPE_TO_XML_QNAME_LOCAL_NAME.translate(localName);
	if (!isNCNameStartCharToEscape(s.charAt(0))) return START_ESCAPE + s;
	return s;
    }
    public String unescapeFromXsdLocalName(String localName) {
	if (START_ESCAPE.equals(localName)) {
	    return "";
	}
	if (localName.charAt(0)==START_ESCAPE.charAt(0)) {
	    localName=localName.substring(1);
	}
	String s=UNESCAPE_FROM_XML_QNAME_LOCAL_NAME.translate(localName);
	return s;
    }
    
    public String escapeUnicode(String name) {
	return ESCAPE_UNICODE.translate(name);
    }
    public String unescapeUnicode(String name) {
	return UNESCAPE_UNICODE.translate(name);
    }   
    
    static final String PN_CHARS_U="[A-Za-z_\\u00C0-\\u00D6\\u00D8-\\u00F6\\u00F8-\\u02FF\\u0370-\\u037D\\u037F-\\u1FFF\\u200C-\\u200D\\u2070-\\u218F\\u2C00-\\u2FEF\\u3001-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFFD]";  // [#x10000-#xEFFFF]
    static final String PN_CHARS="(" + PN_CHARS_U + "|[0-9\\-\\u00B7\\u0300-\\u036F\\u203F-\\u2040])"; 
    static final String PN_CHARS_ESC="((\\\\)([\\=\\'\\(\\)\\,\\-\\:\\;\\[\\]\\.]))";
    static final String HEX="[0-9A-Fa-f]";
    static final String PERCENT="(%(" + HEX + ")(" + HEX +"))"; 
    static final String PN_CHARS_OTHERS="(([/@~&\\+\\*\\?#$!])|" + PN_CHARS_ESC + "|" + PERCENT + ")";
    
    static final String PN_LOCAL= "(((" + PN_CHARS_U + ")|([0-9])|(" + PN_CHARS_OTHERS + ")))" 
	                       + "((((" + PN_CHARS + ")|(\\.)|(" + PN_CHARS_OTHERS + ")))*"
	                        + "(((" + PN_CHARS + ")|(" + PN_CHARS_OTHERS + "))))?";
    
    final Pattern pat=Pattern.compile(QualifiedNameUtils.PN_LOCAL);
    
    public boolean patternExactMatch (String input) {
	if ("".equals(input)) return true;
	Matcher match=pat.matcher(input);
	if (match.find()) {
	    //System.out.println("found " + input.substring(match.start(),match.end()));
	    return match.start()==0 && match.end()==input.length();
	} else {
	    return false;
	}
    }
    /*
    static final String XML_NameStartChar="[:A-Za-z_\\u00C0-\\u00D6\\u00D8-\\u00F6\\u00F8-\\u02FF\\u0370-\\u037D\\u037F-\\u1FFF\\u200C-\\u200D\\u2070-\\u218F\\u2C00-\\u2FEF\\u3001-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFFD]";  // [#x10000-#xEFFFF]
    static final String XML_NameChar="(" + XML_NameStartChar + "|[\\-\\.0-9\\u00B7\\u0300-\\u036F\\u203F-\\u2040])"; 
    static final String XML_Name=XML_NameStartChar + "(" + XML_NameChar + ")*";
    
    static final String NC_NameChar=XML_NameChar + "[^:]";
    static final String NC_NameStartChar=XML_NameStartChar + "[^:]";
    static final String NC_Name="("+NC_NameStartChar + "((" + NC_NameChar + ")*))";*/
    
    static final String XML_NameStartChar="[A-Za-z_\\u00C0-\\u00D6\\u00D8-\\u00F6\\u00F8-\\u02FF\\u0370-\\u037D\\u037F-\\u1FFF\\u200C-\\u200D\\u2070-\\u218F\\u2C00-\\u2FEF\\u3001-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFFD]";  // [#x10000-#xEFFFF]
    static final String XML_NameChar="(" + XML_NameStartChar + "|[\\-\\.0-9\\u00B7\\u0300-\\u036F\\u203F-\\u2040])"; 
    static final String XML_Name=XML_NameStartChar + "(" + XML_NameChar + ")*";
    
    static final String NC_NameChar=XML_NameChar;
    static final String NC_NameStartChar=XML_NameStartChar;
    static final String NC_Name=NC_NameStartChar + "(" + NC_NameChar + ")*";

    final Pattern NC_pat=Pattern.compile(QualifiedNameUtils.NC_Name);
    
    public boolean is_NC_Name (String input) {
	if ("".equals(input)) return false;
	Matcher match=NC_pat.matcher(input);
	if (match.matches()) {
	    //System.out.println("found " + input.substring(match.start(),match.end()));
	    return match.start()==0 && match.end()==input.length();
	} else {
	    return false;
	}
    }
    
}
