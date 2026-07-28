package org.openprovenance.prov.template.compiler.past.emitter;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.squareup.javapoet.*;
import org.openprovenance.prov.model.DOMProcessing;
import org.openprovenance.prov.template.compiler.past.*;
import org.openprovenance.prov.template.compiler.past.Class;
import org.openprovenance.prov.template.compiler.past.TryCatch;
import org.openprovenance.prov.template.compiler.past.ThrowStatement;
import org.openprovenance.prov.template.compiler.past.SuperConstructorCall;
import org.openprovenance.prov.template.compiler.past.Iterator;
import org.openprovenance.prov.template.compiler.past.annotations.Ignore;
import org.openprovenance.prov.template.compiler.past.type.ArrayType;
import org.openprovenance.prov.template.compiler.past.type.ParameterizedType;
import org.openprovenance.prov.template.compiler.past.type.TypeVariable;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static org.openprovenance.prov.template.compiler.past.BinaryOp.INSTANCEOF;


public class Poet implements Emitter<TypeSpec> {
    public JavaFile specWithComment(TypeSpec typeSpec, String templateName, String packge, StackTraceElement stackTraceElement) {
        return JavaFile.builder(packge, typeSpec)
                .addFileComment("Generated automatically by ProvToolbox for template '$L'", templateName)
                .addFileComment("\nby class $L, method $L,\nin file $L, at line $L",
                        stackTraceElement.getClassName(), stackTraceElement.getMethodName(), stackTraceElement.getFileName(), stackTraceElement.getLineNumber())
                .build();
    }

    public WritableObject toWritableObject(Class clazz, String templateName, String packge, StackTraceElement stackTraceElement) {
        JavaFile javaFile=specWithComment(emit(clazz), templateName, packge, stackTraceElement);
        return directory -> javaFile.writeTo(directory);
    }

    public TypeSpec emit(Class clazz) {
        return emitBuilder(clazz).build();
    }

    public TypeSpec.Builder emitBuilder(Class clazz) {

        TypeSpec.Builder builder=(clazz.isInterface)?TypeSpec.interfaceBuilder(clazz.name):TypeSpec.classBuilder(clazz.name);
        for (TypeVariable tv : clazz.typeVariables) {
            builder.addTypeVariable(convertTypeVariable(tv));
        }
        clazz.modifiers.forEach(builder::addModifiers);
        // emit superclass if present
        if (clazz.superclass != null) {
            builder.superclass(convert(clazz.superclass));
        }
        clazz.interfaces.forEach(intfce -> builder.addSuperinterface(convert(intfce)));
        clazz.fields.forEach(field -> builder.addField(convert(field)));
        clazz.comments.forEach(comment -> builder.addJavadoc(comment.format, comment.objects));
        clazz.methods.forEach(method -> builder.addMethod(convert(method)));
        clazz.constructors.forEach(constructor -> builder.addMethod(convert(constructor)));
        if (!clazz.staticBlock.isEmpty()) {
            CodeBlock.Builder block = CodeBlock.builder();
            clazz.staticBlock.forEach(statement -> {
                convertAndAddStatement(statement, block); // was block.addStatement("$L", convert(statement));
            });
            builder.addStaticBlock(block.build());
        }
        return builder;


    }

    public CodeBlock emitAnonymous(Class clazz) {
       // System.out.println("Anonymous class for interface: " + clazz);

        //org.openprovenance.prov.template.compiler.past.type.TypeName xx= clazz.interfaces.get(0);

        //TypeName interfaceType = convert(xx);
        TypeSpec.Builder builder=TypeSpec.anonymousClassBuilder("")
              ;

        for (TypeVariable tv : clazz.typeVariables) {
            builder.addTypeVariable(convertTypeVariable(tv));
        }
        clazz.modifiers.forEach(builder::addModifiers);
        clazz.fields.forEach(field -> builder.addField(convert(field)));
        clazz.comments.forEach(comment -> builder.addJavadoc(comment.format, comment.objects));
        clazz.methods.forEach(method -> builder.addMethod(convert(method)));
        clazz.constructors.forEach(constructor -> builder.addMethod(convert(constructor)));
        clazz.interfaces.forEach(intfce -> builder.addSuperinterface(convert(intfce)));
        return CodeBlock.of("$L",
                builder.build());


    }


