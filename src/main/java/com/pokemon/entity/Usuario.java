package com.pokemon.entity;


import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.pokemon.request.CreateUserRequest;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "usuario")
public class Usuario {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "nombre_team", unique = true)
	private String teamName;
	
	@Column(name = "nombre_entrenador" , unique = true)
	private String traineerName;
	
	@Column(name = "rol")
	private String role; //ADMINISTRADOR - PROVISIONAL
	
	@Column(name = "usuario" , unique = true)
	private String username;
	
	@Column(name = "password")
	private String password;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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

	public List<Pokemon> getPokemones() {
		return pokemones;
	}

	public void setPokemones(List<Pokemon> pokemones) {
		this.pokemones = pokemones;
	}

	@OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
	private List<Pokemon> pokemones;
	
	public Usuario (CreateUserRequest createUserRequest) {
		this.teamName = createUserRequest.getTeamName();
		this.traineerName = createUserRequest.getTraineerName();
		this.role = createUserRequest.getRole();
		this.username = createUserRequest.getUsername();
		this.password = createUserRequest.getPassword();
		
		
	}

	@Override
	public int hashCode() {
		return Objects.hash(username);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Usuario other = (Usuario) obj;
		return Objects.equals(username, other.username);
	}
	
	

}
