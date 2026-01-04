package org.openprovenance.prov.template.compiler.past;
import org.openprovenance.prov.template.compiler.past.type.TypeName;

import javax.lang.model.element.Modifier;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class Method {
    public List<Comment> comments=new java.util.ArrayList<>();
    public String name;
    public TypeName returnType;
    public List<Parameter> parameters=new java.util.ArrayList<>();
    public List<Statement> body=new java.util.ArrayList<>();
    public List<Modifier> modifiers=new java.util.ArrayList<>();
    public List<TypeName> typeVariables=new java.util.ArrayList<>();

    public Method() {}
    public Method(String name, TypeName returnType, List<Parameter> parameters, List<Statement> body, Collection<Modifier> modifiers, List<Comment> comments) {
        this.name = name;
        this.returnType = returnType;
        this.parameters.addAll(parameters);
        this.body.addAll(body);
        this.modifiers.addAll(modifiers);
        this.comments.addAll(comments);
    }
    public Method(String name) {
        this.name = name;
    }


    @Override
    public String toString() {
        return "Method{" +
                "name='" + name + '\'' +
                ", returnType='" + returnType + '\'' +
                ", parameters=" + parameters +
                ", body=" + body +
                '}';
    }

    public boolean isStatic() {
        return modifiers.contains(Modifier.STATIC);
    }

    public Method MODIFIERS(Modifier... modifiers) {
        this.modifiers.addAll(Arrays.asList(modifiers));
        return this;
    }

    public Method RETURNS(TypeName typeT) {
        this.returnType=typeT;
        return this;
    }

    public Method addTypeVariables(TypeName... typeVars) {
        this.typeVariables.addAll(Arrays.asList(typeVars));
        return this;
    }

    public Method PARAMETER(TypeName typeName, String parameterName) {
        this.parameters.add(new Parameter(parameterName, typeName));
        return this;
    }

    public Method addStatement(Statement statement) {
        this.body.add(statement);
        return this;
    }
    public Method BODY(Statement... statements) {
        for (Statement statement: statements) {
            if (statement == null) throw new IllegalArgumentException("null statement");
            this.body.add(statement);
        }
        return this;
    }

    public Method COMMENT(String format, Object... args) {
        this.comments.add(new Comment(format, args));
        return this;
    }

    public Method COMMENT(boolean internal, String format, Object... args) {
        if (internal) {
            this.addStatement(new Comment(format, args));
        } else {
            this.comments.add(new Comment(format, args));
        }
        return this;
    }

    public static Method METHOD(String name) {
        return new Method(name);
    }

}
