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

import static nlg.wrapper.Value.BLOCKLY_NS;

@JsonPropertyOrder({ "block" })
@JsonInclude(JsonInclude.Include.NON_NULL)
@JacksonXmlRootElement(localName = "xml", namespace = "https://developers.google.com/blockly/xml")
public class Root  {
    private List<Block> blocks=new LinkedList<>();

    public Root(Block body) {
        this.blocks.add(body);
    }

    public Root(List<Block> body) {
        this.blocks.addAll(body);
    }


    public Root() {
        blocks=new LinkedList<>();
    }

    @JsonTypeInfo(use=JsonTypeInfo.Id.NAME, include=JsonTypeInfo.As.WRAPPER_OBJECT)
    @JsonSubTypes({
            @JsonSubTypes.Type(value = Block.class, name = "block")
    })
    @JacksonXmlProperty(localName="statements",namespace=BLOCKLY_NS)
    @JacksonXmlElementWrapper(useWrapping = false)
    public List<Block> getBlock() {
        return blocks;
    }

}
