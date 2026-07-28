package org.openprovenance.prov.template.compiler.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.openprovenance.prov.template.compiler.past.Class;
import org.openprovenance.prov.template.compiler.past.checker.ExternalTypeRegistry;
import org.openprovenance.prov.template.compiler.past.checker.TypeChecker;
import org.openprovenance.prov.template.compiler.past.checker.TypeDiagnostic;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Coordinator for type checking that collects all PAST Classes during compilation,
 * then runs type checking after all classes have been registered.
 *
 * Follows the same two-pass pattern as {@link RustGenerationCoordinator}:
 * <pre>
 * // Pass 1: During generation, register each PAST Class:
 * TypeCheckCoordinator.register(pastClass, packageName);
 *
 * // Pass 2: After all classes are generated, run type checking:
 * TypeCheckCoordinator.finalizeTypeChecking();
 * </pre>
 */
public class TypeCheckCoordinator {
    private final ExternalTypeRegistry externalRegistry;
    private final TypeChecker typeChecker;
    private boolean finalized = false;
    private final List<Runnable> preCheckTasks = new ArrayList<>();

    TypeCheckCoordinator(ExternalTypeRegistry externalRegistry) {
        this.externalRegistry = (externalRegistry != null) ? externalRegistry : new ExternalTypeRegistry();
        this.typeChecker = new TypeChecker(externalRegistry);
    }

    TypeCheckCoordinator() {
        this.externalRegistry = new ExternalTypeRegistry();
        this.typeChecker = new TypeChecker(externalRegistry);
    }




    /**
     * Register a PAST Class for type checking.
     * This is Pass 1: collecting all classes before checking.
     */
    public void register(Class pastClass, String packageName) {
        typeChecker.registerClass(pastClass, packageName);
    }

    /**
     * Register a task to run during the type checking phase, just before {@code checkAll()} executes.
     * Use this to integrate other registration steps (e.g. Rust trait discovery) into the same phase,
     * so they benefit from having all PAST classes available but still run before code generation.
     */
    public void registerPreCheckTask(Runnable task) {
        preCheckTasks.add(task);
    }

    /**
     * Run type checking on all registered classes and report diagnostics.
     * This is Pass 2: checking all classes with full type knowledge.
     *
     * @return the list of diagnostics found
     */
    public List<TypeDiagnostic> finalizeTypeChecking() {
        if (finalized) {
            return List.of();
        }
        finalized = true;

        // Run any pre-check tasks (e.g. Rust trait discovery) before type checking executes
        for (Runnable task : preCheckTasks) task.run();
        preCheckTasks.clear();

        List<TypeDiagnostic> diagnostics = typeChecker.checkAll();

        if (!diagnostics.isEmpty()) {
            System.err.println("################## PAST Type Checker: " + diagnostics.size() + " diagnostic(s) ===");
            for (TypeDiagnostic d : diagnostics) {
                System.err.println("  " + d);
            }
            System.err.println("################## End of type checking diagnostics ===");
            System.out.println("################## Type Checker Registry Summary:\n ");
            try {
                new ObjectMapper().writerWithDefaultPrettyPrinter().writeValue(new File("target/registry.json"),typeChecker.getRegistry().getAllSignatures());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            System.out.println("##################  PAST Type Checker: No issues found.");
        }




        return diagnostics;
    }

    /**
     * Get the underlying TypeChecker (for testing or advanced use).
     */
    public TypeChecker getTypeChecker() {
        return typeChecker;
    }

    /**
     * Get the number of registered classes (for debugging).
     */
    public int getRegisteredCount() {
        return typeChecker.getRegistry().getAllSignatures().size();
    }
}
