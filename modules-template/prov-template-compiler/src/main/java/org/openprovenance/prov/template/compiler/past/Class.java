package org.openprovenance.prov.template.compiler.past;


import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.openprovenance.prov.template.compiler.past.annotations.PastAnnotation;
import org.openprovenance.prov.template.compiler.past.type.TypeName;
import org.openprovenance.prov.template.compiler.past.type.TypeVariable;

import javax.lang.model.element.Modifier;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static org.openprovenance.prov.template.compiler.past.annotations.AnnotationConverter.toAnnotation;

@JsonPropertyOrder ({ "comments", "name","modifiers","interfaces","fields","methods" })

public class Class {

    public enum ClassKind { CLASS, INTERFACE, ANONYMOUS}


    final public String name;
    final public List<Comment> comments=new LinkedList<>();
    final public List<Field> fields=new LinkedList<>();
    final public List<Method> methods=new LinkedList<>();
    final public List<Constructor> constructors=new LinkedList<>();
    final public List<TypeName> interfaces=new LinkedList<>();
    final public List<TypeVariable> typeVariables=new LinkedList<>();
    final public List<Modifier> modifiers=new LinkedList<>();
    final public boolean isInterface;
    final public ClassKind classKind;
    final public List<Statement> staticBlock=new LinkedList<>();
    public TypeName superclass = null;
    final public List<PastAnnotation> annotation=new LinkedList<>();

    public Class (String name) {
        this.name = name;
        this.isInterface=false;
        this.classKind=ClassKind.CLASS;
    }
    public Class(String name, List<TypeName> interfaces, List<Field> fields, List<Method> methods, List<Comment> comments) {
        this.name = name;
        if (interfaces!=null) this.interfaces.addAll(interfaces);
        this.fields.addAll(fields);
        this.methods.addAll(methods);
        this.comments.addAll(comments);
        this.isInterface=false;
        this.classKind=ClassKind.CLASS;
    }

    public Class(String interfaceName, boolean isInterface) {
        this.name = interfaceName;
        this.isInterface=isInterface;
        this.classKind=ClassKind.INTERFACE;
    }

    public Class(String name, ClassKind kind) {
        this.name = name;
        this.isInterface=kind==ClassKind.INTERFACE;
        this.classKind=kind;
    }

    public Class INTERFACES(TypeName... interfaceNames) {
        for (TypeName interfaceName: interfaceNames) {
            if (interfaceName == null) throw new IllegalArgumentException("null interface name");
            interfaces.add(interfaceName);
        }
        return this;
    }

    public Class SUPERCLASS(TypeName superType) {
        if (superType == null) throw new IllegalArgumentException("null superclass");
        this.superclass = superType;
        return this;
    }

    public Class TYPE_VARIABLES(TypeVariable... typeVariables) {
        for (TypeVariable typeVariable: typeVariables) {
            if (typeVariable == null) throw new IllegalArgumentException("null type variable");
            this.typeVariables.add(typeVariable);
        }
        return this;
    }


    @Override
    public String toString() {
        return "Class{" +
                "name='" + name + '\'' +
                ", comments=" + comments +
                ", fields=" + fields +
                ", methods=" + methods +
                ", constructors=" + constructors +
                ", interfaces=" + interfaces +
                ", typeVariables=" + typeVariables +
                ", modifiers=" + modifiers +
                ", isInterface=" + isInterface +
                ", classKind=" + classKind +
                ", staticBlock=" + staticBlock +
                ", superclass=" + superclass +
                ", annotations=" + annotation +
                '}';
    }

    public Class MODIFIERS(Modifier... modifiers) {
        for (Modifier modifier: modifiers) {
            if (modifier == null) throw new IllegalArgumentException("null modifier");
            this.modifiers.add(modifier);
        }
        return this;
    }

    public Class COMMENT(String format, Object... args) {
        if (format==null) throw new IllegalArgumentException("null format");
        this.comments.add(new Comment(format, args));
        return this;
    }

    public Class FIELDS(Field... fields) {
        for (Field field: fields) {
            if (field == null) throw new IllegalArgumentException("null field");
            this.fields.add(field);
        }
        return this;
    }

    public Class METHOD(Method method2) {
        if (method2==null) throw new IllegalArgumentException("null method");
        this.methods.add(method2);
        return this;
    }

    public Class METHODS(Method... methods) {
        if ((methods==null) || (methods.length==0)) return this;
        for (Method method: methods) {
            if (method == null) throw new IllegalArgumentException("null method");
            this.methods.add(method);
        }
        return this;
    }
    public Class CONSTRUCTOR(Constructor method2) {
        if (method2==null) throw new IllegalArgumentException("null constructor");
        this.constructors.add(method2);
        return this;
    }
    public Class STATIC_BLOCK(List<Statement> statements) {
        if (statements==null) throw new IllegalArgumentException("null statements");
        if (statements.isEmpty()) return this;
        staticBlock.addAll(statements);
        return this;
    }


    public Class ANNOTATION(PastAnnotation... annot) {
        for (PastAnnotation a: annot) {
            if (a==null) {
                throw new IllegalArgumentException("Annotation cannot be null");
            }
            this.annotation.add(a);
        }
        return this;
    }

    public Class ANNOTATION(String... annot) {
        for (String a: annot) {
            if (a==null) {
                throw new IllegalArgumentException("Annotation cannot be null");
            }
            this.annotation.add(toAnnotation(a));
        }
        return this;
    }


}
