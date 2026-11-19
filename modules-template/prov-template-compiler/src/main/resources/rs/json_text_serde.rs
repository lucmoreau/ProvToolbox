//! EXPERIMENTAL (T46, may change without notice): serde for the `Object` of `json_text.rs`, for the field of a typed bean that holds the other keys of a carrier
//! (`#[serde(with = "crate::json_text_serde")]`). Kept apart so that `json_text.rs` stays standard library only.
//!
//! An object is written as the JSON object it is, members in the order of the object, and read back from one. The text of a number
//! in this JSON is serde_json's; the canonical text (RFC 8785) of a carrier is what `JsonText::write` gives.

use crate::json_text::{JsonText, Object};
use serde::{Deserialize, Deserializer, Serialize, Serializer};

pub fn serialize<S: Serializer>(object: &Object, serializer: S) -> Result<S::Ok, S::Error> {
    let text = JsonText::write(object).map_err(serde::ser::Error::custom)?;
    let value: serde_json::Value = serde_json::from_str(&text).map_err(serde::ser::Error::custom)?;
    value.serialize(serializer)
}

pub fn deserialize<'de, D: Deserializer<'de>>(deserializer: D) -> Result<Object, D::Error> {
    let value = serde_json::Value::deserialize(deserializer)?;
    JsonText::read(&value.to_string()).map_err(serde::de::Error::custom)
}

/// The same for a field that is an `Option<Object>`: `#[serde(with = "crate::json_text_serde::option")]`.
pub mod option {
    use super::*;

    pub fn serialize<S: Serializer>(object: &Option<Object>, serializer: S) -> Result<S::Ok, S::Error> {
        match object {
            Some(o) => super::serialize(o, serializer),
            None => serializer.serialize_none(),
        }
    }

    pub fn deserialize<'de, D: Deserializer<'de>>(deserializer: D) -> Result<Option<Object>, D::Error> {
        match Option::<serde_json::Value>::deserialize(deserializer)? {
            Some(value) => JsonText::read(&value.to_string()).map(Some).map_err(serde::de::Error::custom),
            None => Ok(None),
        }
    }
}
