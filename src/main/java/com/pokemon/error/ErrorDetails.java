package com.pokemon.error;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Getter
@NoArgsConstructor
public class ErrorDetails {
	private HttpStatus httpStatus;
	private String message;
	private String details;
}
