package org.openprovenance.prov.template.compiler;

import org.apache.commons.lang3.tuple.Triple;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.template.compiler.common.BeanDirection;
import org.openprovenance.prov.template.compiler.common.BeanKind;
import org.openprovenance.prov.template.compiler.common.Constants;
import org.openprovenance.prov.template.compiler.configuration.*;
import org.openprovenance.prov.template.compiler.past.*;
import org.openprovenance.prov.template.compiler.past.annotations.JsonIgnoreAnnotation;
import org.openprovenance.prov.template.compiler.past.annotations.OverrideAnnotation;
import org.openprovenance.prov.template.compiler.past.type.ParameterizedType;
import org.openprovenance.prov.template.compiler.past.type.TypeVariable;
import org.openprovenance.prov.template.descriptors.AttributeDescriptor;
import org.openprovenance.prov.template.descriptors.Descriptor;
import org.openprovenance.prov.template.descriptors.NameDescriptor;
import org.openprovenance.prov.template.descriptors.TemplateBindingsSchema;


import javax.lang.model.element.Modifier;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static org.openprovenance.prov.template.compiler.ConfigProcessor.*;
import static org.openprovenance.prov.template.compiler.common.BeanKind.SIMPLE;
import static org.openprovenance.prov.template.compiler.configuration.SpecificationFile.*;
import static org.openprovenance.prov.template.compiler.past.ArrayAccessor.ARRAY_ACCESSOR;
import static org.openprovenance.prov.template.compiler.past.Assignment.ASSIGNMENT;
import static org.openprovenance.prov.template.compiler.past.CastExpression.CAST;
import static org.openprovenance.prov.template.compiler.past.Constant.CONSTANT;
import static org.openprovenance.prov.template.compiler.past.Definition.DEFINITION;
import static org.openprovenance.prov.template.compiler.past.Field.FIELD;
import static org.openprovenance.prov.template.compiler.past.LambdaExpression.LAMBDA;
import static org.openprovenance.prov.template.compiler.past.Method.METHOD;
import static org.openprovenance.prov.template.compiler.past.MethodCall.*;
import static org.openprovenance.prov.template.compiler.past.Parameter.PARAMETER;
import static org.openprovenance.prov.template.compiler.past.PostIncrement.POST_INCREMENT;
import static org.openprovenance.prov.template.compiler.past.Return.RETURN;
import static org.openprovenance.prov.template.compiler.past.Variable.VARIABLE;
import static org.openprovenance.prov.template.compiler.past.type.ClassName.*;
import static  org.openprovenance.prov.template.compiler.past.type.ClassName.STRING;
import static org.openprovenance.prov.template.compiler.past.type.TypeVariable.T;

public class CompilerBeanGenerator {
    public static final String JAVADOC_NO_DOCUMENTATION = "xsd:string";
    public static final String PROCESSOR_PARAMETER_NAME = Constants.GENERATED_VAR_PREFIX + "processor";
    private final CompilerUtil compilerUtil;


    public CompilerBeanGenerator(ProvFactory pFactory) {
        this.compilerUtil=new CompilerUtil(pFactory);
    }


