package br.com.gabrielmarreiros.backend.services;

import br.com.gabrielmarreiros.backend.dto.ticketComment.TicketCommentUpdateDTO;
import br.com.gabrielmarreiros.backend.exceptions.TicketCommentNotFoundException;
import br.com.gabrielmarreiros.backend.exceptions.TicketNotFoundException;
import br.com.gabrielmarreiros.backend.exceptions.UnauthorizedException;
import br.com.gabrielmarreiros.backend.models.*;
import br.com.gabrielmarreiros.backend.repositories.TicketCommentRepository;
import br.com.gabrielmarreiros.backend.repositories.TicketRepository;
import br.com.gabrielmarreiros.backend.repositories.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.UUID;

@Service
public class TicketCommentService {

    private final TicketCommentRepository ticketCommentRepository;
    private final TicketService ticketService;
    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;

    public TicketCommentService(TicketCommentRepository ticketCommentRepository, TicketService ticketService, TicketRepository ticketRepository, UserRepository userRepository) {
        this.ticketCommentRepository = ticketCommentRepository;
        this.ticketService = ticketService;
        this.ticketRepository = ticketRepository;
        this.userRepository = userRepository;
    }

    public Page<TicketComment> getAllTicketCommentsPaginated(UUID ticketId, Pageable pageRequest) {
        boolean ticketExists = this.ticketRepository.existsById(ticketId);

        if(!ticketExists){
            throw new TicketNotFoundException();
        }

        Page<TicketComment> ticketCommentsList = this.ticketCommentRepository.findByTicketIdOrderByCreatedAtDesc(ticketId, pageRequest);

        return ticketCommentsList;
    }

    @Transactional
    public TicketComment saveComment(TicketComment ticketComment) {
        UUID ticketId = ticketComment.getTicket().getId();
        UUID userId = ticketComment.getUser().getId();

        boolean ticketExists = this.ticketRepository.existsById(ticketId);
        boolean userExists = this.userRepository.existsById(userId);

        if(!ticketExists || !userExists){
            throw new RuntimeException();
        }

        Ticket ticket = this.ticketRepository.findById(ticketId).get();

        boolean canSaveComment = this.canInsertCommentInTicket(ticket);

        if(!canSaveComment){
            throw new UnauthorizedException();
        }

        TicketComment createdTicketComment = this.ticketCommentRepository.save(ticketComment);

        return createdTicketComment;
    }

    public TicketComment updateComment(UUID ticketCommentId, TicketCommentUpdateDTO ticketCommentUpdate) {
        TicketComment ticketCommentEntity = this.ticketCommentRepository.findById(ticketCommentId)
                .orElseThrow(TicketCommentNotFoundException::new);

        UUID ticketCommentUserId = ticketCommentEntity.getUser().getId();

        User loggedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        boolean canUpdateComment = loggedUser.getId().equals(ticketCommentUserId);

        if(!canUpdateComment){
            throw new UnauthorizedException();
        }

        ticketCommentEntity.setComment(ticketCommentUpdate.comment());
        ticketCommentEntity.setUpdatedAt(new Date());

        if(!ticketCommentEntity.isEdited()){
            ticketCommentEntity.setEdited(true);
        }

        TicketComment updatedComment = this.ticketCommentRepository.save(ticketCommentEntity);

        return updatedComment;
    }

    public void deleteComment(UUID ticketCommentId) {
        TicketComment ticketCommentEntity = this.ticketCommentRepository.findById(ticketCommentId)
                .orElseThrow(TicketCommentNotFoundException::new);

        UUID ticketCommentUserId = ticketCommentEntity.getUser().getId();

        User loggedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        boolean canDeleteComment = loggedUser.getId().equals(ticketCommentUserId);

        if(!canDeleteComment){
            throw new UnauthorizedException();
        }

        this.ticketCommentRepository.deleteById(ticketCommentId);
    }
    
    public boolean canInsertCommentInTicket(Ticket ticket){
        User loggedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        if(loggedUser.isAdmin()){
            return true;
        }
        
        UUID loggedUserId = loggedUser.getId();
        
        UUID ticketCustomerId = ticket.getCustomer().getId();
        UUID ticketTechnicalId = ticket.getTechnical().getId();
        
        if(loggedUserId.equals(ticketCustomerId) || loggedUserId.equals(ticketTechnicalId)){
            return true;
        }
        
        return false;
    }
}
