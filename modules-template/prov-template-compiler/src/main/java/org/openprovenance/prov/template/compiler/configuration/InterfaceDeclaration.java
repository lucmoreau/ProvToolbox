package org.openprovenance.prov.template.compiler.configuration;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Declaration-owned definition of a project-level bean interface
 * ({@link TemplatesProjectConfiguration#interfaces}): the interface's members are
 * declared here, and templates conform to it — rather than the contract being
 * inferred from the first declaring template's bean.
 *
 * <p>Two member kinds, either or both:</p>
 * <ul>
 *   <li>{@code variables} — name-based accessor pairs ({@code getTime}/{@code setTime}
 *       for variable {@code time}); requires the variable name to be uniform across
 *       declaring templates.</li>
 *   <li>{@code roles} — role-based accessor pairs ({@code getCause}/{@code setCause}
 *       for role {@code cause}); the role→variable binding stays per-template in the
 *       cbindings ({@code @role}), so declaring templates may bind different variables.</li>
 * </ul>
 *
 * <p>JSON forms: a plain array of strings is backward-compatible shorthand for
 * {@code variables}; the object form {@code {"variables": [...], "roles": [...]}}
 * is the full syntax.</p>
 */
@JsonDeserialize(using = InterfaceDeclaration.Deserializer.class)
public class InterfaceDeclaration {

    public List<String> variables;
    public List<String> roles;

    @Override
    public String toString() {
        return "InterfaceDeclaration{variables=" + variables + ", roles=" + roles + '}';
    }

    /** Accepts both the legacy array form (= variables) and the object form. */
    public static class Deserializer extends JsonDeserializer<InterfaceDeclaration> {
        @Override
        public InterfaceDeclaration deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            InterfaceDeclaration declaration = new InterfaceDeclaration();
            if (p.currentToken() == JsonToken.START_ARRAY) {
                declaration.variables = ctxt.readValue(p, ctxt.getTypeFactory()
                        .constructCollectionType(List.class, String.class));
                return declaration;
            }
            JsonNode node = p.readValueAsTree();
            declaration.variables = toList(node.get("variables"));
            declaration.roles = toList(node.get("roles"));
            return declaration;
        }

        private static List<String> toList(JsonNode node) {
            if (node == null || node.isNull()) return null;
            List<String> result = new LinkedList<>();
            node.forEach(item -> result.add(item.asText()));
            return result;
        }
    }
}
