package org.openprovenance.prov.log;

import org.apache.log4j.Level;

/** Adapted from http://jaitechwriteups.blogspot.co.uk/2006/07/create-your-own-logging-level-in-log4j.html */

public class ProvLevel extends Level {

    public static final String PROV_STRING = "PROV";

    /**
     * Value of Provenance trace level. This value is lesser than
     * {@link org.apache.log4j.Priority#INFO_INT} and higher than
     * {@link org.apache.log4j.Level#DEBUG_INT}
     */
    public static final int PROVENANCE_INT = INFO_INT - 10;

    /**
     * {@link Level} representing my log level
     */
    public static final Level PROV = new ProvLevel(PROVENANCE_INT, PROV_STRING, 6);

    /**
     * Constructor
     * 
     * @param arg0
     * @param arg1
     * @param arg2
     */
    protected ProvLevel(int arg0, String arg1, int arg2) {
	super(arg0, arg1, arg2);

    }

    /**
     * Checks whether sArg is "PROV" level. If yes then returns
     * {@link ProvLevel#PROV}, else calls
     * {@link ProvLevel#toLevel(String, Level)} passing it
     * {@link Level#DEBUG} as the defaultLevel.
     * 
     * @see Level#toLevel(java.lang.String)
     * @see Level#toLevel(java.lang.String, org.apache.log4j.Level)
     * 
     */
    public static Level toLevel(String sArg) {
	if (sArg != null && sArg.toUpperCase().equals(PROV_STRING)) {
	    return PROV;
	}
	return (Level) toLevel(sArg, Level.DEBUG);
    }

    /**
     * Checks whether val is {@link ProvLevel #PROVENANCE_INT}. If yes then
     * returns {@link ProvLevel #PROV}, else calls
     * {@link ProvLevel #toLevel(int, Level)} passing it {@link Level#DEBUG}
     * as the defaultLevel
     * 
     * @see Level#toLevel(int)
     * @see Level#toLevel(int, org.apache.log4j.Level)
     * 
     */
    public static Level toLevel(int val) {
	if (val == PROVENANCE_INT) {
	    return PROV;
	}
	return (Level) toLevel(val, Level.DEBUG);
    }

    /**
     * Checks whether val is {@link ProvLevel #PROVENANCE_INT}. If yes then
     * returns {@link ProvLevel #PROV}, else calls
     * {@link Level#toLevel(int, org.apache.log4j.Level)}
     * 
     * @see Level#toLevel(int, org.apache.log4j.Level)
     */
    public static Level toLevel(int val, Level defaultLevel) {
	if (val == PROVENANCE_INT) {
	    return PROV;
	}
	return Level.toLevel(val, defaultLevel);
    }

    /**
     * Checks whether sArg is "MY_TRACE" level. If yes then returns
     * {@link ProvLevel #PROV}, else calls
     * {@link Level#toLevel(java.lang.String, org.apache.log4j.Level)}
     * 
     * @see Level#toLevel(java.lang.String, org.apache.log4j.Level)
     */
    public static Level toLevel(String sArg, Level defaultLevel) {
	if (sArg != null && sArg.toUpperCase().equals(PROV_STRING)) {
	    return PROV;
	}
	return Level.toLevel(sArg, defaultLevel);
    }

}
