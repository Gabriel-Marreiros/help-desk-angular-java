package br.com.gabrielmarreiros.backend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ExceptionsHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(InvalidLoginException.class)
    public ResponseEntity<ErrorResponse> handleInvalidLoginException(ServletWebRequest request, InvalidLoginException ex){
        String description = ex.getMessage() != null ? ex.getMessage() : "Senha ou e-mail incorreto.";

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTitle("Login inválido.");
        errorResponse.setDescription(description);
        errorResponse.setInstance(request.getRequest().getRequestURI());
        errorResponse.setStatus(HttpStatus.BAD_REQUEST);

        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(InvalidTicketStatusException.class)
    public ResponseEntity<ErrorResponse> handleInvalidTicketStatusException(ServletWebRequest request, InvalidTicketStatusException ex){
        String description = ex.getMessage() != null ? ex.getMessage() : "O status do ticket não corresponde à um status válido.";

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTitle("Status do ticket inválido.");
        errorResponse.setDescription(description);
        errorResponse.setInstance(request.getRequest().getRequestURI());
        errorResponse.setStatus(HttpStatus.BAD_REQUEST);

        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ErrorResponse> handleInvalidTokenException(ServletWebRequest request, InvalidTokenException ex){
        String description = ex.getMessage() != null ? ex.getMessage() : "O token enviado é inválido.";

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTitle("Token inválido.");
        errorResponse.setDescription(description);
        errorResponse.setInstance(request.getRequest().getRequestURI());
        errorResponse.setStatus(HttpStatus.BAD_REQUEST);

        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(TicketNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleTicketNotFoundException(ServletWebRequest request, TicketNotFoundException ex){
        String description = ex.getMessage() != null ? ex.getMessage() : "O chamado solicitado não foi encontrado.";

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTitle("Chamado não encontrado.");
        errorResponse.setDescription(description);
        errorResponse.setInstance(request.getRequest().getRequestURI());
        errorResponse.setStatus(HttpStatus.BAD_REQUEST);

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(UserAlreadyRegisteredException.class)
    public ResponseEntity<ErrorResponse> handleUserAlreadyRegisteredException(ServletWebRequest request, UserAlreadyRegisteredException ex){
        String description = ex.getMessage() != null ? ex.getMessage() : "O usuário já existe no sistema.";

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTitle("Usuário já existe.");
        errorResponse.setDescription(description);
        errorResponse.setInstance(request.getRequest().getRequestURI());
        errorResponse.setStatus(HttpStatus.BAD_REQUEST);

        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(ServletWebRequest request, UserNotFoundException ex){
        String description = ex.getMessage() != null ? ex.getMessage() : "O usuário solicitado não foi encontrado.";

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTitle("Usuário não encontrado.");
        errorResponse.setDescription(description);
        errorResponse.setInstance(request.getRequest().getRequestURI());
        errorResponse.setStatus(HttpStatus.BAD_REQUEST);

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

}
