package org.openprovenance.prov.template.compiler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.template.compiler.common.BeanDirection;
import org.openprovenance.prov.template.compiler.configuration.Locations;
import org.openprovenance.prov.template.compiler.configuration.SpecificationFile;
import org.openprovenance.prov.template.compiler.configuration.TemplateCompilerConfig;
import org.openprovenance.prov.template.compiler.configuration.TemplatesCompilerConfig;

import javax.lang.model.element.Modifier;

import static org.openprovenance.prov.template.compiler.common.Constants.TESTER_FILE;

public class CompilerClientTest {

    final CompilerUtil compilerUtil;

    public CompilerClientTest(ProvFactory pFactory) {
        this.compilerUtil=new CompilerUtil(pFactory);
    }


    public SpecificationFile generateTestFile_cli(TemplatesCompilerConfig configs, Locations locations, String directory, String fileName) {

        TypeSpec.Builder builder = compilerUtil.generateClassInitExtends(TESTER_FILE,"junit.framework","TestCase");

        MethodSpec.Builder mbuilder = MethodSpec.methodBuilder("testMain")
                .addModifiers(Modifier.PUBLIC)
                .addException(Exception.class)
                .returns(void.class)
                ;

        int count=0;
        String resvar="res";

        for (TemplateCompilerConfig config: configs.templates) {
            locations.updateWithConfig(config);

            String bn=compilerUtil.templateNameClass(config.name);
            mbuilder.addStatement("System.setOut(new java.io.PrintStream(\"target/example_" + config.name + ".json\"))");
            mbuilder.addStatement("Object $N=$T.examplar()", resvar+count, ClassName.get(locations.getFilePackage(BeanDirection.COMMON), bn));
            mbuilder.addStatement("new $T().writeValue(System.out,$N)",  ObjectMapper.class, resvar+count);
            count++;
        }

        MethodSpec method=mbuilder.build();

        builder.addMethod(method);

        TypeSpec theInitializer=builder.build();

        JavaFile myfile = JavaFile.builder(configs.root_package, theInitializer)
                .addFileComment("Generated Automatically by ProvToolbox ($N) for templates config $N",getClass().getName(), configs.name)
                .build();
        return new SpecificationFile(myfile, directory, fileName, configs.root_package);

    }

}
