package com.pokemon.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.pokemon.entity.Pokemon;
import com.pokemon.entity.Usuario;
import com.pokemon.error.APIException;
import com.pokemon.error.NoUniqueNamesException;
import com.pokemon.repository.PokemonRepository;
import com.pokemon.repository.UsuarioRepository;
import com.pokemon.request.CreatePokemonRequest;
import com.pokemon.request.CreateUserRequest;
import com.pokemon.request.UpdateUserRequest;

@Service
public class UsuarioService {

	@Autowired
	UsuarioRepository usuarioRepository;

	@Autowired
	PokemonRepository pokemonRepository;

	@Autowired
	PasswordEncoder passwordEncoder;

	public Usuario createUsuario(CreateUserRequest createUserRequest) {

		Usuario usuario = new Usuario(createUserRequest);
		usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));

		if (usuarioRepository.findByTeamNameOrTraineerNameOrUsername(usuario.getTeamName(), usuario.getTraineerName(),
				usuario.getUsername()) != null) {
			throw new NoUniqueNamesException("Username, team name, and traineer name is already taken. ");
		}
		usuario = usuarioRepository.save(usuario);

		List<Pokemon> pokemonList = new ArrayList<Pokemon>();

		if (createUserRequest.getPokemon() != null) {
			for (CreatePokemonRequest createPokemonRequest : createUserRequest.getPokemon()) {
				Pokemon pokemon = new Pokemon();
				pokemon.setName(createPokemonRequest.getNombre_pokemon());
				pokemon.setType(createPokemonRequest.getTipo_pokemon());
				pokemon.setUsuario(usuario);

				pokemonList.add(pokemon);
			}

			pokemonRepository.saveAll(pokemonList);

		}

		usuario.setPokemones(pokemonList);

		return usuario;
	}

	public List<Pokemon> getAllPokemonsByUser(String username) {
		Usuario usuario = usuarioRepository.findByUsername(username).get();
		return usuario.getPokemones();
	}

	public String deletePokemon(long id) {
		pokemonRepository.deleteById(id);
		return "Pokemon deleted succesfully";

	}

	public Usuario updateData(UpdateUserRequest updateUser) {

		Usuario user = usuarioRepository.findByUsername(updateUser.getUsername()).orElseThrow( () -> new APIException(HttpStatus.NOT_FOUND, "No se encontro el usuario a modificar"));
		user.setTraineerName(updateUser.getTraineerName());
		user.setTeamName(updateUser.getTeamName());
		user.setRole(updateUser.getRole());
		
		
		if(!updateUser.getPassword().isBlank() && !user.getPassword().equals(updateUser.getPassword())) 
			user.setPassword(passwordEncoder.encode(updateUser.getPassword()));
		
		user = usuarioRepository.save(user);

		List<Pokemon> pokemonList = new ArrayList<Pokemon>();

		updateUser.getPokemon().forEach(pokemon -> {
			

		});
		if (updateUser.getPokemon() != null) {
			for (CreatePokemonRequest pokemon : updateUser.getPokemon()) {
				Pokemon newPokemon = new Pokemon(pokemon, user);

				if (user.getPokemones().contains(newPokemon)) {
					throw new APIException(HttpStatus.CONFLICT,"Pokemon already exist");
				}

				pokemonList.add(newPokemon);
			}

			pokemonRepository.saveAll(pokemonList);

		}

		user.setPokemones(pokemonList);

		return user;
	}

	public Usuario getUserbyId(long id) {
		return usuarioRepository.getById(id);
	}
	
	public Usuario getByUsername(String username) {
		return usuarioRepository.findByUsername(username).get();
	}

}
