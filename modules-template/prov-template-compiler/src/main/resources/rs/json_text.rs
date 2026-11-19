//! EXPERIMENTAL: in-JSON values, subtypes, performatives and flows (T36, T41, T46) are experimental functionality; this module may change without notice.
//!
//! T36/T42: JSON text for the values a template keeps inside a text variable (an `@inJson` carrier): the Rust
//! counterpart of `org.openprovenance.prov.utils.JsonText` (Java), of the JavaScript candy and of the Python module of the same
//! name. All four give the same text for the same values: the vectors `json-text-vectors.json` are run by every port.
//!
//! Standard library only, as the Java class has no library either.
//!
//! The text a value gets: an integer as a JSON integer; a double as RFC 8785 gives it (the ECMAScript Number to string rule: `5.0` is
//! `5`, `1e21` is `1e+21`, `-0` is `0`); a boolean as `true` or `false`; a string as a JSON string, escaping what RFC 8785 escapes;
//! `null`; a nested object or list as the JSON it is. No space is written between tokens. The members of an object are written in
//! the order of the object, which is not RFC 8785's sorted one: the typed beans write the declared keys first.
//!
//! **The digits of a double.** The shortest that read back; of those of that length the closest to the number; an exact tie to
//! the even digit. Rust's own shortest digits (`{:e}`) go up on an exact tie (2^-25 is `2.9802322387695313e-8`, the rule gives `…312e-8`),
//! so the tie is checked here. The digits of Rust are used otherwise, because they are right where the interval of the numbers that
//! read back as a power of two is narrower below: the closest digits of that length may not read back.
//!
//! **A difference from the other ports.** A string is UTF-8 and cannot hold a lone surrogate, so a `\ud800`-style escape that is not
//! half of a pair is refused, where Java and JavaScript keep it.
//!
//! An `Object` is a list of members, which keeps the order they were put in; a key put twice keeps its first place. It has the
//! methods of a map that generated code calls (`insert`, `get`, `contains_key`), taking any value that is a `ToValue`.

use std::fmt;

/// A JSON value. An integer without fraction or exponent that fits an `i64` is read as `Int`, any other number as `Double`.
#[derive(Debug, Clone, PartialEq)]
pub enum Value {
    Null,
    Bool(bool),
    Int(i64),
    Double(f64),
    Str(String),
    List(Vec<Value>),
    Object(Object),
}

/// The members of an object, in the order they were put.
#[derive(Debug, Clone, PartialEq, Default)]
pub struct Object(pub Vec<(String, Value)>);

impl std::ops::Deref for Object {
    type Target = Vec<(String, Value)>;
    fn deref(&self) -> &Self::Target {
        &self.0
    }
}

impl std::ops::DerefMut for Object {
    fn deref_mut(&mut self) -> &mut Self::Target {
        &mut self.0
    }
}

impl Object {
    pub fn new() -> Object {
        Object(Vec::new())
    }

    /// Puts a member: an existing key keeps its place and takes the new value. A `None` is the JSON null.
    pub fn insert<V: ToValue>(&mut self, key: String, value: V) {
        put(self, &key, value.to_value());
    }

    /// The value of a member, if there is one.
    pub fn get(&self, key: &str) -> Option<&Value> {
        self.0.iter().find(|m| m.0 == key).map(|m| &m.1)
    }

    pub fn contains_key(&self, key: &str) -> bool {
        self.get(key).is_some()
    }
}

/// What a generated bean's field or constant is, as a JSON value: a string, a number, a boolean, a value, or an `Option` of one of
/// them (`None` is the JSON null). Generated code passes `&x` for any of them.
pub trait ToValue {
    fn to_value(&self) -> Value;
}

