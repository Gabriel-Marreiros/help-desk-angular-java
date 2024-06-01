package br.com.gabrielmarreiros.backend.dto.ticket;

public record AllTicketsStatusSummaryDTO(
        Long pending,
        Long inProgress,
        Long resolved
){}
