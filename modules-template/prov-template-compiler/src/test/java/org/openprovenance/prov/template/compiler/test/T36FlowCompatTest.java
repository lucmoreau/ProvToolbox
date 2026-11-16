package org.openprovenance.prov.template.compiler.test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * T41 (design §14.3): compatibility. The declaration of T36 stage 9 ({@link T36FlowFixture#FLOW}) compiles to the same classes after the flow was
 * extended: its generated Java, and the Python and JavaScript the emitters write, are equal to what stage 9 wrote, kept in
 * {@code src/test/resources/t36/flow/golden/} (without the lines that name the generator's own methods and line numbers). Regenerate them,
 * on purpose, with {@code -Dt41.writeGoldens=true}.
 */
public class T36FlowCompatTest extends T36FlowFixture {

    static final String GOLD = "src/test/resources/t36/flow/golden/";

    /** What a flow says of itself, not the generator's own bookkeeping: its method names and line numbers. */
    static String normalise(String text) {
        return Arrays.stream(text.split("\n"))
                .filter(l -> !l.contains("Generated automatically") && !l.contains("by class org.openprovenance") && !l.contains("in file Compiler")
                        && !l.contains("PAST generator method") && !l.contains("Created by class"))
                .collect(Collectors.joining("\n")).replaceAll("at line [0-9]+", "at line N");
    }

    void check(String name, String actual) throws Exception {
        Path golden = Path.of(GOLD + name);
        if (Boolean.getBoolean("t41.writeGoldens")) {
            Files.createDirectories(golden.getParent());
            Files.writeString(golden, normalise(actual) + "\n");
            return;
        }
        assertEquals("the declaration of stage 9 compiles to the same " + name, Files.readString(golden).trim(), normalise(actual).trim());
    }

    public void testTheDeclarationOfStage9CompilesToTheSameClasses() throws Exception {
        generate("compat", List.of(LINE_CHANGE, REPRICE, FLOW));
        check("RepriceFlow.java", read("compat", "java/org/example/t36flow/flows/RepriceFlow.java"));
        check("RepriceFlowRules.java", read("compat", "java/org/example/t36flow/flows/RepriceFlowRules.java"));
        check("RepriceFlow.py", emitted("python", "RepriceFlow"));
        check("RepriceFlowRules.py", emitted("python", "RepriceFlowRules"));
        check("RepriceFlow.js", emitted("javascript", "RepriceFlow"));
        check("RepriceFlowRules.js", emitted("javascript", "RepriceFlowRules"));
    }

    String emitted(String language, String name) {
        for (org.openprovenance.prov.template.compiler.past.Class c : lastProcessor.getCompilerFlow().getGeneratedClasses()) {
            if (c.name.equals(name)) {
                return language.equals("python")
                        ? new org.openprovenance.prov.template.compiler.past.emitter.Python(null).emit(c).toString()
                        : new org.openprovenance.prov.template.compiler.past.emitter.JavaScript().emit(c).toString();
            }
        }
        throw new IllegalStateException("no generated class " + name);
    }
}
