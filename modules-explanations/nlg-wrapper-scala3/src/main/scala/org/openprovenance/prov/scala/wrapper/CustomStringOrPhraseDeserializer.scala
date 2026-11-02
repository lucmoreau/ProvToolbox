package org.openprovenance.prov.scala.wrapper

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.{DeserializationContext, JsonDeserializer}
import defs.{Phrase, StringPhrase}
import org.openprovenance.prov.scala.wrapper.defs.Phrase

class CustomStringOrPhraseDeserializer extends JsonDeserializer[Option[Phrase]]  {
  override def deserialize(jsonParser: JsonParser, deserializationContext: DeserializationContext): Option[Phrase] = {

    if (jsonParser.isExpectedStartObjectToken) {
      val value: Phrase =jsonParser.readValueAs(classOf[Phrase])
      Some(value)
    } else {
      val s = jsonParser.readValueAs(classOf[String])
      Some(new StringPhrase(s))
    }
  }
}


