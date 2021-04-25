package org.openprovenance.prov.template.compiler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.MissingNode;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.ValidationMessage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

public class CompilerJsonSchema {
    private final CompilerUtil compilerUtil=new CompilerUtil();
    ObjectMapper om = new ObjectMapper();
    Map<String, Object> jsonSchemaAsAMap = initializeSchemaMap();

    public CompilerJsonSchema() {
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
        final HashMap<String, Object> res = new HashMap<String, Object>();
        res.put("definitions", new HashMap<String, Object>());
        res.put("allOf", new LinkedList<HashMap<String,Object>>());
        res.put("type", "object");
        return res;
    }

    public void generateJSonSchema(String jsonschema, String templateName, String root_dir, JsonNode bindings_schema) {


        JsonNode the_var = bindings_schema.get("var");
        JsonNode the_context = bindings_schema.get("context");


        Iterator<String> iter = the_var.fieldNames();
        boolean first = true;

        Map<String, Object> aSchema = new HashMap<String, Object>();
        aSchema.put(get$id(), "#/definitions/" + templateName);
        aSchema.put("type", "object");

        Map<String, Object> properties = new HashMap<String, Object>();
        aSchema.put("properties", properties);

        List<String> required=new LinkedList<>();
        required.add("isA");


        while (iter.hasNext()) {
            String key = iter.next();
            required.add(key);
            Map<String, Object> atype = new HashMap<String, Object>();
            atype.put(get$id(), "#/definitions/"+templateName+"/properties/"+key);
            atype.put("title", title(key));
            final String jsonType = convertToJsonType(compilerUtil.getJavaTypeForDeclaredType(the_var, key).getName());
            atype.put("type", jsonType);
            final Object defaultValue=defaultValue(jsonType);
            if (defaultValue!=null) atype.put("default", defaultValue);
            //atype.put("required", true);

            final JsonNode entry = the_var.path(key);
            if (entry != null && !(entry instanceof MissingNode)) {
                JsonNode firstNode = entry.get(0);
                if (firstNode instanceof ArrayNode) {
                    firstNode = ((ArrayNode) firstNode).get(0);
                }
                final JsonNode jsonNode = firstNode.get("@documentation");
                String documentation = compilerUtil.noNode(jsonNode) ? "" : jsonNode.textValue();
                final JsonNode jsonNode2 = firstNode.get("@type");
                atype.put("description", documentation + " (" + jsonType + ")" );
            }


            properties.put(key, atype);
        }

        String key="isA";
        Map<String, Object> atype = new HashMap<String, Object>();
        atype.put(get$id(), "#/definitions/"+templateName+"/properties/"+key);
        atype.put("title", title(key));
        atype.put("type", "string");
        atype.put("readOnly", "true");
        atype.put("default", templateName);
        atype.put("pattern", "^" + templateName + "$");
        properties.put(key, atype);
        atype.put("required", true);

        jsonSchemaAsAMap.put("properties", Map.of("isA", Map.of("type", "string", "pattern", "^template_block$"))); // Luc Allow more templates


        aSchema.put("required", required);
        aSchema.put("additionalProperties", false);
        ((Map<String,Object>)jsonSchemaAsAMap.get("definitions")).put(templateName, aSchema);

        Map<String,Object> ifThenBlock=new HashMap<>();
        Map<String,Object> ifBlock=new HashMap<>();
        ifThenBlock.put("if",ifBlock);
        ifThenBlock.put("then", Map.of("$ref","#/definitions/" + templateName));
        ifBlock.put("type", "object");
        ifBlock.put("properties", Map.of("isA", Map.of("const", templateName)));

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

    String convertToJsonType(String name) {
        switch (name) {
            case "java.lang.String":
                return "string";
            case "java.lang.Integer":
                return "integer";
            case "java.lang.Float":
                return "float";
        }
        throw new UnsupportedOperationException("conversion to json type " + name);
    }

    /*
    public JsonSchema setupJsonSchemaCheck() throws ProcessingException, IOException {
        return setupJsonSchemaCheck("src/main/resources/schema/json-schema-v4.json");
    }



    public JsonSchema setupJsonSchemaCheck(String file) throws
            IOException, ProcessingException {
        final JsonNode schemaJSON = JsonLoader.fromPath(file);
        final JsonSchemaFactory factory = JsonSchemaFactory.byDefault();
        JsonSchema schema = factory.getJsonSchema(schemaJSON);
        return schema;
    }

    public boolean checkSchema(JsonSchema schema, String file) throws IOException, ProcessingException {
        System.out.println("Loading " + file);
        final JsonNode fileJSON = JsonLoader.fromPath(file);
        ProcessingReport report = schema.validate(fileJSON);
        if (!report.isSuccess()) {
            System.err.println("Cannot validate " + file + " against the PROV-JSON schema.");
            System.err.println(report);
        } else {
            System.out.println(report);
        }
        return (report.isSuccess());
    }

     */

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