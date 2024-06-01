package br.com.gabrielmarreiros.backend.dto.ticket;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public record TicketUpdateDTO(
        String title,
        String description,
        @JsonProperty("technical")
        UUID technicalId,
        @JsonProperty("priority")
        UUID priorityId
) {
}
