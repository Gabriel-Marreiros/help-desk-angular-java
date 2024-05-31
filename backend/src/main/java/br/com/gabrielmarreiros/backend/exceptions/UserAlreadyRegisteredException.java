package br.com.gabrielmarreiros.backend.exceptions;

public class UserAlreadyRegisteredException extends RuntimeException{
    public UserAlreadyRegisteredException(){}

    public UserAlreadyRegisteredException(String message) {
        super(message);
    }
}
