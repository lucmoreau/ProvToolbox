
package org.openprovenance.prov.template.compiler;

import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.template.compiler.common.BeanDirection;
import org.openprovenance.prov.template.compiler.common.Constants;
import org.openprovenance.prov.template.compiler.configuration.*;
import org.openprovenance.prov.template.compiler.past.Class;
import org.openprovenance.prov.template.compiler.past.Constructor;
import org.openprovenance.prov.template.compiler.past.Method;
import org.openprovenance.prov.template.compiler.past.PastFactory;
import org.openprovenance.prov.template.compiler.past.type.ClassName;
import org.openprovenance.prov.template.compiler.past.type.ParameterizedType;
import org.openprovenance.prov.template.descriptors.AttributeDescriptor;
import org.openprovenance.prov.template.descriptors.TemplateBindingsSchema;

import javax.lang.model.element.Modifier;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import static org.openprovenance.prov.template.compiler.ConfigProcessor.*;
import static org.openprovenance.prov.template.compiler.configuration.SpecificationFile.generateJava;
import static org.openprovenance.prov.template.compiler.configuration.SpecificationFile.generatePython;
import static org.openprovenance.prov.template.compiler.past.Assignment.ASSIGNMENT;
import static org.openprovenance.prov.template.compiler.past.Constant.CONSTANT;
import static org.openprovenance.prov.template.compiler.past.Constructor.CONSTRUCTOR;
import static org.openprovenance.prov.template.compiler.past.Definition.DEFINITION;
import static org.openprovenance.prov.template.compiler.past.Field.FIELD;
import static org.openprovenance.prov.template.compiler.past.Method.METHOD;
import static org.openprovenance.prov.template.compiler.past.MethodCall.*;
import static org.openprovenance.prov.template.compiler.past.Return.RETURN;
import static org.openprovenance.prov.template.compiler.past.Variable.VARIABLE;
import static org.openprovenance.prov.template.compiler.past.type.ClassName.*;
import static org.openprovenance.prov.template.compiler.past.type.TypeVariable.T;

public class CompilerTypeConverter {
    private final CompilerUtil compilerUtil;
    private final PastFactory pastFactory;

    public CompilerTypeConverter(ProvFactory pFactory) {
        this.compilerUtil = new CompilerUtil(pFactory);
        this.pastFactory = compilerUtil.getPastFactory();
    }

    SpecificationFile generateTypeConverter(TemplatesProjectConfiguration configs, Locations locations, String fileName) {
        StackTraceElement stackTraceElement = compilerUtil.thisMethodAndLine();

        ClassName TYPED_GETTER_TYPE = get(Constants.TYPED_GETTER, locations.getFilePackage(configs.name, Constants.TYPED_GETTER));
        ParameterizedType TYPED_GETTER_OF_T = ParameterizedType.get(TYPED_GETTER_TYPE, T());

        Class pastClass = pastFactory.CLASS(Constants.TYPE_CONVERTER)
                .MODIFIERS(Modifier.PUBLIC)
                .TYPE_VARIABLES(T())
                .FIELDS(
                        FIELD("getter", TYPED_GETTER_OF_T).MODIFIERS(Modifier.FINAL)
                );

        // constructor: public TypeConverter(Getter<T> getter) { this.getter = getter; }
        Constructor ctor = CONSTRUCTOR()
                .debugFileLocation()
                .MODIFIERS(Modifier.PUBLIC)
                .PARAMETER(TYPED_GETTER_OF_T, "getter")
                .BODY(
                        ASSIGNMENT(null, METHOD_CALL(VARIABLE("this"), "getter"), VARIABLE("getter"))
                );
        pastClass.CONSTRUCTOR(ctor);

        // create process methods for each simple template; placeholder implementation returns new HashMap<>()
        for (TemplateCompilerConfig config : configs.templates) {
            if (!(config instanceof SimpleTemplateCompilerConfig)) continue;
            TemplateBindingsSchema bindingsSchema=compilerUtil.getBindingsSchema((SimpleTemplateCompilerConfig) config);

            final String templateNameClass = compilerUtil.templateNameClass(config.name);
            final ClassName templateClass = get(templateNameClass, locations.getBeansPackage(config.fullyQualifiedName, BeanDirection.COMMON));

            Method m = createProcessMethod(bindingsSchema, templateClass);

            pastClass.METHOD(m);
        }

        String myPackage = locations.getFilePackage(configs.name, fileName);
        Supplier<Boolean> pythonGenerator = () -> generatePython(pastClass, myPackage, locations.python_dir, stackTraceElement);
        Supplier<Boolean> javaGenerator = () -> generateJava(pastClass, myPackage, configs, fileName + DOT_JAVA_EXTENSION,
                locations.convertToBackendDirectory(myPackage), stackTraceElement, compilerUtil);

        return new SpecificationFile(javaGenerator, pythonGenerator);
    }

