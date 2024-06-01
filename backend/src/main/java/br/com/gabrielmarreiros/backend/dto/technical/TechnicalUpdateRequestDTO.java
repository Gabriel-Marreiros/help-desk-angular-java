package br.com.gabrielmarreiros.backend.dto.technical;

import java.util.Date;

public record TechnicalUpdateRequestDTO(
        String name,
        String email,
        Date dateBirth,
        String phoneNumber,
        String profilePicture
) {}
