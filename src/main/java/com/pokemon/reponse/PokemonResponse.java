package com.pokemon.reponse;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.pokemon.entity.Pokemon;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PokemonResponse {
	
	private Long id;
	
	@JsonAlias("nombrePokemon")
	private String name;
	
	@JsonAlias("tipo_pokemon")
	private String type;
	
	public PokemonResponse (Pokemon pokemon) {
		this.id = pokemon.getId();
		this.name = pokemon.getName();
		this.type = pokemon.getType();
	}
	
}
