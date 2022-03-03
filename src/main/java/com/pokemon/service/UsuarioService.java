package com.pokemon.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pokemon.entity.Pokemon;
import com.pokemon.entity.Tipo;
import com.pokemon.entity.Usuario;
import com.pokemon.error.APIException;
import com.pokemon.error.NoUniqueNamesException;
import com.pokemon.error.RolException;
import com.pokemon.error.TypePokemonException;
import com.pokemon.repository.PokemonRepository;
import com.pokemon.repository.TipoRepository;
import com.pokemon.repository.UsuarioRepository;
import com.pokemon.request.CreatePokemonRequest;
import com.pokemon.request.CreateUserRequest;
import com.pokemon.request.UpdateUserRequest;

@Transactional
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
	TipoRepository tipoRespository;

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
		
		this.validarCantidad(createUserRequest);

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
		Usuario usuario = usuarioRepository.findByUsername(username).orElseThrow(() -> {
			throw new APIException(HttpStatus.NOT_FOUND, "User not found");
		});
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

		Usuario user = usuarioRepository.findByUsername(updateUser.getUsername()).orElseThrow( () -> new APIException(HttpStatus.NOT_FOUND, "User not found"));
		user.setTraineerName(updateUser.getTraineerName());
		user.setTeamName(updateUser.getTeamName());

		if (!user.getRole().equals(updateUser.getRole())){
			throw new APIException(HttpStatus.BAD_REQUEST, "You cant change your role");
		}
		
		
		if(!updateUser.getPassword().isBlank() && !user.getPassword().equals(updateUser.getPassword())) 
			user.setPassword(passwordEncoder.encode(updateUser.getPassword()));

		

		List<Pokemon> pokemonList = new ArrayList<Pokemon>();
		if (updateUser.getPokemon() != null) {
			for (CreatePokemonRequest pokemon : updateUser.getPokemon()) {
				Pokemon newPokemon = new Pokemon();
				newPokemon.setName(pokemon.getNombre_pokemon());
				newPokemon.setUsuario(user);
				List<Tipo> tipos = new ArrayList<>();
				pokemon.getTipo_pokemon().forEach(tipo -> {
					Tipo newTipo = tipoService.getTipo(tipo);
					tipos.add(newTipo);
				});
				newPokemon.setTipos(tipos);

				if (pokemonList.contains(newPokemon)) {
					throw new APIException(HttpStatus.CONFLICT,"Pokemon already exist");
				}
				if (user.getPokemones().contains(newPokemon)) {
					int index = user.getPokemones().indexOf(newPokemon);
					pokemonList.add(user.getPokemones().get(index));
				} else if (user.getRole().equals(PROVISIONAL)){
					throw new APIException(HttpStatus.LOCKED, "your role cant modify his pokemons");
				}
				pokemonList.add(newPokemon);
				if (user.getRole().equals(ADMINISTRADOR) && pokemonList.size()>10){
					throw new APIException(HttpStatus.LOCKED, "ADMIN user has a max of 10 pokemons");
				}
			}

			pokemonRepository.saveAll(pokemonList);

		}
		user.getPokemones().forEach(oldPokemon -> {
			this.deletePokemon(oldPokemon.getId());
		});
		user.setPokemones(pokemonList);
		return usuarioRepository.save(user);
	}

	public Usuario getUserbyId(long id) {
		return usuarioRepository.getById(id);
	}
	
	public Usuario getByUsername(String username) {
		return usuarioRepository.findByUsername(username).orElseThrow(() -> {
			throw new APIException(HttpStatus.NOT_FOUND, "User not found");
		});
	}
	
	//delete username
	
	public String deleteUser(String username) {
		Usuario usuario = usuarioRepository.findByUsername(username).orElseThrow(() -> {
			throw new APIException(HttpStatus.NOT_FOUND, "User not found");
					
		});
		usuarioRepository.deleteById(usuario.getId());
		return "User deleted succesfully";

	}
	


	public void validarCantidad(CreateUserRequest userRequest){
		if(userRequest.getRole().toUpperCase().equals("ADMIN")){
			if(userRequest.getPokemon().size()>10){
				throw new RolException("You can  add 10 pokemons as maximun");
			}
		}
		else{
				if(userRequest.getPokemon().size()>5){
					throw new RolException("You can  add 5 pokemons as maximun");
				}
			
		}
	}


}
