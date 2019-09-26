package org.openprovenance.prov.interop;

public class Formats {

    /** An enumerated type for all the PROV serializations supported by ProvToolbox.
     * Some of these serializations can be input, output, or both. */

    static public enum ProvFormat {
        PROVN, XML, TURTLE, RDFXML, TRIG, JSON, JSONLD, DOT, JPEG, PNG, SVG, PDF
    }

    static public enum ProvFormatType {
        INPUT, OUTPUT, INPUTOUTPUT
    }

}
