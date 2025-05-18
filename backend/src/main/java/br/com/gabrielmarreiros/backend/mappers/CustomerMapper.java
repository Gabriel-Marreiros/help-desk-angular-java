package br.com.gabrielmarreiros.backend.mappers;

import br.com.gabrielmarreiros.backend.dto.customer.CustomerRegisterRequestDTO;
import br.com.gabrielmarreiros.backend.dto.customer.CustomerRegisterResponseDTO;
import br.com.gabrielmarreiros.backend.dto.customer.CustomerResponseDTO;
import br.com.gabrielmarreiros.backend.enums.UserStatusEnum;
import br.com.gabrielmarreiros.backend.models.Customer;
import br.com.gabrielmarreiros.backend.models.User;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CustomerMapper {

    public CustomerRegisterResponseDTO toRegisterResponseDTO(Customer customerEntity, String token){
        CustomerResponseDTO customerResponse = this.toResponseDTO(customerEntity);

        return new CustomerRegisterResponseDTO(customerResponse, token);
    }

    public CustomerResponseDTO toResponseDTO(Customer customerEntity){
        return new CustomerResponseDTO(
                customerEntity.getId(),
                customerEntity.getName(),
                customerEntity.getEmail(),
                customerEntity.getPhoneNumber(),
                customerEntity.getCnpj(),
                customerEntity.getProfilePicture(),
                customerEntity.getRole(),
                customerEntity.getUserStatus()
        );
    }

    public Customer toEntity(CustomerRegisterRequestDTO customerRegisterRequest){
        Customer customer = new Customer();
        customer.setName(customerRegisterRequest.name());
        customer.setEmail(customerRegisterRequest.email());
        customer.setCnpj(customerRegisterRequest.cnpj());
        customer.setPassword(customerRegisterRequest.password());
        customer.setPhoneNumber(customerRegisterRequest.phoneNumber());
        customer.setProfilePicture(customerRegisterRequest.profilePicture());
        customer.setUserStatus(UserStatusEnum.ACTIVE.getValue());

        return customer;
    }

    public List<CustomerResponseDTO> toResponseListDTO(List<Customer> customersEntityList){
        List<CustomerResponseDTO> customerResponseList = customersEntityList.stream()
                                                                            .map(this::toResponseDTO)
                                                                            .toList();

        return customerResponseList;
    }

    public Page<CustomerResponseDTO> toResponsePageDTO(Page<Customer> customersEntityPage) {
        return customersEntityPage.map(this::toResponseDTO);
    }
}
