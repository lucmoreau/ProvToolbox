
package org.openprovenance.prov.scala

import java.io.{File, PrintWriter}
import java.security.KeyStore
import java.security.cert.X509Certificate

import org.openprovenance.prov.scala.immutable.{ProvNInputer, QualifiedName}
import org.openprovenance.prov.scala.interop.{FileInput, Input}
import org.openprovenance.prov.scala.nf.Normalizer
import org.scalatest.{FlatSpec, Matchers}

import scala.collection.mutable.ArrayBuffer

class Canonical2Spec extends FlatSpec with Matchers {
	val EX_NS="http://example/"

	def q(local: String) = {
   new QualifiedName("ex",local,EX_NS)
  }



  
  def readDoc(f: String) = {
    val in:Input=new FileInput(new File(f))
	  val doc=new ProvNInputer().input(in,Map())
	  doc
  }

  var times = ArrayBuffer[(String,Long,Long, Long)]()

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
  
  def checkfile (name: String, test2: Boolean=true) {
	  val doc1=time({readDoc("src/test/resources/canonical/" + name + ".provn")}, "read")
	  
	  
	  time({for (i <-1 until 1000) { 
	    Normalizer.fusion(doc1)}}, "normalize")    
	 }
	  
  
  
  def checkfileN (name: String, test1: Boolean=true, n: Long=2) {
       checkfile(name,test1)
       plotFile(name,times)
       times=ArrayBuffer[(String,Long,Long, Long)]()
  }
  
  def plotFile(name:String, times: ArrayBuffer[(String,Long,Long, Long)]) {

    val read_times=times.filter{case (s,_,_,_) => s=="read"}.map(x=>x._4).takeRight(200)
    val norm_times=times.filter{case (s,_,_,_) => s=="normalize"}.map(x=>x._4/normalize_count).takeRight(200)
    val seri_times=times.filter{case (s,_,_,_) => s=="serializeToFile"}.map(x=>x._4).takeRight(200)
    val sign_times=times.filter{case (s,_,_,_) => s=="serializeToSign"}.map(x=>x._4).takeRight(200)
    val veri_times=times.filter{case (s,_,_,_) => s=="verifySignature"}.map(x=>x._4).takeRight(200)
    val noth_times=times.filter{case (s,_,_,_) => s=="serializeToNothing"}.map(x=>x._4).takeRight(200)
    
    
    println(read_times)
    println(norm_times)
    println(read_times.reduce((x,y)=>x+y)/read_times.length)
    println(norm_times.reduce((x,y)=>x+y)/norm_times.length)
    
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
   
    /*
   "File pc1-with-id2" should " do stats" in {
     checkfileN("pc1-with-id2")
   }

  
   "File pc1-with-id3" should " do stats" in {
     checkfileN("pc1-with-id3",true,300)
   }
      
   "File pc1-with-id4" should " do stats" in {
     checkfileN("pc1-with-id4",true,300)
   }
   * 
   */
         

}
