package org.openprovenance.prov.scala.wrapper

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.jsontype.TypeSerializer
import com.fasterxml.jackson.databind.{JsonSerializer, SerializerProvider}
import defs.StringPhrase

class CustomStringPhraseSerializer extends JsonSerializer[StringPhrase]{
  override def serialize(s: StringPhrase, jsonGenerator: JsonGenerator, serializerProvider: SerializerProvider): Unit = {
    jsonGenerator.writeString(s.value)
  }

  override def serializeWithType(s: StringPhrase, jsonGenerator: JsonGenerator, serializers: SerializerProvider, typeSer: TypeSerializer): Unit = {
    jsonGenerator.writeString(s.value)
  }
}
