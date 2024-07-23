package br.com.gabrielmarreiros.backend.services;

import br.com.gabrielmarreiros.backend.models.Priority;
import br.com.gabrielmarreiros.backend.repositories.PriorityRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PriorityServiceTest {

    @Mock
    private PriorityRepository priorityRepository;
    @InjectMocks
    private PriorityService priorityService;

    @Nested
    class getAllPriorities {

        @Test
        void whenRequesting_thenReturnListOfPriorities(){
//            Arrange
            Priority priorityMock = Mockito.mock(Priority.class);

            Mockito.when(priorityRepository.findAll()).thenReturn(List.of(priorityMock));

//            Action
            List<Priority> methodResponse = priorityService.getAllPriorities();

//            Assert
            assertThat(methodResponse)
                    .isNotEmpty()
                    .isEqualTo(List.of(priorityMock));

        }
    }
}