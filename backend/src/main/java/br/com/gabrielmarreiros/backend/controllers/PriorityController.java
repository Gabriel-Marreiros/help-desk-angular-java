package br.com.gabrielmarreiros.backend.controllers;

import br.com.gabrielmarreiros.backend.models.Priority;
import br.com.gabrielmarreiros.backend.services.PriorityService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/priorities")
public class PriorityController {

    private final PriorityService priorityService;

    public PriorityController(PriorityService priorityService) {
        this.priorityService = priorityService;
    }

    @GetMapping
    public ResponseEntity<List<Priority>> getAllPriorities(){
        List<Priority> priorities = this.priorityService.getAllPriorities();
        return ResponseEntity.status(HttpStatus.OK).body(priorities);
    }
}
