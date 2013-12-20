package org.openprovenance.prov.xml.builder;

import javax.xml.bind.JAXBElement;

import org.apache.commons.lang.SystemUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class JAXBToStringBuilder extends ToStringBuilder {

	public static final ToStringStyle JAXB_STYLE = new JAXBToStringStyle();

	private static ToStringStyle defaultStyle = JAXBToStringBuilder.JAXB_STYLE;

	public static ToStringStyle getDefaultStyle() {
		return defaultStyle;
	}

	public JAXBToStringBuilder(Object object, ToStringStyle style,
			StringBuffer buffer) {
		super(object, style, buffer);
	}

	public JAXBToStringBuilder(Object object, ToStringStyle style) {
		super(object, style);
	}

	public JAXBToStringBuilder(Object object) {
		this(object, getDefaultStyle(), null);
	}

	private static final class JAXBToStringStyle extends ToStringStyle {

		private static final long serialVersionUID = 1L;

		/**
		 * <p>
		 * Constructor.
		 * </p>
		 * 
		 * <p>
		 * Use the static constant rather than instantiating.
		 * </p>
		 */
		private JAXBToStringStyle() {
			super();
			this.setContentStart("[");
			this.setFieldSeparator(SystemUtils.LINE_SEPARATOR + "  ");
			this.setFieldSeparatorAtStart(true);
			this.setContentEnd(SystemUtils.LINE_SEPARATOR + "]");
		}

		/**
		 * <p>
		 * Ensure <code>Singleton</code> after serialization.
		 * </p>
		 * 
		 * @return the singleton
		 */
		private Object readResolve() {
			return ToStringStyle.MULTI_LINE_STYLE;
		}

		@Override
		protected void appendSummary(StringBuffer buffer, String fieldName,
				Object value) {
			super.appendSummary(buffer, fieldName, value);
		}

		@Override
		protected void appendDetail(StringBuffer buffer, String fieldName,
				Object value) {

			if (value instanceof JAXBElement<?>) {
				appendDetail(buffer, fieldName, (JAXBElement<?>) value);
			} else {
				super.appendDetail(buffer, fieldName, value);
			}

		}

		protected void appendDetail(StringBuffer buffer, String fieldName,
				JAXBElement<?> element) {
			buffer.append('<');
			buffer.append(element.getName());
			buffer.append('>');
			buffer.append(element.getValue());
		}
	}
}
