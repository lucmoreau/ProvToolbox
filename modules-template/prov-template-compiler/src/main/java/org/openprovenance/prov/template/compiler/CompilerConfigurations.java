package org.openprovenance.prov.template.compiler;

import com.squareup.javapoet.*;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.template.compiler.common.BeanDirection;
import org.openprovenance.prov.template.compiler.configuration.*;
import org.openprovenance.prov.template.compiler.past.*;
import org.openprovenance.prov.template.compiler.past.Class;
import org.openprovenance.prov.template.compiler.past.emitter.Poet;
import org.openprovenance.prov.template.compiler.past.type.ParameterizedType;
import org.openprovenance.prov.template.compiler.past.type.TypeVariable;
import org.openprovenance.prov.template.log2prov.FileBuilder;

import javax.lang.model.element.Modifier;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.squareup.javapoet.TypeSpec.interfaceBuilder;
import static org.openprovenance.prov.template.compiler.CompilerBeanGenerator.newSpecificationFiles;
import static org.openprovenance.prov.template.compiler.common.Constants.*;
import static org.openprovenance.prov.template.compiler.common.Constants.BUILDER_INTERFACE;
import static org.openprovenance.prov.template.compiler.configuration.SpecificationFile.generateJava;
import static org.openprovenance.prov.template.compiler.configuration.SpecificationFile.generatePython;
import static org.openprovenance.prov.template.compiler.past.Assignment.ASSIGNMENT;
import static org.openprovenance.prov.template.compiler.past.CastExpression.CAST;
import static org.openprovenance.prov.template.compiler.past.Constructor.CONSTRUCTOR;
import static org.openprovenance.prov.template.compiler.past.Field.FIELD;
import static org.openprovenance.prov.template.compiler.past.LambdaExpression.LAMBDA;
import static org.openprovenance.prov.template.compiler.past.Method.METHOD;
import static org.openprovenance.prov.template.compiler.past.MethodCall.FUNCTIONAL_METHOD_CALL;
import static org.openprovenance.prov.template.compiler.past.MethodCall.METHOD_CALL;
import static org.openprovenance.prov.template.compiler.past.Parameter.PARAMETER;
import static org.openprovenance.prov.template.compiler.past.Return.RETURN;
import static org.openprovenance.prov.template.compiler.past.Variable.VARIABLE;
import static org.openprovenance.prov.template.compiler.past.Variable.VariableKind.FIELD_VARIABLE;
import static org.openprovenance.prov.template.compiler.past.type.ClassName.*;
import static org.openprovenance.prov.template.compiler.past.type.ClassName.FUNCTION_OBJARRAY_TO_TYPE;
import static org.openprovenance.prov.template.compiler.past.type.TypeVariable.T;

public class CompilerConfigurations {
    public static final String RECORD_2_RECORD = "Record2Record";
    public static final String PROCESS = "process";
    private final CompilerUtil compilerUtil;

    public CompilerConfigurations(ProvFactory pFactory) {
        this.compilerUtil=new CompilerUtil(pFactory);
    }


    static public final ParameterizedTypeName functionObjArrayTo (TypeName returnType) {
        return ParameterizedTypeName.get(ClassName.get(Function.class), ArrayTypeName.of(Object.class), returnType);
    }

    static final TypeVariableName PARAMETRIC_T=TypeVariableName.get("T");
    public static final ParameterizedTypeName processorOfStringOLD = ParameterizedTypeName.get(ClassName.get(CLIENT_PACKAGE,"ProcessorArgsInterface"), TypeName.get(String.class));
    public static final ParameterizedTypeName processorOfString = functionObjArrayTo(TypeName.get(String.class));
    static final ParameterizedTypeName processorOfUnknown = functionObjArrayTo(TypeVariableName.get("?"));
    public static final TypeName stringArray = ArrayTypeName.get(String[].class);
    static final TypeName intArray = ArrayTypeName.get(int[].class);
    static final ParameterizedTypeName mapString2StringArray = ParameterizedTypeName.get(ClassName.get(Map.class), ClassName.get(String.class), stringArray);
    static final ParameterizedTypeName mapString2StringList = ParameterizedTypeName.get(ClassName.get(Map.class), ClassName.get(String.class), ParameterizedTypeName.get(ClassName.get(List.class), ClassName.get(String.class)));
    static final ParameterizedTypeName mapString2IntArray = ParameterizedTypeName.get(ClassName.get(Map.class), ClassName.get(String.class), intArray);
    static final ParameterizedTypeName mapString2MapString2IntArray = ParameterizedTypeName.get(ClassName.get(Map.class), ClassName.get(String.class), mapString2IntArray);

