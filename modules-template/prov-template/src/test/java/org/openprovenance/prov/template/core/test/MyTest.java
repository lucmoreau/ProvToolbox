package org.openprovenance.prov.template.core.test;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;
import junit.framework.TestCase;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;



import java.util.List;

import com.flipkart.zjsonpatch.Jackson3JsonDiff; // zjsonpatch 0.6: Jackson 3 API
import org.openprovenance.prov.template.core.Bindings;

public class MyTest extends TestCase {

    /*public void testMethod1() throws IOException {
        ObjectMapper om=tools.jackson.databind.json.JsonMapper.builderWithJackson2Defaults().build();
        TestBean tb=new TestBean();
        tb.var=new Hashtable<>();
        Descriptors ds=new Descriptors();
        ds.values=new LinkedList<>();
        SingleDescriptor sd = new SingleDescriptor();
        sd.id="ex:luc";
        ds.values.add(sd);
        tb.var.put("author", ds);
        om.writeValue(System.out,tb);
        assertTrue(true);

    }*/

    public void testMethod2() throws IOException {
        ObjectMapper om=JsonMapper.builderWithJackson2Defaults().build();




        readAndSaveAndCompare(om, "src/test/resources/bindings/bindings101.json", "target/tbindings101.json");
        readAndSaveAndCompare(om, "src/test/resources/bindings/bindings20.json", "target/tbindings20.json");


    }

    private void readAndSaveAndCompare(ObjectMapper om, String inputFile, String outputFile) throws IOException {
        Bindings bindings = om.readValue(new File(inputFile), Bindings.class);
        System.out.println("testBean = " + bindings);
        assertNotNull("testBean is null", bindings);
        om.writeValue(new FileOutputStream(outputFile), bindings);

        // perform a json diff on the two files, and assert that there is no difference
        List<?> diff=differenceBetweenJsonFiles(om, inputFile, outputFile);
        System.out.println("Total differences " + diff.size());
        diff.forEach(p -> System.out.println(" - patch is " + p));
        assertEquals(0, diff.size());
    }

    static List<?> differenceBetweenJsonFiles(ObjectMapper om, String structureIn, String structureOut) throws IOException {
        JsonNode beforeNode = om.readTree(Files.newInputStream(Paths.get(structureIn)));
        JsonNode afterNode = om.readTree(Files.newInputStream(Paths.get(structureOut)));
        JsonNode patchNode = Jackson3JsonDiff.asJson(beforeNode, afterNode);
        String diff = patchNode.toString();

        return om.readValue(diff, List.class);
    }
}
