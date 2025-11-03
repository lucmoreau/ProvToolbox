package org.openprovenance.prov.template.compiler;

import com.squareup.javapoet.*;
import org.apache.commons.lang3.tuple.Triple;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.template.compiler.common.BeanDirection;
import org.openprovenance.prov.template.compiler.common.Constants;
import org.openprovenance.prov.template.compiler.configuration.*;
import org.openprovenance.prov.template.descriptors.TemplateBindingsSchema;

import javax.lang.model.element.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.openprovenance.prov.template.compiler.ConfigProcessor.typeT;
import static org.openprovenance.prov.template.compiler.ConfigProcessor.*;

public class CompilerBeanChecker {
    private final CompilerUtil compilerUtil;

    public CompilerBeanChecker(ProvFactory pFactory) {
        this.compilerUtil=new CompilerUtil(pFactory);
    }


    public SpecificationFile generateBeanChecker(TemplatesProjectConfiguration configs, Locations locations, BeanDirection direction, Map<String, Map<String, Triple<String, List<String>, TemplateBindingsSchema>>> variantTable, String fileName) {
        StackTraceElement stackTraceElement=compilerUtil.thisMethodAndLine();


        TypeSpec.Builder builder = compilerUtil.generateClassInit(fileName);

        String packageForBeanProcessor;
        if (direction==BeanDirection.COMMON) {
            packageForBeanProcessor=locations.getFilePackage(configs.name, Constants.BEAN_PROCESSOR);
            builder.addSuperinterface(ClassName.get(packageForBeanProcessor, Constants.BEAN_PROCESSOR));
        } else {
            packageForBeanProcessor=locations.getFilePackage(configs.name, INPUT_PROCESSOR);
            builder.addSuperinterface(ClassName.get(packageForBeanProcessor, INPUT_PROCESSOR));
        }

        MethodSpec.Builder mspec0 = MethodSpec.methodBuilder(Constants.NOT_NULL_METHOD)
                .addModifiers(Modifier.PRIVATE,Modifier.FINAL, Modifier.STATIC)
                .addParameter(ParameterSpec.builder(typeT,"object").build())
                .addParameter(ParameterSpec.builder(String.class,"field").build())
                .addParameter(ParameterSpec.builder(String.class,"template").build())
                .addTypeVariable(typeT)
                .returns(typeT);
        compilerUtil.specWithComment(mspec0);
        mspec0
                .beginControlFlow("if (object==null)")
                .addStatement("throw new $T(\"The object field \" + field + \" is null in template \" + template)", IllegalStateException.class)
                .nextControlFlow("else")
                .addStatement("return object")
                .endControlFlow();

        builder.addMethod(mspec0.build());


        for (TemplateCompilerConfig config : configs.templates) {
            String packageForBeans2=locations.getBeansPackage(config.name, direction);
            builder.addMethod(generateCheckerMethod(config.name, null, config, direction, packageForBeans2, null));
        }

        if (variantTable!=null) {
            variantTable.keySet().forEach(
                    templateName -> {
                        Map<String, Triple<String, List<String>, TemplateBindingsSchema>> allVariants = variantTable.get(templateName);
                        allVariants.keySet().forEach(
                                shared -> {
                                    Triple<String, List<String>, TemplateBindingsSchema> triple = allVariants.get(shared);
                                    String extension = triple.getLeft();
                                    List<String> sharing = triple.getMiddle();
                                    TemplateBindingsSchema tbs=triple.getRight();

                                    TemplateCompilerConfig config = Arrays.stream(configs.templates).filter(c -> Objects.equals(c.name, templateName)).findFirst().get();
                                    SimpleTemplateCompilerConfig sConfig = (SimpleTemplateCompilerConfig) config;
                                    SimpleTemplateCompilerConfig sConfig2 = sConfig.cloneAsInstanceInComposition(templateName + extension, null);

                                    builder.addMethod(generateCheckerMethod(templateName , extension, sConfig2, direction, packageForBeanProcessor, sharing));

                                }
                        );
                    }
            );
        }



        TypeSpec theLogger = builder.build();

        String myPackage=locations.getFilePackage(configs.name,fileName);

        JavaFile myfile = compilerUtil.specWithComment(theLogger, configs, myPackage, stackTraceElement);

        return new SpecificationFile(myfile, locations.convertToDirectory(myPackage), fileName+DOT_JAVA_EXTENSION, myPackage);
    }

    public MethodSpec generateCheckerMethod(String templateName, String extension, TemplateCompilerConfig config, BeanDirection direction, String packageForBeans, List<String> sharing) {
        final String beanNameClass = compilerUtil.beanNameClass(templateName, direction, extension);

        final ClassName className = ClassName.get(packageForBeans, beanNameClass);
        MethodSpec.Builder mspec = MethodSpec.methodBuilder(PROCESS_METHOD_NAME)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addParameter(ParameterSpec.builder(className,"bean").build())
                .returns(className);
        compilerUtil.specWithComment(mspec);


        if (config instanceof SimpleTemplateCompilerConfig) {
            TemplateBindingsSchema bindingsSchema = compilerUtil.getBindingsSchema((SimpleTemplateCompilerConfig) config);

            for (String key : descriptorUtils.fieldNames(bindingsSchema)) {
                if (descriptorUtils.isCompulsoryInput(key, bindingsSchema)) {
                    mspec.addStatement("$N(bean.$N,$S,$S)", NOT_NULL_METHOD, key, key,templateName);

                }
            }


            if (sharing != null) {
                sharing.forEach(shared -> {
                    mspec.addStatement("$N(bean.$N,$S,$S) /* shared */", NOT_NULL_METHOD, shared, shared, templateName);

                });
            }
        } else {
            mspec.addStatement("bean.$N.forEach(el -> $N(el));", ELEMENTS, PROCESS_METHOD_NAME);
        }
        mspec.addStatement("return bean");

        return (mspec.build());
    }

}