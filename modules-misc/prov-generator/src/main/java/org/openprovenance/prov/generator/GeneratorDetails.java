package org.openprovenance.prov.generator;

import org.openprovenance.prov.model.Document;


public class GeneratorDetails {
    
    int noOfNodes; 
    int noOfEdges;
    String firstNode;
    String namespace;
    Long seed;
    String term;
    Document doc;
    
    public GeneratorDetails(int noOfNodes, int noOfEdges, String firstNode,
                            String namespace, Long seed, String term) {
        this.noOfNodes=noOfNodes;
        this.noOfEdges=noOfEdges;
        this.firstNode=firstNode;
        this.namespace=namespace;
        this.seed=seed;
        this.term=term;
    }   

    public int getNumberOfNodes() {
        return noOfNodes;
    }

    public int getNumberOfEdges() {
        return noOfEdges;
    }

    public String getFirstNode() {
        return firstNode;
    }

    public String getNamespace() {
        return namespace;
    }
    

    public Long getSeed() {
        return seed;
    }

    public String getTermToAnnotate() {
        return term;
    }
    
    public Document getDocument () {
        return doc;
    }
    
    public void setDocument(Document doc) {
        this.doc=doc;
    }
    
    public String toString() {
        return noOfNodes + "," + noOfEdges + "," + firstNode + "," +
                 namespace + "," + seed + "," + term;
    }

}
