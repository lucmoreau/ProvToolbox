// Generated Automatically by ProvToolbox (org.openprovenance.prov.template.compiler.CompilerClientTest) for templates config templates
package org.example.templates;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.lang.Exception;
import junit.framework.TestCase;
import org.example.templates.block.client.Template_blockBuilder;

public class ExampleTest extends TestCase {
  public void testMain() throws Exception {
    System.setOut(new java.io.PrintStream("target/example_template_block.json"));
    Object res=Template_blockBuilder.examplar();
    new ObjectMapper().writeValue(System.out,res);
  }
}
