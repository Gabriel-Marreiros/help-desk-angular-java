package br.com.gabrielmarreiros.backend.dto.technical;

import br.com.gabrielmarreiros.backend.enums.UserStatusEnum;
import br.com.gabrielmarreiros.backend.models.Role;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

public record TechnicalResponseDTO(
        UUID id,
        String name,
        String email,
        String phoneNumber,
        @JsonFormat(pattern = "yyyy-MM-dd")
        Date dateBirth,
        String profilePicture,
        Role role,
        String userStatus
) { }
