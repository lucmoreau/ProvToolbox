// T46: the generated Rust typed bean of a subtype converts to the general bean and back as the Java one does
// (T36SubtypeTest.testTypedBeansConvertToTheGeneralBeanAndBack, same values, same texts).

use t36_bean::json_text::{JsonText, Object, Value};
use t36_bean::org::example::t36::approval::client::integrator::approval_big_inputs::ApprovalBigInputs as Big;
use t36_bean::org::example::t36::approval::client::integrator::approval_inputs::ApprovalInputs as General;

fn big() -> Big {
    let mut b = Big::new();
    b.approver = Some("ex:agent/1".to_string());
    b.order = Some("ex:order/7".to_string());
    b.level = Some(4);
    b.price_before = Some(12.5);
    b.unit_code = Some("kg".to_string());
    b.payload = Some("{ \"a\" : [1, 2.0] }".to_string());
    b.customer_hash_other.insert("note".to_string(), "hello");
    b.customer_hash_other.insert("n".to_string(), 3i64);
    b
}

fn general(approver_name: &str, carrier: Option<&str>) -> General {
    let mut g = General::new();
    g.approver_name = Some(approver_name.to_string());
    g.customer_hash = carrier.map(|c| c.to_string());
    g
}

fn panic_message<R>(f: impl FnOnce() -> R + std::panic::UnwindSafe) -> String {
    let r = std::panic::catch_unwind(f);
    match r {
        Ok(_) => panic!("it did not panic"),
        Err(e) => e.downcast_ref::<String>().cloned().or_else(|| e.downcast_ref::<&str>().map(|s| s.to_string())).unwrap_or_default(),
    }
}

#[test]
fn the_typed_bean_is_a_subtype_of_the_template() {
    assert_eq!(Big::REFINES, "t31.Approval");
    assert_eq!(Big::new().is_a, "t31.approval_big");
}

#[test]
fn to_general_copies_assigns_the_fixed_value_and_writes_the_carrier() {
    let g = big().to_general();
    assert_eq!(g.approver, Some("ex:agent/1".to_string()));
    assert_eq!(g.order, Some("ex:order/7".to_string()));
    assert_eq!(g.level, Some(4));
    assert_eq!(g.approver_name, Some("Admin".to_string()), "the fixed value");
    assert_eq!(
        g.customer_hash.as_deref(),
        Some(r#"{"priceBefore":12.5,"unit":"kg","payload":{"a":[1,2]},"note":"hello","n":3}"#)
    );
}

#[test]
fn from_general_gives_it_back_the_other_keys_included() {
    let g = big().to_general();
    let b2 = Big::from_general(g.clone());
    assert_eq!(b2.price_before, Some(12.5));
    assert_eq!(b2.unit_code, Some("kg".to_string()));
    assert_eq!(b2.payload.as_deref(), Some(r#"{"a":[1,2]}"#));
    assert_eq!(b2.approver, Some("ex:agent/1".to_string()));
    assert_eq!(b2.customer_hash_other.get("note"), Some(&Value::Str("hello".to_string())));
    assert_eq!(b2.customer_hash_other.get("n"), Some(&Value::Int(3)));
    assert_eq!(b2.customer_hash_other.len(), 2);
    assert_eq!(b2.to_general().customer_hash, g.customer_hash);
}

#[test]
fn a_carrier_with_other_keys_in_another_order_is_the_same_object_back() {
    let original = r#"{"note":"x","priceBefore":1,"zzz":[1,{"q":null}],"unit":"m"}"#;
    let b3 = Big::from_general(general("Admin", Some(original)));
    assert_eq!(b3.customer_hash_other.len(), 2);
    let back = b3.to_general().customer_hash.unwrap();
    assert_eq!(back, r#"{"priceBefore":1,"unit":"m","note":"x","zzz":[1,{"q":null}]}"#, "declared keys first");
    let a: Object = JsonText::read(original).unwrap();
    let b: Object = JsonText::read(&back).unwrap();
    let mut a = a.0.clone();
    let mut b = b.0.clone();
    a.sort_by(|x, y| x.0.cmp(&y.0));
    b.sort_by(|x, y| x.0.cmp(&y.0));
    assert_eq!(a, b, "the same JSON object");
}

#[test]
fn an_absent_optional_carrier_is_an_empty_object_and_absent_values_are_none() {
    let m = panic_message(|| Big::from_general(general("Admin", None)));
    assert_eq!(m, "T36: subtype t31.approval_big: the required value priceBefore is absent");
    let b4 = Big::from_general(general("Admin", Some(r#"{"priceBefore":2}"#)));
    assert_eq!(b4.unit_code, None);
    assert_eq!(b4.payload, None);
    assert_eq!(b4.to_general().customer_hash.as_deref(), Some(r#"{"priceBefore":2}"#), "absent optional values are not written");
}

#[test]
fn a_general_bean_that_is_not_the_subtypes_is_refused() {
    let m = panic_message(|| Big::from_general(general("Other", Some(r#"{"priceBefore":2}"#))));
    assert_eq!(m, "T36: subtype t31.approval_big: the bean's approver_name is not the subtype's Admin");
}

#[test]
fn refusals_of_to_general() {
    let m = panic_message(|| Big::new().to_general());
    assert_eq!(m, "T36: subtype t31.approval_big: the required value priceBefore is absent");
    let mut bad = Big::new();
    bad.price_before = Some(1.0);
    bad.customer_hash_other.insert("priceBefore".to_string(), 9i32);
    let m = panic_message(move || bad.to_general());
    assert!(m.contains("subtype t31.approval_big, carrier customer_hash") && m.contains("key priceBefore"), "{}", m);
}

#[test]
fn the_typed_bean_is_serialised_with_its_other_keys() {
    let b = big();
    let text = b.to_json().unwrap();
    let v: serde_json::Value = serde_json::from_str(&text).unwrap();
    assert_eq!(v["isA"], "t31.approval_big");
    assert_eq!(v["priceBefore"], 12.5);
    assert_eq!(v["customer_hashOther"]["note"], "hello");
    assert_eq!(v["customer_hashOther"]["n"], 3);
    let back: Big = serde_json::from_str(&text).unwrap();
    assert_eq!(back.price_before, Some(12.5));
    assert_eq!(back.customer_hash_other.get("note"), Some(&Value::Str("hello".to_string())));
}
