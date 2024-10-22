package org.openprovenance.prov.validation;

import java.io.File;

public class ValidateTest extends CoreValidateTester {

    public void testUnificationActivitySuccess1() {
        testUnification("src/test/resources/validate/unification/activity-success1.provn",
                0, 0, z, one, 0, 0, false);
    }
    public void testUnificationActivitySuccess2() {
        testUnification("src/test/resources/validate/unification/activity-success2.provn",
                0, 0, z, one, 0, 0, false);
    }
    public void testUnificationActivitySuccess3() {
        testUnification("src/test/resources/validate/unification/activity-success3.provn",
                0, 0, z, one, 0, 0, false);
    }
    public void testUnificationActivitySuccess4() {
        testUnification("src/test/resources/validate/unification/activity-success4.provn",
                0, 0, z, one, 0, 0, false);
    }
    public void testUnificationActivityStartSuccess1() {
        testUnification("src/test/resources/validate/unification/activity-start-success1.provn",
                0, 0, z, one, 0, 0, false);
    }

    public void testUnificationActivityStartFail1() {
        testUnification("src/test/resources/validate/unification/activity-start-fail1.provn",
                0, 0, one, one, 0, 0, false);
    }

    public void testUnificationActivityEndSuccess1() {
        testUnification("src/test/resources/validate/unification/activity-end-success1.provn",
                0, 0, z, one, 0, 0, false);
    }
    public void testUnificationActivityEndFail1() {
        testUnification("src/test/resources/validate/unification/activity-end-fail1.provn",
                0, 0, one, one, 0, 0, false);
    }

    //-----------------  derivation

    public void testUnificationDerivationSuccess1() {
        testUnification("src/test/resources/validate/unification/derivation-success1.provn",
                0, 0, z, one, 0, 0, false);
    }
    public void testUnificationDerivationSuccess2() {
        testUnification("src/test/resources/validate/unification/derivation-success2.provn",
                0, 0, z, one, 0, 0, false);
    }
    public void testUnificationDerivationSuccess3() {
        testUnification("src/test/resources/validate/unification/derivation-success3.provn",
                0, 0, z, one, 0, 0, false);
    }
    public void testUnificationDerivationSuccess4() {
        testUnification("src/test/resources/validate/unification/derivation-success4.provn",
                0, 0, z, one, 0, 0, false);
    }
    public void testUnificationDerivationSuccess5() {
        testUnification("src/test/resources/validate/unification/derivation-success5.provn",
                0, 0, z, one, 0, 0, false);
    }

    public void testUnificationDerivationFail1() {
        testUnification("src/test/resources/validate/unification/derivation-fail1.provn",
                0, 0, one, z, 0, 0, false);
    }
    public void testUnificationDerivationFail2() {
        testUnification("src/test/resources/validate/unification/derivation-fail2.provn",
                0, 0, one, z, 0, 0, false);
    }
    public void testUnificationDerivationFail3() {
        testUnification("src/test/resources/validate/unification/derivation-fail3.provn",
                0, 0, one, z, 0, 0, false);
    }
    public void testUnificationDerivationFail4() {
        testUnification("src/test/resources/validate/unification/derivation-fail4.provn",
                0, 0, one, z, 0, 0, false);
    }


    //-----------------  start
    public void testUnificationStartSuccess1() {
        testUnification("src/test/resources/validate/unification/start-success1.provn",
                0, // cycles
                0, // cycles
                z, // failed
                one, // successes
                0, // mismatch
                0, // malformed
                false // exception
        );
    }

    public void testUnificationStartSuccess2() {
        testUnification("src/test/resources/validate/unification/start-success2.provn",
                0, 0, z, one, 0, 0, false);
    }

    public void testUnificationStartSuccess3() {
        testUnification("src/test/resources/validate/unification/start-success3.provn",
                0, 0, z, one, 0, 0, false);
    }
    public void testUnificationStartSuccess4() {
        testUnification("src/test/resources/validate/unification/start-success4.provn",
                0, 0, z, one, 0, 0, false);
    }
    public void testUnificationStartSuccess5() {
        testUnification("src/test/resources/validate/unification/start-success5.provn",
                0, 0, z, v12, 0, 0, false);
    }
    public void testUnificationStartSuccess6() {
        testUnification("src/test/resources/validate/unification/start-success6.provn",
                0, 0, z, v12, 0, 0, false);
    }

