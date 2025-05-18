package br.com.gabrielmarreiros.backend.controllers;

import br.com.gabrielmarreiros.backend.dto.ticket.AllTicketsStatusSummaryDTO;
import br.com.gabrielmarreiros.backend.dto.ticket.TicketRequestDTO;
import br.com.gabrielmarreiros.backend.dto.ticket.TicketResponseDTO;
import br.com.gabrielmarreiros.backend.dto.ticket.TicketUpdateDTO;
import br.com.gabrielmarreiros.backend.enums.TicketStatusEnum;
import br.com.gabrielmarreiros.backend.exceptions.InvalidTicketStatusException;
import br.com.gabrielmarreiros.backend.filters.JwtAuthenticationFilter;
import br.com.gabrielmarreiros.backend.mappers.TicketMapper;
import br.com.gabrielmarreiros.backend.models.Ticket;
import br.com.gabrielmarreiros.backend.services.TicketService;
import br.com.gabrielmarreiros.backend.testConfigs.SpringSecurityTestConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.UUID;

@WebMvcTest(controllers = TicketController.class, excludeFilters = {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {JwtAuthenticationFilter.class})})
@Import(SpringSecurityTestConfig.class)
class TicketControllerTest {
    @MockBean
    private TicketService ticketService;
    @MockBean
    private TicketMapper ticketMapper;
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Nested
    class getAllTickets {

        @Test
        void whenRequesting_thenReturn200Ok() throws Exception {
//            Arrange
            Ticket ticketMock = Mockito.mock(Ticket.class);

            Mockito.when(ticketService.getAllTickets()).thenReturn(List.of(ticketMock));

//            Action
            ResultActions mvcResponse = mvc.perform(
                    MockMvcRequestBuilders.get("/tickets")
            );

//            Assert
            mvcResponse
                    .andExpect(MockMvcResultMatchers.status().isOk());

        }
    }

    @Nested
    class getTicketsPaginated {

        @Test
        void givenAStatusFilter_whenRequesting_thenReturn200OkAndAPageOfTicketsFilteredByStatus() throws Exception {
//            Arrange
            Ticket ticketMock = Mockito.mock(Ticket.class);
            TicketResponseDTO ticketResponseDTOMock = Mockito.mock(TicketResponseDTO.class);

            Page<Ticket> ticketsPage = new PageImpl<>(List.of(ticketMock), PageRequest.of(0, 1), 1);
            Page<TicketResponseDTO> ticketsResponsePage = new PageImpl<>(List.of(ticketResponseDTOMock), PageRequest.of(0, 1), 1);

            Mockito.when(ticketService.getTicketsWithFiltersPaginated(Mockito.any(), Mockito.any())).thenReturn(ticketsPage);
            Mockito.when(ticketMapper.toResponsePageDTO(Mockito.any())).thenReturn(ticketsResponsePage);

//            Action
            ResultActions mvcResponse = mvc.perform(
                    MockMvcRequestBuilders
                        .get("/tickets/paginated")
                        .queryParam("page", "0")
                        .queryParam("size", "1")
                        .queryParam("status", TicketStatusEnum.PENDING.getValue())
            );

//            Assert
            mvcResponse
                .andExpect(MockMvcResultMatchers.status().isOk());

        }

        @Test
        void givenAInvalidStatus_whenRequesting_thenReturn404BadRequest() throws Exception {
            //            Arrange
            String invalidStatus = "Invalid Status";

            Mockito.when(ticketService.getTicketsWithFiltersPaginated(Mockito.any(), Mockito.any())).thenCallRealMethod();

//            Action
            ResultActions mvcResponse = mvc.perform(
                    MockMvcRequestBuilders
                            .get("/tickets/paginated")
                            .queryParam("page", "0")
                            .queryParam("size", "1")
                            .queryParam("status", invalidStatus)
            );

//            Assert
            mvcResponse
                    .andExpect(MockMvcResultMatchers.status().isBadRequest());
        }

    }

    @Nested
    class getTicketById {

        @Test
        void whenRequesting_thenReturn200Ok() throws Exception {
//            Arrange
            UUID ticketId = UUID.randomUUID();

            Ticket ticketMock = Mockito.mock(Ticket.class);
            TicketResponseDTO ticketResponseDTOMock = Mockito.mock(TicketResponseDTO.class);

            Mockito.when(ticketService.getTicketById(ticketId)).thenReturn(ticketMock);
            Mockito.when(ticketMapper.toResponseDTO(Mockito.any())).thenReturn(ticketResponseDTOMock);

//            Action
            ResultActions mvcResponse = mvc.perform(
                    MockMvcRequestBuilders
                            .get("/tickets/" + ticketId)
            );

//            Assert
            mvcResponse
                    .andExpect(MockMvcResultMatchers.status().isOk());
        }
    }

    @Nested
    class saveTicket {