    static final ParameterizedTypeName BiFunctionOfString2StringArray = ParameterizedTypeName.get(ClassName.get(java.util.function.BiFunction.class), mapString2MapString2IntArray, stringArray, PARAMETRIC_T);
    static final ParameterizedTypeName FunctionOfString2StringArray = ParameterizedTypeName.get(ClassName.get(java.util.function.Function.class), ClassName.get(CLIENT_PACKAGE,BUILDER_INTERFACE), PARAMETRIC_T);

    static final ParameterizedTypeName FunctionOfObjectArray2ObjectArray = ParameterizedTypeName.get(ClassName.get(java.util.function.Function.class), ArrayTypeName.get(Object[].class), ArrayTypeName.get(Object[].class));

    static final ParameterizedTypeName MapString2FileBuilder= ParameterizedTypeName.get(ClassName.get(Map.class), ClassName.get(String.class), ClassName.get(FileBuilder.class));

    static final String CONVERTER_VAR="converter";


    static final String ENACTOR_VAR = "beanEnactor";



    public SpecificationFile generateConfigurator(TemplatesProjectConfiguration configs,
                                                  Locations locations,
                                                  String theConfiguratorName,
                                                  org.openprovenance.prov.template.compiler.past.type.TypeName typeName,
                                                  SixtetConsumer<String, String, Method, org.openprovenance.prov.template.compiler.past.type.TypeName, org.openprovenance.prov.template.compiler.past.type.TypeName, org.openprovenance.prov.template.compiler.past.type.TypeName> generator,
                                                  String generatorMethod,
                                                  BeanDirection direction,
                                                  org.openprovenance.prov.template.compiler.past.type.TypeName constructParameterType,
                                                  String constructorParameter,
                                                  TypeVariable parametericType,
                                                  boolean defaultBehaviour,
                                                  String beanPackage,
                                                  BeanDirection outDirection,
                                                  String directory,
                                                  String fileName) {
        return generateConfigurator(configs, locations, theConfiguratorName, typeName, generator, generatorMethod, direction, constructParameterType, constructorParameter, parametericType, defaultBehaviour, beanPackage, outDirection, directory, fileName, null);
    }

