


package nlg.wrapper

import org.openprovenance.prov.scala.wrapper.{BlocklySerializer, IO, defs}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import java.io.{FileInputStream, FileOutputStream}
import java.nio.file.FileSystems
import java.util


class xmlSpec extends  AnyFlatSpec with Matchers {
  "A block" should "be serializable in xml" in {


    val b0=new Field("TEXT", "eat")

    val l1 = new util.LinkedList[BlocklyContents]()
    l1.add(b0);
    val b1=new nlg.wrapper.Block("b0", "b0type", null, null, l1);


    val l2 = new util.LinkedList[Block]()
    l2.add(b1)
    val b2=new Value("SUBJECT", l2);
    val b2b=new Value("VERB", l2);
    val values=new java.util.LinkedList[BlocklyContents]()
    values.add(b2);
    values.add(b2b)
    val b3=new nlg.wrapper.Block("id", "btype", null, null, values);




    val s=new BlocklySerializer(true)

    s.serialiseBlock(new FileOutputStream("target/b0.xml"), b0, true)
    s.serialiseBlock(new FileOutputStream("target/b1.xml"), b1, true)
    s.serialiseBlock(new FileOutputStream("target/b2.xml"), b2, true)
    s.serialiseBlock(new FileOutputStream("target/b3.xml"), b3, true)


    //result should be ("Luc eats cabbage.")
  }

  "A block" should "be deserializable from xml" in {

    val s=new BlocklySerializer(true)


    val (b3,flag3) = roundtrip(s, "target/b3.xml", "target/b3b.xml")

    System.out.println(b3);

    flag3 should be (true)


    val (b4,flag4) = roundtrip2(s, "src/test/resources/blockly/example1.xml", "target/example1.xml")

    System.out.println(b4);
    System.out.println(b4.getBlock);

    flag4 should be (false)  // xml output does not have the same layout


  }


  "data.sources template" should "be exportable to XML" in {
    val template: defs.Phrase = IO.phraseImport("src/test/resources/examples/data.sources1.json")

    println(template)

    val s=new BlocklySerializer(true)

    val block = template.toBlockly()
    val root=new Root(block)
    s.serialiseBlock(new FileOutputStream("target/sources1.xml"), root, true)


  }

  private def roundtrip2(s: BlocklySerializer, infile: String, outfile: String): (Root, Boolean) = {
    val b3 = s.deserialiseRoot(new FileInputStream(infile))
    s.serialiseBlock(new FileOutputStream(outfile), b3, true)
    import java.nio.file.Files
    val f1 = Files.readAllBytes(FileSystems.getDefault().getPath(infile))
    val f2 = Files.readAllBytes(FileSystems.getDefault().getPath(outfile))
    (b3,util.Arrays.equals(f1,f2))
  }


  private def roundtrip(s: BlocklySerializer, infile: String, outfile: String): (Block, Boolean) = {
    val b3 = s.deserialiseBlock(new FileInputStream(infile))
    s.serialiseBlock(new FileOutputStream(outfile), b3, true)
    import java.nio.file.Files
    val f1 = Files.readAllBytes(FileSystems.getDefault().getPath(infile))
    val f2 = Files.readAllBytes(FileSystems.getDefault().getPath(outfile))
    (b3,util.Arrays.equals(f1,f2))
  }
}

