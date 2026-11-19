// T46: the generated Rust flow of a composite record does what the Java one does
// (T36FlowTest.testACompositeRecordIsBuiltInPartsAndTheApplicationLoops): built in parts, the application loops over the elements,
// the shared values are given to every element, the reply reads what the store made of the first.

use t36_bean::org::example::t36flow::actions::client::integrator::confirm_inputs::ConfirmInputs;
use t36_bean::org::example::t36flow::backend::client::integrator::changing_outputs::ChangingOutputs;
use t36_bean::org::example::t36flow::backend::client::integrator::exchanging_composite_inputs::ExchangingCompositeInputs;
use t36_bean::org::example::t36flow::backend::client::integrator::exchanging_composite_outputs::ExchangingCompositeOutputs;
use t36_bean::org::example::t36flow::backend::client::integrator::exchanging_inputs_1::ExchangingInputs1;
use t36_bean::org::example::t36flow::backend::client::integrator::exchanging_outputs::ExchangingOutputs;
use t36_bean::org::example::t36flow::backend::client::integrator::line_change_inputs::LineChangeInputs;
use t36_bean::org::example::t36flow::flows::bundle_flow::BundleFlow;
use t36_bean::org::example::t36flow::flows::bundle_flow_rules::BundleFlowRules;

#[derive(Clone, Default)]
struct Rules;

impl BundleFlowRules for Rules {
    fn caller(&self, _asked: &ConfirmInputs) -> String {
        "agent-7".to_string()
    }
    fn clock(&self, _asked: &ConfirmInputs) -> String {
        "2026-11-16T10:00:00Z".to_string()
    }
    fn document_node(&self, _asked: &ConfirmInputs) -> String {
        "D-new".to_string()
    }
    fn count(&self, _asked: &ConfirmInputs, _recorded: &ExchangingCompositeInputs) -> i32 {
        2
    }
    fn attributes(&self, _asked: &ConfirmInputs, _composite: &ExchangingCompositeInputs, _recorded: &ExchangingInputs1, _cited: &LineChangeInputs, _cited_made: &ChangingOutputs) -> String {
        "{}".to_string()
    }
    fn witness(&self, _asked: &ConfirmInputs, _composite: &ExchangingCompositeInputs) -> i32 {
        7
    }
}

#[test]
fn a_composite_record_is_built_in_parts_and_the_application_loops() {
    let rules = Rules;
    let asked = BundleFlow::ask(rules.clone());
    assert_eq!(asked.agent.as_deref(), Some("agent-7"));
    assert_eq!(asked.subject0.as_deref(), Some("D-new"));
    let mut bundle = BundleFlow::record_bundle(asked.clone(), rules.clone());
    assert_eq!(bundle.count, Some(2));
    assert_eq!(bundle.r#type.as_deref(), Some("exchanging"));
    assert!(bundle.elements.is_empty(), "no element yet: the application adds them");

    // the application loops: one element for each previous record it cites, each standing on its own
    for (document, name) in [("D1", "RFQ-1"), ("D2", "RFQ-2")] {
        let mut previous = LineChangeInputs::new();
        previous.name1 = Some(name.to_string());
        let mut previous_made = ChangingOutputs::new();
        previous_made.document1 = Some(document.to_string());
        let element = BundleFlow::record_bundle_element(asked.clone(), bundle.clone(), previous, previous_made, rules.clone());
        assert_eq!(element.document0.as_deref(), Some(document), "CHAINED from the element's own previous record");
        assert_eq!(element.name1.as_deref(), Some(name), "CARRIED OVER from it");
        assert_eq!(element.atype.as_deref(), Some("Citing"), "CONSTANT");
        assert_eq!(element.agent.as_deref(), Some("agent-7"), "AS ASKED: the user bean's agent");
        assert_eq!(element.time.as_deref(), Some("2026-11-16T10:00:00Z"), "AS ASKED: the user bean's time");
        assert_eq!(element.witness, None, "the shared witness is not the element's own to give: the sharing applies it");
        bundle.add_elements(element);
    }
    let bundle = BundleFlow::record_bundle_share(asked.clone(), bundle, rules.clone());
    assert_eq!(bundle.elements.len(), 2);
    for element in &bundle.elements {
        assert_eq!(element.witness, Some(7), "the shared variable is given to every element");
        assert_eq!(element.receipt, Some(-1), "a shared OUTPUT of the element template, a placeholder: the same new node for every element");
    }

    // what the store made of the composite: one outputs bean for each element; the shared output is read from the first (G8)
    let mut made = ExchangingCompositeOutputs::new();
    let reply = BundleFlow::answer(asked.clone(), bundle.clone(), made.clone());
    assert_eq!(serde_json::to_value(&reply).unwrap()["outcome"], "failed", "no element made: failed");
    for _ in 0..2 {
        let mut o = ExchangingOutputs::new();
        o.receipt = Some(5);
        made.elements.push(o);
    }
    let reply = BundleFlow::answer(asked, bundle, made);
    let reply = serde_json::to_value(&reply).unwrap();
    assert_eq!(reply["outcome"], "accepted");
    assert_eq!(reply["result"], "2");
}
