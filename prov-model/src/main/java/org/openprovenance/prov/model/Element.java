package org.openprovenance.prov.model;

/** 
 * <p>Interface for an Entity, an Activity, or an Agent.
 * <p>An element can have a type ({@link HasType}), can have a label ({@link HasLabel}), 
 * can have a location ({@link HasLocation}), is {@link Identifiable}, and is one of 
 * the assertions supported by PROV ({@link Statement}).
 * 
 *  @author lavm
 */ 
public interface Element extends HasType, HasLabel, HasLocation, Identifiable,  Statement {
    
} 
