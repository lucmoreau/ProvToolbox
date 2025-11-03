package org.openprovenance.prov.template.compiler.configuration;

import org.openprovenance.prov.template.compiler.common.BeanDirection;
import org.openprovenance.prov.template.compiler.common.Constants;

import java.io.File;
import java.util.Map;

import static org.openprovenance.prov.template.compiler.common.Constants.*;

public class Locations {
    public static final String NODEFAULT_FOR = "nodefault.for.";
    final private String cli_src_dir;
    final private TemplatesProjectConfiguration configs;
    final private String l2p_src_dir;
    private final Map<String, String> packages;

    public final String python_dir;






    static final String CONFIG2_EXTENSION = "2";

    public Locations(TemplatesProjectConfiguration configs, Map<String, String> packages, String cli_src_dir, String l2p_src_dir) {
        this.configs=configs;
        this.cli_src_dir=cli_src_dir;
        this.l2p_src_dir=l2p_src_dir;
        this.python_dir=configs.python_dir;
        this.packages=packages;


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

    public String getFilePackage(String name, String file) {
        switch (file) {
            case SQL_BEAN_COMPLETER:
            case SQL_BEAN_ENACTOR:
            case SQL_ENACTOR_IMPLEMENTATION:
            case SQL_COMPOSITE_BEAN_COMPLETER:
            case SQL_COMPOSITE_ENACTOR_IMPLEMENTATION:
            case SQL_COMPOSITE_BEAN_ENACTOR:
                return getSqlCommonBackendPackage(name);

            case SQL_BEAN_COMPLETER3:
            case SQL_ENACTOR_IMPLEMENTATION3:
            case SQL_COMPOSITE_BEAN_ENACTOR3:
            case SQL_COMPOSITE_BEAN_COMPLETER3:
            case SQL_COMPOSITE_ENACTOR_IMPLEMENTATION3:
            case SQL_BEAN_ENACTOR3:
            case SQL_ENACTOR_CONFIGURATOR3:
            case SQL_COMPOSITE_ENACTOR_CONFIGURATOR3:
                return getSqlIntegrationBackendPackage(name);

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
                return getSqlAccessControlBackendPackage(name);

            case LOGGER:
            case TEMPLATE_BUILDERS:
                return getLoggerPackage(name);

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
                return getIntegratorPackage(name);

            case TABLE_CONFIGURATOR+WITH_MAP:
            case TABLE_CONFIGURATOR+"ForTypes"+WITH_MAP:
            case OBJECT_RECORD_MAKER_CONFIGURATOR:
                return getConfiguratorBackendPackage(name);

            case TABLE_CONFIGURATOR:
            case TABLE_CONFIGURATOR+"ForTypes":
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
                return getConfiguratorPackage(name);

            case COMPOSITE_ENACTOR_CONFIGURATOR2:
            case ENACTOR_CONFIGURATOR2:

                return getConfiguratorPackage2(name);

            case TYPE_CONVERTER:
            case DELEGATOR:
            case BEAN_COMPLETER:
            case BEAN_CHECKER:
            case QUERY_INVOKER:
            case BEAN_ENACTOR:
            case BEAN_PROCESSOR:
                return getCommonPackage(name);

            case SQL_INTERFACE:
                return Constants.CLIENT_PACKAGE;

        }
        throw new UnsupportedOperationException("Unknown file " + file);
    }


    /*
    @Deprecated
    public String getFilePackage(BeanDirection dir) {
        switch (dir) {
            case INPUTS:    return config_integrator_package;
            case OUTPUTS:   return config_integrator_package;
            case COMMON:    return config_common_package;
        }
        throw new IllegalStateException("never here");
    }

     */
    public String getBeansPackage(String name, BeanDirection dir) {
        return switch (dir) {
            case INPUTS -> getIntegratorPackage(name);
            case OUTPUTS -> getIntegratorPackage(name);
            case COMMON -> getCommonPackage(name);
        };
    }

    private String getLoggerPackage(String name) {
        return configs.root_package + "." + Constants.SUB_PACKAGE_CLIENT + "." + SUB_PACKAGE_LOGGER;
    }

    public String getBackendPackage(String name) {
        return  packages.getOrDefault(name, NODEFAULT_FOR +name);
        //return "org.example.templates.block";
    }


    public String getConfiguratorBackendPackage(String name) {
        return getBackendPackage(name) + ".configurator";
    }

    private String getIntegratorPackage(String name) {
        return packages.getOrDefault(name, NODEFAULT_FOR +name) + "." + Constants.SUB_PACKAGE_CLIENT + "." + Constants.SUB_PACKAGE_INTEGRATOR;
    }

    private String getCommonPackage(String name) {
        return packages.getOrDefault(name, NODEFAULT_FOR +name) +"." + Constants.SUB_PACKAGE_CLIENT + "." + Constants.SUB_PACKAGE_COMMON;
    }

    private String getConfiguratorPackage(String name) {
        return packages.getOrDefault(name, NODEFAULT_FOR +name)  + "." + Constants.SUB_PACKAGE_CLIENT + "." + CONFIGURATOR;
    }

    private String getConfiguratorPackage2(String name) {
        return configs.root_package + "." + Constants.SUB_PACKAGE_CLIENT + "." + CONFIGURATOR + CONFIG2_EXTENSION;
    }

    private String getSqlCommonBackendPackage(String name) {
        return getBackendPackage(name) + ".sql.common";
    }

    private String getSqlIntegrationBackendPackage(String name) {
        return getBackendPackage(name) + ".sql.integration";
    }

    private String getSqlAccessControlBackendPackage(String name) {
        return getBackendPackage(name) + ".sql.access_control";
    }

}
