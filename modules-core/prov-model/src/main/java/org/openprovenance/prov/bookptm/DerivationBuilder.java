package org.openprovenance.prov.bookptm;

import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.Name;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.model.QualifiedName;
import org.openprovenance.prov.model.builder.Builder;
import org.openprovenance.prov.model.builder.Prefix;

import static org.openprovenance.prov.model.NamespacePrefixMapper.PROV_EXT_NS;

public class DerivationBuilder {

    static final String EX_NS_URI = "http://example.org/ns/";
    static final String EX_IDS_URI = "http://example.org/id/";
    static final String FOAF_URI="http://xmlns.com/foaf/0.1/";
    public static ProvFactory pFactory=new org.openprovenance.prov.vanilla.ProvFactory();
    public static Name name=pFactory.getName();

    public Document makeDocument_Transporting() {
        Builder builder = new Builder(pFactory, pFactory, pFactory);
        Definitions defs = getDefinitions(builder);
        transportingDescription(builder, defs);
        Document doc=builder.build();
        return doc;
    }

    public Document makeDocument_TransportingFullTriangle() {
        Builder builder = new Builder(pFactory, pFactory, pFactory);
        Definitions defs = getDefinitions(builder);
        transportingDescriptionFullTriangle(builder, defs);
        Document doc=builder.build();
        return doc;
    }

    public Document makeDocument_Weighing() {
        Builder builder = new Builder(pFactory, pFactory, pFactory);
        Definitions defs = getDefinitions(builder);
        weighingDescription(builder, defs, true);
        Document doc=builder.build();
        return doc;
    }

    public Document makeDocument_TransportingAttribution() {
        Builder builder = new Builder(pFactory, pFactory, pFactory);
        Definitions defs = getDefinitions(builder);
        transportingAttribution(builder, defs, true);
        Document doc=builder.build();
        return doc;
    }

    public Document makeDocument_Attribution() {
        Builder builder = new Builder(pFactory, pFactory, pFactory);
        Definitions defs = getDefinitions(builder);
        attributionDescription(builder, defs, true);
        Document doc=builder.build();
        return doc;
    }

    public Document makeDocument_AttributionFullTriangle() {
        Builder builder = new Builder(pFactory, pFactory, pFactory);
        Definitions defs = getDefinitions(builder);
        attributionDescriptionFullTriangle(builder, defs, true);
        Document doc=builder.build();
        return doc;
    }

    public Document makeDocument_Attribution2FullTriangle() {
        Builder builder = new Builder(pFactory, pFactory, pFactory);
        Definitions defs = getDefinitions(builder);
        attributionDescription2FullTriangle(builder, defs, true);
        Document doc=builder.build();
        return doc;
    }

    public Document makeDocument_Specialization() {
        Builder builder = new Builder(pFactory, pFactory, pFactory);
        Definitions defs = getDefinitions(builder);
        specializationDescription(builder, defs, true);
        Document doc=builder.build();
        return doc;
    }

    public Document makeDocument_SpecializationFullTriangle() {
        Builder builder = new Builder(pFactory, pFactory, pFactory);
        Definitions defs = getDefinitions(builder);
        specializationDescriptionFullTriangle(builder, defs, true);
        Document doc=builder.build();
        return doc;
    }


    public Document makeDerivation() {
        Builder builder = new Builder(pFactory, pFactory, pFactory);
        return makeDerivation(builder);
    }

    public Document makeDerivation(Builder builder) {

        Definitions defs = getDefinitions(builder);

        transportingDescription(builder, defs);

        attributionDescription(builder, defs, false);

        weighingDescription(builder, defs, false);

        transportingAttribution(builder, defs, false);

        specializationDescription(builder, defs, false);

        Document doc = builder.build();
        return doc;
    }

    private void attributionDescription(Builder builder, Definitions defs, boolean standalone) {

        if (standalone) {

            builder.entity()
                    .id(defs.XID, "b34/5").aka()
                    .type(defs.Box)
                    .label("a box")
                    .build();
        }

        builder.agent()
                .id(defs.XID, "luc").knownAsLocal()
                .type(name.PROV_PERSON)
                .build();

        builder.wasAttributedTo()
                .entity("b34/5")
                .agent("luc")
                .type(defs.Ownership)
                .build();
    }

