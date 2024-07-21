package org.openprovenance.prov.model.test;

import junit.framework.TestCase;
import org.openprovenance.apache.commons.lang.StringEscapeUtils;

public class StringTest extends TestCase {

    public void testStringEscapeUtils0() {
        String s="hel\"lo";
        String t= StringEscapeUtils.escapeCsv(s);
        assertEquals("\"hel\"\"lo\"",t);
    }

    public void testStringEscapeUtils1() {
        String s="\"hello\"";
        String t=StringEscapeUtils.escapeCsv(s);
        assertEquals("\"\"\"hello\"\"\"",t);
    }
    public void testStringEscapeUtils2() {
        String s="hello";
        String t=StringEscapeUtils.escapeCsv(s);
        assertEquals("hello",t);
    }

    public void testStringEscapeUtils3() {
        String s="hel,lo";
        String t=StringEscapeUtils.escapeCsv(s);
        assertEquals("\"hel,lo\"",t);
    }

    public void testStringEscapeUtils4() {
        String s="hel\nlo";
        String t=StringEscapeUtils.escapeCsv(s);
        assertEquals("\"hel\nlo\"",t);
    }
    public void testStringEscapeUtils5() {
        String s="hel\rlo";
        String t=StringEscapeUtils.escapeCsv(s);
        assertEquals("\"hel\rlo\"",t);
    }

    public void testString() {
        String str1 = "abc";
        String str2 = str1.replace("\"", "\"\"");
        assertEquals(str1,str2);
        assert(str1==str2);
    }



}
