//package org.openprovenance.prov.template.emitter;
package com.squareup.javapoet;
import org.openprovenance.prov.template.emitter.*;
import org.openprovenance.prov.template.emitter.minilanguage.*;
import org.openprovenance.prov.template.emitter.minilanguage.Class;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.openprovenance.prov.template.compiler.common.CompilerCommon.*;
import static org.openprovenance.prov.template.emitter.minilanguage.Expression.makeExpression;
import static org.openprovenance.prov.template.emitter.minilanguage.Statement.makeStatement;

public class PoetParser {

    public StringBuffer getSb() {
        return sb;
    }

    private StringBuffer sb=new StringBuffer();
    public PoetParser() {
    }

    public Class parse (TypeSpec spec, Set<String> selectedExports) {
        emitLine("\nfrom dataclasses import dataclass");
        emitLine("from org.openprovenance.apache.commons.lang.StringEscapeUtils import StringEscapeUtils");

        if (spec.name.equals("Logger")) {
            emitLine("from org.openprovenance.prov.client.Builder import Builder");
            emitLine("from org.openprovenance.prov.client.ProcessorArgsInterface import ProcessorArgsInterface");
            emitLine("from typing import List");
            emitLine("from typing import Dict");
            emitLine("from org.example.templates.block.client.common.Template_blockBuilder import Template_blockBuilder");

        }
        emitNewline();
        emitNewline();


        List<Field> fields=new LinkedList<>();
        spec.fieldSpecs.forEach(field -> {
            Field f=parse(field,selectedExports);
            if (f!=null) {
                fields.add(f);
            }
        });

        List<Method> methods=new LinkedList<>();
        spec.methodSpecs.forEach(method -> {
            Method m= parse(method, selectedExports);
            if (m!=null) {
                methods.add(m);
            }
        });

        return new Class(spec.name, new LinkedList<>(), fields, methods, getComments(spec.javadoc));


    }

    private Method parse(MethodSpec method, Set<String> names) {
        if (names==null || names.contains(method.name)) {
            Method methodResult=new Method();
            methodResult.name=method.name;
            methodResult.returnType=method.returnType.toString();
            methodResult.parameters=method.parameters.stream().map(p -> new Parameter(p.name, p.type.toString())).collect(Collectors.toList());


            CodeBlock codeBlock = method.code;
            methodResult.body= parse(codeBlock);
            return methodResult;
        } else {
            return null;
        }
    }

    private List<Statement> parse(CodeBlock codeBlock) {
        CodeBlock.Builder builder = codeBlock.toBuilder();
        List<Statement> statements=new LinkedList<>();
        List<Element> elements=new LinkedList<>();
        Conditional conditional=null;
        List<Statement> back=new LinkedList<>();

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
            } else if ((formatPart.startsWith("//") || formatPart.startsWith("/*"))) {
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

            } else if (formatPart.trim().startsWith("if")) {
                if (!elements.isEmpty()) {
                    statements.add(makeStatement(elements));
                    elements=new LinkedList<>();
                }
                conditional=new Conditional(null); // ISSUE: no nesting of conditionals
            } else if (formatPart.trim().equals(".")) {
                elements.add(new Token(formatPart));
            } else if (formatPart.trim().equals(",")) {
                elements.add(new Token(formatPart));
            } else if (formatPart.trim().equals("(")) {
                elements.add(new Token(formatPart));
            } else if (formatPart.trim().equals(")")) {
                elements.add(new Token(formatPart));
            } else if (formatPart.trim().equals("))")) {
                // ignore
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
                //System.out.println("${");
            } else if (formatPart.equals("$L") || formatPart.equals("$S") || formatPart.equals("$N") || formatPart.equals("$T")) {
                Object arg_i = builder.args.get(i);
                if (arg_i instanceof CodeBlock) {
                    elements.add(new Pair(formatPart, parse((CodeBlock) arg_i)));
                } else {
                    elements.add(new Pair(formatPart,arg_i));
                    if (arg_i instanceof String ) {
                        String arg_i_str=(String)arg_i;
                        if (arg_i_str.contains(MARKER_ENDIF) && conditional != null) {
                            statements.add(makeStatement(elements)); // ISSUE: making statement and not statements, what about other statements)
                            conditional.alternates = statements;
                            //System.out.println("conditional.alternates=" + conditional.alternates);
                            // restore previous statements, and add conditional
                            statements = back;
                            statements.add(conditional);
                            back=null;
                            conditional = null;
                        }
                        if (arg_i_str.contains(MARKER_ELSE) && conditional != null) {
                            statements.add(makeStatement(elements));
                            conditional.consequents=statements;
                            //System.out.println("conditional.consequents=" + conditional.consequents);
                            statements = new LinkedList<>();
                            elements = new LinkedList<>();
                        }
                        if (arg_i_str.contains(MARKER_THEN) && conditional != null) {
                            conditional.predicate=makeExpression(elements);
                            //System.out.println("conditional.predicate=" + conditional.predicate);
                            back=statements;
                            statements=new LinkedList<>();
                            elements=new LinkedList<>();
                        }
                        if (arg_i_str.contains(MARKER_PARAMS)) {
                            //elements.add(new Token(arg_i_str));
                        }
                    }
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



    public void emitLine(String s) {
        for (int i=0; i<indent; i++) {
            sb.append("    ");
        }
        sb.append(s);
        emitNewline();
    }

    int indent=0;
    private void indent() {
        indent++;
    }


    public void emitPrelude(String comment) {
        emitLine("#!/usr/bin/env/python");
        emitLine("");
        Arrays.asList(comment.split("\n")).forEach(s -> emitLine("# " + s));
        emitLine("");
    }

    public List<Statement> parseJavadoc(CodeBlock javadoc) {
        return parse(javadoc);
    }
    /*public void parse(FieldSpec f) {
        parse(f,null);
    }

     */

    public Field parse(FieldSpec f, Set<String> names) {
        if (names==null || names.contains(f.name)) {
            List<Statement> initialiser1 = parse(f.initializer);
            assert  initialiser1.isEmpty() || initialiser1.size() == 1;
            Expression initialiser=null;
            if (!initialiser1.isEmpty()) {
                initialiser= (Expression) initialiser1.get(0);
            }
            return new Field(f.name, convertType(f.type), f.annotations.stream().map(a -> a.toString()).collect(Collectors.toList()), initialiser, getComments2(f.javadoc));
        }
        return null;
    }

    private List<Comment> getComments2(CodeBlock javadoc) {
        //Arrays.asList(javadoc.toString().split("\n")).forEach(s -> emitLine("# " + s));
        return Arrays.stream(javadoc.toString().split("\n")).map(Comment::new).collect(Collectors.toList());
    }

    private List<Comment> getComments(CodeBlock comments) {
        return parseJavadoc(comments).stream().map(s -> (s instanceof Comment)? (Comment) s: new Comment(((Expression)s).getElements().toString())).collect(Collectors.toList());
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