package org.openprovenance.prov.template.compiler.privacy;

import tools.jackson.databind.JsonNode;

import java.util.LinkedHashMap;
import java.util.Map;

/** T31 (R1, R7): the application's privacy policy file, named by the catalogue's {@code privacy} member. */
public class PolicyFile {
    /** Methods the application adds, or redefines under a built-in's name. */
    public Map<String, MethodDefinition> methods = new LinkedHashMap<>();
    /** Scope ({@code *}, {@code method:m}, {@code template:fqn}, {@code variable:fqn#var}) to decision. */
    public Map<String, JsonNode> overrides = new LinkedHashMap<>();
}
