package br.com.gabrielmarreiros.backend.dto.ticket;

import java.util.UUID;

public record TicketFiltersDTO(
        UUID technical,
        UUID customer,
        String status,
        UUID priority,
        String search
) {
}
