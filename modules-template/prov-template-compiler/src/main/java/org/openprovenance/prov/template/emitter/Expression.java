package org.openprovenance.prov.template.emitter;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static org.openprovenance.prov.template.compiler.common.CompilerCommon.MARKER_LAMBDA;
import static org.openprovenance.prov.template.emitter.Pair.*;
import static org.openprovenance.prov.template.emitter.Statement.makeStatement;

public class Expression extends Statement {

    public Expression(List<Element> elements) {
        super(elements);
    }

    static public Expression makeExpression(List<Element> elements) {
        if (elements.size()==1) {
            return new Symbol((String)getArg(elements.get(0)), elements);
        } else if (elements.size()>1
                && isPair(elements.get(0))
                && getArg(elements.get(0)).equals(MARKER_LAMBDA)) {
            List<Element> ll=elements.subList(1,elements.size());
            assert ll.size()==1;
            List<Statement> body=((List<Statement>) getArg(ll.get(0))).stream().map(x->makeStatement(x.getElements())).collect(Collectors.toList());
            return new Lambda(body, elements);

        }  else if (elements.size()>1
                && isPair(elements.get(0))
                && getArg(elements.get(0)).equals("new")) {
            return new Constructor(getArg(elements.get(1)).toString(), elements);

        } else if (elements.size()>3 && elements.get(1) instanceof Token && getToken(elements.get(1)).equals(".")) {
            List<Element> allArgsIncludingMarkers = elements.subList(3, elements.size());
            if (allArgsIncludingMarkers.size()==1 && allArgsIncludingMarkers.get(0).equals(new Token("()"))) {
                return new MethodCall(((Pair)elements.get(0)).getArg(), (String) ((Pair)elements.get(2)).getArg(), new LinkedList<>(), elements);
            } else if (allArgsIncludingMarkers.size()==3
                    && allArgsIncludingMarkers.get(0).equals(new Token("("))
                    && allArgsIncludingMarkers.get(2).equals(new Token(")"))) {
                Object arg=((Pair) allArgsIncludingMarkers.get(1)).getArg();
                if (arg instanceof List) {
                    List<Statement> ll=(List<Statement>)arg;
                    assert ll.size()==1;
                    List<Element> _elements=ll.get(0).getElements();
                    List<Object> args=_elements.stream().filter(x->x instanceof Pair).map(x->((Pair)x).getArg()).collect(Collectors.toList());
                    return new MethodCall(((Pair) elements.get(0)).getArg(), (String) ((Pair) elements.get(2)).getArg(),args, elements);
                } else {
                    throw new UnsupportedOperationException("Cannot handle "+arg);
                }
            }
        }
        return new Expression(elements);
    }

    @Override
    public String toString() {
        return "Expression{" +
                "elements=" + elements +
                '}';
    }
}
