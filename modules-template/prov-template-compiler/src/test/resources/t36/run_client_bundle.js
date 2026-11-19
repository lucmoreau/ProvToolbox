// T34: loads the JSweet bundle of a generated client in node with the runtimes it needs (j4ts, for java.*; the toolbox's candy, for the rest) and
// writes a record through the generated builder of the T36 fixture's approval template.
// usage: node run_client_bundle.js <j4ts bundle.js> <prov-jsweet-candy-java.js> <client bundle.js>
// Prints "log: <the record>" and "csv: <the record of a bean>"; exit status 0 when both are as expected.
const out = (...a) => process.stdout.write(a.join(' ') + '\n');
global.print = out;   // j4ts's System writes the console through print, which node does not have
const vm = require('vm'), fs = require('fs');
const [j4ts, candy, bundle] = process.argv.slice(2);
vm.runInThisContext(fs.readFileSync(j4ts, 'utf8'));
vm.runInThisContext(fs.readFileSync(candy, 'utf8'));
vm.runInThisContext(fs.readFileSync(bundle, 'utf8'));   // a script (module none): the namespaces are global, with the candy's
const common = org.example.t36.approval.client.common;
const builder = new common.ApprovalBuilder();
const sb = new java.lang.StringBuilder();
builder.logApproval(sb, 'a1', 'ex:agent', 'Ann', 'ref-1', 'o1', 'a note', '{"k":1}', 3);
const log = sb.toString().trim();
out('log: ' + log);
const bean = builder.newBean();
bean.approver = 'ex:agent'; bean.order = 'o1'; bean.level = 3;
const csv = bean.process(builder.args2csv());
out('csv: ' + csv);
process.exit(log === 't31.Approval,a1,ex:agent,Ann,ref-1,o1,a note,{"k":1},3' && csv === 't31.Approval,,ex:agent,,,o1,,,3' ? 0 : 1);
