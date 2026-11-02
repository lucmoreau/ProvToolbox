package org.openprovenance.prov.scala.wrapper

import com.ctc.wstx.stax.{WstxInputFactory, WstxOutputFactory}
import com.fasterxml.jackson.core.Version
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.dataformat.xml.{XmlFactory, XmlMapper}
import nlg.wrapper.{Block, Root, StatementsHandler}
import Blockly.BLOCKLY_NS
import nlg.wrapper.stax.{ElementEraserXMLStreamWriter2, NamespaceXMLStreamWriter2}
import org.codehaus.stax2.{XMLOutputFactory2, XMLStreamWriter2}

import java.io.{IOException, InputStream, OutputStream}
import javax.xml.stream.XMLStreamWriter

/*
trait BlocklyContents{}

 */

object Blockly {

  val BLOCKLY_NS = "https://developers.google.com/blockly/xml"

}


class BlocklySerializer(wrap_erase: Boolean) {

  def serialiseBlock(out: OutputStream, document: Any, formatted: Boolean): Unit = {
    val mapper = getMapper(formatted)
    try mapper.writeValue(out, document)
    catch {
      case e: IOException =>
        e.printStackTrace()
        throw new RuntimeException(e)
    }
  }

  def deserialiseBlock(in: InputStream): Block = {
    val mapper = getMapper(true)
    try {
      mapper.readValue(in , classOf[Block])
    } catch {
      case e: IOException =>
        e.printStackTrace()
        throw new RuntimeException(e)
    }
  }


  def deserialiseRoot(in: InputStream): Root = {
    val mapper = getMapper(true)
    try {
      mapper.readValue(in , classOf[Root])
    } catch {
      case e: IOException =>
        e.printStackTrace()
        throw new RuntimeException(e)
    }
  }



  def getMapper(formatted: Boolean): XmlMapper = {
    val mapper = getXmlMapper
    if (formatted) mapper.enable(SerializationFeature.INDENT_OUTPUT)
    val module = new StatementsHandler("CustomBlockly", new Version(1, 0, 0, null, null, null))

    mapper.registerModule(module)

    //val module = makeModule
    //mapper.registerModule(module)
    //val filterProvider = makeFilter
    //mapper.setFilterProvider(filterProvider)
    //provMixin.addProvMixin(mapper)
    mapper
  }


  def getXmlMapper: XmlMapper = {
    val inputFactory2 = new WstxInputFactory
    val outputFactory2 = new WstxOutputFactory() {
      override def createXMLStreamWriter(w: OutputStream, enco: String): XMLStreamWriter = { //mConfig.enableAutomaticNamespaces(true);
        //  mConfig.setProperty(WstxInputProperties.P_RETURN_NULL_FOR_DEFAULT_NAMESPACE,  true);
        val result = super.createXMLStreamWriter(w, enco).asInstanceOf[XMLStreamWriter2]
        //result.setPrefix("prov", "http://www.w3.org/ns/prov#")
        //result.setPrefix("ex", "http://example.org/")
        // result.setPrefix("", "http://www.w3.org/ns/prov#");
        result.setDefaultNamespace(BLOCKLY_NS)
        if (wrap_erase) new ElementEraserXMLStreamWriter2(result, "statements")
        else new NamespaceXMLStreamWriter2(result)
      }
    }
    outputFactory2.setProperty(XMLOutputFactory2.P_AUTOMATIC_NS_PREFIX, true)
    outputFactory2.configureForRobustness()
    val xmlFactory = new XmlFactory(inputFactory2, outputFactory2)
    new XmlMapper(xmlFactory)
  }


}