impl ToValue for Value {
    fn to_value(&self) -> Value {
        self.clone()
    }
}
impl ToValue for str {
    fn to_value(&self) -> Value {
        Value::Str(self.to_string())
    }
}
impl ToValue for String {
    fn to_value(&self) -> Value {
        Value::Str(self.clone())
    }
}
impl ToValue for bool {
    fn to_value(&self) -> Value {
        Value::Bool(*self)
    }
}
macro_rules! int_to_value {
    ($($t:ty),*) => { $( impl ToValue for $t { fn to_value(&self) -> Value { Value::Int(*self as i64) } } )* };
}
int_to_value!(i8, i16, i32, i64, u8, u16, u32);
impl ToValue for f64 {
    fn to_value(&self) -> Value {
        Value::Double(*self)
    }
}
impl ToValue for f32 {
    fn to_value(&self) -> Value {
        // the shortest decimal of the float, as Java's Float.toString gives it
        match self.to_string().parse::<f64>() {
            Ok(d) => Value::Double(d),
            Err(_) => Value::Null,
        }
    }
}
impl<T: ToValue + ?Sized> ToValue for &T {
    fn to_value(&self) -> Value {
        (**self).to_value()
    }
}
impl<T: ToValue> ToValue for Option<T> {
    fn to_value(&self) -> Value {
        match self {
            Some(v) => v.to_value(),
            None => Value::Null,
        }
    }
}
impl ToValue for Object {
    fn to_value(&self) -> Value {
        Value::Object(self.clone())
    }
}

/// What a generated bean holds as text: a string, or an `Option` of one (`None` is an absent text).
pub trait AsText {
    fn as_text(&self) -> Option<&str>;
}
impl AsText for str {
    fn as_text(&self) -> Option<&str> {
        Some(self)
    }
}
impl AsText for String {
    fn as_text(&self) -> Option<&str> {
        Some(self.as_str())
    }
}
impl<T: AsText + ?Sized> AsText for &T {
    fn as_text(&self) -> Option<&str> {
        (**self).as_text()
    }
}
impl<T: AsText> AsText for Option<T> {
    fn as_text(&self) -> Option<&str> {
        self.as_ref().and_then(|t| t.as_text())
    }
}

/// The reason a text or a value is refused.
#[derive(Debug, Clone, PartialEq)]
pub struct JsonTextError(pub String);

impl fmt::Display for JsonTextError {
    fn fmt(&self, f: &mut fmt::Formatter<'_>) -> fmt::Result {
        write!(f, "{}", self.0)
    }
}

impl std::error::Error for JsonTextError {}

pub type Result<T> = std::result::Result<T, JsonTextError>;

fn refused<T>(why: impl Into<String>) -> Result<T> {
    Err(JsonTextError(format!("T36: {}", why.into())))
}

/// Puts a member in an object: an existing key keeps its place and takes the new value.
pub fn put(object: &mut Object, key: &str, value: Value) {
    for m in object.iter_mut() {
        if m.0 == key {
            m.1 = value;
            return;
        }
    }
    object.push((key.to_string(), value));
}

fn get<'a>(object: &'a Object, key: &str) -> Option<&'a Value> {
    Object::get(object, key)
}

pub struct JsonText;

impl JsonText {
    /// A new empty object: what the generated code builds a carrier in.
    pub fn new_object() -> Object {
        Object::new()
    }

    // ---- writing ------------------------------------------------------------------------------------------------------

    /// The text of an object, members in the object's order.
    pub fn write(object: &Object) -> Result<String> {
        let mut out = String::new();
        write_object(&mut out, object)?;
        Ok(out)
    }

    /// The text of any JSON value.
    pub fn to_text(value: &Value) -> Result<String> {
        let mut out = String::new();
        write_value(&mut out, value)?;
        Ok(out)
    }

    /// The text of a value, for the conversion "as text" of a flow: a number or a boolean as the carrier writes it (but a number
    /// in plain digits, never with an exponent: `1e+21` is `1000000000000000000000`, `1e-7` is `0.0000001`), a string as itself, and
    /// null as `None` (not the text `null`, which `to_text` writes). An object or a list has no text.
    pub fn text<V: ToValue + ?Sized>(value: &V) -> Result<Option<String>> {
        Self::text_of(&value.to_value())
    }

