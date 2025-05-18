package br.com.gabrielmarreiros.backend.dto.ticketComment;

import java.util.Date;
import java.util.UUID;

public record TicketCommentRequestDTO(
        String comment,
        UUID user,
        UUID ticket,
        Date createdAt
) { }
