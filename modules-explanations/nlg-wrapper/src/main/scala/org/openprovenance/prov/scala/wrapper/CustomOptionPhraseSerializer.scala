package org.openprovenance.prov.scala.wrapper

import tools.jackson.core.JsonGenerator
import tools.jackson.databind.jsontype.TypeSerializer
import tools.jackson.databind.{ValueSerializer, SerializationContext}
import defs.Phrase

class CustomOptionPhraseSerializer extends ValueSerializer[Option[Phrase]]{
  override def serialize(s: Option[Phrase], jsonGenerator: JsonGenerator, serializerProvider: SerializationContext): Unit = {
    jsonGenerator.writePOJO(s.getOrElse("<<nothing>>"))
  }

  override def serializeWithType(s: Option[Phrase], jsonGenerator: JsonGenerator, serializers: SerializationContext, typeSer: TypeSerializer): Unit = {
    jsonGenerator.writePOJO(s.getOrElse("<<nothing>>"))
  }
}
