package simplenlg.format.english;

import nlg.wrapper.Constants;
import simplenlg.features.DiscourseFunction;
import simplenlg.features.InternalFeature;
import simplenlg.framework.*;
import simplenlg.orthography.english.OrthographyProcessor;

import java.util.List;

import static simplenlg.format.english.MarkupFormatter.*;
import static simplenlg.format.english.MarkupMorphologyProcessor.propagateMarkup;

public class MarkupOrthographyProcessor extends OrthographyProcessor {

    @Override
    public NLGElement realise(NLGElement element) {

/*
        if(element != null) {
            ElementCategory category = element.getCategory();

            if (category instanceof DocumentCategory && element instanceof DocumentElement) {
                List<NLGElement> components = ((DocumentElement) element).getComponents();

                switch ((DocumentCategory) category) {

                    case SENTENCE:
                        return realiseSentence(components, element);
                }
            }
        }
*/

        NLGElement result = super.realise(element);
        if (result!=null) {
            debug_println("{{{ ***** Ortho }}}" + element.getFeatureAsString(Constants.MARKUP_ELEMENT) + " "  + element.getCategory() + " " + element );
            propagateMarkup(element, result);
            debug_println("    -> {{{ ***** Ortho }}} "+ result.getFeatureAsString(Constants.MARKUP_ELEMENT) + " "  + result.getCategory() + " " + result.getClass() + " " + result);

        }

        return result;

    }

/*
    public NLGElement realiseSentence(List<NLGElement> components, NLGElement element) {
        System.out.println(" ---> in realiseSentence " + element.getFeatureAsString(Constants.MARKUP_ELEMENT) + " " + element);

        NLGElement realisedElement = null;

        String markup=element.getFeatureAsString(Constants.MARKUP_ELEMENT);

        if(components != null && components.size() > 0) {
            StringBuffer realisation = new StringBuffer();
            if (markup!=null) {
                realisation.append(startMarkup(markup, ));
            }
            realiseList(realisation, components, "");

            stripLeadingCommas(realisation);
            capitaliseFirstLetter(realisation);
            terminateSentence(realisation, element.getFeatureAsBoolean(InternalFeature.INTERROGATIVE).booleanValue());

            ((DocumentElement) element).clearComponents();
            // realisation.append(' ');
            if (markup!=null) {
                realisation.append(endMarkup(markup));
            }
            element.setRealisation(realisation.toString());
            realisedElement = element;
        }

        return realisedElement;
    }



 */
    /**
     * Capitalises the first character of a sentence if it is a lower case
     * letter.
     *
     * @param realisation
     *            the <code>StringBuffer<code> containing the current
     * realisation of the sentence.
     */
 /*   private void capitaliseFirstLetter(StringBuffer realisation) {
        char character = realisation.charAt(0);
        if(character >= 'a' && character <= 'z') {
            character = (char) ('A' + (character - 'a'));
            realisation.setCharAt(0, character);
        }
    }



  */

    /**
     * Realises a list of elements appending the result to the on-going
     * realisation.
     *
     * @param realisation
     *            the <code>StringBuffer<code> containing the current
     * 			  realisation of the sentence.
     * @param components
     *            the <code>List</code> of <code>NLGElement</code>s representing
     *            the components that make up the sentence.
     * @param listSeparator
     *            the string to use to separate elements of the list, empty if
     *            no separator needed
     */
 /*   private void realiseList(StringBuffer realisation, List<NLGElement> components, String listSeparator) {

        NLGElement realisedChild = null;

        for(int i = 0; i < components.size(); i++ ) {
            NLGElement thisElement = components.get(i);
            realisedChild = realise(thisElement);
            String childRealisation = realisedChild.getRealisation();

            // check that the child realisation is non-empty
            if(childRealisation != null && childRealisation.length() > 0 && !childRealisation.matches("^[\\s\\n]+$")) {
                realisation.append(realisedChild.getRealisation());

                if(components.size() > 1 && i < components.size() - 1) {
                    realisation.append(listSeparator);
                }

                realisation.append(' ');
            }
        }

        if(realisation.length() > 0) {
            realisation.setLength(realisation.length() - 1);
        }
    }


  */
    /**
     * Realises coordinated phrases. Where there are more than two coordinates,
     * then a comma replaces the conjunction word between all the coordinates
     * save the last two. For example, <em>John and Peter and Simon</em> becomes
     * <em>John, Peter and Simon</em>.
     *
     * @param components
     *            the <code>List</code> of <code>NLGElement</code>s representing
     *            the components that make up the sentence.
     * @return the realised element as an <code>NLGElement</code>.
     */
/*    private NLGElement realiseCoordinatedPhrase(List<NLGElement> components) {
        StringBuffer realisation = new StringBuffer();
        NLGElement realisedChild = null;

        int length = components.size();

        for(int index = 0; index < length; index++ ) {
            realisedChild = components.get(index);
            if(index < length - 2
                    && DiscourseFunction.CONJUNCTION.equals(realisedChild.getFeature(InternalFeature.DISCOURSE_FUNCTION))) {

                realisation.append(", "); //$NON-NLS-1$
            } else {
                realisedChild = realise(realisedChild);
                realisation.append(realisedChild.getRealisation()).append(' ');
            }
        }
        realisation.setLength(realisation.length() - 1);
        return new StringElement(realisation.toString().replace(" ,", ",")); //$NON-NLS-1$ //$NON-NLS-2$
    }




 */
    /**
     * Adds the sentence terminator to the sentence. This is a period ('.') for
     * normal sentences or a question mark ('?') for interrogatives.
     *
     * @param realisation
     *            the <code>StringBuffer<code> containing the current
     * realisation of the sentence.
     * @param interrogative
     *            a <code>boolean</code> flag showing <code>true</code> if the
     *            sentence is an interrogative, <code>false</code> otherwise.
     */
  /*  private void terminateSentence(StringBuffer realisation, boolean interrogative) {
        char character = realisation.charAt(realisation.length() - 1);
        if(character != '.' && character != '?') {
            if(interrogative) {
                realisation.append('?');
            } else {
                realisation.append('.');
            }
        }
    }


   */
    /**
     * Remove recursively any leading spaces or commas at the start
     * of a sentence.
     *
     * @param realisation
     *            the <code>StringBuffer<code> containing the current
     * realisation of the sentence.
     */
 /*   private void stripLeadingCommas(StringBuffer realisation) {
        char character = realisation.charAt(0);
        if(character == ' ' || character == ',') {
            realisation.deleteCharAt(0);
            stripLeadingCommas(realisation);
        }
    }


  */

}
