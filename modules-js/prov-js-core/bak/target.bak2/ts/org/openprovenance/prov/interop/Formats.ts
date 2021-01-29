/* Generated from Java with JSweet 3.0.0 - http://www.jsweet.org */
namespace org.openprovenance.prov.interop {
    export class Formats {    }
    Formats["__class"] = "org.openprovenance.prov.interop.Formats";


    export namespace Formats {

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
        export enum ProvFormat {
            PROVN, XML, TURTLE, RDFXML, TRIG, JSON, JSONLD, DOT, JPEG, PNG, SVG, PDF
        }

        export enum ProvFormatType {
            INPUT, OUTPUT, INPUTOUTPUT
        }
    }

}