    private SpecificationFile generateConfigurator(TemplatesProjectConfiguration configs,
                                                  Locations locations,
                                                  String theConfiguratorName,
                                                  org.openprovenance.prov.template.compiler.past.type.TypeName typeName,
                                                  SixtetConsumer<String, String, Method, org.openprovenance.prov.template.compiler.past.type.TypeName, org.openprovenance.prov.template.compiler.past.type.TypeName, org.openprovenance.prov.template.compiler.past.type.TypeName> generator,
                                                  String generatorMethod,
                                                  BeanDirection direction,
                                                  org.openprovenance.prov.template.compiler.past.type.TypeName constructParameterType,
                                                  String constructorParameter,
                                                  org.openprovenance.prov.template.compiler.past.type.TypeVariable parametericType,
                                                  boolean defaultBehaviour,
                                                  String beanPackage,
                                                  BeanDirection outDirection,
                                                  String directory,
                                                  String fileName,
                                                  Consumer<TypeSpec.Builder> optionalCode) {
        StackTraceElement stackTraceElement=compilerUtil.thisMethodAndLine();

        final ParameterizedType tableConfiguratorType = ParameterizedType.get(org.openprovenance.prov.template.compiler.past.type.ClassName.get(TABLE_CONFIGURATOR,locations.getFilePackage(configs.name, TABLE_CONFIGURATOR)), typeName);

        PastFactory pastFactory=new PastFactory();
        Class pastClass = pastFactory.CLASS(theConfiguratorName)
                .MODIFIERS(Modifier.PUBLIC)
                .COMMENT("The table configurator $N\n", theConfiguratorName);



        // the following in only used for the enactorConfigurator
        if (constructParameterType!=null && constructorParameter!=null) {
            pastClass.FIELDS(FIELD(constructorParameter, constructParameterType).MODIFIERS(Modifier.FINAL, Modifier.PRIVATE));
            Constructor cspec= CONSTRUCTOR() // constructor
                    .MODIFIERS(Modifier.PUBLIC)
                    .PARAMETER(constructParameterType,constructorParameter);
            compilerUtil.debugFileLocation(cspec);

            cspec.BODY(ASSIGNMENT(null, METHOD_CALL(VARIABLE("this"), constructorParameter),VARIABLE(constructorParameter)));
            pastClass.CONSTRUCTOR(cspec);
        }


        if (parametericType!=null)  pastClass.TYPE_VARIABLES(parametericType);
        pastClass.INTERFACES(tableConfiguratorType);


        for (TemplateCompilerConfig config : configs.templates) {
            final String templateNameClass = compilerUtil.templateNameClass(config.name);

            final String inBeanNameClass = compilerUtil.beanNameClass(config.name, direction);
            final String outBeanNameClass = compilerUtil.beanNameClass(config.name, outDirection);
            final org.openprovenance.prov.template.compiler.past.type.ClassName className = get(templateNameClass,locations.getBeansPackage(config.fullyQualifiedName, BeanDirection.COMMON));
            String builderParameter = "builder";

            Method method = METHOD(config.name)
                    .COMMENT("Gets configuration\n")
                    .COMMENT("@param $N builder for template $N\n", builderParameter, config.name)
                    .COMMENT("@return $T\n", STRING_ARRAY)
                    .MODIFIERS(Modifier.PUBLIC, Modifier.FINAL)
                    .PARAMETER(className, builderParameter)
                    .RETURNS(typeName);
            compilerUtil.debugFileLocation(method);

            if (config instanceof SimpleTemplateCompilerConfig || defaultBehaviour) {

                generator.accept(builderParameter,
                        config.fullyQualifiedName,
                        method,
                        className,
                        get(inBeanNameClass,(direction==BeanDirection.COMMON)? locations.getBeansPackage(config.fullyQualifiedName, BeanDirection.COMMON) : beanPackage),
                        get(outBeanNameClass,(direction==BeanDirection.COMMON)? locations.getBeansPackage(config.fullyQualifiedName, BeanDirection.COMMON) : beanPackage)
                );
            } else {
                method.BODY(RETURN(Constant.getNull()));
            }
            pastClass.METHOD(method);

        }

        TypeSpec.Builder builder = new Poet().emitBuilder(pastClass);

        if (optionalCode!=null) {
            optionalCode.accept(builder);
        }



        String thePackage=locations.getFilePackage(configs.name, theConfiguratorName);
        Supplier<Boolean> pythonGenerator=() -> generatePython(pastClass, thePackage, locations.python_dir, stackTraceElement);
        Supplier<Boolean> javaGenerator = () -> generateJava(pastClass, thePackage, configs, fileName, directory, stackTraceElement, compilerUtil);
        return new SpecificationFile(javaGenerator,pythonGenerator);
    }

    public SpecificationFile generateRelation0Configurator(TemplatesProjectConfiguration configs, String theConfiguratorName, Locations locations, String directory, String fileName) {
        return  generateConfigurator(configs, locations, theConfiguratorName, MAP_STRING_MAP_STRING_INTARRAY, this::generateRelation0, "generateRelation0Configurator", BeanDirection.COMMON, null, null, null, false, null, BeanDirection.COMMON, directory, fileName);
    }

