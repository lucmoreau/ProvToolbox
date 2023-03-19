package org.openprovenance.prov.template.compiler;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import org.openprovenance.prov.model.*;
import org.openprovenance.prov.template.compiler.common.Constants;
import org.openprovenance.prov.template.compiler.configuration.*;
import org.openprovenance.prov.template.log2prov.FileBuilder;
import org.openprovenance.prov.template.log2prov.Runner;
import org.openprovenance.prov.template.types.ProvenanceKernels;

import javax.lang.model.element.Modifier;

import java.util.List;

public class CompilerBuilderInit {
    private final CompilerUtil compilerUtil=new CompilerUtil();

    private final ProvFactory pFactory;

    public CompilerBuilderInit(ProvFactory pFactory) {
        this.pFactory=pFactory;
    }


    SpecificationFile generateInitializer(TemplatesCompilerConfig configs, Locations locations, String directory, String fileName) {
        StackTraceElement stackTraceElement=compilerUtil.thisMethodAndLine();

        int size=configs.templates.length;



        TypeSpec.Builder builder = compilerUtil.generateClassInit(Constants.INIT);

        builder.addField(String[].class, Constants.BUILDERS, Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL);
        builder.addField(String[].class, Constants.TYPEMANAGERS, Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL);


        builder.addField(ProvFactory.class, Constants.PF, Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL);

        CodeBlock.Builder block = CodeBlock.builder();
        block.addStatement("$N = new String[$L]", Constants.BUILDERS, size);
        block.addStatement("$N = new String[$L]", Constants.TYPEMANAGERS, size);
        int count=0;
        for (TemplateCompilerConfig config: configs.templates) {
            locations.updateWithConfig(config);
            if (!(config instanceof SimpleTemplateCompilerConfig)) continue;
            block.addStatement("$N[$L]=$S", Constants.BUILDERS,count,locations.config_backend+"."+compilerUtil.templateNameClass(config.name));
            block.addStatement("$N[$L]=$S", Constants.TYPEMANAGERS,count,locations.config_backend+"."+compilerUtil.templateNameClass(config.name)+"TypeManagement");
            count++;
        }
        block.addStatement("pf=$T.getFactory()", org.openprovenance.prov.vanilla.ProvFactory.class);



        builder.addStaticBlock(block.build());


        builder.addMethod(MethodSpec.methodBuilder("init")
                .addStatement("return $T.registerBuilders($N,$N)", FileBuilder.class, Constants.BUILDERS, Constants.PF)
                .returns(boolean.class)
                .addModifiers(Modifier.STATIC)
                .addModifiers(Modifier.PUBLIC)
                .build());

        builder.addMethod(generateMain());

        TypeSpec theInitializer=builder.build();

        JavaFile myfile = compilerUtil.specWithComment(theInitializer, configs, configs.init_package, stackTraceElement);

        return new SpecificationFile(myfile, directory, fileName, configs.init_package);
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