    public void testUnificationStartSuccess7() {
        testUnification("src/test/resources/validate/unification/start-success7.provn",
                0, 0, z, v12, 0, 0, false);
    }
    public void testUnificationStartSuccess8() {
        testUnification("src/test/resources/validate/unification/start-success8.provn",
                0, 0, z, v12, 0, 0, false);
    }
    public void testUnificationStartSuccess9() {
        testUnification("src/test/resources/validate/unification/start-success9.provn",
                0, 0, z, v12, 0, 0, false);
    }

    public void testUnificationStartFail1() {
        testUnification("src/test/resources/validate/unification/start-fail1.provn",
                0, 0, one, z, 0, 0, false);
    }
    public void testUnificationStartFail2() {
        testUnification("src/test/resources/validate/unification/start-fail2.provn",
                0, 0, one, z, 0, 0, false);
    }
    public void testUnificationStartFail3() {
        testUnification("src/test/resources/validate/unification/start-fail3.provn",
                0, 0, one, z, 0, 0, false);
    }
    public void testUnificationStartFail4() {
        testUnification("src/test/resources/validate/unification/start-fail4.provn",
                0, 2, one, z, 0, 0, false);
    }
    public void testUnificationStartFail5() {
        testUnification("src/test/resources/validate/unification/start-fail5.provn",
                0, 0, one, z, 0, 0, false);
    }

    public void testUnificationStartFail6() {
        testUnification("src/test/resources/validate/unification/start-fail6.provn",
                0, 0, one, one, 0, 0, false);
    }
    public void testUnificationStartFail7() {
        testUnification("src/test/resources/validate/unification/start-fail7.provn",
                0, 0, v12, one, 0, 0, false);
    }
    public void testUnificationStartFail8() {
        testUnification("src/test/resources/validate/unification/start-fail8.provn",
                0, 0, v123, z, 0, 0, false);
    }


    // -----------------  End

    public void testUnificationEndSuccess1() {
        testUnification("src/test/resources/validate/unification/end-success1.provn",
                0, 0, z, one, 0, 0, false);
    }
    public void testUnificationEndSuccess2() {
        testUnification("src/test/resources/validate/unification/end-success2.provn",
                0, 0, z, one, 0, 0, false);
    }

    public void testUnificationEndSuccess3() {
        testUnification("src/test/resources/validate/unification/end-success3.provn",
                0, 0, z, one, 0, 0, false);
    }
    public void testUnificationEndSuccess4() {
        testUnification("src/test/resources/validate/unification/end-success4.provn",
                0, 0, z, one, 0, 0, false);
    }
    public void testUnificationEndSuccess5() {
        testUnification("src/test/resources/validate/unification/end-success5.provn",
                0, 0, z, v12, 0, 0, false);
    }
    public void testUnificationEndSuccess6() {
        testUnification("src/test/resources/validate/unification/end-success6.provn",
                0, 0, z, v12, 0, 0, false);
    }
    public void testUnificationEndFail1() {
        testUnification("src/test/resources/validate/unification/end-fail1.provn",
                0, 0, one, z, 0, 0, false);
    }
    public void testUnificationEndFail2() {
        testUnification("src/test/resources/validate/unification/end-fail2.provn",
                0, 0, one, z, 0, 0, false);
    }
    public void testUnificationEndFail3() {
        testUnification("src/test/resources/validate/unification/end-fail3.provn",
                0, 0, one, z, 0, 0, false);
    }
    public void testUnificationEndFail4() {
        testUnification("src/test/resources/validate/unification/end-fail4.provn",
                0, 2, one, z, 0, 0, false);
    }
    public void testUnificationEndFail5() {
        testUnification("src/test/resources/validate/unification/end-fail5.provn",
                0, 0, one, z, 0, 0, false);
    }


