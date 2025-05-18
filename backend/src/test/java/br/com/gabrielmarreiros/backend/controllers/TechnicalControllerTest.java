package br.com.gabrielmarreiros.backend.controllers;

import br.com.gabrielmarreiros.backend.dto.technical.*;
import br.com.gabrielmarreiros.backend.filters.JwtAuthenticationFilter;
import br.com.gabrielmarreiros.backend.mappers.TechnicalMapper;
import br.com.gabrielmarreiros.backend.models.Technical;
import br.com.gabrielmarreiros.backend.services.TechnicalService;
import br.com.gabrielmarreiros.backend.services.TokenJwtService;
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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.UUID;

@WebMvcTest(controllers = TechnicalController.class, excludeFilters = {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {JwtAuthenticationFilter.class})})
@Import(SpringSecurityTestConfig.class)
class TechnicalControllerTest {
    @MockBean
    private TechnicalService technicalService;
    @MockBean
    private TechnicalMapper technicalMapper;
    @MockBean
    private TokenJwtService tokenJwtService;
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Nested
    class getAllTechnicians {

        @Test
        void whenRequesting_thenReturn200Ok() throws Exception{
//            Arrange
            Technical technicalMock = Mockito.mock(Technical.class);
            Mockito.when(technicalService.getAllTechnicians()).thenReturn(List.of(technicalMock));

//            Action
            ResultActions mvcResponse = mvc.perform(
                    MockMvcRequestBuilders.get("/technicians")
            );

//            Assert
            mvcResponse
                    .andExpect(MockMvcResultMatchers.status().isOk());

        }
    }

    @Nested
    class getTechniciansPaginated {

        @Test
        void whenRequesting_thenReturn200Ok() throws Exception{
//            Arrange
            Technical technicalMock = Mockito.mock(Technical.class);
            TechnicalResponseDTO technicalResponseDTOMock = Mockito.mock(TechnicalResponseDTO.class);

            Mockito.when(technicalService.getTechniciansPaginated(Mockito.any())).thenReturn(new PageImpl<Technical>(List.of(technicalMock), PageRequest.of(0, 1), 1));
            Mockito.when(technicalMapper.toResponsePageDTO(Mockito.any())).thenReturn(new PageImpl<TechnicalResponseDTO>(List.of(technicalResponseDTOMock), PageRequest.of(0, 1), 1));

//            Action
            ResultActions mvcResponse = mvc.perform(
                    MockMvcRequestBuilders
                            .get("/technicians/paginated")
                            .queryParam("page", "0")
                            .queryParam("size", "1")
            );

//            Assert
            mvcResponse
                    .andExpect(MockMvcResultMatchers.status().isOk());
        }
    }

    @Nested
    class getTechnicalById {

        @Test
        void whenRequesting_thenReturn200Ok() throws Exception{
//            Arrange
            UUID uuid = UUID.randomUUID();

            Technical technicalMock = Mockito.mock(Technical.class);
            Mockito.when(technicalService.getTechnicalById(uuid)).thenReturn(technicalMock);

            TechnicalResponseDTO technicalResponseDTOMock = Mockito.mock(TechnicalResponseDTO.class);
            Mockito.when(technicalMapper.toResponseDTO(Mockito.any())).thenReturn(technicalResponseDTOMock);

//            Action
            ResultActions mvcResponse = mvc.perform(
                    MockMvcRequestBuilders
                            .get("/technicians/" + uuid)
            );

//            Assert
            mvcResponse
                    .andExpect(MockMvcResultMatchers.status().isOk());
        }
    }

    @Nested
    class updateTechnical {

        @Test
        void whenRequesting_thenReturn200Ok() throws Exception{
//            Arrange
            UUID uuid = UUID.randomUUID();

            TechnicalUpdateRequestDTO technicalUpdateRequestDTOMock = Mockito.mock(TechnicalUpdateRequestDTO.class);
            Technical technicalMock = Mockito.mock(Technical.class);
            TechnicalResponseDTO technicalResponseDTOMock = Mockito.mock(TechnicalResponseDTO.class);

            Mockito.when(technicalService.updateTechnical(uuid, technicalUpdateRequestDTOMock)).thenReturn(technicalMock);
            Mockito.when(technicalMapper.toResponseDTO(technicalMock)).thenReturn(technicalResponseDTOMock);

//            Action
            ResultActions mvcResponse = mvc.perform(
                    MockMvcRequestBuilders
                            .put("/technicians/" + uuid)
                            .content(objectMapper.writeValueAsString(technicalUpdateRequestDTOMock))
                            .contentType(MediaType.APPLICATION_JSON)
            );

//            Assert
            mvcResponse
                    .andExpect(MockMvcResultMatchers.status().isOk());
        }
    }

    @Nested
    class getAllTechnicalWithTicketsStatusCount {

        @Test
        void whenRequesting_thenReturn200Ok() throws Exception {
//            Arrange
            TechnicalWithTicketStatusCountDTO technicalWithTicketStatusCountDTOMock = Mockito.mock(TechnicalWithTicketStatusCountDTO.class);

            Mockito.when(technicalService.getAllTechnicalWithTicketStatusCount()).thenReturn(List.of(technicalWithTicketStatusCountDTOMock));

//            Action
            ResultActions mvcResponse = mvc.perform(
                    MockMvcRequestBuilders
                            .get("/technicians/with-tickets-status-count")
            );

//            Assert
            mvcResponse
                    .andExpect(MockMvcResultMatchers.status().isOk());
        }
    }

    @Nested
    class getTechniciansWithTicketsStatusCountPaginated {

        @Test
        void whenRequesting_thenReturn200Ok() throws Exception {
//            Arrange
            TechnicalWithTicketStatusCountDTO technicalWithTicketStatusCountDTOMock = Mockito.mock(TechnicalWithTicketStatusCountDTO.class);

            Mockito.when(technicalService.getTechniciansWithTicketsStatusCountPaginated(Mockito.any(), Mockito.any(PageRequest.class))).thenReturn(new PageImpl<TechnicalWithTicketStatusCountDTO>(List.of(technicalWithTicketStatusCountDTOMock), PageRequest.of(0, 1), 1));

//            Action
            ResultActions mvcResponse = mvc.perform(
                    MockMvcRequestBuilders
                            .get("/technicians/with-tickets-status-count/paginated")
                            .queryParam("page", "0")
                            .queryParam("size", "1")
            );

//            Assert
            mvcResponse
                    .andExpect(MockMvcResultMatchers.status().isOk());
        }
    }

    @Nested
    class changeTechnicalActiveStatus {

        @Test
        void whenRequesting_thenReturn200Ok() throws Exception {
//            Arrange
            UUID technicalId = UUID.randomUUID();
            Technical technicalMock = Mockito.mock(Technical.class);
            TechnicalResponseDTO technicalResponseDTOMock = Mockito.mock(TechnicalResponseDTO.class);

            Mockito.when(technicalService.changeTechnicalActiveStatus(technicalId)).thenReturn(technicalMock);
            Mockito.when(technicalMapper.toResponseDTO(technicalMock)).thenReturn(technicalResponseDTOMock);

//            Action
            ResultActions mvcResponse = mvc.perform(
                    MockMvcRequestBuilders
                            .delete("/technicians/" + technicalId)
            );

//            Assert
            mvcResponse
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(technicalResponseDTOMock)));
        }
    }
}