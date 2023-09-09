package org.openprovenance.prov.validation;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.Duration;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;

public class VarTime extends XMLGregorianCalendar implements
        ExistentialVariable {

    @Override
    public void add(Duration duration) {
        // TODO Auto-generated method stub

    }

    @Override
    public void clear() {
        // TODO Auto-generated method stub

    }

    @Override
    public Object clone() {
        return null;
    }

    @Override
    public int compare(XMLGregorianCalendar xmlGregorianCalendar) {
        return DatatypeConstants.INDETERMINATE;
    }

    @Override
    public int getDay() {
        return DatatypeConstants.FIELD_UNDEFINED;
    }

    @Override
    public BigInteger getEon() {
        return null;
    }

    @Override
    public BigInteger getEonAndYear() {
        return null;
    }

    @Override
    public BigDecimal getFractionalSecond() {
        return null;
    }

    @Override
    public int getHour() {
        return DatatypeConstants.FIELD_UNDEFINED;

    }

    @Override
    public int getMinute() {
        // return DatatypeConstants.FIELD_UNDEFINED;
        return 11;
    }

    @Override
    public int getMonth() {
        return DatatypeConstants.FIELD_UNDEFINED;
    }

    @Override
    public int getSecond() {
        return DatatypeConstants.FIELD_UNDEFINED;
    }

    @Override
    public TimeZone getTimeZone(int defaultZoneoffset) {
        return null;
    }

    @Override
    public int getTimezone() {
        return DatatypeConstants.FIELD_UNDEFINED;

    }

    @Override
    public QName getXMLSchemaType() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int getYear() {
        return DatatypeConstants.FIELD_UNDEFINED;
    }

    @Override
    public boolean isValid() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public XMLGregorianCalendar normalize() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void reset() {
        // TODO Auto-generated method stub

    }

    @Override
    public void setDay(int day) {
        // TODO Auto-generated method stub
    }

    @Override
    public void setFractionalSecond(BigDecimal fractional) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setHour(int hour) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setMillisecond(int millisecond) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setMinute(int minute) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setMonth(int month) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setSecond(int second) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setTimezone(int offset) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setYear(BigInteger year) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setYear(int year) {
        // TODO Auto-generated method stub

    }

    @Override
    public GregorianCalendar toGregorianCalendar() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public GregorianCalendar toGregorianCalendar(TimeZone timezone,
                                                 Locale aLocale,
                                                 XMLGregorianCalendar defaults) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String toXMLFormat() {
        // TODO Auto-generated method stub
        return null;
    }

    public VarTime(String uri) {
        this.uri = uri;
    }

    final private String uri;


    public String getUri() {
        return uri;
    }

    @Override
    public String toString() {
        return uri;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof VarTime) {
            VarTime other = (VarTime) o;
            return this.getUri().equals(other.getUri());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return uri.hashCode();
    }

}
