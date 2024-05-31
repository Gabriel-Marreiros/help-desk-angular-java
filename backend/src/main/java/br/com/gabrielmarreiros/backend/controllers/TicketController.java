package br.com.gabrielmarreiros.backend.controllers;

import br.com.gabrielmarreiros.backend.dto.ticket.AllTicketsStatusSummaryDTO;
import br.com.gabrielmarreiros.backend.dto.ticket.TicketRequestDTO;
import br.com.gabrielmarreiros.backend.dto.ticket.TicketResponseDTO;
import br.com.gabrielmarreiros.backend.dto.ticket.TicketUpdateDTO;
import br.com.gabrielmarreiros.backend.mappers.TicketMapper;
import br.com.gabrielmarreiros.backend.models.Ticket;
import br.com.gabrielmarreiros.backend.repositories.TicketRepository;
import br.com.gabrielmarreiros.backend.services.TicketService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/tickets")
public class TicketController {
    private final TicketService ticketService;
    private final TicketMapper ticketMapper;
    TicketController(TicketService ticketService, TicketMapper ticketMapper){
        this.ticketService = ticketService;
        this.ticketMapper = ticketMapper;
    }

    @GetMapping
    public ResponseEntity<List<TicketResponseDTO>> getAllTickets(){
        List<Ticket> ticketsList = this.ticketService.getAllTickets();

        List<TicketResponseDTO> ticketsListResponse = this.ticketMapper.toResponseListDTO(ticketsList);

        return ResponseEntity.status(HttpStatus.OK).body(ticketsListResponse);
    }

    @GetMapping("/paginated")
    public ResponseEntity<Page<TicketResponseDTO>> getTicketsPaginated(@RequestParam int page, @RequestParam int size, @RequestParam(required = false) Optional<String> status){
        PageRequest pageRequest = PageRequest.of(page, size);

        Page<Ticket> ticketsPage;

        if(status.isPresent()){
            ticketsPage = this.ticketService.getTicketsByStatusPaginated(pageRequest, status.get());
        }
        else{
            ticketsPage = this.ticketService.getTicketsPaginated(pageRequest);
        }

        Page<TicketResponseDTO> ticketsResponsePage = this.ticketMapper.toResponsePageDTO(ticketsPage);

        return ResponseEntity.status(HttpStatus.OK).body(ticketsResponsePage);
    }

    @GetMapping("/by-search-term/paginated")
    public ResponseEntity<Page<TicketResponseDTO>> getTicketsPaginatedBySearchTerm(@RequestParam int page, @RequestParam int size, @RequestParam String searchTerm){
        PageRequest pageRequest = PageRequest.of(page, size);

        Page<Ticket> ticketsPage = this.ticketService.getTicketsBySearchTermPaginated(pageRequest, searchTerm);

        Page<TicketResponseDTO> ticketsResponsePage = this.ticketMapper.toResponsePageDTO(ticketsPage);

        return ResponseEntity.status(HttpStatus.OK).body(ticketsResponsePage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TicketResponseDTO> getTicketById(@PathVariable("id") UUID id){
        Ticket ticket = this.ticketService.getTicketById(id);
        TicketResponseDTO ticketResponse = this.ticketMapper.toResponseDTO(ticket);
        return ResponseEntity.status(HttpStatus.OK).body(ticketResponse);
    }

    @PostMapping
    public ResponseEntity<TicketResponseDTO> saveTicket(@RequestBody TicketRequestDTO ticketRequest){
        Ticket ticketEntity = this.ticketMapper.toEntity(ticketRequest);

        Ticket savedTicket = this.ticketService.saveTicket(ticketEntity);

        TicketResponseDTO savedTicketResponse = this.ticketMapper.toResponseDTO(savedTicket);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedTicketResponse);
    }

    @PutMapping("{id}")
    public ResponseEntity<TicketResponseDTO> updateTicket(@PathVariable("id") UUID id, @RequestBody TicketUpdateDTO ticketUpdate){
        Ticket updatedTicketEntity = this.ticketService.updateTicket(id, ticketUpdate);

        TicketResponseDTO updatedTicketResponse = this.ticketMapper.toResponseDTO(updatedTicketEntity);

        return ResponseEntity.status(HttpStatus.OK).body(updatedTicketResponse);
    }

    @PutMapping("/update-status/{id}")
    public ResponseEntity<String> updateStatus(@PathVariable("id") UUID ticketId, @RequestBody String newStatus) {
        this.ticketService.updateStatus(ticketId, newStatus);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/all-tickets-summary-status")
    public ResponseEntity<AllTicketsStatusSummaryDTO> getAllTicketsStatusSummary(){
        AllTicketsStatusSummaryDTO allTicketsStatusSummary = this.ticketService.getAllTicketsStatusSummary();
        return ResponseEntity.status(HttpStatus.OK).body(allTicketsStatusSummary);
    }

}
