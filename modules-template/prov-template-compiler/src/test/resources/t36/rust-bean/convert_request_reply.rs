// T46: the generated Rust request, reply and dispatcher of the subtypes do what the Java ones do
// (T36PerformativeTest.testTheRequestAndTheReplyCopyTheirValuesAndNoOther and T36SubtypeTest's dispatcher).

use std::any::Any;
use t36_bean::json_text::{Object, Value};
use t36_bean::org::example::t36::approval::client::integrator::approval_inputs::ApprovalInputs as General;
use t36_bean::org::example::t36::approval::client::integrator::plain_inputs::PlainInputs;
use t36_bean::org::example::t36::approval::client::integrator::reprice_inputs::RepriceInputs as Typed;
use t36_bean::org::example::t36::approval::client::integrator::reprice_reply::RepriceReply as Reply;
use t36_bean::org::example::t36::approval::client::integrator::reprice_request::RepriceRequest as Request;
use t36_bean::org::example::t36::catalogue::common::subtypes::Subtypes;

fn asked() -> Request {
    let mut r = Request::new();
    r.order_note = Some("please".to_string());
    r.price_before = Some(2.5);
    r.unit_code = Some("EA".to_string());
    r.memo = Some("m".to_string());
    let mut other = Object::new();
    other.insert("colour".to_string(), "red");
    r.extra_other = Some(other);
    r
}

#[test]
fn copy_into_copies_its_fields_into_the_bean_and_touches_no_other_and_of_gives_them_back() {
    let mut t = Typed::new();
    t.approver = Some("ex:agent".to_string());
    t.level = Some(3);
    t.payload = Some(r#"{"p":1}"#.to_string());
    let request = asked();
    let t = request.copy_into(t);
    assert_eq!(t.price_before, Some(2.5));
    assert_eq!(t.memo.as_deref(), Some("m"));
    assert_eq!(t.extra_other.get("colour"), Some(&Value::Str("red".to_string())));
    assert_eq!(t.approver.as_deref(), Some("ex:agent"));
    assert_eq!(t.level, Some(3));
    assert_eq!(t.payload.as_deref(), Some(r#"{"p":1}"#));
    assert_eq!(t.approver_ref, None, "the reply's values are not the request's to set");

    let back = Request::of(t);
    assert_eq!(back.order_note, request.order_note);
    assert_eq!(back.price_before, request.price_before);
    assert_eq!(back.unit_code, request.unit_code);
    assert_eq!(back.memo, request.memo);
    assert_eq!(back.extra_other, request.extra_other);
}

#[test]
fn an_absent_map_of_other_keys_leaves_the_typed_beans_own() {
    let t = Request::new().copy_into(Typed::new());
    assert!(t.extra_other.is_empty());
}

#[test]
fn the_reply_the_same_and_the_requests_value_is_left() {
    let mut answered = Reply::new();
    answered.approver_ref = Some("ref-1".to_string());
    answered.line_count = Some(7);
    let mut t = Typed::new();
    t.price_before = Some(9.0);
    let t = answered.copy_into(t);
    assert_eq!(t.approver_ref.as_deref(), Some("ref-1"));
    assert_eq!(t.line_count, Some(7));
    assert_eq!(t.price_before, Some(9.0), "the request's value is left");
    let back = Reply::of(t);
    assert_eq!(back.approver_ref, answered.approver_ref);
    assert_eq!(back.line_count, answered.line_count);
}

#[test]
fn the_request_feeds_the_one_record_the_other_keys_among_them() {
    let t = asked().copy_into(Typed::new());
    let general = t.to_general();
    // what the typed bean writes for each carrier: declared values first, then the other keys
    assert_eq!(general.extra.as_deref(), Some(r#"{"memo":"m","colour":"red"}"#));
    assert_eq!(general.customer_hash.as_deref(), Some(r#"{"priceBefore":2.5,"unit":"EA"}"#));
}

#[test]
fn the_dispatcher_chooses_the_subtype_by_the_values_the_bean_fixes() {
    let mut g = General::new();
    g.approver_name = Some("Admin".to_string());
    g.customer_hash = Some(r#"{"priceBefore":1}"#.to_string());
    assert_eq!(Subtypes::which_approval(g.clone()).as_deref(), Some("t31.reprice"));
    let typed: Box<dyn Any> = Subtypes::from_general_approval(g);
    assert!(typed.downcast_ref::<Typed>().is_some(), "the typed bean of the subtype");

    let mut other = General::new();
    other.approver_name = Some("Other".to_string());
    assert_eq!(Subtypes::which_approval(other.clone()).as_deref(), Some("t31.plain"));
    assert!(Subtypes::from_general_approval(other).downcast_ref::<PlainInputs>().is_some());

    let mut none = General::new();
    none.approver_name = Some("Nobody".to_string());
    assert_eq!(Subtypes::which_approval(none), None, "no subtype fixes these values");
}
