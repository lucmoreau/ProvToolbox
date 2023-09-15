package org.openprovenance.prov.scala.interop

import org.openprovenance.prov.scala.factorgraph.{CsvOutputer, MatlabOutputer}
import org.openprovenance.prov.scala.immutable.{Format, ProvNOutputer}
import org.openprovenance.prov.scala.viz.SVGOutputer

object Format2 {

  import Format._



  val outputers: Map[Format.Value, Outputer] =
    Map(PROVN  -> new ProvNOutputer,
      SVG    -> new SVGOutputer,
      MATLAB -> new MatlabOutputer,
      Format.CSV    -> new CsvOutputer,
      DOT    -> new SVGOutputer(otype="dot"),
      PDF    -> new SVGOutputer(otype="pdf"),
      PNG    -> new SVGOutputer(otype="png")
    )


}
