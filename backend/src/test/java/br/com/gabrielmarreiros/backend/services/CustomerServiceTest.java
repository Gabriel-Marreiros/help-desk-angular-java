package br.com.gabrielmarreiros.backend.services;

import br.com.gabrielmarreiros.backend.dto.customer.CustomerUpdateRequestDTO;
import br.com.gabrielmarreiros.backend.exceptions.UserAlreadyRegisteredException;
import br.com.gabrielmarreiros.backend.exceptions.UserNotFoundException;
import br.com.gabrielmarreiros.backend.models.Customer;
import br.com.gabrielmarreiros.backend.models.User;
import br.com.gabrielmarreiros.backend.repositories.CustomerRepository;
import br.com.gabrielmarreiros.backend.repositories.RoleRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private UserService userService;
    @InjectMocks
    private CustomerService customerService;

    @Nested
    class getAllCustomers {

        @Test
        void whenRequesting_thenReturnListOfCustomers() {
//          Arrange
            Customer customerMock = Mockito.mock(Customer.class);
            List<Customer> customerListMock = new ArrayList<>();
            customerListMock.add(customerMock);

            Mockito.when(customerRepository.findAll()).thenReturn(customerListMock);

//          Action
            List<Customer> serviceResponse = customerService.getAllCustomers();

//          Assert
            assertThat(serviceResponse)
                    .isNotEmpty()
                    .isEqualTo(customerListMock);
        }

    }

    @Nested
    class saveCustomer {

        @Test
        void givenAUserThatAlreadyExists_whenRequesting_thenThrowsUserAlreadyRegisteredException(){
//            Arrange
            User userMock = Mockito.mock(User.class);
            Customer customerMock = Mockito.mock(Customer.class);
            Mockito.when(customerMock.getUser()).thenReturn(userMock);

            Mockito.when(userService.verifyUserAlreadyRegistered(Mockito.any())).thenReturn(true);

//            Assert
            assertThatException()
                    .isThrownBy(() -> customerService.saveCustomer(customerMock))
                    .isInstanceOf(UserAlreadyRegisteredException.class);

        }
    }

    @Nested
    class getCustomerById {
        @Test
        void givenTheValidId_whenRequesting_theReturnCustomer() {
//          Arrange
            Customer customerMock = Mockito.mock(Customer.class);
            UUID customerId = UUID.randomUUID();
            customerMock.setId(customerId);

            Mockito.when(customerRepository.findById(customerId)).thenReturn(Optional.of(customerMock));

//          Action
            Customer serviceResponse = customerService.getCustomerById(customerId);

//          Assert
            assertThat(serviceResponse)
                    .isEqualTo(customerMock);
        }

        @Test
        void givenTheInvalidId_whenRequesting_theThrowsUserNotFoundException() {

//          Assert
            assertThatException()
                    .isThrownBy(() -> customerService.getCustomerById(UUID.randomUUID()))
                    .isInstanceOf(UserNotFoundException.class);

        }
    }

    @Nested
    class getCustomersPaginated {

        @Test
        void whenRequesting_thenReturnAListOfCustomersPaginated(){
//            Arrange
            PageRequest pageRequest = PageRequest.of(0, 1);
            Customer customerMock = Mockito.mock(Customer.class);
            Page<Customer> customersPage = new PageImpl<>(List.of(customerMock), pageRequest, 1);

            Mockito.when(customerRepository.findAll(pageRequest)).thenReturn(customersPage);

//            Action
            Page<Customer> methodResponse = customerService.getCustomersPaginated(pageRequest);

//            Assert
            assertThat(methodResponse)
                .isEqualTo(customersPage);
        }

    }

    @Nested
    class updateCustomer {

        @Test
        void whenRequesting_thenUpdateCustomer() {
//            Arrange
            UUID customerId = UUID.randomUUID();
            User userMock = Mockito.mock(User.class);
            CustomerUpdateRequestDTO customerUpdateDTOMock = Mockito.mock(CustomerUpdateRequestDTO.class);
            Customer customerMock = Mockito.mock(Customer.class);

            Mockito.when(customerMock.getUser()).thenReturn(userMock);

            Mockito.when(customerRepository.findById(customerId)).thenReturn(Optional.of(customerMock));
            Mockito.when(customerRepository.save(customerMock)).thenReturn(customerMock);

//            Action
            Customer methodResponse = customerService.updateCustomer(customerId, customerUpdateDTOMock);

//            Assert
            assertThat(methodResponse)
                    .isInstanceOf(Customer.class);
        }

        @Test
        void givenAUnknowId_whenRequesting_thenThrowsUserNotFoundException() {
//            Arrange
            UUID customerId = UUID.randomUUID();
            CustomerUpdateRequestDTO customerUpdateDTOMock = Mockito.mock(CustomerUpdateRequestDTO.class);

            Mockito.when(customerRepository.findById(customerId)).thenReturn(Optional.ofNullable(null));

//            Assert
            assertThatException()
                    .isThrownBy(() -> customerService.updateCustomer(customerId, customerUpdateDTOMock))
                    .isInstanceOf(UserNotFoundException.class);
        }
    }
}