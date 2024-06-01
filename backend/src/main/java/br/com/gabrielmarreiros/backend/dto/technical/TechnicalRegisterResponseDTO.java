package br.com.gabrielmarreiros.backend.dto.technical;

import br.com.gabrielmarreiros.backend.enums.UserStatusEnum;
import br.com.gabrielmarreiros.backend.models.Role;

import java.time.LocalDate;
import java.util.UUID;

public record TechnicalRegisterResponseDTO(
        TechnicalResponseDTO technical,
        String token
) {
}
