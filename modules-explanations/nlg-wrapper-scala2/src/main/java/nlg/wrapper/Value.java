package nlg.wrapper;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;

import java.util.LinkedList;
import java.util.List;

import static nlg.wrapper.Field.BLOCKLY_NS;

@JsonPropertyOrder({ "name", "body" })
@JsonInclude(JsonInclude.Include.NON_NULL)
@JacksonXmlRootElement(localName = "value", namespace = "https://developers.google.com/blockly/xml")
public class Value implements BlocklyContents {
    public final static String BLOCKLY_NS = "https://developers.google.com/blockly/xml";

    private String name;
    private List<Block> blocks=new LinkedList<>();

    public Value(String name, List<Block> blocks) {
        this.name = name;
        this.blocks.addAll(blocks);
    }

    public Value () {
        blocks=new LinkedList<>();
    }

    @JacksonXmlProperty( isAttribute = true)
    public String getName() {
        return name;
    }

    //@JacksonXmlProperty( namespace =BLOCKLY_NS)
    //@JacksonXmlText

    @JsonTypeInfo(use=JsonTypeInfo.Id.NAME, include=JsonTypeInfo.As.WRAPPER_OBJECT)
    @JsonSubTypes({
            @JsonSubTypes.Type(value = Block.class, name = "block"),
            @JsonSubTypes.Type(value = Value.class, name = "value")
    })
    @JacksonXmlProperty(localName="statements",namespace=BLOCKLY_NS)
    @JacksonXmlElementWrapper(useWrapping = false)
    public List<Block> getBlock() {
        return blocks;
    }

}
