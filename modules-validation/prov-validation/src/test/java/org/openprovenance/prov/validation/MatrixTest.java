package org.openprovenance.prov.validation;

import junit.framework.TestCase;

import org.openprovenance.prov.validation.matrix.SparseMatrix;
import org.openprovenance.prov.validation.report.ValidationReport;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class MatrixTest extends TestCase {

	static ValidationReport report;

	static Logger logger = LogManager.getLogger(MatrixTest.class);

	public MatrixTest(String testName) {
		super(testName);
	}

	public void IGNOREDtestMatrix1() {
		System.out.println("1 :" +  Integer.valueOf(1).hashCode());
		System.out.println("2 :" +  Integer.valueOf(2).hashCode());
		System.out.println("1000d :" +  Integer.valueOf(1000).hashCode());
	}

	public void testMatrix2() {
		SparseMatrix m = new SparseMatrix(10, 10);
        assertNull(m.g(0, 0));
		m.set(0, 0, 10);
        assertEquals(10, m.g(0, 0).getValue());

		m.set(0, 1, 11);
        assertEquals(10, m.g(0, 0).getValue());
        assertEquals(11, m.g(0, 1).getValue());

		m.set(4, 5, 20);
        assertEquals(10, m.g(0, 0).getValue());
        assertEquals(11, m.g(0, 1).getValue());
        assertEquals(20, m.g(4, 5).getValue());
		logger.debug(" m is " + m);
		logger.debug(" m row 0 is " + m.getRow(0));
	}

}
