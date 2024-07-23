package br.com.gabrielmarreiros.backend.services;

import br.com.gabrielmarreiros.backend.dto.ticket.AllTicketsStatusSummaryDTO;
import br.com.gabrielmarreiros.backend.dto.ticket.TicketUpdateDTO;
import br.com.gabrielmarreiros.backend.enums.TicketStatusEnum;
import br.com.gabrielmarreiros.backend.exceptions.InvalidTicketStatusException;
import br.com.gabrielmarreiros.backend.exceptions.TicketNotFoundException;
import br.com.gabrielmarreiros.backend.models.Customer;
import br.com.gabrielmarreiros.backend.models.Technical;
import br.com.gabrielmarreiros.backend.models.Ticket;
import br.com.gabrielmarreiros.backend.repositories.TicketRepository;
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

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TicketServiceTest {

    @Mock
    private TicketRepository ticketRepository;
    @Mock
    private TechnicalService technicalService;
    @Mock
    private CustomerService customerService;
    @InjectMocks
    private TicketService ticketService;

    @Nested
    class getAllTickets {

        @Test
        void whenRequesting_thenReturnAListWithAllTickets(){
//            Arrange
            List<Ticket> ticketsListMock = List.of(Mockito.mock(Ticket.class));

            Mockito.when(ticketRepository.findAll()).thenReturn(ticketsListMock);

//            Action
            List<Ticket> methodResponse = ticketService.getAllTickets();

//            Assert
            assertThatList(methodResponse)
                .isNotNull()
                .hasOnlyElementsOfType(Ticket.class);

        }
    }

    @Nested
    class getTicketById {

        @Test
        void givenAValidId_whenRequesting_thenReturnATicket(){
//            Arrange
            UUID ticketId = UUID.randomUUID();
            Ticket ticketMock = Mockito.mock(Ticket.class);

            Mockito.when(ticketRepository.findById(ticketId)).thenReturn(Optional.of(ticketMock));

//            Action
            Ticket methodResponse = ticketService.getTicketById(ticketId);

//            Assert
            assertThat(methodResponse)
                .isInstanceOf(Ticket.class)
                .isEqualTo(ticketMock);

        }

        @Test
        void givenAInvalidId_whenRequesting_thenThrowsTicketNotFoundException(){
//            Arrange
            UUID ticketId = UUID.randomUUID();

            Mockito.when(ticketRepository.findById(ticketId)).thenReturn(Optional.ofNullable(null));

//            Assert
            assertThatException()
                .isThrownBy(() -> ticketService.getTicketById(ticketId))
                .isInstanceOf(TicketNotFoundException.class);
        }
    }

    @Nested
    class saveTicket {

        @Test
        void givenATicket_whenRequesting_thenSaveAndReturnTicket(){
//            Arrange
            Ticket ticketMock = Mockito.mock(Ticket.class);
            Technical technicalMock = Mockito.mock(Technical.class);
            Customer customerMock = Mockito.mock(Customer.class);

            Mockito.when(ticketMock.getTechnical()).thenReturn(technicalMock);
            Mockito.when(ticketMock.getCustomer()).thenReturn(customerMock);

            Mockito.when(technicalService.getTechnicalById(Mockito.any())).thenReturn(technicalMock);
            Mockito.when(customerService.getCustomerById(Mockito.any())).thenReturn(customerMock);
            Mockito.when(ticketRepository.saveAndFlush(Mockito.any())).thenReturn(ticketMock);

//            Action
            Ticket methodResponse = ticketService.saveTicket(ticketMock);

//            Assert
            assertThat(methodResponse)
                .isInstanceOf(Ticket.class);

        }
    }

    @Nested
    class updateStatus {

        @Test
        void givenATicketWithInvalidStatus_whenRequesting_thenThrowsInvalidTicketStatusException(){
//            Arrange
            UUID ticketId = UUID.randomUUID();
            String invalidStatus = "Invalid Status";

//            Assert
            assertThatException()
                    .isThrownBy(() -> ticketService.updateStatus(ticketId, invalidStatus))
                    .isInstanceOf(InvalidTicketStatusException.class);
        }

        @Test
        void givenATicketThatDoesNotExist_whenRequesting_thenThrowsTicketNotFoundException(){
//            Arrange
            UUID ticketId = UUID.randomUUID();
            String validTicketStatus = TicketStatusEnum.IN_PROGRESS.getValue();

            Mockito.when(ticketRepository.updateTicketStatus(ticketId, validTicketStatus)).thenReturn(0);

//            Assert
            assertThatException()
                    .isThrownBy(() -> ticketService.updateStatus(ticketId, validTicketStatus))
                    .isInstanceOf(TicketNotFoundException.class);
        }
    }

    @Nested
    class getAllTicketsStatusSummary {

        @Test
        void whenRequesting_thenReturnAllTicketsStatusSummaryDTO(){
//            Arrange
            AllTicketsStatusSummaryDTO allTicketsStatusSummaryDTOMock = Mockito.mock(AllTicketsStatusSummaryDTO.class);
            Mockito.when(ticketRepository.getAllTicketsStatusSummary()).thenReturn(allTicketsStatusSummaryDTOMock);

//            Action
            AllTicketsStatusSummaryDTO methodResponse = ticketService.getAllTicketsStatusSummary();

//            Assert
            assertThat(methodResponse)
                .isInstanceOf(AllTicketsStatusSummaryDTO.class);

        }
    }

    @Nested
    class getTicketsPaginated {

        @Test
        void whenRequesting_thenReturnTicketsPage(){
//            Arrange
            Ticket ticketMock = Mockito.mock(Ticket.class);
            PageRequest pageRequest = PageRequest.of(0, 1);
            Page<Ticket> ticketPage = new PageImpl<>(List.of(ticketMock), pageRequest, 1);
            Mockito.when(ticketRepository.findAll(pageRequest)).thenReturn(ticketPage);

//            Action
            Page<Ticket> methodResponse = ticketService.getTicketsPaginated(pageRequest);

//            Assert
            assertThat(methodResponse)
                .isEqualTo(ticketPage);

        }
    }

    @Nested
    class getTicketsByStatusPaginated {

        @Test
        void givenAValidTicketStatusFilter_whenRequesting_thenReturnAPageOfTicketsFilteredByStatus(){
//            Arrange
            Ticket ticketMock = Mockito.mock(Ticket.class);
            String ticketStatusFilter = TicketStatusEnum.IN_PROGRESS.getValue();
            PageRequest pageRequest = PageRequest.of(0, 1);
            Page<Ticket> ticketPage = new PageImpl<>(List.of(ticketMock), pageRequest, 1);
            Mockito.when(ticketRepository.findByTicketStatus(ticketStatusFilter, pageRequest)).thenReturn(ticketPage);

//            Action
            Page<Ticket> methodResponse = ticketService.getTicketsByStatusPaginated(pageRequest, ticketStatusFilter);

//            Assert
            assertThat(methodResponse)
                    .isEqualTo(ticketPage);

        }

        @Test
        void givenAInvalidTicketStatusFilter_whenRequesting_thenThrowsInvalidTicketStatusException(){
//            Arrange
            String invalidTicketStatusFilter = "Invalid Status";
            PageRequest pageRequestMock = Mockito.mock(PageRequest.class);


//            Assert
            assertThatException()
                    .isThrownBy(() -> ticketService.getTicketsByStatusPaginated(pageRequestMock, invalidTicketStatusFilter))
                    .isInstanceOf(InvalidTicketStatusException.class);

        }
    }

    @Nested
    class updateTicket {

        @Test
        void whenRequesting_updateTicketAndReturn(){
//            Arrange
            UUID ticketId = UUID.randomUUID();
            TicketUpdateDTO ticketUpdateDTOMock = Mockito.mock(TicketUpdateDTO.class);
            Ticket ticketMock = Mockito.mock(Ticket.class);

            Mockito.doNothing().when(ticketMock).generateSearchTerm();
            Mockito.when(ticketRepository.findById(ticketId)).thenReturn(Optional.of(ticketMock));
            Mockito.when(ticketRepository.save(ticketMock)).thenReturn(ticketMock);

//            Action
            Ticket methodResponse = ticketService.updateTicket(ticketId, ticketUpdateDTOMock);

//            Assert
            assertThat(methodResponse)
                .isInstanceOf(Ticket.class);
        }
    }

    @Nested
    class getTicketsBySearchTermPaginated {

        @Test
        void givenASearchTerm_whenRequesting_thenReturnAPageOfTicketsFiltered(){
//            Arrange
            Ticket ticketMock = Mockito.mock(Ticket.class);
            String searchTerm = "Search Term";
            PageRequest pageRequest = PageRequest.of(0, 1);
            Page<Ticket> ticketPage = new PageImpl<>(List.of(ticketMock), pageRequest, 1);
            Mockito.when(ticketRepository.findBySearchTermIgnoreCaseContaining(searchTerm, pageRequest)).thenReturn(ticketPage);

//            Action
            Page<Ticket> methodResponse = ticketService.getTicketsBySearchTermPaginated(pageRequest, searchTerm);

//            Assert
            assertThat(methodResponse)
                    .isEqualTo(ticketPage);

        }
    }
}