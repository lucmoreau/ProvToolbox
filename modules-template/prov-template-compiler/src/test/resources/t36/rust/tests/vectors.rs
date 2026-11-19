//! T36/T42: runs json-text-vectors.json against the Rust port of JsonText, as the Java, Python and JavaScript runners do.
//! Prints one line per failure and, last, `vectors: <n> run, <m> failed`.
#[path = "../../../../../main/resources/rs/json_text.rs"]
#[allow(dead_code)]
mod json_text;

use json_text::{number, JsonText, Value};

fn member<'a>(o: &'a json_text::Object, k: &str) -> Option<&'a Value> {
    o.iter().find(|m| m.0 == k).map(|m| &m.1)
}

fn text_of(v: Option<&Value>) -> String {
    match v {
        Some(Value::Str(s)) => s.clone(),
        other => panic!("a vector member that is not a string: {:?}", other),
    }
}

#[test]
fn conforms_to_the_vectors() {
    let file = format!("{}/../json-text-vectors.json", env!("CARGO_MANIFEST_DIR"));
    let vectors = match JsonText::parse(&std::fs::read_to_string(&file).expect("the vectors file")).expect("the vectors are JSON") {
        Value::Object(o) => o,
        _ => panic!("the vectors are an object"),
    };
    let section = |name: &str| -> Vec<json_text::Object> {
        match member(&vectors, name) {
            Some(Value::List(l)) => l.iter().map(|x| match x { Value::Object(o) => o.clone(), _ => panic!("a vector is an object") }).collect(),
            _ => panic!("no section {}", name),
        }
    };
    let (mut run, mut failed) = (0, 0);
    let mut fail = |title: &str, why: String| {
        failed += 1;
        println!("FAIL {}: {}", title, why);
    };

    for e in section("canonical") {
        run += 1;
        let title = text_of(member(&e, "title"));
        match JsonText::read(&text_of(member(&e, "input"))).and_then(|o| JsonText::write(&o)) {
            Ok(got) if got == text_of(member(&e, "output")) => {}
            Ok(got) => fail(&title, format!("wrote {}, expected {}", got, text_of(member(&e, "output")))),
            Err(x) => fail(&title, format!("raised {}", x)),
        }
    }
    for e in section("refused") {
        run += 1;
        let title = text_of(member(&e, "title"));
        if JsonText::read(&text_of(member(&e, "input"))).is_ok() {
            fail(&title, "accepted".to_string());
        }
    }
    for e in section("getters") {
        run += 1;
        let title = text_of(member(&e, "title"));
        let obj = JsonText::read(&text_of(member(&e, "input"))).expect("a getter vector's input is an object");
        let key = text_of(member(&e, "key"));
        let got: json_text::Result<Value> = match text_of(member(&e, "method")).as_str() {
            "getString" => JsonText::get_string(&obj, &key).map(|o| o.map_or(Value::Null, Value::Str)),
            "getJson" => JsonText::get_json(&obj, &key).map(|o| o.map_or(Value::Null, Value::Str)),
            "getBoolean" => JsonText::get_boolean(&obj, &key).map(|o| o.map_or(Value::Null, Value::Bool)),
            "getLong" => JsonText::get_long(&obj, &key).map(|o| o.map_or(Value::Null, Value::Int)),
            "getInteger" => JsonText::get_integer(&obj, &key).map(|o| o.map_or(Value::Null, |i| Value::Int(i as i64))),
            "getDouble" => JsonText::get_double(&obj, &key).map(|o| o.map_or(Value::Null, Value::Double)),
            m => panic!("a getter {} this runner does not know", m),
        };
        if member(&e, "refused") == Some(&Value::Bool(true)) {
            if got.is_ok() {
                fail(&title, "accepted".to_string());
            }
            continue;
        }
        let want = member(&e, "result").cloned().unwrap_or(Value::Null);
        match got {
            Ok(g) if JsonText::same(&g, &want) || g == want => {}
            Ok(g) => fail(&title, format!("got {:?}, expected {:?}", g, want)),
            Err(x) => fail(&title, format!("raised {}", x)),
        }
    }
    for e in section("text") {
        run += 1;
        let title = text_of(member(&e, "title"));
        let value = JsonText::parse(&text_of(member(&e, "input"))).expect("a text vector's input is JSON");
        let got = JsonText::text(&value);
        if member(&e, "refused") == Some(&Value::Bool(true)) {
            if got.is_ok() {
                fail(&title, "accepted".to_string());
            }
            continue;
        }
        let want = match member(&e, "result") {
            Some(Value::Str(s)) => Some(s.clone()),
            _ => None,
        };
        match got {
            Ok(g) if g == want => {}
            Ok(g) => fail(&title, format!("got {:?}, expected {:?}", g, want)),
            Err(x) => fail(&title, format!("raised {}", x)),
        }
    }
    for e in section("numbers") {
        run += 1;
        let title = text_of(member(&e, "title"));
        let d = f64::from_bits(u64::from_str_radix(&text_of(member(&e, "bits")), 16).expect("bits"));
        match number(d) {
            Ok(got) if got == text_of(member(&e, "canonical")) => {}
            Ok(got) => fail(&title, format!("wrote {}, expected {}", got, text_of(member(&e, "canonical")))),
            Err(x) => fail(&title, format!("raised {}", x)),
        }
        match JsonText::text(&Value::Double(d)) {
            Ok(Some(got)) if got == text_of(member(&e, "text")) => {}
            other => fail(&title, format!("text {:?}, expected {}", other, text_of(member(&e, "text")))),
        }
    }
    println!("vectors: {} run, {} failed", run, failed);
    assert_eq!(failed, 0, "{} vectors failed", failed);
}
