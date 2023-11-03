package org.openprovenance.prov.template.emitter.minilanguage;

import org.openprovenance.prov.template.emitter.Element;
import org.openprovenance.prov.template.emitter.Pair;
import org.openprovenance.prov.template.emitter.Token;
import org.openprovenance.prov.template.emitter.minilanguage.emitters.Python;

import java.util.List;

import static org.openprovenance.prov.template.emitter.Pair.getArg;

public class Comment extends Statement {
    String comment="";

    public Comment(List<Element> elements) {
        super(elements);
        for (Element e: elements) {
            if (e instanceof Token) {
                comment+=((Token) e).token;
            } else if (e instanceof Pair) {
                comment+=getArg(e).toString();
            } else {
                throw new UnsupportedOperationException("Cannot handle " + e.getClass());
            }
        }
    }

    @Override
    public String toString() {
        return "Comment{" +
                "elements=" + elements +
                '}';
    }

    @Override
    public void emit(Python emitter) {
        emitter.emitBeginLine("# " + comment.substring(2).trim());
        emitter.emitNewline();
    }
}
