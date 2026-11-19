package org.openprovenance.prov.template.compiler;

import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;
import com.networknt.schema.Schema;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.template.compiler.common.BeanDirection;
import org.openprovenance.prov.template.compiler.configuration.SubtypeTemplateCompilerConfig;
import org.openprovenance.prov.template.compiler.configuration.TemplatesProjectConfiguration;
import org.openprovenance.prov.template.descriptors.AttributeDescriptor;
import org.openprovenance.prov.template.descriptors.AttributeDescriptorList;
import org.openprovenance.prov.template.descriptors.InJsonValue;
import org.openprovenance.prov.template.descriptors.InputFieldValue;
import org.openprovenance.prov.template.descriptors.Descriptor;
import org.openprovenance.prov.template.descriptors.EffectivePrivacy;
import org.openprovenance.prov.template.descriptors.NameDescriptor;
import org.openprovenance.prov.template.descriptors.TemplateBindingsSchema;

import java.io.*;
import java.util.*;

import static org.openprovenance.prov.template.compiler.CompilerJsonSchema.convertToJsonType;
import static org.openprovenance.prov.template.compiler.ConfigProcessor.descriptorUtils;

public class CompilerDocumentation {
    private final CompilerUtil compilerUtil;
    ObjectMapper om = JsonMapper.builderWithJackson2Defaults().build();
    Map<String, Object> jsonSchemaAsAMap = initializeSchemaMap();

    public CompilerDocumentation(ProvFactory pFactory) {
        this.compilerUtil=new CompilerUtil(pFactory);
    }


    public Map<String, Object> initializeSchemaMap() {
        final HashMap<String, Object> res = new HashMap<>();
        res.put("definitions", new HashMap<String, Object>());
        res.put("allOf", new LinkedList<HashMap<String,Object>>());
        res.put("type", "object");
        return res;
    }

    public PrintStream getOutputStream() {
        return os;
    }

    private PrintStream os=null;



    public void generateDocumentation(String documentationFile, String templateName, String templateFullyQualifiedName, String root_dir, TemplateBindingsSchema bindingsSchema, List<String> sharing) {
        generateDocumentation(documentationFile, templateName, templateFullyQualifiedName, root_dir, bindingsSchema, sharing, List.of());
    }

