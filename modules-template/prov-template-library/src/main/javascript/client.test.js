

const template_library= require( '../../../target/js/bundle.js');
const fs = require('fs');

const org=template_library.org;
const plead=org.openprovenance.prov.template.library.plead;
const ptm=org.openprovenance.prov.template.library.ptm;


test('plead-approving-template test', () => {
    const pleadApprovingBuilder = plead.client.common.Plead_approvingBuilder;
    let examplarCompactBean = pleadApprovingBuilder.examplar();
    console.log(examplarCompactBean);
    examplarCompactBean.signature = "a,funny,signatue";
    let pleadApprovingBuilder1 = new pleadApprovingBuilder();
    console.log(examplarCompactBean.process(pleadApprovingBuilder1.args2csv()));

    fs.writeFile('target/examplar1.json', JSON.stringify(examplarCompactBean,null, 2), err => {
        if (err) {
            console.error(err);
        }
    });
    fs.writeFile('target/examplar1.csv', examplarCompactBean.process(pleadApprovingBuilder1.args2csv()), err => {
        if (err) {
            console.error(err);
        }
    });
});


test('ptm-expanding-template test', () => {
    let examplarCompactBean = ptm.client.common.Ptm_expandingBuilder.examplar();
    console.log(examplarCompactBean);
    let ptmExpandingBuilder1 = new ptm.client.common.Ptm_expandingBuilder();
    console.log(examplarCompactBean.process(ptmExpandingBuilder1.args2csv()));

    fs.writeFile('target/examplar2.json', JSON.stringify(examplarCompactBean,null, 2), err => {
        if (err) {
            console.error(err);
        }
    });
    fs.writeFile('target/examplar2.csv', examplarCompactBean.process(ptmExpandingBuilder1.args2csv()), err => {
        if (err) {
            console.error(err);
        }
    });
});


