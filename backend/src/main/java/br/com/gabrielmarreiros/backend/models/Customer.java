package br.com.gabrielmarreiros.backend.models;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "customers")
@PrimaryKeyJoinColumn(name = "user_id")
public class Customer extends User {
    @Column(nullable = false)
    private String cnpj;

    public Customer() {}

    public Customer(UUID id) {
        super(id);
    }

    public Customer(String name, String email, String password, String phoneNumber, String profilePicture, Role role, String userStatus, String cnpj) {
        super(name, email, password, phoneNumber, profilePicture, role, userStatus);
        this.cnpj = cnpj;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }
}
