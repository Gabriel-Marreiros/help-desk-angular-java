package br.com.gabrielmarreiros.backend.mappers;

import br.com.gabrielmarreiros.backend.dto.customer.CustomerResponseDTO;
import br.com.gabrielmarreiros.backend.dto.technical.TechnicalResponseDTO;
import br.com.gabrielmarreiros.backend.dto.ticket.TicketRequestDTO;
import br.com.gabrielmarreiros.backend.dto.ticket.TicketResponseDTO;
import br.com.gabrielmarreiros.backend.models.Customer;
import br.com.gabrielmarreiros.backend.models.Priority;
import br.com.gabrielmarreiros.backend.models.Technical;
import br.com.gabrielmarreiros.backend.models.Ticket;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TicketMapper {

    private final CustomerMapper customerMapper;
    private final TechnicalMapper technicalMapper;

    public TicketMapper(CustomerMapper customerMapper, TechnicalMapper technicalMapper) {
        this.customerMapper = customerMapper;
        this.technicalMapper = technicalMapper;
    }

    public Ticket toEntity(TicketRequestDTO ticketRequestDTO){
        Technical technical = new Technical();
        technical.setId(ticketRequestDTO.technical());

        Customer customer = new Customer();
        customer.setId(ticketRequestDTO.customer());

        Priority priority = new Priority();
        priority.setId(ticketRequestDTO.priority());

        Ticket ticketEntity = new Ticket();
        ticketEntity.setTitle(ticketRequestDTO.title());
        ticketEntity.setTechnical(technical);
        ticketEntity.setCustomer(customer);
        ticketEntity.setDescription(ticketRequestDTO.description());
        ticketEntity.setOpeningDate(ticketRequestDTO.openingDate());
        ticketEntity.setPriority(priority);

        return ticketEntity;
    }

    public List<TicketResponseDTO> toResponseListDTO(List<Ticket> ticketEntityList){
        return ticketEntityList.stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public TicketResponseDTO toResponseDTO(Ticket ticketEntity){
        CustomerResponseDTO customer = this.customerMapper.toResponseDTO(ticketEntity.getCustomer());
        TechnicalResponseDTO technical = this.technicalMapper.toResponseDTO(ticketEntity.getTechnical());

        return new TicketResponseDTO(
                ticketEntity.getId(),
                ticketEntity.getCode(),
                ticketEntity.getTitle(),
                ticketEntity.getDescription(),
                customer,
                technical,
                ticketEntity.getOpeningDate(),
                ticketEntity.getClosedDate(),
                ticketEntity.getPriority(),
                ticketEntity.getTicketStatus()
        );
    }

    public Page<TicketResponseDTO> toResponsePageDTO(Page<Ticket> ticketsEntityPage) {
        return ticketsEntityPage.map(this::toResponseDTO);
    }
}
