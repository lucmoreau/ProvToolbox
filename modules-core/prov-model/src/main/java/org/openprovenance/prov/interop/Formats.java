package org.openprovenance.prov.interop;

public class Formats {

    /** An enumerated type for all the PROV serializations supported by ProvToolbox.
     * Some of these serializations can be input, output, or both. */

    static public enum ProvFormat {
        PROVN, XML, TURTLE, RDFXML, TRIG, // TURTLE, RDFXML, TRIG no longer supported by provtoolbox, lead to exceptions at invocation time
        JSON, JSONLD, DOT, JPEG, PNG, SVG, PDF
    }

    static public enum ProvFormatType {
        INPUT, OUTPUT, INPUTOUTPUT
    }

}
