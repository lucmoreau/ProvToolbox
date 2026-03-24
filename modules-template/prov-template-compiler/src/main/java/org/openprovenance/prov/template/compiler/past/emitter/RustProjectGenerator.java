package org.openprovenance.prov.template.compiler.past.emitter;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Utility class to generate and compile Rust projects from PAST-generated code.
 * Creates a complete Cargo project structure with proper module organization.
 */
public class RustProjectGenerator {

    private final Path projectRoot;
    private final Path srcDir;
    private final List<String> generatedModules = new ArrayList<>();

    public RustProjectGenerator(String projectRootPath) {
        this.projectRoot = Paths.get(projectRootPath);
        this.srcDir = this.projectRoot.resolve("src");
    }

    /**
     * Generates a complete Cargo project structure including Cargo.toml, lib.rs, and main.rs
     */
    public boolean generateCargo(String projectName, String version) throws IOException {
        // Create project directories
        Files.createDirectories(projectRoot);
        Files.createDirectories(srcDir);

        // Generate Cargo.toml
        generateCargoToml(projectName, version);

        // Discover all generated Rust modules
        discoverModules();

        // Generate lib.rs with module declarations
        generateLibRs();

        // Generate main.rs with test instantiations
        generateMainRs(projectName);

        return true;
    }

    /**
     * Generates the Cargo.toml file with necessary dependencies
     */
    private void generateCargoToml(String projectName, String version) throws IOException {
        StringBuilder cargo = new StringBuilder();
        cargo.append("[package]\n");
        cargo.append("name = \"").append(toSnakeCase(projectName)).append("\"\n");
        cargo.append("version = \"").append(version).append("\"\n");
        cargo.append("edition = \"2021\"\n");
        cargo.append("\n");
        cargo.append("[dependencies]\n");
        cargo.append("serde = { version = \"1.0\", features = [\"derive\"] }\n");
        cargo.append("serde_json = \"1.0\"\n");
        cargo.append("\n");
        cargo.append("[[bin]]\n");
        cargo.append("name = \"").append(toSnakeCase(projectName)).append("\"\n");
        cargo.append("path = \"src/main.rs\"\n");
        cargo.append("\n");
        cargo.append("[registries.crates-io]\n" +
                "protocol = \"sparse\"\n" );


        /*
        cargo.append("[source.crates-io]\n" +
                "replace-with = \"vendored-sources\"\n" +
                "\n" +
                "[source.vendored-sources]\n" +
                "directory = \"vendor\"\n") ;

         */

        Path cargoToml = projectRoot.resolve("Cargo.toml");
        Files.writeString(cargoToml, cargo.toString());
        System.out.println("Generated: " + cargoToml);
    }

    /**
     * Discovers all .rs files in the src directory and builds module paths
     */
    private void discoverModules() throws IOException {
        generatedModules.clear();

        if (!Files.exists(srcDir)) {
            return;
        }

        try (Stream<Path> paths = Files.walk(srcDir)) {
            paths.filter(path -> path.toString().endsWith(".rs"))
                    .filter(path -> !path.getFileName().toString().equals("lib.rs"))
                    .filter(path -> !path.getFileName().toString().equals("main.rs"))
                    .filter(path -> !path.getFileName().toString().equals("mod.rs"))
                    .forEach(path -> {
                        Path relativePath = srcDir.relativize(path);
                        String modulePath = relativePath.toString()
                                .replace(File.separator, "::")
                                .replace(".rs", "");
                        generatedModules.add(modulePath);
                    });
        }

        System.out.println("Discovered " + generatedModules.size() + " modules");
    }

