package org.openprovenance.prov.validation;

import java.util.HashSet;
import java.util.List;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Set;

import javax.xml.datatype.XMLGregorianCalendar;

import org.openprovenance.prov.model.Activity;
import org.openprovenance.prov.model.Identifiable;
import org.openprovenance.prov.model.QualifiedName;
import org.openprovenance.prov.model.Statement;
import org.openprovenance.prov.model.StatementOrBundle.Kind;
import org.openprovenance.prov.model.Type;
import org.openprovenance.prov.model.WasEndedBy;
import org.openprovenance.prov.model.WasStartedBy;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.model.ProvUtilities;
import org.openprovenance.prov.model.exception.InvalidCaseException;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
/**
 * Class merging two statements of the same type. Can't this be streamlined and
 * can avoid code repetition?
 *
 * problem is newElement needs static type of its argument!
 */

public class Merger {

	static Logger logger = LogManager.getLogger(Merger.class);
	final private ProvUtilities u;
	@SuppressWarnings("unused")
	final private ProvFactory p;
	final public Hashtable<String, List<Statement>> qualifiedNameMismatch;

	final private Indexer indexer;

	public Merger(ProvFactory p, ProvUtilities u,
				  Hashtable<String, List<Statement>> qualifiedNameMismatch,
				  Indexer indexer) {
		this.u = u;
		this.p = p;
		this.qualifiedNameMismatch = qualifiedNameMismatch;
		this.indexer = indexer;
	}

	public <T extends Statement> boolean checkSameQualifiedName(QualifiedName fromArgument,
																QualifiedName toArgument,
																T from,
																T to) {
		logger.debug("        checkSameQualifiedName(): QNames are  "
				+ fromArgument + " " + toArgument);
		String uri = toArgument.getUri();
		String uri2 = fromArgument.getUri();
		if (uri.equals(uri2)) {
			if (fromArgument.getLocalPart().equals(toArgument.getLocalPart())
					&& fromArgument.getNamespaceURI()
					.equals(toArgument.getNamespaceURI())
					&& fromArgument.getPrefix().equals(toArgument.getPrefix()))
				return true;
			List<Statement> l = qualifiedNameMismatch.get(uri);
			if (l == null) {
				l = new LinkedList<>();
				l.add(to);
				qualifiedNameMismatch.put(uri, l);
			}
			l.add(from);
			return false;
		}
		return true;
	}

	QualifiedName getRef(Statement o, int i)  {
		return (QualifiedName) u.getter(o, i);
	}

	XMLGregorianCalendar getTime(Statement o, int i) {
		return (XMLGregorianCalendar) u.getter(o, i);
	}

	public <T extends Statement> T merge(T from, T to) {
		return mergeGenericUnwrapped(from, to);

	}


	static   public int getFirstTimeIndex(Statement s)  {
		final Kind kind = s.getKind();
		switch (kind) {
			case PROV_ACTIVITY: return 1;
			case PROV_AGENT: return 1;
			case PROV_ALTERNATE: return 2;
			case PROV_ASSOCIATION: return 4;
			case PROV_ATTRIBUTION: return 3;
			case PROV_BUNDLE:
				throw new InvalidCaseException("ProvUtilities.getFirstTimeIndex() for " + kind);
			case PROV_COMMUNICATION: return 3;
			case PROV_DELEGATION: return 4;
			case PROV_DERIVATION: return 6;
			case PROV_DICTIONARY_INSERTION:
				throw new InvalidCaseException("ProvUtilities.getFirstTimeIndex() for " + kind);
			case PROV_DICTIONARY_MEMBERSHIP:
				throw new InvalidCaseException("ProvUtilities.getFirstTimeIndex() for " + kind);
			case PROV_DICTIONARY_REMOVAL:
				throw new InvalidCaseException("ProvUtilities.getFirstTimeIndex() for " + kind);
			case PROV_END: return 4;
			case PROV_ENTITY: return 1;
			case PROV_GENERATION: return 3;
			case PROV_INFLUENCE: return 3;
			case PROV_INVALIDATION: return 3;
			case PROV_MEMBERSHIP: return 2;
			case PROV_MENTION: return 3;
			case PROV_SPECIALIZATION: return 2;
			case PROV_START: return 4;
			case PROV_USAGE: return 3;
			default:
				throw new InvalidCaseException("ProvUtilities.getFirstTimeIndex() for " + kind);
		}
	}



