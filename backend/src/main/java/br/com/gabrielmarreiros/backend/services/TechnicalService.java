package br.com.gabrielmarreiros.backend.services;

import br.com.gabrielmarreiros.backend.dto.technical.TechnicalUpdateRequestDTO;
import br.com.gabrielmarreiros.backend.dto.technical.TechnicalWithTicketStatusCountDTO;
import br.com.gabrielmarreiros.backend.enums.RolesEnum;
import br.com.gabrielmarreiros.backend.exceptions.UserAlreadyRegisteredException;
import br.com.gabrielmarreiros.backend.exceptions.UserNotFoundException;
import br.com.gabrielmarreiros.backend.models.Role;
import br.com.gabrielmarreiros.backend.models.Technical;
import br.com.gabrielmarreiros.backend.models.User;
import br.com.gabrielmarreiros.backend.repositories.RoleRepository;
import br.com.gabrielmarreiros.backend.repositories.TechnicalRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class TechnicalService {

    private final UserService userService;
    private final TechnicalRepository technicalRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public TechnicalService(TechnicalRepository technicalRepository, UserService userService, RoleRepository roleRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.technicalRepository = technicalRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Technical> getAllTechnicians(){
        List<Technical> techniciansList = this.technicalRepository.findAll();
        return techniciansList;
    }

    @Transactional
    public Technical saveTechnical(Technical technical){

        boolean userAlreadyRegistered = this.userService.verifyUserAlreadyRegistered(technical.getUser().getEmail());

        if(userAlreadyRegistered){
            throw new UserAlreadyRegisteredException();
        }

        Role technicalRole = this.roleRepository.findByTitle(RolesEnum.TECHNICAL.getValue()).get();

        User userEntity = technical.getUser();
        userEntity.setRole(technicalRole);

        String encodedPassword = this.passwordEncoder.encode(technical.getUser().getPassword());
        technical.getUser().setPassword(encodedPassword);

        User savedUser = this.userService.saveUser(userEntity);

        technical.setUser(savedUser);

        Technical savedTechnicalEntity = this.technicalRepository.save(technical);

        return savedTechnicalEntity;
    }

    public Technical updateTechnical(UUID id, TechnicalUpdateRequestDTO technicalUpdate){
        Technical technical = this.technicalRepository.findById(id).orElseThrow(UserNotFoundException::new);

        technical.getUser().setName(technicalUpdate.name());
        technical.getUser().setEmail(technicalUpdate.email());
        technical.getUser().setPhoneNumber(technicalUpdate.phoneNumber());
        technical.getUser().setProfilePicture(technicalUpdate.profilePicture());
        technical.setDateBirth(technicalUpdate.dateBirth());

        Technical updatedTechnical = this.technicalRepository.save(technical);

        return updatedTechnical;
    }

    public Technical getTechnicalById(UUID id) {
        return this.technicalRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);

    }

    public List<TechnicalWithTicketStatusCountDTO> getAllTechnicalWithTicketStatusCount(){
        return this.technicalRepository.getAllTechnicalsWithTicketsStatusCount();
    }

    public Page<TechnicalWithTicketStatusCountDTO> getTechniciansWithTicketsStatusCountPaginated(PageRequest pageRequest){
        return this.technicalRepository.getTechniciansWithTicketsStatusCountPaginated(pageRequest);
    }

    public Page<Technical> getTechniciansPaginated(PageRequest pageRequest) {
        return this.technicalRepository.findAll(pageRequest);
    }

    @Transactional
    public void changeTechnicalActiveStatus(UUID id) {
        int rows = this.technicalRepository.changeTechnicalActiveStatus(id);
    }
}
