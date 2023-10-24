

var template_library=require( '../../../target/js/bundle.js');
var org=template_library.org;
var plead=org.openprovenance.prov.template.library.plead;
var ptm=org.openprovenance.prov.template.library.ptm;


test('two plus two is four', () => {
    expect(2 + 2).toBe(4);
    console.log(plead.client.common.Plead_approvingBuilder.examplar());


});

