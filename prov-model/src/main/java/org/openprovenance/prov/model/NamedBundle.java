package org.openprovenance.prov.model;

import java.util.List;

/**
 * <p>Interface for a PROV Bundle.
 * <p><a href="http://www.w3.org/TR/prov-dm/#concept-bundle">PROV-DM Definition for Bundle</a>: 
 * A bundle is a named set of provenance descriptions, and is itself an entity, so allowing provenance of provenance to be expressed.

 * <p><span class="strong">Schema Definition:</span>
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="NamedBundle">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence maxOccurs="unbounded">
 *         &lt;group ref="{http://www.w3.org/ns/prov#}documentElements"/>
 *       &lt;/sequence>
 *       &lt;attribute ref="{http://www.w3.org/ns/prov#}id"/>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 * 
 * <p> 
 * @see <a href="http://www.w3.org/TR/prov-dm/#term-bundle">PROV-DM Bundle</a>
 * @see <a href="http://www.w3.org/TR/prov-o/#Bundle">PROV-O Bundle</a>
 * @see <a href="http://www.w3.org/TR/prov-n/#expression-bundle-constructor">PROV-N Bundle</a>
 * @see <a href="http://www.w3.org/TR/prov-xml/#term-Bundle">PROV-XML Bundle</a>
 * @see <a href="http://www.w3.org/Submission/2013/SUBM-prov-json-20130424/#bundle">PROV-JSON Bundle</a>
 *  
 * 

 * 
 * @author lavm
 *
 */
public interface NamedBundle extends Identifiable, StatementOrBundle {

    List<Statement> getStatement();

    void setNamespace(Namespace namespaces);

    Namespace getNamespace();

}
