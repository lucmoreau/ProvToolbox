//package org.openprovenance.prov.template.emitter;
package com.squareup.javapoet;
import org.openprovenance.prov.template.emitter.*;
import org.openprovenance.prov.template.emitter.minilanguage.Method;
import org.openprovenance.prov.template.emitter.minilanguage.Parameter;
import org.openprovenance.prov.template.emitter.minilanguage.Statement;
import org.openprovenance.prov.template.emitter.minilanguage.emitters.Python;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.openprovenance.prov.template.emitter.minilanguage.Statement.makeStatement;

public class CodeEmitter {

    public StringBuffer getSb() {
        return sb;
    }

    private StringBuffer sb=new StringBuffer();
    public CodeEmitter() {
    }

    public void parse(TypeSpec spec, Set<String> names) {
        emitNewline();
        emitLine("from dataclasses import dataclass");
        emitNewline();
        emitJavaDoc(spec.javadoc);
        emitClassName(spec.name);
        spec.fieldSpecs.forEach(field -> {
            parse(field,names);
            emitNewline();
        });
        
        spec.methodSpecs.forEach(method -> {
            Method m= parse(method, names);
            if (m!=null) {
                m.emit(new Python(sb, indent));
                emitNewline();
            }
        });


        unindent();

        /*
        System.out.println("# ++++++++++");
        System.out.println(spec.anonymousTypeArguments);
        System.out.println(spec.annotations);
        System.out.println(spec.typeVariables);
        System.out.println(spec.superclass);
        System.out.println(spec.enumConstants);
        

        System.out.println(spec.staticBlock);
        System.out.println(spec.initializerBlock);
        System.out.println(spec.methodSpecs);
        System.out.println(spec.methodSpecs);
        System.out.println(spec.typeSpecs);
        System.out.println(spec.originatingElements);
        System.out.println(spec.alwaysQualifiedNames);

         */
    }

    private Method parse(MethodSpec method, Set<String> names) {
        System.out.println("++++ method: " + method.name);
        if (names==null || names.contains(method.name)) {
            System.out.println("YES");
            Method methodResult=new Method();
            methodResult.name=method.name;
            methodResult.returnType=method.returnType.toString();
            methodResult.parameters=method.parameters.stream().map(p -> new Parameter(p.name, p.type.toString())).collect(Collectors.toList());


            CodeBlock codeBlock = method.code;
            methodResult.body= parse(codeBlock);

            System.out.println("methodResult: " + methodResult);
            return methodResult;
        } else {
            return null;
        }
    }

    private List<Statement> parse(CodeBlock codeBlock) {
        CodeBlock.Builder builder = codeBlock.toBuilder();
        List<Statement> statements=new LinkedList<>();
        List<Element> elements=new LinkedList<>();

        int i=0;
        for (String formatPart: builder.formatParts) {
            if (formatPart.equals("$[")) {
                if (!elements.isEmpty()) {
                    statements.add(makeStatement(elements));
                }
                elements=new LinkedList<>();
            } else if (formatPart.equals("$]")) {
                if (!elements.isEmpty()) {
                    statements.add(makeStatement(elements));
                    elements = new LinkedList<>();
                }
            } else if (formatPart.startsWith("//") ) {
                if (!elements.isEmpty()) {
                    statements.add(makeStatement(elements));
                    elements = new LinkedList<>();
                }
                elements.add(new Token(formatPart));
            } else if (formatPart.trim().equals("=")) {
                elements.add(new Token(formatPart));
            } else if (formatPart.trim().equals("return")) {
                elements.add(new Token(formatPart));
            } else if (formatPart.trim().isEmpty()) {

            } else if (formatPart.trim().equals(".")) {
                elements.add(new Token(formatPart));
            } else if (formatPart.trim().equals(",")) {
                elements.add(new Token(formatPart));
            } else if (formatPart.trim().equals("(")) {
                elements.add(new Token(formatPart));
            } else if (formatPart.trim().equals(")")) {
                elements.add(new Token(formatPart));
            } else if (formatPart.trim().equals("$<")) {
                elements.add(new Token(formatPart));
            } else if (formatPart.trim().equals("$>")) {
                elements.add(new Token(formatPart));
            } else if (formatPart.trim().startsWith(";")) {
                if (!elements.isEmpty()) {
                    statements.add(makeStatement(elements));
                    elements=new LinkedList<>();
                }
            } else if (formatPart.trim().startsWith("()")) {
                elements.add(new Token(formatPart));
            } else if (formatPart.trim().startsWith("{")) {
                System.out.println("${");
            } else if (formatPart.equals("$L") || formatPart.equals("$N") || formatPart.equals("$T")) {
                Object arg_i = builder.args.get(i);
                if (arg_i instanceof CodeBlock) {
                    elements.add(new Pair(formatPart, parse((CodeBlock) arg_i)));
                } else {
                    elements.add(new Pair(formatPart,arg_i));
                }
                i++;
            } else {
                elements.add(new Token(formatPart));
            }
        }
        if (!elements.isEmpty()) {
            statements.add(makeStatement(elements));
        }
        return statements;
    }

    private void emitNewline() {
        sb.append("\n");
    }

    public void emitClassName(String name) {
        emitLine("@dataclass");
        emitLine("class " + name + ":");
        indent();
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
    public void emitContinueLine(String s) {
        sb.append(s);
    }
    int indent=0;
    private void indent() {
        indent++;
    }

    private void unindent() {
        indent--;
    }



    public void emitPrelude(String comment) {
        emitLine("#!/usr/bin/env/python");
        emitLine("");
        Arrays.asList(comment.split("\n")).forEach(s -> emitLine("# " + s));
        emitLine("");
    }

    public List<Statement>  emitJavaDoc(CodeBlock javadoc) {
        return parse(javadoc);
    }
    public void parse(FieldSpec f) {
        parse(f,null);
    }

    public void parse(FieldSpec f, Set<String> names) {
        if (names==null || names.contains(f.name)) {
            emitJavaDoc(f.javadoc);
            emitBeginLine(f.name + ": " + convertType(f.type));
            if (f.initializer != null && !f.initializer.toString().trim().isEmpty()) {
                emitContinueLine(" = " + f.initializer.toString().replace("\"", "'"));
            }
            emitNewline();
        }
    }

    private String convertType(TypeName type) {
        TypeName box=type.withoutAnnotations().box();
        switch (box.toString()) {
            case "java.lang.String":
                return "str";
            case "java.lang.Integer":
                return "int";
            case "java.lang.Float":
                return "float";
            case "java.lang.String[]":
                return "str[]";
            default:
                return box.toString();
        }

    }
}