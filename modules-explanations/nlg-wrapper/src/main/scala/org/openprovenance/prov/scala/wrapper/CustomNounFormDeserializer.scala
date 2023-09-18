package org.openprovenance.prov.scala.wrapper

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.{DeserializationContext, JsonDeserializer}
import defs.{NounForm, NounPhrase, StringPhrase}

class CustomNounFormDeserializer extends JsonDeserializer [NounForm]  {
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
