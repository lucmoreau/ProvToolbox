


package org.openprovenance.prov.scala.nlgspec_transformer

import nlg.wrapper.{Block, Root}
import org.openprovenance.prov.scala.nlgspec_transformer.specTypes.Phrase
import org.openprovenance.prov.scala.wrapper.BlocklySerializer
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import java.io.{FileInputStream, FileOutputStream}
import java.nio.file.FileSystems
import java.util


class xmlSpec extends  AnyFlatSpec with Matchers {

  "performance4 template" should "be exportable to XML" in {
    val template: Phrase = SpecLoader.phraseImport("src/test/resources/xml/performance4.json")

    println(template)

    val s=new BlocklySerializer(true)

    val block = template.toBlockly()
    val root=new Root(block)
    s.serialiseBlock(new FileOutputStream("target/performance4.xml"), root, formatted = true)


  }


  "data.sources template" should "be exportable to XML" in {
    val template: Phrase = SpecLoader.phraseImport("src/test/resources/xml/data.sources2.json")

    println(template)

    val s=new BlocklySerializer(true)

    val block = template.toBlockly()
    val root=new Root(block)
    s.serialiseBlock(new FileOutputStream("target/data.sources2.xml"), root, formatted = true)


  }

  private def roundtrip2(s: BlocklySerializer, infile: String, outfile: String): (Root, Boolean) = {
    val b3 = s.deserialiseRoot(new FileInputStream(infile))
    s.serialiseBlock(new FileOutputStream(outfile), b3, formatted = true)
    import java.nio.file.Files
    val f1 = Files.readAllBytes(FileSystems.getDefault.getPath(infile))
    val f2 = Files.readAllBytes(FileSystems.getDefault.getPath(outfile))
    (b3,util.Arrays.equals(f1,f2))
  }


  private def roundtrip(s: BlocklySerializer, infile: String, outfile: String): (Block, Boolean) = {
    val b3 = s.deserialiseBlock(new FileInputStream(infile))
    s.serialiseBlock(new FileOutputStream(outfile), b3, formatted = true)
    import java.nio.file.Files
    val f1 = Files.readAllBytes(FileSystems.getDefault.getPath(infile))
    val f2 = Files.readAllBytes(FileSystems.getDefault.getPath(outfile))
    (b3,util.Arrays.equals(f1,f2))
  }
}

