package org.openprovenance.prov.template.compiler.configuration;

public class Locations {
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


    public void updateWithConfig(TemplateCompilerConfig config) {
        this.config_common_package =config.package_+ ".client";
        this.config_integrator_package =config.package_+ ".client.integrator";
        this.config_backend=config.package_;
    }

    public Locations(TemplatesCompilerConfig configs,String cli_src_dir) {
        configs_integrator_package =configs.logger_package+ ".integrator";;
        configurator_package=configs.configurator_package;
        configurator_package2=configs.configurator_package+"2";
        logger_package=configs.logger_package;

        this.integrator_dir= cli_src_dir + "/" +  configs_integrator_package.replace('.', '/') + "/" ;
        this.configurator_dir= cli_src_dir + "/" + configurator_package.replace('.', '/') + "/";

        this.configurator_dir2= cli_src_dir + "/" + configurator_package2.replace('.', '/') + "/";
        this.logger_dir = cli_src_dir + "/" + logger_package.replace('.', '/') + "/";
    }

}
