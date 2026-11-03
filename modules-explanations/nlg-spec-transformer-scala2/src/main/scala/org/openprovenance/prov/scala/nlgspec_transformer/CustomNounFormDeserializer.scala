package org.openprovenance.prov.scala.nlgspec_transformer

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.{DeserializationContext, JsonDeserializer}
import org.openprovenance.prov.scala.nlgspec_transformer.defs.{NounPhrase, StringPhrase}
import org.openprovenance.prov.scala.nlgspec_transformer.specTypes.NounForm

class CustomNounFormDeserializer extends JsonDeserializer [NounForm] {
  override def deserialize(jsonParser: JsonParser, deserializationContext: DeserializationContext): NounForm = {
    println("++++++ In custom nounForm Deserialiser")
    if (jsonParser.isExpectedStartObjectToken) {
      val value: NounPhrase =jsonParser.readValueAs(classOf[NounPhrase])
      value
    } else {
      val s = jsonParser.readValueAs(classOf[String])
      new StringPhrase(s)
    }
  }
}
