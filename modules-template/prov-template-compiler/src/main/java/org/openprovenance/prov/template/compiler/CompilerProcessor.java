package org.openprovenance.prov.template.compiler;

import org.openprovenance.prov.model.*;
import org.openprovenance.prov.template.compiler.common.BeanDirection;
import org.openprovenance.prov.template.compiler.common.Constants;
import org.openprovenance.prov.template.compiler.configuration.Locations;
import org.openprovenance.prov.template.compiler.configuration.SpecificationFile;
import org.openprovenance.prov.template.compiler.configuration.TemplatesProjectConfiguration;
import org.openprovenance.prov.template.compiler.past.Class;
import org.openprovenance.prov.template.compiler.past.Method;
import org.openprovenance.prov.template.compiler.past.PastFactory;
import org.openprovenance.prov.template.compiler.past.type.ClassName;
import org.openprovenance.prov.template.compiler.past.type.ParameterizedType;
import org.openprovenance.prov.template.descriptors.AttributeDescriptor;
import org.openprovenance.prov.template.descriptors.Descriptor;
import org.openprovenance.prov.template.descriptors.NameDescriptor;
import org.openprovenance.prov.template.descriptors.TemplateBindingsSchema;

import javax.lang.model.element.Modifier;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.openprovenance.prov.template.compiler.CompilerConfigurations.PROCESS;
import static org.openprovenance.prov.template.compiler.CompilerConfigurations.RECORD_2_RECORD;
import static org.openprovenance.prov.template.compiler.ConfigProcessor.*;
import static org.openprovenance.prov.template.compiler.configuration.SpecificationFile.*;
import static org.openprovenance.prov.template.compiler.configuration.SpecificationFile.generateRust;
import static org.openprovenance.prov.template.compiler.past.Method.METHOD;
import static org.openprovenance.prov.template.compiler.past.type.ClassName.*;
import static org.openprovenance.prov.template.compiler.past.type.TypeVariable.T;

public class CompilerProcessor {
    private final ProvFactory pFactory;
    private final CompilerUtil compilerUtil;

    public CompilerProcessor(ProvFactory pFactory) {
        this.pFactory=pFactory;
        this.compilerUtil=new CompilerUtil(pFactory);
    }

    PastFactory pastFactory=new PastFactory();


