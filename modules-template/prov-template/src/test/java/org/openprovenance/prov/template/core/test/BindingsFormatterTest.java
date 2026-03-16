package org.openprovenance.prov.template.core.test;

import org.openprovenance.prov.template.formatter.BindingsFormatter;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.*;

/**
 * Tests for BindingsFormat alignment rules
 */
abstract public class BindingsFormatterTest {
    private final BindingsFormatter formatter = new BindingsFormatter();
    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testLevel1KeysAligned() throws IOException {
        String json = "{\"var\":{},\"vargen\":{},\"linked\":{},\"context\":{},\"template\":null}";
        String result = formatter.format(json);

        String[] lines = result.split("\n");
        int colonPos = -1;

        for (String line : lines) {
            if (line.contains("\"var\"") || line.contains("\"vargen\"") ||
                    line.contains("\"linked\"") || line.contains("\"context\"") ||
                    line.contains("\"template\"")) {
                int pos = line.indexOf(" : ");
                if (colonPos == -1) {
                    colonPos = pos;
                } else {
                    assertEquals("Level 1 keys not aligned: " + line, colonPos, pos);
                }
            }
        }

        assertTrue("No level 1 keys found", colonPos > 0);
    }

    @Test
    public void testLevel2KeysAligned() throws IOException {
        String json = "{\"var\":{\"agent\":[{\"@id\":\"var:engineer\"}]," +
                "\"responsible\":[{\"@id\":\"var:org\"}]}}";
        String result = formatter.format(json);

        String[] lines = result.split("\n");
        int colonPos = -1;

        for (String line : lines) {
            if (line.contains("\"agent\"") || line.contains("\"responsible\"")) {
                int pos = line.indexOf(" : ");
                if (colonPos == -1) {
                    colonPos = pos;
                } else {
                    assertEquals("Level 2 keys not aligned: " + line, colonPos, pos);
                }
            }
        }

        assertTrue("No level 2 keys found", colonPos > 0);
    }

    @Test
    public void testLevel2ValuesAligned() throws IOException {
        String json = "{\"var\":{\"a\":[1],\"abc\":[2],\"ab\":[3]}}";
        String result = formatter.format(json);

        String[] lines = result.split("\n");
        int valueStartPos = -1;

        for (String line : lines) {
            if (line.contains("\"a\"") || line.contains("\"abc\"") ||
                    line.contains("\"ab\"")) {
                int colonPos = line.indexOf(" : ");
                int start = colonPos + 3; // after " : "

                if (valueStartPos == -1) {
                    valueStartPos = start;
                } else {
                    assertEquals("Level 2 values not aligned: " + line, valueStartPos, start);
                }
            }
        }

        assertTrue("No level 2 values found", valueStartPos > 0);
    }

    @Test
    public void testSimpleArrayFormat() throws IOException {
        String json = "{\"var\":{\"agent\":[{\"@id\":\"var:engineer\"}]}}";
        String result = formatter.format(json);

        assertTrue("Simple array should start with [ {", result.contains("[ {"));
        assertTrue("Simple array should end with } ]", result.contains("} ]"));
    }

    @Test
    public void testArrayOfArraysFormat() throws IOException {
        String json = "{\"var\":{\"agtype\":[[{\"@id\":\"plead:Engineer\"}," +
                "{\"@id\":\"prov:Person\"}]]}}";
        String result = formatter.format(json);
        System.out.println("Formatted result:\n" + result);

        assertTrue("Array of arrays should start with [[ {", result.contains("[[ {"));
        assertTrue("Array of arrays should end with } ]]", result.contains("} ]]"));
    }

    @Test
    public void testArrayOfArraysAlignment() throws IOException {
        String json = "{\"var\":{\"types\":[[{\"@id\":\"a\"}],[{\"@id\":\"b\"}]]}}";
        String result = formatter.format(json);

        // Each nested array should start with "[" at same position
        String[] lines = result.split("\n");
        for (String line : lines) {
            if (line.contains("\"types\"")) {
                int firstBracket = line.indexOf("[[ ");
                assertTrue("Array of arrays should have [[ ", firstBracket > 0);
                break;
            }
        }
    }

    @Test
    public void testNullTemplate() throws IOException {
        String json = "{\"template\":null}";
        String result = formatter.format(json);

        assertTrue("Null template should be formatted correctly",
                result.contains("\"template\" : null"));
    }

    @Test
    public void testEmptyObjects() throws IOException {
        String json = "{\"var\":{},\"context\":{}}";
        String result = formatter.format(json);

        assertTrue("Should contain var", result.contains("\"var\""));
        assertTrue("Should contain context", result.contains("\"context\""));
        assertTrue("Empty objects should be formatted as {}", result.contains("{}"));
    }

    @Test
    public void testPleadFilteringExample() throws IOException {
        // Load the actual plead-filtering.json resource
        InputStream is = getClass().getResourceAsStream(
                "/bindings/plead-examples/plead-filtering.json");
        assertNotNull("plead-filtering.json not found", is);

        String json = new String(is.readAllBytes(), StandardCharsets.UTF_8);
        String result = formatter.format(json);

        System.out.println("Formatted plead-filtering.json:\n" + result);

        // Verify it's valid JSON
        JsonNode node = mapper.readTree(result);
        assertNotNull("Formatted output should be valid JSON", node);

        // Verify sections present
        assertTrue("Should contain var section", result.contains("\"var\""));
        assertTrue("Should contain vargen section", result.contains("\"vargen\""));
        assertTrue("Should contain linked section", result.contains("\"linked\""));
        assertTrue("Should contain context section", result.contains("\"context\""));
        assertTrue("Should contain template section", result.contains("\"template\""));

        // Verify alignment by checking all level 1 colons are at same position
        String[] lines = result.split("\n");
        int level1ColonPos = -1;
        for (String line : lines) {
            if (line.matches("\\s+\"(var|vargen|linked|context|template)\".*")) {
                int pos = line.indexOf(" : ");
                if (level1ColonPos == -1) {
                    level1ColonPos = pos;
                } else {
                    assertEquals("Level 1 alignment broken at: " + line, level1ColonPos, pos);
                }
            }
        }
    }

    @Test
    public void testContextAlignment() throws IOException {
        String json = "{\"context\":{\"plead\":\"http://example.org/plead#\"," +
                "\"prov\":\"http://www.w3.org/ns/prov#\"," +
                "\"sk\":\"http://example.org/sk#\"}}";
        String result = formatter.format(json);

        String[] lines = result.split("\n");
        int colonPos = -1;

        for (String line : lines) {
            if (line.contains("\"plead\"") || line.contains("\"prov\"") ||
                    line.contains("\"sk\"")) {
                int pos = line.indexOf(" : ");
                if (colonPos == -1) {
                    colonPos = pos;
                } else {
                    assertEquals("Context keys not aligned: " + line, colonPos, pos);
                }
            }
        }
    }
}
