// T36 part D: runs the JavaScript the compiler emits for the request and the reply of a subtype, against a stand-in typed bean.
// usage: node check_request_reply.js <RepriceRequest.js> <RepriceReply.js>
// Prints 'FAIL ...' for a wrong answer and last 'checks done, <n> failed'; exit status 0 when none failed.
const assert = require('assert');
const { RepriceRequest } = require(require('path').resolve(process.argv[2]));
const { RepriceReply } = require(require('path').resolve(process.argv[3]));
let bad = 0;
function eq(title, got, want) {
  try { assert.deepStrictEqual(got, want); } catch (e) { bad++; console.log('FAIL ' + title + ': ' + JSON.stringify(got) + ' != ' + JSON.stringify(want)); }
}
const typed = () => ({ approver: null, approver_ref: null, order_note: null, level: null, priceBefore: null, unitCode: null,
                       lineCount: null, payload: null, memo: null, extraOther: {}, customer_hashOther: {} });

eq('request fields', Object.keys(new RepriceRequest()).sort(), ['extraOther', 'memo', 'order_note', 'priceBefore', 'unitCode']);
eq('reply fields', Object.keys(new RepriceReply()).sort(), ['approver_ref', 'lineCount']);

const t = typed(); t.approver = 'ex:a'; t.level = 3; t.payload = '{}';
const req = new RepriceRequest('n', 2.5, 'EA', 'm', { colour: 'red' });
eq('into returns the bean it is given', req.into(t) === t, true);
eq('copied', [t.order_note, t.priceBefore, t.unitCode, t.memo, t.extraOther], ['n', 2.5, 'EA', 'm', { colour: 'red' }]);
eq('not touched', [t.approver, t.level, t.payload, t.approver_ref, t.lineCount], ['ex:a', 3, '{}', null, null]);
eq('of(into) of a request', Object.assign({}, RepriceRequest.of(t)), Object.assign({}, req));

const t2 = typed(); t2.priceBefore = 9.0;
const rep = new RepriceReply('r1', 4); rep.into(t2);
eq('reply copied', [t2.approver_ref, t2.lineCount], ['r1', 4]);
eq('reply leaves the request values', t2.priceBefore, 9.0);
eq('of(into) of a reply', Object.assign({}, RepriceReply.of(t2)), Object.assign({}, rep));

const t3 = typed(); t3.extraOther = { kept: 1 };
new RepriceRequest(null, 1.0).into(t3);
eq('an absent map of other keys leaves the typed bean\'s own', t3.extraOther, { kept: 1 });

console.log('checks done, ' + bad + ' failed');
process.exit(bad ? 1 : 0);
