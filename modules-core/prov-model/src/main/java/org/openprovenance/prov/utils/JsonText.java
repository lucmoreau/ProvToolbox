package org.openprovenance.prov.utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * T36: JSON text for the values a template keeps inside a text variable (an {@code @inJson} carrier), called by the typed
 * beans the template compiler generates for a template's subtypes. Its JavaScript (jsweet candy) and Python counterparts
 * must give the same text for the same values: see the vectors {@code json-text-vectors.json}, which every port runs.
 * <p>
 * The text a value gets (R5, ruled): an integer as a JSON integer; a double as RFC 8785 gives it (the ECMAScript
 * {@code Number} to string rule, so {@code 5.0} is written {@code 5} and {@code 1e21} is written {@code 1e+21}); a boolean
 * as {@code true} or {@code false}; a string as a JSON string, escaping only what RFC 8785 escapes; {@code null}; a nested
 * map or list as the JSON it is. No space is written between tokens. <b>The members of an object are written in the order
 * of the map</b>, not sorted as RFC 8785 sorts them: the typed beans write the declared keys first, in the order the
 * template declares them (R3, ruled).
 * <p>
 * Objects are read as {@link LinkedHashMap}, lists as {@link ArrayList}; an integer without fraction or exponent that fits a
 * {@code long} is read as a {@link Long}, any other number as a {@link Double}.
 * <p><b>Experimental:</b> in-JSON values, subtypes, performatives and flows (T36, T41) are experimental functionality: their declarations, the classes generated from them and this class may change without notice.
 */
public final class JsonText {

    private JsonText() {
    }

    /** A new empty object, members kept in the order they are put: what the generated code builds a carrier in. */
    public static Map<String, Object> newObject() {
        return new LinkedHashMap<>();
    }

    // ---- writing -----------------------------------------------------------------------------------------------------

    /** The text of an object, members in the map's order. */
    public static String write(Map<String, Object> map) {
        if (map == null) throw new IllegalArgumentException("T36: JSON text of a null map");
        StringBuilder sb = new StringBuilder();
        writeValue(sb, map);
        return sb.toString();
    }

    /** The text of any JSON value: null, a string, a number, a boolean, a map or a list of these. */
    public static String toText(Object value) {
        StringBuilder sb = new StringBuilder();
        writeValue(sb, value);
        return sb.toString();
    }

    /**
     * The text of a value, for the conversion "as text" of a flow (T36 part E): a number or a boolean as the carrier writes it
     * ({@code 5.0} is {@code 5}, {@code 2.50} is {@code 2.5}: a double has no trailing zeros), a string as itself, and {@code null} as
     * {@code null} (not the text {@code null}, which {@link #toText} writes). Anything else has no text.
     * <p>T42: a number is written in plain digits, never with an exponent: where {@link #toText} gives RFC 8785's {@code 1e+21} or
     * {@code 1e-7} (a magnitude from 1e21, or under 1e-6), this gives {@code 1000000000000000000000} or {@code 0.0000001}. Every other
     * number is the same text as {@link #toText} gives.
     */
    public static String text(Object value) {
        if (value == null) return null;
        if (value instanceof String) return (String) value;
        if (value instanceof Double || value instanceof Float) return plain(toText(value));
        if (value instanceof Number || value instanceof Boolean) return toText(value);
        throw new IllegalArgumentException("T36: a value of that kind has no text");
    }

    /**
     * T42: the text of a number without its exponent: the decimal point of RFC 8785's text ({@code 1.2345e+25}, {@code -5e-324}) moved by
     * the exponent. Text with no exponent is returned as it is. The same steps, on strings, as the other ports.
     */
    static String plain(String text) {
        int e = text.indexOf('e');
        if (e < 0) return text;
        String mantissa = text.substring(0, e);
        String exponent = text.substring(e + 1);
        int exp = Integer.parseInt(exponent.startsWith("+") ? exponent.substring(1) : exponent);
        boolean negative = mantissa.startsWith("-");
        if (negative) mantissa = mantissa.substring(1);
        int dot = mantissa.indexOf('.');
        String digits = dot < 0 ? mantissa : mantissa.substring(0, dot) + mantissa.substring(dot + 1);
        int point = (dot < 0 ? mantissa.length() : dot) + exp;      // where the decimal point falls in the digits
        StringBuilder sb = new StringBuilder();
        if (negative) sb.append('-');
        if (point >= digits.length()) {
            sb.append(digits);
            for (int i = digits.length(); i < point; i++) sb.append('0');
        } else if (point > 0) {
            sb.append(digits, 0, point).append('.').append(digits, point, digits.length());
        } else {
            sb.append("0.");
            for (int i = 0; i < -point; i++) sb.append('0');
            sb.append(digits);
        }
        return sb.toString();
    }

