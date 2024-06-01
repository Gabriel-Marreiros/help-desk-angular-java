package br.com.gabrielmarreiros.backend.dto.technical;

import br.com.gabrielmarreiros.backend.enums.UserStatusEnum;

import java.util.UUID;

public record TechnicalWithTicketStatusCountDTO(
    UUID id,
    String name,
    String profilePicture,
    String userStatus,
    Long ticketsPending,
    Long ticketsInProgress,
    Long ticketsResolved
) {
}
