package org.openprovenance.prov.scala.nf

import org.openprovenance.prov.model.Namespace


trait DocumentProxyInterface  {
  def add(s: org.openprovenance.prov.scala.immutable.Statement): DocumentProxyInterface
}

trait DocumentProxyFactory {
  def make (ns:Namespace) : DocumentProxyInterface
}