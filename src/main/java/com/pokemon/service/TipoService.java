package com.pokemon.service;

import org.springframework.stereotype.Service;
import com.pokemon.repository.TipoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import com.pokemon.entity.Tipo;
import com.pokemon.error.APIException;

@Service
public class TipoService {

    @Autowired
    TipoRepository tipoRepository;


    public void agregarTipos(){
        String [] tipos = {"Normal", "Fire", "Water","Grass","Flying", "Fighting", "Poison", "Electric",
        "Ground", "Rock", "Psychic", "Ice", "Bug", "Ghost", "Steel", "Dragon", "Dark", "Fairy"};
        if(tipoRepository.findAll().size()==0){
            for (String tipo: tipos){
                Tipo type =new Tipo();
                type.setTipo(tipo);
                tipoRepository.save(type);
            }

        }
    }

    public Tipo getTipo(String name){
        return tipoRepository.getByNombre(name).orElseThrow(() -> {
            throw new APIException(HttpStatus.NOT_FOUND, "Pokemon type not found");
        });
        }
    
}
