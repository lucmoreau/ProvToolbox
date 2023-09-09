package org.openprovenance.prov.validation;

import java.util.UUID;

import org.openprovenance.prov.model.ActedOnBehalfOf;
import org.openprovenance.prov.model.Activity;
import org.openprovenance.prov.model.Agent;
import org.openprovenance.prov.model.Entity;
import org.openprovenance.prov.model.Bundle;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.model.Namespace;
import org.openprovenance.prov.model.QualifiedName;
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

public class Gensym {
    final UUID gensym;
    final String this_VAL_URI;
    final Namespace namespace;
    final ProvFactory pFactory;
    final public static String VAL_ID = "id_";
    final public static String VAL_PREFIX = "val";
    final public static String VAL_URI = "http://openprovenance.org/validation/";
    private final ObjectMaker om;

    public Gensym(Namespace namespace, ProvFactory pFactory, ObjectMaker om) {
    	this.pFactory=pFactory;
        this.namespace=namespace;
        UUID gensym = UUID.randomUUID();
        this.gensym = gensym;
        this.this_VAL_URI = VAL_URI + gensym + "#";
        this.om=om;
    }

    static int e_count = 0;
    static int a_count = 0;
    static int ag_count = 0;
    static int use_count = 0;
    static int gen_count = 0;
    static int inv_count = 0;
    static int start_count = 0;
    static int der_count = 0;
    static int end_count = 0;
    static int inf_count = 0;
    static int assoc_count = 0;
    static int wat_count = 0;
    static int aob_count = 0;
    static int winflb_count = 0;
    static int bun_count = 0;
    static int time_count = 0;

    public Unknown newUnknown() {
        Unknown u=om.makeUnknown(this_VAL_URI, VAL_PREFIX);
        return u;
    }

    VarTime newVarTime() {
        VarTime vt = new VarTime(newVarTimeId());
        return vt;
    }

    String newVarTimeId() {
        int val = time_count++;
        return this_VAL_URI + VAL_ID + "t_" + val;
    }

    VarQName newId(Entity ignored) {
        int val = e_count++;
        return om.makeVarQName(namespace.stringToQualifiedName(VAL_PREFIX + ":" + VAL_ID + "e_"
                + val, pFactory));
    }

    
    VarQName newId(QualifiedName ignored) {
        int val = e_count++;
        return om.makeVarQName(namespace.stringToQualifiedName(VAL_PREFIX + ":" + VAL_ID + "e_"
                + val, pFactory));
    }

    VarQName newId(Activity ignored) {
        int val = a_count++;
        return om.makeVarQName(namespace.stringToQualifiedName(VAL_PREFIX + ":" + VAL_ID + "a_"
                + val, pFactory));
    }

  //  VarQName newId(ActivityRef ignored) {
  //      int val = a_count++;
  //      return om.makeVarQName(p.stringToQualifiedName(VAL_PREFIX + ":" + VAL_ID + "a_"
  //              + val));
  //  }

    VarQName newId(Agent ignored) {
        int val = ag_count++;
        return om.makeVarQName(namespace.stringToQualifiedName(VAL_PREFIX + ":" + VAL_ID + "ag_"
                + val, pFactory));
    }

    VarQName newId(Used ignored) {
        int val = use_count++;
        return om.makeVarQName(namespace.stringToQualifiedName(VAL_PREFIX + ":" + VAL_ID + "use_"
                + val, pFactory));
    }

    VarQName newId(WasGeneratedBy ignored) {
        int val = gen_count++;
        VarQName tmp = om.makeVarQName(namespace.stringToQualifiedName(VAL_PREFIX + ":" + VAL_ID
                + "gen_" + val, pFactory));
        return tmp;
    }

    VarQName newId(WasInvalidatedBy ignored) {
        int val = inv_count++;
        return om.makeVarQName(namespace.stringToQualifiedName(VAL_PREFIX + ":" + VAL_ID + "inv_"
                + val, pFactory));
    }

    VarQName newId(WasStartedBy ignored) {
        int val = start_count++;
        return om.makeVarQName(namespace.stringToQualifiedName(VAL_PREFIX + ":" + VAL_ID
                + "start_" + val, pFactory));
    }

    VarQName newId(WasEndedBy ignored) {
        int val = end_count++;
        return om.makeVarQName(namespace.stringToQualifiedName(VAL_PREFIX + ":" + VAL_ID + "end_"
                + val, pFactory));
    }

    VarQName newId(WasDerivedFrom ignored) {
        int val = der_count++;
        return om.makeVarQName(namespace.stringToQualifiedName(VAL_PREFIX + ":" + VAL_ID + "der_"
                + val, pFactory));
    }

    VarQName newId(WasInformedBy ignored) {
        int val = inf_count++;
        return om.makeVarQName(namespace.stringToQualifiedName(VAL_PREFIX + ":" + VAL_ID + "inf_"
                + val, pFactory));
    }

    VarQName newId(WasAssociatedWith ignored) {
        int val = assoc_count++;
        return om.makeVarQName(namespace.stringToQualifiedName(VAL_PREFIX + ":" + VAL_ID
                + "assoc_" + val, pFactory));
    }

    VarQName newId(WasAttributedTo ignored) {
        int val = wat_count++;
        return om.makeVarQName(namespace.stringToQualifiedName(VAL_PREFIX + ":" + VAL_ID + "wat_"
                + val, pFactory));
    }

    VarQName newId(WasInfluencedBy ignored) {
        int val = winflb_count++;
        return om.makeVarQName(namespace.stringToQualifiedName(VAL_PREFIX + ":" + VAL_ID
                + "winflb_" + val, pFactory));
    }

    VarQName newId(ActedOnBehalfOf ignored) {
        int val = aob_count++;
        return om.makeVarQName(namespace.stringToQualifiedName(VAL_PREFIX + ":" + VAL_ID + "aob_"
                + val, pFactory));
    }

    VarQName newId(Bundle ignored) {
        int val = bun_count++;
        return om.makeVarQName(namespace.stringToQualifiedName(VAL_PREFIX + ":" + VAL_ID + "bun_"
                + val, pFactory));
    }

    void setId(Entity o) {
        o.setId(newId(o));
    }


    void setId(Activity o) {
        o.setId(newId(o));
    }

 
    void setId(Agent o) {
        o.setId(newId(o));
    }

    void setId(Used o) {
        o.setId(newId(o));
    }

    void setId(WasGeneratedBy o) {
        o.setId(newId(o));
    }

    void setId(WasInvalidatedBy o) {
        o.setId(newId(o));
    }

    void setId(WasStartedBy o) {
        o.setId(newId(o));
    }

    void setId(WasEndedBy o) {
        o.setId(newId(o));
    }

    void setId(WasDerivedFrom o) {
        o.setId(newId(o));
    }

    void setId(WasInformedBy o) {
        o.setId(newId(o));
    }

    void setId(WasAssociatedWith o) {
        o.setId(newId(o));
    }

    void setId(WasAttributedTo o) {
        o.setId(newId(o));
    }

    void setId(WasInfluencedBy o) {
        o.setId(newId(o));
    }

    void setId(ActedOnBehalfOf o) {
        o.setId(newId(o));
    }

   
}