    /** @param subtypes the subtypes of this template (T36), listed after its variables; none for a template without. */
    public void generateDocumentation(String documentationFile, String templateName, String templateFullyQualifiedName, String root_dir, TemplateBindingsSchema bindingsSchema, List<String> sharing,
                                      List<SubtypeTemplateCompilerConfig> subtypes) {

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

            Map<String, List<Descriptor>> theVar= bindingsSchema.getVar();


            String docString=bindingsSchema.getDocumentation();
            if (docString==null) {
                docString="No @documentation.";
            }






            os.println("<div class='csv_template' id='" + "template_" + templateFullyQualifiedName.replace('.','_') + "'>" );

            os.println("<h2>"+templateFullyQualifiedName+"</h2>");
            os.println("<div class='csv_intro'>");
            os.println(docString);
            os.println("</div>");

            os.println("<ul>");



            int column=0;


            List<String> required=new LinkedList<>();
            required.add("isA");


            os.println("<li>");

            os.println(makeSpan(String.valueOf(column), "csv_column"));
            os.println(makeSpan("" + templateFullyQualifiedName+ "", "csv_field_header")+":");
            os.println(makeSpan("Constant field for the name of this template","csv_doc"));

            column++;
            os.println("</li>");


            for (String key: descriptorUtils.fieldNames(bindingsSchema)) {


                required.add(key);

                os.println("<li>");
                os.println(makeSpan(String.valueOf(column), "csv_column"));
                os.println(makeSpan(key, "csv_field"));


                final String jsonType = convertToJsonType(compilerUtil.getJavaTypeForDeclaredType(theVar, key).getName());

                os.print(makeSpan(jsonType, "csv_type"));

                if (descriptorUtils.isInput(key,bindingsSchema)) {
                    String input="";
                    input=input+"I";
                    if (descriptorUtils.isCompulsoryInput(key,bindingsSchema)) {
                        input=input+"*";
                    }
                    os.println(makeSpan(input, "csv_input"));
                }

                if (descriptorUtils.isOutput(key,bindingsSchema)) {
                    String output="O";
                    os.println(makeSpan(output, "csv_output"));
                }

                if (sharing!=null && sharing.contains(key)) {
                    os.print(makeSpan("share-able variable", "csv_shared"));
                }

                // T31 (R12): the annotation in effect; an ignored one leaves the page as it was
                Optional<EffectivePrivacy> privacy = descriptorUtils.getPrivacy(key, bindingsSchema);
                if (privacy.isPresent()) {
                    EffectivePrivacy ep = privacy.get();
                    String mark = ep.isPseudonym()
                            ? "@pseudonym(" + ep.method + "): a reference, never a clear value"
                            : "@transient(" + ep.method + "): never persisted" + (ep.keeps(EffectivePrivacy.Place.LOG) ? "" : ", column left empty");
                    os.print(makeSpan(mark, "csv_privacy"));
                }
                os.println(":");


                Descriptor descriptor=theVar.get(key).get(0);
                String documentation=descriptorUtils.getFromDescriptor(descriptor, AttributeDescriptor::getDocumentation, NameDescriptor::getDocumentation);
                if (documentation==null) {
                    documentation="";
                }
                os.println(makeSpan(documentation,"csv_doc"));
                // T36 part D: whose content the value is (nothing written without @performative)
                String performative = descriptorUtils.getFromDescriptor(descriptor, AttributeDescriptor::getPerformative, NameDescriptor::getPerformative);
                if (performative != null) os.println(makeSpan(html(performative), "csv_performative"));

                // T36: the values this text variable holds as keys of its JSON object (empty, so nothing written, without @inJson)
                List<InJsonValue> inJson = bindingsSchema.getInJsonOf(key);
                if (!inJson.isEmpty()) {
                    os.println("<div class='csv_injson'>Keys of the JSON object this variable holds (other keys are allowed):</div>");
                    os.println("<ul class='csv_injson_keys'>");
                    for (InJsonValue v : inJson) {
                        os.println("<li>" + makeSpan(v.getKey(), "csv_injson_key")
                                + makeSpan(convertToJsonType(compilerUtil.getJavaTypeForDeclaredType(
                                        Map.of(v.getName(), List.of(wrap(v))), v.getName()).getName()), "csv_type")
                                + makeSpan(inJsonInput(v), "csv_input")
                                + (v.getPerformative() == null ? "" : makeSpan(html(v.getPerformative()), "csv_performative"))
                                + makeSpan(v.getDocumentation() == null ? "" : v.getDocumentation(), "csv_doc") + "</li>");
                    }
                    os.println("</ul>");
                }

                os.println("</li>");

                column++;


            }






            os.println("</ul>");

            // T36 (stage 5): the subtypes of the template: what each fixes and carries (nothing written for a template without)
            if (!subtypes.isEmpty()) {
                os.println("<div class='csv_subtypes'>Subtypes: a record of a subtype is a record of this template, with some inputs fixed and some values kept in a JSON text variable.</div>");
                os.println("<ul class='csv_subtypes_list'>");
                for (SubtypeTemplateCompilerConfig st : subtypes) {
                    os.println("<li>" + makeSpan(html(st.fullyQualifiedName), "csv_subtype")
                            + makeSpan("typed bean " + html(compilerUtil.beanNameClass(st.name, BeanDirection.INPUTS)), "csv_doc"));
                    os.println("<ul>");
                    for (Map.Entry<String, Object> f : st.fixed.entrySet()) {
                        os.println("<li>" + makeSpan("fixes", "csv_doc") + makeSpan(html(f.getKey()) + " = " + html(String.valueOf(f.getValue())), "csv_fixed") + "</li>");
                    }
                    for (Map.Entry<String, String> k : st.carries.entrySet()) {
                        InJsonValue v = bindingsSchema.getInJson().get(k.getKey());
                        os.println("<li>" + makeSpan("carries", "csv_doc")
                                + makeSpan(html(v.getKey()) + " in " + html(v.getCarrier()), "csv_carried")
                                + makeSpan(convertToJsonType(compilerUtil.getJavaTypeForDeclaredType(Map.of(v.getName(), List.of(wrap(v))), v.getName()).getName()), "csv_type")
                                + makeSpan(k.getValue(), "csv_input")
                                + makeSpan(v.getDocumentation() == null ? "" : html(v.getDocumentation()), "csv_doc") + "</li>");
                    }
                    // T36 part D: the subtype's values by whose content they are, when any value has a performative
                    Map<String, List<String>> groups = CompilerSubtype.messageGroups(bindingsSchema, st);
                    if (groups.containsKey("request") || groups.containsKey("reply")) {
                        for (Map.Entry<String, List<String>> g : groups.entrySet()) {
                            os.println("<li>" + makeSpan(g.getKey().equals("service") ? "the service's own" : "of the " + g.getKey(), "csv_performative")
                                    + makeSpan(html(String.join(", ", g.getValue())), "csv_doc") + "</li>");
                        }
                    }
                    os.println("</ul>");
                    os.println("</li>");
                }
                os.println("</ul>");
            }
            os.println("</div>");



        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


    }

