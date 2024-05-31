package br.com.gabrielmarreiros.backend.dto.customer;

public record CustomerRegisterRequestDTO(
        String name,
        String email,
        String password,
        String cnpj,
        String phoneNumber,
        String profilePicture
) {
}
