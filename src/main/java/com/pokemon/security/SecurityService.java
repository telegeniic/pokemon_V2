package com.pokemon.security;

import com.pokemon.error.APIException;
import com.pokemon.service.UsuarioService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class SecurityService {
    
    @Autowired
    UsuarioService usuarioService;

    Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    JwtTokenProvider tokenProvider;

    public boolean validateTokenWithUsername(String token, String username){
        token = token.substring("Bearer ".length());
        if (!tokenProvider.getUsernameFromJWT(token).equals(username))
            throw new APIException(HttpStatus.UNAUTHORIZED, "the bearer token dont match with this user");
        return true;
    }
}
