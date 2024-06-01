package br.com.gabrielmarreiros.backend.utils;

import br.com.gabrielmarreiros.backend.enums.TicketStatusEnum;

public class TicketUtils {

    public static boolean ticketStatusIsValid(String ticketStatus){
        for (TicketStatusEnum ticketStatusEnum : TicketStatusEnum.values()) {
            if(ticketStatusEnum.getValue().equals(ticketStatus)){
                return true;
            }
        };

        return false;
    }
}
