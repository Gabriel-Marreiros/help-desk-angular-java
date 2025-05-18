package br.com.gabrielmarreiros.backend.exceptions;

public class UnauthorizedException extends RuntimeException{

    public UnauthorizedException(){}

    public UnauthorizedException(String message){
        super(message);
    }
}
