package org.openprovenance.prov.template.compiler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.MissingNode;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import org.apache.commons.text.StringSubstitutor;
import org.openprovenance.prov.model.*;
import org.openprovenance.prov.template.expander.ExpandAction;
import org.openprovenance.prov.template.expander.ExpandUtil;
import org.openprovenance.prov.template.log2prov.FileBuilder;
import org.openprovenance.prov.template.log2prov.Runner;

import javax.lang.model.element.Modifier;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;

import static org.openprovenance.prov.template.compiler.CompilerUtil.u;
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
        builder.addField(ProvFactory.class,PF, Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL);

        CodeBlock.Builder block = CodeBlock.builder();
        block.addStatement("$N = new String[$L]",BUILDERS, size);
        int count=0;
        for (TemplateCompilerConfig config: configs.templates) {
            block.addStatement("$N[$L]=$S",BUILDERS,count,config.package_+"."+compilerUtil.templateNameClass(config.name));
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
                .addStatement("$T.main($N)", Runner.class, "args");

        return builder.build();

    }

}