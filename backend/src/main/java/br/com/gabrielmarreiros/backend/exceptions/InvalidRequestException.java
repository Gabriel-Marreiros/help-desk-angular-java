package br.com.gabrielmarreiros.backend.exceptions;

public class InvalidRequestException extends RuntimeException{

    public InvalidRequestException() {}

    public InvalidRequestException(String message) {
        super(message);
    }
}
