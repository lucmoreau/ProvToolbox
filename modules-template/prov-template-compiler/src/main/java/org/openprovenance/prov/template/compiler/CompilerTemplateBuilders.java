package org.openprovenance.prov.template.compiler;

import com.squareup.javapoet.*;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.template.compiler.common.BeanDirection;
import org.openprovenance.prov.template.compiler.common.Constants;
import org.openprovenance.prov.template.compiler.configuration.*;
import org.openprovenance.prov.template.compiler.past.Class;
import org.openprovenance.prov.template.compiler.past.PastFactory;
import org.openprovenance.prov.template.compiler.past.type.ClassName;

import javax.lang.model.element.Modifier;

import java.util.function.Supplier;

import static org.openprovenance.prov.template.compiler.common.Constants.*;
import static org.openprovenance.prov.template.compiler.configuration.SpecificationFile.generateJava;
import static org.openprovenance.prov.template.compiler.configuration.SpecificationFile.generatePython;
import static org.openprovenance.prov.template.compiler.past.Field.FIELD;
import static org.openprovenance.prov.template.compiler.past.MethodCall.METHOD_CALL;
import static org.openprovenance.prov.template.compiler.past.Variable.VARIABLE;

public class CompilerTemplateBuilders {
    private final CompilerUtil compilerUtil;
    private final PastFactory pastFactory;

    public CompilerTemplateBuilders(ProvFactory pFactory) {
        this.compilerUtil=new CompilerUtil(pFactory);
        this.pastFactory = compilerUtil.getPastFactory();
    }


    SpecificationFile generateTemplateBuilders(TemplatesProjectConfiguration configs, Locations locations, String fileName) {
        StackTraceElement stackTraceElement=compilerUtil.thisMethodAndLine();


        Class pastClass = pastFactory.CLASS(TEMPLATE_BUILDERS)
                .MODIFIERS(Modifier.PUBLIC)
                ;


        String packageName = locations.getFilePackage(configs.name, LOGGER);
        ClassName loggerClass = ClassName.get(LOGGER, packageName);

        for (TemplateCompilerConfig config : configs.templates) {
            if (!(config instanceof SimpleTemplateCompilerConfig)) continue;

            final String templateNameClass = compilerUtil.templateNameClass(config.name);

            final ClassName className = ClassName.get(templateNameClass, locations.getBeansPackage(config.fullyQualifiedName, BeanDirection.COMMON) );
            pastClass.FIELDS(
                    FIELD(config.name + "Builder", className)
                            .MODIFIERS(Modifier.PUBLIC, Modifier.FINAL)
                            .INITIALIZER(METHOD_CALL(loggerClass,Constants.GENERATED_VAR_PREFIX + config.name)));

        }

        String myPackage = locations.getFilePackage(configs.name, fileName);
        Supplier<Boolean> pythonGenerator = () -> generatePython(pastClass, myPackage, locations.python_dir, stackTraceElement);
        Supplier<Boolean> javaGenerator = () -> generateJava(pastClass, myPackage, configs, fileName + DOT_JAVA_EXTENSION, locations.convertToDirectory(myPackage), stackTraceElement, compilerUtil);
        return new SpecificationFile(javaGenerator, pythonGenerator);

    }


}