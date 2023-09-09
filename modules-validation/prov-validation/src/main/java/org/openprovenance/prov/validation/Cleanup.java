package org.openprovenance.prov.validation;

import java.util.List;

import org.openprovenance.prov.model.ActedOnBehalfOf;
import org.openprovenance.prov.model.Activity;
import org.openprovenance.prov.model.Agent;
import org.openprovenance.prov.model.AlternateOf;
import org.openprovenance.prov.model.Entity;
import org.openprovenance.prov.model.HadMember;
import org.openprovenance.prov.model.MentionOf;
import org.openprovenance.prov.model.Bundle;
import org.openprovenance.prov.model.ProvUtilities;
import org.openprovenance.prov.model.DerivedByInsertionFrom;
import org.openprovenance.prov.model.DerivedByRemovalFrom;
import org.openprovenance.prov.model.DictionaryMembership;
import org.openprovenance.prov.model.SpecializationOf;
import org.openprovenance.prov.model.Statement;
import org.openprovenance.prov.model.StatementAction;
import org.openprovenance.prov.model.Used;
import org.openprovenance.prov.model.WasAssociatedWith;
import org.openprovenance.prov.model.WasAttributedTo;
import org.openprovenance.prov.model.WasDerivedFrom;
import org.openprovenance.prov.model.WasEndedBy;
import org.openprovenance.prov.model.WasGeneratedBy;
import org.openprovenance.prov.model.WasInfluencedBy;
import org.openprovenance.prov.model.WasInformedBy;
import org.openprovenance.prov.model.WasInvalidatedBy;
import org.openprovenance.prov.model.WasStartedBy;
import org.openprovenance.prov.model.extension.QualifiedAlternateOf;
import org.openprovenance.prov.model.extension.QualifiedHadMember;
import org.openprovenance.prov.model.extension.QualifiedSpecializationOf;


/** A class to clean up beans before serialization. 
 *  Currently, only clean up operation is to clear Time Variables*/

public class Cleanup {
    final ProvUtilities u;

    public Cleanup(ProvUtilities u) {
        this.u=u;
    }

    public void cleanup(Config config, List<Statement> records, List<Bundle> bundles) {
        u.forAllStatement(records, new CleanupAction(config));
        if (bundles!=null) {
            for (Bundle b: bundles) {
                u.forAllStatement(b.getStatement(), new CleanupAction(config));
            }
        }
    }


    static class CleanupAction implements StatementAction {
        Config config;

        public CleanupAction(Config config) {
            this.config = config;
        }


        public void doAction(Entity e) {
        }


        public void doAction(Activity a) {
            if (a.getStartTime() instanceof VarTime) a.setStartTime(null);
            if (a.getEndTime() instanceof VarTime) a.setEndTime(null);
        }


        public void doAction(Agent ag) {
        }


        public void doAction(WasGeneratedBy gen) {
            if (gen.getTime() instanceof VarTime) gen.setTime(null);
        }


        public void doAction(Used use) {
            if (use.getTime() instanceof VarTime) use.setTime(null);
        }


        public void doAction(WasInvalidatedBy inv) {
            if (inv.getTime() instanceof VarTime) inv.setTime(null);
        }


        public void doAction(WasStartedBy start) {
            if (start.getTime() instanceof VarTime) start.setTime(null);
        }


        public void doAction(WasEndedBy end) {
            if (end.getTime() instanceof VarTime) end.setTime(null);
        }


        public void doAction(WasInformedBy inf) {
        }


        public void doAction(WasDerivedFrom der) {
            if ((der.getActivity()!=null)
                    && (der.getActivity() instanceof Unknown)) {
                der.setActivity(null);
            }
            if ((der.getGeneration()!=null)
                    && (der.getGeneration() instanceof Unknown)) {
                der.setGeneration(null);
            }
            if ((der.getUsage()!=null)
                    && (der.getUsage() instanceof Unknown)) {
                der.setUsage(null);
            }
        }


        public void doAction(WasAssociatedWith assoc) {
            if ((assoc.getPlan()!=null)
                    && (assoc.getPlan() instanceof Unknown)) {
                assoc.setPlan(null);
            }
        }


        public void doAction(WasAttributedTo attr) {
        }

        public void doAction(ActedOnBehalfOf del) {
        }

        public void doAction(WasInfluencedBy inf) {
        }


        public void doAction(AlternateOf alt) {
        }


        public void doAction(QualifiedAlternateOf alt) {
        }

        public void doAction(MentionOf men) {
        }

        public void doAction(SpecializationOf spec) {
        }

        public void doAction(QualifiedSpecializationOf spec) {
        }


        public void doAction(HadMember mem) {
        }


        public void doAction(QualifiedHadMember mem) {

        }


        public void doAction(DictionaryMembership s) {
        }


        public void doAction(DerivedByRemovalFrom s) {
        }


        public void doAction(DerivedByInsertionFrom s) {
        }

        public void doAction(Bundle s,
                             org.openprovenance.prov.model.ProvUtilities provUtilities) {
        }

    }

}
