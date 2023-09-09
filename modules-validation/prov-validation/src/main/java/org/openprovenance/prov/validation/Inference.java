package org.openprovenance.prov.validation;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.openprovenance.prov.model.AlternateOf;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.model.ProvUtilities;
import org.openprovenance.prov.model.QualifiedName;
import org.openprovenance.prov.model.WasEndedBy;
import org.openprovenance.prov.model.WasGeneratedBy;
import org.openprovenance.prov.model.WasInvalidatedBy;
import org.openprovenance.prov.model.WasStartedBy;
import org.openprovenance.prov.model.SpecializationOf;

public class Inference {

	final static Logger logger = LogManager.getLogger(Inference.class);

	final Indexer indexer;
	final Gensym g;
	final ProvFactory p;
	final ProvUtilities u;
	final Types typeChecker;

	public Inference(Indexer indexer, Types typeChecker) {
		this.indexer = indexer;
		this.g = indexer.g;
		this.p = indexer.p;
		this.u = indexer.u;
		this.typeChecker=typeChecker;
	}

	public void inferIntervalBeginningAndEnds(Config config) {
		for (String key : indexer.wasGeneratedByTable.keySet()) {
			WasGeneratedBy gen = indexer.wasGeneratedByTable.get(key);
			getOrCreateEntityEntry(gen.getEntity().getUri()).addGenerationKey(key);
		}
		for (String key : indexer.wasInvalidatedByTable.keySet()) {
			WasInvalidatedBy inv = indexer.wasInvalidatedByTable.get(key);
			getOrCreateEntityEntry(inv.getEntity().getUri()).addInvalidationKey(key);
		}
		for (String key : indexer.wasStartedByTable.keySet()) {
			WasStartedBy start = indexer.wasStartedByTable.get(key);
			getOrCreateActivityEntry(start.getActivity().getUri()).addStartKey(key);
		}
		for (String key : indexer.wasEndedByTable.keySet()) {
			WasEndedBy end = indexer.wasEndedByTable.get(key);
			getOrCreateActivityEntry(end.getActivity().getUri()).addEndKey(key);
		}

		for (String entity : indexer.entityTable.keySet()) {
			QualifiedName entityId = indexer.entityTable.get(entity).getId();
			EntityEntry ee = entityEntries.get(entity);
			if (ee == null) {
				ee = new EntityEntry();
				entityEntries.put(entity, ee);
			}

			if (ee.getGenerationKey().isEmpty()) {
				QualifiedName generationId = g.newId((WasGeneratedBy) null);
				WasGeneratedBy wgb = p.newWasGeneratedBy(generationId,
						entityId,
						null, null);
				String generationUri = generationId.getUri();
				indexer.wasGeneratedByTable.put(generationUri, wgb);
				ee.addGenerationKey(generationUri);
			}

			if (ee.getInvalidationKey().isEmpty()) {
				QualifiedName invalidationId = g.newId((WasInvalidatedBy) null);
				WasInvalidatedBy wib = p.newWasInvalidatedBy(invalidationId,
						entityId,
						null);
				String invalidationUri = invalidationId.getUri();
				indexer.wasInvalidatedByTable.put(invalidationUri, wib);
				ee.addInvalidationKey(invalidationUri);
			}

		}

		for (String activity : indexer.activityTable.keySet()) {
			QualifiedName activityId = indexer.activityTable.get(activity).getId();
			ActivityEntry ae = activityEntries.get(activity);
			if (ae == null) {
				ae = new ActivityEntry();
				activityEntries.put(activity, ae);
			}

			if (ae.getStartKey().isEmpty()) {
				QualifiedName startId = g.newId((WasStartedBy) null);
				WasStartedBy wsb = p.newWasStartedBy(startId,
						activityId,
						null);
				String startUri = startId.getUri();
				indexer.wasStartedByTable.put(startUri, wsb);
				ae.addStartKey(startUri);
			}

			if (ae.getEndKey().isEmpty()) {
				QualifiedName endId = g.newId((WasEndedBy) null);
				WasEndedBy wsb = p.newWasEndedBy(endId,
						activityId,
						null);
				String endUri = endId.getUri();
				indexer.wasEndedByTable.put(endUri, wsb);
				ae.addEndKey(endUri);
			}

		}

	}

	/*
	 * @return true if set didn't contain entry yet.
	 */
	public boolean addEntry(Hashtable<QualifiedName,
							Set<QualifiedName>> table,
							QualifiedName specific,
							QualifiedName general) {
		Set<QualifiedName> tmp = table.computeIfAbsent(specific, k -> new HashSet<>());
		return tmp.add(general);
	}

	public Hashtable<QualifiedName, Set<QualifiedName>> specializationTable;
	Hashtable<QualifiedName, Set<QualifiedName>> alternateTable;

