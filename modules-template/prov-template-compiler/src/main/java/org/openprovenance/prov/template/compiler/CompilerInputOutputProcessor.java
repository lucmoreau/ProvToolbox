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

public class CompilerInputOutputProcessor {
    public enum ProcessorType {
        INPUT,
        OUTPUT,
        INPUT_OUTPUT
    }
    private final CompilerUtil compilerUtil;

    public CompilerInputOutputProcessor(ProvFactory pFactory) {
        this.compilerUtil=new CompilerUtil(pFactory);
    }


    SpecificationFile generateInputOutputProcessor(TemplatesProjectConfiguration configs, Locations locations, String package_, ProcessorType ioConverter, String directory, String fileName) {
        StackTraceElement stackTraceElement=compilerUtil.thisMethodAndLine();


        String interfaceName = switch (ioConverter) {
            case INPUT_OUTPUT -> INPUT_OUTPUT_PROCESSOR;
            case INPUT -> INPUT_PROCESSOR;
            case OUTPUT -> OUTPUT_PROCESSOR;
        };


        TypeSpec.Builder builder = compilerUtil.generateInterfaceInit(interfaceName);



        for (TemplateCompilerConfig config : configs.templates) {
            final String inputsNameClass = compilerUtil.inputsNameClass(config.name);
            final String outputsNameClass = compilerUtil.outputsNameClass(config.name);

            final ClassName inputClassName = ClassName.get(locations.getBeansPackage(config.name, BeanDirection.INPUTS), inputsNameClass);
            final ClassName outputClassName = ClassName.get(locations.getBeansPackage(config.name, BeanDirection.INPUTS), outputsNameClass);
            final ClassName returnedClassName = switch (ioConverter) {
                case INPUT_OUTPUT, OUTPUT -> outputClassName;
                case INPUT -> inputClassName;
            };
            final ClassName receivedClassName = switch (ioConverter) {
                case OUTPUT -> outputClassName;
                case INPUT, INPUT_OUTPUT -> inputClassName;
            };
            MethodSpec mspec = MethodSpec.methodBuilder(Constants.PROCESS_METHOD_NAME)
                    .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                    .addParameter(ParameterSpec.builder(receivedClassName,"bean").build())
                    .returns(returnedClassName)
                    .build();

            builder.addMethod(mspec);
        }


        TypeSpec theLogger = builder.build();

        JavaFile myfile = compilerUtil.specWithComment(theLogger, configs, package_, stackTraceElement);

        return new SpecificationFile(myfile, directory, fileName, package_);
    }








}