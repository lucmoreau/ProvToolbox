package org.openprovenance.prov.model.test;


import org.openprovenance.prov.model.*;
import org.openprovenance.prov.model.extension.QualifiedAlternateOf;
import org.openprovenance.prov.model.extension.QualifiedHadMember;
import org.openprovenance.prov.model.extension.QualifiedSpecializationOf;

import java.util.*;


/**
 * Unit test for PROV roundtrip conversion starting from Java
 */
public class RoundTripFromJavaTest extends ProvFrameworkTest {

    public static final String EX2_NS = "http://example2.org/";
    public static final String EX2_PREFIX = "ex2";
    public static final String EX3_NS = "http://example3.org/";
    public static final String NS_DEFAULT = EX_NS + "test/";

    public boolean checkTest(String name) {
        return false;
    }

    public boolean checkSchema(String name) {
        if (name.endsWith("SOME PROBLEMATIC TEST" + extension())) {
            return false;
        }

        return true;
    }
    // /////////////////////////////////////////////////////////////////////


    public org.openprovenance.prov.model.QualifiedName qInnerAsEx(String n) {
        return pFactory.newQualifiedName(EX_NS+"inner/", n, EX_PREFIX);
    }
    public org.openprovenance.prov.model.QualifiedName qOuterAsEx(String n) {
        return pFactory.newQualifiedName(EX_NS+"outer/", n, EX_PREFIX);
    }
    public org.openprovenance.prov.model.QualifiedName qInner(String n) {
        return pFactory.newQualifiedName(EX_NS+"inner/", n, INNER_PREFIX);
    }
    public org.openprovenance.prov.model.QualifiedName qOuter(String n) {
        return pFactory.newQualifiedName(EX_NS+"outer/", n, OUTER_PREFIX);
    }
    public org.openprovenance.prov.model.QualifiedName qDefault(String n) {
        return pFactory.newQualifiedName(NS_DEFAULT, n, null);
    }


    // /////////////////////////////////////////////////////////////////////


    public void testRoles() {
        Role r1 = pFactory.newRole("otherRole", name.XSD_STRING);
        Role r2 = pFactory.newRole("otherRole", name.XSD_STRING);
        Location l1 = pFactory.newLocation("otherLocation", name.XSD_STRING);
        Location l2 = pFactory.newLocation("otherLocation", name.XSD_STRING);
        assertEquals(r1, r2);
        assertEquals(l1, l2);
    }

    public boolean test = true;


    public void testEntity0() {
        org.openprovenance.prov.model.Entity a = pFactory.newEntity(q("e0"));
        a.getOther()
         .add(pFactory.newOther(pFactory.newQualifiedName(EX_NS, "tag2", EX_PREFIX),
                                pFactory.newInternationalizedString("bonjour","fr"),
                                name.PROV_LANG_STRING));

        if (test) {

            a.getLocation().add(pFactory.newLocation("un llieu", name.XSD_STRING));
            a.getLocation().add(pFactory.newLocation(1, name.XSD_INT));
            a.getLocation().add(pFactory.newLocation(2.0, name.XSD_DOUBLE));


            // w.setValue(URI.create(EX_NS+"london"));
            a.getLocation().add(pFactory.newLocation(EX_NS + "london", name.XSD_ANY_URI));

            Location loc = pFactory.newLocation(2L,   name.XSD_LONG);
            // FIXME: Location containing a QName does not work
            // loc.getAttributes().put(name.QNAME_XSD_LONG,"1");
            a.getLocation().add(loc);

            // This fails because we don't get to read the type in xsi:type
            // a.getLocation().add(pFactory.newLocation(2,name.QNAME_XSD_UNSIGNED_INT));
            // problem in prov-n parsing, since TreeTraversal.convertTypeLiteral
            // generate a java value without type!
        }
        makeDocAndTest(a, "target/entity0");
    }

    public void testEntity1() {
        Entity a = pFactory.newEntity(q("e1"));
        makeDocAndTest(a, "target/entity1");
    }

    public void testEntity2() {
        Entity a = pFactory.newEntity(q("e2"), "entity2");
        makeDocAndTest(a, "target/entity2");
    }

    public void testEntity3() {
        Entity a = pFactory.newEntity(q("e3"), "entity3");
        addValue(a);
        makeDocAndTest(a, "target/entity3");
    }

    public void testEntity4() {
        Entity a = pFactory.newEntity(q("e4"), "entity4");
        addLabels(a);
        makeDocAndTest(a, "target/entity4");
    }

    public void testEntity5() {
        Entity a = pFactory.newEntity(q("e5"), "entity5");
        addTypes(a);
        makeDocAndTest(a, "target/entity5");
    }

    public void testEntity6() {
        Entity a = pFactory.newEntity(q("e6"), "entity6");
        addLocations(a);
        makeDocAndTest(a, "target/entity6");
    }

    public void testEntity7() {
        Entity a = pFactory.newEntity(q("e7"), "entity7");
        addTypes(a);
        addLocations(a);
        addLabels(a);
        makeDocAndTest(a, "target/entity7");
    }

    public void testEntity8() {
        Entity a = pFactory.newEntity(q("e8"), "entity8");
        addTypes(a);
        addTypes(a);
        addLocations(a);
        addLocations(a);
        addLabels(a);
        addLabels(a);
        makeDocAndTest(a, "target/entity8");
    }

    public void testEntity9() {
        Entity a = pFactory.newEntity(q("e9"), "entity9");
        addTypes(a);
        addLocations(a);
        addLabels(a);
        addFurtherAttributes(a);
        makeDocAndTest(a, "target/entity9");
    }

    public void testEntity10() {
        Entity a = pFactory.newEntity(q("e10"), "entity10");
        addTypes(a);
        addLocations(a);
        addLabels(a);
        addFurtherAttributes(a);
        makeDocAndTest(a, "target/entity10");
    }

    String problematic="?a\\=b";
    //TODO: problematic, but unicode in gneral doesnot serialize to json
    
    public void NOtestEntity100() {
	    Entity e = pFactory.newEntity(q("100-generalEntity"), "entity100");
	    	    	     e.getOther().add(pFactory.newOther(EX_NS, "a01b\\[c", EX_PREFIX,
                                           pFactory.newQualifiedName(EX2_NS, "\\=\\'\\(\\)\\,-\\:\\;\\[\\]\\.",
                                                     EX2_PREFIX),
                                           name.PROV_QUALIFIED_NAME));
        e.getOther().add(pFactory.newOther(EX_NS, "a01bc", EX_PREFIX,
                                           pFactory.newQualifiedName(EX2_NS, "\\=\\'\\(\\)\\,-\\:\\;\\[\\]\\.",
                                                     EX2_PREFIX),
                                           name.PROV_QUALIFIED_NAME));
        e.getOther().add(pFactory.newOther(EX_NS, "unicode", EX_PREFIX,
                                           pFactory.newQualifiedName(EX2_NS, "À-ÖØ-öø-", //˿Ͱͽ not valid
                                                     EX2_PREFIX),
                                           name.PROV_QUALIFIED_NAME)); 
        e.getOther().add(pFactory.newOther(EX_NS, "À-ÖØ-öø-", EX_PREFIX, //˿Ͱͽ not valid
                                           pFactory.newQualifiedName(EX2_NS,"unicode",
                                                     EX2_PREFIX),
						     name.PROV_QUALIFIED_NAME)); 
        e.getOther().add(pFactory.newOther(EX_NS, problematic, EX_PREFIX,
                                           1,
                                           name.XSD_INT));
		      e.getOther().add(pFactory.newOther(EX_NS, "123", EX_PREFIX,
                                           "mystring",
                                           name.XSD_STRING));
        e.getOther().add(pFactory.newOther(EX_NS, "123", EX_PREFIX,
                                           pFactory.newInternationalizedString("ma chaine", "fr"),
                                           name.PROV_LANG_STRING));
        makeDocAndTest(e, "target/entity100");
    }


    public void testEntity101() {
        Entity e = pFactory.newEntity(q("101-generalEntity"), "entity101");
        e.getOther().add(pFactory.newOther(EX_NS, "a01b\\[c", EX_PREFIX,
                                           pFactory.newQualifiedName(EX2_NS, "\\=\\'\\(\\)\\,-\\:\\;\\[\\]\\.", EX2_PREFIX),
                                           name.PROV_QUALIFIED_NAME));
        e.getOther().add(pFactory.newOther(EX_NS, "a01bc", EX_PREFIX,
                                           pFactory.newQualifiedName(EX2_NS, "\\=\\'\\(\\)\\,-\\:\\;\\[\\]\\.", EX2_PREFIX),
                                           name.PROV_QUALIFIED_NAME));
        e.getOther().add(pFactory.newOther(EX_NS, "unicode", EX_PREFIX,
                                           pFactory.newQualifiedName(EX2_NS, "À-ÖØ-öø-˿Ͱͽ", EX2_PREFIX), //validator disabled for this
                                           name.PROV_QUALIFIED_NAME));
        e.getOther().add(pFactory.newOther(EX_NS, "À-ÖØ-öø-", EX_PREFIX, //˿Ͱͽ not supported by jaxb marshaller
                                           pFactory.newQualifiedName(EX2_NS,"unicode", EX2_PREFIX),
                                           name.PROV_QUALIFIED_NAME));
        e.getOther().add(pFactory.newOther(EX_NS, problematic, EX_PREFIX,
                                           1,
                                           name.XSD_INT));
        e.getOther().add(pFactory.newOther(EX_NS, "123", EX_PREFIX,
                                           "mystring",
                                           name.XSD_STRING));
        e.getOther().add(pFactory.newOther(EX_NS, "123", EX_PREFIX,
                                           pFactory.newInternationalizedString("ma chaine", "fr"),
                                           name.PROV_LANG_STRING));
        makeDocAndTest(e, "target/entity101");
    }


    // /////////////////////////////////////////////////////////////////////