    pub fn text_of(value: &Value) -> Result<Option<String>> {
        match value {
            Value::Null => Ok(None),
            Value::Str(s) => Ok(Some(s.clone())),
            Value::Bool(_) | Value::Int(_) => Ok(Some(Self::to_text(value)?)),
            Value::Double(d) => Ok(Some(plain(&number(*d)?))),
            _ => refused("a value of that kind has no text"),
        }
    }

    // ---- reading ------------------------------------------------------------------------------------------------------

    /// The object a text holds. An absent text (blank) is an empty object; any other JSON value is refused.
    pub fn read<T: AsText + ?Sized>(text: &T) -> Result<Object> {
        let text = text.as_text().unwrap_or("");
        if text.trim().is_empty() {
            return Ok(Object::new());
        }
        match Self::parse(text)? {
            Value::Object(o) => Ok(o),
            _ => refused("JSON text is not an object"),
        }
    }

    /// The value a text holds, whatever it is.
    pub fn parse<T: AsText + ?Sized>(text: &T) -> Result<Value> {
        let text = match text.as_text() {
            Some(t) => t,
            None => return Ok(Value::Null),
        };
        let mut p = Parser { s: text.as_bytes(), i: 0 };
        p.ws();
        let v = p.value()?;
        p.ws();
        if p.i != p.s.len() {
            return Err(p.error("trailing characters"));
        }
        Ok(v)
    }

    // ---- what the typed beans use -------------------------------------------------------------------------------------

    /// The members of a carrier, the declared ones first and then the other keys, as one object to write. A key in `others` that is
    /// also in `declared` is refused, so that a declared value is not shadowed unseen. `where_` names the subtype and the carrier.
    pub fn merge(declared: &Object, others: &Object, where_: &str) -> Result<Object> {
        let mut m = declared.clone();
        for (k, v) in others.iter() {
            if get(declared, k).is_some() {
                return refused(format!("{}: the key {} is declared, it cannot also be among the other keys", where_, k));
            }
            put(&mut m, k, v.clone());
        }
        Ok(m)
    }

    /// The members of an object whose key is not one of the declared keys, in the object's order.
    pub fn others(object: &Object, declared: &[&str]) -> Object {
        Object(object.iter().filter(|m| !declared.contains(&m.0.as_str())).cloned().collect())
    }

    /// A string member; absent (or null) is `None`.
    pub fn get_string(m: &Object, key: &str) -> Result<Option<String>> {
        match get(m, key) {
            None | Some(Value::Null) => Ok(None),
            Some(Value::Str(s)) => Ok(Some(s.clone())),
            Some(_) => not_a(key, "a string"),
        }
    }

    /// A member read back as the JSON text of its value (for a `json` value); absent (or null) is `None`.
    pub fn get_json(m: &Object, key: &str) -> Result<Option<String>> {
        match get(m, key) {
            None | Some(Value::Null) => Ok(None),
            Some(v) => Ok(Some(Self::to_text(v)?)),
        }
    }

    pub fn get_boolean(m: &Object, key: &str) -> Result<Option<bool>> {
        match get(m, key) {
            None | Some(Value::Null) => Ok(None),
            Some(Value::Bool(b)) => Ok(Some(*b)),
            Some(_) => not_a(key, "true or false"),
        }
    }

    pub fn get_long(m: &Object, key: &str) -> Result<Option<i64>> {
        match get(m, key) {
            None | Some(Value::Null) => Ok(None),
            Some(Value::Int(i)) => Ok(Some(*i)),
            Some(Value::Double(d)) if *d == d.round() && d.abs() < 9.007199254740992e15 => Ok(Some(*d as i64)),
            Some(_) => not_a(key, "an integer"),
        }
    }

