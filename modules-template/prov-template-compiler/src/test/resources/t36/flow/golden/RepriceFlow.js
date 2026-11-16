const IllegalArgumentException = past.exception.IllegalArgumentException;
const { RepriceReply } = require('org/example/t36flow/actions/client/integrator/RepriceReply.js');
const { JsonText } = require('org/openprovenance/prov/utils/JsonText.js');
const { Line_changeInputs } = require('org/example/t36flow/backend/client/integrator/Line_changeInputs.js');
const { RepriceInputs } = require('org/example/t36flow/actions/client/integrator/RepriceInputs.js');

/**
 * The flow org.example.t36flow.flows.RepriceFlow: the pattern that connects the beans of a request to the beans of the backend's record, from t36.Reprice to t36.LineChange.
 * The request becomes the user bean (ask), the user bean and what stood become the backend's bean (record), and what the store made becomes the reply (answer).
 * Every input of those beans has one stated origin, said beside it: AS ASKED, CHAINED, CARRIED OVER, CONSTANT, SUPPLIED or AS MADE; the values the application computes are the rules of RepriceFlowRules.
 * Generated from the catalogue entry: the application writes the rules and nothing else.
 */
class RepriceFlow {
  constructor() {
  }

  /**
   * 1. The request becomes the user bean: what the person asked is copied, the rest is given here.
   */
  static ask(request, rules, documentMade) {
    // AS ASKED      line, unitPrice, reason: the request's values
    let asked = request.into(new RepriceInputs());
    // CHAINED       subject0: the document as it stood: document1 of the record that made it
    if (documentMade != null) {
      asked.subject0 = documentMade.document1;
    }
    // SUPPLIED      agent: the application's rule caller
    asked.agent = rules.caller(asked, documentMade);
    // SUPPLIED      time: the application's rule clock
    asked.time = rules.clock(asked, documentMade);
    return asked;
  }

  /**
   * 2. The user bean and what stood become the backend's bean. Each stood thing is the inputs and the outputs of the record that made it;
   * a previous record that does not match the request is refused before anything is built.
   */
  static record(asked, document, documentMade, line, lineMade, rules) {
    // GUARD         the document the request addresses is the one the previous record made
    if (asked.subject0 == null) {
      throw new IllegalArgumentException("T36: flow org.example.t36flow.flows.RepriceFlow: the request addresses no document");
    }
    if (JsonText.same(asked.subject0, documentMade.document1) == false) {
      throw new IllegalArgumentException("T36: flow org.example.t36flow.flows.RepriceFlow: the request addresses another document than the one the previous record made");
    }
    // GUARD         the previous record of the line is of the line the request names
    if (JsonText.same(line.partLine, JsonText.text(asked.line)) == false) {
      throw new IllegalArgumentException("T36: flow org.example.t36flow.flows.RepriceFlow: the previous record of the line is of another partLine than the request's line");
    }
    let recorded = new Line_changeInputs();
    // AS ASKED      agent: the user bean's agent
    recorded.agent = asked.agent;
    // AS ASKED      time: the user bean's time
    recorded.time = asked.time;
    // AS ASKED      priceUnit: the user bean's unitPrice, as text
    recorded.priceUnit = JsonText.text(asked.unitPrice);
    // AS ASKED      partLine: the user bean's line, as text
    recorded.partLine = JsonText.text(asked.line);
    // AS ASKED      partPriceUnit: the user bean's unitPrice, as text
    recorded.partPriceUnit = JsonText.text(asked.unitPrice);
    // CHAINED       document0: the document as it stood: document1 of the record that made it
    recorded.document0 = documentMade.document1;
    // CHAINED       part0: the line as it stood: part1 of the record that made it
    recorded.part0 = lineMade.part1;
    // CARRIED OVER  name1: the document's name1, unchanged
    recorded.name1 = document.name1;
    // CARRIED OVER  state1: the document's state1, unchanged
    recorded.state1 = document.state1;
    // CARRIED OVER  ptype: the line's ptype, unchanged
    recorded.ptype = line.ptype;
    // CARRIED OVER  priceBefore: the line's partPriceUnit, unchanged
    recorded.priceBefore = line.partPriceUnit;
    // CARRIED OVER  items: the document's items, unchanged
    recorded.items = document.items;
    // CARRIED OVER  partPriceBefore: the line's partPriceUnit, unchanged
    recorded.partPriceBefore = line.partPriceUnit;
    // CARRIED OVER  partProduct: the line's partProduct, unchanged
    recorded.partProduct = line.partProduct;
    // CARRIED OVER  partQuantity: the line's partQuantity, unchanged
    recorded.partQuantity = line.partQuantity;
    // CARRIED OVER  pattributesOther: the line's pattributesOther, unchanged
    if (line.pattributesOther != null) {
      recorded.pattributesOther = JsonText.others(line.pattributesOther, []);
    }
    // CONSTANT      atype: the constant Repricing
    recorded.atype = "Repricing";
    // CONSTANT      overwrite: the constant true
    recorded.overwrite = "true";
    // CONSTANT      partPriceSource: the constant repriced
    recorded.partPriceSource = "repriced";
    // SUPPLIED      erpId1: the application's rule version
    recorded.erpId1 = rules.version(asked, recorded, document, documentMade, line, lineMade);
    // SUPPLIED      estimate: the application's rule estimate
    recorded.estimate = rules.estimate(asked, recorded, document, documentMade, line, lineMade);
    // ABSENT        attributesOther: left unset
    return recorded;
  }

  /**
   * 3. What the store made becomes the reply.
   */
  static answer(asked, recorded, made) {
    // AS MADE       outcome: accepted when the store made document1, otherwise failed
    if (made.document1 == null) {
      asked.outcome = "failed";
    } else {
      asked.outcome = "accepted";
    }
    // ABSENT        code: left unset
    // AS MADE       result: the backend's erpId1 as recorded
    asked.result = recorded.erpId1;
    return RepriceReply.of(asked);
  }

  /**
   * 3'. A request the service refused: nothing was recorded, the reply says why.
   */
  static refused(asked, code) {
    // CONSTANT      outcome: the constant refused
    asked.outcome = "refused";
    // SUPPLIED      code: an argument of the method
    asked.code = code;
    // ABSENT        result: left unset
    return RepriceReply.of(asked);
  }

  /**
   * 3''. A request that failed in the service for a reason that is not a refusal: nothing was recorded.
   */
  static failed(asked) {
    // CONSTANT      outcome: the constant failed
    asked.outcome = "failed";
    // ABSENT        code: left unset
    // ABSENT        result: left unset
    return RepriceReply.of(asked);
  }

}

module.exports = { RepriceFlow,  };