    /**
     * Generates lib.rs with all module declarations organized hierarchically
     */
    private void generateLibRs() throws IOException {
        StringBuilder lib = new StringBuilder();
        lib.append("// Generated library file for compiled PAST modules\n");
        lib.append("// This file declares all generated modules\n\n");

        // Group modules by their top-level package
        var modulesByPackage = generatedModules.stream()
                .collect(Collectors.groupingBy(m -> m.split("::")[0]));

        // Generate module declarations hierarchically
        for (String topLevel : modulesByPackage.keySet().stream().sorted().collect(Collectors.toList())) {
            lib.append("pub mod ").append(topLevel).append(";\n");
        }

        lib.append("\n");
        lib.append("// Re-export commonly used types\n");
        // Track type names to avoid duplicate re-exports (different modules can define types with the same name)
        Set<String> seenTypeNames = new HashSet<>();
        Set<String> duplicateTypeNames = new HashSet<>();
        for (String module : generatedModules) {
            String typeName = getTypeNameFromModule(module);
            if (typeName != null) {
                if (!seenTypeNames.add(typeName)) {
                    duplicateTypeNames.add(typeName);
                }
            }
        }
        for (String module : generatedModules) {
            String typeName = getTypeNameFromModule(module);
            if (typeName != null && !duplicateTypeNames.contains(typeName)) {
                lib.append("pub use ").append(module).append("::").append(typeName).append(";\n");
            }
        }

        Path libRs = srcDir.resolve("lib.rs");
        Files.writeString(libRs, lib.toString());
        //System.out.println("Generated: " + libRs);
    }

    /**
     * Generates main.rs that instantiates and tests generated classes
     */
    private void generateMainRs(String projectName) throws IOException {
        switch (projectName.toLowerCase()) {
            case "templates":
                generateMainRsForTemplates();
                return;
            case "transport_template_library":
                generateMainRsForTransport();
                return; 
            default:
                System.out.println("No custom main.rs generation logic for project: " + projectName + ", generating default main.rs");
                break;
        }
    }

    private void generateMainRsForTransport() throws IOException {

        StringBuilder main = new StringBuilder();
        main.append("// Generated main file to test compiled PAST modules\n\n");

        // Find a suitable class to instantiate (prefer one with "Bean" in the name)
        String testModule = findTestModule();

        if (testModule != null) {
            String typeName = getTypeNameFromModule(testModule);

            // Declare the root module before using it
            String rootModule = testModule.split("::")[0];
            main.append("mod ").append(rootModule).append(";\n\n");

            main.append("use ").append(testModule).append("::").append(typeName).append(";\n\n");

            main.append("fn main() {\n");
            main.append("    println!(\"Testing generated Rust code from PAST...\");\n");
            main.append("    println!();\n\n");

            main.append("    // Create an instance of ").append(typeName).append("\n");
            main.append("    let mut instance = ").append(typeName).append("::new();\n");

            main.append("""
                instance.item1=Some(1);
                instance.item0=Some(2);
                instance.item= Some(3);
                instance.receiver=     Some(4);
                instance.giver=     Some(5);
                instance.handingover=     Some(6);
                instance.time=     Some("some date".to_string());
                """);

            main.append("\n\n");

            main.append("    println!(\"Created instance of ").append(typeName).append("\");\n");
            main.append("    println!(\"{:?}\", instance);\n");
            main.append("    println!();\n\n");

            main.append("    // Test JSON serialization\n");
            main.append("    match instance.to_json() {\n");
            main.append("        Ok(json) => {\n");
            main.append("            println!(\"JSON representation:\");\n");
            main.append("            println!(\"{}\", json);\n");
            main.append("        }\n");
            main.append("        Err(e) => println!(\"JSON serialization error: {}\", e),\n");
            main.append("    }\n");
            main.append("}\n");
        } else {
            // Fallback if no modules found
            main.append("fn main() {\n");
            main.append("    println!(\"No generated modules found to test.\");\n");
            main.append("    println!(\"Please generate some Rust code first.\");\n");
            main.append("}\n");
        }

        Path mainRs = srcDir.resolve("main.rs");
        Files.writeString(mainRs, main.toString());
        //System.out.println("Generated: " + mainRs);
    }


