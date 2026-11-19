// T33: runs escape-csv-vectors.json against the JSweet candy of StringEscapeUtils (prov-jsweet-candy-java/dist).
// usage: node run_escape_vectors.js <prov-jsweet-candy-java.js> <vectors file>
// Prints one line per failure and, last, 'escape vectors: <n> run, <m> failed'. Exit status 0 when none failed.
const fs = require('fs');
const org = new Function(fs.readFileSync(process.argv[2], 'utf8') + '\n;return org;')();
const U = org.openprovenance.apache.commons.lang.StringEscapeUtils;
const vectors = JSON.parse(fs.readFileSync(process.argv[3], 'utf8')).vectors;
let failed = 0;
for (const v of vectors) {
  const got = U.escapeCsv(v.input);
  if (got !== v.output) { failed++; console.log('FAIL ' + v.title + ': gave ' + JSON.stringify(got) + ', expected ' + JSON.stringify(v.output)); }
}
console.log('escape vectors: ' + vectors.length + ' run, ' + failed + ' failed');
process.exit(failed ? 1 : 0);