    /** One flow: the table of each value of the beans it fills, its kind and where it comes from (design §13.5). It reads the list the generated code is written from. */
    private void flowTable(FlowModel flow) {
        os.println("<div class='csv_flow' id='flow_" + html(flow.flow.fullyQualifiedName.replace('.', '_')) + "'>");
        os.println("<div class='csv_flow_title'>" + makeSpan("Flow " + html(flow.flow.fullyQualifiedName), "csv_flow_name")
                + makeSpan("from " + html(flow.from.fullyQualifiedName) + " to " + html(flow.records.stream().map(FlowModel.Rec::describe).collect(java.util.stream.Collectors.joining(", "))), "csv_doc") + "</div>");
        os.println("<div class='csv_doc'>Every input of the backend's bean and of the user bean has one origin, said here and in a comment beside the assignment in the generated code.</div>");
        os.println("<table class='csv_flow_table'>");
        os.println("<tr><th>step</th><th>value</th><th>kind</th><th>comes from</th></tr>");
        for (Map.Entry<String, List<FlowModel.Item>> step : flow.steps.entrySet()) {
            for (FlowModel.Item i : step.getValue()) {
                os.println("<tr><td>" + html(i.step) + "</td><td>" + html(i.target) + "</td><td class='csv_kind'>" + html(i.kind.word) + "</td><td>" + html(i.describe()) + "</td></tr>");
            }
        }
        os.println("</table>");
        os.println("</div>");
    }

    private static String html(String text) {
        return org.apache.commons.text.StringEscapeUtils.escapeHtml4(text);
    }

    /** T36: a value's descriptor as the one-item list the type lookups expect. */
    private static Descriptor wrap(InJsonValue v) {
        AttributeDescriptorList l = new AttributeDescriptorList();
        l.setItems(List.of(v.getDescriptor()));
        return l;
    }

    private static String inJsonInput(InJsonValue v) {
        if (v.getInput() == InputFieldValue.Compulsory) return "I*";
        return v.getInput() == InputFieldValue.Optional ? "I" : "";
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


    public void generateDocumentationEnd(TemplatesProjectConfiguration configs, String cli_webjar_dir) {
        generateDocumentationEnd(configs, cli_webjar_dir, List.of());
    }

    /** @param flows T36 part E: the flows of the catalogue, each as a table of where its values come from, after the templates; nothing written without a flow. */
    public void generateDocumentationEnd(TemplatesProjectConfiguration configs, String cli_webjar_dir, List<FlowModel> flows) {
        if (os!=null) { // insure this had been initialised
            for (FlowModel flow : flows) flowTable(flow);
            os.println("</body>");
            os.println("</html>");

            getOutputStream().close();
        }

    }


    static class Tester extends  JsonSchemaTesting {
        @Override
        public Schema getJsonSchemaFromClasspath(String name) {
            return super.getJsonSchemaFromClasspath(name);
        }
        @Override
        public Schema getJsonSchemaFromClasspathV7(String name) {
            return super.getJsonSchemaFromClasspathV7(name);
        }

        @Override
        protected Schema getJsonSchemaFromFile(String name) throws FileNotFoundException {
            return super.getJsonSchemaFromFile(name);
        }
    }

    final Tester tester=new Tester();

    public Schema setupJsonSchemaFromClasspath(String file)  {
        System.out.println("### getting schema from classpath " + file);
        Schema schema2=tester.getJsonSchemaFromClasspath(file);
        return schema2;
    }
    public Schema setupJsonSchemaFromClasspathV7(String file)  {
        System.out.println("### getting schema from classpath " + file);
        Schema schema2=tester.getJsonSchemaFromClasspathV7(file);
        return schema2;
    }


    public Schema setupJsonSchemaFromFile(String file) throws FileNotFoundException {
        System.out.println("#### getting schema from File " + file);
        Schema schema2=tester.getJsonSchemaFromFile(file);

        return schema2;

    }


}