package org.openprovenance.prov.template.compiler.configuration;

import org.openprovenance.prov.template.compiler.past.checker.TypeRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * Coordinator that defers all code generation (Java, Python, JavaScript, Rust) until after
 * type checking. Each task receives the finalized {@link TypeRegistry} so generators can
 * exploit type information where appropriate.
 *
 * <pre>
 * // Pass 1: During save(), register each generation task:
 * codeGenCoordinator.addTask(registry -> emit(pastClass, registry));
 *
 * // Pass 2: After type checking, execute all tasks with the registry:
 * codeGenCoordinator.finalizeGeneration(typeRegistry);
 * </pre>
 */
public class CodeGenerationCoordinator {
    private final List<Function<TypeRegistry, Boolean>> tasks = new ArrayList<>();

    public void addTask(Function<TypeRegistry, Boolean> task) {
        tasks.add(task);
    }

    public boolean finalizeGeneration(TypeRegistry registry) {
        boolean allSuccess = true;
        for (Function<TypeRegistry, Boolean> task : tasks) {
            boolean result = task.apply(registry);
            allSuccess = result && allSuccess;
        }
        return allSuccess;
    }
}
