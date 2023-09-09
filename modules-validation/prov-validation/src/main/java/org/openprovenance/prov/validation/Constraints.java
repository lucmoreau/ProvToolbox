package org.openprovenance.prov.validation;

import java.util.Collection;
import java.util.Hashtable;
import java.util.Objects;
import java.util.Set;


import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.model.ProvUtilities;
import org.openprovenance.prov.model.ActedOnBehalfOf;
import org.openprovenance.prov.model.QualifiedName;
import org.openprovenance.prov.model.Used;
import org.openprovenance.prov.model.WasGeneratedBy;
import org.openprovenance.prov.model.WasDerivedFrom;
import org.openprovenance.prov.model.WasStartedBy;
import org.openprovenance.prov.model.WasAttributedTo;
import org.openprovenance.prov.model.WasAssociatedWith;
import org.openprovenance.prov.model.WasEndedBy;
import org.openprovenance.prov.model.WasInformedBy;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/** A class to compute ordering constraints. */

public class Constraints {

	final static Logger logger = LogManager.getLogger(Constraints.class);
	final private Indexer indexer;
	final private EventIndexer evtIndexer;
	final private Inference inference;
	final ProvFactory p;
	final ProvUtilities u;
	final Types typeChecker;

	private EventMatrix matrix;

	public EventMatrix getMatrix() {
		return matrix;
	}

	final int max;

	public Constraints(Types typeChecker,
					   Indexer indexer,
					   Inference inference,
					   EventIndexer evtIndexer) {
		this.indexer = indexer;
		this.evtIndexer = evtIndexer;
		this.inference = inference;
		this.max = evtIndexer.count;
		this.p = indexer.p;
		this.u = indexer.u;
		this.typeChecker=typeChecker;
	}

	public void constraints(Config config) {
		constraints(config, true);
	}

