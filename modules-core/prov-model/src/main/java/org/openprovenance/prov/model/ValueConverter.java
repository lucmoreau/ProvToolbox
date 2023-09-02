package org.openprovenance.prov.model;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;

import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;

import org.apache.commons.io.IOUtils;
import org.openprovenance.prov.model.exception.ConverterException;
import org.xml.sax.SAXException;

/**
 * Conversion from String to Object and vice-versa for common xsd types.
 * 
 * @author lavm
 *
 */

public class ValueConverter {

    final private LiteralConstructor pFactory;
    final private Name name;

    public ValueConverter(ProvFactory pFactory) {
        this.pFactory = pFactory;
        this.name = pFactory.getName();
    }

    // should be implemented with a hash table of converters

    /**
     * Converts a string into a Java object, according to type provided. This
     * function does not convert to QualifiedName since this requires access to
     * a prefix-namespace mapping.
     * 
     * @param datatype
     *            any xsd datatype, provided it is not xsd:QName
     * @param value
     *            is a String
     * @return an object
     */

    public Object convertToJava(QualifiedName datatype, String value) {
        if (datatype.equals(name.XSD_STRING))
            return value;
        if (datatype.equals(name.XSD_INT))
            return Integer.parseInt(value);
        if (datatype.equals(name.XSD_LONG))
            return Long.parseLong(value);
        if (datatype.equals(name.XSD_SHORT))
            return Short.parseShort(value);
        if (datatype.equals(name.XSD_DOUBLE))
            return Double.parseDouble(value);
        if (datatype.equals(name.XSD_FLOAT))
            return Float.parseFloat(value);
        if (datatype.equals(name.XSD_DECIMAL))
            return new java.math.BigDecimal(value);
        if (datatype.equals(name.XSD_BOOLEAN))
            return Boolean.parseBoolean(value);
        if (datatype.equals(name.XSD_BYTE))
            return Byte.parseByte(value);
        if (datatype.equals(name.XSD_UNSIGNED_INT))
            return Long.parseLong(value);
        if (datatype.equals(name.XSD_UNSIGNED_SHORT))
            return Integer.parseInt(value);
        if (datatype.equals(name.XSD_UNSIGNED_BYTE))
            return Short.parseShort(value);
        if (datatype.equals(name.XSD_UNSIGNED_LONG))
            return new java.math.BigInteger(value);
        if (datatype.equals(name.XSD_INTEGER))
            return new java.math.BigInteger(value);
        if (datatype.equals(name.XSD_NON_NEGATIVE_INTEGER))
            return new java.math.BigInteger(value);
        if (datatype.equals(name.XSD_NON_POSITIVE_INTEGER))
            return new java.math.BigInteger(value);
        if (datatype.equals(name.XSD_POSITIVE_INTEGER))
            return new java.math.BigInteger(value);
        if (datatype.equals(name.XSD_ANY_URI)) {
            return value;
        }
        if (datatype.equals(name.PROV_QUALIFIED_NAME)) {
            throw new ConverterException("Not conversion to xsd:QName");
        }
        if (datatype.equals(name.XSD_DATETIME)) {
            return pFactory.newISOTime(value);
        }
        if (datatype.equals(name.XSD_GYEAR)) {
            return pFactory.newGYear(new Integer(value));
        }

        if (datatype.equals(name.XSD_GMONTH)) {
            // format is --02
            return pFactory.newGMonth(new Integer(value.substring(2)));
        }
        if (datatype.equals(name.XSD_GMONTH_DAY)) {
            // format is --12-25
            return pFactory.newGMonthDay(new Integer(value.substring(2, 4)),
                                         new Integer(value.substring(5, 7)));
        }

        if (datatype.equals(name.XSD_GDAY)) {
            // format is ---30
            return pFactory.newGDay(new Integer(value.substring(3)));
        }

        if (datatype.equals(name.XSD_DURATION)) {
            return pFactory.newDuration(value);
        }

        if (datatype.equals(name.XSD_DAY_TIME_DURATION)) {
            return pFactory.newDuration(value);
        }

        if (datatype.equals(name.XSD_YEAR_MONTH_DURATION)) {
            return pFactory.newDuration(value);
        }

        if (datatype.equals(name.XSD_LANGUAGE)) {
            return value;
        }

        if (datatype.equals(name.XSD_TOKEN)) {
            return value;
        }
        if (datatype.equals(name.XSD_NMTOKEN)) {
            return value;
        }

        if (datatype.equals(name.XSD_NAME)) {
            return value;
        }

        if (datatype.equals(name.XSD_NCNAME)) {
            return value;
        }

        if (datatype.equals(name.XSD_NORMALIZED_STRING)) {
            return value;
        }

        if (datatype.equals(name.XSD_HEX_BINARY)) {
            return pFactory.hexDecoding(value);
        }

        if (datatype.equals(name.XSD_BASE64_BINARY)) {
            return pFactory.base64Decoding(value);
        }

        if (datatype.equals(name.RDF_LITERAL)) {
            System.out.println("WARNING: conversion from rdf:Literal to java object is not supported" + value);
            return convertXMLLiteral(value);
        }

        throw new UnsupportedOperationException("UNKNOWN literal type " + datatype);
    }

    public org.w3c.dom.Element convertXMLLiteral(String value) {
            DOMProcessing dp=new DOMProcessing((ProvFactory)pFactory);
            DocumentBuilder db= DOMProcessing.builder;
            InputStream in;
            org.w3c.dom.Document doc=null;
            
            try {
                in = IOUtils.toInputStream(value, "UTF-8");
                doc=db.parse(in);

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (SAXException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            if (doc==null) return null;
            return doc.getDocumentElement();
    }

    public QualifiedName getXsdType(Object o) {
        if (o instanceof Integer)
            return name.XSD_INT;
        if (o instanceof String)
            return name.XSD_STRING;
        if (o instanceof LangString)
            return name.XSD_STRING;
        if (o instanceof BigInteger)
            return name.XSD_INTEGER;
        if (o instanceof Long)
            return name.XSD_LONG;
        if (o instanceof Short)
            return name.XSD_SHORT;
        if (o instanceof Double)
            return name.XSD_DOUBLE;
        if (o instanceof Float)
            return name.XSD_FLOAT;
        if (o instanceof java.math.BigDecimal)
            return name.XSD_DECIMAL;
        if (o instanceof Boolean)
            return name.XSD_BOOLEAN;
        if (o instanceof Byte)
            return name.XSD_BYTE;
        if (o instanceof QualifiedName)
            return name.PROV_QUALIFIED_NAME;
        if (o instanceof XMLGregorianCalendar) {
            XMLGregorianCalendar cal = (XMLGregorianCalendar) o;
            QName t = cal.getXMLSchemaType();
            if (t.getLocalPart().equals(name.XSD_GYEAR.getLocalPart()))
                return name.XSD_GYEAR;
            if (t.getLocalPart().equals(name.XSD_DATETIME.getLocalPart()))
                return name.XSD_DATETIME;
            // TODO: need to support all time related xsd types
            // default, return xsd:datetime
            return name.XSD_DATETIME;
        }
        // issue #54 flagged a concern: value can be an element, when xsi:type
        // was unspecified.
        // this is no longer the case
        // System.out.println("getXsdType() " + o.getClass());

        // Let's be permissive, and return the unknown qualified name
        return name.QUALIFIED_NAME_UNKNOWN_TYPE;
    }

}
