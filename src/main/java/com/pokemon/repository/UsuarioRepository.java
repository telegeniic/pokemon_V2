package com.pokemon.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pokemon.entity.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {


	Usuario findByTeamNameOrTraineerNameOrUsername(String nombreTeam, String nombreEntrenador, String usuario);
	
	Optional<Usuario> findByUsername(String username);
	

    Boolean existsByUsername(String usuario);
    
    //codigo agregado por servando
    Long deleteByUsername(String username);
	
    
    //Usuario findByNombreTeamOrNombreEntrenadorOrUsuario(String nombreTeam, String nombreEntrenador, String usuario);

}
