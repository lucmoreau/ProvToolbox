package org.openprovenance.prov.scala.nlgspec_transformer

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.{DeserializationContext, JsonDeserializer}
import org.openprovenance.prov.scala.nlgspec_transformer.defs.StringPhrase
import org.openprovenance.prov.scala.nlgspec_transformer.specTypes.NounForm

class CustomOptionNounFormDeserializer extends JsonDeserializer [Option[NounForm]]  {
  override def deserialize(jsonParser: JsonParser, deserializationContext: DeserializationContext): Option[NounForm] = {
    if (jsonParser.isExpectedStartObjectToken) {
      val value: NounForm =jsonParser.readValueAs(classOf[NounForm])
      Some(value)
    } else {
      val s = jsonParser.readValueAs(classOf[String])
      Some(new StringPhrase(s))
    }
  }
}
