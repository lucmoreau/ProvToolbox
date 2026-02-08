package org.openprovenance.prov.template.compiler.expansion;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import org.openprovenance.prov.model.Bundle;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.model.QualifiedName;
import org.openprovenance.prov.template.compiler.CompilerUtil;
import org.openprovenance.prov.template.compiler.common.CompilerCommon;
import org.openprovenance.prov.template.compiler.configuration.Locations;
import org.openprovenance.prov.template.compiler.configuration.SpecificationFile;
import org.openprovenance.prov.template.compiler.configuration.TemplatesProjectConfiguration;
import org.openprovenance.prov.template.compiler.past.PastFactory;
import org.openprovenance.prov.template.descriptors.Descriptor;
import org.openprovenance.prov.template.descriptors.TemplateBindingsSchema;

import javax.lang.model.element.Modifier;
import java.util.*;

import static org.openprovenance.prov.template.compiler.CompilerUtil.typeT;
import static org.openprovenance.prov.template.compiler.CompilerUtil.u;
import static org.openprovenance.prov.template.compiler.ConfigProcessor.descriptorUtils;

public class CompilerBuilderInterface {

    private final ProvFactory pFactory;
    private final boolean withMain;
    private final CompilerCommon compilerCommon;
    private final CompilerTypeManagement compilerTypeManagement;
    private final CompilerUtil compilerUtil;
    private final PastFactory pastFactory;

    public CompilerBuilderInterface(boolean withMain, CompilerCommon compilerCommon, ProvFactory pFactory, boolean debugComment, CompilerTypeManagement compilerTypeManagement) {
        this.pFactory=pFactory;
        this.withMain=withMain;
        this.compilerCommon = compilerCommon;
        this.compilerTypeManagement=compilerTypeManagement;
        this.compilerUtil=new CompilerUtil(pFactory);
        this.pastFactory=compilerUtil.getPastFactory();
    }

    public SpecificationFile generateBuilderInterfaceSpecification(TemplatesProjectConfiguration configs, Locations locations, Document doc, String name, String templateName, String packge, TemplateBindingsSchema bindingsSchema, String directory, String fileName) {


        Bundle bun = u.getBundle(doc).get(0);

        Set<QualifiedName> allVars = new HashSet<>();
        Set<QualifiedName> allAtts = new HashSet<>();

        compilerUtil.extractVariablesAndAttributes(bun, allVars, allAtts, pFactory);

        return generateBuilderInterfaceSpecification_aux(configs, locations, name, templateName, packge, bindingsSchema, directory, fileName);

    }

    SpecificationFile generateBuilderInterfaceSpecification_aux(TemplatesProjectConfiguration configs, Locations locations, String name, String templateName, String packge, TemplateBindingsSchema bindingsSchema, String directory, String fileName) {
        StackTraceElement stackTraceElement=compilerUtil.thisMethodAndLine();

        TypeSpec.Builder builder = compilerUtil.generateInterfaceInitParameter(name+"Interface", "T");

        builder.addMethod(generateTemplateGeneratorInterface(bindingsSchema));

        TypeSpec theInterface = builder.build();

        JavaFile myfile = compilerUtil.specWithComment(theInterface, templateName, packge, stackTraceElement);

        return new SpecificationFile(myfile, directory, fileName, packge);
    }


    public MethodSpec generateTemplateGeneratorInterface(TemplateBindingsSchema bindingsSchema) {


        MethodSpec.Builder builder = MethodSpec.methodBuilder("call")
                .addModifiers(Modifier.PUBLIC)
                .addModifiers(Modifier.ABSTRACT)
                .returns(typeT);


        Map<String, List<Descriptor>> theVar=bindingsSchema.getVar();
        Collection<String> variables=descriptorUtils.fieldNames(bindingsSchema);
        compilerUtil.generateDocumentSpecializedParameters(builder, theVar, variables);

        return builder.build();
    }

}