    private MethodSpec convert(Constructor constructor) {
        MethodSpec.Builder builder=MethodSpec.constructorBuilder();
        constructor.modifiers.forEach(builder::addModifiers);
        constructor.parameters.forEach(param -> builder.addParameter(convert(param.type), param.name));
        constructor.comments.forEach(comment -> builder.addJavadoc(comment.format, convertToPoet(comment.objects)));
        constructor.body.forEach(statement -> {
            convertAndAddStatement(statement,builder);
        });
        return builder.build();
    }

    private MethodSpec convert(Method method) {
        MethodSpec.Builder builder=MethodSpec.methodBuilder(method.name);
        // if override annotation
        method.annotation.forEach(annotation -> {
            if (annotation instanceof org.openprovenance.prov.template.compiler.past.annotations.OverrideAnnotation) {
                builder.addAnnotation(Override.class);
            } else if (annotation instanceof org.openprovenance.prov.template.compiler.past.annotations.JsonIgnoreAnnotation) {
                builder.addAnnotation(JsonIgnore.class);
            }
        });

        method.modifiers.forEach(builder::addModifiers);
        if (!voidType(method.returnType)) {
            builder.returns(convert(method.returnType));
        }
        method.parameters.forEach(param -> builder.addParameter(convert(param.type), param.name));
        method.comments.forEach(comment -> builder.addJavadoc(comment.format, convertToPoet(comment.objects)));
        method.typeVariables.forEach(tv -> {
            if (tv instanceof TypeVariable) {
                builder.addTypeVariable(TypeVariableName.get(((TypeVariable)tv).name));
            } else {
                throw new IllegalArgumentException("Unsupported type variable: " + tv);
            }
        });


        method.exceptions.forEach(exc -> builder.addException(convert(exc)));

        method.body.forEach(statement -> {
               convertAndAddStatement(statement,builder);
        });
        return builder.build();
    }

    public interface Body {
        void addStatement(CodeBlock codeBlock);
        void beginControlFlow(String controlFlow, Object... args);
        void nextControlFlow(String controlFlow, Object... args);
        void endControlFlow();
        void endControlFlow(String controlFlow, Object... args);
    }

    public static class MethodBody implements Body {
        private final MethodSpec.Builder builder;

        public MethodBody(MethodSpec.Builder builder) {
            this.builder = builder;
        }

        @Override
        public void addStatement(CodeBlock codeBlock) {
            builder.addStatement(codeBlock);
        }

        @Override
        public void beginControlFlow(String controlFlow, Object... args) {
            builder.beginControlFlow(controlFlow, args);
        }

        @Override
        public void nextControlFlow(String controlFlow, Object... args) {
            builder.nextControlFlow(controlFlow, args);
        }

        @Override
        public void endControlFlow() {
            builder.endControlFlow();
        }

        @Override
        public void endControlFlow(String controlFlow, Object... args) {
            builder.endControlFlow(controlFlow, args);
        }
    }

    public static class LambdaBody implements Body {
        private final CodeBlock.Builder builder;

        public LambdaBody(CodeBlock.Builder builder) {
            this.builder = builder;
        }

        @Override
        public void addStatement(CodeBlock codeBlock) {
            builder.add("$L;\n",codeBlock);
        }

        @Override
        public void beginControlFlow(String controlFlow, Object... args) {
            builder.beginControlFlow(controlFlow, args);
        }

        @Override
        public void nextControlFlow(String controlFlow, Object... args) {
            builder.nextControlFlow(controlFlow, args);
        }

        @Override
        public void endControlFlow() {
            builder.endControlFlow();
        }

        @Override
        public void endControlFlow(String controlFlow, Object... args) {
            builder.endControlFlow(controlFlow, args);
        }
    }

    private void convertAndAddStatement(Statement statement, MethodSpec.Builder builder) {
        convertAndAddStatement(statement, new MethodBody(builder));
    }

    private void convertAndAddStatement(Statement statement, CodeBlock.Builder builder) {
        convertAndAddStatement(statement, new LambdaBody(builder));
    }



