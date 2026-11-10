package org.openprovenance.prov.utils;

import def.js.RegExp;

/**
 * T31 (R16): JSweet candy of {@code org.openprovenance.prov.utils.Pseudonyms} (prov-model), so that the
 * generated client checks a pseudonymised variable in JavaScript as it does in Java. Patterns are anchored
 * ({@code ^...$}), so {@code RegExp.test} matches the whole value, as Java's {@code matches()} does; the {@code u} flag
 * reads non-ASCII characters as code points.
 */
public class Pseudonyms {

    public static boolean matches(String value, String pattern) {
        return value == null || new RegExp(pattern, "u").test(value);
    }

    public static void check(String value, String pattern, String template, String variable, String method) {
        if (!matches(value, pattern)) {
            throw new IllegalArgumentException("T31: template " + template + ", variable " + variable
                    + ": value refused, not in the form of @pseudonym(" + method + ")");
        }
    }
}
