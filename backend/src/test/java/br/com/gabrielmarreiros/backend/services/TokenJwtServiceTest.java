package br.com.gabrielmarreiros.backend.services;

import br.com.gabrielmarreiros.backend.exceptions.InvalidTokenException;
import br.com.gabrielmarreiros.backend.models.Role;
import br.com.gabrielmarreiros.backend.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatException;

@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class TokenJwtServiceTest {
    @Autowired
    private TokenJwtService tokenJwtService;
    private User testUser;
    private String testToken;

    @BeforeEach
    void setUp(){
//        Test Token
        this.testToken = "eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJIZWxwIERlc2sgQW5ndWxhciAmIEphdmEiLCJzdWIiOiJ0ZXN0QGVtYWlsLmNvbSIsInByb2ZpbGVQaWN0dXJlIjoiVGVzdCBQcm9maWxlIFBpY3R1cmUiLCJyb2xlIjoiVGVzdCBSb2xlIiwicm9sZUlkIjoiYTY1ZTA0ZTUtNjVkOS00ZmFlLWI1MWQtNDUyMzBjYjQ4ZjljIiwibmFtZSI6IlRlc3QgTmFtZSIsImlkIjoiODA5YjgyYWYtZWZiZS00ZGEzLTk2YWItZTExN2ZmY2VlNGQ2In0.b0PLneCoTPxF2wXXQxrcv1LeKUVJvuR66s0oRo98p0A";

//        Test User
        Role testRole = new Role();
        testRole.setId(UUID.fromString("a65e04e5-65d9-4fae-b51d-45230cb48f9c"));
        testRole.setTitle("Test Role");

        User testUser = new User();
        testUser.setId(UUID.fromString("809b82af-efbe-4da3-96ab-e117ffcee4d6"));
        testUser.setName("Test Name");
        testUser.setEmail("test@email.com");
        testUser.setRole(testRole);
        testUser.setProfilePicture("Test Profile Picture");

        this.testUser = testUser;
    }

    @Nested
    class generateToken {

        @Test
        void givenAUser_whenRequesting_thenGenerateToken(){
//            Action
            String methodResponse = tokenJwtService.generateToken(testUser);

//            Assert
            assertThat(methodResponse)
                .isEqualTo(testToken);

        }
    }

    @Nested
    class validateToken {
        @Test
        void givenAValidToken_whenRequesting_thenReturnSubject(){
//            Action
            String methodResponse = tokenJwtService.validateToken(testToken);

//            Assert
            assertThat(methodResponse)
                    .isEqualTo(testUser.getEmail());
        }

        @Test
        void givenAInvalidToken_whenRequesting_thenThrowsInvalidTokenException(){
//            Action
            String invalidToken = "Invalid Token";

//            Assert
            assertThatException()
                    .isThrownBy(() -> tokenJwtService.validateToken(invalidToken))
                    .isInstanceOf(InvalidTokenException.class)
                    .withMessage("Token inválido.");
        }
    }
}