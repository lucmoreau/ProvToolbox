package org.openprovenance.prov.validation;


import org.openprovenance.prov.model.ProvUtilities;
import org.openprovenance.prov.model.QualifiedName;
import org.openprovenance.prov.model.WasEndedBy;
import org.openprovenance.prov.model.WasGeneratedBy;
import org.openprovenance.prov.model.WasInvalidatedBy;
import org.openprovenance.prov.model.WasStartedBy;

public class TwoKeys {
	private final String key1;
	private final String key2;
	static ProvUtilities u = new ProvUtilities();

	public TwoKeys(String key1, String key2) {
		this.key1 = key1;
		this.key2 = key2;
		if (key1 == null)
			throw new NullPointerException("key1");
		if (key2 == null)
			throw new NullPointerException("key2");
	}

	public TwoKeys(QualifiedName a2, QualifiedName a1) {
		this(a2.getUri(), a1.getUri());
	}


	public TwoKeys(WasGeneratedBy gen) {
		this(gen.getEntity(), gen.getActivity());
	}

	public TwoKeys(WasInvalidatedBy inv) {
		this(inv.getEntity(), inv.getActivity());
	}

	public TwoKeys(WasStartedBy start) {
		this(start.getActivity(), start.getStarter());
	}

	public TwoKeys(WasEndedBy end) {
		this(end.getActivity(), end.getEnder());
	}

	static public TwoKeys makeTwoKeys(Object o) {
		if (o instanceof WasGeneratedBy)
			return new TwoKeys((WasGeneratedBy) o);
		if (o instanceof WasInvalidatedBy)
			return new TwoKeys((WasInvalidatedBy) o);
		if (o instanceof WasStartedBy)
			return new TwoKeys((WasStartedBy) o);
		if (o instanceof WasEndedBy)
			return new TwoKeys((WasEndedBy) o);
		throw new UnsupportedOperationException();
	}

	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof TwoKeys)) {
			return false;
		}
		TwoKeys other = (TwoKeys) obj;
		return key1.equals(other.key1) && key2.equals(other.key2);
	}

	public int hashCode() {
		int result = 17;
		result = 37 * result + key1.hashCode();
		result = 37 * result + key2.hashCode();
		return result;
	}

	public String toString() {
		return "<" + key1 + "," + key2 + ">";
	}

}
