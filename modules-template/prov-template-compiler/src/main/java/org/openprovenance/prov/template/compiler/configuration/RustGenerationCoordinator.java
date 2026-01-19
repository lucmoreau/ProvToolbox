package org.openprovenance.prov.template.compiler.configuration;

import org.openprovenance.prov.template.compiler.past.Class;
import org.openprovenance.prov.template.compiler.past.emitter.RustCodeGenerator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * Coordinator for Rust code generation that implements two-pass trait discovery.
 *
 * This class integrates with the existing SpecificationFile infrastructure by collecting
 * all Rust generation tasks in Pass 1, then executing them all in Pass 2 with full trait knowledge.
 *
 * Usage Pattern:
 * <pre>
 * // 1. Create coordinator (singleton per compilation run)
 * RustGenerationCoordinator coordinator = new RustGenerationCoordinator();
 *
 * // 2. In your generateBean/generateProcessor methods, wrap Rust generation:
 * Supplier&lt;Boolean&gt; rustGenerator = coordinator.createRustGenerator(
 *     pastClass, packageName, "target/generated-rust/src", stackTraceElement);
 *
 * // 3. After all SpecificationFile.save() calls complete, finalize:
 * coordinator.finalizeGeneration();
 * </pre>
 */
public class RustGenerationCoordinator {
    private final RustCodeGenerator codeGenerator = new RustCodeGenerator();
    private final List<Supplier<Boolean>> deferredGenerators = new ArrayList<>();
    private boolean finalized = false;

    /**
     * Create a Rust generator supplier that delays actual generation until finalization.
     *
     * This method should be called in place of direct generateRust() calls.
     * The returned Supplier performs trait discovery when called, but defers actual code
     * generation until finalizeGeneration() is invoked.
     *
     * @param pastClass The PAST class to generate code for
     * @param packageName The package name
     * @param destinationDir The destination directory
     * @param stackTraceElement Stack trace for error reporting
     * @return A Supplier that performs trait discovery and queues generation
     */
    public Supplier<Boolean> createRustGenerator(
            Class pastClass,
            String packageName,
            String destinationDir,
            StackTraceElement stackTraceElement) {

        return () -> {
            // Pass 1: Discover traits and queue for generation
            codeGenerator.registerClass(pastClass, packageName, destinationDir, stackTraceElement);
            return true;  // Return success; actual generation happens in finalizeGeneration()
        };
    }

    /**
     * Finalize Rust generation by executing all queued generation tasks.
     *
     * This must be called after all SpecificationFile.save() calls have completed.
     * It triggers Pass 2: actual code generation with full trait knowledge.
     *
     * @return true if all generation succeeded
     * @throws IOException if file writing fails
     */
    public boolean finalizeGeneration() throws IOException {
        if (finalized) {
            return true; // Already finalized
        }

        finalized = true;

        // Pass 2: Generate all code with full trait knowledge
        return codeGenerator.generateAll();
    }

    /**
     * Get the number of classes registered for generation (for debugging)
     */
    public int getRegisteredCount() {
        return codeGenerator.getKnownTraits().size();
    }

    /**
     * Reset the coordinator for a new compilation run (for testing/reuse)
     */
    public void reset() {
        deferredGenerators.clear();
        finalized = false;
        // Note: RustCodeGenerator is recreated in constructor, so we'd need a new instance
    }
}
