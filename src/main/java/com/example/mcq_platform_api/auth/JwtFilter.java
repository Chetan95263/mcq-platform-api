package com.example.mcq_platform_api.auth;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.mcq_platform_api.service.CustomUserDetailService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter {
    
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired 
    private CustomUserDetailService userDetailService;
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
             // Skip JWT check for public endpoints
            String path = request.getServletPath();
            if (path.startsWith("/auth/")) {
                filterChain.doFilter(request, response);
                return;
            }

            String header = request.getHeader("Token");
            

            if(header == null || !header.startsWith("Bearer ") ){
                filterChain.doFilter(request, response);
                return;
            }
            String token = header.substring(7);
            String username = jwtUtil.extractUsername(token);

                if(username != null && SecurityContextHolder.getContext().getAuthentication() == null){
                    // Validate token first
                    if(jwtUtil.validateToken(token)) {
                        UserDetails userDetails = userDetailService.loadUserByUsername(username);   
                        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                    );
                    
                    // Set authentication in context
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
                }
            filterChain.doFilter(request, response);

    }

}