	public void constraints(Config config, boolean closure) {
		logger.debug("initialize matrix with (" + evtIndexer.count + ")");
		this.matrix = new EventMatrix(evtIndexer.count, evtIndexer.eventKinds);

		if (config.isTrue(Config.CONSTRAINT_START_PRECEDES_END)) {
			logger.debug("constraint_start_precedes_end");
			constraint_start_precedes_end();
		}

		if (config.isTrue(Config.CONSTRAINT_GENERATION_PRECEDES_INVALIDATION)) {
			logger.debug("constraint_generation_precedes_invalidation");
			constraint_generation_precedes_invalidation();
		}

		if (config.isTrue(Config.CONSTRAINT_DERIVATION_GENERATION_GENERATION_ORDERING)) {
			logger.debug("constraint_derivation_generation_precedes_generation");
			constraint_derivation_generation_precedes_generation();
		}

		if (config.isTrue(Config.CONSTRAINT_DERIVATION_USAGE_GENERATION_ORDERING)) {
			logger.debug(Config.CONSTRAINT_DERIVATION_USAGE_GENERATION_ORDERING);
			constraint_derivation_usage_generation_ordering();
		}

		if (config.isTrue(Config.CONSTRAINT_USAGE_PRECEDES_INVALIDATION)) {
			logger.debug("constraint_usage_precedes_invalidation");
			constraint_usage_precedes_invalidation();
		}

		if (config.isTrue(Config.CONSTRAINT_USAGE_WITHIN_ACTIVITY)) {
			logger.debug("constraint_usage_precedes_end");
			constraint_usage_precedes_end();
			logger.debug("constraint_start_precedes_usage");
			constraint_start_precedes_usage();
		}

		if (config.isTrue(Config.CONSTRAINT_GENERATION_WITHIN_ACTIVITY)) {
			logger.debug("constraint_generation_precedes_end");
			constraint_generation_precedes_end();
			logger.debug("constraint_start_precedes_generation");
			constraint_start_precedes_generation();
		}

		if (config.isTrue(Config.CONSTRAINT_WASSTARTEDBY_ORDERING)) {
			logger.debug(Config.CONSTRAINT_WASSTARTEDBY_ORDERING);
			constraint_wasStartedBy_ordering();
		}

		if (config.isTrue(Config.CONSTRAINT_WASENDEDBY_ORDERING)) {
			logger.debug(Config.CONSTRAINT_WASENDEDBY_ORDERING);
			constraint_wasEndedBy_ordering();
		}

		if (config.isTrue(Config.CONSTRAINT_GENERATION_PRECEDES_USAGE)) {
			logger.debug(Config.CONSTRAINT_GENERATION_PRECEDES_USAGE);
			constraint_generation_precedes_usage();
		}

		if (config.isTrue(Config.CONSTRAINT_WASINFORMEDBY_ORDERING)) {
			logger.debug(Config.CONSTRAINT_WASINFORMEDBY_ORDERING);
			constraint_wasInformedBy_ordering();
		}

		if (config.isTrue(Config.CONSTRAINT_WASASSOCIATEDWITH_ORDERING)) {
			logger.debug(Config.CONSTRAINT_WASASSOCIATEDWITH_ORDERING);
			constraint_wasAssociatedWith_ordering();
		}

		if (config.isTrue(Config.CONSTRAINT_WASATTRIBUTEDTO_ORDERING)) {
			logger.debug(Config.CONSTRAINT_WASATTRIBUTEDTO_ORDERING);
			constraint_wasAttributedTo_ordering();
		}

		if (config.isTrue(Config.CONSTRAINT_ACTEDONBEHALFOF_ORDERING)) {
			logger.debug(Config.CONSTRAINT_ACTEDONBEHALFOF_ORDERING);
			constraint_actedOnBehalfOf_ordering();
		}

		if (config.isTrue(Config.CONSTRAINT_GENERATION_GENERATION_ORDERING)) {
			logger.debug(Config.CONSTRAINT_GENERATION_GENERATION_ORDERING);
			constraint_generation_generation_ordering();
		}

		if (config.isTrue(Config.CONSTRAINT_INVALIDATION_INVALIDATION_ORDERING)) {
			logger.debug(Config.CONSTRAINT_INVALIDATION_INVALIDATION_ORDERING);
			constraint_invalidation_invalidation_ordering();
		}

		if (config.isTrue(Config.CONSTRAINT_START_START_ORDERING)) {
			logger.debug(Config.CONSTRAINT_START_START_ORDERING);
			constraint_start_start_ordering();
		}

		if (config.isTrue(Config.CONSTRAINT_END_END_ORDERING)) {
			logger.debug(Config.CONSTRAINT_END_END_ORDERING);
			constraint_end_end_ordering();
		}

		if (config.isTrue(Config.CONSTRAINT_SPECIALIZATION_GENERATION_ORDERING)) {
			logger.debug(Config.CONSTRAINT_SPECIALIZATION_GENERATION_ORDERING);
			constraint_specialization_generation_ordering();
		}

		if (config.isTrue(Config.CONSTRAINT_SPECIALIZATION_INVALIDATION_ORDERING)) {
			logger.debug(Config.CONSTRAINT_SPECIALIZATION_INVALIDATION_ORDERING);
			constraint_specialization_invalidation_ordering();
		}

		if (config.isTrue(Config.CONSTRAINT_ENTITY_ACTIVITY_DISJOINT)) {
			logger.debug(Config.CONSTRAINT_ENTITY_ACTIVITY_DISJOINT);
			constraint_entity_activity_disjoint();
		}


		// logger.debug("matrix is: \n" + matrix.m);
		// logger.debug("matrix is: \n" + matrix.displayMatrix2());

		logger.debug("do floydWarshall");
		matrix.floydWarshall();

	}



	public void constraint_start_precedes_end() {
		for (String key : inference.activityEntries.keySet()) {
			ActivityEntry ae = inference.activityEntries.get(key);
			for (String start : ae.getStartKey()) {
				for (String end : ae.getEndKey()) {
					precede(evtIndexer.eventIndex.get(start),
							evtIndexer.eventIndex.get(end));
				}
			}
		}
	}

