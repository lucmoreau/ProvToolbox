package org.openprovenance.prov.template.compiler;

import com.squareup.javapoet.*;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.template.compiler.common.BeanDirection;
import org.openprovenance.prov.template.compiler.common.Constants;
import org.openprovenance.prov.template.compiler.configuration.*;

import javax.lang.model.element.Modifier;

import static org.openprovenance.prov.template.compiler.CompilerUtil.listMapType;
import static org.openprovenance.prov.template.compiler.CompilerUtil.mapType;
import static org.openprovenance.prov.template.compiler.common.Constants.*;

public class CompilerBeanCompleter2Composite {
    public static final String OUT_VAR = "out";
    private final CompilerUtil compilerUtil;


    public CompilerBeanCompleter2Composite(ProvFactory pFactory) {
        this.compilerUtil=new CompilerUtil(pFactory);
    }


    SpecificationFile generateBeanCompleter2Composite(TemplatesProjectConfiguration configs, Locations locations, String fileName) {
        StackTraceElement stackTraceElement=compilerUtil.thisMethodAndLine();




        TypeSpec.Builder builder = compilerUtil.generateClassInit(Constants.COMPOSITE_BEAN_COMPLETER2);


        builder.addField(listMapType, LL_VAR, Modifier.FINAL);
        builder.addField(mapType, M_VAR, Modifier.FINAL);


        MethodSpec.Builder cbuilder2= MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(CompilerUtil.mapType, M_VAR);
        compilerUtil.specWithComment(cbuilder2);

        cbuilder2
                .addStatement("this.$N = ($T) $N.get($S)", LL_VAR, listMapType, M_VAR, ELEMENTS)
                .addStatement("this.$N = $N", M_VAR, M_VAR);


        builder.addMethod(cbuilder2.build());



        for (TemplateCompilerConfig config : configs.templates) {
            if (config instanceof SimpleTemplateCompilerConfig) continue;
            CompositeTemplateCompilerConfig config1=(CompositeTemplateCompilerConfig) config;
            String consistsOf=config1.consistsOf;



            final String outputBeanNameClass = compilerUtil.outputsNameClass(config.name);

            // find simple config name for consistsOf
            String shortName=locations.getShortNames().get(consistsOf);

            final ClassName outputClassName = ClassName.get(locations.getBeansPackage(config.fullyQualifiedName, BeanDirection.OUTPUTS), outputBeanNameClass);
            MethodSpec.Builder mspec = createProcessMethod(shortName, locations.getBeansPackage(config.fullyQualifiedName, BeanDirection.OUTPUTS), outputClassName, true);
            builder.addMethod(mspec.build());


        }


        TypeSpec theLogger = builder.build();

        String myPackage=locations.getFilePackage(configs.name, fileName);

        JavaFile myfile = compilerUtil.specWithComment(theLogger, configs, myPackage, stackTraceElement);

        return new SpecificationFile(myfile, locations.convertToDirectory(myPackage), fileName+DOT_JAVA_EXTENSION, myPackage);

    }

    /*
    static public TemplateCompilerConfig getSimpleConfig(TemplatesProjectConfiguration configs, String consistsOf) {
        return Arrays.stream(configs.templates)
                .filter(c -> Objects.equals(c.fullyQualifiedName, consistsOf))
                .findFirst().get();
    }

     */

    private MethodSpec.Builder createProcessMethod(String consistsOf, String integrator_package, ClassName cutputClassName, boolean isOutput) {
        MethodSpec.Builder mspec = MethodSpec.methodBuilder(Constants.PROCESS_METHOD_NAME)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(ParameterSpec.builder(cutputClassName, BEAN_VAR).build())
                .returns(cutputClassName);
        compilerUtil.specWithComment(mspec);

        mspec.addStatement("$N.$N=($T)$N.get($S)", BEAN_VAR, "ID", Integer.class, M_VAR, "ID");

        mspec.beginControlFlow("for ($T $N: $N)", mapType, M_VAR, LL_VAR);
        ClassName composee=ClassName.get(integrator_package,compilerUtil.outputsNameClass(consistsOf));

        mspec.addStatement("$T $N=new $T()", composee, OUT_VAR, composee);

        mspec.addStatement("$N.__addElements(new $N($N).process($N))", BEAN_VAR, BEAN_COMPLETER2, M_VAR, OUT_VAR);


        mspec.endControlFlow();

        mspec.addStatement("return $N", BEAN_VAR);
        return mspec;
    }


}