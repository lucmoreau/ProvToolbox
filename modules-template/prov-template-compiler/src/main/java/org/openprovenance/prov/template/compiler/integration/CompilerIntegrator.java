package org.openprovenance.prov.template.compiler.integration;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;
import org.openprovenance.prov.template.compiler.CompilerUtil;
import org.openprovenance.prov.template.compiler.common.CompilerCommon;
import org.openprovenance.prov.template.compiler.common.Constants;
import org.openprovenance.prov.template.descriptors.TemplateBindingsSchema;

public class CompilerIntegrator {
    private final CompilerCommon compilerCommon;
    private final CompilerUtil compilerUtil=new CompilerUtil();

    public CompilerIntegrator(CompilerCommon compilerCommon) {
        this.compilerCommon=compilerCommon;

    }

    public JavaFile generateIntegrator(String templateName, String packge, TemplateBindingsSchema bindingsSchema) {

        TypeSpec.Builder builder = compilerUtil.generateClassInit(compilerUtil.integratorBuilderNameClass(templateName));

        builder.addMethod(compilerCommon.generateProcessorConverter(templateName, packge, bindingsSchema, Constants.IN_INTEGRATOR));

        builder.addMethod(compilerCommon.generateNameAccessor(templateName));

        TypeSpec spec=builder.build();

        return compilerUtil.specWithComment(spec, templateName, packge, getClass().getName());
    }
}
