package org.openprovenance.prov.template.compiler.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import junit.framework.TestCase;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.template.compiler.configuration.Locations;
import org.openprovenance.prov.template.compiler.configuration.TemplatesProjectConfiguration;
import org.openprovenance.prov.template.compiler.sql.CompilerSqlComposer;
import org.openprovenance.prov.template.descriptors.DescriptorUtils;
import org.openprovenance.prov.template.descriptors.TemplateBindingsSchema;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * T-38: a template with an {@code @unique} input column must compile to an idempotent
 * {@code LANGUAGE plpgsql} insert function (fast/slow/race paths, {@code ON CONFLICT}); a template
 * without any {@code @unique} column must keep the plain {@code LANGUAGE SQL} CTE form (backward-compat).
 */
public class IdempotentInsertFunctionTest extends TestCase {

    private final ProvFactory pf = new org.openprovenance.prov.vanilla.ProvFactory();
    private final ObjectMapper om = new ObjectMapper();

    {
        // Descriptor is polymorphic (AttributeDescriptor / NameDescriptor) — register the project's
        // custom deserializer, exactly as the compiler does when reading cbindings.
        new DescriptorUtils().setupDeserializer(om);
    }

    // currency-registering shaped: two entity outputs + one activity output + one compulsory input.
    // The @unique placeholder is substituted per test.
    private static final String SCHEMA =
            "{\n" +
            "  \"var\": {\n" +
            "    \"currency1\": [{\"@id\":\"currency:*\",\"@type\":\"xsd:int\",\"@input\":\"false\",\"@output\":\"true\",\"@sql.table\":\"currency\"}],\n" +
            "    \"currency\":  [{\"@id\":\"currency:*\",\"@type\":\"xsd:int\",\"@input\":\"false\",\"@output\":\"true\",\"@sql.table\":\"currency\"}],\n" +
            "    \"creating\":  [{\"@id\":\"act:*\",\"@type\":\"xsd:int\",\"@input\":\"false\",\"@output\":\"true\",\"@sql.table\":\"activity\"}],\n" +
            "    \"name\":      [[{\"@value\":\"*\",\"@type\":\"xsd:string\",\"@input\":\"compulsory\",\"@sql.type\":\"nonNullableTEXT\"%UNIQUE%}]]\n" +
            "  },\n" +
            "  \"template\": \"com.silubi.odoo.CurrencyRegistering\"\n" +
            "}\n";

    private String generate(boolean unique) throws Exception {
        String json = SCHEMA.replace("%UNIQUE%", unique ? ",\"@unique\":true" : "");
        TemplateBindingsSchema schema = om.readValue(json, TemplateBindingsSchema.class);

        TemplatesProjectConfiguration configs = new TemplatesProjectConfiguration();
        Map<String, String> shortNames = new HashMap<>();
        shortNames.put("com.silubi.odoo.CurrencyRegistering", "currency_registering");
        Locations locations = new Locations(configs, new HashMap<>(), shortNames, List.of(), "src", "src");

        Map<String, String> functionDeclarations = new LinkedHashMap<>();
        CompilerSqlComposer composer =
                new CompilerSqlComposer(pf, "ID", functionDeclarations, new LinkedHashMap<>());
        composer.generateSQLInsertFunction(null, "com.silubi.odoo.CurrencyRegistering", null,
                locations, "target/test-sql", schema, List.of());

        assertEquals(1, functionDeclarations.size());
        return functionDeclarations.values().iterator().next();
    }

    public void testUniqueColumnEmitsIdempotentPlpgsql() throws Exception {
        String sql = generate(true);
        assertTrue("expected LANGUAGE plpgsql\n" + sql, sql.contains("LANGUAGE plpgsql"));
        assertTrue("expected ON CONFLICT (name)\n" + sql, sql.contains("ON CONFLICT (name)"));
        assertTrue("expected partial-index predicate\n" + sql, sql.contains("name IS NOT NULL"));
        assertTrue("expected fast-path SELECT ... INTO\n" + sql, sql.contains("INTO v_ID"));
        // table-qualified column references (avoids RETURNS TABLE OUT-var ambiguity)
        assertTrue("expected qualified column ref\n" + sql, sql.contains("currency_registering.ID"));
        assertFalse("must not emit LANGUAGE SQL form\n" + sql, sql.contains("language SQL"));
    }

    public void testNonUniqueColumnKeepsLanguageSql() throws Exception {
        String sql = generate(false);
        assertTrue("expected language SQL\n" + sql, sql.contains("language SQL"));
        assertFalse("must not emit plpgsql\n" + sql, sql.contains("LANGUAGE plpgsql"));
        assertFalse("must not emit ON CONFLICT\n" + sql, sql.contains("ON CONFLICT"));
    }
}
