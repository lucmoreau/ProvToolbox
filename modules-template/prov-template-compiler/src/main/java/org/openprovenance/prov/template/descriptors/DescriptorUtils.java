package org.openprovenance.prov.template.descriptors;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.function.Function;

public class DescriptorUtils {
    public void setupDeserializer(ObjectMapper om) {
        JsonDeserializer<Descriptor> customDeserializer = new JsonDeserializer<>() {
            @Override
            public Descriptor deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
                if (jp.isExpectedStartObjectToken()) {
                    return jp.readValueAs(NameDescriptor.class);
                } else if (jp.isExpectedStartArrayToken()) {
                    AttributeDescriptor[] items = jp.readValueAs(AttributeDescriptor[].class);
                    AttributeDescriptorList res = new AttributeDescriptorList();
                    res.setItems(Arrays.asList(items));
                    return res;

                } else {
                    throw new UnsupportedEncodingException("current token " + jp.getCurrentToken());
                }
            }
        };
        SimpleModule aModule = new SimpleModule("aModule", new Version(1, 0, 0, null, "org.openprovenance", "prov-template-compiler"))
                .addDeserializer(Descriptor.class, customDeserializer);
        //	.registerSubtypes(NameDescriptor.class); // add your subtypes here.

        om.registerModule(aModule);
    }

    public Iterator<String> fieldNamesIterator(TemplateBindingsSchema descriptor) {
        return descriptor.getVar().keySet().iterator();
    }
    public Collection<String> fieldNames(TemplateBindingsSchema descriptor) {
        return descriptor.getVar().keySet();
    }

    public boolean isOutput(String key, TemplateBindingsSchema templateBindingsSchema) {
        List<Descriptor> var=templateBindingsSchema.getVar().get(key);
        if (var==null) throw new NullPointerException("isOutput could not find descriptor for " + key + " in template descriptor " + templateBindingsSchema.getTemplate());
        Descriptor descriptor=var.get(0);
        Function<AttributeDescriptor, Boolean> af=(ad)->OutputFieldValue.isOutput(ad.getOutput());
        Function<NameDescriptor, Boolean> nf=(nd)->OutputFieldValue.isOutput(nd.getOutput());
        return getFromDescriptor(descriptor, af, nf);
    }
    public boolean isOutputName(String key, TemplateBindingsSchema templateBindingsSchema) {
        List<Descriptor> var = templateBindingsSchema.getVar().get(key);
        if (var == null)
            throw new NullPointerException("isOutputName could not find descriptor for " + key + " in template descriptor " + templateBindingsSchema.getTemplate());
        Descriptor descriptor = var.get(0);
        Function<AttributeDescriptor, Boolean> af = (ad) -> false;
        Function<NameDescriptor, Boolean> nf = (nd) -> OutputFieldValue.isOutput(nd.getOutput());
        return getFromDescriptor(descriptor, af, nf);
    }
    public Optional<String> getSqlTable(String key, TemplateBindingsSchema templateBindingsSchema) {
        List<Descriptor> var = templateBindingsSchema.getVar().get(key);
        if (var == null)
            throw new NullPointerException("getSqlTable could not find descriptor for " + key + " in template descriptor " + templateBindingsSchema.getTemplate());
        Descriptor descriptor = var.get(0);
        Function<AttributeDescriptor, Optional<String>> af = (ad) -> Optional.empty();
        Function<NameDescriptor, Optional<String>> nf = (nd) -> Optional.ofNullable(nd.getTable());
        return getFromDescriptor(descriptor, af, nf);
    }

    public Optional<AttributeDescriptor.SqlForeign> getOutputSqlForeign(String key, TemplateBindingsSchema templateBindingsSchema) {
        List<Descriptor> var = templateBindingsSchema.getVar().get(key);
        if (var == null)
            throw new NullPointerException("getOutputSqlForeign could not find descriptor for " + key + " in template descriptor " + templateBindingsSchema.getTemplate());
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


    public boolean isInput(String key, TemplateBindingsSchema templateBindingsSchema) {
        List<Descriptor> var=templateBindingsSchema.getVar().get(key);
        if (var==null) throw new NullPointerException("isInput could not find descriptor for " + key + " in template descriptor " + templateBindingsSchema.getTemplate());
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
        switch (descriptor.getDescriptorType()) {
            case ATTRIBUTE:
                return af.apply(((AttributeDescriptorList) descriptor).getItems().get(0));
            case NAME:
                return nf.apply((NameDescriptor) descriptor);
        }
        throw new UnsupportedOperationException("never reaching this point");
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
}
