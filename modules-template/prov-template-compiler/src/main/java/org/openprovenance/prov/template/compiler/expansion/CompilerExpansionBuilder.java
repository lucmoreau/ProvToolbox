package org.openprovenance.prov.template.compiler.expansion;


import org.apache.commons.lang3.tuple.Pair;
import org.openprovenance.prov.model.Statement;
import org.openprovenance.prov.model.*;
import org.openprovenance.prov.model.exception.InvalidCaseException;
import org.openprovenance.prov.model.extension.QualifiedHadMember;
import org.openprovenance.prov.template.compiler.*;
import org.openprovenance.prov.template.compiler.common.BeanDirection;
import org.openprovenance.prov.template.compiler.common.CompilerCommon;
import org.openprovenance.prov.template.compiler.common.Constants;
import org.openprovenance.prov.template.compiler.configuration.Locations;
import org.openprovenance.prov.template.compiler.configuration.SpecificationFile;
import org.openprovenance.prov.template.compiler.configuration.TemplatesProjectConfiguration;
import org.openprovenance.prov.template.compiler.past.*;
import org.openprovenance.prov.template.compiler.past.emitter.Poet;
import org.openprovenance.prov.template.descriptors.*;
import org.openprovenance.prov.template.core.InstantiateUtil;
import org.openprovenance.prov.template.core.exception.MissingAttributeValue;
import org.openprovenance.prov.template.types.TypesRecordProcessor;

import javax.lang.model.element.Modifier;
import java.lang.Class;
import java.util.*;
import java.util.Iterator;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static org.openprovenance.prov.template.compiler.CompilerUtil.u;
import static org.openprovenance.prov.template.compiler.ConfigProcessor.descriptorUtils;
import static org.openprovenance.prov.template.compiler.common.Constants.DOT_JAVA_EXTENSION;
import static org.openprovenance.prov.template.compiler.common.Constants.PROCESSOR;
import static org.openprovenance.prov.template.compiler.configuration.SpecificationFile.generateJava;
import static org.openprovenance.prov.template.compiler.past.ArrayAccessor.ARRAY_ACCESSOR;
import static org.openprovenance.prov.template.compiler.past.Assignment.ASSIGNMENT;
import static org.openprovenance.prov.template.compiler.past.CastExpression.CAST;
import static org.openprovenance.prov.template.compiler.past.BinaryOp.BINARY_OP;
import static org.openprovenance.prov.template.compiler.past.BinaryOp.EQ;
import static org.openprovenance.prov.template.compiler.past.Constant.getNull;
import static org.openprovenance.prov.template.compiler.past.Constant.CONSTANT;
import static org.openprovenance.prov.template.compiler.past.Constructor.CONSTRUCTOR;
import static org.openprovenance.prov.template.compiler.past.Field.FIELD;
import org.openprovenance.prov.template.compiler.past.ArrayInitialiser;
import org.openprovenance.prov.template.compiler.past.LambdaExpression;
import org.openprovenance.prov.template.compiler.past.MethodCall;
import org.openprovenance.prov.template.compiler.past.Parameter;

import static org.openprovenance.prov.template.compiler.past.IfExpression.IF_;
import static org.openprovenance.prov.template.compiler.past.IfStatement.IF;
import static org.openprovenance.prov.template.compiler.past.Method.METHOD;
import static org.openprovenance.prov.template.compiler.past.MethodCall.CONSTRUCTOR_CALL;
import static org.openprovenance.prov.template.compiler.past.MethodCall.METHOD_CALL;
import static org.openprovenance.prov.template.compiler.past.Return.RETURN;
import static org.openprovenance.prov.template.compiler.past.Variable.VARIABLE;
import static org.openprovenance.prov.template.compiler.past.type.ClassName.*;
import static org.openprovenance.prov.template.compiler.past.type.TypeVariable.T;
import static org.openprovenance.prov.template.core.InstantiateUtil.*;

public class CompilerExpansionBuilder {
    private final CompilerUtil compilerUtil;
    private final ProvFactory pFactory;
    private final boolean withMain;
    private final CompilerCommon compilerCommon;
    private final CompilerTypeManagement compilerTypeManagement;
    private final PastFactory pastFactory;



    public CompilerExpansionBuilder(boolean withMain, CompilerCommon compilerCommon, ProvFactory pFactory, boolean debugComment, CompilerTypeManagement compilerTypeManagement) {
        this.pFactory=pFactory;
        this.withMain=withMain;
        this.compilerCommon = compilerCommon;
        this.compilerTypeManagement=compilerTypeManagement;
        this.compilerUtil=new CompilerUtil(pFactory);
        this.pastFactory=compilerUtil.getPastFactory();
    }




    public SpecificationFile generateBuilderSpecification(TemplatesProjectConfiguration configs, Locations locations, Document doc, String name, String templateName, String templateFullyQualifiedName, String packge, TemplateBindingsSchema bindingsSchema, Map<Integer, List<Integer>> successorTable, String directory, String fileName) {


        Bundle bun = u.getBundle(doc).get(0);

        Set<QualifiedName> allVars = new HashSet<>();
        Set<QualifiedName> allAtts = new HashSet<>();

        compilerUtil.extractVariablesAndAttributes(bun, allVars, allAtts, pFactory);

        return generateBuilderSpecification_aux(configs, locations, doc, new ArrayList<>(allVars), new ArrayList<>(allAtts), name, templateName, templateFullyQualifiedName, packge, bindingsSchema, successorTable, directory, fileName);

    }



    SpecificationFile generateBuilderSpecification_aux(TemplatesProjectConfiguration configs, Locations locations, Document doc, Collection<QualifiedName> allVars, Collection<QualifiedName> allAtts, String name, String templateName, String templateFullyQualifiedName, String packge, TemplateBindingsSchema bindingsSchema, Map<Integer, List<Integer>> successorTable, String directory, String fileName) {
        StackTraceElement stackTraceElement = compilerUtil.thisMethodAndLine();

        //TypeSpec.Builder builder = compilerUtil.generateClassBuilder2(name);


        org.openprovenance.prov.template.compiler.past.Class pastClass =
                pastFactory.CLASS(name)
                        .SUPERCLASS(PROV_FILE_BUILDER)
                        .INTERFACES(PROV_PROXY_CLIENT_ACCESSOR)
                        .MODIFIERS(Modifier.PUBLIC)
                        .FIELDS(
                                FIELD("pf", PROV_FACTORY).MODIFIERS(Modifier.PRIVATE, Modifier.FINAL),
                                FIELD("vc", PROV_VALUE_CONVERTER).MODIFIERS(Modifier.PRIVATE, Modifier.FINAL)
                        );

        pastClass.METHOD(compilerCommon.generateNameAccessor(templateName));
        pastClass.METHOD(compilerCommon.generateFullyQualifiedNameAccessor(templateFullyQualifiedName));
        Hashtable<QualifiedName, String> vmap = generateQualifiedNames(doc, pastClass);
        pastClass.METHOD(generateTemplateGenerator(allVars, allAtts, doc, vmap, bindingsSchema, true));

        pastClass.CONSTRUCTOR(generateConstructor2(vmap));
        pastClass.METHOD(commonAccessorGenerato(templateName, locations.getBeansPackage(templateFullyQualifiedName, BeanDirection.COMMON)));
        if (withMain) pastClass.METHOD(generateMain(allVars, allAtts, name, packge, bindingsSchema));
        pastClass.METHOD(generateFactoryMethod(allVars, allAtts, name, bindingsSchema));
        pastClass.METHOD(generateFactoryMethodWithContinuation(allVars, allAtts, name, templateName, packge, bindingsSchema));
        pastClass.METHOD(generateFactoryMethodWithArray(allVars, allAtts, name, bindingsSchema));
        pastClass.METHOD(generateFactoryMethodWithArrayAndContinuation(name, templateName, packge, bindingsSchema));
        pastClass.METHOD(generateUniqueRecordFactoryMethodWithArrayAndContinuation(name, templateName, packge, bindingsSchema));
        pastClass.METHOD(typedRecordGenerator(templateName, packge));
        pastClass.METHOD(typeManagerGenerator(templateName, packge));
        pastClass.METHOD(generateTypePropagatorN());
        pastClass.METHOD(generateTypePropagator(packge + ".client", bindingsSchema, successorTable));
        
        //builder.addMethod(generateTemplateGenerator_old(allVars, allAtts, doc, vmap, bindingsSchema));
        // builder.addMethod(generateTypePropagator_old(packge+".client", bindingsSchema, successorTable));

        Supplier<Boolean> pythonGenerator = () -> true;
        Supplier<Boolean> javaGenerator = () -> generateJava(pastClass, packge, configs, fileName + DOT_JAVA_EXTENSION, directory, stackTraceElement, compilerUtil);

        return new SpecificationFile(javaGenerator, pythonGenerator);
    }


