package org.openprovenance.prov.template.compiler;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;
import com.networknt.schema.Schema;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.template.descriptors.AttributeDescriptor;
import org.openprovenance.prov.template.descriptors.Descriptor;
import org.openprovenance.prov.template.compiler.configuration.SubtypeTemplateCompilerConfig;
import org.openprovenance.prov.template.descriptors.EffectivePrivacy;
import org.openprovenance.prov.template.descriptors.InJsonValue;
import org.openprovenance.prov.template.descriptors.PerformativeChecker;
import org.openprovenance.prov.template.descriptors.NameDescriptor;
import org.openprovenance.prov.template.descriptors.TemplateBindingsSchema;
import tools.jackson.core.JacksonException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

import static org.openprovenance.prov.template.compiler.ConfigProcessor.descriptorUtils;
import static org.openprovenance.prov.template.compiler.common.Constants.ELEMENTS;

public class CompilerJsonSchema {
    private final CompilerUtil compilerUtil;
    private final ProvFactory pFactory;
    ObjectMapper om = JsonMapper.builderWithJackson2Defaults().build();
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

        } catch (JacksonException e) {
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
            // T31 (R9): a pseudonymised property carries its method's pattern, so a form refuses a clear value early
            String privacyMark = "";
            Optional<EffectivePrivacy> privacy = descriptorUtils.getPrivacy(key, bindingsSchema);
            if (privacy.isPresent()) {
                EffectivePrivacy ep = privacy.get();
                if (ep.isPseudonym()) {
                    atype.put("pattern", ep.pattern);
                    privacyMark = " [@pseudonym(" + ep.method + "): a reference, never a clear value]";
                } else {
                    privacyMark = " [@transient(" + ep.method + "): never persisted]";
                }
            }
            // T36: a text variable that holds in-JSON values says which keys of its object they are; its type stays a string
            String inJsonMark = "";
            List<org.openprovenance.prov.template.descriptors.InJsonValue> inJson = bindingsSchema.getInJsonOf(key);
            if (!inJson.isEmpty()) {
                inJsonMark = " [a JSON object; declared keys, others allowed: " + inJson.stream()
                        .map(v -> v.getKey() + " (" + convertToJsonType(compilerUtil.getJavaTypeForDeclaredType(Map.of(v.getName(), List.of(wrapInJson(v))), v.getName()).getName())
                                + (v.getPerformative() == null ? "" : ", " + v.getPerformative()) + ")")
                        .collect(java.util.stream.Collectors.joining(", ")) + "]";
            }
            // T36 part D: whose content the value is (nothing added without @performative)
            String performative = descriptorUtils.getFromDescriptor(descriptor, AttributeDescriptor::getPerformative, NameDescriptor::getPerformative);
            String performativeMark = performative == null ? "" : " [@performative: " + performative + "]";
            atype.put("description", documentation + " (" + jsonType + ")" + privacyMark + inJsonMark + performativeMark);



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

    private static Descriptor wrapInJson(org.openprovenance.prov.template.descriptors.InJsonValue v) {
        org.openprovenance.prov.template.descriptors.AttributeDescriptorList l = new org.openprovenance.prov.template.descriptors.AttributeDescriptorList();
        l.setItems(List.of(v.getDescriptor()));
        return l;
    }

    /**
     * T36 (stage 5): the schema of a subtype's typed bean: its own {@code isA}, the inputs it keeps as fields of their own
     * (not the fixed ones), each carried value as a typed property (required when the subtype requires it), and for each carrier
     * an object of the other keys, whose members are free (the carrier is open). A catalogue's general template keeps its schema:
     * there the carrier is one string.
     */
    @SuppressWarnings("unchecked")
    public void generateSubtypeJSonSchema(SubtypeTemplateCompilerConfig st, TemplateBindingsSchema schema, String idPrefix) {
        Map<String, List<Descriptor>> theVar = schema.getVar();
        Map<String, Object> aSchema = new HashMap<>();
        aSchema.put(get$id(), idPrefix + st.fullyQualifiedName);
        aSchema.put("type", "object");
        Map<String, Object> properties = new HashMap<>();
        aSchema.put("properties", properties);
        List<String> requiredProperties = new LinkedList<>();

        Map<String, Object> isA = new HashMap<>();
        isA.put(get$id(), idPrefix + st.fullyQualifiedName + "/properties/isA");
        isA.put("title", "isA");
        isA.put("type", "string");
        isA.put("readOnly", "true");
        isA.put("default", st.fullyQualifiedName);
        isA.put("pattern", "^" + st.fullyQualifiedName + "$");
        properties.put("isA", isA);
        requiredProperties.add("isA");

        for (String key : CompilerSubtype.ordinaryInputs(schema, st)) {
            properties.put(key, ordinaryProperty(schema, key, idPrefix + st.fullyQualifiedName + "/properties/" + key));
            if (descriptorUtils.isCompulsoryInput(key, schema)) requiredProperties.add(key);
        }

        for (Map.Entry<String, List<InJsonValue>> e : CompilerSubtype.carried(schema, st).entrySet()) {
            for (InJsonValue v : e.getValue()) {
                properties.put(v.getName(), inJsonProperty(st, v, e.getKey(), idPrefix + st.fullyQualifiedName + "/properties/" + v.getName()));
                if (SubtypeTemplateCompilerConfig.REQUIRED.equals(st.carries.get(v.getName()))) requiredProperties.add(v.getName());
            }
            String other = CompilerSubtype.otherKeysField(e.getKey());
            properties.put(other, otherKeysProperty(e.getKey(), idPrefix + st.fullyQualifiedName + "/properties/" + other));
        }

        aSchema.put("required", requiredProperties);
        aSchema.put("additionalProperties", false);
        ((Map<String, Object>) jsonSchemaAsAMap.get("definitions")).put(st.fullyQualifiedName, aSchema);

        Map<String, Object> ifThenBlock = new HashMap<>();
        Map<String, Object> ifBlock = new HashMap<>();
        ifThenBlock.put("if", ifBlock);
        ifThenBlock.put("then", Map.of("$ref", idPrefix + st.fullyQualifiedName));
        ifBlock.put("type", "object");
        ifBlock.put("properties", Map.of("isA", Map.of("const", st.fullyQualifiedName)));
        ((List<Map<String, Object>>) jsonSchemaAsAMap.get("allOf")).add(ifThenBlock);
    }

    /** The property of an ordinary input kept by a subtype's typed bean (or request, or reply). */
    private Map<String, Object> ordinaryProperty(TemplateBindingsSchema schema, String key, String id) {
        Map<String, List<Descriptor>> theVar = schema.getVar();
        Map<String, Object> atype = new HashMap<>();
        atype.put(get$id(), id);
        atype.put("title", title(key));
        final String jsonType = convertToJsonType(compilerUtil.getJavaTypeForDeclaredType(theVar, key).getName());
        atype.put("type", jsonType);
        Descriptor descriptor = theVar.get(key).get(0);
        String documentation = descriptorUtils.getFromDescriptor(descriptor, AttributeDescriptor::getDocumentation, NameDescriptor::getDocumentation);
        String performative = descriptorUtils.getFromDescriptor(descriptor, AttributeDescriptor::getPerformative, NameDescriptor::getPerformative);
        atype.put("description", (documentation == null ? "" : documentation) + " (" + jsonType + ")" + (performative == null ? "" : " [@performative: " + performative + "]"));
        return atype;
    }

    /** The property of an in-JSON value a subtype carries. */
    private Map<String, Object> inJsonProperty(SubtypeTemplateCompilerConfig st, InJsonValue v, String carrier, String id) {
        Map<String, Object> atype = new HashMap<>();
        atype.put(get$id(), id);
        atype.put("title", title(v.getName()));
        final String jsonType = convertToJsonType(compilerUtil.getJavaTypeForDeclaredType(Map.of(v.getName(), List.of(wrapInJson(v))), v.getName()).getName());
        atype.put("type", jsonType);
        boolean required = SubtypeTemplateCompilerConfig.REQUIRED.equals(st.carries.get(v.getName()));
        atype.put("description", (v.getDocumentation() == null ? "" : v.getDocumentation()) + " (" + jsonType + ("json".equals(v.getType()) ? ", JSON text" : "")
                + "; the key \"" + v.getKey() + "\" of " + carrier + ", " + (required ? "required" : "optional") + ")"
                + (v.getPerformative() == null ? "" : " [@performative: " + v.getPerformative() + "]"));
        return atype;
    }

    private Map<String, Object> otherKeysProperty(String carrier, String id) {
        Map<String, Object> others = new HashMap<>();
        others.put(get$id(), id);
        others.put("title", title(CompilerSubtype.otherKeysField(carrier)));
        others.put("type", "object");
        others.put("additionalProperties", true);
        others.put("description", "The other keys of " + carrier + ": its members that no value of this subtype declares (the carrier is open)");
        return others;
    }

    /**
     * T36 part D (§12.5): for a subtype, the schema of its request and of its reply: the values that are content of each, typed,
     * the required ones required, no other property. The request's has the object of other keys of each carrier it keeps one for
     * (R13). They have no {@code isA}, so they add no {@code if isA then $ref} block: an application publishes the request's as
     * the schema of its request body. Nothing is added for a subtype with no value of the request, or none of the reply.
     */
    @SuppressWarnings("unchecked")
    public void generateMessageJSonSchemas(SubtypeTemplateCompilerConfig st, TemplateBindingsSchema schema, String idPrefix) {
        Map<String, List<String>> groups = CompilerSubtype.messageGroups(schema, st);
        for (String performative : List.of(PerformativeChecker.REQUEST, PerformativeChecker.REPLY)) {
            List<String> names = groups.get(performative);
            if (names == null) continue;
            boolean request = PerformativeChecker.REQUEST.equals(performative);
            String name = st.fullyQualifiedName + (request ? CompilerSubtype.REQUEST_SUFFIX : CompilerSubtype.REPLY_SUFFIX);
            String id = idPrefix + name;
            Map<String, Object> aSchema = new HashMap<>();
            aSchema.put(get$id(), id);
            aSchema.put("type", "object");
            aSchema.put("description", "The " + (request ? "request" : "reply") + " of the subtype " + st.fullyQualifiedName + ": the values that are content of the " + (request ? "request" : "reply") + ".");
            Map<String, Object> properties = new HashMap<>();
            aSchema.put("properties", properties);
            List<String> requiredProperties = new LinkedList<>();
            for (String key : CompilerSubtype.ordinaryInputs(schema, st)) {
                if (!names.contains(key)) continue;
                properties.put(key, ordinaryProperty(schema, key, id + "/properties/" + key));
                if (descriptorUtils.isCompulsoryInput(key, schema)) requiredProperties.add(key);
            }
            for (Map.Entry<String, List<InJsonValue>> e : CompilerSubtype.carried(schema, st).entrySet()) {
                for (InJsonValue v : e.getValue()) {
                    if (!names.contains(v.getName())) continue;
                    properties.put(v.getName(), inJsonProperty(st, v, e.getKey(), id + "/properties/" + v.getName()));
                    if (SubtypeTemplateCompilerConfig.REQUIRED.equals(st.carries.get(v.getName()))) requiredProperties.add(v.getName());
                }
            }
            if (request) {
                for (String carrier : CompilerSubtype.requestCarriers(schema, st)) {
                    String other = CompilerSubtype.otherKeysField(carrier);
                    properties.put(other, otherKeysProperty(carrier, id + "/properties/" + other));
                }
            }
            aSchema.put("required", requiredProperties);
            aSchema.put("additionalProperties", false);
            ((Map<String, Object>) jsonSchemaAsAMap.get("definitions")).put(name, aSchema);
        }
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
        public com.networknt.schema.Schema getJsonSchemaFromClasspath(String name) {
            return super.getJsonSchemaFromClasspath(name);
        }
        @Override
        public com.networknt.schema.Schema getJsonSchemaFromClasspathV7(String name) {
            return super.getJsonSchemaFromClasspathV7(name);
        }

        @Override
        protected com.networknt.schema.Schema getJsonSchemaFromFile(String name) throws FileNotFoundException {
            return super.getJsonSchemaFromFile(name);
        }
    }

    final Tester tester=new Tester();

    public Schema setupJsonSchemaFromClasspath(String file)  {
        System.out.println("### getting schema from classpath " + file);
        com.networknt.schema.Schema schema2=tester.getJsonSchemaFromClasspath(file);
        return schema2;
    }
    public Schema setupJsonSchemaFromClasspathV7(String file)  {
        System.out.println("### getting schema from classpath " + file);
        com.networknt.schema.Schema schema2=tester.getJsonSchemaFromClasspathV7(file);
        return schema2;
    }


    public Schema setupJsonSchemaFromFile(String file) throws FileNotFoundException {
        System.out.println("#### getting schema from File " + file);
        com.networknt.schema.Schema schema2=tester.getJsonSchemaFromFile(file);

        return schema2;

    }

    public Set<com.networknt.schema.Error> checkSchema(com.networknt.schema.Schema schema, String file) throws IOException {
        JsonNode node = om.readTree(new File(file));
        // json-schema-validator 3.x returns a List (1.x: a Set of ValidationMessage)
        Set<com.networknt.schema.Error> errors = new java.util.LinkedHashSet<>(schema.validate(node));
        return errors;
    }

}