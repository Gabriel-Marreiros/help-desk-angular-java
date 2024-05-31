package br.com.gabrielmarreiros.backend.services;

import br.com.gabrielmarreiros.backend.dto.login.LoginRequestDTO;
import br.com.gabrielmarreiros.backend.exceptions.InvalidLoginException;
import br.com.gabrielmarreiros.backend.exceptions.InvalidTokenException;
import br.com.gabrielmarreiros.backend.models.User;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final TokenJwtService tokenJwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationService(TokenJwtService tokenJwtService, AuthenticationManager authenticationManager) {
        this.tokenJwtService = tokenJwtService;
        this.authenticationManager = authenticationManager;
    }

    public User login(LoginRequestDTO loginRequest){
        UsernamePasswordAuthenticationToken usernamePasswordAuthentication = new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.password());

        Authentication authentication;

        try{
            authentication = authenticationManager.authenticate(usernamePasswordAuthentication);
        }
        catch(AuthenticationException e){
            throw new InvalidLoginException();
        }

        User authenticatedUser = (User) authentication.getPrincipal();

        return authenticatedUser;
    }

    public boolean validateToken(String token){
        try{
            this.tokenJwtService.validateToken(token);
            return true;
        }
        catch (InvalidTokenException e){
            return false;
        }
    }

}
