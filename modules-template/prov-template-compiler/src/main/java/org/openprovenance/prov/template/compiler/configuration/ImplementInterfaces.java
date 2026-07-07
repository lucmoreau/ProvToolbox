package org.openprovenance.prov.template.compiler.configuration;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

/**
 * Fully qualified names of interfaces the generated beans must implement,
 * per bean direction: {@code plain} for the full bean, {@code input} for the
 * {@code *Inputs} bean, {@code output} for the {@code *Outputs} bean.
 * Each slot accepts a single string or an array of strings.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ImplementInterfaces {
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    public List<String> plain;
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    public List<String> input;
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    public List<String> output;
    /**
     * When true, the compiler also generates the declared interfaces into the generated
     * source tree, deriving their accessor methods from the template's {@code @role}
     * declarations (per bean direction). When false the interfaces are assumed to
     * pre-exist on the classpath.
     */
    public boolean generate;

    @Override
    public String toString() {
        return "ImplementInterfaces{" +
                "plain=" + plain +
                ", input=" + input +
                ", output=" + output +
                '}';
    }
}
