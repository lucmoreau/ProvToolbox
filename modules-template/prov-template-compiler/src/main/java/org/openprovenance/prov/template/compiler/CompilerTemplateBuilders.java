package org.openprovenance.prov.template.compiler;

import com.squareup.javapoet.*;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.template.compiler.common.BeanDirection;
import org.openprovenance.prov.template.compiler.common.Constants;
import org.openprovenance.prov.template.compiler.configuration.*;

import javax.lang.model.element.Modifier;

import static org.openprovenance.prov.template.compiler.common.Constants.DOT_JAVA_EXTENSION;

public class CompilerTemplateBuilders {
    private final CompilerUtil compilerUtil;

    public CompilerTemplateBuilders(ProvFactory pFactory) {
        this.compilerUtil=new CompilerUtil(pFactory);
    }


    SpecificationFile generateTemplateBuilders(TemplatesCompilerConfig configs, Locations locations, String fileName) {
        StackTraceElement stackTraceElement=compilerUtil.thisMethodAndLine();

        if (configs.templateBuilders==null) throw new NullPointerException("templateBuilders is null");

        TypeSpec.Builder builder = compilerUtil.generateClassInit(configs.templateBuilders);

        for (TemplateCompilerConfig config : configs.templates) {
            if (!(config instanceof SimpleTemplateCompilerConfig)) continue;

            final String templateNameClass = compilerUtil.templateNameClass(config.name);
            locations.updateWithConfig(config);
            final ClassName className = ClassName.get(locations.getFilePackage(BeanDirection.COMMON), templateNameClass);
            FieldSpec fspec = FieldSpec.builder(className, config.name + "Builder")
                    .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                    .initializer(configs.logger + "." + Constants.GENERATED_VAR_PREFIX + config.name)
                    .build();

            builder.addField(fspec);
        }

        TypeSpec theLogger = builder.build();

        String myPackage=locations.getFilePackage(fileName);

        JavaFile myfile = compilerUtil.specWithComment(theLogger, configs, myPackage, stackTraceElement);

        return new SpecificationFile(myfile, locations.convertToDirectory(myPackage), fileName+DOT_JAVA_EXTENSION, myPackage);
    }


}