package org.openprovenance.prov.service.core;

import org.junit.Test;
import org.openprovenance.prov.service.core.readers.QueryParameters;

import static org.junit.Assert.assertEquals;

/**
 * The query service's parameter substitution (nextgen T-438, ruling D3 of 2026-10-11: "a test proving the
 * injection first, then a ONE-pass substitution").
 *
 * <p>The contract these tests state: every {@code :name} token of the SQL that names a supplied (or header-defaulted)
 * parameter is replaced ONCE by that parameter's literal; a replaced value is never rescanned, so a value may
 * contain {@code :otherName} without being rewritten; tokens inside single-quoted literals and inside {@code --}
 * or block comments are left alone; {@code ::} is a cast, not a parameter; the result does not depend on the
 * order the parameters were supplied in; a name is matched whole ({@code :tenant} never touches
 * {@code :tenantFilter}); an unknown {@code :name} stays as it is.</p>
 *
 * <p>Before the one-pass rewrite, the substitution was a sequence of {@code String.replace} calls, one per
 * parameter in supply order, each over the WHOLE text including the values the earlier ones had inserted. These
 * tests fail on that code: {@link #aValueIsNeverRescanned} (the injection: the second value breaks out of the
 * first one's quotes), {@link #aNameIsMatchedWhole} (the prefix clash), {@link #theResultDoesNotDependOnSupplyOrder},
 * and the literal / comment cases.</p>
 */
public class QuerySubstitutionTest {

    private static QueryParameters params(Object... kv) {
        QueryParameters p = new QueryParameters();
        for (int i = 0; i < kv.length; i += 2) p.put((String) kv[i], kv[i + 1]);
        return p;
    }

    private static String sub(String sql, Object... kv) {
        return QueryService.substituteParams(sql, params(kv));
    }

    private static final String Q = "'";       // one single quote
    private static final String QQ = "''";     // a doubled quote inside a literal

    @Test
    public void aValueIsNeverRescanned() {
        // the injection: a's value names :b; b's value carries a quote-closing payload. Quote doubling cannot help
        // once the second replacement rewrites :b INSIDE a's already-quoted literal.
        String sql = "SELECT * FROM t WHERE a = :a AND b = :b";
        String payload = Q + ") OR 1=1 OR (" + Q;
        assertEquals("SELECT * FROM t WHERE a = 'x :b y' AND b = " + Q + QQ + ") OR 1=1 OR (" + QQ + Q,
                sub(sql, "a", "x :b y", "b", payload));
    }

    @Test
    public void aNameIsMatchedWhole() {
        String sql = "WHERE tenant = :tenant AND f = :tenantFilter AND d = :sinceDays AND s = :since";
        assertEquals("WHERE tenant = 'silubi' AND f = 'F' AND d = '30' AND s = 'S'",
                sub(sql, "tenant", "silubi", "tenantFilter", "F", "since", "S", "sinceDays", "30"));
    }

    @Test
    public void theResultDoesNotDependOnSupplyOrder() {
        String sql = "WHERE a = :a AND b = :b";
        assertEquals(sub(sql, "a", "1 :b", "b", "2 :a"), sub(sql, "b", "2 :a", "a", "1 :b"));
        assertEquals("WHERE a = '1 :b' AND b = '2 :a'", sub(sql, "b", "2 :a", "a", "1 :b"));
    }

    @Test
    public void literalsAndCommentsAreLeftAlone() {
        String sql = "-- :since default 30\n"
                + "SELECT 1 /* :since here */ FROM t WHERE e LIKE ':res.partner:%' AND s = :since AND r = :res";
        assertEquals("-- :since default 30\n"
                + "SELECT 1 /* :since here */ FROM t WHERE e LIKE ':res.partner:%' AND s = '30' AND r = 'R'",
                sub(sql, "res", "R"));
    }

    @Test
    public void aCastIsNotAParameter() {
        assertEquals("SELECT '1'::text, y::int, 'a'::text FROM t WHERE x = '1'",
                sub("SELECT :x::text, y::int, 'a'::text FROM t WHERE x = :x", "x", "1"));
    }

    @Test
    public void quotesAreDoubledAndNullIsNull() {
        assertEquals("WHERE n = 'O" + QQ + "Brien' AND v IS NULL",
                sub("WHERE n = :n AND v IS :v", "n", "O" + Q + "Brien", "v", null));
        assertEquals("WHERE a = " + Q + QQ + "; DROP TABLE t; --" + Q,
                sub("WHERE a = :a", "a", Q + "; DROP TABLE t; --"));
    }

    @Test
    public void anUnknownPlaceholderStays() {
        assertEquals("SELECT :nobody, 'x' FROM t WHERE a = 'A'",
                sub("SELECT :nobody, 'x' FROM t WHERE a = :a", "a", "A"));
        assertEquals("SELECT :nobody", sub("SELECT :nobody"));
    }

    @Test
    public void aHeaderDefaultFillsAnOmittedParameterOnly() {
        String sql = "-- :tenant default all\n-- :since default 1970-01-01\nWHERE t = :tenant AND s = :since";
        assertEquals("-- :tenant default all\n-- :since default 1970-01-01\nWHERE t = 'silubi' AND s = '1970-01-01'",
                sub(sql, "tenant", "silubi"));
    }
}
