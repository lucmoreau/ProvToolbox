package org.openprovenance.prov.template.descriptors;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
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
    public boolean isInput(String key, TemplateBindingsSchema templateBindingsSchema) {
        List<Descriptor> var=templateBindingsSchema.getVar().get(key);
        if (var==null) throw new NullPointerException("isInput could not find descriptor for " + key + " in template descriptor " + templateBindingsSchema.getTemplate());
        Descriptor descriptor=var.get(0);
        Function<AttributeDescriptor, Boolean> af=(ad)->InputFieldValue.isInput(ad.getInput());
        Function<NameDescriptor, Boolean> nf=(nd)->InputFieldValue.isInput(nd.getInput());
        return getFromDescriptor(descriptor, af, nf);
    }

    private <T> T getFromDescriptor(Descriptor descriptor, Function<AttributeDescriptor,T> af, Function<NameDescriptor,T> nf) {
        switch (descriptor.getDescriptorType()) {
            case ATTRIBUTE:
                return af.apply(((AttributeDescriptorList) descriptor).getItems().get(0));
            case NAME:
                return nf.apply((NameDescriptor) descriptor);
        }
        throw new UnsupportedOperationException("never reaching this point");
    }


}
