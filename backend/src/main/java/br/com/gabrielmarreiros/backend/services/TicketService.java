package br.com.gabrielmarreiros.backend.services;

import br.com.gabrielmarreiros.backend.dto.ticket.AllTicketsStatusSummaryDTO;
import br.com.gabrielmarreiros.backend.dto.ticket.TicketUpdateDTO;
import br.com.gabrielmarreiros.backend.enums.TicketStatusEnum;
import br.com.gabrielmarreiros.backend.exceptions.InvalidTicketStatusException;
import br.com.gabrielmarreiros.backend.exceptions.TicketNotFoundException;
import br.com.gabrielmarreiros.backend.models.Customer;
import br.com.gabrielmarreiros.backend.models.Priority;
import br.com.gabrielmarreiros.backend.models.Technical;
import br.com.gabrielmarreiros.backend.models.Ticket;
import br.com.gabrielmarreiros.backend.repositories.TicketRepository;
import br.com.gabrielmarreiros.backend.utils.TicketUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;
    private final TechnicalService technicalService;
    private final CustomerService customerService;

    public TicketService(TicketRepository ticketRepository, TechnicalService technicalService, CustomerService customerService) {
        this.ticketRepository = ticketRepository;
        this.technicalService = technicalService;
        this.customerService = customerService;
    }

    public List<Ticket> getAllTickets(){
        List<Ticket> ticketsList = this.ticketRepository.findAll();

        return ticketsList;
    }

    public Ticket getTicketById(UUID id){
        Ticket ticketEntity = this.ticketRepository.findById(id)
                                                    .orElseThrow(TicketNotFoundException::new);

        return ticketEntity;
    }

    @Transactional
    public Ticket saveTicket(Ticket ticket){
        Technical technical = this.technicalService.getTechnicalById(ticket.getTechnical().getId());
        Customer customer = this.customerService.getCustomerById(ticket.getCustomer().getId());

        ticket.setTechnical(technical);
        ticket.setCustomer(customer);
        ticket.setTicketStatus(TicketStatusEnum.PENDING.getValue());

        ticket.generateTicketCode();
        ticket.generateSearchTerm();

        Ticket savedTicket = this.ticketRepository.saveAndFlush(ticket);

        return savedTicket;
    }

    @Transactional
    public void updateStatus(UUID ticketId, String newTicketStatus) {
        if (!TicketUtils.ticketStatusIsValid(newTicketStatus)){
            throw new InvalidTicketStatusException();
        }

        int updateCount = this.ticketRepository.updateTicketStatus(ticketId, newTicketStatus);

        if(updateCount == 0){
            throw new TicketNotFoundException();
        }
    }

    public AllTicketsStatusSummaryDTO getAllTicketsStatusSummary(){
        return this.ticketRepository.getAllTicketsStatusSummary();
    }

    public Page<Ticket> getTicketsPaginated(PageRequest pageRequest) {
        return this.ticketRepository.findAll(pageRequest);
    }

    public Page<Ticket> getTicketsByStatusPaginated(PageRequest pageRequest, String status) {
        if (!TicketUtils.ticketStatusIsValid(status)){
            throw new InvalidTicketStatusException();
        }

        return this.ticketRepository.findByTicketStatus(status, pageRequest);
    }

    public Ticket updateTicket(UUID id, TicketUpdateDTO ticketUpdate) {
        Ticket ticketEntity = this.ticketRepository.findById(id).orElseThrow(TicketNotFoundException::new);

        ticketEntity.setTitle(ticketUpdate.title());
        ticketEntity.setDescription(ticketUpdate.description());
        ticketEntity.setTechnical(new Technical(ticketUpdate.technicalId()));
        ticketEntity.setPriority(new Priority(ticketUpdate.priorityId()));

        Ticket updatedTicket = this.ticketRepository.save(ticketEntity);

        updatedTicket.generateSearchTerm();

        this.ticketRepository.save(updatedTicket);

        return updatedTicket;
    }

    public Page<Ticket> getTicketsBySearchTermPaginated(PageRequest pageRequest, String searchTerm) {
        return this.ticketRepository.findBySearchTermIgnoreCaseContaining(searchTerm, pageRequest);
    }
}
