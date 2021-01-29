/* Generated from Java with JSweet 3.0.0 - http://www.jsweet.org */
namespace org.openprovenance.prov.interop {
    /**
     * Definition of all Media Types and file Extensions supported by ProvToolbox.
     * @class
     */
    export interface InteropMediaType {    }

    export namespace InteropMediaType {

        /**
         * The recommended extension for DOT files (Graphviz).
         * @see <a href="http://www.iana.org/assignments/media-types/text/vnd.graphviz">Media Type for Graphviz</a>
         */
        export const EXTENSION_DOT: string = "dot";

        export const EXTENSION_JPEG: string = "jpeg";

        export const EXTENSION_JPG: string = "jpg";

        export const EXTENSION_PNG: string = "png";

        export const EXTENSION_JSON: string = "json";

        export const EXTENSION_JSONLD: string = "jsonld";

        export const EXTENSION_PDF: string = "pdf";

        /**
         * The recommended extension for PROV-N files.
         * @see <a href="http://www.w3.org/TR/prov-n/#media-type">Media Type for PROV-N</a>
         */
        export const EXTENSION_PROVN: string = "provn";

        /**
         * The recommended extension for PROV-XML files.
         * @see <a href="http://www.w3.org/TR/prov-xml/#media-type">Media Type for PROV-XML</a>
         */
        export const EXTENSION_PROVX: string = "provx";

        /**
         * The recommended extension for RDF/XML files.
         * @see <a href="http://www.w3.org/TR/REC-rdf-syntax/#section-MIME-Type">Media Type for RDF/XML</a>
         */
        export const EXTENSION_RDF: string = "rdf";

        /**
         * The extension for SCG files (Note, where is this recommended?).
         * @see <a href="http://www.w3.org/TR/SVGTiny12/mimereg.html#mime-registration">Media Type for SVG</a>
         */
        export const EXTENSION_SVG: string = "svg";

        /**
         * The recommended extension for TRIG files.
         * @see <a href="http://www.w3.org/TR/trig/#sec-mediaReg">Media Type for TRIG</a>
         */
        export const EXTENSION_TRIG: string = "trig";

        /**
         * The recommended extension for Turtle files.
         * @see <a href="http://www.w3.org/TR/turtle/#sec-mediaReg">Media Type for Turtle</a>
         */
        export const EXTENSION_TTL: string = "ttl";

        /**
         * The extension for XML files.
         */
        export const EXTENSION_XML: string = "xml";

        export const MEDIA_APPLICATION_FORM_URLENCODED: string = "application/x-www-form-urlencoded";

        export const MEDIA_APPLICATION_JSONLD: string = "application/ld+json";

        export const MEDIA_APPLICATION_JSON: string = "application/json";

        export const MEDIA_APPLICATION_PDF: string = "application/pdf";

        /**
         * The Internet Media type for PROV-XML
         * @see <a href="http://www.w3.org/TR/prov-xml/#media-type">Media Type for PROV-XML</a>
         */
        export const MEDIA_APPLICATION_PROVENANCE_XML: string = "application/provenance+xml";

        /**
         * The Internet Media type for RDF/XML
         * @see <a href="http://www.w3.org/TR/REC-rdf-syntax/#section-MIME-Type">Media Type for RDF/XML</a>
         */
        export const MEDIA_APPLICATION_RDF_XML: string = "application/rdf+xml";

        export const MEDIA_APPLICATION_XML: string = "application/xml";

        /**
         * The Internet Media type for TRIG
         * @see <a href="http://www.w3.org/TR/trig/#sec-mediaReg">Media Type for TRIG</a>
         */
        export const MEDIA_APPLICATION_TRIG: string = "application/trig";

        export const MEDIA_IMAGE_JPEG: string = "image/jpeg";

        export const MEDIA_IMAGE_PNG: string = "image/png";

        /**
         * The Internet Media type for SVG
         * @see <a href="http://www.w3.org/TR/SVGTiny12/mimereg.html#mime-registration">Media Type for SVG</a>
         */
        export const MEDIA_IMAGE_SVG_XML: string = "image/svg+xml";

        export const MEDIA_TEXT_HTML: string = "text/html";

        export const MEDIA_TEXT_PLAIN: string = "text/plain";

        /**
         * The Internet Media type for PROV-N
         * @see <a href="http://www.w3.org/TR/prov-n/#media-type">Media Type for PROV-N</a>
         */
        export const MEDIA_TEXT_PROVENANCE_NOTATION: string = "text/provenance-notation";

        /**
         * The Internet Media type for Turtle
         * @see <a href="http://www.w3.org/TR/turtle/#sec-mediaReg">Media Type for Turtle</a>
         */
        export const MEDIA_TEXT_TURTLE: string = "text/turtle";

        /**
         * The Internet Media type for Graphviz.
         * @see <a href="http://www.iana.org/assignments/media-types/text/vnd.graphviz">Media Type for Graphviz</a>
         */
        export const MEDIA_TEXT_VND_GRAPHVIZ: string = "text/vnd.graphviz";

        export const MEDIA_TEXT_XML: string = "text/xml";

        export const MEDIA_TEXT_CSV: string = "text/csv";

        /**
         * All Media Types accepted as input for PROV.
         */
        export let ALL_PROV_INPUT_MEDIA_TYPES: string[]; export function ALL_PROV_INPUT_MEDIA_TYPES_$LI$(): string[] { if (InteropMediaType.ALL_PROV_INPUT_MEDIA_TYPES == null) { InteropMediaType.ALL_PROV_INPUT_MEDIA_TYPES = [InteropMediaType.MEDIA_TEXT_TURTLE, InteropMediaType.MEDIA_TEXT_PROVENANCE_NOTATION, InteropMediaType.MEDIA_APPLICATION_PROVENANCE_XML, InteropMediaType.MEDIA_APPLICATION_TRIG, InteropMediaType.MEDIA_APPLICATION_RDF_XML, InteropMediaType.MEDIA_APPLICATION_JSON, InteropMediaType.MEDIA_APPLICATION_PDF]; }  return InteropMediaType.ALL_PROV_INPUT_MEDIA_TYPES; };

        export let ALL_PROV_OUTPUT_MEDIA_TYPES: string[]; export function ALL_PROV_OUTPUT_MEDIA_TYPES_$LI$(): string[] { if (InteropMediaType.ALL_PROV_OUTPUT_MEDIA_TYPES == null) { InteropMediaType.ALL_PROV_OUTPUT_MEDIA_TYPES = [InteropMediaType.MEDIA_TEXT_TURTLE, InteropMediaType.MEDIA_TEXT_PROVENANCE_NOTATION, InteropMediaType.MEDIA_APPLICATION_PROVENANCE_XML, InteropMediaType.MEDIA_APPLICATION_TRIG, InteropMediaType.MEDIA_APPLICATION_RDF_XML, InteropMediaType.MEDIA_APPLICATION_JSON, InteropMediaType.MEDIA_IMAGE_SVG_XML, InteropMediaType.MEDIA_APPLICATION_PDF]; }  return InteropMediaType.ALL_PROV_OUTPUT_MEDIA_TYPES; };

        export const MEDIA_APPLICATION_SQL: string = "application/sql";
    }

}

