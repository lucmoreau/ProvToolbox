package org.openprovenance.prov.template.types;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.openprovenance.prov.model.exception.InvalidCaseException;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

@JsonPropertyOrder({"@type", "number", "value"})
public class DescriptorAtom implements Descriptor {
    Integer number;
    List<String> value;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    Map<String, Set<String>> idata;

    /*
    public DescriptorAtom(Integer number, String value) {
        this.number=number;
        this.value = List.of(value);
    }

     */
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
            final DescriptorAtom that = (DescriptorAtom) o;
            final int relCompare = this.value.toString().compareTo(that.value.toString());
            if (relCompare !=0) { return relCompare; }

            if (this.idata==null) {
                if (that.idata==null) {
                    return 0;
                } else {
                    return -1;
                }
            } else {
                if (that.idata==null) {
                    return 1;
                }
            }

            List<String> allIdataKeys  = new LinkedList<>();
            allIdataKeys.addAll(this.idata.keySet());
            allIdataKeys.addAll(that.idata.keySet());
            allIdataKeys.sort(String::compareTo);

            AtomicInteger theComparison= new AtomicInteger();

            for (String k: allIdataKeys) {
                Set<String> thisData=this.idata.get(k);
                Set<String> thatData=this.idata.get(k);
                if (thisData==null) {
                    theComparison.set(1);
                    break;
                }
                if (thatData==null) {
                    theComparison.set(-1);
                    break;
                }
                List<String> thisL=thisData.stream().sorted(String::compareTo).collect(Collectors.toList());
                List<String> thatL=thatData.stream().sorted(String::compareTo).collect(Collectors.toList());
                if (thisL.equals(thatL)) {
                    // continue iteration
                } else {
                    theComparison.set(thisL.toString().compareTo(thatL.toString()));
                    break;
                }
            }

            return theComparison.get();
        }
        throw new InvalidCaseException(o.toString());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DescriptorAtom that = (DescriptorAtom) o;
        return Objects.equals(number, that.number) && Objects.equals(value, that.value) && Objects.equals(idata, that.idata);
    }

    @Override
    public int hashCode() {
        return Objects.hash(number, value, idata);
    }
}
