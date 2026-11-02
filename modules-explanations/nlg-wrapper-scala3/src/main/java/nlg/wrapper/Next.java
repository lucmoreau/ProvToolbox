package nlg.wrapper;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.LinkedList;
import java.util.List;

@JsonPropertyOrder({ "name", "body" })
@JsonInclude(JsonInclude.Include.NON_NULL)
@JacksonXmlRootElement(localName = "next", namespace = "https://developers.google.com/blockly/xml")
public class Next implements BlocklyContents {

    private Block block=null;

    public Next(Block block) {
        this.block=block;
    }

    public Next() {
    }

    @JacksonXmlProperty( namespace = "https://developers.google.com/blockly/xml")
    public Block getBlock() {
        return block;
    }

}
