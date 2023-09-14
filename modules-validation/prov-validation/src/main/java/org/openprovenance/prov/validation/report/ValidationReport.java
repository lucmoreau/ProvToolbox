package org.openprovenance.prov.validation.report;

import org.openprovenance.apache.commons.lang.builder.EqualsBuilder;
import org.openprovenance.apache.commons.lang.builder.HashCodeBuilder;
import org.openprovenance.apache.commons.lang.builder.ToStringBuilder;
import org.openprovenance.prov.model.Namespace;
import org.openprovenance.prov.model.QualifiedName;
import org.openprovenance.prov.model.Document;
import org.openprovenance.apache.commons.lang.builder.Equals;
import org.openprovenance.apache.commons.lang.builder.HashCode;
import org.openprovenance.apache.commons.lang.builder.ToString;

import javax.xml.namespace.QName;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>Java class for ValidationReport complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ValidationReport"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="cycle" type="{http://openprovenance.org/validation#}Dependencies" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="nonStrictCycle" type="{http://openprovenance.org/validation#}Dependencies" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="failedMerge" type="{http://openprovenance.org/validation#}MergeReport" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="successfulMerge" type="{http://openprovenance.org/validation#}MergeReport" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="qualifiedNameMismatch" type="{http://openprovenance.org/validation#}MergeReport" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="specializationReport" type="{http://openprovenance.org/validation#}SpecializationReport"/&gt;
 *         &lt;element name="typeReport" type="{http://openprovenance.org/validation#}TypeReport"/&gt;
 *         &lt;element name="typeOverlap" type="{http://openprovenance.org/validation#}TypeOverlap" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="validationReport" type="{http://openprovenance.org/validation#}ValidationReport" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="malformedStatements" type="{http://openprovenance.org/validation#}MalformedStatements" minOccurs="0"/&gt;
 *         &lt;element name="deposited" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *         &lt;element ref="{http://www.w3.org/ns/prov#}document"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute ref="{http://www.w3.org/ns/prov#}id"/&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */


public class ValidationReport implements Equals, HashCode, ToString {
    Namespace namespace;

    public Namespace getNamespace() {
        return namespace;
    };

    public void setNamespace(Namespace namespace) {
        this.namespace = namespace;
    }

    public List<Dependencies> cycle;
    public List<Dependencies> nonStrictCycle;
    public List<MergeReport> failedMerge;
    public List<MergeReport> successfulMerge;
    public List<MergeReport> qualifiedNameMismatch;
    public SpecializationReport specializationReport;
    public TypeReport typeReport;
    public List<TypeOverlap> typeOverlap;
    public List<ValidationReport> validationReport;
    public MalformedStatements malformedStatements;
    public boolean deposited;
    public Document document;
    public QualifiedName id;

    /**
     * Gets the value of the cycle property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the cycle property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCycle().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * @return Objects of the following type(s) are allowed in the list
     * {@link Dependencies }
     * 
     * 
     */
    public List<Dependencies> getCycle() {
        if (cycle == null) {
            cycle = new ArrayList<>();
        }
        return this.cycle;
    }

    /**
     * Gets the value of the nonStrictCycle property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the nonStrictCycle property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getNonStrictCycle().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * @return Objects of the following type(s) are allowed in the list
     * {@link Dependencies }
     * 
     * 
     */
    public List<Dependencies> getNonStrictCycle() {
        if (nonStrictCycle == null) {
            nonStrictCycle = new ArrayList<>();
        }
        return this.nonStrictCycle;
    }

    /**
     * Gets the value of the failedMerge property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the failedMerge property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFailedMerge().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * @return Objects of the following type(s) are allowed in the list
     * {@link MergeReport }
     * 
     * 
     */
    public List<MergeReport> getFailedMerge() {
        if (failedMerge == null) {
            failedMerge = new ArrayList<>();
        }
        return this.failedMerge;
    }

    /**
     * Gets the value of the successfulMerge property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the successfulMerge property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSuccessfulMerge().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * @return Objects of the following type(s) are allowed in the list
     * {@link MergeReport }
     * 
     * 
     */
    public List<MergeReport> getSuccessfulMerge() {
        if (successfulMerge == null) {
            successfulMerge = new ArrayList<>();
        }
        return this.successfulMerge;
    }

    /**
     * Gets the value of the qualifiedNameMismatch property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the qualifiedNameMismatch property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getQualifiedNameMismatch().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * @return Objects of the following type(s) are allowed in the list
     * {@link MergeReport }
     * 
     * 
     */
    public List<MergeReport> getQualifiedNameMismatch() {
        if (qualifiedNameMismatch == null) {
            qualifiedNameMismatch = new ArrayList<>();
        }
        return this.qualifiedNameMismatch;
    }

    /**
     * Gets the value of the specializationReport property.
     * 
     * @return
     *     possible object is
     *     {@link SpecializationReport }
     *     
     */
    public SpecializationReport getSpecializationReport() {
        return specializationReport;
    }

    /**
     * Sets the value of the specializationReport property.
     * 
     * @param value
     *     allowed object is
     *     {@link SpecializationReport }
     *     
     */
    public void setSpecializationReport(SpecializationReport value) {
        this.specializationReport = value;
    }

    /**
     * Gets the value of the typeReport property.
     * 
     * @return
     *     possible object is
     *     {@link TypeReport }
     *     
     */
    public TypeReport getTypeReport() {
        return typeReport;
    }

    /**
     * Sets the value of the typeReport property.
     * 
     * @param value
     *     allowed object is
     *     {@link TypeReport }
     *     
     */
    public void setTypeReport(TypeReport value) {
        this.typeReport = value;
    }

    /**
     * Gets the value of the typeOverlap property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the typeOverlap property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTypeOverlap().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TypeOverlap }
     * @return a list of TypeOverlap structures
     */
    public List<TypeOverlap> getTypeOverlap() {
        if (typeOverlap == null) {
            typeOverlap = new ArrayList<>();
        }
        return this.typeOverlap;
    }

    /**
     * Gets the value of the validationReport property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the validationReport property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getValidationReport().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ValidationReport }
     * @return a list of validation reports
     */
    public List<ValidationReport> getValidationReport() {
        if (validationReport == null) {
            validationReport = new ArrayList<>();
        }
        return this.validationReport;
    }

    /**
     * Gets the value of the malformedStatements property.
     * 
     * @return
     *     possible object is
     *     {@link MalformedStatements }
     *     
     */
    public MalformedStatements getMalformedStatements() {
        return malformedStatements;
    }

    /**
     * Sets the value of the malformedStatements property.
     * 
     * @param value
     *     allowed object is
     *     {@link MalformedStatements }
     *     
     */
    public void setMalformedStatements(MalformedStatements value) {
        this.malformedStatements = value;
    }

    /**
     * Gets the value of the deposited property.
     * @return boolean
     */
    public boolean isDeposited() {
        return deposited;
    }

    /**
     * Sets the value of the deposited property.
     * @param value a boolean
     */
    public void setDeposited(boolean value) {
        this.deposited = value;
    }

    /**
     * Gets the value of the document property.
     * 
     * @return
     *     possible object is
     *     {@link Document }
     *     
     */
    public Document getDocument() {
        return document;
    }

    /**
     * Sets the value of the document property.
     * 
     * @param value
     *     allowed object is
     *     {@link Document }
     *     
     */
    public void setDocument(Document value) {
        this.document = value;
    }

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link QName }
     *     
     */
    public QualifiedName getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link QName }
     *     
     */
    public void setId(QualifiedName value) {
        this.id = value;
    }

    public void equals(Object object, EqualsBuilder equalsBuilder) {
        if (!(object instanceof ValidationReport)) {
            equalsBuilder.appendSuper(false);
            return ;
        }
        if (this == object) {
            return ;
        }
        final ValidationReport that = ((ValidationReport) object);
        equalsBuilder.append(this.getCycle(), that.getCycle());
        equalsBuilder.append(this.getNonStrictCycle(), that.getNonStrictCycle());
        equalsBuilder.append(this.getFailedMerge(), that.getFailedMerge());
        equalsBuilder.append(this.getSuccessfulMerge(), that.getSuccessfulMerge());
        equalsBuilder.append(this.getQualifiedNameMismatch(), that.getQualifiedNameMismatch());
        equalsBuilder.append(this.getSpecializationReport(), that.getSpecializationReport());
        equalsBuilder.append(this.getTypeReport(), that.getTypeReport());
        equalsBuilder.append(this.getTypeOverlap(), that.getTypeOverlap());
        equalsBuilder.append(this.getValidationReport(), that.getValidationReport());
        equalsBuilder.append(this.getMalformedStatements(), that.getMalformedStatements());
        equalsBuilder.append(this.isDeposited(), that.isDeposited());
        equalsBuilder.append(this.getDocument(), that.getDocument());
        equalsBuilder.append(this.getId(), that.getId());
    }

    public boolean equals(Object object) {
        if (!(object instanceof ValidationReport)) {
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
        hashCodeBuilder.append(this.getCycle());
        hashCodeBuilder.append(this.getNonStrictCycle());
        hashCodeBuilder.append(this.getFailedMerge());
        hashCodeBuilder.append(this.getSuccessfulMerge());
        hashCodeBuilder.append(this.getQualifiedNameMismatch());
        hashCodeBuilder.append(this.getSpecializationReport());
        hashCodeBuilder.append(this.getTypeReport());
        hashCodeBuilder.append(this.getTypeOverlap());
        hashCodeBuilder.append(this.getValidationReport());
        hashCodeBuilder.append(this.getMalformedStatements());
        hashCodeBuilder.append(this.isDeposited());
        hashCodeBuilder.append(this.getDocument());
        hashCodeBuilder.append(this.getId());
    }

    public int hashCode() {
        final HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        hashCode(hashCodeBuilder);
        return hashCodeBuilder.toHashCode();
    }

    public void toString(ToStringBuilder toStringBuilder) {
        {
            List<Dependencies> theCycle;
            theCycle = this.getCycle();
            toStringBuilder.append("cycle", theCycle);
        }
        {
            List<Dependencies> theNonStrictCycle;
            theNonStrictCycle = this.getNonStrictCycle();
            toStringBuilder.append("nonStrictCycle", theNonStrictCycle);
        }
        {
            List<MergeReport> theFailedMerge;
            theFailedMerge = this.getFailedMerge();
            toStringBuilder.append("failedMerge", theFailedMerge);
        }
        {
            List<MergeReport> theSuccessfulMerge;
            theSuccessfulMerge = this.getSuccessfulMerge();
            toStringBuilder.append("successfulMerge", theSuccessfulMerge);
        }
        {
            List<MergeReport> theQualifiedNameMismatch;
            theQualifiedNameMismatch = this.getQualifiedNameMismatch();
            toStringBuilder.append("qualifiedNameMismatch", theQualifiedNameMismatch);
        }
        {
            SpecializationReport theSpecializationReport;
            theSpecializationReport = this.getSpecializationReport();
            toStringBuilder.append("specializationReport", theSpecializationReport);
        }
        {
            TypeReport theTypeReport;
            theTypeReport = this.getTypeReport();
            toStringBuilder.append("typeReport", theTypeReport);
        }
        {
            List<TypeOverlap> theTypeOverlap;
            theTypeOverlap = this.getTypeOverlap();
            toStringBuilder.append("typeOverlap", theTypeOverlap);
        }
        {
            List<ValidationReport> theValidationReport;
            theValidationReport = this.getValidationReport();
            toStringBuilder.append("validationReport", theValidationReport);
        }
        {
            MalformedStatements theMalformedStatements;
            theMalformedStatements = this.getMalformedStatements();
            toStringBuilder.append("malformedStatements", theMalformedStatements);
        }
        {
            boolean theDeposited;
            theDeposited = this.isDeposited();
            toStringBuilder.append("deposited", theDeposited);
        }
        {
            Document theDocument;
            theDocument = this.getDocument();
            toStringBuilder.append("document", theDocument);
        }
        {
            QualifiedName theId;
            theId = this.getId();
            toStringBuilder.append("id", theId);
        }
    }

    public String toString() {
        final ToStringBuilder toStringBuilder = new ToStringBuilder(this);
        toString(toStringBuilder);
        return toStringBuilder.toString();
    }



}
