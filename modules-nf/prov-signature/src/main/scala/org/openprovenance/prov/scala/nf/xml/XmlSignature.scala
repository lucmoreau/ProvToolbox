package org.openprovenance.prov.scala.nf.xml

import java.io.{ByteArrayInputStream, ByteArrayOutputStream, InputStream, OutputStream}
import java.security.cert.X509Certificate
import java.security.{Key, KeyStore}
import java.util
import java.util.Base64

import javax.xml.namespace.QName
import javax.xml.stream.XMLStreamReader
import org.apache.coheigea.santuario.xml.signature.{TestSecurityEventListener, XmlReaderToWriter}
import org.apache.xml.security.Init
import org.apache.xml.security.stax.ext.{SecurePart, XMLSec, XMLSecurityConstants, XMLSecurityProperties}
import org.apache.xml.security.stax.impl.securityToken.X509SecurityToken
import org.apache.xml.security.stax.securityEvent._
import org.apache.xml.security.stax.securityToken.SecurityTokenConstants



// See https://github.com/coheigea/testcases/blob/master/apache/santuario/santuario-xml-signature/src/test/java/org/apache/coheigea/santuario/xmlsignature/SignatureUtils.java
// http://coheigea.blogspot.com/2014/03/apache-santuario-xml-security-for-java.html
object  XmlSignature {
  val x=Init.init()

  def signUsingStAX  (xmlStreamReader:XMLStreamReader,
                      namesToSign: List[QName],
                      algorithm: String,
                      signingKey: Key,
                      signingCert: X509Certificate)  : ByteArrayOutputStream  = {
    val baos = new ByteArrayOutputStream();
    signUsingStAX(xmlStreamReader, baos, namesToSign, algorithm, signingKey, signingCert)
    baos

  }

  def signUsingStAX (xmlStreamReader:XMLStreamReader,
                     outputStream: OutputStream,
                     namesToSign: List[QName],
                     algorithm: String,
                     signingKey: Key,
                     signingCert: X509Certificate)   = {

    // Set up the Configuration
    val properties = new XMLSecurityProperties();
    val actions: java.util.List[XMLSecurityConstants.Action]  = new util.ArrayList[XMLSecurityConstants.Action]();
    actions.add(XMLSecurityConstants.SIGNATURE);

    properties.setActions(actions);
    properties.setSignatureAlgorithm(algorithm)
    properties.setSignatureCerts( Array{ signingCert })
    properties.setSignatureKey(signingKey)
    properties.setSignatureKeyIdentifier(SecurityTokenConstants.KeyIdentifier_X509KeyIdentifier)
    properties.setSignaturePosition(100)
    properties.setSignatureCanonicalizationAlgorithm(XMLSecurityConstants.NS_C14N11_OMIT_COMMENTS) //NS_C14N11_WITH_COMMENTS)

    namesToSign.foreach( { nameToSign => { val securePart = new SecurePart(nameToSign, SecurePart.Modifier.Content)
      properties.addSignaturePart(securePart)  }})

    val outboundXMLSec = XMLSec.getOutboundXMLSec(properties);
    val xmlStreamWriter = outboundXMLSec.processOutMessage(outputStream, "UTF-8");


    XmlReaderToWriter.writeAll(xmlStreamReader, xmlStreamWriter);
    xmlStreamWriter.close();
  }

  def toStreamReader(in: InputStream) = {
    val xmlStreamReader = Transformer.xmlInputFactory.createXMLStreamReader(in)
    xmlStreamReader
  }

  def getKeyAndCert(stream: InputStream): (Key, X509Certificate) = {
    // Set up the Key
    try {
      val keyStore = KeyStore.getInstance("jks");
      keyStore.load(stream, "cspass".toCharArray());
      val key = keyStore.getKey("myclientkey", "ckpass".toCharArray());
      val cert = keyStore.getCertificate("myclientkey").asInstanceOf[X509Certificate];
      (key, cert)
    } catch {
      case e: Throwable => {
        e.printStackTrace()
        throw e
      }
    }

  }
  def getKeyAndCert(): (Key, X509Certificate) = {
    val stream: InputStream = this.getClass().getResource("clientstore.jks").openStream()
    getKeyAndCert(stream)
  }


  def doSign (xmlStreamReader:XMLStreamReader) = {
    val (key,cert)=getKeyAndCert()
    // Sign using StAX
    val namesToSign = List(new QName("document"))
    val baos = signUsingStAX(xmlStreamReader, namesToSign, "http://www.w3.org/2000/09/xmldsig#rsa-sha1", key, cert)
    baos
  }


  def  doSign (xmlStreamReader:XMLStreamReader, out: OutputStream, key: Key, cert: X509Certificate) = {
    val namesToSign = List(new QName("document"))
    signUsingStAX(xmlStreamReader, out, namesToSign, "http://www.w3.org/2000/09/xmldsig#rsa-sha1", key, cert)
  }


  def doSign (xmlStreamReader:XMLStreamReader, out: OutputStream): Unit = {
    val (key,cert)=getKeyAndCert()
    doSign(xmlStreamReader,out,key,cert)
  }