    private void attributionDescriptionFullTriangle(Builder builder, Definitions defs, boolean standalone) {

        if (standalone) {

            builder.entity()
                    .id(defs.XID, "b34/5").aka()
                    .type(defs.Box)
                    .label("a box")
                    .build();
        }


        builder.entity()
                .id(defs.XID, "fpc045").aka()
                .type(defs.FPC)
                .label("flat-packed cardboard")
                .build();

        builder.entity()
                .id(defs.XID, "bks643").aka()
                //.type(defs.Books)
                .label("books")
                .build();


        builder.agent()
                .id(defs.XID, "luc").knownAsLocal()
                .type(name.PROV_PERSON)
                .build();

        builder.activity()
                .id(defs.XID, "a7667").knownAsLocal()
                .label("packing activity")
                .type(defs.Packing)
                .build();

        builder.wasGeneratedBy()
                .id(defs.XID, "wgb0").aka()
                .entity("b34/5")
                .activity("a7667")
                .build();

        builder.used()
                .id(defs.XID, "used1").aka()
                .activity("a7667")
                .entity("fpc045")
                .build();

        builder.used()
                .id(defs.XID, "used2").aka()
                .activity("a7667")
                .entity("bks643")
                .build();

        builder.wasDerivedFrom()
                .id(defs.XID, "deriv1").aka()
                .generatedEntity("b34/5")
                .usedEntity("fpc045")
                .activity("a7667")
                .generation("wgb0")
                .usage("used1")
                .type(defs.Packing)
                .build();

        builder.wasDerivedFrom()
                .id(defs.XID, "deriv2").aka()
                .generatedEntity("b34/5")
                .usedEntity("bks643")
                .activity("a7667")
                .generation("wgb0")
                .usage("used2")
                .type(defs.Packing)
                .build();


        builder.wasAssociatedWith()
                .id(defs.XID, "waw1").aka()
                .activity("a7667")
                .agent("luc")
                .build();


        builder.wasAttributedTo()
                .entity("b34/5")
                .agent("luc")
                .type(defs.Ownership)
                .attrQn(defs.association,"waw1")
                .attrQn(defs.generation,"wgb0")
                .attrQn(defs.activity, "a7667")
                .build();
    }

    private void attributionDescription2FullTriangle(Builder builder, Definitions defs, boolean standalone) {



        builder.entity()
                .id(defs.XID, "b34/7").aka()
                .type(defs.Box)
                .build();


        builder.entity()
                .id(defs.XID, "crdbrd843").aka()
                .label("cardboard")
                .build();

        builder.entity()
                .id(defs.XID, "bks644").aka()
                //.type(defs.Books)
                .label("books")
                .build();


        builder.agent()
                .id(defs.XID, "luc").knownAsLocal()
                .type(name.PROV_PERSON)
                .build();

        builder.activity()
                .id(defs.XID, "a7699").knownAsLocal()
                .label("unpacking activity")
                .build();

        builder.wasGeneratedBy()
                .id(defs.XID, "wgb11").aka()
                .entity("bks644")
                .activity("a7699")
                .build();

        builder.wasGeneratedBy()
                .id(defs.XID, "wgb12").aka()
                .entity("crdbrd843")
                .activity("a7699")
                .build();

        builder.used()
                .id(defs.XID, "used11").aka()
                .activity("a7699")
                .entity("b34/7")
                .build();


        builder.wasDerivedFrom()
                .id(defs.XID, "deriv11").aka()
                .generatedEntity("bks644")
                .usedEntity("b34/7")
                .activity("a7699")
                .generation("wgb11")
                .usage("used11")
                .build();

        builder.wasDerivedFrom()
                .id(defs.XID, "deriv12").aka()
                .generatedEntity("crdbrd843")
                .usedEntity("b34/7")
                .activity("a7699")
                .generation("wgb12")
                .usage("used11")
                .build();


        builder.wasAssociatedWith()
                .id(defs.XID, "waw11").aka()
                .activity("a7699")
                .agent("luc")
                .build();

        builder.wasInvalidatedBy()
                .id(defs.XID, "wib11").aka()
                .entity("b34/7")
                .activity("a7699")
                .build();


        builder.wasAttributedTo()
                .entity("b34/7")
                .agent("luc")
                .attrQn(defs.association,"waw11")
                .attrQn(defs.invalidation,"wib11")
                .attrQn(defs.activity, "a7699")
                .build();
    }


