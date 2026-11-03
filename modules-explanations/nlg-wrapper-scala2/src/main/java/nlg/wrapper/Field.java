package nlg.wrapper;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;



@JsonPropertyOrder({ "name", "body" })
@JsonInclude(JsonInclude.Include.NON_NULL)
@JacksonXmlRootElement(localName = "field", namespace = Value.BLOCKLY_NS)
public class Field implements BlocklyContents {
    public final static String BLOCKLY_NS = "https://developers.google.com/blockly/xml";

    private String name;
    private String body;

    public Field(String name, String body) {
        this.name = name;
        this.body = body;
    }

    public Field () {}

    @JacksonXmlProperty( isAttribute = true)
    public String getName() {
        return name;
    }

    //@JacksonXmlProperty( namespace =BLOCKLY_NS, isAttribute = true)
    @JacksonXmlText
    public String getBody() {
        return body;
    }

}
