package org.openprovenance.prov.template.emitter.minilanguage;

import org.openprovenance.prov.template.emitter.minilanguage.emitters.Python;

import java.util.LinkedList;
import java.util.List;

public class Field {

    final String name;
    final private List<String> attributes=new LinkedList<>();
    final private String type;
    final private Object initialiser;
    final private List<Comment> comments=new LinkedList<>();


    public Field(String name, String type, List<String> attributes, Object initialiser, List<Comment> comments) {
        this.name = name;
        this.type = type;
        this.attributes.addAll(attributes);
        this.initialiser = initialiser;
        this.comments.addAll(comments);
    }

    public void emit(Python emitter) {
        emit(emitter, false);
    }
    public void emit(Python emitter, boolean continueLine) {
        for (Comment c: comments) {
            if (c.comment!=null && !c.comment.trim().isEmpty()) {
                emitter.emitLine("#" + c.comment, continueLine);
                emitter.emitNewline();
            }
        }

        emitter.emitBeginLine(name);

        if (type != null && !type.trim().isEmpty()) {
            emitter.emitContinueLine(" : " + type);
        }
        if (initialiser != null && !initialiser.toString().trim().isEmpty()) {
            emitter.emitContinueLine(" = " + initialiser.toString().replace("\"", "'"));
        }
        emitter.emitNewline();
        emitter.emitNewline();
    }
}
