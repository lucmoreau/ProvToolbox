package org.openprovenance.prov.template.compiler;

import com.squareup.javapoet.*;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.template.compiler.common.BeanDirection;
import org.openprovenance.prov.template.compiler.common.Constants;
import org.openprovenance.prov.template.compiler.configuration.*;
import org.openprovenance.prov.template.log2prov.FileBuilder;

import javax.lang.model.element.Modifier;

import static org.openprovenance.prov.template.compiler.CompilerUtil.mapString2StringType;
import static org.openprovenance.prov.template.compiler.common.Constants.DOT_JAVA_EXTENSION;
import static org.openprovenance.prov.template.compiler.common.Constants.WITH_MAP;

public class CompilerTableConfiguratorWithMap {
    public static final String PREFIX = "_b_";
    private final CompilerUtil compilerUtil;

    public CompilerTableConfiguratorWithMap(ProvFactory pFactory) {
        this.compilerUtil=new CompilerUtil(pFactory);
    }

    SpecificationFile generateTableConfigurator(TemplatesCompilerConfig configs, Locations locations, String l2p_src_dir) {
        return generateTableConfigurator(configs,false, locations, l2p_src_dir);
    }
    SpecificationFile generateCompositeTableConfigurator(TemplatesCompilerConfig configs, Locations locations, String l2p_src_dir) {
        return generateTableConfigurator(configs,true, locations, l2p_src_dir);
    }

    SpecificationFile generateTableConfigurator(TemplatesCompilerConfig configs, boolean compositeOnly, Locations locations, String l2p_src_dir) {
        StackTraceElement stackTraceElement=compilerUtil.thisMethodAndLine();

        if (configs.tableConfigurator==null) throw new NullPointerException("tableConfigurator is null");
        String originalTableClassName=(compositeOnly)? Constants.COMPOSITE +configs.tableConfigurator:configs.tableConfigurator;
        String tableClassName=originalTableClassName+WITH_MAP;

        String directory=locations.convertToDirectory(l2p_src_dir,"configurator");

        TypeSpec.Builder builder =  compilerUtil.generateClassInit(tableClassName);
        builder.addSuperinterface(ParameterizedTypeName.get(ClassName.get(locations.getFilePackage(originalTableClassName), originalTableClassName), ClassName.get(FileBuilder.class)));

        builder.addField(mapString2StringType, "map", Modifier.PRIVATE, Modifier.FINAL);

        MethodSpec.Builder constructor = MethodSpec.constructorBuilder()
                .addParameter(mapString2StringType, "map")
                .addParameter(ProvFactory.class, "pf")
                .addStatement("this.map=map")
                .addModifiers(Modifier.PUBLIC);


        final ClassName fileBuilderClassName = ClassName.get(FileBuilder.class);

        for (TemplateCompilerConfig config : configs.templates) {
            final String templateNameClass = compilerUtil.templateNameClass(config.name);
            locations.updateWithConfig(config);
            final ClassName className = ClassName.get(locations.getFilePackage(BeanDirection.COMMON), templateNameClass);

            MethodSpec.Builder mspec_builder = MethodSpec.methodBuilder(config.name)
                    .addModifiers(Modifier.PUBLIC)
                    .addParameter(ParameterSpec.builder(className, "builder").build())
                    .returns(fileBuilderClassName);


            if (config instanceof SimpleTemplateCompilerConfig) {
                SimpleTemplateCompilerConfig simpleConfig=(SimpleTemplateCompilerConfig) config;
                final ClassName builderClassName = ClassName.get(simpleConfig.package_,templateNameClass);
                String builderVar = PREFIX + config.name;
                builder.addField(builderClassName, builderVar, Modifier.PRIVATE, Modifier.FINAL);


                constructor.addStatement("$N=new $T(pf)", builderVar, builderClassName);
                mspec_builder.addStatement("if (map!=null) $N.setVariableMap(map)", builderVar);
                mspec_builder.addStatement("return $N", builderVar);
            } else {
                mspec_builder.addStatement("return null");

            }


            MethodSpec mspec=mspec_builder.build();



            builder.addMethod(mspec);

        }

        builder.addMethod(constructor.build());

        TypeSpec theLogger = builder.build();

        String myPackage = configs.root_package + ".configurator";

        JavaFile myfile = compilerUtil.specWithComment(theLogger, configs, myPackage, stackTraceElement);

        return new SpecificationFile(myfile, directory, tableClassName+DOT_JAVA_EXTENSION, myPackage);
    }







  }