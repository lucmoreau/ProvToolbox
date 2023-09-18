/*
 * The contents of this file are subject to the Mozilla Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * The Original Code is "Simplenlg".
 *
 * The Initial Developer of the Original Code is Ehud Reiter, Albert Gatt and Dave Westwater.
 * Portions created by Ehud Reiter, Albert Gatt and Dave Westwater are Copyright (C) 2010-11 The University of Aberdeen. All Rights Reserved.
 *
 * Contributor(s): Ehud Reiter, Albert Gatt, Dave Wewstwater, Roman Kutlak, Margaret Mitchell.
 */
package simplenlg.format.english;

import java.util.ArrayList;
import java.util.List;

import nlg.wrapper.Constants;
import simplenlg.features.DiscourseFunction;
import simplenlg.features.InternalFeature;
import simplenlg.framework.CoordinatedPhraseElement;
import simplenlg.framework.DocumentCategory;
import simplenlg.framework.DocumentElement;
import simplenlg.framework.ElementCategory;
import simplenlg.framework.ListElement;
import simplenlg.framework.NLGElement;
import simplenlg.framework.NLGModule;
import simplenlg.framework.StringElement;

import static simplenlg.format.english.MarkupMorphologyProcessor.propagateMarkup;

/**
 * <p>
 * This processing module adds some simple plain text formatting to the
 * SimpleNLG output. This includes the following:
 * <ul>
 * <li>Adding the document title to the beginning of the text.</li>
 * <li>Adding section titles in the relevant places.</li>
 * <li>Adding appropriate new line breaks for ease-of-reading.</li>
 * <li>Adding list items with ' * '.</li>
 * <li>Adding numbers for enumerated lists (e.g., "1.1 - ", "1.2 - ", etc.)</li>
 * </ul>
 * </p>
 * 
 * @author D. Westwater, University of Aberdeen.
 * @version 4.0
 * 
 */
public class MarkupFormatter extends NLGModule {

    static private NumberedPrefix numberedPrefix = new NumberedPrefix();

    @Override
	public void initialise() {
		// Do nothing
	}

