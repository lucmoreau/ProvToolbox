package org.openprovenance.prov.template.compiler;

import com.squareup.javapoet.*;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.template.compiler.common.BeanDirection;
import org.openprovenance.prov.template.compiler.common.Constants;
import org.openprovenance.prov.template.compiler.configuration.Locations;
import org.openprovenance.prov.template.compiler.configuration.SpecificationFile;
import org.openprovenance.prov.template.compiler.configuration.TemplateCompilerConfig;
import org.openprovenance.prov.template.compiler.configuration.TemplatesProjectConfiguration;

import javax.lang.model.element.Modifier;

import static org.openprovenance.prov.template.compiler.common.Constants.*;

public class CompilerDelegator {
    private final CompilerUtil compilerUtil;


    public CompilerDelegator(ProvFactory pFactory) {
        this.compilerUtil=new CompilerUtil(pFactory);
    }


    public SpecificationFile generateDelegator(TemplatesProjectConfiguration configs, Locations locations, String fileName) {
        StackTraceElement stackTraceElement=compilerUtil.thisMethodAndLine();


        TypeSpec.Builder builder = compilerUtil.generateClassInit(Constants.DELEGATOR);

        ClassName beanProcessorClass = compilerUtil.getClass(BEAN_PROCESSOR, locations);
        builder.addSuperinterface(beanProcessorClass);

        builder.addJavadoc("Delegator for processing beans\n");

        builder.addField(beanProcessorClass, DELEGATOR_VAR, Modifier.FINAL, Modifier.PRIVATE);

        MethodSpec.Builder mspec2 = MethodSpec
                .constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(beanProcessorClass, DELEGATOR_VAR);

        CodeBlock.Builder jdoc = CodeBlock.builder();
        jdoc.add("Constructor for Delegator\n");
        jdoc.add("@param delegator a processor to which processing of beans is delegated\n");
        mspec2.addJavadoc(jdoc.build());

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
            CodeBlock.Builder jdoc2= CodeBlock.builder();
            jdoc2.add("Porcessing method\n");
            jdoc2.add("@param bean an input bean\n");
            jdoc2.add("@return a processed bean\n");
            mspec.addJavadoc(jdoc2.build());

            mspec.addStatement("return $N.process($N)", DELEGATOR_VAR, BEAN_VAR);
            builder.addMethod(mspec.build());
        }

        TypeSpec theLogger = builder.build();

        String myPackage=locations.getFilePackage(fileName);

        JavaFile myfile = compilerUtil.specWithComment(theLogger, configs, myPackage, stackTraceElement);

        return new SpecificationFile(myfile, locations.convertToDirectory(myPackage), fileName+DOT_JAVA_EXTENSION, myPackage);

    }

}