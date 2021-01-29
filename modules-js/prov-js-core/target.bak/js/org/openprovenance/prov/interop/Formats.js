/* Generated from Java with JSweet 3.0.0 - http://www.jsweet.org */
var org;
(function (org) {
    var openprovenance;
    (function (openprovenance) {
        var prov;
        (function (prov) {
            var interop;
            (function (interop) {
                class Formats {
                }
                interop.Formats = Formats;
                Formats["__class"] = "org.openprovenance.prov.interop.Formats";
                (function (Formats) {
                    /**
                     * An enumerated type for all the PROV serializations supported by ProvToolbox.
                     * Some of these serializations can be input, output, or both.
                     * @enum
                     * @property {org.openprovenance.prov.interop.Formats.ProvFormat} PROVN
                     * @property {org.openprovenance.prov.interop.Formats.ProvFormat} XML
                     * @property {org.openprovenance.prov.interop.Formats.ProvFormat} TURTLE
                     * @property {org.openprovenance.prov.interop.Formats.ProvFormat} RDFXML
                     * @property {org.openprovenance.prov.interop.Formats.ProvFormat} TRIG
                     * @property {org.openprovenance.prov.interop.Formats.ProvFormat} JSON
                     * @property {org.openprovenance.prov.interop.Formats.ProvFormat} JSONLD
                     * @property {org.openprovenance.prov.interop.Formats.ProvFormat} DOT
                     * @property {org.openprovenance.prov.interop.Formats.ProvFormat} JPEG
                     * @property {org.openprovenance.prov.interop.Formats.ProvFormat} PNG
                     * @property {org.openprovenance.prov.interop.Formats.ProvFormat} SVG
                     * @property {org.openprovenance.prov.interop.Formats.ProvFormat} PDF
                     * @class
                     */
                    let ProvFormat;
                    (function (ProvFormat) {
                        ProvFormat[ProvFormat["PROVN"] = 0] = "PROVN";
                        ProvFormat[ProvFormat["XML"] = 1] = "XML";
                        ProvFormat[ProvFormat["TURTLE"] = 2] = "TURTLE";
                        ProvFormat[ProvFormat["RDFXML"] = 3] = "RDFXML";
                        ProvFormat[ProvFormat["TRIG"] = 4] = "TRIG";
                        ProvFormat[ProvFormat["JSON"] = 5] = "JSON";
                        ProvFormat[ProvFormat["JSONLD"] = 6] = "JSONLD";
                        ProvFormat[ProvFormat["DOT"] = 7] = "DOT";
                        ProvFormat[ProvFormat["JPEG"] = 8] = "JPEG";
                        ProvFormat[ProvFormat["PNG"] = 9] = "PNG";
                        ProvFormat[ProvFormat["SVG"] = 10] = "SVG";
                        ProvFormat[ProvFormat["PDF"] = 11] = "PDF";
                    })(ProvFormat = Formats.ProvFormat || (Formats.ProvFormat = {}));
                    let ProvFormatType;
                    (function (ProvFormatType) {
                        ProvFormatType[ProvFormatType["INPUT"] = 0] = "INPUT";
                        ProvFormatType[ProvFormatType["OUTPUT"] = 1] = "OUTPUT";
                        ProvFormatType[ProvFormatType["INPUTOUTPUT"] = 2] = "INPUTOUTPUT";
                    })(ProvFormatType = Formats.ProvFormatType || (Formats.ProvFormatType = {}));
                })(Formats = interop.Formats || (interop.Formats = {}));
            })(interop = prov.interop || (prov.interop = {}));
        })(prov = openprovenance.prov || (openprovenance.prov = {}));
    })(openprovenance = org.openprovenance || (org.openprovenance = {}));
})(org || (org = {}));