    public void testUnificationNF1() {
        testUnification("src/test/resources/validate/nf-entity-test1.provn",
                0, 0, z, z, 0, 0, false);
    }

    public void testUnificationNF2() {
        testUnification("src/test/resources/validate/nf-activity-test1.provn",
                0, 0, z, z, 0, 0, false);
    }

    //TODO this is failing
    public void NOtestUnificationNF3() {
        testUnification("src/test/resources/validate/nf-start-test1.provn",
                0, 0, z, z, 0, 0, false);
    }
    //TODO this is failing
    public void NOtestUnificationNF4() {
        testUnification("src/test/resources/validate/nf-start-test2.provn",
                0, 0, z, z, 0, 0, false);
    }


    // ------------------- generation


    public void testUnificationGenerationSuccess1() {
        testUnification("src/test/resources/validate/unification/generation-success1.provn",
                0, 0, z, one, 0, 0, false);
    }
    public void testUnificationGenerationSuccess2() {
        testUnification("src/test/resources/validate/unification/generation-success2.provn",
                0, 0, z, one, 0, 0, false);
    }
    public void testUnificationGenerationSuccess3() {
        testUnification("src/test/resources/validate/unification/generation-success3.provn",
                0, 0, z, one, 0, 0, false);
    }
    //TODO test failing on ordering constraints
    public void NOtestUnificationGenerationSuccess4() {
        testUnification("src/test/resources/validate/unification/generation-success4.provn",
                0, 0, z, z, 0, 0, false);
    }
    public void testUnificationGenerationSuccess5() {
        testUnification("src/test/resources/validate/unification/generation-success5.provn",
                0, 0, z, one, 0, 0, false);
    }
    public void testUnificationGenerationSuccess6() {
        testUnification("src/test/resources/validate/unification/generation-success6.provn",
                0, 0, z, one, 0, 0, false);
    }
    public void testUnificationGenerationSuccess7() {
        testUnification("src/test/resources/validate/unification/generation-success7.provn",
                0, 0, z, v12, 0, 0, false);
    }
    public void testUnificationGenerationSuccess8() {
        testUnification("src/test/resources/validate/unification/generation-success8.provn",
                0, 0, z, v12, 0, 0, false);
    }

    public void testUnificationGenerationFail1() {
        testUnification("src/test/resources/validate/unification/generation-fail1.provn",
                0, 2, one, z, 0, 0, false);
    }
    public void testUnificationGenerationFail2() {
        testUnification("src/test/resources/validate/unification/generation-fail2.provn",
                0, 0, one, z, 0, 0, false);
    }
    public void testUnificationGenerationFail3() {
        testUnification("src/test/resources/validate/unification/generation-fail3.provn",
                0, 0, one, z, 0, 0, false);
    }
    public void testUnificationGenerationFail4() {
        testUnification("src/test/resources/validate/unification/generation-fail4.provn",
                0, 0, one, z, 0, 0, false);
    }
    public void testUnificationGenerationFail5() {
        testUnification("src/test/resources/validate/unification/generation-fail5.provn",
                0, 0, one, z, 0, 0, false);
    }
    public void testUnificationGenerationFail6() {
        testUnification("src/test/resources/validate/unification/generation-fail6.provn",
                0, 0, one, z, 0, 0, false);
    }
    public void testUnificationGenerationFail7() {
        testUnification("src/test/resources/validate/unification/generation-fail7.provn",
                0, 0, one, one, 0, 0, false);
    }



    // ------------------- invalidation


