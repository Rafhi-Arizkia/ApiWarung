package com.api.warung.auth;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashMap;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {
    private  final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    @Autowired
    public JwtAuthFilter(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;

        // Jika header Authorization kosong atau tidak diawali dengan "Bearer",
        // maka kode akan menyelesaikan proses filter dan melanjutkan ke filter selanjutnya
        // tanpa melakukan proses autentikasi.
        // Hal ini menandakan bahwa permintaan client tidak mengirimkan token JWT yang valid.
        if (authHeader == null || !authHeader.startsWith("Bearer")) {
            filterChain.doFilter(request, response);
            return;
        }
        jwt = authHeader.substring(7);
        userEmail = jwtService.extractUsername(jwt);

        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null){
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
            try {
                if (jwtService.validToken(jwt,userDetails)){
                    UsernamePasswordAuthenticationToken usernameAuthenticationToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(usernameAuthenticationToken);
                }
            }catch (ExpiredJwtException exception){
                // permintaan refresh token
                String refreshToken = request.getHeader("refreshToken");
                String requestUrl = request.getRequestURL().toString();

                if (refreshToken != null && refreshToken.equals("true") && requestUrl.contains("refreshToken")){
                    allowRefreshToken(exception,request);
                }else {
                    request.setAttribute("exception :",exception);
                }
            }
        }
    }
    private void allowRefreshToken(ExpiredJwtException exception, HttpServletRequest request){
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new
                UsernamePasswordAuthenticationToken(null,null,null);
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        request.setAttribute("claims",exception.getClaims());
    }
}
