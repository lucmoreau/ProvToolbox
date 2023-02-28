package org.openprovenance.prov.template.compiler.configuration;

import com.squareup.javapoet.JavaFile;
import org.openprovenance.prov.template.compiler.CompilerUtil;

public class SpecificationFile {
    final private CompilerUtil compilerUtil=new CompilerUtil();

    final private JavaFile javaFile;
    final private String directory;
    final private String fileName;
    final private String class_package;

    public SpecificationFile(JavaFile javaFile, String directory, String fileName, String class_package) {
        this.javaFile = javaFile;
        this.directory = directory;
        this.fileName = fileName;
        this.class_package = class_package;
    }

    public boolean save() {
        return compilerUtil.saveToFile(directory, directory + fileName, javaFile);
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
