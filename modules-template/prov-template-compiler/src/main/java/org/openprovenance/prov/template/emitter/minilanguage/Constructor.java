package org.openprovenance.prov.template.emitter.minilanguage;

import org.openprovenance.prov.template.emitter.Element;
import org.openprovenance.prov.template.emitter.minilanguage.emitters.Python;

import java.util.LinkedList;
import java.util.List;

import static org.openprovenance.prov.template.compiler.common.CompilerCommon.MARKER_ARRAY;

public class Constructor extends Expression {
    private final String type;
    private final List<Expression> array;

    public Constructor(String type, List<Element> elements) {
        super(elements);
        this.type = type;
        this.array=null;
    }
    public Constructor(String type, List<Expression> array, List<Element> elements) {
        super(elements);
        this.type = type;
        this.array=array;
    }

    @Override
    public String toString() {
        return "Constructor{" +
                "type='" + type + '\'' +
                '}';
    }

    public void emit(Python emitter, boolean continueLine, List<String> locals) {
        if (type.equals("java.lang.StringBuffer")) {
            emitter.emitLine( "[]",continueLine);
        } else if (type.startsWith("java.util.HashMap<")) {
            emitter.emitLine( "{}",continueLine);
        } else {
            List<String> imports=new LinkedList<>();
            if (type.contains(".")) {

                if (array!=null) {
                    emitter.emitContinueLine("[");
                    boolean first=true;
                    for (Expression e: array) {
                        if (!first) {
                            emitter.emitContinueLine(", ");
                        } else {
                            first=false;
                        }
                        e.emit(emitter, true, locals);
                    }
                    emitter.emitContinueLine("]");
                } else {
                    String[] parts=type.split("\\.");
                    String localType = parts[parts.length - 1];
                    imports.add("from " + type + " import " + localType);
                    emitter.emitLine(localType, continueLine);
                    emitter.emitContinueLine("()");
                }
                System.out.println(imports);
            } else {
                emitter.emitLine(type+"()", continueLine);
            }


        }
    }
}
