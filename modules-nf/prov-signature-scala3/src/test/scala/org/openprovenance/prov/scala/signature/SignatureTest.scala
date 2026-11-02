package org.openprovenance.prov.scala.signature

import junit.framework.TestCase
import org.openprovenance.prov.scala.nf.xml.XmlSignature

import java.io.FileInputStream

class SignatureTest extends TestCase {

  def testKeystore (): Unit = {

    val p=XmlSignature.getKeyAndCert(new FileInputStream("src/test/resources/clientstore.jks"))


    System.out.println(p)
    
  }
  
}