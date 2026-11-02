package org.openprovenance.prov.scala.nlgspec_transformer

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.{DeserializationContext, JsonDeserializer}
import org.openprovenance.prov.scala.nlgspec_transformer.defs.{StringPhrase, VerbPhrase}
import org.openprovenance.prov.scala.nlgspec_transformer.specTypes.VerbForm

class CustomOptionVerbFormDeserializer extends JsonDeserializer [Option[VerbForm]]  {
  override def deserialize(jsonParser: JsonParser, deserializationContext: DeserializationContext): Option[VerbForm] = {
    if (jsonParser.isExpectedStartObjectToken) {
      val value: VerbForm =jsonParser.readValueAs(classOf[VerbForm])
      Some(value)
    } else {
      val s = jsonParser.readValueAs(classOf[String])
      Some(new StringPhrase(s))
    }
  }
}
