package org.openprovenance.prov.template.compiler;

import com.squareup.javapoet.*;
import org.openprovenance.prov.model.*;
import org.openprovenance.prov.template.compiler.common.BeanDirection;
import org.openprovenance.prov.template.compiler.common.Constants;
import org.openprovenance.prov.template.compiler.configuration.Locations;
import org.openprovenance.prov.template.compiler.configuration.SpecificationFile;
import org.openprovenance.prov.template.descriptors.AttributeDescriptor;
import org.openprovenance.prov.template.descriptors.Descriptor;
import org.openprovenance.prov.template.descriptors.NameDescriptor;
import org.openprovenance.prov.template.descriptors.TemplateBindingsSchema;

import javax.lang.model.element.Modifier;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static org.openprovenance.prov.template.compiler.CompilerUtil.typeT;
import static org.openprovenance.prov.template.compiler.ConfigProcessor.*;

public class CompilerProcessor {
    private final ProvFactory pFactory;
    private final CompilerUtil compilerUtil;

    public CompilerProcessor(ProvFactory pFactory) {
        this.pFactory=pFactory;
        this.compilerUtil=new CompilerUtil(pFactory);
    }


    public TypeSpec.Builder generateProcessorClassInit(String name) {
        return TypeSpec.interfaceBuilder(name).addTypeVariable(typeT)
                .addModifiers(Modifier.PUBLIC);
    }

    public SpecificationFile generateProcessor(Locations locations, String templateName, String packge, TemplateBindingsSchema bindingsSchema, boolean inIntegrator, String fileName, String consistsOf) {
        StackTraceElement stackTraceElement=compilerUtil.thisMethodAndLine();

        TypeSpec.Builder builder = generateProcessorClassInit(inIntegrator ? compilerUtil.integratorNameClass(templateName) : compilerUtil.processorNameClass(templateName));

        MethodSpec.Builder mbuilder = MethodSpec.methodBuilder(Constants.PROCESS_METHOD_NAME)
                .addModifiers(Modifier.PUBLIC)
                .addModifiers(Modifier.ABSTRACT)
                .returns(typeT);



        Map<String, List<Descriptor>> theVar=bindingsSchema.getVar();

        CodeBlock.Builder jdoc = CodeBlock.builder();
        String docString=bindingsSchema.getDocumentation();

        String retString = "@return not specified";
        jdoc.add(docString==null? "No @documentation": docString);
        jdoc.add("\n\n");


        for (String key: descriptorUtils.fieldNames(bindingsSchema)) {

            if (inIntegrator &&
                    (!(descriptorUtils.hasInput(key,bindingsSchema) ||
                            descriptorUtils.hasOutput(key,bindingsSchema) )) ) {
                throw new UnsupportedOperationException("In integrator, but no input or output value for " + key);
            } else if (!inIntegrator || descriptorUtils.isInput(key,bindingsSchema)) {

                mbuilder.addParameter(compilerUtil.getJavaTypeForDeclaredType(theVar, key), key);

                Descriptor descriptor = theVar.get(key).get(0);
                Function<NameDescriptor, Void> nf =
                        (nd) -> {
                            String documentation = nd.getDocumentation() == null ? Constants.JAVADOC_NO_DOCUMENTATION : nd.getDocumentation();
                            String type = nd.getType() == null ? Constants.JAVADOC_NO_DOCUMENTATION_DEFAULT_TYPE : nd.getType();
                            jdoc.add("@param $N: $L (expected type: $L)\n", key, documentation, type);
                            return null;
                        };
                Function<AttributeDescriptor, Void> af =
                        (nd) -> {
                            String documentation = nd.getDocumentation() == null ? Constants.JAVADOC_NO_DOCUMENTATION : nd.getDocumentation();
                            String type = nd.getType() == null ? Constants.JAVADOC_NO_DOCUMENTATION_DEFAULT_TYPE : nd.getType();
                            jdoc.add("@param $N: $L (expected type: $L)\n", key, documentation, type);
                            return null;
                        };
                descriptorUtils.getFromDescriptor(descriptor, af, nf);
            }

        }



        if (consistsOf!=null) {
            final TypeName listType=ParameterizedTypeName.get(ClassName.get(List.class),ClassName.get(packge, compilerUtil.beanNameClass(consistsOf, BeanDirection.COMMON)));
            mbuilder.addParameter(listType, Constants.ELEMENTS);
            jdoc.add("@param $N: to do \n", Constants.ELEMENTS);
        }

        jdoc.add(retString);

        mbuilder.addJavadoc(jdoc.build());

        builder.addMethod(mbuilder.build());

        TypeSpec bean=builder.build();

        JavaFile myfile = compilerUtil.specWithComment(bean, templateName, packge, stackTraceElement);

        return new SpecificationFile(myfile, locations.convertToDirectory(packge), fileName, packge);

    }

}
