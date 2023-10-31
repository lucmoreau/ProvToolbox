package org.openprovenance.prov.template.compiler.configuration;

import com.squareup.javapoet.JavaFile;
import org.openprovenance.prov.template.compiler.CompilerUtil;

import java.util.function.Consumer;
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

    public SpecificationFile(JavaFile javaFile, String directory, String fileName, String class_package) {
        this.javaFile = javaFile;
        this.directory = directory;
        this.fileName = fileName;
        this.class_package = class_package;
        this.compilerUtil=new CompilerUtil(null); // note, factory not used when saving fiiles

        this.pyDirectory=null;
        this.pyContent=null;
        this.pyFilename=null;

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
    }
    class JavaInUse {
        public java.util.function.BiFunction<String, String, Integer> f() {
            return (String x, String y) -> 5;
        }
    }
    public boolean save() {
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
}
