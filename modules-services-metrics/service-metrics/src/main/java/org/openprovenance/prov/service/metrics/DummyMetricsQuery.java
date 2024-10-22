package org.openprovenance.prov.service.metrics;

class DummyMetricsQuery extends MetricsQuery {
    public DummyMetricsQuery() {
        super(null);
    }

    @Override
    public TmpTypeMap getTypeMap() {
        TmpTypeMap tmp = new TmpTypeMap();
        tmp.list="[]";
        tmp.map="{}";
        tmp.set="[]";
        return tmp;
    }

    @Override
    public String insertTypeMap(String map, String set, String list) {
        System.out.println("dummy insertTypeMap " + map);
        return "TM0";
    }

    @Override
    public String insertMetricsRecord(String artifact, String url, String features, String counts, String validity, String traffic, String hash) {
        return "MR0";
    }
}