	public void constraint_generation_precedes_invalidation() {
		for (String key : inference.entityEntries.keySet()) {
			EntityEntry ee = inference.entityEntries.get(key);
			for (String generation : ee.getGenerationKey()) {
				for (String invalidation : ee.getInvalidationKey()) {
					precede(evtIndexer.eventIndex.get(generation),
							evtIndexer.eventIndex.get(invalidation));
				}
			}
		}
	}

	public void constraint_generation_precedes_usage() {
		for (String key : indexer.usedTable.keySet()) {
			Used used = indexer.usedTable.get(key);
			QualifiedName e = used.getEntity();
			if (e != null) {
				EntityEntry ee = inference.entityEntries.get(e.getUri());
				if (ee!=null) {
					for (String generation : ee.getGenerationKey()) {
						precede(evtIndexer.eventIndex.get(generation),
								evtIndexer.eventIndex.get(key));
					}
				}

			}
		}
	}

	public void constraint_wasStartedBy_ordering() {
		for (String key : indexer.wasStartedByTable.keySet()) {
			WasStartedBy wasStartedBy = indexer.wasStartedByTable.get(key);
			constraint_start_end_ordering(key, wasStartedBy.getTrigger());
		}
	}

	public void constraint_wasEndedBy_ordering() {
		for (String key : indexer.wasEndedByTable.keySet()) {
			WasEndedBy wasEndedBy = indexer.wasEndedByTable.get(key);
			constraint_start_end_ordering(key, wasEndedBy.getTrigger());
		}
	}

	private void constraint_start_end_ordering(String key, QualifiedName trigger) {
        if (trigger != null) {
			EntityEntry ee = inference.entityEntries.get(trigger.getUri());
			if (ee != null) {
				for (String generation : ee.getGenerationKey()) {
					precede(evtIndexer.eventIndex.get(generation),
							evtIndexer.eventIndex.get(key));
				}

				for (String invalidation : ee.getInvalidationKey()) {
					precede(evtIndexer.eventIndex.get(key),
							evtIndexer.eventIndex.get(invalidation));
				}
			}
		}
	}

	public void constraint_usage_precedes_invalidation() {
		for (String key : indexer.usedTable.keySet()) {
			Used used = indexer.usedTable.get(key);
			QualifiedName e = used.getEntity();
			if (e != null) {
				EntityEntry ee = inference.entityEntries.get(e.getUri());
				if (ee!=null) {
					for (String invalidation : ee.getInvalidationKey()) {
						precede(evtIndexer.eventIndex.get(key),
								evtIndexer.eventIndex.get(invalidation));
					}
				}

			}
		}
	}

	public void constraint_usage_precedes_end() {
		for (String key : indexer.usedTable.keySet()) {
			Used used = indexer.usedTable.get(key);
			QualifiedName a = used.getActivity();
			if (a != null) {
				ActivityEntry ae = inference.activityEntries.get(a.getUri());
				if (ae!=null) {
					for (String end : ae.getEndKey()) {
						precede(evtIndexer.eventIndex.get(key),
								evtIndexer.eventIndex.get(end));
					}
				}
			}
		}
	}

	public void constraint_generation_precedes_end() {
		for (String key : indexer.wasGeneratedByTable.keySet()) {
			WasGeneratedBy wasGeneratedBy = indexer.wasGeneratedByTable.get(key);
			QualifiedName a = wasGeneratedBy.getActivity();
			if (a != null) {
				ActivityEntry ae = inference.activityEntries.get(a.getUri());
				if (ae != null)
					for (String end : ae.getEndKey()) {
						precede(evtIndexer.eventIndex.get(key),
								evtIndexer.eventIndex.get(end));
					}
			}
		}
	}

	public void constraint_start_precedes_usage() {
		for (String usage : indexer.usedTable.keySet()) {
			Used used = indexer.usedTable.get(usage);
			QualifiedName a = used.getActivity();
			if (a != null) {
				ActivityEntry ae = inference.activityEntries.get(a.getUri());
				if (ae!=null) {
					for (String start : ae.getStartKey()) {
						precede(evtIndexer.eventIndex.get(start),
								evtIndexer.eventIndex.get(usage));
					}
				}
			}
		}
	}

