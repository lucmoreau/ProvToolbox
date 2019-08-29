package org.openprovenance.prov.core.vanilla;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.openprovenance.apache.commons.lang.builder.*;
import org.openprovenance.prov.model.QualifiedName;

import java.util.LinkedList;
import java.util.List;

public class HadMember implements org.openprovenance.prov.model.HadMember, Equals, HashCode, ToString {


    protected List<QualifiedName> entity=new LinkedList<>();
    protected QualifiedName collection;




    protected HadMember() {}



    public HadMember(QualifiedName collection,
                     List<QualifiedName> entity) {
        this.collection = collection;
        this.entity = entity;
    }



    /**
     * Get an identifier for the collection whose member is asserted
     *
     * @return QualifiedName for the collection
     * @see <a href="http://www.w3.org/TR/prov-dm/#membership.collection">membership collection</a>
     */
    @Override
    public QualifiedName getCollection() {
        return collection;
    }

    /**
     * Get the list of identifiers of entities that are member of the collection.
     *
     * @return a list of {@link QualifiedName}
     *
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the entity property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getEntity().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link QualifiedName }
     * @see <a href="http://www.w3.org/TR/prov-dm/#membership.entity">membership entity</a>
     */
    @Override
    public List<QualifiedName> getEntity() {
        return entity;
    }

    /**
     * Set an identifier for the collection whose member is asserted
     *
     * @param collection QualifiedName for the collection
     * @see <a href="http://www.w3.org/TR/prov-dm/#membership.collection">membership collection</a>
     */
    @Override
    public void setCollection(QualifiedName collection) {
this.collection=collection;
    }

    /**
     * Gets the type of a provenance statement
     *
     * @return {@link Kind}
     */


    @JsonIgnore
    @Override
    public Kind getKind() {
        return Kind.PROV_MEMBERSHIP;
    }



    public void equals(Object object, EqualsBuilder equalsBuilder) {
        if (!(object instanceof HadMember)) {
            equalsBuilder.appendSuper(false);
            return ;
        }
        if (this == object) {
            return ;
        }
        final HadMember that = ((HadMember) object);
        equalsBuilder.append(this.getEntity(), that.getEntity());
        equalsBuilder.append(this.getCollection(), that.getCollection());
    }

    public boolean equals(Object object) {
        if (!(object instanceof HadMember)) {
            return false;
        }
        if (this == object) {
            return true;
        }
        final EqualsBuilder equalsBuilder = new EqualsBuilder();
        equals(object, equalsBuilder);
        return equalsBuilder.isEquals();
    }

    public void hashCode(HashCodeBuilder hashCodeBuilder) {
        hashCodeBuilder.append(this.getEntity());
        hashCodeBuilder.append(this.getCollection());
    }

    public int hashCode() {
        final HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        hashCode(hashCodeBuilder);
        return hashCodeBuilder.toHashCode();
    }

    public void toString(ToStringBuilder toStringBuilder) {



        {
            List<QualifiedName> theEntity;
            theEntity = this.getEntity();
            toStringBuilder.append("entity", theEntity);
        }

        {
            QualifiedName theCollection;
            theCollection = this.getCollection();
            toStringBuilder.append("collection", theCollection);
        }



    }

    @Override
    public String toString() {
        final ToStringBuilder toStringBuilder = new ToStringBuilder(this);
        toString(toStringBuilder);
        return toStringBuilder.toString();
    }

}
