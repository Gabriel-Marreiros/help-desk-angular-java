package br.com.gabrielmarreiros.backend.repositories;

import br.com.gabrielmarreiros.backend.dto.technical.TechnicalWithTicketStatusCountDTO;
import br.com.gabrielmarreiros.backend.models.Technical;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TechnicalRepository extends JpaRepository<Technical, UUID> {

    @Query("""
        SELECT
            new br.com.gabrielmarreiros.backend.dto.technical.TechnicalWithTicketStatusCountDTO(
                technical.id,
                technical.name,
                technical.profilePicture,
                technical.userStatus,
                COUNT(CASE WHEN ticket.ticketStatus = 'Pendente' THEN 1 END),
                COUNT(CASE WHEN ticket.ticketStatus = 'Em Progresso' THEN 1 END),
                COUNT(CASE WHEN ticket.ticketStatus = 'Resolvido' THEN 1 END))
        FROM
            Technical technical
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
                technical.name,
                technical.profilePicture,
                technical.userStatus,
                COUNT(CASE WHEN ticket.ticketStatus = 'Pendente' THEN 1 END),
                COUNT(CASE WHEN ticket.ticketStatus = 'Em Progresso' THEN 1 END),
                COUNT(CASE WHEN ticket.ticketStatus = 'Resolvido' THEN 1 END))
        FROM
            Technical technical
        LEFT JOIN
            Ticket ticket ON ticket.technical.id = technical.id
        WHERE
            (:#{#example.probe.userStatus} IS NULL OR technical.userStatus = :#{#example.probe.userStatus})
            AND
            (:#{#example.probe.name} IS NULL OR technical.name LIKE %:#{#example.probe.name}%)
        GROUP BY
            technical.id
    """)
    Page<TechnicalWithTicketStatusCountDTO> getTechniciansWithTicketsStatusCountPaginated(@Param("example") Example<Technical> example, PageRequest pageRequest);

    @Modifying
    @Query("""
        UPDATE
            Technical t
        SET
            t.userStatus = CASE WHEN t.userStatus = 'Ativo' THEN 'Inativo' ELSE 'Ativo' END
        WHERE
            t.id = :id
    """)
    int changeTechnicalActiveStatus(UUID id);

}