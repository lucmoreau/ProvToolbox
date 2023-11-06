package org.openprovenance.prov.template.emitter.minilanguage;

import org.openprovenance.prov.template.emitter.Element;
import org.openprovenance.prov.template.emitter.Token;
import org.openprovenance.prov.template.emitter.minilanguage.emitters.Python;

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
            return new Assignment(getArg(elements.get(0)),(String)getArg(elements.get(1)),makeExpression(elements.subList(3,elements.size())), elements);
        } else if (elements.size()>=2 && elements.get(0) instanceof Token && getToken(elements.get(0)).startsWith("return")) {
            return new Return(elements.subList(1,elements.size()), elements);
        } else if (elements.size()>0 && elements.get(0) instanceof Token && getToken(elements.get(0)).startsWith("//")) {
            return new Comment(elements);
        } else {
            return makeExpression(elements);
        }

        //return new Statement(elements);
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

    public void emit(Python emitter, List<String> classVariables) {
        emitter.emitLine(this.toString());
    }

    static public String localized(String str, List<String> classVariables) {
        if (classVariables.contains(str)) {
            return "self."+str;
        } else {
            return str;
        }
    }
}
