package org.openprovenance.prov.validation;

import java.util.Map;
import java.util.Hashtable;

import org.openprovenance.prov.model.ProvFactory;

public class Config {
	public static final String DEFINITION_OPTIONAL_IDENTIFIERS = "optional_identifiers";
	public static final String DEFINITION_OPTIONAL_ATTRIBUTES = "optional_attributes";
	public static final String DEFINITION_SHORT_FORMS = "definition_short_forms";
	public static final String DEFINITION_OPTIONAL_PLACEHOLDERS = "optional_placeholders";
	public static final String INFERENCE_COMMUNICATION_GENERATION_USE_INFERENCE = "communication_generation_use_inference";
	public static final String INFERENCE_GENERATION_USE_COMMUNICATION_INFERENCE = "generation_use_communication_inference";
	public static final String INFERENCE_ENTITY_GENERATION_INVALIDATION_INFERENCE = "entity_generation_invalidation_inference";
	public static final String INFERENCE_ACTIVITY_START_END_INFERENCE = "activity_start_end_inference";
	public static final String INFERENCE_WASSTARTEDBY_INFERENCE = "wasStartedBy_inference";
	public static final String INFERENCE_WASENDEDBY_INFERENCE = "wasEndedBy_inference";
	public static final String INFERENCE_DERIVATION_GENERATION_USE = "derivation_generation_use";
	public static final String INFERENCE_DERIVATION_USE = "derivation_use";
	public static final String INFERENCE_SPECIFIC_DERIVATION_INFERENCE = "specific_derivation_inference";
	public static final String INFERENCE_REVISION_IS_ALTERNATE = "revision_is_alternate";
	public static final String INFERENCE_QUOTATION_IMPLICATION = "quotation_implication";
	public static final String INFERENCE_ATTRIBUTION_INFERENCE = "attribution_inference";
	public static final String INFERENCE_DELEGATION_INFERENCE = " delegation_inference";
	public static final String INFERENCE_ALTERNATE_REFLEXIVE = "alternate_reflexive";
	public static final String INFERENCE_ALTERNATE_TRANSITIVE = "alternate_transitive";
	public static final String INFERENCE_ALTERNATE_SYMMETRIC = "alternate_symmetric";
	public static final String INFERENCE_SPECIALIZATION_TRANSITIVE = "specialization_transitive";
	public static final String INFERENCE_SPECIALIZATION_ALTERNATE = "specialization_alternate";
	public static final String INFERENCE_MENTION_SPECIALIZATION = "mention_specialization";
	public static final String CONSTRAINT_KEY_PROPERTIES = "key_properties";
	public static final String CONSTRAINT_UNIQUE_GENERATION = "unique_generation";
	public static final String CONSTRAINT_UNIQUE_INVALIDATION = "unique_invalidation";
	public static final String CONSTRAINT_UNIQUE_WASSTARTEDBY = "unique_wasStartedBy";
	public static final String CONSTRAINT_UNIQUE_WASENDEDBY = "unique_wasEndedBy";
	public static final String CONSTRAINT_UNIQUE_STARTTIME = "unique_startTime";
	public static final String CONSTRAINT_UNIQUE_ENDTIME = "unique_endTime";
	public static final String CONSTRAINT_UNIQUE_MENTION = "mention_unique";
	public static final String CONSTRAINT_START_PRECEDES_END = "start_precedes_end"; // done
	public static final String CONSTRAINT_USAGE_WITHIN_ACTIVITY = "usage_within_activity"; // done
	public static final String CONSTRAINT_GENERATION_WITHIN_ACTIVITY = "generation_within_activity"; // done
	public static final String CONSTRAINT_WASINFORMEDBY_ORDERING = "wasInformedBy_ordering"; // done
	public static final String CONSTRAINT_GENERATION_PRECEDES_INVALIDATION = "generation_precedes_invalidation"; // done
	public static final String CONSTRAINT_GENERATION_PRECEDES_USAGE = "generation_precedes_usage"; // done
	public static final String CONSTRAINT_USAGE_PRECEDES_INVALIDATION = "usage_precedes_invalidation"; // done
	public static final String CONSTRAINT_DERIVATION_USAGE_GENERATION_ORDERING = "derivation_usage_generation_ordering"; // done
	public static final String CONSTRAINT_DERIVATION_GENERATION_GENERATION_ORDERING = "derivation_generation_generation_ordering"; // done
	public static final String CONSTRAINT_WASSTARTEDBY_ORDERING = "wasStartedBy_ordering"; // done
	public static final String CONSTRAINT_WASENDEDBY_ORDERING = "wasEndedBy_ordering"; // done
	public static final String CONSTRAINT_WASASSOCIATEDWITH_ORDERING = "wasAssociatedWith_ordering"; // done
	public static final String CONSTRAINT_WASATTRIBUTEDTO_ORDERING = "wasAttributedTo_ordering"; // done
	public static final String CONSTRAINT_ACTEDONBEHALFOF_ORDERING = "actedOnBehalfOf_ordering"; // done
	public static final String CONSTRAINT_IMPOSSIBLE_INFLUENCE_REFLEXIVE = "impossible_influence_reflexive";
	public static final String CONSTRAINT_IMPOSSIBLE_SPECIALIZATION_REFLEXIVE = "impossible_specialization_reflexive";
	public static final String CONSTRAINT_IMPOSSIBLE_PROPERTY_OVERLAP = "impossible_property_overlap";
	public static final String CONSTRAINT_IMPOSSIBLE_OBJECT_PROPERTY_OVERLAP = "impossible_object_property_overlap";
	public static final String CONSTRAINT_TYPING = "typing";
	public static final String CONSTRAINT_ENTITY_ACTIVITY_DISJOINT = "entity_activity_disjoint";
	public static final String CONSTRAINT_MEMBERSHIP_EMPTY_COLLECTION = "membership_empty_collection";
	public static final String INFERENCE_INFLUENCE_INFERENCE = "influence_inference";
	public static final String INFERENCE_SPECIALIZATION_ATTRIBUTES_INFERENCE = "specialization_attributes_inference";
	public static final String CONSTRAINT_KEY_OBJECT = "key_object";
	public static final String CONSTRAINT_START_START_ORDERING = "start_start_ordering";
	public static final String CONSTRAINT_END_END_ORDERING = "end_end_ordering";
	public static final String CONSTRAINT_GENERATION_GENERATION_ORDERING = "generation_generation_ordering";
	public static final String CONSTRAINT_INVALIDATION_INVALIDATION_ORDERING = "invalidation_invalidation_ordering";
	public static final String CONSTRAINT_SPECIALIZATION_GENERATION_ORDERING = "specialization_generation_ordering";
	public static final String CONSTRAINT_SPECIALIZATION_INVALIDATION_ORDERING = "specialization_invalidation_ordering";
	public static final String CONSTRAINT_IMPOSSIBLE_UNSPECIFIED_DERIVATION_GENERATION_USE = "impossible_unspecified_derivation_generation_used";

