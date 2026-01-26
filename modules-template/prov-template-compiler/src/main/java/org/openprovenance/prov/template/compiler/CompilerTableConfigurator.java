package org.openprovenance.prov.template.compiler;

import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.template.compiler.common.BeanDirection;
import org.openprovenance.prov.template.compiler.common.Constants;
import org.openprovenance.prov.template.compiler.configuration.*;
import org.openprovenance.prov.template.compiler.past.Class;
import org.openprovenance.prov.template.compiler.past.PastFactory;
import org.openprovenance.prov.template.compiler.past.Method;
import org.openprovenance.prov.template.compiler.past.type.ClassName;

import javax.lang.model.element.Modifier;
import java.util.function.Supplier;

import static org.openprovenance.prov.template.compiler.ConfigProcessor.*;
import static org.openprovenance.prov.template.compiler.configuration.SpecificationFile.generateJava;
import static org.openprovenance.prov.template.compiler.configuration.SpecificationFile.generatePython;
import static org.openprovenance.prov.template.compiler.past.Method.METHOD;
import static org.openprovenance.prov.template.compiler.past.Parameter.PARAMETER;
import static org.openprovenance.prov.template.compiler.past.type.ClassName.*;
import static org.openprovenance.prov.template.compiler.past.type.TypeVariable.T;

/**
 * PAST-based generator for TABLE_CONFIGURATOR / COMPOSITE_TABLE_CONFIGURATOR
 */
public class CompilerTableConfigurator {
    private final CompilerUtil compilerUtil;
    private final PastFactory pastFactory;

    public CompilerTableConfigurator(ProvFactory pFactory) {
        this.compilerUtil = new CompilerUtil(pFactory);
        this.pastFactory = compilerUtil.getPastFactory();
    }

    SpecificationFile generateTableConfigurator(TemplatesProjectConfiguration configs, Locations locations) {
        return generateTableConfigurator(configs, false, locations);
    }

    SpecificationFile generateCompositeTableConfigurator(TemplatesProjectConfiguration configs, Locations locations) {
        return generateTableConfigurator(configs, true, locations);
    }

    SpecificationFile generateTableConfigurator(TemplatesProjectConfiguration configs, boolean compositeOnly, Locations locations) {
        StackTraceElement stackTraceElement = compilerUtil.thisMethodAndLine();

        String tableClassName = (compositeOnly) ? Constants.COMPOSITE_TABLE_CONFIGURATOR : Constants.TABLE_CONFIGURATOR;

        Class iface = pastFactory.INTERFACE(tableClassName)
                .MODIFIERS(Modifier.PUBLIC)
                .TYPE_VARIABLES(T());

        for (TemplateCompilerConfig config : configs.templates) {
            if (!compositeOnly || !(config instanceof SimpleTemplateCompilerConfig)) {
                final String templateNameClass = compilerUtil.templateNameClass(config.name);
                final ClassName className = get(templateNameClass, locations.getBeansPackage(config.fullyQualifiedName, BeanDirection.COMMON));

                Method m = METHOD(config.name)
                        .MODIFIERS(Modifier.PUBLIC, Modifier.ABSTRACT)
                        .PARAMETER(className, "builder")
                        .RETURNS(T());

                //compilerUtil.debugFileLocation(m);
                iface.METHOD(m);
            }
        }

        String myPackage = locations.getFilePackage(configs.name, tableClassName);
        Supplier<Boolean> pythonGenerator = () -> generatePython(iface, myPackage, locations.python_dir, stackTraceElement);
        Supplier<Boolean> javaGenerator = () -> generateJava(iface, myPackage, configs, tableClassName + DOT_JAVA_EXTENSION, locations.convertToDirectory(myPackage), stackTraceElement, compilerUtil);

        return new SpecificationFile(javaGenerator, pythonGenerator);
    }
}