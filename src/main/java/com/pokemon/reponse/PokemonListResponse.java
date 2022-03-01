package com.pokemon.reponse;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PokemonListResponse {
    
    private List<PokemonResponse> pokemons;

    public PokemonListResponse(){

    }

    public PokemonListResponse(List<PokemonResponse> pokemons){
        this.pokemons = pokemons;
    }
}
