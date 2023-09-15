package org.openprovenance.prov.scala.nf.xml

import java.io.FileInputStream

import junit.framework.TestCase

class SignatureTest extends TestCase {

  def testKeystore () {

    val p=XmlSignature.getKeyAndCert(new FileInputStream("src/test/resources/clientstore.jks"))


    System.out.println(p)
    
  }
  
}