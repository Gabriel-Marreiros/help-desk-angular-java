package br.com.gabrielmarreiros.backend.exceptions;

public class InvalidTicketStatusException extends RuntimeException {
    public InvalidTicketStatusException() {}

    public InvalidTicketStatusException(String message) {
        super(message);
    }
}