	@Override
	public NLGElement realise(NLGElement element) {
		debug_println("*** (1)" + element);

		NLGElement realisedComponent = null;
		StringBuffer realisation = new StringBuffer();
		
		if (element != null) {
			ElementCategory category = element.getCategory();
			List<NLGElement> components = element.getChildren();
            debug_println("*** (1)" + category);

			//NB: The order of the if-statements below is important!
			
			// check if this is a canned text first
			if (element instanceof StringElement) {
				debug_println("*** (2)" + element );
				debug_println("*** (2)" + components );
				debug_println("*** (2)" + element.getAllFeatures() );

				String elementRealisation = element.getRealisation();
				String s;
				elementRealisation = addMarkupToRealisation(element, elementRealisation);
				realisation.append(elementRealisation);

			} else if (category instanceof DocumentCategory) {
				debug_println("*** (3)" + components );

                // && element instanceof DocumentElement
				String title = element instanceof DocumentElement ? ((DocumentElement) element)
						.getTitle()
						: null;
				// String title = ((DocumentElement) element).getTitle();
						
				switch ((DocumentCategory) category) {

				case DOCUMENT:
					appendTitle(realisation, title, 2);
					realiseSubComponents(realisation, components);
					break;
				case SECTION:
					appendTitle(realisation, title, 1);
					realiseSubComponents(realisation, components);
					break;
				case LIST:
					realiseSubComponents(realisation, components);
					break;

                case ENUMERATED_LIST:
                    numberedPrefix.upALevel();
                    if (title != null) {
                        realisation.append(title).append('\n');
                    }

                    if (null != components && 0 < components.size()) {

                        realisedComponent = realise(components.get(0));
                        if (realisedComponent != null) {
                            realisation.append(realisedComponent.getRealisation());
                        }
                        for (int i = 1; i < components.size(); i++) {
                            if (realisedComponent != null && !realisedComponent.getRealisation().endsWith("\n")) {
                                realisation.append(' ');
                            }
                            if(components.get(i).getParent().getCategory() == DocumentCategory.ENUMERATED_LIST) {
                                numberedPrefix.increment();
                            }
                            realisedComponent = realise(components.get(i));
                            if (realisedComponent != null) {
                                realisation.append(realisedComponent.getRealisation());
                            }
                        }
                    }

                    numberedPrefix.downALevel();
                    break;

				case PARAGRAPH:
					if (null != components && 0 < components.size()) {
						realisedComponent = realise(components.get(0));
						if (realisedComponent != null) {
							realisation.append(realisedComponent.getRealisation());
						}
						for (int i = 1; i < components.size(); i++) {
							if (realisedComponent != null) {
								realisation.append(' ');
							}
							realisedComponent = realise(components.get(i));
							if (realisedComponent != null) {
								realisation.append(realisedComponent.getRealisation());
							}
						}
					}
					realisation.append("\n\n");
					break;

				case SENTENCE:
					//realisation.append(element.getRealisation());
					realiseSubComponentsWithMarkup(realisation, element, components);
					stripLeadingCommas(realisation);
					capitaliseFirstLetter(realisation);
					terminateSentence(realisation, element.getFeatureAsBoolean(InternalFeature.INTERROGATIVE).booleanValue());

					break;

				case LIST_ITEM:
                    if(element.getParent() != null) {
                        if(element.getParent().getCategory() == DocumentCategory.LIST) {
                            realisation.append(" * ");
                        } else if(element.getParent().getCategory() == DocumentCategory.ENUMERATED_LIST) {
                            realisation.append(numberedPrefix.getPrefix() + " - ");
                        }
                    }

					for (NLGElement eachComponent : components) {
						realisedComponent = realise(eachComponent);
						
						if (realisedComponent != null) {
							realisation.append(realisedComponent
									.getRealisation());	
							
							if(components.indexOf(eachComponent) < components.size()-1) {
								realisation.append(' ');
							}
						}
					}
					//finally, append newline
					realisation.append("\n");
					break;
				}

				// also need to check if element is a ListElement (items can
				// have embedded lists post-orthography) or a coordinate
			} else if (element instanceof ListElement) {

				debug_println("***(4a) " + element);
				debug_println("***(4a) " +  ((element instanceof ListElement)? "ListElement" : "CoordinatedPhraseElement") + " !!!  " + element.getFeatureAsString(Constants.MARKUP_ELEMENT));


				String markup=null;
				if (element instanceof ListElement) {
					if ((markup = element.getFeatureAsString(Constants.MARKUP_ELEMENT)) != null) {
						realisation.append(startMarkup(markup, element.getFeatureAsString(Constants.MARKUP_ATTRIBUTES)));
					}
				}


				boolean first=true;
				for (NLGElement eachComponent : components) {
					realisedComponent = realise(eachComponent);
					if (realisedComponent != null) {
						if (first) {
							first=false;
						} else {
							realisation.append(' ');
						}
						realisation.append(realisedComponent.getRealisation());
					}
				}


				if (markup!=null) {
					if (element instanceof ListElement) {
						realisation.append(endMarkup(markup));
					}
				}

			}
			else if (element instanceof CoordinatedPhraseElement) {
                debug_println("***(4) " + element);
                debug_println("***(4) " +  ((element instanceof ListElement)? "ListElement" : "CoordinatedPhraseElement") + " !!!  " + element.getFeatureAsString(Constants.MARKUP_ELEMENT));


				String markup=null;
				if (element instanceof CoordinatedPhraseElement) {
					if ((markup = element.getFeatureAsString(Constants.MARKUP_ELEMENT)) != null) {
						realisation.append(startMarkup(markup, element.getFeatureAsString(Constants.MARKUP_ATTRIBUTES)));
					}
				}

				boolean first=true;
				int index=0;
				int length=components.size();

				//System.out.println("length: " + length);

				for (NLGElement eachComponent : components) {

					//System.out.println("index " + index + " " + eachComponent);

					realisedComponent = realise(eachComponent);
					if (realisedComponent != null) {
						if (first) {
							first=false;
						} else {
							realisation.append(' ');
						}
						if ((index != length - 2)
								&& DiscourseFunction.CONJUNCTION.equals(eachComponent.getFeature(InternalFeature.DISCOURSE_FUNCTION))) {
      //from OrthographyProcessor, line 434
							realisation.append(", "); //$NON-NLS-1$
						} else {
							realisation.append(realisedComponent.getRealisation());
						}
					}
					index++;
				}

				if (markup!=null) {
					if (element instanceof CoordinatedPhraseElement) {
						realisation.append(endMarkup(markup));
					}
				}

				realisation= fixPunctuationAndMarkup(realisation);

			}
		}
		
		return new StringElement(realisation.toString());
	}

