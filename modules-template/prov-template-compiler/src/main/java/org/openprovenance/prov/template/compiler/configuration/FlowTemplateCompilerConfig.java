package org.openprovenance.prov.template.compiler.configuration;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * T36 part E (the flow, design §13; E1: "Entry in templates"): the Java pattern that connects the beans of a request to the
 * beans of the backend's record. It names the user subtype ({@code from}) it starts from and the backend subtype ({@code to}) it
 * ends in, and says, for each input of those beans, which of six kinds it is and where it comes from. Every input is accounted
 * for exactly once; {@link org.openprovenance.prov.template.compiler.FlowChecker} refuses a declaration that leaves one out or
 * names one twice. It adds nothing to the server side: a flow generates one client-side class and one interface of rules.
 * <p>
 * The members keep the JSON the catalogue gives: {@code FlowChecker} reads them into a model the generator and the descriptions share.
 */
public class FlowTemplateCompilerConfig extends ClientSideTemplateCompilerConfig {

    /** The fully qualified name of the user subtype the flow starts from. */
    public String from;
    /** The fully qualified name of the backend subtype the flow ends in. */
    public String to;
    /** Each thing that stood before, by a name of the flow's own: {@code node}, {@code madeBy}, optional {@code addressed}, {@code identity} ({@code user}, {@code backend}) and {@code of}. */
    public Map<String, Map<String, Object>> stood = new LinkedHashMap<>();
    /** The user bean's own values (not the request's, nor the reply's): each {@code supplied} or {@code constant}. */
    public Map<String, Object> ask = new LinkedHashMap<>();
    /** The inputs of the backend's bean that {@link #carry} does not cover, each with its kind. */
    public Map<String, Object> record = new LinkedHashMap<>();
    /** From which stood thing the remaining values come: an explicit list of names, or {@code "rest"}. */
    public Map<String, Object> carry = new LinkedHashMap<>();
    /** For each carrier of the backend's bean, the stood thing whose other keys are carried. */
    public Map<String, String> carryOpen = new LinkedHashMap<>();
    /** For {@code answer}, {@code refused} and {@code failed}: how each value of the reply class is given. */
    public Map<String, Map<String, Object>> reply = new LinkedHashMap<>();

    @Override
    public String toString() {
        return "FlowTemplateCompilerConfig{name='" + name + "', fullyQualifiedName='" + fullyQualifiedName + "', from='" + from + "', to='" + to + "'}";
    }
}