    public void testUnificationInvalidationSuccess1() {
        testUnification("src/test/resources/validate/unification/invalidation-success1.provn",
                0, 0, z, one, 0, 0, false);
    }
    public void testUnificationInvalidationSuccess2() {
        testUnification("src/test/resources/validate/unification/invalidation-success2.provn",
                0, 0, z, one, 0, 0, false);
    }
    public void testUnificationInvalidationSuccess3() {
        testUnification("src/test/resources/validate/unification/invalidation-success3.provn",
                0, 0, z, one, 0, 0, false);
    }
    //TODO test failing on ordering constraints
    public void NOtestUnificationInvalidationSuccess4() {
        testUnification("src/test/resources/validate/unification/invalidation-success4.provn",
                0, 0, z, z, 0, 0, false);
    }
    public void testUnificationInvalidationSuccess5() {
        testUnification("src/test/resources/validate/unification/invalidation-success5.provn",
                0, 0, z, one, 0, 0, false);
    }
    public void testUnificationInvalidationSuccess6() {
        testUnification("src/test/resources/validate/unification/invalidation-success6.provn",
                0, 0, z, one, 0, 0, false);
    }
    public void testUnificationInvalidationSuccess7() {
        testUnification("src/test/resources/validate/unification/invalidation-success7.provn",
                0, 0, z, v12, 0, 0, false);
    }
    public void testUnificationInvalidationSuccess8() {
        testUnification("src/test/resources/validate/unification/invalidation-success8.provn",
                0, 0, z, v12, 0, 0, false);
    }

    public void testUnificationInvalidationFail1() {
        testUnification("src/test/resources/validate/unification/invalidation-fail1.provn",
                0, 2, one, z, 0, 0, false);
    }
    public void testUnificationInvalidationFail2() {
        testUnification("src/test/resources/validate/unification/invalidation-fail2.provn",
                0, 0, one, z, 0, 0, false);
    }
    public void testUnificationInvalidationFail3() {
        testUnification("src/test/resources/validate/unification/invalidation-fail3.provn",
                0, 0, one, z, 0, 0, false);
    }
    public void testUnificationInvalidationFail4() {
        testUnification("src/test/resources/validate/unification/invalidation-fail4.provn",
                0, 0, one, z, 0, 0, false);
    }
    public void testUnificationInvalidationFail5() {
        testUnification("src/test/resources/validate/unification/invalidation-fail5.provn",
                0, 0, one, z, 0, 0, false);
    }
    public void testUnificationInvalidationFail6() {
        testUnification("src/test/resources/validate/unification/invalidation-fail6.provn",
                0, 0, one, z, 0, 0, false);
    }
    public void testUnificationInvalidationFail7() {
        testUnification("src/test/resources/validate/unification/invalidation-fail7.provn",
                0, 0, one, one, 0, 0, false);
    }


    // ------------------- Usage


    public void testUnificationUsageSuccess1() {
        testUnification("src/test/resources/validate/unification/usage-success1.provn",
                0, 0, z, one, 0, 0, false);
    }
    public void testUnificationUsageSuccess2() {
        testUnification("src/test/resources/validate/unification/usage-success2.provn",
                0, 0, z, one, 0, 0, false);
    }
    public void testUnificationUsageSuccess3() { // SUCCESS: no unique-ness property
        testUnification("src/test/resources/validate/unification/usage-success3.provn",
                0, 0, z, z, 0, 0, false);
    }
    //TODO test failing on ordering constraints
    public void NOtestUnificationUsageSuccess4() {
        testUnification("src/test/resources/validate/unification/usage-success4.provn",
                0, 0, z, z, 0, 0, false);
    }
    public void testUnificationUsageSuccess5() {
        testUnification("src/test/resources/validate/unification/usage-success5.provn",
                0, 0, z, one, 0, 0, false);
    }
    public void testUnificationUsageSuccess6() {
        testUnification("src/test/resources/validate/unification/usage-success6.provn",
                0, 0, z, one, 0, 0, false);
    }
    public void testUnificationUsageSuccess7() {
        testUnification("src/test/resources/validate/unification/usage-success7.provn",
                0, 0, z, v12, 0, 0, false);
    }
    public void testUnificationUsageSuccess8() {
        testUnification("src/test/resources/validate/unification/usage-success8.provn",
                0, 0, z, one, 0, 0, false);
    }

