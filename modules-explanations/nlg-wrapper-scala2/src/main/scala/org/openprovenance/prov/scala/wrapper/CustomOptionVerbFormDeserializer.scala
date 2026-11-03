package org.openprovenance.prov.scala.wrapper

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.{DeserializationContext, JsonDeserializer}
import defs.{StringPhrase, VerbForm}

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
