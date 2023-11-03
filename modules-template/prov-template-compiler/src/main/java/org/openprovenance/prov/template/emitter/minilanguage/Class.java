package org.openprovenance.prov.template.emitter.minilanguage;

import org.openprovenance.prov.template.emitter.minilanguage.emitters.Python;

import java.util.LinkedList;
import java.util.List;

public class Class {
    final private String name;
    final private List<Comment> comments=new LinkedList<>();
    final private List<Field> fields=new LinkedList<>();
    final private List<Method> methods=new LinkedList<>();
    private final List<String> interfaces=new LinkedList<>();

    public Class(String name,  List<String> interfaces, List<Field> fields,  List<Method> methods,  List<Comment> comments) {
        this.name = name;
        this.interfaces.addAll(interfaces);
        this.fields.addAll(fields);
        this.methods.addAll(methods);
        this.comments.addAll(comments);
    }


    public void emit(Python emitter) {

        //emitter.emitLine("@dataclass");
        emitter.emitBeginLine("class " + name + ":");
        emitter.emitNewline();
        emitter.indent();
        boolean noComment=true;
        for (Comment c: comments) {
            if (c!=null && c.comment!=null && !c.comment.trim().isEmpty()) {
                emitter.emitLine("\"\"\"" + c.comment + "\"\"\"");
                noComment=false;
            }
        }
        if (!noComment) {
            emitter.emitNewline();
        }

        for (Field f: fields) {
            f.emit(emitter);
        }
        for (Method m: methods) {
            m.emit(emitter);
        }
    }



}
