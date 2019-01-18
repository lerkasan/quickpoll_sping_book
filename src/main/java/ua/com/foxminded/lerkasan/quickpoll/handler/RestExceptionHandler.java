package ua.com.foxminded.lerkasan.quickpoll.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
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

import static org.springframework.http.HttpStatus.*;

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
        String messageTitleId = String.valueOf(BAD_REQUEST.value()) + TITLE;
        String message = messageSource.getMessage(messageTitleId, null, DEFAULT_LOCALE);
        errorDetail.setTitle(BAD_REQUEST.name() + " - " + message);
        return handleExceptionInternal(ex, errorDetail, headers, status, request);
    }

    @ResponseStatus(UNPROCESSABLE_ENTITY)
//    @ExceptionHandler(value = {JsonParseException.class, JsonMappingException.class, HttpMessageNotReadableException.class})  // !!! THIS WILL CAUSE AMBIGUITY WITH handleMethodArgumentNotValid method in ResponseEntityExceptionHandler class
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ErrorDetails errorDetail = fillInErrorDetails(ex, status);
        return handleExceptionInternal(ex, errorDetail, headers, status, request);
    }

    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(ResourceNotFoundException.class)
    protected ResponseEntity<?> handleResourceNotFound(
            ResourceNotFoundException ex) {
        ErrorDetails errorDetail = fillInErrorDetails(ex, NOT_FOUND);
        String messageTitleId = String.valueOf(NOT_FOUND.value()) + TITLE;
        String message = messageSource.getMessage(messageTitleId, null, DEFAULT_LOCALE);
        errorDetail.setTitle(NOT_FOUND.name() + " - " + message);
        return new ResponseEntity<>(errorDetail, NOT_FOUND);
    }

    private ErrorDetails fillInErrorDetails(Exception ex, HttpStatus status) {
        ErrorDetails errorDetail = new ErrorDetails();
        errorDetail.setStatus(status.value());
        errorDetail.setDetail(ex.getMessage());
        errorDetail.setExceptionName(ex.getClass().getCanonicalName());
        errorDetail.setTimestamp(LocalDateTime.now());
        return errorDetail;
    }
}
