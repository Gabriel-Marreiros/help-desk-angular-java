package br.com.gabrielmarreiros.backend.dto.customer;

import br.com.gabrielmarreiros.backend.enums.UserStatusEnum;
import br.com.gabrielmarreiros.backend.models.Role;

import java.util.UUID;

public record CustomerRegisterResponseDTO(
        CustomerResponseDTO customer,
        String token
) { }