	static   public int getLastIndex(Statement s)  {
		final Kind kind = s.getKind();
		switch (kind) {
			case PROV_ACTIVITY: return 3;
			case PROV_AGENT: return 1;
			case PROV_ALTERNATE: return 2;
			case PROV_ASSOCIATION: return 4;
			case PROV_ATTRIBUTION: return 3;
			case PROV_BUNDLE:
				throw new InvalidCaseException("ProvUtilities.getLastIndex() for " + kind);
			case PROV_COMMUNICATION: return 3;
			case PROV_DELEGATION: return 4;
			case PROV_DERIVATION: return 6;
			case PROV_DICTIONARY_INSERTION:
				throw new InvalidCaseException("ProvUtilities.getLastIndex() for " + kind);
			case PROV_DICTIONARY_MEMBERSHIP:
				throw new InvalidCaseException("ProvUtilities.getLastIndex() for " + kind);
			case PROV_DICTIONARY_REMOVAL:
				throw new InvalidCaseException("ProvUtilities.getLastIndex() for " + kind);
			case PROV_END: return 5;
			case PROV_ENTITY: return 1;
			case PROV_GENERATION: return 4;
			case PROV_INFLUENCE: return 3;
			case PROV_INVALIDATION: return 4;
			case PROV_MEMBERSHIP: return 2;
			case PROV_MENTION: return 3;
			case PROV_SPECIALIZATION: return 2;
			case PROV_START: return 5;
			case PROV_USAGE: return 4;
			default:
				throw new InvalidCaseException("ProvUtilities.getLastIndex() for " + kind);
		}
	}



	private <T extends Statement> T mergeGenericUnwrapped(T from, T to) {


		QualifiedName toId=null;
		QualifiedName fromId=null;

		// checking Id (argument 0)
		if (from instanceof Identifiable) {
			toId = (QualifiedName) u.getter(to, 0);
			fromId = (QualifiedName) u.getter(from, 0);

			logger.debug("     mergeGenericUnwrapped():  " + fromId + " " + toId);

			if (!unify(fromId, toId)) {
				logger.debug("$$$$$$$$$$$$$$$$$$$$ mergeGenericUnwrapped failedtoUnify  "
						+ toId + " " + toId);
				return null;
			}

			checkSameQualifiedName(fromId, toId, from, to);
		}

		boolean success = true;




		for (int i = getFirstTimeIndex(from); i <= getLastIndex(from) - 1; i++) {
			XMLGregorianCalendar fromTime = getTime(from, i);
			XMLGregorianCalendar toTime = getTime(to, i);
			success = success && unify(fromTime, toTime);
		}

		for (int i = 1; i < getFirstTimeIndex(from); i++) {
			QualifiedName fromArgument = getRef(from, i);
			if (fromArgument != null) {
				QualifiedName toArgument = getRef(to, i);
				if (toArgument == null) {
					// u.setter(to, i, fromArgument);
					throw new NullPointerException(
							"toArgument should never be null");
				} else {
					if (fromArgument.getUri()
							.equals(toArgument.getUri())) {
						checkSameQualifiedName(fromArgument,
								toArgument, from, to);
					} else {
						if (!unify(fromArgument, toArgument)) {
							logger.debug("$$$$$$$$$$$$$$$$$$$$ mergeGenericUnwrapped failed Unification  ");
							return null;
						}
					}
				}
			}
		}


		if (from instanceof Identifiable) {
			mergeTypeAttributes(from, to, toId, fromId);
		}

		if (success) {
			logger.debug("     mergeGenericUnwrapped(): successful merge  "
					+ unificationSubstitution);
			return to;
		}
		return null;
	}

