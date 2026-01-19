package org.openprovenance.prov.template.compiler;


import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.template.compiler.common.BeanDirection;
import org.openprovenance.prov.template.compiler.common.Constants;
import org.openprovenance.prov.template.compiler.configuration.Locations;
import org.openprovenance.prov.template.compiler.configuration.SpecificationFile;
import org.openprovenance.prov.template.compiler.configuration.TemplateCompilerConfig;
import org.openprovenance.prov.template.compiler.configuration.TemplatesProjectConfiguration;
import org.openprovenance.prov.template.compiler.past.Class;
import org.openprovenance.prov.template.compiler.past.type.ClassName;
import org.openprovenance.prov.template.compiler.past.Method;
import org.openprovenance.prov.template.compiler.past.PastFactory;

import javax.lang.model.element.Modifier;

import java.util.function.Supplier;

import static org.openprovenance.prov.template.compiler.common.Constants.*;
import static org.openprovenance.prov.template.compiler.configuration.SpecificationFile.*;
import static org.openprovenance.prov.template.compiler.configuration.SpecificationFile.generateRust;
import static org.openprovenance.prov.template.compiler.past.Method.METHOD;
import static org.openprovenance.prov.template.compiler.past.type.TypeVariable.T;

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

    PastFactory pastFactory=new PastFactory();

    SpecificationFile generateInputOutputProcessor(TemplatesProjectConfiguration configs, Locations locations, String package_, ProcessorType ioConverter, String directory, String fileName) {
        StackTraceElement stackTraceElement=compilerUtil.thisMethodAndLine();


        String interfaceName = switch (ioConverter) {
            case INPUT_OUTPUT -> INPUT_OUTPUT_PROCESSOR;
            case INPUT -> INPUT_PROCESSOR;
            case OUTPUT -> OUTPUT_PROCESSOR;
        };


        Class pastClass = pastFactory.INTERFACE(interfaceName)
                .MODIFIERS(Modifier.PUBLIC)
                //.TYPE_VARIABLES(T())
                ;

        for (TemplateCompilerConfig config : configs.templates) {
            final String inputsNameClass = compilerUtil.inputsNameClass(config.name);
            final String outputsNameClass = compilerUtil.outputsNameClass(config.name);

            final ClassName inputClassName = ClassName.get(inputsNameClass, locations.getBeansPackage(config.fullyQualifiedName, BeanDirection.INPUTS));
            final ClassName outputClassName = ClassName.get(outputsNameClass,locations.getBeansPackage(config.fullyQualifiedName, BeanDirection.INPUTS));
            final ClassName returnedClassName = switch (ioConverter) {
                case INPUT_OUTPUT, OUTPUT -> outputClassName;
                case INPUT -> inputClassName;
            };
            final ClassName receivedClassName = switch (ioConverter) {
                case OUTPUT -> outputClassName;
                case INPUT, INPUT_OUTPUT -> inputClassName;
            };
            Method mspec =METHOD(Constants.PROCESS_METHOD_NAME)
                    .MODIFIERS(Modifier.PUBLIC, Modifier.ABSTRACT)
                    .PARAMETER(receivedClassName,"bean")
                    .RETURNS(returnedClassName);

            pastClass.METHOD(mspec);
        }

        Supplier<Boolean> pythonGenerator=() -> generatePython(pastClass, package_, locations.python_dir, stackTraceElement);
        Supplier<Boolean> javaGenerator = () -> generateJava(pastClass, package_, configs, fileName, directory, stackTraceElement, compilerUtil);
        Supplier<Boolean> jsGenerator = () -> generateJavaScript(pastClass, package_, "target/generated-js", stackTraceElement);
        Supplier<Boolean> rustGenerator = () -> generateRust(pastClass, package_, "target/generated-rust/src", stackTraceElement);
        return new SpecificationFile(javaGenerator,pythonGenerator,jsGenerator,rustGenerator);

    }








}