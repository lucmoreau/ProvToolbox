// T46: the generated Rust flow does what the Java one does (T36FlowTest: testTheGeneratedFlowRunsAndFillsEveryValueFromItsOrigin,
// testAChangedRequestValueChangesExactlyTheValuesThatCameFromIt, testEachGuardRefusesAMismatchBeforeAnythingIsBuilt).
// In Rust a bean is passed by value, as a stood thing that is absent is an empty bean (Java passes null).

use std::collections::BTreeMap;
use std::sync::{Arc, Mutex};
use t36_bean::json_text::Value;
use t36_bean::org::example::t36flow::actions::client::integrator::reprice_inputs::RepriceInputs;
use t36_bean::org::example::t36flow::actions::client::integrator::reprice_request::RepriceRequest;
use t36_bean::org::example::t36flow::backend::client::integrator::changing_outputs::ChangingOutputs;
use t36_bean::org::example::t36flow::backend::client::integrator::line_change_inputs::LineChangeInputs;
use t36_bean::org::example::t36flow::flows::reprice_flow::RepriceFlow;
use t36_bean::org::example::t36flow::flows::reprice_flow_rules::RepriceFlowRules;

/// The application's rules, as a stand-in: what it was asked is kept.
#[derive(Clone, Default)]
struct Rules {
    calls: Arc<Mutex<Vec<String>>>,
}

impl RepriceFlowRules for Rules {
    fn caller(&self, _asked: &RepriceInputs, _document_made: &ChangingOutputs) -> String {
        self.calls.lock().unwrap().push("caller".to_string());
        "agent-7".to_string()
    }
    fn clock(&self, _asked: &RepriceInputs, _document_made: &ChangingOutputs) -> String {
        self.calls.lock().unwrap().push("clock".to_string());
        "2026-11-16T10:00:00Z".to_string()
    }
    fn estimate(&self, _asked: &RepriceInputs, _recorded: &LineChangeInputs, _document: &LineChangeInputs, _document_made: &ChangingOutputs, _line: &LineChangeInputs, _line_made: &ChangingOutputs) -> String {
        self.calls.lock().unwrap().push("estimate".to_string());
        "estimate-after".to_string()
    }
    fn version(&self, _asked: &RepriceInputs, _recorded: &LineChangeInputs, _document: &LineChangeInputs, _document_made: &ChangingOutputs, _line: &LineChangeInputs, _line_made: &ChangingOutputs) -> String {
        self.calls.lock().unwrap().push("version".to_string());
        "v2".to_string()
    }
}

/// What stood: the record that made the document, and the one that made the line, each with what the store made of it.
#[derive(Clone)]
struct Stood {
    line: LineChangeInputs,
    line_made: ChangingOutputs,
    document: LineChangeInputs,
    document_made: ChangingOutputs,
}

fn stood(line_number: &str, document_made: Option<&str>) -> Stood {
    let mut line = LineChangeInputs::new();
    line.part_line = Some(line_number.to_string());
    line.part_price_unit = Some("10".to_string());
    line.part_product = Some("bolt".to_string());
    line.part_quantity = Some("100".to_string());
    line.ptype = Some("part".to_string());
    line.pattributes_other.insert("colour".to_string(), "red");
    let mut line_made = ChangingOutputs::new();
    line_made.part1 = Some("P1".to_string());
    let mut document = LineChangeInputs::new();
    document.items = Some("5".to_string());
    document.name1 = Some("RFQ-1".to_string());
    document.state1 = Some("open".to_string());
    document.attributes_other.insert("act".to_string(), "removal");
    let mut dm = ChangingOutputs::new();
    dm.document1 = document_made.map(|d| d.to_string());
    Stood { line, line_made, document, document_made: dm }
}

fn request(line: i32, price: f64) -> RepriceRequest {
    let mut r = RepriceRequest::new();
    r.line = Some(line);
    r.unit_price = Some(price);
    r.reason = Some("cheaper".to_string());
    r
}

fn ask(r: RepriceRequest, rules: &Rules, s: &Stood) -> RepriceInputs {
    RepriceFlow::ask(r, rules.clone(), s.document_made.clone())
}