    public void testDocument1() {
        Activity a = pFactory.newActivity(q("a1"));
        makeDocAndTest(new Statement[0],null, "target/document1");
    }


    ////

    public void testActivity1() {
        Activity a = pFactory.newActivity(q("a1"));
        makeDocAndTest(a, "target/activity1");
    }

    public void testActivity2() {
        Activity a = pFactory.newActivity(q("a2"), "activity2");
        makeDocAndTest(a, "target/activity2");
    }

    public void testActivity3() {
        Activity a = pFactory.newActivity(q("a1"));
        a.setStartTime(pFactory.newTimeNow());
        a.setEndTime(pFactory.newTimeNow());
        makeDocAndTest(a, "target/activity3");
    }

    public void testActivity4() {
        Activity a = pFactory.newActivity(q("a2"), "activity2");
        addLabels(a);
        makeDocAndTest(a, "target/activity4");
    }

    public void testActivity5() {
        Activity a = pFactory.newActivity(q("a5"), "activity5");
        addTypes(a);
        makeDocAndTest(a, "target/activity5");
    }

    public void testActivity6() {
        Activity a = pFactory.newActivity(q("a6"), "activity6");
        addLocations(a);
        makeDocAndTest(a, "target/activity6");
    }

    public void testActivity7() {
        Activity a = pFactory.newActivity(q("a7"), "activity7");
        addTypes(a);
        addLocations(a);
        addLabels(a);
        makeDocAndTest(a, "target/activity7");
    }

    public void testActivity8() {
        Activity a = pFactory.newActivity(q("a8"), "activity8");
        a.setStartTime(pFactory.newTimeNow());
        a.setEndTime(pFactory.newTimeNow());
        addTypes(a);
        addTypes(a);
        addLocations(a);
        addLocations(a);
        addLabels(a);
        addLabels(a);
        makeDocAndTest(a, "target/activity8");
    }

    public void testActivity9() {
        Activity a = pFactory.newActivity(q("a9"), "activity9");
        addTypes(a);
        addLocations(a);
        addLabels(a);
        addFurtherAttributes(a);
        makeDocAndTest(a, "target/activity9");
    }

    // /////////////////////////////////////////////////////////////////////


    public void testAgent1() {
        Agent a = pFactory.newAgent(q("ag1"));
        makeDocAndTest(a, "target/agent1");
    }

    public void testAgent2() {
        Agent a = pFactory.newAgent(q("ag2"), "agent2");
        makeDocAndTest(a, "target/agent2");
    }

    public void testAgent3() {
        Agent a = pFactory.newAgent(q("ag2"), "agent2");
        a.getLabel().add(pFactory.newInternationalizedString("hello"));
        makeDocAndTest(a, "target/agent3");
    }

    public void testAgent4() {
        Agent a = pFactory.newAgent(q("ag2"), "agent2");
        a.getLabel().add(pFactory.newInternationalizedString("hello"));
        a.getLabel().add(pFactory.newInternationalizedString("bye", "en"));
        makeDocAndTest(a, "target/agent4");
    }

    public void testAgent5() {
        Agent a = pFactory.newAgent(q("ag2"), "agent2");
        a.getLabel().add(pFactory.newInternationalizedString("hello"));
        a.getLabel().add(pFactory.newInternationalizedString("bye", "en"));
        a.getLabel().add(pFactory.newInternationalizedString("bonjour", "fr"));
        makeDocAndTest(a, "target/agent5");
    }

    public void testAgent6() {
        Agent a = pFactory.newAgent(q("ag6"), "agent6");
        addTypes(a);
        makeDocAndTest(a, "target/agent6");
    }

    public void testAgent7() {
        Agent a = pFactory.newAgent(q("ag7"), "agent7");
        addTypes(a);
        a.getLabel().add(pFactory.newInternationalizedString("hello"));
        a.getLabel().add(pFactory.newInternationalizedString("bye", "en"));
        a.getLabel().add(pFactory.newInternationalizedString("bonjour", "fr"));
        a.getLocation().add(pFactory.newLocation("London",
                                                 name.XSD_STRING));
        a.getLocation().add(pFactory.newLocation(1, name.XSD_INT));
        a.getLocation().add(pFactory.newLocation(1.0, name.XSD_FLOAT));
        a.getLocation().add(pFactory.newLocation(true, name.XSD_BOOLEAN));
        // a.getLocation().add(pFactory.newLocation(new QName(EX_NS, "london",
        // EX_PREFIX),vconv));
        a.getLocation().add(pFactory.newLocation(pFactory.newTimeNow(),
                                                 name.XSD_DATETIME));
        a.getLocation().add(pFactory.newLocation(EX_NS+"london", name.XSD_ANY_URI));
        makeDocAndTest(a, "target/agent7");
    }

    public void testAgent8() {
        Agent a = pFactory.newAgent(q("ag8"), "agent8");

        addTypes(a);
        addTypes(a);


        a.getLocation().add(pFactory.newLocation(EX_NS + "london", name.XSD_ANY_URI));
        a.getLocation().add(pFactory.newLocation(EX_NS + "london", name.XSD_ANY_URI));

        makeDocAndTest(a, "target/agent8");
    }

    // /////////////////////////////////////////////////////////////////////




    public void testGeneration1() {
        WasGeneratedBy gen = pFactory.newWasGeneratedBy(q("gen1"), q("e1"),
                                                        null, null);
        makeDocAndTest(gen, "target/generation1");
    }

    public void testGeneration2() {
        WasGeneratedBy gen = pFactory.newWasGeneratedBy(q("gen2"), q("e1"),
                                                        null, q("a1"));

        makeDocAndTest(gen, "target/generation2");
    }

    public void testGeneration3() {
        WasGeneratedBy gen = pFactory.newWasGeneratedBy(q("gen3"), q("e1"),
                                                        "somerole", q("a1"));
        gen.getRole().add(pFactory.newRole("otherRole", name.XSD_STRING));
        makeDocAndTest(gen, "target/generation3");
    }

    public void testGeneration4() {
        WasGeneratedBy gen = pFactory.newWasGeneratedBy(q("gen4"), q("e1"),
                                                        "somerole", q("a1"));
        gen.setTime(pFactory.newTimeNow());
        makeDocAndTest(gen, "target/generation4");
    }

    public void testGeneration5() {
        WasGeneratedBy gen = pFactory.newWasGeneratedBy(q("gen4"), q("e1"),
                                                        "somerole", q("a1"));
        gen.setTime(pFactory.newTimeNow());
        addTypes(gen);
        addLocations(gen);
        addLabels(gen);
        addFurtherAttributes(gen);

        makeDocAndTest(gen, "target/generation5");
    }

    public void testGeneration6() {
        WasGeneratedBy gen = pFactory.newWasGeneratedBy((org.openprovenance.prov.vanilla.QualifiedName) null,
                                                        q("e1"), null, q("a1"));
        makeDocAndTest(gen, "target/generation6");
    }

    public void testGeneration7() {
        WasGeneratedBy gen = pFactory.newWasGeneratedBy((org.openprovenance.prov.vanilla.QualifiedName) null,
                                                        q("e1"), "somerole",
                                                        q("a1"));
        gen.setTime(pFactory.newTimeNow());
        addTypes(gen);
        addLocations(gen);
        addLabels(gen);
        addFurtherAttributes(gen);

        makeDocAndTest(gen, "target/generation7");
    }



    // ////////////////////////////////

    public void testUsage1() {
        Used use = pFactory.newUsed(q("use1"), null, null, q("e1"));
        makeDocAndTest(use, "target/usage1");
    }

    public void testUsage2() {
        Used use = pFactory.newUsed(q("use2"), q("a1"), null, q("e1"));
        makeDocAndTest(use, "target/usage2");
    }

    public void testUsage3() {
        Used use = pFactory.newUsed(q("use3"), q("a1"), "somerole", q("e1"));
        use.getRole().add(pFactory.newRole("otherRole", name.XSD_STRING));

        makeDocAndTest(use, "target/usage3");
    }

    public void testUsage4() {
        Used use = pFactory.newUsed(q("use4"), q("a1"), "somerole", q("e1"));
        use.setTime(pFactory.newTimeNow());

        makeDocAndTest(use, "target/usage4");
    }

    public void testUsage5() {
        Used use = pFactory.newUsed(q("use5"), q("a1"), "somerole", q("e1"));
        use.setTime(pFactory.newTimeNow());
        addTypes(use);
        addLocations(use);
        addLabels(use);
        addFurtherAttributes(use);

        makeDocAndTest(use, "target/usage5");
    }

    public void testUsage6() {
        Used use = pFactory.newUsed((org.openprovenance.prov.vanilla.QualifiedName) null, q("a1"), null,
                                    q("e1"));

        makeDocAndTest(use, "target/usage6");
    }

    public void testUsage7() {
        Used use = pFactory.newUsed((org.openprovenance.prov.vanilla.QualifiedName) null, q("a1"), "somerole",
                                    q("e1"));
        use.setTime(pFactory.newTimeNow());
        addTypes(use);
        addLocations(use);
        addLabels(use);
        addFurtherAttributes(use);

        makeDocAndTest(use, "target/usage7");
    }

    // //////////////////////////////////////////////



    public void testInvalidation1() {
        WasInvalidatedBy inv = pFactory.newWasInvalidatedBy(q("inv1"), q("e1"),
                                                            null);
        makeDocAndTest(inv, "target/invalidation1");
    }

    public void testInvalidation2() {
        WasInvalidatedBy inv = pFactory.newWasInvalidatedBy(q("inv2"), q("e1"),
                                                            q("a1"));
        makeDocAndTest(inv, "target/invalidation2");
    }

    public void testInvalidation3() {
        WasInvalidatedBy inv = pFactory.newWasInvalidatedBy(q("inv3"), q("e1"),
                                                            q("a1"));
        inv.getRole().add(pFactory.newRole("someRole", name.XSD_STRING));
        inv.getRole().add(pFactory.newRole("otherRole", name.XSD_STRING));
        makeDocAndTest(inv, "target/invalidation3");
    }

