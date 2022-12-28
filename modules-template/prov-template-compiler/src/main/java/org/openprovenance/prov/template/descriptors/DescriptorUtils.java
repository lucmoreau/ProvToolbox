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
import java.util.Iterator;

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

    public Iterator<String> fieldNames(TemplateBindingsSchema descriptor) {
        return descriptor.getVar().keySet().iterator();
    }
}