    /**
     * Returns a PAST {@link Constructor} .
     */
    public Constructor generateConstructor2(Hashtable<QualifiedName, String> vmap) {
        Constructor constructor = CONSTRUCTOR()
                .MODIFIERS(Modifier.PUBLIC)
                .PARAMETER(PROV_FACTORY, "pf")
                .debugFileLocation();

        // this.pf = pf
        constructor.BODY(ASSIGNMENT(null, METHOD_CALL(VARIABLE("this"), "pf"), VARIABLE("pf")));

        // For each qualified name in the map: this.varName = pf.newQualifiedName(nsURI, localPart, prefix)
        for (Map.Entry<QualifiedName, String> e : vmap.entrySet()) {
            final QualifiedName q = e.getKey();
            constructor.BODY(
                    ASSIGNMENT(null,
                            METHOD_CALL(VARIABLE("this"), e.getValue()),
                            METHOD_CALL(VARIABLE("pf"), "newQualifiedName",
                                    CONSTANT(q.getNamespaceURI()),
                                    CONSTANT(q.getLocalPart()),
                                    CONSTANT(q.getPrefix()))));
        }

        constructor.BODY(
                // this.vc = new ValueConverter(pf)
                ASSIGNMENT(null,
                        METHOD_CALL(VARIABLE("this"), "vc"),
                        CONSTRUCTOR_CALL(PROV_VALUE_CONVERTER, List.of(VARIABLE("pf")))),

                // register(this)
                METHOD_CALL("register", List.of(VARIABLE("this"))));

        return constructor;
    }

    /**
     * Returns a PAST {@link Method} .
     */
    public Method commonAccessorGenerato(String templateName, String packge) {
        Method method = METHOD("getClientBuilder")
                .commentFileLocation()
                .MODIFIERS(Modifier.PUBLIC)
                .RETURNS(BUILDER_INTERFACE)
                .BODY(
                        RETURN(CONSTRUCTOR_CALL(
                                org.openprovenance.prov.template.compiler.past.type.ClassName.get(
                                        compilerUtil.templateNameClass(templateName), packge),
                                List.of())));

        return method;
    }

    /**
     * Returns a PAST {@link org.openprovenance.prov.template.compiler.past.Expression}
     */
    public org.openprovenance.prov.template.compiler.past.Expression createExamplarExpression(Map<String, List<Descriptor>> theVars, String key, int num, ProvFactory pFactory) {
        List<Descriptor> descriptors = theVars.get(key);
        Descriptor descriptor = (descriptors == null) ? null : descriptors.get(0);
        Object examplar = (descriptor == null) ? null : descriptorUtils.getFromDescriptor(descriptor, AttributeDescriptor::getExamplar, NameDescriptor::getExamplar);

        if (examplar != null) {
            Class<?> declaredJavaType = compilerUtil.getJavaTypeForDeclaredType(theVars, key);
            final String converter = compilerUtil.getConverterForDeclaredType2(declaredJavaType);
            if (converter == null) {
                return CONSTANT(examplar.toString());
            } else {
                // converter(examplar) — e.g., Integer.valueOf(123)
                String[] parts = converter.split("\\.");
                if (parts.length == 2) {
                    org.openprovenance.prov.template.compiler.past.type.ClassName cls =
                            org.openprovenance.prov.template.compiler.past.type.ClassName.get(parts[0], "past.lang");
                    return METHOD_CALL(cls, parts[1], CONSTANT(examplar.toString()));
                }
                return CONSTANT(examplar.toString());
            }
        } else {
            if (descriptor != null) {
                switch (descriptor.getDescriptorType()) {
                    case NAME:
                        Class<?> declaredJavaType = compilerUtil.getJavaTypeForDeclaredType(theVars, key);
                        final String converter = compilerUtil.getConverterForDeclaredType2(declaredJavaType);
                        if (converter == null) {
                            return CONSTANT("v" + num);
                        } else {
                            String example = compilerUtil.generateExampleForType(
                                    descriptorUtils.getFromDescriptor(descriptor, AttributeDescriptor::getType, NameDescriptor::getType),
                                    key, pFactory);
                            String[] parts = converter.split("\\.");
                            if (parts.length == 2) {
                                org.openprovenance.prov.template.compiler.past.type.ClassName cls =
                                        org.openprovenance.prov.template.compiler.past.type.ClassName.get(parts[0], "past.lang");
                                return METHOD_CALL(cls, parts[1], CONSTANT(example));
                            }
                            return CONSTANT(example);
                        }
                    case ATTRIBUTE:
                        AttributeDescriptor ad = ((AttributeDescriptorList) descriptor).getItems().get(0);
                        String hasType = ad.getType();
                        if (hasType != null) {
                            return switch (hasType) {
                                case "xsd:int" -> CONSTANT(num);
                                case "xsd:long" -> CONSTANT(Long.valueOf(num));
                                case "xsd:string" -> CONSTANT("v" + num);
                                case "xsd:boolean" -> CONSTANT(true);
                                case "xsd:float" -> new org.openprovenance.prov.template.compiler.past.Constant(Float.valueOf((float) (num + 0.01)));
                                case "xsd:double" -> new org.openprovenance.prov.template.compiler.past.Constant(Double.valueOf(num + 0.01));
                                case "xsd:date", "xsd:dateTime" -> CONSTANT(pFactory.newTimeNow().toXMLFormat());
                                default -> throw new UnsupportedOperationException("createExamplarExpression: unsupported type " + hasType);
                            };
                        } else {
                            throw new UnsupportedOperationException("No type for " + key);
                        }
                    default:
                        throw new UnsupportedOperationException("This exception is never reached");
                }
            } else {
                throw new UnsupportedOperationException("cannot find null descriptor here!!");
            }
        }
    }

    /**
     * Returns a PAST {@link Method}
     */
    public Method generateMain(Collection<QualifiedName> allVars, Collection<QualifiedName> allAtts, String name, String packge, TemplateBindingsSchema bindingsSchema) {
        Method method = METHOD("main")
                .MODIFIERS(Modifier.PUBLIC, Modifier.STATIC)
                .RETURNS(VOID)
                .PARAMETER(STRING_ARRAY, "args");
        compilerUtil.debugFileLocation(method);

        Map<String, List<Descriptor>> theVars = bindingsSchema.getVar();

        org.openprovenance.prov.template.compiler.past.type.ClassName nameClass =
                org.openprovenance.prov.template.compiler.past.type.ClassName.get(name, packge);

        // Framework fr = Framework.dynamicLoad()
        method.addStatement(ASSIGNMENT(PROV_FRAMEWORK, VARIABLE("fr"),
                METHOD_CALL(PROV_FRAMEWORK, "dynamicLoad", List.of())));
        // ProvFactory pf = fr.getFactory()
        method.addStatement(ASSIGNMENT(PROV_FACTORY, VARIABLE("pf"),
                METHOD_CALL(VARIABLE("fr"), "getFactory", List.of())));
        // <Name> me = new <Name>(pf)
        method.addStatement(ASSIGNMENT(nameClass, VARIABLE("me"),
                CONSTRUCTOR_CALL(nameClass, List.of(VARIABLE("pf")))));

        // For each var: QualifiedName __var_X = pf.newQualifiedName("http://example.org/", "X", "ex")
        for (QualifiedName q : allVars) {
            method.addStatement(ASSIGNMENT(PROV_QUALIFIED_NAME, VARIABLE(compilerUtil.varPrefix(q.getLocalPart())),
                    METHOD_CALL(VARIABLE("pf"), "newQualifiedName",
                            CONSTANT("http://example.org/"), CONSTANT(q.getLocalPart()), CONSTANT("ex"))));
        }

        // For each att: either String __att_X = "example" or Object __att_X = null
        for (QualifiedName q : allAtts) {
            String declaredType = null;
            if (theVars != null) {
                for (String key : theVars.keySet()) {
                    if (q.getLocalPart().equals(key)) {
                        if (theVars.containsKey(key) && theVars.get(key) != null) {
                            declaredType = compilerUtil.getDeclaredType(theVars, key);
                        }
                    }
                }
            }

            if (declaredType != null) {
                String example = compilerUtil.generateExampleForType(declaredType, q.getLocalPart(), pFactory);
                method.addStatement(ASSIGNMENT(STRING, VARIABLE(compilerUtil.attPrefix(q.getLocalPart())),
                        CONSTANT(example)));
            } else {
                method.addStatement(ASSIGNMENT(OBJECT, VARIABLE(compilerUtil.attPrefix(q.getLocalPart())),
                        getNull()));
            }
        }

        // Build argument list for me.generator(...)
        List<org.openprovenance.prov.template.compiler.past.Expression> generatorArgs = new ArrayList<>();
        Set<String> seen = new HashSet<>();
        for (QualifiedName q : allVars) {
            generatorArgs.add(VARIABLE(compilerUtil.varPrefix(q.getLocalPart())));
            seen.add(q.getLocalPart());
        }
        for (QualifiedName q : allAtts) {
            if (!seen.contains(q.getLocalPart())) {
                generatorArgs.add(VARIABLE(compilerUtil.attPrefix(q.getLocalPart())));
            }
        }

        // System.out accessor
        org.openprovenance.prov.template.compiler.past.Expression systemOut =
                new org.openprovenance.prov.template.compiler.past.MethodCall(
                        org.openprovenance.prov.template.compiler.past.type.ClassName.get("System", "past.lang"), "out");
        // Formats.ProvFormat.PROVN accessor
        org.openprovenance.prov.template.compiler.past.Expression provFormatProvn =
                new org.openprovenance.prov.template.compiler.past.MethodCall(
                        new org.openprovenance.prov.template.compiler.past.MethodCall(PROV_FORMATS, "ProvFormat"),
                        "PROVN");

        // Document document = me.generator(args...)
        method.addStatement(ASSIGNMENT(PROV_DOCUMENT, VARIABLE("document"),
                METHOD_CALL(VARIABLE("me"), "generator", generatorArgs)));
        // fr.writeDocument(System.out, document, Formats.ProvFormat.PROVN)
        method.addStatement(METHOD_CALL(VARIABLE("fr"), "writeDocument",
                systemOut, VARIABLE("document"), provFormatProvn));

        // if (theVars != null): second call using make(exemplars...)
        if (theVars != null) {
            List<org.openprovenance.prov.template.compiler.past.Expression> makeArgs = new ArrayList<>();
            int count = 0;
            for (String key : theVars.keySet()) {
                if (theVars.get(key) == null) continue;
                makeArgs.add(createExamplarExpression(theVars, key, count++, pFactory));
            }

            // document = me.make(args...)
            method.addStatement(ASSIGNMENT(null, VARIABLE("document"),
                    METHOD_CALL(VARIABLE("me"), "make", makeArgs)));
            // fr.writeDocument(System.out, document, Formats.ProvFormat.PROVN)
            method.addStatement(METHOD_CALL(VARIABLE("fr"), "writeDocument",
                    systemOut, VARIABLE("document"), provFormatProvn));
        }

        return method;
    }

