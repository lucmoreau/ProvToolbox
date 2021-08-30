package org.openprovenance.prov.template.compiler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.MissingNode;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.ValidationMessage;

import java.io.*;
import java.util.*;

public class CompilerDocumentation {
    private final CompilerUtil compilerUtil=new CompilerUtil();
    ObjectMapper om = new ObjectMapper();
    Map<String, Object> jsonSchemaAsAMap = initializeSchemaMap();

    public CompilerDocumentation() {
    }


    public Map<String, Object> initializeSchemaMap() {
        final HashMap<String, Object> res = new HashMap<String, Object>();
        res.put("definitions", new HashMap<String, Object>());
        res.put("allOf", new LinkedList<HashMap<String,Object>>());
        res.put("type", "object");
        return res;
    }

    public PrintStream getOutputStream() {
        return os;
    }

    private PrintStream os=null;



    public void generateDocumentation(String documentationFile, String templateName, String root_dir, JsonNode bindings_schema) {

        if (os==null) {
            new File(root_dir).mkdirs();
        }
        final String path = root_dir + "/" + documentationFile;


        try {
            if (os==null) {
                os = new PrintStream(new FileOutputStream(path));

                os.println("<html>");
                os.println("<body>");
                os.println("<head>");
                os.println(" <meta content=\"text/html;charset=utf-8\" http-equiv=\"Content-Type\"/>");
                os.println(" <meta content=\"utf-8\" http-equiv=\"encoding\"/>");
                os.println(" <link rel='stylesheet' href='../css/provtemplate.css'>");
                os.println("</head>");

            }

            JsonNode the_var = bindings_schema.get("var");
            JsonNode the_context = bindings_schema.get("context");
            JsonNode the_documentation = bindings_schema.get("@documentation");



            Iterator<String> iter = the_var.fieldNames();
            boolean first = true;

            os.println("<div class='csv_template' id='" + "template_" + templateName + "'>" );

            os.println("<h2>"+templateName+"</h2>");
            os.println("<div class='csv_intro'>");
            String docString = compilerUtil.noNode(the_documentation) ? "No @documentation." : the_documentation.textValue();
            os.println(docString);
            os.println("</div>");

            os.println("<ul>");



            int column=0;


            List<String> required=new LinkedList<>();
            required.add("isA");


            os.println("<li>");

            os.println(makeSpan(String.valueOf(column), "csv_column"));
            os.println(makeSpan("" + templateName+ "", "csv_field_header")+":");
            os.println(makeSpan("Constant field for the name of this template","csv_doc"));

            column++;
            os.println("</li>");


            while (iter.hasNext()) {
                String key = iter.next();
                required.add(key);

                os.println("<li>");

                os.println(makeSpan(String.valueOf(column), "csv_column"));
                os.println(makeSpan(key, "csv_field"));


                final String jsonType = convertToJsonType(this.compilerUtil.getJavaTypeForDeclaredType(the_var, key).getName());
                final String declaredType = this.compilerUtil.getDeclaredType(the_var, key);

                os.println(makeSpan(jsonType, "csv_type")+":");

                //os.println("<span class='json_type'>"+jsonType+"</span>");



                final JsonNode entry = the_var.path(key);
                if (entry != null && !(entry instanceof MissingNode)) {
                    JsonNode firstNode = entry.get(0);
                    if (firstNode instanceof ArrayNode) {
                        firstNode = ((ArrayNode) firstNode).get(0);
                    }
                    final JsonNode jsonNode = firstNode.get("@documentation");
                    String documentation = this.compilerUtil.noNode(jsonNode) ? "" : jsonNode.textValue();
                    final JsonNode jsonNode2 = firstNode.get("@type");
                    os.println(makeSpan(documentation,"csv_doc"));
                }

                os.println("</li>");

                column++;


            }






            os.println("</ul>");
            os.println("</div>");



        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


    }

    public String makeSpan(String key, String classe) {
        return "<span class='" + classe + "'>"+key+"</span> ";
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
            case "java.lang.Boolean":
                return "boolean";
        }
        throw new UnsupportedOperationException("conversion to json type " + name);
    }

    public void generateDocumentationEnd(TemplatesCompilerConfig configs, String cli_webjar_dir) {
        os.println("</body>");
        os.println("</html>");

        getOutputStream().close();

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
        public JsonSchema getJsonSchemaFromClasspath(String name) {
            return super.getJsonSchemaFromClasspath(name);
        }
        @Override
        public JsonSchema getJsonSchemaFromClasspathV7(String name) {
            return super.getJsonSchemaFromClasspathV7(name);
        }

        @Override
        protected JsonSchema getJsonSchemaFromFile(String name) throws FileNotFoundException {
            return super.getJsonSchemaFromFile(name);
        }
    }

    final Tester tester=new Tester();

    public JsonSchema setupJsonSchemaFromClasspath(String file)  {
        System.out.println("### getting schema from classpath " + file);
        JsonSchema schema2=tester.getJsonSchemaFromClasspath(file);
        return schema2;
    }
    public JsonSchema setupJsonSchemaFromClasspathV7(String file)  {
        System.out.println("### getting schema from classpath " + file);
        JsonSchema schema2=tester.getJsonSchemaFromClasspathV7(file);
        return schema2;
    }


    public JsonSchema setupJsonSchemaFromFile(String file) throws FileNotFoundException {
        System.out.println("#### getting schema from File " + file);
        JsonSchema schema2=tester.getJsonSchemaFromFile(file);

        return schema2;

    }

    public Set<ValidationMessage> checkSchema(JsonSchema schema, String file) throws IOException {
        JsonNode node = om.readTree(new File(file));
        Set<ValidationMessage> errors = schema.validate(node);
        return errors;
    }

}