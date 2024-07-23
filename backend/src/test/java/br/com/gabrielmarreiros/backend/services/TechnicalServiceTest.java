package br.com.gabrielmarreiros.backend.services;

import br.com.gabrielmarreiros.backend.dto.customer.CustomerUpdateRequestDTO;
import br.com.gabrielmarreiros.backend.dto.technical.TechnicalUpdateRequestDTO;
import br.com.gabrielmarreiros.backend.exceptions.UserAlreadyRegisteredException;
import br.com.gabrielmarreiros.backend.exceptions.UserNotFoundException;
import br.com.gabrielmarreiros.backend.models.Customer;
import br.com.gabrielmarreiros.backend.models.Technical;
import br.com.gabrielmarreiros.backend.models.User;
import br.com.gabrielmarreiros.backend.repositories.TechnicalRepository;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TechnicalServiceTest {
    @Mock
    private UserService userService;
    @Mock
    private TechnicalRepository technicalRepository;
    @InjectMocks
    private TechnicalService technicalService;

    @Nested
    class getAllTechnicians {

        @Test
        void whenRequesting_thenReturnListOfTechnicians() {
//          Arrange
            Technical technicalMock = Mockito.mock(Technical.class);
            List<Technical> technicalListMock = new ArrayList<>();
            technicalListMock.add(technicalMock);

            Mockito.when(technicalRepository.findAll()).thenReturn(technicalListMock);

//          Action
            List<Technical> serviceResponse = technicalService.getAllTechnicians();

//          Assert
            assertThat(serviceResponse)
                    .isNotEmpty()
                    .isEqualTo(technicalListMock);
        }
    }

    @Nested
    class saveTechnical {

        @Test
        void givenAUserThatAlreadyExists_whenRequesting_thenThrowsUserAlreadyRegisteredException(){
//            Arrange
            User userMock = Mockito.mock(User.class);
            Technical technicalMock = Mockito.mock(Technical.class);
            Mockito.when(technicalMock.getUser()).thenReturn(userMock);

            Mockito.when(userService.verifyUserAlreadyRegistered(Mockito.any())).thenReturn(true);

//            Assert
            assertThatException()
                    .isThrownBy(() -> technicalService.saveTechnical(technicalMock))
                    .isInstanceOf(UserAlreadyRegisteredException.class);

        }
    }

    @Nested
    class updateTechnical {

        @Test
        void whenRequesting_thenUpdateAndReturnTechnical() {
//            Arrange
            UUID technicalId = UUID.randomUUID();
            User userMock = Mockito.mock(User.class);
            TechnicalUpdateRequestDTO technicalUpdateDTOMock = Mockito.mock(TechnicalUpdateRequestDTO.class);
            Technical technicalMock = Mockito.mock(Technical.class);

            Mockito.when(technicalMock.getUser()).thenReturn(userMock);

            Mockito.when(technicalRepository.findById(technicalId)).thenReturn(Optional.of(technicalMock));
            Mockito.when(technicalRepository.save(technicalMock)).thenReturn(technicalMock);

//            Action
            Technical methodResponse = technicalService.updateTechnical(technicalId, technicalUpdateDTOMock);

//            Assert
            assertThat(methodResponse)
                    .isInstanceOf(Technical.class);
        }

        @Test
        void givenAUnknowId_whenRequesting_thenThrowsUserNotFoundException() {
//            Arrange
            UUID technicalId = UUID.randomUUID();
            TechnicalUpdateRequestDTO technicalUpdateDTOMock = Mockito.mock(TechnicalUpdateRequestDTO.class);

            Mockito.when(technicalRepository.findById(technicalId)).thenReturn(Optional.ofNullable(null));

//            Assert
            assertThatException()
                    .isThrownBy(() -> technicalService.updateTechnical(technicalId, technicalUpdateDTOMock))
                    .isInstanceOf(UserNotFoundException.class);
        }
    }

    @Nested
    class getTechnicalById {

        @Test
        void givenTheValidId_whenRequesting_theReturnTechnical() {
//          Arrange
            Technical technicalMock = Mockito.mock(Technical.class);
            UUID technicalId = UUID.randomUUID();
            technicalMock.setId(technicalId);

            Mockito.when(technicalRepository.findById(technicalId)).thenReturn(Optional.of(technicalMock));

//          Action
            Technical serviceResponse = technicalService.getTechnicalById(technicalId);

//          Assert
            assertThat(serviceResponse)
                    .isEqualTo(technicalMock);
        }

        @Test
        void givenTheInvalidId_whenRequesting_theThrowsUserNotFoundException() {

//          Assert
            assertThatException()
                    .isThrownBy(() -> technicalService.getTechnicalById(UUID.randomUUID()))
                    .isInstanceOf(UserNotFoundException.class);

        }
    }

    @Nested
    class getTechniciansPaginated {

        @Test
        void whenRequesting_thenReturnAListOfCustomersPaginated(){
//            Arrange
            PageRequest pageRequest = PageRequest.of(0, 1);
            Technical technicalMock = Mockito.mock(Technical.class);
            Page<Technical> technicalsPage = new PageImpl<>(List.of(technicalMock), pageRequest, 1);

            Mockito.when(technicalRepository.findAll(pageRequest)).thenReturn(technicalsPage);

//            Action
            Page<Technical> methodResponse = technicalService.getTechniciansPaginated(pageRequest);

//            Assert
            assertThat(methodResponse)
                    .isEqualTo(technicalsPage);
        }
    }

}