	public boolean unify(XMLGregorianCalendar fromTime,
						 XMLGregorianCalendar toTime)
			throws NullPointerException {
		boolean success=true;
		if (fromTime == null) {
			throw new NullPointerException("fromArgument should never be null");
		} else {

			if (toTime == null) {
				// u.setter(to, i, fromTime); //TODO no setter
				throw new NullPointerException("toArgument should never be null");
			} else {
				if (fromTime instanceof VarTime) {
					VarTime vFromTime = (VarTime) fromTime;
					if (!unify(vFromTime, toTime)) {
						logger.debug("$$$$$$$$$$$$$$$$$$$$ mergeGenericUnwrapped failedtoUnify  "
								+ vFromTime + " " + toTime);
						success = false;
					}
				} else {
					if (toTime instanceof VarTime) {
						VarTime vToTime = (VarTime) toTime;
						if (!unify(vToTime, fromTime)) {
							logger.debug("$$$$$$$$$$$$$$$$$$$$ mergeGenericUnwrapped failedtoUnify 2 "
									+ vToTime + " " + fromTime);
							success = false;
						}
					} else {
						// we are sure we are dealing with a time constant
						if (fromTime.equals(toTime)) {
						} else {
							logger.debug("$$$$$$$$$$$$$$$$$$$$ mergeGenericUnwrapped incompatible time  ");
							success = false;
						}
					}
				}
			}
		}
		return success;
	}

	private <T extends Statement> void mergeTypeAttributes(T from, T to,
														   QualifiedName toId, QualifiedName fromId) {
		logger.debug("        mergeAttributes " + toId + " " + fromId);
		String toUri = toId.getUri();
		String fromUri = fromId.getUri();
		Set<Type> toTypes=indexer.getTypeTable(to).get(toUri);
		if (toTypes==null) {
			toTypes=new HashSet<Type>();
			indexer.getTypeTable(to).put(toUri,toTypes);
		}
		Set<Type> fromTypes=indexer.getTypeTable(from).get(fromUri);
		if (fromTypes==null) {
			fromTypes=new HashSet<Type>();
			indexer.getTypeTable(from).put(fromUri,fromTypes);
		}

		if (!(toTypes.equals(fromTypes))) {
			Set<Type> toCopy=new HashSet<Type>(toTypes);
			toTypes.addAll(fromTypes);
			fromTypes.addAll(toCopy);
		}
	}

	Hashtable<VarQNameWrapper, QualifiedName> unificationSubstitution = new Hashtable<VarQNameWrapper, QualifiedName>();
	boolean updated = false;

	void unificationSubstitutionPut(VarQName key, QualifiedName val) {
		QualifiedName old = unificationSubstitution.put(new VarQNameWrapper(key), val);
		if ((old == null) || (!(equalQName(old, val)))) {
			updated = true;
		}
	}

	void unificationSubstitutionPut(VarQNameWrapper key, QualifiedName val) {
		QualifiedName old = unificationSubstitution.put(key, val);
		if ((old == null) || (!(equalQName(old, val)))) {
			updated = true;
		}
	}

	private void unificationSubstitutionPut(VarTime key,
											XMLGregorianCalendar val) {
		XMLGregorianCalendar old = unificationTimeSubstitution.put(key, val);
		if ((old == null) || (!(old.equals(val))))
			updated = true;

	}

	public void updateSubstitutionTable(VarQName id1, QualifiedName id2) {
		for (VarQNameWrapper key : unificationSubstitution.keySet()) {
			QualifiedName value = unificationSubstitution.get(key);
			if (equalQName(id1, value)) {
				unificationSubstitutionPut(key, id2);
			}
		}
	}

	private void updateSubstitutionTable(VarTime time1,
										 XMLGregorianCalendar time2) {
		for (VarTime key : unificationTimeSubstitution.keySet()) {
			XMLGregorianCalendar value = unificationTimeSubstitution.get(key);
			if (equalTime(value, time1)) {
				unificationSubstitutionPut(key, time2);
			}
		}
	}

	Hashtable<VarTime, XMLGregorianCalendar> unificationTimeSubstitution = new Hashtable<VarTime, XMLGregorianCalendar>();

