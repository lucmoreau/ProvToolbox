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

@JsonInclude(JsonInclude.Include.NON_NULL)
@JacksonXmlRootElement(localName = "statement", namespace = "https://developers.google.com/blockly/xml")
public class Statement implements BlocklyContents {
    private String name;
    private Block block =null;

    public Statement(String name, Block block) {
        this.name = name;
        this.block=block;
    }

    public Statement() {
        block =null;
    }

    @JacksonXmlProperty( isAttribute = true)
    public String getName() {
        return name;
    }



    @JacksonXmlProperty( namespace = "https://developers.google.com/blockly/xml")
    public Block getBlock() {
        return block;
    }

}
