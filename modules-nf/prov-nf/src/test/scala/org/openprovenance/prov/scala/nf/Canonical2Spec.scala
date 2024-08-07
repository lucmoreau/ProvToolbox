
package org.openprovenance.prov.scala.nf

import org.openprovenance.prov.scala.immutable.{Document, ProvNInputer, QualifiedName}
import org.openprovenance.prov.scala.interop.{FileInput, Input}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import java.io.{File, PrintWriter}
import scala.annotation.unused
import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

class Canonical2Spec extends AnyFlatSpec with Matchers {
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
    val tuple=(comment,t0,t1,(t1-t0)/1000)
    times += tuple
    result
  }

  val normalize_count=10 
  

/*
 
  val keyStore = KeyStore.getInstance("jks");
  keyStore.load(this.getClass().getClassLoader().getResource("src/test/resources/clientstore.jks").openStream(), "cspass".toCharArray());
  val cryptoKey = keyStore.getKey("myclientkey", "ckpass".toCharArray());
  val cert = keyStore.getCertificate("myclientkey").asInstanceOf[X509Certificate];
   */
  
  def checkfile (name: String, @unused test2: Boolean=true): Unit = {
	  val doc1=time({readDoc("src/test/resources/canonical/" + name + ".provn")}, "read")
	  
	  
	  time({for (i <-1 until 1000) { 
	    Normalizer.fusion(doc1)}}, "normalize")    
	 }
	  
  
  
  def checkfileN (name: String, test1: Boolean=true, @unused n: Long=2): Unit = {
       checkfile(name,test1)
       plotFile(name,times)
       times=ArrayBuffer[(String,Long,Long, Long)]()
  }
  
  def plotFile(name:String, times: ArrayBuffer[(String,Long,Long, Long)]): Unit = {

    val read_times: mutable.Seq[Long] =times.filter{case (s,_,_,_) => s=="read"}.map(x=>x._4).takeRight(200)
    val norm_times: mutable.Seq[Long] =times.filter{case (s,_,_,_) => s=="normalize"}.map(x=>x._4/normalize_count).takeRight(200)
    val seri_times: mutable.Seq[Long] =times.filter{case (s,_,_,_) => s=="serializeToFile"}.map(x=>x._4).takeRight(200)
    val sign_times: mutable.Seq[Long] =times.filter{case (s,_,_,_) => s=="serializeToSign"}.map(x=>x._4).takeRight(200)
    val veri_times: mutable.Seq[Long] =times.filter{case (s,_,_,_) => s=="verifySignature"}.map(x=>x._4).takeRight(200)
    val noth_times: mutable.Seq[Long] =times.filter{case (s,_,_,_) => s=="serializeToNothing"}.map(x=>x._4).takeRight(200)
    
    
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
    
    //cmd #< new File("src/main/R/plot.R") !

    
  }
  
 
   "File pc1-with-id" should " do stats" in {
     checkfileN("pc1-with-id")
   }
   


}
