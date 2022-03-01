package com.pokemon.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.pokemon.entity.Tipo;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface TipoRepository extends JpaRepository<Tipo, Long> {

    @Query("From Tipo where tipo=:tipo")
	Tipo getByNombre(@Param("tipo") String nombre);
    
}
