package br.com.gabrielmarreiros.backend.dto.login;

public record LoginRequestDTO(
    String email,
    String password
){}
