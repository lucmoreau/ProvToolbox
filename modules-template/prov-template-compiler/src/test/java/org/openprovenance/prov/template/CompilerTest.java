package org.openprovenance.prov.template;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.ValidationMessage;
import junit.framework.TestCase;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.model.QualifiedName;
import org.openprovenance.prov.notation.Utility;
import org.openprovenance.prov.template.compiler.ConfigProcessor;
import org.openprovenance.prov.template.compiler.TemplateCompilerConfig;
import org.openprovenance.prov.template.compiler.TemplatesCompilerConfig;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Set;

import static org.openprovenance.prov.template.expander.ExpandUtil.VAR_NS;

public class CompilerTest extends TestCase {

    static final String EX_NS = "http://example.org/";

    public CompilerTest(String testName) {
	super(testName);
    }
    
    ProvFactory pf=new org.openprovenance.prov.vanilla.ProvFactory();
    QualifiedName var_a=pf.newQualifiedName(VAR_NS, "a", "var");
    QualifiedName var_b=pf.newQualifiedName(VAR_NS, "b", "var");
    QualifiedName var_c=pf.newQualifiedName(VAR_NS, "c", "var");
    QualifiedName var_d=pf.newQualifiedName(VAR_NS, "d", "var");

    ObjectMapper objectMapper = new ObjectMapper();


    public void testCompiler1() throws IOException {

        testCompilerWithSingleTemplate("src/test/resources/config1.json");
    }

    public void testCompilerWithSingleTemplate(String path) throws IOException {
        System.out.println("Compiling " + path);
        Utility u=new Utility();


        ConfigProcessor cp=new ConfigProcessor(pf);
        // We cannot run the following because it requires prov-interop, which is not available yet
        //cp.processTemplateGenerationConfig("src/test/resources/config1.json", pf);


        TemplatesCompilerConfig configs = objectMapper.readValue(new File(path), TemplatesCompilerConfig.class);
        TemplateCompilerConfig config = configs.templates[0];
        System.out.println(configs);
        Document doc = u.readDocument(config.template, pf);


        final String root_dir = configs.destination + "/" + configs.name;
        new File(root_dir).mkdirs();
        final String cli_lib = configs.name + "_cli";
        final String cli_dir = root_dir + "/" + cli_lib;
        new File(cli_dir).mkdirs();
        final String l2p_lib = configs.name + "_l2p";
        final String l2p_dir = root_dir + "/" + l2p_lib;
        new File(l2p_dir).mkdirs();


        final String l2p_src_dir=l2p_dir+"/src/main/java";
        final String l2p_test_src_dir=l2p_dir+"/src/test/java";
        final String cli_src_dir=cli_dir+"/src/main/java";
        final String cli_test_src_dir=cli_dir+"/src/test/java";

        new File(l2p_src_dir).mkdirs();
        new File(cli_src_dir).mkdirs();
        new File(l2p_test_src_dir).mkdirs();
        new File(cli_test_src_dir).mkdirs();

        cp.doGenerateServerForEntry1(doc,config,configs,cli_src_dir,  l2p_src_dir);
        cp.doGenerateProject(configs,root_dir,cli_lib,l2p_lib,l2p_dir,l2p_src_dir,l2p_test_src_dir,cli_test_src_dir);
        cp.doGenerateClientAndProject(configs,cli_lib,cli_dir,cli_src_dir);
        cp.generateJSonSchemaEnd(configs,cli_src_dir);

        System.out.println("#### Invoking maven " + path);
        execute(new String[] {"/usr/local/bin/mvn", "clean", "install"},"target/libs/templates");

        final String theExamplarJsonFile = cli_dir + "/target/example_template_block.json";
        final String templateJsonSchema  = cli_dir + "/src/main/resources/" + configs.jsonschema;

        System.out.println("#### jq test " + theExamplarJsonFile);
        execute(new String[] {"/usr/local/bin/jq", ".", theExamplarJsonFile},".");

        execute(new String[] {"/usr/local/bin/ajv", "-s", templateJsonSchema, "-d", theExamplarJsonFile},".");

        com.networknt.schema.JsonSchema schema=cp.getCompilerJsonSchema().setupJsonSchemaFromClasspathV7("schema/json-schema-v7.json");
        Set<ValidationMessage> result=cp.getCompilerJsonSchema().checkSchema(schema, templateJsonSchema);
        System.out.println(result);

        System.out.println("#### checking schema compatibility for " + theExamplarJsonFile);
        com.networknt.schema.JsonSchema schema2=cp.getCompilerJsonSchema().setupJsonSchemaFromFile( templateJsonSchema);
        Set<ValidationMessage> result2=cp.getCompilerJsonSchema().checkSchema(schema2, theExamplarJsonFile);
        System.out.println(result2);


    }

    public static void execute(String [] command, String where) {

        try {
            Runtime rt = Runtime.getRuntime();
            String home=System.getProperty("java.home");
            String path=System.getenv("PATH");
            Process pr = rt.exec(command, new String[] {"JAVA_HOME=" + home, "PATH="+path}, new File(where));
            BufferedReader input = new BufferedReader(new InputStreamReader(pr.getInputStream()));
            String line=null;
            while((line=input.readLine()) != null) {
                System.out.println(line);
            }

            int exitVal = pr.waitFor();
            System.out.println("Exited with error code "+exitVal);

        } catch(Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }


}