    public SpecificationFile generateRelationConfigurator(TemplatesProjectConfiguration configs, String theConfiguratorName, Locations locations, String directory, String fileName) {
        return  generateConfigurator(configs, locations, theConfiguratorName, T(), this::generateRelation, "generateRelationConfigurator", BeanDirection.COMMON, BIFUNCTION_MAP_STRING_MAP_STRING_INTARRAY_STRINGARRAY_TO_T, CONVERTER_VAR, T(), false, null, BeanDirection.COMMON, directory, fileName);
    }
    public SpecificationFile generateBuilderProcessorConfigurator(TemplatesProjectConfiguration configs, String theConfiguratorName, Locations locations, String directory, String fileName) {
        return  generateConfigurator(configs, locations, theConfiguratorName, T(), this::generateBuilderProcessor, "generateBuilderProcessorConfigurator", BeanDirection.COMMON, FUNCTION_BUILDER_T, PROCESSOR, T(), false, null, BeanDirection.COMMON, directory, fileName);
    }

    public SpecificationFile generateObjectRecordMakerConfigurator(TemplatesProjectConfiguration configs, String theConfiguratorName, Locations locations, String directory, String fileName) {
        return  generateConfigurator(configs, locations, theConfiguratorName, FUNCTION_OBJARRAY_TO_OBJ_ARRAY, new WrapperClass(locations)::generateObjectRecordMaker, "generateObjectRecordMakerConfigurator", BeanDirection.COMMON, MAP_STRING_FILEBUILDER, DISPATCHER_VAR, null, false, null, BeanDirection.COMMON, directory, fileName);
    }

    public SpecificationFile generateSqlConfigurator(TemplatesProjectConfiguration configs, String theConfiguratorName, Locations locations, String directory, String fileName) {
        return  generateConfigurator(configs, locations, theConfiguratorName, FUNCTION_OBJARRAY_TO_STRING, this::generateMethodRecord2SqlConverter, "generateSqlConfigurator", BeanDirection.COMMON, null, null, null, false, null, BeanDirection.COMMON, directory, fileName);
    }
    public SpecificationFile generatePropertyOrderConfigurator(TemplatesProjectConfiguration configs, String theConfiguratorName, Locations locations, String directory, String fileName) {
        return  generateConfigurator(configs, locations, theConfiguratorName, STRING_ARRAY, this::generatePropertyOrder, "generatePropertyOrder", BeanDirection.COMMON, null, null, null, true, null, BeanDirection.COMMON, directory, fileName);
    }
    public SpecificationFile generateInputsConfigurator(TemplatesProjectConfiguration configs, String theConfiguratorName, Locations locations, String directory, String fileName) {
        return  generateConfigurator(configs, locations, theConfiguratorName, STRING_ARRAY, this::generateInputPropertyOrder, "generateInputsConfigurator", BeanDirection.COMMON, null, null, null, true, null, BeanDirection.COMMON, directory, fileName);
    }
    public SpecificationFile generateOutputsConfigurator(TemplatesProjectConfiguration configs, String theConfiguratorName, Locations locations, String directory, String fileName) {
        return  generateConfigurator(configs, locations, theConfiguratorName, STRING_ARRAY, this::generateOutputPropertyOrder, "generateOutputPropertyOrder", BeanDirection.COMMON, null, null, null, true, null, BeanDirection.COMMON, directory, fileName);
    }
    public SpecificationFile generateCsvConfigurator(TemplatesProjectConfiguration configs, String theConfiguratorName, Locations locations, String directory, String fileName) {
        return  generateConfigurator(configs, locations, theConfiguratorName, FUNCTION_OBJARRAY_TO_STRING, this::generateMethodRecord2CsvConverter, "generateCsvConfigurator", BeanDirection.COMMON, null, null, null, false, null, BeanDirection.COMMON, directory, fileName);
    }