    private void convertAndAddStatement(Statement statement, Body builder) {
        switch (statement.statementKind) {

            case IF_STATEMENT -> {
                IfStatement ifStatement = (IfStatement) statement;
                CodeBlock conditionCode = convert(ifStatement.condition);

                builder.beginControlFlow("if ($L)", conditionCode);
                ifStatement.thenBlock.forEach(s -> convertAndAddStatement(s,builder));
                if (!ifStatement.elseBlock.isEmpty()) {
                    builder.nextControlFlow("else");
                    ifStatement.elseBlock.forEach(s -> convertAndAddStatement(s,builder));
                }
                builder.endControlFlow();
                return;
            }

            case TRY_CATCH -> {
                TryCatch tryCatch = (TryCatch) statement;
                builder.beginControlFlow("try");
                tryCatch.tryBlock.forEach(s -> convertAndAddStatement(s,builder));
                builder.nextControlFlow("catch ($T $L)", convert(tryCatch.exceptionType), tryCatch.exceptionName);
                tryCatch.catchBlock.forEach(s -> convertAndAddStatement(s,builder));
                builder.endControlFlow();
                return;
            }


            case FOR_LOOP ->  {
                ForLoop forLoop = (ForLoop) statement;
                CodeBlock initCode = convert(forLoop.initialization);
                CodeBlock conditionCode = convert(forLoop.condition);
                CodeBlock updateCode = convert(forLoop.update);
                builder.beginControlFlow("for ( $L; $L; $L )", initCode, conditionCode, updateCode);
                forLoop.body.forEach(s -> convertAndAddStatement(s,builder));
                builder.endControlFlow();
                return;
            }

            case DO_LOOP ->  {
                DoLoop doLoop = (DoLoop) statement;
                CodeBlock conditionCode = convert(doLoop.condition);
                builder.beginControlFlow("do ");
                doLoop.body.forEach(s -> convertAndAddStatement(s,builder));
                builder.endControlFlow(" while ( $L );", conditionCode);
                return;
            }

            case ITERATOR -> {
                Iterator iterator = (Iterator) statement;
                CodeBlock collectionCode = convert(iterator.collection);
                builder.beginControlFlow("for ( $T $L : $L )",
                        convert(iterator.parameter.type),
                        iterator.parameter.name,
                        collectionCode);
                iterator.body.forEach(s -> convertAndAddStatement(s,builder));
                builder.endControlFlow();
                return;
            }


        }
        builder.addStatement(convert(statement));
    }

    private Object [] convertToPoet(Object[] objects) {
        List<Object> converted=new LinkedList<>();
        for (Object o: objects) {
            if (o instanceof org.openprovenance.prov.template.compiler.past.type.TypeName) {
                converted.add(convert((org.openprovenance.prov.template.compiler.past.type.TypeName) o));
            } else {
                converted.add(o);
            }
        }
        return converted.toArray();
    }

    private boolean voidType(org.openprovenance.prov.template.compiler.past.type.TypeName returnType) {
        if (returnType instanceof org.openprovenance.prov.template.compiler.past.type.ClassName) {
            org.openprovenance.prov.template.compiler.past.type.ClassName cn = (org.openprovenance.prov.template.compiler.past.type.ClassName) returnType;
            return cn.packge.equals("past.lang") && cn.simpleName.equals("Void");
        }
        return false;
    }

