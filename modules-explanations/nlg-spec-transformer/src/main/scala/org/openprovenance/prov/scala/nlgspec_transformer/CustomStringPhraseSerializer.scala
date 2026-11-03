package org.openprovenance.prov.scala.nlgspec_transformer

import tools.jackson.core.JsonGenerator
import tools.jackson.databind.jsontype.TypeSerializer
import tools.jackson.databind.{ValueSerializer, SerializationContext}

import org.openprovenance.prov.scala.nlgspec_transformer.defs.StringPhrase



class CustomStringPhraseSerializer extends ValueSerializer[StringPhrase]{
  override def serialize(s: StringPhrase, jsonGenerator: JsonGenerator, serializerProvider: SerializationContext): Unit = {
    jsonGenerator.writeString(s.value)
  }

  override def serializeWithType(s: StringPhrase, jsonGenerator: JsonGenerator, serializers: SerializationContext, typeSer: TypeSerializer): Unit = {
    //super.serializeWithType(value, gen, serializers, typeSer)
    jsonGenerator.writeString(s.value)
  }
}
