package org.openprovenance.prov.template.compiler.expansion;

import org.openprovenance.prov.model.*;
import org.openprovenance.prov.template.compiler.CompilerUtil;
import org.openprovenance.prov.template.compiler.common.CompilerCommon;
import org.openprovenance.prov.template.compiler.configuration.Locations;
import org.openprovenance.prov.template.compiler.configuration.SpecificationFile;
import org.openprovenance.prov.template.compiler.configuration.TemplatesProjectConfiguration;
import org.openprovenance.prov.template.compiler.past.*;
import org.openprovenance.prov.template.compiler.past.Class;
import org.openprovenance.prov.template.compiler.past.type.ClassName;
import org.openprovenance.prov.template.compiler.past.type.ParameterizedType;
import org.openprovenance.prov.template.descriptors.Descriptor;
import org.openprovenance.prov.template.descriptors.TemplateBindingsSchema;

import javax.lang.model.element.Modifier;
import java.util.*;
import java.util.function.Supplier;

import static org.openprovenance.prov.template.compiler.CompilerUtil.u;
import static org.openprovenance.prov.template.compiler.ConfigProcessor.descriptorUtils;
import static org.openprovenance.prov.template.compiler.common.Constants.DOT_JAVA_EXTENSION;
import static org.openprovenance.prov.template.compiler.configuration.SpecificationFile.generateJava;
import static org.openprovenance.prov.template.compiler.configuration.SpecificationFile.generatePython;
import static org.openprovenance.prov.template.compiler.past.ArrayInitialiser.ARRAY_INITIALISER;
import static org.openprovenance.prov.template.compiler.past.Constant.CONSTANT;
import static org.openprovenance.prov.template.compiler.past.Method.METHOD;
import static org.openprovenance.prov.template.compiler.past.Return.RETURN;
import static org.openprovenance.prov.template.compiler.past.Variable.VARIABLE;
import static org.openprovenance.prov.template.compiler.past.type.ClassName.OBJECT;
import static org.openprovenance.prov.template.compiler.past.type.ClassName.OBJECT_ARRAY;

public class CompilerTypedRecord {
    private final CompilerUtil compilerUtil;
    private final ProvFactory pFactory;
    private final boolean withMain;
    private final CompilerCommon compilerCommon;
    private final boolean debugComment;
    private final PastFactory pastFactory;


    public CompilerTypedRecord(boolean withMain, CompilerCommon compilerCommon, ProvFactory pFactory, boolean debugComment) {
        this.pFactory=pFactory;
        this.withMain=withMain;
        this.compilerCommon = compilerCommon;
        this.debugComment=debugComment;
        this.compilerUtil=new CompilerUtil(pFactory);
        this.pastFactory=compilerUtil.getPastFactory();
    }

    public SpecificationFile generatedTypedRecordConstructor(TemplatesProjectConfiguration configs, Locations locations, Document doc, String name, String templateName, String templateFullyQualifiedName, String packge, String resource, TemplateBindingsSchema bindingsSchema, String directory, String fileName) {


        Bundle bun = u.getBundle(doc).get(0);

        Set<QualifiedName> allVars = new HashSet<>();
        Set<QualifiedName> allAtts = new HashSet<>();

        compilerUtil.extractVariablesAndAttributes(bun, allVars, allAtts, pFactory);

        return generateTypeDeclaration_aux(configs, locations, doc, name, templateName, templateFullyQualifiedName, packge, resource, bindingsSchema, directory, fileName);

    }



    public SpecificationFile generateTypeDeclaration_aux(TemplatesProjectConfiguration configs, Locations locations, Document doc, String name, String templateName, String templateFullyQualifiedName, String packge, String resource, TemplateBindingsSchema bindingsSchema, String directory, String fileName) {
        StackTraceElement stackTraceElement=compilerUtil.thisMethodAndLine();

        Method method = METHOD("call")
                .commentFileLocation()
                .MODIFIERS(Modifier.PUBLIC)
                .RETURNS(OBJECT_ARRAY);

        Map<String, List<Descriptor>> theVar=bindingsSchema.getVar();
        Collection<String> variables=descriptorUtils.fieldNames(bindingsSchema);

        compilerUtil.generateDocumentSpecializedParameters(method, theVar, variables);

        // Build the array initialiser: new Object[] { templateFullyQualifiedName, arg1, arg2, ... }
        List<Expression> arrayElements = new ArrayList<>();
        arrayElements.add(CONSTANT(templateFullyQualifiedName));
        for (String key : theVar.keySet()) {
            if (theVar.get(key) == null) continue;
            arrayElements.add(VARIABLE(key));
        }

        method.BODY(RETURN(ARRAY_INITIALISER(OBJECT, arrayElements)));

        final ParameterizedType superinterface = ParameterizedType.get(ClassName.get(name + "Interface", packge), OBJECT_ARRAY);

        Class pastClass = pastFactory.CLASS(name + "TypedRecord")
                .MODIFIERS(Modifier.PUBLIC)
                .INTERFACES(superinterface)
                .METHOD(method);

        String myPackage = packge;

        Supplier<Boolean> pythonGenerator = () -> generatePython(pastClass, myPackage, locations.python_dir, stackTraceElement);
        Supplier<Boolean> javaGenerator = () -> generateJava(pastClass, myPackage, templateName, fileName, directory, stackTraceElement, compilerUtil);

        return new SpecificationFile(javaGenerator, pythonGenerator);
    }


}
