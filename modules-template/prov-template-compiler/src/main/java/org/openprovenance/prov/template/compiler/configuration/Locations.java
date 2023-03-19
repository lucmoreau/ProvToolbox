package org.openprovenance.prov.template.compiler.configuration;

import org.openprovenance.prov.template.compiler.common.BeanDirection;
import org.openprovenance.prov.template.compiler.common.Constants;

import static org.openprovenance.prov.template.compiler.common.Constants.*;

public class Locations {
    final private String cli_src_dir;
    final private TemplatesCompilerConfig configs;
    final private String configurator_package;
    final private String logger_package;
    private String config_common_package;


    final public String configurator_dir;
    final public String configurator_package2;
    final public String configurator_dir2;

    final public String logger_dir;
    private final String compositeTableConfigurator;

    public String config_integrator_package; // FIXME: should be made private
    public String config_backend;// FIXME: should be made private




    public String config_integrator_dir;

    static String INTEGRATOR="integrator";
    static String CLIENT="client"; // FIXME: need to be able to rename
    static String COMMON="common"; // FIXME: need to be able to rename
    static final String CONFIG2_EXTENSION = "2";

    public void updateWithConfig(TemplateCompilerConfig config) {
        this.config_common_package     = config.package_+ "." + CLIENT + "." + COMMON;
        this.config_integrator_package = config.package_+ "." + CLIENT + "." + INTEGRATOR;
        this.config_backend            = config.package_;

        this.config_integrator_dir=convertToDirectory(cli_src_dir, config_integrator_package);

    }

    public Locations(TemplatesCompilerConfig configs, String cli_src_dir) {
        this.configs=configs;
        this.cli_src_dir=cli_src_dir;
        this.compositeTableConfigurator=COMPOSITE + configs.tableConfigurator;

        configurator_package=configs.configurator_package;
        configurator_package2=configs.configurator_package+ CONFIG2_EXTENSION;

        logger_package=configs.logger_package;

        this.configurator_dir  = convertToDirectory(cli_src_dir, configurator_package);
        this.configurator_dir2 = convertToDirectory(cli_src_dir, configurator_package2);
        this.logger_dir        = convertToDirectory(cli_src_dir, logger_package);
    }

    private String convertToDirectory(String rootDir, String aPackage) {
        return rootDir + "/" + aPackage.replace('.', '/') + "/";
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
            case TEMPLATE_INVOKER:                  return config_integrator_package;
            case INPUT_OUTPUT_PROCESSOR:            return config_integrator_package;
            case QUERY_INVOKER3:                    return config_integrator_package;
            case INPUT_PROCESSOR:                   return config_integrator_package;

            case COMPOSITE_ENACTOR_CONFIGURATOR:    return configurator_package;
            case DELEGATOR:                         return configurator_package;
            case QUERY_INVOKER:                     return configurator_package;
            case BEAN_COMPLETER:                    return configurator_package;
            case BEAN_CHECKER:                      return configurator_package;

            case BEAN_CHECKER3:                     return configurator_package2;
            case BEAN_COMPLETER2:                   return configurator_package2;

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

}
