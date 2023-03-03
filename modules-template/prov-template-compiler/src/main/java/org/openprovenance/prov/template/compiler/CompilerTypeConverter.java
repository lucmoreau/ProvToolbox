package org.openprovenance.prov.template.compiler;

import com.squareup.javapoet.*;
import org.openprovenance.prov.template.compiler.common.Constants;
import org.openprovenance.prov.template.compiler.configuration.*;
import org.openprovenance.prov.template.descriptors.AttributeDescriptor;
import org.openprovenance.prov.template.descriptors.TemplateBindingsSchema;

import javax.lang.model.element.Modifier;

import java.util.HashMap;
import java.util.Map;

import static org.openprovenance.prov.template.compiler.CompilerUtil.*;
import static org.openprovenance.prov.template.compiler.ConfigProcessor.descriptorUtils;

public class CompilerTypeConverter {

    private final CompilerUtil compilerUtil=new CompilerUtil();


    public CompilerTypeConverter() {
    }


    SpecificationFile generateTypeConverter(TemplatesCompilerConfig configs, Locations locations, String directory, String fileName) {
        StackTraceElement stackTraceElement=compilerUtil.thisMethodAndLine();

        if (configs.beanProcessor==null) throw new NullPointerException("beanProcessor is null");

        ParameterizedTypeName getterOfT=ParameterizedTypeName.get(ClassName.get(configs.configurator_package + "2", Constants.TYPE_CONVERTER+"."+Constants.GETTER),typeT);



        TypeSpec.Builder builder = compilerUtil.generateClassInit(Constants.TYPE_CONVERTER);
        builder.addTypeVariable(typeT);

        //builder.addSuperinterface(ClassName.get(configs.logger_package,configs.beanProcessor));



        TypeSpec.Builder inface=compilerUtil.generateInterfaceInit(Constants.GETTER);
        inface.addTypeVariable(typeT);

        inface.addMethod(MethodSpec.methodBuilder("getString")
                .addModifiers(Modifier.ABSTRACT,Modifier.PUBLIC)
                .addParameter(String.class,"col")
                .returns(typeT)
                .build());
        inface.addMethod(MethodSpec.methodBuilder("getObject")
                .addModifiers(Modifier.ABSTRACT,Modifier.PUBLIC)
                .addParameter(String.class,"col")
                .returns(typeT)
                .build());
        inface.addMethod(MethodSpec.methodBuilder("getTimestamp")
                .addModifiers(Modifier.ABSTRACT,Modifier.PUBLIC)
                .addParameter(String.class,"col")
                .returns(typeT)
                .build());
        builder.addType(inface.build());


        builder.addField(getterOfT,"getter", Modifier.FINAL);

        MethodSpec.Builder cbuilder3= MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(getterOfT, "getter")
                .addStatement("this.getter = getter");

        builder.addMethod(cbuilder3.build());

        for (TemplateCompilerConfig config : configs.templates) {
            if (!(config instanceof SimpleTemplateCompilerConfig)) continue;
            TemplateBindingsSchema bindingsSchema=compilerUtil.getBindingsSchema((SimpleTemplateCompilerConfig) config);

            final String templateNameClass = compilerUtil.templateNameClass(config.name);
            locations.updateWithConfig(config);
            final ClassName templateClass = ClassName.get(locations.config_common_package, templateNameClass);
            MethodSpec.Builder mspec = createProcessMethod(bindingsSchema, templateClass);
            builder.addMethod(mspec.build());
        }


        TypeSpec theLogger = builder.build();

        JavaFile myfile = compilerUtil.specWithComment(theLogger, configs, locations.configurator_package2, stackTraceElement);

        return new SpecificationFile(myfile, directory, fileName, locations.configurator_package2);

    }

    private MethodSpec.Builder createProcessMethod(TemplateBindingsSchema bindingsSchema, ClassName outputClassName) {
        MethodSpec.Builder mspec = MethodSpec.methodBuilder(Constants.PROCESS_METHOD_NAME)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(ParameterSpec.builder(outputClassName,"builder").build())
                .returns(mapTypeT);

        mspec.addStatement("$T m=new $T()",mapTypeT,hashMapTypeT);

        for (String key: descriptorUtils.fieldNames(bindingsSchema)) {

                Class<?> cl=compilerUtil.getJavaTypeForDeclaredType(bindingsSchema.getVar(), key);
                String sqlType=descriptorUtils.getFromDescriptor(bindingsSchema.getVar().get(key).get(0), AttributeDescriptor::getSqlType, nd->null);



            mspec.addStatement("m.put($S, getter.$N($S))", key, convertToMethod(cl.getSimpleName(), sqlType), key);

        }

        mspec.addStatement("return m");
        return mspec;
    }

    Map<String,String> mapper=new HashMap<>() {{
        put("string", "getString");
        put("int", "getObject");
        put("integer", "getObject");
        put("timestamptz", "getTimestamp");
    }};



    private String convertToMethod(String simpleClassName, String sqlName) {
        String res=null;
        if (sqlName!=null) {
            res=mapper.get(sqlName.toLowerCase());
        }
        if (res==null) {
            res=mapper.get(simpleClassName.toLowerCase());
        }
        if (res==null) throw new IllegalStateException("Unexpected value: " + simpleClassName);
        return res;
    }


}