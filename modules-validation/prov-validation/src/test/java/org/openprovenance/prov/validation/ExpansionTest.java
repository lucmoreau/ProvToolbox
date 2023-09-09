package org.openprovenance.prov.validation;

import java.util.Hashtable;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.openprovenance.prov.model.ActedOnBehalfOf;
import org.openprovenance.prov.model.HadMember;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.model.ProvUtilities;
import org.openprovenance.prov.model.Namespace;
import org.openprovenance.prov.model.QualifiedName;
import org.openprovenance.prov.model.Used;
import org.openprovenance.prov.model.WasAssociatedWith;
import org.openprovenance.prov.model.WasDerivedFrom;
import org.openprovenance.prov.model.WasEndedBy;
import org.openprovenance.prov.model.WasGeneratedBy;
import org.openprovenance.prov.model.WasInvalidatedBy;
import org.openprovenance.prov.model.WasStartedBy;

import junit.framework.TestCase;

/**
 * @author lavm Testing of completion procedure
 *
 */
public class ExpansionTest extends TestCase {
	static Logger logger = LogManager.getLogger(ValidateTest.class);
	final Hashtable<String, String> namespaces = new Hashtable<String, String>();

	ProvFactory pFactory = new  org.openprovenance.prov.scala.mutable.ProvFactory();
	ProvUtilities u=new ProvUtilities();

	Namespace namespace=new Namespace();

	public QualifiedName q(String n) {
		return namespace.stringToQualifiedName(n,pFactory);
	}


	public ExpansionTest(String testName) {
		super(testName);
		namespace.register("ex", "http://example2.org/");
		namespace.registerDefault("http://example1.org/");
	}


	Indexer ind = new Indexer(pFactory, new ValidationObjectMaker());


	Config config = Config.newYesToAllConfig(pFactory, new ValidationObjectMaker());
	Types types=new Types(ind, u,config);
	Expansion expa=new Expansion(ind,config,types);


	public void testUnknown() {
		assertTrue(ind.g.newUnknown().equals(ind.g.newUnknown()));
	}

	public void testExpansionStart1() {
		WasStartedBy start = pFactory.newWasStartedBy(q("start1"),
				q("start1_a1"),
				q("start1_e1"),
				null);
		start.setTime(pFactory.newTimeNow());

		assertTrue(start.getActivity() != null);
		assertTrue(start.getStarter() == null);
		assertTrue(start.getTime() !=null);

		expa.expansion_wasStartedBy(config, start);

		assertTrue(start.getActivity() != null);
		assertTrue(start.getStarter() != null);
		assertTrue(start.getTime() !=null);



	}

	public void testExpansionStart1bis() {
		WasStartedBy start = pFactory.newWasStartedBy(q("start1"),
				q("start1_a1"),
				q("start1_e1"),
				null);

		assertTrue(start.getActivity() != null);
		assertTrue(start.getStarter() == null);
		assertTrue(start.getTime() ==null);

		expa.expansion_wasStartedBy(config, start);

		assertTrue(start.getActivity() != null);
		assertTrue(start.getStarter() != null);
		assertTrue(start.getTime() !=null);



	}

	public void testExpansionStart2() {
		WasStartedBy start = pFactory.newWasStartedBy(q("start2"),
				q("start2_a1"),
				null,
				null);
		assertTrue(start.getActivity() != null);
		assertTrue(start.getTrigger() == null);
		assertTrue(start.getStarter() == null);
		expa.expansion_wasStartedBy(config, start);
		assertTrue(start.getActivity() != null);
		assertTrue(start.getTrigger() != null);
		assertTrue(start.getStarter() != null);

	}

	public void testExpansionStart3() {
		WasStartedBy start = pFactory.newWasStartedBy(q("start1"), null, null,null);
		assertTrue(start.getActivity() == null);
		assertTrue(start.getTrigger() == null);
		assertTrue(start.getStarter() == null);
		try {
			expa.expansion_wasStartedBy(config, start);
		} catch (UnsupportedOperationException uoe) {
			assertTrue(true);

		}
	}

	public void testExpansionStart4() {
		WasStartedBy start = pFactory.newWasStartedBy((QualifiedName) null,
				q("a1"),
				q("e1"),
				null);
		assertTrue(start.getId() == null);
		expa.expansion_wasStartedBy(config, start);
		assertTrue(start.getId() != null);
	}

	public void testExpansionEnd1() {
		WasEndedBy end = pFactory.newWasEndedBy(q("end1"),
				q("a1"),
				q("e1"),
				null);
		assertTrue(end.getActivity() != null);
		assertTrue(end.getEnder() == null);
		expa.expansion_wasEndedBy(config, end);
		assertTrue(end.getActivity() != null);
		assertTrue(end.getEnder() != null);
	}

	public void testExpansionEnd2() {
		WasEndedBy end = pFactory.newWasEndedBy(q("end1"),
				q("a1"),
				null,
				null);
		assertTrue(end.getActivity() != null);
		assertTrue(end.getTrigger() == null);
		assertTrue(end.getEnder() == null);
		expa.expansion_wasEndedBy(config, end);
		assertTrue(end.getActivity() != null);
		assertTrue(end.getTrigger() != null);
		assertTrue(end.getEnder() != null);
	}

