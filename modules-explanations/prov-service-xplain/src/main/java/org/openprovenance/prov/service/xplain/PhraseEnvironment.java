package org.openprovenance.prov.service.xplain;

import org.openprovenance.prov.scala.nlgspec_transformer.defs;
import org.openprovenance.prov.scala.nlgspec_transformer.specTypes;
import org.openprovenance.prov.vanilla.Document;
import scala.collection.immutable.Map;


public class PhraseEnvironment {
    private specTypes.Phrase phrase;
    private  Document document;
    private defs.Dictionary dictionary;
    private Map<String,String> context;
    private int format;

    public int getFormat() {
        return format;
    }

    public void setFormat(int format) {
        this.format = format;
    }

    public Map<String, Map<String, String>> getSelect() {
        return select;
    }

    public void setSelect(Map<String, Map<String, String>> select) {
        this.select = select;
    }

    private Map<String,Map<String,String>> select;
    private Map<String, Object> profiles;
    private String theProfile;
    private String query;

    public Map<String, String> getContext() {
        return context;
    }

    public void setContext(Map<String, String> context) {
        this.context = context;
    }


    public void setDictionary(defs.Dictionary dictionary) {
        this.dictionary = dictionary;
    }

    public defs.Dictionary getDictionary() {
        return dictionary;
    }

    public PhraseEnvironment() {
    }

    public specTypes.Phrase getPhrase() {
        return phrase;
    }

    public void setPhrase(specTypes.Phrase phrase) {
        this.phrase = phrase;
    }

    public void setDocument(Document document) {
        this.document = document;
    }

    public Document getDocument() {
        return document;
    }

    public Map<String, Object> getProfiles() {
        return profiles;
    }

    public void setProfiles(Map<String, Object> profiles) {
        this.profiles = profiles;
    }

    public void setTheProfile(String theProfile) {
        this.theProfile = theProfile;
    }

    public String getTheProfile() {
        return theProfile;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }
}
