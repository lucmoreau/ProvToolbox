
import java.util.List;
import java.util.Arrays;
import java.io.File;
import java.io.PrintStream;
import java.io.FileOutputStream;
import org.example.templates.client.Logger;

public class Example {
    public static void main(String[] args) throws Exception {
        final Logger logger = new Logger();

        List<String> log=Arrays.asList(
                logger.logTemplate_block("operation/01", "Operation1", "Luc",
                        "entity/e1", "value1", "entity/e2", 2,
                        "entity/e3", "Type2", 3),
                logger.logTemplate_block("operation/02", "Operation1", "Luc",
                        "entity/e4", "value4", "entity/e3", 3,
                        "entity/e5", "Type5", 5),
                logger.logTemplate_block("operation/03", "Operation1", "Luc",
                        "entity/e6", "value6", "entity/e5", 5,
                        "entity/e7", "Type7", 7)
        );

        PrintStream ps=new PrintStream(new FileOutputStream(new File(args[0])));

        log.stream().forEach(s -> ps.println(s));
    }
}