    public SpecificationFile generateBean(TemplatesProjectConfiguration configs, Locations locations, String templateName, String templateFullyQualifiedName, TemplateBindingsSchema bindingsSchema, BeanKind beanKind, BeanDirection beanDirection, String consistOf, List<String> sharing, String extension, String fileName) {
        StackTraceElement stackTraceElement=compilerUtil.thisMethodAndLine();

        String name = compilerUtil.beanNameClass(templateName, beanDirection);
        if (extension!=null) {
            name=name+extension;
        }

        org.openprovenance.prov.template.compiler.past.Class pastClass=compilerUtil.getPastFactory()
                .CLASS(name)
                .MODIFIERS(Modifier.PUBLIC)
                .COMMENT((beanKind==SIMPLE)?"A simple bean for $N": "A composite bean for $N", templateName)
                .COMMENT((beanDirection==BeanDirection.INPUTS)?" that only contains the input of this template."
                        :(beanDirection==BeanDirection.OUTPUTS)?" that only contains the outputs of this template."
                        :" that captures all variables of this template.");

        if (sharing!=null) {
            pastClass.COMMENT("\n This includes shared variables $N.", sharing.toString());
        }

        if (templateFullyQualifiedName==null) {
            System.out.println("$$$$ Warning: templateFullyQualifiedName not specified " + templateName);
            templateFullyQualifiedName=templateName;
        }

        addDeclaredInterfaces(pastClass, configs, templateFullyQualifiedName, beanDirection);

        pastClass.FIELDS(FIELD(Constants.IS_A, STRING)
                .COMMENT("The template name")
                .MODIFIERS(Modifier.PUBLIC, Modifier.FINAL)
                .INITIALIZER(new Constant(templateFullyQualifiedName)));


        Map<String, List<Descriptor>> theVar = bindingsSchema.getVar();

        if (beanDirection == BeanDirection.OUTPUTS) {

            pastClass.FIELDS(FIELD("ID", INTEGER)
                    .COMMENT("Allows for database key to be returned.")
                    .MODIFIERS(Modifier.PUBLIC));
        }



        Map<String, org.openprovenance.prov.template.compiler.past.type.TypeName> rolesInBean=new LinkedHashMap<>();

        for (String key: descriptorUtils.fieldNames(bindingsSchema)) {
            if (beanDirection==BeanDirection.COMMON
                    || (beanKind==BeanKind.COMPOSITE)
                    || (beanDirection==BeanDirection.OUTPUTS && descriptorUtils.isOutput(key, bindingsSchema))
                    || (beanDirection==BeanDirection.INPUTS && (descriptorUtils.isInput(key, bindingsSchema) || sharing!=null && sharing.contains(key)))){

                org.openprovenance.prov.template.compiler.past.type.TypeName fieldType=compilerUtil.getPastTypeForDeclaredType(theVar, key);
                Field field=FIELD(key, fieldType)
                        .MODIFIERS(Modifier.PUBLIC);

                Descriptor descriptor=theVar.get(key).get(0);
                Function<NameDescriptor,Void> nf=
                        (nd) -> {
                            String documentation=nd.getDocumentation()==null? Constants.JAVADOC_NO_DOCUMENTATION : nd.getDocumentation();
                            String type=nd.getType()==null? JAVADOC_NO_DOCUMENTATION : nd.getType();
                            field.COMMENT("$N: $L (expected type: $L)\n", key, documentation, type);
                            if (sharing!=null && sharing.contains(key)) {
                                field.COMMENT("This is a shared variable in a template composition.\n");
                            }
                            return null;
                        };
                Function<AttributeDescriptor,Void> af=
                        (nd) -> {
                            String documentation=nd.getDocumentation()==null? Constants.JAVADOC_NO_DOCUMENTATION : nd.getDocumentation();
                            String type=nd.getType()==null? JAVADOC_NO_DOCUMENTATION : nd.getType();
                            field.COMMENT("$N: $L (expected type: $L)\n", key, documentation, type);
                            if (sharing!=null && sharing.contains(key)) {
                                field.COMMENT("This is a shared variable in a template composition.\n ");
                            }
                            return null;
                        };
                descriptorUtils.getFromDescriptor(descriptor,af,nf);

                pastClass.FIELDS(field);

                String role=descriptorUtils.getFromDescriptor(descriptor, (ad) -> null, NameDescriptor::getRole);
                if (role!=null) {
                    generateRoleAccessors(pastClass, key, role, fieldType);
                    rolesInBean.put(role, fieldType);
                }

            }
        }

        if (beanKind==SIMPLE && extension==null) {
            generateRoleInterfaces(configs, locations, templateFullyQualifiedName, beanDirection, rolesInBean, stackTraceElement);
        }



        String beanPackge=          locations.getBeansPackage(templateFullyQualifiedName, beanDirection);
        String beanProcessorPackage=locations.getBeansPackage(templateFullyQualifiedName, beanDirection);

        if (beanKind== SIMPLE ) {
            Method method = generateInvokeProcessor(templateName, beanProcessorPackage, bindingsSchema, null, beanDirection, beanKind);
            pastClass.METHOD(method);

        } else if (beanKind==BeanKind.COMPOSITE) {

            String variant=null;

            if (sharing!=null) {
                variant = newVariant(consistOf, sharing, configs);
            }

            Method method = generateInvokeProcessor(templateName, beanProcessorPackage, bindingsSchema, ELEMENTS, beanDirection, beanKind);
            pastClass.METHOD(method);

            generateCompositeList(consistOf, beanPackge, locations, pastClass, beanDirection, variant, sharing);
            generateCompositeListExtender(consistOf, beanPackge, locations, pastClass, beanDirection, variant, sharing);
        }


        String directory = locations.convertToDirectory(beanPackge);
        Supplier<Boolean> pythonGenerator=() -> generatePython(pastClass, beanPackge, locations, stackTraceElement);
        Supplier<Boolean> javaGenerator = () -> generateJava(pastClass, beanPackge, configs, directory, stackTraceElement, compilerUtil);
        Supplier<Boolean> jsGenerator = () -> generateJavaScript(pastClass, beanPackge, locations, stackTraceElement);
        Supplier<Boolean> rustGenerator = () -> generateRust(pastClass, beanPackge, locations, stackTraceElement);
        return new SpecificationFile(javaGenerator,pythonGenerator,jsGenerator,rustGenerator);
    }


