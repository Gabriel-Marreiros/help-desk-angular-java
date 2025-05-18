package br.com.gabrielmarreiros.backend.enums;

public enum TicketStatusEnum {
    NEW_TICKET("Novo Chamado"),
    PENDING("Pendente"),
    IN_PROGRESS("Em Progresso"),
    RESOLVED("Resolvido"),
    CANCELED("Cancelado");

    private final String value;

    TicketStatusEnum(String value){
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