    pub fn get_integer(m: &Object, key: &str) -> Result<Option<i32>> {
        match Self::get_long(m, key)? {
            None => Ok(None),
            Some(l) if l < i32::MIN as i64 || l > i32::MAX as i64 => not_a(key, "an integer that fits an int"),
            Some(l) => Ok(Some(l as i32)),
        }
    }

    pub fn get_double(m: &Object, key: &str) -> Result<Option<f64>> {
        match get(m, key) {
            None | Some(Value::Null) => Ok(None),
            Some(Value::Int(i)) => Ok(Some(*i as f64)),
            Some(Value::Double(d)) => Ok(Some(*d)),
            Some(_) => not_a(key, "a number"),
        }
    }

    pub fn get_float(m: &Object, key: &str) -> Result<Option<f32>> {
        Ok(Self::get_double(m, key)?.map(|d| d as f32))
    }

    /// Whether a value is a constant: equal, numbers by value (`3` as an int is `3` as a double).
    pub fn same<A: ToValue + ?Sized, B: ToValue + ?Sized>(actual: &A, constant: &B) -> bool {
        Self::same_value(&actual.to_value(), &constant.to_value())
    }

    pub fn same_value(actual: &Value, constant: &Value) -> bool {
        match (actual, constant) {
            (Value::Int(a), Value::Int(b)) => a == b,
            (Value::Int(i), Value::Double(d)) | (Value::Double(d), Value::Int(i)) => *d == d.round() && d.abs() < 9.2e18 && *d as i64 == *i,
            (Value::Double(a), Value::Double(b)) => a == b || (a.is_nan() && b.is_nan()),
            _ => actual == constant,
        }
    }
}

fn not_a<T>(key: &str, what: &str) -> Result<T> {
    refused(format!("the member {} is not {}", key, what))
}

// ---- writing -----------------------------------------------------------------------------------------------------------

fn write_value(out: &mut String, v: &Value) -> Result<()> {
    match v {
        Value::Null => out.push_str("null"),
        Value::Bool(b) => out.push_str(if *b { "true" } else { "false" }),
        Value::Int(i) => out.push_str(&i.to_string()),
        Value::Double(d) => out.push_str(&number(*d)?),
        Value::Str(s) => write_string(out, s),
        Value::List(l) => {
            out.push('[');
            for (n, x) in l.iter().enumerate() {
                if n > 0 {
                    out.push(',');
                }
                write_value(out, x)?;
            }
            out.push(']');
        }
        Value::Object(o) => write_object(out, o)?,
    }
    Ok(())
}

fn write_object(out: &mut String, o: &Object) -> Result<()> {
    out.push('{');
    for (n, (k, v)) in o.iter().enumerate() {
        if n > 0 {
            out.push(',');
        }
        write_string(out, k);
        out.push(':');
        write_value(out, v)?;
    }
    out.push('}');
    Ok(())
}

fn write_string(out: &mut String, s: &str) {
    out.push('"');
    for c in s.chars() {
        match c {
            '"' => out.push_str("\\\""),
            '\\' => out.push_str("\\\\"),
            '\u{8}' => out.push_str("\\b"),
            '\u{c}' => out.push_str("\\f"),
            '\n' => out.push_str("\\n"),
            '\r' => out.push_str("\\r"),
            '\t' => out.push_str("\\t"),
            c if (c as u32) < 0x20 => out.push_str(&format!("\\u{:04x}", c as u32)),
            c => out.push(c),
        }
    }
    out.push('"');
}

// ---- numbers -----------------------------------------------------------------------------------------------------------

