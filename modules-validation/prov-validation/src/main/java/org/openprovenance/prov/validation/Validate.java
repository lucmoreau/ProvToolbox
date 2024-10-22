package org.openprovenance.prov.validation;

import java.util.*;


import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.openprovenance.prov.configuration.Configuration;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.Entity;
import org.openprovenance.prov.model.Name;
import org.openprovenance.prov.model.Bundle;
import org.openprovenance.prov.model.Namespace;
import org.openprovenance.prov.model.QualifiedName;
import org.openprovenance.prov.model.SpecializationOf;
import org.openprovenance.prov.model.Statement;
import org.openprovenance.prov.model.StatementOrBundle;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.model.ProvUtilities;
import org.openprovenance.prov.validation.matrix.SparseMatrix;
import org.openprovenance.prov.validation.report.Dependencies;
import org.openprovenance.prov.validation.report.MalformedStatements;
import org.openprovenance.prov.validation.report.MergeReport;
import org.openprovenance.prov.validation.report.SpecializationReport;
import org.openprovenance.prov.validation.report.TypeOverlap;
import org.openprovenance.prov.validation.report.ValidationReport;

public class Validate {


	static Logger logger = LogManager.getLogger(Validate.class);

	private static String fileName = "config.properties";

	public static final String validatorVersion = getPropertiesFromClasspath(fileName).getProperty("validator.version");

	public static final String longValidatorVersion = validatorVersion + " (" + getPropertiesFromClasspath(fileName).getProperty("timestamp") + ")";

	public static final String longVersion = "Validator " + longValidatorVersion + ", ProvToolbox " + Configuration.longToolboxVersion;

	private static Properties getPropertiesFromClasspath(String propFileName) {
		return Configuration.getPropertiesFromClasspath(Validate.class, propFileName);
	}


	ProvUtilities u = new ProvUtilities();

	final Config config;
	public final Indexer ind;
	final Expansion expa;
	public final Uniqueness uniq;
	final Unification unif;
	final Inference inf;
	final public EventIndexer evtIdx;
	final public Types typeChecker;
	public Constraints constraints;
	final ProvFactory p;
	final Name name;
	final Gensym g;

	public Validate(Config config) {
		this(config, false);
	}

	public Validate(Config config, boolean staticGensym) {
		this.config = config;
		this.ind = new Indexer(config.p, config.om, staticGensym);
		this.p=ind.p;
		this.name=p.getName();
		this.g=ind.g;

		this.typeChecker=new Types(ind,u,config);

		this.expa = new Expansion(ind,config,typeChecker);
		this.uniq = new Uniqueness(ind);
		this.unif = new Unification(ind, uniq);
		this.inf = new Inference(ind,typeChecker);
		this.evtIdx = new EventIndexer(ind);

	}

	public Indexer getIndexer() {
		return ind;
	}

	public Constraints getConstraints() {
		return constraints;
	}

	public EventIndexer getEventIndexer() {
		return evtIdx;
	}

	public Inference getInference() {
		return inf;
	}

	public ValidationReport validate(Document inDocument) throws java.io.IOException {
		Pair<ValidationReport,Document> result=validate(u.getStatement(inDocument),true, inDocument.getNamespace());

		ValidationReport report=result.car;
		Document outDocument = result.cdr;




		for (Bundle bu: u.getNamedBundle(inDocument)) {
			List<Statement> statments= bu.getStatement();
			Validate validator=new Validate(config);
			Pair<ValidationReport,Document> buRes=validator.validate(statments,false, bu.getNamespace());
			ValidationReport buRep=buRes.car;
			QualifiedName id = bu.getId();
			org.openprovenance.prov.vanilla.QualifiedName qnId=new org.openprovenance.prov.vanilla.QualifiedName(id.getNamespaceURI(),id.getLocalPart(),id.getPrefix());
			buRep.setId(qnId);
			report.getValidationReport().add(buRep);

			Bundle bundle=u.getNamedBundle(buRes.cdr).get(0);

			List<StatementOrBundle> ll=outDocument.getStatementOrBundle();
			ll.add(bundle);


			// add a bit of provenance about the new bundle
			// state that it was derived from the input bundle
			Entity e=p.newEntity(bundle.getId());
			e.getType().add(p.newType(name.PROV_BUNDLE,name.PROV_QUALIFIED_NAME));
			ll.add(e);
			ll.add(p.newWasDerivedFrom((QualifiedName)null, bundle.getId(), bu.getId()));

			// chain namespaces
			bundle.getNamespace().setParent(outDocument.getNamespace());

		}

		Namespace.withThreadNamespace(outDocument.getNamespace());



		new Cleanup(u).cleanup(config, u.getStatement(outDocument), u.getNamedBundle(outDocument));

		return report;
	}

