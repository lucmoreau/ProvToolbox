package org.openprovenance.prov.template.compiler.oldstuff;

import com.squareup.javapoet.*;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.model.QualifiedName;
import org.openprovenance.prov.template.compiler.CompilerUtil;
import org.openprovenance.prov.template.compiler.common.BeanDirection;
import org.openprovenance.prov.template.compiler.configuration.*;
import org.openprovenance.prov.template.log2prov.FileBuilder;

import javax.lang.model.element.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.openprovenance.prov.template.compiler.CompilerUtil.mapString2StringType;
import static org.openprovenance.prov.template.compiler.common.Constants.*;

public class CompilerTableConfiguratorForTypesOld {
    private final CompilerUtil compilerUtil;
    static final TypeName setStringT= ParameterizedTypeName.get(ClassName.get(Set.class), ClassName.get(String.class));
    static final TypeName mapQualifiedName2StringSetType=ParameterizedTypeName.get(ClassName.get(Map.class), ClassName.get(QualifiedName.class),setStringT);
    static final TypeName mapString2StringSetType=ParameterizedTypeName.get(ClassName.get(Map.class), ClassName.get(String.class),setStringT);
    public static final ParameterizedTypeName fileBuilderMapType=ParameterizedTypeName.get(ClassName.get(Map.class),ClassName.get(String.class),ClassName.get(FileBuilder.class));
    static final TypeName stringArrayType=ArrayTypeName.get(String[].class);
    static final TypeName mapString2StringArrayType=ParameterizedTypeName.get(ClassName.get(Map.class),ClassName.get(String.class),stringArrayType);

    public CompilerTableConfiguratorForTypesOld(ProvFactory pFactory) {
        this.compilerUtil=new CompilerUtil(pFactory);
    }

    SpecificationFile generateTableConfigurator(TemplatesProjectConfiguration configs, Locations locations, String l2p_src_dir) {
        return generateTableConfigurator(configs,false, locations, l2p_src_dir);
    }
    SpecificationFile generateCompositeTableConfigurator(TemplatesProjectConfiguration configs, Locations locations, String l2p_src_dir) {
        return generateTableConfigurator(configs,true, locations, l2p_src_dir);
    }