    private static void writeValue(StringBuilder sb, Object v) {
        if (v == null) {
            sb.append("null");
        } else if (v instanceof String) {
            writeString(sb, (String) v);
        } else if (v instanceof Boolean) {
            sb.append(((Boolean) v) ? "true" : "false");
        } else if (v instanceof Integer || v instanceof Long || v instanceof Short || v instanceof Byte) {
            sb.append(((Number) v).longValue());
        } else if (v instanceof Double) {
            sb.append(number((Double) v));
        } else if (v instanceof Float) {
            sb.append(number((Float) v));
        } else if (v instanceof Map) {
            sb.append('{');
            boolean first = true;
            for (Map.Entry<?, ?> e : ((Map<?, ?>) v).entrySet()) {
                if (!(e.getKey() instanceof String)) throw new IllegalArgumentException("T36: JSON object key is not a string: " + e.getKey());
                if (!first) sb.append(',');
                first = false;
                writeString(sb, (String) e.getKey());
                sb.append(':');
                writeValue(sb, e.getValue());
            }
            sb.append('}');
        } else if (v instanceof List) {
            sb.append('[');
            boolean first = true;
            for (Object o : (List<?>) v) {
                if (!first) sb.append(',');
                first = false;
                writeValue(sb, o);
            }
            sb.append(']');
        } else {
            throw new IllegalArgumentException("T36: not a JSON value: " + v.getClass().getName());
        }
    }

