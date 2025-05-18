package br.com.gabrielmarreiros.backend.controllers;

import br.com.gabrielmarreiros.backend.filters.JwtAuthenticationFilter;
import br.com.gabrielmarreiros.backend.models.Priority;
import br.com.gabrielmarreiros.backend.services.PriorityService;
import br.com.gabrielmarreiros.backend.testConfigs.SpringSecurityTestConfig;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

@WebMvcTest(controllers = PriorityController.class, excludeFilters = {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {JwtAuthenticationFilter.class})})
@Import(SpringSecurityTestConfig.class)
class PriorityControllerTest {
    @MockBean
    private PriorityService priorityService;
    @Autowired
    private MockMvc mvc;

    @Nested
    class getAllPriorities {

        @Test
        void whenRequesting_thenReturn200Ok() throws Exception {
//            Arrange
            Priority priorityMock = Mockito.mock(Priority.class);
            Mockito.when(priorityService.getAllPriorities()).thenReturn(List.of(priorityMock));

//            Action
            var mvcResponse = mvc.perform(
                MockMvcRequestBuilders.get("/priorities")
            );

//            Assert
            mvcResponse
                .andExpect(MockMvcResultMatchers.status().isOk());
        }
    }
}