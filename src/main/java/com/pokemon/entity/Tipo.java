package com.pokemon.entity;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "tipos")
public class Tipo {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idTipo")
	private Long id;
	
	@NotNull
	@Column(name = "nombre_tipo")
	private String tipo;
	
	
	@ManyToMany(fetch=FetchType.LAZY,mappedBy="tipos")
	private List<Pokemon> pokemons =new ArrayList<>();

	public void setTipo(String tipo){
		this.tipo=tipo;
	}
	public String getTipo(){
		return tipo;
	}

}
