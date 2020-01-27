package com.netcracker.frolic.config;

import com.netcracker.frolic.security.JsonWebTokenFilter;
import com.netcracker.frolic.security.JsonWebTokenUtil;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class JwtFilterConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private JsonWebTokenUtil jsonWebTokenUtil;

    public JwtFilterConfig(JsonWebTokenUtil jsonWebTokenUtil)
    { this.jsonWebTokenUtil = jsonWebTokenUtil; }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        JsonWebTokenFilter customFilter = new JsonWebTokenFilter(jsonWebTokenUtil);
        http.addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
