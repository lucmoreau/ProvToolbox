package org.openprovenance.prov.log;

import org.apache.logging.log4j.Level;

/** Adapted from http://jaitechwriteups.blogspot.co.uk/2006/07/create-your-own-logging-level-in-log4j.html */

public class ProvLevel  {

    public static final String PROV_STRING = "PROV";

    /**
     * Value of Provenance trace level. This value is lesser than
     */
    public static final int PROVENANCE_INT = Level.INFO.intLevel() - 10;

    /**
     * {@link Level} representing my log level
     */
    public static final Level PROV =  Level.forName(PROV_STRING,PROVENANCE_INT);

}
