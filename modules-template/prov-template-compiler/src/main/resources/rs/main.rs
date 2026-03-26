// Rust equivalent of src/test/js/fs_run_workflow.js (local-mode path only).
//
// The JS file (from line 50 onward) does the following:
//
//   1. const { LocalEnactor } = require('.../LocalEnactor.js');
//   2. templateInstantion2 = new LocalEnactor(false);
//   3. class ThisWorkflow extends PleadWorkflow { time() { return now().toISOString() } }
//   4. new ThisWorkflow(templateInstantion2, inputs0, outputs0).workflow(111,333,...)
//   5. console.log(templateInstantion2.getHistory())
//   6. console.log("ID of last element in history " + outputs[outputs.length-1].ID)
//   7. console.log(templateInstantion2.getCounterMap())
//   8. console.log(templateInstantion2.getRecordedValues())
//
// Design note – why not `LocalEnactor` directly?
// ------------------------------------------------
// In the generated Rust, `LocalEnactor` is a thin facade around
// `BeanHistory<BeanLocalEnactor3>` but does NOT implement `InputOutputProcessor`
// and keeps its `inner_parent` field private.  `PleadWorkflow::template_instantiation()`
// requires `&mut Option<Box<dyn InputOutputProcessor>>`, so we cannot hand a
// `LocalEnactor` to the workflow directly.
//
// We mirror the internal construction of `LocalEnactor::new(false)` by building the
// `BeanHistory<BeanLocalEnactor3>` ourselves and wrapping it in an
// `Rc<RefCell<>>` so we can share one instance between:
//   * `ThisWorkflow` (as the `InputOutputProcessor` the workflow calls into), and
//   * `main` (for post-workflow introspection of history / counters).

mod org;

