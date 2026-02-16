package org.openprovenance.prov.template.compiler;

import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.template.compiler.common.BeanDirection;
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
import static org.openprovenance.prov.template.compiler.past.ArrayAccessor.ARRAY_ACCESSOR;
import static org.openprovenance.prov.template.compiler.past.Assignment.ASSIGNMENT;
import static org.openprovenance.prov.template.compiler.past.BinaryOp.BINARY_OP;
import static org.openprovenance.prov.template.compiler.past.BinaryOp.INSTANCEOF;
import static org.openprovenance.prov.template.compiler.past.CastExpression.CAST;
import static org.openprovenance.prov.template.compiler.past.Constant.CONSTANT;
import static org.openprovenance.prov.template.compiler.past.Constructor.CONSTRUCTOR;
import static org.openprovenance.prov.template.compiler.past.Definition.DEFINITION;
import static org.openprovenance.prov.template.compiler.past.Field.FIELD;
import static org.openprovenance.prov.template.compiler.past.ForLoop.FOR;
import static org.openprovenance.prov.template.compiler.past.IfStatement.IF;
import static org.openprovenance.prov.template.compiler.past.Method.METHOD;
import static org.openprovenance.prov.template.compiler.past.MethodCall.*;
import static org.openprovenance.prov.template.compiler.past.Parameter.PARAMETER;
import static org.openprovenance.prov.template.compiler.past.Return.RETURN;
import static org.openprovenance.prov.template.compiler.past.Variable.VARIABLE;
import static org.openprovenance.prov.template.compiler.past.type.ClassName.*;

public class CompilerTableConfiguratorForTypes {
    private final CompilerUtil compilerUtil;
    private final PastFactory pastFactory;

