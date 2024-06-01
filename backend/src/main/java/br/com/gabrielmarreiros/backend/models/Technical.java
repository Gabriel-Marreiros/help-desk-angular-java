package br.com.gabrielmarreiros.backend.models;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "technicians")
public class Technical {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date dateBirth;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Technical() {}

    public Technical(UUID id) {
        this.id = id;
    }

    public Technical(Date dateBirth, User user) {
        this.dateBirth = dateBirth;
        this.user = user;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Date getDateBirth() {
        return dateBirth;
    }

    public void setDateBirth(Date dateBirth) {
        this.dateBirth = dateBirth;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
