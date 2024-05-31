package br.com.gabrielmarreiros.backend.dto.ticket;

import br.com.gabrielmarreiros.backend.dto.customer.CustomerResponseDTO;
import br.com.gabrielmarreiros.backend.dto.technical.TechnicalResponseDTO;
import br.com.gabrielmarreiros.backend.enums.TicketStatusEnum;
import br.com.gabrielmarreiros.backend.models.Customer;
import br.com.gabrielmarreiros.backend.models.Priority;
import br.com.gabrielmarreiros.backend.models.Technical;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

public record TicketResponseDTO(
        UUID id,
        String code,
        String title,
        String description,
        CustomerResponseDTO customer,
        TechnicalResponseDTO technical,
        Date openingDate,
        Date closedDate,
        Priority priority,
        String ticketStatus
) { }