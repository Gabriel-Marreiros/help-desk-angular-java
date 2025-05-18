package br.com.gabrielmarreiros.backend.services;

import br.com.gabrielmarreiros.backend.dto.ticket.AllTicketsStatusSummaryDTO;
import br.com.gabrielmarreiros.backend.dto.ticket.TicketFiltersDTO;
import br.com.gabrielmarreiros.backend.dto.ticket.TicketUpdateDTO;
import br.com.gabrielmarreiros.backend.enums.RolesEnum;
import br.com.gabrielmarreiros.backend.enums.TicketStatusEnum;
import br.com.gabrielmarreiros.backend.exceptions.InvalidTicketStatusException;
import br.com.gabrielmarreiros.backend.exceptions.TicketNotFoundException;
import br.com.gabrielmarreiros.backend.models.*;
import br.com.gabrielmarreiros.backend.repositories.TicketRepository;
import br.com.gabrielmarreiros.backend.testConfigs.SpringSecurityTestConfig;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithSecurityContext;
import org.springframework.security.test.context.support.WithUserDetails;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TicketServiceTest {

    @Mock
    private TicketRepository ticketRepository;
    @Mock
    private CustomerService customerService;
    @InjectMocks
    private TicketService ticketService;

    @BeforeEach
    public void beforeEach(){
        User user = new User();
        user.setRole(new Role(RolesEnum.ADMIN.value()));

        Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @AfterEach
    public void afterEach(){
        SecurityContextHolder.clearContext();
    }

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
            Customer customerMock = Mockito.mock(Customer.class);

            Mockito.when(ticketMock.getCustomer()).thenReturn(customerMock);

            Mockito.when(customerService.getCustomerById(Mockito.any())).thenReturn(customerMock);
            Mockito.when(ticketRepository.save(Mockito.any())).thenReturn(ticketMock);

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
            UUID ticketId = UUID.fromString("733aefa2-22df-48d5-a16e-9242cc4156b9");

            Ticket ticketMock = Mockito.mock(Ticket.class);
            Mockito.when(ticketMock.getTicketStatus()).thenReturn(TicketStatusEnum.NEW_TICKET.getValue());

            String invalidStatus = "Invalid Status";

            Mockito.when(ticketRepository.findById(ticketId)).thenReturn(Optional.of(ticketMock));

//            Assert
            assertThatException()
                    .isThrownBy(() -> ticketService.updateTicketStatus(ticketId, invalidStatus))
                    .isInstanceOf(InvalidTicketStatusException.class);
        }

        @Test
        void givenATicketThatDoesNotExist_whenRequesting_thenThrowsTicketNotFoundException(){
//            Arrange
            UUID ticketId = UUID.randomUUID();
            String validTicketStatus = TicketStatusEnum.IN_PROGRESS.getValue();

            Mockito.when(ticketRepository.findById(ticketId)).thenReturn(Optional.empty());

//            Assert
            assertThatException()
                    .isThrownBy(() -> ticketService.updateTicketStatus(ticketId, validTicketStatus))
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
    class getTicketsWithFiltersPaginated {

        @Test
        void givenAValidTicketStatusFilter_whenRequesting_thenReturnAPageOfTicketsFiltered(){
//            Arrange
            TicketFiltersDTO ticketFilters = Mockito.mock(TicketFiltersDTO.class);
            Ticket ticketMock = Mockito.mock(Ticket.class);
            PageRequest pageRequest = PageRequest.of(0, 1);
            Page<Ticket> ticketPage = new PageImpl<>(List.of(ticketMock), pageRequest, 1);
            Mockito.when(ticketRepository.findAll(Mockito.any(Example.class), Mockito.eq(pageRequest))).thenReturn(ticketPage);

//            Action
            Page<Ticket> methodResponse = ticketService.getTicketsWithFiltersPaginated(ticketFilters, pageRequest);

//            Assert
            assertThat(methodResponse)
                    .isEqualTo(ticketPage);
        }

        @Test
        void givenAInvalidTicketStatusFilter_whenRequesting_thenThrowsInvalidTicketStatusException(){
//            Arrange
            TicketFiltersDTO ticketFilters = Mockito.mock(TicketFiltersDTO.class);
            Mockito.when(ticketFilters.status()).thenReturn("Invalid Status");
            PageRequest pageRequestMock = Mockito.mock(PageRequest.class);
            
//            Assert
            assertThatException()
                    .isThrownBy(() -> ticketService.getTicketsWithFiltersPaginated(ticketFilters, pageRequestMock))
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
            Mockito.when(ticketMock.getTicketStatus()).thenReturn(TicketStatusEnum.NEW_TICKET.getValue());

            Mockito.when(ticketRepository.findById(ticketId)).thenReturn(Optional.of(ticketMock));
            Mockito.when(ticketRepository.save(ticketMock)).thenReturn(ticketMock);

//            Action
            Ticket methodResponse = ticketService.updateTicket(ticketId, ticketUpdateDTOMock);

//            Assert
            assertThat(methodResponse)
                .isInstanceOf(Ticket.class);
        }
    }

}