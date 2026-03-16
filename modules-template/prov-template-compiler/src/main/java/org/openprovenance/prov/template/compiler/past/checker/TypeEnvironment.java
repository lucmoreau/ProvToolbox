package org.openprovenance.prov.template.compiler.past.checker;

import org.openprovenance.prov.template.compiler.past.type.TypeName;

import javax.lang.model.element.Modifier;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class TypeEnvironment {
    private final TypeEnvironment parent;
    private final Map<String, TypeName> variables = new HashMap<>();
    private final Map<String, Set<Modifier>> variableModifiers = new HashMap<>();
    private boolean inStaticContext;

    public TypeEnvironment() {
        this.parent = null;
        this.inStaticContext = false;
    }

    private TypeEnvironment(TypeEnvironment parent) {
        this.parent = parent;
        this.inStaticContext = parent.inStaticContext;
    }

    // --- Define variables ---

    public void define(String name, TypeName type) {
        variables.put(name, type);
        variableModifiers.put(name, Collections.emptySet());
    }

    public void define(String name, TypeName type, Set<Modifier> modifiers) {
        variables.put(name, type);
        variableModifiers.put(name, (modifiers != null) ? modifiers : Collections.emptySet());
    }

    // --- Lookup (walks up parent chain) ---

    public TypeName lookup(String name) {
        TypeName type = variables.get(name);
        if (type != null) return type;
        if (parent != null) return parent.lookup(name);
        return null;
    }

    public Set<Modifier> getModifiers(String name) {
        Set<Modifier> mods = variableModifiers.get(name);
        if (mods != null) return mods;
        if (parent != null) return parent.getModifiers(name);
        return null;
    }

    public boolean isFinal(String name) {
        Set<Modifier> mods = getModifiers(name);
        return mods != null && mods.contains(Modifier.FINAL);
    }

    public boolean isDefined(String name) {
        if (variables.containsKey(name)) return true;
        if (parent != null) return parent.isDefined(name);
        return false;
    }

    // --- Static context ---

    public boolean isInStaticContext() {
        return inStaticContext;
    }

    public void setInStaticContext(boolean inStaticContext) {
        this.inStaticContext = inStaticContext;
    }

    // --- Scope management ---

    public TypeEnvironment pushScope() {
        return new TypeEnvironment(this);
    }

    public Map<String, TypeName> getVariables() {
        return variables;
    }
}
