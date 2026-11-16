package org.openprovenance.prov.template.compiler.test;

import junit.framework.TestCase;
import org.openprovenance.prov.model.Attribute;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.model.QualifiedName;
import org.openprovenance.prov.template.compiler.expansion.StatementCompilerAction2;
import org.openprovenance.prov.template.compiler.past.Class;
import org.openprovenance.prov.template.compiler.past.Method;
import org.openprovenance.prov.template.compiler.past.PastFactory;
import org.openprovenance.prov.template.compiler.past.Statement;
import org.openprovenance.prov.template.compiler.past.emitter.JavaScript;
import org.openprovenance.prov.template.core.InstantiateUtil;

import javax.lang.model.element.Modifier;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

import static org.openprovenance.prov.template.compiler.past.Method.METHOD;

/**
 * A template's {@code tmpl:time} on a usage, generation or invalidation must reach the generated builder.
 * <p>
 * Regression: {@link StatementCompilerAction2} passed a literal {@code null} as the time of
 * {@code newUsed}, {@code newWasGeneratedBy} and {@code newWasInvalidatedBy}, so a builder's
 * {@code make(...)} received the record's time and dropped it: the book's Weighing and HandingOver
 * records were served without their generation time.
 */
public class TemplateTimeCompilationTest extends TestCase {

    static final String TIME_ARG = "pf.newISOTime(time.toString())";

    private final ProvFactory pf = org.openprovenance.prov.vanilla.ProvFactory.getFactory();

    private QualifiedName var(String local) {
        return pf.newQualifiedName(InstantiateUtil.VAR_NS, local, "var");
    }

    private List<Attribute> timeAttribute() {
        List<Attribute> attrs = new LinkedList<>();
        attrs.add(pf.newAttribute(
                pf.newQualifiedName(InstantiateUtil.TMPL_NS, InstantiateUtil.TIME, InstantiateUtil.TMPL_PREFIX),
                var("time"),
                pf.getName().PROV_QUALIFIED_NAME));
        return attrs;
    }

    /** Compiles statements with {@link StatementCompilerAction2} and emits them as JavaScript. */
    private String compile(java.util.function.Consumer<StatementCompilerAction2> doAction) {
        List<Statement> statements = new ArrayList<>();
        StatementCompilerAction2 action = new StatementCompilerAction2(pf, List.of(), List.of(),
                new Hashtable<>(), statements, "__C_document.getStatementOrBundle()", null);
        doAction.accept(action);
        Method m = METHOD("generator").MODIFIERS(Modifier.PUBLIC);
        for (Statement s : statements) {
            m.BODY(s);
        }
        Class c = new PastFactory().CLASS("Builder").MODIFIERS(Modifier.PUBLIC);
        c.METHOD(m);
        return new JavaScript().emit(c).toString();
    }

    /** The line of emitted code that calls {@code pf.<factoryMethod>(...)}. */
    private static String callTo(String code, String factoryMethod) {
        for (String line : code.split("\n")) {
            if (line.contains("pf." + factoryMethod + "(")) return line;
        }
        fail("no call to pf." + factoryMethod + " in:\n" + code);
        return null;
    }

    public void testGenerationTimeIsPassed() {
        String code = compile(a -> a.doAction(pf.newWasGeneratedBy(null, var("item1"), var("weighing"), null, timeAttribute())));
        assertTrue(code, callTo(code, "newWasGeneratedBy").contains(TIME_ARG));
    }

    public void testUsageTimeIsPassed() {
        String code = compile(a -> a.doAction(pf.newUsed(null, var("weighing"), var("item0"), null, timeAttribute())));
        assertTrue(code, callTo(code, "newUsed").contains(TIME_ARG));
    }

    public void testInvalidationTimeIsPassed() {
        String code = compile(a -> a.doAction(pf.newWasInvalidatedBy(null, var("item0"), var("weighing"), null, timeAttribute())));
        assertTrue(code, callTo(code, "newWasInvalidatedBy").contains(TIME_ARG));
    }

    /** Without tmpl:time, the time stays null. */
    public void testNoTimeWithoutTmplTime() {
        String code = compile(a -> a.doAction(pf.newWasGeneratedBy(null, var("item1"), var("weighing"), null, new LinkedList<>())));
        String call = callTo(code, "newWasGeneratedBy");
        assertFalse(code, call.contains("newISOTime"));
        assertTrue(code, call.contains("null"));
    }
}
