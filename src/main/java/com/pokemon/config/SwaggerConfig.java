package com.pokemon.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

	@Bean
	public Docket customImplementation() {
		return new Docket(DocumentationType.SWAGGER_2)
				
				.select()
				.apis(RequestHandlerSelectors.any())
				
				.paths(PathSelectors.any())
				
				.build()
				.apiInfo(apiInfo());
		
	}
	
	public ApiInfo apiInfo() {
		return new ApiInfoBuilder()
				.title(" Pokemon Api").
				description(" JAVA + ANGULAR Integration "
						+ "\n API REST to register, sign in users, add pokemon to the Pokemon Team, etc."
						+ "\n Also is a complementation of Pokedex aplication.").build();
	}
	
	
	

}