    public void weighingDescription(Builder builder, Definitions defs, boolean standalone) {

        if (standalone) {
            builder.entity()
                    .id(defs.XID, "b34/6").aka()
                    .type(defs.Box)
                    .label("a box")
                    .build();
        }

        builder.activity()
                .id(defs.XID, "a7588").knownAsLocal()
                .label("weighing activity")
                .type(defs.Weighing)
                .build();

        builder.entity()
                .id(defs.XID, "b34/7").aka()
                .type(defs.Box)
                .label("a weighted box")
                .attr(defs.weight, "8kg")
                .build();

        builder.wasGeneratedBy()
                .entity("b34/7")
                .activity("a7588")
                .build();

        builder.used()
                .activity("a7588")
                .entity("b34/6")
                .build();

        builder.wasDerivedFrom()
                .generatedEntity("b34/7")
                .usedEntity("b34/6")
                .type(defs.Weighing)
                .activity("a7588")
                .build();

        builder.agent()
                .id(defs.XID, "alice").knownAsLocal()
                .type(name.PROV_PERSON)
                .attr(defs.foaf_name, "Alice")
                .build();

        builder.wasAssociatedWith()
                .activity("a7588")
                .agent("alice")
                .role(defs.scientist)
                .build();

        builder.agent()
                .id(defs.XID, "scale101").knownAsLocal()
                .type(defs.Instrument)
                .build();

        builder.wasAssociatedWith()
                .activity("a7588")
                .agent("scale101")
                .role(defs.scale)
                .build();

    }

    private void transportingDescription(Builder builder, Definitions defs) {
        builder.entity()
                .id(defs.XID, "b34/5").aka()
                .type(defs.Box)
                .label("a box somewhere")
                .location(defs.London)
                .attr(defs.weight, "10kg")
                .build();

        builder.entity()
                .id(defs.XID, "b34/6").aka()
                .type(defs.Box)
                .label("a box elsewhere")
                .location(defs.Brighton)
                .build();

        builder.activity()
                .id(defs.XID, "a7543").knownAsLocal()
                .label("some activity")
                .type(defs.Transporting)
                .start("2024-07-16T10:05:00")
                .end("2024-07-16T12:47:00")
                .build();

        builder.wasDerivedFrom()
                .generatedEntity("b34/6")
                .usedEntity("b34/5")
                .type(defs.Transporting)
                .activity("a7543")
                .build();

        builder.wasGeneratedBy()
                .entity("b34/6")
                .activity("a7543")
                .type(defs.DropOff)
                .time("2024-07-16T11:58:00")
                .label("drop-off at destination")
                .build();

        builder.used()
                .activity("a7543")
                .entity("b34/5")
                .type(defs.PickUp)
                .time("2024-07-16T10:20:00")
                .label("pick up at origin")
                .build();
    }

    private void transportingDescriptionFullTriangle(Builder builder, Definitions defs) {
        builder.entity()
                .id(defs.XID, "b34/5").aka()
                .type(defs.Box)
                .label("a box somewhere")
                .location(defs.London)
                .attr(defs.weight, "10kg")
                .build();

        builder.entity()
                .id(defs.XID, "b34/6").aka()
                .type(defs.Box)
                .label("a box elsewhere")
                .location(defs.Brighton)
                .build();

        builder.activity()
                .id(defs.XID, "a7543").knownAsLocal()
                .label("some activity")
                .type(defs.Transporting)
                .start("2024-07-16T10:05:00")
                .end("2024-07-16T12:47:00")
                .build();

        builder.wasGeneratedBy()
                .id(defs.XID, "wgb1").aka()
                .entity("b34/6")
                .activity("a7543")
                .type(defs.DropOff)
                .time("2024-07-16T11:58:00")
                .label("drop-off at destination")
                .build();

        builder.used()
                .id(defs.XID, "used1").aka()
                .activity("a7543")
                .entity("b34/5")
                .type(defs.PickUp)
                .time("2024-07-16T10:20:00")
                .label("pick up at origin")
                .build();

        builder.wasDerivedFrom()
                .generatedEntity("b34/6")
                .usedEntity("b34/5")
                .type(defs.Transporting)
                .activity("a7543")
                .generation("wgb1")
                .usage("used1")
                .build();

    }


