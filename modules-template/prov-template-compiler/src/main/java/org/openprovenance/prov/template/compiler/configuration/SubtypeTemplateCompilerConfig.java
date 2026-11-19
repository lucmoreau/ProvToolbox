package org.openprovenance.prov.template.compiler.configuration;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * T36 (R11, ruled: a third class): a <em>subtype</em> of a simple template. Its records are records of the template it
 * {@code refines}: same table, same insert function. It adds nothing to the server side, so every generator that walks
 * {@code configs.templates} skips it; only the subtype generators read it.
 * <p>
 * In the catalogue: {@code {"@type": "subtype", "name": …, "fullyQualifiedName": …, "package": …, "refines": …,
 * "fixed": {"atype": "ex:Repricing"}, "carries": {"unitPrice": "required", "note": "optional"}}}.
 * <p><b>Experimental:</b> in-JSON values, subtypes, performatives and flows (T36, T41) are experimental functionality: their declarations, the classes generated from them and this class may change without notice.
 */
public class SubtypeTemplateCompilerConfig extends ClientSideTemplateCompilerConfig {

    public static final String REQUIRED = "required";
    public static final String OPTIONAL = "optional";

    /** The fully qualified name of the simple template this is a subtype of. */
    public String refines;
    /** Inputs of that template given a constant for every record of the subtype, by variable name. */
    public Map<String, Object> fixed = new LinkedHashMap<>();
    /** The in-JSON values of that template this subtype has, each {@link #REQUIRED} or {@link #OPTIONAL}. */
    public Map<String, String> carries = new LinkedHashMap<>();

    @Override
    public String toString() {
        return "SubtypeTemplateCompilerConfig{" +
                "@type='" + type_ + '\'' +
                ", name='" + name + '\'' +
                ", fullyQualifiedName='" + fullyQualifiedName + '\'' +
                ", package_='" + package_ + '\'' +
                ", refines='" + refines + '\'' +
                ", fixed=" + fixed +
                ", carries=" + carries +
                '}';
    }
}
