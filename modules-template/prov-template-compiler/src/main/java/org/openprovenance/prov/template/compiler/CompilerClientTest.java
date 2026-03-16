package org.openprovenance.prov.template.compiler;

import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.template.compiler.common.BeanDirection;
import org.openprovenance.prov.template.compiler.configuration.Locations;
import org.openprovenance.prov.template.compiler.configuration.SpecificationFile;
import org.openprovenance.prov.template.compiler.configuration.TemplateCompilerConfig;
import org.openprovenance.prov.template.compiler.configuration.TemplatesProjectConfiguration;
import org.openprovenance.prov.template.compiler.past.*;
import org.openprovenance.prov.template.compiler.past.Class;
import org.openprovenance.prov.template.compiler.past.type.ClassName;

import javax.lang.model.element.Modifier;
import java.util.List;
import java.util.function.Supplier;

import static org.openprovenance.prov.template.compiler.common.Constants.DOT_JAVA_EXTENSION;
import static org.openprovenance.prov.template.compiler.common.Constants.TESTER_FILE;
import static org.openprovenance.prov.template.compiler.configuration.SpecificationFile.generateJava;
import static org.openprovenance.prov.template.compiler.configuration.SpecificationFile.generatePython;
import static org.openprovenance.prov.template.compiler.past.BinaryOp.BINARY_OP;
import static org.openprovenance.prov.template.compiler.past.Constant.CONSTANT;
import static org.openprovenance.prov.template.compiler.past.Definition.DEFINITION;
import static org.openprovenance.prov.template.compiler.past.Method.METHOD;
import static org.openprovenance.prov.template.compiler.past.MethodCall.CONSTRUCTOR_CALL;
import static org.openprovenance.prov.template.compiler.past.MethodCall.METHOD_CALL;
import static org.openprovenance.prov.template.compiler.past.Variable.VARIABLE;
import static org.openprovenance.prov.template.compiler.past.type.ClassName.*;

public class CompilerClientTest {

    final CompilerUtil compilerUtil;
    final PastFactory pastFactory;

    static final ClassName PRINT_STREAM = ClassName.get("PrintStream", "java.io");
    static final ClassName OBJECT_MAPPER = ClassName.get("ObjectMapper", "com.fasterxml.jackson.databind");
    static final ClassName TEST_CASE = ClassName.get("TestCase", "junit.framework");

    public CompilerClientTest(ProvFactory pFactory) {
        this.compilerUtil=new CompilerUtil(pFactory);
        this.pastFactory=compilerUtil.getPastFactory();
    }


    public SpecificationFile generateTestFile_cli(TemplatesProjectConfiguration configs, Locations locations, String directory, String fileName) {
        StackTraceElement stackTraceElement = compilerUtil.thisMethodAndLine();

        Class pastClass = pastFactory.CLASS(TESTER_FILE)
                .MODIFIERS(Modifier.PUBLIC)
                .SUPERCLASS(TEST_CASE);

        Method method = METHOD("testMain")
                .commentFileLocation()
                .MODIFIERS(Modifier.PUBLIC)
                .THROWS(PAST_EXCEPTION)
                .RETURNS(VOID);

        int count=0;
        String resvar="res";

        for (TemplateCompilerConfig config: configs.templates) {
            String bn=compilerUtil.templateNameClass(config.name);
            ClassName beanClass = ClassName.get(bn, locations.getBeansPackage(config.fullyQualifiedName, BeanDirection.COMMON));

            // System.setOut(new java.io.PrintStream("target/example_" + config.name + ".json"))
            Expression fileNameExpr = BINARY_OP(
                    BINARY_OP(CONSTANT("target/example_"), "+", CONSTANT(config.name)),
                    "+",
                    CONSTANT(".json"));
            method.BODY(
                    METHOD_CALL(SYSTEM, "setOut",
                            CONSTRUCTOR_CALL(PRINT_STREAM, List.of(fileNameExpr))));

            // Object res0 = Bn.examplar()
            method.BODY(
                    DEFINITION(OBJECT, VARIABLE(resvar + count),
                            METHOD_CALL(beanClass, "examplar", List.of())));

            // new ObjectMapper().writeValue(System.out, res0)
            method.BODY(
                    METHOD_CALL(
                            CONSTRUCTOR_CALL(OBJECT_MAPPER, List.of()),
                            "writeValue",
                            List.of(METHOD_CALL(SYSTEM, "out"), VARIABLE(resvar + count))));

            count++;
        }

        pastClass.METHOD(method);

        String myPackage = configs.root_package;

        Supplier<Boolean> pythonGenerator = () -> generatePython(pastClass, myPackage, locations.python_dir, stackTraceElement);
        Supplier<Boolean> javaGenerator = () -> generateJava(pastClass, myPackage, configs, fileName, directory, stackTraceElement, compilerUtil);

        return new SpecificationFile(javaGenerator, pythonGenerator);
    }

}
