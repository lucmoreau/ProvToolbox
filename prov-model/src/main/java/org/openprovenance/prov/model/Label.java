package org.openprovenance.prov.model;


/**
 * Interface for PROV attribute label.
 * This interface is not directly used in the data model (since labels are directly expressed as {@link LangString} (see {@link HasLabel})). However,
 * it is introduced for generic conversion of all attributes. 
 * @author lavm
 *
 */

public interface Label extends TypedValue {

}
