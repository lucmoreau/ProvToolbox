package org.openprovenance.prov.validation;

/**
 * This class is introduced as a wrapper utility, to wrap a VarQName, so that
 * they can be indexed according to their URI. QName does not allow equals and
 * hashcode methods to be redefined.
 */
public class VarQNameWrapper {

    final private VarQName vqn;

    public VarQName getVarQName() {
	return vqn;
    }

    public VarQNameWrapper(VarQName vqn) {
	this.vqn = vqn;
    }

    public int hashCode() {
	return vqn.getUri().hashCode();
    }

    public boolean equals(Object o) {
	if (o instanceof VarQNameWrapper) {
	    return ((VarQNameWrapper) o).vqn.getUri().equals(this.vqn.getUri());
	} else
	    return false;
    }

}
