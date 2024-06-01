package br.com.gabrielmarreiros.backend.repositories;

import br.com.gabrielmarreiros.backend.models.Priority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PriorityRepository extends JpaRepository<Priority, UUID> {
}