    public Map<String, Map<String, Triple<String, List<String>, TemplateBindingsSchema>>> variantTable=new HashMap<>();

    String newVariant(String templateFullyQualifiedName, List<String> sharing, TemplatesProjectConfiguration configs) {
        String shared= stringForSharedVariables(sharing);
        variantTable.putIfAbsent(templateFullyQualifiedName,new HashMap<>());
        Triple<String, List<String>, TemplateBindingsSchema> triple = variantTable.get(templateFullyQualifiedName).get(shared);
        if (triple ==null) {
            String extension = "_" + (variantTable.get(templateFullyQualifiedName).keySet().size() + 1);



            TemplateCompilerConfig config=Arrays.stream(configs.templates).filter(c -> Objects.equals(c.fullyQualifiedName, templateFullyQualifiedName)).findFirst().get();
            SimpleTemplateCompilerConfig sConfig=(SimpleTemplateCompilerConfig) config;
            SimpleTemplateCompilerConfig sConfig2=sConfig.cloneAsInstanceInComposition(sConfig.name+extension,templateFullyQualifiedName+extension, null);

            TemplateBindingsSchema bindingsSchema=compilerUtil.getBindingsSchema(sConfig2);

            variantTable.get(templateFullyQualifiedName).put(shared, Triple.of(extension,sharing,bindingsSchema));
            return extension;

        } else {
            return triple.getLeft();
        }
    }

    private String stringForSharedVariables(List<String> sharing) {
        return sharing.stream().sorted().collect(Collectors.joining("_"));
    }


    /**
     * Adds the interfaces declared in the template's {@link ImplementInterfaces} configuration
     * to the generated bean, selected by bean direction: {@code plain} for the full bean
     * (COMMON), {@code input} for {@code *Inputs}, {@code output} for {@code *Outputs}.
     *
     * <p>Interface names are fully qualified; the generator adds the {@code implements}
     * clause only — javac verifies at build time that the generated accessors satisfy the
     * interface's abstract methods.</p>
     */
    private void addDeclaredInterfaces(org.openprovenance.prov.template.compiler.past.Class pastClass,
                                       TemplatesProjectConfiguration configs,
                                       String templateFullyQualifiedName,
                                       BeanDirection beanDirection) {
        ImplementInterfaces interfaces=declaredInterfaces(configs, templateFullyQualifiedName);
        if (interfaces==null) return;
        List<String> names=interfacesForDirection(interfaces, beanDirection);
        if (names==null) return;
        for (String fqn: names) {
            int dot=fqn.lastIndexOf('.');
            if (dot<0) {
                throw new IllegalStateException("Interface name '" + fqn + "' declared for template "
                        + templateFullyQualifiedName + " must be fully qualified");
            }
            pastClass.INTERFACES(org.openprovenance.prov.template.compiler.past.type.ClassName
                    .get(fqn.substring(dot+1), fqn.substring(0, dot)));
        }
        Method isaGetter=METHOD(Constants.IS_A)
                .commentFileLocation()
                .MODIFIERS(Modifier.PUBLIC)
                .RETURNS(STRING)
                .COMMENT("Returns the template name for this bean.")
                .BODY(RETURN(VARIABLE(Constants.IS_A, Variable.VariableKind.FIELD_VARIABLE)));
        pastClass.METHOD(isaGetter);
    }

