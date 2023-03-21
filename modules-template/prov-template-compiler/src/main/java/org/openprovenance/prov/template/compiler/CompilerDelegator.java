package org.openprovenance.prov.template.compiler;

import com.squareup.javapoet.*;
import org.openprovenance.prov.template.compiler.common.BeanDirection;
import org.openprovenance.prov.template.compiler.common.Constants;
import org.openprovenance.prov.template.compiler.configuration.Locations;
import org.openprovenance.prov.template.compiler.configuration.SpecificationFile;
import org.openprovenance.prov.template.compiler.configuration.TemplateCompilerConfig;
import org.openprovenance.prov.template.compiler.configuration.TemplatesCompilerConfig;

import javax.lang.model.element.Modifier;

import static org.openprovenance.prov.template.compiler.common.Constants.*;

public class CompilerDelegator {
    private final CompilerUtil compilerUtil=new CompilerUtil();


    public CompilerDelegator() {
    }


    public SpecificationFile generateDelegator(TemplatesCompilerConfig configs, Locations locations, String fileName) {
        StackTraceElement stackTraceElement=compilerUtil.thisMethodAndLine();


        TypeSpec.Builder builder = compilerUtil.generateClassInit(Constants.DELEGATOR);

        ClassName beanProcessorClass = ClassName.get(locations.getFilePackage(configs.beanProcessor), configs.beanProcessor);
        builder.addSuperinterface(beanProcessorClass);

        builder.addField(beanProcessorClass, DELEGATOR_VAR, Modifier.FINAL, Modifier.PRIVATE);

        MethodSpec.Builder mspec2 = MethodSpec
                .constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(beanProcessorClass, DELEGATOR_VAR);
        compilerUtil.specWithComment(mspec2);
        mspec2.addStatement("this.$N=$N", DELEGATOR_VAR, DELEGATOR_VAR);

        builder.addMethod(mspec2.build());


        for (TemplateCompilerConfig config : configs.templates) {

            final String beanNameClass = compilerUtil.commonNameClass(config.name);
            locations.updateWithConfig(config);
            final ClassName className = ClassName.get(locations.getFilePackage(BeanDirection.COMMON), beanNameClass);
            MethodSpec.Builder mspec = MethodSpec.methodBuilder(Constants.PROCESS_METHOD_NAME)
                    .addModifiers(Modifier.PUBLIC)  // this one is not final!
                    .addParameter(ParameterSpec.builder(className,BEAN_VAR).build())
                    .returns(className);
            compilerUtil.specWithComment(mspec);
            mspec.addStatement("return $N.process($N)", DELEGATOR_VAR, BEAN_VAR);
            builder.addMethod(mspec.build());
        }

        TypeSpec theLogger = builder.build();

        String myPackage=locations.getFilePackage(fileName);

        JavaFile myfile = compilerUtil.specWithComment(theLogger, configs, myPackage, stackTraceElement);

        return new SpecificationFile(myfile, locations.convertToDirectory(myPackage), fileName+DOT_JAVA_EXTENSION, myPackage);

    }

}