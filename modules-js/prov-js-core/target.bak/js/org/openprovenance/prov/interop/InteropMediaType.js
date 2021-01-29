/* Generated from Java with JSweet 3.0.0 - http://www.jsweet.org */
var org;
(function (org) {
    var openprovenance;
    (function (openprovenance) {
        var prov;
        (function (prov) {
            var interop;
            (function (interop) {
                let InteropMediaType;
                (function (InteropMediaType) {
                    /**
                     * The recommended extension for DOT files (Graphviz).
                     * @see <a href="http://www.iana.org/assignments/media-types/text/vnd.graphviz">Media Type for Graphviz</a>
                     */
                    InteropMediaType.EXTENSION_DOT = "dot";
                    InteropMediaType.EXTENSION_JPEG = "jpeg";
                    InteropMediaType.EXTENSION_JPG = "jpg";
                    InteropMediaType.EXTENSION_PNG = "png";
                    InteropMediaType.EXTENSION_JSON = "json";
                    InteropMediaType.EXTENSION_JSONLD = "jsonld";
                    InteropMediaType.EXTENSION_PDF = "pdf";
                    /**
                     * The recommended extension for PROV-N files.
                     * @see <a href="http://www.w3.org/TR/prov-n/#media-type">Media Type for PROV-N</a>
                     */
                    InteropMediaType.EXTENSION_PROVN = "provn";
                    /**
                     * The recommended extension for PROV-XML files.
                     * @see <a href="http://www.w3.org/TR/prov-xml/#media-type">Media Type for PROV-XML</a>
                     */
                    InteropMediaType.EXTENSION_PROVX = "provx";
                    /**
                     * The recommended extension for RDF/XML files.
                     * @see <a href="http://www.w3.org/TR/REC-rdf-syntax/#section-MIME-Type">Media Type for RDF/XML</a>
                     */
                    InteropMediaType.EXTENSION_RDF = "rdf";
                    /**
                     * The extension for SCG files (Note, where is this recommended?).
                     * @see <a href="http://www.w3.org/TR/SVGTiny12/mimereg.html#mime-registration">Media Type for SVG</a>
                     */
                    InteropMediaType.EXTENSION_SVG = "svg";
                    /**
                     * The recommended extension for TRIG files.
                     * @see <a href="http://www.w3.org/TR/trig/#sec-mediaReg">Media Type for TRIG</a>
                     */
                    InteropMediaType.EXTENSION_TRIG = "trig";
                    /**
                     * The recommended extension for Turtle files.
                     * @see <a href="http://www.w3.org/TR/turtle/#sec-mediaReg">Media Type for Turtle</a>
                     */
                    InteropMediaType.EXTENSION_TTL = "ttl";
                    /**
                     * The extension for XML files.
                     */
                    InteropMediaType.EXTENSION_XML = "xml";
                    InteropMediaType.MEDIA_APPLICATION_FORM_URLENCODED = "application/x-www-form-urlencoded";
                    InteropMediaType.MEDIA_APPLICATION_JSONLD = "application/ld+json";
                    InteropMediaType.MEDIA_APPLICATION_JSON = "application/json";
                    InteropMediaType.MEDIA_APPLICATION_PDF = "application/pdf";
                    /**
                     * The Internet Media type for PROV-XML
                     * @see <a href="http://www.w3.org/TR/prov-xml/#media-type">Media Type for PROV-XML</a>
                     */
                    InteropMediaType.MEDIA_APPLICATION_PROVENANCE_XML = "application/provenance+xml";
                    /**
                     * The Internet Media type for RDF/XML
                     * @see <a href="http://www.w3.org/TR/REC-rdf-syntax/#section-MIME-Type">Media Type for RDF/XML</a>
                     */
                    InteropMediaType.MEDIA_APPLICATION_RDF_XML = "application/rdf+xml";
                    InteropMediaType.MEDIA_APPLICATION_XML = "application/xml";
                    /**
                     * The Internet Media type for TRIG
                     * @see <a href="http://www.w3.org/TR/trig/#sec-mediaReg">Media Type for TRIG</a>
                     */
                    InteropMediaType.MEDIA_APPLICATION_TRIG = "application/trig";
                    InteropMediaType.MEDIA_IMAGE_JPEG = "image/jpeg";
                    InteropMediaType.MEDIA_IMAGE_PNG = "image/png";
                    /**
                     * The Internet Media type for SVG
                     * @see <a href="http://www.w3.org/TR/SVGTiny12/mimereg.html#mime-registration">Media Type for SVG</a>
                     */
                    InteropMediaType.MEDIA_IMAGE_SVG_XML = "image/svg+xml";
                    InteropMediaType.MEDIA_TEXT_HTML = "text/html";
                    InteropMediaType.MEDIA_TEXT_PLAIN = "text/plain";
                    /**
                     * The Internet Media type for PROV-N
                     * @see <a href="http://www.w3.org/TR/prov-n/#media-type">Media Type for PROV-N</a>
                     */
                    InteropMediaType.MEDIA_TEXT_PROVENANCE_NOTATION = "text/provenance-notation";
                    /**
                     * The Internet Media type for Turtle
                     * @see <a href="http://www.w3.org/TR/turtle/#sec-mediaReg">Media Type for Turtle</a>
                     */
                    InteropMediaType.MEDIA_TEXT_TURTLE = "text/turtle";
                    /**
                     * The Internet Media type for Graphviz.
                     * @see <a href="http://www.iana.org/assignments/media-types/text/vnd.graphviz">Media Type for Graphviz</a>
                     */
                    InteropMediaType.MEDIA_TEXT_VND_GRAPHVIZ = "text/vnd.graphviz";
                    InteropMediaType.MEDIA_TEXT_XML = "text/xml";
                    InteropMediaType.MEDIA_TEXT_CSV = "text/csv";
                    function ALL_PROV_INPUT_MEDIA_TYPES_$LI$() { if (InteropMediaType.ALL_PROV_INPUT_MEDIA_TYPES == null) {
                        InteropMediaType.ALL_PROV_INPUT_MEDIA_TYPES = [InteropMediaType.MEDIA_TEXT_TURTLE, InteropMediaType.MEDIA_TEXT_PROVENANCE_NOTATION, InteropMediaType.MEDIA_APPLICATION_PROVENANCE_XML, InteropMediaType.MEDIA_APPLICATION_TRIG, InteropMediaType.MEDIA_APPLICATION_RDF_XML, InteropMediaType.MEDIA_APPLICATION_JSON, InteropMediaType.MEDIA_APPLICATION_PDF];
                    } return InteropMediaType.ALL_PROV_INPUT_MEDIA_TYPES; }
                    InteropMediaType.ALL_PROV_INPUT_MEDIA_TYPES_$LI$ = ALL_PROV_INPUT_MEDIA_TYPES_$LI$;
                    ;
                    function ALL_PROV_OUTPUT_MEDIA_TYPES_$LI$() { if (InteropMediaType.ALL_PROV_OUTPUT_MEDIA_TYPES == null) {
                        InteropMediaType.ALL_PROV_OUTPUT_MEDIA_TYPES = [InteropMediaType.MEDIA_TEXT_TURTLE, InteropMediaType.MEDIA_TEXT_PROVENANCE_NOTATION, InteropMediaType.MEDIA_APPLICATION_PROVENANCE_XML, InteropMediaType.MEDIA_APPLICATION_TRIG, InteropMediaType.MEDIA_APPLICATION_RDF_XML, InteropMediaType.MEDIA_APPLICATION_JSON, InteropMediaType.MEDIA_IMAGE_SVG_XML, InteropMediaType.MEDIA_APPLICATION_PDF];
                    } return InteropMediaType.ALL_PROV_OUTPUT_MEDIA_TYPES; }
                    InteropMediaType.ALL_PROV_OUTPUT_MEDIA_TYPES_$LI$ = ALL_PROV_OUTPUT_MEDIA_TYPES_$LI$;
                    ;
                    InteropMediaType.MEDIA_APPLICATION_SQL = "application/sql";
                })(InteropMediaType = interop.InteropMediaType || (interop.InteropMediaType = {}));
            })(interop = prov.interop || (prov.interop = {}));
        })(prov = openprovenance.prov || (openprovenance.prov = {}));
    })(openprovenance = org.openprovenance || (org.openprovenance = {}));
})(org || (org = {}));
