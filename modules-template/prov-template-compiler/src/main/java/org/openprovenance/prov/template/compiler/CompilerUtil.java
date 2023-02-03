package org.openprovenance.prov.template.compiler;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.Map.Entry;

import javax.lang.model.element.Modifier;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.MissingNode;
import com.fasterxml.jackson.databind.node.NullNode;
import com.squareup.javapoet.*;
import org.apache.commons.text.CaseUtils;
import org.openprovenance.prov.model.Attribute;
import org.openprovenance.prov.model.Bundle;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.model.ProvUtilities;
import org.openprovenance.prov.model.QualifiedName;
import org.openprovenance.prov.model.Statement;
import org.openprovenance.prov.model.ValueConverter;
import org.openprovenance.prov.template.compiler.common.BeanDirection;
import org.openprovenance.prov.template.compiler.common.Constants;
import org.openprovenance.prov.template.compiler.configuration.SimpleTemplateCompilerConfig;
import org.openprovenance.prov.template.descriptors.*;
import org.openprovenance.prov.template.expander.ExpandUtil;
import org.openprovenance.prov.template.log2prov.FileBuilder;

//import com.google.common.base.CaseFormat;
import com.squareup.javapoet.TypeSpec.Builder;

import static org.openprovenance.prov.template.compiler.ConfigProcessor.objectMapper;
import static org.openprovenance.prov.template.compiler.common.Constants.*;


public class CompilerUtil {


    public static final TypeVariableName typeT = TypeVariableName.get("T");
    static final TypeName classType=ParameterizedTypeName.get(ClassName.get(Class.class), typeT);
    static final TypeName mapType=ParameterizedTypeName.get(ClassName.get(Map.class),ClassName.get(String.class),ClassName.get(Object.class));
    static final TypeName listMapType=ParameterizedTypeName.get(ClassName.get(List.class),mapType);
    static final TypeName hashMapType=ParameterizedTypeName.get(ClassName.get(HashMap.class),ClassName.get(String.class),ClassName.get(Object.class));

    static final TypeName mapTypeT=ParameterizedTypeName.get(ClassName.get(Map.class),ClassName.get(String.class),typeT);
    static final TypeName hashMapTypeT=ParameterizedTypeName.get(ClassName.get(HashMap.class),ClassName.get(String.class),typeT);
    public static final ParameterizedTypeName hashmapType = ParameterizedTypeName.get(ClassName.get(HashMap.class), TypeName.get(Integer.class), TypeName.get(int[].class));
    public static final ParameterizedTypeName builderMapType=ParameterizedTypeName.get(ClassName.get(Map.class),ClassName.get(String.class),ClassName.get(CLIENT_PACKAGE,"Builder"));
    public static final TypeName listOfArrays=ParameterizedTypeName.get(ClassName.get(List.class),ArrayTypeName.get(Object[].class));

    public String generateNewNameForVariable(String key) {
        return GENERATED_VAR_PREFIX + key;
    }

    public String capitalize(String templateName) {
        return templateName.substring(0, 1).toUpperCase()+templateName.substring(1);
    }

    public String templateNameClass(String templateName) {
        return capitalize(templateName) + "Builder";
    }


