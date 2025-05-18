package br.com.gabrielmarreiros.backend.controllers;

import br.com.gabrielmarreiros.backend.dto.technical.*;
import br.com.gabrielmarreiros.backend.exceptions.ErrorResponse;
import br.com.gabrielmarreiros.backend.mappers.TechnicalMapper;
import br.com.gabrielmarreiros.backend.models.Technical;
import br.com.gabrielmarreiros.backend.services.TechnicalService;
import br.com.gabrielmarreiros.backend.services.TokenJwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Technicians", description = "Recursos para manipulação de técnicos")
@RestController
@RequestMapping("/technicians")
public class TechnicalController {

    private final TechnicalService technicalService;
    private final TechnicalMapper technicalMapper;

    public TechnicalController(TechnicalService technicalService, TechnicalMapper technicalMapper) {
        this.technicalService = technicalService;
        this.technicalMapper = technicalMapper;
    }

    @Operation(
            summary = "Get All Technicians",
            description = "Retorna todos os técnicos registrados no sistema",
            responses = {@ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json"))}
    )
    @GetMapping
    public ResponseEntity<List<TechnicalResponseDTO>> getAllTechnicians(){
        List<Technical> techniciansList = this.technicalService.getAllTechnicians();

        List<TechnicalResponseDTO> techniciansResponseList = this.technicalMapper.toResponseListDTO(techniciansList);

        return ResponseEntity.status(HttpStatus.OK).body(techniciansResponseList);
    }

    @Operation(
            summary = "Get All Technicians Paginated",
            description = "Retorna todos os técnicos registrados no sistema de forma páginada",
            responses = {@ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json"))}
    )
    @GetMapping("/paginated")
    public ResponseEntity<Page<TechnicalResponseDTO>> getTechniciansPaginated(@RequestParam int page, @RequestParam int size){
        PageRequest pageRequest = PageRequest.of(page, size);

        Page<Technical> technicalsPage = this.technicalService.getTechniciansPaginated(pageRequest);

        Page<TechnicalResponseDTO> technicalsResponsePage = this.technicalMapper.toResponsePageDTO(technicalsPage);

        return ResponseEntity.status(HttpStatus.OK).body(technicalsResponsePage);
    }


    @Operation(
            summary = "Get Technical By ID",
            description = "Retorna as informações do técnico do ID informado",
            responses = {
                    @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json"))
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<TechnicalResponseDTO> getTechnicalById(@PathVariable("id") UUID id){
        Technical technical = this.technicalService.getTechnicalById(id);

        TechnicalResponseDTO technicalResponse = this.technicalMapper.toResponseDTO(technical);

        return ResponseEntity.status(HttpStatus.OK).body(technicalResponse);
    }
    @Operation(
            summary = "Update Technical",
            description = "Realiza atualização do técnico",
            responses = {
                    @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json"))
            }
    )
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
    public ResponseEntity<Page<TechnicalWithTicketStatusCountDTO>> getTechniciansWithTicketsStatusCountPaginated(
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String search){
        PageRequest pageRequest = PageRequest.of(page, size);

        TechnicalFiltersDTO technicalFilters = new TechnicalFiltersDTO(
                status,
                search
        );

        Page<TechnicalWithTicketStatusCountDTO> techniciansWithTicketsStatusCountPaginated = this.technicalService.getTechniciansWithTicketsStatusCountPaginated(technicalFilters, pageRequest);

        return ResponseEntity.status(HttpStatus.OK).body(techniciansWithTicketsStatusCountPaginated);
    }
    @Operation(
            summary = "Change Technical Active Status",
            description = "Realiza modificação do status do técnico informado",
            responses = {@ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json"))}
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<TechnicalResponseDTO> changeTechnicalActiveStatus(@PathVariable("id") UUID id){
        Technical technical = this.technicalService.changeTechnicalActiveStatus(id);

        TechnicalResponseDTO technicalResponseDTO = this.technicalMapper.toResponseDTO(technical);

        return ResponseEntity.status(HttpStatus.OK).body(technicalResponseDTO);
    }

}
