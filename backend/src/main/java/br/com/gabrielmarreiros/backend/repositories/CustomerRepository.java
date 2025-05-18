package br.com.gabrielmarreiros.backend.repositories;

import br.com.gabrielmarreiros.backend.models.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, UUID> {

    @Modifying
    @Query("""
        UPDATE
            Customer c
        SET
            c.userStatus = CASE WHEN c.userStatus = 'Ativo' THEN 'Inativo' ELSE 'Ativo' END
        WHERE
            c.id = :id
    """)
    int changeCustomerActiveStatus(@Param("id") UUID id);

}
