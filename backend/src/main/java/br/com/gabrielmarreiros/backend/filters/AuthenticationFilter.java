package br.com.gabrielmarreiros.backend.filters;

import br.com.gabrielmarreiros.backend.exceptions.InvalidTokenException;
import br.com.gabrielmarreiros.backend.exceptions.UserNotFoundException;
import br.com.gabrielmarreiros.backend.services.TokenJwtService;
import br.com.gabrielmarreiros.backend.services.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
public class AuthenticationFilter extends OncePerRequestFilter {

    private final TokenJwtService tokenJwtService;
    private final UserService userService;

    public AuthenticationFilter(TokenJwtService tokenJwtService, UserService userService) {
        this.tokenJwtService = tokenJwtService;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = this.getTokenFromHeader(request);

        if (token != null){
            try{
                String subject = this.tokenJwtService.validateToken(token);
                UserDetails userDetails = this.userService.loadUserByUsername(subject);
                Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            catch (InvalidTokenException | UserNotFoundException e){
                SecurityContextHolder.clearContext();
            }
        }

        filterChain.doFilter(request, response);
    }

    private String getTokenFromHeader(HttpServletRequest request){
        Optional<String> tokenHeader = Optional.ofNullable(request.getHeader("Authorization"));

        if(tokenHeader.isEmpty()){
            return null;
        }

        return tokenHeader.get().replace("Bearer ", "");
    }
}
