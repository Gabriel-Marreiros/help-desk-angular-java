package br.com.gabrielmarreiros.backend.services;

import br.com.gabrielmarreiros.backend.dto.ticket.AllTicketsStatusSummaryDTO;
import br.com.gabrielmarreiros.backend.dto.ticket.TicketFiltersDTO;
import br.com.gabrielmarreiros.backend.dto.ticket.TicketUpdateDTO;
import br.com.gabrielmarreiros.backend.enums.TicketStatusEnum;
import br.com.gabrielmarreiros.backend.exceptions.InvalidRequestException;
import br.com.gabrielmarreiros.backend.exceptions.InvalidTicketStatusException;
import br.com.gabrielmarreiros.backend.exceptions.TicketNotFoundException;
import br.com.gabrielmarreiros.backend.exceptions.UnauthorizedException;
import br.com.gabrielmarreiros.backend.models.*;
import br.com.gabrielmarreiros.backend.repositories.TicketRepository;
import br.com.gabrielmarreiros.backend.utils.TicketUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.contains;
import static org.springframework.data.domain.ExampleMatcher.matching;

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
        UUID customerId = ticket.getCustomer().getId();
        Customer customer = this.customerService.getCustomerById(customerId);
        ticket.setCustomer(customer);

        if(ticket.getTechnical() != null){
            User loggedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            if(!loggedUser.isAdmin()){
                throw new UnauthorizedException();
            }

            UUID technicalId = ticket.getTechnical().getId();
            Technical technical = this.technicalService.getTechnicalById(technicalId);
            ticket.setTechnical(technical);
        }

        String ticketStatus = ticket.getTechnical() != null ? TicketStatusEnum.PENDING.getValue() : TicketStatusEnum.NEW_TICKET.getValue();

        ticket.setTicketStatus(ticketStatus);

        ticket.generateTicketCode();
        ticket.generateSearchTerm();

        Ticket savedTicket = this.ticketRepository.save(ticket);

        return savedTicket;
    }

    @Transactional
    public Ticket updateTicketStatus(UUID ticketId, String newTicketStatus) {
        Ticket ticket = this.ticketRepository.findById(ticketId)
                .orElseThrow(TicketNotFoundException::new);

        boolean ticketIsClosed = this.ticketIsClosed(ticket);

        if(ticketIsClosed){
            throw new InvalidRequestException();
        }

        boolean canChangeTicketStatus = this.canEditTicket(ticket);

        if(!canChangeTicketStatus){
            throw new UnauthorizedException();
        }

        if (!TicketUtils.ticketStatusIsValid(newTicketStatus)){
            throw new InvalidTicketStatusException();
        }

        ticket.setTicketStatus(newTicketStatus);

        if(newTicketStatus.equals(TicketStatusEnum.RESOLVED.getValue()) || newTicketStatus.equals(TicketStatusEnum.CANCELED.getValue())){
            ticket.setClosedDate(new Date());
        }

        Ticket updatedTicket = this.ticketRepository.save(ticket);;

        return updatedTicket;
    }

    public AllTicketsStatusSummaryDTO getAllTicketsStatusSummary(){
        return this.ticketRepository.getAllTicketsStatusSummary();
    }

    public Page<Ticket> getTicketsPaginated(PageRequest pageRequest) {
        return this.ticketRepository.findAll(pageRequest);
    }

    public Page<Ticket> getTicketsWithFiltersPaginated(TicketFiltersDTO ticketFiltersDTO, PageRequest pageRequest) {
        if (ticketFiltersDTO.status() != null && !TicketUtils.ticketStatusIsValid(ticketFiltersDTO.status())){
            throw new InvalidTicketStatusException();
        };

        Ticket ticketFilter = new Ticket();
        ticketFilter.setCustomer(new Customer(ticketFiltersDTO.customer()));
        ticketFilter.setPriority(new Priority(ticketFiltersDTO.priority()));
        ticketFilter.setTicketStatus(ticketFiltersDTO.status());
        ticketFilter.setSearchTerm(ticketFiltersDTO.search());

        if(ticketFiltersDTO.technical() != null){
            ticketFilter.setTechnical(new Technical(ticketFiltersDTO.technical()));
        }

        ExampleMatcher filterMatcher = matching().withMatcher("searchTerm", contains().ignoreCase());

        Example<Ticket> ticketExample = Example.of(ticketFilter, filterMatcher);

        Page<Ticket> ticketsPage = this.ticketRepository.findAll(ticketExample, pageRequest);

        return ticketsPage;
    }

    public Ticket updateTicket(UUID ticketId, TicketUpdateDTO ticketUpdate) {
        Ticket ticketEntity = this.ticketRepository.findById(ticketId)
                .orElseThrow(TicketNotFoundException::new);

        boolean canUpdateTicket = this.canEditTicket(ticketEntity);

        if(!canUpdateTicket){
            throw new UnauthorizedException();
        }

        boolean ticketIsClosed = this.ticketIsClosed(ticketEntity);

        if(ticketIsClosed){
            throw new InvalidRequestException();
        }

        boolean hasCustomerOrTechnicalUpdate = ticketUpdate.technicalId() != null || ticketUpdate.customerId() != null;

        if(hasCustomerOrTechnicalUpdate){
            User loggedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            if(!loggedUser.isAdmin()){
                throw new InvalidRequestException();
            }

            ticketEntity.setTechnical(new Technical(ticketUpdate.technicalId()));
            ticketEntity.setCustomer(new Customer(ticketUpdate.customerId()));
        }

        ticketEntity.setTitle(ticketUpdate.title());
        ticketEntity.setDescription(ticketUpdate.description());
        ticketEntity.setPriority(new Priority(ticketUpdate.priorityId()));

        ticketEntity.generateSearchTerm();

        Ticket updatedTicket = this.ticketRepository.save(ticketEntity);

        return updatedTicket;
    }

    public Ticket assignTicketTechnical(UUID ticketId, UUID technicalId){
        Ticket ticket = this.ticketRepository.findById(ticketId)
                .orElseThrow(TicketNotFoundException::new);

        if(ticket.getTechnical() != null){
            throw new InvalidRequestException();
        }

        Technical technical = this.technicalService.getTechnicalById(technicalId);

        if(!technical.isEnabled()){
            throw new InvalidRequestException();
        }

        ticket.setTechnical(technical);
        ticket.setTicketStatus(TicketStatusEnum.PENDING.getValue());

        Ticket updatedTicket = ticketRepository.save(ticket);

        return updatedTicket;
    }

    public boolean canEditTicket(Ticket ticket) {
        User loggedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if(loggedUser.isAdmin()){
            return true;
        }

        UUID ticketCustomerId = ticket.getCustomer().getId();
        UUID loggedUserId = loggedUser.getId();

        if(loggedUserId.equals(ticketCustomerId)){
            return true;
        }

        return false;
    }

    public boolean ticketIsClosed(Ticket ticket){
        String oldTicketStatus = ticket.getTicketStatus();

        if(oldTicketStatus.equals(TicketStatusEnum.RESOLVED.getValue()) || oldTicketStatus.equals(TicketStatusEnum.CANCELED.getValue())){
            return true;
        }

        return false;
    }
}
