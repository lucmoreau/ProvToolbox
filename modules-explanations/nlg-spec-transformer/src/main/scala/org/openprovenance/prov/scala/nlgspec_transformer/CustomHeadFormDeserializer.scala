package org.openprovenance.prov.scala.nlgspec_transformer

import tools.jackson.core.JsonParser
import tools.jackson.databind.{DeserializationContext, ValueDeserializer}
import org.openprovenance.prov.scala.nlgspec_transformer.defs.{Funcall, StringPhrase}
import org.openprovenance.prov.scala.nlgspec_transformer.specTypes.HeadForm

class CustomHeadFormDeserializer extends ValueDeserializer [Option[HeadForm]] {
  override def deserialize(jsonParser: JsonParser, deserializationContext: DeserializationContext): Option[HeadForm] = {
    if (jsonParser.isExpectedStartObjectToken) {
      val value: Funcall =deserializationContext.readValue(jsonParser, classOf[Funcall])
      Some(value)
    } else {
      val s = deserializationContext.readValue(jsonParser, classOf[String])
      Some(new StringPhrase(s))
    }
  }
}
