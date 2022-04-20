package ru.gaidamaka.converter;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public final class XMLCalendarMapper {

    private XMLCalendarMapper(){}

    public static XMLGregorianCalendar fromLocalDateTime(LocalDateTime localDateTime){
        try {
            XMLGregorianCalendar calendar = DatatypeFactory.newInstance().newXMLGregorianCalendar();
            calendar.setYear(localDateTime.getYear());
            calendar.setMonth(localDateTime.getMonthValue());
            calendar.setDay(localDateTime.getDayOfMonth());
            calendar.setHour(localDateTime.getHour());
            calendar.setMinute(localDateTime.getMinute());
            calendar.setSecond(localDateTime.getSecond());
            calendar.setFractionalSecond(new BigDecimal("0." + localDateTime.getNano()));
            return calendar;
        } catch (DatatypeConfigurationException e) {
            throw new IllegalStateException("DatatypeFactory exception", e);
        }
    }

}
