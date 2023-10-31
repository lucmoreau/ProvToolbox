//package org.openprovenance.prov.template.emitter;
package com.squareup.javapoet;
import com.squareup.javapoet.*;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

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


        de_indent();

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

    private void emitCode(MethodSpec method, Set<String> names) {
        if (names==null || names.contains(method.name)) {

            emitLine("def " + method.name + "(self" +
                    (method.parameters.isEmpty()?"":",") +
                    method.parameters.stream().map(p -> p.name).collect(Collectors.joining(", ")) + "):");
            indent();

            Arrays.asList(method.code.toString().split("\n")).forEach(l -> {
                emitLine(l.replace("//", "#"));
            });

            CodeBlock codeBlock = method.code;
            emitCode(codeBlock);

            de_indent();
            emitNewline();
        }
    }

    private void emitCode(CodeBlock codeBlock) {
        CodeBlock.Builder builder = codeBlock.toBuilder();
        int i=0;
        //System.out.println("formatParts: " + builder.formatParts.size());
        //System.out.println("formatParts: " + builder.args.size());
        for (String formatPart: builder.formatParts) {
            if (formatPart.equals("$[")) {
                System.out.println("$[: start statement");
            } else if (formatPart.equals("$]")) {
                System.out.println("$]: end statement");
            } else if (formatPart.trim().equals("=")) {
                System.out.println("$assignment");
            } else if (formatPart.trim().equals("return")) {
                System.out.println("$return");
            } else if (formatPart.trim().isEmpty()) {
                System.out.println("$space");
            } else if (formatPart.trim().equals(".")) {
                System.out.println("$.");
            } else if (formatPart.trim().equals(",")) {
                System.out.println("$,");
            } else if (formatPart.trim().equals("(")) {
                System.out.println("$(");
            } else if (formatPart.trim().equals(")")) {
                System.out.println("$)");
            } else if (formatPart.trim().equals("$<")) {
                System.out.println("$<");
            } else if (formatPart.trim().equals("$>")) {
                System.out.println("$>");
            } else if (formatPart.trim().startsWith(";")) {
                System.out.println("$;");
            } else if (formatPart.trim().startsWith("()")) {
                System.out.println("$()");
            } else if (formatPart.trim().startsWith("{")) {
                System.out.println("${");
            } else if (formatPart.equals("$L") || formatPart.equals("$N") || formatPart.equals("$T")) {
                System.out.print(formatPart);
                Object arg_i = builder.args.get(i);
                if (arg_i instanceof CodeBlock) {
                    System.out.print(" ->> ");
                    emitCode((CodeBlock) arg_i);
                } else {
                    System.out.print(" -> " + arg_i);
                }
                i++;
            } else {
                System.out.print(formatPart + " (other)");
            }
            System.out.println("");
        }
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

    private void de_indent() {
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