	public void testExpansionEnd3() {
		WasEndedBy end = pFactory.newWasEndedBy(q("end1"), null, null, null);
		assertTrue(end.getActivity() == null);
		assertTrue(end.getTrigger() == null);
		assertTrue(end.getEnder() == null);
		try {
			expa.expansion_wasEndedBy(config, end);
		} catch (UnsupportedOperationException uoe) {
			assertTrue(true);

		}
	}

	public void testExpansionEnd4() {
		WasEndedBy end = pFactory.newWasEndedBy((QualifiedName) null,
				q("a1"),
				q("e1"),
				null);
		assertTrue(end.getId() == null);
		expa.expansion_wasEndedBy(config, end);
		assertTrue(end.getId() != null);
	}

	public void testExpansionGeneration1() {
		WasGeneratedBy der = pFactory.newWasGeneratedBy(q("gen1"),
				q("e1"),
				null);
		assertTrue(der.getEntity() != null);
		assertTrue(der.getActivity() == null);
		expa.expansion_wasGeneratedBy(config, der);
		assertTrue(der.getActivity() != null);
		assertTrue(der.getEntity() != null);
	}

	public void testExpansionGeneration2() {
		WasGeneratedBy der = pFactory.newWasGeneratedBy(q("gen2"),
				null,
				null);
		assertTrue(der.getEntity() == null);
		assertTrue(der.getActivity() == null);
		try {
			expa.expansion_wasGeneratedBy(config, der);
		} catch (UnsupportedOperationException uoe) {
			assertTrue(true);

		}
	}

	public void testExpansionInvalidation1() {
		WasInvalidatedBy inv = pFactory.newWasInvalidatedBy(q("inv1"),
				q("e1"),
				null);
		assertTrue(inv.getEntity() != null);
		assertTrue(inv.getActivity() == null);
		expa.expansion_wasInvalidatedBy(config, inv);
		assertTrue(inv.getActivity() != null);
		assertTrue(inv.getEntity() != null);
	}

	public void testExpansionInvalidation2() {
		WasInvalidatedBy inv = pFactory.newWasInvalidatedBy(q("inv1"),
				(QualifiedName)null,
				null);
		assertTrue(inv.getEntity() == null);
		assertTrue(inv.getActivity() == null);
		try {
			expa.expansion_wasInvalidatedBy(config, inv);
		} catch (UnsupportedOperationException uoe) {
			assertTrue(true);

		}
	}

	public void testExpansionUsage1() {
		Used use = pFactory.newUsed(q("use1"),
				q("e1"),
				null);
		assertTrue(use.getEntity() == null);
		assertTrue(use.getActivity() != null);
		expa.expansion_used(config, use);
		assertTrue(use.getActivity() != null);
		assertTrue(use.getEntity() != null);
	}

	public void testExpansionUsage2() {
		Used use = pFactory.newUsed(q("use1"),
				(QualifiedName) null,
				null);
		assertTrue(use.getEntity() == null);
		assertTrue(use.getActivity() == null);
		try {
			expa.expansion_used(config, use);
		} catch (UnsupportedOperationException uoe) {
			assertTrue(true);

		}
	}

	public void testExpansionDerivation1() {
		WasDerivedFrom der = pFactory.newWasDerivedFrom(q("der1"),
				q("e2"),
				q("e1"));
		assertTrue(der.getGeneratedEntity() != null);
		assertTrue(der.getUsedEntity() != null);
		assertTrue(der.getActivity() == null);
		expa.expansion_wasDerivedFrom(config, der);
		assertTrue(der.getGeneratedEntity() != null);
		assertTrue(der.getUsedEntity() != null);
		assertTrue(der.getActivity() != null);
		assertTrue(der.getActivity() instanceof Unknown);
		assertTrue(der.getGeneration() instanceof Unknown);
		assertTrue(der.getUsage() instanceof Unknown);


	}

	public void testExpansionDerivation2() {
		WasDerivedFrom der = pFactory.newWasDerivedFrom(q("der2"),
				q("e2"),
				null);
		assertTrue(der.getGeneratedEntity() != null);
		assertTrue(der.getUsedEntity() == null);
		assertTrue(der.getActivity() == null);
		try {
			expa.expansion_wasDerivedFrom(config, der);
		} catch (UnsupportedOperationException uoe) {
			assertTrue(true);
		}
	}
	public void testExpansionDerivation3() {
		WasDerivedFrom der = pFactory.newWasDerivedFrom(q("der3"),
				null,
				q("e2"));
		assertTrue(der.getGeneratedEntity() == null);
		assertTrue(der.getUsedEntity() != null);
		assertTrue(der.getActivity() == null);
		try {
			expa.expansion_wasDerivedFrom(config, der);
		} catch (UnsupportedOperationException uoe) {
			assertTrue(true);
		}
	}
	public void testExpansionDerivation4() {
		WasDerivedFrom der = pFactory.newWasDerivedFrom(q("der4"),
				q("e2"),
				q("e1"));
		der.setActivity(q("a"));
		assertTrue(der.getGeneratedEntity() != null);
		assertTrue(der.getUsedEntity() != null);
		assertTrue(der.getActivity() != null);
		expa.expansion_wasDerivedFrom(config, der);
		assertTrue(der.getGeneratedEntity() != null);
		assertTrue(der.getUsedEntity() != null);
		assertTrue(der.getActivity() != null);
		assertTrue(der.getGeneration() != null);
		assertTrue(der.getUsage() != null);


	}