    public void testUnificationUsageFail1() { // SUCCESS: no uniqueness
        testUnification("src/test/resources/validate/unification/usage-fail1.provn",
                0, 0, z, z, 0, 0, false);
    }
    public void testUnificationUsageFail2() {
        testUnification("src/test/resources/validate/unification/usage-fail2.provn",
                0, 0, one, z, 0, 0, false);
    }
    public void testUnificationUsageFail3() {
        testUnification("src/test/resources/validate/unification/usage-fail3.provn",
                0, 0, one, z, 0, 0, false);
    }
    public void testUnificationUsageFail4() {
        testUnification("src/test/resources/validate/unification/usage-fail4.provn",
                0, 0, one, z, 0, 0, false);
    }
    public void testUnificationUsageFail5() {  // SUCCESS: no uniqueness
        testUnification("src/test/resources/validate/unification/usage-fail5.provn",
                0, 0, z, z, 0, 0, false);
    }
    public void testUnificationUsageFail6() { // SUCCESS: no uniqueness
        testUnification("src/test/resources/validate/unification/usage-fail6.provn",
                0, 0, z, z, 0, 0, false);
    }
    public void testUnificationUsageFail7() {
        testUnification("src/test/resources/validate/unification/usage-fail7.provn",
                0, 0, z, one, 0, 0, false);
    }

    //  -----------------  association

    public void testUnificationAssociationSuccess1() {
        testUnification("src/test/resources/validate/unification/association-success1.provn",
                0, 0, z, one, 0, 0, false);
    }
    public void testUnificationAssociationSuccess2() {
        testUnification("src/test/resources/validate/unification/association-success2.provn",
                0, 0, z, z, 0, 0, false);
    }
    public void testUnificationAssociationSuccess3() {
        testUnification("src/test/resources/validate/unification/association-success3.provn",
                0, 0, z, one, 0, 0, false);
    }
    public void testUnificationAssociationSuccess4() {
        testUnification("src/test/resources/validate/unification/association-success4.provn",
                0, 0, z, one, 0, 0, false);
    }
    public void testUnificationAssociationSuccess5() {
        testUnification("src/test/resources/validate/unification/association-success5.provn",
                0, 0, z, z, 0, 0, false);
    }
    public void testUnificationAssociationFail1() {
        testUnification("src/test/resources/validate/unification/association-fail1.provn",
                0, 0, one, z, 0, 0, false);
    }
    public void testUnificationAssociationFail2() {
        testUnification("src/test/resources/validate/unification/association-fail2.provn",
                0, 0, one, z, 0, 0, false);
    }
    public void testUnificationAssociationFail3() {
        testUnification("src/test/resources/validate/unification/association-fail3.provn",
                0, 0, one, z, 0, 0, false);
    }
    public void testUnificationAssociationFail4() {
        testUnification("src/test/resources/validate/unification/association-fail4.provn",
                0, 0, one, z, 0, 0, false);
    }
    public void testUnificationAssociationFail5() {
        testUnification("src/test/resources/validate/unification/association-fail5.provn",
                0, 0, one, z, 0, 0, false);
    }
    public void NOtestUnificationAssociationFail6() {
        testUnification("src/test/resources/validate/unification/association-fail6.provn",
                0, 0, z, z, 0, 1, false);
    }

//  -----------------  communication

    public void testUnificationCommunicationSuccess1() {
        testUnification("src/test/resources/validate/unification/communication-success1.provn",
                0, 0, z, one, 0, 0, false);
    }
    public void testUnificationCommunicationSuccess2() {
        testUnification("src/test/resources/validate/unification/communication-success2.provn",
                0, 0, z, z, 0, 0, false);
    }
    public void testUnificationCommunicationSuccess3() {
        testUnification("src/test/resources/validate/unification/communication-success3.provn",
                0, 0, z, z, 0, 0, false);
    }
    public void testUnificationCommunicationSuccess4() {
        testUnification("src/test/resources/validate/unification/communication-success4.provn",
                0, 0, z, z, 0, 0, false);
    }

    public void NOtestUnificationCommunicationFail1() {
        testUnification("src/test/resources/validate/unification/communication-fail1.provn",
                0, 0, z, z, 0, 1, false);
    }

    public void NOtestUnificationCommunicationFail2() {
        testUnification("src/test/resources/validate/unification/communication-fail2.provn",
                0, 0, z, z, 0, 1, false);
    }


    //-----------------  attribution

