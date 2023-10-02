package org.openprovenance.prov.scala.query

import org.openprovenance.prov.scala.immutable._
import org.openprovenance.prov.scala.nlgspec_transformer.Environment
import org.openprovenance.prov.scala.primitive.{Result, Triple}
import org.openprovenance.prov.scala.query.Run.MainEngine
import org.openprovenance.prov.scala.query.QueryInterpreter






abstract class EngineProcessFunction(engine:Engine with MainEngine with QueryInterpreter) {


  def isFunction(amap: Map[String, Object]): Boolean = amap exists { case (s, _) => s == "@object" }


  def processFunction(objects: Map[String, engine.RField],
                      amap: Map[String, Object],
                      environment: Environment): (Option[Result], Set[Triple]) = {

    val statements: Map[String, Statement] = objects.map{case (s,f) => (s, engine.toStatement(f))}

    val result=org.openprovenance.prov.scala.primitive.Primitive.processFunction(statements, amap, environment)


    result
  }
}

