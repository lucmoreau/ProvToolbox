
package org.openprovenance.apache.commons.lang;

public class StringEscapeUtils {
    public static String escapeJavaScript(String format) { return format; }

    public static String escapeCsv(String format) {
        return "\"" + format + "\"";  // TODO: Temporary HACK, should do full escaping of double quotes
    }
}