	public void constraint_start_precedes_generation() {
		for (String generation : indexer.wasGeneratedByTable.keySet()) {
			WasGeneratedBy wasGeneratedBy = indexer.wasGeneratedByTable.get(generation);
			QualifiedName a = wasGeneratedBy.getActivity();
			if (a != null) {
				ActivityEntry ae = inference.activityEntries.get(a.getUri());
				if (ae != null) {
					for (String start : ae.getStartKey()) {
						precede(evtIndexer.eventIndex.get(start),
								evtIndexer.eventIndex.get(generation));
					}
				}
			}
		}
	}

	public void constraint_derivation_generation_precedes_generation() {
		for (String key : indexer.wasDerivedFromTable.keySet()) {
			WasDerivedFrom wdf = indexer.wasDerivedFromTable.get(key);
			QualifiedName e2 = wdf.getGeneratedEntity();
			QualifiedName e1 = wdf.getUsedEntity();

			EntityEntry ee2 = inference.entityEntries.get(e2.getUri());
			EntityEntry ee1 = inference.entityEntries.get(e1.getUri());

			if (ee2!=null) {
				for (String generation2 : ee2.getGenerationKey()) {
					if (ee1!=null) {
						for (String generation1 : ee1.getGenerationKey()) {
							logger.debug("g_g " + generation1 + " " + generation2);
							precedeStrict(evtIndexer.eventIndex.get(generation1),
									evtIndexer.eventIndex.get(generation2));
						}
					}
				}
			}
		}
	}

	public void constraint_derivation_usage_generation_ordering() {
		for (String key : indexer.wasDerivedFromTable.keySet()) {
			WasDerivedFrom wdf = indexer.wasDerivedFromTable.get(key);
			QualifiedName genRef = wdf.getGeneration();
			QualifiedName useRef = wdf.getUsage();
			if ((genRef == null) || (useRef == null))
				return;
			precede(evtIndexer.eventIndex.get(useRef.getUri()),
					evtIndexer.eventIndex.get(genRef.getUri()));
		}
	}

	public void constraint_wasInformedBy_ordering() {
		for (String key : indexer.wasInformedByTable.keySet()) {
			WasInformedBy wib = indexer.wasInformedByTable.get(key);
			QualifiedName informant = wib.getInformant();
			QualifiedName informed = wib.getInformed();
			ActivityEntry ae1 = inference.activityEntries.get(informant.getUri());
			ActivityEntry ae2 = inference.activityEntries.get(informed.getUri());

			for (String start : ae1.getStartKey()) {
				if (ae2!=null) {
					for (String end : ae2.getEndKey()) {
						precede(evtIndexer.eventIndex.get(start),
								evtIndexer.eventIndex.get(end));
					}
				}
			}
		}
	}

	public void constraint_wasAssociatedWith_ordering() {
		for (String key : indexer.wasAssociatedWithTable.keySet()) {
			WasAssociatedWith assoc = indexer.wasAssociatedWithTable.get(key);
			QualifiedName a = assoc.getActivity();

			ActivityEntry ae = inference.activityEntries.get(a.getUri());

			if (ae != null) {

				QualifiedName ag = assoc.getAgent();
				EntityEntry agE = inference.entityEntries.get(ag.getUri());
				// checking if the agent is declared as an entity

				if (agE != null) {
					for (String start : ae.getStartKey()) {
						for (String invalidation : agE.getInvalidationKey()) {
							precede(evtIndexer.eventIndex.get(start),
									evtIndexer.eventIndex.get(invalidation));
						}
					}
					for (String end : ae.getEndKey()) {
						for (String generation : agE.getGenerationKey()) {
							precede(evtIndexer.eventIndex.get(generation),
									evtIndexer.eventIndex.get(end));
						}
					}
				}

				ActivityEntry agA = inference.activityEntries.get(ag.getUri());
				if (agA != null) {
					for (String start1 : ae.getStartKey()) {
						for (String end2 : agA.getEndKey()) {
							precede(evtIndexer.eventIndex.get(start1),
									evtIndexer.eventIndex.get(end2));
						}
					}
					for (String end2 : ae.getEndKey()) {
						for (String start1 : agA.getStartKey()) {
							precede(evtIndexer.eventIndex.get(start1),
									evtIndexer.eventIndex.get(end2));
						}
					}
				}

			}
		}
	}

