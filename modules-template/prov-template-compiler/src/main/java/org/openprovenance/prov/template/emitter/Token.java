package org.openprovenance.prov.template.emitter;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class Token implements Element {
    public final String token;

    public Token(String token) {
        this.token=token;
    }

    @Override
    public String toString() {
        return "Token{" + token + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Token token1 = (Token) o;

        return new EqualsBuilder().append(token, token1.token).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(token).toHashCode();
    }
}
