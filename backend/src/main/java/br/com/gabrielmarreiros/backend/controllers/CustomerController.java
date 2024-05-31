package br.com.gabrielmarreiros.backend.controllers;

import br.com.gabrielmarreiros.backend.dto.customer.CustomerRegisterRequestDTO;
import br.com.gabrielmarreiros.backend.dto.customer.CustomerRegisterResponseDTO;
import br.com.gabrielmarreiros.backend.dto.customer.CustomerResponseDTO;
import br.com.gabrielmarreiros.backend.dto.customer.CustomerUpdateRequestDTO;
import br.com.gabrielmarreiros.backend.dto.technical.TechnicalResponseDTO;
import br.com.gabrielmarreiros.backend.dto.technical.TechnicalUpdateRequestDTO;
import br.com.gabrielmarreiros.backend.mappers.CustomerMapper;
import br.com.gabrielmarreiros.backend.models.Customer;
import br.com.gabrielmarreiros.backend.models.Technical;
import br.com.gabrielmarreiros.backend.services.CustomerService;
import br.com.gabrielmarreiros.backend.services.TokenJwtService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerService customerService;
    private final CustomerMapper customerMapper;
    private final TokenJwtService tokenJwtService;

    CustomerController(CustomerService customerService, CustomerMapper customerMapper, TokenJwtService tokenJwtService){
        this.customerService = customerService;
        this.customerMapper = customerMapper;
        this.tokenJwtService = tokenJwtService;
    }

    @GetMapping
    public ResponseEntity<List<CustomerResponseDTO>> getAllCustomers(){
        List<Customer> customersEntityList = this.customerService.getAllCustomers();

        List<CustomerResponseDTO> customersResponseList = this.customerMapper.toResponseListDTO(customersEntityList);

        return ResponseEntity.status(HttpStatus.OK).body(customersResponseList);
    }

    @GetMapping("/paginated")
    public ResponseEntity<Page<CustomerResponseDTO>> getCustomersPaginated(@RequestParam int page, @RequestParam int size){
        PageRequest pageRequest = PageRequest.of(page, size);

        Page<Customer> customersPage = this.customerService.getCustomersPaginated(pageRequest);

        Page<CustomerResponseDTO> customersResponsePage = this.customerMapper.toResponsePageDTO(customersPage);

        return ResponseEntity.status(HttpStatus.OK).body(customersResponsePage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponseDTO> getCustomerById(@PathVariable("id") UUID id){
        Customer customerEntity = this.customerService.getCustomerById(id);

        CustomerResponseDTO customerResponse = this.customerMapper.toResponseDTO(customerEntity);

        return ResponseEntity.status(HttpStatus.OK).body(customerResponse);
    }

    @PostMapping
    public ResponseEntity<CustomerRegisterResponseDTO> registerCustomer(@RequestBody CustomerRegisterRequestDTO customerRegisterRequest){
        Customer customerEntity = this.customerMapper.toEntity(customerRegisterRequest);

        Customer registeredCustomer = this.customerService.saveCustomer(customerEntity);;

        String token = this.tokenJwtService.generateToken(registeredCustomer.getUser());

        CustomerRegisterResponseDTO customerRegisterResponse = this.customerMapper.toRegisterResponseDTO(registeredCustomer, token);

        return ResponseEntity.status(HttpStatus.CREATED).body(customerRegisterResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerResponseDTO> updateCustomer(@PathVariable("id") UUID id, @RequestBody CustomerUpdateRequestDTO customerUpdate){
        Customer updatedCustomer = this.customerService.updateCustomer(id, customerUpdate);

        CustomerResponseDTO updatedCustomerResponse = this.customerMapper.toResponseDTO(updatedCustomer);

        return ResponseEntity.status(HttpStatus.OK).body(updatedCustomerResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity changeCustomerActiveStatus(@PathVariable("id") UUID id){

        this.customerService.changeCustomerActiveStatus(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