    public void testInvalidation4() {
        WasInvalidatedBy inv = pFactory.newWasInvalidatedBy(q("inv4"), q("e1"),
                                                            q("a1"));
        inv.getRole().add(pFactory.newRole("someRole", name.XSD_STRING));
        inv.setTime(pFactory.newTimeNow());
        makeDocAndTest(inv, "target/invalidation4");
    }

    public void testInvalidation5() {
        WasInvalidatedBy inv = pFactory.newWasInvalidatedBy(q("inv4"), q("e1"),
                                                            q("a1"));
        inv.getRole().add(pFactory.newRole("someRole", name.XSD_STRING));
        inv.setTime(pFactory.newTimeNow());
        addTypes(inv);
        addLocations(inv);

        addLabels(inv);
        addFurtherAttributes(inv);

        makeDocAndTest(inv, "target/invalidation5");
    }

    public void testInvalidation6() {
        WasInvalidatedBy inv = pFactory.newWasInvalidatedBy((org.openprovenance.prov.vanilla.QualifiedName) null,
                                                            q("e1"), q("a1"));
        makeDocAndTest(inv, "target/invalidation6");
    }

    public void testInvalidation7() {
        WasInvalidatedBy inv = pFactory.newWasInvalidatedBy((org.openprovenance.prov.vanilla.QualifiedName) null,
                                                            q("e1"), q("a1"));
        inv.getRole().add(pFactory.newRole("someRole", name.XSD_STRING));
        inv.setTime(pFactory.newTimeNow());
        addTypes(inv);
        addLocations(inv);
        addLabels(inv);
        addFurtherAttributes(inv);

        makeDocAndTest(inv, "target/invalidation7");
    }



    // ////////////////////////////////

    public void testStart1() {
        WasStartedBy start = pFactory.newWasStartedBy(q("start1"), null,
                                                      q("e1"));

        makeDocAndTest(start, "target/start1");
    }

    public void testStart2() {
        WasStartedBy start = pFactory.newWasStartedBy(q("start2"), q("a1"),
                                                      q("e1"));

        makeDocAndTest(start, "target/start2");
    }

    public void testStart3() {
        WasStartedBy start = pFactory.newWasStartedBy(q("start3"), q("a1"),
                                                      null);

        makeDocAndTest(start, "target/start3");
    }

    public void testStart4() {
        WasStartedBy start = pFactory.newWasStartedBy(q("start4"), null,
                                                      q("e1"));
        start.setStarter(q("a2"));

        makeDocAndTest(start, "target/start4");
    }

    public void testStart5() {
        WasStartedBy start = pFactory.newWasStartedBy(q("start5"), q("a1"),
                                                      q("e1"));
        start.setStarter(q("a2"));

        makeDocAndTest(start, "target/start5");
    }

    public void testStart6() {
        WasStartedBy start = pFactory.newWasStartedBy(q("start6"), q("a1"),
                                                      null);
        start.setStarter(q("a2"));

        makeDocAndTest(start, "target/start6");
    }

    public void testStart7() {
        WasStartedBy start = pFactory.newWasStartedBy(q("start7"), q("a1"),
                                                      null);
        start.setStarter(q("a2"));
        start.setTime(pFactory.newTimeNow());

        makeDocAndTest(start, "target/start7");
    }

    public void testStart8() {
        WasStartedBy start = pFactory.newWasStartedBy(q("start8"), q("a1"),
                                                      null);
        start.setStarter(q("a2"));
        start.setTime(pFactory.newTimeNow());
        start.getRole()
             .add(pFactory.newRole("someRole", name.XSD_STRING));
        start.getRole()
             .add(pFactory.newRole("otherRole", name.XSD_STRING));
        addTypes(start);
        addLocations(start);
        addLabels(start);
        addFurtherAttributes(start);

        makeDocAndTest(start, "target/start8");
    }

    public void testStart9() {
        WasStartedBy start = pFactory.newWasStartedBy((org.openprovenance.prov.vanilla.QualifiedName) null,
                                                      q("a1"), q("e1"));

        makeDocAndTest(start, "target/start9");
    }

    public void testStart10() {
        WasStartedBy start = pFactory.newWasStartedBy((org.openprovenance.prov.vanilla.QualifiedName) null,
                                                      q("a1"), null);
        start.setStarter(q("a2"));
        start.setTime(pFactory.newTimeNow());
        start.getRole()
             .add(pFactory.newRole("someRole", name.XSD_STRING));
        start.getRole()
             .add(pFactory.newRole("otherRole", name.XSD_STRING));
        addTypes(start);
        addLocations(start);
        addLabels(start);
        addFurtherAttributes(start);

        makeDocAndTest(start, "target/start10");
    }



    // ////////////////////////////////

    public void testEnd1() {
        WasEndedBy end = pFactory.newWasEndedBy(q("end1"), null, q("e1"));

        makeDocAndTest(end, "target/end1");
    }

    public void testEnd2() {
        WasEndedBy end = pFactory.newWasEndedBy(q("end2"), q("a1"), q("e1"));

        makeDocAndTest(end, "target/end2");
    }

    public void testEnd3() {
        WasEndedBy end = pFactory.newWasEndedBy(q("end3"), q("a1"), null);

        makeDocAndTest(end, "target/end3");
    }

    public void testEnd4() {
        WasEndedBy end = pFactory.newWasEndedBy(q("end4"), null, q("e1"));
        end.setEnder(q("a2"));

        makeDocAndTest(end, "target/end4");
    }

    public void testEnd5() {
        WasEndedBy end = pFactory.newWasEndedBy(q("end5"), q("a1"), q("e1"));
        end.setEnder(q("a2"));

        makeDocAndTest(end, "target/end5");
    }

    public void testEnd6() {
        WasEndedBy end = pFactory.newWasEndedBy(q("end6"), q("a1"), null);
        end.setEnder(q("a2"));

        makeDocAndTest(end, "target/end6");
    }

    public void testEnd7() {
        WasEndedBy end = pFactory.newWasEndedBy(q("end7"), q("a1"), null);
        end.setEnder(q("a2"));
        end.setTime(pFactory.newTimeNow());

        makeDocAndTest(end, "target/end7");
    }

    public void testEnd8() {
        WasEndedBy end = pFactory.newWasEndedBy(q("end8"), q("a1"), null);
        end.setEnder(q("a2"));
        end.setTime(pFactory.newTimeNow());
        end.getRole().add(pFactory.newRole("someRole", name.XSD_STRING));
        end.getRole().add(pFactory.newRole("otherRole", name.XSD_STRING));
        addTypes(end);
        addLocations(end);
        addLabels(end);
        addFurtherAttributes(end);

        makeDocAndTest(end, "target/end8");
    }

    public void testEnd9() {
        WasEndedBy end = pFactory.newWasEndedBy((org.openprovenance.prov.vanilla.QualifiedName) null, q("a1"),
                                                q("e1"));

        makeDocAndTest(end, "target/end9");
    }

    public void testEnd10() {
        WasEndedBy end = pFactory.newWasEndedBy((org.openprovenance.prov.vanilla.QualifiedName) null, q("a1"),
                                                null);
        end.setEnder(q("a2"));
        end.setTime(pFactory.newTimeNow());
        end.getRole().add(pFactory.newRole("someRole", name.XSD_STRING));
        end.getRole().add(pFactory.newRole("otherRole", name.XSD_STRING));
        addTypes(end);
        addLocations(end);
        addLabels(end);
        addFurtherAttributes(end);

        makeDocAndTest(end, "target/end10");
    }


    // ////////////////////////////////

    public void testDerivation1() {
        WasDerivedFrom der = pFactory.newWasDerivedFrom(q("der1"), null,
                                                        q("e1"));
        makeDocAndTest(der, "target/derivation1");
    }

    public void testDerivation2() {
        WasDerivedFrom der = pFactory.newWasDerivedFrom(q("der2"), q("e2"),
                                                        null);
        makeDocAndTest(der, "target/derivation2");
    }

    public void testDerivation3() {
        WasDerivedFrom der = pFactory.newWasDerivedFrom(q("der3"), q("e2"),
                                                        q("e1"));
        makeDocAndTest(der, "target/derivation3");
    }

    public void testDerivation4() {
        WasDerivedFrom der = pFactory.newWasDerivedFrom(q("der4"), q("e2"),
                                                        q("e1"));
        addLabel(der);
        makeDocAndTest(der, "target/derivation4");
    }

    public void testDerivation5() {
        WasDerivedFrom der = pFactory.newWasDerivedFrom(q("der5"), q("e2"),
                                                        q("e1"));
        der.setActivity(q("a"));
        makeDocAndTest(der, "target/derivation5");
    }

    public void testDerivation6() {
        WasDerivedFrom der = pFactory.newWasDerivedFrom(q("der6"), q("e2"),
                                                        q("e1"));
        der.setActivity(q("a"));
        der.setUsage(q("u"));
        makeDocAndTest(der, "target/derivation6");
    }

    public void testDerivation7() {
        WasDerivedFrom der = pFactory.newWasDerivedFrom(q("der7"), q("e2"),
                                                        q("e1"));
        der.setActivity(q("a"));
        der.setUsage(q("u"));
        der.setGeneration(q("g"));

        makeDocAndTest(der, "target/derivation7");
    }

    public void testDerivation8() {
        WasDerivedFrom der = pFactory.newWasDerivedFrom(q("der8"), q("e2"),
                                                        q("e1"));
        addLabel(der);
        addTypes(der);
        addFurtherAttributes(der);

        makeDocAndTest(der, "target/derivation8");
    }

