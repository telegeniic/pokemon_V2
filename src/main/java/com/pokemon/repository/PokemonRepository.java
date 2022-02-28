package com.pokemon.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pokemon.entity.Pokemon;

public interface PokemonRepository extends JpaRepository<Pokemon, Long> {
	List<Pokemon> findByUsuarioId(long id);
	
	Pokemon findByName(String name);
	
	
}