/// RFC 8785 (ECMAScript Number::toString) for a double; -0 is `0`; NaN and infinities are refused.
pub fn number(d: f64) -> Result<String> {
    if d.is_nan() || d.is_infinite() {
        return refused(format!("not a JSON number: {}", d));
    }
    if d == 0.0 {
        return Ok("0".to_string());
    }
    let (digits, exponent) = shortest(d.abs());
    let k = digits.len() as i32;
    let n = exponent + 1; // the value is 0.<digits> x 10^n
    let mut s = String::new();
    if d < 0.0 {
        s.push('-');
    }
    if k <= n && n <= 21 {
        s.push_str(&digits);
        s.push_str(&"0".repeat((n - k) as usize));
    } else if 0 < n && n <= 21 {
        s.push_str(&digits[..n as usize]);
        s.push('.');
        s.push_str(&digits[n as usize..]);
    } else if -6 < n && n <= 0 {
        s.push_str("0.");
        s.push_str(&"0".repeat((-n) as usize));
        s.push_str(&digits);
    } else {
        let e = n - 1;
        s.push_str(&digits[..1]);
        if k > 1 {
            s.push('.');
            s.push_str(&digits[1..]);
        }
        s.push('e');
        s.push(if e < 0 { '-' } else { '+' });
        s.push_str(&e.abs().to_string());
    }
    Ok(s)
}

/// The digits (no point) and the decimal exponent of the first of them, of a positive finite double: the shortest that read back,
/// of those the closest, an exact tie to the even digit.
fn shortest(x: f64) -> (String, i32) {
    let (mut digits, exponent) = split_e(&format!("{:e}", x));
    let k = digits.len();
    // An exact tie needs the exact value to have k+1 digits, the last a 5. Rust's rounding of x to k+1 digits is exact then.
    let (wide, wide_exponent) = split_e(&format!("{:.*e}", k, x));
    if wide_exponent == exponent && wide.len() == k + 1 && wide.ends_with('5') {
        let (exact, _) = split_e(&format!("{:.800e}", x)); // a double has at most 767 significant digits
        if exact[k + 1..].bytes().all(|b| b == b'0') {
            let down = wide[..k].to_string();
            let up = increment(&down);
            for candidate in [Some(down), up].into_iter().flatten() {
                let last = candidate.as_bytes()[k - 1] - b'0';
                if last % 2 == 0 && reads_back(&candidate, exponent, x) {
                    digits = candidate;
                    break;
                }
            }
        }
    }
    (digits, exponent)
}

/// "d.ddde-5" or "de5" as the digits without the point and the exponent.
fn split_e(text: &str) -> (String, i32) {
    let (m, e) = text.split_once('e').expect("{:e} has an exponent");
    (m.chars().filter(|c| *c != '.').collect(), e.parse().expect("an exponent"))
}

/// The digits plus one in the last place; None if that carries into a new digit.
fn increment(digits: &str) -> Option<String> {
    let mut b: Vec<u8> = digits.bytes().collect();
    for i in (0..b.len()).rev() {
        if b[i] == b'9' {
            b[i] = b'0';
        } else {
            b[i] += 1;
            return Some(String::from_utf8(b).unwrap());
        }
    }
    None
}

fn reads_back(digits: &str, exponent: i32, x: f64) -> bool {
    format!("{}.{}e{}", &digits[..1], &digits[1..], exponent).parse::<f64>().map(|y| y == x).unwrap_or(false)
}

/// The text of a number without its exponent: the decimal point of RFC 8785's text ("1.2345e+25", "-5e-324") moved by the exponent.
pub fn plain(text: &str) -> String {
    let e = match text.find('e') {
        None => return text.to_string(),
        Some(e) => e,
    };
    let mut mantissa = &text[..e];
    let exponent = &text[e + 1..];
    let exp: i32 = exponent.strip_prefix('+').unwrap_or(exponent).parse().expect("an exponent");
    let negative = mantissa.starts_with('-');
    if negative {
        mantissa = &mantissa[1..];
    }
    let dot = mantissa.find('.');
    let digits: String = match dot {
        None => mantissa.to_string(),
        Some(d) => format!("{}{}", &mantissa[..d], &mantissa[d + 1..]),
    };
    let point = dot.unwrap_or(mantissa.len()) as i32 + exp; // where the decimal point falls in the digits
    let mut s = String::new();
    if negative {
        s.push('-');
    }
    if point >= digits.len() as i32 {
        s.push_str(&digits);
        s.push_str(&"0".repeat(point as usize - digits.len()));
    } else if point > 0 {
        s.push_str(&digits[..point as usize]);
        s.push('.');
        s.push_str(&digits[point as usize..]);
    } else {
        s.push_str("0.");
        s.push_str(&"0".repeat((-point) as usize));
        s.push_str(&digits);
    }
    s
}

