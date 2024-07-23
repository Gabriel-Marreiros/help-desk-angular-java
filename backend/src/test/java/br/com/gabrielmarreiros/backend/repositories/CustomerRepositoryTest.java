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
//          Action
            customerRepository.changeCustomerActiveStatus(UUID.fromString("8f069839-6943-4c4a-ae8f-6581d9dcf42c"));
            Customer repositoryResponse = customerRepository.findById(UUID.fromString("1ec17e60-48b6-457e-8597-54e20c806ef9")).orElse(null);

//          Assert
            assertThat(repositoryResponse.getUser().getUserStatus())
                    .isEqualTo(UserStatusEnum.INACTIVE.getValue());
        }

        @Test
        @DisplayName("")
        void whenRequesting_thenActiveUser() {
//          Action
            customerRepository.changeCustomerActiveStatus(UUID.fromString("6c2071db-3ebe-49a1-84a8-f728c7d9f45d"));
            Customer repositoryResponse = customerRepository.findById(UUID.fromString("42141baf-d7a1-425e-84b3-e6bd3c5e3e93")).orElse(null);

//          Assert
            assertThat(repositoryResponse.getUser().getUserStatus())
                    .isEqualTo(UserStatusEnum.ACTIVE.getValue());
        }
    }
}