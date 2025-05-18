package br.com.gabrielmarreiros.backend.controllers;

import br.com.gabrielmarreiros.backend.dto.ticketComment.TicketCommentRequestDTO;
import br.com.gabrielmarreiros.backend.dto.ticketComment.TicketCommentResponseDTO;
import br.com.gabrielmarreiros.backend.dto.ticketComment.TicketCommentUpdateDTO;
import br.com.gabrielmarreiros.backend.mappers.TicketCommentMapper;
import br.com.gabrielmarreiros.backend.models.TicketComment;
import br.com.gabrielmarreiros.backend.services.TicketCommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Tickets Comments", description = "Recursos para manipulação dos comentários de um chamado")
@RestController
@RequestMapping("/ticket/comments")
public class TicketCommentController {

    private final TicketCommentService ticketCommentService;
    private final TicketCommentMapper ticketCommentMapper;

    public TicketCommentController(TicketCommentService ticketCommentService, TicketCommentMapper ticketCommentMapper) {
        this.ticketCommentService = ticketCommentService;
        this.ticketCommentMapper = ticketCommentMapper;
    }

    @Operation(
            summary = "Get All Ticket Comments Paginated",
            description = "Retorna todos os comentários de um chamado",
            responses = {
                @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json"))
            }
    )
    @GetMapping("/{ticketId}")
    public ResponseEntity<Page<TicketCommentResponseDTO>> getAllTicketCommentsPaginated(@PathVariable UUID ticketId, @RequestParam int page, @RequestParam int size){
        PageRequest pageRequest = PageRequest.of(page, size);

        Page<TicketComment> ticketComments = this.ticketCommentService.getAllTicketCommentsPaginated(ticketId, pageRequest);

        Page<TicketCommentResponseDTO> ticketCommentResponseList = ticketCommentMapper.toPageResponseDTO(ticketComments);

        return ResponseEntity.status(HttpStatus.OK).body(ticketCommentResponseList);
    }

    @Operation(
            summary = "Save Comment",
            description = "Salva o comentário de um chamado",
            responses = {
                    @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json"))
            }
    )
    @PostMapping
    public ResponseEntity<TicketCommentResponseDTO> saveComment(@RequestBody TicketCommentRequestDTO ticketCommentRequest){
        TicketComment ticketCommentEntity = this.ticketCommentMapper.toEntity(ticketCommentRequest);

        TicketComment createdComment = this.ticketCommentService.saveComment(ticketCommentEntity);

        TicketCommentResponseDTO ticketCommentResponse = this.ticketCommentMapper.toResponseDTO(createdComment);

        return ResponseEntity.status(HttpStatus.CREATED).body(ticketCommentResponse);
    }

    @Operation(
            summary = "Update Comment",
            description = "Atualiza o comentário de um chamado",
            responses = {
                    @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json"))
            }
    )
    @PutMapping("/{ticketCommentId}")
    public ResponseEntity<TicketCommentResponseDTO> updateComment(@PathVariable UUID ticketCommentId, @RequestBody TicketCommentUpdateDTO ticketCommentUpdate){

        TicketComment updatedComment = this.ticketCommentService.updateComment(ticketCommentId, ticketCommentUpdate);

        TicketCommentResponseDTO ticketCommentResponse = this.ticketCommentMapper.toResponseDTO(updatedComment);

        return ResponseEntity.status(HttpStatus.OK).body(ticketCommentResponse);
    }

    @Operation(
            summary = "Delete Comment",
            description = "Apaga o comentário de um chamado",
            responses = {
                    @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json"))
            }
    )
    @DeleteMapping("/{commentId}")
    public ResponseEntity deleteComment(@PathVariable UUID commentId){
        this.ticketCommentService.deleteComment(commentId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
