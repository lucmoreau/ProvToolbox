package org.openprovenance.prov.template.compiler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
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
import org.openprovenance.prov.model.Attribute;
import org.openprovenance.prov.model.Bundle;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.model.ProvUtilities;
import org.openprovenance.prov.model.QualifiedName;
import org.openprovenance.prov.model.Statement;
import org.openprovenance.prov.model.ValueConverter;
import org.openprovenance.prov.template.expander.ExpandUtil;
import org.openprovenance.prov.template.log2prov.FileBuilder;

import com.google.common.base.CaseFormat;
import com.squareup.javapoet.TypeSpec.Builder;

import static org.openprovenance.prov.template.compiler.ConfigProcessor.objectMapper;


public class CompilerUtil {
    

    public String capitalize(String templateName) {
        return templateName.substring(0, 1).toUpperCase()+templateName.substring(1);
    }

    public String templateNameClass(String templateName) {
        return capitalize(templateName) + "Builder";
    }
    public String beanNameClass(String templateName) {
        return capitalize(templateName) + "Bean";
    }
    public String continuationNameClass(String templateName) {
        return capitalize(templateName) + "Continuation";
    }

    public String loggerName(String template) {
        return "log" + capitalize(template);

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
  
    public Builder generateClassBuilder3(String name) {
        return TypeSpec.classBuilder(name)
                .addModifiers(Modifier.PUBLIC);
    }
   
    public Builder generateClassBuilder2(String name) {
        return TypeSpec.classBuilder(name)
                .superclass(FileBuilder.class)
                .addModifiers(Modifier.PUBLIC)
                .addField(ProvFactory.class, "pf", Modifier.PRIVATE, Modifier.FINAL)
                .addField(ValueConverter.class, "vc", Modifier.PRIVATE, Modifier.FINAL);
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
        return CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, s); 
    }
    
    final static ProvUtilities u = new ProvUtilities();
        

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


    public JsonNode get_bindings_schema(TemplateCompilerConfig config) {
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

    static final ParameterizedTypeName hashmapType = ParameterizedTypeName.get(ClassName.get(HashMap.class), TypeName.get(Integer.class), TypeName.get(int[].class));

    public Class<?> getJavaTypeForDeclaredType(JsonNode the_var, String key) {
        if (the_var.get(key).get(0).get("@id") != null) {
            return String.class;
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
            } else {
                System.out.println("key is " + key);
                System.out.println("decl is " + the_var);

                throw new UnsupportedOperationException();
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
        if (the_var.get(key).get(0).get("@id") != null) {
            return "\"v" + num + "\"";
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

}
