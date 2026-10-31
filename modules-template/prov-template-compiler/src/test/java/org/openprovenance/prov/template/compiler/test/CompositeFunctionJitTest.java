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
 * The composite insert functions (array, composite-and-linker) are declared {@code SET jit = off}: their statements
 * join set-returning functions estimated at 1000 rows each, so with JIT on PostgreSQL compiled them at every call
 * (service-throughput benchmark). A simple record's insert function keeps no setting.
 */
public class CompositeFunctionJitTest extends TestCase {

    private final ProvFactory pf = new org.openprovenance.prov.vanilla.ProvFactory();
    private final ObjectMapper om = new ObjectMapper();

    {
        new DescriptorUtils().setupDeserializer(om);
    }

    private static final String SCHEMA =
            "{\n" +
            "  \"var\": {\n" +
            "    \"currency\":  [{\"@id\":\"currency:*\",\"@type\":\"xsd:int\",\"@input\":\"false\",\"@output\":\"true\",\"@sql.table\":\"currency\"}],\n" +
            "    \"creating\":  [{\"@id\":\"act:*\",\"@type\":\"xsd:int\",\"@input\":\"false\",\"@output\":\"true\",\"@sql.table\":\"activity\"}],\n" +
            "    \"name\":      [[{\"@value\":\"*\",\"@type\":\"xsd:string\",\"@input\":\"compulsory\",\"@sql.type\":\"nonNullableTEXT\"}]]\n" +
            "  },\n" +
            "  \"template\": \"com.example.CurrencyRegistering\"\n" +
            "}\n";

    private static final String ELEMENT = "com.example.CurrencyRegistering";
    private static final String COMPOSITE = "com.example.CurrencyRegisteringComposite";

    private final Map<String, String> functionDeclarations = new LinkedHashMap<>();
    private final Map<String, String> arrayFunctionDeclarations = new LinkedHashMap<>();

    @Override
    protected void setUp() throws Exception {
        TemplateBindingsSchema schema = om.readValue(SCHEMA, TemplateBindingsSchema.class);
        Map<String, String> shortNames = new HashMap<>();
        shortNames.put(ELEMENT, "currency_registering");
        shortNames.put(COMPOSITE, "currency_registering_composite");
        Locations locations = new Locations(new TemplatesProjectConfiguration(), new HashMap<>(), shortNames, List.of(), "src", "src");

        CompilerSqlComposer composer = new CompilerSqlComposer(pf, "ID", functionDeclarations, arrayFunctionDeclarations);
        composer.generateSQLInsertFunction(null, ELEMENT, null, locations, "target/test-sql", schema, List.of());
        composer.generateSQLInsertArrayFunction(COMPOSITE, ELEMENT, locations, schema, List.of("currency"));
        composer.generateSQLInsertCompositeAndLinkerFunction(COMPOSITE, ELEMENT, locations, schema, List.of("currency"));
    }

    public void testCompositeFunctionsRunWithJitOff() {
        assertEquals(2, arrayFunctionDeclarations.size());
        arrayFunctionDeclarations.forEach((name, sql) ->
                assertTrue(name + " must end with language SQL SET jit = off\n" + sql,
                        sql.contains("language SQL SET jit = off;")));
        assertTrue(arrayFunctionDeclarations.containsKey("insert_currency_registering_composite_array"));
        assertTrue(arrayFunctionDeclarations.containsKey("insert_currency_registering_composite_and_linker"));
    }

    public void testSimpleInsertFunctionKeepsNoSetting() {
        assertEquals(1, functionDeclarations.size());
        String sql = functionDeclarations.values().iterator().next();
        assertTrue("expected language SQL\n" + sql, sql.contains("language SQL;"));
        assertFalse("a simple insert must not set jit\n" + sql, sql.contains("SET jit"));
    }
}
