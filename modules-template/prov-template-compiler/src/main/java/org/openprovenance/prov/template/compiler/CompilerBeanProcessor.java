package org.openprovenance.prov.template.compiler;

import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.template.compiler.common.BeanDirection;
import org.openprovenance.prov.template.compiler.common.Constants;
import org.openprovenance.prov.template.compiler.configuration.Locations;
import org.openprovenance.prov.template.compiler.configuration.SpecificationFile;
import org.openprovenance.prov.template.compiler.configuration.TemplateCompilerConfig;
import org.openprovenance.prov.template.compiler.configuration.TemplatesProjectConfiguration;
import org.openprovenance.prov.template.compiler.past.Class;
import org.openprovenance.prov.template.compiler.past.PastFactory;
import org.openprovenance.prov.template.compiler.past.type.ClassName;

import javax.lang.model.element.Modifier;

import java.util.function.Supplier;

import static org.openprovenance.prov.template.compiler.common.Constants.BEAN_PROCESSOR;
import static org.openprovenance.prov.template.compiler.common.Constants.DOT_JAVA_EXTENSION;
import static org.openprovenance.prov.template.compiler.configuration.SpecificationFile.generateJava;
import static org.openprovenance.prov.template.compiler.configuration.SpecificationFile.generatePython;
import static org.openprovenance.prov.template.compiler.past.Method.METHOD;

public class CompilerBeanProcessor {
    private final CompilerUtil compilerUtil;
    private final PastFactory pastFactory;

    public CompilerBeanProcessor(ProvFactory pFactory) {
        this.compilerUtil=new CompilerUtil(pFactory);
        this.pastFactory=compilerUtil.getPastFactory();
    }

    SpecificationFile generateBeanProcessor(TemplatesProjectConfiguration configs, Locations locations, String fileName) {
        StackTraceElement stackTraceElement=compilerUtil.thisMethodAndLine();
        Class pastClass= pastFactory
                .INTERFACE(BEAN_PROCESSOR)
                .MODIFIERS(Modifier.PUBLIC);

        for (TemplateCompilerConfig config : configs.templates) {
            final String beanNameClass = compilerUtil.commonNameClass(config.name);
            final ClassName className = ClassName.get(beanNameClass, locations.getBeansPackage(config.fullyQualifiedName, BeanDirection.COMMON));
            pastClass.METHOD(METHOD(Constants.PROCESS_METHOD_NAME)
                    .MODIFIERS(Modifier.PUBLIC, Modifier.ABSTRACT)
                    .PARAMETER(className,"bean")
                    .RETURNS(className));
        }

        String myPackage=locations.getFilePackage(configs.name,fileName);

        Supplier<Boolean> pythonGenerator=() -> generatePython(pastClass, myPackage, locations.python_dir, stackTraceElement);
        Supplier<Boolean> javaGenerator = () -> generateJava(pastClass, myPackage, configs, fileName + DOT_JAVA_EXTENSION, locations.convertToDirectory(myPackage), stackTraceElement, compilerUtil);

        return new SpecificationFile(javaGenerator,pythonGenerator);

    }







}