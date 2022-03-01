package com.pokemon.request;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.UniqueElements;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class UpdateUserRequest {
	
	@NotBlank(message = "Team name is required")
	@JsonProperty("nombre_team")
	private String teamName;
	
	@NotBlank(message = "Traineer name is required")
	@JsonProperty("nombre_entrenador")
	private String traineerName;
	
	@NotBlank(message = "Role is required")
	@JsonProperty("rol")
	private String role;
	
	@NotBlank(message = "Username is required")
	@JsonProperty("usuario")
	private String username;
	
	//@NotBlank(message = "Password is required")
	private String password;
	
	@NotNull
	@NotEmpty
	@UniqueElements
	private List<CreatePokemonRequest> pokemon;

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public String getTraineerName() {
		return traineerName;
	}

	public void setTraineerName(String traineerName) {
		this.traineerName = traineerName;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<CreatePokemonRequest> getPokemon() {
		return pokemon;
	}

	public void setPokemon(List<CreatePokemonRequest> pokemon) {
		this.pokemon = pokemon;
	}


	
}
