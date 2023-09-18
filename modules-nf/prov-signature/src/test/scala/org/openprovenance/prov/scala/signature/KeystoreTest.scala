package org.openprovenance.prov.scala.signature

import junit.framework.TestCase
//import org.openprovenance.prov.scala.interop.CommandLine

abstract class KeystoreTest extends TestCase {
  
  def testSignature () {
    val args= Array("signature", 
                   "--infile", "src/test/resources/nf/ent1.provn", "--outfile", "target/ent1-signature.org.openprovenance.prov.xml",
                   "--id", "id5432", "--store", "src/test/resources/clientstore.jks", "--storepass", "cspass", "--key", "myclientkey", "--keypass", "ckpass")
 //   CommandLine.main(args)
    
  }
  
}