
package org.openprovenance.prov.scala.typemap

import org.scalatest.{FlatSpec, Matchers}

abstract class TypeLibraryIssueSpec extends FlatSpec with Matchers {


  "a Type Library " should " be constructed " in {
    org.openprovenance.prov.scala.typemap.TypeMap.main(Array("src/test/resources/typemap/issue1/index2-simp.txt", "target/issue1-library.json"));
  }

}