    /**
     * Builds a PAST string concatenation expression from a descriptor ID pattern
     * where {@code *} is replaced by the variable value at runtime.
     * For example, {@code "ex:*"} becomes {@code "ex:" + key + ""} via
     * {@code METHOD_CALL(STRING, "concat", CONSTANT("ex:"), VARIABLE(key), CONSTANT(""))}.
     */
    private org.openprovenance.prov.template.compiler.past.Expression buildIdStringExpression(String idPattern, String variableName) {
        String[] parts = idPattern.split("\\*", -1);
        List<org.openprovenance.prov.template.compiler.past.Expression> concatArgs = new ArrayList<>();
        for (int i = 0; i < parts.length; i++) {
            concatArgs.add(CONSTANT(parts[i]));
            if (i < parts.length - 1) {
                concatArgs.add(VARIABLE(variableName));
            }
        }
        if (concatArgs.size() == 1) {
            return concatArgs.get(0);
        }
        return METHOD_CALL(STRING, "concat", concatArgs);
    }

    /**
     * Returns a PAST {@link Method}.
     */
    public Method generateFactoryMethod(Collection<QualifiedName> allVars, Collection<QualifiedName> allAtts, String name, TemplateBindingsSchema bindingsSchema) {
        Method method = METHOD("make")
                .commentFileLocation()
                .MODIFIERS(Modifier.PUBLIC)
                .RETURNS(PROV_DOCUMENT);


        // Document __C_document = null
        method.addStatement(ASSIGNMENT(PROV_DOCUMENT, VARIABLE("__C_document"), getNull()));
        // Namespace __C_ns = new Namespace()
        method.addStatement(ASSIGNMENT(PROV_NAMESPACE, VARIABLE(Constants.C_NS),
                CONSTRUCTOR_CALL(PROV_NAMESPACE, List.of())));
        // StringSubstitutor subst = new StringSubstitutor(getVariableMap())
        method.addStatement(ASSIGNMENT(APACHE_STRING_SUBSTITUTOR, VARIABLE("subst"),
                CONSTRUCTOR_CALL(APACHE_STRING_SUBSTITUTOR,
                        List.of(METHOD_CALL("getVariableMap", List.of())))));

        Map<String, String> theContext = bindingsSchema.getContext();
        Map<String, List<Descriptor>> theVar = bindingsSchema.getVar();

        // Parameters
        compilerUtil.generateSpecializedParameters(method, bindingsSchema.getVar());

        // Register namespace prefixes
        for (String prefix : theContext.keySet()) {
            String uri = theContext.get(prefix);
            // __C_ns.register(prefix, subst.replace(uri))
            method.addStatement(METHOD_CALL(VARIABLE(Constants.C_NS), "register",
                    CONSTANT(prefix),
                    METHOD_CALL(VARIABLE("subst"), "replace", CONSTANT(uri))));
        }

        // Process variables
        List<org.openprovenance.prov.template.compiler.past.Expression> generatorArgs = new ArrayList<>();
        Set<String> seen = new HashSet<>();

        for (QualifiedName q : allVars) {
            final String key = q.getLocalPart();
            seen.add(key);
            final String newName = compilerUtil.varPrefix(key);
            List<Descriptor> descriptors = theVar.get(key);
            if (descriptors != null && !descriptors.isEmpty() && (descriptors.get(0) instanceof NameDescriptor)) {
                NameDescriptor descriptor = (NameDescriptor) descriptors.get(0);
                String s = descriptor.getId();
                int existsColumn = s.indexOf(":");
                if (existsColumn >= 0) {
                    String pre = s.substring(0, existsColumn);
                    if (pre != null && !pre.isEmpty() && theContext.get(pre) == null) {
                        throw new InvalidCaseException("CompilerExpansionBuilder: Reference to prefix '" + pre + "' in '" + s + "' for key '" + key + "', not available in context " + theContext);
                    }
                }
                String escape = descriptor.getEscape();
                boolean toEscape = escape != null && "true".equals(escape);

                // Build: QualifiedName newName = (key==null) ? null : __C_ns.stringToQualifiedName(idExpr, pf [, false])
                org.openprovenance.prov.template.compiler.past.Expression idExpr = buildIdStringExpression(s, key);
                List<org.openprovenance.prov.template.compiler.past.Expression> stqArgs = new ArrayList<>();
                stqArgs.add(idExpr);
                stqArgs.add(VARIABLE("pf"));
                if (toEscape) {
                    stqArgs.add(CONSTANT(false));
                }
                org.openprovenance.prov.template.compiler.past.Expression stqCall =
                        METHOD_CALL(VARIABLE(Constants.C_NS), "stringToQualifiedName", stqArgs);
                method.addStatement(ASSIGNMENT(PROV_QUALIFIED_NAME, VARIABLE(newName),
                        IfExpression.IFEXPRESSION(
                                BINARY_OP(VARIABLE(key), EQ, getNull()),
                                getNull(),
                                stqCall)));
            } else {
                // QualifiedName newName = null
                method.addStatement(ASSIGNMENT(PROV_QUALIFIED_NAME, VARIABLE(newName), getNull()));
            }
            generatorArgs.add(VARIABLE(newName));
        }

        // Process attributes
        for (QualifiedName q : allAtts) {
            final String key = q.getLocalPart();
            String newName = key;
            if (!seen.contains(key)) {
                List<Descriptor> descriptors = theVar.get(key);
                if (descriptors != null && !descriptors.isEmpty() && (descriptors.get(0) instanceof NameDescriptor)) {
                    NameDescriptor descriptor = (NameDescriptor) descriptors.get(0);
                    String s = descriptor.getId();
                    int existsColumn = s.indexOf(":");
                    if (existsColumn >= 0) {
                        String pre = s.substring(0, existsColumn);
                        if (pre != null && !pre.isEmpty() && theContext.get(pre) == null) {
                            throw new InvalidCaseException("CompilerExpansionBuilder: Reference to prefix '" + pre + "' in '" + s + "' for key '" + key + "', not available in context " + theContext);
                        }
                    }
                    newName = compilerUtil.attPrefix(key);
                    // QualifiedName newName = (key==null) ? null : __C_ns.stringToQualifiedName(idExpr, pf)
                    org.openprovenance.prov.template.compiler.past.Expression idExpr = buildIdStringExpression(s, key);
                    org.openprovenance.prov.template.compiler.past.Expression stqCall =
                            METHOD_CALL(VARIABLE(Constants.C_NS), "stringToQualifiedName", idExpr, VARIABLE("pf"));
                    method.addStatement(ASSIGNMENT(PROV_QUALIFIED_NAME, VARIABLE(newName),
                            IF_(BINARY_OP(VARIABLE(key), EQ, getNull()))
                                    .THEN(getNull())
                                    .ELSE(stqCall)));
                } else {
                    if (descriptors != null && !descriptors.isEmpty()) {
                        // is already declared, but not with @id — no statement needed
                    } else {
                        method.addStatement(ASSIGNMENT(OBJECT, VARIABLE(newName), getNull()));
                    }
                }
                generatorArgs.add(VARIABLE(newName));
            }
        }

        // __C_document = generator(args...)
        method.addStatement(ASSIGNMENT(null, VARIABLE("__C_document"),
                METHOD_CALL("generator", generatorArgs)));
        // return __C_document
        method.addStatement(RETURN(VARIABLE("__C_document")));

        return method;
    }

