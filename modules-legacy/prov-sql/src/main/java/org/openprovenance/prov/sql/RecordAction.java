package org.openprovenance.prov.sql;

public interface RecordAction {
    public void run(Entity e);
    public void run(Activity a);
    public void run(Agent ag);
    public void run(WasGeneratedBy gen);
    public void run(Used use);
    public void run(WasInvalidatedBy inv);
    public void run(WasStartedBy start);
    public void run(WasEndedBy end);
    public void run(WasInformedBy inf);
    public void run(WasDerivedFrom der);
    public void run(WasAssociatedWith assoc);
    public void run(WasAttributedTo attr);
    public void run(ActedOnBehalfOf del);
    public void run(WasInfluencedBy inf);
    public void run(AlternateOf alt);
    public void run(MentionOf men);
    public void run(SpecializationOf spec);
    public void run(HadMember mem);

}
