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
@JacksonXmlRootElement(localName = "block", namespace = Value.BLOCKLY_NS)
public class Block {
    String id;
    String type;
    String x;
    String y;
    private List<BlocklyContents> blocks=new LinkedList<>();

    @JacksonXmlProperty( isAttribute = true)
    public String getId() {
        return id;
    }

    @JacksonXmlProperty( isAttribute = true)
    public String getType() {
        return type;
    }

    @JacksonXmlProperty( namespace =BLOCKLY_NS)
    public String getX() {
        return x;
    }

    @JacksonXmlProperty( namespace =BLOCKLY_NS)
    public String getY() {
        return y;
    }

    //@JacksonXmlProperty( localName="body", namespace =BLOCKLY_NS)

    @JsonTypeInfo(use=JsonTypeInfo.Id.NAME, include=JsonTypeInfo.As.WRAPPER_OBJECT)
    @JsonSubTypes({
            @JsonSubTypes.Type(value = Field.class, name = "field"),
            @JsonSubTypes.Type(value = Value.class, name = "value"),
            @JsonSubTypes.Type(value = Mutation.class, name = "mutation"),
            @JsonSubTypes.Type(value = Statement.class, name = "statement"),
            @JsonSubTypes.Type(value = Next.class, name = "next")
    })
    @JacksonXmlProperty(localName="statements",namespace=BLOCKLY_NS)
    @JacksonXmlElementWrapper(useWrapping = false)
    public List<BlocklyContents> getBody() {
        return blocks;
    }

    public Block(String id, String type, String x, String y, List<BlocklyContents> blocks) {
        this.id = id;
        this.type = type;
        this.x = x;
        this.y = y;
        this.blocks.addAll(blocks);
    }

    public Block() {
        this.blocks=new LinkedList<>();
    }
}
