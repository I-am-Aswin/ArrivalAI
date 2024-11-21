package org.broz.arrivalai.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.broz.arrivalai.services.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private JwtService jwtService;
    private UserDetailsService userDetailsService;

    @Autowired
    public JwtFilter(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        if ( authHeader != null && authHeader.startsWith("Bearer ") ){
            String jwt = authHeader.substring(7);
            String uname = jwtService.extractUsername( jwt );

            if( uname != null && SecurityContextHolder.getContext().getAuthentication() == null ) {
                UserDetails user = userDetailsService.loadUserByUsername( uname );

                if( jwtService.validate( jwt, user ) ) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                    authToken.setDetails( new WebAuthenticationDetailsSource().buildDetails( request ) );
                    SecurityContextHolder.getContext().setAuthentication( authToken);
                }
            }
        }
        filterChain.doFilter(request, response);
    }
}
