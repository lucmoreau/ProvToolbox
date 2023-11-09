package org.openprovenance.prov.template.emitter.minilanguage;

import org.openprovenance.prov.template.emitter.minilanguage.emitters.Python;

import java.util.*;
import java.util.stream.Collectors;

public class Class {
    final private String name;
    final private List<Comment> comments=new LinkedList<>();
    public final List<Field> fields=new LinkedList<>();
    final private List<Method> methods=new LinkedList<>();
    private final List<String> interfaces=new LinkedList<>();
    public List<Field> fieldsForClassInitialiser=new LinkedList<>();

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

        // class variables are used in methods, to decide which variable to prefix with self.
        List<String> classVariables=fields.stream().map(x->x.name).collect(Collectors.toList());

        Collection<String> staticMethods=new HashSet<>();

        for (Method m: methods) {
            if (m.isStatic()) {
                staticMethods.add(m.name);
                m.emit(emitter, classVariables, new LinkedList<>()); // if static, then we need to prefix with cls
            } else {
                m.emit(emitter, new LinkedList<>(), classVariables); // if not static, then we need to prefix with self
            }
        }
        for (Field f: fields) {
            if (belongingClassInitialiser(f, staticMethods)) {
                fieldsForClassInitialiser.add(f);
                System.out.println("#### fieldsForClassInitialiser " + fieldsForClassInitialiser);
            } else {
                f.emit(emitter);
            }
        }
    }

    public boolean belongingClassInitialiser(Field f, Collection<String> staticMethods) {
        if (f.initialiser==null) return false;
        if (f.initialiser instanceof MethodCall) {
            MethodCall mc=(MethodCall)f.initialiser;
            if (mc.object instanceof Symbol) {
                Symbol s=(Symbol)mc.object;
                if (s.symbol.equals(this.name)) {
                    return true;
                }
            }
            if (staticMethods.contains(mc.methodName)) {
                return true;
            }
        }
        return false;
    }



    public void emitClassInitialiser(Python emitter, int indent) {
        if (fieldsForClassInitialiser.isEmpty()) return;
        emitter.indent=indent;
        emitter.emitLine("# class initialiser for " + name);
        for (Field f: fieldsForClassInitialiser) {
            emitter.indent=indent;
            emitter.emitBeginLine(name + ".");
            ((MethodCall)f.initialiser).object=new Symbol(name,null);
            f.emit(emitter);

        }

    }
}
