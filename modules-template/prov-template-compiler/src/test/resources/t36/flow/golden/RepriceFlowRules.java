package org.example.t36flow.flows;

import java.lang.String;
import org.example.t36flow.actions.client.integrator.RepriceInputs;
import org.example.t36flow.backend.client.integrator.ChangingOutputs;
import org.example.t36flow.backend.client.integrator.Line_changeInputs;

/**
 * The rules of the flow org.example.t36flow.flows.RepriceFlow: one method for each value the application computes, with the beans in scope in the step that uses it.
 * A rule is the application's: the flow copies nothing for these values.
 */
public abstract interface RepriceFlowRules {
  /**
   * The rule for agent, in the step ask.
   */
  String caller(RepriceInputs asked, ChangingOutputs documentMade);

  /**
   * The rule for time, in the step ask.
   */
  String clock(RepriceInputs asked, ChangingOutputs documentMade);

  /**
   * The rule for estimate, in the step record.
   */
  String estimate(RepriceInputs asked, Line_changeInputs recorded, Line_changeInputs document,
      ChangingOutputs documentMade, Line_changeInputs line, ChangingOutputs lineMade);

  /**
   * The rule for erpId1, in the step record.
   */
  String version(RepriceInputs asked, Line_changeInputs recorded, Line_changeInputs document,
      ChangingOutputs documentMade, Line_changeInputs line, ChangingOutputs lineMade);
}