    private static void writeString(StringBuilder sb, String s) {
        sb.append('"');
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            switch (c) {
                case '"':  sb.append("\\\""); break;
                case '\\': sb.append("\\\\"); break;
                case '\b': sb.append("\\b"); break;
                case '\f': sb.append("\\f"); break;
                case '\n': sb.append("\\n"); break;
                case '\r': sb.append("\\r"); break;
                case '\t': sb.append("\\t"); break;
                default:
                    if (c < 0x20) {
                        sb.append("\\u00");
                        sb.append(Character.forDigit(c >> 4, 16)).append(Character.forDigit(c & 15, 16));
                    } else {
                        sb.append(c);
                    }
            }
        }
        sb.append('"');
    }

    /** RFC 8785 (ECMAScript Number::toString) for a double. */
    static String number(double d) {
        if (Double.isNaN(d) || Double.isInfinite(d)) throw new IllegalArgumentException("T36: not a JSON number: " + d);
        if (d == 0) return "0";
        double abs = Math.abs(d);
        return es(d < 0, oneDigit(abs, Double.toString(abs), x -> x.doubleValue() == abs));
    }

    /** The same rule for a float: the shortest digits that read back as that float. */
    static String number(float f) {
        if (Float.isNaN(f) || Float.isInfinite(f)) throw new IllegalArgumentException("T36: not a JSON number: " + f);
        if (f == 0) return "0";
        float abs = Math.abs(f);
        return es(f < 0, oneDigit(abs, Float.toString(abs), x -> x.floatValue() == abs));
    }

    /**
     * T42 (found with the differential test of the ports): Java's {@code toString} is never shorter than two digits, and when one
     * would do it takes the closest two-digit decimal, so the smallest subnormals read {@code 4.9E-324} where ECMAScript, RFC 8785 and the
     * other ports read {@code 5E-324}. When Java gives two digits, the one-digit decimals on either side of the exact value are tried;
     * of those that read back as the number, the closest to it (an exact tie to the even one) is the shortest text, as ECMAScript says.
     * Anything else is Java's text.
     */
    private static String oneDigit(double exact, String javaText, java.util.function.Predicate<BigDecimal> readsBack) {
        BigDecimal bd = new BigDecimal(javaText).stripTrailingZeros();
        if (bd.precision() != 2) return javaText;
        BigDecimal value = new BigDecimal(exact);
        BigDecimal best = null;
        BigDecimal bestDistance = null;
        for (java.math.RoundingMode mode : new java.math.RoundingMode[] { java.math.RoundingMode.FLOOR, java.math.RoundingMode.CEILING }) {
            BigDecimal candidate = value.round(new java.math.MathContext(1, mode));
            if (!readsBack.test(candidate)) continue;
            BigDecimal distance = candidate.subtract(value).abs();
            int c = best == null ? -1 : distance.compareTo(bestDistance);
            if (c < 0 || (c == 0 && candidate.unscaledValue().testBit(0) == false)) {   // closer, or an exact tie and this one is even
                best = candidate;
                bestDistance = distance;
            }
        }
        return best == null ? javaText : best.toString();
    }

    /** @param shortest Java's shortest decimal text of a positive number (JDK 19 on: the shortest that reads back). */
    private static String es(boolean negative, String shortest) {
        BigDecimal bd = new BigDecimal(shortest).stripTrailingZeros();
        String digits = bd.unscaledValue().toString();
        int k = digits.length();
        int n = k - bd.scale();                       // the value is 0.<digits> x 10^n
        StringBuilder sb = new StringBuilder();
        if (negative) sb.append('-');
        if (k <= n && n <= 21) {
            sb.append(digits);
            for (int i = 0; i < n - k; i++) sb.append('0');
        } else if (0 < n && n <= 21) {
            sb.append(digits, 0, n).append('.').append(digits, n, k);
        } else if (-6 < n && n <= 0) {
            sb.append("0.");
            for (int i = 0; i < -n; i++) sb.append('0');
            sb.append(digits);
        } else {
            int e = n - 1;
            sb.append(digits.charAt(0));
            if (k > 1) sb.append('.').append(digits, 1, k);
            sb.append('e').append(e < 0 ? '-' : '+').append(Math.abs(e));
        }
        return sb.toString();
    }

    // ---- reading -----------------------------------------------------------------------------------------------------

    /** The object a text holds. An absent text ({@code null} or blank) is an empty object; any other JSON value is refused. */
    public static Map<String, Object> read(String text) {
        if (text == null || text.trim().isEmpty()) return new LinkedHashMap<>();
        Object v = parse(text);
        if (!(v instanceof Map)) throw new IllegalArgumentException("T36: JSON text is not an object");
        @SuppressWarnings("unchecked") Map<String, Object> m = (Map<String, Object>) v;
        return m;
    }

    /** The value a text holds, whatever it is. */
    public static Object parse(String text) {
        Parser p = new Parser(text);
        p.ws();
        Object v = p.value();
        p.ws();
        if (p.i != text.length()) throw p.error("trailing characters");
        return v;
    }

    private static final class Parser {
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
            switch (c) {
                case '{': return object();
                case '[': return array();
                case '"': return string();
                case 't': literal("true"); return Boolean.TRUE;
                case 'f': literal("false"); return Boolean.FALSE;
                case 'n': literal("null"); return null;
                default:
                    if (c == '-' || (c >= '0' && c <= '9')) return number();
                    throw error("unexpected character '" + c + "'");
            }
        }

        void literal(String word) {
            if (!s.startsWith(word, i)) throw error("expected " + word);
            i += word.length();
        }

        Map<String, Object> object() {
            Map<String, Object> m = new LinkedHashMap<>();
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
                char c = s.charAt(i++);
                if (c == '}') return m;
                if (c != ',') throw error("expected ',' or '}'");
            }
        }

        List<Object> array() {
            List<Object> l = new ArrayList<>();
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
                char c = s.charAt(i++);
                if (c == ']') return l;
                if (c != ',') throw error("expected ',' or ']'");
            }
        }

        String string() {
            StringBuilder sb = new StringBuilder();
            i++;
            while (true) {
                if (i >= s.length()) throw error("unterminated string");
                char c = s.charAt(i++);
                if (c == '"') return sb.toString();
                if (c < 0x20) throw error("control character in string");
                if (c != '\\') {
                    sb.append(c);
                    continue;
                }
                if (i >= s.length()) throw error("unterminated escape");
                char e = s.charAt(i++);
                switch (e) {
                    case '"': sb.append('"'); break;
                    case '\\': sb.append('\\'); break;
                    case '/': sb.append('/'); break;
                    case 'b': sb.append('\b'); break;
                    case 'f': sb.append('\f'); break;
                    case 'n': sb.append('\n'); break;
                    case 'r': sb.append('\r'); break;
                    case 't': sb.append('\t'); break;
                    case 'u':
                        if (i + 4 > s.length()) throw error("short \\u escape");
                        try {
                            sb.append((char) Integer.parseInt(s.substring(i, i + 4), 16));
                        } catch (NumberFormatException x) {
                            throw error("bad \\u escape");
                        }
                        i += 4;
                        break;
                    default: throw error("bad escape \\" + e);
                }
            }
        }

        Object number() {
            int start = i;
            if (s.charAt(i) == '-') i++;
            if (i >= s.length()) throw error("bad number");
            if (s.charAt(i) == '0') {
                i++;
            } else if (s.charAt(i) >= '1' && s.charAt(i) <= '9') {
                while (i < s.length() && Character.isDigit(s.charAt(i))) i++;
            } else {
                throw error("bad number");
            }
            boolean integral = true;
            if (i < s.length() && s.charAt(i) == '.') {
                integral = false;
                i++;
                int d = i;
                while (i < s.length() && Character.isDigit(s.charAt(i))) i++;
                if (d == i) throw error("bad number");
            }
            if (i < s.length() && (s.charAt(i) == 'e' || s.charAt(i) == 'E')) {
                integral = false;
                i++;
                if (i < s.length() && (s.charAt(i) == '+' || s.charAt(i) == '-')) i++;
                int d = i;
                while (i < s.length() && Character.isDigit(s.charAt(i))) i++;
                if (d == i) throw error("bad number");
            }
            String text = s.substring(start, i);
            if (integral) {
                try {
                    return Long.parseLong(text);
                } catch (NumberFormatException x) {
                    // beyond a long: a double
                }
            }
            return Double.parseDouble(text);
        }
    }

    // ---- what the typed beans use ------------------------------------------------------------------------------------

    /**
     * The members of a carrier, the declared ones first and then the other keys, as one object to write. A key in
     * {@code others} that is also in {@code declared} is refused, so that a declared value is not shadowed unseen.
     *
     * @param where names the subtype and the carrier, for the message.
     */
    public static Map<String, Object> merge(Map<String, Object> declared, Map<String, Object> others, String where) {
        Map<String, Object> m = new LinkedHashMap<>(declared);
        if (others != null) {
            for (Map.Entry<String, Object> e : others.entrySet()) {
                if (declared.containsKey(e.getKey())) {
                    throw new IllegalArgumentException("T36: " + where + ": the key " + e.getKey()
                            + " is declared, it cannot also be among the other keys");
                }
                m.put(e.getKey(), e.getValue());
            }
        }
        return m;
    }

    /** The members of an object whose key is not one of the declared keys, in the object's order. */
    public static Map<String, Object> others(Map<String, Object> object, String[] declared) {
        Map<String, Object> m = new LinkedHashMap<>(object);
        for (String k : declared) m.remove(k);
        return m;
    }

    /** A string member; absent (or null) is null. */
    public static String getString(Map<String, Object> m, String key) {
        Object v = m.get(key);
        if (v == null) return null;
        if (!(v instanceof String)) throw notA(key, "a string", v);
        return (String) v;
    }

    /** A member read back as the JSON text of its value (for a {@code json} value); absent (or null) is null. */
    public static String getJson(Map<String, Object> m, String key) {
        Object v = m.get(key);
        return v == null ? null : toText(v);
    }

    public static Boolean getBoolean(Map<String, Object> m, String key) {
        Object v = m.get(key);
        if (v == null) return null;
        if (!(v instanceof Boolean)) throw notA(key, "true or false", v);
        return (Boolean) v;
    }

    public static Long getLong(Map<String, Object> m, String key) {
        Object v = m.get(key);
        if (v == null) return null;
        if (v instanceof Long) return (Long) v;
        if (v instanceof Double && ((Double) v) == Math.rint((Double) v) && Math.abs((Double) v) < 9.007199254740992E15) return ((Double) v).longValue();
        throw notA(key, "an integer", v);
    }

    public static Integer getInteger(Map<String, Object> m, String key) {
        Long l = getLong(m, key);
        if (l == null) return null;
        if (l < Integer.MIN_VALUE || l > Integer.MAX_VALUE) throw notA(key, "an integer that fits an int", l);
        return (int) (long) l;
    }

    public static Double getDouble(Map<String, Object> m, String key) {
        Object v = m.get(key);
        if (v == null) return null;
        if (!(v instanceof Number)) throw notA(key, "a number", v);
        return ((Number) v).doubleValue();
    }

    public static Float getFloat(Map<String, Object> m, String key) {
        Object v = m.get(key);
        if (v == null) return null;
        if (!(v instanceof Number)) throw notA(key, "a number", v);
        return ((Number) v).floatValue();
    }

    private static IllegalArgumentException notA(String key, String what, Object v) {
        return new IllegalArgumentException("T36: the member " + key + " is not " + what);
    }

    /** Whether a value is a constant: equal, numbers by value ({@code 3} as an int is {@code 3} as a long). */
    public static boolean same(Object actual, Object constant) {
        if (actual instanceof Number && constant instanceof Number) {
            try {
                return new BigDecimal(actual.toString()).compareTo(new BigDecimal(constant.toString())) == 0;
            } catch (NumberFormatException e) {
                return Objects.equals(actual, constant);   // NaN or an infinity
            }
        }
        return Objects.equals(actual, constant);
    }
}
