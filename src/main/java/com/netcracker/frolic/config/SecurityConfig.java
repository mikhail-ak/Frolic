package com.netcracker.frolic.config;

import com.netcracker.frolic.security.JsonWebTokenUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JsonWebTokenUtil jsonWebTokenUtil;

    public SecurityConfig(JsonWebTokenUtil jsonWebTokenUtil) {
        this.jsonWebTokenUtil = jsonWebTokenUtil;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception
    { return super.authenticationManagerBean(); }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/game-shop/**").permitAll()
                .antMatchers(HttpMethod.PUT, "/game-handle/**").hasRole("ADMIN")
                .anyRequest().authenticated()
                .and()
                .apply(new JwtFilterConfig(jsonWebTokenUtil));
    }
}