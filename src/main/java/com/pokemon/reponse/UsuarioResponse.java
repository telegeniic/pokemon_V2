package com.pokemon.reponse;


import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pokemon.entity.Usuario;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UsuarioResponse {
	
	private Long id;
	
	@JsonProperty("nombre_team")
	private String teamname;
	
	@JsonProperty("nombre_entrenador")
	private String traineername;
	
	@JsonProperty("rol")
	private String role;
	
	@JsonProperty("usuario")
	private String username;
	
	//private String password;
	
	
	private List<PokemonResponse> pokemon;
	
	public UsuarioResponse (Usuario usuario) {
		this.id = usuario.getId();
		this.teamname = usuario.getTeamName();
		this.traineername = usuario.getTraineerName();
		this.role = usuario.getRole();
		this.username = usuario.getUsername();

		
		
		if (usuario.getPokemones() != null) {
			pokemon = usuario.getPokemones().stream().map(PokemonResponse::new).collect(Collectors.toList());
		}
	}


}
