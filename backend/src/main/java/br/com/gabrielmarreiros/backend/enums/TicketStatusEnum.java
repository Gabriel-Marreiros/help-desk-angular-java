package br.com.gabrielmarreiros.backend.enums;

public enum TicketStatusEnum {
    PENDING("Pendente"),
    IN_PROGRESS("Em Progresso"),
    RESOLVED("Resolvido");

    private final String value;

    TicketStatusEnum(String value){
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
