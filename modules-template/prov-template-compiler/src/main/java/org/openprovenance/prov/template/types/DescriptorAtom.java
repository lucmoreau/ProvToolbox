package org.openprovenance.prov.template.types;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.openprovenance.prov.model.exception.InvalidCaseException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@JsonPropertyOrder({"@type", "number", "value"})
public class DescriptorAtom implements Descriptor {
    Integer number;
    List<String> value;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    Map<String, Set<String>> idata;

    public DescriptorAtom(Integer number, String value) {
        this.number=number;
        this.value = List.of(value);
    }
    public DescriptorAtom(Integer number, List<String> value, Map<String, Set<String>> idata) {
        this.number=number;
        this.value = value;
        if (idata!=null && !idata.isEmpty()) {
            this.idata=new HashMap<>(idata);
        }
    }

    @Override
    public String toText(Function<String,String> relationTranslator) {
        return value.stream().collect(Collectors.joining(","));
    }

    @Override
    @JsonProperty("@type")
    public String getCategory() {
        return "atom";
    }

    public Integer getNumber() {
        return number;
    }
    public List<String> getValue() {
        return value;
    }
    public Map<String,Set<String>> getIdata() {
        return idata;
    }

    /**
     * Compares this object with the specified object for order.  Returns a
     * negative integer, zero, or a positive integer as this object is less
     * than, equal to, or greater than the specified object.
     *
     * <p>The implementor must ensure
     * {@code sgn(x.compareTo(y)) == -sgn(y.compareTo(x))}
     * for all {@code x} and {@code y}.  (This
     * implies that {@code x.compareTo(y)} must throw an exception iff
     * {@code y.compareTo(x)} throws an exception.)
     *
     * <p>The implementor must also ensure that the relation is transitive:
     * {@code (x.compareTo(y) > 0 && y.compareTo(z) > 0)} implies
     * {@code x.compareTo(z) > 0}.
     *
     * <p>Finally, the implementor must ensure that {@code x.compareTo(y)==0}
     * implies that {@code sgn(x.compareTo(z)) == sgn(y.compareTo(z))}, for
     * all {@code z}.
     *
     * <p>It is strongly recommended, but <i>not</i> strictly required that
     * {@code (x.compareTo(y)==0) == (x.equals(y))}.  Generally speaking, any
     * class that implements the {@code Comparable} interface and violates
     * this condition should clearly indicate this fact.  The recommended
     * language is "Note: this class has a natural ordering that is
     * inconsistent with equals."
     *
     * <p>In the foregoing description, the notation
     * {@code sgn(}<i>expression</i>{@code )} designates the mathematical
     * <i>signum</i> function, which is defined to return one of {@code -1},
     * {@code 0}, or {@code 1} according to whether the value of
     * <i>expression</i> is negative, zero, or positive, respectively.
     *
     * @param o the object to be compared.
     * @return a negative integer, zero, or a positive integer as this object
     * is less than, equal to, or greater than the specified object.
     * @throws NullPointerException if the specified object is null
     * @throws ClassCastException   if the specified object's type prevents it
     *                              from being compared to this object.
     */
    @Override
    public int compareTo(Descriptor o) {
        if (o instanceof DescriptorTree) {
            return -1;
        }
        if (o instanceof DescriptorAtom) {
            return this.value.toString().compareTo(((DescriptorAtom)o).value.toString());
        }
        throw new InvalidCaseException(o.toString());
    }
}