    private void generateMainRsForTemplates() throws IOException {

        StringBuilder main = new StringBuilder();
        main.append("// Generated main file to test compiled PAST modules\n\n");

        // Find a suitable class to instantiate (prefer one with "Bean" in the name)
        String testModule = findTestModule();

        if (testModule != null) {
            String typeName = getTypeNameFromModule(testModule);

            // Declare the root module before using it
            String rootModule = testModule.split("::")[0];
            main.append("mod ").append(rootModule).append(";\n\n");

            main.append("use ").append(testModule).append("::").append(typeName).append(";\n\n");

            main.append("fn main() {\n");
            main.append("    println!(\"Testing generated Rust code from PAST...\");\n");
            main.append("    println!();\n\n");

            main.append("    // Create an instance of ").append(typeName).append("\n");
            main.append("    let instance = ").append(typeName).append("::new()l\n");
            main.append("        // Add default constructor arguments here\n");
            main.append("        // TODO: Customize based on actual constructor signature\n");
            main.append("""
                            Some("added".to_string()),
                            Some("AdditionType".to_string()),
                            Some("Bob".to_string()),
                            Some("input1".to_string()),
                            Some("10".to_string()),
                            Some("input2".to_string()),
                            Some(42),
                            Some("output".to_string()),
                            None,
                            Some(118)\
                            """);

            main.append("   \n\n");

            main.append("    println!(\"Created instance of ").append(typeName).append("\");\n");
            main.append("    println!(\"{:?}\", instance);\n");
            main.append("    println!();\n\n");

            main.append("    // Test JSON serialization\n");
            main.append("    match instance.to_json() {\n");
            main.append("        Ok(json) => {\n");
            main.append("            println!(\"JSON representation:\");\n");
            main.append("            println!(\"{}\", json);\n");
            main.append("        }\n");
            main.append("        Err(e) => println!(\"JSON serialization error: {}\", e),\n");
            main.append("    }\n");
            main.append("}\n");
        } else {
            // Fallback if no modules found
            main.append("fn main() {\n");
            main.append("    println!(\"No generated modules found to test.\");\n");
            main.append("    println!(\"Please generate some Rust code first.\");\n");
            main.append("}\n");
        }

        Path mainRs = srcDir.resolve("main.rs");
        Files.writeString(mainRs, main.toString());
       // System.out.println("Generated: " + mainRs);
    }

    /**
     * Generates necessary mod.rs files for module hierarchy
     */
    public void generateModFiles() throws IOException {
        // Track all directories that need mod.rs files
        var directoriesWithModules = new java.util.TreeSet<Path>();

        try (Stream<Path> paths = Files.walk(srcDir)) {
            paths.filter(path -> path.toString().endsWith(".rs"))
                    .filter(path -> !path.getFileName().toString().equals("lib.rs"))
                    .filter(path -> !path.getFileName().toString().equals("main.rs"))
                    .filter(path -> !path.getFileName().toString().equals("mod.rs"))
                    .forEach(path -> {
                        Path dir = path.getParent();
                        while (dir != null && dir.startsWith(srcDir) && !dir.equals(srcDir)) {
                            directoriesWithModules.add(dir);
                            dir = dir.getParent();
                        }
                    });
        }

        // Generate mod.rs for each directory
        for (Path dir : directoriesWithModules) {
            generateModRsForDirectory(dir);
        }
    }

    /**
     * Generates mod.rs file for a specific directory
     */
    private void generateModRsForDirectory(Path dir) throws IOException {
        StringBuilder modRs = new StringBuilder();
        modRs.append("// Generated module file\n\n");

        // List all .rs files and subdirectories in this directory
        try (Stream<Path> paths = Files.list(dir)) {
            List<Path> children = paths.sorted().collect(Collectors.toList());

            for (Path child : children) {
                String name = child.getFileName().toString();

                if (Files.isDirectory(child)) {
                    // It's a subdirectory - declare as module
                    modRs.append("pub mod ").append(name).append(";\n");
                } else if (name.endsWith(".rs") && !name.equals("mod.rs")) {
                    // It's a .rs file - declare as module
                    String moduleName = name.replace(".rs", "");
                    modRs.append("pub mod ").append(moduleName).append(";\n");
                }
            }
        }

        Path modFile = dir.resolve("mod.rs");
        Files.writeString(modFile, modRs.toString());
        //System.out.println("Generated: " + modFile);
    }

