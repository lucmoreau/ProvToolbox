package org.openprovenance.prov.scala.interop

import java.io.File

import org.openprovenance.prov.scala.immutable.{Document, Indexing}



sealed trait Output
case class FileOutput(f: File) extends Output
case class StreamOutput(s: java.io.OutputStream) extends Output
case class StandardOutput() extends Output


trait Input
case class FileInput(f: File) extends Input
case class StandardInput() extends Input
case class StreamInput(is: java.io.InputStream) extends Input


trait Outputer {
  def output(d: Document, out: Output, params:Map[String,String]): Unit
  def output(d: Indexing, out: Output, params:Map[String,String]): Unit
}

trait Inputer {
  def input(in: Input, params:Map[String,String]):Document
}
