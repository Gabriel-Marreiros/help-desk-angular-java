package br.com.gabrielmarreiros.backend.exceptions;

public class TicketNotFoundException extends RuntimeException{
    public TicketNotFoundException() {}

    public TicketNotFoundException(String message) {
        super(message);
    }

}
