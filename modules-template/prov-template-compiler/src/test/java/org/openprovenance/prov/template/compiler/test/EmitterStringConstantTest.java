package org.openprovenance.prov.template.compiler.test;

import junit.framework.TestCase;
import org.openprovenance.prov.template.compiler.past.Class;
import org.openprovenance.prov.template.compiler.past.Method;
import org.openprovenance.prov.template.compiler.past.PastFactory;
import org.openprovenance.prov.template.compiler.past.emitter.JavaScript;
import org.openprovenance.prov.template.compiler.past.emitter.Python;

import javax.lang.model.element.Modifier;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.openprovenance.prov.template.compiler.past.Constant.CONSTANT;
import static org.openprovenance.prov.template.compiler.past.Method.METHOD;
import static org.openprovenance.prov.template.compiler.past.MethodCall.METHOD_CALL;
import static org.openprovenance.prov.template.compiler.past.Variable.VARIABLE;
import static org.openprovenance.prov.template.compiler.past.type.ClassName.STRING_BUILDER;

/**
 * String constants must be emitted as single, valid literals in the generated Python and JavaScript.
 * <p>
 * Regression: the Python emitter wrote constants verbatim between quotes, so the query invokers' SQL fragments
 * containing a newline (e.g. {@code "WITH \n    insertion_result AS (  "}) produced an unterminated string literal,
 * and a constant containing both quote kinds or a backslash was broken or changed. The JavaScript emitter only
 * escaped double quotes.
 */
public class EmitterStringConstantTest extends TestCase {

    static final List<String> VALUES = List.of(
            "WITH \n    insertion_result AS (  ",
            ";\n",
            "it's \"quoted\"",
            "back\\slash \\n not a newline",
            "tab\there\r\n");

    private Class pastClass() {
        Class c = new PastFactory().CLASS("Constants").MODIFIERS(Modifier.PUBLIC);
        Method m = METHOD("fill").MODIFIERS(Modifier.PUBLIC).PARAMETER(STRING_BUILDER, "sb");
        for (String v : VALUES) {
            m.BODY(METHOD_CALL(VARIABLE("sb"), "append", List.of(CONSTANT(v))));
        }
        c.METHOD(m);
        return c;
    }

    public void testPythonConstantsAreEscaped() throws Exception {
        String py = new Python(null).emit(pastClass()).toString();
        assertTrue(py, py.contains("sb.append(\"WITH \\n    insertion_result AS (  \")"));
        assertTrue(py, py.contains("sb.append(\";\\n\")"));
        assertTrue(py, py.contains("sb.append(\"it's \\\"quoted\\\"\")"));
        assertTrue(py, py.contains("sb.append(\"back\\\\slash \\\\n not a newline\")"));
        assertTrue(py, py.contains("sb.append(\"tab\\there\\r\\n\")"));
        assertParses(py, ".py", "python3", "-c",
                "import ast,sys; ast.parse(open(sys.argv[1]).read())");
    }

    public void testJavaScriptConstantsAreEscaped() throws Exception {
        String js = new JavaScript().emit(pastClass()).toString();
        assertTrue(js, js.contains("\"WITH \\n    insertion_result AS (  \""));
        assertTrue(js, js.contains("\"it's \\\"quoted\\\"\""));
        assertTrue(js, js.contains("\"back\\\\slash \\\\n not a newline\""));
        assertParses(js, ".mjs", "node", "--check");
    }

    /** Parses the emitted code with the real interpreter, when it is installed. */
    private static void assertParses(String code, String suffix, String... command) throws IOException, InterruptedException {
        if (!onPath(command[0])) {
            System.out.println("EmitterStringConstantTest: " + command[0] + " not found, syntax check skipped");
            return;
        }
        Path f = Files.createTempFile("emitted", suffix);
        Files.writeString(f, code);
        String[] full = new String[command.length + 1];
        System.arraycopy(command, 0, full, 0, command.length);
        full[command.length] = f.toString();
        Process p = new ProcessBuilder(full).redirectErrorStream(true).start();
        String out = new String(p.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        assertEquals(code + "\n" + out, 0, p.waitFor());
    }

    private static boolean onPath(String exe) {
        String path = System.getenv("PATH");
        if (path == null) return false;
        for (String dir : path.split(File.pathSeparator)) {
            if (new File(dir, exe).canExecute()) return true;
        }
        return false;
    }
}