        @Test
        void whenRequesting_thenReturn200Ok() throws Exception {
//            Arrange
            TicketRequestDTO ticketRequestDTOMock = Mockito.mock(TicketRequestDTO.class);
            Ticket ticketMock = Mockito.mock(Ticket.class);
            TicketResponseDTO ticketResponseDTOMock = Mockito.mock(TicketResponseDTO.class);

            Mockito.when(ticketMapper.toEntity(Mockito.any(TicketRequestDTO.class))).thenReturn(ticketMock);
            Mockito.when(ticketService.saveTicket(ticketMock)).thenReturn(ticketMock);
            Mockito.when(ticketMapper.toResponseDTO(Mockito.any(Ticket.class))).thenReturn(ticketResponseDTOMock);

//            Action
            ResultActions mvcResponse = mvc.perform(
                    MockMvcRequestBuilders
                            .post("/tickets")
                            .content(objectMapper.writeValueAsString(ticketRequestDTOMock))
                            .contentType(MediaType.APPLICATION_JSON)
            );

//            Assert
            mvcResponse
                    .andExpect(MockMvcResultMatchers.status().isCreated());
        }
    }

    @Nested
    class updateTicket {

        @Test
        void whenRequesting_thenReturn200Ok() throws Exception {
//            Arrange
            UUID ticketId = UUID.randomUUID();

            TicketUpdateDTO ticketUpdateDTOMock = Mockito.mock(TicketUpdateDTO.class);
            Ticket ticketMock = Mockito.mock(Ticket.class);
            TicketResponseDTO ticketResponseDTOMock = Mockito.mock(TicketResponseDTO.class);

            Mockito.when(ticketService.updateTicket(Mockito.any(UUID.class), Mockito.any(TicketUpdateDTO.class))).thenReturn(ticketMock);
            Mockito.when(ticketMapper.toResponseDTO(Mockito.any(Ticket.class))).thenReturn(ticketResponseDTOMock);

//            Action
            ResultActions mvcResponse = mvc.perform(
                    MockMvcRequestBuilders
                            .put("/tickets/" + ticketId)
                            .content(objectMapper.writeValueAsString(ticketUpdateDTOMock))
                            .contentType(MediaType.APPLICATION_JSON)
            );

//            Assert
            mvcResponse
                    .andExpect(MockMvcResultMatchers.status().isOk());
        }
    }

    @Nested
    class updateStatus {

        @Test
        void givenAValidStatus_whenRequesting_thenReturn200Ok() throws Exception {
//            Arrange
            UUID ticketId = UUID.randomUUID();
            Ticket ticketMock = Mockito.mock(Ticket.class);
            TicketResponseDTO ticketResponseDTOMock = Mockito.mock(TicketResponseDTO.class);

            Mockito.when(ticketService.updateTicketStatus(Mockito.any(UUID.class), Mockito.any(String.class))).thenReturn(ticketMock);
            Mockito.when(ticketMapper.toResponseDTO(ticketMock)).thenReturn(ticketResponseDTOMock);

//            Action
            ResultActions mvcResponse = mvc.perform(
                    MockMvcRequestBuilders
                            .put("/tickets/update-status/" + ticketId)
                            .content(TicketStatusEnum.IN_PROGRESS.getValue())
                            .contentType(MediaType.APPLICATION_JSON)
            );

//            Assert
            mvcResponse
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(ticketResponseDTOMock)));
        }

        @Test
        void givenAInvalidStatus_whenRequesting_thenReturn404BadRequest() throws Exception {
//            Arrange
            UUID ticketId = UUID.randomUUID();
            String invalidStatus = "Invalid Status";

            Mockito.when(ticketService.updateTicketStatus(ticketId, invalidStatus)).thenThrow(InvalidTicketStatusException.class);

//            Action
            ResultActions mvcResponse = mvc.perform(
                    MockMvcRequestBuilders
                            .put("/tickets/update-status/" + ticketId)
                            .content(invalidStatus)
                            .contentType(MediaType.APPLICATION_JSON)
            );

//            Assert
            mvcResponse
                    .andExpect(MockMvcResultMatchers.status().isBadRequest());
        }
    }

    @Nested
    class getAllTicketsStatusSummary {

        @Test
        void whenRequesting_thenReturn200Ok() throws Exception {
//            Arrange
            AllTicketsStatusSummaryDTO allTicketsStatusSummaryDTOMock = Mockito.mock(AllTicketsStatusSummaryDTO.class);

            Mockito.when(ticketService.getAllTicketsStatusSummary()).thenReturn(allTicketsStatusSummaryDTOMock);

//            Action
            ResultActions mvcResponse = mvc.perform(
                    MockMvcRequestBuilders
                            .get("/tickets/all-tickets-summary-status")
            );

//            Assert
            mvcResponse
                    .andExpect(MockMvcResultMatchers.status().isOk());
        }
    }
}