	public Pair<ValidationReport,Document> validate(List<Statement> statements, boolean isDocument, Namespace namespace)
			throws
			java.io.IOException {

		//ProvSerialiser serial = ProvSerialiser.getThreadProvSerialiser();

		ValidationReport report;

		logger.debug("Expanding terms");
		expa.expansion(config, statements);

		// ValidationReport report2 = constraints.getReport();
		// serial.serialiseValidationReport(new File(reportFile + "index2"),
		// report2, true);

		logger.debug("Indexing bundle");
		ind.index(statements);
		//System.out.println("********** found entities " + u.getEntity(b));
		logger.debug("... Indexing complete");

		logger.debug("Applying uniqueness constraints");
		ind.merger.updated = true; // always enter the loop once

		int iteration = 0;
		while (ind.merger.updated == true) {
			logger.debug("Applying uniqueness constraints: iteration "
					+ iteration++);

			ind.merger.updated = false;
			unif.applyUnification();
			uniq.uniqueAll(config);

		}




		logger.debug("Substitution is " + ind.merger.unificationSubstitution);
		logger.debug("Unique startTable is " + uniq.wasStartedByTable);

		unif.removeUnifiedEntries();
		unif.applyUnification();

		typeChecker.addDeclaredTypes();

		// comp.applyUnification();

		inf.inferIntervalBeginningAndEnds(config);

		inf.computeSpecializationClosure(config);
		inf.computeAlternateClosure(config);

		inf.specializationAttributesInference(config);

		logger.debug("spec table " + inf.specializationTable);

		inf.specializationIsNotReflexive(config);

		logger.debug("Save normal form");

		// logger.info(inf.summary());

		Document doc = completeDocument();
		//if (out!=null) serial.serialiseDocument(new File(out), doc, true);

		logger.debug("Event index");

		evtIdx.createEventIndex();

		logger.debug("... event index complete");

		logger.debug("Constraints");

		logger.debug("constraint checking");

		this.constraints = new Constraints(typeChecker, ind, inf, evtIdx);
		constraints.constraints(config);
		logger.debug("... constraint checking complete");

		//logger.debug("matrix is: \n" + constraints.getMatrix().displayMatrix2());

		logger.debug("maximum is        "
				+ constraints.getMatrix().getMaximum());

		List<Object> diagonal = constraints.getMatrix().diagonal();
		logger.debug("matrix diagonal is:");
		int i = 0;
		for (Object d : diagonal) {
			if (d != null) {
				logger.debug("m(" + i + "," + i + ")=" + d);
			}
			i++;
		}
		report = getReport();


		//LUC
		if (true) report.setNamespace(namespace);


		//logger.debug("serializeing report\n");

		//if (reportFile!=null) {
		//serial.serialiseValidationReport(new File(reportFile), report, true);
		//}

    	/*
	logger.debug("generating image\n");
	try {
	    constraints.getMatrix().generateImage1("target/pc1-complete.png");
	} catch (java.lang.OutOfMemoryError ofme) {
	    // forget it, image too big!
	} catch (java.lang.NegativeArraySizeException nase) {
	    // likewise
	}
	logger.debug("generatedImage\n");
    	 */

		return new Pair<ValidationReport, Document>(report,doc);

	}


