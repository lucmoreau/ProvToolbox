package org.openprovenance.prov.template.compiler.configuration;


import java.util.Arrays;

public class CompositeTemplateCompilerConfig extends TemplateCompilerConfig {

    public String consistsOf;
    public String [] instances;

    @Override
    public String toString() {
        return "CompositeTemplateCompilerConfig{" +
                "consistsOf='" + consistsOf + '\'' +
                ", instances=" + Arrays.toString(instances) +
                ", type='" + type_ + '\'' +
                ", name='" + name + '\'' +
                ", package_='" + package_ + '\'' +
                '}';
    }


}
