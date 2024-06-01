package br.com.gabrielmarreiros.backend.dto.technical;

import br.com.gabrielmarreiros.backend.enums.UserStatusEnum;

import java.time.LocalDate;
import java.util.Date;

public record TechnicalRegisterRequestDTO(
        String email,
        String password,
        String name,
        String phoneNumber,
        Date dateBirth,
        String profilePicture,
        String userStatus
) {

}