    public void testDerivation9() {
        WasDerivedFrom der = pFactory.newWasDerivedFrom((org.openprovenance.prov.vanilla.QualifiedName) null,
                                                        q("e2"), null);
        addTypes(der);
        makeDocAndTest(der, "target/derivation9");
    }

    public void testDerivation10() {
        WasDerivedFrom der = pFactory.newWasDerivedFrom((org.openprovenance.prov.vanilla.QualifiedName) null,
                                                        q("e2"), q("e1"));
        der.setActivity(q("a"));
        der.setUsage(q("u"));
        der.setGeneration(q("g"));
        makeDocAndTest(der, "target/derivation10");
    }

    public void testDerivation11() {
        WasDerivedFrom der = pFactory.newWasDerivedFrom(q("rev1"), q("e2"),
                                                        q("e1"));
        der.setActivity(q("a"));
        der.setUsage(q("u"));
        der.setGeneration(q("g"));
        pFactory.addRevisionType(der);
        makeDocAndTest(der, "target/derivation11");
    }

    public void testDerivation12() {
        WasDerivedFrom der = pFactory.newWasDerivedFrom(q("quo1"), q("e2"),
                                                        q("e1"));
        der.setActivity(q("a"));
        der.setUsage(q("u"));
        der.setGeneration(q("g"));
        pFactory.addQuotationType(der);
        makeDocAndTest(der, "target/derivation12");
    }

    public void testDerivation13() {
        WasDerivedFrom der = pFactory.newWasDerivedFrom(q("prim1"), q("e2"),
                                                        q("e1"));
        der.setActivity(q("a"));
        der.setUsage(q("u"));
        der.setGeneration(q("g"));
        pFactory.addPrimarySourceType(der);
        makeDocAndTest(der, "target/derivation13");
    }


    // ////////////////////////////////

    public void testAssociation1() {
        WasAssociatedWith assoc = pFactory.newWasAssociatedWith(q("assoc1"),
                                                                q("a1"), null);
        makeDocAndTest(assoc, "target/association1");
    }

    public void testAssociation2() {
        WasAssociatedWith assoc = pFactory.newWasAssociatedWith(q("assoc2"),
                                                                null, q("ag1"));
        makeDocAndTest(assoc, "target/association2");
    }

    public void testAssociation3() {
        WasAssociatedWith assoc = pFactory.newWasAssociatedWith(q("assoc3"),
                                                                q("a1"),
                                                                q("ag1"));
        makeDocAndTest(assoc, "target/association3");
    }

    public void testAssociation4() {
        WasAssociatedWith assoc = pFactory.newWasAssociatedWith(q("assoc4"),
                                                                q("a1"),
                                                                q("ag1"));
        assoc.setPlan(q("plan1"));
        makeDocAndTest(assoc, "target/association4");
    }

    public void testAssociation5() {
        WasAssociatedWith assoc = pFactory.newWasAssociatedWith((org.openprovenance.prov.vanilla.QualifiedName) null,
                                                                q("a1"),
                                                                q("ag1"));
        makeDocAndTest(assoc, "target/association5");
    }

    public void testAssociation6() {
        WasAssociatedWith assoc = pFactory.newWasAssociatedWith(q("assoc6"),
                                                                q("a1"),
                                                                q("ag1"));
        assoc.setPlan(q("plan1"));
        addLabels(assoc);
        makeDocAndTest(assoc, "target/association6");
    }

    public void testAssociation7() {
        WasAssociatedWith assoc = pFactory.newWasAssociatedWith(q("assoc7"),
                                                                q("a1"),
                                                                q("ag1"));
        assoc.setPlan(q("plan1"));
        addLabels(assoc);
        addTypes(assoc);
        makeDocAndTest(assoc, "target/association7");
    }

    public void testAssociation8() {
        WasAssociatedWith assoc = pFactory.newWasAssociatedWith(q("assoc8"),
                                                                q("a1"),
                                                                q("ag1"));
        assoc.setPlan(q("plan1"));
        assoc.getRole()
             .add(pFactory.newRole("someRole", name.XSD_STRING));
        assoc.getRole().add(pFactory.newRole("someOtherRole",
                                             name.XSD_STRING));
        makeDocAndTest(assoc, "target/association8");
    }

    public void testAssociation9() {

        WasAssociatedWith assoc = pFactory.newWasAssociatedWith(q("assoc9"),
                                                                q("a1"),
                                                                q("ag1"));
        assoc.setPlan(q("plan1"));
        addLabels(assoc);
        addTypes(assoc);
        addFurtherAttributes(assoc);
        makeDocAndTest(assoc, "target/association9");


    }


    // ////////////////////////////////

    public void testAttribution1() {
        WasAttributedTo attr = pFactory.newWasAttributedTo(q("attr1"), q("e1"),
                                                           null);
        makeDocAndTest(attr, "target/attribution1");
    }

    public void testAttribution2() {
        WasAttributedTo attr = pFactory.newWasAttributedTo(q("attr2"), null,
                                                           q("ag1"));
        makeDocAndTest(attr, "target/attribution2");
    }

    public void testAttribution3() {
        WasAttributedTo attr = pFactory.newWasAttributedTo(q("attr3"), q("e1"),
                                                           q("ag1"));
        makeDocAndTest(attr, "target/attribution3");
    }

    public void testAttribution4() {
        WasAttributedTo attr = pFactory.newWasAttributedTo(q("attr4"), q("e1"),
                                                           q("ag1"));
        makeDocAndTest(attr, "target/attribution4");
    }

    public void testAttribution5() {
        WasAttributedTo attr = pFactory.newWasAttributedTo((org.openprovenance.prov.vanilla.QualifiedName) null,
                                                           q("e1"), q("ag1"));
        makeDocAndTest(attr, "target/attribution5");
    }

    public void testAttribution6() {
        WasAttributedTo attr = pFactory.newWasAttributedTo(q("attr6"), q("e1"),
                                                           q("ag1"));
        addLabels(attr);
        makeDocAndTest(attr, "target/attribution6");
    }

    public void testAttribution7() {
        WasAttributedTo attr = pFactory.newWasAttributedTo(q("attr7"), q("e1"),
                                                           q("ag1"));
        addLabels(attr);
        addTypes(attr);
        makeDocAndTest(attr, "target/attribution7");
    }

    public void testAttribution8() {
        WasAttributedTo attr = pFactory.newWasAttributedTo(q("attr8"), q("e1"),
                                                           q("ag1"));
        addLabels(attr);
        addTypes(attr);
        addFurtherAttributes(attr);
        makeDocAndTest(attr, "target/attribution8");
    }



    // ////////////////////////////////

    public void testDelegation1() {
        ActedOnBehalfOf del = pFactory.newActedOnBehalfOf(q("del1"), q("e1"),
                                                          null, null);
        makeDocAndTest(del, "target/delegation1");
    }

    public void testDelegation2() {
        ActedOnBehalfOf del = pFactory.newActedOnBehalfOf(q("del2"), null,
                                                          q("ag1"), null);
        makeDocAndTest(del, "target/delegation2");
    }

    public void testDelegation3() {
        ActedOnBehalfOf del = pFactory.newActedOnBehalfOf(q("del3"), q("e1"),
                                                          q("ag1"), null);
        makeDocAndTest(del, "target/delegation3");
    }

    public void testDelegation4() {
        ActedOnBehalfOf del = pFactory.newActedOnBehalfOf(q("del4"), q("e1"),
                                                          q("ag1"), q("a"));

        makeDocAndTest(del, "target/delegation4");
    }

    public void testDelegation5() {
        ActedOnBehalfOf del = pFactory.newActedOnBehalfOf((QualifiedName) null,
                                                          q("e1"), q("ag1"),
                                                          null);
        makeDocAndTest(del, "target/delegation5");
    }

    public void testDelegation6() {
        ActedOnBehalfOf del = pFactory.newActedOnBehalfOf(q("del6"), q("e1"),
                                                          q("ag1"), q("a"));
        addLabels(del);
        makeDocAndTest(del, "target/delegation6");
    }

    public void testDelegation7() {
        ActedOnBehalfOf del = pFactory.newActedOnBehalfOf(q("del7"), q("e1"),
                                                          q("ag1"), q("a"));
        addLabels(del);
        addTypes(del);
        makeDocAndTest(del, "target/delegation7");
    }

    public void testDelegation8() {
        ActedOnBehalfOf del = pFactory.newActedOnBehalfOf(q("del8"), q("e1"),
                                                          q("ag1"), q("a"));
        addLabels(del);
        addTypes(del);
        addFurtherAttributes(del);
        makeDocAndTest(del, "target/delegation8");
    }

    // ////////////////////////////////

    public void testCommunication1() {
        WasInformedBy inf = pFactory.newWasInformedBy(q("inf1"), q("a2"), null);
        makeDocAndTest(inf, "target/communication1");
    }

    public void testCommunication2() {
        WasInformedBy inf = pFactory.newWasInformedBy(q("inf2"), null, q("a1"));
        makeDocAndTest(inf, "target/communication2");
    }

    public void testCommunication3() {
        WasInformedBy inf = pFactory.newWasInformedBy(q("inf3"), q("a2"),
                                                      q("a1"));
        makeDocAndTest(inf, "target/communication3");
    }

    public void testCommunication4() {
        WasInformedBy inf = pFactory.newWasInformedBy((org.openprovenance.prov.vanilla.QualifiedName) null,
                                                      q("a2"), q("a1"));
        makeDocAndTest(inf, "target/communication4");
    }

    public void testCommunication5() {
        WasInformedBy inf = pFactory.newWasInformedBy(q("inf5"), q("a2"),
                                                      q("a1"));
        addLabels(inf);
        makeDocAndTest(inf, "target/communication5");
    }

    public void testCommunication6() {
        WasInformedBy inf = pFactory.newWasInformedBy(q("inf6"), q("a2"),
                                                      q("a1"));
        addLabels(inf);
        addTypes(inf);
        makeDocAndTest(inf, "target/communication6");
    }

