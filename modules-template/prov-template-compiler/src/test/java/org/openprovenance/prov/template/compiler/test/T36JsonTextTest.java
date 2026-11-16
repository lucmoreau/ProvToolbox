package org.openprovenance.prov.template.compiler.test;

import junit.framework.TestCase;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * T36 stage 3: the Java runtime of the JSON text of an {@code @inJson} carrier,
 * {@code org.openprovenance.prov.utils.JsonText} in prov-model, against the conformance vectors every port runs
 * ({@code t36/json-text-vectors.json}). The class is looked up at run time: built without {@code -am}, this module's tests
 * compile against the installed prov-model, which may predate T36, and the test then says so and passes (as T31's does).
 */
public class T36JsonTextTest extends TestCase {

    static final String VECTORS = "src/test/resources/t36/json-text-vectors.json";
    final ObjectMapper om = JsonMapper.builderWithJackson2Defaults().build();

    static Class<?> runtime() {
        try {
            return Class.forName("org.openprovenance.prov.utils.JsonText");
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    static Object call(Class<?> c, String name, Object... args) throws Exception {
        for (Method m : c.getMethods()) {
            if (m.getName().equals(name) && m.getParameterCount() == args.length) {
                try {
                    return m.invoke(null, args);
                } catch (InvocationTargetException e) {
                    if (e.getCause() instanceof Exception) throw (Exception) e.getCause();
                    throw e;
                }
            }
        }
        throw new NoSuchMethodException(name);
    }

    public void testCanonicalVectors() throws Exception {
        Class<?> c = runtime();
        if (c == null) {
            System.out.println("[T36] JsonText is not in the prov-model on the classpath: conformance skipped (build with -am)");
            return;
        }
        JsonNode v = om.readTree(new File(VECTORS));
        int n = 0;
        for (JsonNode e : v.get("canonical")) {
            Object read = call(c, "read", e.get("input").asText());
            assertEquals(e.get("title").asText(), e.get("output").asText(), call(c, "write", read));
            n++;
        }
        assertTrue(n >= 25);
    }

    public void testRefusedVectors() throws Exception {
        Class<?> c = runtime();
        if (c == null) return;
        for (JsonNode e : om.readTree(new File(VECTORS)).get("refused")) {
            try {
                call(c, "read", e.get("input").asText());
                fail(e.get("title").asText() + ": accepted");
            } catch (IllegalArgumentException x) {
                assertTrue(x.getMessage(), x.getMessage().startsWith("T36: "));
            }
        }
    }

    public void testGetterVectors() throws Exception {
        Class<?> c = runtime();
        if (c == null) return;
        for (JsonNode e : om.readTree(new File(VECTORS)).get("getters")) {
            String title = e.get("title").asText();
            Object object = call(c, "read", e.get("input").asText());
            if (e.has("refused")) {
                try {
                    call(c, e.get("method").asText(), object, e.get("key").asText());
                    fail(title + ": accepted");
                } catch (IllegalArgumentException x) {
                    // refused
                }
                continue;
            }
            Object got = call(c, e.get("method").asText(), object, e.get("key").asText());
            JsonNode want = e.get("result");
            if (want.isNull()) {
                assertNull(title, got);
            } else if (want.isNumber()) {
                assertEquals(title, want.asDouble(), ((Number) got).doubleValue(), 0.0);
            } else if (want.isBoolean()) {
                assertEquals(title, want.asBoolean(), got);
            } else {
                assertEquals(title, want.asText(), got);
            }
        }
    }

    public void testTextVectors() throws Exception {
        Class<?> c = runtime();
        if (c == null) return;
        for (JsonNode e : om.readTree(new File(VECTORS)).get("text")) {
            String title = e.get("title").asText();
            Object value = call(c, "parse", e.get("input").asText());
            if (e.has("refused")) {
                try {
                    call(c, "text", value);
                    fail(title + ": accepted");
                } catch (IllegalArgumentException x) {
                    // refused
                }
                continue;
            }
            Object got = call(c, "text", value);
            if (e.get("result").isNull()) assertNull(title, got);
            else assertEquals(title, e.get("result").asText(), got);
        }
    }

    @SuppressWarnings("unchecked")
    public void testMergeOthersAndSame() throws Exception {
        Class<?> c = runtime();
        if (c == null) return;
        Map<String, Object> declared = (Map<String, Object>) call(c, "read", "{\"a\":1,\"b\":2}");
        Map<String, Object> others = (Map<String, Object>) call(c, "read", "{\"z\":3,\"y\":4}");
        assertEquals("declared first, then the others", "{\"a\":1,\"b\":2,\"z\":3,\"y\":4}", call(c, "write", call(c, "merge", declared, others, "w")));
        try {
            call(c, "merge", declared, call(c, "read", "{\"b\":9}"), "subtype S, carrier C");
            fail("a clash was accepted");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage(), e.getMessage().contains("subtype S, carrier C") && e.getMessage().contains("key b"));
        }
        Map<String, Object> all = (Map<String, Object>) call(c, "read", "{\"a\":1,\"x\":2,\"b\":3,\"y\":{\"q\":1}}");
        assertEquals("{\"x\":2,\"y\":{\"q\":1}}", call(c, "write", call(c, "others", all, new String[]{"a", "b"})));
        assertTrue((Boolean) call(c, "same", 3, 3L));
        assertTrue((Boolean) call(c, "same", 3, 3.0));
        assertFalse((Boolean) call(c, "same", 3, 4));
        assertTrue((Boolean) call(c, "same", "Admin", "Admin"));
        assertFalse((Boolean) call(c, "same", "Admin", "admin"));
        assertFalse((Boolean) call(c, "same", null, "Admin"));
        assertTrue((Boolean) call(c, "same", null, null));
        assertEquals("a float is written as its own shortest digits", "0.1", call(c, "toText", 0.1f));
        assertEquals("1e+21", call(c, "toText", 1e21));
        assertEquals("a map's text", "{\"a\":[1,\"x\"]}", call(c, "toText", call(c, "read", "{\"a\":[1,\"x\"]}")));
    }
}
