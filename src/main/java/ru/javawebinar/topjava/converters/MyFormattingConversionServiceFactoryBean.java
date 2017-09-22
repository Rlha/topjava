package ru.javawebinar.topjava.converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.format.support.FormattingConversionServiceFactoryBean;
import org.springframework.stereotype.Component;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by Администратор on 22.09.2017.
 */
@Component
public class MyFormattingConversionServiceFactoryBean extends FormattingConversionServiceFactoryBean {

    @Autowired
    LocalDateConverter localDateConverter;
    @Autowired
    LocalTimeConverter localTimeConverter;

    @Override
    public void setConverters(Set<?> converters) {
        super.setConverters(converters);
    }

    @Override
    public void afterPropertiesSet() {
        super.afterPropertiesSet();
        FormattingConversionService conversionService = getObject();
        conversionService.addConverter(localDateConverter);
        conversionService.addConverter(localTimeConverter);
    }
}