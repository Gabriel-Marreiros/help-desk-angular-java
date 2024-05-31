package br.com.gabrielmarreiros.backend.controllers;

import br.com.gabrielmarreiros.backend.dto.technical.*;
import br.com.gabrielmarreiros.backend.mappers.TechnicalMapper;
import br.com.gabrielmarreiros.backend.models.Technical;
import br.com.gabrielmarreiros.backend.services.TechnicalService;
import br.com.gabrielmarreiros.backend.services.TokenJwtService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/technicians")
public class TechnicalController {

    private final TechnicalService technicalService;
    private final TechnicalMapper technicalMapper;
    private final TokenJwtService tokenJwtService;

    public TechnicalController(TechnicalService technicalService, TechnicalMapper technicalMapper, TokenJwtService tokenJwtService) {
        this.technicalService = technicalService;
        this.technicalMapper = technicalMapper;
        this.tokenJwtService = tokenJwtService;
    }

    @GetMapping
    public ResponseEntity<List<TechnicalResponseDTO>> getAllTechnicians(){
        List<Technical> techniciansList = this.technicalService.getAllTechnicians();

        List<TechnicalResponseDTO> techniciansResponseList = this.technicalMapper.toResponseListDTO(techniciansList);

        return ResponseEntity.status(HttpStatus.OK).body(techniciansResponseList);
    }

    @GetMapping("/paginated")
    public ResponseEntity<Page<TechnicalResponseDTO>> getTechniciansPaginated(@RequestParam int page, @RequestParam int size){
        PageRequest pageRequest = PageRequest.of(page, size);

        Page<Technical> technicalsPage = this.technicalService.getTechniciansPaginated(pageRequest);

        Page<TechnicalResponseDTO> technicalsResponsePage = this.technicalMapper.toResponsePageDTO(technicalsPage);

        return ResponseEntity.status(HttpStatus.OK).body(technicalsResponsePage);
    }


    @GetMapping("/{id}")
    public ResponseEntity<TechnicalResponseDTO> getTechnicalById(@PathVariable("id") UUID id){
        Technical technical = this.technicalService.getTechnicalById(id);

        TechnicalResponseDTO technicalResponse = this.technicalMapper.toResponseDTO(technical);

        return ResponseEntity.status(HttpStatus.OK).body(technicalResponse);
    }

    @PostMapping
    public ResponseEntity<TechnicalRegisterResponseDTO> registerTechnical(@RequestBody TechnicalRegisterRequestDTO technicalRegisterRequest){
        Technical technicalEntity = this.technicalMapper.toEntity(technicalRegisterRequest);

        Technical registeredTechnical = this.technicalService.saveTechnical(technicalEntity);

        String token = this.tokenJwtService.generateToken(registeredTechnical.getUser());

        TechnicalRegisterResponseDTO technicalRegisterResponse = this.technicalMapper.toRegisterResponseDTO(registeredTechnical, token);

        return ResponseEntity.status(HttpStatus.CREATED).body(technicalRegisterResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TechnicalResponseDTO> updateTechnical(@PathVariable("id") UUID id, @RequestBody TechnicalUpdateRequestDTO technicalUpdate){
        Technical updatedtechnical = this.technicalService.updateTechnical(id, technicalUpdate);

        TechnicalResponseDTO updatedTechnicalResponse = this.technicalMapper.toResponseDTO(updatedtechnical);

        return ResponseEntity.status(HttpStatus.OK).body(updatedTechnicalResponse);
    }

    @GetMapping("/with-tickets-status-count")
    public ResponseEntity<List<TechnicalWithTicketStatusCountDTO>> getAllTechnicalWithTicketsStatusCount(){
        List<TechnicalWithTicketStatusCountDTO> techniciansWithTicketsStatusCount = this.technicalService.getAllTechnicalWithTicketStatusCount();
        return ResponseEntity.status(HttpStatus.OK).body(techniciansWithTicketsStatusCount);
    }

    @GetMapping("/with-tickets-status-count/paginated")
    public ResponseEntity<Page<TechnicalWithTicketStatusCountDTO>> getTechniciansWithTicketsStatusCountPaginated(@RequestParam int page, @RequestParam int size){
        PageRequest pageRequest = PageRequest.of(page, size);

        Page<TechnicalWithTicketStatusCountDTO> techniciansWithTicketsStatusCountPaginated = this.technicalService.getTechniciansWithTicketsStatusCountPaginated(pageRequest);

        return ResponseEntity.status(HttpStatus.OK).body(techniciansWithTicketsStatusCountPaginated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity inactivateUser(@PathVariable("id") UUID id){
        this.technicalService.changeTechnicalActiveStatus(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
