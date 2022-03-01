package com.pokemon.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.pokemon.entity.Pokemon;
import com.pokemon.entity.Tipo;
import com.pokemon.entity.Usuario;
import com.pokemon.error.NoUniqueNamesException;
import com.pokemon.error.RolException;
import com.pokemon.error.TypePokemonException;
import com.pokemon.repository.PokemonRepository;
import com.pokemon.repository.TipoRepository;
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
	
	@Autowired
    TipoRepository tipoRepository;

	
	@Autowired
	TipoService tipoService;

	public Usuario createUsuario(CreateUserRequest createUserRequest) {
		
		tipoService.agregarTipos();

		Usuario usuario = new Usuario(createUserRequest);
		usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));

		if (usuarioRepository.findByTeamNameOrTraineerNameOrUsername(usuario.getTeamName(), usuario.getTraineerName(),
				usuario.getUsername()) != null) {
			throw new NoUniqueNamesException("Username, team name, and traineer name is already taken. ");
		}
		List<String> roles = new ArrayList<String>();
		roles.add("ADMIN");
		roles.add("USER");
		if(!(roles.contains(createUserRequest.getRole().toUpperCase())))
		throw new RolException("Role doesn't exist");
		
		usuario = usuarioRepository.save(usuario);
		

		List<Pokemon> pokemonList = new ArrayList<Pokemon>();

		if (createUserRequest.getPokemon() != null) {
			for (CreatePokemonRequest createPokemonRequest : createUserRequest.getPokemon()) {
				Pokemon pokemon = new Pokemon();
				List<Tipo> tipos_pokemon=new ArrayList<Tipo>();
				pokemon.setName(createPokemonRequest.getNombre_pokemon());

				if(createPokemonRequest.getTipo_pokemon().isEmpty()){
					this.deleteUser(usuario.getId());
					throw new TypePokemonException("Pokemon type's list is empty");
				}

				if(createPokemonRequest.getNombre_pokemon().isEmpty()||
				createPokemonRequest.getNombre_pokemon()==null){
					this.deleteUser(usuario.getId());
					throw new TypePokemonException("Pokemon name´s is empty");
				}
				for (String nombreTipo:createPokemonRequest.getTipo_pokemon()){
					Tipo type=new Tipo();
					
					for(Tipo name:tipoRepository.findAll()){
						if((nombreTipo.toLowerCase()).equals(name.getTipo().toLowerCase())){
							type=tipoService.getTipo(nombreTipo);
							tipos_pokemon.add(type);
							break;
						}
					}

					if(type.getTipo()==null){
					this.deleteUser(usuario.getId());
					throw new TypePokemonException("Pokemon type's not exist");
					}
				}
				pokemon.setTipos(tipos_pokemon);
				pokemon.setUsuario(usuario);

				pokemonList.add(pokemon);
			}

			pokemonRepository.saveAll(pokemonList);

		}else{
			this.deleteUser(usuario.getId());
		throw new TypePokemonException("Pokemon list´s is empty");
		}

		usuario.setPokemones(pokemonList);

		return usuario;
	}

	public List<Pokemon> getAllPokemonsByUser(String username) {
		Usuario usuario = usuarioRepository.findByUsername(username).get();
		return usuario.getPokemones();
	}

	public void deleteUser(long id) {
		usuarioRepository.deleteById(id);

	}

	public String deletePokemon(long id) {
		pokemonRepository.deleteById(id);
		return "Pokemon deleted succesfully";

	}

	public Usuario updateData(UpdateUserRequest updateUser) {

		Usuario user = usuarioRepository.findByUsername(updateUser.getUsername()).get();

		if (user != null) {
			user.setTraineerName(updateUser.getTraineerName());
			user.setTeamName(updateUser.getTeamName());
			user.setRole(updateUser.getRole());
			
			
			if(!updateUser.getPassword().isBlank() && !user.getPassword().equals(updateUser.getPassword())) 
				user.setPassword(passwordEncoder.encode(updateUser.getPassword()));
			
			user = usuarioRepository.save(user);
		}

		List<Pokemon> pokemonList = new ArrayList<Pokemon>();

		if (updateUser.getPokemon() != null) {
			for (CreatePokemonRequest createPokemonRequest : updateUser.getPokemon()) {
				Pokemon pokemon = new Pokemon();
				pokemon.setName(createPokemonRequest.getNombre_pokemon());
				//pokemon.setType(createPokemonRequest.getTipo_pokemon());
				pokemon.setUsuario(user);

				if (user.getPokemones().contains(pokemon)) {
					throw new NoUniqueNamesException("Pokemon already exist");
				}

				pokemonList.add(pokemon);
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
