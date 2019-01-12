package org.todo;

import javax.swing.text.DateFormatter;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DueDate {
    protected XMLGregorianCalendar xmlGregorianCalendar;
    protected LocalDate date;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");


    public DueDate(XMLGregorianCalendar xmlGregorianCalendar) {
         if(xmlGregorianCalendar == null)
        {
            xmlGregorianCalendar = null;
            date = null;
        }
        else
        {
            xmlGregorianCalendar = xmlGregorianCalendar;
            date = xmlGregorianCalendar.toGregorianCalendar().toZonedDateTime().toLocalDate();
        }
    }

    public void setDate(LocalDate date)
    {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(new Date());
        try {
            xmlGregorianCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendarDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH), DatatypeConstants.FIELD_UNDEFINED);
        }
        catch (Exception e){
            System.err.println("setDate(LocalDate date)");
        }
    }

    public void setDateByString(String dateString) {
        if(!dateString.isEmpty()) {
            date = LocalDate.parse(dateString, formatter);
            setDate(date);
        }
    }

    public XMLGregorianCalendar getXmlGregorianCalendar()
    {
        return xmlGregorianCalendar;
    }

    public LocalDate getLocalDate()
    {
        return date;
    }

    @Override
    public String toString() {
        if(date != null)
        {
            return formatter.format(date);
        }
        return "NONE";
    }
}