	public Hashtable<QualifiedName, Set<QualifiedName>> computeSpecializationClosure(Config config) {
		Hashtable<QualifiedName, Set<QualifiedName>> table = new Hashtable<>();
		for (SpecializationOf spec : indexer.specializationOfList) {
			QualifiedName general = spec.getGeneralEntity();
			QualifiedName specific = spec.getSpecificEntity();
			addEntry(table, specific, general);
		}
		if (config.isTrue(Config.INFERENCE_SPECIALIZATION_TRANSITIVE)) {
			logger.debug(Config.INFERENCE_SPECIALIZATION_TRANSITIVE);
			computeTransitiveClosure(table);
		}
		specializationTable = table;
		return table;
	}

	public Hashtable<QualifiedName, Set<QualifiedName>> computeAlternateClosure(Config config) {
		Hashtable<QualifiedName, Set<QualifiedName>> table = new Hashtable<>();
		for (AlternateOf spec : indexer.alternateOfList) {
			QualifiedName e1 = spec.getAlternate2();
			QualifiedName e2 = spec.getAlternate1();
			addEntry(table, e2, e1);
			if (config.isTrue(Config.INFERENCE_ALTERNATE_SYMMETRIC)) {
				addEntry(table, e1, e2);
			}
			if (config.isTrue(Config.INFERENCE_ALTERNATE_REFLEXIVE)) {
				addEntry(table, e1, e1);
				addEntry(table, e2, e2);
			}
			addEntry(table, e2, e1);
		}
		for (SpecializationOf spec : indexer.specializationOfList) {
			QualifiedName general = spec.getGeneralEntity();
			QualifiedName specific = spec.getSpecificEntity();
			if (config.isTrue(Config.INFERENCE_SPECIALIZATION_ALTERNATE)) {
				addEntry(table, specific, general);
				if (config.isTrue(Config.INFERENCE_ALTERNATE_SYMMETRIC)) {
					addEntry(table, general, specific);
				}
			}
		}

		if (config.isTrue(Config.INFERENCE_ALTERNATE_TRANSITIVE)) {
			logger.debug(Config.INFERENCE_ALTERNATE_TRANSITIVE);
			computeTransitiveClosure(table);
		}
		alternateTable = table;
		return table;
	}

	public void computeTransitiveClosure(Hashtable<QualifiedName, Set<QualifiedName>> table) {
		boolean updated = true;
		while (updated) {
			logger.debug("table is " + table);
			updated = false;
			for (QualifiedName i : new LinkedList<>(table.keySet())) { // copies
				// into list
				// to allow
				// update of
				// tables/lists
				for (QualifiedName j : new LinkedList<>(table.get(i))) {
					Set<QualifiedName> set = table.get(j);
					if (set != null) {
						for (QualifiedName k : set) {
							updated = updated || addEntry(table, i, k);
						}
					}
				}
			}
		}
	}

	public void specializationIsNotReflexive(Config config) {
		if (config.isTrue(Config.CONSTRAINT_IMPOSSIBLE_SPECIALIZATION_REFLEXIVE)) {
			logger.debug(Config.CONSTRAINT_IMPOSSIBLE_SPECIALIZATION_REFLEXIVE);
			specializationIsNotReflexive();
		}
	}

	List<SpecializationOf> failedSpecialization = new LinkedList<>();

	public void specializationIsNotReflexive() {
		List<QualifiedName> fails = new LinkedList<>();
		for (QualifiedName key : specializationTable.keySet()) {
			if (specializationTable.get(key).contains(key)) {
				fails.add(key);
			}
		}
		for (QualifiedName fail : fails) {
			failedSpecialization.add(p.newSpecializationOf(fail, fail));
		}
	}

	public void specializationAttributesInference(Config config) {
		if (config.isTrue(Config.INFERENCE_SPECIALIZATION_ATTRIBUTES_INFERENCE)) {
			logger.debug("%%% " +Config.INFERENCE_SPECIALIZATION_ATTRIBUTES_INFERENCE);
			//TODO: it's only types that are propagated here, I should propagate all other attributes

			for (QualifiedName key : specializationTable.keySet()) {
				String specific=key.getUri();
				Set<QualifiedName> set=specializationTable.get(key);
				for (QualifiedName name: set) {
					String general=name.getUri();
					typeChecker.aggregatedTypes.get(specific).addAll(typeChecker.aggregatedTypes.get(general));
				}
			}
		}
	}

	public String summary() {
		return "" + activityEntries + "\n" + entityEntries;
	}

	public Hashtable<String, ActivityEntry> activityEntries = new Hashtable<>();
	public Hashtable<String, EntityEntry> entityEntries = new Hashtable<>();

	public ActivityEntry getOrCreateActivityEntry(String activityURI) {
		ActivityEntry entry = activityEntries.get(activityURI);
		if (entry == null) {
			entry = new ActivityEntry();
			activityEntries.put(activityURI, entry);
		}
		return entry;
	}

	public EntityEntry getOrCreateEntityEntry(String entityURI) {
		EntityEntry entry = entityEntries.get(entityURI);
		if (entry == null) {
			entry = new EntityEntry();
			entityEntries.put(entityURI, entry);
		}
		return entry;
	}



}
