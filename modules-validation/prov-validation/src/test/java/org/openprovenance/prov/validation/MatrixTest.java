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

	public void testMatrix1() {
		System.out.println("1 :" +  Integer.valueOf(1).hashCode());
		System.out.println("2 :" +  Integer.valueOf(2).hashCode());
		System.out.println("1000d :" +  Integer.valueOf(1000).hashCode());
	}

	public void testMatrix2() throws jakarta.xml.bind.JAXBException,
			java.io.IOException {
		SparseMatrix m = new SparseMatrix(10, 10);
		assertTrue(m.g(0, 0) == null);
		m.set(0, 0, 10);
		assertTrue(m.g(0, 0).getValue() == 10);

		m.set(0, 1, 11);
		assertTrue(m.g(0, 0).getValue() == 10);
		assertTrue(m.g(0, 1).getValue() == 11);

		m.set(4, 5, 20);
		assertTrue(m.g(0, 0).getValue() == 10);
		assertTrue(m.g(0, 1).getValue() == 11);
		assertTrue(m.g(4, 5).getValue() == 20);
		System.out.println(" m is " + m);
		System.out.println(" m row 0 is "
				+ ((org.openprovenance.prov.validation.matrix.SparseMatrix) m).getRow(0));
	}

}