	private boolean unify(VarTime time1, XMLGregorianCalendar time2) {
		logger.debug("        unify(): unification for VarTime  " + time1 + " "
				+ time2);

		XMLGregorianCalendar entry1 = unificationTimeSubstitution.get(time1);
		XMLGregorianCalendar entry2 = (time2 instanceof VarTime) ? unificationTimeSubstitution.get((VarTime) time2)
				: null;

		XMLGregorianCalendar unifiedTime1 = (entry1 == null) ? time1 : entry1;
		XMLGregorianCalendar unifiedTime2 = (entry2 == null) ? time2 : entry2;

		if (equalTime(unifiedTime1, unifiedTime2)) {
			logger.debug("        unify()time: SAME " + unifiedTime1);
			return true;
		}

		if (unifiedTime1 instanceof VarTime) {
			unificationSubstitutionPut((VarTime) unifiedTime1, unifiedTime2);
			updateSubstitutionTable((VarTime) unifiedTime1, unifiedTime2);
			logger.debug("        unify()time: " + unificationTimeSubstitution);
			return true;
		} else {

			if (unifiedTime2 instanceof VarTime) {
				unificationSubstitutionPut((VarTime) unifiedTime2, unifiedTime1);
				updateSubstitutionTable((VarTime) unifiedTime2, unifiedTime1);
				logger.debug("        unify()time: " + unificationTimeSubstitution);
				return true;
			} else {
				logger.debug("        unify()time: UNIFICATION FAILURE "
						+ unificationTimeSubstitution);
				return false;
			}
		}
	}


	public void sameStartTime(String activityURI, Activity activity, WasStartedBy start) {
		logger.debug("      sameStartTime >>> " + activityURI + " " + start.getId());
		boolean flag=unify(activity.getStartTime(),start.getTime());
		if (!flag) {
			logger.debug("      failed to unify startTime ");
			indexer.addToFailedMerge(activity, start, activityURI);
		}
	}

	public void sameEndTime(String activityURI, Activity activity, WasEndedBy end) {
		logger.debug("      sameEndTime >>> " + activityURI + " " + end.getId());
		boolean flag=unify(activity.getEndTime(),end.getTime());
		if (!flag) {
			logger.debug("      failed to unify endTime ");
			indexer.addToFailedMerge(activity, end, activityURI);
		}
	}


	/**
	 * Method to work around a bug of
	 * com.sun.org.apache.xerces.internal.jaxp.datatype
	 * .XMLGregorianCalendarImpl.equals()
	 */
	boolean equalTime(XMLGregorianCalendar time1,
					  XMLGregorianCalendar time2) {
		if ((time1 instanceof VarTime) && (time2 instanceof VarTime))
			return time1.equals(time2);
		if ((time1 instanceof VarTime) || (time2 instanceof VarTime))
			return false;
		return time1.equals(time2);
	}

	boolean equalQName(QualifiedName qn1, QualifiedName qn2) {
		if ((qn1 instanceof VarQName) && (qn2 instanceof VarQName))
			return ((VarQName) qn1).getUri().equals(((VarQName) qn2).getUri());
		if ((qn1 instanceof VarQName) || (qn2 instanceof VarQName))
			return false;
		if (qn1.equals(qn2)) return true;
		return qn1.getUri().equals(qn2.getUri());
	}

	private boolean unify(QualifiedName id1, QualifiedName id2) {
		logger.debug("        unify(): unification for QNames  " + id1 + " "
				+ id2);

		QualifiedName entry1 = (id1 instanceof VarQName) ? unificationSubstitution.get((VarQName) id1)
				: null;
		QualifiedName entry2 = (id2 instanceof VarQName) ? unificationSubstitution.get((VarQName) id2)
				: null;

		QualifiedName unified_uri1 = (entry1 == null) ? id1 : entry1;
		QualifiedName unified_uri2 = (entry2 == null) ? id2 : entry2;

		if (indexer.existentialVariable(unified_uri1)) {
			VarQName vqn1 = (VarQName) unified_uri1;
			unificationSubstitutionPut(vqn1, unified_uri2);
			updateSubstitutionTable(vqn1, unified_uri2);
			logger.debug("        unify(): " + unificationSubstitution);
			return true;
		} else if (indexer.existentialVariable(unified_uri2)) {
			VarQName vqn2 = (VarQName) unified_uri2;
			unificationSubstitutionPut(vqn2, unified_uri1);
			updateSubstitutionTable(vqn2, unified_uri1);
			logger.debug("        unify(): " + unificationSubstitution);
			return true;
		} else if (equalQName(unified_uri1, unified_uri2)) {
			logger.debug("        unify(): SAME " + unified_uri1);
			return true;
		} else {
			logger.debug("        unify(): UNIFICATION FAILURE "
					+ unificationSubstitution);
			return false;
		}
	}

