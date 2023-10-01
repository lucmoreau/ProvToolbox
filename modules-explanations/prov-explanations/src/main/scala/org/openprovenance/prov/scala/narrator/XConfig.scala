package org.openprovenance.prov.scala.narrator

import org.openprovenance.prov.scala.interop.{Input, Output}

trait XConfig {
  def snlg: Output


  def languageAsFilep: Boolean

  def selected_templates: Seq[String]

  def profile: String

  def batch_templates: Option[String]

  def language: Seq[String]

  def linear: Boolean

  def infile: Input

  def format_option: Int

  def infiles: String

}
