package org.openprovenance.prov.validation;

public class Validate2Test extends CoreValidateTester{

    //-----------------  bundle

    
    
    public void testUnificationBundleStartSuccess1() {
        testUnification("src/test/resources/validate/unification/bundle-success1.provn",
                        0, 0, z, z, 0, 0, false);
    }

    
    
      
}
