package org.openprovenance.prov.tutorial.tutorial6;


import org.openprovenance.prov.interop.InteropFramework;
import org.openprovenance.prov.interop.InteropFramework.ProvFormat;
import org.openprovenance.prov.model.Activity;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.Entity;
import org.openprovenance.prov.model.Name;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.model.QualifiedName;
import org.openprovenance.prov.model.Used;
import org.openprovenance.prov.model.WasDerivedFrom;
import org.openprovenance.prov.model.WasGeneratedBy;

public class ChallengeUtil implements ChallengeConstants {

    protected final ProvFactory pFactory;
    protected final Name name;

    public ChallengeUtil(ProvFactory pFactory) {
        this.pFactory=pFactory;
        this.name=pFactory.getName();
    }

    public Entity newFile(ProvFactory pFactory, String id, String label) {
    
           Entity a = pFactory.newEntity(pc(id), label);
           pFactory.addType(a, pFactory.newType(pFactory.newQualifiedName(PRIM_NS, FILE, PRIM_PREFIX),name.PROV_QUALIFIED_NAME));
           return a;
    }

    protected Entity newParameter(ProvFactory pFactory, String id, String label, String value) {
    
        Entity a = pFactory.newEntity(pc(id), label);
        pFactory.addType(a, pFactory.newType(pFactory.newQualifiedName(PRIM_NS, STRING, PRIM_PREFIX),name.PROV_QUALIFIED_NAME));
        a.setValue(pFactory.newValue(value));
        return a;
    }

    
    public QualifiedName pc(String n) {
        return pFactory.newQualifiedName(PC1_NS, n, PC1_PREFIX);
    }

    public QualifiedName prim(String name) {
        return pFactory.newQualifiedName(PRIM_NS, name, PRIM_PREFIX);
    }
    
    
    public Used newUsed(Activity activity, String role, Entity entity) {
        return newUsed(activity.getId(), role, entity.getId());
    }

    public Used newUsed(org.openprovenance.prov.model.QualifiedName activity, String role, org.openprovenance.prov.model.QualifiedName entity) {
        Used u1 = pFactory.newUsed(activity, entity);
        u1.getRole().add(pFactory.newRole(prim(role), name.PROV_QUALIFIED_NAME));
        return u1;
    
    }

    public WasGeneratedBy newWasGeneratedBy(Entity entity, String role, Activity activity) {
        return newWasGeneratedBy(entity.getId(), role, activity.getId());
    }

    public WasGeneratedBy newWasGeneratedBy(org.openprovenance.prov.model.QualifiedName entity, String role, org.openprovenance.prov.model.QualifiedName activity) {
        WasGeneratedBy u1 = pFactory.newWasGeneratedBy(null, entity, activity);
        u1.getRole().add(pFactory.newRole(prim(role), name.PROV_QUALIFIED_NAME));
        return u1;
    
    }

    public WasDerivedFrom newWasDerivedFrom(Entity entity2, Entity entity1) {
        WasDerivedFrom wdf = pFactory.newWasDerivedFrom(null, entity2.getId(), entity1.getId());
        return wdf;
    }

    public void closingBanner() {
        System.out.println("");
        System.out.println("*************************");
    }

    public void openingBanner() {
        System.out.println("*************************");
        System.out.println("* Converting document  ");
        System.out.println("*************************");
    }


    public void doConversions(Document document, String file) {
        InteropFramework intF = new InteropFramework();
        intF.writeDocument(file, document);
        intF.writeDocument(System.out, ProvFormat.PROVN, document);
    }
    
    
   
}