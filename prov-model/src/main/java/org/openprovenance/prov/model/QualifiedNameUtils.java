package org.openprovenance.prov.model;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.text.translate.AggregateTranslator;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.apache.commons.lang3.text.translate.JavaUnicodeEscaper;
import org.apache.commons.lang3.text.translate.LookupTranslator;
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
	                                                 JavaUnicodeEscaper.outsideOf(32, 0x7f) 
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
	                                                 JavaUnicodeEscaper.outsideOf(32, 0x7f) 
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
	                                                 JavaUnicodeEscaper.outsideOf(32, 0x7f) 
		    );
    
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
    
    static final String PN_CHARS_U="[A-Za-z_]";  //TODO: [#x00C0-#x00D6] | [#x00D8-#x00F6] | [#x00F8-#x02FF] | [#x0370-#x037D] | [#x037F-#x1FFF] | [#x200C-#x200D] | [#x2070-#x218F] | [#x2C00-#x2FEF] | [#x3001-#xD7FF] | [#xF900-#xFDCF] | [#xFDF0-#xFFFD] | [#x10000-#xEFFFF]
    static final String PN_CHARS="[A-Za-z_\\-0-9]"; //TODO: | #x00B7 | [#x0300-#x036F] | [#x203F-#x2040]
    static final String PN_CHARS_ESC="((\\\\)([\\=\\'\\(\\)\\,\\-\\:\\;\\[\\]\\.\\%]))";
    static final String HEX="[0-9A-Fa-f]";
    static final String PERCENT="(%(" + HEX + ")(" + HEX +"))"; 
    static final String PN_CHARS_OTHERS="(([/@~&\\+\\*\\?#$!])|" + PN_CHARS_ESC + "|" + PERCENT + ")"; // Percent
    
    static final String PN_LOCAL= "(((" + PN_CHARS_U + ")|([0-9])|(" + PN_CHARS_OTHERS + ")))" 
	                       + "((((" + PN_CHARS + ")|(\\.)|(" + PN_CHARS_OTHERS + ")))*"
	                        + "(((" + PN_CHARS + ")|(" + PN_CHARS_OTHERS + "))))?";
    
    final Pattern pat=Pattern.compile(QualifiedNameUtils.PN_LOCAL);
    
    public boolean patternExactMatch (String input) {
	if ("".equals(input)) return true;
	Matcher match=pat.matcher(input);
	if (match.find()) {
	    System.out.println("found " + input.substring(match.start(),match.end()));
	    return match.start()==0 && match.end()==input.length();
	} else {
	    return false;
	}
    }

}
