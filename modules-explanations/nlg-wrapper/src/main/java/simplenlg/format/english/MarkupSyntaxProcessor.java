package simplenlg.format.english;

import nlg.wrapper.Constants;
import simplenlg.framework.NLGElement;
import simplenlg.syntax.english.SyntaxProcessor;

import static simplenlg.format.english.MarkupFormatter.debug_println;

public class MarkupSyntaxProcessor extends SyntaxProcessor {
    @Override
    public NLGElement realise(NLGElement element) {
        NLGElement realised = super.realise(element);
        if (realised!=null) {
            debug_println("{{{ ***** Syntax }}}" + element.getFeatureAsString(Constants.MARKUP_ELEMENT)+ " " + element.getCategory() + " " + element.getClass() + " " + element);
            MarkupMorphologyProcessor.propagateMarkup(element, realised);
            debug_println("    -> {{{ ***** Syntax }}} " + realised.getFeatureAsString(Constants.MARKUP_ELEMENT) + " " + realised.getCategory() + " " + realised.getClass() + " " + realised);
            debug_println("    ->\n  " + realised.printTree("  "));
        }

        return realised;
    }
}
