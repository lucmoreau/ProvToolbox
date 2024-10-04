package org.openprovenance.prov.sparql;

public class Config {

    private String infile;
    private String outfile;
    private String informat;
    private String outformat;
    private String query;
    private String ontology;

    public void setInfile(String infile) {
        this.infile = infile;
    }

    public void setOutfile(String outfile) {
        this.outfile = outfile;
    }

    public void setInformat(String informat) {
        this.informat = informat;
    }

    public void setOutformat(String outformat) {
        this.outformat = outformat;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public void setOntology(String ontology) {
        this.ontology = ontology;
    }

    public String getInfile() {
        return infile;
    }

    public String getOutfile() {
        return outfile;
    }

    public String getInformat() {
        return informat;
    }

    public String getOutformat() {
        return outformat;
    }

    public String getQuery() {
        return query;
    }

    public String getOntology() {
        return ontology;
    }
}
