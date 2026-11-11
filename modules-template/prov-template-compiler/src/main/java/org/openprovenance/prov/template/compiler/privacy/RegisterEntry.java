package org.openprovenance.prov.template.compiler.privacy;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

/** T31 (R12): one row of the privacy register, the audit record of a catalogue's data-protection annotations. */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"template", "variable", "declared", "effective", "decidedBy", "reason", "definedBy", "digest",
        "keeps", "pattern", "resolver", "note"})
public class RegisterEntry {
    public String template;
    public String variable;
    /** As the library's bindings schema declares it, e.g. {@code @pseudonym(pt:ext-ref)}; null when none. */
    public String declared;
    /** In effect, e.g. {@code @pseudonym(acme:staff-no)}, or {@code ignored}. */
    public String effective;
    /** {@code library} (honoured as declared) or the policy scope that decided, e.g. {@code variable:t31.Approval#clerk}. */
    public String decidedBy;
    public String reason;
    /** Where the method's definition came from: {@code built-in}, {@code policy} or {@code class:<name>}. */
    public String definedBy;
    /** SHA-256 of the method's resolved definition, as in the database constraint's comment and the code's constant. */
    public String digest;
    public List<String> keeps;
    public String pattern;
    public String resolver;
    public String note;
}
