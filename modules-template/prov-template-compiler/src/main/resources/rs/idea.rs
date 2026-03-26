

use std::any::Any;

// ---------------------------------------------------------------------------
// HistoryEntry — typed sum-type for all beans that can appear in history.
//
// Using an enum instead of Box<dyn Any> means the Vec is fully serializable:
// serde's externally-tagged representation adds a "type" discriminant so
// JSON consumers can tell the variants apart.
// ---------------------------------------------------------------------------

/// One entry in the workflow history: the merged input+output view for a
/// single template invocation.  The `"type"` field in the JSON output names
/// the variant (e.g. `"FileTransforming"`, `"FileApproving"`, …).
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


fn to_history_entries(vec: Vec<Box<dyn Any>>) -> Vec<HistoryEntry> {
    vec.into_iter()
       .filter_map(|entry| try_convert(entry))
       .collect()
}

fn try_convert(entry: Box<dyn Any>) -> Option<HistoryEntry> {
    let entry = match entry.downcast::<FileInitBean>() {
        Ok(b)  => return Some(HistoryEntry::FileInit(*b)),
        Err(e) => e,
    };
    let entry = match entry.downcast::<FileTransformingBean>() {
        Ok(b)  => return Some(HistoryEntry::FileTransforming(*b)),
        Err(e) => e,
    };
    let entry = match entry.downcast::<FileFilteringBean>() {
        Ok(b)  => return Some(HistoryEntry::FileFiltering(*b)),
        Err(e) => e,
    };
    let entry = match entry.downcast::<FileTrainingBean>() {
        Ok(b)  => return Some(HistoryEntry::FileTraining(*b)),
        Err(e) => e,
    };
    let entry = match entry.downcast::<FileValidatingBean>() {
        Ok(b)  => return Some(HistoryEntry::FileValidating(*b)),
        Err(e) => e,
    };
    let entry = match entry.downcast::<FileApprovingBean>() {
        Ok(b)  => return Some(HistoryEntry::FileApproving(*b)),
        Err(e) => e,
    };
    let entry = match entry.downcast::<FileSplittingBean>() {
        Ok(b)  => return Some(HistoryEntry::FileSplitting(*b)),
        Err(e) => e,
    };
    match entry.downcast::<FileTransformingCompositeBean>() {
        Ok(b)  => Some(HistoryEntry::FileTransformingComposite(*b)),
        Err(_) => None,   // unknown type — drop it or panic, your choice
    }
}
