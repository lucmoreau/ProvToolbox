package org.openprovenance.prov.template.compiler.configuration;

import static org.openprovenance.prov.template.compiler.common.Constants.*;

public class Locations {
    private final String cli_src_dir;
    private final TemplatesCompilerConfig configs;

    final public String configs_integrator_package;
    final public String integrator_dir;

    final public String configurator_package;
    final public String configurator_dir;
    final public String configurator_package2;
    final public String configurator_dir2;

    final public String logger_package;
    final public String logger_dir;
    private final String compositeTableConfigurator;

    public String config_common_package;
    public String config_integrator_package;
    public String config_backend;
    public String config_integrator_dir;

    static String INTEGRATOR="integrator";
    static String COMMON="client"; // FIXME: need to be able to rename
    static final String CONFIG2_EXTENSION = "2";

    public void updateWithConfig(TemplateCompilerConfig config) {
        this.config_common_package     = config.package_+ "." + COMMON;
        this.config_integrator_package = config.package_+ "." + COMMON + "." + INTEGRATOR;
        this.config_backend            = config.package_;

        this.config_integrator_dir=convertToDirectory(cli_src_dir, config_integrator_package);

    }

    public Locations(TemplatesCompilerConfig configs, String cli_src_dir) {
        this.configs=configs;
        this.cli_src_dir=cli_src_dir;
        this.compositeTableConfigurator=COMPOSITE + configs.tableConfigurator;

        configs_integrator_package =configs.logger_package+ "." + INTEGRATOR;

        configurator_package=configs.configurator_package;
        configurator_package2=configs.configurator_package+ CONFIG2_EXTENSION;

        logger_package=configs.logger_package;

        this.integrator_dir    = convertToDirectory(cli_src_dir, configs_integrator_package);
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
        }
        switch (file) {
            case TEMPLATE_INVOKER:                  return config_integrator_package;
            case COMPOSITE_ENACTOR_CONFIGURATOR:    return configurator_package;
            case DELEGATOR:                         return configurator_package;
            case INPUT_OUTPUT_PROCESSOR:            return config_integrator_package;
            case QUERY_INVOKER:                     return configurator_package;
            case QUERY_INVOKER3:                    return config_integrator_package;
            case BEAN_COMPLETER:                    return configurator_package;
            case BEAN_CHECKER3:                     return configurator_package2;
            case BEAN_CHECKER:                      return configurator_package;
            case INPUT_PROCESSOR:                   return config_integrator_package;
        }
        throw new UnsupportedOperationException("Unknown file " + file);
    }

}
