package org.openprovenance.prov.validation;

import java.util.Hashtable;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.openprovenance.prov.model.ActedOnBehalfOf;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.model.ProvUtilities;
import org.openprovenance.prov.model.Statement;
import org.openprovenance.prov.model.Used;
import org.openprovenance.prov.model.Activity;
import org.openprovenance.prov.model.WasAssociatedWith;
import org.openprovenance.prov.model.WasAttributedTo;
import org.openprovenance.prov.model.WasStartedBy;
import org.openprovenance.prov.model.WasEndedBy;
import org.openprovenance.prov.model.WasInvalidatedBy;
import org.openprovenance.prov.model.WasGeneratedBy;
import org.openprovenance.prov.model.WasDerivedFrom;

public class Unification {

	static Logger logger = LogManager.getLogger(Unification.class);

	final private Indexer indexer;
	final ProvFactory p;
	final ProvUtilities u;
	final Uniqueness uniq;


	public Unification(Indexer indexer, Uniqueness uniq) {
		this.indexer = indexer;
		this.uniq = uniq;
		this.p = indexer.p;
		this.u = indexer.u;
	}

	public void removeUnifiedEntries() {
		logger.debug("removeUnifiedEntry1  TODO !!!!!!!!!!!!"
				+ indexer.merger.unificationSubstitution);

		removeUnifiedEntry1(WasStartedBy.class, indexer.wasStartedByTable,
				uniq.wasStartedByTable);
		removeUnifiedEntry1(WasEndedBy.class, indexer.wasEndedByTable,
				uniq.wasEndedByTable);
		removeUnifiedEntry1(WasGeneratedBy.class, indexer.wasGeneratedByTable,
				uniq.wasGeneratedByTable);
		removeUnifiedEntry1(WasInvalidatedBy.class,
				indexer.wasInvalidatedByTable,
				uniq.wasInvalidatedByTable);

		removeUnifiedEntry2(WasDerivedFrom.class, indexer.wasDerivedFromTable);
		removeUnifiedEntry2(WasAttributedTo.class, indexer.wasAttributedToTable);
		removeUnifiedEntry2(WasAssociatedWith.class,
				indexer.wasAssociatedWithTable);
		removeUnifiedEntry2(ActedOnBehalfOf.class, indexer.actedOnBehalfOfTable);
		removeUnifiedEntry2(Used.class, indexer.usedTable);

	}

	/*
	 * This method applies to classes <T> to which the unique constraint
	 * applies.
	 */

	public <T> void removeUnifiedEntry1(Class<T> class1,
										Hashtable<String, T> table1,
										Hashtable<TwoKeys, T> table2) {
		for (VarQNameWrapper key : indexer.merger.unificationSubstitution.keySet()) {
			T entry = table1.remove(key.getVarQName().getUri());
			if (entry != null) {
				logger.debug("removeUnifiedEntry1  removed " + key);
				TwoKeys tk = TwoKeys.makeTwoKeys(entry);
				T entry2 = table2.remove(tk);
				if (entry2 != null) {
					logger.debug("removeUnifiedEntry1  removed twoKey " + tk);
				}
			}
		}
	}

	/*
	 * This method applies to classes <T> to which the unique constraint does
	 * NOT apply.
	 */

	public <T> void removeUnifiedEntry2(Class<T> class1,
										Hashtable<String, T> table1) {
		for (VarQNameWrapper key : indexer.merger.unificationSubstitution.keySet()) {
			T entry = table1.remove(key);
			if (entry != null) {
				logger.debug("removeUnifiedEntry2  removed " + key);
			}
		}
	}

	public void applyUnification() {
		logger.debug("applyUnification: "
				+ indexer.merger.unificationSubstitution);

		applyUnification(WasStartedBy.class, indexer.wasStartedByTable);
		applyUnification(WasEndedBy.class, indexer.wasEndedByTable);
		applyUnification(WasGeneratedBy.class, indexer.wasGeneratedByTable);
		applyUnification(WasInvalidatedBy.class, indexer.wasInvalidatedByTable);

		applyUnification(WasDerivedFrom.class, indexer.wasDerivedFromTable);
		applyUnification(WasAttributedTo.class, indexer.wasAttributedToTable);
		applyUnification(WasAssociatedWith.class,
				indexer.wasAssociatedWithTable);
		applyUnification(ActedOnBehalfOf.class, indexer.actedOnBehalfOfTable);
		applyUnification(Used.class, indexer.usedTable);

		applyUnification(Activity.class, indexer.activityTable);

	}

	public <T extends Statement> void applyUnification(Class<T> class1,
													   Hashtable<String, T> table1) {
		for (String key : table1.keySet()) {
			T entry = table1.get(key);
			if (entry != null) {
				logger.debug("applyUnification <T>  to " + key);
				indexer.merger.applySubstitution(entry,
						indexer.merger.unificationSubstitution,
						indexer.merger.unificationTimeSubstitution);
			} else {
				logger.debug("applyUnification  coudln't find entry " + key);
				throw new UnsupportedOperationException();
			}
		}
	}


}