    public CodeBlock convert(Statement statement) {
        switch (statement.statementKind) {
            case EXPRESSION_STATEMENT -> {
                Expression expr = (Expression) statement;
                return convert(expr);
            }
            case ASSIGNMENT -> {
                return convert((Assignment) statement);
            }
            case DEFINITION -> {
                return convert((Definition) statement);
            }
            case RETURN ->  {
                Return ret = (Return) statement;
                CodeBlock valueCode = convert(ret.expression);
                return CodeBlock.of("return $L", valueCode);
            }
            case COMMENT ->  {
                Comment comment = (Comment) statement;
                return CodeBlock.of("// " + comment.format, comment.objects);
            }
            case IF_STATEMENT -> {
                throw new IllegalArgumentException("If statements should be handled separately to ensure proper formatting");
                /*
                IfStatement ifStatement = (IfStatement) statement;
                CodeBlock conditionCode = convert(ifStatement.condition);

                CodeBlock.Builder builder = CodeBlock.builder();

                builder.beginControlFlow("if ($L)", conditionCode);
                if (ifStatement.thenBlock.size()==1) {
                    builder.add(CodeBlock.of("$L ;",convert(ifStatement.thenBlock.get(0))));
                } else {
                    ifStatement.thenBlock.stream().map(s -> CodeBlock.of("$L;\n", convert(s))).forEach(builder::add);
                }
                if (!ifStatement.elseBlock.isEmpty()) {
                    builder.nextControlFlow("else");
                    if (ifStatement.elseBlock.size()==1) {
                        builder.add(CodeBlock.of("$L ;", convert(ifStatement.elseBlock.get(0))));
                    } else {
                        ifStatement.elseBlock.stream().map(s -> CodeBlock.of("$L;\n", convert(s))).forEach(builder::add);
                    }

                }
                builder.endControlFlow();
                return builder.build();

                 */
            }

            case FOR_LOOP ->  {
                throw new IllegalArgumentException("ForLoop statements should be handled separately to ensure proper formatting");
                        /*
                ForLoop forLoop = (ForLoop) statement;
                CodeBlock initCode = convert(forLoop.initialization);
                CodeBlock conditionCode = convert(forLoop.condition);
                CodeBlock updateCode = convert(forLoop.update);
                CodeBlock.Builder builder= CodeBlock.builder();
                builder.beginControlFlow("for ( $L; $L; $L )", initCode, conditionCode, updateCode);
                forLoop.body.stream().map(s -> CodeBlock.of("$L;\n", convert(s))).forEach(builder::add);
                builder.endControlFlow();
                return builder.build();

                         */
            }

            case DO_LOOP ->  {
                throw new IllegalArgumentException("Do loops should be handled separately to ensure proper formatting");
                /*
                DoLoop doLoop = (DoLoop) statement;
                CodeBlock conditionCode = convert(doLoop.condition);
                CodeBlock.Builder builder= CodeBlock.builder();
                builder.beginControlFlow("do ");
                doLoop.body.stream().map(s -> CodeBlock.of("$L;\n", convert(s))).forEach(builder::add);
                builder.endControlFlow(" while ( $L );", conditionCode);
                return builder.build();

                 */
            }

            case ITERATOR -> {
                throw new IllegalArgumentException("Iterators should be handled separately to ensure proper formatting");
                /*
                Iterator iterator = (Iterator) statement;
                CodeBlock collectionCode = convert(iterator.collection);
                CodeBlock.Builder builder= CodeBlock.builder();

                builder.beginControlFlow("for ( $T $L : $L )",
                        convert(iterator.parameter.type),
                        iterator.parameter.name,
                        collectionCode);
                iterator.body.stream().map(s -> CodeBlock.of("$L;\n", convert(s))).forEach(builder::add);

                builder.endControlFlow();
                return builder.build();

                 */
            }

            case TRY_CATCH -> {
               throw new IllegalArgumentException("TryCatch statements should be handled separately to ensure proper formatting");
            }

            case THROW -> {
                ThrowStatement throwStmt = (ThrowStatement) statement;
                CodeBlock exprCode = convert(throwStmt.expression);
                return CodeBlock.of("throw $L", exprCode);
            }

            case SUPER_CONSTRUCTOR_CALL -> {
                SuperConstructorCall superCall = (SuperConstructorCall) statement;
                CodeBlock argsCode = CodeBlock.join(
                        superCall.arguments.stream().map(this::convert).collect(Collectors.toList()),
                        ",");
                return CodeBlock.of("super($L)", argsCode);
            }
        }
        throw new IllegalArgumentException("Statement conversion not supported yet " + statement);
    }

    public CodeBlock convert(Assignment assignment) {
        CodeBlock leftHandCode = convert(assignment.leftHandExpression);
        CodeBlock valueCode = convert(assignment.value);
        if (assignment.modifiers.contains(javax.lang.model.element.Modifier.FINAL)) {
            return CodeBlock.of("final $L=$L", leftHandCode, valueCode);
        } else {
            return CodeBlock.of("$L=$L", leftHandCode, valueCode);
        }
    }


    public CodeBlock convert(Definition definition) {
        CodeBlock leftHandCode = convert(definition.leftHandExpression);
        CodeBlock valueCode = convert(definition.value);
        if (definition.modifiers.contains(javax.lang.model.element.Modifier.FINAL)) {
            org.openprovenance.prov.template.compiler.past.type.TypeName type = definition.type;
            if (type != null) {
                TypeName typeName = convert(type);
                return CodeBlock.of("final $T $L=$L", typeName, leftHandCode, valueCode);
            } else {
                return CodeBlock.of("final $L=$L", leftHandCode, valueCode);
            }
        } else {
            if (definition.type != null) {
                TypeName typeName = convert(definition.type);
                return CodeBlock.of("$T $L=$L", typeName, leftHandCode, valueCode);
            } else {
                throw new IllegalArgumentException("Definition without type is not supported: " + definition);
            }
        }
    }

