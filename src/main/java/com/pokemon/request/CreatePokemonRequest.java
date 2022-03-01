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


	public String getNombre_pokemon() {
		return nombre_pokemon;
	}


	public void setNombre_pokemon(String nombre_pokemon) {
		this.nombre_pokemon = nombre_pokemon;
	}


	public List<String> getTipo_pokemon() {
		return tipo_pokemon;
	}


	public void setTipo_pokemon(List<String> tipo_pokemon) {
		this.tipo_pokemon = tipo_pokemon;
	}
	

	
}
