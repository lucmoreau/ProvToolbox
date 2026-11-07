package org.openprovenance.prov.scala.wrapper

import tools.jackson.core.JsonParser
import tools.jackson.databind.{DeserializationContext, ValueDeserializer}
import defs.{StringPhrase, VerbForm}

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