// ---- reading -----------------------------------------------------------------------------------------------------------

struct Parser<'a> {
    s: &'a [u8],
    i: usize,
}

impl<'a> Parser<'a> {
    fn error(&self, why: &str) -> JsonTextError {
        JsonTextError(format!("T36: JSON text, offset {}: {}", self.i, why))
    }

    fn ws(&mut self) {
        while self.i < self.s.len() && matches!(self.s[self.i], b' ' | b'\t' | b'\n' | b'\r') {
            self.i += 1;
        }
    }

    fn value(&mut self) -> Result<Value> {
        if self.i >= self.s.len() {
            return Err(self.error("unexpected end"));
        }
        match self.s[self.i] {
            b'{' => self.object(),
            b'[' => self.array(),
            b'"' => Ok(Value::Str(self.string()?)),
            b't' => self.literal("true", Value::Bool(true)),
            b'f' => self.literal("false", Value::Bool(false)),
            b'n' => self.literal("null", Value::Null),
            c if c == b'-' || c.is_ascii_digit() => self.number(),
            c => Err(self.error(&format!("unexpected character '{}'", c as char))),
        }
    }

    fn literal(&mut self, word: &str, v: Value) -> Result<Value> {
        if !self.s[self.i..].starts_with(word.as_bytes()) {
            return Err(self.error(&format!("expected {}", word)));
        }
        self.i += word.len();
        Ok(v)
    }

    fn object(&mut self) -> Result<Value> {
        let mut m: Object = Object::new();
        self.i += 1;
        self.ws();
        if self.i < self.s.len() && self.s[self.i] == b'}' {
            self.i += 1;
            return Ok(Value::Object(m));
        }
        loop {
            self.ws();
            if self.i >= self.s.len() || self.s[self.i] != b'"' {
                return Err(self.error("expected a key"));
            }
            let k = self.string()?;
            self.ws();
            if self.i >= self.s.len() || self.s[self.i] != b':' {
                return Err(self.error("expected ':'"));
            }
            self.i += 1;
            self.ws();
            let v = self.value()?;
            put(&mut m, &k, v);
            self.ws();
            if self.i >= self.s.len() {
                return Err(self.error("unterminated object"));
            }
            match self.s[self.i] {
                b',' => self.i += 1,
                b'}' => {
                    self.i += 1;
                    return Ok(Value::Object(m));
                }
                _ => return Err(self.error("expected ',' or '}'")),
            }
        }
    }

    fn array(&mut self) -> Result<Value> {
        let mut l = Vec::new();
        self.i += 1;
        self.ws();
        if self.i < self.s.len() && self.s[self.i] == b']' {
            self.i += 1;
            return Ok(Value::List(l));
        }
        loop {
            self.ws();
            l.push(self.value()?);
            self.ws();
            if self.i >= self.s.len() {
                return Err(self.error("unterminated array"));
            }
            match self.s[self.i] {
                b',' => self.i += 1,
                b']' => {
                    self.i += 1;
                    return Ok(Value::List(l));
                }
                _ => return Err(self.error("expected ',' or ']'")),
            }
        }
    }

    fn hex4(&mut self) -> Result<u32> {
        if self.i + 4 > self.s.len() {
            return Err(self.error("short \\u escape"));
        }
        let t = std::str::from_utf8(&self.s[self.i..self.i + 4]).map_err(|_| self.error("bad \\u escape"))?;
        let v = u32::from_str_radix(t, 16).map_err(|_| self.error("bad \\u escape"))?;
        if t.starts_with('+') || t.starts_with('-') {
            return Err(self.error("bad \\u escape"));
        }
        self.i += 4;
        Ok(v)
    }

