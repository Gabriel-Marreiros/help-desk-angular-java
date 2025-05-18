package br.com.gabrielmarreiros.backend.repositories;

import br.com.gabrielmarreiros.backend.dto.ticket.AllTicketsStatusSummaryDTO;
import br.com.gabrielmarreiros.backend.enums.TicketStatusEnum;
import br.com.gabrielmarreiros.backend.models.Ticket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, UUID> {

    @Modifying()
    @Query("""
        UPDATE
            Ticket t
        SET
            t.ticketStatus = :newStatus
        WHERE
            t.id = :id
    """)
    int updateTicketStatus(@Param("id") UUID id, @Param("newStatus") String newStatus);

    @Query("""
        SELECT
            new br.com.gabrielmarreiros.backend.dto.ticket.AllTicketsStatusSummaryDTO(
                COUNT(CASE WHEN ticket.ticketStatus = 'Pendente' THEN 1 END),
                COUNT(CASE WHEN ticket.ticketStatus = 'Em Progresso' THEN 1 END),
                COUNT(CASE WHEN ticket.ticketStatus = 'Resolvido' THEN 1 END)
            )
        FROM
            Ticket ticket
    """)
    AllTicketsStatusSummaryDTO getAllTicketsStatusSummary();

}
