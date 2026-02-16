package org.openprovenance.prov.template.compiler.past;


import org.openprovenance.prov.template.compiler.past.type.ClassName;
import org.openprovenance.prov.template.compiler.past.type.TypeName;

import java.util.List;

public class MethodCall extends Expression {
    public final Expression object;
    public final String methodName;
    public final List<Expression> arguments;
    public final MethodCallKind operatorKind;
    public final TypeName className;
    public final Class clazz;

    public enum MethodCallKind { NO_OPERATOR, CONSTRUCTOR_CALL, OPERATOR_VARIABLE, OBJECT_METHOD_CALL, STATIC_METHOD_CALL, FUNCTIONAL_INTERFACE_CALL, OBJECT_ACCESSOR, SUPER_METHOD_CALL };

    public MethodCall(String methodName, List<Expression> arguments) {
        this.methodName=methodName;
        this.arguments=arguments;
        this.object=null;
        this.operatorKind =MethodCallKind.NO_OPERATOR;
        this.expressionKind=ExpressionKind.METHOD_CALL;
        this.className=null;
        this.clazz=null;
    }
    public MethodCall(String object,String methodName, List<Expression> arguments) {
        if (!"super".equals(object)) {
            throw new IllegalArgumentException("Only 'super' is allowed as object name in this constructor");
        }
        this.methodName=methodName;
        this.arguments=arguments;
        this.object=null;
        this.operatorKind =MethodCallKind.SUPER_METHOD_CALL;
        this.expressionKind=ExpressionKind.METHOD_CALL;
        this.className=null;
        this.clazz=null;
    }

    public MethodCall(String object, TypeName className, List<Expression> arguments) {
        if (!"new".equals(object)) {
            throw new IllegalArgumentException("Only 'new' is allowed as object name in this constructor");
        }
        this.object=null;
        this.methodName=null;
        this.className=className;
        this.arguments=arguments;
        this.operatorKind =MethodCallKind.CONSTRUCTOR_CALL;
        this.expressionKind=ExpressionKind.METHOD_CALL;
        this.clazz=null;

    }
    public MethodCall(String object, Class clazz, List<Expression> arguments) {
        if (!"new".equals(object)) {
            throw new IllegalArgumentException("Only 'new' is allowed as object name in this constructor");
        }
        this.clazz=clazz;
        this.object=null;
        this.methodName=null;
        this.className=null;
        this.arguments=arguments;
        this.operatorKind =MethodCallKind.CONSTRUCTOR_CALL;
        this.expressionKind=ExpressionKind.METHOD_CALL;
    }

    public MethodCall(Variable object, String methodName, List<Expression> arguments) {
        this.object=object;
        this.methodName=methodName;
        this.arguments=arguments;
        this.operatorKind =MethodCallKind.OPERATOR_VARIABLE;
        this.expressionKind=ExpressionKind.METHOD_CALL;
        this.className=null;
        this.clazz=null;

    }

    public MethodCall(Expression object, String methodName, List<Expression> arguments, boolean functionalInterfaceCall) {
        this.object=object;
        this.methodName=methodName;
        this.arguments=arguments;
        this.operatorKind = (functionalInterfaceCall)?MethodCallKind.FUNCTIONAL_INTERFACE_CALL: MethodCallKind.OPERATOR_VARIABLE;
        this.expressionKind=ExpressionKind.METHOD_CALL;
        this.className=null;
        this.clazz=null;

    }



    public MethodCall(ClassName object, String methodName, List<Expression> arguments) {
        this.object=null;
        this.methodName=methodName;
        this.arguments=arguments;
        this.operatorKind =MethodCallKind.STATIC_METHOD_CALL;
        this.expressionKind=ExpressionKind.METHOD_CALL;
        this.className=object;
        this.clazz=null;


    }

    public MethodCall(MethodCall object, String methodName, List<Expression> arguments) {
        this.methodName=methodName;
        this.arguments=arguments;
        this.object=object;
        this.operatorKind =MethodCallKind.OBJECT_METHOD_CALL;
        this.expressionKind=ExpressionKind.METHOD_CALL;
        this.className=null;
        this.clazz=null;
    }
    public MethodCall(ArrayAccessor object, String methodName, List<Expression> arguments) {
        this.methodName=methodName;
        this.arguments=arguments;
        this.object=object;
        this.operatorKind =MethodCallKind.OBJECT_METHOD_CALL;
        this.expressionKind=ExpressionKind.METHOD_CALL;
        this.className=null;
        this.clazz=null;

    }
    public MethodCall(CastExpression object, String methodName, List<Expression> arguments) {
        this.methodName=methodName;
        this.arguments=arguments;
        this.object=object;
        this.operatorKind =MethodCallKind.OBJECT_METHOD_CALL;
        this.expressionKind=ExpressionKind.METHOD_CALL;
        this.className=null;
        this.clazz=null;

    }