    /**
     * Returns a PAST {@link Method}.
     */
    public Method generateFactoryMethodWithContinuation(Collection<QualifiedName> allVars, Collection<QualifiedName> allAtts, String name, String template, String packge, TemplateBindingsSchema bindingsSchema) {

        Method method = METHOD("make")
                .commentFileLocation()
                .MODIFIERS(Modifier.PUBLIC)
                .RETURNS(T())
                .addTypeVariables(T());

        Map<String, List<Descriptor>> theVar = bindingsSchema.getVar();
        Map<String, String> theContext = bindingsSchema.getContext();

        // T __C_result = null
        method.addStatement(ASSIGNMENT(T(), VARIABLE("__C_result"), getNull()));
        // Namespace __C_ns = new Namespace()
        method.addStatement(ASSIGNMENT(PROV_NAMESPACE, VARIABLE(Constants.C_NS),
                CONSTRUCTOR_CALL(PROV_NAMESPACE, List.of())));
        // StringSubstitutor subst = new StringSubstitutor(getVariableMap())
        method.addStatement(ASSIGNMENT(APACHE_STRING_SUBSTITUTOR, VARIABLE("subst"),
                CONSTRUCTOR_CALL(APACHE_STRING_SUBSTITUTOR,
                        List.of(METHOD_CALL("getVariableMap", List.of())))));

        // Parameters from bindings schema
        compilerUtil.generateSpecializedParameters(method, theVar);

        // Parameter: processor of type <TemplateName>Interface<T>
        org.openprovenance.prov.template.compiler.past.type.ClassName interfaceClass =
                org.openprovenance.prov.template.compiler.past.type.ClassName.get(
                        compilerUtil.templateNameClass(template) + "Interface", packge);
        org.openprovenance.prov.template.compiler.past.type.ParameterizedType processorType =
                org.openprovenance.prov.template.compiler.past.type.ParameterizedType.get(interfaceClass, T());
        method.PARAMETER(processorType, "processor");

        // Register namespace prefixes
        for (String prefix : theContext.keySet()) {
            String uri = theContext.get(prefix);
            method.addStatement(METHOD_CALL(VARIABLE(Constants.C_NS), "register",
                    CONSTANT(prefix),
                    METHOD_CALL(VARIABLE("subst"), "replace", CONSTANT(uri))));
        }

        // Track variable name translation (key -> translated name)
        Map<String, String> translator = new HashMap<>();

        // Process variables
        Set<String> seen = new HashSet<>();
        for (QualifiedName q : allVars) {
            final String key = q.getLocalPart();
            seen.add(key);
            final String newName = compilerUtil.varPrefix(key);
            translator.put(key, newName);
            List<Descriptor> descriptors = theVar.get(key);
            if (descriptors != null && !descriptors.isEmpty() && (descriptors.get(0) instanceof NameDescriptor)) {
                NameDescriptor descriptor = (NameDescriptor) descriptors.get(0);
                String escape = descriptor.getEscape();
                boolean toEscape = "true".equals(escape);
                String id = descriptor.getId();

                org.openprovenance.prov.template.compiler.past.Expression idExpr = buildIdStringExpression(id, key);
                List<org.openprovenance.prov.template.compiler.past.Expression> stqArgs = new ArrayList<>();
                stqArgs.add(idExpr);
                stqArgs.add(VARIABLE("pf"));
                if (toEscape) {
                    stqArgs.add(CONSTANT(false));
                }
                org.openprovenance.prov.template.compiler.past.Expression stqCall =
                        METHOD_CALL(VARIABLE(Constants.C_NS), "stringToQualifiedName", stqArgs);
                method.addStatement(ASSIGNMENT(PROV_QUALIFIED_NAME, VARIABLE(newName),
                        IF_(BINARY_OP(VARIABLE(key), EQ, getNull()))
                                .THEN(getNull())
                                .ELSE(stqCall)));
            } else {
                // QualifiedName newName = null
                method.addStatement(ASSIGNMENT(PROV_QUALIFIED_NAME, VARIABLE(newName), getNull()));
            }
        }

        // Process attributes
        for (QualifiedName q : allAtts) {
            final String key = q.getLocalPart();
            if (!seen.contains(key)) {
                List<Descriptor> descriptors = theVar.get(key);
                if (descriptors != null && !descriptors.isEmpty() && (descriptors.get(0) instanceof NameDescriptor)) {
                    NameDescriptor descriptor = (NameDescriptor) descriptors.get(0);
                    String escape = descriptor.getEscape();
                    boolean toEscape = "true".equals(escape);
                    String id = descriptor.getId();
                    final String newName = compilerUtil.attPrefix(key);
                    translator.put(key, newName);

                    org.openprovenance.prov.template.compiler.past.Expression idExpr = buildIdStringExpression(id, key);
                    List<org.openprovenance.prov.template.compiler.past.Expression> stqArgs = new ArrayList<>();
                    stqArgs.add(idExpr);
                    stqArgs.add(VARIABLE("pf"));
                    if (toEscape) {
                        stqArgs.add(CONSTANT(false));
                    }
                    org.openprovenance.prov.template.compiler.past.Expression stqCall =
                            METHOD_CALL(VARIABLE(Constants.C_NS), "stringToQualifiedName", stqArgs);
                    method.addStatement(ASSIGNMENT(PROV_QUALIFIED_NAME, VARIABLE(newName),
                            IF_(BINARY_OP(VARIABLE(key), EQ, getNull()))
                                    .THEN(getNull())
                                    .ELSE(stqCall)));
                }
            }
        }

        // Build argument list for processor.call(args...) using translator
        List<org.openprovenance.prov.template.compiler.past.Expression> callArgs = new ArrayList<>();
        for (String key : theVar.keySet()) {
            if (!theVar.containsKey(key)) continue;
            if (theVar.get(key) == null) continue;
            String argName = key;
            if (translator.get(key) != null) {
                argName = translator.get(key);
            }
            callArgs.add(VARIABLE(argName));
        }

        // __C_result = processor.call(args...)
        method.addStatement(ASSIGNMENT(null, VARIABLE("__C_result"),
                METHOD_CALL(VARIABLE("processor"), "call", callArgs)));
        // return __C_result
        method.addStatement(RETURN(VARIABLE("__C_result")));

        return method;
    }

    /**
     *
     * Returns a PAST {@link Method} .
     */
    public Method generateTemplateGenerator(Collection<QualifiedName> allVars, Collection<QualifiedName> allAtts, Document doc, Hashtable<QualifiedName, String> vmap, TemplateBindingsSchema bindingsSchema, boolean past) {

        Method method = METHOD("generator")
                .commentFileLocation()
                .MODIFIERS(Modifier.PUBLIC)
                .RETURNS(PROV_DOCUMENT);

        // Parameters
        for (QualifiedName q : allVars) {
            method.PARAMETER(PROV_QUALIFIED_NAME, q.getLocalPart());
        }
        for (QualifiedName q : allAtts) {
            if (!allVars.contains(q)) {
                method.PARAMETER(org.openprovenance.prov.template.compiler.past.type.ClassName.OBJECT, q.getLocalPart());
            }
        }

        // Body: variable declarations
        // QualifiedName nullqn = null
        method.addStatement(ASSIGNMENT(PROV_QUALIFIED_NAME, VARIABLE("nullqn"), getNull()));
        // Collection<Attribute> attrs = null
        method.addStatement(ASSIGNMENT(PROV_COLLECTION_OF_ATTRIBUTES, VARIABLE("attrs"), getNull()));
        // Document __C_document = pf.newDocument()
        method.addStatement(ASSIGNMENT(PROV_DOCUMENT, VARIABLE("__C_document"),
                METHOD_CALL(VARIABLE("pf"), "newDocument", List.of())));

        // Gensym variable handling
        for (QualifiedName q : allVars) {
            if (InstantiateUtil.isGensymVariable(q)) {
                final String vgen = q.getLocalPart();
                // if (vgen==null) vgen=InstantiateAction.getUUIDQualifiedName2(pf)
                method.addStatement(
                        IF(BINARY_OP(VARIABLE(vgen), EQ, getNull()))
                                .THEN(ASSIGNMENT(null, VARIABLE(vgen),
                                        METHOD_CALL(PROV_INSTANTIATE_ACTION, "getUUIDQualifiedName2", VARIABLE("pf")))));
            }
        }

        // Process PROV template statements using StatementCompilerAction2
        List<org.openprovenance.prov.template.compiler.past.Statement> bodyStatements = new ArrayList<>();
        StatementCompilerAction2 action = new StatementCompilerAction2(pFactory, allVars, allAtts, vmap, bodyStatements, "__C_document.getStatementOrBundle()", bindingsSchema);
        for (StatementOrBundle s : doc.getStatementOrBundle()) {
            u.doAction(s, action);
        }
        for (org.openprovenance.prov.template.compiler.past.Statement stmt : bodyStatements) {
            method.addStatement(stmt);
        }

        // new ProvUtilities().updateNamespaces(__C_document)
        method.addStatement(
                METHOD_CALL(
                        CONSTRUCTOR_CALL(PROV_UTILITIES, List.of()),
                        "updateNamespaces",
                        List.of(VARIABLE("__C_document"))));

        // return __C_document
        method.addStatement(RETURN(VARIABLE("__C_document")));

        return method;
    }


