package org.openprovenance.prov.model;



/**
 * Legacy interface
 * 
 */

public interface DictionaryFactory {


    DerivedByInsertionFrom createDerivedByInsertionFrom();

    DerivedByRemovalFrom createDerivedByRemovalFrom();

    DictionaryMembership createDictionaryMembership();


    Entry createEntry();



   
}
