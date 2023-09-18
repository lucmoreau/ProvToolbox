package org.openprovenance.prov.scala.wrapper

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.{DeserializationContext, JsonDeserializer}
import defs.{NounForm, StringPhrase}

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
