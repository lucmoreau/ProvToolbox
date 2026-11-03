package org.openprovenance.prov.scala.interop
import org.openprovenance.prov.scala.nf.CommandLine.parseDocument
import org.openprovenance.prov.scala.nf.{DocumentProxyFromStatements, Normalizer}
import org.openprovenance.prov.scala.nf.xml.{XmlNfBean, XmlSignature}

import java.io._
import java.security.KeyStore
import java.security.cert.X509Certificate
import javax.xml.namespace.QName



class UtilsSignature {


  def normalize(config: Config): Unit = {
    val in: Input = config.infile
    val doc = parseDocument(in)
    val nfDoc = Normalizer.fusion(doc)

    val out: Output = config.outfiles.head // select one of them!
    // use config.pretty?
    exportToXMLFile(out, nfDoc, null)


  }

  def sign1(config: Config): (X509Certificate, ByteArrayOutputStream) = {
    val in: Input = config.infile
    val doc = parseDocument(in)
    val nfDoc = Normalizer.fusion(doc)


    val baos = new ByteArrayOutputStream


    val store = config.store
    val storepass = config.storepass
    val key = config.key
    val keypass = config.keypass


    val keyStore = KeyStore.getInstance("jks")
    keyStore.load(new FileInputStream(store), storepass.toCharArray)
    val cryptoKey = keyStore.getKey(key, keypass.toCharArray)
    val cert = keyStore.getCertificate(key).asInstanceOf[X509Certificate]


    val pipeIn = XmlNfBean.serializeToPipe(nfDoc, config.id)
    XmlSignature.doSign(XmlSignature.toStreamReader(pipeIn), baos, cryptoKey, cert)
    (cert, baos)
  }

  def sign(config: Config): Unit = {
    val (_, baos) = sign1(config)

    val out: Output = config.outfiles.head // select one of them!

    exportToOutput(out, baos)

  }

  def sign(in: InputStream, out: OutputStream): Unit = {
    val config: Config= Config(outfiles = List(StreamOutput(out)), infile = StreamInput(in))
    sign(config)
  }

  def signature(config: Config): Unit = {
    val (cert, baos) = sign1(config)


    val arr = baos.toByteArray

    val namesToSign = List(new QName("document"))


    val listener = XmlSignature.getEventListenerUsingStAX(new ByteArrayInputStream(arr), namesToSign, cert)
    val (sign, token, algos, signedElementEvents) = XmlSignature.extractSignature(listener, namesToSign)


    val out: Output = config.outfiles.head // select one of them!

    exportToOutput(out, sign)
    algos.foreach(algo => {
      println(algo.getAlgorithmURI)
      println(algo.getAlgorithmUsage)
      println(algo.getKeyLength)
      println(algo.getCorrelationID)
      println(algo.getSecurityEventType)
    })

    signedElementEvents.foreach(signedElementEvent => {
      println("id " + signedElementEvent.getCorrelationID)
      println("path " + signedElementEvent.getElementPath)
      println("token " + signedElementEvent.getSecurityToken)
      println("order " + signedElementEvent.getProtectionOrder)
    })

    println("token id " + token.getId)
    println("token id " + token.getElementPath)
    println("token id " + token.getSha1Identifier)


  }


  def exportToXMLFile(out: Output, doc: DocumentProxyFromStatements, id: String): Unit = {
    out match {
      case FileOutput(f) => XmlNfBean.toXMLFile(f.toString(), doc, id)
      case StandardOutput() => XmlNfBean.toXMLOutputStream(doc, id)
      case _ => new UnsupportedOperationException
    }
  }

  def exportToOutput(out: Output, doc: ByteArrayOutputStream): Unit = {
    out match {
      case FileOutput(f) =>
        val os = new PrintWriter(f)
        os.print(doc)
        os.close()
      case StreamOutput(os) =>
        new PrintWriter(os).print(doc)
        os.close()
      case StandardOutput() => System.out.print(doc)
    }
  }

  def exportToOutput(out: Output, doc: String): Unit = {
    out match {
      case FileOutput(f) =>
        val os = new PrintWriter(f)
        os.print(doc)
        os.close()
      case StandardOutput() => System.out.print(doc)
      case StreamOutput(_) => throw new UnsupportedOperationException
    }
  }


}