    public String beanNameClass(String templateName, BeanDirection beanDirection) {
        String name;
        switch (beanDirection) {
            case INPUTS:
                name= inputsNameClass(templateName);
                break;
            case OUTPUTS:
                name= outputsNameClass(templateName);
                break;
            case COMMON:
                name= commonNameClass(templateName);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + beanDirection);
        }
        return name;
    }

    public String commonNameClass(String templateName) {
        return capitalize(templateName) + "Bean";
    }
    public String outputsNameClass(String templateName) {
        return capitalize(templateName) + "Outputs";
    }
    public String inputsNameClass(String templateName) {
        return capitalize(templateName) + "Inputs";
    }
    public String processorNameClass(String templateName) {
        return capitalize(templateName) + "Processor";
    }

    public String integratorBuilderNameClass(String templateName) {
        return capitalize(templateName) + "IntegratorBuilder";
    }
    public String integratorNameClass(String templateName) {
        return capitalize(templateName) + "Integrator";
    }
    public String loggerName(String template) {
        return "log" + capitalize(template);

    }
    public String sqlName(String template) {
        return "sqlTuple" ; //+ capitalize(template)

    }


    public void extractVariablesAndAttributes(Bundle bundle,
                                              Set<QualifiedName> allVars,
                                              Set<QualifiedName> allAtts,
                                              ProvFactory pFactory) {
        for (Statement statement: bundle.getStatement()) {
            Set<QualifiedName> vars=ExpandUtil.freeVariables(statement);
            allVars.addAll(vars);
            allVars.addAll(ExpandUtil.freeVariables(bundle));
            Set<QualifiedName> vars2=ExpandUtil.freeAttributeVariables(statement, pFactory);
            allAtts.addAll(vars2);
        }
    }
    
    public Builder generateClassInit(String name) {
        return TypeSpec.classBuilder(name)
                .addModifiers(Modifier.PUBLIC);
    }

    
    public Builder generateClassInitExtends(String name, String packge, String supername) {
        return TypeSpec.classBuilder(name)
                .superclass(ClassName.get(packge,supername))
                .addModifiers(Modifier.PUBLIC);
    }
    public Builder generateInterfaceInit(String name) {
        return TypeSpec.interfaceBuilder(name)
                .addModifiers(Modifier.PUBLIC);
    }
    public Builder generateInterfaceInitParameter(String name, String type) {
        return TypeSpec.interfaceBuilder(name)
                .addTypeVariable(TypeVariableName.get(type))
                .addModifiers(Modifier.PUBLIC);
    }
    public Builder generateInterfaceInitParameter(String name, TypeVariableName type) {
        return TypeSpec.interfaceBuilder(name)
                .addTypeVariable(type)
                .addModifiers(Modifier.PUBLIC);
    }
  
    public Builder generateClassBuilder3(String name) {
        return TypeSpec.classBuilder(name)
                .addModifiers(Modifier.PUBLIC);
    }
   
    public Builder generateClassBuilder2(String name) {
        return TypeSpec.classBuilder(name)
                .superclass(FileBuilder.class)
                .addSuperinterface(ClassName.get(org.openprovenance.prov.template.log2prov.interfaces.ProxyClientAccessorInterface.class))
                .addModifiers(Modifier.PUBLIC)
                .addField(ProvFactory.class, "pf", Modifier.PRIVATE, Modifier.FINAL)
                .addField(ValueConverter.class, "vc", Modifier.PRIVATE, Modifier.FINAL);
    }

    public Builder generateTypeManagementClass(String name) {
        return TypeSpec.classBuilder(name+"TypeManagement")
                .addModifiers(Modifier.PUBLIC);
    }
    public Builder generateTypePropagateClass(String name) {
        return TypeSpec.classBuilder(name+"TypePropagate")
                .addModifiers(Modifier.PUBLIC);
    }
    public Builder generateTypedRecordClass(String name) {
        return TypeSpec.classBuilder(name+"TypedRecord")
                .addModifiers(Modifier.PUBLIC);
    }
    public MethodSpec generateConstructor2(Hashtable<QualifiedName, String> vmap) {
        com.squareup.javapoet.MethodSpec.Builder builder= MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(ProvFactory.class, "pf")
                .addStatement("this.$N = $N", "pf", "pf");
        for (Entry<QualifiedName, String> e: vmap.entrySet()) {
            final QualifiedName q = e.getKey();
            builder.addStatement("this.$N = pf.newQualifiedName($S,$S,$S)", e.getValue(),q.getNamespaceURI(),q.getLocalPart(),q.getPrefix());
        }
        builder.addStatement("this.vc = new ValueConverter(pf)");
        builder.addStatement("register(this)");
        return builder .build();
    }
      
    public String camelcase(String s) {
        //return CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, s);  //use guava
         return CaseUtils.toCamelCase(s,true);
    }

    final public static ProvUtilities u = new ProvUtilities();
        

    public Set<QualifiedName> allQualifiedNames(Statement statement) {
        HashSet<QualifiedName> result = new HashSet<QualifiedName>();
        for (int i = 0; i < ExpandUtil.getFirstTimeIndex(statement); i++) {
            Object o = u.getter(statement, i);
            if (o instanceof QualifiedName) {
                QualifiedName name = (QualifiedName) o;
                result.add(name);
            } else {
                if (o instanceof List) {
                    List<QualifiedName> ll = (List<QualifiedName>) o;
                    for (QualifiedName name : ll) {
                        result.add(name);
                    }
                }
            }
        }
        return result;
    }   
    

    public HashSet<QualifiedName> allQualifiedNamesInAttributes(Statement statement, ProvFactory pf) {
        HashSet<QualifiedName> result = new HashSet<QualifiedName>();
        Collection<Attribute> ll = pf.getAttributes(statement);
        for (Attribute attr : ll) {
            result.add(attr.getElementName());
            if (attr.getType()!=null) result.add(attr.getType());
            if (attr.getValue() instanceof QualifiedName) result.add((QualifiedName)attr.getValue());
        }
        return result;
    }

    public void allQualifiedNames(Bundle bundle,
                                  Set<QualifiedName> result,
                                  ProvFactory pFactory) {
        result.add(bundle.getId());
        for (Statement statement: bundle.getStatement()) {
            Set<QualifiedName> vars=allQualifiedNames(statement);
            result.addAll(vars);
            Set<QualifiedName> vars2=allQualifiedNamesInAttributes(statement, pFactory);
            result.addAll(vars2);
        }
    }

    public Document readDocumentFromFile(String file) throws ClassNotFoundException,
                                                             NoSuchMethodException,
                                                             SecurityException,
                                                             InstantiationException,
                                                             IllegalAccessException,
                                                             IllegalArgumentException,
                                                             InvocationTargetException {
        Object interop=getInteropFramework();
        Method method = interop.getClass().getMethod("readDocumentFromFile", String.class);
        Document doc=(Document)method.invoke(interop,file);
        return doc;
    }

    public void writeDocument(String file, Document doc) throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Object interop=getInteropFramework();
        Method method = interop.getClass().getMethod("writeDocument", String.class, Document.class);
        method.invoke(interop,file,doc);
    }


    public Object getInteropFramework() throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException,
                                               IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Class<?> clazz = Class.forName("org.openprovenance.prov.interop.InteropFramework");
        Constructor<?> ctor = clazz.getConstructor();
        return ctor.newInstance(new Object[] { }); 
    }



    public boolean saveToFile(String destinationDir, String destination, JavaFile spec) {
        PrintWriter out;
        try {
            File dir=new File(destinationDir);
            if (!dir.exists() && !dir.mkdirs()) {
                System.err.println("failed to create directory " + destinationDir);
                return false;
            };
            out = new PrintWriter(destination);
            out.print(spec);
            out.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }


    public JsonNode get_bindings_schema(SimpleTemplateCompilerConfig config) {
        JsonNode bindings_schema=null;
        if (config.bindings != null) {
            try {
                bindings_schema = objectMapper.readTree(new File(config.bindings));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bindings_schema;
    }

    public TemplateBindingsSchema getBindingsSchema(SimpleTemplateCompilerConfig config) {
        TemplateBindingsSchema bindingsSchema = getBindingsSchema(config.bindings);
        return bindingsSchema;
    }


    public TemplateBindingsSchema getBindingsSchema(String bindings) {
        TemplateBindingsSchema bindingsSchema=null;
        if (bindings != null) {
            if (bindings.equals(Constants.OPENPROVENANCE_COMPOSITE_BEAN_JSON)) {
                try {
                    bindingsSchema=objectMapper.readValue(this.getClass().getResourceAsStream(RESOURCE_COMPOSITE_BEAN_JSON), TemplateBindingsSchema.class);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                try {
                    bindingsSchema = objectMapper.readValue(new File(bindings), TemplateBindingsSchema.class);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return bindingsSchema;
    }

    static final public ParameterizedTypeName mapIntArrayType = ParameterizedTypeName.get(ClassName.get(Map.class), TypeName.get(Integer.class), TypeName.get(int[].class));

    public Class<?> getJavaTypeForDeclaredType(Map<String, List<Descriptor>> varMap, String key) {
        Descriptor descriptor=varMap.get(key).get(0);
        switch (descriptor.getDescriptorType()) {
            case ATTRIBUTE:
                AttributeDescriptor ad=((AttributeDescriptorList) descriptor).getItems().get(0);
                String hasType = ad.getType();
                if (hasType != null) {
                    return getClassForType(hasType);
                } else {
                    System.out.println("key is " + key);
                    System.out.println("decl is " + varMap);

                    throw new UnsupportedOperationException();
                }

            case NAME:
                NameDescriptor nd=(NameDescriptor) descriptor;
                String idType=nd.getType();
                if (idType==null) {
                    return String.class;
                } else {
                    return getClassForType(idType);
                }
        }
        throw new UnsupportedOperationException("This exception is never reached");
    }

    public Class<?> getJavaTypeForDeclaredType(JsonNode the_var, String key) {
        JsonNode the_key = the_var.get(key);
        if (the_key.get(0).get("@id") != null) {
            JsonNode jsonNode = the_key.get(0).get("@type");
            String idType =  (jsonNode==null)?null:jsonNode.textValue();
            if (idType==null) {
                return String.class;
            } else {
                return getClassForType(idType);
            }
        } else {
            if (the_key.get(0).get(0) == null) {
                System.out.println("key is " + key);
                System.out.println("decl is " + the_var);

                throw new UnsupportedOperationException();
            }
            JsonNode hasType = the_key.get(0).get(0).get("@type");
            if (hasType != null) {
                String keyType = hasType.textValue();
                return getClassForType(keyType);
            } else {
                System.out.println("key is " + key);
                System.out.println("decl is " + the_var);

                throw new UnsupportedOperationException();
            }
        }
    }

    private Class<? extends Serializable> getClassForType(String keyType) {
        switch (keyType) {
            case "xsd:int":
                return Integer.class;
            case "xsd:long":
                return Long.class;
            case "xsd:string":
                return String.class;
            case "xsd:boolean":
                return Boolean.class;
            case "xsd:float":
                return Float.class;
            case "xsd:double":
                return Double.class;
            case "xsd:dateTime":
                return String.class;
            default:
                throw new UnsupportedOperationException();
        }
    }

    public Class<?> getJavaDocumentTypeForDeclaredType(Map<String, List<Descriptor>> theVar, String key) {
        if (theVar.get(key).get(0) instanceof NameDescriptor) {
            return QualifiedName.class;
        } else {
            AttributeDescriptor ad=((AttributeDescriptorList) theVar.get(key).get(0)).getItems().get(0);
            if (ad == null) {
                System.out.println("key is " + key);
                System.out.println("decl is " + theVar);
                throw new UnsupportedOperationException();
            }
            String keyType=ad.getType();
            if (keyType != null) {
                switch (keyType) {
                    case "xsd:int":
                    case "xsd:long":
                    case "xsd:string":
                    case "xsd:boolean":
                    case "xsd:float":
                    case "xsd:double":
                    case "xsd:dateTime":
                        return Object.class;
                    default:
                        throw new UnsupportedOperationException();
                }
            } else {
                System.out.println("key is " + key);
                System.out.println("decl is " + theVar);
                throw new UnsupportedOperationException("Null type");
            }
        }
    }

    public void generateSpecializedParameters(MethodSpec.Builder builder, JsonNode the_var) {
        Iterator<String> iter = the_var.fieldNames();
        while (iter.hasNext()) {
            String key = iter.next();
            builder.addParameter(getJavaTypeForDeclaredType(the_var, key), key);
        }
    }
    public void generateDocumentSpecializedParameters(MethodSpec.Builder builder, Map<String, List<Descriptor>> theVar, Collection<String> variables) {
        for (String key: variables) {
            builder.addParameter(getJavaDocumentTypeForDeclaredType(theVar, key), key);
        }
    }
    public boolean isVariableDenotingQualifiedName(String key, JsonNode the_var) {
        final JsonNode entry = the_var.path(key);

        return entry != null && !(entry instanceof MissingNode) && ( entry.get(0).get("@id") != null);
    }



    public String generateArgumentsListForCall(JsonNode the_var, Map<String,String> translator) {
        Iterator<String> iter = the_var.fieldNames();
        boolean first = true;
        String args = "";
        while (iter.hasNext()) {
            String key = iter.next();

            if (first) {
                first = false;
            } else {
                args = args + ", ";
            }
            String newName=key;
            if (translator!=null) {
                final String tmp = translator.get(key);
                if (tmp !=null) {
                    newName=tmp;
                }
            }
            args = args + newName;

        }
        return args;
    }



    public void generateSpecializedParametersJavadoc(MethodSpec.Builder builder, JsonNode the_var, JsonNode the_documentation, JsonNode the_return) {
        String docString = noNode(the_documentation) ? "No @documentation." : the_documentation.textValue();
        String retString = noNode(the_return) ? "@return not documented." : the_return.textValue();
        builder.addJavadoc(docString);
        builder.addJavadoc("\n\n");
        Iterator<String> iter = the_var.fieldNames();
        while (iter.hasNext()) {
            String key = iter.next();

            final JsonNode entry = the_var.path(key);
            if (entry != null && !(entry instanceof MissingNode)) {
                JsonNode firstNode = entry.get(0);
                if (firstNode instanceof ArrayNode) {
                    firstNode = ((ArrayNode) firstNode).get(0);
                }
                final JsonNode jsonNode = firstNode.get("@documentation");
                String documentation = noNode(jsonNode) ? "-- no @documentation" : jsonNode.textValue();
                final JsonNode jsonNode2 = firstNode.get("@type");
                String type = noNode(jsonNode2) ? "xsd:string" : jsonNode2.textValue();
                builder.addJavadoc("@param $N $L (expected type: $L)\n", key, documentation, type);
            } else {
                builder.addJavadoc("@param $N -- no bindings schemas \n", key);
            }
        }
        builder.addJavadoc(retString);
    }

    public boolean noNode(final JsonNode jsonNode2) {
        return jsonNode2 == null || jsonNode2 instanceof MissingNode || jsonNode2 instanceof NullNode;
    }


    public String generateExampleForType(String declaredType, String localPart, ProvFactory pFactory) {
        if (declaredType == null) {
            return "test_" + localPart;
        } else {
            switch (declaredType) {
                case "xsd:dateTime":
                    return pFactory.newTimeNow().toXMLFormat();
                case "xsd:float":
                    return "123.00f";
                case "xsd:int":
                    return "12345";
                default:
                    return "test_" + localPart;
            }
        }
    }

    public String createExamplar(JsonNode the_var, String key, int num, ProvFactory pFactory) {
        if (the_var.get(key).get(0).get("@examplar") != null) {
            return the_var.get(key).get(0).get("@examplar").toString();
        } else if (the_var.get(key).get(0).get("@id") != null) {
            JsonNode jsonNode1 = the_var.get(key);
            JsonNode jsonNode2=(jsonNode1==null)?null:jsonNode1.get(0);
            JsonNode jsonNode3=(jsonNode2==null)?null:jsonNode2.get("@type");
            String idType =  (jsonNode3==null)?null:jsonNode3.textValue();
            String example = generateExampleForType(idType,key, pFactory);
            Class<?> declaredJavaType=getJavaTypeForDeclaredType(the_var, key);
            final String converter = getConverterForDeclaredType2(declaredJavaType);

            if (converter == null) {
                return "\"v" + num + "\"";
            } else {
                return converter + "(" + example + ")";
            }


        } else {
            if (the_var.get(key).get(0).get(0) == null) {
                System.out.println("key is " + key);
                System.out.println("decl is " + the_var);
                throw new UnsupportedOperationException();
            }
            JsonNode hasType = the_var.get(key).get(0).get(0).get("@type");
            if (hasType != null) {
                String keyType = hasType.textValue();
                switch (keyType) {
                    case "xsd:int":
                        return "" + num;
                    case "xsd:long":
                        return "" + num + "L";
                    case "xsd:string":
                        return "\"v" + num + "\"";
                    case "xsd:boolean":
                        return "true";
                    case "xsd:float":
                        return "" + num + ".01f";
                    case "xsd:double":
                        return "" + num + ".01d";
                    case "xsd:dateTime":
                        return "\"" + pFactory.newTimeNow().toXMLFormat() + "\"";
                    default:
                        throw new UnsupportedOperationException();
                }
            } else {
                System.out.println("key is " + key);
                System.out.println("decl is " + the_var);

                throw new UnsupportedOperationException();
            }
        }

    }

    public String getDeclaredType(JsonNode the_var, String key) {
        if (the_var.get(key).get(0).get("@id") != null) {
            return "prov:QualifiedName";
        } else {
            if (the_var.get(key).get(0).get(0) == null) {
                System.out.println("key is " + key);
                System.out.println("decl is " + the_var);

                throw new UnsupportedOperationException();
            }
            JsonNode hasType = the_var.get(key).get(0).get(0).get("@type");
            if (hasType != null) {
                String keyType = hasType.textValue();
                return keyType;
            } else {
                System.out.println("key is " + key);
                System.out.println("decl is " + the_var);

                throw new UnsupportedOperationException();
            }
        }
    }

    public String varPrefix(String localPart) {
        return "__var_" + localPart;
    }

    public String attPrefix(String localPart) {
        return "__att_" + localPart;
    }


    public String getConverterForDeclaredType2(Class cl) {
        if (cl != null) {
            String keyType = cl.getName();
            switch (keyType) {
                case "java.lang.Integer":
                    return "Integer.valueOf";
                case "java.lang.Long":
                    return "Long.valueOf";
                case "java.lang.String":
                    return null;
                case "java.lang.Boolean":
                    return "Boolean.valueOf";
                case "java.lang.Float":
                    return "Float.valueOf";
                case "java.lang.Double":
                    return "Double.valueOf";
                default:
                    throw new UnsupportedOperationException();
            }
        } else {
            return null;
        }
    }

    public String getConverterForDeclaredType(Class cl) {
        if (cl != null) {
            String keyType = cl.getName();
            switch (keyType) {
                case "java.lang.Integer":
                    return "toInt";
                case "java.lang.Long":
                    return "toLong";
                case "java.lang.String":
                    return null;
                case "java.lang.Boolean":
                    return "toBoolean";
                case "java.lang.Float":
                    return "toFloat";
                case "java.lang.Double":
                    return "toDouble";
                default:
                    throw new UnsupportedOperationException();
            }
        } else {
            return null;
        }
    }

    public String getResultSetMethodForClass(Class<?> cl) {
        if (cl != null) {
            String keyType = cl.getName();
            switch (keyType) {
                case "java.lang.Integer":
                    return "getInt";
                case "java.lang.Long":
                    return "getLong";
                case "java.lang.String":
                    return "getString";
                case "java.lang.Boolean":
                    return "getBoolean";
                case "java.lang.Float":
                    return "getFloat";
                case "java.lang.Double":
                    return "getDouble";
                default:
                    throw new UnsupportedOperationException("Unknown class " + keyType);
            }
        } else {
            return null;
        }
    }

    public JavaFile specWithComment(TypeSpec typeSpec, String templateName, String packge, String className) {
        return JavaFile.builder(packge, typeSpec)
                .addFileComment("Generated Automatically by ProvToolbox ($N) for template $N", className, templateName)
                .build();
    }
}
