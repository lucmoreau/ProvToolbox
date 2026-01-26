package org.openprovenance.prov.template.compiler;

import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.template.compiler.common.BeanDirection;
import org.openprovenance.prov.template.compiler.common.Constants;
import org.openprovenance.prov.template.compiler.configuration.*;
import org.openprovenance.prov.template.compiler.past.*;
import org.openprovenance.prov.template.compiler.past.Class;
import org.openprovenance.prov.template.compiler.past.type.ClassName;
import org.openprovenance.prov.template.compiler.past.type.ParameterizedType;

import javax.lang.model.element.Modifier;
import java.util.List;
import java.util.function.Supplier;

import static org.openprovenance.prov.template.compiler.ConfigProcessor.*;
import static org.openprovenance.prov.template.compiler.configuration.SpecificationFile.generateJava;
import static org.openprovenance.prov.template.compiler.configuration.SpecificationFile.generatePython;
import static org.openprovenance.prov.template.compiler.past.Assignment.ASSIGNMENT;
import static org.openprovenance.prov.template.compiler.past.BinaryOp.BINARY_OP;
import static org.openprovenance.prov.template.compiler.past.Constructor.CONSTRUCTOR;
import static org.openprovenance.prov.template.compiler.past.Field.FIELD;
import static org.openprovenance.prov.template.compiler.past.IfStatement.IF;
import static org.openprovenance.prov.template.compiler.past.Method.METHOD;
import static org.openprovenance.prov.template.compiler.past.MethodCall.*;
import static org.openprovenance.prov.template.compiler.past.Parameter.PARAMETER;
import static org.openprovenance.prov.template.compiler.past.Return.RETURN;
import static org.openprovenance.prov.template.compiler.past.Variable.VARIABLE;
import static org.openprovenance.prov.template.compiler.past.type.ClassName.*;

public class    CompilerTableConfiguratorWithMap {
    public static final String PREFIX = "_b_";
    private final CompilerUtil compilerUtil;
    private final PastFactory pastFactory;

    public CompilerTableConfiguratorWithMap(ProvFactory pFactory) {
        this.compilerUtil = new CompilerUtil(pFactory);
        this.pastFactory = compilerUtil.getPastFactory();
    }

    SpecificationFile generateTableConfigurator(TemplatesProjectConfiguration configs, Locations locations, String l2p_src_dir) {
        return generateTableConfigurator(configs, false, locations, l2p_src_dir);
    }

    SpecificationFile generateCompositeTableConfigurator(TemplatesProjectConfiguration configs, Locations locations, String l2p_src_dir) {
        return generateTableConfigurator(configs, true, locations, l2p_src_dir);
    }

    SpecificationFile generateTableConfigurator(TemplatesProjectConfiguration configs, boolean compositeOnly, Locations locations, String l2p_src_dir) {
        StackTraceElement stackTraceElement = compilerUtil.thisMethodAndLine();

        String originalTableClassName = (compositeOnly) ? Constants.COMPOSITE_TABLE_CONFIGURATOR : Constants.TABLE_CONFIGURATOR;
        String tableClassName = originalTableClassName + Constants.WITH_MAP;

        ClassName originalInterface = get(originalTableClassName, locations.getFilePackage(configs.name, originalTableClassName));


        Class pastClass = pastFactory.CLASS(tableClassName)
                .MODIFIERS(Modifier.PUBLIC)
                .INTERFACES(ParameterizedType.get(originalInterface, PROV_FILE_BUILDER))
                .FIELDS(
                        FIELD("map", MAP_STRING_STRING).MODIFIERS(Modifier.PRIVATE, Modifier.FINAL)
                );


        Constructor ctor = CONSTRUCTOR()
                .debugFileLocation()
                .MODIFIERS(Modifier.PUBLIC)
                .PARAMETERS(
                        PARAMETER("map", MAP_STRING_STRING),
                        PARAMETER("pf", PROV_FACTORY)
                )
                .BODY(
                        ASSIGNMENT(null, METHOD_CALL(VARIABLE("this"), "map"), VARIABLE("map"))
                );

        for (TemplateCompilerConfig config : configs.templates) {
            final String templateNameClass = compilerUtil.templateNameClass(config.name);
            final ClassName className = ClassName.get(templateNameClass, locations.getBeansPackage(config.fullyQualifiedName, BeanDirection.COMMON) );

            Method method = METHOD(config.name)
                    .MODIFIERS(Modifier.PUBLIC)
                    .PARAMETER(className, "builder")
                    .RETURNS(PROV_FILE_BUILDER);

            if (config instanceof SimpleTemplateCompilerConfig) {
                method.debugFileLocation();
                SimpleTemplateCompilerConfig simpleConfig = (SimpleTemplateCompilerConfig) config;
                ClassName builderClass = get(templateNameClass, simpleConfig.package_);
                String builderVar = PREFIX + config.name;
                pastClass.FIELDS(FIELD(builderVar, builderClass).MODIFIERS(Modifier.PRIVATE, Modifier.FINAL));


                ctor.BODY(
                        ASSIGNMENT(null, METHOD_CALL(VARIABLE("this"), builderVar),
                                CONSTRUCTOR_CALL(builderClass, List.of(VARIABLE("pf"))))
                );

                method.BODY(

                        IF(BINARY_OP(VARIABLE("map"), "!=", Constant.getNull()))
                                .THEN(METHOD_CALL(
                                        VARIABLE(builderVar),
                                        "setVariableMap",
                                        List.of(VARIABLE("map")) ) ),
                        RETURN(METHOD_CALL(VARIABLE("this"), builderVar))
                );
            } else {
                method.debugFileLocation();
                method.BODY(RETURN(Constant.getNull())
                );
            }

            pastClass.CONSTRUCTOR(ctor);
            pastClass.METHOD(method);
        }

        String myPackage = locations.getConfiguratorBackendPackage(configs.name);
        Supplier<Boolean> pythonGenerator = () -> generatePython(pastClass, myPackage, locations.python_dir, stackTraceElement);
        Supplier<Boolean> javaGenerator = () -> generateJava(pastClass, myPackage, configs, tableClassName + DOT_JAVA_EXTENSION,
                locations.convertToBackendDirectory(myPackage), stackTraceElement, compilerUtil);

        return new SpecificationFile(javaGenerator, pythonGenerator);
    }
}