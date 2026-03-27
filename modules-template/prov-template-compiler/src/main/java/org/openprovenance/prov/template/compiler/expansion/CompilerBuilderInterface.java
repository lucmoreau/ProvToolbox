package org.openprovenance.prov.template.compiler.expansion;

import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.template.compiler.CompilerUtil;
import org.openprovenance.prov.template.compiler.configuration.Locations;
import org.openprovenance.prov.template.compiler.configuration.SpecificationFile;
import org.openprovenance.prov.template.compiler.configuration.TemplatesProjectConfiguration;
import org.openprovenance.prov.template.compiler.past.Class;
import org.openprovenance.prov.template.compiler.past.Method;
import org.openprovenance.prov.template.compiler.past.PastFactory;
import org.openprovenance.prov.template.descriptors.Descriptor;
import org.openprovenance.prov.template.descriptors.TemplateBindingsSchema;

import javax.lang.model.element.Modifier;
import java.util.*;
import java.util.function.Supplier;

import static org.openprovenance.prov.template.compiler.ConfigProcessor.descriptorUtils;
import static org.openprovenance.prov.template.compiler.configuration.SpecificationFile.generateJava;
import static org.openprovenance.prov.template.compiler.configuration.SpecificationFile.generatePython;
import static org.openprovenance.prov.template.compiler.past.Method.METHOD;
import static org.openprovenance.prov.template.compiler.past.type.TypeVariable.T;

public class CompilerBuilderInterface {

    private final CompilerUtil compilerUtil;
    private final PastFactory pastFactory;

    public CompilerBuilderInterface(ProvFactory pFactory) {
        this.compilerUtil=new CompilerUtil(pFactory);
        this.pastFactory=compilerUtil.getPastFactory();
    }

    public SpecificationFile generateBuilderInterfaceSpecification(TemplatesProjectConfiguration configs, Locations locations, String name, String templateName, String packge, TemplateBindingsSchema bindingsSchema, String directory, String fileName) {
        StackTraceElement stackTraceElement=compilerUtil.thisMethodAndLine();


        Map<String, List<Descriptor>> theVar = bindingsSchema.getVar();
        Collection<String> variables = descriptorUtils.fieldNames(bindingsSchema);

        Class pastClass = pastFactory
                .INTERFACE(name + "Interface")
                .MODIFIERS(Modifier.PUBLIC)
                .TYPE_VARIABLES(T());

        Method callMethod = METHOD("call")
                .MODIFIERS(Modifier.PUBLIC, Modifier.ABSTRACT)
                .RETURNS(T());
        compilerUtil.generateDocumentSpecializedParameters(callMethod, theVar, variables);

        pastClass.METHOD(callMethod);

        Supplier<Boolean> javaGenerator = () -> generateJava(pastClass, packge, templateName, directory, stackTraceElement, compilerUtil);
        Supplier<Boolean> pythonGenerator = () -> generatePython(pastClass, packge, locations, stackTraceElement);

        return new SpecificationFile(javaGenerator, pythonGenerator);
    }

}
