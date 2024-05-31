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
                customerEntity.getUser().getId(),
                customerEntity.getUser().getName(),
                customerEntity.getUser().getEmail(),
                customerEntity.getUser().getPhoneNumber(),
                customerEntity.getCnpj(),
                customerEntity.getUser().getProfilePicture(),
                customerEntity.getUser().getRole(),
                customerEntity.getUser().getUserStatus()
        );
    }

    public Customer toEntity(CustomerRegisterRequestDTO customerRegisterRequest){
        User user = new User();
        user.setName(customerRegisterRequest.name());
        user.setEmail(customerRegisterRequest.email());
        user.setPassword(customerRegisterRequest.password());
        user.setPhoneNumber(customerRegisterRequest.phoneNumber());
        user.setProfilePicture(customerRegisterRequest.profilePicture());
        user.setUserStatus(UserStatusEnum.ACTIVE.getValue());

        Customer customerEntity = new Customer();
        customerEntity.setUser(user);
        customerEntity.setCnpj(customerRegisterRequest.cnpj());

        return customerEntity;
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
