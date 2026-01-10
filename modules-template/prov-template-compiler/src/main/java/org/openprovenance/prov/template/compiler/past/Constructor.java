package org.openprovenance.prov.template.compiler.past;
import org.openprovenance.prov.template.compiler.past.type.TypeName;

import javax.lang.model.element.Modifier;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class Constructor {
    public List<Comment> comments=new java.util.ArrayList<>();
    //public String name;
    //public TypeName returnType;
    public List<Parameter> parameters=new java.util.ArrayList<>();
    public List<Statement> body=new java.util.ArrayList<>();
    public List<Modifier> modifiers=new java.util.ArrayList<>();
    //public List<TypeName> typeVariables=new java.util.ArrayList<>();
    public List<String> annotation=new java.util.ArrayList<>();

    public Constructor() {}
    public Constructor(List<Parameter> parameters, List<Statement> body, Collection<Modifier> modifiers, List<Comment> comments) {
        this.parameters.addAll(parameters);
        this.body.addAll(body);
        this.modifiers.addAll(modifiers);
        this.comments.addAll(comments);
    }


    @Override
    public String toString() {
        return "Constructor{" +
                "comments=" + comments +
                ", parameters=" + parameters +
                ", body=" + body +
                ", modifiers=" + modifiers +
                ", annotation=" + annotation +
                '}';
    }

    public boolean isStatic() {
        return modifiers.contains(Modifier.STATIC);
    }

    public Constructor MODIFIERS(Modifier... modifiers) {
        this.modifiers.addAll(Arrays.asList(modifiers));
        return this;
    }

    public Constructor PARAMETERS(Parameter... parameters) {
        for (Parameter parameter: parameters) {
            if (parameter == null) throw new IllegalArgumentException("null parameter");
            this.parameters.add(parameter);
        }
        return this;
    }
    public Constructor PARAMETERS(List<Parameter> parameters) {
        for (Parameter parameter: parameters) {
            if (parameter == null) throw new IllegalArgumentException("null parameter");
            this.parameters.add(parameter);
        }
        return this;
    }

    public Constructor PARAMETER(TypeName typeName, String parameterName) {
        this.parameters.add(new Parameter(parameterName, typeName));
        return this;
    }

    public Constructor addStatement(Statement statement) {
        this.body.add(statement);
        return this;
    }
    public Constructor BODY(Statement... statements) {
        for (Statement statement: statements) {
            if (statement == null) throw new IllegalArgumentException("null statement");
            this.body.add(statement);
        }
        return this;
    }

    public Constructor COMMENT(String format, Object... args) {
        this.comments.add(new Comment(format, args));
        return this;
    }

    public Constructor COMMENT(boolean internal, String format, Object... args) {
        if (internal) {
            this.addStatement(new Comment(format, args));
        } else {
            this.comments.add(new Comment(format, args));
        }
        return this;
    }

    public static Constructor CONSTRUCTOR() {
        return new Constructor();
    }

    public Constructor ANNOTATIONS(String... names) {
        this.annotation.addAll(Arrays.asList(names));
        return this;
    }

}