fn record(asked: RepriceInputs, rules: &Rules, s: &Stood) -> LineChangeInputs {
    RepriceFlow::record(asked, s.document.clone(), s.document_made.clone(), s.line.clone(), s.line_made.clone(), rules.clone())
}

fn fields_of(b: &LineChangeInputs) -> BTreeMap<String, serde_json::Value> {
    match serde_json::to_value(b).unwrap() {
        serde_json::Value::Object(m) => m.into_iter().filter(|(k, _)| k != "isA").collect(),
        _ => panic!("a bean is an object"),
    }
}

fn panic_message<R>(f: impl FnOnce() -> R + std::panic::UnwindSafe) -> String {
    match std::panic::catch_unwind(f) {
        Ok(_) => panic!("it did not panic"),
        Err(e) => e.downcast_ref::<String>().cloned().or_else(|| e.downcast_ref::<&str>().map(|s| s.to_string())).unwrap_or_default(),
    }
}

#[test]
fn the_flow_runs_and_fills_every_value_from_its_origin() {
    let rules = Rules::default();
    let s = stood("3", Some("D1"));

    // 1. ask: the request becomes the user bean; the rest of it is given
    let asked = ask(request(3, 12.5), &rules, &s);
    assert_eq!(asked.line, Some(3));
    assert_eq!(asked.unit_price, Some(12.5));
    assert_eq!(asked.reason.as_deref(), Some("cheaper"));
    assert_eq!(asked.subject0.as_deref(), Some("D1"), "CHAINED");
    assert_eq!(asked.agent.as_deref(), Some("agent-7"), "SUPPLIED");
    assert_eq!(asked.time.as_deref(), Some("2026-11-16T10:00:00Z"));
    assert_eq!(asked.outcome, None, "the reply's values are not given yet");

    // 2. record: every value of the backend's bean has its origin
    let recorded = record(asked.clone(), &rules, &s);
    let mut expected: BTreeMap<&str, &str> = BTreeMap::new();
    for (k, v) in [
        ("agent", "agent-7"), ("time", "2026-11-16T10:00:00Z"),                                  // AS ASKED
        ("priceUnit", "12.5"), ("partLine", "3"), ("partPriceUnit", "12.5"),                    // AS ASKED, as text
        ("document0", "D1"), ("part0", "P1"),                                                   // CHAINED
        ("name1", "RFQ-1"), ("state1", "open"), ("items", "5"),                                 // CARRIED OVER
        ("ptype", "part"), ("partProduct", "bolt"), ("partQuantity", "100"),
        ("priceBefore", "10"), ("partPriceBefore", "10"),
        ("atype", "Repricing"), ("overwrite", "true"), ("partPriceSource", "repriced"),         // CONSTANT
        ("estimate", "estimate-after"), ("erpId1", "v2"),                                       // SUPPLIED
    ] {
        expected.insert(k, v);
    }
    let mut got = fields_of(&recorded);
    let pattributes_other = got.remove("pattributesOther").unwrap();
    let attributes_other = got.remove("attributesOther").unwrap();
    let mut filled = Vec::new();
    for (k, v) in &got {
        match expected.get(k.as_str()) {
            Some(e) => assert_eq!(v, &serde_json::Value::String(e.to_string()), "the value {} has the origin the flow says", k),
            None => assert!(v.is_null(), "the value {} has no origin and is {}", k, v),
        }
        if !v.is_null() {
            filled.push(k.clone());
        }
    }
    filled.sort();
    let mut names: Vec<String> = expected.keys().map(|k| k.to_string()).collect();
    names.sort();
    assert_eq!(filled, names, "nothing else is filled, nothing is missing");
    assert_eq!(pattributes_other, serde_json::json!({"colour": "red"}), "the line's other keys are carried over");
    assert_eq!(attributes_other, serde_json::json!({}), "the act's other keys are not carried");
    assert_eq!(*rules.calls.lock().unwrap(), vec!["caller", "clock", "version", "estimate"], "the supplied values are asked of the rules, once each");

    // 3. answer: what the store made becomes the reply
    let mut made = ChangingOutputs::new();
    made.document1 = Some("D2".to_string());
    let reply = RepriceFlow::answer(asked, recorded, made);
    assert_eq!(reply_json(&reply)["outcome"], "accepted");
}

