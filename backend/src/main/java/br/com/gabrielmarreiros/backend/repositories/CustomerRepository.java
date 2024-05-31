package br.com.gabrielmarreiros.backend.repositories;

import br.com.gabrielmarreiros.backend.models.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, UUID> {

    @Modifying
    @Query("""
        UPDATE
            User u
        SET
            u.userStatus = CASE WHEN u.userStatus = 'Ativo' THEN 'Inativo' ELSE 'Ativo' END
        WHERE
            u.id = :id
    """)
    int changeCustomerActiveStatus(@Param("id") UUID id);
}
