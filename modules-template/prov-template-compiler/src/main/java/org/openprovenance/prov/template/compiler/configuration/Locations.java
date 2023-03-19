package org.openprovenance.prov.template.compiler.configuration;

public class Locations {
    private final String cli_src_dir;

    final public String configs_integrator_package;
    final public String integrator_dir;

    final public String configurator_package;
    final public String configurator_dir;
    final public String configurator_package2;
    final public String configurator_dir2;

    final public String logger_package;
    final public String logger_dir;

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
        this.cli_src_dir=cli_src_dir;
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

}
