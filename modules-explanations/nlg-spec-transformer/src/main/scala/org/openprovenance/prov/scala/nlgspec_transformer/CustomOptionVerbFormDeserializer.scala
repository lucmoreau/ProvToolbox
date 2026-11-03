package org.openprovenance.prov.scala.nlgspec_transformer

import tools.jackson.core.JsonParser
import tools.jackson.databind.{DeserializationContext, ValueDeserializer}
import org.openprovenance.prov.scala.nlgspec_transformer.defs.{StringPhrase, VerbPhrase}
import org.openprovenance.prov.scala.nlgspec_transformer.specTypes.VerbForm

class CustomOptionVerbFormDeserializer extends ValueDeserializer [Option[VerbForm]]  {
  override def deserialize(jsonParser: JsonParser, deserializationContext: DeserializationContext): Option[VerbForm] = {
    if (jsonParser.isExpectedStartObjectToken) {
      val value: VerbForm =deserializationContext.readValue(jsonParser, classOf[VerbForm])
      Some(value)
    } else {
      val s = deserializationContext.readValue(jsonParser, classOf[String])
      Some(new StringPhrase(s))
    }
  }
}
