package br.com.gabrielmarreiros.backend.exceptions;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException() {}

    public UserNotFoundException(String message){
        super(message);
    }
}
