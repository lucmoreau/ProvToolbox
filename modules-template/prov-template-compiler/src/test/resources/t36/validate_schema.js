// T40: validates JSON records against a generated schema with ajv (the library behind ajv-cli, found on NODE_PATH or beside the CLI).
// The generated schemas carry members that ajv's meta-schema refuses ("readOnly": "true", "required": true in a property), so the
// schema itself is not validated: only what it says of the records is run. Usage: node validate_schema.js schema.json record.json...
// Prints one line "<record> valid" or "<record> invalid <errors>" for each; exit 0.
const fs = require('fs');
let Ajv;
for (const p of ['ajv', '/usr/local/lib/node_modules/ajv-cli/node_modules/ajv', '/usr/local/lib/node_modules/ajv']) {
  try { Ajv = require(p); break; } catch (e) { /* next */ }
}
if (!Ajv) { console.log('NO-AJV'); process.exit(0); }
const schema = JSON.parse(fs.readFileSync(process.argv[2], 'utf8'));
const ajv = new Ajv({ validateSchema: false, allErrors: true, unknownFormats: 'ignore', schemaId: 'auto' });
const validate = ajv.compile(schema);
for (const f of process.argv.slice(3)) {
  const ok = validate(JSON.parse(fs.readFileSync(f, 'utf8')));
  console.log(f + (ok ? ' valid' : ' invalid ' + JSON.stringify(validate.errors.map(e => e.dataPath + ' ' + e.message))));
}