    public FieldSpec convert(Field field) {
        FieldSpec.Builder builder=FieldSpec.builder(convert(field.type), field.name);
        field.modifiers.forEach(builder::addModifiers);
        if (field.initialiser!=null) builder.initializer(convert(field.initialiser));
        field.comments.forEach(comment -> builder.addJavadoc(comment.format, comment.objects));

        return builder.build();
    }

    public CodeBlock convert(Expression expression) {
        if (expression==null) {
            throw new IllegalArgumentException("Null expression" + expression);
        }
        switch (expression.expressionKind) {
            case VALUE -> {
            }
            case VARIABLE -> {
                Variable variable = (Variable) expression;
                return CodeBlock.of("$N", variable.name);
            }
            case METHOD_CALL -> {
                MethodCall methodCall = (MethodCall) expression;
                return convert(methodCall);
            }
            case CLASS_INSTANTIATION -> {
            }
            case PARAMETER_REFERENCE -> {
            }
            case CONSTANT -> {
                Constant constant = (Constant) expression;
                switch (constant.constantType) {
                    case INTEGER -> {
                        return CodeBlock.of("$L", constant.value);
                    }
                    case BOOLEAN -> {
                        return CodeBlock.of("$L", constant.value);
                    }
                    case BOOL -> {
                        return CodeBlock.of("$L", constant.value);
                    }
                    case STRING -> {
                        return CodeBlock.of("$S", constant.value);
                    }
                    case FLOAT -> {
                        return CodeBlock.of("$LF", constant.value);
                    }
                    case LONG -> {
                        return CodeBlock.of("$LL", constant.value);
                    }
                    case DOUBLE -> {
                        return CodeBlock.of("$Ld", constant.value);
                    }
                    case NULL -> {
                        return CodeBlock.of("null");
                    }
                    default ->  throw new IllegalArgumentException("Unsupported constant type: " + constant.constantType);
                }
            }
            case CAST -> {
                CastExpression cast = (CastExpression) expression;
                TypeName typeName = convert(cast.targetType);
                CodeBlock exprCode = convert(cast.expression);
                return CodeBlock.of("($T)$L", typeName,  exprCode);
            }
            case ARRAY_INITIALISER -> {
                ArrayInitialiser arrayInitialiser = (ArrayInitialiser) expression;
                CodeBlock elementsCode = CodeBlock.join(
                        arrayInitialiser.values.stream().map(this::convert).collect(Collectors.toList()),
                        ",");
                TypeName elementTypeName = convert(arrayInitialiser.elementType);
                return CodeBlock.of("new $T[] { $L }", elementTypeName, elementsCode);
            }
            case LAMBDA_EXPRESSION -> {
                LambdaExpression lambda = (LambdaExpression) expression;

                CodeBlock.Builder builder = CodeBlock.builder();
                builder.add((lambda.parameters.size()==1)? "$L -> {\n": "($L) -> {\n",
                        lambda.parameters.stream().map(p -> p.name).reduce((a, b) -> a + ", " + b).orElse(""));
                builder.indent();
                //lambda.body.forEach(s -> builder.add("$L;\n", convert(s)));
                lambda.body.forEach(s -> convertAndAddStatement(s,builder));
                builder.unindent();
                builder.add("}");

                return builder.build();


                /*

                return CodeBlock.of(
                        (lambda.parameters.size()==1)? "$L -> { $L }": "($L) -> {\n$L }",
                        lambda.parameters.stream().map(p -> p.name).reduce((a, b) -> a + ", " + b).orElse(""),
                        CodeBlock.join(lambda.body.stream().map(s -> CodeBlock.of("$L;", convert(s))).collect(Collectors.toList()), "\n"));
                        //CodeBlock.of((lambda.body.size()==1)? ";" : "")) ; // handles the special case of single statement body: TO CHECK, should it be for all?

                 */
            }
            case ARRAY_ACCESSOR -> {
                ArrayAccessor arrayAccessor = (ArrayAccessor) expression;
                CodeBlock arrayCode = convert(arrayAccessor.arrayExpression);
                CodeBlock indexCode = convert(arrayAccessor.indexExpression);
                return CodeBlock.of("$L[$L]", arrayCode, indexCode);
            }
            case POST_INCREMENT -> {
                PostIncrement postIncrement = (PostIncrement) expression;
                CodeBlock exprCode = convert(postIncrement.expression);
                if (postIncrement.increment<0) {
                    return CodeBlock.of("$L--", exprCode);
                } else {
                    return CodeBlock.of("$L++", exprCode);
                }
            }
            case BINARY_OP -> {
                BinaryOp binaryOperation = (BinaryOp) expression;
                CodeBlock leftCode = convert(binaryOperation.left);
                CodeBlock rightCode = convert(binaryOperation.right);
                if (binaryOperation.op.equals(INSTANCEOF)) {
                    throw new IllegalArgumentException("Use InstanceOf expression instead of binary operator for instanceof checks");
                } if (binaryOperation.op.equals("Objects.equals")) {
                    return CodeBlock.of("$T.equals($L,$L)", Objects.class, leftCode, rightCode);
                }
                else {
                    return CodeBlock.of("($L $L $L)", leftCode, binaryOperation.op, rightCode);
                }
            }

            case IF_EXPRESSION ->  {
                IfExpression ifExpression = (IfExpression) expression;
                CodeBlock conditionCode = convert(ifExpression.condition);
                CodeBlock thenCode = convert(ifExpression.thenExpression);
                CodeBlock elseCode = convert(ifExpression.elseExpression);
                return CodeBlock.of("($L ? $L : $L)", conditionCode, thenCode, elseCode);
            }

            case ARRAY_ALLOCATOR -> {
                ArrayAllocator arrayAllocator = (ArrayAllocator) expression;
                CodeBlock sizeCode = convert(arrayAllocator.size);
                if (arrayAllocator.elementType instanceof ArrayType) {
                    // multi-dimensional array
                    ArrayType at = (ArrayType) arrayAllocator.elementType;
                    TypeName elementTypeName = convert(at.elementType);
                    return CodeBlock.of("new $T[$L][]", elementTypeName, sizeCode);
                } else {
                    TypeName elementTypeName = convert(arrayAllocator.elementType);
                    return CodeBlock.of("new $T[$L]", elementTypeName, sizeCode);
                }
            }

            case INSTANCEOF ->  {
                InstanceOf instanceOf = (InstanceOf) expression;
                CodeBlock exprCode = convert(instanceOf.expression);
                return CodeBlock.of("($L instanceof $T)", exprCode, convert(instanceOf.type));
            }
        }
        throw new IllegalArgumentException("Expression conversion not supported yet " + expression.expressionKind);
    }

