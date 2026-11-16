// T36: runs json-text-vectors.json against the JSweet candy of JsonText (prov-jsweet-candy-java/dist).
// usage: node run_json_text_vectors.js <prov-jsweet-candy-java.js> <vectors file>
// Prints one line per failure and, last, 'vectors: <n> run, <m> failed'. Exit status 0 when none failed.
const fs = require('fs');
const src = fs.readFileSync(process.argv[2], 'utf8');
const org = new Function(src + '\n;return org;')();
const JsonText = org.openprovenance.prov.utils.JsonText;
const vectors = JSON.parse(fs.readFileSync(process.argv[3], 'utf8'));
let run = 0, failed = 0;
const fail = (title, why) => { failed++; console.log('FAIL ' + title + ': ' + why); };
const refused = (f) => { try { f(); return false; } catch (e) { return e instanceof Error || true; } };

for (const e of vectors.canonical) {
  run++;
  try {
    const got = JsonText.write(JsonText.read(e.input));
    if (got !== e.output) fail(e.title, 'wrote ' + got + ', expected ' + e.output);
  } catch (x) { fail(e.title, 'raised ' + x); }
}
for (const e of vectors.refused) {
  run++;
  if (!refused(() => JsonText.read(e.input))) fail(e.title, 'accepted');
}
for (const e of vectors.getters) {
  run++;
  const obj = JsonText.read(e.input);
  if (e.refused) {
    if (!refused(() => JsonText[e.method](obj, e.key))) fail(e.title, 'accepted');
    continue;
  }
  let got;
  try { got = JsonText[e.method](obj, e.key); } catch (x) { fail(e.title, 'raised ' + x); continue; }
  const ok = (e.result === null) ? (got === null || got === undefined) : got === e.result;
  if (!ok) fail(e.title, 'got ' + JSON.stringify(got) + ', expected ' + JSON.stringify(e.result));
}
for (const e of vectors.text) {
  run++;
  const value = JsonText.parse(e.input);
  if (e.refused) {
    if (!refused(() => JsonText.text(value))) fail(e.title, 'accepted');
    continue;
  }
  let got;
  try { got = JsonText.text(value); } catch (x) { fail(e.title, 'raised ' + x); continue; }
  const ok = (e.result === null) ? (got === null || got === undefined) : got === e.result;
  if (!ok) fail(e.title, 'got ' + JSON.stringify(got) + ', expected ' + JSON.stringify(e.result));
}
console.log('vectors: ' + run + ' run, ' + failed + ' failed');
process.exit(failed ? 1 : 0);