    public void transportingAttribution(Builder builder, Definitions defs, boolean standalone) {
        if (standalone) {
            builder.activity()
                    .id(defs.XID, "a7543").knownAsLocal()
                    .type(defs.Transporting)
                    .build();
        }

        builder.agent()
                .id(defs.XID, "ag990").knownAsLocal()
                .type(name.PROV_ORGANIZATION)
                .attr(defs.foaf_name, "BigTransport, Inc.")
                .build();


        builder.agent()
                .id(defs.XID, "bob").knownAsLocal()
                .type(name.PROV_PERSON)
                .attr(defs.foaf_name, "Bob")
                .build();

        builder.wasAssociatedWith()
                .activity("a7543")
                .agent("bob")
                .role(defs.driver)
                .build();

        builder.actedOnBehalfOf()
                .delegate("bob")
                .responsible("ag990")
                .build();

    }

    private void specializationDescription(Builder builder, Definitions defs, boolean standalone) {
        if (standalone) {

            builder.entity()
                    .id(defs.XID, "b34/5").aka()
                    .type(defs.Box)
                    .label("a box somewhere")
                    .location(defs.London)
                    .attr(defs.weight, "10kg")
                    .build();

            builder.entity()
                    .id(defs.XID, "b34/6").aka()
                    .type(defs.Box)
                    .label("a box elsewhere")
                    .location(defs.Brighton)
                    .build();

            builder.wasDerivedFrom()
                    .generatedEntity("b34/6")
                    .usedEntity("b34/5")
                    .type(defs.Transporting)
                    .build();
        }

        builder.entity()
                .id(defs.XID, "b34").aka()
                .type(defs.Box)
                .label("a box")
                .build();

        builder.specializationOf()
                .generalEntity("b34")
                .specificEntity("b34/5")
                .build();

        builder.specializationOf()
                .generalEntity("b34")
                .specificEntity("b34/6")
                .build();
    }
    private void specializationDescriptionFullTriangle(Builder builder, Definitions defs, boolean standalone) {
        if (standalone) {

            builder.entity()
                    .id(defs.XID, "b34/5").aka()
                    .type(defs.Box)
                    .label("a box somewhere")
                    .location(defs.London)
                    .attr(defs.weight, "10kg")
                    .build();

            builder.entity()
                    .id(defs.XID, "b34/6").aka()
                    .type(defs.Box)
                    .label("a box elsewhere")
                    .location(defs.Brighton)
                    .build();

            builder.wasDerivedFrom()
                    .id(defs.XID,"deriv1").aka()
                    .generatedEntity("b34/6")
                    .usedEntity("b34/5")
                    .type(defs.Transporting)
                    .build();
        }

        builder.entity()
                .id(defs.XID, "b34").aka()
                .type(defs.Box)
                .label("a box")
                .build();

        builder.specializationOf()
                .id(defs.XID,"spe1").aka()
                .generalEntity("b34")
                .specificEntity("b34/5")
                .build();

        builder.specializationOf()
                .generalEntity("b34")
                .specificEntity("b34/6")
                .attrQn(defs.specialization,"spe1")
                .attrQn(defs.derivation,"deriv1")
                .attrQn(defs.entity, "b34/5")
                .build();
    }



