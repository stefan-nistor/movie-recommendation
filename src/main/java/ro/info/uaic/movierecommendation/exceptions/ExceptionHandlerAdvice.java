package ro.info.uaic.movierecommendation.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ro.info.uaic.movierecommendation.util.JsonUtil;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
public class ExceptionHandlerAdvice extends ResponseEntityExceptionHandler {
    final static String TIMESTAMP = "Timestamp";
    final static String MESSAGE = "Message";

    @ExceptionHandler(UserException.class)
    public ResponseEntity<Object> userAlreadyExists(UserException userException) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put(TIMESTAMP, LocalDateTime.now());
        result.put(MESSAGE, userException.getMessage());
        return new ResponseEntity<>(JsonUtil.objectToJsonString(result), HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(EmailFormatException.class)
    public ResponseEntity<Object> invalidEmailFormat(EmailFormatException emailFormatException) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put(TIMESTAMP, LocalDateTime.now());
        result.put(MESSAGE, emailFormatException.getMessage());
        return new ResponseEntity<>(JsonUtil.objectToJsonString(result), HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Object> userNotFound(UserNotFoundException userNotFoundException) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put(TIMESTAMP, LocalDateTime.now());
        result.put(MESSAGE, userNotFoundException.getMessage());
        return new ResponseEntity<>(JsonUtil.objectToJsonString(result), HttpStatus.NOT_FOUND);
    }

}