#[test]
fn a_changed_request_value_changes_exactly_the_values_that_came_from_it() {
    let rules = Rules::default();
    let s = stood("3", Some("D1"));
    let at12 = fields_of(&record(ask(request(3, 12.5), &rules, &s), &rules, &s));
    let at13 = fields_of(&record(ask(request(3, 13.5), &rules, &s), &rules, &s));
    let differ: Vec<&String> = at12.keys().filter(|k| at12[*k] != at13[*k]).collect();
    assert_eq!(differ, vec!["partPriceUnit", "priceUnit"]);
    // the known limit of a double: 2.50 is the double 2.5 and is recorded as 2.5; 5.0 is 5
    assert_eq!(fields_of(&record(ask(request(3, 2.50), &rules, &s), &rules, &s))["priceUnit"], "2.5");
    assert_eq!(fields_of(&record(ask(request(3, 5.0), &rules, &s), &rules, &s))["priceUnit"], "5");
}

#[test]
fn each_guard_refuses_a_mismatch_before_anything_is_built() {
    let rules = Rules::default();

    // the previous record is of line 4, the request of line 3
    let wrong_line = stood("4", Some("D1"));
    let asked = ask(request(3, 12.5), &rules, &wrong_line);
    let (r, s) = (rules.clone(), wrong_line.clone());
    let m = panic_message(move || record(asked, &r, &s));
    assert!(m.contains("the previous record of the line is of another partLine than the request's line"), "{}", m);

    // the request addresses document D1, the previous record made D2
    let addresses_d1 = ask(request(3, 12.5), &rules, &stood("3", Some("D1")));
    let made_d2 = stood("3", Some("D2"));
    let r = rules.clone();
    let m = panic_message(move || record(addresses_d1, &r, &made_d2));
    assert!(m.contains("addresses another document than the one the previous record made"), "{}", m);

    // the document has no record that made it: nothing to address
    let none = stood("3", None);
    let addresses_nothing = ask(request(3, 12.5), &rules, &none);
    assert_eq!(addresses_nothing.subject0, None);
    let r = rules.clone();
    let m = panic_message(move || record(addresses_nothing, &r, &none));
    assert!(m.contains("addresses no document"), "{}", m);
}

#[test]
fn the_reply_says_what_the_store_made_or_why_not() {
    let rules = Rules::default();
    let s = stood("3", Some("D1"));
    let asked = ask(request(3, 12.5), &rules, &s);
    let recorded = record(asked.clone(), &rules, &s);

    let mut made = ChangingOutputs::new();
    made.document1 = Some("D2".to_string());
    let reply = RepriceFlow::answer(asked.clone(), recorded.clone(), made);
    assert_eq!(reply_json(&reply)["outcome"], "accepted");
    assert_eq!(reply_json(&reply)["result"], "v2", "AS MADE: the backend's own value, given back");
    assert!(reply_json(&reply).get("code").is_none());

    let reply = RepriceFlow::answer(asked, recorded, ChangingOutputs::new());
    assert_eq!(reply_json(&reply)["outcome"], "failed", "nothing made");

    let refused = RepriceFlow::refused(RepriceInputs::new(), "NO_CHANGE");
    assert_eq!(reply_json(&refused)["outcome"], "refused");
    assert_eq!(reply_json(&refused)["code"], "NO_CHANGE");
    assert!(reply_json(&refused).get("result").is_none());

    let failed = RepriceFlow::failed(RepriceInputs::new());
    assert_eq!(reply_json(&failed)["outcome"], "failed");
    assert!(reply_json(&failed).get("code").is_none());
}

fn reply_json<T: serde::Serialize>(r: &T) -> serde_json::Value {
    serde_json::to_value(r).unwrap()
}

#[allow(dead_code)]
fn unused(_: Value) {}
