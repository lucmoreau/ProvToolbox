// T45: the generated Rust flow with a spread (T36FlowTest.testASpreadGivesTheMembersOfAJsonTextTheCallerGaveAsTheOtherKeys). The setup is flow.rs's.
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


fn run(reason: Option<&str>) -> LineChangeInputs {
    let rules = Rules::default();
    let s = stood("3", Some("D1"));
    let mut asked = ask(request(3, 12.5), &rules, &s);
    asked.reason = reason.map(|r| r.to_string());
    record(asked, &rules, &s)
}

#[test]
fn a_spread_gives_the_members_of_a_json_text_as_the_other_keys() {
    let recorded = run(Some(r#"{"colour":"red","n":2,"nested":{"a":[1]}}"#));
    let other = &recorded.attributes_other;
    assert_eq!(other.len(), 3);
    assert_eq!(other.get("colour"), Some(&Value::Str("red".to_string())));
    assert_eq!(other.get("n"), Some(&Value::Int(2)));
    assert!(matches!(other.get("nested"), Some(Value::Object(_))));
}

#[test]
fn an_absent_or_blank_text_is_no_other_key() {
    assert!(run(None).attributes_other.is_empty());
    assert!(run(Some("  ")).attributes_other.is_empty());
}

#[test]
fn a_text_that_is_no_object_is_refused() {
    let m = match std::panic::catch_unwind(|| run(Some("[1,2]"))) {
        Ok(_) => panic!("an array is no object"),
        Err(e) => e.downcast_ref::<String>().cloned().or_else(|| e.downcast_ref::<&str>().map(|s| s.to_string())).unwrap_or_default(),
    };
    assert!(m.contains("JSON text is not an object"), "{}", m);
}
