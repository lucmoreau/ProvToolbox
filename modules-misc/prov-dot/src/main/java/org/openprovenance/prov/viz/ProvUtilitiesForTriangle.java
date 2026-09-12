package org.openprovenance.prov.viz;

import org.openprovenance.prov.model.*;
import org.openprovenance.prov.vanilla.QualifiedHadMember;
import org.openprovenance.prov.vanilla.QualifiedSpecializationOf;

import java.util.*;
import java.util.stream.Collectors;

import static org.openprovenance.prov.model.NamespacePrefixMapper.PROV_EXT_NS;

/**
 * Utilities for the qualified rendering: every relation is n-ary, its other causes being the statements
 * its {@code provext:*} attributes point to, so that the triangles (usage-generation-derivation,
 * association-generation-attribution, ...) are drawn as such.
 */
public class ProvUtilitiesForTriangle extends ProvUtilities {
    private final List<String> exceptions;

    public ProvUtilitiesForTriangle() {
        super();
        exceptions = List.of();
    }

    /** @param exceptions local names of relations to keep binary */
    public ProvUtilitiesForTriangle(List<String> exceptions) {
        super();
        this.exceptions = exceptions;
    }

    private List<QualifiedName> provextReferences(HasOther statement) {
        Hashtable<String, List<Other>> attributes = attributesWithNamespace(statement, PROV_EXT_NS);
        List<Other> result = attributes.values().stream().flatMap(Collection::stream).collect(Collectors.toList());
        return result.stream().map(x -> (QualifiedName) x.getValue()).collect(Collectors.toList());
    }

    @Override
    public List<QualifiedName> getOtherCauses(Relation r) {
        if (r instanceof Identifiable && ((Identifiable) r).getId() != null) {
            if (exceptions.contains(((Identifiable) r).getId().getLocalPart())) {
                return null;
            }
        }
        if (r instanceof WasAttributedTo) {
            return provextReferences((WasAttributedTo) r);
        } else if (r instanceof WasDerivedFrom) {
            WasDerivedFrom der = (WasDerivedFrom) r;
            List<QualifiedName> result = new LinkedList<>();
            if (der.getGeneration() != null) result.add(der.getGeneration());
            if (der.getUsage() != null) result.add(der.getUsage());
            if (der.getActivity() != null) result.add(der.getActivity());
            return result;
        } else if (r instanceof WasAssociatedWith) {
            List<QualifiedName> result = provextReferences((WasAssociatedWith) r);
            List<QualifiedName> otherCauses = super.getOtherCauses(r);
            if (otherCauses != null) result.addAll(otherCauses);
            return result;
        } else if (r instanceof Used) {
            return provextReferences((Used) r);
        } else if (r instanceof WasInformedBy) {
            return provextReferences((WasInformedBy) r);
        } else if (r instanceof WasStartedBy) {
            List<QualifiedName> result1 = super.getOtherCauses(r);
            List<QualifiedName> result2 = provextReferences((WasStartedBy) r);
            if (result1 != null) result2.addAll(result1);
            return result2;
        } else if (r instanceof WasEndedBy) {
            List<QualifiedName> result1 = super.getOtherCauses(r);
            List<QualifiedName> result2 = provextReferences((WasEndedBy) r);
            if (result1 != null) result2.addAll(result1);
            return result2;
        } else if (r instanceof QualifiedSpecializationOf) {
            return provextReferences((QualifiedSpecializationOf) r);
        } else if (r instanceof QualifiedHadMember) {
            return provextReferences((QualifiedHadMember) r);
        } else if (r instanceof WasGeneratedBy || r instanceof SpecializationOf || r instanceof WasInvalidatedBy) {
            return Collections.emptyList();
        } else {
            return super.getOtherCauses(r);
        }
    }
}