    public void testCommunication7() {
        WasInformedBy inf = pFactory.newWasInformedBy(q("inf7"), q("a2"),
                                                      q("a1"));
        addLabels(inf);
        addTypes(inf);
        addFurtherAttributes(inf);
        makeDocAndTest(inf, "target/communication7");
    }

    // ////////////////////////////////

    public void testInfluence1() {
        WasInfluencedBy inf = pFactory.newWasInfluencedBy(q("inf1"), q("a2"),
                                                          null);
        makeDocAndTest(inf, "target/influence1");
    }

    public void testInfluence2() {
        WasInfluencedBy inf = pFactory.newWasInfluencedBy(q("inf2"), null,
                                                          q("a1"));
        makeDocAndTest(inf, "target/influence2");
    }

    public void testInfluence3() {
        WasInfluencedBy inf = pFactory.newWasInfluencedBy(q("inf3"), q("a2"),
                                                          q("a1"));
        makeDocAndTest(inf, "target/influence3");
    }

    public void testInfluence4() {
        WasInfluencedBy inf = pFactory.newWasInfluencedBy((org.openprovenance.prov.vanilla.QualifiedName) null,
                                                          q("a2"), q("a1"));
        makeDocAndTest(inf, "target/influence4");
    }

    public void testInfluence5() {
        WasInfluencedBy inf = pFactory.newWasInfluencedBy(q("inf5"), q("a2"),
                                                          q("a1"));
        addLabels(inf);
        makeDocAndTest(inf, "target/influence5");
    }

    public void testInfluence6() {
        WasInfluencedBy inf = pFactory.newWasInfluencedBy(q("inf6"), q("a2"),
                                                          q("a1"));
        addLabels(inf);
        addTypes(inf);
        makeDocAndTest(inf, "target/influence6");
    }

    public void testInfluence7() {
        WasInfluencedBy inf = pFactory.newWasInfluencedBy(q("inf7"), q("a2"),
                                                          q("a1"));
        addLabels(inf);
        addTypes(inf);
        addFurtherAttributes(inf);
        makeDocAndTest(inf, "target/influence7");
    }

    // ////////////////////////////////



    public void testAlternate1() {
        AlternateOf alt = pFactory.newAlternateOf(q("e1"), q("e2"));
        makeDocAndTest(alt, "target/alternate1");
    }

    public void testSpecialization1() {
        SpecializationOf spe = pFactory.newSpecializationOf(q("e2"), q("e1"));
        makeDocAndTest(spe, "target/specialization1");
    }

    /*

    public void testMention1() {
        MentionOf men = pFactory.newMentionOf(q("e2"), q("e1"), null);
        makeDocAndTest(men, "target/mention1");
    }

    public void testMention2() {
        MentionOf men = pFactory.newMentionOf(q("e2"), q("e1"), q("b"));
        makeDocAndTest(men, "target/mention2");
    }

     */


    public void testMembership1() {
        HadMember mem = pFactory.newHadMember(q("c"), q("e1"));
        makeDocAndTest(mem, "target/member1");
    }

    public void testMembership2() {
        HadMember mem = pFactory.newHadMember(q("c"), q("e1"), q("e2"));
        // TODO: multiple arguments not supported by toolbox
        makeDocAndTest(mem, "target/member2");
    }

    public void testMembership3() {
        HadMember mem = pFactory.newHadMember(q("c"), q("e1"), q("e2"), q("e3"));
        // TODO: multiple arguments not supported by toolbox
        makeDocAndTest(mem, "target/member3");
    }

