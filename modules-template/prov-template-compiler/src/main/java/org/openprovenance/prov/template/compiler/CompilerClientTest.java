package org.openprovenance.prov.template.compiler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import org.openprovenance.prov.template.compiler.configuration.TemplateCompilerConfig;
import org.openprovenance.prov.template.compiler.configuration.TemplatesCompilerConfig;

import javax.lang.model.element.Modifier;

import static org.openprovenance.prov.template.compiler.ConfigProcessor.TESTER_FILE;

public class CompilerClientTest {

    CompilerUtil compilerUtil=new CompilerUtil();




    public JavaFile generateTestFile_cli(TemplatesCompilerConfig configs) {

        TypeSpec.Builder builder = compilerUtil.generateClassInitExtends(TESTER_FILE,"junit.framework","TestCase");

        MethodSpec.Builder mbuilder = MethodSpec.methodBuilder("testMain")
                .addModifiers(Modifier.PUBLIC)
                .addException(Exception.class)
                .returns(void.class)
                ;

        int count=0;
        String resvar="res";

        for (TemplateCompilerConfig template: configs.templates) {
            String bn=compilerUtil.templateNameClass(template.name);
            mbuilder.addStatement("System.setOut(new java.io.PrintStream(\"target/example_" + template.name + ".json\"))");
            mbuilder.addStatement("Object $N=$T.examplar()", resvar+count, ClassName.get(template.package_+ ".client", bn));
            mbuilder.addStatement("new $T().writeValue(System.out,$N)",  ObjectMapper.class, resvar+count);
            count++;
        }

        MethodSpec method=mbuilder.build();

        builder.addMethod(method);

        TypeSpec theInitializer=builder.build();

        JavaFile myfile = JavaFile.builder(configs.init_package, theInitializer)
                .addFileComment("Generated Automatically by ProvToolbox ($N) for templates config $N",getClass().getName(), configs.name)
                .build();

        return myfile;
    }

}
