package br.com.gabrielmarreiros.backend.dto.customer;

import br.com.gabrielmarreiros.backend.enums.UserStatusEnum;
import br.com.gabrielmarreiros.backend.models.Role;

import java.util.UUID;

public record CustomerResponseDTO(
    UUID customerId,
    UUID userId,
    String name,
    String email,
    String phoneNumber,
    String cnpj,
    String profilePicture,
    Role role,
    String userStatus
) { }
