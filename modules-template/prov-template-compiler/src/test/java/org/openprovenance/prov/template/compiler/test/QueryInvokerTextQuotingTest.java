package org.openprovenance.prov.template.compiler.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.javapoet.JavaFile;
import junit.framework.TestCase;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.template.compiler.CompilerQueryInvoker;
import org.openprovenance.prov.template.compiler.common.Constants;
import org.openprovenance.prov.template.compiler.past.Class;
import org.openprovenance.prov.template.compiler.past.PastFactory;
import org.openprovenance.prov.template.compiler.past.emitter.Poet;
import org.openprovenance.prov.template.descriptors.DescriptorUtils;
import org.openprovenance.prov.template.descriptors.TemplateBindingsSchema;

import javax.lang.model.element.Modifier;
import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;

/**
 * Text inputs must reach the SQL composed by a generated QueryInvoker as quoted, escaped literals.
 * <p>
 * Regression: in the transport catalogue, {@code ilocation} (transporting) and {@code unit} (weighing) are
 * {@code xsd:string} inputs without an {@code @sql.type}; the generator used to append them raw
 * ({@code sb.append(bean.ilocation)}), so {@code "x"} failed with {@code column "x" does not exist}
 * and a crafted value was SQL injection. A String field without {@code @sql.type} now defaults to nullableTEXT.
 */
public class QueryInvokerTextQuotingTest extends TestCase {

    private final ProvFactory pf = new org.openprovenance.prov.vanilla.ProvFactory();
    private final ObjectMapper om = new ObjectMapper();
    private final CompilerQueryInvoker compiler = new CompilerQueryInvoker(pf);

    {
        new DescriptorUtils().setupDeserializer(om);
    }

    private static final String SCHEMA =
            "{\n" +
            "  \"var\": {\n" +
            "    \"item0\":     [{\"@id\":\"item:*\",\"@type\":\"xsd:int\",\"@input\":\"compulsory\",\"@output\":\"false\",\"@sql.table\":\"item\"}],\n" +
            "    \"weighing\":  [{\"@id\":\"act:*\",\"@type\":\"xsd:int\",\"@input\":\"false\",\"@output\":\"true\",\"@sql.table\":\"activity\"}],\n" +
            "    \"weight\":    [[{\"@value\":\"*\",\"@type\":\"xsd:double\",\"@input\":\"compulsory\",\"@sql.type\":\"double precision\"}]],\n" +
            "    \"unit\":      [[{\"@value\":\"*\",\"@type\":\"xsd:string\",\"@input\":\"optional\"}]],\n" +
            "    \"label\":     [[{\"@value\":\"*\",\"@type\":\"xsd:string\",\"@input\":\"compulsory\",\"@sql.type\":\"nonNullableTEXT\"}]],\n" +
            "    \"time\":      [[{\"@value\":\"*\",\"@type\":\"xsd:dateTime\",\"@input\":\"optional\",\"@sql.type\":\"timestamptz\"}]],\n" +
            "    \"bad\":       [[{\"@value\":\"*\",\"@type\":\"xsd:string\",\"@input\":\"optional\",\"@sql.type\":\"uuid\"}]]\n" +
            "  },\n" +
            "  \"template\": \"org.example.Weighing\"\n" +
            "}\n";

    private TemplateBindingsSchema schema() throws Exception {
        return om.readValue(SCHEMA, TemplateBindingsSchema.class);
    }

    public void testStringWithoutSqlTypeDefaultsToNullableText() throws Exception {
        assertEquals(Constants.NULLABLE_TEXT, compiler.sqlConversionFor("unit", schema(), "weighing"));
    }

    public void testExplicitSqlTypeIsKept() throws Exception {
        TemplateBindingsSchema s = schema();
        assertEquals(Constants.NON_NULLABLE_TEXT, compiler.sqlConversionFor("label", s, "weighing"));
        assertEquals(Constants.TIMESTAMPTZ, compiler.sqlConversionFor("time", s, "weighing"));
    }

    public void testNumbersAreAppendedAsIs() throws Exception {
        TemplateBindingsSchema s = schema();
        assertNull(compiler.sqlConversionFor("item0", s, "weighing"));
        assertNull(compiler.sqlConversionFor("weight", s, "weighing"));
    }

    public void testStringWithUnconvertibleSqlTypeIsRejected() throws Exception {
        try {
            compiler.sqlConversionFor("bad", schema(), "weighing");
            fail("a string variable with an @sql.type that has no converter must not be appended raw");
        } catch (IllegalStateException e) {
            assertTrue(e.getMessage(), e.getMessage().contains("'bad'"));
        }
    }

    /**
     * Emits the generated converter methods, compiles them, and checks that a value containing a quote
     * comes out as a single, well-formed SQL literal (the quote doubled), i.e. it round-trips through Postgres
     * as the original string.
     */
    public void testGeneratedConvertersEscapeQuotes() throws Exception {
        Class pastClass = new PastFactory().CLASS("Converters").MODIFIERS(Modifier.PUBLIC);
        CompilerQueryInvoker.addSpecialTypesMethods(
                Set.of(Constants.NULLABLE_TEXT, Constants.NON_NULLABLE_TEXT, Constants.TIMESTAMPTZ, Constants.SQL_DATE, Constants.JSON_TEXT),
                pastClass);
        String source = JavaFile.builder("generated", new Poet().emit(pastClass)).build().toString();

        Path dir = Files.createTempDirectory("converters");
        Path src = dir.resolve("generated").resolve("Converters.java");
        Files.createDirectories(src.getParent());
        Files.writeString(src, source);
        JavaCompiler javac = ToolProvider.getSystemJavaCompiler();
        ByteArrayOutputStream err = new ByteArrayOutputStream();
        assertEquals(source + "\n" + err, 0, javac.run(null, null, err, "-d", dir.toString(), src.toString()));

        try (URLClassLoader loader = new URLClassLoader(new URL[]{dir.toUri().toURL()}, getClass().getClassLoader())) {
            java.lang.Class<?> c = loader.loadClass("generated.Converters");
            Object conv = c.getDeclaredConstructor().newInstance();
            java.util.function.BiFunction<String, String, Object> call = (m, v) -> {
                try {
                    return c.getMethod(m, String.class).invoke(conv, v);
                } catch (ReflectiveOperationException e) {
                    throw new RuntimeException(e);
                }
            };

            assertEquals("'x'::TEXT", call.apply("convertToNullableTEXT", "x"));
            assertEquals("'O''Brien''); drop table item; --'::TEXT", call.apply("convertToNullableTEXT", "O'Brien'); drop table item; --"));
            assertEquals("''::TEXT", call.apply("convertToNullableTEXT", null));
            assertEquals("'it''s'::TEXT", call.apply("convertToNonNullableTEXT", "it's"));
            assertEquals("'2024-01-01'' or 1=1'::timestamptz", call.apply("convertToTimestamptz", "2024-01-01' or 1=1"));
            assertEquals("'2024-01-01'''::date", call.apply("convertToDate", "2024-01-01'"));
            assertEquals("'{\"a\":\"it''s\"}'::json", call.apply("convertToJsonTEXT", "{\"a\":\"it's\"}"));
        }
    }
}
