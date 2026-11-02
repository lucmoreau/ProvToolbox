package nlg.wrapper;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;

import static nlg.wrapper.Value.BLOCKLY_NS;

@JsonPropertyOrder({ "items", "body" })
@JsonInclude(JsonInclude.Include.NON_NULL)
@JacksonXmlRootElement(localName = "mutation", namespace = "https://developers.google.com/blockly/xml")
public class Mutation implements BlocklyContents {
    private String items;
    private String body;

    public Mutation(String items, String body) {
        this.items = items;
        this.body = body;
    }

    public Mutation () {}
    @JacksonXmlProperty(  isAttribute = true)
    public String getItems() {
        return items;
    }

    @JacksonXmlProperty( namespace =BLOCKLY_NS)
    @JacksonXmlText
    public String getBody() {
        return body;
    }

}
