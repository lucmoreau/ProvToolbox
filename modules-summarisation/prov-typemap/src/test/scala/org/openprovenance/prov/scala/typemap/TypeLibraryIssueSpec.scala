
package org.openprovenance.prov.scala.typemap

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers


class TypeLibraryIssueSpec extends AnyFlatSpec with Matchers {


  "a Type Library " should " be constructed " in {
    org.openprovenance.prov.scala.typemap.TypeMap.main(Array("src/test/resources/typemap/issue1/index2-simp.txt", "target/issue1-library.json"));
  }

}
