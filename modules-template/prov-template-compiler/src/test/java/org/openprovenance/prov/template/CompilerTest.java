package org.openprovenance.prov.template;

import com.fasterxml.jackson.databind.ObjectMapper;
import junit.framework.TestCase;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.model.QualifiedName;
import org.openprovenance.prov.notation.Utility;
import org.openprovenance.prov.template.compiler.ConfigProcessor;
import org.openprovenance.prov.template.compiler.TemplateCompilerConfig;
import org.openprovenance.prov.template.compiler.TemplatesCompilerConfig;

import java.io.File;

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





    public void testCompiler1() throws Throwable {
        ObjectMapper objectMapper = new ObjectMapper();
        Utility u=new Utility();

        // This is not working, as it rquires prov-interop in the classpath
        // ConfigProcessor.processTemplateGenerationConfig("src/test/resources/config1.json",pf);


        ConfigProcessor cp=new ConfigProcessor(pf);
        TemplatesCompilerConfig configs = objectMapper.readValue(new File("src/test/resources/config1.json"), TemplatesCompilerConfig.class);
        TemplateCompilerConfig config=configs.templates[0];

        System.out.println(configs);


        Document doc=u.readDocument(config.template, pf);

        boolean result=cp.generate(doc,config.name, config.package_, "target/libs/templates/templates_cli/src/main/java", "target/libs/templates/templates_l2p/src/main/java", "resource", objectMapper.readTree(new File(config.bindings)));

    }


}
