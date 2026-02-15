package org.openprovenance.prov.template.compiler.past.checker;

import org.openprovenance.prov.template.compiler.past.type.ClassName;
import org.openprovenance.prov.template.compiler.past.type.TypeName;
import org.openprovenance.prov.template.compiler.past.type.TypeVariable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ExternalTypeBuilder {
    private final ExternalTypeRegistry registry;
    private final ClassName className;
    private final List<Runnable> pending = new ArrayList<>();
    private final List<TypeVariable> typeParams = new ArrayList<>();

    ExternalTypeBuilder(ExternalTypeRegistry registry, ClassName className) {
        this.registry = registry;
        this.className = className;
    }
    ExternalTypeBuilder(ExternalTypeRegistry registry, ClassName className, TypeVariable... typeParams) {
        this.registry = registry;
        this.className = className;
        if (typeParams != null) Collections.addAll(this.typeParams, typeParams);
    }


    public ExternalTypeBuilder method(String name, TypeName returnType, TypeName... params) {
        List<TypeName> paramList = List.of(params);
        pending.add(() -> registry.registerMethod(className, name, paramList, returnType, false));
        return this;
    }

    public ExternalTypeBuilder staticMethod(String name, TypeName returnType, TypeName... params) {
        List<TypeName> paramList = List.of(params);
        pending.add(() -> registry.registerMethod(className, name, paramList, returnType, true));
        return this;
    }

    public ExternalTypeBuilder field(String name, TypeName type) {
        pending.add(() -> registry.registerField(className, name, type));
        return this;
    }

    public ExternalTypeBuilder constructor(TypeName... params) {
        List<TypeName> paramList = List.of(params);
        pending.add(() -> registry.registerConstructor(className, paramList));
        return this;
    }

    public void register() {
        if (!typeParams.isEmpty()) {
            registry.registerTypeParams(className, typeParams);
        }
        for (Runnable r : pending) {
            r.run();
        }
        pending.clear();
    }
}