    public void testUnificationAttributionSuccess1() {
        testUnification("src/test/resources/validate/unification/attribution-success1.provn",
                0, 0, z, one, 0, 0, false);
    }
    public void testUnificationAttributionSuccess2() {
        testUnification("src/test/resources/validate/unification/attribution-success2.provn",
                0, 0, z, z, 0, 0, false);
    }
    public void testUnificationAttributionSuccess3() {
        testUnification("src/test/resources/validate/unification/attribution-success3.provn",
                0, 0, z, z, 0, 0, false);
    }
    public void testUnificationAttributionSuccess4() {
        testUnification("src/test/resources/validate/unification/attribution-success4.provn",
                0, 0, z, z, 0, 0, false);
    }

    public void NOtestUnificationAttributionFail1() {
        testUnification("src/test/resources/validate/unification/attribution-fail1.provn",
                0, 0, z, z, 0, 1, false);
    }

    public void NOtestUnificationAttributionFail2() {
        testUnification("src/test/resources/validate/unification/attribution-fail2.provn",
                0, 0, z, z, 0, 1, false);
    }


    //  -----------------  delegation

    public void testUnificationDelegationSuccess1() {
        testUnification("src/test/resources/validate/unification/delegation-success1.provn",
                0, 0, z, one, 0, 0, false);
    }
    public void testUnificationDelegationSuccess2() {
        testUnification("src/test/resources/validate/unification/delegation-success2.provn",
                0, 0, z, z, 0, 0, false);
    }
    public void NOtestUnificationDelegationSuccess3() {
        testUnification("src/test/resources/validate/unification/delegation-success3.provn",
                0, 0, z, one, 0, 0, false);
    }
    public void NOtestUnificationDelegationSuccess4() {
        testUnification("src/test/resources/validate/unification/delegation-success4.provn",
                0, 0, z, one, 0, 0, false);
    }
    public void testUnificationDelegationSuccess5() {
        testUnification("src/test/resources/validate/unification/delegation-success5.provn",
                0, 0, z, z, 0, 0, false);
    }
    public void testUnificationDelegationFail1() {
        testUnification("src/test/resources/validate/unification/delegation-fail1.provn",
                0, 0, one, z, 0, 0, false);
    }
    public void testUnificationDelegationFail2() {
        testUnification("src/test/resources/validate/unification/delegation-fail2.provn",
                0, 0, one, z, 0, 0, false);
    }
    public void testUnificationDelegationFail3() {
        testUnification("src/test/resources/validate/unification/delegation-fail3.provn",
                0, 0, one, z, 0, 0, false);
    }
    public void testUnificationDelegationFail4() {  //SUCCESS
        testUnification("src/test/resources/validate/unification/delegation-fail4.provn",
                0, 0, z, one, 0, 0, false);
    }
    public void NOtestUnificationDelegationFail5() { // SUCCESS
        testUnification("src/test/resources/validate/unification/delegation-fail5.provn",
                0, 0, z, one, 0, 0, false);
    }
    public void NOtestUnificationDelegationFail6() {
        testUnification("src/test/resources/validate/unification/delegation-fail6.provn",
                0, 0, z, z, 0, 1, false);
    }


//  -----------------  influence

    public void testUnificationInfluenceSuccess1() {
        testUnification("src/test/resources/validate/unification/influence-success1.provn",
                0, 0, z, one, 0, 0, false);
    }
    public void testUnificationInfluenceSuccess2() {
        testUnification("src/test/resources/validate/unification/influence-success2.provn",
                0, 0, z, z, 0, 0, false);
    }
    public void testUnificationInfluenceSuccess3() {
        testUnification("src/test/resources/validate/unification/influence-success3.provn",
                0, 0, z, z, 0, 0, false);
    }
    public void testUnificationInfluenceSuccess4() {
        testUnification("src/test/resources/validate/unification/influence-success4.provn",
                0, 0, z, z, 0, 0, false);
    }

    public void NOtestUnificationInfluenceFail1() {
        testUnification("src/test/resources/validate/unification/influence-fail1.provn",
                0, 0, z, z, 0, 1, false);
    }

    public void NOtestUnificationInfluenceFail2() {
        testUnification("src/test/resources/validate/unification/influence-fail2.provn",
                0, 0, z, z, 0, 1, false);
    }



