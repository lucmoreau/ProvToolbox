/* Generated from Java with JSweet 3.0.0 - http://www.jsweet.org */
namespace org.openprovenance.prov.model {
    /**
     * <p>Interface for a PROV Bundle.
     * <p><a href="http://www.w3.org/TR/prov-dm/#concept-bundle">PROV-DM Definition for Bundle</a>:
     * A bundle is a named set of provenance descriptions, and is itself an entity, so allowing provenance of provenance to be expressed.
     * 
     * <p><span class="strong">Schema Definition:</span>
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType name="NamedBundle"&gt;
     * &lt;complexContent&gt;
     * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     * &lt;sequence maxOccurs="unbounded"&gt;
     * &lt;group ref="{http://www.w3.org/ns/prov#}documentElements"/&gt;
     * &lt;/sequence&gt;
     * &lt;attribute ref="{http://www.w3.org/ns/prov#}id"/&gt;
     * &lt;/restriction&gt;
     * &lt;/complexContent&gt;
     * &lt;/complexType&gt;
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
     * 
     * @author lavm
     * @class
     */
    export interface Bundle extends org.openprovenance.prov.model.Identifiable, org.openprovenance.prov.model.StatementOrBundle {
        getStatement(): Array<org.openprovenance.prov.model.Statement>;

        setNamespace(namespaces: org.openprovenance.prov.model.Namespace);

        getNamespace(): org.openprovenance.prov.model.Namespace;
    }
}

