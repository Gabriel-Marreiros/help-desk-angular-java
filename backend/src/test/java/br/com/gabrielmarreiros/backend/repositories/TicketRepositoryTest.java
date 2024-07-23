package br.com.gabrielmarreiros.backend.repositories;

import br.com.gabrielmarreiros.backend.enums.TicketStatusEnum;
import br.com.gabrielmarreiros.backend.models.Ticket;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class TicketRepositoryTest {

    @Autowired
    TicketRepository ticketRepository;

    @Nested
    class updateTicketStatus {

        @Test
        @DisplayName("")
        void givenATicketId_whenRequesting_thenUpdateAndReturnTicket(){
//            Arrange
            UUID ticketId = UUID.fromString("733aefa2-22df-48d5-a16e-9242cc4156b9");

//            Action
            int repositoryResponse = ticketRepository.updateTicketStatus(ticketId, TicketStatusEnum.IN_PROGRESS.getValue());
            Ticket updatedTicket = ticketRepository.findById(ticketId).get();

//            Assert
            assertThat(repositoryResponse)
                    .isEqualTo(1);

            assertThat(updatedTicket.getTicketStatus())
                    .isEqualTo(TicketStatusEnum.IN_PROGRESS.getValue());
        }
    }

    @Nested
    class getAllTicketsStatusSummary {

        @Test
        @DisplayName("")
        void whenRequesting_thenReturnTicketsStatusSummary(){
//            Action
            var repositoryResponse = ticketRepository.getAllTicketsStatusSummary();

//            Assert
            assertThat(repositoryResponse)
                    .isNotNull()
                    .hasNoNullFieldsOrProperties()
                    .hasFieldOrPropertyWithValue("pending", 2L)
                    .hasFieldOrPropertyWithValue("inProgress", 2L)
                    .hasFieldOrPropertyWithValue("resolved", 2L);
        }
    }
}