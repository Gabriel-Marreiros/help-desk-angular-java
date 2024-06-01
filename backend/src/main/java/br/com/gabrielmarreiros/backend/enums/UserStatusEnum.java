package br.com.gabrielmarreiros.backend.enums;

public enum UserStatusEnum {
    ACTIVE("Ativo"),
    INACTIVE("Inativo");

    private final String value;

    UserStatusEnum(String value){
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
