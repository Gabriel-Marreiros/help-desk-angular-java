package br.com.gabrielmarreiros.backend.mappers;

import br.com.gabrielmarreiros.backend.dto.ticketComment.TicketCommentRequestDTO;
import br.com.gabrielmarreiros.backend.dto.ticketComment.TicketCommentResponseDTO;
import br.com.gabrielmarreiros.backend.dto.user.UserResponseDTO;
import br.com.gabrielmarreiros.backend.exceptions.UserNotFoundException;
import br.com.gabrielmarreiros.backend.models.Ticket;
import br.com.gabrielmarreiros.backend.models.TicketComment;
import br.com.gabrielmarreiros.backend.models.User;
import br.com.gabrielmarreiros.backend.repositories.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TicketCommentMapper {

    private final UserRepository userRepository;

    public TicketCommentMapper(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public TicketComment toEntity(TicketCommentRequestDTO ticketCommentDTO){
        User user = this.userRepository.findById(ticketCommentDTO.user()).orElseThrow(UserNotFoundException::new);

        Ticket ticket = new Ticket();
        ticket.setId(ticketCommentDTO.ticket());

        TicketComment ticketCommentEntity = new TicketComment();
        ticketCommentEntity.setComment(ticketCommentDTO.comment());
        ticketCommentEntity.setCreatedAt(ticketCommentDTO.createdAt());
        ticketCommentEntity.setUser(user);
        ticketCommentEntity.setTicket(ticket);

        return ticketCommentEntity;
    }

    public TicketCommentResponseDTO toResponseDTO(TicketComment ticketCommentEntity){
        UserResponseDTO userResponseDTO = new UserResponseDTO(
                ticketCommentEntity.getUser().getId(),
                ticketCommentEntity.getUser().getName(),
                ticketCommentEntity.getUser().getEmail(),
                ticketCommentEntity.getUser().getPhoneNumber(),
                ticketCommentEntity.getUser().getProfilePicture(),
                ticketCommentEntity.getUser().getRole(),
                ticketCommentEntity.getUser().getUserStatus()
        );

        return new TicketCommentResponseDTO(
                ticketCommentEntity.getId(),
                ticketCommentEntity.getComment(),
                userResponseDTO,
                ticketCommentEntity.getTicket().getId(),
                ticketCommentEntity.isEdited(),
                ticketCommentEntity.getCreatedAt(),
                ticketCommentEntity.getUpdatedAt()
        );
    }

    public List<TicketCommentResponseDTO> toResponseListDTO(List<TicketComment> ticketCommentEntityList){
        return ticketCommentEntityList.stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public Page<TicketCommentResponseDTO> toPageResponseDTO(Page<TicketComment> ticketCommentsPage){
        return ticketCommentsPage.map(this::toResponseDTO);
    }
}
