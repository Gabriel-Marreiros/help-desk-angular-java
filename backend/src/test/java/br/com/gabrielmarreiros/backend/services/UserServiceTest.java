package br.com.gabrielmarreiros.backend.services;

import br.com.gabrielmarreiros.backend.exceptions.UserNotFoundException;
import br.com.gabrielmarreiros.backend.models.User;
import br.com.gabrielmarreiros.backend.repositories.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Nested
    class loadUserByUsername {
        @Test
        void givenAEmail_whenRquesting_thenReturnUser(){
//            Arrange
            User userMock = Mockito.mock(User.class);
            Mockito.when(userRepository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(userMock));

//            Action
            UserDetails serviceResponse = userService.loadUserByUsername("test@email.com");

//            Assert
            assertThat(serviceResponse)
                    .isEqualTo(userMock);
        }

        @Test
        void givenAnUnregisteredEmail_whenRequesting_thenThrowsUserNotFoundException(){
//            Arrange
            Mockito.when(userRepository.findByEmail(Mockito.anyString())).thenReturn(Optional.ofNullable(null));

//            Assert
            assertThatExceptionOfType(UserNotFoundException.class)
                    .isThrownBy(() -> userService.loadUserByUsername("test@email.com"));
        }
    }

    @Nested
    class verifyUserAlreadyRegistered {
        @Test
        void givenAnEmailAlreadyRegistered_whenRequesting_thenReturnTrue(){
//            Arrange
            User userMock = Mockito.mock(User.class);
            Mockito.when(userRepository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(userMock));

//            Action
            boolean serviceResponse = userService.verifyUserAlreadyRegistered("test@email.com");

//            Assert
            assertThat(serviceResponse)
                    .isTrue();
        }

        @Test
        void givenAnUnregisteredEmail_whenRequesting_thenReturnFalse(){
//            Arrange
            Mockito.when(userRepository.findByEmail(Mockito.anyString())).thenReturn(Optional.ofNullable(null));

//            Action
            boolean serviceResponse = userService.verifyUserAlreadyRegistered("test@email.com");

//            Assert
            assertThat(serviceResponse)
                    .isFalse();
        }
    }
}