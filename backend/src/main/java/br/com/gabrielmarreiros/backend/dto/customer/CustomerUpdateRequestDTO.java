package br.com.gabrielmarreiros.backend.dto.customer;

public record CustomerUpdateRequestDTO(
        String name,
        String cnpj,
        String email,
        String phoneNumber,
        String profilePicture
) {
}
