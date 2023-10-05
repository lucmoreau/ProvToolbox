package org.openprovenance.prov.scala.narrator

import org.openprovenance.prov.scala.interop.{Input, Output}
import org.openprovenance.prov.scala.query.QConfig

trait XConfig extends QConfig {
  def snlg: Output


  def languageAsFilep: Boolean

  def selected_templates: Seq[String]

  def profile: String

  def batch_templates: Option[String]

  def language: Seq[String]

  def linear: Boolean

  def infile: Input

  def format_option: Int


  override def infiles: String

}
