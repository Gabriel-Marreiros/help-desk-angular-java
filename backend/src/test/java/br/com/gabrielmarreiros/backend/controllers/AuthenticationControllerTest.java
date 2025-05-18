package br.com.gabrielmarreiros.backend.controllers;

import br.com.gabrielmarreiros.backend.dto.customer.CustomerRegisterRequestDTO;
import br.com.gabrielmarreiros.backend.dto.customer.CustomerRegisterResponseDTO;
import br.com.gabrielmarreiros.backend.dto.login.LoginRequestDTO;
import br.com.gabrielmarreiros.backend.dto.technical.TechnicalRegisterRequestDTO;
import br.com.gabrielmarreiros.backend.dto.technical.TechnicalRegisterResponseDTO;
import br.com.gabrielmarreiros.backend.filters.JwtAuthenticationFilter;
import br.com.gabrielmarreiros.backend.mappers.CustomerMapper;
import br.com.gabrielmarreiros.backend.mappers.TechnicalMapper;
import br.com.gabrielmarreiros.backend.models.Customer;
import br.com.gabrielmarreiros.backend.models.Technical;
import br.com.gabrielmarreiros.backend.models.User;
import br.com.gabrielmarreiros.backend.services.*;
import br.com.gabrielmarreiros.backend.testConfigs.SpringSecurityTestConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.Matchers.is;

@WebMvcTest(controllers = AuthenticationController.class, excludeFilters = {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {JwtAuthenticationFilter.class})})
@Import(SpringSecurityTestConfig.class)
class AuthenticationControllerTest {
    @MockBean
    private AuthenticationService authenticationService;
    @MockBean
    private CustomerService customerService;
    @MockBean
    private CustomerMapper customerMapper;
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
    class login {

        @Test
        @DisplayName("")
        void whenRequesting_thenReturn200Ok() throws Exception {
//            Arrange
            String loginRequest = objectMapper.writeValueAsString(new LoginRequestDTO("email", "senha"));
            User userMock = Mockito.mock(User.class);
            Mockito.when(authenticationService.login(Mockito.any())).thenReturn(userMock);
            Mockito.when(tokenJwtService.generateToken(Mockito.any())).thenReturn("token");


//            Action
            var mvcResponse = mvc.perform(
                    MockMvcRequestBuilders
                            .post("/auth/login")
                            .content(loginRequest)
                            .contentType(MediaType.APPLICATION_JSON)
            );

//            Assert
            mvcResponse
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.token", is("token")));
        }
    }

    @Nested
    class registerCustomer {

        @Test
        void whenRequesting_thenReturn201Created() throws Exception {
//            Arrange
            CustomerRegisterRequestDTO customerRegisterRequestDTOMock = Mockito.mock(CustomerRegisterRequestDTO.class);
            Customer customerMock = Mockito.mock(Customer.class);
            CustomerRegisterResponseDTO customerRegisterResponseDTOMock = Mockito.mock(CustomerRegisterResponseDTO.class);

            Mockito.when(customerMapper.toEntity(Mockito.any())).thenReturn(customerMock);
            Mockito.when(customerService.saveCustomer(Mockito.any())).thenReturn(customerMock);
            Mockito.when(tokenJwtService.generateToken(Mockito.any())).thenReturn("token");
            Mockito.when(customerMapper.toRegisterResponseDTO(Mockito.any(Customer.class), Mockito.eq("token"))).thenReturn(customerRegisterResponseDTOMock);

//            Action
            ResultActions mvcResponse = mvc.perform(
                    MockMvcRequestBuilders
                            .post("/auth/register/customer")
                            .content(objectMapper.writeValueAsString(customerRegisterRequestDTOMock))
                            .contentType(MediaType.APPLICATION_JSON)
            );

//            Assert
            mvcResponse
                    .andExpect(MockMvcResultMatchers.status().isCreated())
                    .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
        }
    }

    @Nested
    class registerTechnical {

        @Test
        void whenRequesting_thenReturn201Created() throws Exception{
//            Arrange
            TechnicalRegisterRequestDTO technicalRegisterRequestDTOMock = Mockito.mock(TechnicalRegisterRequestDTO.class);
            Technical technicalMock = Mockito.mock(Technical.class);
            TechnicalRegisterResponseDTO technicalRegisterResponseDTOMock = Mockito.mock(TechnicalRegisterResponseDTO.class);

            Mockito.when(technicalMapper.toEntity(Mockito.any())).thenReturn(technicalMock);
            Mockito.when(technicalService.saveTechnical(Mockito.any())).thenReturn(technicalMock);
            Mockito.when(tokenJwtService.generateToken(Mockito.any())).thenReturn("token");
            Mockito.when(technicalMapper.toRegisterResponseDTO(Mockito.any(Technical.class), Mockito.eq("token"))).thenReturn(technicalRegisterResponseDTOMock);

//            Action
            ResultActions mvcResponse = mvc.perform(
                    MockMvcRequestBuilders
                            .post("/auth/register/technical")
                            .content(objectMapper.writeValueAsString(technicalRegisterRequestDTOMock))
                            .contentType(MediaType.APPLICATION_JSON)
            );

//            Assert
            mvcResponse
                    .andExpect(MockMvcResultMatchers.status().isCreated())
                    .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
        }
    }

    @Nested
    class validateToken {

        @Test
        @DisplayName("")
        void whenRequesting_thenReturn200Ok() throws Exception {
//            Arrange
            Mockito.when(authenticationService.validateToken(Mockito.any())).thenReturn(true);

//            Action
            var mvcResponse = mvc.perform(
                MockMvcRequestBuilders
                    .post("/auth/validate-token")
                    .content("token")
            );

//            Assert
            mvcResponse
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", is(true)));
        }
    }
}