package org.openprovenance.prov.template.compiler.past.emitter;

import org.openprovenance.prov.template.compiler.past.Class;
import org.openprovenance.prov.template.compiler.past.checker.TypeRegistry;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Coordinator for Rust code generation with two-pass trait discovery.
 *
 * Usage:
 * <pre>
 * RustCodeGenerator generator = new RustCodeGenerator();
 *
 * // Pass 1: Register all classes for trait discovery
 * generator.registerClass(pastClass1, "com.example", "target/generated-rust/src", stackTrace1);
 * generator.registerClass(pastClass2, "com.example", "target/generated-rust/src", stackTrace2);
 *
 * // Pass 2: Generate all code with full trait knowledge
 * generator.generateAll();
 * </pre>
 */
public class RustCodeGenerator {
    private final Set<String> knownTraits = new HashSet<>();
    private final Set<String> statefulTraits = new HashSet<>();
    private final List<GenerationTask> tasks = new ArrayList<>();

    /**
     * Holds information about a single code generation task
     */
    private static class GenerationTask {
        final Class pastClass;
        final String packageName;
        final String destinationDir;
        final StackTraceElement stackTraceElement;

        GenerationTask(Class pastClass, String packageName, String destinationDir, StackTraceElement stackTraceElement) {
            this.pastClass = pastClass;
            this.packageName = packageName;
            this.destinationDir = destinationDir;
            this.stackTraceElement = stackTraceElement;
        }
    }

    /**
     * Register a class for code generation. This performs trait discovery.
     *
     * @param pastClass The PAST class to generate code for
     * @param packageName The package name
     * @param destinationDir The destination directory
     * @param stackTraceElement Stack trace for error reporting
     */
    public void registerClass(Class pastClass, String packageName, String destinationDir, StackTraceElement stackTraceElement) {
        // Discover traits from this class
       // System.out.println("Registering class for Rust generation: " + pastClass.name + " known traits so far: " + knownTraits);
        Rust rust = new Rust(knownTraits, statefulTraits);
        rust.discoverTraits(pastClass);
      //  System.out.println("Found traits before processing: " + knownTraits);
       // System.out.println("Found stateful traits: " + statefulTraits);

        // Store for later generation
        tasks.add(new GenerationTask(pastClass, packageName, destinationDir, stackTraceElement));
    }

    /**
     * Generate a single class. Call this after all classes have been registered (trait discovery
     * complete) and type checking has completed. Mirrors the per-class pattern used by Java/Python/JS.
     *
     * @param pastClass the PAST class to generate
     * @param packageName the package name
     * @param destinationDir the destination directory
     * @param stackTraceElement stack trace for error reporting
     * @param typeRegistry the TypeRegistry from the type checking phase (may be null)
     * @return true if generation succeeded
     * @throws IOException if file writing fails
     */
    public boolean generateClass(Class pastClass, String packageName, String destinationDir,
                                 StackTraceElement stackTraceElement, TypeRegistry typeRegistry) throws IOException {
        Rust rust = new Rust(knownTraits, statefulTraits, typeRegistry);
        rust.toWritableObject(pastClass, pastClass.name, packageName, stackTraceElement)
                .writeTo(new File(destinationDir));
        return true;
    }

    /**
     * Generate all registered classes using the TypeRegistry produced by type checking.
     * Call this after all classes have been registered and type checking has completed.
     *
     * @param typeRegistry the TypeRegistry from the type checking phase (may be null)
     * @return true if all generation succeeded
     * @throws IOException if file writing fails
     */
    public boolean generateAll(TypeRegistry typeRegistry) throws IOException {
        Rust rust = new Rust(knownTraits, statefulTraits, typeRegistry);

        for (GenerationTask task : tasks) {
            rust.toWritableObject(
                task.pastClass,
                task.pastClass.name,
                task.packageName,
                task.stackTraceElement
            ).writeTo(new File(task.destinationDir));
        }

        return true;
    }

    /**
     * Generate all registered classes without a TypeRegistry (for standalone / test use).
     *
     * @return true if all generation succeeded
     * @throws IOException if file writing fails
     */
    public boolean generateAll() throws IOException {
        return generateAll(null);
    }

    /**
     * Get the set of discovered traits (for debugging/inspection)
     */
    public Set<String> getKnownTraits() {
        return Collections.unmodifiableSet(knownTraits);
    }
}
