package org.openprovenance.prov.scala.signature

import junit.framework.TestCase

abstract class KeystoreTest extends TestCase {
  
  def testSignature (): Unit = {
    val args= Array("signature", 
                   "--infile", "src/test/resources/nf/ent1.provn", "--outfile", "target/ent1-signature.org.openprovenance.prov.xml",
                   "--id", "id5432", "--store", "src/test/resources/clientstore.jks", "--storepass", "cspass", "--key", "myclientkey", "--keypass", "ckpass")
 //   CommandLine.main(args)
    
  }
  
}