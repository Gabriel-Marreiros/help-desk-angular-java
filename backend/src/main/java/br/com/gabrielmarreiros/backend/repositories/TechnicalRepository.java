package br.com.gabrielmarreiros.backend.repositories;

import br.com.gabrielmarreiros.backend.dto.technical.TechnicalWithTicketStatusCountDTO;
import br.com.gabrielmarreiros.backend.models.Technical;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TechnicalRepository extends JpaRepository<Technical, UUID> {

    @Query("""
        SELECT
            new br.com.gabrielmarreiros.backend.dto.technical.TechnicalWithTicketStatusCountDTO(
                technical.id,
                user.name,
                user.profilePicture,
                user.userStatus,
                COUNT(CASE WHEN ticket.ticketStatus = 'Pendente' THEN 1 END),
                COUNT(CASE WHEN ticket.ticketStatus = 'Em Progresso' THEN 1 END),
                COUNT(CASE WHEN ticket.ticketStatus = 'Resolvido' THEN 1 END))
        FROM
            Technical technical
        INNER JOIN
            User user ON technical.user.id = user.id
        LEFT JOIN
            Ticket ticket ON ticket.technical.id = technical.id
        GROUP BY
            technical.id
    """)
    List<TechnicalWithTicketStatusCountDTO> getAllTechnicalsWithTicketsStatusCount();

    @Query("""
        SELECT
            new br.com.gabrielmarreiros.backend.dto.technical.TechnicalWithTicketStatusCountDTO(
                technical.id,
                user.name,
                user.profilePicture,
                user.userStatus,
                COUNT(CASE WHEN ticket.ticketStatus = 'Pendente' THEN 1 END),
                COUNT(CASE WHEN ticket.ticketStatus = 'Em Progresso' THEN 1 END),
                COUNT(CASE WHEN ticket.ticketStatus = 'Resolvido' THEN 1 END))
        FROM
            Technical technical
        INNER JOIN
            User user ON technical.user.id = user.id
        LEFT JOIN
            Ticket ticket ON ticket.technical.id = technical.id
        GROUP BY
            technical.id
    """)
    Page<TechnicalWithTicketStatusCountDTO> getTechniciansWithTicketsStatusCountPaginated(PageRequest pageRequest);

    @Modifying
    @Query("""
        UPDATE
            User u
        SET
            u.userStatus = CASE WHEN u.userStatus = 'Ativo' THEN 'Inativo' ELSE 'Ativo' END
        WHERE
            u.id = :id
    """)
    int changeTechnicalActiveStatus(UUID id);
}
