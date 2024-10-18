package org.openprovenance.prov.template.compiler;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import org.apache.maven.model.*;
import org.apache.maven.model.io.xpp3.MavenXpp3Writer;
import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.codehaus.plexus.util.xml.Xpp3DomBuilder;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.openprovenance.prov.configuration.Configuration;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.template.compiler.configuration.SpecificationFile;
import org.openprovenance.prov.template.compiler.configuration.TemplateCompilerConfig;
import org.openprovenance.prov.template.compiler.configuration.TemplatesProjectConfiguration;

import javax.lang.model.element.Modifier;
import java.io.*;
import java.util.Properties;

import static org.openprovenance.prov.template.compiler.common.Constants.TESTER_FILE;

public class CompilerMaven {
    private final ConfigProcessor configProcessor;
    private final CompilerUtil compilerUtil;

    public CompilerMaven(ProvFactory pFactory, ConfigProcessor configProcessor) {
        this.configProcessor = configProcessor;
        this.compilerUtil=new CompilerUtil(pFactory);
    }

    public boolean makeRootPom(TemplatesProjectConfiguration configs, String root_dir, String cli_lib, String l2p_lib) {
        Model model = new Model();
        model.setGroupId(configs.group);
        model.setArtifactId(configs.name);
        model.setVersion(configs.version);
        model.setName(configs.name);
        model.setPackaging("pom");
        model.setDescription(configs.description);
        model.setModelVersion("4.0.0");
        model.addModule(cli_lib);
        model.addModule(l2p_lib);

        addCompilerDeclaration(model);
        addJunitDependency(model);

        model.addProperty("java12.home", "/usr/local/java/jdk-12.0.1.jdk/Contents/Home/");


        try {
            new MavenXpp3Writer().write(new FileWriter(root_dir + "/pom.xml"), model);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }

    public boolean makeSubPom(TemplatesProjectConfiguration configs, String dir, String name, boolean dependencies, boolean jsweet, boolean jackson, boolean escape) {
        Model model = new Model();
        model.setArtifactId(name);
        model.setName(name);
        model.setPackaging("jar");
        model.setDescription(configs.description + " (" + name + ")");
        Parent parent = new Parent();
        parent.setArtifactId(configs.name);
        parent.setGroupId(configs.group);
        parent.setVersion(configs.version);
        model.setParent(parent);
        model.setModelVersion("4.0.0");

        if (dependencies) {

            addClientBuilderDependency(name.replace("l2p","cli"),configs.group,configs.version, model);

            addProvDependency("prov-model", model);
            addProvDependency("prov-n", model);
            //addProvDependency("prov-json", model);
            addProvDependency("prov-template-compiler", model);
            addProvDependency("prov-interop", model);

        } else if (escape) {
            addProvDependency("prov-model", model);
        }

        if (jsweet) {
            addJsweetCall(model);
            //addJSweetDependency(model);
            //addProvDependency("prov-jsweet-candy-js", model);
            addProvDependency("prov-jsweet-candy-java", model);
        }

        if (jackson) {
            addJacksonDependency(model);
        }

        if (jsweet) {
            addBuildHelperMavenPlugin(model);
        }

        try {
            new MavenXpp3Writer().write(new FileWriter(dir + "/pom.xml"), model);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }
    public boolean makeSubPomJweet(TemplatesProjectConfiguration configs, String dir, String name, boolean jsweet) {
        Model model = new Model();
        model.setArtifactId(name);
        model.setName(name);
        model.setPackaging("jar");
        model.setDescription(configs.description + " (" + name + ") [JSweet Compilation]");
        Parent parent = new Parent();
        parent.setArtifactId(configs.name);
        parent.setGroupId(configs.group);
        parent.setVersion(configs.version);
        model.setParent(parent);
        model.setModelVersion("4.0.0");


        if (jsweet) {
            addJSweetDependency(model);
            //addProvDependency("prov-jsweet-candy-js", model);
            addProvDependency("prov-jsweet-candy-java", model);
        }

  
        


     

        if (false) {
            addJacksonDependency(model);
        }

        if (jsweet) {
            addBuildHelperMavenPlugin(model);
        }

        try {
            new File(dir + "/jsweet").mkdirs();
            new MavenXpp3Writer().write(new FileWriter(dir + "/jsweet/jsweet-pom.xml"), model);

            // get property java12.home from properties file compiler.properties

            Properties properties=Configuration.getPropertiesFromClasspath(this.getClass(),"compiler.properties");
            String java12Home=properties.getProperty("java12.home");


            String script= """
#!/bin/bash
export JAVA_HOME="${java12.home}"

mvn -f jsweet/jsweet-pom.xml jsweet:jsweet
""".replace("${java12.home}", java12Home);

            // write script into file dir + "/jsweet/jsweet.sh"
            FileWriter fw = new FileWriter(dir + "/jsweet/jsweet.sh");
            fw.write(script);
            fw.close();

            // make the script executable
            new File(dir + "/jsweet/jsweet.sh").setExecutable(true);

            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }

          /*
       add following plugin to model
        			<plugin>
				<artifactId>maven-antrun-plugin</artifactId>
				<version>${antrun.plugin.version}</version>
				<executions>
					<execution>
						<id>check-java12-exists</id>
						<phase>generate-sources</phase>
						<goals>
							<goal>run</goal>
						</goals>
						<configuration>
							<target>
								<available file="${java12.home}" type="dir" property="java12Exists" />
							</target>
							<exportAntProperties>true</exportAntProperties>
						</configuration>
					</execution>
					<execution>
						<id>antrun-jsweet</id>
						<phase>generate-sources</phase>
						<goals>
							<goal>run</goal>
						</goals>
						<configuration>
							<!--suppress UnresolvedMavenProperty -->
							<target if="${java12Exists}">
								<echo message="Java 12 found, doing jsweet compilation" />
								<exec executable="${project.build.directory}/jsweet/jsweet.sh">

								</exec>
							</target>
						</configuration>
					</execution>
					<execution>
						<id>antrun.no.jsweet</id>
						<phase>generate-sources</phase>
						<goals>
							<goal>run</goal>
						</goals>
						<configuration>
							<!--suppress UnresolvedMavenProperty -->
							<target unless="${java12Exists}">
								<echo message="Java 12 not found, skipping jsweet compilation, no npm test" />
							</target>
						</configuration>
					</execution>
					<execution>
						<phase>package</phase>
						<configuration>
							<target>
								<echo message="copying generated bundles to dist..." />
								<copy file="${project.build.directory}/resources/META-INF/resources/webjars/${project.artifactId}/${project.version}/bundle.js" tofile="dist/${project.artifactId}.js" verbose="true" failonerror="false" />
								<copy file="${project.build.directory}/resources/src/typings/${project.artifactId}/${project.version}/bundle.d.ts" tofile="dist/${project.artifactId}.d.ts" verbose="true" failonerror="false" />
							</target>
						</configuration>
						<goals>
							<goal>run</goal>
						</goals>
					</execution>
				</executions>
			</plugin>

         */

    private void addJsweetCall(Model model) {
        Plugin plugin = new Plugin();
        plugin.setArtifactId("maven-antrun-plugin");
        plugin.setGroupId("org.apache.maven.plugins");
        plugin.setVersion("3.0.0");

        StringBuilder configString = new StringBuilder()
                .append("<configuration>")
                .append("<target>")
                .append("<available file=\"${java12.home}\" type=\"dir\" property=\"java12Exists\" />")
                .append("</target>")
                .append("<exportAntProperties>true</exportAntProperties>")
                .append("</configuration>");





        PluginExecution pe1=new PluginExecution();
        pe1.addGoal("run");
        pe1.setId("check-java12-exists");
        pe1.setPhase("generate-sources");

        Xpp3Dom config = null;
        try {
            config = Xpp3DomBuilder.build(new StringReader(configString.toString()));
        } catch (XmlPullParserException | IOException ex) {
            throw new RuntimeException("Issue creating config for enforcer plugin", ex);
        }
        pe1.setConfiguration(config);

        PluginExecution pe2=new PluginExecution();
        pe2.addGoal("run");
        pe2.setId("antrun-jsweet");
        pe2.setPhase("generate-sources");

        StringBuilder configString2 = new StringBuilder()
                .append("<configuration>")
                .append("<target if=\"${java12Exists}\">")
                .append("<echo message=\"Java 12 found, doing jsweet compilation\" />")
                .append("<exec executable=\"${project.basedir}/jsweet/jsweet.sh\">")
                .append("</exec>")
                .append("</target>")
                .append("</configuration>");
        try {
            config = Xpp3DomBuilder.build(new StringReader(configString2.toString()));
        } catch (XmlPullParserException | IOException ex) {
            throw new RuntimeException("Issue creating config for enforcer plugin", ex);
        }
        pe2.setConfiguration(config);

        PluginExecution pe3=new PluginExecution();
        pe3.addGoal("run");
        pe3.setId("antrun.no.jsweet");
        pe3.setPhase("generate-sources");

        StringBuilder configString3 = new StringBuilder()
                .append("<configuration>")
                .append("<target unless=\"${java12Exists}\">")
                .append("<echo message=\"Java 12 not found, skipping jsweet compilation, no npm test\" />")
                .append("</target>")
                .append("</configuration>");

        try {
            config = Xpp3DomBuilder.build(new StringReader(configString3.toString()));
        } catch (XmlPullParserException | IOException ex) {
            throw new RuntimeException("Issue creating config for enforcer plugin", ex);
        }
        pe3.setConfiguration(config);

        PluginExecution pe4=new PluginExecution();
        pe4.addGoal("run");
        pe4.setPhase("package");

        StringBuilder configString4 = new StringBuilder()
                .append("<configuration>")
                .append("<target>")
                .append("<echo message=\"copying generated bundles to dist...\" />")
                .append("<copy file=\"${project.build.directory}/resources/META-INF/resources/webjars/${project.artifactId}/${project.version}/bundle.js\" tofile=\"dist/${project.artifactId}.js\" verbose=\"true\" failonerror=\"false\" />")
                .append("<copy file=\"${project.build.directory}/resources/src/typings/${project.artifactId}/${project.version}/bundle.d.ts\" tofile=\"dist/${project.artifactId}.d.ts\" verbose=\"true\" failonerror=\"false\" />")
                .append("</target>")
                .append("</configuration>");

        try {
            config = Xpp3DomBuilder.build(new StringReader(configString4.toString()));
        } catch (XmlPullParserException | IOException ex) {
            throw new RuntimeException("Issue creating config for enforcer plugin", ex);
        }
        pe4.setConfiguration(config);

        plugin.addExecution(pe1);
        plugin.addExecution(pe2);
        plugin.addExecution(pe3);
        plugin.addExecution(pe4);

        Build b=new Build();
        b.addPlugin(plugin);
        model.setBuild(b);

    }

    public String getProvPackageId() {
        return "org.openprovenance.prov";
    }

    public void addClientBuilderDependency(String artifact, String group, String version, Model model) {
        Dependency dep = new Dependency();
        dep.setArtifactId(artifact);
        dep.setGroupId(group);
        dep.setVersion(version);
        model.addDependency(dep);
    }

    public void addProvDependency(String artifact, Model model) {
        Dependency dep = new Dependency();
        dep.setArtifactId(artifact);
        dep.setGroupId(getProvPackageId());
        dep.setVersion(getProvVersion());
       // dep.setScope("provided");  // NOTE the scope. We need it at compile time to construct the js file. At runtime, we only import the js webjar in the javascript file.
        model.addDependency(dep);
    }

    public void addJunitDependency(Model model) {
        Dependency dep = new Dependency();
        dep.setArtifactId("junit");
        dep.setGroupId("junit");
        dep.setVersion("4.11");
        dep.setScope("test");
        model.addDependency(dep);
    }

    public void addJacksonDependency(Model model) {
        Dependency dep = new Dependency();
        dep.setArtifactId("jackson-databind");
        dep.setGroupId("com.fasterxml.jackson.core");
        dep.setVersion("2.9.9");
        dep.setScope("test");
        model.addDependency(dep);
    }

    public void addJSweetDependency(Model model) {
        Plugin plugin = new Plugin();
        plugin.setArtifactId("jsweet-maven-plugin");
        plugin.setGroupId("org.jsweet");
        plugin.setVersion("3.1.0");

        StringBuilder configString = new StringBuilder()
                .append("<configuration>")
                .append("<verbose>false</verbose>")
                .append("<outDir>target/js</outDir>")
                .append("<tsOut>target/ts</tsOut>")
                .append("<candiesJsOut>webapp/candies</candiesJsOut>")
                .append("<targetVersion>ES6</targetVersion>")
                .append("<module>none</module>")
                .append("<moduleResolution>classic</moduleResolution>")
                .append("</configuration>");

        Xpp3Dom config = null;
        try {
            config = Xpp3DomBuilder.build(new StringReader(configString.toString()));
        } catch (XmlPullParserException | IOException ex) {
            throw new RuntimeException("Issue creating config for enforcer plugin", ex);
        }
        plugin.setConfiguration(config);

        PluginExecution pe1=new PluginExecution();
        pe1.addGoal("jsweet");
        pe1.setId("generate-js");
        pe1.setPhase("generate-sources");

        PluginExecution pe2=new PluginExecution();
        pe2.addGoal("clean");
        pe2.setId("clean");
        pe2.setPhase("clean");
        plugin.addExecution(pe1);
        plugin.addExecution(pe2);

        Build b=new Build();
        b.addPlugin(plugin);
        model.setBuild(b);
    }

    public void addBuildHelperMavenPlugin(Model model) {
        Plugin plugin = new Plugin();
        plugin.setArtifactId("build-helper-maven-plugin");
        plugin.setGroupId("org.codehaus.mojo");
        plugin.setVersion("3.0.0");


        StringBuilder configString = new StringBuilder()
                .append("\n" +
                        "\n" +
                        "<configuration>\n" +
                        "  <resources>\n" +
                        "    <resource>\n" +
                        "      <directory>${project.build.directory}</directory>\n" +
                        "      <targetPath>META-INF/resources/webjars/${project.artifactId}/${project.version}/json</targetPath>\n" +
                        "      <includes>\n" +
                        "        <include>*.json</include>\n" +
                        "      </includes>\n" +
                        "    </resource>\n" +
                        "    <resource>\n" +
                        "      <directory>${project.build.directory}/js</directory>\n" +
                        "      <targetPath>META-INF/resources/webjars/${project.artifactId}/${project.version}/js</targetPath>\n" +
                        "      <excludes>\n" +
                        "        <exclude>**/junk/**</exclude>\n" +
                        "      </excludes>\n" +
                        "    </resource>\n" +
                        "    <resource>\n" +
                        "      <directory>src/main/js</directory>\n" +
                        "      <targetPath>META-INF/resources/webjars/${project.artifactId}/${project.version}/js</targetPath>\n" +
                        "      <excludes>\n" +
                        "        <exclude>**/junk/**</exclude>\n" +
                        "      </excludes>\n" +
                        "    </resource>\n" +
                        "    <resource>\n" +
                        "      <directory>src/main/resources</directory>\n" +
                        "      <targetPath>META-INF/resources/webjars/${project.artifactId}/${project.version}/schema</targetPath>\n" +
                        "      <includes>\n" +
                        "        <includes>schema.json</includes>\n" +
                        "      </includes>\n" +
                        "    </resource>\n" +
                        "    <resource>\n" +
                        "      <directory>src/main/css</directory>\n" +
                        "      <targetPath>META-INF/resources/webjars/${project.artifactId}/${project.version}/css</targetPath>\n" +
                        "      <includes>\n" +
                        "        <includes>*.css</includes>\n" +
                        "      </includes>\n" +
                        "    </resource>\n" +
                        "    <resource>\n" +
                        "      <directory>${project.build.directory}/ts</directory>\n" +
                        "      <targetPath>META-INF/resources/webjars/${project.artifactId}/${project.version}/ts</targetPath>\n" +
                        "      <excludes>\n" +
                        "        <exclude>**/junk/**</exclude>\n" +
                        "      </excludes>\n" +
                        "    </resource>\n" +
                        "    <resource>\n" +
                        "      <directory>/bindings</directory>\n" +
                        "      <targetPath>META-INF/resources/webjars/${project.artifactId}/${project.version}/bindings</targetPath>\n" +
                        "      <excludes>\n" +
                        "        <exclude>**/junk/**</exclude>\n" +
                        "      </excludes>\n" +
                        "    </resource>\n" +
                        "    <resource>\n" +
                        "      <directory>${project.build.directory}/templates</directory>\n" +
                        "      <targetPath>META-INF/resources/webjars/${project.artifactId}/${project.version}/templates</targetPath>\n" +
                        "      <excludes>\n" +
                        "        <exclude>**/junk/**</exclude>\n" +
                        "      </excludes>\n" +
                        "    </resource>\n" +
                        "    <resource>\n" +
                        "      <directory>${project.build.directory}/resources</directory>\n" +
                        "      <targetPath>META-INF/resources/webjars/${project.artifactId}/${project.version}/schema</targetPath>\n" +
                        "      <excludes>\n" +
                        "        <exclude>**/junk/**</exclude>\n" +
                        "      </excludes>\n" +
                        "    </resource>\n" +
                        "  </resources>\n" +
                        "</configuration>\n");


        Xpp3Dom config = null;
        try {
            config = Xpp3DomBuilder.build(new StringReader(configString.toString()));
        } catch (XmlPullParserException | IOException ex) {
            throw new RuntimeException("Issue creating config for enforcer plugin", ex);
        }
        plugin.setConfiguration(config);

        PluginExecution pe1=new PluginExecution();
        pe1.addGoal("add-resource");
        pe1.setId("prepare-webjar");
        pe1.setPhase("generate-sources");


        plugin.addExecution(pe1);

        Build b= model.getBuild();
        if (b==null) b=new Build();
        b.addPlugin(plugin);
        model.setBuild(b);
    }





    public SpecificationFile generateTestFile_l2p(TemplatesProjectConfiguration configs, String directory, String fileName) {

        TypeSpec.Builder builder = compilerUtil.generateClassInitExtends(TESTER_FILE,"junit.framework","TestCase");

        MethodSpec.Builder mbuilder = MethodSpec.methodBuilder("testMain")
                .addModifiers(Modifier.PUBLIC)
                .addException(Exception.class)
                .returns(void.class)
                .addStatement("$T pf=org.openprovenance.prov.interop.InteropFramework.getDefaultFactory()", ProvFactory.class)
                ;

        for (TemplateCompilerConfig template: configs.templates) {
            String bn=compilerUtil.templateNameClass(template.name);
            mbuilder.addStatement("System.setOut(new java.io.PrintStream(\"target/" + template.name + ".provn\"))");
            mbuilder.addStatement("$T.main(null)", ClassName.get(template.package_, bn));
        }

        MethodSpec method=mbuilder.build();

        builder.addMethod(method);

        TypeSpec theInitializer=builder.build();

        JavaFile myfile = JavaFile.builder(configs.root_package, theInitializer)
                .addFileComment("Generated Automatically by ProvToolbox ($N) for templates config $N",getClass().getName(), configs.name)
                .build();
        return new SpecificationFile(myfile, directory, fileName, configs.root_package);

    }




    public void addCompilerDeclaration(Model model) {
        Plugin plugin1=new Plugin();
        plugin1.setGroupId("org.apache.maven.plugins");
        plugin1.setArtifactId("maven-compiler-plugin");
        plugin1.setVersion("3.8.1");

        StringBuilder configString1 = new StringBuilder()
                .append("<configuration>")
                .append("<source>1.8</source>")
                .append("<target>1.8</target>")
                .append("</configuration>");

        Plugin plugin2=new Plugin();
        plugin2.setGroupId("org.apache.maven.plugins");
        plugin2.setArtifactId("maven-javadoc-plugin");
        plugin2.setVersion("3.1.1");

        StringBuilder configString2 = new StringBuilder()
                .append("<configuration>")
                .append("<source>1.8</source>")
                .append("</configuration>");

        Xpp3Dom config1 = null;
        Xpp3Dom config2 = null;
        try {
            config1 = Xpp3DomBuilder.build(new StringReader(configString1.toString()));
            config2 = Xpp3DomBuilder.build(new StringReader(configString2.toString()));
        } catch (XmlPullParserException | IOException ex) {
            throw new RuntimeException("Issue creating config for enforcer plugin", ex);
        }

        PluginManagement pm=new PluginManagement();
        pm.addPlugin(plugin1);
        plugin1.setConfiguration(config1);
        pm.addPlugin(plugin2);
        plugin2.setConfiguration(config2);

        Build build=new Build();
        model.setBuild(build);

        build.setPluginManagement(pm);

    }

    public String getProvVersion() {
        return Configuration.toolboxVersion;
    }




}