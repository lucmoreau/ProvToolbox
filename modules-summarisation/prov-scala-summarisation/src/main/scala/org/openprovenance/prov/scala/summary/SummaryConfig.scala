package org.openprovenance.prov.scala.summary

import java.io.File

case class SummaryConfig(level: Int, kernel: Boolean, aggregated: Boolean,
                         level0: File = null, triangle: Boolean = false, always_with_type_0: Boolean = false,
                         primitivep: Boolean = false, prov_constraints_inference: Boolean = false,
                         filter: List[String] = List(), maxlevel: Int = 0, nsBase: String = "",
                         prettyMethod: PrettyMethod.Pretty=PrettyMethod.Name,
                         aggregatep: Boolean = false, aggregate0p: Boolean = false, withLevel0Description: Boolean = false) {
  def this(level: Int, kernel: Boolean, aggregated: Boolean) = this(level, kernel, aggregated, null, false, false, false, false)
}