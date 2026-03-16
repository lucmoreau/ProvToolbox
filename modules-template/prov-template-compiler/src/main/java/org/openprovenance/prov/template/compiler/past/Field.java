package org.openprovenance.prov.template.compiler.past;

import org.openprovenance.prov.template.compiler.past.annotations.PastAnnotation;
import org.openprovenance.prov.template.compiler.past.type.TypeName;


import javax.lang.model.element.Modifier;
import java.util.LinkedList;
import java.util.List;

import static org.openprovenance.prov.template.compiler.past.annotations.AnnotationConverter.toAnnotation;

public class Field {

    public final String name;
    final public List<String> attributes=new LinkedList<>();
    final public TypeName type;
    public Expression initialiser;
    final public List<Comment> comments=new LinkedList<>();
    final public List<Modifier> modifiers=new LinkedList<>();
    public List<PastAnnotation> annotation=new java.util.ArrayList<>();



    public Field(String name, TypeName type, List<String> attributes, Expression initialiser, List<Comment> comments) {
        this.name = name;
        this.type = type;
        this.attributes.addAll(attributes);
        this.initialiser = initialiser;
        this.comments.addAll(comments);
    }


    public Field(String name, TypeName type) {
        this.name = name;
        this.type = type;
    }

    @Override
    public String toString() {
        return "Field{" +
                "name='" + name + '\'' +
                ", attributes=" + attributes +
                ", type=" + type +
                ", initialiser=" + initialiser +
                ", comments=" + comments +
                ", modifiers=" + modifiers +
                '}';
    }

    static public Field FIELD(String name, TypeName type) {
        return new Field(name,type,new LinkedList<>(),null,new LinkedList<>());
    }


    public Field COMMENT(String format, Object... args) {
        this.comments.add(new Comment(format, args));
        return this;
    }

    public Field MODIFIERS(Modifier... modifiers) {
        this.modifiers.addAll(List.of(modifiers));
        return this;
    }

    public Field INITIALIZER(Expression initialiser) {
        this.initialiser = initialiser;
        return this;
    }

    public Field ANNOTATION(PastAnnotation... annotations) {
        for (PastAnnotation annotation: annotations) {
            if (annotation == null) throw new IllegalArgumentException("null annotation");
            this.annotation.add(annotation);
        }
        return this;
    }
    public Field ANNOTATION(String ... annotations) {
        for (String annotation: annotations) {
            if (annotation == null) throw new IllegalArgumentException("null annotation");
            this.annotation.add(toAnnotation(annotation));
        }
        return this;
    }
}
