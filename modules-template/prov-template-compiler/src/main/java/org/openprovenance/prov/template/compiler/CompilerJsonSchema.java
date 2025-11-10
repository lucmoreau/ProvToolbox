package org.openprovenance.prov.template.compiler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.networknt.schema.JsonSchema;
import com.networknt.schema.ValidationMessage;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.template.descriptors.AttributeDescriptor;
import org.openprovenance.prov.template.descriptors.Descriptor;
import org.openprovenance.prov.template.descriptors.NameDescriptor;
import org.openprovenance.prov.template.descriptors.TemplateBindingsSchema;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

import static org.openprovenance.prov.template.compiler.ConfigProcessor.descriptorUtils;
import static org.openprovenance.prov.template.compiler.common.Constants.ELEMENTS;

public class CompilerJsonSchema {
    private final CompilerUtil compilerUtil;
    private final ProvFactory pFactory;
    ObjectMapper om = new ObjectMapper();
    Map<String, Object> jsonSchemaAsAMap = initializeSchemaMap();

    public CompilerJsonSchema(ProvFactory pFactory) {
        this.compilerUtil=new CompilerUtil(pFactory);
        this.pFactory=pFactory;
    }

    public void generateJSonSchemaEnd(String jsonschema, String root_dir) {


        new File(root_dir).mkdirs();

        final String path = root_dir + "/" + jsonschema;
        jsonSchemaAsAMap.put(get$id(), "https://openprovenance.org/template/beans#" + jsonschema);
        jsonSchemaAsAMap.put("type", "object");
        jsonSchemaAsAMap.put("title", "The Root Schema");

        try {

            om.writerWithDefaultPrettyPrinter().writeValue(new File(path), jsonSchemaAsAMap);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Map<String, Object> initializeSchemaMap() {
        final HashMap<String, Object> res = new HashMap<>();
        res.put("definitions", new HashMap<String, Object>());
        res.put("allOf", new LinkedList<HashMap<String,Object>>());
        res.put("type", "object");
        return res;
    }

    public void generateJSonSchema(String templateName, String templateFullyQualifiedName, TemplateBindingsSchema bindingsSchema, String consistsOf, String idPrefix, List<String> sharing) {


        Map<String, List<Descriptor>> theVar=bindingsSchema.getVar();
        Collection<String> variables=descriptorUtils.fieldNames(bindingsSchema);


        Map<String, Object> aSchema = new HashMap<>();
        aSchema.put(get$id(), idPrefix + templateFullyQualifiedName);
        aSchema.put("type", "object");

        Map<String, Object> properties = new HashMap<>();
        aSchema.put("properties", properties);

        List<String> requiredProperties=new LinkedList<>();
        requiredProperties.add("isA");


        for (String key: variables) {

            requiredProperties.add(key);
            Map<String, Object> atype = new HashMap<>();
            atype.put(get$id(), idPrefix +templateFullyQualifiedName+"/properties/"+key);
            atype.put("title", title(key));
            final String jsonType = convertToJsonType(compilerUtil.getJavaTypeForDeclaredType(theVar, key).getName());
            atype.put("type", jsonType);
            //final Object defaultValue=defaultValue(jsonType);
            //if (defaultValue!=null) atype.put("default", defaultValue);
            //atype.put("required", true);

            // LUC FIXME: should be generated for composite only, and subtype needs to be adjusted.
            if (consistsOf!=null && key.equals("type")) {
                atype.put("readOnly", "true");
                atype.put("default", consistsOf);
            }


            Descriptor descriptor=theVar.get(key).get(0);
            String documentation=descriptorUtils.getFromDescriptor(descriptor, AttributeDescriptor::getDocumentation, NameDescriptor::getDocumentation);
            //String type=descriptorUtils.getFromDescriptor(descriptor, AttributeDescriptor::getType, NameDescriptor::getType);
            if (documentation==null) documentation="";
            atype.put("description", documentation + " (" + jsonType + ")" );



            properties.put(key, atype);
        }

        if (consistsOf!=null) {
            String elementKey = ELEMENTS;
            requiredProperties.add(elementKey);
            Map<String, Object> atype2 = new HashMap<>();
            atype2.put(get$id(), idPrefix + templateFullyQualifiedName + "/properties/" + elementKey);
            atype2.put("title", title(elementKey));
            atype2.put("type", "array");

            // jsonform does not support $ref in items, so we need to expand the schema

            Map<String,Object>subschema= (Map<String, Object>) ((Map<String,Object>)jsonSchemaAsAMap.get("definitions")).get(consistsOf);

            Map<String, Object> subschema2 = (subschema==null)? new HashMap<>(): new HashMap<>(subschema);
            // note: the $id are not correct
            subschema2.put("title", consistsOf + " {{idx}}");
            Map<String, Object> propertiesMap = (Map<String, Object>) subschema2.get("properties");
            for (String sharingKey: sharing) {
                Map<String, Object> sharedKeyMap = (Map<String, Object>) propertiesMap.get(sharingKey);
                sharedKeyMap.put("required", "true");
                if ("integer".equals(sharedKeyMap.get("type"))) {
                    sharedKeyMap.put("maximum", -1);
                    sharedKeyMap.put("description", "<span class='shared_var'>Shared variable:</span> " + ((String)sharedKeyMap.get("description")).replace("integer", "<span class='shared_var'>convention: negative integer</span>"));
                } else {
                    sharedKeyMap.put("description", "<span class='shared_var'>Shared variable:</span> " + sharedKeyMap.get("description"));

                }
            }


            atype2.put("items",subschema2);
            properties.put(elementKey, atype2);

            ((Map<String, Object>)properties.get("count")).put("readOnly", "true");



        }

       // if (templateFullyQualifiedName==null) {
       //     System.out.println("### Warning: fully qualified name not provided for template " + templateName + ", using template name as fully qualified name");
//templateFullyQualifiedName = templateName;
       // }



        String key="isA";
        Map<String, Object> atype = new HashMap<>();
        atype.put(get$id(), idPrefix +templateFullyQualifiedName+"/properties/"+key);
        atype.put("title", title(key));
        atype.put("type", "string");
        atype.put("readOnly", "true");
        atype.put("default", templateFullyQualifiedName);
        atype.put("pattern", "^" + templateFullyQualifiedName + "$");
        properties.put(key, atype);
        atype.put("required", true);

        jsonSchemaAsAMap.put("properties", Map.of("isA", Map.of("type", "string", "pattern", "^" + templateFullyQualifiedName + "$"))); // Luc Allow more templates


        aSchema.put("required", requiredProperties);
        aSchema.put("additionalProperties", false);
        ((Map<String,Object>)jsonSchemaAsAMap.get("definitions")).put(templateFullyQualifiedName, aSchema);

        Map<String,Object> ifThenBlock=new HashMap<>();
        Map<String,Object> ifBlock=new HashMap<>();
        ifThenBlock.put("if",ifBlock);
        ifThenBlock.put("then", Map.of("$ref", idPrefix + templateFullyQualifiedName));
        ifBlock.put("type", "object");
        ifBlock.put("properties", Map.of("isA", Map.of("const", templateFullyQualifiedName)));

        ((List<Map<String,Object>>)jsonSchemaAsAMap.get("allOf")).add(ifThenBlock);

    }

    public Object defaultValue(String jsonType) {
        switch (jsonType) {
            case "integer": return 0;
            case "float": return 0.0;
        }
        return null;
    }

    public String title(String key) {
        //return "The Schema for " + key;
        return key;
    }

    boolean draft04=false;

    public String get$id() {
        if (draft04) {
            return "id";
        }
        return "$id";
    }

    static String convertToJsonType(String name) {
        switch (name) {
            case "java.lang.String":
                return "string";
            case "java.lang.Integer":
                return "integer";
            case "java.lang.Float":
                return "number";
            case "java.lang.Double":
                return "number";
            case "java.lang.Boolean":
                return "boolean";
        }
        throw new UnsupportedOperationException("conversion to json type " + name);
    }


    static class Tester extends  JsonSchemaTesting {
        @Override
        public com.networknt.schema.JsonSchema getJsonSchemaFromClasspath(String name) {
            return super.getJsonSchemaFromClasspath(name);
        }
        @Override
        public com.networknt.schema.JsonSchema getJsonSchemaFromClasspathV7(String name) {
            return super.getJsonSchemaFromClasspathV7(name);
        }

        @Override
        protected com.networknt.schema.JsonSchema getJsonSchemaFromFile(String name) throws FileNotFoundException {
            return super.getJsonSchemaFromFile(name);
        }
    }

    final Tester tester=new Tester();

    public JsonSchema setupJsonSchemaFromClasspath(String file)  {
        System.out.println("### getting schema from classpath " + file);
        com.networknt.schema.JsonSchema schema2=tester.getJsonSchemaFromClasspath(file);
        return schema2;
    }
    public JsonSchema setupJsonSchemaFromClasspathV7(String file)  {
        System.out.println("### getting schema from classpath " + file);
        com.networknt.schema.JsonSchema schema2=tester.getJsonSchemaFromClasspathV7(file);
        return schema2;
    }


    public JsonSchema setupJsonSchemaFromFile(String file) throws FileNotFoundException {
        System.out.println("#### getting schema from File " + file);
        com.networknt.schema.JsonSchema schema2=tester.getJsonSchemaFromFile(file);

        return schema2;

    }

    public Set<ValidationMessage> checkSchema(com.networknt.schema.JsonSchema schema, String file) throws IOException {
        JsonNode node = om.readTree(new File(file));
        Set<ValidationMessage> errors = schema.validate(node);
        return errors;
    }

}