    /** Looks up the {@link ImplementInterfaces} declared for a template, or null. */
    private ImplementInterfaces declaredInterfaces(TemplatesProjectConfiguration configs, String templateFullyQualifiedName) {
        if (configs==null || configs.templates==null) return null;
        return Arrays.stream(configs.templates)
                .filter(c -> c instanceof SimpleTemplateCompilerConfig)
                .map(c -> (SimpleTemplateCompilerConfig) c)
                .filter(c -> Objects.equals(c.fullyQualifiedName, templateFullyQualifiedName))
                .findFirst()
                .map(c -> c.interfaces)
                .orElse(null);
    }

    /** Selects the interface names for a bean direction: plain=COMMON, input=INPUTS, output=OUTPUTS. */
    private List<String> interfacesForDirection(ImplementInterfaces interfaces, BeanDirection beanDirection) {
        return switch (beanDirection) {
            case COMMON  -> interfaces.plain;
            case INPUTS  -> interfaces.input;
            case OUTPUTS -> interfaces.output;
        };
    }

    /** Registry of already-generated role interfaces: FQN → (role → accessor type). */
    private final Map<String, Map<String,String>> generatedRoleInterfaces=new HashMap<>();

    /**
     * Generates the interfaces declared with {@code "generate": true} into the generated
     * source tree, deriving one getter/setter pair per {@code @role} present in the bean
     * direction. An interface declared by several templates is generated once; declaring
     * the same interface with a different role signature is an error.
     */
    private void generateRoleInterfaces(TemplatesProjectConfiguration configs,
                                        Locations locations,
                                        String templateFullyQualifiedName,
                                        BeanDirection beanDirection,
                                        Map<String, org.openprovenance.prov.template.compiler.past.type.TypeName> rolesInBean,
                                        StackTraceElement stackTraceElement) {
        ImplementInterfaces interfaces=declaredInterfaces(configs, templateFullyQualifiedName);
        if (interfaces==null || !interfaces.generate) return;
        List<String> names=interfacesForDirection(interfaces, beanDirection);
        if (names==null) return;
        for (String fqn: names) {
            Map<String,String> signature=new LinkedHashMap<>();
            rolesInBean.forEach((role, type) -> signature.put(role, String.valueOf(type)));
            Map<String,String> previous=generatedRoleInterfaces.putIfAbsent(fqn, signature);
            if (previous!=null) {
                if (!previous.equals(signature)) {
                    throw new IllegalStateException("Interface " + fqn + " is declared with conflicting role signatures: "
                            + previous + " vs " + signature + " (template " + templateFullyQualifiedName + ")");
                }
                continue; // identical declaration — already generated
            }
            int dot=fqn.lastIndexOf('.');
            if (dot<0) {
                throw new IllegalStateException("Interface name '" + fqn + "' declared for template "
                        + templateFullyQualifiedName + " must be fully qualified");
            }
            String packge=fqn.substring(0, dot);
            String simpleName=fqn.substring(dot+1);

            org.openprovenance.prov.template.compiler.past.Class intface=compilerUtil.getPastFactory()
                    .INTERFACE(simpleName)
                    .MODIFIERS(Modifier.PUBLIC)
                    .COMMENT("Role-accessor contract generated from the @role declarations of template $N.", templateFullyQualifiedName)
                    .COMMENT((beanDirection==BeanDirection.INPUTS)?"\nImplemented by the inputs bean."
                            :(beanDirection==BeanDirection.OUTPUTS)?"\nImplemented by the outputs bean."
                            :"\nImplemented by the full bean.");

            // Every bean declaring interfaces also gets an isA() accessor (template name);
            // declaring it here lets generic engine code identify the template through the interface.
            // (Deliberately not a JavaBean getter, so Jackson leaves the wire format untouched.)
            intface.METHOD(METHOD(Constants.IS_A)
                    .MODIFIERS(Modifier.PUBLIC, Modifier.ABSTRACT)
                    .RETURNS(STRING)
                    .COMMENT("Returns the template name for this bean."));

            rolesInBean.forEach((role, type) -> {
                String suffix=compilerUtil.capitalize(role);
                intface.METHOD(METHOD("get" + suffix)
                        .MODIFIERS(Modifier.PUBLIC, Modifier.ABSTRACT)
                        .RETURNS(type)
                        .COMMENT("Role accessor ($N).", role));
                intface.METHOD(METHOD("set" + suffix)
                        .MODIFIERS(Modifier.PUBLIC, Modifier.ABSTRACT)
                        .RETURNS(VOID)
                        .PARAMETER(type, "value")
                        .COMMENT("Role mutator ($N).", role));
            });

            String directory=locations.convertToDirectory(packge);
            Supplier<Boolean> javaGenerator=() -> generateJava(intface, packge, configs, directory, stackTraceElement, compilerUtil);
            new SpecificationFile(javaGenerator, emptyGenerator, emptyGenerator, emptyGenerator).save();
        }
    }


