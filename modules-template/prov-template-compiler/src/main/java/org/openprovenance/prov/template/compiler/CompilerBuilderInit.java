package org.openprovenance.prov.template.compiler;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import org.openprovenance.prov.model.*;
import org.openprovenance.prov.template.compiler.configuration.TemplateCompilerConfig;
import org.openprovenance.prov.template.compiler.configuration.TemplatesCompilerConfig;
import org.openprovenance.prov.template.log2prov.FileBuilder;
import org.openprovenance.prov.template.log2prov.Runner;
import org.openprovenance.prov.template.types.ProvenanceKernels;

import javax.lang.model.element.Modifier;

import java.util.List;

import static org.openprovenance.prov.template.compiler.ConfigProcessor.*;

public class CompilerBuilderInit {
    private final CompilerUtil compilerUtil=new CompilerUtil();

    private final ProvFactory pFactory;

    public CompilerBuilderInit(ProvFactory pFactory) {
        this.pFactory=pFactory;
    }


    JavaFile generateInitializer(TemplatesCompilerConfig configs) {

        int size=configs.templates.length;



        TypeSpec.Builder builder = compilerUtil.generateClassInit(INIT);

        builder.addField(String[].class,BUILDERS, Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL);
        builder.addField(String[].class,TYPEMANAGERS, Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL);


        builder.addField(ProvFactory.class,PF, Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL);

        CodeBlock.Builder block = CodeBlock.builder();
        block.addStatement("$N = new String[$L]",BUILDERS, size);
        block.addStatement("$N = new String[$L]",TYPEMANAGERS, size);
        int count=0;
        for (TemplateCompilerConfig config: configs.templates) {
            block.addStatement("$N[$L]=$S",BUILDERS,count,config.package_+"."+compilerUtil.templateNameClass(config.name));
            block.addStatement("$N[$L]=$S",TYPEMANAGERS,count,config.package_+"."+compilerUtil.templateNameClass(config.name)+"TypeManagement");
            count++;
        }
        block.addStatement("pf=$T.getFactory()", org.openprovenance.prov.vanilla.ProvFactory.class);



        builder.addStaticBlock(block.build());


        builder.addMethod(MethodSpec.methodBuilder("init")
                .addStatement("return $T.registerBuilders($N,$N)", FileBuilder.class,BUILDERS,PF)
                .returns(boolean.class)
                .addModifiers(Modifier.STATIC)
                .addModifiers(Modifier.PUBLIC)
                .build());

        builder.addMethod(generateMain());


        TypeSpec theInitializer=builder.build();



        JavaFile myfile = JavaFile.builder(configs.init_package, theInitializer)
                .addFileComment("Generated Automatically by ProvToolbox ($N) for templates config $N",getClass().getName(), configs.name)
                .build();

        return myfile;
    }

    public MethodSpec generateMain() {

        MethodSpec.Builder builder = MethodSpec.methodBuilder("main")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(void.class)
                .addParameter(String[].class, "args")
                .addException(Exception.class)
                .addStatement("init()")
                .beginControlFlow("if ($S.equals(args[0]))","kernel")
                .addStatement("System.out.println(\"arguments \" + $T.of(args))", List.class)
                .addStatement("$T.main($N)", ProvenanceKernels.class, "args")
                .nextControlFlow("else")
                .addStatement("$T.main($N)", Runner.class, "args")
                .endControlFlow();

        return builder.build();

    }

}