package org.openprovenance.prov.utils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * T36: JSweet candy of {@code org.openprovenance.prov.utils.JsonText} (prov-model), so that the generated client writes and
 * reads the JSON text of an {@code @inJson} carrier in JavaScript as it does in Java. Both must give the same text for the
 * same values: the conformance vectors of the compiler's tests ({@code t36/json-text-vectors.json}) are run against each.
 * <p>
 * This is the JavaScript implementation: JavaScript has one number type, so a number is written as the language writes it,
 * which is the rule of RFC 8785 ({@code "" + d}); it is not meant to run on a JVM, where prov-model's class is used. Written
 * with what JSweet translates: no {@code BigDecimal}, no {@code String.format}, no exceptions from number parsing. An object is a
 * JavaScript object, so a key that reads as an array index (an integer) is written before the others, whatever its place.
 */
public class JsonText {

    private static final String HEX = "0123456789abcdef";

    // ---- writing -----------------------------------------------------------------------------------------------------

    public static Map<String, Object> newObject() {
        return new LinkedHashMap<String, Object>();
    }

    public static String write(Map<String, Object> map) {
        if (map == null) throw new IllegalArgumentException("T36: JSON text of a null map");
        return toText(map);
    }

    public static String toText(Object value) {
        StringBuilder sb = new StringBuilder();
        writeValue(sb, value);
        return sb.toString();
    }

    /** The text of a number or a boolean as the carrier writes it, a string as itself, null as null (T36 part E: the conversion "as text" of a flow). */
    public static String text(Object value) {
        if (value == null) return null;
        if (value instanceof String) return (String) value;
        if (value instanceof Number || value instanceof Boolean) return toText(value);
        throw new IllegalArgumentException("T36: a value of that kind has no text");
    }

    @SuppressWarnings("unchecked")
    private static void writeValue(StringBuilder sb, Object v) {
        if (v == null) {
            sb.append("null");
        } else if (v instanceof String) {
            writeString(sb, (String) v);
        } else if (v instanceof Boolean) {
            sb.append(((Boolean) v).booleanValue() ? "true" : "false");
        } else if (v instanceof Number) {
            double d = ((Number) v).doubleValue();
            if (d != d || d == Double.POSITIVE_INFINITY || d == Double.NEGATIVE_INFINITY) {
                throw new IllegalArgumentException("T36: not a JSON number: " + d);
            }
            sb.append("" + d);   // JavaScript's Number to string: RFC 8785
        } else if (v instanceof List) {
            // a list before a map: in JavaScript an array is an Object too
            sb.append('[');
            boolean first = true;
            List<Object> l = (List<Object>) v;
            for (int i = 0; i < l.size(); i++) {
                if (!first) sb.append(',');
                first = false;
                writeValue(sb, l.get(i));
            }
            sb.append(']');
        } else if (v instanceof Map) {
            // Map<String, Object>, not Map<Object, Object>: JSweet keeps a string-keyed map as the properties of an object
            sb.append('{');
            boolean first = true;
            Map<String, Object> m = (Map<String, Object>) v;
            for (String k : m.keySet()) {
                if (!first) sb.append(',');
                first = false;
                writeString(sb, k);
                sb.append(':');
                writeValue(sb, m.get(k));
            }
            sb.append('}');
        } else {
            throw new IllegalArgumentException("T36: not a JSON value");
        }
    }

