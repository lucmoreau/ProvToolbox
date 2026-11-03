package org.openprovenance.prov.scala.wrapper

import tools.jackson.core.JsonParser
import tools.jackson.databind.{DeserializationContext, ValueDeserializer}
import defs.{Phrase, StringPhrase}
import org.openprovenance.prov.scala.wrapper.defs.Phrase

class CustomStringOrPhraseDeserializer extends ValueDeserializer[Option[Phrase]]  {
  override def deserialize(jsonParser: JsonParser, deserializationContext: DeserializationContext): Option[Phrase] = {

    if (jsonParser.isExpectedStartObjectToken) {
      val value: Phrase =deserializationContext.readValue(jsonParser, classOf[Phrase])
      Some(value)
    } else {
      val s = deserializationContext.readValue(jsonParser, classOf[String])
      Some(new StringPhrase(s))
    }
  }
}


