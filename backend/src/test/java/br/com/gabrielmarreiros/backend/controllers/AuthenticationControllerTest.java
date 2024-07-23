package br.com.gabrielmarreiros.backend.controllers;

import br.com.gabrielmarreiros.backend.dto.login.LoginRequestDTO;
import br.com.gabrielmarreiros.backend.filters.AuthenticationFilter;
import br.com.gabrielmarreiros.backend.models.User;
import br.com.gabrielmarreiros.backend.services.AuthenticationService;
import br.com.gabrielmarreiros.backend.services.TokenJwtService;
import br.com.gabrielmarreiros.backend.services.UserService;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.Matchers.is;

@WebMvcTest(controllers = AuthenticationController.class, excludeFilters = {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {AuthenticationFilter.class})})
@Import(SpringSecurityTestConfig.class)
class AuthenticationControllerTest {
    @MockBean
    private AuthenticationService authenticationService;
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