    private static void writeString(StringBuilder sb, String s) {
        sb.append('"');
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '"') {
                sb.append("\\\"");
            } else if (c == '\\') {
                sb.append("\\\\");
            } else if (c == '\b') {
                sb.append("\\b");
            } else if (c == '\f') {
                sb.append("\\f");
            } else if (c == '\n') {
                sb.append("\\n");
            } else if (c == '\r') {
                sb.append("\\r");
            } else if (c == '\t') {
                sb.append("\\t");
            } else if (c < 0x20) {
                sb.append("\\u00").append(HEX.charAt((c >> 4) & 15)).append(HEX.charAt(c & 15));
            } else {
                sb.append(c);
            }
        }
        sb.append('"');
    }

    // ---- reading -----------------------------------------------------------------------------------------------------

    @SuppressWarnings("unchecked")
    public static Map<String, Object> read(String text) {
        if (text == null || text.trim().length() == 0) return newObject();
        Object v = parse(text);
        if (v instanceof List || !(v instanceof Map)) throw new IllegalArgumentException("T36: JSON text is not an object");
        return (Map<String, Object>) v;
    }

    public static Object parse(String text) {
        Parser p = new Parser(text);
        p.ws();
        Object v = p.value();
        p.ws();
        if (p.i != text.length()) throw p.error("trailing characters");
        return v;
    }

    private static class Parser {
        final String s;
        int i = 0;

        Parser(String s) {
            this.s = s;
        }

        IllegalArgumentException error(String why) {
            return new IllegalArgumentException("T36: JSON text, offset " + i + ": " + why);
        }

        void ws() {
            while (i < s.length() && (s.charAt(i) == ' ' || s.charAt(i) == '\t' || s.charAt(i) == '\n' || s.charAt(i) == '\r')) i++;
        }

        Object value() {
            if (i >= s.length()) throw error("unexpected end");
            char c = s.charAt(i);
            if (c == '{') return object();
            if (c == '[') return array();
            if (c == '"') return string();
            if (c == 't') {
                literal("true");
                return Boolean.TRUE;
            }
            if (c == 'f') {
                literal("false");
                return Boolean.FALSE;
            }
            if (c == 'n') {
                literal("null");
                return null;
            }
            if (c == '-' || (c >= '0' && c <= '9')) return number();
            throw error("unexpected character");
        }

        void literal(String word) {
            if (!s.startsWith(word, i)) throw error("expected " + word);
            i += word.length();
        }

        Map<String, Object> object() {
            Map<String, Object> m = newObject();
            i++;
            ws();
            if (i < s.length() && s.charAt(i) == '}') {
                i++;
                return m;
            }
            while (true) {
                ws();
                if (i >= s.length() || s.charAt(i) != '"') throw error("expected a key");
                String k = string();
                ws();
                if (i >= s.length() || s.charAt(i) != ':') throw error("expected ':'");
                i++;
                ws();
                m.put(k, value());
                ws();
                if (i >= s.length()) throw error("unexpected end");
                char c = s.charAt(i);
                i++;
                if (c == '}') return m;
                if (c != ',') throw error("expected ',' or '}'");
            }
        }

        List<Object> array() {
            List<Object> l = new ArrayList<Object>();
            i++;
            ws();
            if (i < s.length() && s.charAt(i) == ']') {
                i++;
                return l;
            }
            while (true) {
                ws();
                l.add(value());
                ws();
                if (i >= s.length()) throw error("unexpected end");
                char c = s.charAt(i);
                i++;
                if (c == ']') return l;
                if (c != ',') throw error("expected ',' or ']'");
            }
        }

        int hex(char c) {
            int h = HEX.indexOf(Character.toLowerCase(c));
            if (h < 0) throw error("bad \\u escape");
            return h;
        }

        String string() {
            StringBuilder sb = new StringBuilder();
            i++;
            while (true) {
                if (i >= s.length()) throw error("unterminated string");
                char c = s.charAt(i);
                i++;
                if (c == '"') return sb.toString();
                if (c < 0x20) throw error("control character in string");
                if (c != '\\') {
                    sb.append(c);
                    continue;
                }
                if (i >= s.length()) throw error("unterminated escape");
                char e = s.charAt(i);
                i++;
                if (e == '"') sb.append('"');
                else if (e == '\\') sb.append('\\');
                else if (e == '/') sb.append('/');
                else if (e == 'b') sb.append('\b');
                else if (e == 'f') sb.append('\f');
                else if (e == 'n') sb.append('\n');
                else if (e == 'r') sb.append('\r');
                else if (e == 't') sb.append('\t');
                else if (e == 'u') {
                    if (i + 4 > s.length()) throw error("short \\u escape");
                    int code = (hex(s.charAt(i)) << 12) | (hex(s.charAt(i + 1)) << 8) | (hex(s.charAt(i + 2)) << 4) | hex(s.charAt(i + 3));
                    sb.append((char) code);
                    i += 4;
                } else {
                    throw error("bad escape");
                }
            }
        }

        boolean digit() {
            return i < s.length() && s.charAt(i) >= '0' && s.charAt(i) <= '9';
        }

        Object number() {
            int start = i;
            if (s.charAt(i) == '-') i++;
            if (i >= s.length()) throw error("bad number");
            if (s.charAt(i) == '0') {
                i++;
            } else if (s.charAt(i) >= '1' && s.charAt(i) <= '9') {
                while (digit()) i++;
            } else {
                throw error("bad number");
            }
            if (i < s.length() && s.charAt(i) == '.') {
                i++;
                int d = i;
                while (digit()) i++;
                if (d == i) throw error("bad number");
            }
            if (i < s.length() && (s.charAt(i) == 'e' || s.charAt(i) == 'E')) {
                i++;
                if (i < s.length() && (s.charAt(i) == '+' || s.charAt(i) == '-')) i++;
                int d = i;
                while (digit()) i++;
                if (d == i) throw error("bad number");
            }
            return Double.valueOf(Double.parseDouble(s.substring(start, i)));
        }
    }

    // ---- what the typed beans use ------------------------------------------------------------------------------------

    public static Map<String, Object> merge(Map<String, Object> declared, Map<String, Object> others, String where) {
        Map<String, Object> m = newObject();
        for (String k : declared.keySet()) m.put(k, declared.get(k));
        if (others != null) {
            for (String k : others.keySet()) {
                if (declared.containsKey(k)) {
                    throw new IllegalArgumentException("T36: " + where + ": the key " + k + " is declared, it cannot also be among the other keys");
                }
                m.put(k, others.get(k));
            }
        }
        return m;
    }

    public static Map<String, Object> others(Map<String, Object> object, String[] declared) {
        Map<String, Object> m = newObject();
        for (String k : object.keySet()) {
            boolean isDeclared = false;
            for (int i = 0; i < declared.length; i++) if (declared[i].equals(k)) isDeclared = true;
            if (!isDeclared) m.put(k, object.get(k));
        }
        return m;
    }

    private static IllegalArgumentException notA(String key, String what) {
        return new IllegalArgumentException("T36: the member " + key + " is not " + what);
    }

    public static String getString(Map<String, Object> m, String key) {
        Object v = m.get(key);
        if (v == null) return null;
        if (!(v instanceof String)) throw notA(key, "a string");
        return (String) v;
    }

    public static String getJson(Map<String, Object> m, String key) {
        Object v = m.get(key);
        return v == null ? null : toText(v);
    }

    public static Boolean getBoolean(Map<String, Object> m, String key) {
        Object v = m.get(key);
        if (v == null) return null;
        if (!(v instanceof Boolean)) throw notA(key, "true or false");
        return (Boolean) v;
    }

    public static Long getLong(Map<String, Object> m, String key) {
        Object v = m.get(key);
        if (v == null) return null;
        if (!(v instanceof Number)) throw notA(key, "an integer");
        double d = ((Number) v).doubleValue();
        if (d != Math.floor(d) || Math.abs(d) >= 9.007199254740992E15) throw notA(key, "an integer");
        return Long.valueOf((long) d);
    }

    public static Integer getInteger(Map<String, Object> m, String key) {
        Long l = getLong(m, key);
        if (l == null) return null;
        if (l.longValue() < -2147483648L || l.longValue() > 2147483647L) throw notA(key, "an integer that fits an int");
        return Integer.valueOf((int) l.longValue());
    }

    public static Double getDouble(Map<String, Object> m, String key) {
        Object v = m.get(key);
        if (v == null) return null;
        if (!(v instanceof Number)) throw notA(key, "a number");
        return Double.valueOf(((Number) v).doubleValue());
    }

    public static Float getFloat(Map<String, Object> m, String key) {
        Double d = getDouble(m, key);
        return d == null ? null : Float.valueOf((float) d.doubleValue());
    }

    public static boolean same(Object actual, Object constant) {
        if (actual instanceof Number && constant instanceof Number) {
            return ((Number) actual).doubleValue() == ((Number) constant).doubleValue();
        }
        if (actual == null) return constant == null;
        return actual.equals(constant);
    }
}
