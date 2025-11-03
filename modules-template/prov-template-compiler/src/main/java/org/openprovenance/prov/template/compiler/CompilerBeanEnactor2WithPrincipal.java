package org.openprovenance.prov.template.compiler;

import com.squareup.javapoet.*;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.template.compiler.common.BeanDirection;
import org.openprovenance.prov.template.compiler.common.Constants;
import org.openprovenance.prov.template.compiler.configuration.Locations;
import org.openprovenance.prov.template.compiler.configuration.SpecificationFile;
import org.openprovenance.prov.template.compiler.configuration.TemplateCompilerConfig;
import org.openprovenance.prov.template.compiler.configuration.TemplatesProjectConfiguration;

import javax.lang.model.element.Modifier;

import static org.openprovenance.prov.template.compiler.ConfigProcessor.*;
import static org.openprovenance.prov.template.compiler.sql.CompilerSqlIntegration.BIFUN;

public class CompilerBeanEnactor2WithPrincipal {
    private final CompilerUtil compilerUtil;
    static final ParameterizedTypeName SupplierOfString= ParameterizedTypeName.get(ClassName.get(java.util.function.Supplier.class), ClassName.get(String.class));

    public CompilerBeanEnactor2WithPrincipal(ProvFactory pFactory) {
        this.compilerUtil=new CompilerUtil(pFactory);
    }


