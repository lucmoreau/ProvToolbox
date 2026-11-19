package org.openprovenance.prov.template.compiler;

import org.apache.maven.model.*;
import org.apache.maven.model.io.xpp3.MavenXpp3Writer;
import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.codehaus.plexus.util.xml.Xpp3DomBuilder;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.openprovenance.prov.configuration.Configuration;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.template.compiler.configuration.TemplateConfigurationEnum;
import org.openprovenance.prov.template.compiler.configuration.Locations;
import org.openprovenance.prov.template.compiler.configuration.SpecificationFile;
import org.openprovenance.prov.template.compiler.configuration.TemplateCompilerConfig;
import org.openprovenance.prov.template.compiler.configuration.TemplatesProjectConfiguration;
import org.openprovenance.prov.template.compiler.past.*;
import org.openprovenance.prov.template.compiler.past.Class;
import org.openprovenance.prov.template.compiler.past.type.ClassName;

import javax.lang.model.element.Modifier;
import java.io.*;
import java.util.List;
import java.util.Properties;
import java.util.function.Supplier;

import static org.openprovenance.prov.template.compiler.common.Constants.TESTER_FILE;
import static org.openprovenance.prov.template.compiler.configuration.SpecificationFile.generateJava;
import static org.openprovenance.prov.template.compiler.configuration.SpecificationFile.generatePython;
import static org.openprovenance.prov.template.compiler.past.BinaryOp.BINARY_OP;
import static org.openprovenance.prov.template.compiler.past.Constant.CONSTANT;
import static org.openprovenance.prov.template.compiler.past.Constant.getNull;
import static org.openprovenance.prov.template.compiler.past.Definition.DEFINITION;
import static org.openprovenance.prov.template.compiler.past.Method.METHOD;
import static org.openprovenance.prov.template.compiler.past.MethodCall.CONSTRUCTOR_CALL;
import static org.openprovenance.prov.template.compiler.past.MethodCall.METHOD_CALL;
import static org.openprovenance.prov.template.compiler.past.Variable.VARIABLE;
import static org.openprovenance.prov.template.compiler.past.type.ClassName.*;

public class CompilerMaven {
    private final ConfigProcessor configProcessor;
    private final CompilerUtil compilerUtil;
    private final PastFactory pastFactory;

    static final ClassName PRINT_STREAM = ClassName.get("PrintStream", "java.io");
    static final ClassName TEST_CASE = ClassName.get("TestCase", "junit.framework");
    static final ClassName INTEROP_FRAMEWORK = ClassName.get("InteropFramework", "org.openprovenance.prov.interop");