    /*
    public MethodSpec generateTemplateGenerator_old(Collection<QualifiedName> allVars, Collection<QualifiedName> allAtts, Document doc, Hashtable<QualifiedName, String> vmap, TemplateBindingsSchema bindingsSchema) {


        MethodSpec.Builder builder = MethodSpec.methodBuilder("generator_old")
                .addModifiers(Modifier.PUBLIC)
                .returns(Document.class);

        compilerUtil.specWithComment(builder);




        builder
                .addStatement("$T nullqn = null", QualifiedName.class)
                .addStatement("$T attrs=null", StatementCompilerAction.cl_collectionOfAttributes)
                .addStatement("$T __C_document = pf.newDocument()", Document.class);
        for (QualifiedName q : allVars) {
            builder.addParameter(QualifiedName.class, q.getLocalPart());
        }
        for (QualifiedName q : allAtts) {
            if (allVars.contains(q)) {
                // no need to redeclare
            } else {
                builder.addParameter(Object.class, q.getLocalPart()); // without type declaration, any object may be accepted, assuming this is not a q also in allVars, and it's a declared variable.
            }
        }
        for (QualifiedName q : allVars) {
            if (InstantiateUtil.isGensymVariable(q)) {
                final String vgen = q.getLocalPart();
                builder.addStatement("if ($N==null) $N=$T.getUUIDQualifiedName2(pf)", vgen, vgen, InstantiateAction.class);
            }
        }


        StatementCompilerAction action = new StatementCompilerAction(pFactory, allVars, allAtts, vmap, builder, "__C_document.getStatementOrBundle()", bindingsSchema);
        for (StatementOrBundle s : doc.getStatementOrBundle()) {
            u.doAction(s, action);
        }

        builder.addStatement("new $T().updateNamespaces(__C_document)", ProvUtilities.class);

        builder.addStatement("return __C_document");

        MethodSpec method = builder.build();

        return method;
    }


     */



    private org.openprovenance.prov.template.compiler.past.Expression propagateTypesNCall(int count, int successor, int relation, org.openprovenance.prov.template.compiler.past.Expression specificRelation) {
        return METHOD_CALL("propagateTypes_n", List.of(
                VARIABLE("record"),
                VARIABLE("mapLevelN"),
                VARIABLE("mapLevelNP1"),
                CONSTANT(count),
                CONSTANT(successor),
                CONSTANT(relation),
                specificRelation,
                VARIABLE("uniqId")));
    }

    private <ARELATION extends Identifiable> void generateStatementForRelation(Method method, int count, int successor, int relation, Map<String,List<Descriptor>> theVar, Map<String, Set<Pair<QualifiedName, ARELATION>>> successors, Map<String, Collection<String>> knownTypes, Map<String, Collection<String>> unknownTypes, String key) {
        if (successors.get(key) != null) {

            final List<ARELATION> relations = successors.get(key).stream().map(Pair::getRight).collect(Collectors.toList());
            final List<QualifiedName> identifiers = relations.stream().map(Identifiable::getId).collect(Collectors.toList());

            method.COMMENT(true, "Identifiers: " + identifiers);
            method.COMMENT(true, "KnownTypes: "   + successors.get(key).stream().map(p -> knownTypes.get(p.getRight().getId().getUri())).collect(Collectors.toList()));
            method.COMMENT(true, "UnknownTypes: " + successors.get(key).stream().map(p -> unknownTypes.get(p.getRight().getId().getUri())).collect(Collectors.toList()));

            final List<Collection<QualifiedName>> optionalActivityTypes = relations.stream().map(p -> doCollectElementVariables((org.openprovenance.prov.model.Statement)p, ACTIVITY_TYPE_URI)).collect(Collectors.toList());
            final List<Collection<QualifiedName>> optionalActivities    = relations.stream().map(p -> doCollectElementVariables((org.openprovenance.prov.model.Statement)p, TMPL_ACTIVITY_URI)).collect(Collectors.toList());

            method.COMMENT(true, "ActivityTypes: " + optionalActivityTypes);
            method.COMMENT(true, "Activities: "    + optionalActivities);

            if (optionalActivityTypes.isEmpty() || optionalActivityTypes.get(0) == null) {
                method.addStatement(propagateTypesNCall(count, successor, relation, CONSTANT(-1)));
            } else {

                final QualifiedName firstRelationIdentifier = identifiers.get(0);
                final ARELATION firstRelation = relations.get(0);

                final Optional<QualifiedName> firstActivityType = optionalActivityTypes.get(0).stream().findFirst();
                if (firstActivityType.isEmpty()) {

                    method.addStatement(propagateTypesNCall(count, successor, relation, CONSTANT(-1)));

                } else {

                    if (optionalActivities.isEmpty() || optionalActivities.get(0) == null)
                        throw new MissingAttributeValue(TMPL_ACTIVITY_URI + " in " + firstRelation);
                    final Optional<QualifiedName> firstActivity = optionalActivities.get(0).stream().findFirst();
                    if (firstActivity.isEmpty())
                        throw new MissingAttributeValue(TMPL_ACTIVITY_URI + " in " + firstRelation);

                    method.COMMENT(true, "propagating for $N", key);
                    method.COMMENT(true, "URI: " + firstActivity.get().getUri());

                    final String tmpa = "l1a_" + count;
                    final String tmpb = "l1b_" + count;

                    method.COMMENT(true, "Position: " + findPosition(TypesRecordProcessor.localName(firstActivity.get().getUri()), theVar));

                    // Integer tmpa = mapLevel0.get(uri + ((QualifiedName)record[pos]).getLocalPart())
                    int pos = findPosition(TypesRecordProcessor.localName(firstActivity.get().getUri()), theVar);
                    org.openprovenance.prov.template.compiler.past.Expression getLocalPart =
                            METHOD_CALL(CAST(PROV_QUALIFIED_NAME, ARRAY_ACCESSOR(VARIABLE("record"), CONSTANT(pos))), "getLocalPart", List.of());
                    org.openprovenance.prov.template.compiler.past.Expression concatUri =
                            METHOD_CALL(STRING, "concat", CONSTANT(firstRelationIdentifier.getUri() + "."), getLocalPart);
                    method.addStatement(ASSIGNMENT(INTEGER, VARIABLE(tmpa),
                            METHOD_CALL(VARIABLE("mapLevel0"), "get", concatUri)));

                    // int tmpb = (tmpa==null) ? -1 : tmpa
                    method.addStatement(ASSIGNMENT(_int, VARIABLE(tmpb),
                            IF_(BINARY_OP(VARIABLE(tmpa), EQ, getNull()))
                                    .THEN(CONSTANT(-1))
                                    .ELSE(VARIABLE(tmpa))));

                    method.addStatement(propagateTypesNCall(count, successor, relation, VARIABLE(tmpb)));
                }
            }
        } else {
            method.addStatement(propagateTypesNCall(count, successor, relation, CONSTANT(-1)));
        }
    }

