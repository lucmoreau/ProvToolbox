package org.openprovenance.prov.template.emitter.minilanguage;

import org.openprovenance.prov.template.emitter.Element;
import org.openprovenance.prov.template.emitter.minilanguage.emitters.Python;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MethodCall extends Expression {
    private final Object object;
    private final String methodName;
    private final List<?> arguments;

    public MethodCall(Object object, String methodName, List<?> arguments, List<Element> elements) {
        super(elements);
        this.object=object;
        this.methodName=methodName;
        this.arguments=arguments;
    }

    @Override
    public String toString() {
        return "MethodCall{" +
                "object=" + object +
                ", methodName=" + methodName +
                ", arguments=" + arguments +
                '}';
    }

    public void emit(Python emitter, boolean continueLine, List<String> locals) {
        if (object.equals("sb") && methodName.equals("toString")) {
            emitter.emitLine("''.join(sb)", continueLine);
            return;
        }
        emitter.emitLine(convertName(object.toString(), locals),continueLine);
        if (!methodName.equals("process")) { // this is lambda, and in java we need to call method
            emitter.emitContinueLine(".");
            emitter.emitContinueLine(methodName);
        }
        emitter.emitContinueLine("(");
        // concat all arguments, with a comma separator
        boolean first=true;
        for (Object argument : arguments) {
            if (first) {
                first=false;
            } else {
                emitter.emitContinueLine(",");
            }
            if (argument instanceof Expression) {
                ((Expression) argument).emit(emitter, true, locals);
            } else {
                emitter.emitContinueLine(localized(argument.toString(),locals));

            }
        }
        emitter.emitContinueLine(")");
        if (!continueLine) {
            emitter.emitNewline();
        }
    }

    static public String convertName(String string, List<String> locals) {
        if (table.containsKey(string)) {
            return table.get(string);
        } else if (string.equals("self") || locals.contains(string)) {
            return string;
        } else {
            return "self." + string;
        }
    }

    static Map<String,String> table=new HashMap<>() {{
        put("org.openprovenance.apache.commons.lang.StringEscapeUtils","StringEscapeUtils");

    }};
}
