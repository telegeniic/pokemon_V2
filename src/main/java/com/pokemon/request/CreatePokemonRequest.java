package com.pokemon.request;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;


import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter

@EqualsAndHashCode

public class CreatePokemonRequest {

	@NotNull
	@NotEmpty
	@JsonProperty("nombre_pokemon")
	private String nombre_pokemon;
	
	
	@NotNull
	@NotEmpty
	@JsonProperty("tipo_pokemon")
	private List<String> tipo_pokemon;
	

	
}
