package ru.javawebinar.topjava.util.exception;


import org.postgresql.util.PSQLException;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.validation.BindException;

import java.util.stream.Collectors;

public class ErrorInfo {
    private final String url;
    private final String cause;
    private final String detail;

    public ErrorInfo(CharSequence url, Throwable ex) {
        this.url = url.toString();
        this.cause = ex.getClass().getSimpleName();
        this.detail = ex.getLocalizedMessage();
    }

    public ErrorInfo(CharSequence url, BindException ex, MessageSource messageSource) {
        this.url = url.toString();
        this.cause = ex.getClass().getSimpleName();
        this.detail = ex.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> String.format("%s \"%s\": %s",
                        messageSource.getMessage("common.field", null, LocaleContextHolder.getLocale()),
                        messageSource.getMessage(ex.getBindingResult().getObjectName() + "." + fieldError.getField(), null, LocaleContextHolder.getLocale()),
                        fieldError.getDefaultMessage()))
                .collect(Collectors.joining("<br>"));
    }

    public ErrorInfo(CharSequence url, PSQLException ex, MessageSource messageSource) {
        this.url = url.toString();
        this.cause = ex.getClass().getSimpleName();
        this.detail = messageSource.getMessage("constraint." + ex.getServerErrorMessage().getConstraint(), null, LocaleContextHolder.getLocale());
    }
}