    /**
     * Compiles the Rust project using cargo build
     * @return true if compilation succeeds, false otherwise
     */
    public boolean compileProject() {
        System.out.println("\n=== Compiling Rust project ===");
        System.out.println("Working directory: " + projectRoot);

        try {
            ProcessBuilder pb = new ProcessBuilder("cargo", "build");
            pb.directory(projectRoot.toFile());
            pb.redirectErrorStream(true);

            Process process = pb.start();

            // Read and print output
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }
            }

            int exitCode = process.waitFor();

            if (exitCode == 0) {
                System.out.println("\n✓ Compilation successful!");
                return true;
            } else {
                System.err.println("\n✗ Compilation failed with exit code: " + exitCode);
                return false;
            }

        } catch (IOException e) {
            System.err.println("Error running cargo: " + e.getMessage());
            System.err.println("Make sure Rust and Cargo are installed: https://rustup.rs/");
            return false;
        } catch (InterruptedException e) {
            System.err.println("Compilation interrupted: " + e.getMessage());
            Thread.currentThread().interrupt();
            return false;
        }
    }

    /**
     * Runs the compiled Rust binary
     * @return true if execution succeeds, false otherwise
     */
    public boolean runProject() {
        System.out.println("\n=== Running Rust project ===");

        try {
            ProcessBuilder pb = new ProcessBuilder("cargo", "run");
            pb.directory(projectRoot.toFile());
            pb.redirectErrorStream(true);

            Process process = pb.start();

            // Read and print output
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }
            }

            int exitCode = process.waitFor();

            if (exitCode == 0) {
                System.out.println("\n✓ Execution successful!");
                return true;
            } else {
                System.err.println("\n✗ Execution failed with exit code: " + exitCode);
                return false;
            }

        } catch (IOException e) {
            System.err.println("Error running cargo: " + e.getMessage());
            return false;
        } catch (InterruptedException e) {
            System.err.println("Execution interrupted: " + e.getMessage());
            Thread.currentThread().interrupt();
            return false;
        }
    }

    /**
     * Complete workflow: generate, compile, and run
     */
    public boolean generateCompileAndRun(String projectName, String version) {
        try {
            System.out.println("=== Generating Rust Project ===");
            if (!generateCargo(projectName, version)) {
                System.err.println("Failed to generate Cargo project");
                return false;
            }

            generateModFiles();

            if (!compileProject()) {
                System.err.println("Compilation failed, skipping execution");
                return false;
            }

            return runProject();

        } catch (IOException e) {
            System.err.println("Error during project generation: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Helper methods

    private String findTestModule() {
        // Prefer modules with "bean" in the name
        return generatedModules.stream()
                .filter(m -> m.toLowerCase().contains("bean"))
                .findFirst()
                .orElse(generatedModules.isEmpty() ? null : generatedModules.get(0));
    }

    private String getTypeNameFromModule(String modulePath) {
        // Convert module path to likely type name
        // e.g., "org::example::templates::block::client::common::template_block_bean"
        // -> "TemplateBlockBean"
        String[] parts = modulePath.split("::");
        if (parts.length == 0) return null;

        String filename = parts[parts.length - 1];
        return toPascalCase(filename);
    }

    private String toSnakeCase(String name) {
        if (name == null || name.isEmpty()) return name;

        StringBuilder result = new StringBuilder();
        result.append(Character.toLowerCase(name.charAt(0)));

        for (int i = 1; i < name.length(); i++) {
            char c = name.charAt(i);
            if (Character.isUpperCase(c)) {
                result.append('_');
                result.append(Character.toLowerCase(c));
            } else {
                result.append(c);
            }
        }

        return result.toString();
    }

    private String toPascalCase(String name) {
        if (name == null || name.isEmpty()) return name;

        StringBuilder result = new StringBuilder();
        boolean capitalizeNext = true;

        for (int i = 0; i < name.length(); i++) {
            char c = name.charAt(i);
            if (c == '_') {
                capitalizeNext = true;
            } else {
                if (capitalizeNext) {
                    result.append(Character.toUpperCase(c));
                    capitalizeNext = false;
                } else {
                    result.append(c);
                }
            }
        }

        return result.toString();
    }
}