    /*
    public void testScruffyGeneration1() {
        WasGeneratedBy gen1 = pFactory.newWasGeneratedBy(q("gen1"),
                                                         q("e1"),
                                                         null,
                                                         q("a1"));
        gen1.setTime(pFactory.newTimeNow());
        WasGeneratedBy gen2 = pFactory.newWasGeneratedBy(q("gen1"),
                                                         q("e1"),
                                                         null,
                                                         q("a1"));
        gen2.setTime(pFactory.newISOTime("2012-12-03T21:08:16.686Z"));
        Entity e1 = pFactory.newEntity(q("e1"));
        Activity a1 = pFactory.newActivity(q("a1"));
        Statement[] opt = new Statement[] { e1, a1 };
        Statement[] statements = new Statement[] { gen1, gen2};
        makeDocAndTest(statements, opt, "target/scruffy-generation1");
    }

    public void testScruffyGeneration2() {
        WasGeneratedBy gen1 = pFactory.newWasGeneratedBy(q("gen1"),
                                                         q("e1"),
                                                         null,
                                                         q("a1"));
        gen1.setTime(pFactory.newTimeNow());


        gen1.getOther().add(pFactory.newOther(EX_NS, "tag2", EX_PREFIX,
                                              "hello-scruff-gen2", name.XSD_STRING));


        WasGeneratedBy gen2 = pFactory.newWasGeneratedBy(q("gen1"),
                                                         q("e1"),
                                                         null,
                                                         q("a1"));
        gen2.setTime(pFactory.newISOTime("2012-12-03T21:08:16.686Z"));



        gen2.getOther().add(pFactory.newOther(EX_NS, "tag2", EX_PREFIX, "hi-scruff-gen2",
                                              name.XSD_STRING));


        Entity e1 = pFactory.newEntity(q("e1"));
        Activity a1 = pFactory.newActivity(q("a1"));
        Statement[] opt = new Statement[] { e1, a1 };
        Statement[] statements = new Statement[] { gen1, gen2 };
        makeDocAndTest(statements, opt, "target/scruffy-generation2");
    }

    public void testScruffyInvalidation1() {
        WasInvalidatedBy inv1 = pFactory.newWasInvalidatedBy(q("inv1"),
                                                             q("e1"), q("a1"));
        inv1.setTime(pFactory.newTimeNow());
        WasInvalidatedBy inv2 = pFactory.newWasInvalidatedBy(q("inv1"),
                                                             q("e1"), q("a1"));
        inv2.setTime(pFactory.newISOTime("2012-12-03T21:08:16.686Z"));
        Entity e1 = pFactory.newEntity(q("e1"));
        Activity a1 = pFactory.newActivity(q("a1"));
        Statement[] opt = new Statement[] { e1, a1 };
        Statement[] statements = new Statement[] { inv1, inv2 };
        makeDocAndTest(statements, opt, "target/scruffy-invalidation1");
    }

    public void testScruffyInvalidation2() {
        WasInvalidatedBy inv1 = pFactory.newWasInvalidatedBy(q("inv1"),
                                                             q("e1"), q("a1"));
        inv1.setTime(pFactory.newTimeNow());
        inv1.getOther().add(pFactory.newOther(EX_NS, "tag2", EX_PREFIX,
                                              "hello", name.XSD_STRING));

        WasInvalidatedBy inv2 = pFactory.newWasInvalidatedBy(q("inv1"),
                                                             q("e1"), q("a1"));
        inv2.setTime(pFactory.newISOTime("2012-12-03T21:08:16.686Z"));
        inv2.getOther().add(pFactory.newOther(EX_NS, "tag2", EX_PREFIX, "hi",
                                              name.XSD_STRING));

        Entity e1 = pFactory.newEntity(q("e1"));
        Activity a1 = pFactory.newActivity(q("a1"));
        Statement[] opt = new Statement[] { e1, a1 };
        Statement[] statements = new Statement[] { inv1, inv2 };
        makeDocAndTest(statements, opt, "target/scruffy-invalidation2");
    }

    public void testScruffyUsage1() {
        Used use1 = pFactory.newUsed(q("use1"), q("a1"), null, q("e1"));
        use1.setTime(pFactory.newTimeNow());
        Used use2 = pFactory.newUsed(q("use1"), q("a1"), null, q("e1"));
        use2.setTime(pFactory.newISOTime("2012-12-03T21:08:16.686Z"));
        Entity e1 = pFactory.newEntity(q("e1"));
        Activity a1 = pFactory.newActivity(q("a1"));
        Statement[] opt = new Statement[] { e1, a1 };
        Statement[] statements = new Statement[] { use1, use2 };
        makeDocAndTest(statements, opt, "target/scruffy-usage1");
    }

    public void testScruffyUsage2() {
        Used use1 = pFactory.newUsed(q("use1"), q("a1"), null, q("e1"));
        use1.setTime(pFactory.newTimeNow());
        use1.getOther().add(pFactory.newOther(EX_NS, "tag2", EX_PREFIX,
                                              "hello", name.XSD_STRING));

        Used use2 = pFactory.newUsed(q("use1"), q("a1"), null, q("e1"));
        use2.setTime(pFactory.newISOTime("2012-12-03T21:08:16.686Z"));
        use2.getOther().add(pFactory.newOther(EX_NS, "tag2", EX_PREFIX, "hi",
                                              name.XSD_STRING));

        Entity e1 = pFactory.newEntity(q("e1"));
        Activity a1 = pFactory.newActivity(q("a1"));
        Statement[] opt = new Statement[] { e1, a1 };
        Statement[] statements = new Statement[] { use1, use2 };
        makeDocAndTest(statements, opt, "target/scruffy-usage2");
    }

    public void testScruffyStart1() {
        WasStartedBy start1 = pFactory.newWasStartedBy(q("start1"), q("a1"),
                                                       q("e1"));
        start1.setTime(pFactory.newTimeNow());

        WasStartedBy start2 = pFactory.newWasStartedBy(q("start1"), q("a1"),
                                                       q("e1"));
        start2.setTime(pFactory.newISOTime("2012-12-03T21:08:16.686Z"));

        Entity e1 = pFactory.newEntity(q("e1"));
        Activity a1 = pFactory.newActivity(q("a1"));
        Statement[] opt = new Statement[] { e1, a1 };
        Statement[] statements = new Statement[] { start1, start2 };
        makeDocAndTest(statements, opt, "target/scruffy-start1");
    }

    public void testScruffyStart2() {
        WasStartedBy start1 = pFactory.newWasStartedBy(q("start1"), q("a1"),
                                                       q("e1"));
        start1.setTime(pFactory.newTimeNow());
        start1.getOther()
              .add(pFactory.newOther(EX_NS, "tag2", EX_PREFIX, "hello",
                                     name.XSD_STRING));

        WasStartedBy start2 = pFactory.newWasStartedBy(q("start1"), q("a1"),
                                                       q("e1"));
        start2.setTime(pFactory.newISOTime("2012-12-03T21:08:16.686Z"));
        start2.getOther().add(pFactory.newOther(EX_NS, "tag2", EX_PREFIX, "hi",
                                                name.XSD_STRING));

        Entity e1 = pFactory.newEntity(q("e1"));
        Activity a1 = pFactory.newActivity(q("a1"));
        Statement[] opt = new Statement[] { e1, a1 };
        Statement[] statements = new Statement[] { start1, start2 };
        makeDocAndTest(statements, opt, "target/scruffy-start2");
    }

    public void testScruffyStart3() {
        WasStartedBy start1 = pFactory.newWasStartedBy(q("start1"), q("a1"),
                                                       q("e1"));
        start1.setTime(pFactory.newTimeNow());
        start1.getOther()
              .add(pFactory.newOther(EX_NS, "tag2", EX_PREFIX, "hello",
                                     name.XSD_STRING));
        start1.setStarter(q("a1s"));

        WasStartedBy start2 = pFactory.newWasStartedBy(q("start1"), q("a1"),
                                                       q("e1"));
        start2.setTime(pFactory.newISOTime("2012-12-03T21:08:16.686Z"));
        start2.getOther().add(pFactory.newOther(EX_NS, "tag2", EX_PREFIX, "hi",
                                                name.XSD_STRING));
        start2.setStarter(q("a2s"));

        Entity e1 = pFactory.newEntity(q("e1"));
        Activity a1 = pFactory.newActivity(q("a1"));
        Activity a2 = pFactory.newActivity(q("a2"));
        Activity a2s = pFactory.newActivity(q("a2s"));
        Statement[] opt = new Statement[] { e1, a1, a2, a2s };
        Statement[] statements = new Statement[] { start1, start2 };
        makeDocAndTest(statements, opt, "target/scruffy-start3");
    }

    public void testScruffyStart4() {
        WasStartedBy start1 = pFactory.newWasStartedBy(q("start1"), q("a1"),
                                                       q("e1"));
        start1.setTime(pFactory.newTimeNow());
        start1.getOther()
              .add(pFactory.newOther(EX_NS, "tag2", EX_PREFIX, "hello",
                                     name.XSD_STRING));
        start1.setStarter(q("a1s"));

        WasStartedBy start2 = pFactory.newWasStartedBy(q("start1"), q("a2"),
                                                       q("e2"));
        start2.setTime(pFactory.newISOTime("2012-12-03T21:08:16.686Z"));
        start2.getOther().add(pFactory.newOther(EX_NS, "tag2", EX_PREFIX, "hi",
                                                name.XSD_STRING));
        start2.setStarter(q("a2s"));

        Entity e1 = pFactory.newEntity(q("e1"));
        Activity a1 = pFactory.newActivity(q("a1"));
        Activity a1s = pFactory.newActivity(q("a1s"));
        Entity e2 = pFactory.newEntity(q("e2"));
        Activity a2 = pFactory.newActivity(q("a2"));
        Activity a2s = pFactory.newActivity(q("a2s"));
        Statement[] opt = new Statement[] { e1, a1, a1s, e2, a2, a2s };
        Statement[] statements = new Statement[] { start1, start2 };
        makeDocAndTest(statements, opt, "target/scruffy-start4");
    }

    public void testScruffyEnd1() {
        WasEndedBy end1 = pFactory.newWasEndedBy(q("end1"), q("a1"), q("e1"));
        end1.setTime(pFactory.newTimeNow());

        WasEndedBy end2 = pFactory.newWasEndedBy(q("end1"), q("a1"), q("e1"));
        end2.setTime(pFactory.newISOTime("2012-12-03T21:08:16.686Z"));

        Entity e1 = pFactory.newEntity(q("e1"));
        Activity a1 = pFactory.newActivity(q("a1"));
        Statement[] opt = new Statement[] { e1, a1 };
        Statement[] statements = new Statement[] { end1, end2 };
        makeDocAndTest(statements, opt, "target/scruffy-end1");
    }

    public void testScruffyEnd2() {
        WasEndedBy end1 = pFactory.newWasEndedBy(q("end1"), q("a1"), q("e1"));
        end1.setTime(pFactory.newTimeNow());
        end1.getOther().add(pFactory.newOther(EX_NS, "tag2", EX_PREFIX,
                                              "hello", name.XSD_STRING));

        WasEndedBy end2 = pFactory.newWasEndedBy(q("end1"), q("a1"), q("e1"));
        end2.setTime(pFactory.newISOTime("2012-12-03T21:08:16.686Z"));
        end2.getOther().add(pFactory.newOther(EX_NS, "tag2", EX_PREFIX, "hi",
                                              name.XSD_STRING));

        Entity e1 = pFactory.newEntity(q("e1"));
        Activity a1 = pFactory.newActivity(q("a1"));
        Statement[] opt = new Statement[] { e1, a1 };
        Statement[] statements = new Statement[] { end1, end2 };
        makeDocAndTest(statements, opt, "target/scruffy-end2");
    }

    public void testScruffyEnd3() {
        WasEndedBy end1 = pFactory.newWasEndedBy(q("end1"), q("a1"), q("e1"));
        end1.setTime(pFactory.newTimeNow());
        end1.getOther().add(pFactory.newOther(EX_NS, "tag2", EX_PREFIX,
                                              "hello", name.XSD_STRING));
        end1.setEnder(q("a1s"));

        WasEndedBy end2 = pFactory.newWasEndedBy(q("end1"), q("a1"), q("e1"));
        end2.setTime(pFactory.newISOTime("2012-12-03T21:08:16.686Z"));
        end2.getOther().add(pFactory.newOther(EX_NS, "tag2", EX_PREFIX, "hi",
                                              name.XSD_STRING));
        end2.setEnder(q("a2s"));

        Entity e1 = pFactory.newEntity(q("e1"));
        Activity a1 = pFactory.newActivity(q("a1"));
        Activity a2 = pFactory.newActivity(q("a2"));
        Activity a2s = pFactory.newActivity(q("a2s"));
        Statement[] opt = new Statement[] { e1, a1, a2, a2s };
        Statement[] statements = new Statement[] { end1, end2 };
        makeDocAndTest(statements, opt, "target/scruffy-end3");
    }

    public void testScruffyEnd4() {
        WasEndedBy end1 = pFactory.newWasEndedBy(q("end4"), q("a1"), q("e1"));
        end1.setTime(pFactory.newTimeNow());
        end1.getOther().add(pFactory.newOther(EX_NS, "tag2", EX_PREFIX,
                                              "hello", name.XSD_STRING));
        end1.setEnder(q("a1s"));

        WasEndedBy end2 = pFactory.newWasEndedBy(q("end4"), q("a2"), q("e2"));
        end2.setTime(pFactory.newISOTime("2012-12-03T21:08:16.686Z"));
        end2.getOther().add(pFactory.newOther(EX_NS, "tag2", EX_PREFIX, "hi",
                                              name.XSD_STRING));
        end2.setEnder(q("a2s"));

        Entity e1 = pFactory.newEntity(q("e1"));
        Activity a1 = pFactory.newActivity(q("a1"));
        Activity a1s = pFactory.newActivity(q("a1s"));
        Entity e2 = pFactory.newEntity(q("e2"));
        Activity a2 = pFactory.newActivity(q("a2"));
        Activity a2s = pFactory.newActivity(q("a2s"));
        Statement[] opt = new Statement[] { e1, a1, a1s, e2, a2, a2s };
        Statement[] statements = new Statement[] { end1, end2 };
        makeDocAndTest(statements, opt, "target/scruffy-end4");
    }
    */

    public void testBundle1() {
        Used use1 = pFactory.newUsed(q("use1"), q("a1"), null, q("e1"));
        Entity e1 = pFactory.newEntity(q("e1"));
        Activity a1 = pFactory.newActivity(q("a1"));
        List<Statement> st1 = new LinkedList<Statement>();
        st1.add(a1);
        st1.add(e1);
        st1.add(use1);

        Bundle b1 = pFactory.newNamedBundle(q("bundle1"), st1);

        Used use2 = pFactory.newUsed(q("use2"), q("aa1"), null, q("ee1"));
        Entity ee1 = pFactory.newEntity(q("ee1"));
        Activity aa1 = pFactory.newActivity(q("aa1"));
        List<Statement> st2 = new LinkedList<Statement>();
        st2.add(aa1);
        st2.add(ee1);
        st2.add(use2);

        b1.setNamespace(Namespace.gatherNamespaces(b1,pFactory));

        Bundle b2 = pFactory.newNamedBundle(q("bundle2"), st2);

        Entity eb1 = pFactory.newEntity(q("bundle1"));
        pFactory.addBundleType(eb1);

        Entity eb2 = pFactory.newEntity(q("bundle2"));
        pFactory.addBundleType(eb2);

        b2.setNamespace(Namespace.gatherNamespaces(b2,pFactory));

        Statement[] statements = new Statement[] { eb1, eb2, };
        Bundle[] bundles = new Bundle[] { b1, b2 };

        makeDocAndTest(statements, bundles, "target/bundle1", null, true);

    }