	public QualifiedName substitute(QualifiedName id, Hashtable<VarQNameWrapper, QualifiedName> subst) {
		if (id instanceof VarQName) {
			QualifiedName res = subst.get(new VarQNameWrapper((VarQName) id));
			return res;
		} else {
			return null;
		}
	}

	public XMLGregorianCalendar substitute(XMLGregorianCalendar id,
										   Hashtable<VarTime, XMLGregorianCalendar> subst) {
		XMLGregorianCalendar res = subst.get(id);
		if (res != null) {
			logger.debug("SUBSTITUTE got " + res + ") for " + id);
			return res;
		} else {
			return null;
		}
	}

	public <T extends Statement> T applySubstitution(T from,
													 Hashtable<VarQNameWrapper, QualifiedName> table,
													 Hashtable<VarTime, XMLGregorianCalendar> timeTable) {
		try {
			return applySubstitutionUnwrapped(from, table, timeTable);
		} catch (java.lang.NoSuchMethodException nsme) {
			nsme.printStackTrace();
		} catch (java.lang.IllegalAccessException iae) {
			iae.printStackTrace();
		} catch (java.lang.reflect.InvocationTargetException ite) {
			ite.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		logger.debug("!!! applySubstitution PROBLEM: " + " " + from);
		throw new UnsupportedOperationException();
	}

	public <T extends Statement> T applySubstitutionUnwrapped(T anEntry,
															  Hashtable<VarQNameWrapper, QualifiedName> table,
															  Hashtable<VarTime, XMLGregorianCalendar> timeTable)
			throws java.lang.NoSuchMethodException,
			java.lang.IllegalAccessException,
			java.lang.reflect.InvocationTargetException,
			InstantiationException,
			IllegalArgumentException {

		// checking Id (argument 0)
		QualifiedName anEntryId = (QualifiedName) u.getter(anEntry, 0);
		logger.debug("     applySubstitutionUnwrapped():  to " + anEntryId);

		QualifiedName isNew = substitute(anEntryId, table);
		if (isNew != null)
			u.setter(anEntry, 0, isNew);

		for (int i = getFirstTimeIndex(anEntry); i <= getLastIndex(anEntry) - 1; i++) {
			logger.debug("%%%%%% " + anEntry.getClass() + " " + i + " "
					+ (getLastIndex(anEntry) - 1));
			XMLGregorianCalendar anEntryTime = getTime(anEntry, i);
			XMLGregorianCalendar isNewTime = substitute(anEntryTime, timeTable);
			if (isNewTime != null) {
				u.setter(anEntry, i, isNewTime);
			} else {
				if (anEntryTime != null) {
					if (anEntryTime instanceof VarTime) { // TODO, should I
						// reset the variable?
						// logger.debug("$$$$$$$$$$$$$$$$$$$$ applySubstitutionUnwrapped resetting variable time  ");
						// u.setter(anEntry,i,null);
					}
				}
			}
		}

		for (int i = 1; i < getFirstTimeIndex(anEntry); i++) {
			QualifiedName anEntryArgument = getRef(anEntry, i);
			if (anEntryArgument != null) {

				QualifiedName anEntryId2 = anEntryArgument;
				QualifiedName isNew2 = substitute(anEntryId2, table);

				if (isNew2 != null) {
					//Class cl = u.getTypes().get(anEntry.getClass())[i];
					//Constructor cons = cl.getConstructor();
					//Ref r = (Ref) cons.newInstance();
					//TODO: used to be dynmic type
					//Ref r= new org.openprovenance.prov.org.openprovenance.prov.xml.IDRef();
					//r.setRef();

					//Was: 		    Ref r= p.newIDRef(isNew2);
					u.setter(anEntry, i, isNew2);
				}
			}
		}

		return anEntry;
	}

}
