package org.openprovenance.prov.nf.xml;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElements;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAccessType;
import java.util.Map;


@XmlRootElement(name = "document")
@XmlAccessorType(XmlAccessType.FIELD)
public class Document {
	public Map<String, String> prefixes;
	
    @JacksonXmlElementWrapper(useWrapping = false)
    @JsonTypeInfo(use=JsonTypeInfo.Id.NAME, include=JsonTypeInfo.As.WRAPPER_OBJECT)

    @XmlElements({
        @XmlElement(name = "entity", type = Entity.class),
        @XmlElement(name = "activity", type = Activity.class),
        @XmlElement(name = "agent", type = Agent.class),
        @XmlElement(name = "actedOnBehalfOf", type = ActedOnBehalfOf.class),
        @XmlElement(name = "alternateOf", type = AlternateOf.class),
        @XmlElement(name = "hadMember", type = HadMember.class),
        @XmlElement(name = "specializationOf", type = SpecializationOf.class),
        @XmlElement(name = "used", type = Used.class),
        @XmlElement(name = "wasAssociatedWith", type = WasAssociatedWith.class),
        @XmlElement(name = "wasAttributedTo", type = WasAttributedTo.class),
        @XmlElement(name = "wasDerivedFrom", type = WasDerivedFrom.class),
        @XmlElement(name = "wasEndedBy", type = WasEndedBy.class),
        @XmlElement(name = "wasGeneratedBy", type = WasGeneratedBy.class),
        @XmlElement(name = "wasInfluencedBy", type = WasInfluencedBy.class),
        @XmlElement(name = "wasInformedBy", type = WasInformedBy.class),
        @XmlElement(name = "wasInvalidatedBy", type = WasInvalidatedBy.class),
        @XmlElement(name = "wasStartedBy", type = WasStartedBy.class),
    })
	public List<Statement> statements;
    
 //   @JacksonXmlElementWrapper(useWrapping = false)  
 //   @JsonTypeInfo(use=JsonTypeInfo.Id.NAME, include=JsonTypeInfo.As.WRAPPER_OBJECT)
 //   public StatementList ll;
    
}