    public SpecificationFile generateBuilderConfigurator(TemplatesProjectConfiguration configs, String theConfiguratorName, Locations locations, String directory, String fileName) {
        return  generateConfigurator(configs, locations, theConfiguratorName,  get(BUILDER_INTERFACE,CLIENT_PACKAGE), this::generateReturnSelf, "generateReturnSelf", BeanDirection.COMMON, null, null, null, false, null, BeanDirection.COMMON, directory, fileName);
    }

    public SpecificationFile generateSqlInsertConfigurator(TemplatesProjectConfiguration configs, String theConfiguratorName, Locations locations, String directory, String fileName) {
        return  generateConfigurator(configs, locations, theConfiguratorName, STRING, this::generateSqlInsert, "generateSqlInsert", BeanDirection.COMMON, null, null, null, false, null, BeanDirection.COMMON, directory, fileName);
    }
    public SpecificationFile generateConverterConfigurator(TemplatesProjectConfiguration configs, String theConfiguratorName, Locations locations, String directory, String fileName) {
        return  generateConfigurator(configs, locations, theConfiguratorName, FUNCTION_OBJARRAY_TO_ANY, this::generateMethodRecordConverter, "generateConverterConfigurator", BeanDirection.COMMON, null, null, null, false, null, BeanDirection.COMMON, directory, fileName);
    }

    public SpecificationFile generateRecord2RecordConfiguration(TemplatesProjectConfiguration configs, String theConfiguratorName, Locations locations, String directory, String fileName) {
       // internal interfacee generated separately
        // was org.openprovenance.prov.template.compiler.past.type.ClassName record2recordType=get(RECORD_2_RECORD_CONFIGURATOR+"."+RECORD_2_RECORD, locations.getFilePackage(configs.name, RECORD_2_RECORD_CONFIGURATOR));
        org.openprovenance.prov.template.compiler.past.type.ClassName record2recordType=get(RECORD_2_RECORD, locations.getFilePackage(configs.name, RECORD_2_RECORD_CONFIGURATOR));
        return  generateConfigurator(configs, locations, theConfiguratorName, record2recordType, this::generateMethodRecord2RecordConverter, "generateConverterConfigurator", BeanDirection.COMMON, null, null, null, false, null, BeanDirection.COMMON, directory, fileName);
    }
    public SpecificationFile generateEnactorConfigurator(TemplatesProjectConfiguration configs, String theConfiguratorName, Locations locations, String directory, String fileName) {
        return  generateConfigurator(configs, locations, theConfiguratorName, FUNCTION_OBJARRAY_TO_ANY, this::generateMethodEnactor, "generateEnactorConfigurator", BeanDirection.COMMON, get(BEAN_PROCESSOR,locations.getFilePackage(configs.name, BEAN_PROCESSOR)), ENACTOR_VAR, null, false, null, BeanDirection.COMMON, directory, fileName);
    }

    public SpecificationFile generateEnactorConfigurator2(TemplatesProjectConfiguration configs, String theConfiguratorName, String integrator_package, Locations locations, String directory, String fileName) {
        return  generateConfigurator(configs, locations, theConfiguratorName, FUNCTION_OBJARRAY_TO_ANY, new WrapperClass2(locations,this)::generateMethodEnactor2, "generateEnactorConfigurator2", BeanDirection.INPUTS, get(INPUT_OUTPUT_PROCESSOR,locations.getFilePackage(configs.name, INPUT_OUTPUT_PROCESSOR)), ENACTOR_VAR, null, false, integrator_package,BeanDirection.OUTPUTS, directory, fileName);
    }

    public void generateMethodRecord2SqlConverter(String builderParameter, String name, Method mspec, org.openprovenance.prov.template.compiler.past.type.TypeName className, org.openprovenance.prov.template.compiler.past.type.TypeName beanType, org.openprovenance.prov.template.compiler.past.type.TypeName _out) {
        mspec.BODY(RETURN(METHOD_CALL(VARIABLE(builderParameter),"aRecord2SqlConverter"))); //"return $N.aRecord2SqlConverter", builderParameter);
    }