    /** The roles accepted in a variable's {@code @role} declaration. */
    public static final Set<String> KNOWN_ROLES =
            Set.of("cause", "effect", "general", "cause2", "effect2", "general2");

    /**
     * Generates uniform role-based accessors for a variable that declares a {@code @role}.
     * For a variable {@code key} with role {@code cause} this emits {@code getCause()} /
     * {@code setCause(value)} getting or setting the underlying field.
     */
    private void generateRoleAccessors(org.openprovenance.prov.template.compiler.past.Class pastClass,
                                       String key,
                                       String role,
                                       org.openprovenance.prov.template.compiler.past.type.TypeName fieldType) {
        if (!KNOWN_ROLES.contains(role)) {
            throw new IllegalStateException("Unknown @role '" + role + "' for variable '" + key
                    + "': expected one of " + KNOWN_ROLES);
        }
        String suffix=compilerUtil.capitalize(role);

        Method getter=METHOD("get" + suffix)
                .commentFileLocation()
                .MODIFIERS(Modifier.PUBLIC)
                .ANNOTATIONS(JsonIgnoreAnnotation.NAME)
                .RETURNS(fieldType)
                .COMMENT("Role accessor ($N) for variable $N.", role, key)
                .BODY(RETURN(VARIABLE(key, Variable.VariableKind.FIELD_VARIABLE)));
        pastClass.METHOD(getter);

        Method setter=METHOD("set" + suffix)
                .commentFileLocation()
                .MODIFIERS(Modifier.PUBLIC)
                .ANNOTATIONS(JsonIgnoreAnnotation.NAME)
                .RETURNS(VOID)
                .PARAMETER(fieldType, "value")
                .COMMENT("Role mutator ($N) for variable $N.", role, key)
                .BODY(ASSIGNMENT(VARIABLE(key, Variable.VariableKind.FIELD_VARIABLE), VARIABLE("value")));
        pastClass.METHOD(setter);
    }


    private void generateCompositeList(String templateName, String packge, Locations locations, org.openprovenance.prov.template.compiler.past.Class builder, BeanDirection beanDirection, String variant, List<String> sharing) {

        String shortName=locations.getShortNames().get(templateName);
        String name = compilerUtil.beanNameClass(shortName, beanDirection, variant);

        org.openprovenance.prov.template.compiler.past.type.ClassName consistsOfClass = org.openprovenance.prov.template.compiler.past.type.ClassName.get(name, packge);
        ParameterizedType elementList=ParameterizedType.get(org.openprovenance.prov.template.compiler.past.type.ClassName.LIST, consistsOfClass);
        Field b1=FIELD(ELEMENTS, elementList)
                .MODIFIERS(Modifier.PUBLIC)
                .INITIALIZER(CONSTRUCTOR_CALL(
                        // Declaring LinkedList as parameterized type with no argument <> to be able to generate new $T<>()
                        ParameterizedType.get(org.openprovenance.prov.template.compiler.past.type.ClassName.LINKED_LIST), new LinkedList<>()))
                .COMMENT("List of composed templates generated Automatically by ProvToolbox ($N.$N()) for template $N.",
                        this.getClass().getSimpleName(), "generateCompositeList", templateName);

        if(variant!=null) {
            b1.COMMENT("\nVariant $N for shared variables $N ($N).", variant, stringForSharedVariables(sharing), sharing.toString());
        }

        builder.FIELDS(b1);


        ParameterizedType classofUnknown1= ParameterizedType.get(CLASS, TypeVariable.get("?"));

        Field b2=new Field("consistsOf", STRING)
                .MODIFIERS(Modifier.PUBLIC)
                .COMMENT("Class of elements inside this composite")
                .INITIALIZER(CONSTANT(consistsOfClass.packge + "." + consistsOfClass.simpleName));
        builder.FIELDS(b2);

        if (variant!=null) {
            for (String shared : sharing) {
                generateMutatorForSharedVariables(templateName, packge, builder, beanDirection, variant, shared, name, consistsOfClass);
                generateMutatorForDistinctVariables(templateName, packge, builder, beanDirection, variant, shared, name, consistsOfClass);
            }
        }

    }


