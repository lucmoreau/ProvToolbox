package org.openprovenance.prov.datalog;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class DataLogTest extends TestCase
{
    public void testDataLog() throws  java.lang.Throwable {

	DataLogUtility u=new DataLogUtility();

	String s=u.asn2datalog("../asn/src/test/resources/prov/w3c-publication1.prov-asn",null);

	System.out.println("=============");
	System.out.println(s);
	System.out.println("=============");

    }
}


