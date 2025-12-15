package org.openprovenance.prov.template.compiler.expansion;

import com.fasterxml.jackson.databind.JsonNode;
import com.squareup.javapoet.*;
import org.openprovenance.prov.model.*;
import org.openprovenance.prov.template.compiler.common.CompilerCommon;
import org.openprovenance.prov.template.compiler.CompilerUtil;
import org.openprovenance.prov.template.compiler.configuration.Locations;
import org.openprovenance.prov.template.compiler.configuration.SpecificationFile;
import org.openprovenance.prov.template.compiler.configuration.TemplatesProjectConfiguration;
import org.openprovenance.prov.template.descriptors.Descriptor;
import org.openprovenance.prov.template.descriptors.TemplateBindingsSchema;

import javax.lang.model.element.Modifier;
import java.util.*;

import static org.openprovenance.prov.template.compiler.CompilerUtil.u;
import static org.openprovenance.prov.template.compiler.ConfigProcessor.descriptorUtils;

public class CompilerTypedRecord {
    private final CompilerUtil compilerUtil;
    private final ProvFactory pFactory;
    private final boolean withMain;
    private final CompilerCommon compilerCommon;
    private final boolean debugComment;


    public CompilerTypedRecord(boolean withMain, CompilerCommon compilerCommon, ProvFactory pFactory, boolean debugComment) {
        this.pFactory=pFactory;
        this.withMain=withMain;
        this.compilerCommon = compilerCommon;
        this.debugComment=debugComment;
        this.compilerUtil=new CompilerUtil(pFactory);
    }

    public SpecificationFile generatedTypedRecordConstructor(TemplatesProjectConfiguration configs, Locations locations, Document doc, String name, String templateName, String packge, String resource, JsonNode bindings_schema, TemplateBindingsSchema bindingsSchema, String directory, String fileName) {


        Bundle bun = u.getBundle(doc).get(0);

        Set<QualifiedName> allVars = new HashSet<>();
        Set<QualifiedName> allAtts = new HashSet<>();

        compilerUtil.extractVariablesAndAttributes(bun, allVars, allAtts, pFactory);

        return generateTypeDeclaration_aux(configs, locations, doc, allVars, allAtts, name, templateName, packge, resource, bindings_schema, bindingsSchema, directory, fileName);

    }



    public SpecificationFile generateTypeDeclaration_aux(TemplatesProjectConfiguration configs, Locations locations, Document doc, Set<QualifiedName> allVars, Set<QualifiedName> allAtts, String name, String templateName, String packge, String resource, JsonNode bindings_schema, TemplateBindingsSchema bindingsSchema, String directory, String fileName) {
        StackTraceElement stackTraceElement=compilerUtil.thisMethodAndLine();


        MethodSpec.Builder mbuilder = MethodSpec.methodBuilder("call")
                .addModifiers(Modifier.PUBLIC)
                .returns(TypeVariableName.get(Object[].class));

        compilerUtil.specWithComment(mbuilder);


        JsonNode the_var = bindings_schema.get("var");

        Map<String, List<Descriptor>> theVar=bindingsSchema.getVar();
        Collection<String> variables=descriptorUtils.fieldNames(bindingsSchema);

        compilerUtil.generateDocumentSpecializedParameters(mbuilder, theVar, variables);

        String allArgs= compilerUtil.generateArgumentsListForCall(the_var,null, bindingsSchema.getVar());



        mbuilder.addStatement("return new Object[] { $S , " + allArgs + "}", templateName );



        TypeSpec.Builder builder = compilerUtil.generateTypedRecordClass(name);

        final ParameterizedTypeName superinterface=ParameterizedTypeName.get(ClassName.get(packge,name + "Interface"),TypeName.get(Object[].class));
        builder.addSuperinterface(superinterface);


        builder.addMethod(mbuilder.build());

        TypeSpec bean = builder.build();

        JavaFile myfile = compilerUtil.specWithComment(bean, templateName, packge, stackTraceElement);

        return new SpecificationFile(myfile, directory, fileName, packge);
    }


}