    private Method createProcessMethod(TemplateBindingsSchema bindingsSchema, ClassName templateClass) {
        Method m = METHOD(Constants.PROCESS_METHOD_NAME)
                .MODIFIERS(Modifier.PUBLIC)
                .PARAMETER(templateClass, "builder")
                .RETURNS(MAP_STRING_T)
                .commentFileLocation();

        m.BODY(
                DEFINITION(MAP_STRING_T, VARIABLE("m"), CONSTRUCTOR_CALL(HASH_MAP_GENERICS, List.of()))
        );

        for (String key: descriptorUtils.fieldNames(bindingsSchema)) {
            ClassName cl=compilerUtil.getPastTypeForDeclaredType(bindingsSchema.getVar(), key);
            String sqlType=descriptorUtils.getFromDescriptor(bindingsSchema.getVar().get(key).get(0), AttributeDescriptor::getSqlType, nd->null);
            m.BODY(

                    METHOD_CALL(VARIABLE("m"), "put",
                            List.of(CONSTANT(key),
                                    METHOD_CALL(VARIABLE("getter"),
                                            convertToMethod(cl.simpleName, sqlType),
                                            List.of(CONSTANT(key))   )))  );
        }
        m.BODY(RETURN(VARIABLE("m")));
        return m;
    }
    Map<String,String> mapper=new HashMap<>() {{
        put("string", "getString");
        put("int", "getObject");
        put("integer", "getObject");
        put("double precision", "getObject");
        put("timestamptz", "getTimestamp");
        put("boolean", "getBoolean");
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


    public SpecificationFile generateTypedGetterInterface(TemplatesProjectConfiguration configs, Locations locations, String fileName) {
        StackTraceElement stackTraceElement=compilerUtil.thisMethodAndLine();


        Class pastClass=pastFactory.INTERFACE(TYPED_GETTER)
                .MODIFIERS(Modifier.PUBLIC)
                .TYPE_VARIABLES(T())
                .METHOD(
                        METHOD("getString")
                                .MODIFIERS(Modifier.PUBLIC, Modifier.ABSTRACT)
                                .PARAMETER(STRING, "col")
                                .RETURNS(T()))
                .METHOD(
                        METHOD("getObject")
                                .MODIFIERS(Modifier.PUBLIC, Modifier.ABSTRACT)
                                .PARAMETER(STRING, "col")
                                .RETURNS(T()))
                .METHOD(
                        METHOD("getTimestamp")
                                .MODIFIERS(Modifier.PUBLIC, Modifier.ABSTRACT)
                                .PARAMETER(STRING, "col")
                                .RETURNS(T()))
                .METHOD(
                        METHOD("getBoolean")
                                .MODIFIERS(Modifier.PUBLIC, Modifier.ABSTRACT)
                                .PARAMETER(STRING, "col")
                                .RETURNS(T()));




        String myPackage = locations.getFilePackage(configs.name, fileName);

        Supplier<Boolean> pythonGenerator = () -> generatePython(pastClass, myPackage, locations.python_dir, stackTraceElement);
        Supplier<Boolean> javaGenerator = () -> generateJava(pastClass, myPackage, configs, fileName + DOT_JAVA_EXTENSION, locations.convertToDirectory(myPackage), stackTraceElement, compilerUtil);

        return new SpecificationFile(javaGenerator, pythonGenerator);
    }


}
