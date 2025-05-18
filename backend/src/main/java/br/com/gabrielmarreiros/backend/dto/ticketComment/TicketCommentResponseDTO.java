package br.com.gabrielmarreiros.backend.dto.ticketComment;

import br.com.gabrielmarreiros.backend.dto.user.UserResponseDTO;
import br.com.gabrielmarreiros.backend.models.User;

import java.util.Date;
import java.util.UUID;

public record TicketCommentResponseDTO(
    UUID id,
    String comment,
    UserResponseDTO user,
    UUID ticket,
    boolean edited,
    Date createdAt,
    Date updatedAt
) { }
