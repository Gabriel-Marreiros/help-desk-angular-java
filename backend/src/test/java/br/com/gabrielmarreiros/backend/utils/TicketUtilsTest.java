package br.com.gabrielmarreiros.backend.utils;

import br.com.gabrielmarreiros.backend.enums.TicketStatusEnum;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TicketUtilsTest {

    @Nested
    class ticketStatusIsValid {

        @Test
        void givenAValidTicketStatus_whenRequesting_thenReturnTrue(){
//            Arrange
            String validStatus = TicketStatusEnum.RESOLVED.getValue();

//            Action
            boolean methodResponse = TicketUtils.ticketStatusIsValid(validStatus);

//            Assert
            assertThat(methodResponse)
                .isTrue();
        }

        @Test
        void givenAInvalidTicketStatus_whenRequesting_thenReturnFalse(){
//            Arrange
            String invalidStatus = "Invalid Status";

//            Action
            boolean methodResponse = TicketUtils.ticketStatusIsValid(invalidStatus);

//            Assert
            assertThat(methodResponse)
                .isFalse();
        }
    }
}