    public void generateMethodRecord2CsvConverter(String builderParameter, String name, Method mspec, org.openprovenance.prov.template.compiler.past.type.TypeName className, org.openprovenance.prov.template.compiler.past.type.TypeName beanType, org.openprovenance.prov.template.compiler.past.type.TypeName _out) {
        mspec.BODY(RETURN(METHOD_CALL(VARIABLE(builderParameter), "processorConverter", List.of(METHOD_CALL(VARIABLE(builderParameter), "aArgs2CsVConverter")))));
    }

    public void generatePropertyOrder(String builderParameter, String name, Method mspec, org.openprovenance.prov.template.compiler.past.type.TypeName className, org.openprovenance.prov.template.compiler.past.type.TypeName beanType, org.openprovenance.prov.template.compiler.past.type.TypeName _out) {
        mspec.addStatement(RETURN(METHOD_CALL(VARIABLE(builderParameter), "getPropertyOrder", List.of()))); //"return $N.getPropertyOrder()", builderParameter);
    }

    public void generateRelation0(String builderParameter, String name, Method mspec, org.openprovenance.prov.template.compiler.past.type.TypeName className, org.openprovenance.prov.template.compiler.past.type.TypeName beanType, org.openprovenance.prov.template.compiler.past.type.TypeName _out) {
        mspec.BODY((RETURN(METHOD_CALL((org.openprovenance.prov.template.compiler.past.type.ClassName)className,"__relations"))));
    }

    public void generateRelation(String builderParameter, String name, Method mspec, org.openprovenance.prov.template.compiler.past.type.TypeName className, org.openprovenance.prov.template.compiler.past.type.TypeName beanType, org.openprovenance.prov.template.compiler.past.type.TypeName _out) {
        mspec.addStatement(RETURN(FUNCTIONAL_METHOD_CALL(VARIABLE(CONVERTER_VAR, FIELD_VARIABLE), "apply", List.of(METHOD_CALL((org.openprovenance.prov.template.compiler.past.type.ClassName)className,"__relations"), METHOD_CALL(VARIABLE(builderParameter), "getPropertyOrder", List.of())))));
    }

    public void generateBuilderProcessor(String builderParameter, String name, Method mspec, org.openprovenance.prov.template.compiler.past.type.TypeName className, org.openprovenance.prov.template.compiler.past.type.TypeName beanType, org.openprovenance.prov.template.compiler.past.type.TypeName _out) {
        mspec.BODY(RETURN(FUNCTIONAL_METHOD_CALL(VARIABLE(PROCESSOR, FIELD_VARIABLE),"apply", List.of(VARIABLE(builderParameter)))));//"return $N.apply($N)", PROCESSOR, builderParameter);
    }

    static class WrapperClass {
        private final Locations locations;

        public void generateObjectRecordMaker(String builderParameter, String fullyQualifiedName, Method mspec, org.openprovenance.prov.template.compiler.past.type.TypeName className, org.openprovenance.prov.template.compiler.past.type.TypeName beanType, org.openprovenance.prov.template.compiler.past.type.TypeName outType) {

            final String backendPackage = locations.getBackendPackage(fullyQualifiedName);

            String backendBuilder = ((org.openprovenance.prov.template.compiler.past.type.ClassName)className).simpleName;

            mspec.BODY(
                    ASSIGNMENT(get(backendBuilder, backendPackage), VARIABLE(TEMPLATE_BUILDER_VARIABLE),
                            CAST(
                                    get(backendBuilder, backendPackage),
                                    METHOD_CALL(
                                            VARIABLE(DISPATCHER_VAR, FIELD_VARIABLE),
                                            "get",
                                            List.of(METHOD_CALL(VARIABLE(builderParameter), "getName", List.of()))
                                    ))),

                    RETURN(LAMBDA(PARAMETER("record", OBJECT_ARRAY))
                            .BODY(RETURN(METHOD_CALL(
                                            VARIABLE(TEMPLATE_BUILDER_VARIABLE),
                                            "make",
                                            List.of(VARIABLE("record"), METHOD_CALL(VARIABLE(TEMPLATE_BUILDER_VARIABLE), "getTypedRecord", List.of()))
                                    )
                            )))  );
        }

