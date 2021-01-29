/* Generated from Java with JSweet 3.0.0 - http://www.jsweet.org */
var org;
(function (org) {
    var openprovenance;
    (function (openprovenance) {
        var prov;
        (function (prov) {
            var model;
            (function (model) {
                let NamespacePrefixMapper;
                (function (NamespacePrefixMapper) {
                    NamespacePrefixMapper.PROV_EXT_NS = "http://openprovenance.org/prov/extension#";
                    NamespacePrefixMapper.PROV_NS = "http://www.w3.org/ns/prov#";
                    NamespacePrefixMapper.XSI_NS = "http://www.w3.org/2001/XMLSchema-instance";
                    NamespacePrefixMapper.XSD_NS = "http://www.w3.org/2001/XMLSchema#";
                    NamespacePrefixMapper.PRINTER_NS = "http://openprovenance.org/model/opmPrinterConfig";
                    NamespacePrefixMapper.XML_NS = "http://www.w3.org/XML/1998/namespace";
                    NamespacePrefixMapper.PROV_PREFIX = "prov";
                    NamespacePrefixMapper.XSD_PREFIX = "xsd";
                    NamespacePrefixMapper.RDF_NS = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
                    NamespacePrefixMapper.RDF_PREFIX = "rdf";
                    NamespacePrefixMapper.RDFS_NS = "http://www.w3.org/2000/01/rdf-schema#";
                    NamespacePrefixMapper.RDFS_PREFIX = "rdfs";
                    NamespacePrefixMapper.BOOK_PREFIX = "bk";
                    NamespacePrefixMapper.BOOK_NS = "http://www.provbook.org/ns/#";
                    NamespacePrefixMapper.SHARED_PROV_TOOLBOX_PREFIX = "http://openprovenance.org/provtoolbox/";
                    function TOOLBOX_NS_$LI$() { if (NamespacePrefixMapper.TOOLBOX_NS == null) {
                        NamespacePrefixMapper.TOOLBOX_NS = NamespacePrefixMapper.SHARED_PROV_TOOLBOX_PREFIX + "ns#";
                    } return NamespacePrefixMapper.TOOLBOX_NS; }
                    NamespacePrefixMapper.TOOLBOX_NS_$LI$ = TOOLBOX_NS_$LI$;
                    ;
                    NamespacePrefixMapper.TOOLBOX_PREFIX = "box";
                    function DOT_NS_$LI$() { if (NamespacePrefixMapper.DOT_NS == null) {
                        NamespacePrefixMapper.DOT_NS = NamespacePrefixMapper.SHARED_PROV_TOOLBOX_PREFIX + "dot/ns#";
                    } return NamespacePrefixMapper.DOT_NS; }
                    NamespacePrefixMapper.DOT_NS_$LI$ = DOT_NS_$LI$;
                    ;
                    NamespacePrefixMapper.DOT_PREFIX = "dot";
                    function SUMMARY_NS_$LI$() { if (NamespacePrefixMapper.SUMMARY_NS == null) {
                        NamespacePrefixMapper.SUMMARY_NS = NamespacePrefixMapper.SHARED_PROV_TOOLBOX_PREFIX + "summary/ns#";
                    } return NamespacePrefixMapper.SUMMARY_NS; }
                    NamespacePrefixMapper.SUMMARY_NS_$LI$ = SUMMARY_NS_$LI$;
                    ;
                    NamespacePrefixMapper.SUMMARY_PREFIX = "sum";
                })(NamespacePrefixMapper = model.NamespacePrefixMapper || (model.NamespacePrefixMapper = {}));
            })(model = prov.model || (prov.model = {}));
        })(prov = openprovenance.prov || (openprovenance.prov = {}));
    })(openprovenance = org.openprovenance || (org.openprovenance = {}));
})(org || (org = {}));
