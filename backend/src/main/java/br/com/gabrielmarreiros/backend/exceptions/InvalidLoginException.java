package br.com.gabrielmarreiros.backend.exceptions;

public class InvalidLoginException extends RuntimeException{

    public InvalidLoginException() {}

    public InvalidLoginException(String message) {
        super(message);
    }
}
