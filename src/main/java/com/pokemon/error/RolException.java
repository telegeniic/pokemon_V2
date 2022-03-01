package com.pokemon.error;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class RolException extends RuntimeException {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//private String message = "";

	public RolException(String message) {
		super(message);
		
	}

}
