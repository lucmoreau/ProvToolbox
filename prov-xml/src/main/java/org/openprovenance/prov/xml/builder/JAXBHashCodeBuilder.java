package org.openprovenance.prov.xml.builder;

import javax.xml.bind.JAXBElement;

import org.apache.commons.lang.builder.HashCodeBuilder;

public class JAXBHashCodeBuilder extends HashCodeBuilder {

	@Override
	public HashCodeBuilder append(Object object) {
		if (object == null) {
			appendSuper(0);
			return this;
		} else {
			final Class theClass = object.getClass();
			if (theClass.isArray()) {
				super.append(object);
			} else {
				if (object instanceof Iterable<?>) {
					append((Iterable<?>) object);
				} else if (object instanceof JAXBElement<?>) {
					append((JAXBElement<?>) object);
				} else {
					super.append(object);
				}
			}
		}
		return this;
	}

	public HashCodeBuilder append(Iterable<?> iterable) {
		if (iterable == null) {
			appendSuper(0);
			return this;
		}
		for (Object element : iterable) {
			append(element);
		}
		return this;
	}

	public HashCodeBuilder append(JAXBElement<?> element) {
		if (element == null) {
			appendSuper(0);
			return this;
		} else {
			append(element.getName()).append(element.getScope()).append(
					element.getValue());
			return this;
		}
	}

}
