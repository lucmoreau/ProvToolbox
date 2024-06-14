package org.openprovenance.prov.bookptm;

import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.Name;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.model.QualifiedName;
import org.openprovenance.prov.model.builder.Builder;
import org.openprovenance.prov.model.builder.Prefix;

import java.io.IOException;

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
        attributionDescripton(builder, defs, true);
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

    public Document makeDerivation() {
        Builder builder = new Builder(pFactory, pFactory, pFactory);
        return makeDerivation(builder);
    }

    public Document makeDerivation(Builder builder) {

        Definitions defs = getDefinitions(builder);

        transportingDescription(builder, defs);

        attributionDescripton(builder, defs, false);

        weighingDescription(builder, defs, false);

        transportingAttribution(builder, defs, false);

        specializationDescription(builder, defs, false);

        Document doc = builder.build();
        return doc;
    }

    private void attributionDescripton(Builder builder, Definitions defs, boolean standalone) {

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
                .id(defs.XID, "b347").aka()
                .type(defs.Box)
                .label("a weighted box")
                .attr(defs.weight, "8kg")
                .build();

        builder.wasGeneratedBy()
                .entity("b347")
                .activity("a7588")
                .build();

        builder.used()
                .activity("a7588")
                .entity("b34/6")
                .build();

        builder.wasDerivedFrom()
                .generatedEntity("b347")
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



    public Definitions getDefinitions(Builder builder) {
        Prefix VOCAB  = builder.prefix("vocab");
        Prefix XID    = builder.prefix("xid");
        Prefix FOAF    = builder.prefix("foaf");
        builder.prefix(VOCAB, EX_NS_URI);
        builder.prefix(XID, EX_IDS_URI);
        builder.prefix(FOAF, FOAF_URI);

        QualifiedName Box          = builder.qn(VOCAB, "Box");
        QualifiedName Transporting = builder.qn(VOCAB, "Transporting");
        QualifiedName Weighing     = builder.qn(VOCAB, "Weighing");
        QualifiedName PickUp       = builder.qn(VOCAB, "PickUp");
        QualifiedName DropOff      = builder.qn(VOCAB, "DropOff");
        QualifiedName London       = builder.qn(VOCAB, "London");
        QualifiedName Brighton     = builder.qn(VOCAB, "Brighton");
        QualifiedName weight       = builder.qn(VOCAB, "weight");
        QualifiedName driver       = builder.qn(VOCAB, "driver");
        QualifiedName scientist    = builder.qn(VOCAB, "scientist");
        QualifiedName scale        = builder.qn(VOCAB, "scale");
        QualifiedName Instrument   = builder.qn(VOCAB, "Instrument");

        QualifiedName foaf_name    = builder.qn(FOAF, "name");
        Definitions result = new Definitions(XID, Box, Transporting, Weighing, PickUp, DropOff, London, Brighton, weight, foaf_name, driver, scientist, scale, Instrument);
        return result;
    }

    public static class Definitions {
        public final Prefix XID;
        public final QualifiedName Box;
        public final QualifiedName Transporting;
        public final QualifiedName Weighing;
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

        public Definitions(Prefix XID, QualifiedName Box, QualifiedName Transporting, QualifiedName Weighing, QualifiedName PickUp, QualifiedName DropOff, QualifiedName London, QualifiedName Brighton, QualifiedName weight, QualifiedName foaf_name, QualifiedName driver, QualifiedName scientist, QualifiedName scale, QualifiedName instrument) {
            this.XID = XID;
            this.Box = Box;
            this.Transporting = Transporting;
            this.Weighing = Weighing;
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
        }
    }

}

