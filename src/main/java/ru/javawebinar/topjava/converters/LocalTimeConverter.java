package ru.javawebinar.topjava.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by Администратор on 22.09.2017.
 */
@Component
public class LocalTimeConverter implements Converter<String, LocalTime> {

    private static DateTimeFormatter formatter = DateTimeFormatter.ISO_TIME;

    @Override
    public LocalTime convert(String localDateString) {
        try {
            return LocalTime.parse(localDateString, formatter);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