	public final Map<String, Object> config;

	final ProvFactory p;

	final ObjectMaker om;

	public Config(Map<String, Object> config, ProvFactory p, ObjectMaker om) {
		this.config = config;
		this.om=om;
		this.p=p;
	}

	public boolean isTrue(String s) {
		Object o = config.get(s);
		if (o == null) return false;
		return (s.equals(o));
	}

	static public Map<String, Object> implementedTable = new Hashtable<>();

	static void registerRule(int num, Map<String, Object> table, String rule) {
		registerRule(num, table, rule, true);
	}

	static void registerRule(int num, Map<String, Object> table, String rule, boolean implemented) {
		table.put(rule, rule);
		if (implemented) implementedTable.put(rule, rule);
	}

	static public Config newYesToAllConfig(ProvFactory p, ObjectMaker om) {
		Map<String, Object> result = new Hashtable<>();

		registerRule(1, result, DEFINITION_OPTIONAL_IDENTIFIERS, false);
		registerRule(2, result, DEFINITION_OPTIONAL_ATTRIBUTES, false);
		registerRule(3, result, DEFINITION_SHORT_FORMS, false);
		registerRule(4, result, DEFINITION_OPTIONAL_PLACEHOLDERS, false);
		registerRule(5, result, INFERENCE_COMMUNICATION_GENERATION_USE_INFERENCE, false);
		registerRule(6, result, INFERENCE_GENERATION_USE_COMMUNICATION_INFERENCE, false);
		registerRule(7, result, INFERENCE_ENTITY_GENERATION_INVALIDATION_INFERENCE, false);
		registerRule(8, result, INFERENCE_ACTIVITY_START_END_INFERENCE, false);
		registerRule(9, result, INFERENCE_WASSTARTEDBY_INFERENCE, false);
		registerRule(10, result, INFERENCE_WASENDEDBY_INFERENCE, false);
		registerRule(11, result, INFERENCE_DERIVATION_GENERATION_USE, false);
		registerRule(12, result, INFERENCE_REVISION_IS_ALTERNATE, false);
		registerRule(13, result, INFERENCE_ATTRIBUTION_INFERENCE, false);
		registerRule(14, result, INFERENCE_DELEGATION_INFERENCE, false);
		registerRule(15, result, INFERENCE_INFLUENCE_INFERENCE, false);
		registerRule(16, result, INFERENCE_ALTERNATE_REFLEXIVE);
		registerRule(17, result, INFERENCE_ALTERNATE_TRANSITIVE);
		registerRule(18, result, INFERENCE_ALTERNATE_SYMMETRIC);
		registerRule(19, result, INFERENCE_SPECIALIZATION_TRANSITIVE);
		registerRule(20, result, INFERENCE_SPECIALIZATION_ALTERNATE);
		registerRule(21, result, INFERENCE_SPECIALIZATION_ATTRIBUTES_INFERENCE);
		registerRule(22, result, INFERENCE_MENTION_SPECIALIZATION, false);

		registerRule(23, result, CONSTRAINT_KEY_OBJECT, false);
		registerRule(24, result, CONSTRAINT_KEY_PROPERTIES, false);
		registerRule(25, result, CONSTRAINT_UNIQUE_GENERATION, false);
		registerRule(26, result, CONSTRAINT_UNIQUE_INVALIDATION, false);
		registerRule(27, result, CONSTRAINT_UNIQUE_WASSTARTEDBY, false);
		registerRule(28, result, CONSTRAINT_UNIQUE_WASENDEDBY, false);
		registerRule(29, result, CONSTRAINT_UNIQUE_STARTTIME, false);
		registerRule(30, result, CONSTRAINT_UNIQUE_ENDTIME, false);
		registerRule(31, result, CONSTRAINT_UNIQUE_MENTION, false);

		registerRule(32, result, CONSTRAINT_START_PRECEDES_END);
		registerRule(33, result, CONSTRAINT_START_START_ORDERING);
		registerRule(34, result, CONSTRAINT_END_END_ORDERING);
		registerRule(35, result, CONSTRAINT_USAGE_WITHIN_ACTIVITY);
		registerRule(36, result, CONSTRAINT_GENERATION_WITHIN_ACTIVITY);
		registerRule(37, result, CONSTRAINT_WASINFORMEDBY_ORDERING, false);
		registerRule(38, result, CONSTRAINT_GENERATION_PRECEDES_INVALIDATION);
		registerRule(39, result, CONSTRAINT_GENERATION_PRECEDES_USAGE);
		registerRule(40, result, CONSTRAINT_USAGE_PRECEDES_INVALIDATION);
		registerRule(41, result, CONSTRAINT_GENERATION_GENERATION_ORDERING);
		registerRule(42, result, CONSTRAINT_INVALIDATION_INVALIDATION_ORDERING);
		registerRule(43, result, CONSTRAINT_DERIVATION_USAGE_GENERATION_ORDERING);
		registerRule(44, result, CONSTRAINT_DERIVATION_GENERATION_GENERATION_ORDERING);
		registerRule(45, result, CONSTRAINT_WASSTARTEDBY_ORDERING);
		registerRule(46, result, CONSTRAINT_WASENDEDBY_ORDERING);
		registerRule(47, result, CONSTRAINT_SPECIALIZATION_GENERATION_ORDERING);
		registerRule(48, result, CONSTRAINT_SPECIALIZATION_INVALIDATION_ORDERING);

		registerRule(49, result, CONSTRAINT_WASASSOCIATEDWITH_ORDERING);
		registerRule(50, result, CONSTRAINT_WASATTRIBUTEDTO_ORDERING);
		registerRule(51, result, CONSTRAINT_ACTEDONBEHALFOF_ORDERING);

		registerRule(52, result, CONSTRAINT_TYPING);
		registerRule(53, result, CONSTRAINT_IMPOSSIBLE_UNSPECIFIED_DERIVATION_GENERATION_USE,false);
		registerRule(54, result, CONSTRAINT_IMPOSSIBLE_SPECIALIZATION_REFLEXIVE);
		registerRule(55, result, CONSTRAINT_IMPOSSIBLE_PROPERTY_OVERLAP, false);
		registerRule(56, result, CONSTRAINT_IMPOSSIBLE_OBJECT_PROPERTY_OVERLAP,false);
		registerRule(57, result, CONSTRAINT_ENTITY_ACTIVITY_DISJOINT);
		registerRule(58, result, CONSTRAINT_MEMBERSHIP_EMPTY_COLLECTION, false);

		return new Config(result, p,om);
	}

}
