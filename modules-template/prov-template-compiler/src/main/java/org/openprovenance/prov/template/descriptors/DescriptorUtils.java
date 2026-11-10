package org.openprovenance.prov.template.descriptors;

import tools.jackson.core.JsonParser;
import tools.jackson.core.Version;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.module.SimpleModule;
import tools.jackson.databind.ValueDeserializer;

import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class DescriptorUtils {
    /** Jackson 3: mappers are immutable, so the descriptor deserializer is added to a builder (was: registered on a given mapper). */
    public <B extends tools.jackson.databind.cfg.MapperBuilder<?, ?>> B setupDeserializer(B om) {
        ValueDeserializer<Descriptor> customDeserializer = new ValueDeserializer<>() {
            @Override
            public Descriptor deserialize(JsonParser jp, DeserializationContext ctxt) {
                if (jp.isExpectedStartObjectToken()) {
                    return jp.readValueAs(NameDescriptor.class);
                } else if (jp.isExpectedStartArrayToken()) {
                    AttributeDescriptor[] items = jp.readValueAs(AttributeDescriptor[].class);
                    AttributeDescriptorList res = new AttributeDescriptorList();
                    res.setItems(Arrays.asList(items));
                    return res;

                } else {
                    throw tools.jackson.databind.DatabindException.from(ctxt, "current token " + jp.currentToken()); // jackson 3: unchecked
                }
            }
        };
        SimpleModule aModule = new SimpleModule("aModule", new Version(1, 0, 0, null, "org.openprovenance", "prov-template-compiler"))
                .addDeserializer(Descriptor.class, customDeserializer);
        //	.registerSubtypes(NameDescriptor.class); // add your subtypes here.

        om.addModule(aModule);
        return om;
    }

    public Iterator<String> fieldNamesIterator(TemplateBindingsSchema descriptor) {
        return descriptor.getVar().keySet().iterator();
    }
    public Collection<String> fieldNames(TemplateBindingsSchema descriptor) {
        Map<String, List<Descriptor>> theVars = descriptor.getVar();
        return theVars.keySet().stream().filter((v) -> theVars.containsKey(v) && theVars.get(v)!=null).collect(Collectors.toList());
    }

    /** T31: the data-protection annotation in effect on a variable, once the application's policy is applied. */
    public Optional<EffectivePrivacy> getPrivacy(String key, TemplateBindingsSchema templateBindingsSchema) {
        List<Descriptor> var = templateBindingsSchema.getVar().get(key);
        if (var == null || var.isEmpty() || var.get(0) == null) return Optional.empty();
        return Optional.ofNullable(getFromDescriptor(var.get(0), AttributeDescriptor::getEffectivePrivacy, NameDescriptor::getEffectivePrivacy));
    }

    /** T31: whether a variable's value may go to a place (store, log, expansion). True for an unannotated variable. */
    public boolean keeps(String key, TemplateBindingsSchema templateBindingsSchema, EffectivePrivacy.Place place) {
        return getPrivacy(key, templateBindingsSchema).map(p -> p.keeps(place)).orElse(true);
    }

    /** T31: the variables, in order, whose value may go to a place. Equal to {@link #fieldNames} when nothing is transient. */
    public Collection<String> fieldNames(TemplateBindingsSchema templateBindingsSchema, EffectivePrivacy.Place place) {
        return fieldNames(templateBindingsSchema).stream()
                .filter(key -> keeps(key, templateBindingsSchema, place))
                .collect(Collectors.toList());
    }

    /** T31: the variables stored in SQL (column, type, insert parameter, invoker). */
    public Collection<String> storedFieldNames(TemplateBindingsSchema templateBindingsSchema) {
        return fieldNames(templateBindingsSchema, EffectivePrivacy.Place.STORE);
    }

    /** T31: the variables written to the CSV log record. */
    public Collection<String> loggedFieldNames(TemplateBindingsSchema templateBindingsSchema) {
        return fieldNames(templateBindingsSchema, EffectivePrivacy.Place.LOG);
    }

    /** T31: the variables bound in the expanded PROV document. */
    public Collection<String> expandedFieldNames(TemplateBindingsSchema templateBindingsSchema) {
        return fieldNames(templateBindingsSchema, EffectivePrivacy.Place.EXPANSION);
    }

    /** T31: whether any variable of the template carries an annotation in effect. */
    public boolean hasPrivacy(TemplateBindingsSchema templateBindingsSchema) {
        return fieldNames(templateBindingsSchema).stream().anyMatch(key -> getPrivacy(key, templateBindingsSchema).isPresent());
    }

    public boolean isSemanticType(String key, TemplateBindingsSchema templateBindingsSchema) {
        List<Descriptor> var=templateBindingsSchema.getVar().get(key);
        if (var == null) return false;
        // if (var==null) throw new NullPointerException("isOutput could not find descriptor for " + key + " in template descriptor " + templateBindingsSchema.getTemplate());
        Descriptor descriptor=var.get(0);
        Function<AttributeDescriptor, Boolean> af=(ad)->false;
        Function<NameDescriptor, Boolean> nf=(nd)->Objects.equals(nd.getSemantic(),"true");
        return getFromDescriptor(descriptor, af, nf);
    }

    public boolean isOutput(String key, TemplateBindingsSchema templateBindingsSchema) {
        List<Descriptor> var=templateBindingsSchema.getVar().get(key);
        if (var == null) return false;
        // if (var==null) throw new NullPointerException("isOutput could not find descriptor for " + key + " in template descriptor " + templateBindingsSchema.getTemplate());
        Descriptor descriptor=var.get(0);
        Function<AttributeDescriptor, Boolean> af=(ad)->OutputFieldValue.isOutput(ad.getOutput());
        Function<NameDescriptor, Boolean> nf=(nd)->OutputFieldValue.isOutput(nd.getOutput());
        return getFromDescriptor(descriptor, af, nf);
    }
    public boolean hasOutput(String key, TemplateBindingsSchema templateBindingsSchema) {
        List<Descriptor> var=templateBindingsSchema.getVar().get(key);
        if (var == null) return false;
        //if (var==null) throw new NullPointerException("hasOutput could not find descriptor for " + key + " in template descriptor " + templateBindingsSchema.getTemplate());
        Descriptor descriptor=var.get(0);
        Function<AttributeDescriptor, Boolean> af=(ad)->OutputFieldValue.hasOutput(ad.getOutput());
        Function<NameDescriptor, Boolean> nf=(nd)->OutputFieldValue.hasOutput(nd.getOutput());
        return getFromDescriptor(descriptor, af, nf);
    }
    public boolean isOutputName(String key, TemplateBindingsSchema templateBindingsSchema) {
        List<Descriptor> var = templateBindingsSchema.getVar().get(key);
        if (var == null) return false;
        //if (var == null)throw new NullPointerException("isOutputName could not find descriptor for " + key + " in template descriptor " + templateBindingsSchema.getTemplate());
        Descriptor descriptor = var.get(0);
        Function<AttributeDescriptor, Boolean> af = (ad) -> false;
        Function<NameDescriptor, Boolean> nf = (nd) -> OutputFieldValue.isOutput(nd.getOutput());
        return getFromDescriptor(descriptor, af, nf);
    }
    public Optional<String> getSqlTable(String key, TemplateBindingsSchema templateBindingsSchema) {
        List<Descriptor> var = templateBindingsSchema.getVar().get(key);

        if (var == null) return Optional.empty();
            //throw new NullPointerException("getSqlTable could not find descriptor for " + key + " in template descriptor " + templateBindingsSchema.getTemplate());
        Descriptor descriptor = var.get(0);
        Function<AttributeDescriptor, Optional<String>> af = (ad) -> Optional.empty();
        Function<NameDescriptor, Optional<String>> nf = (nd) -> Optional.ofNullable(nd.getTable());
        return getFromDescriptor(descriptor, af, nf);
    }

    public Optional<AttributeDescriptor.SqlForeign> getOutputSqlForeign(String key, TemplateBindingsSchema templateBindingsSchema) {
        List<Descriptor> var = templateBindingsSchema.getVar().get(key);
        if (var == null) return Optional.empty();
            //throw new NullPointerException("getOutputSqlForeign could not find descriptor for " + key + " in template descriptor " + templateBindingsSchema.getTemplate());
        Descriptor descriptor = var.get(0);
        Function<AttributeDescriptor, Optional<AttributeDescriptor.SqlForeign>> af = (ad) -> Optional.ofNullable(ad.getSqlForeign());
        Function<NameDescriptor, Optional<AttributeDescriptor.SqlForeign>> nf = (nd) -> Optional.empty();
        return getFromDescriptor(descriptor, af, nf);
    }
    public Optional<Map<String,String>> getSqlNewInputs(String key, TemplateBindingsSchema templateBindingsSchema) {
        List<Descriptor> var = templateBindingsSchema.getVar().get(key);
        if (var == null)
            throw new NullPointerException("getSqlNewInputs could not find descriptor for " + key + " in template descriptor " + templateBindingsSchema.getTemplate());
        Descriptor descriptor = var.get(0);
        Function<AttributeDescriptor, Optional<Map<String,String>>> af = (ad) -> Optional.empty();
        Function<NameDescriptor, Optional<Map<String,String>>> nf = (nd) -> {
            Map<String, String> newInputs = nd.getNewInputs();
            return Optional.ofNullable((newInputs==null)?null:(newInputs.isEmpty())? null: newInputs);
        };
        return getFromDescriptor(descriptor, af, nf);
    }

    public Optional<Map<String,String>> getSqlAlsoOutputs(String key, TemplateBindingsSchema templateBindingsSchema) {
        List<Descriptor> var = templateBindingsSchema.getVar().get(key);
        if (var == null)
            throw new NullPointerException("getSqlAlsoOutputs could not find descriptor for " + key + " in template descriptor " + templateBindingsSchema.getTemplate());
        Descriptor descriptor = var.get(0);
        Function<AttributeDescriptor, Optional<Map<String,String>>> af = (ad) -> Optional.empty();
        Function<NameDescriptor, Optional<Map<String,String>>> nf = (nd) -> {
            Map<String, String> newInputs = nd.getAlsoOutputs();
            return Optional.ofNullable((newInputs==null)?null:(newInputs.isEmpty())? null: newInputs);
        };
        return getFromDescriptor(descriptor, af, nf);
    }

    public Optional<Boolean> isUnique(String key, TemplateBindingsSchema templateBindingsSchema) {
        List<Descriptor> var = templateBindingsSchema.getVar().get(key);
        if (var == null) return Optional.empty();
        //throw new NullPointerException("getOutputSqlForeign could not find descriptor for " + key + " in template descriptor " + templateBindingsSchema.getTemplate());
        Descriptor descriptor = var.get(0);
        Function<AttributeDescriptor, Optional<Boolean>> af = (ad) -> Optional.of(ad.isUnique());
        Function<NameDescriptor, Optional<Boolean>> nf = (nd) -> Optional.of(nd.isUnique());
        return getFromDescriptor(descriptor, af, nf);
    }

    public boolean hasInput(String key, TemplateBindingsSchema templateBindingsSchema) {
        List<Descriptor> var=templateBindingsSchema.getVar().get(key);
        if (var==null) throw new NullPointerException("hasInput could not find descriptor for " + key + " in template descriptor " + templateBindingsSchema.getTemplate());
        Descriptor descriptor=var.get(0);
        Function<AttributeDescriptor, Boolean> af=(ad)->InputFieldValue.hasInput(ad.getInput());
        Function<NameDescriptor, Boolean> nf=(nd)->InputFieldValue.hasInput(nd.getInput());
        return getFromDescriptor(descriptor, af, nf);
    }
    public boolean isInput(String key, TemplateBindingsSchema templateBindingsSchema) {
        List<Descriptor> var=templateBindingsSchema.getVar().get(key);
        if (var==null) return false; //throw new NullPointerException("isInput could not find descriptor for " + key + " in template descriptor " + templateBindingsSchema.getTemplate());
        Descriptor descriptor=var.get(0);
        Function<AttributeDescriptor, Boolean> af=(ad)->InputFieldValue.isInput(ad.getInput());
        Function<NameDescriptor, Boolean> nf=(nd)->InputFieldValue.isInput(nd.getInput());
        return getFromDescriptor(descriptor, af, nf);
    }
    public boolean isCompulsoryInput(String key, TemplateBindingsSchema templateBindingsSchema) {
        List<Descriptor> var=templateBindingsSchema.getVar().get(key);
        if (var==null) throw new NullPointerException("isCompulsoryInput could not find descriptor for " + key + " in template descriptor " + templateBindingsSchema.getTemplate());
        Descriptor descriptor=var.get(0);
        Function<AttributeDescriptor, Boolean> af=(ad)->InputFieldValue.isCompulsoryInput(ad.getInput());
        Function<NameDescriptor, Boolean> nf=(nd)->InputFieldValue.isCompulsoryInput(nd.getInput());
        return getFromDescriptor(descriptor, af, nf);
    }

    public <T> T getFromDescriptor(Descriptor descriptor, Function<AttributeDescriptor,T> af, Function<NameDescriptor,T> nf) {
        return switch (descriptor.getDescriptorType()) {
            case ATTRIBUTE -> af.apply(((AttributeDescriptorList) descriptor).getItems().get(0));
            case NAME      -> nf.apply((NameDescriptor) descriptor);
        };
    }

    public String getSqlType(String key, TemplateBindingsSchema templateBindingsSchema) {
        List<Descriptor> var=templateBindingsSchema.getVar().get(key);
        if (var==null) throw new NullPointerException("getSqlType could not find descriptor for " + key + " in template descriptor " + templateBindingsSchema.getTemplate());
        Descriptor descriptor=var.get(0);
        return getFromDescriptor(descriptor,
                                 AttributeDescriptor::getSqlType,
                                 NameDescriptor::getSqlType);

    }


    public Map<String, String> checkSqlInputs(Map<String, String> theInputs, TemplateBindingsSchema templateBindingsSchema) {
        for (String value: theInputs.values()) {
            if (!isInput(value, templateBindingsSchema)) {
                throw new UnsupportedOperationException("Input value " + value + " is not known in template descriptor form " + templateBindingsSchema.getTemplate());
            }
        }
        return theInputs;
    }

    public Map<String, String> checkSqlOutputs(Map<String, String> theOutputs, TemplateBindingsSchema templateBindingsSchema) {
        for (String value: theOutputs.values()) {
            if (!isOutput(value, templateBindingsSchema)) {
                throw new UnsupportedOperationException("Output value " + value + " is not known in template descriptor form " + templateBindingsSchema.getTemplate());
            }
        }
        return theOutputs;
    }

    public Optional<String> getNameVariable(String var, Descriptor descriptor) {
        return getFromDescriptor(descriptor, (ad)->Optional.empty(), (nd)->Optional.of(var));
    }
    public Optional<String> getAttributeVariable(String var, Descriptor descriptor) {
        return getFromDescriptor(descriptor, (ad)->Optional.of(var), (nd)->Optional.empty() );
    }

    public List<String> getNameVariables(TemplateBindingsSchema templateBindingsSchema) {
        List<String> res=new ArrayList<>();
        for (String key: fieldNames(templateBindingsSchema)) {
            getNameVariable(key, templateBindingsSchema.getVar().get(key).get(0)).ifPresent(res::add);
        }
        return res;
    }

    public List<String> getAttributeVariables(TemplateBindingsSchema templateBindingsSchema) {
        List<String> res=new ArrayList<>();
        for (String key: fieldNames(templateBindingsSchema)) {
            getAttributeVariable(key, templateBindingsSchema.getVar().get(key).get(0)).ifPresent(res::add);
        }
        return res;
    }


}
