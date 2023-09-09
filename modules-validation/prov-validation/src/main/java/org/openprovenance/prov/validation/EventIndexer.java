package org.openprovenance.prov.validation;

import java.util.*;

import org.openprovenance.prov.model.ModelConstructor;
import org.openprovenance.prov.model.ProvUtilities;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.openprovenance.prov.model.Statement;

/**
 * A class to complete a graph, adding a generation/invalidation for each
 * entity, start/end for each activity.
 */

public class EventIndexer {

    final static Logger logger = LogManager.getLogger(EventIndexer.class);
    public static final int MAX_VALUE = 70000;

    final private Indexer indexer;
    final ModelConstructor p;
    final ProvUtilities u;


	public EventIndexer(Indexer indexer) {
		this.indexer = indexer;
		this.p = indexer.p;
		this.u = indexer.u;
	}

    Vector<String> events;
    Vector<String> eventKinds;
    public Map<String, Integer> eventIndex;
    public Map<String, Statement> eventTable;

    int count = 0;

    /**
     * Create a rough order: start, generation, usage, end, invalidation.
     * However, for large graphs, limit to MAX_VALUE events. Priorize
     * wasGeneratedBy over the other others.
     */

	public void createEventIndex() {
		List<String> eventsList = new LinkedList<String>();
		List<String> eventKindsList = new LinkedList<String>();
		eventIndex = new Hashtable<>();
		eventTable = new Hashtable<>();

		int for_wgb = Math.min(indexer.wasGeneratedByTable.keySet().size(),
				MAX_VALUE);

		int others = Math.max(0, MAX_VALUE - for_wgb);

		for (String key : indexer.wasStartedByTable.keySet()) {
			if (others > 0) {
				eventsList.add(key);
				eventKindsList.add("s");
				eventIndex.put(key, count++);
				others--;
			}
			eventTable.put(key, indexer.wasStartedByTable.get(key));
		}

		for (String key : indexer.wasGeneratedByTable.keySet()) {
			if (for_wgb > 0) {
				eventsList.add(key);
				eventKindsList.add("g");
				eventIndex.put(key, count++);
				for_wgb--;
			}
			eventTable.put(key, indexer.wasGeneratedByTable.get(key));
		}

		for (String key : indexer.usedTable.keySet()) {
			if (others > 0) {
				eventsList.add(key);
				eventKindsList.add("u");
				eventIndex.put(key, count++);
				others--;
			}
			eventTable.put(key, indexer.usedTable.get(key));
		}

		for (String key : indexer.wasEndedByTable.keySet()) {
			if (others > 0) {
				eventsList.add(key);
				eventKindsList.add("e");
				eventIndex.put(key, count++);
				others--;
			}
			eventTable.put(key, indexer.wasEndedByTable.get(key));
		}

		for (String key : indexer.wasInvalidatedByTable.keySet()) {
			if (others > 0) {
				eventsList.add(key);
				eventKindsList.add("i");
				eventIndex.put(key, count++);
				others--;
			}
			eventTable.put(key, indexer.wasInvalidatedByTable.get(key));
		}

		// truncate!
		if (count > MAX_VALUE) {
			count = MAX_VALUE;
		}


		events = new Vector<>(eventsList);
		eventKinds = new Vector<>(eventKindsList);

		logger.debug("allocating ");

		logger.debug("events: " + events);
		logger.debug("eventIndex: " + eventIndex);
	}

}