	public StringBuffer fixPunctuationAndMarkup(StringBuffer realisation) {
		return new StringBuffer(realisation.toString()
				.replace("</li> ,", ",</li>")
				.replace(" ,", ", ")
				.replace("</li> and <li>", " and</li> <li>"));
	}

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
	private void terminateSentence(StringBuffer realisation, boolean interrogative) {
		char character = realisation.charAt(realisation.length() - 1);
		if(character != '.' && character != '?') {
			if(interrogative) {
				realisation.append('?');
			} else {
				realisation.append('.');
			}
		}
	}

	/**
	 * Remove recursively any leading spaces or commas at the start
	 * of a sentence.
	 *
	 * @param realisation
	 *            the <code>StringBuffer<code> containing the current
	 * realisation of the sentence.
	 */
	private void stripLeadingCommas(StringBuffer realisation) {
		char character = realisation.charAt(0);
		if(character == ' ' || character == ',') {
			realisation.deleteCharAt(0);
			stripLeadingCommas(realisation);
		}
	}


	/**
	 * Capitalises the first character of a sentence if it is a lower case
	 * letter.
	 *
	 * @param realisation
	 *            the <code>StringBuffer<code> containing the current
	 * realisation of the sentence.
	 */
	private void capitaliseFirstLetter(StringBuffer realisation) {
		char character = realisation.charAt(0);
		if(character >= 'a' && character <= 'z') {
			character = (char) ('A' + (character - 'a'));
			realisation.setCharAt(0, character);
		}
	}

	public static void debug_println(String s) {
//    	/System.out.println(s);
	}

	public String addMarkupToRealisation(NLGElement element, String elementRealisation) {
		String s;
		if (((s=element.getFeatureAsString(Constants.MARKUP_ELEMENT))!=null)
		   && s!="") {
			elementRealisation= startMarkup(s,element.getFeatureAsString(Constants.MARKUP_ATTRIBUTES) ) + elementRealisation + endMarkup(s);
		}
		return elementRealisation;
	}

	static public String endMarkup(String s) {
		return "</" + s + ">";
	}

	static public String startMarkup(String s, String attributes) {
    	String toInsert = ((attributes==null) || ("".equals(attributes))) ? "" : " " + attributes;
		return "<" + s +  toInsert + ">";
	}


	/**
	 * realiseSubComponents -- Realises subcomponents iteratively.
	 * @param realisation -- The current realisation StringBuffer.
	 * @param components -- The components to realise.
	 */
	private void realiseSubComponents(StringBuffer realisation,
			List<NLGElement> components) {
		NLGElement realisedComponent;
		for (NLGElement eachComponent : components) {
			realisedComponent = realise(eachComponent);
			if (realisedComponent != null) {
				realisation.append(realisedComponent
						.getRealisation());
			}
		}
	}

	/**
	 * realiseSubComponents -- Realises subcomponents iteratively.
	 * @param realisation -- The current realisation StringBuffer.
	 * @param element
	 * @param components -- The components to realise.
	 */
	private void realiseSubComponentsWithMarkup(StringBuffer realisation,
												NLGElement element,
												List<NLGElement> components) {
		String markup;
		if (((markup=element.getFeatureAsString(Constants.MARKUP_ELEMENT))!=null)
		    && (markup!="")) {
			realisation.append(startMarkup(markup,element.getFeatureAsString(Constants.MARKUP_ATTRIBUTES) ));
		}


		NLGElement realisedComponent;
		boolean first=true;
		for (NLGElement eachComponent : components) {
			realisedComponent = realise(eachComponent);
            propagateMarkup(element, realisedComponent);
			if (realisedComponent != null) {
				if (first) {
					first=false;
				} else {
					realisation.append(' ');
				}
				realisation.append(realisedComponent
						.getRealisation());
			}
		}

		if ((markup!=null) && (markup!="")) {
			realisation.append(endMarkup(markup));
		}
	}
	
	/**
	 * appendTitle -- Appends document or section title to the realised document.
	 * @param realisation -- The current realisation StringBuffer.
	 * @param title -- The title to append.
	 * @param numberOfLineBreaksAfterTitle -- Number of line breaks to append.
	 */
	private void appendTitle(StringBuffer realisation, String title, int numberOfLineBreaksAfterTitle) {
        if (title != null && !title.isEmpty()) {
            realisation.append(title);
            for(int i = 0; i < numberOfLineBreaksAfterTitle; i++) {
                realisation.append("\n");
            }
        }
    }

	@Override
	public List<NLGElement> realise(List<NLGElement> elements) {
		List<NLGElement> realisedList = new ArrayList<NLGElement>();

		if (elements != null) {
			for (NLGElement eachElement : elements) {
				realisedList.add(realise(eachElement));
			}
		}
		return realisedList;
	}
}