    public CompilerTableConfiguratorForTypes(ProvFactory pFactory) {
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

        String originalTableClassName = (compositeOnly) ? COMPOSITE_TABLE_CONFIGURATOR : TABLE_CONFIGURATOR;
        String tableClassName = originalTableClassName + "ForTypes" + WITH_MAP;

        ClassName originalInterface = get(originalTableClassName, locations.getFilePackage(configs.name, originalTableClassName));

        Class pastClass = pastFactory.CLASS(tableClassName)
                .MODIFIERS(Modifier.PUBLIC)
                .INTERFACES(ParameterizedType.get(originalInterface, MAP_STRING_STRING_SET))
                .FIELDS(
                        FIELD("map", MAP_STRING_STRING).MODIFIERS(Modifier.PRIVATE, Modifier.FINAL),
                        FIELD(PROPERTY_ORDER, MAP_STRING_STRING_ARRAY).MODIFIERS(Modifier.PRIVATE, Modifier.FINAL),
                        FIELD(DOCUMENT_BUILDER_DISPATCHER, MAP_STRING_FILEBUILDER).MODIFIERS(Modifier.PRIVATE, Modifier.FINAL)
                );

        Constructor ctor = CONSTRUCTOR()
                .commentFileLocation()
                .MODIFIERS(Modifier.PUBLIC)
                .PARAMETERS(
                        PARAMETER("map", MAP_STRING_STRING),
                        PARAMETER(PROPERTY_ORDER,MAP_STRING_STRING_ARRAY ),
                        PARAMETER(DOCUMENT_BUILDER_DISPATCHER, MAP_STRING_FILEBUILDER),
                        PARAMETER("pf", PROV_FACTORY)
                )
                .BODY(
                        ASSIGNMENT( METHOD_CALL(VARIABLE("this"), "map"), VARIABLE("map")),
                        ASSIGNMENT( METHOD_CALL(VARIABLE("this"), PROPERTY_ORDER), VARIABLE(PROPERTY_ORDER)),
                        ASSIGNMENT( METHOD_CALL(VARIABLE("this"), DOCUMENT_BUILDER_DISPATCHER), VARIABLE(DOCUMENT_BUILDER_DISPATCHER))
                );

        pastClass.CONSTRUCTOR(ctor);

        for (TemplateCompilerConfig config : configs.templates) {
            final String templateNameClass = compilerUtil.templateNameClass(config.name);
            final ClassName builderParamType = get(templateNameClass, locations.getBeansPackage(config.fullyQualifiedName, BeanDirection.COMMON));

            Method m = METHOD(config.name)
                    .MODIFIERS(Modifier.PUBLIC)
                    .PARAMETER(builderParamType, "builder")
                    .RETURNS(MAP_STRING_STRING_SET)
                    .commentFileLocation();

            if (config instanceof SimpleTemplateCompilerConfig) {
                ClassName builderClassName = get(templateNameClass, locations.getBackendPackage(config.fullyQualifiedName));
                m.BODY(
                        DEFINITION(STRING_ARRAY, VARIABLE("properties"),
                                METHOD_CALL(VARIABLE(PROPERTY_ORDER), "get", List.of(CONSTANT(config.fullyQualifiedName)))),

                        DEFINITION(builderClassName, VARIABLE(TEMPLATE_BUILDER_VARIABLE),
                                CAST(builderClassName, METHOD_CALL(
                                        VARIABLE(DOCUMENT_BUILDER_DISPATCHER),
                                        "get",
                                        List.of(CONSTANT(config.fullyQualifiedName))
                                ) )),

                        DEFINITION(OBJECT_ARRAY, VARIABLE("record2"),
                                METHOD_CALL(
                                        VARIABLE(TEMPLATE_BUILDER_VARIABLE),
                                        "make",
                                        List.of(
                                                METHOD_CALL(
                                                        VARIABLE(TEMPLATE_BUILDER_VARIABLE),
                                                        "getTypedRecord",
                                                        List.of()
                                                )
                                        )
                                )
                        ),

                        DEFINITION(MAP_QUALIFIEDNAME_STRING_SET, VARIABLE("knownTypeMap"),
                                CONSTRUCTOR_CALL(HASH_MAP_GENERICS, List.of())
                        ),

                        METHOD_CALL(
                                VARIABLE(TEMPLATE_BUILDER_VARIABLE),
                                "make",
                                List.of(
                                        METHOD_CALL(
                                                VARIABLE(TEMPLATE_BUILDER_VARIABLE),
                                                "getTypeManager",
                                                List.of(
                                                        VARIABLE("knownTypeMap"),
                                                        CONSTRUCTOR_CALL(HASH_MAP_GENERICS, List.of()),
                                                        CONSTRUCTOR_CALL(HASH_MAP_GENERICS, List.of()),
                                                        CONSTRUCTOR_CALL(HASH_MAP_GENERICS, List.of()),
                                                        CONSTRUCTOR_CALL(HASH_MAP_GENERICS, List.of())
                                                )
                                        )
                                )
                        ),
                        DEFINITION(MAP_STRING_STRING_SET, VARIABLE(PROPERTY_MAP),
                                CONSTRUCTOR_CALL(HASH_MAP_GENERICS, List.of())
                        ),

                        FOR(DEFINITION(_int, VARIABLE("i"), CONSTANT(0)),
                                BINARY_OP(VARIABLE("i"), "<", METHOD_CALL(VARIABLE("record2"), "length")),
                                ASSIGNMENT( VARIABLE("i"),
                                        BINARY_OP(VARIABLE("i"), "+", CONSTANT(1))))
                                .BODY(
                                        DEFINITION(STRING, VARIABLE("property"),
                                                ARRAY_ACCESSOR(VARIABLE("properties"), VARIABLE("i"))),

                                        DEFINITION(OBJECT, VARIABLE("value"),
                                                ARRAY_ACCESSOR(VARIABLE("record2"), VARIABLE("i"))),
                                        IF(BINARY_OP(VARIABLE("value"), INSTANCEOF, METHOD_CALL(PROV_QUALIFIED_NAME,"class")))
                                                .THEN(
                                                        METHOD_CALL(
                                                                VARIABLE(PROPERTY_MAP),
                                                                "put",
                                                                List.of(
                                                                        VARIABLE("property"),
                                                                        METHOD_CALL(
                                                                                VARIABLE("knownTypeMap"),
                                                                                "get",
                                                                                List.of(
                                                                                        CAST(PROV_QUALIFIED_NAME, VARIABLE("value"))
                                                                                )
                                                                        )
                                                                )
                                                        )
                                                )),

                        RETURN(VARIABLE(PROPERTY_MAP)) );


            } else {
                m.BODY(RETURN(Constant.getNull()));
            }

            pastClass.METHOD(m);
        }

        String myPackage = locations.getConfiguratorBackendPackage(configs.name);
        String directory=locations.convertToDirectory(l2p_src_dir,"");

        Supplier<Boolean> pythonGenerator = () -> generatePython(pastClass, myPackage, locations.python_dir, stackTraceElement);
        Supplier<Boolean> javaGenerator = () -> generateJava(pastClass, myPackage, configs, tableClassName + DOT_JAVA_EXTENSION,
                directory, stackTraceElement, compilerUtil);

        return new SpecificationFile(javaGenerator, pythonGenerator);
    }
}