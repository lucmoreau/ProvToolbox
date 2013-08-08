package org.openprovenance.prov.model;


public interface RecordValue {
    public Object run(Entity e);
    public Object run(Activity a);
    public Object run(Agent ag);
    public Object run(WasGeneratedBy gen);
    public Object run(Used use);
    public Object run(WasInvalidatedBy inv);
    public Object run(WasStartedBy start);
    public Object run(WasEndedBy end);
    public Object run(WasInformedBy inf);
    public Object run(WasDerivedFrom der);
    public Object run(WasAssociatedWith assoc);
    public Object run(WasAttributedTo attr);
    public Object run(ActedOnBehalfOf del);
    public Object run(WasInfluencedBy inf);
    public Object run(AlternateOf alt);
    public Object run(MentionOf men);
    public Object run(SpecializationOf spec);
    public Object run(HadMember mem);


}
