

const pf = new org.openprovenance.prov.vanilla.ProvFactory();
const e1 = pf.newEntity$org_openprovenance_prov_model_QualifiedName(pf.newQualifiedName$java_lang_String$java_lang_String$java_lang_String("http://ex.org/", "e1", "ex"));
const e2 = pf.newEntity$org_openprovenance_prov_model_QualifiedName(pf.newQualifiedName$java_lang_String$java_lang_String$java_lang_String("http://ex.org/", "e2", "ex"));
const activity1 = pf.newActivity$org_openprovenance_prov_model_QualifiedName(pf.newQualifiedName$java_lang_String$java_lang_String$java_lang_String("http://ex.org/", "a1", "ex"));
const activity2 = pf.newActivity$org_openprovenance_prov_model_QualifiedName(pf.newQualifiedName$java_lang_String$java_lang_String$java_lang_String("http://ex.org/", "a1", "ex"));
const agent = pf.newAgent$org_openprovenance_prov_model_QualifiedName(pf.newQualifiedName$java_lang_String$java_lang_String$java_lang_String("http://ex.org/", "ag1", "ex"));
const wgb = pf.newWasGeneratedBy$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(null, e1.getId(), activity1.getId());
pf.addType$org_openprovenance_prov_model_HasType$org_openprovenance_prov_model_Type(e1, pf.newType(pf.getName().newProvQualifiedName("SoftwareAgent"), pf.getName().PROV_QUALIFIED_NAME));
activity1.setStartTime(pf.newTimeNow());
activity2.setStartTime(pf.newTimeNow());
pf.addType$org_openprovenance_prov_model_HasType$org_openprovenance_prov_model_Type(activity2, pf.newType(pf.getName().newProvQualifiedName("SoftwareAgent"), pf.getName().PROV_QUALIFIED_NAME));

const wdf=pf.newWasDerivedFrom$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(e2.getId(),e1.getId())

console.log(e1);
console.log(e1.getKind());
const doc = pf.newDocument$();
/* add */ (doc.getStatementOrBundle().push(e1) > 0);
/* add */ (doc.getStatementOrBundle().push(e2) > 0);
/* add */ (doc.getStatementOrBundle().push(activity1) > 0);
/* add */ (doc.getStatementOrBundle().push(agent) > 0);
/* add */ (doc.getStatementOrBundle().push(wgb) > 0);
/* add */ (doc.getStatementOrBundle().push(wdf) > 0);
const ns = new org.openprovenance.prov.model.Namespace();
ns.addKnownNamespaces();
ns.register("ex", "http://ex.org/");
doc.setNamespace(ns);

const docString = new org.openprovenance.prov.json.ProvSerialiser(pf).serialiseDocument$org_openprovenance_prov_model_Document(doc);
console.log(docString);
const doc2 = new org.openprovenance.prov.json.ProvDeserialiser(pf).deserialiseDocument$java_lang_String(docString);
console.log(doc2);
const doc3 = pf.newDocument$();
doc3.setNamespace(ns);
(doc3.getStatementOrBundle().push(e1) > 0);
console.log(/* equals */ ((o1, o2) => { if (o1 && o1.equals) {
    return o1.equals(o2);
}
else {
    return o1 === o2;
} })(doc, doc2));

console.log(e1.equals(e1))

console.log("should be false")
console.log(doc.equals(doc3))
console.log(doc.equals(e1))

console.log(activity1.equals(activity2))
