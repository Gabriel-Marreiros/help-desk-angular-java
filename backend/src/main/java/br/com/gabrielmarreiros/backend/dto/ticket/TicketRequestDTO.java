package br.com.gabrielmarreiros.backend.dto.ticket;

import br.com.gabrielmarreiros.backend.models.Priority;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

public record TicketRequestDTO(
   String title,
   UUID technical,
   UUID customer,
   String description,
   Date openingDate,
   UUID priority,
   String status
) { }
