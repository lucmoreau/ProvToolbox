package org.openprovenance.prov.core.xml.serialization;

import tools.jackson.databind.BeanDescription;
import tools.jackson.databind.DeserializationConfig;
import tools.jackson.databind.ValueDeserializer;
import tools.jackson.databind.deser.ValueDeserializerModifier;
import tools.jackson.databind.deser.bean.BeanDeserializerBase;
import tools.jackson.databind.module.SimpleModule;

import java.util.HashSet;
import java.util.Set;

/**
 * Restores, for PROV statements, the Jackson 2 behaviour that lets the {@code @JsonAnySetter}
 * receive the PROV attribute keys ({@code type}, {@code label}, {@code location}, {@code role},
 * {@code value}).
 *
 * <p>The mix-ins mark the typed accessors ({@code getType()}, {@code getLocation()}, ...) with
 * {@code @JsonIgnore}, and route all attributes through {@code setIndexedAttributes}, the any-setter.
 * Jackson 2 only treated {@code @JsonIgnore}d names as "ignorable" when a class had no any-setter,
 * so these keys reached the any-setter. Jackson 3 registers them as ignorable unconditionally
 * (databind #5952), so the attributes would be silently dropped. This module removes these names
 * from the ignorable set of every bean deserializer that has an any-setter; other ignorals are kept.
 * Copy of the class of the same name in prov-jsonld (this module does not depend on it).
 * See JACKSON3_MIGRATION.md (catalogue of fixes).</p>
 */
public class AnySetterAttributesModule extends SimpleModule {

    /** The JSON-LD keys of PROV attributes, which are also the names of {@code @JsonIgnore}d accessors. */
    public static final Set<String> PROV_ATTRIBUTE_KEYS = Set.of("type", "label", "location", "role", "value");

    public AnySetterAttributesModule() {
        super("AnySetterAttributesModule");
    }

    @Override
    public void setupModule(SetupContext context) {
        super.setupModule(context);
        context.addDeserializerModifier(new ValueDeserializerModifier() {
            @Override
            public ValueDeserializer<?> modifyDeserializer(DeserializationConfig config,
                                                           BeanDescription.Supplier beanDescRef,
                                                           ValueDeserializer<?> deserializer) {
                if (deserializer instanceof BeanDeserializerBase bean && bean.hasAnySetter()) {
                    Set<String> ignored = new HashSet<>(beanDescRef.get().getIgnoredPropertyNames());
                    if (ignored.removeAll(PROV_ATTRIBUTE_KEYS)) {
                        return bean.withByNameInclusion(ignored, null);
                    }
                }
                return deserializer;
            }
        });
    }
}
