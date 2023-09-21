
package org.openprovenance.prov.scala.typemap

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers


class ProvTypeMapSpec extends AnyFlatSpec with Matchers {


  "A ProvType map " should "be readable" in {

    val inputs=Array("src/test/resources/summary1-2020-03-01-03.26.17-types.json")

    println("creating typemap")
    TypeMap.doOneFile(inputs)

  }


  /*
   "A ProvType map " should "be buildable from index" in {

     val inputs=Array("/Users/luc/tmp/index.txt")

     println("creating typemap")
     TypeMap.doIndexFile(inputs, "tmap.json")

   }
 */

}
