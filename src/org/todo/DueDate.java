package org.todo;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

public class DueDate {
    protected XMLGregorianCalendar xmlGregorianCalendar;
    protected Date date;
    protected DateFormat  dateFormat = new SimpleDateFormat("yyyy-MM-dd");


    public DueDate(XMLGregorianCalendar xmlGregorianCalendar) {
         if(xmlGregorianCalendar == null)
        {
            xmlGregorianCalendar = null;
            date = null;
        }
        else
        {
            xmlGregorianCalendar = xmlGregorianCalendar;
            date = xmlGregorianCalendar.toGregorianCalendar().getTime();
        }
    }

    public void setDate(Date date)
    {
        GregorianCalendar gCalendar = new GregorianCalendar();
        gCalendar.setTime(date);
        xmlGregorianCalendar = null;
        try {
            xmlGregorianCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(gCalendar);
        } catch (DatatypeConfigurationException ex) {
            System.out.println("Unable to set due Gregorian due date");
        }
    }

    public void setDateByString(String dateString) throws ParseException {
        date = dateFormat.parse(dateString);
        setDate(date);
    }

    public XMLGregorianCalendar getXmlGregorianCalendar()
    {
        return xmlGregorianCalendar;
    }

    @Override
    public String toString() {
        if(date != null)
        {
            return dateFormat.format(date);
        }
        return "NONE";
    }
}
