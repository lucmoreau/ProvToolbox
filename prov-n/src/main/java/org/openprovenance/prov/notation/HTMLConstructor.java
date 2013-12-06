package org.openprovenance.prov.notation;
import java.io.Writer;
import java.util.List;

import javax.xml.datatype.XMLGregorianCalendar;

import org.openprovenance.prov.model.Attribute;
import org.openprovenance.prov.model.QualifiedNameExport;
import org.openprovenance.prov.model.QualifiedName;


/* FIXME: this does not work, since methods define here do not override those of NotationConstructor. */

public class HTMLConstructor extends NotationConstructor {
    
    public HTMLConstructor(Writer writer, QualifiedNameExport qnExport) {
	super(writer, qnExport);
    }

    public String keyword(String s) {
	return "<span class='prov-keyword'>" + s + "</span>";
    }
    public String symbol(String s) {
	return "<span class='prov-symbol'>" + s + "</span>";
    }
    public String showprefix(String s) {
	return "<span class='prov-prefix " + "prov-prefix-"+ s + "'>" + s + "</span>";
    }
    public String showlocal(String s, String prefix) {
	return "<span class='prov-local " + "prov-local-"+ s + " prov-local-prefix-" + prefix + "'>" + s + "</span>";
    }

    public String breakline() {
	return "<br>\n";
    }
    public String showuri(String s) {
	String stripped=s.substring(1,s.length()-1);
	return  "&lt;" + "<a href='" + stripped + "'>" + stripped + "</a>" + "&gt;";
    }

    public String optionalTime(Object time) {
        return ((time==null)? "" : (", " + time));
    }            

    public Object optional2(Object str) {
        return ((str==null)? "" : str);
    }


    public Object optional(Object str) {
        return ((str==null)? "-" : str);
    }




    public Object convertId(String id) {
	if (id==null) return null;

        int index=id.indexOf(':');
	String prefix=id.substring(0,index);
	String local=id.substring(index+1,id.length());

        return "<span class='prov-id'>" + showprefix(prefix) + ":" + showlocal(local,prefix) + "</span>";
    }

    public Object convertAttribute(Object name, Object value) {
        return "<span class='prov-attribute "+ name.toString().replace(':','-') + "'>" + name + "</span>" + symbol("=") + value;
    }

    public Object convertTypedLiteral(String datatype, Object value) {
        if ("xsd:QName".equals(datatype)) {
            String val=(String)value;
            return "'" + convertId(val.substring(1, val.length() -1 )) + "'";
        } else {
	    if ("prov:Qualified_Name".equals(datatype)) {
		String val=(String)value;
		return "'" + val.substring(1, val.length() -1 ) + "'";
	    } else {
		return  " <span class='prov-datatype'>" + value + "</span> " + symbol("%%") + " <span class='prov-datatype'>" + datatype + "</span>";
	    }
	}
    }



    public Object convertStart(String start) {
        return start;
    }
    public Object convertEnd(String end) {
        return end;
    }
    public Object convertString(String s) {
        return  "<span class='prov-string'>" + s + "</span>";
    }

    public Object convertInt(int i) {
        return i;
    }

    public String optionalId(Object id) {
        return ((id==null)? "" : (id + ";"));
    }            



    public Object convertQualifiedName(String qname) {
        return qname;
    }
    public Object convertIRI(String iri) {
        return iri;
    }

    public  Object newActivity(QualifiedName id, XMLGregorianCalendar startTime,XMLGregorianCalendar endTime, List<Attribute> aAttrs) {
	
        return "<span class='prov-expression'> " + super.newActivity(id,startTime,endTime,aAttrs) + "</span>";
    }

    public Object newEntity(QualifiedName id, List<Attribute> attrs) {
        return "<span class='prov-expression'> " + super.newEntity(id,attrs) + "</span>";
    }



}
