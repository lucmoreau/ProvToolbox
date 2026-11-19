package org.openprovenance.prov.template.compiler.past.emitter;

import java.util.List;
import java.util.Map;

/**
 * The static calls to the classes of a runtime that Rust has its own port of (T46): {@code JsonText}, whose Rust port is
 * {@code src/main/resources/rs/json_text.rs}. The generic static call of the emitter writes {@code Class::method(args)} and nothing
 * else; these calls need their arguments borrowed ({@code &x}), their {@code Result} unwrapped (a panic, as a {@code throw} is
 * in the emitter) and, when the result is an {@code Option}, to be left as one.
 *
 * <p>Java passes a possibly null {@code String}, {@code Object} or {@code Map}; the port takes {@code &impl ToValue} or
 * {@code &impl AsText}, which both an {@code Option} field and a plain value are, so each argument is written {@code &x}.
 * <p><b>Experimental:</b> in-JSON values, subtypes, performatives and flows (T36, T41) are experimental functionality: their declarations, the classes generated from them and this class may change without notice.
 */
public final class RustRuntimeCalls {

    private RustRuntimeCalls() {}

    /** How one call is written. */
    record Spec(String rustName, String arguments, boolean unwrap, boolean resultIsOption) {
        /** True when argument {@code i} is borrowed ({@code r}); {@code v} is as the emitter writes it. The last letter repeats. */
        boolean borrowed(int i) {
            if (arguments.isEmpty()) return false;
            return arguments.charAt(Math.min(i, arguments.length() - 1)) == 'r';
        }
    }

    /** The classes: Java simple name to the path to import. */
    static final Map<String, String> CLASSES = Map.of("JsonText", "crate::json_text::JsonText");

    /** The files of the runtime (resources under /rs/), which a generated project holds at its source root. */
    public static final List<String> RUNTIME_FILES = List.of("json_text.rs", "json_text_serde.rs");

    private static final Map<String, Spec> JSON_TEXT = Map.ofEntries(
            Map.entry("newObject", new Spec("new_object", "", false, false)),
            Map.entry("parse", new Spec("parse", "r", true, false)),
            Map.entry("write", new Spec("write", "r", true, false)),
            Map.entry("toText", new Spec("to_text", "r", true, false)),
            Map.entry("text", new Spec("text", "r", true, true)),
            Map.entry("read", new Spec("read", "r", true, false)),
            Map.entry("merge", new Spec("merge", "rrv", true, false)),
            Map.entry("others", new Spec("others", "rr", false, false)),
            Map.entry("same", new Spec("same", "rr", false, false)),
            Map.entry("getString", new Spec("get_string", "rv", true, true)),
            Map.entry("getJson", new Spec("get_json", "rv", true, true)),
            Map.entry("getBoolean", new Spec("get_boolean", "rv", true, true)),
            Map.entry("getLong", new Spec("get_long", "rv", true, true)),
            Map.entry("getInteger", new Spec("get_integer", "rv", true, true)),
            Map.entry("getDouble", new Spec("get_double", "rv", true, true)),
            Map.entry("getFloat", new Spec("get_float", "rv", true, true)));

    /** The spec of a static call to {@code className.methodName}, or null when the class is not a runtime class or has no such call. */
    static Spec lookup(String className, String methodName) {
        if ("JsonText".equals(className)) return JSON_TEXT.get(methodName);
        return null;
    }
}
