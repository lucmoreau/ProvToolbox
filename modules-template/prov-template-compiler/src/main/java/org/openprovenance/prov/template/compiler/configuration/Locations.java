package org.openprovenance.prov.template.compiler.configuration;

import org.openprovenance.prov.template.compiler.common.BeanDirection;
import org.openprovenance.prov.template.compiler.common.Constants;

import java.io.File;

import static org.openprovenance.prov.template.compiler.common.Constants.*;

public class Locations {
    final private String cli_src_dir;
    final private TemplatesProjectConfiguration configs;
    final private String configurator_package;
    final private String logger_package;
    final private String configurator_package2;
    final private String l2p_src_dir;
    private String config_common_package;
    private String config_backend;
    private String config_integrator_package;
    private String config_access_control_package;
    private String config_sql_common_backend_package;
    private String config_sql_integration_backend_package;
    private String config_sql_access_control_backend_package;
    public final String python_dir;






    static final String CONFIG2_EXTENSION = "2";

    public void updateWithConfig(TemplateCompilerConfig config) {
        if (config.package_==null) config.package_=configs.root_package;
        this.config_common_package     = config.package_+ "." + Constants.SUB_PACKAGE_CLIENT + "." + Constants.SUB_PACKAGE_COMMON;
        this.config_integrator_package = config.package_+ "." + Constants.SUB_PACKAGE_CLIENT + "." + Constants.SUB_PACKAGE_INTEGRATOR;
        this.config_access_control_package = config.package_+ "." + Constants.SUB_PACKAGE_CLIENT + "." + Constants.SUB_PACKAGE_ACCESS_CONTROL;
        this.config_backend            = config.package_;
        this.config_sql_common_backend_package = config_backend + ".sql.common";
        this.config_sql_integration_backend_package = config_backend + ".sql.integration";
        this.config_sql_access_control_backend_package = config_backend + ".sql.access_control";
    }

    public Locations(TemplatesProjectConfiguration configs, String cli_src_dir, String l2p_src_dir) {
        this.configs=configs;
        this.cli_src_dir=cli_src_dir;
        this.l2p_src_dir=l2p_src_dir;
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
    public String convertToBackendDirectory(String aPackage) {
        return l2p_src_dir + "/" + aPackage.replace('.', '/') + "/";
    }


    public String getFilePackage(String file) {
        switch (file) {
            case SQL_BEAN_COMPLETER:
            case SQL_BEAN_ENACTOR:
            case SQL_ENACTOR_IMPLEMENTATION:
            case SQL_COMPOSITE_BEAN_COMPLETER:
            case SQL_COMPOSITE_ENACTOR_IMPLEMENTATION:
            case SQL_COMPOSITE_BEAN_ENACTOR:
                return config_sql_common_backend_package;

            case SQL_BEAN_COMPLETER3:
            case SQL_ENACTOR_IMPLEMENTATION3:
            case SQL_COMPOSITE_BEAN_ENACTOR3:
            case SQL_COMPOSITE_BEAN_COMPLETER3:
            case SQL_COMPOSITE_ENACTOR_IMPLEMENTATION3:
            case SQL_BEAN_ENACTOR3:
            case SQL_ENACTOR_CONFIGURATOR3:
            case SQL_COMPOSITE_ENACTOR_CONFIGURATOR3:
                return config_sql_integration_backend_package;

            case SQL_BEAN_COMPLETER4:
            case BEAN_ENACTOR2_COMPOSITE_WP:
            case BEAN_ENACTOR2_WP:
            case QUERY_INVOKER2WP:
            case SQL_ENACTOR_IMPLEMENTATION4:
            case SQL_BEAN_ENACTOR4:
            case SQL_ENACTOR_CONFIGURATOR4:
            case SQL_COMPOSITE_BEAN_ENACTOR4:
            case SQL_COMPOSITE_ENACTOR_CONFIGURATOR4:
            case SQL_COMPOSITE_ENACTOR_IMPLEMENTATION4:
            case SQL_COMPOSITE_BEAN_COMPLETER4:

                return config_sql_access_control_backend_package;

            case LOGGER:
            case TEMPLATE_BUILDERS:
                return logger_package;

            case BEAN_ENACTOR2:
            case BEAN_LOCAL_ENACTOR2:
            case BEAN_ENACTOR2_COMPOSITE:
            case TEMPLATE_INVOKER:
            case INPUT_OUTPUT_PROCESSOR:
            case QUERY_INVOKER2:
            case INPUT_PROCESSOR:
            case COMPOSITE_BEAN_COMPLETER2:
            case BEAN_CHECKER2:
            case BEAN_COMPLETER2:
            case BEAN_COMPLETER3:
                return config_integrator_package;

            case TABLE_CONFIGURATOR:
            case TABLE_CONFIGURATOR+"ForTypes":
            case TABLE_CONFIGURATOR+WITH_MAP:
            case TABLE_CONFIGURATOR+"ForTypes"+WITH_MAP:
            case OBJECT_RECORD_MAKER_CONFIGURATOR:
            case CONVERTER_CONFIGURATOR:
            case BUILDER_CONFIGURATOR:
            case SQL_INSERT_CONFIGURATOR:
            case PROPERTY_ORDER_CONFIGURATOR:
            case RELATION0_CONFIGURATOR:
            case RELATION_CONFIGURATOR:
            case BUILDER_PROCESSOR_CONFIGURATOR:
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
