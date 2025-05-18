package br.com.gabrielmarreiros.backend.dto.user;

import br.com.gabrielmarreiros.backend.models.Role;

import java.util.UUID;

public record UserResponseDTO(
        UUID id,
        String name,
        String email,
        String phoneNumber,
        String profilePicture,
        Role role,
        String userStatus
) {
}