	public ValidationReport getReport() {
		List<Object> diagonal = constraints.getMatrix().diagonal();
		ValidationReport report = new ValidationReport();

		int i = 0;
		for (Object o : diagonal) {
			if (o != null) {
				Integer d = (Integer) o;
				if (d > 0) {
					List<Integer> path = constraints.getMatrix().getPath(i, i);
					logger.debug(" path: " + path + " with " + d);
					if (SparseMatrix.isNonStrictOrdering(d)) {
						report.getNonStrictCycle().add(getCyclicEvents(path));
					} else {
						report.getCycle().add(getCyclicEvents(path));

					}
				}
			}
			i++;
		}

		for (String key : ind.successfulMerge.keySet()) {
			MergeReport mr = new MergeReport();
			mr.setKey(key);
			mr.getStatement().addAll(ind.successfulMerge.get(key));
			report.getSuccessfulMerge().add(mr);
		}
		for (String key : ind.failedMerge.keySet()) {
			MergeReport mr = new MergeReport();
			mr.setKey(key);
			mr.getStatement().addAll(ind.failedMerge.get(key));
			report.getFailedMerge().add(mr);
		}

		for (String key : ind.qualifiedNameMismatch.keySet()) {
			MergeReport mr = new MergeReport();
			mr.setKey(key);
			mr.getStatement().addAll(ind.qualifiedNameMismatch.get(key));
			report.getQualifiedNameMismatch().add(mr);
		}

		if (inf.failedSpecialization != null
				&& !inf.failedSpecialization.isEmpty()) {
			SpecializationReport sr = new SpecializationReport();
			report.setSpecializationReport(sr);
			for (SpecializationOf fs: inf.failedSpecialization) {
				sr.getSpecializationOf().add(p.newSpecializationOf(fs))
				;
			}
		}

    	/*
	if (constraints.intersection2!=null) {
	    if (!constraints.intersection2.isEmpty()) {
	        TypeReport tr=new TypeReport();
	        report.setTypeReport(tr);
	        for (String key: constraints.intersection1) {
	            tr.setKey(key);
	            tr.getEntityAndActivityAndWasGeneratedBy().add(ind.activityTable.get(key));
	            tr.getEntityAndActivityAndWasGeneratedBy().add(ind.entityTable.get(key));
	        }
	    }
	}
    	 */

		for (String key: constraints.typeOverlapTable.keySet()) {
			Collection<String> inferredTypes=constraints.typeOverlapTable.get(key);
			if ((inferredTypes!=null) && (!inferredTypes.isEmpty())) {
				Collection<String> conflicts=typeChecker.conflictingTypes(inferredTypes);
				if ((conflicts!=null) && (!conflicts.isEmpty())) {
					TypeOverlap to=new TypeOverlap();
					report.getTypeOverlap().add(to);
					to.getType().addAll(conflicts);
					to.setKey(key);
				}
			}
		}

		List<Statement> malformed = expa.getMalformed().getStatement();
		if (!(malformed.isEmpty())) {
			MalformedStatements m=new MalformedStatements();
			report.setMalformedStatements(m);
			m.getStatement().addAll(malformed);
		}

		return report;
	}

	public Dependencies getCyclicEvents(List<Integer> l) {
		Dependencies dep = new Dependencies();
		for (int i : l) {
			Statement mutableStatement=(Statement)evtIdx.eventTable.get(evtIdx.events.get(i));
			dep.getStatement().add(mutableStatement);
		}
		return dep;
	}

	public Document completeDocument() {
		Bundle nbundle = p.newNamedBundle((QualifiedName) null,
				ind.activityTable.values(),
				ind.entityTable.values(),
				ind.agentTable.values(),
				new LinkedList<Statement>());

		nbundle.setId(g.newId(nbundle));


		List<Statement> dependencies = nbundle.getStatement();

		dependencies.addAll(ind.usedTable.values());
		dependencies.addAll(ind.wasGeneratedByTable.values());
		dependencies.addAll(ind.wasInvalidatedByTable.values());
		dependencies.addAll(ind.wasStartedByTable.values());
		dependencies.addAll(ind.wasEndedByTable.values());
		dependencies.addAll(ind.wasDerivedFromTable.values());
		dependencies.addAll(ind.wasInformedByTable.values());
		dependencies.addAll(ind.wasAssociatedWithTable.values());
		dependencies.addAll(ind.wasAttributedToTable.values());
		dependencies.addAll(ind.actedOnBehalfOfTable.values());
		dependencies.addAll(ind.wasInfluencedByTable.values());
		for (QualifiedName e1 : inf.specializationTable.keySet()) {
			Set<QualifiedName> set = inf.specializationTable.get(e1);
			for (QualifiedName e2 : set) {
				dependencies.add(p.newSpecializationOf(e1,e2));
			}
		}
		for (QualifiedName e1 : inf.alternateTable.keySet()) {
			Set<QualifiedName> set = inf.alternateTable.get(e1);
			for (QualifiedName e2 : set) {
				dependencies.add(p.newAlternateOf(e1,e2));
			}
		}
		dependencies.addAll(ind.membershipList);
		dependencies.addAll(ind.mentionOfTable.values());


		Document doc = p.newDocument();
		doc.getStatementOrBundle().add(nbundle);
		Namespace ns=Namespace.gatherNamespaces(nbundle);
		nbundle.setNamespace(ns);
		doc.setNamespace(ns);
		//doc.setNss(prepNamespaces(ind));
		return doc;
	}


}
