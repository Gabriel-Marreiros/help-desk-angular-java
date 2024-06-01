package br.com.gabrielmarreiros.backend.models;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "priorities")
public class Priority {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(unique = true, nullable = false)
    private String title;

    public Priority() {}

    public Priority(UUID id) {
        this.id = id;
    }

    public Priority(UUID id, String title) {
        this.id = id;
        this.title = title;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id){
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String priority) {
        this.title = priority;
    }
}
