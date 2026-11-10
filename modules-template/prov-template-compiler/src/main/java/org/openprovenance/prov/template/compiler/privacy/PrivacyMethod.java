package org.openprovenance.prov.template.compiler.privacy;

/**
 * T31 (R4): a data-protection method that can't be stated as data. Found by {@link java.util.ServiceLoader} on the
 * compiler's classpath, or named by an application's policy ({@code "class": "..."}).
 * <p>
 * It runs inside the compiler only, when the code is generated: the definition it returns is written into the
 * generated artefacts, and the class is never called while the application runs (T31 Ruling 1).
 */
public interface PrivacyMethod {
    /** The qualified method name, e.g. {@code acme:staff-no}. Ignored when the policy names the class. */
    String name();

    /** The method's definition, fixed from then on. */
    MethodDefinition definition();
}
