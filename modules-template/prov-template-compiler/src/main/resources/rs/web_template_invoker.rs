//! Rust equivalent of:
//!   src/test/js/fs_WebTemplateInvoker.js   (ServiceInvoker + WebTemplateInvoker)
//!   src/test/js/fs_remoteEnactor.js        (RemoteEnactor = BeanHistory<WebTemplateInvoker>)
//!
//! # Design
//!
//! The JS `WebTemplateInvoker extends TemplateInvoker` pattern cannot be mapped directly
//! to the generated `TemplateInvoker` Rust trait because that trait's abstract method
//!
//!   `fn generic_post_and_return<IN, OUT>(&self, …) -> OUT;`
//!
//! has no bounds on `IN` or `OUT`, yet we need `IN: Serialize` to POST the bean and
//! `OUT: Clone` to clone the prototype outbean.  In Rust the implementation cannot add
//! bounds that are absent from the trait declaration.
//!
//! Therefore `WebTemplateInvoker` implements `InputOutputProcessor` directly, and
//! provides `generic_post_and_return` as an ordinary (non-trait) generic method with
//! the necessary bounds.  The per-template `process_xxx_inputs` methods each call this
//! private helper, replicating every default body from the generated `TemplateInvoker`
//! trait exactly.
//!
//! `RemoteEnactor` = `BeanHistory<WebTemplateInvoker>` (a type alias + constructor fn).

use std::any::Any;
use std::collections::HashMap;

use serde::Serialize;

use crate::org::openprovenance::book::fs::client::integrator::{
    file_approving_inputs::FileApprovingInputs,
    file_approving_outputs::FileApprovingOutputs,
    file_filtering_inputs::FileFilteringInputs,
    file_filtering_outputs::FileFilteringOutputs,
    file_init_inputs::FileInitInputs,
    file_init_outputs::FileInitOutputs,
    file_splitting_inputs::FileSplittingInputs,
    file_splitting_outputs::FileSplittingOutputs,
    file_training_inputs::FileTrainingInputs,
    file_training_outputs::FileTrainingOutputs,
    file_transforming_composite_inputs::FileTransformingCompositeInputs,
    file_transforming_composite_outputs::FileTransformingCompositeOutputs,
    file_transforming_inputs::FileTransformingInputs,
    file_transforming_outputs::FileTransformingOutputs,
    file_validating_inputs::FileValidatingInputs,
    file_validating_outputs::FileValidatingOutputs,
};
use crate::org::openprovenance::templates::catalogue::fs::integrator::{
    bean_completer2::BeanCompleter2,
    bean_history::BeanHistory,
    composite_bean_completer2::CompositeBeanCompleter2,
    input_output_processor::InputOutputProcessor,
};

// ---------------------------------------------------------------------------
// ServiceInvoker
//
// JS:
//   class ServiceInvoker {
//       postInstructionsInOut(url, body, accessToken) {
//           const token = fs.readFileSync('/Users/luc/.keycloak_token', 'utf8').trim();
//           return this.fetcher.post(url, [body], token);
//       }
//   }
//
// The JS token-file read is handled by the caller (main) before constructing the
// RemoteEnactor, so ServiceInvoker simply uses the token it receives.
// ---------------------------------------------------------------------------

pub struct ServiceInvoker;

impl ServiceInvoker {
    pub fn new() -> Self {
        ServiceInvoker
    }

    /// POST `body` (serialised as a one-element JSON array) to `url` with a
    /// Bearer token.  Blocks until the full response body arrives.
    ///
    /// Mirrors the JS: `this.fetcher.post(url, [body], token)`.
    ///
    /// Returns the full parsed JSON response value (expected to be an array
    /// whose first element is the output map).
    pub fn post_instructions_in_out<T: Serialize>(
        &self,
        url: &str,
        body: &T,
        access_token: &str,
    ) -> Result<serde_json::Value, Box<dyn std::error::Error>> {
        // Wrap the single bean in an array — the protocol expects [bean]
        let json_body = serde_json::to_string(&[body])?;

        let response = ureq::post(url)
            .set("Authorization", &format!("Bearer {}", access_token))
            .set("Content-Type", "application/json")
            .send_string(&json_body)?;

        let value: serde_json::Value = response.into_json()?;
        Ok(value)
    }
}

