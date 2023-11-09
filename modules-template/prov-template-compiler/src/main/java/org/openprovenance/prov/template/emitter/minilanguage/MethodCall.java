package org.openprovenance.prov.template.emitter.minilanguage;

import org.openprovenance.prov.template.emitter.Element;
import org.openprovenance.prov.template.emitter.minilanguage.emitters.Python;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MethodCall extends Expression {
    public Expression object;
    final String methodName;
    private final List<?> arguments;

    public MethodCall(Object object, String methodName, List<?> arguments, List<Element> elements) {
        super(elements);
        this.methodName=methodName;
        this.arguments=arguments;
        if (object==null) {
            this.object=null;
        } else if (object instanceof String) {
            this.object=new Symbol((String)object, elements);
        } else if (object instanceof com.squareup.javapoet.ClassName) { // type constructor
            this.object=new Symbol(object.toString(), elements);
        } else if (object instanceof MethodCall) {
            this.object=(MethodCall)object;
        }
        else {
            System.out.println("#### object " + object + "  " + object.getClass());
            throw new UnsupportedOperationException("Cannot handle " + object.getClass());
        }
    }

    @Override
    public String toString() {
        return "MethodCall{" +
                "object=" + object +
                ", methodName=" + methodName +
                ", arguments=" + arguments +
                '}';
    }


    public void emit(Python emitter, boolean continueLine, List<String> classVariables, List<String> instanceVariables) {
        if ("put".equals(methodName) && arguments!=null && arguments.size() == 2) {
            object.emit(emitter, continueLine, classVariables, instanceVariables);
            emitter.emitContinueLine("[");
            ((Expression)arguments.get(0)).emit(emitter, true, classVariables, instanceVariables);
            emitter.emitContinueLine("]");
            emitter.emitContinueLine("=");
            ((Expression)arguments.get(1)).emit(emitter, true, classVariables, instanceVariables);
            emitter.emitNewline();
        } else if ("get".equals(methodName) && arguments!=null && arguments.size() == 1) {
            object.emit(emitter, continueLine, classVariables, instanceVariables);
            emitter.emitContinueLine("[");
            ((Expression)arguments.get(0)).emit(emitter, true, classVariables, instanceVariables);
            emitter.emitContinueLine("]");
        } else {
            if (object != null) {
                if (object instanceof Symbol && ((Symbol) object).symbol.equals("sb") && methodName.equals("toString")) {
                    emitter.emitLine("''.join(sb)", continueLine);
                    return;
                }
                object.emit(emitter, continueLine, classVariables, instanceVariables);
            }
            if (!methodName.equals("process")) { // this is lambda, and in java we need to call method
                if (object != null) {
                    emitter.emitContinueLine(".");
                }
                if (methodName.equals("toBean")) emitter.emitContinueLine("cls."); // FIXME: not systematic
                emitter.emitContinueLine(methodName);
            }
            if (arguments==null) {
                if (!continueLine) {
                    emitter.emitNewline();
                }
                return;
            }
            emitter.emitContinueLine("(");
            // concat all arguments, with a comma separator
            boolean first = true;
            for (Object argument : arguments) {
                if (first) {
                    first = false;
                } else {
                    emitter.emitContinueLine(",");
                }
                if (argument instanceof Expression) {
                    ((Expression) argument).emit(emitter, true, classVariables, instanceVariables);
                } else {
                    emitter.emitContinueLine(localized(argument.toString(), classVariables, instanceVariables));
                }
            }
            emitter.emitContinueLine(")");
            if (!continueLine) {
                emitter.emitNewline();
            }
        }
    }

    static public String convertName(String string, List<String> classVariables, List<String> instanceVariables) {
        if (table.containsKey(string)) {
            return table.get(string);
        } else {
            return localized(string, classVariables, instanceVariables);
        }
    }

    static Map<String,String> table=new HashMap<>() {{
        put("org.openprovenance.apache.commons.lang.StringEscapeUtils","StringEscapeUtils");

    }};
}
