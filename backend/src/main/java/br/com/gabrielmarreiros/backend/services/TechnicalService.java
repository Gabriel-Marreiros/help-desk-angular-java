package br.com.gabrielmarreiros.backend.services;

import br.com.gabrielmarreiros.backend.dto.technical.TechnicalFiltersDTO;
import br.com.gabrielmarreiros.backend.dto.technical.TechnicalUpdateRequestDTO;
import br.com.gabrielmarreiros.backend.dto.technical.TechnicalWithTicketStatusCountDTO;
import br.com.gabrielmarreiros.backend.enums.RolesEnum;
import br.com.gabrielmarreiros.backend.exceptions.UnauthorizedException;
import br.com.gabrielmarreiros.backend.exceptions.UserAlreadyRegisteredException;
import br.com.gabrielmarreiros.backend.exceptions.UserNotFoundException;
import br.com.gabrielmarreiros.backend.models.Role;
import br.com.gabrielmarreiros.backend.models.Technical;
import br.com.gabrielmarreiros.backend.models.User;
import br.com.gabrielmarreiros.backend.repositories.RoleRepository;
import br.com.gabrielmarreiros.backend.repositories.TechnicalRepository;
import org.springframework.data.domain.*;
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
        Sort sort = Sort.by(Sort.Direction.ASC, "name");
        List<Technical> techniciansList = this.technicalRepository.findAll(sort);

        return techniciansList;
    }

    @Transactional
    public Technical saveTechnical(Technical technical){

        boolean userAlreadyRegistered = this.userService.verifyUserAlreadyRegistered(technical.getEmail());

        if(userAlreadyRegistered){
            throw new UserAlreadyRegisteredException();
        }

        Role technicalRole = this.roleRepository.findByTitle(RolesEnum.TECHNICAL.value()).get();

        User userEntity = technical;
        userEntity.setRole(technicalRole);

        String encodedPassword = this.passwordEncoder.encode(technical.getPassword());
        technical.setPassword(encodedPassword);

        Technical savedTechnical = this.technicalRepository.save(technical);

        return savedTechnical;
    }

    public Technical updateTechnical(UUID userId, TechnicalUpdateRequestDTO technicalUpdate){
        boolean canUpdateTechnical = this.userService.isUserHimselfOrAdmin(userId);

        if(!canUpdateTechnical){
            throw new UnauthorizedException();
        }

        Technical technical = this.technicalRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        technical.setName(technicalUpdate.name());
        technical.setEmail(technicalUpdate.email());
        technical.setPhoneNumber(technicalUpdate.phoneNumber());
        technical.setProfilePicture(technicalUpdate.profilePicture());
        technical.setDateBirth(technicalUpdate.dateBirth());

        Technical updatedTechnical = this.technicalRepository.save(technical);

        return updatedTechnical;
    }

    public Technical getTechnicalById(UUID userId) {
        Technical technical = this.technicalRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        return technical;
    }

    public List<TechnicalWithTicketStatusCountDTO> getAllTechnicalWithTicketStatusCount(){
        return this.technicalRepository.getAllTechnicalsWithTicketsStatusCount();
    }

    public Page<TechnicalWithTicketStatusCountDTO> getTechniciansWithTicketsStatusCountPaginated(TechnicalFiltersDTO technicalFilters, PageRequest pageRequest){
        Technical technicalFilter = new Technical();
        technicalFilter.setUserStatus(technicalFilters.status());
        technicalFilter.setName(technicalFilters.search());

        ExampleMatcher filterMatcher = ExampleMatcher.matching().withMatcher("name", matcher -> matcher.contains().ignoreCase());

        Example<Technical> technicalExample = Example.of(technicalFilter, filterMatcher);

        return this.technicalRepository.getTechniciansWithTicketsStatusCountPaginated(technicalExample, pageRequest);
    }

    public Page<Technical> getTechniciansPaginated(PageRequest pageRequest) {
        return this.technicalRepository.findAll(pageRequest);
    }

    @Transactional
    public Technical changeTechnicalActiveStatus(UUID userId) {
        boolean technicalExists = this.technicalRepository.existsById(userId);

        if(!technicalExists){
            throw new UserNotFoundException();
        }

        boolean canChangeActiveStatus = this.userService.isUserHimselfOrAdmin(userId);

        if(!canChangeActiveStatus){
            throw new UnauthorizedException();
        }

        this.technicalRepository.changeTechnicalActiveStatus(userId);

        Technical updatedTechnical = this.technicalRepository.findById(userId).get();

        return updatedTechnical;
    }
}
