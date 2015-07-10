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

}
