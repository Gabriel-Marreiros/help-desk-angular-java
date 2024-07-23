package br.com.gabrielmarreiros.backend.controllers;

import br.com.gabrielmarreiros.backend.dto.customer.CustomerRegisterRequestDTO;
import br.com.gabrielmarreiros.backend.dto.customer.CustomerRegisterResponseDTO;
import br.com.gabrielmarreiros.backend.dto.customer.CustomerResponseDTO;
import br.com.gabrielmarreiros.backend.dto.customer.CustomerUpdateRequestDTO;
import br.com.gabrielmarreiros.backend.filters.AuthenticationFilter;
import br.com.gabrielmarreiros.backend.mappers.CustomerMapper;
import br.com.gabrielmarreiros.backend.models.Customer;
import br.com.gabrielmarreiros.backend.models.Priority;
import br.com.gabrielmarreiros.backend.models.User;
import br.com.gabrielmarreiros.backend.services.CustomerService;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(controllers = CustomerController.class, excludeFilters = {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {AuthenticationFilter.class})})
@Import(SpringSecurityTestConfig.class)
class CustomerControllerTest {
    @MockBean
    private CustomerService customerService;
    @MockBean
    private CustomerMapper customerMapper;
    @MockBean
    private TokenJwtService tokenJwtService;
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Nested
    class getAllCustomers {

        @Test
        void whenRequesting_thenReturn200Ok() throws Exception {
//            Arrange
            Customer customerMock = Mockito.mock(Customer.class);
            Mockito.when(customerService.getAllCustomers()).thenReturn(List.of(customerMock));

//            Action
            ResultActions mvcResponse = mvc.perform(
                    MockMvcRequestBuilders.get("/customers")
            );

//            Assert
            mvcResponse
                    .andExpect(MockMvcResultMatchers.status().isOk());

        }
    }

    @Nested
    class getCustomersPaginated {

        @Test
        void whenRequesting_thenReturn200Ok() throws Exception {
//            Arrange
            Customer customerMock = Mockito.mock(Customer.class);
            CustomerResponseDTO customerResponseDTOMock = Mockito.mock(CustomerResponseDTO.class);

            Mockito.when(customerService.getCustomersPaginated(Mockito.any())).thenReturn(new PageImpl<Customer>(List.of(customerMock), PageRequest.of(0, 1), 1));
            Mockito.when(customerMapper.toResponsePageDTO(Mockito.any())).thenReturn(new PageImpl<CustomerResponseDTO>(List.of(customerResponseDTOMock), PageRequest.of(0, 1), 1));

//            Action
            ResultActions mvcResponse = mvc.perform(
                    MockMvcRequestBuilders
                            .get("/customers/paginated")
                            .param("page", "0")
                            .param("size", "1")
            );

//            Assert
            mvcResponse
                    .andExpect(MockMvcResultMatchers.status().isOk());
        }
    }

    @Nested
    class getCustomerById {

        @Test
        void whenRequesting_thenReturn200Ok() throws Exception {
//            Arrange
            UUID uuid = UUID.randomUUID();

            Customer customerMock = Mockito.mock(Customer.class);
            Mockito.when(customerService.getCustomerById(uuid)).thenReturn(customerMock);

            CustomerResponseDTO customerResponseDTOMock = Mockito.mock(CustomerResponseDTO.class);
            Mockito.when(customerMapper.toResponseDTO(Mockito.any())).thenReturn(customerResponseDTOMock);

//            Action
            ResultActions mvcResponse = mvc.perform(
                    MockMvcRequestBuilders
                            .get("/customers/" + uuid)
            );

//            Assert
            mvcResponse
                    .andExpect(MockMvcResultMatchers.status().isOk());
        }
    }

    @Nested
    class registerCustomer {

        @Test
        void whenRequesting_thenReturn201Created() throws Exception {
//            Arrange
            CustomerRegisterRequestDTO customerRegisterRequestDTOMock = Mockito.mock(CustomerRegisterRequestDTO.class);
            User userMock = Mockito.mock(User.class);
            Customer customerMock = Mockito.mock(Customer.class);
            customerMock.setUser(userMock);
            CustomerRegisterResponseDTO customerRegisterResponseDTOMock = Mockito.mock(CustomerRegisterResponseDTO.class);

            Mockito.when(customerMapper.toEntity(Mockito.any())).thenReturn(customerMock);
            Mockito.when(customerService.saveCustomer(Mockito.any())).thenReturn(customerMock);
            Mockito.when(tokenJwtService.generateToken(Mockito.any())).thenReturn("token");
            Mockito.when(customerMapper.toRegisterResponseDTO(Mockito.any(Customer.class), Mockito.eq("token"))).thenReturn(customerRegisterResponseDTOMock);

//            Action
            ResultActions mvcResponse = mvc.perform(
                MockMvcRequestBuilders
                    .post("/customers")
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
    class updateCustomer {

        @Test
        void whenRequesting_thenReturn200Ok() throws Exception {
//            Arrange
            UUID uuid = UUID.randomUUID();

            CustomerUpdateRequestDTO customerUpdateRequestDTOMock = Mockito.mock(CustomerUpdateRequestDTO.class);
            Customer customerMock = Mockito.mock(Customer.class);
            CustomerResponseDTO customerResponseDTOMock = Mockito.mock(CustomerResponseDTO.class);

            Mockito.when(customerService.updateCustomer(uuid, customerUpdateRequestDTOMock)).thenReturn(customerMock);
            Mockito.when(customerMapper.toResponseDTO(customerMock)).thenReturn(customerResponseDTOMock);

//            Action
            ResultActions mvcResponse = mvc.perform(
                MockMvcRequestBuilders
                    .put("/customers/" + uuid)
                    .content(objectMapper.writeValueAsString(customerUpdateRequestDTOMock))
                    .contentType(MediaType.APPLICATION_JSON)
            );

//            Assert
            mvcResponse
                .andExpect(MockMvcResultMatchers.status().isOk());

        }
    }

    @Nested
    class changeCustomerActiveStatus {

        @Test
        void whenRequesting_thenReturn204NoContent() throws Exception {
//            Arrange
            UUID customerId = UUID.randomUUID();

            Mockito.doNothing().when(customerService).changeCustomerActiveStatus(customerId);

//            Action
            ResultActions mvcResponse = mvc.perform(
                MockMvcRequestBuilders
                    .delete("/customers/" + customerId)
            );

//            Assert
            mvcResponse
                .andExpect(MockMvcResultMatchers.status().isNoContent());
        }
    }
}