    /*
     *
     * public void testValidateGraph1() throws
     * java.io.IOException { testVadate("src/test/resources/validate/graph1.provn");
     * assertTrue(report.getCycle().size() == 2); }
     *
     * public void testValidateGraph3() throws
     *
     * java.io.IOException { testValidate("src/test/resources/validate/graph3.provn");
     * assertTrue(report.getCycle().isEmpty()); }
     *
     * public void testValidateGraph4() throws
     * java.io.IOException { testValidate("src/test/resources/validate/graph4.provn");
     * assertTrue(report.getCycle().isEmpty()); }
     *
     * public void testValidateGraph5() throws
     * java.io.IOException { testValidate("src/test/resources/validate/graph5.provn");
     * assertTrue(report.getCycle().isEmpty()); }
     *
     * public void testValidateGraph6() throws
     * java.io.IOException { testValidate("src/test/resources/validate/graph6.provn");
     * assertTrue(report.getCycle().isEmpty()); }
     *
     * public void testValidateGraph7() throws
     * java.io.IOException { testValidate("src/test/resources/validate/graph7.provn");
     * assertTrue(report.getCycle().isEmpty()); }
     *
     * public void testValidateGraph8() throws
     * java.io.IOException { testValidate("src/test/resources/validate/graph8.provn");
     * assertTrue(report.getCycle().isEmpty()); }
     *
     * public void testValidateGraph9() throws
     * java.io.IOException { testValidate("src/test/resources/validate/graph9.provn");
     * assertTrue(report.getCycle().isEmpty()); }
     *
     * public void testValidateGraph10() throws
     * java.io.IOException { testValidate("src/test/resources/validate/graph10.provn");
     * assertTrue(report.getCycle().isEmpty()); }
     *
     * public void testValidateGraph11() throws
     * java.io.IOException { testValidate("src/test/resources/validate/graph11.provn");
     * assertTrue(report.getCycle().isEmpty()); }
     *
     * public void testValidateGraph12() throws
     * java.io.IOException { testValidate("src/test/resources/validate/graph12.provn");
     * assertTrue(report.getCycle().isEmpty()); }
     *
     * public void testValidateGraph13() throws
     * java.io.IOException { testValidate("src/test/resources/validate/graph13.provn");
     * assertTrue(report.getCycle().isEmpty()); }
     *
     * public void testValidateGraph14() throws
     * java.io.IOException { testValidate("src/test/resources/validate/graph14.provn");
     * assertTrue(report.getCycle().isEmpty()); }
     *
     * public void testValidateGraph15() throws
     * java.io.IOException { testValidate("src/test/resources/validate/graph15.provn");
     * assertTrue(report.getCycle().isEmpty()); }
     *
     * public void testValidateGraph16() throws
     * java.io.IOException { testValidate("src/test/resources/validate/graph16.provn");
     * assertTrue(report.getCycle().isEmpty()); }
     *
     * public void testValidateGraph17() throws
     * java.io.IOException { testValidate("src/test/resources/validate/graph17.provn");
     * assertTrue(report.getCycle().size() == 0); //TODO: used to be 2 }
     *
     * public void testValidateGraph18() throws
     * java.io.IOException { testValidate("src/test/resources/validate/graph18.provn");
     * assertTrue(report.getCycle().size() == 0); }
     *
     * public void testValidateGraph19() throws
     * java.io.IOException { testValidate("src/test/resources/validate/graph19.provn");
     * assertTrue(report.getCycle().size() == 0); }
     *
     * public void testValidateGraph20() throws
     * java.io.IOException { testValidate("src/test/resources/validate/graph20.provn");
     * assertTrue(report.getCycle().size() == 1); }
     *
     * public void testValidateGraph21() throws
     * java.io.IOException { testValidate("src/test/resources/validate/graph21.provn");
     * assertTrue(report.getCycle().size() == 1); }
     *
     * // tests for used public void testValidateGraph22() throws
     * java.io.IOException {
     * testValidate("src/test/resources/validate/graph22.provn");
     * assertTrue(report.getCycle().size() == 1); }
     *
     * public void testValidateGraph23() throws
     * java.io.IOException { testValidate("src/test/resources/validate/graph23.provn");
     * assertTrue(report.getFailedMerge().size() == 1);
     * assertTrue(report.getFailedMerge().get(0).getAny().size() == 2); }
     *
     * public void testValidateGraph24() throws
     * java.io.IOException { testValidate("src/test/resources/validate/graph24.provn");
     * assertTrue(report.getFailedMerge().size() == 1);
     * assertTrue(report.getFailedMerge().get(0).getAny().size() == 2); }
     *
     * public void testValidateGraph25() throws
     * java.io.IOException { testValidate("src/test/resources/validate/graph25.provn");
     * assertTrue(report.getFailedMerge().size() == 1);
     * assertTrue(report.getFailedMerge().get(0).getAny().size() == 2); }
     *
     * public void testValidateGraph26() throws
     * java.io.IOException { testValidate("src/test/resources/validate/graph26.provn");
     * assertTrue(report.getFailedMerge().size() == 0);
     * assertTrue(report.getQualifiedNameMismatch().size() == 2);
     * assertTrue(report.getQualifiedNameMismatch().get(0).getAny().size() ==
     * 2); assertTrue(report.getQualifiedNameMismatch().get(1).getAny().size()
     * == 2); }
     *
     */

