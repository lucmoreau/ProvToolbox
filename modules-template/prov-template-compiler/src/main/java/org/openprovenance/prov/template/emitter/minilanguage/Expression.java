package org.openprovenance.prov.template.emitter.minilanguage;

import org.openprovenance.prov.template.emitter.Element;
import org.openprovenance.prov.template.emitter.Pair;
import org.openprovenance.prov.template.emitter.Token;
import org.openprovenance.prov.template.emitter.minilanguage.emitters.Python;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static org.openprovenance.prov.template.compiler.common.CompilerCommon.*;
import static org.openprovenance.prov.template.emitter.Pair.*;

public class Expression extends Statement {

    public Expression(List<Element> elements) {
        super(elements);
    }

    static public Expression makeExpression(List<Element> elements) {
        if (elements.size()==1 && elements.get(0) instanceof Pair && getFormat(elements.get(0)).equals("$N")) {
            return new Symbol((String)getArg(elements.get(0)), elements);
        } else if (elements.size()==1 && elements.get(0) instanceof Pair && getFormat(elements.get(0)).equals("$S")) {
            return new Constant((String)getArg(elements.get(0)), elements);
        } else if (elements.size()==1 && elements.get(0) instanceof Pair && getFormat(elements.get(0)).equals("$L")) {
            return ((List<Expression>) getArg(elements.get(0))).get(0);
        } else if (elements.size()>1
                && isPair(elements.get(0))
                && MARKER_LAMBDA.equals(getArg(elements.get(0)))) {
            List<Element> ll=elements.subList(1,elements.size());
            assert ll.size()==1;
            List<Statement> parameters_and_body=((List<Statement>) getArg(ll.get(0))).stream().map(x->makeStatement(x.getElements())).collect(Collectors.toList());
            List<Element> parameterElements=parameters_and_body.get(0).getElements();
            assert parameterElements.size()>4;
            assert parameterElements.get(0).equals(new Token("("));
            assert parameterElements.get(1) instanceof Pair;
            assert parameterElements.get(2).equals(new Token(") -> "));
            List<Element> _params=((List<Expression>)getArg(parameterElements.get(1))).get(0).getElements();
            List<Parameter> parameters=new LinkedList<>();
            Parameter parameter=null;
            for (Element e: _params) {
                if (e instanceof Pair) {
                    Pair p=(Pair)e;
                    if (p.getFormatPart().equals("$T")) {
                        parameter=new Parameter(null, p.getArg().toString());
                    } else if (p.getFormatPart().equals("$N")) {
                        assert parameter != null;
                        parameter.name = p.getArg().toString();
                        parameters.add(parameter);
                    }
                } else if (e instanceof Token) {
                    assert getToken(e).equals(", ");
                } else {
                    throw new UnsupportedOperationException("Cannot handle "+e);
                }
            }


            List<Statement> body=parameters_and_body.subList(1,parameters_and_body.size());

            return new Lambda(parameters, body, elements);

        }  else if (elements.size()>1
                && isPair(elements.get(0))
                && "new".equals(getArg(elements.get(0)))) {

            if (elements.size()>3
                    && isPair(elements.get(2))
                && MARKER_ARRAY.equals(getArg(elements.get(2)))) {

                List<Element> allArgs=elements.subList(3, elements.size());
                return new Constructor(getArg(elements.get(1)).toString(), makeExpressions(allArgs), elements);
            } else {
                return new Constructor(getArg(elements.get(1)).toString(), elements);
            }
        } else if (elements.size()>3 && elements.get(1) instanceof Token && getToken(elements.get(1)).equals(".")) {
            List<Element> allArgsIncludingMarkers = elements.subList(3, elements.size());
            if (allArgsIncludingMarkers.size()==1 && allArgsIncludingMarkers.get(0).equals(new Token("()"))) {
                // method call with no arguments
                return new MethodCall(((Pair)elements.get(0)).getArg(), (String) ((Pair)elements.get(2)).getArg(), new LinkedList<>(), elements);
            } else if (allArgsIncludingMarkers.size()==3
                    && allArgsIncludingMarkers.get(0).equals(new Token("("))
                    && allArgsIncludingMarkers.get(2).equals(new Token(")"))) {
                // method call with one argument
                Object arg=getArg( allArgsIncludingMarkers.get(1));
                String format=getFormat( allArgsIncludingMarkers.get(1));
                if (arg instanceof List) {
                    List<Statement> ll = (List<Statement>) arg;
                    assert ll.size() == 1;
                    List<Element> _elements = ll.get(0).getElements();
                    List<Object> args = _elements.stream().filter(x -> x instanceof Pair).map(x -> ((Pair) x).getArg()).collect(Collectors.toList());
                    return new MethodCall(getArg(elements.get(0)), (String) getArg(elements.get(2)), args, elements);
                } else if (arg instanceof String) {
                    List<Object> args = new LinkedList<>();
                    if (format.equals("$S")) {
                        args.add("\"" + arg + "\"");
                    } else {
                        args.add(arg);
                    }
                    return new MethodCall(((Pair) elements.get(0)).getArg(), (String) ((Pair) elements.get(2)).getArg(), args, elements);

                } else {
                    throw new UnsupportedOperationException("Cannot handle "+arg);
                }
            } else if (allArgsIncludingMarkers.get(0).equals(new Token("("))) {
                return new MethodCall(((Pair) elements.get(0)).getArg(), (String) ((Pair) elements.get(2)).getArg(), makeExpressions(allArgsIncludingMarkers.subList(1,allArgsIncludingMarkers.size())), elements);
            } else if (allArgsIncludingMarkers.get(0).equals(new Token("."))) {
                // accessor in object position (accessor is marked by argument=null as opposed to empty list())
                MethodCall object=new MethodCall(((Pair) elements.get(0)).getArg(), (String) ((Pair) elements.get(2)).getArg(), null, elements);
                allArgsIncludingMarkers.add(0, new Pair("$L", object));
                //System.out.println("Found object " + object);
                //System.out.println(" recursing with " + allArgsIncludingMarkers);
                return makeExpression(allArgsIncludingMarkers);

            } else if (allArgsIncludingMarkers.get(0).equals(new Token("()."))) {
                // method call in object position (with empty argument list)
                MethodCall object=new MethodCall(((Pair) elements.get(0)).getArg(), (String) ((Pair) elements.get(2)).getArg(), new LinkedList<>(), elements);
                allArgsIncludingMarkers.remove(0);
                allArgsIncludingMarkers.add(0, new Token("."));
                allArgsIncludingMarkers.add(0, new Pair("$L", object));
                //System.out.println("Found object " + object);
                //System.out.println(" recursing with " + allArgsIncludingMarkers);
                return makeExpression(allArgsIncludingMarkers);

            }
            else {
                // falling through

                System.out.println("Falling through " + elements);
                System.out.println(" Falling through " + allArgsIncludingMarkers);


                return makeExpression(allArgsIncludingMarkers);

            }
        } else if (elements.size()>3 && tokenExists(elements, 1, "(")) {
            List<Element> allArgs = elements.subList(2, elements.size());
            // method call with more arguments
            List<Expression> arguments = makeExpressions(allArgs);
            assert arguments.size() == 1;
            List<Element> _elements = arguments.get(0).getElements();
            if ("new".equals(getArg(_elements.get(0)))) {
                return new MethodCall(null, (String) ((Pair) elements.get(0)).getArg(), arguments, elements);
            } else {
                List<Object> args = _elements.stream().filter(x -> x instanceof Pair).map(x -> ((Pair) x).getArg()).collect(Collectors.toList());
                return new MethodCall(null, (String) ((Pair) elements.get(0)).getArg(), args, elements);
            }
        }
        else if (elements.size()>=3 && tokenExists(elements, 1, "==")) {  // binary operator
            //infix expression
            return new BinaryOp(getArg(elements.get(0)), (String) getToken(elements.get(1)), getArg(elements.get(2)), elements);
        }
        return new Expression(elements);
    }

    private static List<Expression> makeExpressions(List<Element> elements) {
        List<List<Element>> ll=new LinkedList<>();
        List<Element> current=new LinkedList<>();
        for (Element e: elements) {
            if (e instanceof Pair && getArg(e).equals(MARKER_PARAMS)) {
                ll.add(current);
                current=new LinkedList<>();
            } else if (e instanceof Token && (getToken(e).equals(",") || getToken(e).equals("}") || getToken(e).equals(")"))) {
                    // nothing to do
            } else {
                current.add(e);
            }
        }
        if (!current.isEmpty()) {
            ll.add(current);
        }
        return ll.stream().map(Expression::makeExpression).collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return "Expression{" +
                "elements=" + elements +
                '}';
    }

    public void emit(Python emitter, List<String> classVariables,  List<String> instanceVariables) {
        emit(emitter, false,  classVariables, instanceVariables);
    }
    public void emit(Python emitter, boolean continueLine, List<String> classVariables,  List<String> instanceVariables) {
        emitter.emitLine("#" + this.toString());
    }
}
