package br.com.gabrielmarreiros.backend.repositories;

import br.com.gabrielmarreiros.backend.enums.RolesEnum;
import br.com.gabrielmarreiros.backend.enums.UserStatusEnum;
import br.com.gabrielmarreiros.backend.models.Customer;
import br.com.gabrielmarreiros.backend.models.Role;
import br.com.gabrielmarreiros.backend.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class CustomerRepositoryTest {

    @Autowired
    CustomerRepository customerRepository;

    @Nested
    class changeCustomerActiveStatus {
        @Test
        @DisplayName("")
        void whenRequesting_thenInactiveUser() {
//          Arrange
            UUID customerId = UUID.fromString("8f069839-6943-4c4a-ae8f-6581d9dcf42c");

//          Action
            customerRepository.changeCustomerActiveStatus(customerId);
            Customer repositoryResponse = customerRepository.findById(customerId).orElse(null);

//          Assert
            assertThat(repositoryResponse.getUserStatus())
                    .isEqualTo(UserStatusEnum.INACTIVE.getValue());
        }

        @Test
        @DisplayName("")
        void whenRequesting_thenActiveUser() {
//          Arrange
            UUID customerId = UUID.fromString("6c2071db-3ebe-49a1-84a8-f728c7d9f45d");

//          Action
            customerRepository.changeCustomerActiveStatus(customerId);
            Customer repositoryResponse = customerRepository.findById(customerId).orElse(null);

//          Assert
            assertThat(repositoryResponse.getUserStatus())
                    .isEqualTo(UserStatusEnum.ACTIVE.getValue());
        }
    }
}