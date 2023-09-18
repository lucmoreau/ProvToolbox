package org.openprovenance.prov.scala.nlgspec_transformer

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.jsontype.TypeSerializer
import com.fasterxml.jackson.databind.{JsonSerializer, SerializerProvider}

import org.openprovenance.prov.scala.nlgspec_transformer.defs.StringPhrase



class CustomStringPhraseSerializer extends JsonSerializer[StringPhrase]{
  override def serialize(s: StringPhrase, jsonGenerator: JsonGenerator, serializerProvider: SerializerProvider): Unit = {
    jsonGenerator.writeString(s.value)
  }

  override def serializeWithType(s: StringPhrase, jsonGenerator: JsonGenerator, serializers: SerializerProvider, typeSer: TypeSerializer): Unit = {
    //super.serializeWithType(value, gen, serializers, typeSer)
    jsonGenerator.writeString(s.value)
  }
}
