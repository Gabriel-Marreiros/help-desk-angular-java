package br.com.gabrielmarreiros.backend.exceptions;

public class TicketCommentNotFoundException extends RuntimeException{
    public TicketCommentNotFoundException() {}

    public TicketCommentNotFoundException(String message) {
        super(message);
    }

}
