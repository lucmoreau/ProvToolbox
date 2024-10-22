package org.openprovenance.prov.rules.counters;

public class EntityActivityDerivationCounter {
    public int triangle=0;
    public int entities=0;
    public int rootEntities;
    public int activities;
    public int entitiesWithTriangle;
    public int activitiesWithTriangle;
    public int wdf_with_activity=0;
    public int wdf_with_wgb=0;
    public int wdf_with_usd=0;
    public int count0=0;
    public int count1=0;
    public int count2=0;
    public int count3=0;


    @Override
    public String toString() {
        return "EntityActivityDerivationCounter{" +
                "triangle=" + triangle +
                ", entities=" + entities +
                ", rootEntities=" + rootEntities +
                ", activities=" + activities +
                ", entitiesWithTriangle=" + entitiesWithTriangle +
                ", activitiesWithTriangle=" + activitiesWithTriangle +
                ", wdf_with_activity=" + wdf_with_activity +
                ", wdf_with_wgb=" + wdf_with_wgb +
                ", wdf_with_usd=" + wdf_with_usd +
                ", count0=" + count0 +
                ", count1=" + count1 +
                ", count2=" + count2 +
                ", count3=" + count3 +
                '}';
    }
}
