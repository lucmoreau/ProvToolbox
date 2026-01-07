package org.openprovenance.prov.template.formatter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.util.*;

/**
 * Formats Bindings JSON with strict alignment rules:
 * 1. Level 1 keys vertically aligned
 * 2. Level 2 keys vertically aligned within each section
 * 3. Level 2 values vertically aligned
 * 4. First array elements aligned
 * 5. Closing brackets aligned
 * 6. Nested array elements aligned
 */
abstract public class BindingsFormatter {
    private final ObjectMapper mapper = new ObjectMapper();
    private StringBuilder sb;
    private int level2ValueColumn = -1;

    // Standard section order
    private static final List<String> LEVEL1_ORDER =
            Arrays.asList("var", "vargen", "linked", "context", "template");

    /**
     * Format a Bindings object as aligned JSON
     */
    public String format(Object bindings) throws IOException {
        String json = mapper.writeValueAsString(bindings);
        JsonNode root = mapper.readTree(json);
        return format(root);
    }

    /**
     * Format a JSON string with alignment rules
     */
    public String format(String json) throws IOException {
        JsonNode root = mapper.readTree(json);
        return format(root);
    }

    /**
     * Format a JsonNode with alignment rules
     */
    public String format(JsonNode root) {
        sb = new StringBuilder();

        if (!root.isObject()) {
            return root.toString();
        }

        ObjectNode obj = (ObjectNode) root;

        // Calculate level 1 key width for alignment
        int level1KeyWidth = LEVEL1_ORDER.stream()
                .filter(obj::has)
                .mapToInt(String::length)
                .max()
                .orElse(0);

        sb.append("{\n");

        boolean firstSection = true;
        for (String key : LEVEL1_ORDER) {
            if (!obj.has(key)) continue;

            if (!firstSection) {
                sb.append(",\n");
            }
            firstSection = false;

            formatLevel1Entry(key, obj.get(key), level1KeyWidth);
        }

        sb.append("\n}");
        return sb.toString();
    }

    private void formatLevel1Entry(String key, JsonNode value, int keyWidth) {
        sb.append("    \"").append(key).append("\"");
        int padding = keyWidth - key.length();
        for (int i = 0; i < padding; i++) sb.append(" ");
        sb.append(" : ");

        if (value.isNull()) {
            sb.append("null");
        } else if (value.isObject()) {
            formatLevel2Object((ObjectNode) value);
        } else {
            sb.append(value.toString());
        }
    }

    private void formatLevel2Object(ObjectNode obj) {
        if (obj.isEmpty()) {
            sb.append("{}");
            return;
        }

        // Calculate level 2 key width
        List<String> keys = new ArrayList<>();
        obj.fieldNames().forEachRemaining(keys::add);
        int level2KeyWidth = keys.stream()
                .mapToInt(String::length)
                .max()
                .orElse(0);

        // Calculate the column where values start (after "        \"key\" : ")
        level2ValueColumn = 8 + 1 + level2KeyWidth + 1 + 3; // indent + quote + key + quote + " : "

        sb.append("{\n");

        boolean firstField = true;
        for (String key : keys) {
            if (!firstField) {
                sb.append(",\n");
            }
            firstField = false;

            sb.append("        \"").append(key).append("\"");
            int padding = level2KeyWidth - key.length();
            for (int i = 0; i < padding; i++) sb.append(" ");
            sb.append(" : ");

            JsonNode value = obj.get(key);
            formatLevel2Value(value);
        }

        sb.append("\n    }");
    }

    private void formatLevel2Value(JsonNode value) {
        if (value.isArray()) {
            formatLevel2Array((ArrayNode) value);
        } else if (value.isObject()) {
            sb.append(value.toString());
        } else {
            sb.append(value.toString());
        }
    }

    private void formatLevel2Array(ArrayNode array) {
        if (array.isEmpty()) {
            sb.append("[]");
            return;
        }

        // Check if this is an array of arrays
        boolean isArrayOfArrays = array.size() > 0 && array.get(0).isArray();

        if (isArrayOfArrays) {
            formatArrayOfArrays(array);
        } else {
            formatSimpleArray(array);
        }
    }

    private void formatSimpleArray(ArrayNode array) {
        sb.append("[  ");

        for (int i = 0; i < array.size(); i++) {
            if (i > 0) sb.append(", ");
            sb.append(array.get(i).toString());
        }

        // Align closing bracket
        int spaces = Math.max(1, level2ValueColumn + 2 - sb.length() + sb.lastIndexOf("\n") + 1);
        for (int i = 0; i < spaces; i++) sb.append(" ");
        sb.append("]");
    }

    private void formatArrayOfArrays(ArrayNode array) {
        sb.append("[[");

        for (int i = 0; i < array.size(); i++) {
            if (i > 0) {
                sb.append(",");
                // New line and align the opening bracket
                sb.append("\n");
                for (int j = 0; j < level2ValueColumn + 1; j++) sb.append(" ");
                sb.append("[");
            }
            sb.append(" ");

            ArrayNode inner = (ArrayNode) array.get(i);
            for (int j = 0; j < inner.size(); j++) {
                if (j > 0) sb.append(", ");
                sb.append(inner.get(j).toString());
            }

            sb.append(" ]");
        }

        sb.append("]");
    }

    /**
     * Get the StringBuilder for testing purposes
     */
    StringBuilder getStringBuilder() {
        return sb;
    }
}