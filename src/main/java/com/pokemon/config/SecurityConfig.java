package com.pokemon.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


import com.pokemon.security.CustomUserDetailsService;
import com.pokemon.security.JwtAuthenticationEntryPoint;
import com.pokemon.security.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private JwtAuthenticationEntryPoint authenticationEntryPoint;

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(){
        return  new JwtAuthenticationFilter();
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
	    
    }

    private static final String[] AUTH_WHITELIST = {

        // -- swagger ui
        "/swagger-resources/**",
        "/swagger-ui.html",
        "/v2/api-docs",
        "/webjars/**",
        "/error"
};


    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	
        http     .csrf().disable()
                .exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint)
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/pokemon/user/{username}").authenticated()
                .antMatchers(HttpMethod.GET, "/pokemon/pokemons/{username}").authenticated()
                .antMatchers(HttpMethod.PUT, "/pokemon/update").authenticated()
                .antMatchers(HttpMethod.POST, "/pokemon/create").permitAll()
                .antMatchers(HttpMethod.DELETE, "/pokemon/deletePokemon/{id}").permitAll()
                .antMatchers(HttpMethod.DELETE, "/pokemon/delete/{username}").authenticated()
                .antMatchers(HttpMethod.OPTIONS, "/*").permitAll()
                .antMatchers(AUTH_WHITELIST).permitAll()
                .antMatchers("/pokemon/signin").permitAll()
                .antMatchers("/swagger-ui/**").permitAll()
                
                .antMatchers("/**").permitAll()
                .anyRequest()
                .authenticated();
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
	    
    }
    
    

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
    

    //    @Override
//    @Bean
//    protected UserDetailsService userDetailsService() {
//        UserDetails ramesh = User.builder().username("ramesh").password(passwordEncoder()
//                .encode("password")).roles("USER").build();
//        UserDetails admin = User.builder().username("admin").password(passwordEncoder()
//                .encode("admin")).roles("ADMIN").build();
//        return new InMemoryUserDetailsManager(ramesh, admin);
//  
}
