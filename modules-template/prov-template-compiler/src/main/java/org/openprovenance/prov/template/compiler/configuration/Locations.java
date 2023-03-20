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
    private String config_common_package;
    private String config_backend;
    private String config_integrator_package;


    private final String compositeTableConfigurator;





    static String INTEGRATOR="integrator";
    static String CLIENT="client"; // FIXME: need to be able to rename
    static String COMMON="common";
    static final String CONFIG2_EXTENSION = "2";

    public void updateWithConfig(TemplateCompilerConfig config) {
        this.config_common_package     = config.package_+ "." + CLIENT + "." + COMMON;
        this.config_integrator_package = config.package_+ "." + CLIENT + "." + INTEGRATOR;
        this.config_backend            = config.package_;
    }

    public Locations(TemplatesCompilerConfig configs, String cli_src_dir) {
        this.configs=configs;
        this.cli_src_dir=cli_src_dir;
        this.compositeTableConfigurator=COMPOSITE + configs.tableConfigurator;

        configurator_package=configs.configurator_package;
        configurator_package2=configs.configurator_package+ CONFIG2_EXTENSION;
        logger_package=configs.logger_package;

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
        if (file.equals(configs.logger)) {
            return logger_package;
        } else if (file.equals(configs.tableConfigurator)) {
            return configurator_package;
        } else if (file.equals(compositeTableConfigurator)) {
            return configurator_package;
        } else if (file.equals(configs.beanProcessor)) {
            return config_common_package;
        } else if (file.equals(configs.templateBuilders)) {
            return logger_package;
        }
        switch (file) {
            case BEAN_ENACTOR2:
            case TEMPLATE_INVOKER:
            case INPUT_OUTPUT_PROCESSOR:
            case QUERY_INVOKER2:
            case INPUT_PROCESSOR:                   return config_integrator_package;

            case CONVERTER_CONFIGURATOR:
            case BUILDER_CONFIGURATOR:
            case SQL_INSERT_CONFIGURATOR:
            case PROPERTY_ORDER_CONFIGURATOR:
            case SQL_CONFIGURATOR:
            case CSV_CONFIGURATOR:
            case ENACTOR_CONFIGURATOR:
            case COMPOSITE_ENACTOR_CONFIGURATOR:
            case DELEGATOR:
            case BEAN_COMPLETER:
            case BEAN_CHECKER:                      return configurator_package;

            case COMPOSITE_ENACTOR_CONFIGURATOR2:
            case ENACTOR_CONFIGURATOR2:
            case COMPOSITE_BEAN_COMPLETER2:
            case TYPE_CONVERTER:
            case BEAN_CHECKER2:
            case BEAN_COMPLETER2:                   return configurator_package2;

            case QUERY_INVOKER:
            case BEAN_ENACTOR:                      return config_common_package;

            case SQL_INTERFACE:                     return Constants.CLIENT_PACKAGE;
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
