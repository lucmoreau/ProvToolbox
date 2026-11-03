package org.openprovenance.prov.scala.narrator

import org.openprovenance.prov.scala.interop.{Input, Output}

case class XplainConfig(snlg: Output=null,
                        languageAsFilep: Boolean=true,
                        selected_templates: Seq[String]=Seq(),
                        profile: String=null,
                        batch_templates: Option[String]=None,
                        language: Seq[String]=Seq(),
                        linear:Boolean=false,
                        infile:Input=null,
                        infiles:String=null,
                        format_option:Int=0) extends XConfig {
  def this(c: XConfig) = {
    this(c.snlg, c.languageAsFilep, c.selected_templates, c.profile, c.batch_templates, c.language, c.linear, c.infile, c.infiles, c.format_option)
  }

}
