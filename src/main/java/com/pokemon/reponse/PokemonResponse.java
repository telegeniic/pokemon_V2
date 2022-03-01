package com.pokemon.reponse;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.pokemon.entity.Pokemon;
import com.pokemon.entity.Tipo;

import lombok.Getter;
import lombok.Setter;
import java.util.List;
import java.util.ArrayList;

@Getter
@Setter
public class PokemonResponse {
	
	private Long id;
	
	@JsonAlias("nombrePokemon")
	private String name;
	
	@JsonAlias("tipo_pokemon")
	private List<String> type;
	
	public PokemonResponse (Pokemon pokemon) {
		this.type=new ArrayList<String>();
		this.id = pokemon.getId();
		this.name = pokemon.getName();
		for(Tipo tipoPokemon: pokemon.getTipos()){
				this.type.add(tipoPokemon.getTipo());
		}
	}
	
}
