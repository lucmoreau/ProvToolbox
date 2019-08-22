
package org.openprovenance.prov.model;

import java.io.PrintStream;
import java.lang.reflect.Method;
import java.util.*;

import org.apache.commons.collections4.bag.HashBag;
import org.apache.log4j.Logger;

/**
 * @author Trung Dong Huynh &lt;tdh@ecs.soton.ac.uk&gt;
 * @author lavm
 * Comparator Class
 *
 */


public class DocumentEquality {
	static Logger logger = Logger.getLogger(DocumentEquality.class);

	private boolean mergeDuplicates;

	final private PrintStream out;

	public DocumentEquality() {
		this.mergeDuplicates = false;
		this.out=null;
	}


	public DocumentEquality(boolean mergeDuplicates, PrintStream out) {
		this.mergeDuplicates = mergeDuplicates;
		this.out=out;
	}

	public void log(Object s) {
		if (out!=null) {
			out.println(s);
		} else {
			logger.debug(s);
		}
	}

	private boolean collectionEqual(Collection<?> c1, Collection<?> c2) {
		Collection<?> bag1;
		Collection<?> bag2;
		if (mergeDuplicates) {
			bag1 = new HashSet<Object>(c1);
			bag2 = new HashSet<Object>(c2);
		} else {
			bag1 = new HashBag(c1);
			bag2 = new HashBag(c2);
		}
		System.out.println("bag1: " +bag1);
		System.out.println("bag2: " +bag2);
		return bag1.equals(bag2);
	}


	@SuppressWarnings("unchecked")
	private boolean statementEqual(StatementOrBundle r1, StatementOrBundle r2) {
		// If one of the statements is a named bundle
		if (r1 instanceof Bundle && r2 instanceof Bundle) {
			Bundle b1 = (Bundle) r1;
			Bundle b2 = (Bundle) r2;
			if (!b1.getId().equals(b2.getId()))
				return false;
			List<?> stmts1 = b1.getStatement();
			List<?> stmts2 = b2.getStatement();
			return statementListEqual((List<StatementOrBundle>) stmts1,
					(List<StatementOrBundle>) stmts2);
		}
		// Two normal statements
		Class<?> class1 = r1.getClass();
		if (class1 != r2.getClass()) {
			return false;
		}
		if (r1.equals(r2)) {
			return true;
		}
		Method[] allMethods = class1.getDeclaredMethods();
		for (Method m : allMethods) {
			String methodName = m.getName();
			if (methodName.startsWith("get") && (!methodName.equals("getAll"))&& (!methodName.equals("getAttributes"))) {
				try {
					Object attr1 = m.invoke(r1);
					Object attr2 = m.invoke(r2);
					if (attr1 == null && attr2 == null)
						continue;
					// Try the standard check first. This will also fail the
					// test if either attributes is null.
					if (attr1.equals(attr2))
						continue;
					if (attr1 instanceof Collection<?>
							&& attr2 instanceof Collection<?>)
						if (collectionEqual((Collection<?>) attr1,
								(Collection<?>) attr2))
							continue;

					if (( attr1.getClass().isArray())
							&& attr2 .getClass().isArray())
						if (collectionEqual(Set.of((Object [])attr1),
								Set.of((Object [])attr2)))
							continue;

						System.out.println("%%%%%%%%%%%% method " + methodName);
					/*
					 * // the two attributes are not equal String attrName =
					 * methodName.substring(3);
					 * System.out.println("The following " + attrName +
					 * " attributes are not the same");
					 * System.out.println(attr1); System.out.println(attr2);
					 *
					 * logger.debug("The following " + attrName +
					 * " attributes are not the same"); logger.debug(attr1);
					 * logger.debug(attr2);
					 */
					return false;
				} catch (Exception e) {
					// Any exception means no equality
					log(e);
					return false;
				}
			}
		}

		return true;
	}

	private boolean statementListEqual(List<StatementOrBundle> stmts1,
									   List<StatementOrBundle> stmts2) {
		if (stmts1.size() != stmts2.size()) {
			return false;
		}
		// Cloning the lists to avoid modification of the originals
		List<StatementOrBundle> list1 = new ArrayList<StatementOrBundle>(stmts1);
		List<StatementOrBundle> list2 = new ArrayList<StatementOrBundle>(stmts2);
		for (StatementOrBundle stmt1 : list1) {
			// Try to find the same in list2
			StatementOrBundle found = null;
			for (StatementOrBundle stmt2 : list2) {
				if (statementEqual(stmt1, stmt2)) {
					found = stmt2;
					break;
				}
			}
			if (found == null) {
				log("---------------------------");
				log("Cannot find the following statement in the second document");
				log("---------------------------");
				log("statement: " + stmt1);
				log("---------------------------");
				log("list of statements: ");
				log(stmts2);
				log("---------------------------");

				return false;
			}
			list2.remove(found);
		}

		return true;
	}

	public boolean check(Document d1, Document d2) {
		return statementListEqual(d1.getStatementOrBundle(),
				d2.getStatementOrBundle());
	}

}
