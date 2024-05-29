package org.openprovenance.prov.template.compiler.configuration;

import org.openprovenance.prov.template.compiler.common.BeanDirection;
import org.openprovenance.prov.template.compiler.common.Constants;

import java.io.File;

import static org.openprovenance.prov.template.compiler.common.Constants.*;

public class Locations {
    final private String cli_src_dir;
    final private TemplatesCompilerConfig configs;
    final private String configurator_package;
    final private String logger_package;
    final private String configurator_package2;
    public final String python_dir;
    private String config_common_package;
    private String config_backend;
    private String config_integrator_package;






    static final String CONFIG2_EXTENSION = "2";

    public void updateWithConfig(TemplateCompilerConfig config) {
        if (config.package_==null) config.package_=configs.root_package;
        this.config_common_package     = config.package_+ "." + Constants.SUB_PACKAGE_CLIENT + "." + Constants.SUB_PACKAGE_COMMON;
        this.config_integrator_package = config.package_+ "." + Constants.SUB_PACKAGE_CLIENT + "." + Constants.SUB_PACKAGE_INTEGRATOR;
        this.config_backend            = config.package_;
    }

    public Locations(TemplatesCompilerConfig configs, String cli_src_dir) {
        this.configs=configs;
        this.cli_src_dir=cli_src_dir;
        this.python_dir=configs.python_dir;

        this.configurator_package  = configs.root_package + "." + Constants.SUB_PACKAGE_CLIENT + "." + CONFIGURATOR;
        this.configurator_package2 = configs.root_package + "." + Constants.SUB_PACKAGE_CLIENT + "." + CONFIGURATOR + CONFIG2_EXTENSION;
        this.logger_package        = configs.root_package + "." + Constants.SUB_PACKAGE_CLIENT + "." + SUB_PACKAGE_LOGGER;

    }

    public String convertToDirectory(String rootDir, String aPackage) {
        String theDirectory = rootDir + "/" + aPackage.replace('.', '/') + "/";
        new File(theDirectory).mkdirs();
        return theDirectory;
    }
    public String convertToDirectory(String aPackage) {
        return cli_src_dir + "/" + aPackage.replace('.', '/') + "/";
    }


    public String getFilePackage(String file) {
        switch (file) {
            case LOGGER:
            case TEMPLATE_BUILDERS:
                return logger_package;

            case BEAN_ENACTOR2:
            case TEMPLATE_INVOKER:
            case INPUT_OUTPUT_PROCESSOR:
            case QUERY_INVOKER2:
            case INPUT_PROCESSOR:
            case COMPOSITE_BEAN_COMPLETER2:
            case BEAN_CHECKER2:
            case BEAN_COMPLETER2:
                return config_integrator_package;

            case TABLE_CONFIGURATOR:
            case TABLE_CONFIGURATOR+"ForTypes":
            case TABLE_CONFIGURATOR+WITH_MAP:
            case TABLE_CONFIGURATOR+"ForTypes"+WITH_MAP:
            case CONVERTER_CONFIGURATOR:
            case BUILDER_CONFIGURATOR:
            case SQL_INSERT_CONFIGURATOR:
            case PROPERTY_ORDER_CONFIGURATOR:
            case OUTPUTS_CONFIGURATOR:
            case INPUTS_CONFIGURATOR:
            case SQL_CONFIGURATOR:
            case CSV_CONFIGURATOR:
            case ENACTOR_CONFIGURATOR:
            case COMPOSITE_ENACTOR_CONFIGURATOR:
            case RECORD_2_RECORD_CONFIGURATOR:
            case COMPOSITE_TABLE_CONFIGURATOR:
                return configurator_package;

            case COMPOSITE_ENACTOR_CONFIGURATOR2:
            case ENACTOR_CONFIGURATOR2:

                return configurator_package2;

            case TYPE_CONVERTER:
            case DELEGATOR:
            case BEAN_COMPLETER:
            case BEAN_CHECKER:
            case QUERY_INVOKER:
            case BEAN_ENACTOR:
            case BEAN_PROCESSOR:
                return config_common_package;

            case SQL_INTERFACE:
                return Constants.CLIENT_PACKAGE;
        }
        throw new UnsupportedOperationException("Unknown file " + file);
    }


    public String getFilePackage(BeanDirection dir) {
        switch (dir) {
            case INPUTS:    return config_integrator_package;
            case OUTPUTS:   return config_integrator_package;
            case COMMON:    return config_common_package;
        }
        throw new IllegalStateException("never here");
    }

    public String getFileBackendPackage(String name) {
        return config_backend;
    }
}