    private void generateMutatorForDistinctVariables(String templateName, String packge, org.openprovenance.prov.template.compiler.past.Class builder, BeanDirection beanDirection, String variant, String shared, String name, org.openprovenance.prov.template.compiler.past.type.ClassName consistsOfClass) {
        Method method = METHOD("distinct" + compilerUtil.capitalize(shared))
                .PARAMETER(INTEGER, "v")
                .MODIFIERS(Modifier.PUBLIC)
                .RETURNS(VOID);
        compilerUtil.debugFileLocation(method);

        String countName="__count";

        method.BODY(
                DEFINITION(
                        intArray,
                        VARIABLE(countName),
                        new org.openprovenance.prov.template.compiler.past.ArrayInitialiser(_int,List.of(VARIABLE("v"))))
                        .addModifier(Modifier.FINAL),
                METHOD_CALL(
                        VARIABLE(ELEMENTS, Variable.VariableKind.FIELD_VARIABLE),
                        "forEach",
                        LAMBDA(PARAMETER("b", consistsOfClass))
                                .BODY(ASSIGNMENT(
                                        METHOD_CALL(VARIABLE("b"), shared),
                                        POST_INCREMENT(ARRAY_ACCESSOR(VARIABLE(countName), CONSTANT(0)),-1)))));
        builder.METHOD(method);
    }


    private void generateMutatorForSharedVariables(String templateName, String packge, org.openprovenance.prov.template.compiler.past.Class builder, BeanDirection beanDirection, String variant, String shared, String name, org.openprovenance.prov.template.compiler.past.type.ClassName consistsOfClass) {

        Method method=METHOD("shareAll" + compilerUtil.capitalize(shared))
                .PARAMETER(INTEGER, "v")
                .MODIFIERS(Modifier.PUBLIC)
                .RETURNS(VOID);
        compilerUtil.debugFileLocation(method);

        method.BODY(
                METHOD_CALL(
                        VARIABLE(ELEMENTS, Variable.VariableKind.FIELD_VARIABLE),
                        "forEach",
                        LAMBDA(PARAMETER("b", consistsOfClass))
                                .BODY(ASSIGNMENT(
                                        METHOD_CALL(VARIABLE("b"), shared),
                                        VARIABLE("v")))));



        builder.METHOD(method);
    }




    private void generateCompositeListExtender(String templateName, String packge, Locations locations, org.openprovenance.prov.template.compiler.past.Class builder, BeanDirection beanDirection, String variant, List<String> sharing) {
        String shortName=locations.getShortNames().get(templateName);
        String name = compilerUtil.beanNameClass(shortName, beanDirection, variant);

        Method method=METHOD(ADD_ELEMENTS)
                .MODIFIERS(Modifier.PUBLIC)
                .PARAMETER(org.openprovenance.prov.template.compiler.past.type.ClassName.OBJECT, "o")
                .RETURNS(VOID);
        compilerUtil.debugFileLocation(method);

        method.BODY(METHOD_CALL(
                new Variable(ELEMENTS, Variable.VariableKind.FIELD_VARIABLE),
                "add", CAST(org.openprovenance.prov.template.compiler.past.type.ClassName.get(name, packge), VARIABLE("o"))));
        builder.METHOD(method);
    }






