package org.openprovenance.prov.template.log2prov;

/** A streaming interface for a database backend, which accumulates (SQL) inserts statements, by means of its process method. */

public interface RecordProcessor {
    void process(String methodName, String args);

    void insertHead(String finalMethodName, String tmp);

    StringBuffer getDocument();

    void initDb();

    void end();
}
