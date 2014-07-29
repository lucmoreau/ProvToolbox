package org.openprovenance.prov.interop;

import javax.ws.rs.core.MediaType;

/** Definition of all Media Types and file Extensions supported by ProvToolbox. */

public interface InteropMediaType {

    /** The recommended extension for DOT files (Graphviz).
     * @see <a href="http://www.iana.org/assignments/media-types/text/vnd.graphviz">Media Type for Graphviz</a> */
    public static final String EXTENSION_DOT = "dot";
    public static final String EXTENSION_JPEG = "jpeg";
    public static final String EXTENSION_JPG = "jpg";
    public static final String EXTENSION_JSON = "json";
    public static final String EXTENSION_PDF = "pdf";
    /** The recommended extension for PROV-N files.
     * @see <a href="http://www.w3.org/TR/prov-n/#media-type">Media Type for PROV-N</a> */
    public static final String EXTENSION_PROVN = "provn";
    /** The recommended extension for PROV-XML files.
     * @see <a href="http://www.w3.org/TR/prov-xml/#media-type">Media Type for PROV-XML</a> */
    public static final String EXTENSION_PROVX = "provx";
    /** The recommended extension for RDF/XML files.
     * @see <a href="http://www.w3.org/TR/REC-rdf-syntax/#section-MIME-Type">Media Type for RDF/XML</a> */
    public static final String EXTENSION_RDF = "rdf";
    /** The extension for SCG files (Note, where is this recommended?).
     * @see <a href="http://www.w3.org/TR/SVGTiny12/mimereg.html#mime-registration">Media Type for SVG</a> */
    public static final String EXTENSION_SVG = "svg";
    /** The recommended extension for TRIG files.
     * @see <a href="http://www.w3.org/TR/trig/#sec-mediaReg">Media Type for TRIG</a> */
    public static final String EXTENSION_TRIG = "trig";
    /** The recommended extension for Turtle files.
     * @see <a href="http://www.w3.org/TR/turtle/#sec-mediaReg">Media Type for Turtle</a> */
    public static final String EXTENSION_TTL = "ttl";
    /** The extension for XML files. */
    public static final String EXTENSION_XML = "xml";
    public static final String MEDIA_APPLICATION_FORM_URLENCODED = MediaType.APPLICATION_FORM_URLENCODED;
    public static final String MEDIA_APPLICATION_JSON = MediaType.APPLICATION_JSON;
    public static final String MEDIA_APPLICATION_PDF = "application/pdf";
    /** The Internet Media type for PROV-XML
     * @see <a href="http://www.w3.org/TR/prov-xml/#media-type">Media Type for PROV-XML</a> */
    public static final String MEDIA_APPLICATION_PROVENANCE_XML = "application/provenance+xml";
    /** The Internet Media type for RDF/XML
     * @see <a href="http://www.w3.org/TR/REC-rdf-syntax/#section-MIME-Type">Media Type for RDF/XML</a> */
    public static final String MEDIA_APPLICATION_RDF_XML = "application/rdf+xml";
    public static final String MEDIA_APPLICATION_XML = MediaType.APPLICATION_XML;
    /** The Internet Media type for TRIG
     * @see <a href="http://www.w3.org/TR/trig/#sec-mediaReg">Media Type for TRIG</a> */
    public static final String MEDIA_APPLICATION_TRIG = "application/trig";
    public static final String MEDIA_IMAGE_JPEG = "image/jpeg";
    /** The Internet Media type for SVG
     * @see <a href="http://www.w3.org/TR/SVGTiny12/mimereg.html#mime-registration">Media Type for SVG</a> */
    public static final String MEDIA_IMAGE_SVG_XML = "image/svg+xml";
    public static final String MEDIA_TEXT_HTML = MediaType.TEXT_HTML;
    public static final String MEDIA_TEXT_PLAIN = MediaType.TEXT_PLAIN;
    /** The Internet Media type for PROV-N
     * @see <a href="http://www.w3.org/TR/prov-n/#media-type">Media Type for PROV-N</a> */
    public static final String MEDIA_TEXT_PROVENANCE_NOTATION = "text/provenance-notation";
    /** The Internet Media type for Turtle
     * @see <a href="http://www.w3.org/TR/turtle/#sec-mediaReg">Media Type for Turtle</a> */
    public static final String MEDIA_TEXT_TURTLE = "text/turtle";
    /** The Internet Media type for Graphviz.
     * @see <a href="http://www.iana.org/assignments/media-types/text/vnd.graphviz">Media Type for Graphviz</a> */
    public static final String MEDIA_TEXT_VND_GRAPHVIZ = "text/vnd.graphviz";
    public static final String MEDIA_TEXT_XML = MediaType.TEXT_XML;
    /**
     * All Media Types accepted as input for PROV. 
     */
    public final static String[] ALL_PROV_MEDIA_TYPES = new String[] {
	    MEDIA_TEXT_TURTLE, MEDIA_TEXT_PROVENANCE_NOTATION,
	    MEDIA_APPLICATION_PROVENANCE_XML, MEDIA_APPLICATION_TRIG,
	    MEDIA_APPLICATION_RDF_XML, MEDIA_APPLICATION_JSON,
	    MEDIA_APPLICATION_PDF };

}