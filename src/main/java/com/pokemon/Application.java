package com.pokemon;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan({"com.pokemon.controller", "com.pokemon.service", "com.pokemon.error", "com.pokemon.config", "com.pokemon.security"})
@EntityScan("com.pokemon.entity")
@EnableJpaRepositories("com.pokemon.repository")
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	
	

}
