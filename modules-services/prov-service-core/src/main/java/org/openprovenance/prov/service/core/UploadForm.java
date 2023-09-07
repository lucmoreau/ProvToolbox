package org.openprovenance.prov.service.core;

import jakarta.ws.rs.FormParam;


import io.swagger.v3.oas.annotations.Parameter;


import java.io.File;


public class UploadForm {
 
	public UploadForm() {
	}
 
	private File file;
 
	public File getFile() {
		return file;
	}
 
    @Parameter(name = "file", description = "file with some provenance")
	@FormParam("file")
	//@PartType("application/octet-stream")
	public void setFile(File file) {
		this.file = file;
	}
 
	
	private String url;

	public String getUrl() {
		return url;
	}

	@Parameter(name = "url", example = "url to some provenance" )
	@FormParam("url")
	public void setUrl(String url) {
		this.url = url;
	}
	
	private String statements;
	private TypeIn type;

	private TypeOut translate;

	public String getStatements() {
		return statements;
	}

    @Parameter(name = "statements", description = "provenance statements") 
	@FormParam("statements")

	public void setStatements(String statements) {
		this.statements = statements;
	}

	public TypeIn getType() {
		return type;
	}

    @Parameter(name = "type", description = "type of whatever")
	@FormParam("type")
	public void setType(TypeIn type) {
		this.type = type;
	}

	@Parameter(name = "translate", description = "translation operation")
	@FormParam("translate")
	public void setTranslate(TypeOut translate) {
		this.translate = translate;
	}

    enum TypeOut { json, provn, provx, ttl, trig, svg, pdf, jpg }
    enum TypeIn { json, provn, provx, ttl, trig }

}