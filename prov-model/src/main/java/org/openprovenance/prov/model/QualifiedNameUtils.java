package org.openprovenance.prov.model;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.text.translate.AggregateTranslator;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.apache.commons.lang3.text.translate.EntityArrays;
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
	                                                	 {"-", "\\-"},
	                                                	 {":", "\\:"},
	                                                	 {";", "\\;"},
	                                                	 {"[", "\\["},
	                                                	 {"]", "\\]"},
	                                                	 {".", "\\."},
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
	                                                	 {"\\-", "-"},
	                                                	 {"\\:", ":"},
	                                                	 {"\\;", ";"},
	                                                	 {"\\[", "["},
	                                                	 {"\\]", "]"},
	                                                	 {"\\.", "."},
	                                                 }),
	                                                 new UnicodeUnescaper() 
		    );


    
    public String escapeProvLocalName(String localName) {
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
}
