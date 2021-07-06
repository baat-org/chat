package org.baat.chat.error;

import org.apache.logging.log4j.util.Strings;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@ControllerAdvice
public class ChatErrorHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        final BindingResult result = ex.getBindingResult();
        final FieldError error = result.getFieldError();
        String errorMessage = null;
        if (error != null) {
            errorMessage = error.getField() + " - " + error.getDefaultMessage();
        }
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<String> handleConstraintViolationException(final ConstraintViolationException ex) {
        final Function<Path, String> getLast = path -> {
            if (path == null) return "";

            String field = "";
            for (Path.Node node : path) {
                field = node.getName();
            }
            return field;
        };

        final Set<ConstraintViolation<?>> constraintViolations = ex.getConstraintViolations();
        final Set<String> messages = new HashSet<>(constraintViolations.size());
        messages.addAll(constraintViolations.stream()
                .map(constraintViolation -> String.format("%s %s", getLast.apply(constraintViolation.getPropertyPath()), constraintViolation.getMessage()))
                .collect(Collectors.toList()));

        return new ResponseEntity<>(Strings.join(messages, '\n'), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity<String> handleIllegalArgumentException(final IllegalArgumentException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({IllegalAccessException.class})
    public ResponseEntity<String> handleIllegalAccessException(final IllegalAccessException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({IllegalStateException.class})
    public ResponseEntity<String> handleIllegalStateException(final IllegalStateException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
