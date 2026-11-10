package org.openprovenance.prov.template.compiler.privacy;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

/**
 * T31 (R4): a data-protection method, as data. ProvToolbox's built-ins ({@code privacy/methods.json}) and an
 * application's own methods (its policy file's {@code methods}) use this format.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MethodDefinition {
    /** {@code transient} or {@code pseudonym}. */
    public String kind;
    /** Transient: the places the value is kept in, among {@code store}, {@code log}, {@code expansion}. */
    public List<String> keeps;
    /** Pseudonym: the form a valid value takes (POSIX ERE subset read alike by Java, Python, JavaScript, Rust, PostgreSQL). */
    public String pattern;
    /** Pseudonym, optional: a boolean SQL expression over {@code v}, replacing the pattern test in the database. */
    public String sql;
    /** Pseudonym, optional (R17): the application's own PAST method {package, class, method} for the client check. */
    public Map<String, String> past;
    /** With {@code past}: the languages for which the application provides that method. */
    public List<String> targets;
    /** The system readers resolve a value against. */
    public String resolver;
    public String documentation;
    /** A {@link PrivacyMethod} class, run by the compiler, that supplies the definition. */
    @JsonProperty("class")
    public String className;
}