// ---------------------------------------------------------------------------
// WebTemplateInvoker
// ---------------------------------------------------------------------------

pub struct WebTemplateInvoker {
    pub url: String,
    pub access_token: String,
    /// When `true`, the JSON request body and response are printed to stderr
    /// before and after every POST.
    pub debug: bool,
    si: ServiceInvoker,
}

impl WebTemplateInvoker {
    pub fn new(url: impl Into<String>, access_token: impl Into<String>) -> Self {
        Self {
            url: url.into(),
            access_token: access_token.into(),
            debug: false,
            si: ServiceInvoker::new(),
        }
    }

    pub fn with_debug(mut self, debug: bool) -> Self {
        self.debug = debug;
        self
    }

    /// Core implementation mirroring the JS `generic_post_and_return`.
    ///
    /// 1. Serialise `inputs` as `[inputs]` and POST to `self.url`.
    /// 2. Parse `response[0]` into `HashMap<String, Box<dyn Any>>`:
    ///    - JSON strings  → `Box<String>`
    ///    - JSON integers → `Box<i32>`  (BeanCompleter2 downcasts to i32)
    ///    - JSON booleans → `Box<bool>`
    ///    - JSON null / nested objects are skipped (not used by BeanCompleter2)
    /// 3. Call `completer(map, outbean)` and return the result.
    ///
    /// On any I/O or parse error the method logs to stderr and returns `outbean`
    /// unchanged, avoiding panics in network-facing code.
    pub fn generic_post_and_return<IN, OUT>(
        &self,
        outbean: OUT,
        inputs: &IN,
        completer: impl Fn(HashMap<String, Box<dyn Any>>, OUT) -> OUT,
    ) -> OUT
    where
        IN: Serialize,
    {
        // ---- Serialise request (and optionally debug-print) ----
        let json_body = match serde_json::to_string_pretty(&[inputs]) {
            Ok(s)  => s,
            Err(e) => {
                eprintln!("WebTemplateInvoker: serialisation error: {}", e);
                return outbean;
            }
        };
        if self.debug {
            eprintln!(">> POST {}\n{}", self.url, json_body);
        }

        // ---- Blocking HTTP POST ----
        let result0 = match self.si.post_instructions_in_out(&self.url, inputs, &self.access_token) {
            Ok(v)  => v,
            Err(e) => {
                eprintln!("WebTemplateInvoker: HTTP error: {}", e);
                return outbean;
            }
        };
        if self.debug {
            eprintln!("<< Response\n{}", serde_json::to_string_pretty(&result0).unwrap_or_else(|_| format!("{:?}", result0)));
        }

        // JS: let result1 = result0[0]
        let result1 = match result0.get(0) {
            Some(v) => v.clone(),
            None => {
                eprintln!("WebTemplateInvoker: response array is empty");
                return outbean;
            }
        };

        // JS: for (let key in result1) { if (result1.hasOwnProperty(key)) map.put(key, result1[key]) }
        let mut map: HashMap<String, Box<dyn Any>> = HashMap::new();
        if let Some(obj) = result1.as_object() {
            for (k, v) in obj {
                match v {
                    serde_json::Value::String(s) => {
                        map.insert(k.clone(), Box::new(s.clone()));
                    }
                    serde_json::Value::Number(n) => {
                        if let Some(i) = n.as_i64() {
                            map.insert(k.clone(), Box::new(i as i32));
                        }
                    }
                    serde_json::Value::Bool(b) => {
                        map.insert(k.clone(), Box::new(*b));
                    }
                    _ => {} // null and nested objects not consumed by BeanCompleter2
                }
            }
        }

        completer(map, outbean)
    }
}

