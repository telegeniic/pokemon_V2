package com.pokemon.request;



import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.UniqueElements;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CreateUserRequest {
	
	@NotNull
	@NotEmpty
	@JsonProperty("nombre_team")
	private String teamName;
	
	@NotNull
	@NotEmpty
	@JsonProperty("nombre_entrenador")
	private String traineerName;
	
	@NotNull
	@NotEmpty
	@JsonProperty("rol")
	private String role;
	
	@NotNull
	@NotEmpty
	@JsonProperty("usuario")
	private String username;
	
	@NotNull
	@NotEmpty
	private String password;
	
	@NotNull
	@NotEmpty
	@UniqueElements
	private List<CreatePokemonRequest> pokemon;
		

}
