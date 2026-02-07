package org.openprovenance.prov.template.compiler;

import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.template.compiler.common.Constants;
import org.openprovenance.prov.template.compiler.configuration.*;
import org.openprovenance.prov.template.compiler.past.Statement;
import org.openprovenance.prov.template.compiler.past.Class;
import org.openprovenance.prov.template.compiler.past.Method;
import org.openprovenance.prov.template.compiler.past.PastFactory;
import org.openprovenance.prov.template.compiler.past.type.ClassName;

import javax.lang.model.element.Modifier;
import java.util.List;
import java.util.function.Supplier;

import static org.openprovenance.prov.template.compiler.ConfigProcessor.*;
import static org.openprovenance.prov.template.compiler.configuration.SpecificationFile.generateJava;
import static org.openprovenance.prov.template.compiler.configuration.SpecificationFile.generatePython;
import static org.openprovenance.prov.template.compiler.past.ArrayAccessor.ARRAY_ACCESSOR;
import static org.openprovenance.prov.template.compiler.past.ArrayAllocator.ARRAY_ALLOCATOR;
import static org.openprovenance.prov.template.compiler.past.Assignment.ASSIGNMENT;
import static org.openprovenance.prov.template.compiler.past.BinaryOp.BINARY_OP;
import static org.openprovenance.prov.template.compiler.past.Constant.CONSTANT;
import static org.openprovenance.prov.template.compiler.past.Field.FIELD;
import static org.openprovenance.prov.template.compiler.past.IfStatement.IF;
import static org.openprovenance.prov.template.compiler.past.Method.METHOD;
import static org.openprovenance.prov.template.compiler.past.MethodCall.METHOD_CALL;
import static org.openprovenance.prov.template.compiler.past.Return.RETURN;
import static org.openprovenance.prov.template.compiler.past.Variable.VARIABLE;
import static org.openprovenance.prov.template.compiler.past.type.ClassName.*;

public class CompilerBuilderInit {
    private final CompilerUtil compilerUtil;
    private final PastFactory pastFactory;

    public CompilerBuilderInit(ProvFactory pFactory) {
        this.compilerUtil = new CompilerUtil(pFactory);
        this.pastFactory = compilerUtil.getPastFactory();
    }

    SpecificationFile generateInitializer(TemplatesProjectConfiguration configs, Locations locations, String directory, String fileName) {
        StackTraceElement stackTraceElement = compilerUtil.thisMethodAndLine();

        String className = Constants.INIT;

        Class pastClass = pastFactory.CLASS(className)
                .MODIFIERS(Modifier.PUBLIC)
                .FIELDS(
                        FIELD(BUILDERS, STRING_ARRAY).MODIFIERS(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL),
                        FIELD(TYPEMANAGERS, STRING_ARRAY).MODIFIERS(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL),
                        FIELD(PF, PROV_FACTORY).MODIFIERS(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
        );

        Method init = METHOD("init")
                .commentFileLocation()
                .MODIFIERS(Modifier.PUBLIC, Modifier.STATIC)
                .RETURNS(ClassName.BOOLEAN)
                .BODY(
                        RETURN(METHOD_CALL(PROV_FILE_BUILDER, "registerBuilders",
                                List.of(VARIABLE(BUILDERS), VARIABLE(PF))))
                );
        pastClass.METHOD(init);

        Method main = METHOD("main")
                .commentFileLocation()
                .MODIFIERS(Modifier.PUBLIC, Modifier.STATIC)
                .PARAMETER(STRING_ARRAY, "args")
                .RETURNS(VOID)
                .THROWS(PAST_EXCEPTION)
                .BODY(
                        METHOD_CALL( "init", List.of()),
                        IF(BINARY_OP(ARRAY_ACCESSOR(VARIABLE("args"),CONSTANT(0)),"Objects.equals",CONSTANT("kernel")))
                                .THEN(
                                        METHOD_CALL(METHOD_CALL(VARIABLE("System"),"out"),
                                                "println",

                                                List.of(METHOD_CALL(LIST, "of", List.of(VARIABLE("args"))))),

                                        METHOD_CALL(PROV_PROVENANCE_KERNELS, "main", List.of(VARIABLE("args"))))
                                .ELSE(
                                        METHOD_CALL(PROV_RUNNER, "main", List.of(VARIABLE("args")))
                                )
                );

        pastClass.METHOD(main);

        int size=configs.templates.length;
        List<Statement> staticBlockStatements=
                new java.util.ArrayList<>(List.of(
                        ASSIGNMENT(null, VARIABLE(BUILDERS), ARRAY_ALLOCATOR(STRING, CONSTANT(size))),
                        ASSIGNMENT(null, VARIABLE(TYPEMANAGERS), ARRAY_ALLOCATOR(STRING, CONSTANT(size))),
                        ASSIGNMENT(null, VARIABLE(PF), METHOD_CALL(PROV_VANILLA_FACTORY, "getFactory", List.of()))
                ));

        int count=0;
        for (TemplateCompilerConfig config: configs.templates) {
            if (!(config instanceof SimpleTemplateCompilerConfig)) continue;
            staticBlockStatements.add(
                    ASSIGNMENT(null,
                            ARRAY_ACCESSOR(VARIABLE(BUILDERS), CONSTANT(count)),
                            CONSTANT(locations.getBackendPackage(config.fullyQualifiedName)+"."+compilerUtil.templateNameClass(config.name))));
            staticBlockStatements.add(
                    ASSIGNMENT(null,
                            ARRAY_ACCESSOR(VARIABLE(TYPEMANAGERS), CONSTANT(count)),
                            CONSTANT(locations.getBackendPackage(config.fullyQualifiedName)+"."+compilerUtil.templateNameClass(config.name)+"TypeManagement")));

             count++;
        }

        pastClass.STATIC_BLOCK(staticBlockStatements);


        String myPackage = configs.root_package;
        Supplier<Boolean> pythonGenerator = () -> generatePython(pastClass, myPackage, locations.python_dir, stackTraceElement);
        Supplier<Boolean> javaGenerator = () -> generateJava(pastClass, myPackage, configs, fileName + DOT_JAVA_EXTENSION, directory, stackTraceElement, compilerUtil);

        return new SpecificationFile(javaGenerator, pythonGenerator);
    }
}