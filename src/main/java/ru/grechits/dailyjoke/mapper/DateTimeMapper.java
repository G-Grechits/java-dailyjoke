package ru.grechits.dailyjoke.mapper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeMapper {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static LocalDateTime toLocalDateTime(String dateTime) {
        return LocalDateTime.parse(dateTime, formatter);
    }

    public static String toTextDateTime(LocalDateTime dateTime) {
        return dateTime.format(formatter);
    }
}
