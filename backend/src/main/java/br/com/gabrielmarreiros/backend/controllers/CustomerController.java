package br.com.gabrielmarreiros.backend.controllers;

import br.com.gabrielmarreiros.backend.dto.customer.*;
import br.com.gabrielmarreiros.backend.dto.technical.TechnicalResponseDTO;
import br.com.gabrielmarreiros.backend.dto.technical.TechnicalUpdateRequestDTO;
import br.com.gabrielmarreiros.backend.enums.UserStatusEnum;
import br.com.gabrielmarreiros.backend.exceptions.ErrorResponse;
import br.com.gabrielmarreiros.backend.mappers.CustomerMapper;
import br.com.gabrielmarreiros.backend.models.Customer;
import br.com.gabrielmarreiros.backend.models.Technical;
import br.com.gabrielmarreiros.backend.services.CustomerService;
import br.com.gabrielmarreiros.backend.services.TokenJwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Customers", description = "Recursos para manipulação de clientes")
@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerService customerService;
    private final CustomerMapper customerMapper;

    CustomerController(CustomerService customerService, CustomerMapper customerMapper){
        this.customerService = customerService;
        this.customerMapper = customerMapper;
    }

    @Operation(
            summary = "Get All Customers",
            description = "Retorna todos os clientes registrados no sistema",
            responses = {@ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json"))}
    )
    @GetMapping
    public ResponseEntity<List<CustomerResponseDTO>> getAllCustomers(){
        List<Customer> customersEntityList = this.customerService.getAllCustomers();

        List<CustomerResponseDTO> customersResponseList = this.customerMapper.toResponseListDTO(customersEntityList);

        return ResponseEntity.status(HttpStatus.OK).body(customersResponseList);
    }

    @Operation(
            summary = "Get All Customers Paginated",
            description = "Retorna todos os clientes registrados no sistema de forma páginada",
            responses = {@ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json"))}
    )
    @GetMapping("/paginated")
    public ResponseEntity<Page<CustomerResponseDTO>> getCustomersPaginated(
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String search){

        PageRequest pageRequest = PageRequest.of(page, size);

        CustomerFiltersDTO customerFilter = new CustomerFiltersDTO(
                status,
                search
        );

        Page<Customer> customersPage = this.customerService.getCustomersPaginated(customerFilter, pageRequest);

        Page<CustomerResponseDTO> customersResponsePage = this.customerMapper.toResponsePageDTO(customersPage);

        return ResponseEntity.status(HttpStatus.OK).body(customersResponsePage);
    }

    @Operation(
            summary = "Get Customer By ID",
            description = "Retorna as informações do cliente do ID informado",
            responses = {@ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json"))}
    )
    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponseDTO> getCustomerById(@PathVariable("id") UUID id){
        Customer customerEntity = this.customerService.getCustomerById(id);

        CustomerResponseDTO customerResponse = this.customerMapper.toResponseDTO(customerEntity);

        return ResponseEntity.status(HttpStatus.OK).body(customerResponse);
    }

    @Operation(
            summary = "Update Customer",
            description = "Realiza atualização do cliente",
            responses = {
                    @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json"))
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<CustomerResponseDTO> updateCustomer(@PathVariable("id") UUID id, @RequestBody CustomerUpdateRequestDTO customerUpdate){
        Customer updatedCustomer = this.customerService.updateCustomer(id, customerUpdate);

        CustomerResponseDTO updatedCustomerResponse = this.customerMapper.toResponseDTO(updatedCustomer);

        return ResponseEntity.status(HttpStatus.OK).body(updatedCustomerResponse);
    }

    @Operation(
            summary = "Change Customer Status",
            description = "Realiza modificação do status do cliente informado",
            responses = {@ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json"))}
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<CustomerResponseDTO> changeCustomerActiveStatus(@PathVariable("id") UUID id){
        Customer customer = this.customerService.changeCustomerActiveStatus(id);

        CustomerResponseDTO customerResponseDTO = this.customerMapper.toResponseDTO(customer);

        return ResponseEntity.status(HttpStatus.OK).body(customerResponseDTO);
    }
}