    SpecificationFile generateTableConfigurator(TemplatesProjectConfiguration configs, boolean compositeOnly, Locations locations, String l2p_src_dir) {
        StackTraceElement stackTraceElement=compilerUtil.thisMethodAndLine();

        String originalTableClassName=(compositeOnly)? COMPOSITE_TABLE_CONFIGURATOR:TABLE_CONFIGURATOR;
        String tableClassName=originalTableClassName+"ForTypes"+WITH_MAP;


        TypeSpec.Builder builder =  compilerUtil.generateClassInit(tableClassName);
        builder.addSuperinterface(ParameterizedTypeName.get(ClassName.get(locations.getFilePackage(configs.name, originalTableClassName), originalTableClassName), mapString2StringSetType));

        builder.addField(mapString2StringType, "map", Modifier.PRIVATE, Modifier.FINAL);
        builder.addField(mapString2StringArrayType, PROPERTY_ORDER, Modifier.PRIVATE, Modifier.FINAL);
        builder.addField(fileBuilderMapType, DOCUMENT_BUILDER_DISPATCHER, Modifier.PRIVATE, Modifier.FINAL);

        MethodSpec.Builder constructor = MethodSpec.constructorBuilder()
                .addParameter(mapString2StringType, "map")
                .addParameter(mapString2StringArrayType, PROPERTY_ORDER)
                .addParameter(fileBuilderMapType, DOCUMENT_BUILDER_DISPATCHER)
                .addParameter(ProvFactory.class, "pf")
                .addStatement("this.map=map")
                .addStatement("this.$N=$N",PROPERTY_ORDER, PROPERTY_ORDER)
                .addStatement("this.$N=$N",DOCUMENT_BUILDER_DISPATCHER, DOCUMENT_BUILDER_DISPATCHER)
                .addModifiers(Modifier.PUBLIC);



        for (TemplateCompilerConfig config : configs.templates) {
            final String templateNameClass = compilerUtil.templateNameClass(config.name);
            final ClassName className = ClassName.get(locations.getBeansPackage(config.fullyQualifiedName, BeanDirection.COMMON), templateNameClass);

            MethodSpec.Builder mspec_builder = MethodSpec.methodBuilder(config.name)
                    .addModifiers(Modifier.PUBLIC)
                    .addParameter(ParameterSpec.builder(className, "builder").build())
                    .returns(mapString2StringSetType);


            if (config instanceof SimpleTemplateCompilerConfig) {
                SimpleTemplateCompilerConfig simpleConfig=(SimpleTemplateCompilerConfig) config;
                /*
                final ClassName builderClassName = ClassName.get(simpleConfig.package_,templateNameClass);
                String builderVar = PREFIX + config.name;
                builder.addField(builderClassName, builderVar, Modifier.PRIVATE, Modifier.FINAL);


                constructor.addStatement("$N=new $T(pf)", builderVar, builderClassName);
                mspec_builder.addStatement("if (map!=null) $N.setVariableMap(map)", builderVar);
                mspec_builder.addStatement("return $N", builderVar);

                 */
                ClassName builderClass=ClassName.get(locations.getBackendPackage(config.fullyQualifiedName),compilerUtil.templateNameClass(config.name));

                mspec_builder.addStatement("$T $N=$N.get($S)", stringArrayType, "properties", PROPERTY_ORDER, config.fullyQualifiedName);

                mspec_builder.addStatement("$T $N = ($T) $N.get($S)", builderClass, TEMPLATE_BUILDER_VARIABLE, builderClass, DOCUMENT_BUILDER_DISPATCHER, config.fullyQualifiedName);

                mspec_builder.addStatement("$T $N=$N.make($N.getTypedRecord())", Object[].class, "record2", TEMPLATE_BUILDER_VARIABLE, TEMPLATE_BUILDER_VARIABLE);

                mspec_builder.addStatement("$T knownTypeMap=new $T<>()", mapQualifiedName2StringSetType, HashMap.class);
                //        plead_trainingBuilder.make(plead_trainingBuilder.getTypeManager(knownTypeMap, new HashMap<>(), new HashMap<>(), new HashMap<>(), new HashMap<>()));
                mspec_builder.addStatement("$N.make($N.getTypeManager(knownTypeMap, new $T<>(), new $T<>(), new $T<>(), new $T<>()))", TEMPLATE_BUILDER_VARIABLE, TEMPLATE_BUILDER_VARIABLE, HashMap.class, HashMap.class, HashMap.class, HashMap.class);

                mspec_builder.addStatement("$T $N=new $T<>()", mapString2StringSetType, PROPERTY_MAP, HashMap.class);
                mspec_builder.beginControlFlow("for (int i=0; i<$N.length; i++)", "record2");
                mspec_builder.addStatement("$T property=$N[i]", String.class, "properties");
                mspec_builder.addStatement("$T value=$N[i]", Object.class, "record2");
                mspec_builder.beginControlFlow("if (value instanceof $T)", QualifiedName.class);
                mspec_builder.addStatement("$N.put(property, knownTypeMap.get((QualifiedName) value))", PROPERTY_MAP);
                mspec_builder.endControlFlow();
                mspec_builder.endControlFlow();

                mspec_builder.addStatement("return $N", PROPERTY_MAP);


            } else {
                mspec_builder.addStatement("return null");

            }


            MethodSpec mspec=mspec_builder.build();



            builder.addMethod(mspec);

        }

        builder.addMethod(constructor.build());

        TypeSpec theLogger = builder.build();

        String myPackage = locations.getConfiguratorBackendPackage(configs.name);
        String directory=locations.convertToDirectory(l2p_src_dir,"");

        JavaFile myfile = compilerUtil.specWithComment(theLogger, configs, myPackage, stackTraceElement);

        return new SpecificationFile(myfile, directory, tableClassName+DOT_JAVA_EXTENSION, myPackage);
    }







  }