	public void testExpansionAssociation1() {
		WasAssociatedWith assoc = pFactory.newWasAssociatedWith(q("assoc1"),
				q("a1"),
				null);
		assertTrue(assoc.getAgent() == null);
		assertTrue(assoc.getActivity() != null);
		assertTrue(assoc.getPlan() == null);
		expa.expansion_wasAssociatedWith(config, assoc);
		assertTrue(assoc.getActivity() != null);
		assertTrue(assoc.getAgent() != null);
		assertTrue(assoc.getPlan() != null);
		assertTrue(assoc.getPlan() instanceof Unknown);

	}

	public void testExpansionAssociation2() {
		WasAssociatedWith assoc = pFactory.newWasAssociatedWith(q("assoc1"),
				q("a1"),
				q("ag1"));
		assertTrue(assoc.getAgent() != null);
		assertTrue(assoc.getActivity() != null);
		assertTrue(assoc.getPlan() == null);
		expa.expansion_wasAssociatedWith(config, assoc);
		assertTrue(assoc.getActivity() != null);
		assertTrue(assoc.getAgent() != null);
		assertTrue(assoc.getPlan() instanceof Unknown);

	}
	public void testExpansionAssociation3() {
		WasAssociatedWith assoc = pFactory.newWasAssociatedWith(q("assoc1"),
				null,
				q("ag1"));
		assertTrue(assoc.getAgent() != null);
		assertTrue(assoc.getActivity() == null);
		assertTrue(assoc.getPlan() == null);
		try {
			expa.expansion_wasAssociatedWith(config, assoc);
		} catch (UnsupportedOperationException uoe) {
			assertTrue(true);
		}

	}
	public void testExpansionDelegation1() {
		ActedOnBehalfOf del = pFactory.newActedOnBehalfOf(q("del1"),
				q("del1_ag2"),
				null);
		assertTrue(del.getDelegate() != null);
		assertTrue(del.getResponsible() == null);
		assertTrue(del.getActivity() == null);
		expa.expansion_actedOnBehalfOf(config, del);
		assertTrue(del.getDelegate() != null);
		assertTrue(del.getResponsible() != null);
		assertTrue(del.getActivity() != null);
	}

	public void testExpansionDelegation2() {
		ActedOnBehalfOf del = pFactory.newActedOnBehalfOf(q("del2"),
				q("ag2"),
				q("ag1"));
		assertTrue(del.getDelegate() != null);
		assertTrue(del.getResponsible() != null);
		assertTrue(del.getActivity() == null);
		expa.expansion_actedOnBehalfOf(config, del);
		assertTrue(del.getDelegate() != null);
		assertTrue(del.getResponsible() != null);
		assertTrue(del.getActivity() != null);

	}


	public void testExpansionDelegation3() {
		ActedOnBehalfOf del = pFactory.newActedOnBehalfOf(q("del3"),
				q("ag2"),
				null,
				q("a"),
				null);
		assertTrue(del.getDelegate() != null);
		assertTrue(del.getResponsible() == null);
		assertTrue(del.getActivity() != null);
		expa.expansion_actedOnBehalfOf(config, del);
		assertTrue(del.getDelegate() != null);
		assertTrue(del.getResponsible() != null);
		assertTrue(del.getActivity() != null);

	}

	public void testExpansionDelegation4() {
		ActedOnBehalfOf del = pFactory.newActedOnBehalfOf(q("del4"),
				null,
				q("ag1"),
				q("a"),
				null);
		assertTrue(del.getDelegate() == null);
		assertTrue(del.getResponsible() != null);
		assertTrue(del.getActivity() != null);
		try {
			expa.expansion_actedOnBehalfOf(config, del);
		} catch (UnsupportedOperationException uoe) {
			assertTrue(true);
		}
	}


	public void testExpansionMembership1() {
		HadMember mem = pFactory.newHadMember((QualifiedName)null);

		assertTrue(mem.getCollection() == null);
		assertTrue(mem.getEntity().isEmpty());
		try {
			expa.expansion_membership(config, mem);
		} catch (UnsupportedOperationException uoe) {
			assertTrue(true);
		}
	}
	public void testExpansionMembership2() {
		HadMember mem = pFactory.newHadMember(q("ex:c1"));

		assertTrue(mem.getCollection()!= null);
		assertTrue(mem.getEntity().isEmpty());
		try {
			expa.expansion_membership(config, mem);
		} catch (UnsupportedOperationException uoe) {
			assertTrue(true);
		}
	}

}
