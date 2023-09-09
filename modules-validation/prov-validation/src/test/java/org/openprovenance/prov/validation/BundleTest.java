package org.openprovenance.prov.validation;


public class BundleTest extends CoreValidateTester{

    
    public void testUnificationBundleSuccess1() {
        testUnification("src/test/resources/validate/unification/bundle-success1.provn",
                        0, 0, z, z, 0, 0, false);
    }
    
    public void testUnificationBundleSuccess2() {
        testUnification("src/test/resources/validate/unification/bundle-success2.provn",
                        0, 0, z, z, 0, 0, false);
    }

    public void testUnificationBundleFail1() {
        testUnification("src/test/resources/validate/unification/bundle-fail1.provn",
                        0, 0, z, z, 0, 0, false);
        // note, nested reports identifying failure TODO: check nested reports
    }
    
    // TODO detect when a document contains two bundles with same name
   
}
