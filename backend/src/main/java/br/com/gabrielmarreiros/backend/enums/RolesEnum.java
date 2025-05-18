package br.com.gabrielmarreiros.backend.enums;

public enum RolesEnum {
    ADMIN("Administrador"),
    CUSTOMER("Cliente"),
    TECHNICAL("Técnico");

    private final String value;

    RolesEnum(String value){
        this.value = value;
    }

    public String value(){
        return this.value;
    }

}
