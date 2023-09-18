package org.openprovenance.prov.scala.nlgspec_transformer

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.{DeserializationContext, JsonDeserializer}
import org.openprovenance.prov.scala.nlgspec_transformer.defs.{Funcall, StringPhrase}
import org.openprovenance.prov.scala.nlgspec_transformer.specTypes.HeadForm

class CustomHeadFormDeserializer extends JsonDeserializer [Option[HeadForm]] {
  override def deserialize(jsonParser: JsonParser, deserializationContext: DeserializationContext): Option[HeadForm] = {
    if (jsonParser.isExpectedStartObjectToken) {
      val value: Funcall =jsonParser.readValueAs(classOf[Funcall])
      Some(value)
    } else {
      val s = jsonParser.readValueAs(classOf[String])
      Some(new StringPhrase(s))
    }
  }
}