  /**
    * Verify the document using the StAX API of Apache Santuario - XML Security for Java.
    */
  def verifyUsingStAX(inputStream: InputStream,
                      namesToSign: List[QName],
                      cert: X509Certificate)  = {

    val listener=getEventListenerUsingStAX(inputStream,namesToSign,cert)
    val (sig,token,algo,signedElementEvents)=extractSignature(listener,namesToSign)


    // TODO: this is part of the verification
    namesToSign.foreach( {nameToSign : QName => {
      var found = false;
      signedElementEvents.foreach( { signedElement => {
        //println("signedElement " + signedElement)
        if (signedElement.isSigned()
          && nameToSign.equals(getSignedQName(signedElement.getElementPath()))) {
          found = true;
        }}})
      if (!found) throw new UnsupportedOperationException // TODO: add my own signature. Assert.assertTrue(found);
    }})

    //println("token id " + token.getId)
    token.getX509Certificates()(0)== cert
  }

  def getEventListenerUsingStAX(inputStream: InputStream,
                                namesToSign: List[QName],
                                cert: X509Certificate)  = {

    // Set up the Configuration
    val properties = new XMLSecurityProperties()
    val actions: java.util.List[XMLSecurityConstants.Action]  = new util.ArrayList[XMLSecurityConstants.Action]();
    actions.add(XMLSecurityConstants.SIGNATURE)
    //actions.add(XMLSecurityConstants.ENCRYPT)

    properties.setActions(actions);
    properties.setSignatureVerificationKey(cert.getPublicKey());


    val inboundXMLSec = XMLSec.getInboundWSSec(properties);

    val xmlStreamReader = Transformer.xmlInputFactory.createXMLStreamReader(inputStream);

    val eventListener = new TestSecurityEventListener();
    val securityStreamReader = inboundXMLSec.processInMessage(xmlStreamReader, null, eventListener);

    while (securityStreamReader.hasNext()) {
      securityStreamReader.next();
    }
    xmlStreamReader.close();
    inputStream.close()


    eventListener
  }

  def extractSignature(eventListener: TestSecurityEventListener, namesToSign: List[QName]) = {

    import scala.collection.JavaConverters._


    val sigValueEvent      =eventListener.getSecurityEvents(SecurityEventConstants.SignatureValue).asInstanceOf[java.util.List[SignatureValueSecurityEvent]].asScala
    val signedElemEvent    =eventListener.getSecurityEvents(SecurityEventConstants.SignedElement).asInstanceOf[java.util.List[SignedElementSecurityEvent]].asScala
    val algoEvent          =eventListener.getSecurityEvents(SecurityEventConstants.AlgorithmSuite).asInstanceOf[java.util.List[AlgorithmSuiteSecurityEvent]].asScala

    val keyNameTokenEvent  =eventListener.getSecurityEvents(SecurityEventConstants.KeyNameToken).asInstanceOf[java.util.List[KeyNameTokenSecurityEvent]].asScala
    val keyValueTokenEvent =eventListener.getSecurityEvents(SecurityEventConstants.KeyValueToken).asInstanceOf[java.util.List[KeyValueTokenSecurityEvent]].asScala
    val defaultTokenEvent  =eventListener.getSecurityEvents(SecurityEventConstants.DefaultToken).asInstanceOf[java.util.List[DefaultTokenSecurityEvent]].asScala
    val anyEvent           =eventListener.getSecurityEvents(SecurityEventConstants.ContentEncrypted).asInstanceOf[java.util.List[Any]].asScala
    val encryptedElemEvent =eventListener.getSecurityEvents(SecurityEventConstants.EncryptedElement).asInstanceOf[java.util.List[EncryptedElementSecurityEvent]].asScala
    val encryptedKeyTokenEvent =eventListener.getSecurityEvents(SecurityEventConstants.EncryptedKeyToken).asInstanceOf[java.util.List[EncryptedKeyTokenSecurityEvent]].asScala
    val x509TokenEvent     =eventListener.getSecurityEvents(SecurityEventConstants.X509Token).asInstanceOf[java.util.List[X509TokenSecurityEvent]].asScala


    /*
     println(keyNameTokenEvent)
     println(keyValueTokenEvent)
     println(defaultTokenEvent)
     println(anyEvent)
     println(encryptedElemEvent)
     println(encryptedKeyTokenEvent)
     println(x509TokenEvent)


     println(sigValueEvent)
     println(signedElemEvent)
     println(algoEvent)

     println(sigValueEvent)
     * */


    val buf=sigValueEvent.head.getSignatureValue
    val corId=sigValueEvent.head.getCorrelationID



    val sig=Base64.getEncoder.encodeToString(buf)

    // Check Signing cert
    val tokenEvent =x509TokenEvent.head
    //Assert.assertNotNull(tokenEvent);


    //Assert.assertTrue(tokenEvent.getSecurityToken() instanceof X509SecurityToken);
    val x509SecurityToken = tokenEvent.getSecurityToken().asInstanceOf[X509SecurityToken]
    (sig,x509SecurityToken,algoEvent,signedElemEvent)
  }

  def doVerify(baos: ByteArrayOutputStream) = {

    val (key,cert)=getKeyAndCert()

    val namesToSign = List(new QName("document"))

    val arr=baos.toByteArray()
    //println("Verifying " + baos.toString())
    verifyUsingStAX( new ByteArrayInputStream(arr),namesToSign, cert);
  }

  def getSignedQName(qnames: java.util.List[QName]):  QName  = {
    if (qnames == null || qnames.isEmpty()) {
      return null;
    }

    return qnames.get(qnames.size() - 1);
  }

}
