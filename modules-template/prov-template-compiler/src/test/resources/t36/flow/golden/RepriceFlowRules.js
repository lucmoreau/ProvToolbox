/**
 * The rules of the flow org.example.t36flow.flows.RepriceFlow: one method for each value the application computes, with the beans in scope in the step that uses it.
 * A rule is the application's: the flow copies nothing for these values.
 */
class RepriceFlowRules {
  constructor() {
  }

  /**
   * The rule for agent, in the step ask.
   */
  caller(asked, documentMade) {
  }

  /**
   * The rule for time, in the step ask.
   */
  clock(asked, documentMade) {
  }

  /**
   * The rule for estimate, in the step record.
   */
  estimate(asked, recorded, document, documentMade, line, lineMade) {
  }

  /**
   * The rule for erpId1, in the step record.
   */
  version(asked, recorded, document, documentMade, line, lineMade) {
  }

}

module.exports = { RepriceFlowRules,  };
