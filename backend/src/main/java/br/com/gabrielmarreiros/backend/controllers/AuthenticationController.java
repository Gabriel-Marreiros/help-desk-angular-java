package br.com.gabrielmarreiros.backend.controllers;

import br.com.gabrielmarreiros.backend.dto.customer.CustomerRegisterRequestDTO;
import br.com.gabrielmarreiros.backend.dto.customer.CustomerRegisterResponseDTO;
import br.com.gabrielmarreiros.backend.dto.login.LoginRequestDTO;
import br.com.gabrielmarreiros.backend.dto.login.LoginResponseDTO;
import br.com.gabrielmarreiros.backend.dto.technical.TechnicalRegisterRequestDTO;
import br.com.gabrielmarreiros.backend.dto.technical.TechnicalRegisterResponseDTO;
import br.com.gabrielmarreiros.backend.exceptions.InvalidTokenException;
import br.com.gabrielmarreiros.backend.mappers.CustomerMapper;
import br.com.gabrielmarreiros.backend.mappers.TechnicalMapper;
import br.com.gabrielmarreiros.backend.models.Customer;
import br.com.gabrielmarreiros.backend.models.Technical;
import br.com.gabrielmarreiros.backend.models.User;
import br.com.gabrielmarreiros.backend.services.AuthenticationService;
import br.com.gabrielmarreiros.backend.services.TokenJwtService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final TokenJwtService tokenJwtService;

    public AuthenticationController(AuthenticationService authenticationService, TokenJwtService tokenJwtService) {
        this.authenticationService = authenticationService;
        this.tokenJwtService = tokenJwtService;
    }


    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginRequest){

        User authenticatedUser = this.authenticationService.login(loginRequest);

        String token = this.tokenJwtService.generateToken(authenticatedUser);

        LoginResponseDTO loginResponse = new LoginResponseDTO(token);

        return ResponseEntity.status(HttpStatus.OK).body(loginResponse);
    }

    @PostMapping("/validate-token")
    public ResponseEntity<Boolean> validateToken(@RequestBody String token){
        boolean validity = this.authenticationService.validateToken(token);

        return ResponseEntity.status(HttpStatus.OK).body(validity);
    }

}
