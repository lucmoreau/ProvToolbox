package org.openprovenance.prov.template.types;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.openprovenance.prov.model.exception.InvalidCaseException;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.openprovenance.prov.template.types.TypesRecordProcessor.numeral;

@JsonPropertyOrder({"@type", "number", "count", "relation", "arguments"})
public class DescriptorTree implements Descriptor {
    final String category="tree";
    final Integer number;
    long count;
    String relation;
    List<Descriptor> arguments;


    public DescriptorTree(Integer number, long count, String relation, List<Descriptor> arguments) {
        this.number=number;
        this.count=count;
        this.relation = relation;
        this.arguments = arguments;
    }

    @Override
    public String toText(Function<String,String> relationTranslator) {
        return numeral(count) + relationTranslator.apply(relation) + arguments.stream().map(d -> d.toText(relationTranslator)).collect(Collectors.joining(",", "(", ")"));
    }

    @Override
    @JsonProperty("@type")
    public String getCategory() {
        return category;
    }

    public Integer getNumber() {
        return number;
    }

    public long getCount() {
        return count;
    }

    public String getRelation() {
        return relation;
    }
/*
    @JsonTypeInfo(use=JsonTypeInfo.Id.NAME, include=JsonTypeInfo.As.PROPERTY, property = "@type")
    @JsonSubTypes({
            @JsonSubTypes.Type(value = DescriptorAtom.class, name = "atom"),
            @JsonSubTypes.Type(value = DescriptorTree.class, name = "tree")
    })

 */
    public List<Descriptor> getArguments() {
        return arguments;
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
        if (o instanceof DescriptorAtom) {
            return 1;
        }
        if (o instanceof DescriptorTree) {
            DescriptorTree that=(DescriptorTree) o;

            final int relCompare = this.relation.compareTo(that.getRelation());
            if (relCompare !=0) { return relCompare; }

            int thisSize=this.arguments.size();
            int thatSize=that.arguments.size();
            int minSize=Math.min(thisSize,thatSize);

            for (int i=0; i< minSize; i++) {
                Descriptor thisDescriptor = this.arguments.get(i);
                Descriptor thatDescriptor = that.arguments.get(i);
                final int descriptorCompare = thisDescriptor.compareTo(thatDescriptor);
                if (descriptorCompare != 0) {
                    return descriptorCompare;
                }
            }

            return Integer.compare(thisSize, thatSize);

        }
        throw new InvalidCaseException(o.toString());
    }

}
