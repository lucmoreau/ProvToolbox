package org.openprovenance.prov.service.validation;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.Namespace;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.service.core.PostService;
import org.openprovenance.prov.service.core.ServiceUtils;
import org.openprovenance.prov.service.core.ServiceUtilsConfig;
import org.openprovenance.prov.storage.api.DocumentResource;
import org.openprovenance.prov.storage.api.ResourceIndex;
import org.openprovenance.prov.storage.api.ResourceStorage;
import org.openprovenance.prov.storage.api.NonDocumentGenericResourceStorage;
import org.openprovenance.prov.storage.api.NonDocumentResourceStorage;
import org.openprovenance.prov.validation.Config;
import org.openprovenance.prov.validation.Constraints;
import org.openprovenance.prov.validation.Validate;
import org.openprovenance.prov.validation.report.ValidationReport;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;


public class ValidationServiceUtils  extends ServiceUtils {

	static Logger logger = LogManager.getLogger(ValidationServiceUtils.class);
	private final ProvFactory f;
	private final Namespace ns;
	private final ServiceUtils utils;
	private final ResourceIndex<ValidationResource> validationDocumentResourceIndex;

	@Override
	public ResourceIndex<DocumentResource> getDocumentResourceIndex() {
		return utils.getDocumentResourceIndex();
	}

	@Override
	public ResourceStorage getStorageManager() {
		return utils.getStorageManager();
	}



	public 	ValidationServiceUtils(PostService ps, ProvFactory f, Namespace ns, ServiceUtils utils, ServiceUtilsConfig config) {
		super(ps, config);
		this.f=f;
		this.ns=ns;
		this.utils=utils;
		ResourceIndex<?> indexer=utils.getExtensionMap().get(ValidationResource.getResourceKind());
		this.validationDocumentResourceIndex=(ResourceIndex<ValidationResource>) indexer;
	}

	public ResourceIndex<ValidationResource> getValidationResourceIndex() {
		return validationDocumentResourceIndex;
	}

	public Object normalizedDocument(String visibleId) {

		ValidationResource vr = getValidationResourceIndex().get(visibleId);

		String result;

		if (vr == null) {
			result = "Not found resource for : " + visibleId;
			return Response.status(Response.Status.NOT_FOUND).entity(result).build();
		}

		ValidationResource vr2=(ValidationResource)vr;


		if (!vr.getCompleted()) {
			result = "Not found constraints resource for : " + visibleId;
			return Response.status(Response.Status.NOT_FOUND).entity(result)
					.build();
		}

		//Document doc = vr2.getValidate().completeDocument();
		return null;
	}



	/*
	public void performValidation_old(Document bundle, ValidationResource vr) throws JAXBException,
			IOException {
		synchronized (vr) {

			if (vr.getReport() != null)
				return;


			vr.getExtension().put(ValidationResource.COMPLETE,vr.getStorageId() + "-complete.xml");
			vr.getExtension().put(ValidationResource.REPORT,vr.getStorageId() + "-reportFile.xml");
			vr.getExtension().put(ValidationResource.BUNDLE,(org.openprovenance.prov.xml.Document) bundle);

			Validate validator = new Validate(Config.newYesToAllConfig(f));

			ValidationReport report = validator.validate(bundle); // TODO: not
			// sure I should
			// pass those
			// files
			//vr.setReport(report);
			//vr.setIndexer(validator.getIndexer());
			// vr.completer=validator.getEventIndexer();
			vr.setCompleted(validator.getConstraints()!=null);
			//vr.setInference(validator.getInference());
			//vr.setValidate(validator);


			logger.warn("TODO: must use storage interface to serialize " + vr.getExtension().get(ValidationResource.REPORT));

			ProvSerialiser serial = ProvSerialiser.getThreadProvSerialiser();

			serial.serialiseValidationReport(new File((String)vr.getExtension().get(ValidationResource.REPORT)), report,
					ns, true);
		}
	}


	 */


	public boolean performValidation(Document doc,
									 ValidationResource vr,
									 ResourceIndex<ValidationResource> index, NonDocumentGenericResourceStorage<ValidationReport> reportStorage,
									 NonDocumentGenericResourceStorage<String> matrixStorage,
									 NonDocumentResourceStorage nonDocumentResourceStorage) throws IOException {

		logger.debug("validating document " + vr.getVisibleId());

		Validate validator = new Validate(Config.newYesToAllConfig(f, new ValidationObjectMaker()));

		ValidationReport report = validator.validate(doc);


		Namespace ns=doc.getNamespace();



		Constraints constraints=validator.getConstraints();

		boolean completed=constraints!=null;

		vr.setCompleted(completed);

		logger.debug("This is vr before saving " + vr + " " + vr.getCompleted() + " " + completed);


		if (completed) {

			// Do i want to expose this as a visible resource?
			//NonDocumentResource reportResource=getNonDocumentResourceIndex().newResource();

			String reportStorageId     = nonDocumentResourceStorage.newStore("xml", MediaType.APPLICATION_XML);
			String jsonReportStorageId = reportStorage.newStore("json", MediaType.APPLICATION_JSON);
			String matrixTextId        = matrixStorage.newStore("txt", MediaType.TEXT_PLAIN);
			String matrixPngId         = matrixStorage.newStore("png", ValidationService.IMAGE_PNG_TYPE.toString());

			vr.setReport(reportStorageId);
			vr.setJsonReport(jsonReportStorageId);
			vr.setMatrix(matrixTextId);
			vr.setPngMatrix(matrixPngId);

			logger.debug("This is vr before saving " + vr);

			// save this
			index.put(vr.getVisibleId(), vr);


			//store report.json

			report.getNamespace().register("val", "http://foo.foo/");
			System.out.println("report.getNamespace()=" + report.getNamespace());
			reportStorage.serializeObjectToStore(report,jsonReportStorageId);


			// for XML Serialisation, remove all namespaces
			report.setNamespace(null);
			report.getValidationReport().forEach(r -> r.setNamespace(null));


			//store report.xml
			ByteArrayOutputStream baos=new ByteArrayOutputStream();
			utils.getConfig().serialiser.serialiseObject(baos,report,"ignore", false);
			nonDocumentResourceStorage.copyStringToStore(baos.toString(),reportStorageId);



			//store matrix.txt
			String matrix = constraints.getMatrix().displayMatrix2();
			matrixStorage.copyStringToStore(matrix,matrixTextId);

			// store matrix.png
			ByteArrayOutputStream baos2=new ByteArrayOutputStream();
			constraints.getMatrix().generateImage1(baos2);
			matrixStorage.copyStringToStore(Base64.getEncoder().encodeToString(baos2.toByteArray()),matrixPngId);

			logger.debug("stored validation report document and matrix " + vr.getReport());
		} else {
			index.put(vr.getVisibleId(), vr);
		}

		return vr.getCompleted();
	}

	/*
	static class MyReportSerializer extends org.openprovenance.prov.core.xml.serialization.ProvSerialiser {

		@Override
		public SimpleModule makeModule() {
			SimpleModule smod=super.makeModule();
			smod.addSerializer(VarQName.class,new CustomVarQNameSerializer());
			System.out.println("*** adding VarQName serializer");
			return smod;
		}

		public class  CustomVarQNameSerializer extends StdSerializer<VarQName> {
			protected CustomVarQNameSerializer(Class<VarQName> t) {
				super(t);
			}

			protected CustomVarQNameSerializer() {
				super(VarQName.class);
			}

			@Override
			public void serialize(VarQName varQName, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
				System.out.println("*** in VarQName serializer");
				jsonGenerator.writeString(varQName.toString());
			};
		}
	}

	 */


}