    fn string(&mut self) -> Result<String> {
        let mut out: Vec<u8> = Vec::new();
        self.i += 1;
        loop {
            if self.i >= self.s.len() {
                return Err(self.error("unterminated string"));
            }
            let c = self.s[self.i];
            self.i += 1;
            if c == b'"' {
                return String::from_utf8(out).map_err(|_| self.error("not UTF-8"));
            }
            if c < 0x20 {
                return Err(self.error("control character in string"));
            }
            if c != b'\\' {
                out.push(c);
                continue;
            }
            if self.i >= self.s.len() {
                return Err(self.error("unterminated escape"));
            }
            let e = self.s[self.i];
            self.i += 1;
            match e {
                b'"' => out.push(b'"'),
                b'\\' => out.push(b'\\'),
                b'/' => out.push(b'/'),
                b'b' => out.push(8),
                b'f' => out.push(12),
                b'n' => out.push(b'\n'),
                b'r' => out.push(b'\r'),
                b't' => out.push(b'\t'),
                b'u' => {
                    let mut cp = self.hex4()?;
                    if (0xd800..0xdc00).contains(&cp) {
                        // a high surrogate: the low one must follow as another escape
                        if self.s[self.i..].starts_with(b"\\u") {
                            self.i += 2;
                            let low = self.hex4()?;
                            if !(0xdc00..0xe000).contains(&low) {
                                return Err(self.error("a lone surrogate (a string here is UTF-8)"));
                            }
                            cp = 0x10000 + ((cp - 0xd800) << 10) + (low - 0xdc00);
                        } else {
                            return Err(self.error("a lone surrogate (a string here is UTF-8)"));
                        }
                    } else if (0xdc00..0xe000).contains(&cp) {
                        return Err(self.error("a lone surrogate (a string here is UTF-8)"));
                    }
                    let ch = char::from_u32(cp).ok_or_else(|| self.error("bad \\u escape"))?;
                    let mut buf = [0u8; 4];
                    out.extend_from_slice(ch.encode_utf8(&mut buf).as_bytes());
                }
                _ => return Err(self.error(&format!("bad escape \\{}", e as char))),
            }
        }
    }

    fn number(&mut self) -> Result<Value> {
        let start = self.i;
        if self.s[self.i] == b'-' {
            self.i += 1;
        }
        if self.i >= self.s.len() {
            return Err(self.error("bad number"));
        }
        if self.s[self.i] == b'0' {
            self.i += 1;
        } else if (b'1'..=b'9').contains(&self.s[self.i]) {
            while self.i < self.s.len() && self.s[self.i].is_ascii_digit() {
                self.i += 1;
            }
        } else {
            return Err(self.error("bad number"));
        }
        let mut integral = true;
        if self.i < self.s.len() && self.s[self.i] == b'.' {
            integral = false;
            self.i += 1;
            let d = self.i;
            while self.i < self.s.len() && self.s[self.i].is_ascii_digit() {
                self.i += 1;
            }
            if d == self.i {
                return Err(self.error("bad number"));
            }
        }
        if self.i < self.s.len() && (self.s[self.i] == b'e' || self.s[self.i] == b'E') {
            integral = false;
            self.i += 1;
            if self.i < self.s.len() && (self.s[self.i] == b'+' || self.s[self.i] == b'-') {
                self.i += 1;
            }
            let d = self.i;
            while self.i < self.s.len() && self.s[self.i].is_ascii_digit() {
                self.i += 1;
            }
            if d == self.i {
                return Err(self.error("bad number"));
            }
        }
        let text = std::str::from_utf8(&self.s[start..self.i]).expect("ASCII");
        if integral {
            if let Ok(i) = text.parse::<i64>() {
                return Ok(Value::Int(i));
            } // beyond an i64: a double
        }
        text.parse::<f64>().map(Value::Double).map_err(|_| self.error("bad number"))
    }
}
