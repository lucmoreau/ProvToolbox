package org.openprovenance.prov.service.core;

import java.util.Arrays;
import java.util.List;

public interface Constants {
	public static final String DOCUMENT_NOT_FOUND = "Document not found";
	public static final String WILDCARD = "*";
	public static final String ACCESS_CONTROL_ALLOW_ORIGIN = "Access-Control-Allow-Origin";
	public static final String HEADER_PARAM_ACCEPT = "Accept";
    public static final String PROVAPI = "provapi";
    

    public static final String ALLOWABLE_INPUT_DOCUMENT_EXTENSIONS = "ttl,provn,provx,json,rdf,trig,jsonld";
    public static final String ALLOWABLE_OUTPUT_DOCUMENT_EXTENSIONS = "ttl,provn,provx,json,rdf,trig,svg,png,pdf,jpg,jpeg,jsonld";
    public static List<String> translationExtensions = Arrays.asList(ALLOWABLE_OUTPUT_DOCUMENT_EXTENSIONS.split(","));


    public static final String EXPANSION = "expansion";
    public static final String TEMPLATE_EXPANSION = "templateExpansion";
    public static final String SUMMARISATION = "summarisation";


}
