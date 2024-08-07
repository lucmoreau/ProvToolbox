
package org.openprovenance.prov.scala.signature

import org.openprovenance.prov.scala.immutable.{Document, ProvNInputer, QualifiedName}
import org.openprovenance.prov.scala.interop.{FileInput, Input}
import org.openprovenance.prov.scala.nf.Normalizer
import org.openprovenance.prov.scala.nf.xml.{XmlNfBean, XmlSignature}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import java.io.{ByteArrayOutputStream, File, PrintWriter}
import scala.annotation.unused
import scala.collection.mutable.ArrayBuffer

abstract class CanonicalSpec extends AnyFlatSpec with Matchers {
	val EX_NS="http://example/"

	def q(local: String): QualifiedName = {
   new QualifiedName("ex",local,EX_NS)
  }


  def readDoc(f: String): Document = {
    val in:Input=FileInput(new File(f))
    val doc=new ProvNInputer().input(in,Map())
    doc
  }
  var times: ArrayBuffer[(String, Long, Long, Long)] = ArrayBuffer[(String,Long,Long, Long)]()

  def time[R](block: => R, comment: String): R = {  
    val t0 = System.nanoTime()
    val result = block    // call-by-name
    val t1 = System.nanoTime()
    val tuple: (String, Long, Long, Long) =(comment,t0,t1,(t1-t0)/1000)
    times += tuple
    result
  }

  val normalize_count=10 

  val (cryptoKey,cert)= XmlSignature.getKeyAndCert

  /*
  val keyStore = KeyStore.getInstance("JKS");
  keyStore.load(this.getClass().getClassLoader().getResource("src/test/resources/store/clientstore.jks").openStream(), "cspass".toCharArray());
  val cryptoKey = keyStore.getKey("myclientkey", "ckpass".toCharArray());
  val cert = keyStore.getCertificate("myclientkey").asInstanceOf[X509Certificate];
     */
  
  def checkfile (name: String, @unused test2: Boolean=true): Unit = {
	  val doc1=time({readDoc("src/test/resources/canonical/" + name + ".provn")}, "read")
	  val doc3=time({1.to(normalize_count).map(_ => Normalizer.fusion(doc1)).last}, "normalize")
	  
	  
	  time({XmlNfBean.toXMLFile("target/" + name + "-canonical.xml",doc3,null)}, "serializeToFile")
	  
	  
	  val baos=new ByteArrayOutputStream
     

        
//    thread.start()
	  time( { val in=XmlNfBean.serializeToPipe(doc3,"id12345");  XmlSignature.doSign( XmlSignature.toStreamReader(in), baos, cryptoKey, cert) },"serializeToSign")
    // Verify using StAx
    time( { XmlSignature.doVerify(baos) should be (true) }, "verifySignature")
   	  
    time( { val in=XmlNfBean.serializeToPipe(doc3,"id12345"); XmlSignature.toStreamReader(in) },"serializeToNothing")

    

  }
  def checkfileN (name: String, test1: Boolean=true, n: Long=2): Unit = {
     if (n>0) {
       checkfile(name,test1)
       checkfileN(name,test1,n-1)
     } else {
       plotFile(name,times)
       times=ArrayBuffer[(String,Long,Long, Long)]()
     }
  }
  
  def plotFile(name:String, times: ArrayBuffer[(String,Long,Long, Long)]): Unit = {

    val read_times=times.filter{case (s,_,_,_) => s=="read"}.map(x=>x._4).takeRight(200)
    val norm_times=times.filter{case (s,_,_,_) => s=="normalize"}.map(x=>x._4/normalize_count).takeRight(200)
    val seri_times=times.filter{case (s,_,_,_) => s=="serializeToFile"}.map(x=>x._4).takeRight(200)
    val sign_times=times.filter{case (s,_,_,_) => s=="serializeToSign"}.map(x=>x._4).takeRight(200)
    val veri_times=times.filter{case (s,_,_,_) => s=="verifySignature"}.map(x=>x._4).takeRight(200)
    val noth_times=times.filter{case (s,_,_,_) => s=="serializeToNothing"}.map(x=>x._4).takeRight(200)
    
    
    println(read_times)
    println(norm_times)
    println(read_times.sum/read_times.length)
    println(norm_times.sum/norm_times.length)
    
    val csv_file="target/stats_" +name + ".csv"
    val pw=new PrintWriter(new File(csv_file))
    pw.println("# read, normalize, serialize, sign, verify, serial0")
    //read_times.zip(norm_times.zip(seri_times)).foreach(p=>pw.println(p._1 + "," + p._2._1/normalize_count+ "," + p._2._2))
    
    read_times.zip(norm_times).zip(seri_times).zip(sign_times).zip(veri_times).zip(noth_times).foreach{ case (((((a, b), c), d), e),f) => pw.println(a + "," + b + "," + c + "," + d + "," + e + "," + f) }

    pw.close()
    
    val cmd="/usr/local/bin/R --no-save --args target/stats_" + name + ".csv target/plot-" + name + ".pdf" 
    println(cmd)
    
  //  cmd #< new File("src/main/R/plot.R") !

    
  }
  
  "File pc1-full" should " do stats" in {
     checkfileN("pc1-full")
   }


}
