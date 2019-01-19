package ua.com.foxminded.lerkasan.quickpoll.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ua.com.foxminded.lerkasan.quickpoll.dto.error.ErrorDetails;
import ua.com.foxminded.lerkasan.quickpoll.dto.error.ValidationError;
import ua.com.foxminded.lerkasan.quickpoll.exception.ResourceNotFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.METHOD_NOT_ALLOWED;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    private static final String TITLE = ".title";
    private static final Locale DEFAULT_LOCALE = Locale.getDefault();

    @Autowired
    private MessageSource messageSource;

    @ResponseStatus(BAD_REQUEST)
//    @ExceptionHandler(MethodArgumentNotValidException.class) // !!! THIS WILL CAUSE AMBIGUITY WITH handleMethodArgumentNotValid method in ResponseEntityExceptionHandler class
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ErrorDetails errorDetail = fillInErrorDetails(ex, status);
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        Map<String, List<ValidationError>> validationErrors = fieldErrors.stream()
                .map(fieldError -> new ValidationError(fieldError.getField(), fieldError.getCode(), messageSource.getMessage(fieldError, DEFAULT_LOCALE)))
                .collect(Collectors.groupingBy(ValidationError::getField));
        errorDetail.setErrors(validationErrors);
        return handleExceptionInternal(ex, errorDetail, headers, status, request);
    }

    @ResponseStatus(BAD_REQUEST)
//    @ExceptionHandler(HttpMessageNotReadableException.class)  // !!! THIS WILL CAUSE AMBIGUITY WITH handleMethodArgumentNotValid method in ResponseEntityExceptionHandler class
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ErrorDetails errorDetail = fillInErrorDetails(ex, status);
        return handleExceptionInternal(ex, errorDetail, headers, status, request);
    }

    @ResponseStatus(METHOD_NOT_ALLOWED)
    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
            HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ErrorDetails errorDetail = fillInErrorDetails(ex, status);
        return handleExceptionInternal(ex, errorDetail, headers, status, request);
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(JsonProcessingException.class)
    protected ResponseEntity<Object> handleJsonProcessing(
            JsonProcessingException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ErrorDetails errorDetail = fillInErrorDetails(ex, status);
        return handleExceptionInternal(ex, errorDetail, headers, status, request);
    }

    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(ResourceNotFoundException.class)
    protected ResponseEntity<?> handleResourceNotFound(ResourceNotFoundException ex) {
        ErrorDetails errorDetail = fillInErrorDetails(ex, NOT_FOUND);;
        return new ResponseEntity<>(errorDetail, NOT_FOUND);
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    protected ResponseEntity<?> handleIllegalArgument(IllegalArgumentException ex) {
        ErrorDetails errorDetail = fillInErrorDetails(ex, BAD_REQUEST);;
        return new ResponseEntity<>(errorDetail, BAD_REQUEST);
    }

    private ErrorDetails fillInErrorDetails(Exception ex, HttpStatus status) {
        ErrorDetails errorDetail = new ErrorDetails();
        String messageTitleId = String.valueOf(status.value()) + TITLE;
        String message;
        try {
            message = status.name() + " - " + messageSource.getMessage(messageTitleId, null, DEFAULT_LOCALE);
        } catch (NoSuchMessageException e) {
            message = status.name();
        }
        errorDetail.setTitle(message);
        errorDetail.setStatus(status.value());
        errorDetail.setDetail(ex.getMessage());
        errorDetail.setExceptionName(ex.getClass().getCanonicalName());
        errorDetail.setTimestamp(LocalDateTime.now());
        return errorDetail;
    }
}