	public void constraint_wasAttributedTo_ordering() {
		for (String key : indexer.wasAttributedToTable.keySet()) {
			WasAttributedTo wat = indexer.wasAttributedToTable.get(key);
			QualifiedName e = wat.getEntity();

			EntityEntry ee = inference.entityEntries.get(e.getUri());
			if (ee==null) return;
			for (String generation2 : ee.getGenerationKey()) {
				QualifiedName ag = wat.getAgent();
				EntityEntry agE = inference.entityEntries.get(ag.getUri());
				// consider the agent as an entity
				if (agE != null) {
					for (String generation1 : agE.getGenerationKey()) {
						precede(evtIndexer.eventIndex.get(generation1),
								evtIndexer.eventIndex.get(generation2));
					}
				}
				ActivityEntry agA = inference.activityEntries.get(ag.getUri());
				// consider the agent as an activity
				if (agA != null) {
					for (String start1 : agA.getStartKey()) {
						precede(evtIndexer.eventIndex.get(start1),
								evtIndexer.eventIndex.get(generation2));
					}
				}
			}
		}
	}

	public void constraint_actedOnBehalfOf_ordering() {
		for (String key : indexer.actedOnBehalfOfTable.keySet()) {
			ActedOnBehalfOf aob = indexer.actedOnBehalfOfTable.get(key);

			QualifiedName ag1 = aob.getResponsible();
			QualifiedName ag2 = aob.getDelegate();

			EntityEntry ee1 = inference.entityEntries.get(ag1.getUri());
			EntityEntry ee2 = inference.entityEntries.get(ag2.getUri());

			if ((ee1 != null) && (ee2 != null)) {
				for (String invalidation2 : ee2.getInvalidationKey()) {
					for (String generation1 : ee1.getGenerationKey()) {
						precede(evtIndexer.eventIndex.get(generation1),
								evtIndexer.eventIndex.get(invalidation2));
					}
				}
			}

			ActivityEntry ae1 = inference.activityEntries.get(ag1.getUri());
			ActivityEntry ae2 = inference.activityEntries.get(ag2.getUri());

			if ((ae1 != null) && (ae2 != null)) {
				for (String end2 : ae2.getEndKey()) {
					for (String start1 : ae1.getStartKey()) {
						precede(evtIndexer.eventIndex.get(start1),
								evtIndexer.eventIndex.get(end2));
					}
				}
			}

		}
	}

	public void constraint_generation_generation_ordering() {
		for (String key : inference.entityEntries.keySet()) {
			EntityEntry ee = inference.entityEntries.get(key);
			for (String generation1 : ee.getGenerationKey()) {
				for (String generation2 : ee.getGenerationKey()) {
					if (!Objects.equals(generation1, generation2)) {
						precede(evtIndexer.eventIndex.get(generation1),
								evtIndexer.eventIndex.get(generation2));
					}
				}
			}
		}
	}

	public void constraint_invalidation_invalidation_ordering() {
		for (String key : inference.entityEntries.keySet()) {
			EntityEntry ee = inference.entityEntries.get(key);
			for (String invalidation1 : ee.getInvalidationKey()) {
				for (String invalidation2 : ee.getInvalidationKey()) {
					if (!Objects.equals(invalidation1, invalidation2)) {
						precede(evtIndexer.eventIndex.get(invalidation1),
								evtIndexer.eventIndex.get(invalidation2));
					}
				}
			}
		}
	}

	public void constraint_start_start_ordering() {
		for (String key : inference.activityEntries.keySet()) {
			ActivityEntry ee = inference.activityEntries.get(key);
			for (String start1 : ee.getStartKey()) {
				for (String start2 : ee.getStartKey()) {
					if (!Objects.equals(start1, start2)) {
						precede(evtIndexer.eventIndex.get(start1),
								evtIndexer.eventIndex.get(start2));
					}
				}
			}
		}
	}

