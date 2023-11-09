package org.openprovenance.prov.template.emitter.minilanguage.emitters;

import java.util.LinkedList;
import java.util.List;

public class Python {

    private List<String> imports=new LinkedList<>();

    public int indent;
    public void indent() {
        indent++;
    }

    public void unindent() {
        indent--;
    }

    final private StringBuffer sb;

    public Python() {
        sb=new StringBuffer();
        indent=0;
    }

    public Python(StringBuffer sb, int indent) {
        this.sb=sb;
        this.indent=indent;
    }

    public void emitNewline() {
        sb.append("\n");
    }
    public void emitLine(String s) {
        for (int i=0; i<indent; i++) {
            sb.append("    ");
        }
        sb.append(s);
        emitNewline();
    }
    public void emitBeginLine(String s) {
        for (int i=0; i<indent; i++) {
            sb.append("    ");
        }
        sb.append(s);
    }

    public void emitLine(String s, boolean continueLine) {
        if (continueLine) {
            emitContinueLine(s);
        } else {
            emitBeginLine(s);
        }
    }
    public void emitContinueLine(String s) {
        sb.append(s);
    }

    public StringBuffer getSb() {
        return sb;
    }

    public List<String> getImports() {
        return imports;
    }
}
