package com.teach.gram.config.gram;

import com.teach.gram.model.User;
import com.teach.gram.repository.UserRepository;
import com.teach.gram.service.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    public SecurityFilter(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String token = getToken(request);

        if (token != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            String username = tokenService.validateToken(token);

            Optional<User> userOptional = userRepository.findByUsername(username);

            if (userOptional.isEmpty())
                throw new RuntimeException("User not found");

            User user = userOptional.get();

            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities()
            );

            SecurityContextHolder.getContext().setAuthentication(authToken);
        }

        filterChain.doFilter(request, response);

    }

    private String getToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");

        if (token != null && token.startsWith("Bearer "))
            return token.substring(7);

        return null;
    }
}