    public CodeBlock convert(MethodCall methodCall) {
        switch (methodCall.operatorKind) {
            case NO_OPERATOR -> {
                CodeBlock argsCode = CodeBlock.join(
                        methodCall.arguments.stream().map(this::convert).collect(Collectors.toList()),
                        ",");
                return CodeBlock.of("$L($L)", methodCall.methodName, argsCode);
            }
            case CONSTRUCTOR_CALL -> {
                CodeBlock argsCode = CodeBlock.join(
                        methodCall.arguments.stream().map(this::convert).collect(Collectors.toList()),
                        ",");
                if ((methodCall.className instanceof org.openprovenance.prov.template.compiler.past.type.ParameterizedType)
                    && (((ParameterizedType)methodCall.className).typeArguments.length==0)) {
                    ParameterizedType pt = (ParameterizedType) methodCall.className;

                    return CodeBlock.of("new $T<>($L)", convert(pt.rawType), argsCode);

                } else if (methodCall.clazz!=null) {
                    return emitAnonymous(methodCall.clazz) ;
                } else {
                    return CodeBlock.of("new $T($L)", convert(methodCall.className), argsCode);
                }
            }
            case FUNCTIONAL_INTERFACE_CALL  -> {
                CodeBlock argsCode = CodeBlock.join(
                        methodCall.arguments.stream().map(this::convert).collect(Collectors.toList()),
                        ",");
                CodeBlock operator = convert(methodCall.object);
                return CodeBlock.of("$L.$L($L)", operator, methodCall.methodName, argsCode);
            }
            case OPERATOR_VARIABLE -> {
                CodeBlock argsCode = CodeBlock.join(
                        methodCall.arguments.stream().map(this::convert).collect(Collectors.toList()),
                        ",");
                Variable variable = (Variable)methodCall.object;
                assert variable != null;
                if (!variable.annotation.isEmpty()) {
                    for (var annot: variable.annotation) {
                        if (annot instanceof Ignore) {
                            return CodeBlock.of("$L($L)", methodCall.methodName, argsCode);
                        }
                    }
                }
                CodeBlock operator = convert(methodCall.object);

                return CodeBlock.of("$L.$L($L)", operator, methodCall.methodName, argsCode);
            }
            case OBJECT_METHOD_CALL -> {
                CodeBlock argsCode = CodeBlock.join(
                        methodCall.arguments.stream().map(this::convert).collect(Collectors.toList()),
                        ",");
                CodeBlock objectCode = convert(methodCall.object);
                if (methodCall.object instanceof CastExpression) {
                    // ensure that the cast is parenthesised to avoid issues with operator precedence
                    return CodeBlock.of("($L).$L($L)", objectCode, methodCall.methodName, argsCode);
                } else {
                    return CodeBlock.of("$L.$L($L)", objectCode, methodCall.methodName, argsCode);
                }
            }
            case SUPER_METHOD_CALL -> {
                CodeBlock argsCode = CodeBlock.join(
                        methodCall.arguments.stream().map(this::convert).collect(Collectors.toList()),
                        ",");

                if (methodCall.methodName==null) {
                    return CodeBlock.of("super($L)", argsCode);
                } else {
                    return CodeBlock.of("super.$L($L)", methodCall.methodName, argsCode);
                }
            }
            case OBJECT_ACCESSOR -> {
                if (methodCall.className != null) {
                    return CodeBlock.of("$T.$L", convert(methodCall.className), methodCall.methodName);
                } else if (methodCall.object instanceof Variable) {
                    Variable var = (Variable) methodCall.object;
                    return CodeBlock.of("$L.$N", var.name, methodCall.methodName);
                } else if (methodCall.object instanceof MethodCall) {
                    MethodCall mc = (MethodCall) methodCall.object;
                    return CodeBlock.of("$L.$N", convert(mc), methodCall.methodName);
                } else if (methodCall.object instanceof CastExpression) {
                    CastExpression cast = (CastExpression) methodCall.object;
                    return CodeBlock.of("($L).$N", convert(cast), methodCall.methodName);
                } else {
                    throw new IllegalArgumentException("Unsupported object type in accessor: " + methodCall.object);
                }
            }
            case STATIC_METHOD_CALL -> {
                if (methodCall.className!=null &&
                        Objects.equals(org.openprovenance.prov.template.compiler.past.type.ClassName.STRING.simpleName,
                                ((org.openprovenance.prov.template.compiler.past.type.ClassName) methodCall.className).simpleName)
                        && "concat".equals(methodCall.methodName)) {

                    CodeBlock argsCode = CodeBlock.join(
                            methodCall.arguments.stream().map(this::convert).collect(Collectors.toList()),
                            "+");

                    return argsCode;
                }
                CodeBlock argsCode = CodeBlock.join(
                        methodCall.arguments.stream().map(this::convert).collect(Collectors.toList()),
                        ",");
                return CodeBlock.of("$T.$L($L)", convert(methodCall.className), methodCall.methodName, argsCode);
            }
        }
        throw new IllegalArgumentException("MethodCall conversion not supported yet " + methodCall.operatorKind);
    }