    public CompilerMaven(ProvFactory pFactory, ConfigProcessor configProcessor) {
        this.configProcessor = configProcessor;
        this.compilerUtil=new CompilerUtil(pFactory);
        this.pastFactory=compilerUtil.getPastFactory();
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

        if (!Locations.externalPackagesOf(configs).isEmpty()) {
            addExternalSources(model);
        }

        try {
            new MavenXpp3Writer().write(new FileWriter(dir + "/pom.xml"), model);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }
    /**
     * T34: the JSweet build of a client project, on the pattern of provenance-templates-library: a standalone POM, {@code pom-jsweet.xml}, at the
     * project's root (its base directory is the project's, so the sources are {@code src/main/java}), run from the project's POM
     * ({@link #addJsweetCall}) as {@code mvn -f pom-jsweet.xml jsweet:jsweet}. It had been {@code jsweet/jsweet-pom.xml}, whose base directory is
     * {@code jsweet/}, where jsweet found no source and "succeeded" with no output, run by a script that set {@code JAVA_HOME} to a JDK 12.
     * The POM has the coordinates of the project (the bundle's webjar path and typings path are built from them), and depends on what the
     * transpiled sources use: j4ts and the candy that supplies the rest of {@code java.util} and the toolbox's own classes.
     */
    public boolean makeSubPomJweet(TemplatesProjectConfiguration configs, String dir, String name, boolean jsweet) {
        if (!jsweet) return true;
        Model model = new Model();
        model.setModelVersion("4.0.0");
        model.setGroupId(configs.group);
        model.setArtifactId(name);
        model.setVersion(configs.version);
        model.setPackaging("jar");
        model.setName(name);
        model.setDescription(configs.description + " (" + name + ") [JSweet Compilation]");
        model.addProperty("project.build.sourceEncoding", "UTF-8");

        addClientBuilderDependency("j4ts", "org.jsweet", "2.0.0", model);
        addProvDependency("prov-jsweet-candy-java", model);
        addJSweetDependency(model);

        try {
            new MavenXpp3Writer().write(new FileWriter(dir + "/pom-jsweet.xml"), model);
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

    /**
     * T34: runs the JSweet build of {@code pom-jsweet.xml} from antrun, at {@code generate-sources}, as provenance-templates-library does: a nested
     * {@code mvn -f pom-jsweet.xml jsweet:jsweet} whose JVM opens the {@code jdk.compiler} packages jsweet reaches into (it fails on a recent JDK
     * with "An API incompatibility was encountered" without), with the caller's local repository ({@code -Dmaven.repo.local}), and
     * {@code ${jsweet.mvn.options}}, a property that is empty unless the caller sets it ({@code -Djsweet.mvn.options=-o} for an offline
     * build). Then, at {@code package}, the bundle and its typings are copied to {@code dist}. No JDK 12, no script.
     */
    private void addJsweetCall(Model model) {
        Plugin plugin = new Plugin();
        plugin.setArtifactId("maven-antrun-plugin");
        plugin.setGroupId("org.apache.maven.plugins");
        plugin.setVersion("3.1.0");

        model.addProperty("jsweet.mvn.options", "");

        StringBuilder opens = new StringBuilder();
        for (String p : List.of("api", "code", "file", "model", "parser", "tree", "util")) {
            if (opens.length() > 0) opens.append(" ");
            opens.append("--add-opens jdk.compiler/com.sun.tools.javac.").append(p).append("=ALL-UNNAMED");
        }

        PluginExecution pe = new PluginExecution();
        pe.addGoal("run");
        pe.setId("antrun-jsweet");
        pe.setPhase("generate-sources");
        pe.setConfiguration(dom("<configuration><target>"
                + "<echo message=\"Running the JSweet build (mvn -f pom-jsweet.xml jsweet:jsweet)\" />"
                + "<exec executable=\"mvn\" dir=\"${project.basedir}\" failonerror=\"true\">"
                + "<env key=\"MAVEN_OPTS\" value=\"" + opens + "\" />"
                + "<arg line=\"-f pom-jsweet.xml -Dmaven.repo.local=${settings.localRepository} ${jsweet.mvn.options} jsweet:jsweet\" />"
                + "</exec>"
                + "</target></configuration>"));

        PluginExecution copy = new PluginExecution();
        copy.addGoal("run");
        copy.setId("copy-jsweet-bundle");
        copy.setPhase("package");
        copy.setConfiguration(dom("<configuration><target>"
                + "<echo message=\"copying generated bundles to dist...\" />"
                + "<copy file=\"${project.build.directory}/js/bundle.js\" tofile=\"dist/${project.artifactId}.js\" verbose=\"true\" failonerror=\"false\" />"
                + "<copy file=\"${project.build.directory}/resources/src/typings/${project.artifactId}/${project.version}/bundle.d.ts\" tofile=\"dist/${project.artifactId}.d.ts\" verbose=\"true\" failonerror=\"false\" />"
                + "</target></configuration>"));

        plugin.addExecution(pe);
        plugin.addExecution(copy);

        Build b = model.getBuild();
        if (b == null) {
            b = new Build();
            model.setBuild(b);
        }
        b.addPlugin(plugin);
    }

    private static Xpp3Dom dom(String xml) {
        try {
            return Xpp3DomBuilder.build(new StringReader(xml));
        } catch (XmlPullParserException | IOException ex) {
            throw new RuntimeException("Issue creating a plugin configuration", ex);
        }
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
        dep.setGroupId("tools.jackson.core"); // Jackson 3 (was com.fasterxml.jackson.core 2.9.9)
        dep.setVersion("3.2.4");
        dep.setScope("test");
        model.addDependency(dep);
    }

    /**
     * The jsweet-maven-plugin of {@code pom-jsweet.xml}, configured as provenance-templates-library does, but for one thing: the project's own
     * sources ({@code compileSourceRootsOverride}), one bundle ({@code target/js/bundle.js}) and its typings
     * ({@code target/resources/src/typings/<artifact>/<version>/bundle.d.ts}). The plugin's configuration, not an execution's, so that
     * {@code mvn jsweet:jsweet} reads it. The module is {@code none}, not {@code commonjs}: the bundle is a script whose namespaces are global, as
     * the candy's are. With CommonJS the client's own {@code org} is a module's, which hides the candy's {@code org.openprovenance} from TypeScript
     * ("Property 'openprovenance' does not exist on type 'typeof org'"): the library's catalogues, under {@code org.openprovenance}, do not meet it, and
     * a client whose builders call {@code StringEscapeUtils} (the candy's) under another root package does.
     */
    public void addJSweetDependency(Model model) {
        Plugin plugin = new Plugin();
        plugin.setArtifactId("jsweet-maven-plugin");
        plugin.setGroupId("org.jsweet");
        plugin.setVersion("3.1.0");
        plugin.setConfiguration(dom("<configuration>"
                + "<compileSourceRootsOverride>${project.basedir}/src/main/java</compileSourceRootsOverride>"
                + "<verbose>false</verbose>"
                + "<outDir>${project.build.directory}/js</outDir>"
                + "<tsOut>${project.build.directory}/ts</tsOut>"
                + "<candiesJsOut>${project.build.directory}/webapp/candies</candiesJsOut>"
                + "<excludes><exclude>**/node_modules/**</exclude><exclude>**/node/**</exclude></excludes>"
                + "<targetVersion>ES6</targetVersion>"
                + "<module>none</module>"
                + "<bundle>true</bundle>"
                + "<declaration>true</declaration>"
                + "<dtsOut>${project.build.directory}/resources/src/typings/${project.artifactId}/${project.version}</dtsOut>"
                + "</configuration>"));

        Build b = new Build();
        b.addPlugin(plugin);
        model.setBuild(b);
    }

    /**
     * T43: the classes of the external templates are written under {@code src/external/java}; the module compiles them with its own,
     * as it did when they were all in {@code src/main/java}. An application that does not want them leaves that root out, and reads
     * {@code external-packages.txt} to keep them out of the jar it builds: here the backend compiles against this module's jar, so
     * this module's jar keeps them.
     */
    public void addExternalSources(Model model) {
        Build b = model.getBuild();
        if (b == null) {
            b = new Build();
            model.setBuild(b);
        }
        Plugin plugin = null;
        for (Plugin p : b.getPlugins()) {
            if ("build-helper-maven-plugin".equals(p.getArtifactId())) plugin = p;
        }
        if (plugin == null) {
            plugin = new Plugin();
            plugin.setGroupId("org.codehaus.mojo");
            plugin.setArtifactId("build-helper-maven-plugin");
            plugin.setVersion("3.0.0");
            b.addPlugin(plugin);
        }
        Xpp3Dom configuration = new Xpp3Dom("configuration");
        Xpp3Dom sources = new Xpp3Dom("sources");
        Xpp3Dom source = new Xpp3Dom("source");
        source.setValue("${project.basedir}/src/external/java");
        sources.addChild(source);
        configuration.addChild(sources);
        PluginExecution pe = new PluginExecution();
        pe.setId("add-external-sources");
        pe.setPhase("generate-sources");
        pe.addGoal("add-source");
        pe.setConfiguration(configuration);
        plugin.addExecution(pe);
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





    public SpecificationFile generateTestFile_l2p(TemplatesProjectConfiguration configs, Locations locations, String directory, String fileName) {
        StackTraceElement stackTraceElement = compilerUtil.thisMethodAndLine();

        Class pastClass = pastFactory.CLASS(TESTER_FILE)
                .MODIFIERS(Modifier.PUBLIC)
                .SUPERCLASS(TEST_CASE);

        Method method = METHOD("testMain")
                .commentFileLocation()
                .MODIFIERS(Modifier.PUBLIC)
                .THROWS(PAST_EXCEPTION)
                .RETURNS(VOID);

        // ProvFactory pf = InteropFramework.getDefaultFactory()
        method.BODY(
                DEFINITION(PROV_FACTORY, VARIABLE("pf"),
                        METHOD_CALL(INTEROP_FRAMEWORK, "getDefaultFactory", List.of())));

        for (TemplateCompilerConfig template: configs.templates) {
            if (TemplateConfigurationEnum.isClientSide(template)) continue; // T36 (R11): a subtype adds nothing to the server side
            String bn=compilerUtil.templateNameClass(template.name);
            ClassName templateClass = ClassName.get(bn, template.package_);

            // System.setOut(new java.io.PrintStream("target/" + template.name + ".provn"))
            Expression fileNameExpr = BINARY_OP(
                    BINARY_OP(CONSTANT("target/"), "+", CONSTANT(template.name)),
                    "+",
                    CONSTANT(".provn"));
            method.BODY(
                    METHOD_CALL(SYSTEM, "setOut",
                            CONSTRUCTOR_CALL(PRINT_STREAM, List.of(fileNameExpr))));

            // Bn.main(null)
            method.BODY(
                    METHOD_CALL(templateClass, "main", List.of(getNull())));
        }

        pastClass.METHOD(method);

        String myPackage = configs.root_package;

        Supplier<Boolean> pythonGenerator = () -> generatePython(pastClass, myPackage, locations, stackTraceElement);
        Supplier<Boolean> javaGenerator = () -> generateJava(pastClass, myPackage, configs, directory, stackTraceElement, compilerUtil);

        return new SpecificationFile(javaGenerator, pythonGenerator);
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