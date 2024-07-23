package br.com.gabrielmarreiros.backend.repositories;

import br.com.gabrielmarreiros.backend.dto.technical.TechnicalWithTicketStatusCountDTO;
import br.com.gabrielmarreiros.backend.enums.UserStatusEnum;
import br.com.gabrielmarreiros.backend.models.Technical;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@DataJpaTest()
@ActiveProfiles("test")
class TechnicalRepositoryTest {

    @Autowired
    TechnicalRepository technicalRepository;

    @Nested
    class getAllTechnicalsWithTicketsStatusCount {

        @Test
        @DisplayName("")
        void whenRequesting_thenReturnAListOfAllTechnicalsWithTicketStatusCount(){
//            Action
            List<TechnicalWithTicketStatusCountDTO> repositoryResponse = technicalRepository.getAllTechnicalsWithTicketsStatusCount();

//            Assert
            Assertions.assertThatList(repositoryResponse)
                    .isNotNull()
                    .extracting("ticketsPending", "ticketsInProgress", "ticketsResolved")
                    .contains(
                            tuple(2L, 1L, 0L),
                            tuple(0L, 1L, 2L),
                            tuple(0L, 0L, 0L)
                    );
        }
    }

    @Nested
    class getTechniciansWithTicketsStatusCountPaginated {

        @Test
        @DisplayName("")
        void givenAPageRequest_whenRequesting_thenReturnAPageOfTechniciansWithTicketsStatusCount(){
//            Arrange
            PageRequest pageRequest = PageRequest.of(0, 3);

//            Action
            Page<TechnicalWithTicketStatusCountDTO> repositoryResponse = technicalRepository.getTechniciansWithTicketsStatusCountPaginated(pageRequest);

//            Assert
            Assertions.assertThatList(repositoryResponse.getContent())
                    .isNotNull()
                    .extracting("ticketsPending", "ticketsInProgress", "ticketsResolved")
                    .contains(
                            tuple(2L, 1L, 0L),
                            tuple(0L, 1L, 2L),
                            tuple(0L, 0L, 0L)
                    );
        }
    }

    @Nested
    class changeTechnicalActiveStatus {

        @Test
        @DisplayName("")
        void whenRequesting_thenInactiveUser() {
//          Action
            technicalRepository.changeTechnicalActiveStatus(UUID.fromString("16f2a0b5-73bd-4634-90d2-a6f051c8f0e5"));
            Technical repositoryResponse = technicalRepository.findById(UUID.fromString("351725fa-eacc-4e62-a066-65c1fe02a6f0")).orElse(null);

//          Assert
            assertThat(repositoryResponse.getUser().getUserStatus())
                    .isEqualTo(UserStatusEnum.INACTIVE.getValue());
        }

        @Test
        @DisplayName("")
        void whenRequesting_thenActiveUser() {
//          Action
            technicalRepository.changeTechnicalActiveStatus(UUID.fromString("8146338a-c0a2-43bb-8aac-a8702e884b08"));
            Technical repositoryResponse = technicalRepository.findById(UUID.fromString("416f6475-cf96-43f6-aaec-e6f654b69d2d")).orElse(null);

//          Assert
            assertThat(repositoryResponse.getUser().getUserStatus())
                    .isEqualTo(UserStatusEnum.ACTIVE.getValue());
        }
    }
}