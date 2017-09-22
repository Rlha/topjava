package ru.javawebinar.topjava.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Created by Администратор on 22.09.2017.
 */
@Component
public class LocalDateConverter implements Converter<String, LocalDate> {

    private static DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE;

    @Override
    public LocalDate convert(String localDateString) {
        try {
            return LocalDate.parse(localDateString, formatter);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