use std::any::Any;
use std::cell::RefCell;
use std::collections::HashMap;
use std::rc::Rc;
use std::sync::atomic::Ordering;
use serde::{Serialize, Deserialize};
use serde_json;
use org::openprovenance::book::fs::client::integrator::{
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
use crate::org::openprovenance::book::fs::client::common::file_approving_bean::FileApprovingBean;
use crate::org::openprovenance::book::fs::client::common::file_init_bean::FileInitBean;
use crate::org::openprovenance::book::fs::client::common::file_filtering_bean::FileFilteringBean;
use crate::org::openprovenance::book::fs::client::common::file_training_bean::FileTrainingBean;
use crate::org::openprovenance::book::fs::client::common::file_transforming_bean::FileTransformingBean;
use crate::org::openprovenance::book::fs::client::common::file_splitting_bean::FileSplittingBean;
use crate::org::openprovenance::book::fs::client::common::file_validating_bean::FileValidatingBean;
use crate::org::openprovenance::book::fs::client::common::file_transforming_composite_bean::FileTransformingCompositeBean;

use org::openprovenance::book::workflows::plead_workflow::PleadWorkflow;
use org::openprovenance::templates::catalogue::fs::integrator::{
    bean_history::BeanHistory,
    bean_local_enactor3::BeanLocalEnactor3,
    input_output_processor::InputOutputProcessor,
};

#[derive(Debug, Clone, Serialize, Deserialize)]
#[serde(tag = "type")]
pub enum HistoryEntry {
    FileInit(FileInitBean),
    FileTransforming(FileTransformingBean),
    FileFiltering(FileFilteringBean),
    FileTraining(FileTrainingBean),
    FileValidating(FileValidatingBean),
    FileApproving(FileApprovingBean),
    FileSplitting(FileSplittingBean),
    FileTransformingComposite(FileTransformingCompositeBean),
}


fn to_history_entries(vec: &[Box<dyn Any>]) -> Vec<HistoryEntry> {
    vec.iter()
       .filter_map(|entry| try_convert(entry.as_ref()))
       .collect()
}

fn try_convert(entry: &dyn Any) -> Option<HistoryEntry> {
    if let Some(b) = entry.downcast_ref::<FileInitBean>()                  { return Some(HistoryEntry::FileInit(b.clone())); }
    if let Some(b) = entry.downcast_ref::<FileTransformingBean>()          { return Some(HistoryEntry::FileTransforming(b.clone())); }
    if let Some(b) = entry.downcast_ref::<FileFilteringBean>()             { return Some(HistoryEntry::FileFiltering(b.clone())); }
    if let Some(b) = entry.downcast_ref::<FileTrainingBean>()              { return Some(HistoryEntry::FileTraining(b.clone())); }
    if let Some(b) = entry.downcast_ref::<FileValidatingBean>()            { return Some(HistoryEntry::FileValidating(b.clone())); }
    if let Some(b) = entry.downcast_ref::<FileApprovingBean>()             { return Some(HistoryEntry::FileApproving(b.clone())); }
    if let Some(b) = entry.downcast_ref::<FileSplittingBean>()             { return Some(HistoryEntry::FileSplitting(b.clone())); }
    if let Some(b) = entry.downcast_ref::<FileTransformingCompositeBean>() { return Some(HistoryEntry::FileTransformingComposite(b.clone())); }
    None
}

// ---------------------------------------------------------------------------
// HistoryProcessor
//
// Rc<RefCell<BeanHistory<BeanLocalEnactor3>>> wrapper that implements
// InputOutputProcessor by delegating each call to the shared inner history.
// This lets us keep a second Rc handle in `main` for post-workflow inspection
// while the workflow holds the first handle inside its Box<dyn InputOutputProcessor>.
// ---------------------------------------------------------------------------
type SharedHistory = Rc<RefCell<BeanHistory<BeanLocalEnactor3>>>;

struct HistoryProcessor(SharedHistory);

impl InputOutputProcessor for HistoryProcessor {
    fn process_file_init_inputs(&mut self, bean: &FileInitInputs) -> FileInitOutputs {
        self.0.borrow_mut().process_file_init_inputs(bean)
    }
    fn process_file_transforming_inputs(&mut self, bean: &FileTransformingInputs) -> FileTransformingOutputs {
        self.0.borrow_mut().process_file_transforming_inputs(bean)
    }
    fn process_file_filtering_inputs(&mut self, bean: &FileFilteringInputs) -> FileFilteringOutputs {
        self.0.borrow_mut().process_file_filtering_inputs(bean)
    }
    fn process_file_training_inputs(&mut self, bean: &FileTrainingInputs) -> FileTrainingOutputs {
        self.0.borrow_mut().process_file_training_inputs(bean)
    }
    fn process_file_validating_inputs(&mut self, bean: &FileValidatingInputs) -> FileValidatingOutputs {
        self.0.borrow_mut().process_file_validating_inputs(bean)
    }
    fn process_file_approving_inputs(&mut self, bean: &FileApprovingInputs) -> FileApprovingOutputs {
        self.0.borrow_mut().process_file_approving_inputs(bean)
    }
    fn process_file_splitting_inputs(&mut self, bean: &FileSplittingInputs) -> FileSplittingOutputs {
        self.0.borrow_mut().process_file_splitting_inputs(bean)
    }
    fn process_file_transforming_composite_inputs(
        &mut self,
        bean: &FileTransformingCompositeInputs,
    ) -> FileTransformingCompositeOutputs {
        self.0.borrow_mut().process_file_transforming_composite_inputs(bean)
    }
}

// ---------------------------------------------------------------------------
// ThisWorkflow
//
// Concrete implementor of PleadWorkflow — mirrors the JS class:
//
//   class ThisWorkflow extends PleadWorkflow {
//       constructor(templateInstantion, inputs, outputs) { super(...) }
//       time() { return new Date().toISOString() }
//   }
// ---------------------------------------------------------------------------
struct ThisWorkflow {
    template_instantiation: Option<Box<dyn InputOutputProcessor>>,
    inputs: Option<Vec<Box<dyn Any>>>,
    outputs: Option<Vec<Box<dyn Any>>>,
}

impl ThisWorkflow {
    fn new(processor: HistoryProcessor) -> Self {
        Self {
            template_instantiation: Some(Box::new(processor)),
            inputs: Some(Vec::new()),
            outputs: Some(Vec::new()),
        }
    }
}

impl PleadWorkflow for ThisWorkflow {
    /// Current UTC time as an ISO-8601 string — mirrors JS `new Date().toISOString()`.
    fn time(&self) -> String {
        use std::time::{SystemTime, UNIX_EPOCH};
        let secs = SystemTime::now()
            .duration_since(UNIX_EPOCH)
            .map(|d| d.as_secs())
            .unwrap_or(0);
        unix_secs_to_iso8601(secs)
    }

    fn inputs(&mut self) -> &mut Option<Vec<Box<dyn Any>>> {
        &mut self.inputs
    }

    fn outputs(&mut self) -> &mut Option<Vec<Box<dyn Any>>> {
        &mut self.outputs
    }

    fn template_instantiation(&mut self) -> &mut Option<Box<dyn InputOutputProcessor>> {
        &mut self.template_instantiation
    }
}

// ---------------------------------------------------------------------------
// Minimal ISO-8601 UTC formatter (avoids a `chrono` dependency).
// ---------------------------------------------------------------------------

/// Format a Unix timestamp (whole seconds) as `YYYY-MM-DDTHH:MM:SSZ`.
fn unix_secs_to_iso8601(secs: u64) -> String {
    let sec  = secs % 60;
    let min  = (secs / 60) % 60;
    let hour = (secs / 3600) % 24;
    let days = secs / 86400; // days elapsed since 1970-01-01

    // Determine year
    let mut year = 1970u32;
    let mut rem  = days;
    loop {
        let diy = days_in_year(year) as u64;
        if rem < diy { break; }
        rem  -= diy;
        year += 1;
    }

    // Determine month and day-of-month (rem=0 → day 1)
    let month_days: [u64; 12] = [
        31, if is_leap(year) { 29 } else { 28 },
        31, 30, 31, 30, 31, 31, 30, 31, 30, 31,
    ];
    let mut month = 1u32;
    let mut day   = rem + 1;
    for &md in &month_days {
        if day <= md { break; }
        day   -= md;
        month += 1;
    }

    format!("{:04}-{:02}-{:02}T{:02}:{:02}:{:02}Z", year, month, day, hour, min, sec)
}

fn days_in_year(y: u32) -> u32 { if is_leap(y) { 366 } else { 365 } }
fn is_leap(y: u32) -> bool { (y % 4 == 0 && y % 100 != 0) || y % 400 == 0 }

// ---------------------------------------------------------------------------
// main
// ---------------------------------------------------------------------------
fn main() {
    // -----------------------------------------------------------------------
    // Step 1: Build the shared enactor.
    //
    // JS: templateInstantion2 = new LocalEnactor(false);
    //
    // LocalEnactor::new(false) internally constructs:
    //   BeanHistory::<BeanLocalEnactor3>::new(
    //       BeanLocalEnactor3::new(HashMap::new(), HashMap::new(), false),
    //       Vec::new())
    // We replicate that and share ownership via Rc<RefCell<>>.
    // -----------------------------------------------------------------------
    let shared: SharedHistory = Rc::new(RefCell::new(
        BeanHistory::<BeanLocalEnactor3>::new(
            BeanLocalEnactor3::new(HashMap::new(), HashMap::new(), false),
            Vec::new(),
        ),
    ));

    // -----------------------------------------------------------------------
    // Step 2: Build and run the workflow.
    //
    // JS:
    //   var inputs0=[], outputs0=[];
    //   const pleadWorkflow = new ThisWorkflow(templateInstantion2, inputs0, outputs0);
    //   pleadWorkflow.workflow(111, 333, "inputfile", 123, 56, 78, 456, 768,
    //                          '/home/bob',
    //                          "2026-03-01T09:03:51.168987Z",
    //                          "2026-03-01T09:03:51.168987Z");
    // -----------------------------------------------------------------------
    let mut plead_workflow = ThisWorkflow::new(HistoryProcessor(Rc::clone(&shared)));

    plead_workflow.workflow(
        Some(111),                          // engineer
        Some(333),                          // manager
        "inputfile",                        // filename_root
        Some(123),                          // old_file_id
        Some(56),                           // tmethod
        Some(78),                           // fmethod
        Some(456),                          // n_rows
        Some(768),                          // n_cols
        "/home/bob",                        // path
        "2026-03-01T09:03:51.168987Z",      // start
        "2026-03-01T09:03:51.168987Z",      // end
    );

    // -----------------------------------------------------------------------
    // Step 3: Print history.
    //
    // JS: console.log(templateInstantion2.getHistory());
    //
    // BeanHistory records one merged bean (combined input + output view) per
    // workflow step.  Each entry is erased to Box<dyn Any> in the Vec.
    // -----------------------------------------------------------------------
    println!("=== History ===");
    {
        let enactor = shared.borrow();
        let history = enactor.get_history();
        println!("  {} entries", history.len());
        for (i, _entry) in history.iter().enumerate() {
            // Each entry is a Box<dyn Any> (FileTransformingBean, FileFilteringBean, …).
            // Without a common Debug/Display supertrait on the history entries we
            // report their index; the count alone mirrors the JS output faithfully.
            println!("  [{i}] <bean>");
        }
        let entries: Vec<HistoryEntry> = to_history_entries(history);
        match serde_json::to_string_pretty(&entries) {
            Ok(json)  => println!("{}", json),
            Err(e)    => println!("serialization error: {}", e),
        }
    }

    // -----------------------------------------------------------------------
    // Step 4: Print the ID of the last workflow output.
    //
    // JS:
    //   inputs0.forEach(i => inputs.push(i));
    //   outputs0.forEach(o => outputs.push(o));
    //   console.log("ID of last element in history " + outputs[outputs.length-1].ID);
    //
    // PleadWorkflow pushes each step's raw output into self.outputs().  The
    // final step is "Approving", whose Rust type is FileApprovingOutputs.
    // The JS `.ID` property corresponds to Rust's `i_d` field.
    // -----------------------------------------------------------------------
    println!("\n=== ID of last element in history ===");
    {
        let outputs = plead_workflow.outputs.as_ref().unwrap();
        match outputs.last() {
            None => println!("  (no outputs)"),
            Some(last) => match last.downcast_ref::<FileApprovingOutputs>() {
                Some(approving) => println!("  {:?}", approving.i_d),
                None => println!("  (unexpected type for last output)"),
            },
        }
    }

    // -----------------------------------------------------------------------
    // Step 5: Print counter map.
    //
    // JS: console.log(templateInstantion2.getCounterMap());
    // -----------------------------------------------------------------------
    println!("\n=== Counter map ===");
    {
        let enactor = shared.borrow();
        let counter_map = enactor.get_delegator().get_counter_map();
        if counter_map.is_empty() {
            println!("  (empty)");
        } else {
            let mut keys: Vec<&String> = counter_map.keys().collect();
            keys.sort();
            for k in keys {
                println!("  {}: {}", k, counter_map[k].load(Ordering::Relaxed));
            }
        }
    }

    // -----------------------------------------------------------------------
    // Step 6: Print recorded values.
    //
    // JS: console.log(templateInstantion2.getRecordedValues());
    // -----------------------------------------------------------------------
    println!("\n=== Recorded values ===");
    {
        let enactor = shared.borrow();
        let recorded = enactor.get_delegator().get_recorded_values();
        if recorded.is_empty() {
            println!("  (empty)");
        } else {
            let mut keys: Vec<&String> = recorded.keys().collect();
            keys.sort();
            for k in keys {
                println!("  {}: {:?}", k, recorded[k]);
            }
        }
    }
}

