package br.com.gabrielmarreiros.backend.services;

import br.com.gabrielmarreiros.backend.dto.login.LoginRequestDTO;
import br.com.gabrielmarreiros.backend.exceptions.InvalidLoginException;
import br.com.gabrielmarreiros.backend.exceptions.InvalidTokenException;
import br.com.gabrielmarreiros.backend.models.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {
    @Mock
    private TokenJwtService tokenJwtService;
    @Mock
    private AuthenticationManager authenticationManager;
    @InjectMocks
    private AuthenticationService authenticationService;

    @Nested
    class login {
        
        @Test
        void givenACorrectLogin_whenRequesting_thenReturnUserEntity(){
//            Arrange
            LoginRequestDTO loginRequestMock = Mockito.mock(LoginRequestDTO.class);
            User userMock = Mockito.mock(User.class);
            Authentication authenticationMock = Mockito.mock(Authentication.class);

            Mockito.when(authenticationMock.getPrincipal()).thenReturn(userMock);
            Mockito.when(authenticationManager.authenticate(Mockito.any())).thenReturn(authenticationMock);

//            Action
            User serviceResponse = authenticationService.login(loginRequestMock);

//            Assert
            assertThat(serviceResponse)
                    .isEqualTo(userMock);

        }

        @Test
        void givenAIncorrectLogin_whenRequesting_thenThrowInvalidLoginException(){
//            Arrange
            LoginRequestDTO loginRequestMock = Mockito.mock(LoginRequestDTO.class);
            AuthenticationException authenticationExceptionMock = Mockito.mock(AuthenticationException.class);

            Mockito.when(authenticationManager.authenticate(Mockito.any())).thenThrow(authenticationExceptionMock);

//            Assert
            assertThatThrownBy(() -> authenticationService.login(loginRequestMock))
                    .isInstanceOf(InvalidLoginException.class);
        }
    }

    @Nested
    class validateToken {
        @Test
        void givenAValidToken_whenRequesting_thenReturnTrue(){
//            Arrange
            String tokenMock = "tokenTest";
            Mockito.when(tokenJwtService.validateToken(tokenMock)).thenReturn("subject");

//            Action
            Boolean serviceResponse = authenticationService.validateToken(tokenMock);

//            Assert
            assertThat(serviceResponse).isTrue();
        }

        @Test
        void givenAInvalidToken_whenRequesting_thenReturnFalse(){
//            Arrange
            String tokenMock = "tokenTest";
            Mockito.when(tokenJwtService.validateToken(tokenMock)).thenThrow(InvalidTokenException.class);

//            Action
            Boolean serviceResponse = authenticationService.validateToken(tokenMock);

//            Assert
            assertThat(serviceResponse).isFalse();
        }
    }
}