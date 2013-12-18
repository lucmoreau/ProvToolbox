package org.openprovenance.prov.model;

import java.util.List;

import org.openprovenance.prov.model.Namespace;

/**
 * Interface for a PROV Document.
 * <p><a href="http://www.w3.org/TR/prov-dm/#concept-bundle">PROV-N Definition for Document</a>: 
 * A document is a house-keeping construct of PROV-N capable of packaging up PROV-N expressions 
 * and namespace declarations. A document forms a self-contained package of provenance descriptions 
 * for the purpose of exchanging them. A document may be used to package up PROV-N expressions in 
 * response to a request for the provenance of something.
 * 
 *  
 * <p><span class="strong">Relevant Factory Methods:</span>
 * <ul>
 * <li> {@link ProvFactory#newDocument()}
 * <li> {@link ProvFactory#newDocument(Namespace, java.util.Collection, java.util.Collection)}
 * <li> {@link ProvFactory#newDocument(java.util.Collection, java.util.Collection, java.util.Collection, java.util.Collection)}
 * </ul>
 * 
 * 
 * 
 * <p><span class="strong">Schema Definition:</span>
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Document">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;choice maxOccurs="unbounded">
 *           &lt;group ref="{http://www.w3.org/ns/prov#}documentElements"/>
 *           &lt;element name="bundleContent" type="{http://www.w3.org/ns/prov#}NamedBundle"/>
 *         &lt;/choice>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>

 * @author lavm
 * 
 * <p> 
 * @see <a href="http://www.w3.org/TR/prov-n/#document">PROV-N Document</a>
 * @see <a href="http://www.w3.org/TR/prov-xml/#term-Document">PROV-XML Document</a>

 *
 */
public interface Document {

    List<StatementOrBundle> getStatementOrBundle();

    void setNamespace(Namespace ns);
    
    Namespace getNamespace();

}
