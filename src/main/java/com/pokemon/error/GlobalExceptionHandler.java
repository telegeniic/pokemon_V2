package com.pokemon.error;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.security.core.AuthenticationException;

import com.pokemon.controller.UsuarioController;

@ControllerAdvice
public class GlobalExceptionHandler {
	
	// Logger for information
	Logger log = LoggerFactory.getLogger(UsuarioController.class);
	
	@ExceptionHandler(NoUniqueNamesException.class)
	public ResponseEntity<ErrorDetails> handleResourceNoUniqueNamesException(NoUniqueNamesException exception, 
			WebRequest webrequest){


		ErrorDetails error = new ErrorDetails(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage(), webrequest.getDescription(false));

		error(error);

		return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorDetails> handleResourceMethodArgumentNotValidException(MethodArgumentNotValidException exception, 
			WebRequest webrequest){
		ErrorDetails error = null;
		String message = "";
		if(exception.getMessage().contains("unique")) {
			message = " You can only add one pokemon per name. ";
		}else {
			message = " You must select unless a pokemon and it's type.";
		}
		error = new ErrorDetails(HttpStatus.INTERNAL_SERVER_ERROR,message, webrequest.getDescription(false));
		
		error(error);

		return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	
	@ExceptionHandler(APIException.class)
	public ResponseEntity<ErrorDetails> handleResourceBlogAPIException(APIException exception, 
			WebRequest webrequest){
		ErrorDetails error = new ErrorDetails(exception.getStatus(), exception.getMessage() , webrequest.getDescription(false));
		
		error(error);
		return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(AuthenticationException.class)
	public ResponseEntity<ErrorDetails> handleResourceAuthenticationException(AuthenticationException exception, 
			WebRequest webrequest){
		ErrorDetails error = new ErrorDetails(HttpStatus.BAD_REQUEST, " Wrong credentials." , webrequest.getDescription(false));
		
		error(error);
		return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	private void error(ErrorDetails error) {
		log.error(" A error has ocurred: " + error.getMessage() + " on " + error.getDetails());
	}
	

	@ExceptionHandler(TypePokemonException.class)
	public ResponseEntity<ErrorDetails> handleResourceTypePokemonException(TypePokemonException exception, 
			WebRequest webrequest){


		ErrorDetails error = new ErrorDetails(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage(), webrequest.getDescription(false));

		error(error);

		return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(RolException.class)
	public ResponseEntity<ErrorDetails> handleResourceRolException(RolException exception, 
			WebRequest webrequest){


		ErrorDetails error = new ErrorDetails(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage(), webrequest.getDescription(false));

		error(error);

		return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	
	

}
