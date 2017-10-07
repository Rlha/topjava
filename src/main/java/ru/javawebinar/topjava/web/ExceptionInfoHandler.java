package ru.javawebinar.topjava.web;

import org.postgresql.util.PSQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import ru.javawebinar.topjava.util.ValidationUtil;
import ru.javawebinar.topjava.util.exception.ErrorInfo;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import javax.servlet.http.HttpServletRequest;


@ControllerAdvice(annotations = RestController.class)
@Order(Ordered.HIGHEST_PRECEDENCE + 5)
public class ExceptionInfoHandler {

    @Autowired
    MessageSource messageSource;

    private static Logger LOG = LoggerFactory.getLogger(ExceptionInfoHandler.class);

    @ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(DuplicateKeyException.class)
    @ResponseBody
    public ErrorInfo handleError(HttpServletRequest req, DuplicateKeyException e) {
        return logAndGetDuplicateKeyErrorInfo(req, e, false, messageSource);
    }

    @ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(BindException.class)
    @ResponseBody
    public ErrorInfo handleError(HttpServletRequest req, BindException e) {
        return logAndGetBindingResultErrorInfo(req, e, false, messageSource);
    }

    @ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ErrorInfo handleError(HttpServletRequest req, MethodArgumentNotValidException e) {
        return logAndGetErrorInfo(req, e, false);
    }

    //  http://stackoverflow.com/a/22358422/548473
    @ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(NotFoundException.class)
    @ResponseBody
    public ErrorInfo handleError(HttpServletRequest req, NotFoundException e) {
        return logAndGetErrorInfo(req, e, false);
    }

    @ResponseStatus(value = HttpStatus.CONFLICT)  // 409
    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseBody
    public ErrorInfo conflict(HttpServletRequest req, DataIntegrityViolationException e) {
        return logAndGetErrorInfo(req, e, true);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ErrorInfo handleError(HttpServletRequest req, Exception e) {
        return logAndGetErrorInfo(req, e, true);
    }

    private void logException(HttpServletRequest req, boolean logException, Throwable rootCause) {
        if (logException) {
            LOG.error("Exception at request " + req.getRequestURL(), rootCause);
        } else {
            LOG.warn("Exception at request " + req.getRequestURL() + ": " + rootCause.toString());
        }
    }

    private static ErrorInfo logAndGetErrorInfo(HttpServletRequest req, Exception e, boolean logException) {
        Throwable rootCause = ValidationUtil.getRootCause(e);
        return new ErrorInfo(req.getRequestURL(), rootCause);
    }

    private static ErrorInfo logAndGetBindingResultErrorInfo(HttpServletRequest req, BindException e, boolean logException, MessageSource messageSource) {
        Throwable rootCause = ValidationUtil.getRootCause(e);
        logAndGetErrorInfo(req, e, logException);
        ErrorInfo errorInfo = null;
        if (rootCause instanceof BindException) {
            errorInfo = new ErrorInfo(req.getRequestURL(), (BindException) rootCause, messageSource);
        } else {
            errorInfo = new ErrorInfo(req.getRequestURL(), rootCause);
        }
        return errorInfo;
    }

    private static ErrorInfo logAndGetDuplicateKeyErrorInfo(HttpServletRequest req, DuplicateKeyException e, boolean logException, MessageSource messageSource) {
        Throwable rootCause = ValidationUtil.getRootCause(e);
        logAndGetErrorInfo(req, e, logException);
        ErrorInfo errorInfo = null;
        if (rootCause instanceof PSQLException) {
            errorInfo = new ErrorInfo(req.getRequestURL(), (PSQLException) rootCause, messageSource);
        } else {
            errorInfo = new ErrorInfo(req.getRequestURL(), rootCause);
        }
        return errorInfo;
    }
}