        WrapperClass(Locations locations) {
            this.locations = locations;
        }
    }


    static class WrapperClass2 {
        private final Locations locations;
        private final CompilerConfigurations compilerConfigurations;

        public void generateMethodEnactor2(String builderParameter, String fullyQualifiedName, Method mspec, org.openprovenance.prov.template.compiler.past.type.TypeName className, org.openprovenance.prov.template.compiler.past.type.TypeName _inType, org.openprovenance.prov.template.compiler.past.type.TypeName _outType) {
            String inPackage=locations.getBeansPackage(fullyQualifiedName, BeanDirection.INPUTS);
            org.openprovenance.prov.template.compiler.past.type.ClassName inType=get(((org.openprovenance.prov.template.compiler.past.type.ClassName)_inType).simpleName,inPackage);
            String outPackage=locations.getBeansPackage(fullyQualifiedName, BeanDirection.OUTPUTS);
            org.openprovenance.prov.template.compiler.past.type.ClassName outType=get(((org.openprovenance.prov.template.compiler.past.type.ClassName)_outType).simpleName,inPackage);

            compilerConfigurations.generateMethodEnactor2(builderParameter, fullyQualifiedName, mspec, className, inType, outType);
        }

        WrapperClass2(Locations locations, CompilerConfigurations compilerConfigurations) {
            this.locations = locations;
            this.compilerConfigurations = compilerConfigurations;
        }
    }


    public void generateInputPropertyOrder(String builderParameter, String name, Method mspec, org.openprovenance.prov.template.compiler.past.type.TypeName className, org.openprovenance.prov.template.compiler.past.type.TypeName beanType, org.openprovenance.prov.template.compiler.past.type.TypeName _out) {
        mspec.addStatement(RETURN(METHOD_CALL(VARIABLE(builderParameter), "getInputs", List.of())));//"return $N.getInputs()", builderParameter);
    }
    public void generateOutputPropertyOrder(String builderParameter, String name, Method mspec,  org.openprovenance.prov.template.compiler.past.type.TypeName className,  org.openprovenance.prov.template.compiler.past.type.TypeName beanType,  org.openprovenance.prov.template.compiler.past.type.TypeName _out) {
        mspec.addStatement(RETURN(METHOD_CALL(VARIABLE(builderParameter), "getOutputs", List.of()))); //"return $N.getOutputs()", builderParameter);
    }
    public void generateSqlInsert(String builderParameter, String name, Method mspec, org.openprovenance.prov.template.compiler.past.type.TypeName className, org.openprovenance.prov.template.compiler.past.type.TypeName beanType, org.openprovenance.prov.template.compiler.past.type.TypeName _out) {
        mspec.BODY(RETURN(METHOD_CALL(VARIABLE(builderParameter), "getSQLInsert", List.of()))); //"return $N.getSQLInsert()", builderParameter);
    }
    public void generateMethodRecordConverter(String builderParameter, String name, Method mspec, org.openprovenance.prov.template.compiler.past.type.TypeName className, org.openprovenance.prov.template.compiler.past.type.TypeName beanType, org.openprovenance.prov.template.compiler.past.type.TypeName _out) {
        mspec.BODY(RETURN(METHOD_CALL(VARIABLE(builderParameter), "aRecord2BeanConverter"))); //"return $N.aRecord2BeanConverter", builderParameter);
    }