    public Method generateTypePropagator(String packge, TemplateBindingsSchema bindingsSchema, Map<Integer, List<Integer>> successorTable) {

        org.openprovenance.prov.template.compiler.past.type.ParameterizedType levelNMap =
                org.openprovenance.prov.template.compiler.past.type.ParameterizedType.get(MAP, STRING, INTEGER);
        org.openprovenance.prov.template.compiler.past.type.ParameterizedType collectionIntArray =
                org.openprovenance.prov.template.compiler.past.type.ParameterizedType.get(COLLECTION, intArray);
        org.openprovenance.prov.template.compiler.past.type.ParameterizedType levelNP1CMap =
                org.openprovenance.prov.template.compiler.past.type.ParameterizedType.get(MAP, STRING, collectionIntArray);

        Method method = METHOD("propagateTypes")
                .commentFileLocation()
                .MODIFIERS(Modifier.PUBLIC)
                .RETURNS(VOID);

        method.PARAMETER(OBJECT_ARRAY, "record");
        method.PARAMETER(levelNMap, "mapLevelN");
        method.PARAMETER(levelNP1CMap, "mapLevelNP1");
        method.PARAMETER(levelNMap, "mapLevel0");
        method.PARAMETER(levelNMap, "uniqId");

        method.COMMENT(true, successorTable.toString());

        int count = 1;

        Map<String, List<Descriptor>> theVar = bindingsSchema.getVar();
        java.util.Iterator<String> iter = theVar.keySet().iterator();

        Map<String, Set<Pair<QualifiedName, WasDerivedFrom>>>  successors1  = compilerCommon.getSuccessors1();
        Map<String, Set<Pair<QualifiedName, WasAttributedTo>>> successors2  = compilerCommon.getSuccessors2();
        Map<String, Set<Pair<QualifiedName, HadMember>>>       successors3  = compilerCommon.getSuccessors3();
        Map<String, Set<Pair<QualifiedName, QualifiedHadMember>>> successors3b = compilerCommon.getSuccessors3b();
        Map<String, Set<Pair<QualifiedName, SpecializationOf>>> successors4 = compilerCommon.getSuccessors4();

        Map<String, Collection<String>> knownTypes   = compilerTypeManagement.getKnownTypes();
        Map<String, Collection<String>> unknownTypes  = compilerTypeManagement.getUnknownTypes();

        while (iter.hasNext()) {
            String key = iter.next();
            if (compilerUtil.isVariableDenotingQualifiedName(key, theVar)) {
                method.COMMENT(true, "Variable: " + key);
                method.COMMENT(true, "Count: " + count);

                if ((successors1.get(key) != null) || (successors2.get(key) != null) || (successors3.get(key) != null) || (successors3b.get(key) != null) || (successors4.get(key) != null)) {

                    List<Integer> rowValues = successorTable.get(count);
                    if (rowValues == null || rowValues.isEmpty()) throw new InvalidCaseException("successor table incorrect");

                    for (int i = 0; i < rowValues.size() / 2; i++) {
                        int successor = rowValues.get(i * 2);
                        int relation = rowValues.get(i * 2 + 1);

                        if (relation == StatementOrBundle.Kind.PROV_DERIVATION.ordinal()) {
                            generateStatementForRelation(method, count, successor, relation, theVar, successors1, knownTypes, unknownTypes, key);
                        } else if (relation == StatementOrBundle.Kind.PROV_MEMBERSHIP.ordinal()) {
                            generateStatementForRelation(method, count, successor, relation, theVar, successors3b, knownTypes, unknownTypes, key);
                        } else {
                            method.addStatement(propagateTypesNCall(count, successor, relation, CONSTANT(-1)));
                        }
                    }
                } else {
                    method.COMMENT(true, "No successor for: " + count);
                }
            }
            method.COMMENT(true, "");

            count++;
        }
        return method;
    }

    /*
    public MethodSpec generateTypePropagator_old(String packge, TemplateBindingsSchema bindingsSchema, Map<Integer, List<Integer>> successorTable) {


        MethodSpec.Builder builder = MethodSpec.methodBuilder("propagateTypes_old")
                .addModifiers(Modifier.PUBLIC)
                .returns(void.class);

        builder.addParameter(ParameterSpec.builder(recordType,"record").build());
        builder.addParameter(ParameterSpec.builder(levelNMapType,"mapLevelN").build());
        builder.addParameter(ParameterSpec.builder(levelNP1CMapType,"mapLevelNP1").build());
        builder.addParameter(ParameterSpec.builder(levelNMapType,"mapLevel0").build());
        builder.addParameter(ParameterSpec.builder(levelNMapType,"uniqId").build());

        compilerUtil.specWithComment(builder);


        builder.addComment(successorTable.toString());


        int count=1; // ignore the tempalte name

        Map<String,List<Descriptor>> theVar=bindingsSchema.getVar();
        Iterator<String> iter = theVar.keySet().iterator();


        Map<String, Set<Pair<QualifiedName, WasDerivedFrom>>>  successors1= compilerCommon.getSuccessors1();
        Map<String, Set<Pair<QualifiedName, WasAttributedTo>>> successors2= compilerCommon.getSuccessors2();
        Map<String, Set<Pair<QualifiedName, HadMember>>>       successors3= compilerCommon.getSuccessors3();
        Map<String, Set<Pair<QualifiedName, QualifiedHadMember>>> successors3b= compilerCommon.getSuccessors3b();
        Map<String, Set<Pair<QualifiedName, SpecializationOf>>>successors4= compilerCommon.getSuccessors4();


        Map<String, Collection<String>> knownTypes   = compilerTypeManagement.getKnownTypes();
        Map<String, Collection<String>> unknownTypes = compilerTypeManagement.getUnknownTypes();

        while (iter.hasNext()) {
            String key = iter.next();
            if (compilerUtil.isVariableDenotingQualifiedName(key,theVar)) {
                builder.addComment("Variable: " + key);
                builder.addComment("Count: " + count);

                if ((successors1.get(key) != null) || (successors2.get(key) != null) || (successors3.get(key) != null) || (successors3b.get(key) != null) || (successors4.get(key) != null)) {
                    //generateStatementForRelation_OLD(builder, count, the_var, successors1,  knownTypes, unknownTypes, key);
                    //generateStatementForRelation_OLD(builder, count, the_var, successors3b, knownTypes, unknownTypes, key);

                    List<Integer> rowValues=successorTable.get(count);
                    if (rowValues==null || rowValues.isEmpty()) throw new InvalidCaseException("successor table incorrect");

                    for (int i=0; i<rowValues.size()/2; i++) {
                        int successor = rowValues.get(i * 2);
                        int relation = rowValues.get(i * 2 + 1);

                        if (relation == StatementOrBundle.Kind.PROV_DERIVATION.ordinal()) {
                            generateStatementForRelation_NEW(builder, count, successor, relation, theVar, successors1, knownTypes, unknownTypes, key);
                        } else if (relation == StatementOrBundle.Kind.PROV_MEMBERSHIP.ordinal()) {
                            generateStatementForRelation_NEW(builder, count, successor, relation, theVar, successors3b, knownTypes, unknownTypes, key);
                        } else {
                            builder.addStatement("propagateTypes_n(record,mapLevelN,mapLevelNP1,$L,$L,$L,$L,uniqId)", count, successor, relation, -1);
                        }
                    }
                } else {
                    builder.addComment("No successor for: " + count);
                }
            }
            builder.addComment("");

            count++;
        }
        return builder.build();
    }



    private <ARELATION extends Identifiable> void generateStatementForRelation_NEW(MethodSpec.Builder builder, int count, int successor, int relation, Map<String,List<Descriptor>> theVar, Map<String, Set<Pair<QualifiedName, ARELATION>>> successors, Map<String, Collection<String>> knownTypes, Map<String, Collection<String>> unknownTypes, String key) {
        if (successors.get(key) != null) {

            final List<ARELATION> relations = successors.get(key).stream().map(Pair::getRight).collect(Collectors.toList());
            final List<QualifiedName> identifiers = relations.stream().map(Identifiable::getId).collect(Collectors.toList());

            builder.addComment("Identifiers: " + identifiers);
            builder.addComment("KnownTypes: "   + successors.get(key).stream().map(p -> knownTypes.get(p.getRight().getId().getUri())).collect(Collectors.toList()));
            builder.addComment("UnknownTypes: " + successors.get(key).stream().map(p -> unknownTypes.get(p.getRight().getId().getUri())).collect(Collectors.toList()));

            final List<Collection<QualifiedName>> optionalActivityTypes = relations.stream().map(p -> doCollectElementVariables((Statement)p, ACTIVITY_TYPE_URI)).collect(Collectors.toList());
            final List<Collection<QualifiedName>> optionalActivities    = relations.stream().map(p -> doCollectElementVariables((Statement)p, TMPL_ACTIVITY_URI)).collect(Collectors.toList());

            builder.addComment("ActivityTypes: " + optionalActivityTypes);
            builder.addComment("Activities: "    + optionalActivities);

            if (optionalActivityTypes.isEmpty() || optionalActivityTypes.get(0) == null) {
                builder.addStatement("propagateTypes_n(record,mapLevelN,mapLevelNP1,$L,$L,$L,$L,uniqId)", count, successor, relation,-1);
            } else {

                final QualifiedName firstRelationIdentifier = identifiers.get(0);
                final ARELATION firstRelation = relations.get(0);

                final Optional<QualifiedName> firstActivityType = optionalActivityTypes.get(0).stream().findFirst();
                if (firstActivityType.isEmpty()) {

                    builder.addStatement("propagateTypes_n(record,mapLevelN,mapLevelNP1,$L,$L,$L,$L,uniqId)", count, successor, relation, -1);

                } else {

                    if (optionalActivities.isEmpty() || optionalActivities.get(0) == null)
                        throw new MissingAttributeValue(TMPL_ACTIVITY_URI + " in " + firstRelation);
                    final Optional<QualifiedName> firstActivity = optionalActivities.get(0).stream().findFirst();
                    if (firstActivity.isEmpty())
                        throw new MissingAttributeValue(TMPL_ACTIVITY_URI + " in " + firstRelation);

                    builder.addComment("propagating for $N", key);
                    builder.addComment("URI: " + firstActivity.get().getUri());

                    //builder.addStatement("System.out.println(\"maplevelN \" + mapLevelN.get($S))",identifiers.get(0).getUri());
                    final String tmpa = "l1a_" + count;
                    final String tmpb = "l1b_" + count;

                    builder.addComment("Position: " + findPosition(TypesRecordProcessor.localName(firstActivity.get().getUri()), theVar));

                    //TypesRecordProcessor.localName(firstActivity.get().getUri())
                    builder.addStatement("$T $N=mapLevel0.get($S + (($T)record[$L]).getLocalPart())", Integer.class, tmpa, firstRelationIdentifier.getUri() + ".", QualifiedName.class, findPosition(TypesRecordProcessor.localName(firstActivity.get().getUri()), theVar

                    ));
                    builder.addStatement("int $N=($N==null)?-1:$N", tmpb, tmpa, tmpa);

                    builder.addStatement("propagateTypes_n(record,mapLevelN,mapLevelNP1,$L,$L,$L,$N, uniqId)", count, successor, relation, tmpb);
                }
            }
        } else {
            builder.addStatement("propagateTypes_n(record,mapLevelN,mapLevelNP1,$L,$L,$L,$L,uniqId)", count, successor, relation,-1);
        }
    }

     */

