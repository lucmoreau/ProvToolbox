package org.openprovenance.prov.template.compiler.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openprovenance.prov.template.compiler.CompilerUtil;
import org.openprovenance.prov.template.compiler.past.Class;
import org.openprovenance.prov.template.compiler.past.checker.ExternalTypeRegistry;
import org.openprovenance.prov.template.compiler.past.checker.TypeDiagnostic;
import org.openprovenance.prov.template.compiler.past.checker.TypeRegistry;
import org.openprovenance.prov.template.compiler.past.emitter.Poet;
import org.openprovenance.prov.template.compiler.past.emitter.RustCodeGenerator;
import org.openprovenance.prov.template.compiler.past.emitter.RustProjectGenerator;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.function.Supplier;

import static org.openprovenance.prov.template.compiler.common.Constants.DOT_JAVA_EXTENSION;
import static org.openprovenance.prov.template.compiler.past.checker.ExternalTypeRegistry.initializeExternalRegistry;

public class SpecificationFile {
    //logger declaration
    static Logger logger = LogManager.getLogger(SpecificationFile.class);

    final private CompilerUtil compilerUtil;

    final private JavaFile javaFile;
    final private String directory;
    final private String fileName;
    final private String class_package;
    private final String pyDirectory;
    private final String pyFilename;
    private final Supplier<String> pyContent;
    private final Supplier<Boolean> javaGenerator;
    private final Supplier<Boolean> pythonGenerator;
    private final Supplier<Boolean> jsGenerator;
    private final Supplier<Boolean> rustGenerator;

    public SpecificationFile(Supplier<Boolean> javaGenerator, Supplier<Boolean> pythonGenerator) {
        this.compilerUtil=null;
        this.javaFile=null;
        this.directory=null;
        this.fileName=null;
        this.class_package=null;
        this.pyDirectory=null;
        this.pyFilename=null;
        this.pyContent=null;
        this.javaGenerator=javaGenerator;
        this.pythonGenerator=pythonGenerator;
        this.jsGenerator=null;
        this.rustGenerator=null;

    }
    public SpecificationFile(Supplier<Boolean> javaGenerator, Supplier<Boolean> pythonGenerator, Supplier<Boolean> jsGenerator, Supplier<Boolean> rustGenerator) {
        this.compilerUtil=null;
        this.javaFile=null;
        this.directory=null;
        this.fileName=null;
        this.class_package=null;
        this.pyDirectory=null;
        this.pyFilename=null;
        this.pyContent=null;
        this.javaGenerator=javaGenerator;
        this.pythonGenerator=pythonGenerator;
        this.jsGenerator=jsGenerator;
        this.rustGenerator=rustGenerator;
    }

    static public Supplier<Boolean> emptyGenerator= () -> true;


    class JavaInUse {
        public java.util.function.BiFunction<String, String, Integer> f() {
            return (String x, String y) -> 5;
        }
    }

    static boolean rustProjectCreated=false;
    static RustCodeGenerator rustCodeGenerator = new RustCodeGenerator();
    static ExternalTypeRegistry externalRegistry = initializeExternalRegistry(new ExternalTypeRegistry());
    static TypeCheckCoordinator typeCheckCoordinator = new TypeCheckCoordinator(externalRegistry);
    static CodeGenerationCoordinator codeGenCoordinator = new CodeGenerationCoordinator();
    static TypeRegistry typeRegistry = null;



    /**
     * Reset the Rust code generator for a new compilation run.
     */
    public static void resetRustCoordinator() {
        rustCodeGenerator = new RustCodeGenerator();
    }

    /**
     * Reset the type check coordinator for a new compilation run.
     */
    public static void resetTypeCheckCoordinator() {
        typeCheckCoordinator = new TypeCheckCoordinator(externalRegistry);
        typeRegistry = null;
    }

    /**
     * Reset the code generation coordinator for a new compilation run.
     * Also resets Rust state since Rust generation is part of the same coordinator.
     */
    public static void resetCodeGenCoordinator() {
        codeGenCoordinator = new CodeGenerationCoordinator();
        resetRustCoordinator();
    }

    /**
     * Finalize type checking after all PAST classes have been registered.
     * Call this after all SpecificationFile.save() calls complete, before code generation.
     * Stores the resulting TypeRegistry for use by generators.
     *
     * @return the list of type diagnostics found
     */
    public static List<TypeDiagnostic> finalizeTypeChecking() {
        List<TypeDiagnostic> diagnostics = typeCheckCoordinator.finalizeTypeChecking();
        typeRegistry = typeCheckCoordinator.getTypeChecker().getRegistry();
        return diagnostics;
    }

    /**
     * Execute all deferred code generation tasks (Java, Python, JavaScript, Rust).
     * Call this after finalizeTypeChecking(). Each task receives the TypeRegistry
     * so generators can exploit type information where appropriate.
     *
     * @return true if all generation tasks succeeded
     */
    public static boolean finalizeCodeGeneration() {
        System.out.println("################## Code generation started (Java/Python/JavaScript/Rust)...");
        return codeGenCoordinator.finalizeGeneration(typeRegistry);
    }

    /**
     * Return the TypeRegistry produced by type checking.
     * Available after finalizeTypeChecking() has been called.
     * Generators may use this to exploit type information where appropriate.
     */
    public static TypeRegistry getTypeRegistry() {
        return typeRegistry;
    }

