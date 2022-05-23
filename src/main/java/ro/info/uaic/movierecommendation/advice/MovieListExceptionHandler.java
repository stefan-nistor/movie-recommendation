package ro.info.uaic.movierecommendation.advice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ro.info.uaic.movierecommendation.apierror.ApiError;
import ro.info.uaic.movierecommendation.exceptions.MovieAlreadyInListException;
import ro.info.uaic.movierecommendation.exceptions.MovieListAlreadyExistsException;
import ro.info.uaic.movierecommendation.exceptions.MovieListNotFoundException;
import ro.info.uaic.movierecommendation.exceptions.MovieNotInListException;

import javax.swing.text.html.parser.Entity;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
@Slf4j
public class MovieListExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(MovieListNotFoundException.class)
    protected ResponseEntity<Object> handleEntityNotFound(MovieListNotFoundException exception) {
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND);
        apiError.setMessage(exception.getMessage());
        return buildResponseEntity(apiError);
    }

    private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    @ExceptionHandler(MovieAlreadyInListException.class)
    protected ResponseEntity<Object> handleEntityAlreadyExists(MovieAlreadyInListException exception) {
        ApiError apiError = new ApiError(HttpStatus.CONFLICT);
        apiError.setMessage(exception.getMessage());
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(MovieNotInListException.class)
    protected ResponseEntity<Object> handleEntityNotPresent(MovieNotInListException exception) {
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND);
        apiError.setMessage(exception.getMessage());
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(MovieListAlreadyExistsException.class)
    protected ResponseEntity<Object> handleEntityAlreadyExists(MovieListAlreadyExistsException exception) {
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND);
        apiError.setMessage(exception.getMessage());
        return buildResponseEntity(apiError);
    }

}