    private int findPosition(String name, Map<String, List<Descriptor>> theVar) {
        Iterator<String> iter = theVar.keySet().iterator();

        int count=1;
        while (iter.hasNext()) {
            String key = iter.next();
            if (key.equals(name)) return count;
            count++;
        }
        return count;
    }

    static public String escape (QualifiedName qn) {
        String uri=qn.getUri();
        return uri.replace("/","_").replace("#","_").replace(":","_").replace(".","_");
    }


    public Collection<QualifiedName> doCollectElementVariables(Statement s, String search) {
        return doCollectElementVariables(pFactory,s,search);
    }


    static public Collection<QualifiedName> doCollectElementVariables(ProvFactory pFactory, Statement s, String search) {
        Collection<Attribute> attributes = pFactory.getAttributes(s);
        if (!(attributes.isEmpty())) {
            boolean found=false;
            Collection<QualifiedName> res=new LinkedList<>();
            for (Attribute attribute:attributes) {
                QualifiedName element=attribute.getElementName();
                Object value=attribute.getValue();
                if (value instanceof QualifiedName) {
                    QualifiedName vq=(QualifiedName) value;
                    if (search.equals(element.getUri())) {
                        res.add(vq);
                        found=true;
                    }
                }
            }
            if (found) return res;
        }
        return null;
    }

    public Method generateTypePropagatorN() {
        final String var_successor = "successor";
        final String var_genericRelation = "genericRelation";
        final String var_record = "record";
        final String var_specificRelation = "specificRelation";
        final String var_count = "count";
        final String var_in_type = "in_type";

        org.openprovenance.prov.template.compiler.past.type.ParameterizedType levelNMap =
                org.openprovenance.prov.template.compiler.past.type.ParameterizedType.get(MAP, STRING, INTEGER);
        org.openprovenance.prov.template.compiler.past.type.ParameterizedType collectionIntArray =
                org.openprovenance.prov.template.compiler.past.type.ParameterizedType.get(COLLECTION, intArray);
        org.openprovenance.prov.template.compiler.past.type.ParameterizedType levelNP1CMap =
                org.openprovenance.prov.template.compiler.past.type.ParameterizedType.get(MAP, STRING, collectionIntArray);

        Method method = METHOD("propagateTypes_n")
                .commentFileLocation()
                .MODIFIERS(Modifier.PUBLIC)
                .RETURNS(VOID);

        method.PARAMETER(OBJECT_ARRAY, var_record);
        method.PARAMETER(levelNMap, "mapLevelN");
        method.PARAMETER(levelNP1CMap, "mapLevelNP1");
        method.PARAMETER(INTEGER, var_count);
        method.PARAMETER(_int, var_successor);
        method.PARAMETER(_int, var_genericRelation);
        method.PARAMETER(_int, var_specificRelation);
        method.PARAMETER(levelNMap, "uniqId");

        // if (record[count]!=null) {
        org.openprovenance.prov.template.compiler.past.Expression recordCount =
                ARRAY_ACCESSOR(VARIABLE(var_record), VARIABLE(var_count));

        //   String uri=((QualifiedName)(record[count])).getUri()
        org.openprovenance.prov.template.compiler.past.Statement uriAssignment =
                ASSIGNMENT(STRING, VARIABLE("uri"),
                        METHOD_CALL(CAST(PROV_QUALIFIED_NAME, recordCount), "getUri", List.of()));

        //   Integer in_type=mapLevelN.get(uri)
        org.openprovenance.prov.template.compiler.past.Statement inTypeAssignment =
                ASSIGNMENT(INTEGER, VARIABLE(var_in_type),
                        METHOD_CALL(VARIABLE("mapLevelN"), "get", VARIABLE("uri")));

        // if (record[successor]!=null) {
        org.openprovenance.prov.template.compiler.past.Expression recordSuccessor =
                ARRAY_ACCESSOR(VARIABLE(var_record), VARIABLE(var_successor));

        //   String uri2=((QualifiedName)(record[successor])).getUri()
        org.openprovenance.prov.template.compiler.past.Statement uri2Assignment =
                ASSIGNMENT(STRING, VARIABLE("uri2"),
                        METHOD_CALL(CAST(PROV_QUALIFIED_NAME, recordSuccessor), "getUri", List.of()));

        //   mapLevelNP1.computeIfAbsent(uri2, k -> new LinkedList<>())
        org.openprovenance.prov.template.compiler.past.Expression computeIfAbsent =
                METHOD_CALL(VARIABLE("mapLevelNP1"), "computeIfAbsent",
                        VARIABLE("uri2"),
                        LambdaExpression.LAMBDA(Parameter.PARAMETER("k", null))
                                .BODY(RETURN(CONSTRUCTOR_CALL(LINKED_LIST_GENERICS, List.of()))));

        //   mapLevelNP1.get(uri2).add(new int[] { successor, genericRelation, specificRelation, in_type, count, uniqId.get(uri) })
        MethodCall getUri2 =
                METHOD_CALL(VARIABLE("mapLevelNP1"), "get", VARIABLE("uri2"));
        org.openprovenance.prov.template.compiler.past.Expression arrayInit =
                ArrayInitialiser.ARRAY_INITIALISER(_int,
                        VARIABLE(var_successor),
                        VARIABLE(var_genericRelation),
                        VARIABLE(var_specificRelation),
                        VARIABLE(var_in_type),
                        VARIABLE(var_count),
                        METHOD_CALL(VARIABLE("uniqId"), "get", VARIABLE("uri")));
        org.openprovenance.prov.template.compiler.past.Expression addCall =
                METHOD_CALL(getUri2, "add", List.of(arrayInit));

        // Build nested if statements (innermost first)
        // if (record[successor]!=null) { uri2=...; computeIfAbsent; add }
        // if (in_type!=null) { if (record[successor]!=null) { ... } }
        // if (record[count]!=null) { uri=...; in_type=...; if (in_type!=null) { ... } }
        IfStatement outerIf =
                IF(BINARY_OP(recordCount, "!=", getNull()))
                        .THEN(uriAssignment,
                                inTypeAssignment,
                                IF(BINARY_OP(VARIABLE(var_in_type), "!=", getNull()))
                                        .THEN(IF(BINARY_OP(recordSuccessor, "!=", getNull()))
                                                .THEN(uri2Assignment,
                                                        computeIfAbsent,
                                                        addCall)));

        method.addStatement(outerIf);

        return method;
    }



    public Hashtable<QualifiedName, String> generateQualifiedNames(Document doc, org.openprovenance.prov.template.compiler.past.Class builder) {
        Bundle bun = u.getBundle(doc).get(0);
        Set<QualifiedName> set = new HashSet<>();
        compilerUtil.allQualifiedNames(bun, set, pFactory);
        set.remove(pFactory.newQualifiedName(InstantiateUtil.TMPL_NS, InstantiateUtil.LABEL, InstantiateUtil.TMPL_PREFIX));
        set.add(pFactory.getName().PROV_LABEL);
        Hashtable<QualifiedName, String> qnVariables = new Hashtable<>();
        for (QualifiedName qn : set) {
            if (!(InstantiateUtil.isVariable(qn))) {
                final String v = variableForQualifiedName(qn);
                qnVariables.put(qn, v);
                builder.FIELDS(FIELD(v,PROV_QUALIFIED_NAME).MODIFIERS(Modifier.PUBLIC, Modifier.FINAL));
            }
        }
        return qnVariables;
    }

    public String variableForQualifiedName(QualifiedName qn) {
        return "_Q_" + qn.getPrefix() + "_" + qn.getLocalPart();
    }



