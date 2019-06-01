package com.orion.stocks.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javafx.util.StringConverter;

public class DateUtil {

    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
    public static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DATETIME_FORMAT);

    public static StringConverter<LocalDate> dateConverter = new StringConverter<LocalDate>() {
        @Override
        public String toString(LocalDate localDate) {
            if(localDate == null) {
                return "";
            }
            return dateFormatter.format(localDate);
        }

        @Override
        public LocalDate fromString(String dateString) {
            if(dateString == null || dateString.trim().isEmpty()) {
                return null;
            }
            return LocalDate.parse(dateString, dateFormatter);
        }
    };

}
