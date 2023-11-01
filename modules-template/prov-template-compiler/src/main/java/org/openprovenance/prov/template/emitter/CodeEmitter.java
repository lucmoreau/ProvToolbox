//package org.openprovenance.prov.template.emitter;
package com.squareup.javapoet;
import org.openprovenance.prov.template.emitter.*;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.openprovenance.prov.template.emitter.Statement.makeStatement;

public class CodeEmitter {

    public StringBuffer getSb() {
        return sb;
    }

    private StringBuffer sb=new StringBuffer();
    public CodeEmitter() {
    }

    public void emitCode(TypeSpec spec, Set<String> names) {
        emitNewline();
        emitLine("from dataclasses import dataclass");
        emitNewline();
        emitJavaDoc(spec.javadoc);
        emitClassName(spec.name);
        spec.fieldSpecs.forEach(field -> {
            emitCode(field,names);
            emitNewline();
        });
        
        spec.methodSpecs.forEach(method -> {
            emitCode(method, names);
            emitNewline();
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

    private Method emitCode(MethodSpec method, Set<String> names) {
        System.out.println("++++ method: " + method.name);
        if (names==null || names.contains(method.name)) {
            System.out.println("YES");
            Method methodResult=new Method();
            methodResult.name=method.name;
            methodResult.returnType=method.returnType.toString();
            methodResult.parameters=method.parameters.stream().map(p -> new Parameter(p.name, p.type.toString())).collect(Collectors.toList());

            emitLine("def " + method.name + "(self" +
                    (method.parameters.isEmpty()?"":",") +
                    method.parameters.stream().map(p -> p.name).collect(Collectors.joining(", ")) + "):");
            indent();

            Arrays.asList(method.code.toString().split("\n")).forEach(l -> {
                emitLine(l.replace("//", "#"));
            });



            CodeBlock codeBlock = method.code;
            methodResult.body=emitCode(codeBlock);

            unindent();
            emitNewline();
            System.out.println("methodResult: " + methodResult);
            return methodResult;
        } else {
            return null;
        }
    }

    private List<Statement> emitCode(CodeBlock codeBlock) {
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
                elements.add(new Comment(formatPart));
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
                    elements.add(new Pair(formatPart,emitCode((CodeBlock) arg_i)));
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

    public void emitJavaDoc(CodeBlock javadoc) {
        Arrays.asList(javadoc.toString().split("\n")).forEach(s -> emitLine("# " + s));
    }
    public void emitCode(FieldSpec f) {
        emitCode(f,null);
    }

    public void emitCode(FieldSpec f, Set<String> names) {
        if (names==null || names.contains(f.name)) {
            emitJavaDoc(f.javadoc);
            emitBeginLine(f.name + ":  " + convertType(f.type));
            if (f.initializer != null) {
                emitContinueLine(f.initializer.toString());
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