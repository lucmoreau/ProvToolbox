package org.openprovenance.prov.scala.wrapper

import tools.jackson.core.JsonParser
import tools.jackson.databind.{DeserializationContext, ValueDeserializer}
import defs.{NounForm, NounPhrase, StringPhrase}

class CustomNounFormDeserializer extends ValueDeserializer [NounForm]  {
  override def deserialize(jsonParser: JsonParser, deserializationContext: DeserializationContext): NounForm = {
    println("++++++ In custom nounForm Deserialiser")
    if (jsonParser.isExpectedStartObjectToken) {
      val value: NounPhrase =deserializationContext.readValue(jsonParser, classOf[NounPhrase])
      value
    } else {
      val s = deserializationContext.readValue(jsonParser, classOf[String])
      new StringPhrase(s)
    }
  }
}
