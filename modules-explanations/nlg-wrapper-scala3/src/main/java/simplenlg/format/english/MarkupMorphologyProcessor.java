package simplenlg.format.english;

import nlg.wrapper.Constants;
import simplenlg.framework.NLGElement;
import simplenlg.morphology.english.MorphologyProcessor;

import static simplenlg.format.english.MarkupFormatter.debug_println;

public class MarkupMorphologyProcessor extends MorphologyProcessor {



    @Override
    public NLGElement realise(NLGElement element) {

        NLGElement result = super.realise(element);
        if (result!=null) {
            debug_println("{{{ ***** Morpho }}}" + element);
            propagateMarkup(element, result);
            debug_println("    -> {{{ ***** Morpho }}} " + element.getFeatureAsString(Constants.MARKUP_ELEMENT) + " " + result.getCategory() + " " +  result.getClass() + " " + result);

        }
        return result;

    }

    static String[] markupsToPropagate=new String [] {Constants.MARKUP_ELEMENT, Constants.MARKUP_ATTRIBUTES};

    public static void propagateMarkup(NLGElement src, NLGElement dst) {
        if (src !=null ) {
            String s;
            for (String markup: markupsToPropagate) {
                if ((s = src.getFeatureAsString(markup)) != null) {
                    debug_println(s);
               //     System.out.println("**** cat " + dst.getClass());
                    dst.setFeature(markup, s);
                }
            }
        }
    }


}
