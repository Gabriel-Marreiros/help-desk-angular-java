package br.com.gabrielmarreiros.backend.utils;

import br.com.gabrielmarreiros.backend.enums.TicketStatusEnum;
import br.com.gabrielmarreiros.backend.exceptions.InvalidTicketStatusException;

public class TicketUtils {

    public static boolean ticketStatusIsValid(String ticketStatus){
        for (TicketStatusEnum ticketStatusEnum : TicketStatusEnum.values()) {
            if(ticketStatusEnum.getValue().equals(ticketStatus)){
                return true;
            }
        };

        return false;
    }

    static public TicketStatusEnum getTicketStatusEnumFromString(String ticketStatus) {
        if(ticketStatus == null){
            return null;
        }

        for (TicketStatusEnum ticketStatusEnum : TicketStatusEnum.values()) {
            if(ticketStatusEnum.getValue().equals(ticketStatus)){
                return ticketStatusEnum;
            }
        };

        throw new InvalidTicketStatusException();
    }
}
