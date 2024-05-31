package br.com.gabrielmarreiros.backend.models;

import jakarta.persistence.*;

import java.util.Date;
import java.util.Random;
import java.util.StringJoiner;
import java.util.UUID;

@Entity
@Table(name = "tickets")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String code;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date openingDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date closedDate;

    @ManyToOne
    @JoinColumn(name = "priority_id", nullable = false)
    private Priority priority;

    @Column(nullable = false)
    private String ticketStatus;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "technical_id", nullable = false)
    private Technical technical;

    private String searchTerm;

    public Ticket() {}

    public Ticket(String code, String title, String description, Date openingDate, Date closedDate, Priority priority, String ticketStatus, Customer customer, Technical technical) {
        this.code = code;
        this.title = title;
        this.description = description;
        this.openingDate = openingDate;
        this.closedDate = closedDate;
        this.priority = priority;
        this.ticketStatus = ticketStatus;
        this.customer = customer;
        this.technical = technical;
    }

    public void generateSearchTerm(){
        var stringJoiner =  new StringJoiner(" ");

        stringJoiner.add(this.code)
                .add(this.title)
                .add(this.description)
                .add(this.technical.getUser().getName())
                .add(this.customer.getUser().getName());

        this.searchTerm = stringJoiner.toString();
    }

    public void generateTicketCode(){
        this.code = String.valueOf(new Random().nextInt(1000));
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getOpeningDate() {
        return openingDate;
    }

    public void setOpeningDate(Date openingDate) {
        this.openingDate = openingDate;
    }

    public Date getClosedDate() {
        return closedDate;
    }

    public void setClosedDate(Date closedDate) {
        this.closedDate = closedDate;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public String getTicketStatus() {
        return ticketStatus;
    }

    public void setTicketStatus(String ticketStatus) {
        this.ticketStatus = ticketStatus;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }


    public Technical getTechnical() {
        return technical;
    }

    public void setTechnical(Technical technical) {
        this.technical = technical;
    }

    public String getSearchTerm() {
        return searchTerm;
    }

    public void setSearchTerm(String searchTerm) {
        this.searchTerm = searchTerm;
    }
}
