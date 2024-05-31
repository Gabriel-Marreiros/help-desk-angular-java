package br.com.gabrielmarreiros.backend.services;

import br.com.gabrielmarreiros.backend.dto.customer.CustomerUpdateRequestDTO;
import br.com.gabrielmarreiros.backend.enums.RolesEnum;
import br.com.gabrielmarreiros.backend.exceptions.UserAlreadyRegisteredException;
import br.com.gabrielmarreiros.backend.exceptions.UserNotFoundException;
import br.com.gabrielmarreiros.backend.models.Customer;
import br.com.gabrielmarreiros.backend.models.Role;
import br.com.gabrielmarreiros.backend.models.Technical;
import br.com.gabrielmarreiros.backend.models.User;
import br.com.gabrielmarreiros.backend.repositories.CustomerRepository;
import br.com.gabrielmarreiros.backend.repositories.RoleRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final UserService userService;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    CustomerService(CustomerRepository customerRepository, UserService userService, RoleRepository roleRepository, BCryptPasswordEncoder passwordEncoder) {
        this.customerRepository = customerRepository;
        this.userService = userService;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Customer> getAllCustomers() {
        List<Customer> customers = this.customerRepository.findAll();

        return customers;
    }

    @Transactional
    public Customer saveCustomer(Customer customer) {

        boolean userAlreadyRegistered = this.userService.verifyUserAlreadyRegistered(customer.getUser().getEmail());

        if (userAlreadyRegistered) {
            throw new UserAlreadyRegisteredException();
        }

        Role role = this.roleRepository.findByTitle(RolesEnum.CUSTOMER.getValue()).get();
        customer.getUser().setRole(role);

        String encodedPassword = this.passwordEncoder.encode(customer.getUser().getPassword());
        customer.getUser().setPassword(encodedPassword);

        User savedUser = this.userService.saveUser(customer.getUser());

        customer.setUser(savedUser);

        Customer savedCustumer = this.customerRepository.save(customer);

        return savedCustumer;
    }

    public Customer getCustomerById(UUID id) {
        Customer customer = this.customerRepository.findById(id)
                                                    .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado"));

        return customer;
    }

    public Page<Customer> getCustomersPaginated(PageRequest pageRequest) {
        return this.customerRepository.findAll(pageRequest);
    }

    @Transactional
    public void changeCustomerActiveStatus(UUID id){
        int rows = this.customerRepository.changeCustomerActiveStatus(id);
    }

    @Transactional
    public Customer updateCustomer(UUID id, CustomerUpdateRequestDTO customerUpdate) {
        Customer customer = this.customerRepository.findById(id).orElseThrow(UserNotFoundException::new);

        customer.getUser().setName(customerUpdate.name());
        customer.getUser().setEmail(customerUpdate.email());
        customer.getUser().setPhoneNumber(customerUpdate.phoneNumber());
        customer.getUser().setProfilePicture(customerUpdate.profilePicture());
        customer.setCnpj(customerUpdate.cnpj());

        Customer updatedCustomer = this.customerRepository.save(customer);

        return updatedCustomer;
    }
}
