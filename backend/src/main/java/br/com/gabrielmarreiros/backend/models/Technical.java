package br.com.gabrielmarreiros.backend.models;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "technicians")
@PrimaryKeyJoinColumn(name = "user_id")
public class Technical extends User {

    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date dateBirth;

    public Technical() {}

    public Technical(UUID id) {
        super(id);
    }

    public Technical(String name, String email, String password, String phoneNumber, String profilePicture, Role role, String userStatus, Date dateBirth) {
        super(name, email, password, phoneNumber, profilePicture, role, userStatus);
        this.dateBirth = dateBirth;
    }

    public Date getDateBirth() {
        return dateBirth;
    }

    public void setDateBirth(Date dateBirth) {
        this.dateBirth = dateBirth;
    }

}
