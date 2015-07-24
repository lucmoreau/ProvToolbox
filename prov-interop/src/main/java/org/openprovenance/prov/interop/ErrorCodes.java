package org.openprovenance.prov.interop;

/** Error Codes returned by provconvert
 * @author lavm
 *
 */
public interface ErrorCodes {
    static final int STATUS_OK=0;
    static final int STATUS_PARSING_FAIL=1;
    static final int STATUS_NO_INPUT=2;
    static final int STATUS_NO_OUTPUT_OR_COMPARISON=3;
    static final int STATUS_COMPARE_NO_ARG1=4;
    static final int STATUS_COMPARE_NO_ARG2=5;
    static final int STATUS_COMPARE_DIFFERENT=6;

}
