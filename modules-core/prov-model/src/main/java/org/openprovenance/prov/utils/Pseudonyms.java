package org.openprovenance.prov.utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

/**
 * T31 (R9, R16): the client check of a pseudonymised template variable, called by the code the template compiler
 * generates. Its JavaScript (jsweet candy), Python and Rust counterparts must give the same answers: see the conformance
 * vectors in the compiler's tests.
 * <p>
 * The pattern is passed by the generated code, where it is fixed at generation: this class only matches. Patterns are
 * anchored ({@code ^...$}) and within the subset Java, Python, JavaScript, Rust and PostgreSQL read alike.
 */
public class Pseudonyms {

    private static final Map<String, Pattern> compiled = new ConcurrentHashMap<>();

    private Pseudonyms() {
    }

    /** Whether a value is in the method's form. A null value (absent) is accepted. */
    public static boolean matches(String value, String pattern) {
        return value == null || compiled.computeIfAbsent(pattern, Pattern::compile).matcher(value).matches();
    }

    /**
     * Refuses a value not in the method's form. The message names the template, the variable and the method, never the
     * value: an error must not carry a clear value into a log.
     */
    public static void check(String value, String pattern, String template, String variable, String method) {
        if (!matches(value, pattern)) {
            throw new IllegalArgumentException("T31: template " + template + ", variable " + variable
                    + ": value refused, not in the form of @pseudonym(" + method + ")");
        }
    }
}