	public void constraint_end_end_ordering() {
		for (String key : inference.activityEntries.keySet()) {
			ActivityEntry ee = inference.activityEntries.get(key);
			for (String end1 : ee.getEndKey()) {
				for (String end2 : ee.getEndKey()) {
					if (!Objects.equals(end1, end2)) {
						precede(evtIndexer.eventIndex.get(end1),
								evtIndexer.eventIndex.get(end2));
					}
				}
			}
		}
	}

	public void constraint_specialization_generation_ordering() {
		for (String key2 : inference.entityEntries.keySet()) {
			EntityEntry ee2 = inference.entityEntries.get(key2);
			for (String generation2 : ee2.getGenerationKey()) {
				QualifiedName entity2 = indexer.wasGeneratedByTable.get(generation2)
						.getEntity();
				if (inference.specializationTable != null) {
					Set<QualifiedName> set1 = inference.specializationTable.get(entity2);
					if (set1 != null) {
						for (QualifiedName entity1 : set1) {
							String key1 = entity1.getUri();
							EntityEntry ee1 = inference.entityEntries.get(key1);
							if (ee1 != null) {
								for (String generation1 : ee1.getGenerationKey()) {
									precede(evtIndexer.eventIndex.get(generation1),
											evtIndexer.eventIndex.get(generation2));
								}
							}
						}
					}

				}
			}

		}
	}

	public void constraint_specialization_invalidation_ordering() {
		for (String key2 : inference.entityEntries.keySet()) {
			EntityEntry ee2 = inference.entityEntries.get(key2);
			for (String invalidation2 : ee2.getInvalidationKey()) {
				QualifiedName entity2 = indexer.wasInvalidatedByTable.get(invalidation2)
						.getEntity();
				if (inference.specializationTable != null) {
					Set<QualifiedName> set1 = inference.specializationTable.get(entity2);
					if (set1 != null) {
						for (QualifiedName entity1 : set1) {
							String key1 = entity1.getUri();
							EntityEntry ee1 = inference.entityEntries.get(key1);
							if (ee1 != null) {
								for (String invalidation1 : ee1.getInvalidationKey()) {
									precede(evtIndexer.eventIndex.get(invalidation2),
											evtIndexer.eventIndex.get(invalidation1));
								}
							}
						}
					}

				}
			}

		}
	}

	public Hashtable<String,Collection<String>> typeOverlapTable=new Hashtable<String,Collection<String>>();

	public void constraint_entity_activity_disjoint() {

		//intersection1=new HashSet<String>(inference.activityEntries.keySet());
		//intersection1.retainAll(inference.entityEntries.keySet());



		//intersection2=new HashSet<String>(indexer.activityTable.keySet());
		//intersection2.retainAll(indexer.entityTable.keySet());

		for (String key:  typeChecker.aggregatedTypes.keySet()) {
			Set<String> types=typeChecker.aggregatedTypes.get(key);
			if (types==null) {
				//cannot be here
				throw new UnsupportedOperationException();
			}
			Collection<String> invalid=typeChecker.conflictingTypes(types);
			if (invalid!=null)
				typeOverlapTable.put(key, invalid);
		}
	}



	public static final int PRECEDE_STRICT = 1;
	public static final int PRECEDE = 2;

	/* Check that a non-strict ordering does not override a strict ordering. */

	public void precede(Integer event1, Integer event2) {
		if ((event1 == null) || (event2 == null)) {
			return;
		}

		logger.debug("precede: " + event1 + " " + event2);
		if ((event1 < max) && (event2 < max))  {
			org.openprovenance.prov.validation.matrix.Pair p=matrix.m.g(event1,event2);
			if (p==null)
				matrix.m.set(event1, event2, PRECEDE);

		}
	}

	public void precedeStrict(Integer event1, Integer event2) {
		if ((event1 == null) || (event2 == null)) {
			return;
		}

		logger.debug("precede strict: " + event1 + " " + event2);
		if ((event1 < max) && (event2 < max))
			matrix.m.set(event1, event2, PRECEDE_STRICT);
	}

}