    public void testValidatePC1() {
        testUnification("src/test/resources/validate/pc1-full.provn",
                0, 0, z, z, 0, 0, false);
    }

    public void testValidatePrimer() throws java.io.IOException {
        doTestValidate("src/test/resources/validate/primer.provn");
        assertTrue(report.getCycle().size()==0);
        assertTrue(report.getNonStrictCycle().size()==2); // two simultaneous generations
        assertTrue(report.getFailedMerge().size() == 0);
    }


    public void testValidateJPL() throws java.io.IOException {
        String jplFile="../../ProvToolbox/toolbox/target/jpl.provn";
        if (new File(jplFile).exists()) {
            doTestValidate(jplFile);
            assertTrue(report.getCycle().size()==0);
            assertTrue(report.getNonStrictCycle().size()==0);
            assertTrue(report.getFailedMerge().size() == 0);
        }
    }

    public void testValidateInvalid() {
        String invalidFile="../../ProvToolbox/prov-org.openprovenance.prov.xml/src/test/resources/validate/invalid.provn";
        if (new File(invalidFile).exists()) {
            testUnification(invalidFile,
                    5, 0, one, two, 1, 1, 1, false); // note added a type counter
        }
    }


    /*
     *
     * public void testValidateMalformedEnd() throws
     * java.io.IOException { try {
     * testValidate("src/test/resources/validate/malformed-end.provn"); } catch
     * (UnsupportedOperationException e) { assertTrue(true); } }
     */

    /* 
       Dear Luc,
       Here is a link to the graph that causes ProvValidator to crash.
       https://provenance.ecs.soton.ac.uk/store/documents/9673/
       Also I have attached a copy of the error report that results.
       Thank you,
       Thomas Bytheway
    */
    public void testFailingDocumentTomBytheway() throws java.io.IOException {
        doTestValidate("src/test/resources/validate/issue/tom-bytheway.provn");
    }
    public void testFailingDocumentPicaso() throws java.io.IOException {
        doTestValidate("src/test/resources/validate/issue/picaso.provn");
    }

    public void testFailingDocumentExpansion() throws java.io.IOException {
        doTestValidate("src/test/resources/validate/issue/expansion.provn");
    }


    public void NOtestValidateAmir() throws java.io.IOException {
        doTestValidate("/home/lavm/Downloads/amir.provn");
    }

    public void NOtestValidateAmir2() throws java.io.IOException {
        System.out.println("");
        System.out.println("");
        System.out.println("");
        System.out
                .println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        System.out.println("");
        doTestValidate("/home/lavm/Downloads/amir2.provn");
    }

    /*    public void testValidatePicaso() {
        testUnification("src/test/resources/validate/picaso-file.provn",
                        0, 0, z, z, 0, 0, false);
    }
    */


}