/// Implement `InputOutputProcessor` by routing each template call through
/// `generic_post_and_return`.  This replicates the default bodies that the
/// generated `TemplateInvoker` trait would have provided.
impl InputOutputProcessor for WebTemplateInvoker {
    fn process_file_init_inputs(&mut self, bean: &FileInitInputs) -> FileInitOutputs {
        self.generic_post_and_return(
            FileInitOutputs::new(),
            bean,
            |m, o| BeanCompleter2::new(m).process_file_init_outputs(o),
        )
    }

    fn process_file_transforming_inputs(&mut self, bean: &FileTransformingInputs) -> FileTransformingOutputs {
        self.generic_post_and_return(
            FileTransformingOutputs::new(),
            bean,
            |m, o| BeanCompleter2::new(m).process_file_transforming_outputs(o),
        )
    }

    fn process_file_filtering_inputs(&mut self, bean: &FileFilteringInputs) -> FileFilteringOutputs {
        self.generic_post_and_return(
            FileFilteringOutputs::new(),
            bean,
            |m, o| BeanCompleter2::new(m).process_file_filtering_outputs(o),
        )
    }

    fn process_file_training_inputs(&mut self, bean: &FileTrainingInputs) -> FileTrainingOutputs {
        self.generic_post_and_return(
            FileTrainingOutputs::new(),
            bean,
            |m, o| BeanCompleter2::new(m).process_file_training_outputs(o),
        )
    }

    fn process_file_validating_inputs(&mut self, bean: &FileValidatingInputs) -> FileValidatingOutputs {
        self.generic_post_and_return(
            FileValidatingOutputs::new(),
            bean,
            |m, o| BeanCompleter2::new(m).process_file_validating_outputs(o),
        )
    }

    fn process_file_approving_inputs(&mut self, bean: &FileApprovingInputs) -> FileApprovingOutputs {
        self.generic_post_and_return(
            FileApprovingOutputs::new(),
            bean,
            |m, o| BeanCompleter2::new(m).process_file_approving_outputs(o),
        )
    }

    fn process_file_splitting_inputs(&mut self, bean: &FileSplittingInputs) -> FileSplittingOutputs {
        self.generic_post_and_return(
            FileSplittingOutputs::new(),
            bean,
            |m, o| BeanCompleter2::new(m).process_file_splitting_outputs(o),
        )
    }

    fn process_file_transforming_composite_inputs(
        &mut self,
        bean: &FileTransformingCompositeInputs,
    ) -> FileTransformingCompositeOutputs {
        self.generic_post_and_return(
            FileTransformingCompositeOutputs::new(),
            bean,
            |m, mut o| {
                o.elements = Vec::new();
                CompositeBeanCompleter2::new(m).process(o)
            },
        )
    }
}

// ---------------------------------------------------------------------------
// RemoteEnactor
//
// JS:
//   class RemoteEnactor extends BeanHistory {
//       constructor(url, accessToken) {
//           super(new WebTemplateInvoker(url, accessToken), []);
//       }
//   }
// ---------------------------------------------------------------------------

/// `RemoteEnactor` wraps a `WebTemplateInvoker` in a `BeanHistory` so that every
/// `process_xxx_inputs` call is both dispatched to the remote service and recorded
/// in the history vec — exactly as `LocalEnactor` does for the local path.
pub type RemoteEnactor = BeanHistory<WebTemplateInvoker>;

/// Constructor helper mirroring `new RemoteEnactor(url, accessToken)`.
/// Pass `debug = true` to enable JSON request/response logging on stderr.
pub fn new_remote_enactor(
    url: impl Into<String>,
    access_token: impl Into<String>,
    debug: bool,
) -> RemoteEnactor {
    BeanHistory::new(
        WebTemplateInvoker::new(url, access_token).with_debug(debug),
        Vec::new(),
    )
}