    SpecificationFile generateBeanEnactor2WithPrincipal(TemplatesProjectConfiguration configs, Locations locations, String fileName) {
        StackTraceElement stackTraceElement=compilerUtil.thisMethodAndLine();


        // Note, this is a inner interface, and the construction of its TypeName is a bit convoluted
        final TypeName ENACTOR_IMPLEMENTATION_TYPE=ParameterizedTypeName.get(ClassName.get(locations.getFilePackage(configs.name,Constants.BEAN_ENACTOR2)+"."+ Constants.BEAN_ENACTOR2, Constants.ENACTOR_IMPLEMENTATION), typeResult);
        // Note, this is a inner interface, and the construction of its TypeName is a bit convoluted
        final TypeName THIS_ENACTOR_IMPLEMENTATION_TYPE=ParameterizedTypeName.get(ClassName.get(locations.getFilePackage(configs.name,Constants.BEAN_ENACTOR2_WP)+"."+ Constants.BEAN_ENACTOR2_WP, Constants.ENACTOR_IMPLEMENTATION), typeResult);

        TypeSpec.Builder builder = compilerUtil.generateClassInit(Constants.BEAN_ENACTOR2_WP);
        // add superclass BeanEnactor2
        builder.superclass(ParameterizedTypeName.get(ClassName.get(locations.getFilePackage(configs.name,Constants.BEAN_ENACTOR2), Constants.BEAN_ENACTOR2), typeResult));
        builder.addModifiers(Modifier.ABSTRACT);
        builder.addTypeVariable(typeResult);


        ClassName queryInvokerClass = ClassName.get(locations.getFilePackage(configs.name,Constants.QUERY_INVOKER2WP), Constants.QUERY_INVOKER2WP);
        ClassName beanCompleterClass = ClassName.get(locations.getFilePackage(configs.name,Constants.BEAN_COMPLETER2), Constants.BEAN_COMPLETER2);

        ClassName ioProcessorClass = ClassName.get(locations.getFilePackage(configs.name, INPUT_OUTPUT_PROCESSOR), INPUT_OUTPUT_PROCESSOR);
        ClassName inputProcessorClass = ClassName.get(locations.getFilePackage(configs.name, INPUT_PROCESSOR), INPUT_PROCESSOR);
        builder.addSuperinterface(ioProcessorClass);




        TypeSpec.Builder inface=compilerUtil.generateInterfaceInit(Constants.ENACTOR_IMPLEMENTATION);
        inface.addSuperinterface(ENACTOR_IMPLEMENTATION_TYPE);
        inface.addTypeVariable(typeResult);

        /*
        MethodSpec.Builder method1 = MethodSpec.methodBuilder("generic_enact")
                .addModifiers(Modifier.PUBLIC,Modifier.ABSTRACT)
                .addParameter(ParameterSpec.builder(typeOut,"output").build())
                .addParameter(ParameterSpec.builder(typeIn,"bean").build())
                .addParameter(ParameterSpec.builder(consumerIn,"checker").build())
                .addParameter(ParameterSpec.builder(biconsumerTypeIn,"queryInvoker").build())
                .addParameter(ParameterSpec.builder(biconsumerTypeOut,"completeBean").build())
                .addTypeVariable(typeIn)
                .addTypeVariable(typeOut)
                .returns(typeOut);


        inface.addMethod(method1.build());

        MethodSpec.Builder method2 = MethodSpec.methodBuilder("beanCompleterFactory")
                .addModifiers(Modifier.PUBLIC,Modifier.ABSTRACT)
                .addParameter(ParameterSpec.builder(typeResult,"rs").build())
                .returns(beanCompleterClass);

        inface.addMethod(method2.build());


        MethodSpec.Builder method3 = MethodSpec.methodBuilder("beanCompleterFactory")
                .addModifiers(Modifier.PUBLIC,Modifier.ABSTRACT)
                .addParameter(ParameterSpec.builder(typeResult,"rs").build())
                .addParameter(ParameterSpec.builder(Object[].class,"extra").build())
                .returns(beanCompleterClass);

        inface.addMethod(method3.build());

         */
        MethodSpec.Builder method4 = MethodSpec.methodBuilder("beanCompleterFactory")
                .addModifiers(Modifier.PUBLIC,Modifier.ABSTRACT)
                .addParameter(ParameterSpec.builder(typeResult,"rs").build())
                .addParameter(ParameterSpec.builder(Object[].class,"extra").build())
                .addParameter(ParameterSpec.builder(BIFUN,"postProcessing").build())
                .returns(beanCompleterClass);
        inface.addMethod(method4.build());

        MethodSpec.Builder method5 = MethodSpec.methodBuilder("beanCompleterFactory")
                .addModifiers(Modifier.PUBLIC,Modifier.ABSTRACT)
                .addParameter(ParameterSpec.builder(typeResult,"rs").build())
                .addParameter(ParameterSpec.builder(BIFUN,"postProcessing").build())

                .returns(beanCompleterClass);
        inface.addMethod(method5.build());



        builder.addType(inface.build());



        builder.addField(inputProcessorClass,"checker",Modifier.FINAL, Modifier.PRIVATE);
        builder.addField(BIFUN,"postProcessing",Modifier.FINAL, Modifier.PRIVATE);

        /*
        // add Field   static private final ThreadLocal<String> principal= ThreadLocal.withInitial(() -> "unknown");
        final TypeName THREAD_LOCAL_STRING=ParameterizedTypeName.get(ClassName.get(ThreadLocal.class), ClassName.get(String.class));
        FieldSpec.Builder principalField=FieldSpec.builder(THREAD_LOCAL_STRING, principalVar, Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL).initializer("$T.withInitial(() ->  $S)", ThreadLocal.class, CompilerCommon.UNKNOWN);
        builder.addField(principalField.build());

        // add static method setPrincipal taking a principal and setting it in the ThreadLocal
        MethodSpec.Builder setPrincipal= MethodSpec.methodBuilder("setPrincipal")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addParameter(String.class, "p")
                .addStatement("$N.set(p)", principalVar);
        builder.addMethod(setPrincipal.build());

        // add static method getPrincipal returning a  ThreadLocal
        MethodSpec.Builder getPrincipal= MethodSpec.methodBuilder("getPrincipal")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(String.class)
                .addStatement("return $N.get()", principalVar);
        builder.addMethod(getPrincipal.build());

         */

        builder.addField(THIS_ENACTOR_IMPLEMENTATION_TYPE, Constants.REALISER, Modifier.FINAL, Modifier.PRIVATE);
        builder.addField(SupplierOfString, PRINCIPAL_MANAGER_VAR, Modifier.FINAL, Modifier.PRIVATE);


        MethodSpec.Builder cbuilder3= MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(THIS_ENACTOR_IMPLEMENTATION_TYPE, Constants.REALISER)
                .addParameter(inputProcessorClass, "checker")
                .addParameter(BIFUN, "postProcessing")
                .addParameter(SupplierOfString, PRINCIPAL_MANAGER_VAR);
        compilerUtil.specWithComment(cbuilder3);

        cbuilder3
                .addStatement("super($N,$N)", Constants.REALISER, "checker")
                .addStatement("this.$N = $N", Constants.REALISER, Constants.REALISER)
                .addStatement("this.$N = $N", "checker", "checker")
                .addStatement("this.$N = $N", "postProcessing", "postProcessing")
                .addStatement("this.$N = $N", PRINCIPAL_MANAGER_VAR, PRINCIPAL_MANAGER_VAR);

        builder.addMethod(cbuilder3.build());



        for (TemplateCompilerConfig config : configs.templates) {
            /*
                    if (config.package_==null) config.package_=configs.root_package;
                    this.config_common_package     = config.package_+ "." + Constants.SUB_PACKAGE_CLIENT + "." + Constants.SUB_PACKAGE_COMMON;
                    this.config_integrator_package = config.package_+ "." + Constants.SUB_PACKAGE_CLIENT + "." + Constants.SUB_PACKAGE_INTEGRATOR;
                    this.config_access_control_package = config.package_+ "." + Constants.SUB_PACKAGE_CLIENT + "." + Constants.SUB_PACKAGE_ACCESS_CONTROL;
                    this.config_backend            = config.package_;
                    this.config_sql_common_backend_package = config_backend + ".sql.common";
                    this.config_sql_integration_backend_package = config_backend + ".sql.integration";
                    this.config_sql_access_control_backend_package = config_backend + ".sql.access_control";



             */

            final String outputNameClass = compilerUtil.outputsNameClass(config.name);
            final String inputNameClass = compilerUtil.inputsNameClass(config.name);
            final ClassName outputClassName = ClassName.get(locations.getBeansPackage(config.name, BeanDirection.OUTPUTS), outputNameClass);
            final ClassName inputClassName = ClassName.get(locations.getBeansPackage(config.name, BeanDirection.INPUTS), inputNameClass);

            MethodSpec.Builder mspec = MethodSpec.methodBuilder(Constants.PROCESS_METHOD_NAME)
                    .addModifiers(Modifier.PUBLIC)
                    .addParameter(ParameterSpec.builder(inputClassName,"bean").build())
                    .returns(outputClassName);

            compilerUtil.specWithComment(mspec);

                mspec.addStatement("return $N.generic_enact(new $T(),bean,\n" +
                        "                b -> checker.process(b),\n" +
                        "                (sb,b) -> new $T(sb,$N.get()).process(b),\n" +
                        "                (rs,b) -> $N.beanCompleterFactory(rs,postProcessing).process(b))", Constants.REALISER, outputClassName, queryInvokerClass, PRINCIPAL_MANAGER_VAR, Constants.REALISER);

            builder.addMethod(mspec.build());
        }


        TypeSpec theLogger = builder.build();

        String myPackage= locations.getFilePackage(configs.name,fileName);

        JavaFile myfile = compilerUtil.specWithComment(theLogger, configs, myPackage, stackTraceElement);

        return new SpecificationFile(myfile, locations.convertToBackendDirectory(myPackage), fileName+ DOT_JAVA_EXTENSION, myPackage);

    }



}