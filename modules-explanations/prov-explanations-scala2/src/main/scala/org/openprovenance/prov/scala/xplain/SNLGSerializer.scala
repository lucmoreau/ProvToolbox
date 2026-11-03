package org.openprovenance.prov.scala.xplain

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.{JsonSerializer, SerializerProvider}

class SNLGSerializer extends JsonSerializer[String] {
  override def serialize(str: String, jsonGenerator: JsonGenerator, serializerProvider: SerializerProvider): Unit = {
    jsonGenerator.writeRawValue(str)
  }
}

