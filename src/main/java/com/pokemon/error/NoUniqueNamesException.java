package com.pokemon.error;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class NoUniqueNamesException extends RuntimeException {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//private String message = "";

	public NoUniqueNamesException(String message) {
		super(message);
		
	}

}
