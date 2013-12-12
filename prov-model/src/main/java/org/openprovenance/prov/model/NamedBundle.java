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
 * @author lavm
 *
 */
public interface NamedBundle extends Identifiable, StatementOrBundle {

    List<Statement> getStatement();

    void setNamespace(Namespace namespaces);

    Namespace getNamespace();

}