    public boolean save() {
        boolean javaGen=true;
        boolean pyGen=true;
        boolean jsGen=true;
        boolean rustGen=true;
        if (javaGenerator!=null) {
            javaGen = javaGenerator.get();
        }
        if ( pythonGenerator!=null) {
            pyGen = pythonGenerator.get();
        }
        if (jsGenerator!=null) {
            jsGen = jsGenerator.get();
        }
        if (rustGenerator!=null) {
            rustGen = rustGenerator.get();
        }
        return javaGen && pyGen && jsGen && rustGen;
    }


    public static void compileRustProject(String rust_dir, String projectName, String version) {
        if (rust_dir==null) return;
        System.out.println("##################Rust compilation started...");

        if (!rustProjectCreated) {
            // create a Cargo.toml file
            rustProjectCreated=true;
            RustProjectGenerator generator = new RustProjectGenerator("target/generated-rust");

            try {
                generator.generateCargo(projectName, version);
                generator.generateModFiles();
                if (generator.compileProject()) {
                    generator.runProject();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public JavaFile getJavaFile() {
        return javaFile;
    }

    public String getDirectory() {
        return directory;
    }

    public String getFileName() {
        return fileName;
    }

    public String getClassPackage() {
        return class_package;
    }

    public static boolean generateJava(org.openprovenance.prov.template.compiler.past.Class pastClass, String packageName, TemplatesProjectConfiguration configs, String directory, StackTraceElement stackTraceElement, CompilerUtil compilerUtil) {
        String fileName=pastClass.name+DOT_JAVA_EXTENSION;
        typeCheckCoordinator.register(pastClass, packageName);
        codeGenCoordinator.addTask(registry -> {
            TypeSpec spec;
            try {
                spec = new Poet().emit(pastClass);
            } catch (RuntimeException e) {
                System.out.println("Error emitting class for template " + pastClass.name + " in package " + packageName);
                throw e;
            }
            JavaFile myfile = compilerUtil.specWithComment(spec, configs, packageName, stackTraceElement);
            return compilerUtil.saveToFile(directory, directory + fileName, myfile);
        });
        return true;
    }

    public static boolean generateJava(org.openprovenance.prov.template.compiler.past.Class pastClass, String packageName, String templateName, String directory, StackTraceElement stackTraceElement, CompilerUtil compilerUtil) {
        String fileName=pastClass.name+DOT_JAVA_EXTENSION;
        typeCheckCoordinator.register(pastClass, packageName);
        codeGenCoordinator.addTask(registry -> {
            TypeSpec spec;
            try {
                spec = new Poet().emit(pastClass);
            } catch (RuntimeException e) {
                System.out.println("Error emitting class for template " + pastClass.name + " in package " + packageName);
                throw e;
            }
            JavaFile myfile = compilerUtil.specWithComment(spec, templateName, packageName, stackTraceElement);
            return compilerUtil.saveToFile(directory, directory + fileName, myfile);
        });
        return true;
    }

    public static boolean generatePython(Class pastClass, String packageName, Locations locations, StackTraceElement stackTraceElement) {
        String destinationDir=locations.python_dir;
        if (destinationDir == null) return false;
        codeGenCoordinator.addTask(registry -> {
            try {
                new org.openprovenance.prov.template.compiler.past.emitter.Python(registry)
                        .toWritableObject(pastClass, pastClass.name, packageName, stackTraceElement)
                        .writeTo(new File(destinationDir));
                return true;
            } catch (RuntimeException | IOException e) {
                try {
                    new ObjectMapper().writerWithDefaultPrettyPrinter().writeValue(System.out, pastClass);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                throw new RuntimeException(e);
            }
        });
        return true;
    }

    public static boolean generateJavaScript(Class pastClass, String packageName, Locations locations, StackTraceElement stackTraceElement) {
        String destinationDir=locations.javascript_dir;
        if (destinationDir == null) return false;
        codeGenCoordinator.addTask(registry -> {
            try {
                new org.openprovenance.prov.template.compiler.past.emitter.JavaScript(registry)
                        .toWritableObject(pastClass, pastClass.name, packageName, stackTraceElement)
                        .writeTo(new File(destinationDir));
                return true;
            } catch (RuntimeException | IOException e) {
                try {
                    e.printStackTrace();
                    new ObjectMapper().writerWithDefaultPrettyPrinter().writeValue(System.out, pastClass);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                throw new RuntimeException(e);
            }
        });
        return true;
    }

    public static boolean generateRust(Class pastClass, String packageName, Locations locations, StackTraceElement stackTraceElement) {
        String destinationDir=locations.rust_dir;
        if (destinationDir == null) return false;
        // create destinationDir
        new File(destinationDir).mkdirs();
        // Pass 1: Defer trait discovery to the type checking phase (same phase as typeCheckCoordinator.register)
        typeCheckCoordinator.registerPreCheckTask(() ->
                rustCodeGenerator.registerClass(pastClass, packageName, destinationDir, stackTraceElement));
        // Pass 2: Add a per-class code generation task — same pattern as generateJava/generatePython
        codeGenCoordinator.addTask(registry -> {
            try {
                return rustCodeGenerator.generateClass(pastClass, packageName, destinationDir, stackTraceElement, registry);
            } catch (IOException e) {
                throw new RuntimeException("Rust generation failed for " + pastClass.name, e);
            }
        });
        return true;
    }

}
