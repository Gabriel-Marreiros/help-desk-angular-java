package br.com.gabrielmarreiros.backend.services;

import br.com.gabrielmarreiros.backend.models.Priority;
import br.com.gabrielmarreiros.backend.repositories.PriorityRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PriorityService {

    private final PriorityRepository priorityRepository;

    public PriorityService(PriorityRepository priorityRepository) {
        this.priorityRepository = priorityRepository;
    }

    public List<Priority> getAllPriorities(){
        return this.priorityRepository.findAll();
    }
}
