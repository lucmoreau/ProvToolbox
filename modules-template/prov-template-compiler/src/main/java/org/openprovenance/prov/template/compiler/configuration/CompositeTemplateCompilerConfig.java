package org.openprovenance.prov.template.compiler.configuration;

import java.util.List;

public class CompositeTemplateCompilerConfig extends TemplateCompilerConfig {

    public String consistsOf;
    public List<String> sharing;

    @Override
    public String toString() {
        return "CompositeTemplateCompilerConfig{" +
                "consistsOf='" + consistsOf + '\'' +
                ", sharing=" + sharing +
                ", type_='" + type_ + '\'' +
                ", name='" + name + '\'' +
                ", fullyQualifiedName='" + fullyQualifiedName + '\'' +
                ", package_='" + package_ + '\'' +
                '}';
    }


}
