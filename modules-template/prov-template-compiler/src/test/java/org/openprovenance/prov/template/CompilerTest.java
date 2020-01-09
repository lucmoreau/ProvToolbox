package org.openprovenance.prov.template;

import junit.framework.TestCase;
import org.openprovenance.prov.model.*;
import org.openprovenance.prov.notation.Utility;
import org.openprovenance.prov.template.compiler.ConfigProcessor;
import org.openprovenance.prov.template.compiler.TemplateCompiler;
import org.openprovenance.prov.template.expander.Bindings;
import org.openprovenance.prov.template.expander.BindingsJson;
import org.openprovenance.prov.template.expander.BindingsJson.BindingsBean;
import org.openprovenance.prov.template.expander.Groupings;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import static org.openprovenance.prov.template.expander.ExpandUtil.TMPL_NS;
import static org.openprovenance.prov.template.expander.ExpandUtil.VAR_NS;

public class CompilerTest extends TestCase {

    static final String EX_NS = "http://example.org/";

    public CompilerTest(String testName) {
	super(testName);
    }
    
    ProvFactory pf=new org.openprovenance.prov.xml.ProvFactory();
    QualifiedName var_a=pf.newQualifiedName(VAR_NS, "a", "var");
    QualifiedName var_b=pf.newQualifiedName(VAR_NS, "b", "var");
    QualifiedName var_c=pf.newQualifiedName(VAR_NS, "c", "var");
    QualifiedName var_d=pf.newQualifiedName(VAR_NS, "d", "var");
 
    



    public void testCompiler1() {

        Utility u=new Utility();

        ConfigProcessor.processTemplateGenerationConfig("src/test/resources/config1.json",pf);
        
        
    }


}
