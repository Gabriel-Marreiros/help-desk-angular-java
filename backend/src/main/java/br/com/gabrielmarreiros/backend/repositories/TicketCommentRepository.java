package br.com.gabrielmarreiros.backend.repositories;

import br.com.gabrielmarreiros.backend.models.TicketComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TicketCommentRepository extends JpaRepository<TicketComment, UUID> {

    Page<TicketComment> findByTicketIdOrderByCreatedAtDesc(UUID ticketId, Pageable pageable);
}
