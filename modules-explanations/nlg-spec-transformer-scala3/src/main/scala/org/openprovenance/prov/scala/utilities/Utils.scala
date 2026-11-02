package org.openprovenance.prov.scala.utilities

import java.io.{OutputStream, PrintStream}

import org.openprovenance.prov.model.Namespace
import org.openprovenance.prov.scala.immutable.QualifiedName

object Utils {
  def time[A](a: => A) = {
    val now = System.nanoTime
    val result = a
    val micros = (System.nanoTime - now) / 1000
    println("%d microseconds".format(micros))
    result
  }
  def captureOut(func: => Any): String = {
    val source = new java.io.ByteArrayOutputStream()
    withOutputFull(new java.io.PrintStream(source))(func)
    source.toString
  }
  def withOutput[T](out: PrintStream)(f: => Unit): Unit = {
    scala.Console.withOut(out)(scala.Console.withErr(out)(f))
  }
  def devnull(f: => Unit): Unit = {
    withOutput(nullout)(f)
  }
  def nullout = new PrintStream(new OutputStream() {
    override def write(b: Int) = {}
    override def write(b: Array[Byte]) = {}
    override def write(b: Array[Byte], off: Int, len: Int) = {}
  })
  def withOutputFull(out: PrintStream)(func: => Unit): Unit = {
    val oldStdOut = System.out
    val oldStdErr = System.err
    try {
      System.setOut(out)
      System.setErr(out)
      scala.Console.withOut(out)(scala.Console.withErr(out)(func))
    } finally {
      out.flush()
      out.close()
      System.setOut(oldStdOut)
      System.setErr(oldStdErr)
    }
  }

  def toNamespace(context: Map[String,String]): Namespace = {
    val ns=new Namespace
    ns.addKnownNamespaces()
    context.foreach{case (p,n) => ns.register(p,n)}
    ns
  }


  // TODO: was copied from org.openprovenance.prov.scala.immutable.Indexer.
  final def fourNullable(set: Set[QualifiedName], q1: QualifiedName, q2: QualifiedName, q3: QualifiedName, q4: QualifiedName): Set[QualifiedName] = {
    if (q4==null) {
      if (q1==null) {
        if (q2==null) {
          if (q3==null) {
            set
          } else {
            set + q3
          }
        } else {
          if (q3==null) {
            set + q2
          } else {
            set + q2 + q3
          }
        }
      } else {
        if (q2==null) {
          if (q3==null) {
            set + q1
          } else {
            set + q1 + q3
          }
        } else {
          if (q3==null) {
            set + q1 + q2
          } else {
            set + q1 + q2 + q3
          }
        }
      }
    } else {
      if (q1==null) {
        if (q2==null) {
          if (q3==null) {
            set + q4
          } else {
            set + q3 + q4
          }
        } else {
          if (q3==null) {
            set + q2 + q4
          } else {
            set + q2 + q3 + q4
          }
        }
      } else {
        if (q2==null) {
          if (q3==null) {
            set + q1 + q4
          } else {
            set + q1 + q3 + q4
          }
        } else {
          if (q3==null) {
            set + q1 + q2 + q4
          } else {
            set + q1 + q2 + q3 + q4
          }
        }
      }
    }
  }

}
