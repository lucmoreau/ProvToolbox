
package org.openprovenance.prov.template.compiler;

import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.template.compiler.common.BeanDirection;
import org.openprovenance.prov.template.compiler.common.Constants;
import org.openprovenance.prov.template.compiler.configuration.*;
import org.openprovenance.prov.template.compiler.past.Class;
import org.openprovenance.prov.template.compiler.past.Constructor;
import org.openprovenance.prov.template.compiler.past.Method;
import org.openprovenance.prov.template.compiler.past.PastFactory;
import org.openprovenance.prov.template.compiler.past.type.ClassName;

import javax.lang.model.element.Modifier;
import java.util.List;
import java.util.function.Supplier;

import static org.openprovenance.prov.template.compiler.ConfigProcessor.*;
import static org.openprovenance.prov.template.compiler.configuration.SpecificationFile.generateJava;
import static org.openprovenance.prov.template.compiler.configuration.SpecificationFile.generatePython;
import static org.openprovenance.prov.template.compiler.past.Assignment.ASSIGNMENT;
import static org.openprovenance.prov.template.compiler.past.Constructor.CONSTRUCTOR;
import static org.openprovenance.prov.template.compiler.past.Field.FIELD;
import static org.openprovenance.prov.template.compiler.past.Method.METHOD;
import static org.openprovenance.prov.template.compiler.past.MethodCall.METHOD_CALL;
import static org.openprovenance.prov.template.compiler.past.Return.RETURN;
import static org.openprovenance.prov.template.compiler.past.Variable.VARIABLE;
import static org.openprovenance.prov.template.compiler.past.type.ClassName.*;

public class CompilerDelegator {
    private final CompilerUtil compilerUtil;
    private final PastFactory pastFactory;

    public CompilerDelegator(ProvFactory pFactory) {
        this.compilerUtil = new CompilerUtil(pFactory);
        this.pastFactory = compilerUtil.getPastFactory();
    }

    public SpecificationFile generateDelegator(TemplatesProjectConfiguration configs, Locations locations, String fileName) {
        StackTraceElement stackTraceElement = compilerUtil.thisMethodAndLine();

        ClassName beanProcessorType = get(BEAN_PROCESSOR, locations.getFilePackage(configs.name, BEAN_PROCESSOR));

        Class pastClass = pastFactory.CLASS(Constants.DELEGATOR)
                .COMMENT("Delegator for processing beans\n")
                .MODIFIERS(Modifier.PUBLIC)
                .INTERFACES(beanProcessorType)
                .FIELDS(
                        FIELD(DELEGATOR_VAR, beanProcessorType).MODIFIERS(Modifier.FINAL, Modifier.PRIVATE)
                );

        Constructor ctor = CONSTRUCTOR()
                .debugFileLocation()
                .MODIFIERS(Modifier.PUBLIC)
                .PARAMETER(beanProcessorType, DELEGATOR_VAR)
                .BODY(
                        ASSIGNMENT(null, METHOD_CALL(VARIABLE("this"), DELEGATOR_VAR), VARIABLE(DELEGATOR_VAR))
                );
        pastClass.CONSTRUCTOR(ctor);

        for (TemplateCompilerConfig config : configs.templates) {
            final String beanNameClass = compilerUtil.commonNameClass(config.name);
            ClassName beanType = get(beanNameClass, locations.getBeansPackage(config.fullyQualifiedName, BeanDirection.COMMON));

            Method m = METHOD(PROCESS_METHOD_NAME)
                    .commentFileLocation()
                    .MODIFIERS(Modifier.PUBLIC)
                    .PARAMETER(beanType, BEAN_VAR)
                    .RETURNS(beanType)
                    .COMMENT("Processing method\n")
                    .COMMENT("@param bean an input bean\n")
                    .COMMENT("@return a processed bean\n");

            m.BODY(
                    RETURN(
                            METHOD_CALL(VARIABLE(DELEGATOR_VAR), PROCESS_METHOD_NAME, List.of(VARIABLE(BEAN_VAR))))

            );

            pastClass.METHOD(m);
        }

        String myPackage = locations.getFilePackage(configs.name, fileName);
        Supplier<Boolean> pythonGenerator = () -> generatePython(pastClass, myPackage, locations.python_dir, stackTraceElement);
        Supplier<Boolean> javaGenerator = () -> generateJava(pastClass, myPackage, configs, fileName + DOT_JAVA_EXTENSION, locations.convertToDirectory(myPackage), stackTraceElement, compilerUtil);
        return new SpecificationFile(javaGenerator, pythonGenerator);
    }
}