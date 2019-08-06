package org.baat.chat.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class UserErrorHandler extends ResponseEntityExceptionHandler {
	//TODO bean validation framework
	//	@ExceptionHandler({MethodArgumentNotValidException.class})
	//	public ResponseEntity<String> handleMethodArgumentNotValidException(final MethodArgumentNotValidException ex) {
	//		BindingResult result = ex.getBindingResult();
	//		FieldError error = result.getFieldError();
	//		String errorMessage = null;
	//		if (error != null) {
	//			errorMessage = error.getField() + " - " + error.getDefaultMessage();
	//		}
	//		return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
	//	}

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
