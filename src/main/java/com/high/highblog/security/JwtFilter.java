package com.high.highblog.security;

import com.high.highblog.bloc.JwtBloc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

@Component
public class JwtFilter
        extends OncePerRequestFilter {

    private final JwtBloc jwtBloc;

    public JwtFilter(final JwtBloc jwtBloc){
        this.jwtBloc = jwtBloc;
    }

    @Override
    protected void doFilterInternal(final HttpServletRequest request,
                                    final HttpServletResponse response,
                                    final FilterChain filterChain)
            throws ServletException, IOException {

        String autHeader = request.getHeader("Authorization");
        if (autHeader != null && autHeader.startsWith("Bearer" + " ")) {
            String token = autHeader.replace("Bearer" + " ", "").trim();

            jwtBloc.validateToken(token);

            Authentication authentication =
                    jwtBloc.getUserDetailsFromToken(token)
                           .map(userDetails -> new UsernamePasswordAuthenticationToken(userDetails,
                                                                                       null,
                                                                                       userDetails.getAuthorities()))
                           .orElse(null);

            SecurityContextHolder.getContext().setAuthentication(authentication);

            logger.info("Authentication successfully");

        }

        filterChain.doFilter(request, response);

    }
}
