
package org.openprovenance.prov.scala.signature

import org.openprovenance.prov.scala.immutable.QualifiedName
import org.openprovenance.prov.scala.interop.{FileInput, Input}
import org.openprovenance.prov.scala.nf.CommandLine
import org.openprovenance.prov.scala.nf.xml.{XmlBean, XmlSignature}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import java.io.{ByteArrayOutputStream, File}
import javax.xml.stream.XMLOutputFactory

abstract class NormalFormSpec extends AnyFlatSpec with Matchers {
	val EX_NS="http://example/"


	def q(local: String) = {
   new QualifiedName("ex",local,EX_NS)
  }


  def nf(f: String) = {
    val in:Input=new FileInput(new File(f))
    val doc=CommandLine.parseDocumentToNormalForm(in)
    doc
  }



  "Delegation aobo1" should "have a signature" in {
    val doc=nf("src/test/resources/nf/aob1.provn")
    val d=doc.toDocument()
    println("here 1")
    val (in,out)=XmlBean.pipe()
    val xmlOutputFactory = XMLOutputFactory.newFactory();
    
    val sw = xmlOutputFactory.createXMLStreamWriter(out);
    val thread = new Thread {
        override def run {
            println("in thread")
            //XmlBean.toXML(sw,d,null)
            XmlBean.toXML(sw,d,"id12345")
            println("finished thread")
            out.close()
        }
    }

    thread.start()
    
    val baos=new ByteArrayOutputStream
    XmlSignature.doSign( XmlSignature.toStreamReader(in), baos )
    // Verify using StAx
    XmlSignature.doVerify(baos) should be (true)
   
    
    
    
    println("here 4")
    //println(b)
    //println(x)
     //   x    
  }



}