    private org.openprovenance.prov.template.compiler.past.type.TypeName processorClassType(String template, String packge, BeanDirection beanDirection) {
        return ParameterizedType.get(org.openprovenance.prov.template.compiler.past.type.ClassName.get(compilerUtil.processorNameClass(template,beanDirection),packge), T());
    }
    public Method generateInvokeProcessor(String template, String processorPackage, TemplateBindingsSchema bindingsSchema, String elements, BeanDirection beanDirection, BeanKind beanKind) {

        List<String> fieldNames = (List<String>) descriptorUtils.fieldNames(bindingsSchema);
        if (fieldNames.contains(PROCESSOR_PARAMETER_NAME)) {
            throw new IllegalStateException("Template " + template + " contains variable " + PROCESSOR_PARAMETER_NAME + " " + fieldNames);
        }

        Method method=METHOD(PROCESSOR_PROCESS_METHOD_NAME)
                .MODIFIERS(Modifier.PUBLIC)
                .commentFileLocation()
                .RETURNS(T())
                .addTypeVariables(T())
                .PARAMETER(processorClassType(template,processorPackage,beanDirection), PROCESSOR_PARAMETER_NAME);

        List<String> actualFieldNames;
        if (elements!=null) {
            actualFieldNames=new LinkedList<>(fieldNames);
            actualFieldNames.add(elements);
        } else {
            actualFieldNames=fieldNames;
        }

        if (beanDirection==BeanDirection.COMMON) {
            method.addStatement(RETURN(
                    FUNCTIONAL_METHOD_CALL(
                            VARIABLE(PROCESSOR_PARAMETER_NAME),
                            PROCESSOR_PROCESS_METHOD_NAME,
                            actualFieldNames
                                    .stream()
                                    .map(field -> VARIABLE(field, Variable.VariableKind.FIELD_VARIABLE))
                                    .collect(Collectors.toList()))));
        } else if (beanDirection==BeanDirection.INPUTS) {
            method.addStatement(RETURN(
                    FUNCTIONAL_METHOD_CALL(
                            VARIABLE(PROCESSOR_PARAMETER_NAME),
                            PROCESSOR_PROCESS_METHOD_NAME,
                            actualFieldNames
                                    .stream()
                                    .filter(field -> (descriptorUtils.isInput(field,bindingsSchema) || beanKind==BeanKind.COMPOSITE) )
                                    . map(field -> VARIABLE(field, Variable.VariableKind.FIELD_VARIABLE))
                                    .collect(Collectors.toList()))));
        } else if (beanDirection==BeanDirection.OUTPUTS) {
            method.addStatement(RETURN(
                    FUNCTIONAL_METHOD_CALL(
                            VARIABLE(PROCESSOR_PARAMETER_NAME),
                            PROCESSOR_PROCESS_METHOD_NAME,
                            actualFieldNames
                                    .stream()
                                    .filter(field -> (descriptorUtils.isOutput(field,bindingsSchema) || beanKind==BeanKind.COMPOSITE))
                                    .map (field ->VARIABLE(field, Variable.VariableKind.FIELD_VARIABLE))
                                    .collect(Collectors.toList()))));
        } else {
            throw new IllegalStateException("Unexpected value: " + beanDirection);
        }

        return method;
    }


    public void generateSimpleConfigsWithVariants(Locations locations, TemplatesProjectConfiguration configs) {
        variantTable.keySet().forEach(
                templateFullyQualifiedName -> {
                    Map<String, Triple<String, List<String>, TemplateBindingsSchema>> allVariants=variantTable.get(templateFullyQualifiedName);
                    allVariants.keySet().forEach(
                            shared -> {
                                Triple<String, List<String>, TemplateBindingsSchema> pair=allVariants.get(shared);
                                String extension=pair.getLeft();
                                List<String> sharing=pair.getMiddle();
                                TemplateBindingsSchema bindingsSchema = pair.getRight();

                                // find in configs the template configuration with qualifiedName templateFullyQualifiedName
                                SimpleTemplateCompilerConfig config=Arrays.stream(configs.templates)
                                        .filter(c -> c instanceof SimpleTemplateCompilerConfig)
                                        .map(c -> (SimpleTemplateCompilerConfig) c)
                                        .filter(c -> Objects.equals(c.fullyQualifiedName, templateFullyQualifiedName))
                                        .findFirst().get();

                                System.out.println("Generating variant bean for template " + templateFullyQualifiedName + " with extension " + extension + " for shared variables " + sharing + " with name " + config.name);
                                SpecificationFile spec=generateBean(configs, locations, config.name, templateFullyQualifiedName, bindingsSchema, SIMPLE, BeanDirection.INPUTS, null, sharing, extension, compilerUtil.beanNameClass(config.name,BeanDirection.INPUTS,extension)+DOT_JAVA_EXTENSION);
                                spec.save();

                                }
                    );
                }
        );


    }
}
