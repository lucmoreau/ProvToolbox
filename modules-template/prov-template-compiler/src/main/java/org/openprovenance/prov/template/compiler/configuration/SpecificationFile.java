package org.openprovenance.prov.template.compiler.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;
import org.openprovenance.prov.template.compiler.CompilerUtil;
import org.openprovenance.prov.template.compiler.past.emitter.Poet;

import java.io.File;
import java.io.IOException;
import java.util.function.Supplier;

public class SpecificationFile {
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
    private final SpecificationFile javaSpec;

    public SpecificationFile(JavaFile javaFile, String directory, String fileName, String class_package) {
        this.javaFile = javaFile;
        this.directory = directory;
        this.fileName = fileName;
        this.class_package = class_package;
        this.compilerUtil=new CompilerUtil(null); // note, factory not used when saving fiiles

        this.pyDirectory=null;
        this.pyContent=null;
        this.pyFilename=null;
        this.javaGenerator=null;
        this.pythonGenerator=null;
        this.javaSpec=null;

    }

    public SpecificationFile(JavaFile javaFile, String directory, String fileName, String class_package, String pyDirectory, String pyFilename, Supplier<String> pyContent) {
        this.javaFile = javaFile;
        this.directory = directory;
        this.fileName = fileName;
        this.class_package = class_package;
        this.compilerUtil=new CompilerUtil(null); // note, factory not used when saving fiiles

        this.pyDirectory=pyDirectory;
        this.pyFilename=pyFilename;
        this.pyContent=pyContent;
        this.javaGenerator=null;
        this.pythonGenerator=null;
        this.javaSpec=null;
    }

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
        this.javaSpec=null;
    }

    public SpecificationFile(SpecificationFile javaSpec, Supplier<Boolean> pythonGenerator) {
        this.compilerUtil=null;
        this.javaFile=null;
        this.directory=null;
        this.fileName=null;
        this.class_package=null;
        this.pyDirectory=null;
        this.pyFilename=null;
        this.pyContent=null;
        this.javaGenerator=null;
        this.pythonGenerator=pythonGenerator;
        this.javaSpec=javaSpec;
    }

    class JavaInUse {
        public java.util.function.BiFunction<String, String, Integer> f() {
            return (String x, String y) -> 5;
        }
    }
    public boolean save() {
        if (javaGenerator!=null && pythonGenerator!=null) {
            boolean javaGen=javaGenerator.get();
            boolean pyGen=pythonGenerator.get();
            return javaGen && pyGen;
        }
        if (javaSpec!=null && pythonGenerator!=null) {
            boolean javaSaved=javaSpec.save();
            boolean pyGen=pythonGenerator.get();
            return javaSaved && pyGen;
        }

        // old method

        boolean pySaved=true;
        if (pyDirectory!=null && pyFilename!=null && pyContent!=null)
            pySaved=compilerUtil.saveToFile(pyDirectory, pyDirectory+pyFilename, pyContent);

        boolean javaSaved=compilerUtil.saveToFile(directory, directory + fileName, javaFile);

        return javaSaved && pySaved;
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

    public static boolean generateJava(org.openprovenance.prov.template.compiler.past.Class pastClass, String packageName, TemplatesProjectConfiguration configs, String fileName, String directory, StackTraceElement stackTraceElement, CompilerUtil compilerUtil) {
        TypeSpec spec;
        try {
            spec = new Poet().emit(pastClass);
        } catch (RuntimeException e) {
            System.out.println("Error emitting class for template " + pastClass.name + " in package " + packageName);
            throw e;
        }
        JavaFile myfile = compilerUtil.specWithComment(spec, configs, packageName, stackTraceElement);
        boolean saved=compilerUtil.saveToFile(directory, directory + fileName, myfile);
        return saved;
    }

    public static boolean generatePython(org.openprovenance.prov.template.compiler.past.Class pastClass, String packageName, String destinationDir, StackTraceElement stackTraceElement) {
        try {
            if (destinationDir==null) return false;
            new org.openprovenance.prov.template.compiler.past.emitter.Python()
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
    }
}