    public Definitions getDefinitions(Builder builder) {
        Prefix VOCAB  = builder.prefix("vocab");
        Prefix XID    = builder.prefix("xid");
        Prefix FOAF    = builder.prefix("foaf");
        Prefix PROVEXT = builder.prefix("provext");
        builder.prefix(VOCAB, EX_NS_URI);
        builder.prefix(XID, EX_IDS_URI);
        builder.prefix(FOAF, FOAF_URI);
        builder.prefix(PROVEXT, PROV_EXT_NS);

        QualifiedName Box          = builder.qn(VOCAB, "Box");
        QualifiedName FPC          = builder.qn(VOCAB, "FPC");
        QualifiedName Ownership    = builder.qn(VOCAB, "Ownership");
        QualifiedName Transporting = builder.qn(VOCAB, "Transporting");
        QualifiedName Weighing     = builder.qn(VOCAB, "Weighing");
        QualifiedName Packing     = builder.qn(VOCAB, "Packing");
        QualifiedName PickUp       = builder.qn(VOCAB, "PickUp");
        QualifiedName DropOff      = builder.qn(VOCAB, "DropOff");
        QualifiedName London       = builder.qn(VOCAB, "London");
        QualifiedName Brighton     = builder.qn(VOCAB, "Brighton");
        QualifiedName weight       = builder.qn(VOCAB, "weight");
        QualifiedName driver       = builder.qn(VOCAB, "driver");
        QualifiedName scientist    = builder.qn(VOCAB, "scientist");
        QualifiedName scale        = builder.qn(VOCAB, "scale");
        QualifiedName Instrument   = builder.qn(VOCAB, "Instrument");
        QualifiedName specialization=builder.qn(PROVEXT, "specialization");
        QualifiedName derivation    =builder.qn(PROVEXT, "derivation");
        QualifiedName entity        =builder.qn(PROVEXT, "entity");
        QualifiedName association   =builder.qn(PROVEXT, "association");
        QualifiedName generation    =builder.qn(PROVEXT, "generation");
        QualifiedName activity      =builder.qn(PROVEXT, "activity");
        QualifiedName invalidation  =builder.qn(PROVEXT, "invalidation");


        QualifiedName foaf_name    = builder.qn(FOAF, "name");
        Definitions result = new Definitions(XID, Box, FPC, Ownership, Transporting, Weighing, Packing, PickUp, DropOff, London, Brighton, weight, foaf_name, driver, scientist, scale, Instrument, specialization, derivation, entity, association, generation, activity, invalidation);
        return result;
    }

    public static class Definitions {
        public final Prefix XID;
        public final QualifiedName Box;
        public final QualifiedName FPC;
        public final QualifiedName Ownership;
        public final QualifiedName Transporting;
        public final QualifiedName Weighing;
        public final QualifiedName Packing;
        public final QualifiedName PickUp;
        public final QualifiedName DropOff;
        public final QualifiedName London;
        public final QualifiedName Brighton;
        public final QualifiedName weight;
        public final QualifiedName foaf_name;
        public final QualifiedName driver;
        public final QualifiedName scientist;
        public final QualifiedName scale;
        public final QualifiedName Instrument;
        public final QualifiedName specialization;
        public final QualifiedName derivation;
        public final QualifiedName entity;
        public final QualifiedName association;
        public final QualifiedName generation;
        public final QualifiedName activity;
        public final QualifiedName invalidation;

        public Definitions(Prefix XID, QualifiedName Box, QualifiedName fpc, QualifiedName Ownership, QualifiedName Transporting, QualifiedName Weighing, QualifiedName packing, QualifiedName PickUp, QualifiedName DropOff, QualifiedName London, QualifiedName Brighton, QualifiedName weight, QualifiedName foaf_name, QualifiedName driver, QualifiedName scientist, QualifiedName scale, QualifiedName instrument, QualifiedName specialization, QualifiedName derivation, QualifiedName entity, QualifiedName association, QualifiedName generation, QualifiedName activity, QualifiedName invalidation) {
            this.XID = XID;
            this.Box = Box;
            this.FPC = fpc;
            this.Ownership = Ownership;
            this.Transporting = Transporting;
            this.Weighing = Weighing;
            this.Packing = packing;
            this.PickUp = PickUp;
            this.DropOff = DropOff;
            this.London = London;
            this.Brighton = Brighton;
            this.weight = weight;
            this.foaf_name = foaf_name;
            this.driver = driver;
            this.scientist = scientist;
            this.scale = scale;
            this.Instrument = instrument;
            this.specialization = specialization;
            this.derivation = derivation;
            this.entity = entity;
            this.association = association;
            this.generation = generation;
            this.activity = activity;
            this.invalidation = invalidation;
        }
    }

}