    public void testBundle2() {
        Used use1 = pFactory.newUsed(q("use1"), q("a1"), null, q("e1"));
        Entity e1 = pFactory.newEntity(q("e1"));
        Activity a1 = pFactory.newActivity(q("a1"));
        List<Statement> st1 = new LinkedList<Statement>();
        st1.add(a1);
        st1.add(e1);
        st1.add(use1);

        Bundle b1 = pFactory.newNamedBundle(q("bundle1"), st1);
        b1.setNamespace(Namespace.gatherNamespaces(b1,pFactory));

        Used use2 = pFactory.newUsed(q("use2"), q("e1"), null, q("a1"));
        Entity ee1 = pFactory.newEntity(q("a1"));
        Activity aa1 = pFactory.newActivity(q("e1"));
        List<Statement> st2 = new LinkedList<Statement>();
        st2.add(aa1);
        st2.add(ee1);
        st2.add(use2);

        Bundle b2 = pFactory.newNamedBundle(q("bundle2"), st2);
        b2.setNamespace(Namespace.gatherNamespaces(b2,pFactory));

        Entity eb1 = pFactory.newEntity(q("bundle1"));
        pFactory.addBundleType(eb1);

        Entity eb2 = pFactory.newEntity(q("bundle2"));
        pFactory.addBundleType(eb2);

        Statement[] statements = new Statement[] { eb1, eb2, };
        Bundle[] bundles = new Bundle[] { b1, b2 };

        makeDocAndTest(statements, bundles, "target/bundle2", null, true);

    }


    public void testBundle3() {
        Used use1 = pFactory.newUsed(q("use1"), q("a1"), null, q("e1"));
        Entity e1 = pFactory.newEntity(q("e1"));
        Activity a1 = pFactory.newActivity(q("a1"));
        List<Statement> st1 = new LinkedList<Statement>();
        st1.add(a1);
        st1.add(e1);
        st1.add(use1);

        Bundle b1 = pFactory.newNamedBundle(q("bundle1"), st1);

        Used use2 = pFactory.newUsed(q("use2"), q("aa1"), null, q("ee1"));
        Entity ee1 = pFactory.newEntity(q("ee1"));
        Activity aa1 = pFactory.newActivity(q("aa1"));
        List<Statement> st2 = new LinkedList<Statement>();
        st2.add(aa1);
        st2.add(ee1);
        st2.add(use2);

        Namespace ns1 = Namespace.gatherNamespaces(b1,pFactory);
        b1.setNamespace(ns1);

        Bundle b2 = pFactory.newNamedBundle(q("bundle2"), st2);

        Entity eb1 = pFactory.newEntity(q("bundle1"));
        pFactory.addBundleType(eb1);

        Entity eb2 = pFactory.newEntity(q("bundle2"));
        pFactory.addBundleType(eb2);

        Namespace ns2 = Namespace.gatherNamespaces(b2,pFactory);
        b2.setNamespace(ns2);

        Statement[] statements = new Statement[] { eb1, eb2, };
        Bundle[] bundles = new Bundle[] { b1, b2 };

        makeDocAndTest(statements, bundles, "target/bundle3", null, true);

    }


    public org.openprovenance.prov.model.QualifiedName another(String n) {
        return pFactory.newQualifiedName("http://another.org/", n, EX_PREFIX);
    }


    //TODO: issue with namespace not read properly when rereading a bundle!
    // TODO: document namespace not restored at the end of creation of bundle! so bundle namespace would still be extant after reading bundle

    public void testBundle4() {
        Used use1 = pFactory.newUsed(q("use1"), q("a1"), null, q("e1"));
        Entity e1 = pFactory.newEntity(q("e1"));
        Activity a1 = pFactory.newActivity(q("a1"));
        List<Statement> st1 = new LinkedList<>();
        st1.add(a1);
        st1.add(e1);
        st1.add(use1);
        Bundle b1 = pFactory.newNamedBundle(q("bundle4"), st1);
        Namespace ns1 = Namespace.gatherNamespaces(b1,pFactory);
        b1.setNamespace(ns1);
        //System.out.println("bundle 1 ns " + b1);


        Used use2 = pFactory.newUsed(another("use2"), another("aa1"), null, another("ee1"));
        Entity ee1 = pFactory.newEntity(another("ee1"));
        Activity aa1 = pFactory.newActivity(another("aa1"));
        List<Statement> st2 = new LinkedList<>();
        st2.add(aa1);
        st2.add(ee1);
        st2.add(use2);
        Bundle b2 = pFactory.newNamedBundle(another("bundle4_other"), st2);
        Namespace ns2 = Namespace.gatherNamespaces(b2,pFactory);
        b2.setNamespace(ns2);
        //System.out.println("bundle 2 ns " + b2);


        Entity eb1 = pFactory.newEntity(pFactory.newQualifiedName(EX_NS, "bundle4", "foo"));
        pFactory.addBundleType(eb1);

        Entity eb2 = pFactory.newEntity(another("bundle4_other"));
        pFactory.addBundleType(eb2);


        Statement[] statements = new Statement[] { eb1, eb2, };
        Bundle[] bundles = new Bundle[] { b1, b2 };

        makeDocAndTest(statements, bundles, "target/bundle4", null, true);

    }

    public void testBundle5() {
        Activity a1 = pFactory.newActivity(qInnerAsEx("a1"));
        List<Statement> st1 = new LinkedList<>();
        st1.add(a1);



        Bundle b5 = pFactory.newNamedBundle(qInnerAsEx("bundle5"), st1);
        Namespace ns5 = Namespace.gatherNamespaces(b5, pFactory);
        b5.setNamespace(ns5);


        Entity e1 = pFactory.newEntity(qOuterAsEx("e1"));

        //System.out.println("bundle 1 ns " + b5);
        Statement[] statements = new Statement[]{e1};
        Bundle[] bundles = new Bundle[]{b5};



        makeDocAndTest(statements, bundles, "target/bundle5", null, true);
    }

    public void testBundle6() {
        Activity a1 = pFactory.newActivity(qInner("a1"));
        List<Statement> st1 = new LinkedList<>();
        st1.add(a1);


        QualifiedName b5Id = qInner("bundle5");
        Bundle b6 = pFactory.newNamedBundle(b5Id, st1);
        Namespace ns6 = Namespace.gatherNamespaces(b6, pFactory);
        b6.setNamespace(ns6);


        QualifiedName q1 = qOuter("e1");
        Entity e1 = pFactory.newEntity(q1);

        QualifiedName q2 = qOuter("a2");
        Entity a2 = pFactory.newEntity(q2);
        b6.getStatement().add(a2);

        //System.out.println("bundle 1 ns " + b6);
        Statement[] statements = new Statement[]{e1};
        Bundle[] bundles = new Bundle[]{b6};

        Namespace ns=new Namespace();
        ns.register(q1.getPrefix(), q1.getNamespaceURI());

        ns6.setParent(ns);

        Document doc=pFactory.newDocument(ns,  Arrays.asList(statements), Arrays.asList(bundles));



        doTest(doc, "target/bundle6", null, true);
    }


    public void testDefault1() {
        QualifiedName q1 = qDefault("a1");
        Activity a1 = pFactory.newActivity(q1);
        List<Statement> statements = new LinkedList<>();
        statements.add(a1);


        QualifiedName q2 = qOuter("e2");
        Entity e2 = pFactory.newEntity(q2);
        statements.add(e2);


        Namespace ns=new Namespace();
        ns.register(q2.getPrefix(), q2.getNamespaceURI());
        ns.registerDefault(q1.getNamespaceURI());
        ns.register("bar", q1.getNamespaceURI());  // the default namespace also needs to be registered with a prefix, to facilitate provx serialisation as it seems to have prov ns as default


        Document doc=pFactory.newDocument(ns, statements, new LinkedList<>());



        doTest(doc, "target/default1", null, true);
    }

