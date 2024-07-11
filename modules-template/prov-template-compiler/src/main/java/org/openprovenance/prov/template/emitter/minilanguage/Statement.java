package org.openprovenance.prov.template.emitter.minilanguage;

import org.openprovenance.prov.template.compiler.common.Constants;
import org.openprovenance.prov.template.emitter.Element;
import org.openprovenance.prov.template.emitter.Pair;
import org.openprovenance.prov.template.emitter.Token;
import org.openprovenance.prov.template.emitter.minilanguage.emitters.Python;

import java.util.LinkedList;
import java.util.List;

import static org.openprovenance.prov.template.emitter.minilanguage.Expression.makeExpression;
import static org.openprovenance.prov.template.emitter.Pair.getArg;
import static org.openprovenance.prov.template.emitter.Pair.getToken;

public class Statement {
    protected List<Element> elements;

    public Statement(List<Element> elements) {
        this.elements=elements;
    }

    static public Statement makeStatement(List<Element> elements) {
        if (elements.size()>3 && elements.get(2) instanceof Token && getToken(elements.get(2)).equals("=")) {

            return new Assignment(getArg(elements.get(0)),new Symbol((String)getArg(elements.get(1)),null),makeExpression(elements.subList(3,elements.size())), elements);
        } else if (elements.size()>=2 && elements.get(0) instanceof Token && getToken(elements.get(0)).startsWith("return")) {
            return new Return(elements.subList(1,elements.size()), elements);
        } else if (!elements.isEmpty() && elements.get(0) instanceof Token && getToken(elements.get(0)).startsWith("//")) {
            return new Comment(elements);
        } else if (elements.size()>3 && elements.get(3) instanceof Token  && elements.get(3).equals(new Token("="))) {
            List<Element> allArgsIncludingMarkers = elements.subList(3, elements.size());
            MethodCall object=new MethodCall(((Pair) elements.get(0)).getArg(), (String) ((Pair) elements.get(2)).getArg(), null, elements);
            allArgsIncludingMarkers.add(0, new Pair("$L", object));
            //System.out.println(" statement recursing with " + allArgsIncludingMarkers);
            return makeStatement(allArgsIncludingMarkers);
        } else if (elements.size()>3 && elements.get(3) instanceof Token  && elements.get(3).equals(new Token("=("))) {
            List<Element> allArgsIncludingMarkers = elements.subList(3, elements.size());
            MethodCall object=new MethodCall(((Pair) elements.get(0)).getArg(), (String) ((Pair) elements.get(2)).getArg(), null, elements);
            allArgsIncludingMarkers.remove(0);
            allArgsIncludingMarkers.add(0, new Token("("));
            allArgsIncludingMarkers.add(0, new Token("="));
            allArgsIncludingMarkers.add(0, new Pair("$L", object));

            //System.out.println(" statement recursing with " + allArgsIncludingMarkers);
            return makeStatement(allArgsIncludingMarkers);
        } else if (elements.size()==3 && elements.get(1) instanceof Token && getToken(elements.get(1)).equals("=")) {
            return new Assignment(null, (Expression) getArg(elements.get(0)),new Constant((String)getArg(elements.get(2)),null), elements);
        } else if (elements.size()>3
                && tokenExists(elements,1,"=")
                && tokenExists(elements,2,"(")
                && tokenExists(elements,4,")")
                && tokenExists(elements,6,"[")) {
            List<Expression> ll=new LinkedList<>();
            ll.add(new ConstantInteger((Integer)getArg(elements.get(7)),null));
            return new Assignment(null, (Expression) getArg(elements.get(0)),new MethodCall((String)getArg(elements.get(5)), "get", ll, elements), elements);
        }else if (elements.size()>3
                && tokenExists(elements,1,"=")
                && tokenExists(elements,2,"(")
                && tokenExists(elements,4,"[")) {
            List<Expression> ll=new LinkedList<>();
            ll.add(new ConstantInteger((Integer)getArg(elements.get(5)),null));
            return new Assignment(null, (Expression) getArg(elements.get(0)),new MethodCall((String)getArg(elements.get(3)), "get", ll, elements), elements);
        }
        else {
            //System.out.println("makeStatemetn, calling makeExpression with " + elements);
            return makeExpression(elements);
        }

        //return new Statement(elements);
    }

    public static boolean tokenExists(List<Element> elements, int i, String s) {
        return elements.get(i) instanceof Token && getToken(elements.get(i)).equals(s);
    }

    @Override
    public String toString() {
        return "Statement{" +
                "elements=" + elements +
                '}';
    }

    public List<Element> getElements() {
        return elements;
    }

    public void emit(Python emitter, List<String> classVariables,  List<String> instanceVariables) {
        emitter.emitLine(this.toString());
    }

    static public String localized(String str, List<String> classVariables, List<String> instanceVariables) {
        if (classVariables.contains(str)) {
            return "cls."+str;
        } else if (instanceVariables.contains(str)) {
            return "self."+str;
        }else {
            return str;
        }
    }
}