    public void generateMethodRecord2RecordConverter(String builderParameter, String name, Method mspec, org.openprovenance.prov.template.compiler.past.type.TypeName className, org.openprovenance.prov.template.compiler.past.type.TypeName beanType, org.openprovenance.prov.template.compiler.past.type.TypeName _out) {
        mspec.BODY(RETURN(LAMBDA(PARAMETER("x", OBJECT_ARRAY))
                .BODY(RETURN(
                        FUNCTIONAL_METHOD_CALL(
                                FUNCTIONAL_METHOD_CALL(METHOD_CALL(
                                                VARIABLE(builderParameter),
                                                "aRecord2BeanConverter"
                                        ),
                                        "apply",
                                        List.of(VARIABLE("x"))),
                                "process",
                                List.of(
                                        METHOD_CALL(
                                                VARIABLE(builderParameter),
                                                "aArgs2RecordConverter",
                                                List.of()
                                        )
                                )
                        )
                ))));
    }



    public void generateMethodEnactor(String builderParameter, String _name, Method mspec, org.openprovenance.prov.template.compiler.past.type.TypeName className, org.openprovenance.prov.template.compiler.past.type.TypeName beanType, org.openprovenance.prov.template.compiler.past.type.TypeName _out) {
        mspec.COMMENT("Generated Automatically by ProvToolbox method $N.$N()", getClass().getName(), "generateMethodEnactor");
        mspec.BODY(

                ASSIGNMENT(FUNCTION_OBJARRAY_TO_TYPE(beanType), VARIABLE("beanConverter"),
                        METHOD_CALL(VARIABLE(builderParameter), "aRecord2BeanConverter")),

                ASSIGNMENT(FUNCTION_OBJARRAY_TO_TYPE(beanType), VARIABLE("enactor"),

                        LAMBDA(PARAMETER("array", OBJECT_ARRAY))
                                .BODY(ASSIGNMENT(beanType, VARIABLE("bean"),
                                                FUNCTIONAL_METHOD_CALL(VARIABLE("beanConverter"), "apply", List.of(VARIABLE("array")))),
                                        RETURN(
                                                FUNCTIONAL_METHOD_CALL(
                                                        VARIABLE(ENACTOR_VAR),
                                                        "process",
                                                        List.of(VARIABLE("bean"))
                                                )
                                        ))),
                RETURN(VARIABLE("enactor"))
        );
    }
    public void generateMethodEnactor2(String builderParameter, String _name, Method mspec, org.openprovenance.prov.template.compiler.past.type.TypeName className, org.openprovenance.prov.template.compiler.past.type.TypeName inputBeanType, org.openprovenance.prov.template.compiler.past.type.TypeName outputBeanType) {

        mspec.COMMENT("Generated Automatically by ProvToolbox method $N.$N()", getClass().getName(), "generateMethodEnactor2");

        mspec.BODY(
                ASSIGNMENT(FUNCTION_OBJARRAY_TO_TYPE(inputBeanType), VARIABLE("beanConverter"),
                        METHOD_CALL(METHOD_CALL(VARIABLE(builderParameter), "getIntegrator", List.of()),
                                "aRecord2InputsConverter")),

                ASSIGNMENT(FUNCTION_OBJARRAY_TO_TYPE(outputBeanType), VARIABLE("enactor"),
                        LAMBDA(PARAMETER("array", OBJECT_ARRAY))
                                .BODY(ASSIGNMENT(inputBeanType, VARIABLE("bean"),
                                                FUNCTIONAL_METHOD_CALL(VARIABLE("beanConverter"), "apply", List.of(VARIABLE("array")))),
                                        RETURN(
                                                FUNCTIONAL_METHOD_CALL(
                                                        VARIABLE(ENACTOR_VAR),
                                                        "process",
                                                        List.of(VARIABLE("bean"))
                                                )
                                        ))),
                RETURN(VARIABLE("enactor")));

    }


    public void generateReturnSelf(String builderParameter, String name, Method mspec, org.openprovenance.prov.template.compiler.past.type.TypeName className, org.openprovenance.prov.template.compiler.past.type.TypeName beanType, org.openprovenance.prov.template.compiler.past.type.TypeName _out) {
        mspec.BODY(RETURN(VARIABLE(builderParameter)));
    }

}