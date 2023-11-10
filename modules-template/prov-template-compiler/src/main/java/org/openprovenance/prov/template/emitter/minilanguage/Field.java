package org.openprovenance.prov.template.emitter.minilanguage;

import org.openprovenance.prov.client_copy.Builder;
import org.openprovenance.prov.template.emitter.minilanguage.emitters.Python;

import javax.lang.model.element.Modifier;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.openprovenance.prov.template.compiler.common.Constants.A_ARGS_BEAN_CONVERTER;

public class Field {

    public final String name;
    final private List<String> attributes=new LinkedList<>();
    final private String type;
    public final Expression initialiser;
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

        if (name.equals(A_ARGS_BEAN_CONVERTER)) {
            Lambda lambda = (Lambda) initialiser;
            List<Parameter> parameters=lambda.parameters;
            List<Statement> body=lambda.body;

            // convert this Java lambda declaration in a Python method definition
            Method method=new Method();
            method.name=A_ARGS_BEAN_CONVERTER;
            method.parameters=parameters;
            method.body=body;
            method.modifiers=java.util.Collections.singleton(Modifier.STATIC);
            method.emit(emitter, new LinkedList<>(), new LinkedList<>());

        } else {

            emitter.emitBeginLine(name);




            String mappedType = typeConversionTable.get(type);
            if (mappedType != null) {
                emitter.emitContinueLine(" : " + mappedType);
                String[] imprts = getImportTypes(type);
                if (imprts!=null) Arrays.asList(imprts).forEach(imprt -> emitter.getImports().add(imprt));
            } else if (type != null && !type.trim().isEmpty()) {
                if (type.contains(".")) {
                    String[] parts = type.split("\\.");
                    String localType = parts[parts.length - 1];
                    emitter.getImports().add("from " + type + " import " + localType);
                    emitter.emitContinueLine(" : " + convertType(localType));
                } else {
                    emitter.emitContinueLine(" : " + type);
                }
            }
            if (initialiser != null && !initialiser.toString().trim().isEmpty()) {
                emitter.emitContinueLine(" = "); // initialiser.toString().replace("\"", "'"));
                initialiser.emit(emitter, true, new LinkedList<>(), new LinkedList<>());  // no need to pass any class variables, given that we are initialising fields
            } else {
                emitter.emitContinueLine(" = None");
            }
        }
        emitter.emitNewline();
        emitter.emitNewline();
    }


    @Override
    public String toString() {
        return "Field{" +
                "name='" + name + '\'' +
                ", attributes=" + attributes +
                ", type='" + type + '\'' +
                ", initialiser=" + initialiser +
                ", comments=" + comments +
                '}';
    }

    static public String convertType(String type) {
        String res=typeConversionTable.get(type);
        if (res!=null) return res;
        if (type.contains("<")) {
            // return string precedent <
            return type.substring(0, type.indexOf("<"));
        }
        return type;
    }

    private String[] getImportTypes(String type) {
        String[] strings = typeToImportsTable.get(type);
        if (strings!=null) return strings;
        if (type.contains("<") && type.contains(">")) {
            // return string precedent <
            String contentType=type.substring(type.indexOf("<")+1,type.indexOf(">"));
            System.out.println("contentType=" + contentType);
            if (contentType.contains(".")) {
                String[] parts = contentType.split("\\.");
                String localType = parts[parts.length - 1];
                return new String[]{"from " + contentType + " import " + localType};
            }

        }
        return null;
    }


    static final java.util.Map<String, String> typeConversionTable = new java.util.HashMap<>() {{
        put("org.openprovenance.prov.client.Builder[]", "List[Builder]");
        put("java.util.Map<java.lang.String, org.openprovenance.prov.client.Builder>", "Dict[str,Builder]"); // watch out: string representation with a space!!
        put("java.util.Map<java.lang.String, org.openprovenance.prov.client.ProcessorArgsInterface<?>>", "Dict[str,ProcessorArgsInterface]");
        //put("org.example.templates.block.client.common.Template_blockProcessor<org.example.templates.block.client.common.Template_blockBean>", "Template_blockProcessor");
    }};

    static final java.util.Map<String, String[]> typeToImportsTable = new java.util.HashMap<>() {{
        put("org.openprovenance.prov.client.Builder[]", new String[] {"from typing import List", "from org.openprovenance.prov.client.Builder import Builder"});
        put("java.util.Map<java.lang.String, org.openprovenance.prov.client.Builder>", new String[] {"from typing import Dict"});
        put("java.util.Map<java.lang.String, org.openprovenance.prov.client.ProcessorArgsInterface<?>>", new String[] {"from org.openprovenance.prov.client.ProcessorArgsInterface import ProcessorArgsInterface"});
       // put("org.example.templates.block.client.common.Template_blockProcessor<org.example.templates.block.client.common.Template_blockBean>", new String[] {"from org.example.templates.block.client.common.Template_blockBean import Template_blockBean"});
    }};
}
