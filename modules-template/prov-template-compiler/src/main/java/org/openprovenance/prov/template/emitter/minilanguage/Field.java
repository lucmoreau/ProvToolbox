package org.openprovenance.prov.template.emitter.minilanguage;

import org.openprovenance.prov.template.emitter.minilanguage.emitters.Python;

import java.util.LinkedList;
import java.util.List;

public class Field {

    final String name;
    final private List<String> attributes=new LinkedList<>();
    final private String type;
    final private Expression initialiser;
    final private List<Comment> comments=new LinkedList<>();


    public Field(String name, String type, List<String> attributes, Expression initialiser, List<Comment> comments) {
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

        List<String> imports=new LinkedList<>();


        String mappedType = table.get(type);
        if (mappedType !=null) {
            emitter.emitContinueLine(" : " + mappedType);
        } else if (type != null && !type.trim().isEmpty()) {
            if (type.contains(".")) {
                String[] parts = type.split("\\.");
                String localType = parts[parts.length - 1];
                imports.add("from " + type + " import " + localType);
                emitter.emitContinueLine(" : " + convertType(localType));
                System.out.println(imports);
            } else {
                emitter.emitContinueLine(" : " + type);
            }
        }
        if (initialiser != null && !initialiser.toString().trim().isEmpty()) {
            emitter.emitContinueLine(" = "); // initialiser.toString().replace("\"", "'"));
            initialiser.emit(emitter, true, new LinkedList<>());  // no need to pass any class variables, given that we are initialising fields
        } else {
            emitter.emitContinueLine(" = None");
        }
        emitter.emitNewline();
        emitter.emitNewline();
    }

    static public String convertType(String type) {
        return table.getOrDefault(type, type);
    }

    static final java.util.Map<String, String> table = new java.util.HashMap<>() {{
        put("Builder[]", "List[Builder]");
        put("java.util.Map<java.lang.String, org.openprovenance.prov.client.Builder>", "Dict[str,Builder]"); // watch out: string representation with a space!!
        put("java.util.Map<java.lang.String, org.openprovenance.prov.client.ProcessorArgsInterface<?>>", "Dict[str,ProcessorArgsInterface]");
    }};
}
