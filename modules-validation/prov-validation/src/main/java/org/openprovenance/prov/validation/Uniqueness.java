package org.openprovenance.prov.validation;

import java.util.Hashtable;

import org.openprovenance.prov.model.Activity;
import org.openprovenance.prov.model.Influence;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.model.ProvUtilities;
import org.openprovenance.prov.model.WasEndedBy;
import org.openprovenance.prov.model.WasGeneratedBy;
import org.openprovenance.prov.model.WasInvalidatedBy;
import org.openprovenance.prov.model.WasStartedBy;

/** This class deals with uniqueness constraints, in the process unifying terms. */

public class Uniqueness {

    public final Indexer indexer;
    final ProvFactory p;
    final ProvUtilities u;

    public Uniqueness(Indexer indexer) {
        this.indexer = indexer;
        this.p = indexer.p;
        this.u = indexer.u;
    }

    public Hashtable<TwoKeys, WasGeneratedBy> wasGeneratedByTable = new Hashtable<>();
    public Hashtable<TwoKeys, WasInvalidatedBy> wasInvalidatedByTable = new Hashtable<>();
    public Hashtable<TwoKeys, WasStartedBy> wasStartedByTable = new Hashtable<>();
    public Hashtable<TwoKeys, WasEndedBy> wasEndedByTable = new Hashtable<>();

    public void uniqueAll(Config config) {
        uniqueAll_wasStartedBy(config);
        uniqueAll_wasEndedBy(config);
        uniqueAll_wasGeneratedBy(config);
        uniqueAll_wasInvalidatedBy(config);
        uniqueAll_activityStartTime(config);
        uniqueAll_activityEndTime(config);
    }


    public void uniqueAll_wasStartedBy(Config config) {
        uniqueAll_Record(WasStartedBy.class, indexer.wasStartedByTable,
                wasStartedByTable, config);
    }

    public void uniqueAll_wasEndedBy(Config config) {
        uniqueAll_Record(WasEndedBy.class, indexer.wasEndedByTable,
                wasEndedByTable, config);
    }

    public void uniqueAll_wasGeneratedBy(Config config) {
        uniqueAll_Record(WasGeneratedBy.class, indexer.wasGeneratedByTable,
                wasGeneratedByTable, config);
    }

    public void uniqueAll_wasInvalidatedBy(Config config) {
        uniqueAll_Record(WasInvalidatedBy.class, indexer.wasInvalidatedByTable,
                wasInvalidatedByTable, config);
    }

    public void uniqueAll_activityStartTime(Config config) {
        for (String activityKey: indexer.activityTable.keySet()) {
            Activity activity=indexer.activityTable.get(activityKey);
            for (String startKey: indexer.wasStartedByTable.keySet()) {
                WasStartedBy start=indexer.wasStartedByTable.get(startKey);
                if (indexer.merger.equalQName(start.getActivity(),
                        activity.getId())) {
                    indexer.merger.sameStartTime(activityKey,activity,start);
                }
            }
        }
    }

    public void uniqueAll_activityEndTime(Config config) {
        for (String activityKey: indexer.activityTable.keySet()) {
            Activity activity=indexer.activityTable.get(activityKey);
            for (String endKey: indexer.wasEndedByTable.keySet()) {
                WasEndedBy end=indexer.wasEndedByTable.get(endKey);
                if (indexer.merger.equalQName(end.getActivity(),
                        activity.getId())) {
                    indexer.merger.sameEndTime(activityKey,activity,end);
                }
            }
        }
    }



    public <T extends Influence> void uniqueAll_Record(Class<T> cl,
                                                       Hashtable<String, T> table1,
                                                       Hashtable<TwoKeys, T> table2,
                                                       Config config) {
        for (String key : table1.keySet()) {
            T tmp = table1.get(key);
            TwoKeys tk = TwoKeys.makeTwoKeys(tmp);
            T start = table2.get(tk);
            if (start == null) {
                table2.put(tk, tmp);
            } else {
                if (start == tmp)
                    return;
                indexer.resolveDuplicate(start, tmp);
            }
        }

    }

}
