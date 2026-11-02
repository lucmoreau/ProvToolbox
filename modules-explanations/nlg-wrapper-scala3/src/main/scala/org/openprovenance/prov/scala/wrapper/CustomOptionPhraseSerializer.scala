package org.openprovenance.prov.scala.wrapper

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.jsontype.TypeSerializer
import com.fasterxml.jackson.databind.{JsonSerializer, SerializerProvider}
import defs.Phrase

class CustomOptionPhraseSerializer extends JsonSerializer[Option[Phrase]]{
  override def serialize(s: Option[Phrase], jsonGenerator: JsonGenerator, serializerProvider: SerializerProvider): Unit = {
    jsonGenerator.writeObject(s.getOrElse("<<nothing>>"))
  }

  override def serializeWithType(s: Option[Phrase], jsonGenerator: JsonGenerator, serializers: SerializerProvider, typeSer: TypeSerializer): Unit = {
    jsonGenerator.writeObject(s.getOrElse("<<nothing>>"))
  }
}
