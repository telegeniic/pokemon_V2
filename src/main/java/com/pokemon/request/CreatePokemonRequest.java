package com.pokemon.request;

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
	private String tipo_pokemon;
	
}
