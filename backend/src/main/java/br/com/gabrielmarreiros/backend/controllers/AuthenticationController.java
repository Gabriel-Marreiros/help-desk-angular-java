package br.com.gabrielmarreiros.backend.controllers;

import br.com.gabrielmarreiros.backend.dto.customer.CustomerRegisterRequestDTO;
import br.com.gabrielmarreiros.backend.dto.customer.CustomerRegisterResponseDTO;
import br.com.gabrielmarreiros.backend.dto.customer.CustomerResponseDTO;
import br.com.gabrielmarreiros.backend.dto.login.LoginRequestDTO;
import br.com.gabrielmarreiros.backend.dto.login.LoginResponseDTO;
import br.com.gabrielmarreiros.backend.dto.technical.TechnicalRegisterRequestDTO;
import br.com.gabrielmarreiros.backend.dto.technical.TechnicalRegisterResponseDTO;
import br.com.gabrielmarreiros.backend.exceptions.ErrorResponse;
import br.com.gabrielmarreiros.backend.mappers.CustomerMapper;
import br.com.gabrielmarreiros.backend.mappers.TechnicalMapper;
import br.com.gabrielmarreiros.backend.models.Customer;
import br.com.gabrielmarreiros.backend.models.Technical;
import br.com.gabrielmarreiros.backend.models.User;
import br.com.gabrielmarreiros.backend.services.AuthenticationService;
import br.com.gabrielmarreiros.backend.services.CustomerService;
import br.com.gabrielmarreiros.backend.services.TechnicalService;
import br.com.gabrielmarreiros.backend.services.TokenJwtService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Authentication", description = "Recursos para autenticação")
@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final TokenJwtService tokenJwtService;
    private final CustomerMapper customerMapper;
    private final CustomerService customerService;
    private final TechnicalMapper technicalMapper;
    private final TechnicalService technicalService;

    public AuthenticationController(AuthenticationService authenticationService, TokenJwtService tokenJwtService, CustomerMapper customerMapper, CustomerService customerService, TechnicalMapper technicalMapper, TechnicalService technicalService) {
        this.authenticationService = authenticationService;
        this.customerMapper = customerMapper;
        this.customerService = customerService;
        this.technicalMapper = technicalMapper;
        this.technicalService = technicalService;
        this.tokenJwtService = tokenJwtService;
    }

    @Operation(
        summary = "Login",
        description = "Realiza o login de um usuário do sistema",
        responses = {
                @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json")),
                @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json"))
        }
    )
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginRequest){

        User authenticatedUser = this.authenticationService.login(loginRequest);

        String token = this.tokenJwtService.generateToken(authenticatedUser);

        LoginResponseDTO loginResponse = new LoginResponseDTO(token);

        return ResponseEntity.status(HttpStatus.OK).body(loginResponse);
    }

    @Operation(
            summary = "Register Customer",
            description = "Realiza o registro de um cliente no sistema",
            responses = {
                    @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json"))
            }
    )
    @PostMapping("/register/customer")
    public ResponseEntity<CustomerRegisterResponseDTO> registerCustomer(@RequestBody CustomerRegisterRequestDTO customerRegisterRequest){
        Customer customerEntity = this.customerMapper.toEntity(customerRegisterRequest);

        Customer registeredCustomer = this.customerService.saveCustomer(customerEntity);;

        String token = this.tokenJwtService.generateToken(registeredCustomer);

        CustomerRegisterResponseDTO customerRegisterResponse = this.customerMapper.toRegisterResponseDTO(registeredCustomer, token);

        return ResponseEntity.status(HttpStatus.CREATED).body(customerRegisterResponse);
    }

    @Operation(
            summary = "Register Technical",
            description = "Realiza o registro de um técnico no sistema",
            responses = {
                    @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json"))
            }
    )
    @PostMapping("/register/technical")
    public ResponseEntity<TechnicalRegisterResponseDTO> registerTechnical(@RequestBody TechnicalRegisterRequestDTO technicalRegisterRequest){
        Technical technicalEntity = this.technicalMapper.toEntity(technicalRegisterRequest);

        Technical registeredTechnical = this.technicalService.saveTechnical(technicalEntity);

        String token = this.tokenJwtService.generateToken(registeredTechnical);

        TechnicalRegisterResponseDTO technicalRegisterResponse = this.technicalMapper.toRegisterResponseDTO(registeredTechnical, token);

        return ResponseEntity.status(HttpStatus.CREATED).body(technicalRegisterResponse);
    }

    @Operation(
            summary = "Validate Token",
            description = "Realiza a validação de um token JWT",
            responses = {
                    @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json"))
            }
    )
    @PostMapping("/validate-token")
    public ResponseEntity<Boolean> validateToken(@RequestBody String token){
        boolean validity = this.authenticationService.validateToken(token);

        return ResponseEntity.status(HttpStatus.OK).body(validity);
    }

}
