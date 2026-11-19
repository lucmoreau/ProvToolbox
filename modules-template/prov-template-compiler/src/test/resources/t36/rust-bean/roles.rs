// T47: the role accessors of a bean (a variable that declares @role) compile in Rust and read and write the field.
// The getter returns the field's Option, as Java's null is None; the setter takes a value and stores it.

use t36_bean::org::example::t36flow::backend::client::integrator::changing_outputs::ChangingOutputs;

#[test]
fn a_role_accessor_reads_and_writes_the_field() {
    let mut o = ChangingOutputs::new();
    assert_eq!(o.get_effect(), None);
    assert_eq!(o.get_general(), None);
    o.set_effect("D1");
    o.set_general("C1");
    assert_eq!(o.get_effect(), Some("D1".to_string()));
    assert_eq!(o.get_general(), Some("C1".to_string()));
    assert_eq!(o.document1.as_deref(), Some("D1"), "the accessor writes the field");
    assert_eq!(o.changing.as_deref(), Some("C1"));
}
