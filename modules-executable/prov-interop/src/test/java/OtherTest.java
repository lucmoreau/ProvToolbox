import junit.framework.TestCase;
import org.openprovenance.prov.interop.CommandLineArguments;

public class OtherTest extends TestCase {
    private String url = "http://localhost:8080";

    public OtherTest(String testName) {
        super(testName);
    }

    String[] args = {
            "-infile", "src/main/resources/templates/org/openprovenance/templates/triangles/usage-generation-derivation.jsonld",
            "-bindings", "src/main/resources/bindings/org/openprovenance/bindings/triangles/integer-division.json",
            "-outfile", "target/output--integer-division.jsonld"
    };

    String[] args1 = {
            "-infile", "/Users/luc/git-papers/papers/book-ptm/project/template-intro1/src/main/resources/templates/org/openprovenance/templates/triangles/usage-generation-derivation.jsonld",
            "-bindings", "/Users/luc/git-papers/papers/book-ptm/project/template-intro1/src/main/resources/bindings/org/openprovenance/bindings/examples/division-121-4.json",
            "-outfile", "target/output--division-121-4.jsonld"
    };


    public void testHelp() throws Exception {
        //CommandLineArguments.main(new String[]{"-help"});
        //CommandLineArguments.mainNoExit(args);
        CommandLineArguments.mainNoExit(args1);
    }

}
