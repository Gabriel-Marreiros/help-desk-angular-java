package br.com.gabrielmarreiros.backend.controllers;

import br.com.gabrielmarreiros.backend.dto.ticket.*;
import br.com.gabrielmarreiros.backend.exceptions.ErrorResponse;
import br.com.gabrielmarreiros.backend.mappers.TicketMapper;
import br.com.gabrielmarreiros.backend.models.Ticket;
import br.com.gabrielmarreiros.backend.services.TicketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Tickets", description = "Recursos para manipulação de chamados")
@RestController
@RequestMapping("/tickets")
public class TicketController {
    private final TicketService ticketService;
    private final TicketMapper ticketMapper;
    TicketController(TicketService ticketService, TicketMapper ticketMapper){
        this.ticketService = ticketService;
        this.ticketMapper = ticketMapper;
    }

    @Operation(
            summary = "Get All Tickets",
            description = "Retorna uma lista de todos os chamados do sistema",
            responses = {@ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json"))}
    )
    @GetMapping
    public ResponseEntity<List<TicketResponseDTO>> getAllTickets(){
        List<Ticket> ticketsList = this.ticketService.getAllTickets();

        List<TicketResponseDTO> ticketsListResponse = this.ticketMapper.toResponseListDTO(ticketsList);

        return ResponseEntity.status(HttpStatus.OK).body(ticketsListResponse);
    }

    @Operation(
            summary = "Get Tickets By With Filters Paginated",
            description = "Retorna uma lista de chamados filtrados de forma páginada",
            parameters = {
                    @Parameter(name = "page", description = "Número da página", required = true),
                    @Parameter(name = "size", description = "Quantidade de items que devem ser retornados", required = true),
                    @Parameter(name = "technical", description = "Id do técnico dos chamados que devem ser retornados"),
                    @Parameter(name = "customer", description = "Id do cliente dos chamados que devem ser retornados"),
                    @Parameter(name = "status", description = "Valor do status dos chamados que devem ser retornados"),
                    @Parameter(name = "priority", description = "Valor da prioridade dos chamados que devem ser retornados"),
                    @Parameter(name = "search", description = "Valor de pesquisa")
            },
            responses = {
                    @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json"))
            }
    )
    @GetMapping(path = "/paginated")
    public ResponseEntity<Page<TicketResponseDTO>> getTicketsPaginated(
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam(required = false) UUID technical,
            @RequestParam(required = false) UUID customer,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) UUID priority,
            @RequestParam(required = false) String search
    ){

        PageRequest pageRequest = PageRequest.of(page, size);

        TicketFiltersDTO ticketFiltersDTO = new TicketFiltersDTO(
                technical,
                customer,
                status,
                priority,
                search
        );

        Page<Ticket> ticketsPage = this.ticketService.getTicketsWithFiltersPaginated(ticketFiltersDTO, pageRequest);

        Page<TicketResponseDTO> ticketsResponsePage = this.ticketMapper.toResponsePageDTO(ticketsPage);

        return ResponseEntity.status(HttpStatus.OK).body(ticketsResponsePage);
    }

    @Operation(
            summary = "Get Ticket By ID",
            description = "Retorna as informações do chamado do ID informado",
            responses = {
                    @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json"))
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<TicketResponseDTO> getTicketById(@PathVariable("id") UUID id){
        Ticket ticket = this.ticketService.getTicketById(id);

        TicketResponseDTO ticketResponse = this.ticketMapper.toResponseDTO(ticket);

        return ResponseEntity.status(HttpStatus.OK).body(ticketResponse);
    }

    @Operation(
            summary = "Save Ticket",
            description = "Realiza o registro de um chamado",
            responses = {@ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json"))}
    )
    @PostMapping
    public ResponseEntity<TicketResponseDTO> saveTicket(@RequestBody TicketRequestDTO ticketRequest){
        Ticket ticketEntity = this.ticketMapper.toEntity(ticketRequest);

        Ticket savedTicket = this.ticketService.saveTicket(ticketEntity);

        TicketResponseDTO savedTicketResponse = this.ticketMapper.toResponseDTO(savedTicket);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedTicketResponse);
    }

    @Operation(
            summary = "Update Ticket",
            description = "Realiza a atualização de um chamado",
            responses = {
                    @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json"))
            }
    )
    @PutMapping("{id}")
    public ResponseEntity<TicketResponseDTO> updateTicket(@PathVariable("id") UUID id, @RequestBody TicketUpdateDTO ticketUpdate){
        Ticket updatedTicketEntity = this.ticketService.updateTicket(id, ticketUpdate);

        TicketResponseDTO updatedTicketResponse = this.ticketMapper.toResponseDTO(updatedTicketEntity);

        return ResponseEntity.status(HttpStatus.OK).body(updatedTicketResponse);
    }

    @PatchMapping("{id}/assign-technical")
    public ResponseEntity<TicketResponseDTO> assignTicketTechnical(@PathVariable("id") UUID ticketId, @RequestBody AssignTicketTechnicalDTO assignTicketTechnicalDTO){
        Ticket ticket = this.ticketService.assignTicketTechnical(ticketId, assignTicketTechnicalDTO.technicalId());

        TicketResponseDTO ticketResponseDTO = this.ticketMapper.toResponseDTO(ticket);

        return ResponseEntity.status(HttpStatus.OK).body(ticketResponseDTO);
    }

    @Operation(
            summary = "Update Ticket Status",
            description = "Realiza a atualização do status do chamado informado",
            responses = {
                    @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")),
                    @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json"))
            }
    )
    @PutMapping("/update-status/{id}")
    public ResponseEntity<TicketResponseDTO> updateTicketStatus(@PathVariable("id") UUID ticketId, @RequestBody String newStatus) {
        Ticket ticket = this.ticketService.updateTicketStatus(ticketId, newStatus);

        TicketResponseDTO ticketResponseDTO = this.ticketMapper.toResponseDTO(ticket);

        return ResponseEntity.status(HttpStatus.OK).body(ticketResponseDTO);
    }

    @Operation(
            summary = "Get Status Summary Of All Tickets",
            description = "Retorna informações referentes ao status de todos os chamados do sistema",
            responses = {@ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json"))}
    )
    @GetMapping("/all-tickets-summary-status")
    public ResponseEntity<AllTicketsStatusSummaryDTO> getAllTicketsStatusSummary(){
        AllTicketsStatusSummaryDTO allTicketsStatusSummary = this.ticketService.getAllTicketsStatusSummary();

        return ResponseEntity.status(HttpStatus.OK).body(allTicketsStatusSummary);
    }

}
