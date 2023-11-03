
package org.openprovenance.apache.commons.lang;

public class StringEscapeUtils {
    public static String escapeJavaScript(String format) { return format; }

    public static String escapeCsv(String format) {
        String newFormat = format.replace("\"", "\"\"");
        if (newFormat==format) {
            if (format.contains(",") || format.contains("\n") || format.contains("\r")) {
                return "\"" + format + "\"";
            } else {
                return format;
            }
        } else {
            return "\"" + newFormat + "\"";
        }
    }
}