    public Method generateFactoryMethodWithArray(Collection<QualifiedName> allVars, Collection<QualifiedName> allAtts, String name, TemplateBindingsSchema bindingsSchema) {
        Method method = METHOD("make")
                .commentFileLocation()
                .MODIFIERS(Modifier.PUBLIC)
                .RETURNS(PROV_DOCUMENT);

        Map<String, List<Descriptor>> theVar = bindingsSchema.getVar();

        method.PARAMETER(OBJECT_ARRAY, "__record");

        int count = 1;
        List<org.openprovenance.prov.template.compiler.past.Expression> args = new ArrayList<>();
        for (String key : theVar.keySet()) {
            if (theVar.get(key) == null) {
                continue;
            }
            final org.openprovenance.prov.template.compiler.past.type.ClassName pastType = compilerUtil.getPastTypeForDeclaredType(theVar, key);
            final Class<?> atype = compilerUtil.getJavaTypeForDeclaredType(theVar, key);
            final String converter = compilerUtil.getConverterForDeclaredType(atype);
            org.openprovenance.prov.template.compiler.past.Expression arrayAccess =
                    ARRAY_ACCESSOR(VARIABLE("__record"), CONSTANT(count));
            if (converter == null) {
                // Type key = (Type) __record[count]
                method.addStatement(ASSIGNMENT(pastType, VARIABLE(key), CAST(pastType, arrayAccess)));
            } else {
                // Type key = converter(__record[count])
                method.addStatement(ASSIGNMENT(pastType, VARIABLE(key), METHOD_CALL(converter, List.of(arrayAccess))));
            }
            args.add(VARIABLE(key));
            count++;
        }
        // return make(args...)
        method.addStatement(RETURN(METHOD_CALL("make", args)));

        return method;
    }

    public Method generateFactoryMethodWithArrayAndContinuation(String name, String template, String packge, TemplateBindingsSchema bindingsSchema) {

        Method method = METHOD("make")
                .commentFileLocation()
                .MODIFIERS(Modifier.PUBLIC)
                .RETURNS(T())
                .addTypeVariables(T());

        Map<String, List<Descriptor>> theVar = bindingsSchema.getVar();

        method.PARAMETER(OBJECT_ARRAY, "__record");

        // Parameter: processor of type <TemplateName>Interface<T>
        org.openprovenance.prov.template.compiler.past.type.ClassName interfaceClass =
                org.openprovenance.prov.template.compiler.past.type.ClassName.get(
                        compilerUtil.templateNameClass(template) + "Interface", packge);
        org.openprovenance.prov.template.compiler.past.type.ParameterizedType processorType =
                org.openprovenance.prov.template.compiler.past.type.ParameterizedType.get(interfaceClass, T());
        method.PARAMETER(processorType, PROCESSOR);

        int count = 1;
        List<org.openprovenance.prov.template.compiler.past.Expression> args = new ArrayList<>();
        for (String key : theVar.keySet()) {
            if (theVar.get(key) == null) {
                continue;
            }
            final org.openprovenance.prov.template.compiler.past.type.ClassName pastType = compilerUtil.getPastTypeForDeclaredType(theVar, key);
            final Class<?> atype = compilerUtil.getJavaTypeForDeclaredType(theVar, key);
            final String converter = compilerUtil.getConverterForDeclaredType2(atype);
            if (converter == null) {
                // Type key = (Type) __record[count]
                method.addStatement(ASSIGNMENT(pastType, VARIABLE(key), CAST(pastType, ARRAY_ACCESSOR(VARIABLE("__record"), CONSTANT(count)))));
            } else {
                // Type key = converter(__record[count])
                method.addStatement(ASSIGNMENT(pastType, VARIABLE(key),
                        IF_(BINARY_OP(ARRAY_ACCESSOR(VARIABLE("__record"), CONSTANT(count)), EQ, Constant.getNull()))
                                .THEN(Constant.getNull())
                                .ELSE(METHOD_CALL(converter, List.of(METHOD_CALL(ARRAY_ACCESSOR(VARIABLE("__record"), CONSTANT(count)), "toString", List.of()))))));
            }
            args.add(VARIABLE(key));
            count++;
        }
        // return make(args..., _processor)
        args.add(VARIABLE(PROCESSOR));
        method.addStatement(RETURN(METHOD_CALL("make", args)));

        return method;
    }

    public Method generateUniqueRecordFactoryMethodWithArrayAndContinuation(String name, String template, String packge, TemplateBindingsSchema bindingsSchema) {

        Method method = METHOD("make")
                .commentFileLocation()
                .MODIFIERS(Modifier.PUBLIC)
                .RETURNS(T())
                .addTypeVariables(T());

        // Parameter: processor of type <TemplateName>Interface<T>
        org.openprovenance.prov.template.compiler.past.type.ClassName interfaceClass =
                org.openprovenance.prov.template.compiler.past.type.ClassName.get(
                        compilerUtil.templateNameClass(template) + "Interface", packge);
        org.openprovenance.prov.template.compiler.past.type.ParameterizedType processorType =
                org.openprovenance.prov.template.compiler.past.type.ParameterizedType.get(interfaceClass, T());
        method.PARAMETER(processorType, PROCESSOR);

        Map<String, List<Descriptor>> theVar = bindingsSchema.getVar();
        Collection<String> variables = descriptorUtils.fieldNames(bindingsSchema);

        int count = 1;
        for (String variable : variables) {
            List<Descriptor> descriptors = theVar.get(variable);
            if (descriptors != null) {
                for (Descriptor descriptor : descriptors) {
                    final org.openprovenance.prov.template.compiler.past.type.ClassName pastType = compilerUtil.getPastTypeForDeclaredType(theVar, variable);
                    final Class<?> atype = compilerUtil.getJavaTypeForDeclaredType(theVar, variable);
                    final String converter = compilerUtil.getConverterForDeclaredType(atype);
                    if (descriptor instanceof NameDescriptor) {
                        if (converter == null) {
                            // Type var = (Type) "count"
                            method.addStatement(ASSIGNMENT(pastType, VARIABLE(variable), CAST(pastType, CONSTANT("" + count))));
                        } else {
                            // Type var = converter(count)
                            method.addStatement(ASSIGNMENT(pastType, VARIABLE(variable), METHOD_CALL(converter, List.of(CONSTANT(count)))));
                        }
                    } else {
                        // Type var = null /* count */
                        method.addStatement(ASSIGNMENT(pastType, VARIABLE(variable), getNull()));
                    }
                    count++;
                }
            }
        }
        // return make(args..., _processor)
        List<org.openprovenance.prov.template.compiler.past.Expression> args = new ArrayList<>();
        for (String variable : variables) {
            args.add(VARIABLE(variable));
        }
        args.add(VARIABLE(PROCESSOR));
        method.addStatement(RETURN(METHOD_CALL("make", args)));

        return method;
    }








    public Method typeManagerGenerator(String templateName, String packge) {
        org.openprovenance.prov.template.compiler.past.type.TypeVariable typeT = org.openprovenance.prov.template.compiler.past.type.TypeVariable.T();

        org.openprovenance.prov.template.compiler.past.type.ClassName typeManagementClass =
                org.openprovenance.prov.template.compiler.past.type.ClassName.get(
                        compilerUtil.templateNameClass(templateName) + "TypeManagement", packge);
        org.openprovenance.prov.template.compiler.past.type.ParameterizedType returnType =
                org.openprovenance.prov.template.compiler.past.type.ParameterizedType.get(typeManagementClass, typeT);

        Method method = METHOD("getTypeManager")
                .commentFileLocation()
                .MODIFIERS(Modifier.PUBLIC)
                .RETURNS(returnType)
                .addTypeVariables(typeT);

        method.PARAMETER(MAP_QUALIFIEDNAME_STRING_SET, "knownTypeMap");
        method.PARAMETER(MAP_QUALIFIEDNAME_STRING_SET, "unknownTypeMap");
        method.PARAMETER(MAP_STRING_MAP_STRING_BIFUNCTION, "propertyConverters");
        method.PARAMETER(MAP_QUALIFIEDNAME_MAP_STRING_COLLECTION_OF_STRING, "idata");
        method.PARAMETER(MAP_STRING_MAP_STRING_TRIFUNCTION, "idataConverters");

        // return new <TemplateName>TypeManagement<>(pf, knownTypeMap, unknownTypeMap, propertyConverters, idata, idataConverters)
        org.openprovenance.prov.template.compiler.past.type.ParameterizedType diamondType =
                org.openprovenance.prov.template.compiler.past.type.ParameterizedType.get(typeManagementClass);
        method.addStatement(RETURN(CONSTRUCTOR_CALL(diamondType, List.of(
                VARIABLE("pf"),
                VARIABLE("knownTypeMap"),
                VARIABLE("unknownTypeMap"),
                VARIABLE("propertyConverters"),
                VARIABLE("idata"),
                VARIABLE("idataConverters")))));

        return method;
    }


    public Method typedRecordGenerator(String templateName, String packge) {
        org.openprovenance.prov.template.compiler.past.type.ClassName typedRecordClass =
                org.openprovenance.prov.template.compiler.past.type.ClassName.get(
                        compilerUtil.templateNameClass(templateName) + "TypedRecord", packge);

        Method method = METHOD("getTypedRecord")
                .commentFileLocation()
                .MODIFIERS(Modifier.PUBLIC)
                .RETURNS(typedRecordClass)
                // return new <TemplateName>TypedRecord()
                .BODY(RETURN(CONSTRUCTOR_CALL(typedRecordClass, List.of())));

        return method;
    }

}
