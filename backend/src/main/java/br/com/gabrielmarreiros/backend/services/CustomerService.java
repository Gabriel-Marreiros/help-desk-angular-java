package br.com.gabrielmarreiros.backend.services;

import br.com.gabrielmarreiros.backend.dto.customer.CustomerFiltersDTO;
import br.com.gabrielmarreiros.backend.dto.customer.CustomerUpdateRequestDTO;
import br.com.gabrielmarreiros.backend.enums.RolesEnum;
import br.com.gabrielmarreiros.backend.exceptions.UnauthorizedException;
import br.com.gabrielmarreiros.backend.exceptions.UserAlreadyRegisteredException;
import br.com.gabrielmarreiros.backend.exceptions.UserNotFoundException;
import br.com.gabrielmarreiros.backend.models.Customer;
import br.com.gabrielmarreiros.backend.models.Role;
import br.com.gabrielmarreiros.backend.models.User;
import br.com.gabrielmarreiros.backend.repositories.CustomerRepository;
import br.com.gabrielmarreiros.backend.repositories.RoleRepository;
import org.springframework.data.domain.*;
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
        Sort sort = Sort.by(Sort.Direction.ASC, "name");
        List<Customer> customers = this.customerRepository.findAll(sort);

        return customers;
    }

    @Transactional
    public Customer saveCustomer(Customer customer) {

        boolean userAlreadyRegistered = this.userService.verifyUserAlreadyRegistered(customer.getEmail());

        if (userAlreadyRegistered) {
            throw new UserAlreadyRegisteredException();
        }

        Role role = this.roleRepository.findByTitle(RolesEnum.CUSTOMER.value()).get();
        customer.setRole(role);

        String encodedPassword = this.passwordEncoder.encode(customer.getPassword());
        customer.setPassword(encodedPassword);

        Customer savedCustumer = this.customerRepository.save(customer);

        return savedCustumer;
    }

    public Customer getCustomerById(UUID userId) {
        Customer customer = this.customerRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        return customer;
    }

    public Page<Customer> getCustomersPaginated(CustomerFiltersDTO customerFilters, PageRequest pageRequest) {
        Customer customerFilter = new Customer();
        customerFilter.setUserStatus(customerFilters.status());
        customerFilter.setName(customerFilters.search());

        ExampleMatcher filterMatcher = ExampleMatcher.matching().withMatcher("name", matcher -> matcher.contains().ignoreCase());

        Example<Customer> customerExample = Example.of(customerFilter, filterMatcher);

        Page<Customer> customersPage = this.customerRepository.findAll(customerExample, pageRequest);

        return customersPage;
    }

    @Transactional
    public Customer changeCustomerActiveStatus(UUID userId){
        boolean userExists = this.customerRepository.existsById(userId);

        if(!userExists){
            throw new UserNotFoundException();
        }

        boolean canChangeActiveStatus = this.userService.isUserHimselfOrAdmin(userId);

        if(!canChangeActiveStatus){
            throw new UnauthorizedException();
        }

        this.customerRepository.changeCustomerActiveStatus(userId);

        Customer customer = this.customerRepository.findById(userId).get();

        return customer;
    }

    @Transactional
    public Customer updateCustomer(UUID userId, CustomerUpdateRequestDTO customerUpdate) {
        boolean canUpdateCustomer = this.userService.isUserHimselfOrAdmin(userId);

        if(!canUpdateCustomer){
            throw new UnauthorizedException();
        }

        Customer customer = this.customerRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        customer.setName(customerUpdate.name());
        customer.setEmail(customerUpdate.email());
        customer.setPhoneNumber(customerUpdate.phoneNumber());
        customer.setProfilePicture(customerUpdate.profilePicture());
        customer.setCnpj(customerUpdate.cnpj());

        Customer updatedCustomer = this.customerRepository.save(customer);

        return updatedCustomer;
    }
}
