package br.com.gabrielmarreiros.backend.controllers;

import br.com.gabrielmarreiros.backend.dto.customer.*;
import br.com.gabrielmarreiros.backend.dto.ticket.TicketResponseDTO;
import br.com.gabrielmarreiros.backend.filters.JwtAuthenticationFilter;
import br.com.gabrielmarreiros.backend.mappers.CustomerMapper;
import br.com.gabrielmarreiros.backend.models.Customer;
import br.com.gabrielmarreiros.backend.models.Ticket;
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
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.UUID;

@WebMvcTest(controllers = CustomerController.class, excludeFilters = {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {JwtAuthenticationFilter.class})})
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

            Mockito.when(customerService.getCustomersPaginated(Mockito.any(CustomerFiltersDTO.class), Mockito.any(PageRequest.class))).thenReturn(new PageImpl<Customer>(List.of(customerMock), PageRequest.of(0, 1), 1));
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
        void whenRequesting_thenReturn200Ok() throws Exception {
//            Arrange
            UUID customerId = UUID.randomUUID();
            Customer customerMock = Mockito.mock(Customer.class);
            CustomerResponseDTO customerResponseDTOMock = Mockito.mock(CustomerResponseDTO.class);

            Mockito.when(customerService.changeCustomerActiveStatus(customerId)).thenReturn(customerMock);
            Mockito.when(customerMapper.toResponseDTO(customerMock)).thenReturn(customerResponseDTOMock);

//            Action
            ResultActions mvcResponse = mvc.perform(
                MockMvcRequestBuilders
                    .delete("/customers/" + customerId)
            );

//            Assert
            mvcResponse
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(customerResponseDTOMock)));
        }
    }
}