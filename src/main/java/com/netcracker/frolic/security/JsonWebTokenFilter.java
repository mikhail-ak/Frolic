package com.netcracker.frolic.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class JsonWebTokenFilter extends GenericFilterBean {

    private final JsonWebTokenUtil jsonWebTokenUtil;

    public JsonWebTokenFilter(JsonWebTokenUtil jsonWebTokenUtil)
    { this.jsonWebTokenUtil = jsonWebTokenUtil; }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain)
            throws IOException, ServletException {

        String token = jsonWebTokenUtil.resolveToken((HttpServletRequest) req);
        if (token != null && jsonWebTokenUtil.validateToken(token)) {
            Authentication auth = jsonWebTokenUtil.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(auth);
        }
        filterChain.doFilter(req, res);
    }

}
