package org.openprovenance.prov.xml.builder;

import java.util.Iterator;

import javax.xml.bind.JAXBElement;

import org.apache.commons.lang.builder.EqualsBuilder;


public class JAXBEqualsBuilder extends EqualsBuilder {

	@Override
	public EqualsBuilder append(Object lhs, Object rhs) {
		if (!isEquals()) {
			return this;
		}
		if (lhs == rhs) {
			return this;
		}
		if (lhs == null || rhs == null) {
			this.setEquals(false);
			return this;
		}
		final Class lhsClass = lhs.getClass();
		if (lhsClass.isArray()) {
			super.append(lhs, rhs);
		} else {
			if (lhs instanceof Iterable<?> && rhs instanceof Iterable<?>) {
				append((Iterable<?>) lhs, (Iterable<?>) rhs);
			} else if (lhs instanceof JAXBElement<?>
					&& rhs instanceof JAXBElement<?>) {
				append((JAXBElement<?>) lhs, (JAXBElement<?>) rhs);
			} else if (lhs instanceof Equals) {
				((Equals) lhs).equals(rhs, this);
			} else {
				setEquals(lhs.equals(rhs));
			}
		}
		return this;
	}

	public EqualsBuilder append(Iterable<?> lhs, Iterable<?> rhs) {
		if (!isEquals()) {
			return this;
		}
		if (lhs == rhs) {
			return this;
		}
		if (lhs == null || rhs == null) {
			this.setEquals(false);
			return this;
		}

		final Iterator<?> e1 = lhs.iterator();
		final Iterator<?> e2 = rhs.iterator();

		while (e1.hasNext() && e2.hasNext() && isEquals()) {
			append(e1.next(), e2.next());
		}
		if (e1.hasNext() || e2.hasNext())
			setEquals(false);
		return this;
	}

	public EqualsBuilder append(JAXBElement<?> lhs, JAXBElement<?> rhs) {
		if (!isEquals()) {
			return this;
		}
		if (lhs == rhs) {
			return this;
		}
		if (lhs == null || rhs == null) {
			this.setEquals(false);
			return this;
		}

		append(lhs.getName(), rhs.getName())
		// .append(lhs.getScope(), rhs.getScope())
				.append(lhs.getValue(), rhs.getValue());
		return this;
	}

}