    public TypeName convert(org.openprovenance.prov.template.compiler.past.type.TypeName typeName) {
        switch (typeName.typeKind) {
            case CLASS -> {
                org.openprovenance.prov.template.compiler.past.type.ClassName cn = (org.openprovenance.prov.template.compiler.past.type.ClassName) typeName;
                return convertClass(cn);
            }
            case VARIABLE -> {
                TypeVariable tn = (TypeVariable) typeName;
                return convertTypeVariable(tn);
            }
            case ARRAY -> {
                ArrayType at = (ArrayType) typeName;
                return ArrayTypeName.of(convert(at.elementType));
            }
            case PARAMETERIZED -> {
                ParameterizedType pt = (ParameterizedType) typeName;
                return ParameterizedTypeName.get(
                        (ClassName) convert(pt.rawType),
                        Arrays.stream(pt.typeArguments).map(this::convert).toArray(TypeName[]::new));
            }
            default -> throw new IllegalArgumentException("conversion not supported yet");
        }
    }

    private TypeVariableName convertTypeVariable(TypeVariable tn) {
        if (tn.bounds.isEmpty()) {
            return TypeVariableName.get(tn.name);
        } else {
            return TypeVariableName.get(tn.name).withBounds(tn.bounds.stream().map(this::convert).toArray(TypeName[]::new));
        }
    }