    public SpecificationFile generateProcessor(TemplatesProjectConfiguration configs, Locations locations, String templateName, String packge, TemplateBindingsSchema bindingsSchema, boolean inIntegrator, String fileName, String consistsOf) {
        StackTraceElement stackTraceElement=compilerUtil.thisMethodAndLine();

        String interfaceName = inIntegrator ? compilerUtil.integratorNameClass(templateName) : compilerUtil.processorNameClass(templateName);


        Class pastClass = pastFactory.INTERFACE(interfaceName)
                .MODIFIERS(Modifier.PUBLIC)
                .TYPE_VARIABLES(T());

        Method mbuilder=METHOD(Constants.PROCESS_METHOD_NAME)
                .MODIFIERS(Modifier.PUBLIC, Modifier.ABSTRACT)
                .RETURNS(T());


        Map<String, List<Descriptor>> theVar=bindingsSchema.getVar();

        String docString=bindingsSchema.getDocumentation();

        mbuilder.COMMENT(docString==null? "No @documentation": docString);
        mbuilder.COMMENT("\n\n");


        for (String key: descriptorUtils.fieldNames(bindingsSchema)) {

            if (inIntegrator &&
                    (!(descriptorUtils.hasInput(key,bindingsSchema) ||
                            descriptorUtils.hasOutput(key,bindingsSchema) )) ) {
                throw new UnsupportedOperationException("In integrator, but no input or output value for " + key);
            } else if (!inIntegrator || descriptorUtils.isInput(key,bindingsSchema)) {

                mbuilder.PARAMETER(compilerUtil.getPastTypeForDeclaredType(theVar, key), key);

                Descriptor descriptor = theVar.get(key).get(0);
                Function<NameDescriptor, Void> nf =
                        (nd) -> {
                            String documentation = nd.getDocumentation() == null ? Constants.JAVADOC_NO_DOCUMENTATION : nd.getDocumentation();
                            String type = nd.getType() == null ? Constants.JAVADOC_NO_DOCUMENTATION_DEFAULT_TYPE : nd.getType();
                            mbuilder.COMMENT("@param $N: $L (expected type: $L)\n", key, documentation, type);
                            return null;
                        };
                Function<AttributeDescriptor, Void> af =
                        (nd) -> {
                            String documentation = nd.getDocumentation() == null ? Constants.JAVADOC_NO_DOCUMENTATION : nd.getDocumentation();
                            String type = nd.getType() == null ? Constants.JAVADOC_NO_DOCUMENTATION_DEFAULT_TYPE : nd.getType();
                            mbuilder.COMMENT("@param $N: $L (expected type: $L)\n", key, documentation, type);
                            return null;
                        };
                descriptorUtils.getFromDescriptor(descriptor, af, nf);
            }

        }


        if (consistsOf!=null) {
            String shortConsistsOf=locations.getShortNames().get(consistsOf);
            final ParameterizedType listType= ParameterizedType.get(LIST, ClassName.get(compilerUtil.beanNameClass(shortConsistsOf, BeanDirection.COMMON),packge));
            mbuilder.PARAMETER(listType, Constants.ELEMENTS);
            mbuilder.COMMENT("@param $N: to do \n", Constants.ELEMENTS);
        }

        mbuilder.COMMENT("@return &lt;$T&gt;\n",T());

        pastClass.METHOD(mbuilder);

        String directory=locations.convertToDirectory(packge);

        Supplier<Boolean> pythonGenerator=() -> generatePython(pastClass, packge, locations, stackTraceElement);
        Supplier<Boolean> javaGenerator = () -> generateJava(pastClass, packge, configs, directory, stackTraceElement, compilerUtil);
        Supplier<Boolean> jsGenerator = () -> generateJavaScript(pastClass, packge, locations, stackTraceElement);
        Supplier<Boolean> rustGenerator = () -> generateRust(pastClass, packge, locations, stackTraceElement);
        return new SpecificationFile(javaGenerator,pythonGenerator,jsGenerator,rustGenerator);




    }

    public SpecificationFile generateRecord2Record(TemplatesProjectConfiguration configs, Locations locations, String fileName) {
        StackTraceElement stackTraceElement=compilerUtil.thisMethodAndLine();

        Class pastClass=pastFactory.CLASS(RECORD_2_RECORD,true)
                .MODIFIERS(Modifier.PUBLIC, Modifier.STATIC)
                .METHOD(
                        METHOD(PROCESS)
                                .MODIFIERS(Modifier.PUBLIC, Modifier.ABSTRACT)
                                .PARAMETER(OBJECT_ARRAY, "args")
                                .RETURNS(OBJECT_ARRAY));




        org.openprovenance.prov.template.compiler.past.type.ClassName record2recordType=get(RECORD_2_RECORD_CONFIGURATOR+"."+RECORD_2_RECORD, locations.getFilePackage(configs.name, RECORD_2_RECORD_CONFIGURATOR));


        String packge=locations.getFilePackage(configs.name, RECORD_2_RECORD_CONFIGURATOR);

        String directory=locations.convertToDirectory(packge);

        Supplier<Boolean> pythonGenerator=() -> generatePython(pastClass, packge, locations, stackTraceElement);
        Supplier<Boolean> javaGenerator = () -> generateJava(pastClass, packge, configs, directory, stackTraceElement, compilerUtil);
        Supplier<Boolean> jsGenerator = () -> generateJavaScript(pastClass, packge, locations, stackTraceElement);
        Supplier<Boolean> rustGenerator = () -> generateRust(pastClass, packge, locations, stackTraceElement);
        return new SpecificationFile(javaGenerator,pythonGenerator,jsGenerator,rustGenerator);




    }

}