    public MethodCall(ClassName className, String accessorName) {
        this.methodName=accessorName;
        this.arguments=null;
        this.object=null;
        this.operatorKind =MethodCallKind.OBJECT_ACCESSOR;
        this.expressionKind=ExpressionKind.METHOD_CALL;
        this.className=className;
        this.clazz=null;

    }

    public MethodCall(Variable variable, String accessorName) {
        this.methodName=accessorName;
        this.arguments=null;
        this.object=variable;
        this.operatorKind =MethodCallKind.OBJECT_ACCESSOR;
        this.expressionKind=ExpressionKind.METHOD_CALL;
        this.className=null;
        this.clazz=null;

    }

    public MethodCall(Expression object, String accessorName) {
        this.methodName=accessorName;
        this.arguments=null;
        this.object=object;
        this.operatorKind =MethodCallKind.OBJECT_ACCESSOR;
        this.expressionKind=ExpressionKind.METHOD_CALL;
        this.className=null;
        this.clazz=null;

    }


    @Override
    public String toString() {
        return "MethodCall{" +
                "object=" + object +
                ", methodName='" + methodName + '\'' +
                ", arguments=" + arguments +
                ", operatorKind=" + operatorKind +
                ", className=" + className +
                ", clazz=" + clazz +
                ", expressionKind=" + expressionKind +
                ", inferredType=" + inferredType +
                ", statementKind=" + statementKind +
                '}';
    }

    public static MethodCall CONSTRUCTOR_CALL(TypeName className, List<Expression> arguments) {
        return new MethodCall("new", className, arguments);
    }
    public static MethodCall SUPER_METHOD_CALL(String methodName, List<Expression> arguments) {
        return new MethodCall("super", methodName, arguments);
    }
    public static MethodCall CONSTRUCTOR_CALL(Class clazz, List<Expression> arguments) {
        return new MethodCall("new", clazz, arguments);
    }
    public static MethodCall METHOD_CALL(Variable object, String methodName, List<Expression> arguments) {
        return new MethodCall(object, methodName, arguments);
    }
    public static MethodCall METHOD_CALL(ArrayAccessor object, String methodName, List<Expression> arguments) {
        return new MethodCall(object, methodName, arguments);
    }
    public static MethodCall METHOD_CALL(CastExpression object, String methodName, List<Expression> arguments) {
        return new MethodCall(object, methodName, arguments);
    }
    public static MethodCall FUNCTIONAL_METHOD_CALL(Expression object, String methodName, List<Expression> arguments) {
        return new MethodCall(object, methodName, arguments,true);
    }
    public static MethodCall METHOD_CALL(MethodCall object, String methodName, List<Expression> arguments) {
        return new MethodCall(object, methodName, arguments);
    }
    public static MethodCall METHOD_CALL(Variable object, String methodName, Expression... arguments) {
        return new MethodCall(object, methodName, List.of(arguments));
    }
    public static MethodCall FUNCTIONAL_METHOD_CALL(Variable object, String methodName, Expression... arguments) {
        return new MethodCall(object, methodName, List.of(arguments),true);
    }
    public static MethodCall METHOD_CALL(ClassName object, String methodName, Expression... arguments) {
        return new MethodCall(object, methodName, List.of(arguments));
    }
    public static MethodCall METHOD_CALL(ClassName object, String methodName, List<Expression> arguments) {
        return new MethodCall(object, methodName, arguments);
    }
    public static MethodCall METHOD_CALL(Variable object, String methodName) {
        return new MethodCall(object, methodName);
    }
    public static MethodCall METHOD_CALL(ClassName className, String accessorName) {
        return new MethodCall(className, accessorName);
    }
    public static MethodCall METHOD_CALL(String methodName, List<Expression> arguments) {
        return new MethodCall(methodName, arguments);
    }
    public static MethodCall METHOD_CALL(Expression object, String methodName) {
        return new MethodCall(object, methodName);
    }


}
