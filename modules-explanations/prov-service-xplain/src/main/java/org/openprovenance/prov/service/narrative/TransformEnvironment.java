package org.openprovenance.prov.service.narrative;

import org.openprovenance.prov.scala.immutable.Statement;
import scala.collection.Seq;
import scala.collection.immutable.Map;

public class TransformEnvironment {
    public Object environment;

    public Map<String, Statement> statements;

    public Map<String, Seq<Statement>> seqStatements;

}
