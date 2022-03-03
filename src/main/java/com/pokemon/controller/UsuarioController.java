package com.pokemon.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pokemon.entity.Pokemon;
import com.pokemon.entity.Usuario;
import com.pokemon.reponse.JWTAuthResponse;
import com.pokemon.reponse.PokemonListResponse;
import com.pokemon.reponse.PokemonResponse;
import com.pokemon.reponse.UsuarioResponse;
import com.pokemon.request.CreateUserRequest;

import com.pokemon.request.UpdateUserRequest;

import com.pokemon.request.LoginDto;
import com.pokemon.security.JwtTokenProvider;
import com.pokemon.security.SecurityService;
import com.pokemon.service.TipoService;
import com.pokemon.service.UsuarioService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;




@RestController
@RequestMapping("/pokemon/")
@Api(value="API REST Pokemons")
@CrossOrigin(origins = "http://localhost:8080")
public class UsuarioController {
	
	@Autowired
	UsuarioService usuarioService;

	@Autowired
	TipoService tipoService;

	@Autowired
	SecurityService securityService;

	
	// Logger for information
	Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider tokenProvider;


	@GetMapping("pokemons/{username}")
	@ApiOperation(value="Obtaining the pokemons team of selected User by id")
	public PokemonListResponse getAllPokemonsByUser(@RequestHeader Map<String, String> headers, @PathVariable String username) {
		log.info("headers: ");
		securityService.validateTokenWithUsername(headers.get("authorization"), username);
		log.info("Showing pokemons from "+username);
		List<Pokemon> pokemonList = usuarioService.getAllPokemonsByUser(username);
		
		List<PokemonResponse> pokemonResponseList = new ArrayList<PokemonResponse>();
		
		pokemonList.stream().forEach(pokemon -> {
			pokemonResponseList.add(new PokemonResponse(pokemon));
		});

		PokemonListResponse response = new PokemonListResponse(pokemonResponseList);
		
		return response;
	}

	@PostMapping("create")
	//Create a user 
	@ApiOperation(value="Register the user on Data Base")
	public UsuarioResponse createUser (@RequestHeader Map<String, String> headers, @Valid @RequestBody CreateUserRequest createUserRequest) {
		log.info("Creating user "+createUserRequest.getUsername());
		
		Usuario usuario = usuarioService.createUsuario(createUserRequest);
		
		log.info(" The user '" + usuario.getUsername() + "' has been created. ");
		return new UsuarioResponse(usuario);
	}
  
	//@PreAuthorize("hasAnyRole('Administrador','Provisional')")
	@PutMapping("update")
	@ApiOperation("Update Data General user & add new Pokemons to the team! ")
	//Update the data for the user
	public UsuarioResponse updateUser(@RequestHeader Map<String, String> headers, @Valid @RequestBody UpdateUserRequest updateUser) {
		log.info("Updating user: "+updateUser.getUsername());

		securityService.validateTokenWithUsername(headers.get("authorization"), updateUser.getUsername());

		return new UsuarioResponse(usuarioService.updateData(updateUser));
	}
	


	@GetMapping("user/{username}")
	@ApiOperation("Get User Information, by id")
	//Bring you the hole information about a user
	public UsuarioResponse getUser(@RequestHeader Map<String, String> headers, @PathVariable String username) {
		log.info("showing info from "+username);

		securityService.validateTokenWithUsername(headers.get("authorization"), username);
		return new UsuarioResponse(usuarioService.getByUsername(username));
	}


	@ApiOperation("Delete pokemon by id")
	@DeleteMapping("deletePokemon/{id}")
	//Delete the pokemon by the pokemon_id
	public String deletePokemon(@RequestHeader Map<String, String> headers, @PathVariable long id ) {
		log.info("Deleting the pokemon with id"+id);
		return usuarioService.deletePokemon(id) ;
	}


	@ApiOperation("Sign in Button, to access to the Pokedex")
    @PostMapping("/signin")
    public ResponseEntity<JWTAuthResponse> authenticateUser(@RequestHeader Map<String, String> headers, @RequestBody LoginDto loginDto){
        log.info("Sign in the user: "+loginDto.getUsernameOrEmail());
		
		Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDto.getUsernameOrEmail(), loginDto.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // get token form tokenProvider
        String token = tokenProvider.generateToken(authentication);

        return ResponseEntity.ok(new JWTAuthResponse(token));

    }
	//Delete the username by username
	@ApiOperation("Delete a user when connection failure")
	@DeleteMapping("/delete/{username}")
	public void  deleteUser(@RequestHeader Map<String, String> headers, @PathVariable String username) {
		log.info("Deleting the user "+username);

		securityService.validateTokenWithUsername(headers.get("authorization"), username);
		usuarioService.deleteUser(username);
		
	}

	@GetMapping("/set_types")
	public void createTypes(){
		log.info("creating types table");
		tipoService.agregarTipos();
	}    

}    
