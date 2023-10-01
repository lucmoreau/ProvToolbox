package org.openprovenance.prov.scala.summary

import org.openprovenance.prov.scala.immutable.Format.Format
import org.openprovenance.prov.scala.interop.Output
import org.openprovenance.prov.scala.summary.PrettyMethod.Pretty

import java.io.File

trait SConfig {
  def kernel: Boolean
  //def aggregated: Boolean
  def level0: File
  def triangle: Boolean
  def always_with_type_0: Boolean
  def primitivep: Boolean
  def prov_constraints_inference: Boolean
  def filter: Seq[String]
  def maxlevel: Int
  def nsBase: String
  def prettyMethod: Pretty
  def aggregatep: Boolean
  def aggregate0p: Boolean
  def withLevel0Description: Boolean
  def to: Int
  def outfiles: Seq[Output]
  def outformats: Seq[Format]
  def defaultFormat: Format
}