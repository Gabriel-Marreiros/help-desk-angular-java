package br.com.gabrielmarreiros.backend.controllers;

import br.com.gabrielmarreiros.backend.models.Priority;
import br.com.gabrielmarreiros.backend.services.PriorityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Priorities", description = "Recursos para manipulação de prioridades")
@RestController
@RequestMapping("/priorities")
public class PriorityController {

    private final PriorityService priorityService;

    public PriorityController(PriorityService priorityService) {
        this.priorityService = priorityService;
    }

    @Operation(
            summary = "Get All Priorities",
            description = "Retorna uma lista com todas as opções de prioridades do sistema",
            responses = {@ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json"))}
    )
    @GetMapping
    public ResponseEntity<List<Priority>> getAllPriorities(){
        List<Priority> priorities = this.priorityService.getAllPriorities();
        return ResponseEntity.status(HttpStatus.OK).body(priorities);
    }
}