    /*
    public void testDictionaryInsertion1() {
        DerivedByInsertionFrom d1 = pFactory.newDerivedByInsertionFrom(null,
                                                                       q("d2"),
                                                                       q("d1"),
                                                                       null,
                                                                       null);

        Statement[] statements = new Statement[] { d1 };
        Statement[] opt = new Statement[] {};
        makeDocAndTest(statements, opt, "target/dictionaryInsertion1");

    }

    public void testDictionaryInsertion2() {
        DerivedByInsertionFrom d2 = pFactory.newDerivedByInsertionFrom(q("deriv"),
                                                                       q("d2"),
                                                                       q("d1"),
                                                                       null,
                                                                       null);

        Statement[] statements = new Statement[] { d2 };
        Statement[] opt = new Statement[] {};
        makeDocAndTest(statements, opt, "target/dictionaryInsertion2");

    }

    public void testDictionaryInsertion3() {
        List<Entry> ll = new LinkedList<Entry>();
        Entry p1 = pFactory.newEntry(pFactory.newKey("a", name.XSD_STRING),
                                     q("e0"));

        ll.add(p1);
        DerivedByInsertionFrom d3 = pFactory.newDerivedByInsertionFrom(q("deriv3"),
                                                                       q("d2"),
                                                                       q("d1"),
                                                                       ll, null);

        Statement[] statements = new Statement[] { d3 };
        Statement[] opt = new Statement[] {};
        makeDocAndTest(statements, opt, "target/dictionaryInsertion3");

    }

    public void testDictionaryInsertion4() {
        List<Entry> ll = new LinkedList<Entry>();
        Entry p1 = pFactory.newEntry(pFactory.newKey("a", name.XSD_STRING),
                                     q("e0"));

        ll.add(p1);
        Entry p2 = pFactory.newEntry(pFactory.newKey(1, name.XSD_INT),
                                     q("e1"));

        ll.add(p2);
        DerivedByInsertionFrom d4 = pFactory.newDerivedByInsertionFrom(q("deriv4"),
                                                                       q("d2"),
                                                                       q("d1"),
                                                                       ll, null);

        Statement[] statements = new Statement[] { d4 };
        Statement[] opt = new Statement[] {};
        makeDocAndTest(statements, opt, "target/dictionaryInsertion4");
    }

    public void testDictionaryInsertion5() {
        List<Entry> ll = new LinkedList<Entry>();
        Entry p1 = pFactory.newEntry(pFactory.newKey("a", name.XSD_STRING),
                                     q("e0"));

        ll.add(p1);
        Entry p2 = pFactory.newEntry(pFactory.newKey(1, name.XSD_INT),
                                     q("e1"));

        ll.add(p2);
        Entry p3 = pFactory.newEntry(pFactory.newKey(q("a"),
                                                     name.PROV_QUALIFIED_NAME),
                                     q("e2"));

        ll.add(p3);
        DerivedByInsertionFrom d5 = pFactory.newDerivedByInsertionFrom(q("deriv5"),
                                                                       q("d2"),
                                                                       q("d1"),
                                                                       ll, null);
        addFurtherAttributes(d5);

        Statement[] statements = new Statement[] { d5 };
        Statement[] opt = new Statement[] {};
        makeDocAndTest(statements, opt, "target/dictionaryInsertion5");

    }

    public void testDictionaryInsertion6() {
        List<Entry> ll = new LinkedList<Entry>();
        Entry p1 = pFactory.newEntry(pFactory.newKey("a", name.XSD_STRING),
                                     q("e0"));

        ll.add(p1);
        Entry p2 = pFactory.newEntry(pFactory.newKey(1, name.XSD_INT),
                                     q("e1"));

        ll.add(p2);
        Entry p3 = pFactory.newEntry(pFactory.newKey(q("a"),
                                                     name.PROV_QUALIFIED_NAME),
                                     q("e2"));

        ll.add(p3);
        DerivedByInsertionFrom d5 = pFactory.newDerivedByInsertionFrom(null,
                                                                       q("d2"),
                                                                       q("d1"),
                                                                       ll, null);

        Statement[] statements = new Statement[] { d5 };
        Statement[] opt = new Statement[] {};
        makeDocAndTest(statements, opt, "target/dictionaryInsertion6");

    }

    public void testDictionaryInsertion7() {
        List<Entry> ll = new LinkedList<Entry>();
        Entry p1 = pFactory.newEntry(pFactory.newKey("a", name.XSD_STRING),
                                     q("e0"));

        ll.add(p1);
        Entry p2 = pFactory.newEntry(pFactory.newKey(1, name.XSD_INT),
                                     q("e1"));

        ll.add(p2);
        Entry p3 = pFactory.newEntry(pFactory.newKey(q("a"),
                                                     name.PROV_QUALIFIED_NAME),
                                     q("e2"));

        ll.add(p3);

        DerivedByInsertionFrom d7 = pFactory.newDerivedByInsertionFrom(null,
                                                                       q("d2"),
                                                                       q("d1"),
                                                                       ll, null);

        addLabels(d7);
        Statement[] statements = new Statement[] { d7 };
        Statement[] opt = new Statement[] {};
        makeDocAndTest(statements, opt, "target/dictionaryInsertion7");

    }

    public void testDictionaryRemoval1() {
        DerivedByRemovalFrom d1 = pFactory.newDerivedByRemovalFrom(null,
                                                                   q("d2"),
                                                                   q("d1"),
                                                                   null, null);

        Statement[] statements = new Statement[] { d1 };
        Statement[] opt = new Statement[] {};
        makeDocAndTest(statements, opt, "target/dictionaryRemoval1");

    }

    public void testDictionaryRemoval2() {
        DerivedByRemovalFrom d2 = pFactory.newDerivedByRemovalFrom(q("removal"),
                                                                   q("d2"),
                                                                   q("d1"),
                                                                   null, null);

        Statement[] statements = new Statement[] { d2 };
        Statement[] opt = new Statement[] {};
        makeDocAndTest(statements, opt, "target/dictionaryRemoval2");

    }

    public void testDictionaryRemoval3() {
        List<org.openprovenance.prov.model.Key> ll = new LinkedList<org.openprovenance.prov.model.Key>();
        ll.add(pFactory.newKey("a", name.XSD_STRING));
        DerivedByRemovalFrom d3 = pFactory.newDerivedByRemovalFrom(q("removal3"),
                                                                   q("d2"),
                                                                   q("d1"), ll,
                                                                   null);

        Statement[] statements = new Statement[] { d3 };
        Statement[] opt = new Statement[] {};
        makeDocAndTest(statements, opt, "target/dictionaryRemoval3");

    }

    public void testDictionaryRemoval4() {
        List<org.openprovenance.prov.model.Key> ll = new LinkedList<org.openprovenance.prov.model.Key>();
        ll.add(pFactory.newKey("a", name.XSD_STRING));
        ll.add(pFactory.newKey("1", name.XSD_INT));

        DerivedByRemovalFrom d4 = pFactory.newDerivedByRemovalFrom(q("removal4"),
                                                                   q("d2"),
                                                                   q("d1"), ll,
                                                                   null);

        Statement[] statements = new Statement[] { d4 };
        Statement[] opt = new Statement[] {};
        makeDocAndTest(statements, opt, "target/dictionaryRemoval4");
    }

    public void testDictionaryRemoval5() {
        List<org.openprovenance.prov.model.Key> ll = new LinkedList<org.openprovenance.prov.model.Key>();
        ll.add(pFactory.newKey("a", name.XSD_STRING));
        ll.add(pFactory.newKey("1", name.XSD_INT));
        ll.add(pFactory.newKey(q("a"), name.PROV_QUALIFIED_NAME));

        DerivedByRemovalFrom d5 = pFactory.newDerivedByRemovalFrom(q("removal5"),
                                                                   q("d2"),
                                                                   q("d1"), ll,
                                                                   null);
        addFurtherAttributes(d5);

        Statement[] statements = new Statement[] { d5 };
        Statement[] opt = new Statement[] {};
        makeDocAndTest(statements, opt, "target/dictionaryRemoval5");

    }
    */

    public void testDictionaryMembership1() { // this makes no sense, member
                                                // is mandatory.
        DictionaryMembership mem = pFactory.newDictionaryMembership(q("d"),
                                                                    null);

        Statement[] statements = new Statement[] { mem };
        Statement[] opt = new Statement[] {};
        makeDocAndTest(statements, opt, "target/dictionaryMembership1");
    }

    public void testDictionaryMembership2() {
        List<Entry> ll = new LinkedList<Entry>();
        Entry p1 = pFactory.newEntry(pFactory.newKey("a", name.XSD_STRING),
                                     q("e0"));
        ll.add(p1);
        DictionaryMembership d5 = pFactory.newDictionaryMembership(q("d"), ll);

        Statement[] statements = new Statement[] { d5 };
        Statement[] opt = new Statement[] {};
        makeDocAndTest(statements, opt, "target/dictionaryMembership2");

    }

    public void testDictionaryMembership3() {
        List<Entry> ll = new LinkedList<Entry>();
        Entry p1 = pFactory.newEntry(pFactory.newKey("a", name.XSD_STRING),
                                     q("e0"));

        ll.add(p1);
        Entry p2 = pFactory.newEntry(pFactory.newKey(1, name.XSD_INT),
                                     q("e1"));

        ll.add(p2);
        DictionaryMembership d5 = pFactory.newDictionaryMembership(q("d"), ll);

        Statement[] statements = new Statement[] { d5 };
        Statement[] opt = new Statement[] {};
        makeDocAndTest(statements, opt, "target/dictionaryMembership3");

    }


    public void testDictionaryMembership4() {
        List<Entry> ll = new LinkedList<Entry>();
        Entry p1 = pFactory.newEntry(pFactory.newKey("a", name.XSD_STRING),
                                     q("e0"));

        ll.add(p1);
        Entry p2 = pFactory.newEntry(pFactory.newKey(1, name.XSD_INT),
                                     q("e1"));

        ll.add(p2);
        Entry p3 = pFactory.newEntry(pFactory.newKey(q("a"),
                                                     name.PROV_QUALIFIED_NAME),
                                     q("e2"));

        ll.add(p3);

        DictionaryMembership d5 = pFactory.newDictionaryMembership(q("d"), ll);

        Statement[] statements = new Statement[] { d5 };
        Statement[] opt = new Statement[] {};
        makeDocAndTest(statements, opt, "target/dictionaryMembership4");

    }



    public void testQualifiedSpecializationOf1() {
        QualifiedSpecializationOf gen = pFactory.newQualifiedSpecializationOf(q("spec1"), q("e1"),q("e2"), null);
        makeDocAndTest(gen, "target/qualified-specialization1");

    }

    public void testQualifiedSpecializationOf2() {
        QualifiedSpecializationOf gen = pFactory.newQualifiedSpecializationOf(null, q("e1"),q("e2"), null);
        makeDocAndTest(gen, "target/qualified-specialization2");

    }




    public void testQualifiedAlternateOf1() {
        QualifiedAlternateOf gen = pFactory.newQualifiedAlternateOf(q("alt1"), q("e1"),q("e2"), null);
        makeDocAndTest(gen, "target/qualified-alternate1");
    }

    public void testQualifiedAlternateOf2() {
        QualifiedAlternateOf gen = pFactory.newQualifiedAlternateOf(null, q("e1"),q("e2"), null);
        makeDocAndTest(gen, "target/qualified-alternate2");

    }




    public void testQualifiedHadMember1() {
        List<org.openprovenance.prov.model.QualifiedName> entities=new LinkedList<org.openprovenance.prov.model.QualifiedName>();
        entities.add(q("e2"));

        QualifiedHadMember gen = pFactory.newQualifiedHadMember(q("mem"), q("e1"),entities, null);
        makeDocAndTest(gen, "target/qualified-member1");

    }

    public void testQualifiedHadMember2() {
        List<org.openprovenance.prov.model.QualifiedName> entities=new LinkedList<org.openprovenance.prov.model.QualifiedName>();
        entities.add(q("e2"));
        QualifiedHadMember gen = pFactory.newQualifiedHadMember(null, q("e1"),entities, null);
        makeDocAndTest(gen, "target/qualified-member2");

    }


}
