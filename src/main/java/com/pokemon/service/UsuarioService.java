package com.pokemon.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.pokemon.entity.Pokemon;
import com.pokemon.entity.Tipo;
import com.pokemon.entity.Usuario;
import com.pokemon.error.APIException;
import com.pokemon.error.NoUniqueNamesException;
import com.pokemon.repository.PokemonRepository;
import com.pokemon.repository.TipoRespository;
import com.pokemon.repository.UsuarioRepository;
import com.pokemon.request.CreatePokemonRequest;
import com.pokemon.request.CreateUserRequest;
import com.pokemon.request.UpdateUserRequest;

@Service
public class UsuarioService {

	private static final String[] ROLES = {"ADMINISTRADOR", "PROVISIONAL"};
	private static final String ADMINISTRADOR = ROLES[0];
	private static final String PROVISIONAL = ROLES[1];

	@Autowired
	UsuarioRepository usuarioRepository;

	@Autowired
	PokemonRepository pokemonRepository;

	@Autowired
	TipoRespository tipoRespository;

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
				//pokemon.setType(createPokemonRequest.getTipo_pokemon());
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

		Usuario user = usuarioRepository.findByUsername(updateUser.getUsername()).orElseThrow( () -> new APIException(HttpStatus.NOT_FOUND, "User not found"));
		user.setTraineerName(updateUser.getTraineerName());
		user.setTeamName(updateUser.getTeamName());
		user.setRole(updateUser.getRole());
		
		
		if(!updateUser.getPassword().isBlank() && !user.getPassword().equals(updateUser.getPassword())) 
			user.setPassword(passwordEncoder.encode(updateUser.getPassword()));

		if (user.getRole().equals(PROVISIONAL)){
			return usuarioRepository.save(user);
		}

		List<Pokemon> pokemonList = new ArrayList<Pokemon>();
		if (updateUser.getPokemon() != null) {
			for (CreatePokemonRequest pokemon : updateUser.getPokemon()) {
				Pokemon newPokemon = new Pokemon();
				newPokemon.setName(pokemon.getNombre_pokemon());
				newPokemon.setUsuario(user);
				List<Tipo> tipos = new ArrayList<>();
				pokemon.getTipo_pokemon().forEach(tipo -> {
					Tipo newTipo = tipoRespository.findByTipo(tipo).orElseThrow(() -> {
						throw new APIException(HttpStatus.NOT_FOUND, "Pokemon type not found");
					});
					tipos.add(newTipo);
				});
				newPokemon.setTipos(tipos);

				if (user.getPokemones().contains(newPokemon)) {
					throw new APIException(HttpStatus.CONFLICT,"Pokemon already exist");
				}
				pokemonList.add(newPokemon);
				if (user.getRole().equals(ADMINISTRADOR) && pokemonList.size()>10){
					throw new APIException(HttpStatus.LOCKED, "ADMIN user has a max of 10 pokemons");
				}
			}

			pokemonRepository.saveAll(pokemonList);

		}
		user.setPokemones(pokemonList);
		return usuarioRepository.save(user);
	}

	public Usuario getUserbyId(long id) {
		return usuarioRepository.getById(id);
	}
	
	public Usuario getByUsername(String username) {
		return usuarioRepository.findByUsername(username).get();
	}

}
