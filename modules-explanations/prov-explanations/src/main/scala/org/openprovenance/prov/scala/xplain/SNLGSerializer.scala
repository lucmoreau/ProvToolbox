package org.openprovenance.prov.scala.xplain

import tools.jackson.core.JsonGenerator
import tools.jackson.databind.{ValueSerializer, SerializationContext}

class SNLGSerializer extends ValueSerializer[String] {
  override def serialize(str: String, jsonGenerator: JsonGenerator, serializerProvider: SerializationContext): Unit = {
    jsonGenerator.writeRawValue(str)
  }
}