    public TypeName convertClass(org.openprovenance.prov.template.compiler.past.type.ClassName cn) {
        switch (cn.packge) {
            case "past.lang" -> {
                switch (cn.simpleName) {
                    case "Object" ->  { return ClassName.get(Object.class); }
                    case "String" ->  { return ClassName.get(String.class); }
                    case "Integer" ->  { return ClassName.get(Integer.class); }
                    case "Double" ->  { return ClassName.get(Double.class); }
                    case "Long" ->  { return ClassName.get(Long.class); }
                    case "bool" ->  { return TypeName.get(boolean.class); }
                    case "Boolean" ->  { return ClassName.get(Boolean.class); }
                    case "Class" ->  { return ClassName.get(java.lang.Class.class); }
                    case "Void" ->  { return ClassName.get(Void.class); }
                    case "Supplier" ->  { return ClassName.get(java.util.function.Supplier.class); }
                    case "Consumer" ->  { return ClassName.get(java.util.function.Consumer.class); }
                    case "BiConsumer" ->  { return ClassName.get(java.util.function.BiConsumer.class); }
                    case "Function" ->  { return ClassName.get(java.util.function.Function.class); }
                    case "BiFunction" ->  { return ClassName.get(java.util.function.BiFunction.class); }
                    case "long" ->  { return TypeName.get(long.class); }
                    case "double" ->  { return TypeName.get(double.class); }
                    case "int" ->  { return TypeName.get(int.class); }
                    case "int[]" ->  { return ArrayTypeName.of(TypeName.get(int.class)); }
                    case "System" ->  { return ClassName.get(System.class); }
                    case "AtomicInteger" ->  { return TypeName.get(AtomicInteger.class); }
                    default ->  { /* continue */ }
                }
            }
            case "past.util" -> {
                switch (cn.simpleName) {
                    case "List"          ->  { return ClassName.get(List.class); }
                    case "LinkedList"    ->  { return ClassName.get(LinkedList.class); }
                    case "ArrayList"     ->  { return ClassName.get(ArrayList.class); }
                    case "Void"          ->  { return ClassName.get(Void.class); }
                    case "StringBuilder" ->  { return ClassName.get(StringBuilder.class); }
                    case "Map"           ->  { return ClassName.get(Map.class); }
                    case "Set"           ->  { return ClassName.get(Set.class); }
                    case "HashSet"       ->  { return ClassName.get(HashSet.class); }
                    case "Collection"    ->  { return ClassName.get(Collection.class); }
                    case "HashMap"       ->  { return ClassName.get(HashMap.class); }
                    default ->  { /* continue */ }
                }
            }
            case "past.exception" -> {
                switch (cn.simpleName) {
                    case "UnsupportedOperationException" ->  { return ClassName.get(UnsupportedOperationException.class); }
                    case "IllegalArgumentException" ->  { return ClassName.get(IllegalArgumentException.class); }
                    case "IllegalStateException" ->  { return ClassName.get(IllegalStateException.class); }
                    case "Exception" ->  { return ClassName.get(Exception.class); }
                    case "RuntimeException" ->  { return ClassName.get(RuntimeException.class); }
                    default ->  { /* continue */ }
                }
            }
            case "java.sql" -> {
                switch (cn.simpleName) {
                    case "ResultSet" ->  { return ClassName.get(java.sql.ResultSet.class); }
                    case "SQLException" ->  { return ClassName.get(java.sql.SQLException.class); }
                    case "ResultSetMetaData" ->  { return ClassName.get(java.sql.ResultSetMetaData.class); }
                    default ->  { /* continue */ }
                }
            }
            case "java.lang" ->   { /* continue */ }
            case "java.util" ->   { /* continue */ }
            default ->  { /* continue */ }
        }
        return ClassName.get(cn.packge, cn.simpleName);
    }
}
