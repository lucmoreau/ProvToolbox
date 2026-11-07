package org.openprovenance.prov.scala.nlgspec_transformer

import tools.jackson.core.JsonParser
import tools.jackson.databind.{DeserializationContext, ValueDeserializer}
import org.openprovenance.prov.scala.nlgspec_transformer.defs.StringPhrase
import org.openprovenance.prov.scala.nlgspec_transformer.specTypes.NounForm

class CustomOptionNounFormDeserializer extends ValueDeserializer [Option[NounForm]]  {
  override def deserialize(jsonParser: JsonParser, deserializationContext: DeserializationContext): Option[NounForm] = {
    if (jsonParser.isExpectedStartObjectToken) {
      val value: NounForm =deserializationContext.readValue(jsonParser, classOf[NounForm])
      Some(value)
    } else {
      val s = deserializationContext.readValue(jsonParser, classOf[String])
      Some(new StringPhrase(s))
    }
  }
}
